package com.ideatech.ams.risk.thread;


import com.ideatech.ams.risk.account.dao.AccountAllDataDao;
import com.ideatech.ams.risk.account.entity.AccountAllData;
import com.ideatech.common.util.FileUtils;
import com.ideatech.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class JnnsOpenAcctRiskProeccssFileThread extends Thread {
    private String fileName3;
    private String fileName5;
    private String filepath;
    private int fileIndex;
    private int judge;
    private String className;
    private List<String> filelist;

    private Map<String, String> regAreaCodeMap;

    private AccountAllDataDao accountAllDataDao;

    private Map<String, List<AccountAllData>> accountDataMap;

    private Map<String, String> organCodeMap;

    public int getJudge() {
        return judge;
    }

    public void setJudge(int judge) {
        this.judge = judge;
    }

    public JnnsOpenAcctRiskProeccssFileThread(int fileIndex, List<String> filelist, String filepath, String className, String fileName3, String fileName5, AccountAllDataDao accountAllDataDao, Map<String, String> regAreaCodeMap, Map<String, List<AccountAllData>> accountDataMap, Map<String, String> organCodeMap) {
        this.fileIndex = fileIndex;
        this.filelist = filelist;
        this.accountAllDataDao = accountAllDataDao;
        this.filepath = filepath;
        this.className = className;
        this.fileName3 = fileName3;
        this.fileName5 = fileName5;
        this.regAreaCodeMap = regAreaCodeMap;
        this.accountDataMap = accountDataMap;
        this.organCodeMap = organCodeMap;
    }

    @Override
    public void run() {
        for (int i = 0; i < filelist.size(); i++) {
            if (i == fileIndex) {
                //读取文件
                try {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(filepath + File.separator + filelist.get(i)), "UTF-8"));
                    String line;
                    String[] tlqFile;
                    List<String[]> list = new ArrayList<>();
                    File file = new File(filepath + File.separator + filelist.get(i));
                    List<String> allList = org.apache.commons.io.FileUtils.readLines(file, "UTF-8");
                    int totalNum = allList.size();
                    // 一次读入一行，直到读入null为文件结束
                    int processNum = 0;
                    while ((line = bf.readLine()) != null) {
                        processNum++;
                        tlqFile = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, "\t<f>\t");
                        tlqFile[tlqFile.length - 1] = tlqFile[tlqFile.length - 1].replaceAll("<#r>", "");
                        list.add(tlqFile);
                        //保存实体类数据
                        if (list.size() >= 1000) {
                            if (className.equals(fileName3)) {
                                saveClientCorpData(list);
                            } else if (className.equals(fileName5)) {
                                saveAccountAllData(list);
                            }
                        }
                        if (processNum % 300 == 0) {
                            log.info("文件【" + file.getName() + "】共" + totalNum + "条账户，总共已经处理成功数量：" + processNum);
                        }
                    }
                    if (list.size() > 0) {
                        if (className.equals(fileName3)) {
                            saveClientCorpData(list);
                        }
                        if (className.equals(fileName5)) {
                            saveAccountAllData(list);
                        }
                    }
                    log.info("文件【" + file.getName() + "】处理完成");
                    bf.close();
                    judge = 0;
                    FileUtils.deleteFile(filepath + File.separator + filelist.get(fileIndex));
                } catch (Exception e) {
                    log.error("处理开户风险切割文件【" + filelist.get(fileIndex) + "】异常", e);
                    judge = 1;
                }
            }

        }
        // accountDataMap.clear();
    }

    /**
     * @Description 保存 YD_INTF_AMS_CLIENT_CORP數據
     * @author yangwz
     * @date 2020/8/6 16:10
     */
    @Transactional
    public void saveAccountAllData(List<String[]> resultList) {
        List<AccountAllData> accountAllDataList = new ArrayList<AccountAllData>();
        try {
            AccountAllData accountAllData;
            for (String[] str : resultList) {
                accountAllData = new AccountAllData();
                accountAllData.setAcctNo((str[0] == null) ? "" : str[0]);
                accountAllData.setAcctType((str[1] == null) ? "" : str[1]);
                accountAllData.setAcctName((str[2] == null) ? "" : str[2]);
                accountAllData.setAccountStatus((str[3] == null) ? "" : str[3]);
                accountAllData.setAcctCreateDate((str[4] == null) ? "" : str[4]);
                accountAllData.setEffectiveDate((str[5] == null) ? "" : str[5]);
                accountAllData.setAccountKey((str[6] == null) ? "" : str[6]);
                accountAllData.setOrganCode((str[7] == null) ? "" : str[7]);
                accountAllData.setOrganFullId(organCodeMap.get(accountAllData.getOrganCode()));
                accountAllData.setBankName((str[8] == null) ? "" : str[8]);
                accountAllData.setCustomerNo((str[9] == null) ? "" : str[9]);
                accountAllData.setDepositorName((str[10] == null) ? "" : str[10]);
                //判断是否有工商到期日期
                accountAllData.setDepositorType((str[11] == null) ? "" : str[11]);
                accountAllData.setRegProvince((str[12] == null) ? "" : str[12]);
                accountAllData.setRegCity((str[13] == null) ? "" : str[13]);
                accountAllData.setRegArea((str[14] == null) ? "" : str[14]);
                accountAllData.setRegAddress((str[15] == null) ? "" : str[15]);
                accountAllData.setIndustryCode((str[16] == null) ? "" : str[16]);
                accountAllData.setRegOffice((str[17] == null) ? "" : str[17]);
                accountAllData.setFileType((str[18] == null) ? "" : str[18]);
                accountAllData.setFileNo((str[19] == null) ? "" : str[19]);
                accountAllData.setFileSetUpdate((str[20] == null) ? "" : str[20]);
                accountAllData.setFileDue((str[21] == null) ? "" : str[21]);
                accountAllData.setRegCurrencyType((str[22] == null) ? "" : str[22]);
                accountAllData.setRegisterEdcapital((str[23] == null) ? "" : str[23]);
                accountAllData.setBusinessScope((str[24] == null) ? "" : str[24]);
                accountAllData.setCorpScale((str[25] == null) ? "" : str[25]);
                accountAllData.setLegalType((str[26] == null) ? "" : str[26]);
                accountAllData.setLegalName((str[27] == null) ? "" : str[27]);
                accountAllData.setLegalIdcardType((str[28] == null) ? "" : str[28]);
                accountAllData.setLegalIdcardNo((str[29] == null) ? "" : str[29]);
                accountAllData.setLegalIdcardDue((str[30] == null) ? "" : str[30]);
                accountAllData.setLegalTelephone((str[31] == null) ? "" : str[31]);
                accountAllData.setOrgCode((str[32] == null) ? "" : str[32]);
                accountAllData.setOrgCodeDue((str[33] == null) ? "" : str[33]);
                accountAllData.setStateTaxregNo((str[34] == null) ? "" : str[34]);
                accountAllData.setStateTaxDue((str[35] == null) ? "" : str[35]);
                accountAllData.setTaxRegNo((str[36] == null) ? "" : str[36]);
                accountAllData.setTaxDue((str[37] == null) ? "" : str[37]);
                accountAllData.setWorkAddress((str[38] == null) ? "" : str[38]);
                accountAllData.setTelephone((str[39] == null) ? "" : str[39]);
                accountAllData.setZipCode((str[40] == null) ? "" : str[40]);
                accountAllData.setEconomyType((str[41] == null) ? "" : str[41]);
                accountAllDataList.add(accountAllData);
            }
            accountAllDataDao.save(accountAllDataList);
            resultList.clear();
        } catch (Exception e) {
            log.error("保存【AccountAllData】表异常", e);
            judge = 1;
        }
    }

    @Transactional
    public void saveClientCorpData(List<String[]> resultList) {
        List<AccountAllData> accountAllDataList = new ArrayList<>();
        List<AccountAllData> list;
        String strAreaCode;
        try {
            int processNum = 0;
            for (String[] str : resultList) {
                processNum++;
                if (StringUtils.isNotBlank(str[1])) {
                    list = accountDataMap.get(str[1]);
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (AccountAllData accountAllData : list) {
                            if (accountAllData != null && StringUtils.isNotBlank(accountAllData.getId().toString())) {
                                //判断注册地区代码是否一致，不一致用核心字段代替。
                                if (StringUtils.isNotBlank(str[8])) {
                                    strAreaCode = str[8];
                                    //A003341126,截取 A003后面6位
                                    if (strAreaCode.length() > 4) {
                                        strAreaCode = strAreaCode.substring(4);
                                        //根据省市区获取人行注册地地区代码
                                        strAreaCode = regAreaCodeMap.get(strAreaCode);
                                    }
                                    if (StringUtils.isBlank(accountAllData.getRegAreaCode())) {
                                        accountAllData.setRegAreaCode(strAreaCode);
                                    } else {
                                        if (!strAreaCode.equals(accountAllData.getRegAreaCode())) {
                                            accountAllData.setRegAreaCode(strAreaCode);
                                        }
                                    }
                                }
                                //判断注册详细地址是否一致，不一致用核心字段代替。
                                if (StringUtils.isNotBlank(str[7])) {
                                    if (StringUtils.isBlank(accountAllData.getRegAddress())) {
                                        accountAllData.setRegAddress(str[7]);
                                    } else {
                                        if (!str[7].equals(accountAllData.getRegAddress())) {
                                            accountAllData.setRegAddress(str[7]);
                                        }
                                    }
                                }
                                //判断办公地区代码是否一致，不一致用核心字段代替。
                                if (StringUtils.isNotBlank(str[13])) {
                                    strAreaCode = str[13];
                                    //A003341126,截取 A003后面6位
                                    if (strAreaCode.length() > 4) {
                                        strAreaCode = strAreaCode.substring(4);
                                        //根据省市区获取人行注册地地区代码
                                        strAreaCode = regAreaCodeMap.get(strAreaCode);
                                    }
                                    if (StringUtils.isBlank(accountAllData.getWorkArea())) {
                                        accountAllData.setWorkArea(strAreaCode);
                                    } else {
                                        if (!strAreaCode.equals(accountAllData.getWorkArea())) {
                                            accountAllData.setWorkArea(strAreaCode);
                                        }
                                    }
                                }
                                //判断办公详细地址是否一致，不一致用核心字段代替。
                                if (StringUtils.isNotBlank(str[14])) {
                                    if (StringUtils.isBlank(accountAllData.getWorkAddress())) {
                                        accountAllData.setWorkAddress(str[14]);
                                    } else {
                                        if (!str[14].equals(accountAllData.getWorkAddress())) {
                                            accountAllData.setWorkAddress(str[14]);
                                        }
                                    }
                                }
                                accountAllDataList.add(accountAllData);
                            }
                        }
                    }
                }
                if (processNum % 100 == 0) {
                    log.info("已经处理数量：" + processNum);
                }
            }
            accountAllDataDao.save(accountAllDataList);
            resultList.clear();
        } catch (Exception e) {
            log.error("更新【AccountAllData】表异常", e);
            judge = 1;
        }
    }
}
