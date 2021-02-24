package com.ideatech.ams.imageCore2Ams.excutor;


import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.dao.JnnsImageAllDao;
import com.ideatech.ams.domain.JnnsImageAll;
import com.ideatech.ams.service.JnnsImageService;
import com.ideatech.common.utils.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 影像存表处理类
 *
 *
 */
@Slf4j
@Data
public class CoreImageFileIntoExcutor {


    private ConcurrentHashMap<String, List<JnnsImageAll>> concurrentHashMap;

    private JnnsImageService jnnsImageService;

    private AccountBillsAllService accountBillsAllService;

    private JnnsImageAllDao jnnsImageAllDao;

    private Integer flag;

    private Integer flagAllBills;

    private Integer flagImageBatchGetSuccess;

    /**
     * 存量影像信息保存进表
     */
    public void saveImageInfoInit() {


        ConcurrentHashMap.KeySetView<String, List<JnnsImageAll>> strings = concurrentHashMap.keySet();

        flag = 0;
        flagAllBills = 0;
        flagImageBatchGetSuccess=0;
        int index = 0;
        for (String string : strings) {
            if (index % 2000 == 1) {
                log.info(Thread.currentThread().getName()+"已处理" + index + "条影像信息！还需处理"+strings.size()+"条影像信息！");
            }
            //处理一条数据移除一条
            // List<JnnsImageAll> remove =concurrentHashMap.get(string);
            List<JnnsImageAll> remove = concurrentHashMap.remove(string);
            intoTable2ImageInfo(string, remove);
            index++;

        }

        flag = null;
        flagAllBills = null;
    }

    /**
     * 根据账号进行影像信息进表操作，先要通过影像流水查过来影像批次号
     * 一条流水对应一条批次号，业务逻辑一一对应关系
     * @param acctNo
     * @param list
     */
    public void intoTable2ImageInfo(String acctNo, List<JnnsImageAll> list) {
        //模糊查询，避免存量流水账号有交换号而匹配不到
        List<AccountBillsAllInfo> byAcctNo = accountBillsAllService.findByAcctNoLike(acctNo);

        if (CollectionUtils.isEmpty(byAcctNo)){
            if (acctNo.length() > 4){
              String  noExtrAcctNo  =  acctNo.substring(4);
               // 如果模糊查询不到存量流水信息，使用准确匹配查询，参数账号去除前3，除交换号查询
                byAcctNo =  accountBillsAllService.findByAcctNo(noExtrAcctNo);
            }
        }

        //log.info(""+byAcctNo);
        if (CollectionUtils.isNotEmpty(byAcctNo)) {
            if (flagAllBills % 2000 == 1) {
                log.info("------"+Thread.currentThread().getName()+"已查出匹配流水集" + flagAllBills + "个！------");
            }
            for (AccountBillsAllInfo accountBillsAllInfo : byAcctNo) {

              boolean check  =  StringUtils.equals(acctNo,accountBillsAllInfo.getAcctNo());

              if (!check){
                  String  billAcctNo = accountBillsAllInfo.getAcctNo();
                  if (billAcctNo.length() > 4 && billAcctNo.length() > acctNo.length() ){
                      billAcctNo  =  billAcctNo.substring(4);
                  }else if (acctNo.length() > 4 && acctNo.length() > billAcctNo.length()){
                      acctNo = acctNo.substring(4);
                  }
                  check =  StringUtils.equals(acctNo,billAcctNo);
              }

                if (accountBillsAllInfo.getBillType() == BillType.ACCT_INIT  && check) {

                    if (flag % 3000 == 1) {
                        log.info("----"+Thread.currentThread().getName()+"已查出匹配流水" + flag + "条！-----");
                    }
                    flag++;
                    List<JnnsImageAll> imageAllList = jnnsImageAllDao.findByBillId(accountBillsAllInfo.getId() + "");

                    for (int i = 0; i < list.size(); i++) {

                        //支持存量重复导导入
                        JnnsImageAll jnnsImageAll = list.get(i);
                        System.out.println("匹配数据+++++++++++++++"+list.get(i));


                        //影像存量导入时的账号由于交换号的原因与存量账户的数据不一致时，影像表中以存量账号为准
                        jnnsImageAll.setAcctNo(accountBillsAllInfo.getAcctNo());
                        //System.out.println(imageAllList);

                     String batchAndDateImageBySerialNum = getBatchAndBusiDate2ImageBySerialNum(jnnsImageAll.getContentCode());
                        System.out.println("流水号================="+jnnsImageAll.getContentCode());

                     String[] split = null;

                     if (StringUtils.isNotEmpty(batchAndDateImageBySerialNum)){
                         log.info("batchAndDateImageBySerialNum==========="+batchAndDateImageBySerialNum);
                          split =  batchAndDateImageBySerialNum.split(",");
                          if (split == null) batchAndDateImageBySerialNum.split("，");
                      }

                      //需要将批次号设置进去
                      if (split != null && split.length >1){
                          log.info("spit================"+split);
                          System.out.println(split);
                          flagImageBatchGetSuccess++;
                          if (flagImageBatchGetSuccess % 100==0)  log.info(""+flagImageBatchGetSuccess+"条影像批次号！");
                          jnnsImageAll.setImageCode(split[1]);
                          jnnsImageAll.setBusiStartDate(split[0]);
                          System.out.println(split[0]);
                      }

                        if (imageAllList != null) {
                            for (JnnsImageAll jnnsImage : imageAllList) {
                                String flag = jnnsImage.getInitTypeFlag();
                                if (jnnsImage.getBillType() == BillType.ACCT_INIT && flag != null && flag.equals(jnnsImageAll.getInitTypeFlag()) && StringUtils.equals(jnnsImage.getContentCode(),jnnsImageAll.getContentCode())) {
                                    //  jnnsImageAll.setId(jnnsImage.getId());
                                    jnnsImageAll = jnnsImage;
                                    //  System.out.println(jnnsImage.getId());
                                }
                            }
                        }
                        jnnsImageAll.setBillId(accountBillsAllInfo.getId() + "");
                        jnnsImageAll.setCustomerNo(accountBillsAllInfo.getCustomerNo());
                        jnnsImageAll.setAcctType(accountBillsAllInfo.getAcctType());
                        jnnsImageAll.setOrganFullId(accountBillsAllInfo.getOrganFullId());
                        jnnsImageAll.setBillType(BillType.ACCT_INIT);
                        if (split != null && split.length >1){
                            jnnsImageAll.setImageCode(split[1]);
                            jnnsImageAll.setBusiStartDate(split[0]);
                        }

                        jnnsImageAllDao.save(jnnsImageAll);


                    }

                }

            }

        }

    }
        /**
         * 通过流水号去影像平台获取该影像批次号
         * @param serialNum
         * @return
         */
        public String getBatchAndBusiDate2ImageBySerialNum (String serialNum){


            String batchNumAndBusDate = jnnsImageService.heightQueryExamplSupper(serialNum);

            return batchNumAndBusDate;
        }



}