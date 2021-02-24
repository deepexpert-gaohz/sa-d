package com.ideatech.ams.risk.service.impl;

import com.ideatech.ams.risk.account.dao.AccountAllDataDao;
import com.ideatech.ams.risk.account.entity.AccountAllData;
import com.ideatech.ams.risk.service.JnnsOpenAcctRiskFileService;
import com.ideatech.ams.risk.thread.JnnsOpenAcctRiskProeccssFileThread;
import com.ideatech.ams.system.area.dto.AreaDto;
import com.ideatech.ams.system.area.service.AreaService;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by xls on 2019/10/18.
 * 获取ODS推送的T+1文件
 */
@Service
public class JnnsOpenAcctRiskFileServiceImpl implements JnnsOpenAcctRiskFileService {
    private static final Logger log = LoggerFactory.getLogger(JnnsOpenAcctRiskFileServiceImpl.class);

    /**
     * 文件格式
     */
    @Value("${ams.szsm.ods.fileFormat:.dat}")
    private String fileFormat;

    /**
     * 分隔符
     */
    @Value("${ams.szsm.ods.symbol:1}")
    private String symbol;

    /**
     * 编码集
     */
    @Value("${ams.szsm.ods.charset:UTF-8}")
    private String charset;

    /**
     * 取回SZSModsFTP文件的本地存放地址
     */
    @Value("${ams.szsm.ods.filePath:D:/home/weblogic/coreFile/risk}")
    private String filePath;

    /**
     * 取回SZSModsFTP文件的本地存放地址
     */
    @Value("${ams.szsm.ods.fileFinshPath:D:/home/weblogic/coreFile/risk}")
    private String fileFinshPath;

    /**
     * 访问SZSModsFTP的取回文件名3
     */
    @Value("${ams.szsm.ods.fileName3:ZG_ENT_INFO_Z}")
    private String fileName3;

    /**
     * 访问SZSModsFTP的取回文件名5
     */
    @Value("${ams.szsm.ods.fileName5}")
    private String fileName5;

    @Autowired
    private AccountAllDataDao accountAllDataDao;

    @Autowired
    AreaService areaService;

    @Autowired
    OrganizationDao organizationDao;

    @Override
    public void pullOdsFile(String date) {
        try {
            log.info("*****************************开始处理" + fileName5 + "的ODS数据*********************************");

            File file = new File(filePath + "/" + fileName5 + "_" + date + fileFormat);
            File finshFile = new File(fileFinshPath + "/" + fileName5 + "_" + date + fileFormat);
            if (!file.exists()) {
                log.info("文件TFS_CUST_CPRO_ACCT_INFO【" + fileName5 + "】处理不存在");
            } else {
                //处理文件
                extData(date, fileName5);
                log.info("文件TFS_CUST_CPRO_ACCT_INFO【" + fileName5 + "】处理成功");
                //处理完成后 转移文件到另一个文件夹
                FileUtils.moveFileToDirectory(file, new File(fileFinshPath), true);
                if (finshFile.exists()) {
                    finshFile.delete();
                }
            }
            log.info("*****************************开始处理" + fileName3 + "的ODS数据*********************************");
            file = new File(filePath + "/" + fileName3 + "_" + date + fileFormat);
            finshFile = new File(fileFinshPath + "/" + fileName3 + "_" + date + fileFormat);
            if (!file.exists()) {
                log.info("文件UDM_INTF_AMS_CLIENT_CORP【" + fileName3 + "】处理不存在");
            } else {
                //处理文件
                extData(date, fileName3);
                log.info("文件UDM_INTF_AMS_CLIENT_CORP【" + fileName3 + "】处理成功");
                //处理完成后 转移文件到另一个文件夹
                FileUtils.moveFileToDirectory(file, new File(fileFinshPath), true);
                if (finshFile.exists()) {
                    finshFile.delete();
                }
            }
        } catch (Exception e) {
            log.error("文件处理异常", e);
        }
    }


