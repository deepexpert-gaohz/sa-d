package com.ideatech.ams.risk.thread;


import com.ideatech.ams.risk.account.dao.AccountAllDataDao;
import com.ideatech.ams.risk.dao.JnnsRiskCompareAccountDao;
import com.ideatech.ams.risk.domain.JnnsRiskCompareAccount;
import com.ideatech.ams.risk.riskdata.dao.RiskCheckInfoDao;
import com.ideatech.ams.risk.riskdata.entity.RiskCheckInfo;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.common.util.DateUtil;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.FileUtils;
import com.ideatech.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 多线程读取多个文件
 */
@Slf4j
public class JnnsCompareAcctFileProcessThread extends Thread {
    /**
     * 分隔符
     */
    private String fileName1;
    private String fileName3;
    private String fileName2;
    private String filepath;
    private int fileIndex;
    private int judge;
    private String className;
    private JnnsRiskCompareAccountDao compareAccountDao;
    private OrganizationDao organizationDao;

    private RiskCheckInfoDao riskCheckInfoDao;
    private AccountAllDataDao accountAllDataDao;
    private List<String> filelist;

    private PlatformTransactionManager transactionManager;

    private Map<String, String> accountDataMap;

    private Map<String, String> organCodeMap;

    public int getJudge() {
        return judge;
    }

    public void setJudge(int judge) {
        this.judge = judge;
    }

    public JnnsCompareAcctFileProcessThread(int fileIndex, List<String> filelist, JnnsRiskCompareAccountDao yinQiDuiZhangRepository, OrganizationDao organizationDao,
                                            RiskCheckInfoDao riskCheckInfoDao, String filepath,
                                            String className, String fileName1, String fileName2, String fileName3, AccountAllDataDao accountAllDataDao,
                                            PlatformTransactionManager transactionManager, Map<String, String> accountDataMap, Map<String, String> organCodeMap) {
        this.fileIndex = fileIndex;
        this.filelist = filelist;
        this.compareAccountDao = yinQiDuiZhangRepository;
        this.organizationDao = organizationDao;
        this.riskCheckInfoDao = riskCheckInfoDao;
        this.accountAllDataDao = accountAllDataDao;
        this.filepath = filepath;
        this.className = className;
        this.fileName1 = fileName1;
        this.fileName2 = fileName2;
        this.fileName3 = fileName3;
        this.transactionManager = transactionManager;
        this.accountDataMap = accountDataMap;
        this.organCodeMap = organCodeMap;
    }


    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void run() {
        TransactionDefinition definition = new DefaultTransactionDefinition(
                TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        for (int i = 0; i < filelist.size(); i++) {
            if (i == fileIndex) {
                //读取文件
                File readfile = new File(filepath + filelist.get(i));
                InputStreamReader isr;
                try {
                    isr = new InputStreamReader(new FileInputStream(readfile), "UTF-8");
                    BufferedReader reader = new BufferedReader(isr);
                    String line;
                    String[] tlqFile;
                    List<String[]> list = new ArrayList<String[]>();
                    List<String> allList = org.apache.commons.io.FileUtils.readLines(readfile, "UTF-8");
                    // 一次读入一行，直到读入null为文件结束
                    int processNum = 0;
                    while ((line = reader.readLine()) != null) {
                        tlqFile = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, "\t<f>\t");
                        list.add(tlqFile);
                        processNum++;
                        //保存实体类数据
                        if (list.size() >= 1000) {
                            if (className.equals(fileName1)) {
                                saveBalanceCheckData(list, formatter.format(date));
                            } else if (className.equals(fileName3)) {
                                saveAccountNetbankData(list, formatter.format(date));
                            } else if (className.equals(fileName2)) {
                                savePrintMainData(list, formatter.format(date));
                            }
                        }
                        if (processNum % 300 == 0) {
                            log.info("文件【" + readfile.getName() + "】共" + allList.size() + "条账户，总共已经处理成功数量：" + processNum);
                            transactionManager.commit(transaction);
                            transaction = transactionManager.getTransaction(definition);
                        }
                    }
                    if (list.size() > 0) {
                        if (className.equals(fileName1)) {
                            saveBalanceCheckData(list, formatter.format(date));
                        } else if (className.equals(fileName2)) {
                            savePrintMainData(list, formatter.format(date));
                        } else if (className.equals(fileName3)) {
                            saveAccountNetbankData(list, formatter.format(date));
                        }
                    }
                    reader.close();
                    isr.close();
                    judge = 0;
                    transactionManager.commit(transaction);
                    FileUtils.deleteFile(filepath + File.separator + filelist.get(fileIndex));
                } catch (Exception e) {
                    transactionManager.rollback(transaction);
                    log.error("处理账户对账切割文件【" + filelist.get(fileIndex) + "】异常", e);
                    judge = 1;
                }
            }
        }
    }


