package com.ideatech.ams.risk.service.impl;

import com.ideatech.ams.risk.account.dao.AccountAllDataDao;
import com.ideatech.ams.risk.account.entity.AccountAllData;
import com.ideatech.ams.risk.dao.JnnsRiskCompareAccountDao;
import com.ideatech.ams.risk.riskdata.dao.RiskCheckInfoDao;
import com.ideatech.ams.risk.service.JnnsRiskCompareAcctFileService;
import com.ideatech.ams.risk.thread.JnnsCompareAcctFileProcessThread;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.utils.DateUtils;
import com.ideatech.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class JnnsRiskCompareAcctFileServiceImpl implements JnnsRiskCompareAcctFileService {
    /**
     * 编码集
     */
    @Value("${ams.jnbc.duiZhang.charset:UTF-8}")
    private String charset;

    /**
     * 文件格式
     */
    @Value("${ams.jnbc.duiZhang.fileFormat:.txt}")
    private String fileFormat;


    /**
     * 取回SZSModsFTP文件的本地存放地址
     */
    @Value("${ams.jnbc.duiZhang.filePath:/ams/jnbc/risk/yqdz}")
    private String filePath;

    /**
     * 访问SZSModsFTP的取回文件名1
     */
    @Value("${ams.jnbc.duiZhang.fileName1:RCS_AC_BALANCE_CHECK_OUT_}")
    private String fileName1;

    /**
     * 访问SZSModsFTP的取回文件名2
     */
    @Value("${ams.jnbc.duiZhang.fileName2:RCS_AC_PRINT_MAIN_}")
    private String fileName2;

    /**
     * 访问SZSModsFTP的取回文件名3
     */
    @Value("${ams.jnbc.duiZhang.fileName3:RCS_AC_ACCOUNT_NETBANK_HISTORY_}")
    private String fileName3;
    /**
     * 取回SZSModsFTP文件的本地存放地址
     */
    @Value("${ams.szsm.ods.fileFinshPath:D:/home/weblogic/coreFile/risk}")
    private String fileFinshPath;

    @Autowired
    private JnnsRiskCompareAccountDao yinQiDuiZhangRepository;

    @Autowired
    private OrganizationDao organizationDao;


    @Autowired
    private AccountAllDataDao accountAllDataDao;

    @Autowired
    private RiskCheckInfoDao riskCheckInfoDao;

    @Autowired
    private JnnsRiskCompareAccountDao jnnsRiskCompareAccountDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    public void getFile() {
        //获取前一天的数据
        String date = DateUtils.DateToStr(DateUtil.subDays(new Date(), 1), "yyyyMMdd");

        File file1 = new File(filePath + File.separator + fileName1 + date + fileFormat);
        File file2 = new File(filePath + File.separator + fileName2 + date + fileFormat);
        File file3 = new File(filePath + File.separator + fileName3 + date + fileFormat);

        //判断文件是否存在
        if (file1.exists() && file2.exists() && file3.exists()) {
            log.info("清空数据库相关表");
            //先清空数据
            TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus transaction = transactionManager.getTransaction(definition);
            riskCheckInfoDao.deleteAll();
            jnnsRiskCompareAccountDao.deleteAll();
            transactionManager.commit(transaction);
            log.info("清空数据库相关表完成");
            //处理文件 RCS_AC_BALANCE_CHECK_OUT_
            try {
                extData(date, fileName1, filePath);
                //处理完成后 转移文件到另一个文件夹
                moveFile(fileName1, date, file1);
            } catch (Exception e) {
                log.error("获取文件【" + fileName1 + "】报错", e);
            }
            //处理文件 RCS_AC_PRINT_MAIN_
            try {
                extData(date, fileName2, filePath);
                moveFile(fileName2, date, file2);
            } catch (Exception e) {
                log.error("获取文件【" + fileName1 + "】报错", e);
            }
            //处理文件 RCS_AC_ACCOUNT_NETBANK_HISTORY_
            try {
                extData(date, fileName3, filePath);
                moveFile(fileName3, date, file3);
            } catch (Exception e) {
                log.error("获取文件【" + fileName1 + "】报错", e);
            }
        } else {
            log.info("处理对账时，3个文件不存在...等待下次处理");
        }
    }

    private void moveFile(String fileName, String date, File file) throws IOException {
        File finshFile = new File(fileFinshPath + "/" + fileName + date + fileFormat);
        FileUtils.moveFileToDirectory(file, new File(fileFinshPath), true);
        if (finshFile.exists()) {
            finshFile.delete();
        }
    }

    /**
     * @Description 拆分大文件 執行綫程
     * @author yangwz
     * @date 2020/8/6 16:19
     */
    public boolean extData(String date, String filename, String allPath) {
        BufferedReader bf = null;
        BufferedWriter bw = null;
        int fileNum = 1;
        log.info("***************开始拆分文件***************");
        File file = new File(allPath + File.separator + filename + date + fileFormat);
        try {
            //本地路径
            File allFilePaths = new File(allPath);
            if (!allFilePaths.exists()) {
                allFilePaths.mkdirs();
            }
            if (!file.exists()) {
                log.info("处理【" + filename + "】文件名为【" + file.getName() + "】时，文件不存在！");
                return false;
            }
            bf = new BufferedReader(new InputStreamReader(new FileInputStream(allPath + File.separator + filename + date + fileFormat), charset));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(allPath + File.separator + "data" + filename + date + "_" + fileNum + fileFormat), charset));
            int i = 0;
            int first = 1;
            String str = null;
            while ((str = bf.readLine()) != null) {
                //标记第一行数据去掉
                if (first != 1) {
                    //每十万行拆分一个文件
                    //行数
                    if (i == 100000) {
                        bw.flush();
                        i = 0;
                        fileNum++;
                        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(allPath + File.separator + "data" + filename + date + "_" + fileNum + fileFormat), charset));
                    }
                    if (i != 0) {
                        bw.newLine();
                    }
                    bw.write(str);
                    i++;
                } else {
                    first = 0;
                }
            }
            bw.flush();
        } catch (Exception e) {
            log.error("处理文件文件【" + file.getName() + "】切分文件时异常", e);
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("***************拆分文件完成***************");
        log.info("共拆分" + fileNum + "个文件");
        //多线程执行文件
        return executeThread(filename, allPath);
    }

    /**
     * @Description 多线程
     * @author yangwz
     * @date 2020/8/6 16:20
     */
    public boolean executeThread(String filename, String allPath) {

        String filepath = allPath + "/";
        File file = new File(filepath);
        //读取目录下所有文件
        String[] filelist = file.list();
        List<String> fList = new ArrayList<String>();
        for (int i = 0; i < filelist.length; i++) {
            if (filelist[i].startsWith("data") && filelist[i].contains(filename) && filelist[i].endsWith(fileFormat)) {
                fList.add(filelist[i]);
            }
        }
        Map<String, String> organCodeMap =getOrganMap();
        //获取全量账户是信息
        Map<String, String> accountDataMap = new HashMap<>();
        if (filename.equals(fileName2)) {
            List<AccountAllData> accountAllDataList = accountAllDataDao.findAll();
            if (CollectionUtils.isNotEmpty(accountAllDataList)) {
                for (AccountAllData accountAllData : accountAllDataList) {
                    accountDataMap.put(accountAllData.getAcctNo(), accountAllData.getWorkAddress());
                }
            }
            accountAllDataList.clear();
        }
        log.info("***************开始导入银企对账数据***************");
        long startTime = System.currentTimeMillis();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 15,
                5000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(fList.size() + 10));
        //根据文件数进行循环，多线程批量删除数据
        for (int i = 0; i < fList.size(); i++) {
            JnnsCompareAcctFileProcessThread fileThread = new JnnsCompareAcctFileProcessThread(i, fList, yinQiDuiZhangRepository, organizationDao, riskCheckInfoDao,
                    filepath, filename, fileName1, fileName2, fileName3, accountAllDataDao, transactionManager, accountDataMap,organCodeMap);
            threadPoolExecutor.execute(fileThread);
            try {
                fileThread.join();
                if (fileThread.getJudge() == 1) {
                    return false;
                }
                log.info(" " + fileThread.getJudge());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        boolean ifFlag = true;
        while (ifFlag) {
            try {
                Thread.sleep(10000L);
                if (threadPoolExecutor.getActiveCount() == 0) {
                    ifFlag = false;
                } else {
                    log.info("--------插数银企对账跑批线程还未结束---------------总共" + fList.size() + "个线程----已完成" + (threadPoolExecutor.getCompletedTaskCount()) + "个线程");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //关闭线程池
        threadPoolExecutor.shutdown();
        long entTime = System.currentTimeMillis();
        log.info("使用时间" + (entTime - startTime) / 1000 + "秒,完成企业对账取数插入表表中过程。");
        return true;
    }

    /**
     * 获取机构表的organCode与fullId对应关系
     *
     * @return
     */
    private Map<String, String> getOrganMap() {
        Map<String, String> organMap = new HashMap<String, String>();
        List<OrganizationPo> organizationPoList = organizationDao.findAll();
        if (CollectionUtils.isNotEmpty(organizationPoList)) {
            for (OrganizationPo organizationPo : organizationPoList) {
                organMap.put(organizationPo.getCode(), organizationPo.getFullId());
            }
        }
        return organMap;
    }
}