    /**
     * @Description 多线程1111
     * @author yangwz
     * @date 2020/8/6 16:20
     */
    public boolean executeThread(String filename) {
        Map<String, String> regAreaCodeMap = new HashMap<>();
        Map<String, List<AccountAllData>> accountDataMap = new HashMap<>();
        File file = new File(filePath);
        //读取目录下所有文件
        String[] filelist = file.list();
        List<String> fList = new ArrayList<String>();
        for (int i = 0; i < filelist.length; i++) {
            if (filelist[i].startsWith("data") && filelist[i].contains(filename) && filelist[i].endsWith(fileFormat)) {
                fList.add(filelist[i]);
            }
        }

        Map<String, String> organCodeMap =getOrganMap();
        if (fileName5.equals(filename)) {
            long start = System.currentTimeMillis();
            log.info("***************开始刪除AllAccountData数据库数据");
            this.deleteBatchData(accountAllDataDao);
            long end = System.currentTimeMillis();
            log.info("***************AllAccountData数据库数据刪除完成,总共耗时--" + (end - start) / 1000 + "秒");
        } else if (fileName3.equals(filename)) {
            //获取省市区与人行注册地地区代码对应关系
            regAreaCodeMap = getAreaMap();
            //客户号 与账户集合
            List<AccountAllData> accountAllDataList = accountAllDataDao.findAll();
            if (CollectionUtils.isNotEmpty(accountAllDataList)) {
                List<AccountAllData> accountAllDataListByCustomerNo;
                for (AccountAllData accountAllData : accountAllDataList) {
                    if (accountDataMap.get(accountAllData.getCustomerNo()) != null) {
                        accountAllDataListByCustomerNo = accountDataMap.get(accountAllData.getCustomerNo());
                        accountAllDataListByCustomerNo.add(accountAllData);
                    } else {
                        accountAllDataListByCustomerNo = new ArrayList<>();
                        accountAllDataListByCustomerNo.add(accountAllData);
                    }
                    accountDataMap.put(accountAllData.getCustomerNo(), accountAllDataListByCustomerNo);
                }
                accountAllDataList.clear();
            }
        }
        log.info("***************开始导入AllAccountData数据");
        long startTime = System.currentTimeMillis();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 15,
                5000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(fList.size() + 10));
        //根据文件数进行循环，多线程批量删除数据
        JnnsOpenAcctRiskProeccssFileThread odsFileThread;
        for (int i = 0; i < fList.size(); i++) {
            odsFileThread = new JnnsOpenAcctRiskProeccssFileThread(i, fList, filePath, filename, fileName3, fileName5, accountAllDataDao, regAreaCodeMap, accountDataMap,organCodeMap);
            threadPoolExecutor.execute(odsFileThread);
            try {
                odsFileThread.join();
                if (odsFileThread.getJudge() == 1) {
                    return false;
                }
                log.info("" + odsFileThread.getJudge());
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
                    log.info("--------插数跑批线程还未结束---------------总共" + fList.size() + "个线程----已完成" + (threadPoolExecutor.getCompletedTaskCount()) + "个线程");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //关闭线程池
        threadPoolExecutor.shutdown();
        long entTime = System.currentTimeMillis();
        log.info("使用时间" + (entTime - startTime) / 1000 + "秒,完成从ODS文件取数插入表表中过程。");
        return true;
    }


    /**
     * 获取省市区与注册地区代码关系
     *
     * @return
     */
    private Map<String, String> getAreaMap() {
        Map<String, String> areaMap = new HashMap<>();
        List<AreaDto> areaDtoList = areaService.findAll();
        if (CollectionUtils.isNotEmpty(areaDtoList)) {
            log.info("====================area表共：" + areaDtoList.size());
            for (AreaDto areaDto : areaDtoList) {
                areaMap.put(areaDto.getAreaCode(), areaDto.getRegCode());
            }
        }
        return areaMap;
    }

    /**
     * 批量删除
     *
     * @param t
     */
    private void deleteBatchData(Object t) {
        int count = 0;
        if (t instanceof AccountAllDataDao) {
            count = (int) accountAllDataDao.count();
        }
        int nums = (int) count / 50000 + 1;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 15, 5000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(nums + 10));
        for (int i = 0; i < nums; i++) {
            ThreadDeleteTask threadDeleteTask = new ThreadDeleteTask(t);
            threadPoolExecutor.execute(threadDeleteTask);
        }
        boolean ifFlag = true;
        while (ifFlag) {
            try {
                Thread.sleep(10000L);
                if (threadPoolExecutor.getActiveCount() == 0) {
                    ifFlag = false;
                } else {
                    log.info("--------删除线程还未结束---------------总共" + threadPoolExecutor.getTaskCount() + "个线程----已完成" + (threadPoolExecutor.getCompletedTaskCount()) + "个线程");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        threadPoolExecutor.shutdown();
    }

    public class ThreadDeleteTask implements Runnable {
        private Object t1;

        public ThreadDeleteTask(Object t) {
            this.t1 = t;
        }

        @Override
        public void run() {
            if (t1 instanceof AccountAllDataDao) {
                accountAllDataDao.deleteByCount(50000);
            }
        }
    }

    /**
     * @Description 拆分大文件 執行綫程
     * @author yangwz
     * @date 2020/8/6 16:19
     */
    public boolean extData(String date, String filename) {
        BufferedReader bf = null;
        BufferedWriter bw = null;
        int fileNum = 1;
        log.info("***************开始拆分文件");
        try {
            bf = new BufferedReader(new InputStreamReader(new FileInputStream(filePath + "/" + filename + "_" + date + fileFormat), charset));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath + "/data" + filename + "_" + date + "_" + fileNum + fileFormat), charset));
            int i = 0;
            int first = 0;
            String str = null;
            while ((str = bf.readLine()) != null) {
                //标记第一行数据去掉
                if (first != 1) {
                    //每十万行拆分一个文件
                    //行数
                    if (i == 30000) {
                        bw.flush();
                        i = 0;
                        fileNum++;
                        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath + "/data" + filename + "_" + date + "_" + fileNum + fileFormat), charset));
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
            log.error("处理文件文件【" + filePath + "/" + filename + "_" + date + fileFormat + "】切分文件时异常", e);
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
        return executeThread(filename);
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