    /**
     * @author liuz
     * @date 2019-9-19
     * 配置yd_risk_record_info表中插入风险数据
     */
    public RiskCheckInfo insetRecordInfo(String riskId, String iDate) {
        RiskCheckInfo riskCheckInfo = new RiskCheckInfo();
        riskCheckInfo.setRiskId(riskId);
        riskCheckInfo.setDeleted(false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            riskCheckInfo.setRiskDate(sdf.parse(iDate));
        } catch (ParseException e) {
            log.error("银企对账时时间插入异常", e);
        }
        riskCheckInfo.setStatus("0");
        return riskCheckInfo;
    }

    public static void main(String[] args) {
        try {
            String nowMonth = "202002";
            String beforeThreeMonth = DateUtils.DateToStr(DateUtil.subMonths(DateUtils.parseDate(nowMonth), 3), "yyyyMM");
            System.out.println(beforeThreeMonth);
            System.out.println("202007".compareTo("202008"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void saveBalanceCheckData(List<String[]> list, String iDate) {
        try {
            //当前年月
            String nowMonth = DateUtils.getNowDateShort("yyyyMM");
            //前三个月时间
            String beforeFirsitThreeMonth = DateUtils.DateToStr(DateUtil.subMonths(DateUtils.parseDate(nowMonth), 1), "yyyyMM");
            String beforeSecondThreeMonth = DateUtils.DateToStr(DateUtil.subMonths(DateUtils.parseDate(nowMonth), 2), "yyyyMM");
            String beforeThreeThreeMonth = DateUtils.DateToStr(DateUtil.subMonths(DateUtils.parseDate(nowMonth), 3), "yyyyMM");
            //上个月未对账账户
            Map<String, JnnsRiskCompareAccount> firstMonthNotReachAcctMap = new HashMap<>();
            //上上个月未对账账户
            Map<String, JnnsRiskCompareAccount> secoendMonthNotReachAcctMap = new HashMap<>();
            //上上上个月所有账户
            Map<String, JnnsRiskCompareAccount> threeMonthAllAcctMap = new HashMap<>();
            //遍历分割文件中的数据
            if (CollectionUtils.isNotEmpty(list)) {
                JnnsRiskCompareAccount jnnsRiskCompareAccount;
                //对账标识 0 未对账
                String reachType;
                //账户对账日期
                String accountMonth;
                //账户开户机构号
                String organCode;
                for (String[] acctLine : list) {
                    accountMonth = acctLine[6].substring(0, 6);
                    organCode = acctLine[32];
                    //只取最近3个月的数据
                    reachType = acctLine[18];
                    if (beforeThreeThreeMonth.compareTo(accountMonth) <= 0) {
                        //保存基础信息
                        jnnsRiskCompareAccount = saveCompareAccountData(accountMonth, reachType, acctLine[7], acctLine[30], "1", organCode);
                        //梳理未对账账户
                        if (reachType.equals("0")) {
                            if (beforeFirsitThreeMonth.equals(accountMonth)) {
                                firstMonthNotReachAcctMap.put(acctLine[7], jnnsRiskCompareAccount);
                            } else if (beforeSecondThreeMonth.equals(accountMonth)) {
                                secoendMonthNotReachAcctMap.put(acctLine[7], jnnsRiskCompareAccount);
                            }
                        }
                        threeMonthAllAcctMap.put(acctLine[7], jnnsRiskCompareAccount);
                    }
                }
                // 使用第1个月的账户与2、3月份比对，连续2个月都未对账，则需要预警
                saveRiskCheckData(iDate, firstMonthNotReachAcctMap, secoendMonthNotReachAcctMap, threeMonthAllAcctMap, "1");
                list.clear();
            }
        } catch (Exception e) {
            log.error("保存并处理BalanceCheck文件生成对账预警信息时异常", e);
            judge = 1;
        }
    }

    /**
     * @Description
     * @author xls
     * @date 2020/9/9 10:10
     */
    public void saveAccountNetbankData(List<String[]> list, String iDate) {
        try {
            //当前年月
            String nowMonth = DateUtils.getNowDateShort("yyyyMM");
            //前三个月时间
            String beforeFirsitThreeMonth = DateUtils.DateToStr(DateUtil.subMonths(DateUtils.parseDate(nowMonth), 1), "yyyyMM");
            String beforeSecondThreeMonth = DateUtils.DateToStr(DateUtil.subMonths(DateUtils.parseDate(nowMonth), 2), "yyyyMM");
            String beforeThreeThreeMonth = DateUtils.DateToStr(DateUtil.subMonths(DateUtils.parseDate(nowMonth), 3), "yyyyMM");
            //上个月未对账账户
            Map<String, JnnsRiskCompareAccount> firstMonthNotReachAcctMap = new HashMap<>();
            //上上个月未对账账户
            Map<String, JnnsRiskCompareAccount> secoendMonthNotReachAcctMap = new HashMap<>();
            //上上上个月所有账户
            Map<String, JnnsRiskCompareAccount> threeMonthAllAcctMap = new HashMap<>();
            //遍历分割文件中的数据
            if (CollectionUtils.isNotEmpty(list)) {
                JnnsRiskCompareAccount jnnsRiskCompareAccount;
                //对账标识 0 未对账
                String reachType;
                //账户对账日期
                String accountMonth;
                //账户开户机构号
                String organCode;
                for (String[] acctLine : list) {
                    accountMonth = acctLine[0].substring(0, 6);
                    //只取最近3个月的数据
                    if (beforeThreeThreeMonth.compareTo(accountMonth) <= 0) {
                        reachType = acctLine[10];
                        organCode = acctLine[9];
                        //保存基础信息
                        jnnsRiskCompareAccount = saveCompareAccountData(accountMonth, reachType, acctLine[2], acctLine[1], "2", organCode);
                        //梳理未对账账户
                        if (reachType.equals("0")) {
                            if (beforeFirsitThreeMonth.equals(accountMonth)) {
                                firstMonthNotReachAcctMap.put(acctLine[2], jnnsRiskCompareAccount);
                            } else if (beforeSecondThreeMonth.equals(accountMonth)) {
                                secoendMonthNotReachAcctMap.put(acctLine[2], jnnsRiskCompareAccount);
                            }
                        }
                        threeMonthAllAcctMap.put(acctLine[2], jnnsRiskCompareAccount);
                    }
                }
                // 使用第1个月的账户与2、3月份比对，连续2个月都未对账，则需要预警
                saveRiskCheckData(iDate, firstMonthNotReachAcctMap, secoendMonthNotReachAcctMap, threeMonthAllAcctMap, "2");
                list.clear();
            }
        } catch (Exception e) {
            log.error("保存并处理AccountNetbank文件生成对账预警信息时异常", e);
            judge = 1;
        }
    }

    /**
     * 保存对账基础信息
     *
     * @param accountMonth
     * @param reachType
     * @param acctNo
     * @param customerNo
     * @return
     */
    private JnnsRiskCompareAccount saveCompareAccountData(String accountMonth, String reachType, String acctNo, String customerNo, String sourceType, String organCode) {
        JnnsRiskCompareAccount jnnsRiskCompareAccount = new JnnsRiskCompareAccount();
        jnnsRiskCompareAccount.setRiskId("RISK_4001");
        jnnsRiskCompareAccount.setAcctNo(acctNo);
        jnnsRiskCompareAccount.setReachType(reachType);
        jnnsRiskCompareAccount.setCompareMonth(accountMonth);
        jnnsRiskCompareAccount.setCustomerNo(customerNo);
        jnnsRiskCompareAccount.setSourceType(sourceType);
        jnnsRiskCompareAccount.setOrganFullId(organCodeMap.get(organCode));
        jnnsRiskCompareAccount.setOrganCode(organCode);
        compareAccountDao.save(jnnsRiskCompareAccount);

        return jnnsRiskCompareAccount;
    }


    /**
     * 保存对账风险监测结果信息
     *
     * @param iDate
     * @param firstMonthNotReachAcctMap
     * @param secoendMonthNotReachAcctMap
     * @param threeMonthAllAcctMap
     */
    private void saveRiskCheckData(String iDate, Map<String, JnnsRiskCompareAccount> firstMonthNotReachAcctMap,
                                   Map<String, JnnsRiskCompareAccount> secoendMonthNotReachAcctMap,
                                   Map<String, JnnsRiskCompareAccount> threeMonthAllAcctMap, String sourceType) {
        // 使用第1个月的账户与2、3月份比对，连续2个月都未对账，则需要预警
        Set<String> threeAccts = threeMonthAllAcctMap.keySet();
        if (threeAccts != null && threeAccts.size() > 0) {
            RiskCheckInfo riskCheckInfo;
            for (String threeAcct : threeAccts) {
                if (secoendMonthNotReachAcctMap.get(threeAcct) != null && firstMonthNotReachAcctMap.get(threeAcct) != null) {
                    riskCheckInfo = this.insetRecordInfo("RISK_4001", iDate);
                    riskCheckInfo.setRiskPoint(threeMonthAllAcctMap.get(threeAcct).getAcctNo());
                    riskCheckInfo.setAccountNo(threeMonthAllAcctMap.get(threeAcct).getAcctNo());
                    riskCheckInfo.setSourceType(sourceType);
                    riskCheckInfo.setRiskDesc("超过两次对账周期未对账");
                    riskCheckInfo.setCustomerId(threeMonthAllAcctMap.get(threeAcct).getCustomerNo());
                    riskCheckInfo.setOrganCode(threeMonthAllAcctMap.get(threeAcct).getOrganCode());
                    riskCheckInfo.setOrganFullId(organCodeMap.get(riskCheckInfo.getOrganCode()));
                    riskCheckInfoDao.save(riskCheckInfo);
                }
            }
        }
    }

    /**
     * @Description
     * @author xls
     * @date 2020/9/9 10:10
     */
    @Transactional
    public void savePrintMainData(List<String[]> list, String iDate) {
        List<JnnsRiskCompareAccount> yinQiDuiZhangInfoList = new ArrayList<>();
        try {
            //当前年月
            String nowMonth = DateUtils.getNowDateShort("yyyyMM");
            //前一个个月时间
            String beforeFirsitThreeMonth = DateUtils.DateToStr(DateUtil.subMonths(DateUtils.parseDate(nowMonth), 1), "yyyyMM");
            JnnsRiskCompareAccount jnnsRiskCompareAccount;
            //账户对账日期
            String accountMonth;
            for (String[] s : list) {
                accountMonth = s[2].substring(0, 6);
                jnnsRiskCompareAccount = new JnnsRiskCompareAccount();
                if (StringUtils.isNotBlank(s[7]) && StringUtils.isNotBlank(s[12])) {
                    if ("01".equals(s[12]) || "02".equals(s[12]) || "03".equals(s[12]) || "04".equals(s[12])) {
                        if (getAddress(jnnsRiskCompareAccount, s[7], s[10]) && (beforeFirsitThreeMonth.compareTo(DateUtils.DateToStr(DateUtils.parseDate(s[2]), "yyyyMM"))) <= 0) {
                            jnnsRiskCompareAccount.setAcctNo(s[7]);
                            jnnsRiskCompareAccount.setRiskId("RISK_4002");
                            jnnsRiskCompareAccount.setSourceType("3");
                            jnnsRiskCompareAccount.setOrganCode(s[22]);
                            jnnsRiskCompareAccount.setOrganFullId(organCodeMap.get(s[22]));
                            jnnsRiskCompareAccount.setCompareMonth(accountMonth);
                            yinQiDuiZhangInfoList.add(jnnsRiskCompareAccount);
                            RiskCheckInfo riskCheckInfo = this.insetRecordInfo("RISK_4002", iDate);
                            riskCheckInfo.setAccountNo(s[7]);
                            riskCheckInfo.setRiskPoint("经营地址：" + jnnsRiskCompareAccount.getWorkAddress() + "\n 对账地址：" + jnnsRiskCompareAccount.getDzAddress());
                            riskCheckInfo.setRiskDesc("对账地址与经营地址不符.");
                            riskCheckInfo.setCustomerId((s[19] == null) ? "" : s[19]);
                            riskCheckInfo.setOrganCode(s[22]);
                            riskCheckInfo.setOrganFullId(organCodeMap.get(s[22]));
                            riskCheckInfoDao.save(riskCheckInfo);
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(yinQiDuiZhangInfoList)) {
                    compareAccountDao.save(yinQiDuiZhangInfoList);
                }
            }
            list.clear();
        } catch (Exception e) {
            log.error("保存并处理PrintMainData文件生成对账预警信息时异常", e);
            judge = 1;
        }
    }

    private boolean getAddress(JnnsRiskCompareAccount jnnsRiskCompareAccount, String acctNo, String dzAddress) {
        String wordAddress = accountDataMap.get(acctNo);
        if (StringUtils.isEmpty(wordAddress)) {
            return false;
        }
        if (StringUtils.isNotBlank(dzAddress) && !wordAddress.equals(dzAddress)) {
            jnnsRiskCompareAccount.setDzAddress(dzAddress);
            jnnsRiskCompareAccount.setWorkAddress(wordAddress);
            return true;
        }
        return false;
    }


}