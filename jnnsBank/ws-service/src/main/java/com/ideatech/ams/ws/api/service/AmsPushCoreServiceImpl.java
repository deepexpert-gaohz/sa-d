package com.ideatech.ams.ws.api.service;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.PbcSyncListDto;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.service.AccountPublicService;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.PbcSyncListService;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.customer.dto.CustomerPublicInfo;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.dto.CustomerPublicMidInfo;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.customer.service.CustomerPublicMidService;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.customer.service.CustomersAllService;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.enums.AccountStatus;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class AmsPushCoreServiceImpl implements AmsPushCoreService {


    @Autowired
    private AmsMainService amsMainService;

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private AmsHzPushService amsHzPushService;

    @Autowired
    private PbcSyncListService pbcSyncListService;

    @Autowired
    private PbcAmsService pbcAmsService;

    @Autowired
    private CustomerPublicService customerPublicService;

    @Autowired
    private CustomersAllService customersAllService;

    @Autowired
    private CustomerPublicLogService customerPublicLogService;

    @Autowired
    private CustomerPublicMidService customerPublicMidService;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private AccountPublicService accountPublicService;

    @Autowired
    private TransactionUtils transactionUtils;

    @Autowired
    private AccountsAllService accountsAllService;

    @Value("${ams.schedule.pushCore.file:/home/weblogic/idea/apprott/pushCoreFilePath}")
    private String pushCoreFilePath;

    @Value("${ams.schedule.pushCancelHeZhunCore.file:/home/weblogic/idea/apprott/pushCancelHeZhunCoreFilePath}")
    private String pushCancelHeZhunCoreFilePath;

    /**
     * 1.同时存在两个核准号：非临时、预算
     * 2.只有账户核准号：临时、特殊
     * 3.只有基本户核准号：基本、一般、非预算
     */
    @Override
    public void checkAndPushCore() {
        //取到人行未推送的同步列表
        List<PbcSyncListDto> pbcSyncList = pbcSyncListService.getUnPushSyncList();

        //拿到一个批次人行已核准的账户数据(批量)
        List<PbcSyncListDto> writeTxtList = new ArrayList<>(16);

        //循环list并根据账号去人行查询是否已核准
        for (final PbcSyncListDto pbcSyncListDto : pbcSyncList) {

            //取消核准的推送数据跳过
            if(pbcSyncListDto.getCancelHeZhun() != null && pbcSyncListDto.getCancelHeZhun()){
                continue;
            }

            //拿到账号
            String acctNo = pbcSyncListDto.getAcctNo();

            AmsAccountInfo amsAccountInfo = null;

            //所有都需要推送
            boolean needPush = true;
            String amsAccountInfoJson = "";
            //如果人行未查询则去人行查询
            try {
                //拿到人行登录对象
                PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullId(pbcSyncListDto.getOrganFullId(), EAccountType.AMS);
                if (pbcAccountDto == null) {
                    log.error("该机构未配置人行用户名密码");
                    continue;
                    //                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "未配置人行用户名密码");
                }
                //查询人行返回对象
                amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(pbcAccountDto, acctNo);
                //不为空是保存核准号并同步状态改为yes
                if (amsAccountInfo != null && amsAccountInfo.getAccountStatus() == AccountStatus.normal) {

                    log.info("人行返回josn{}", JSON.toJSONString(amsAccountInfo));
                    amsAccountInfoJson = JSON.toJSONString(amsAccountInfo);

                    String accountKey = amsAccountInfo.getAccountKey();
                    String acctAccountKey = amsAccountInfo.getAccountLicenseNo();
                    log.info("人行返回核准号{}", accountKey);
                    log.info("人行返回的账户核准号{}", acctAccountKey);

                    if (pbcSyncListDto.getBillType() == BillType.ACCT_CHANGE) {
                        //如果是变更， 核准号修改后才推送
                        if (amsAccountInfo.getAcctType() == AccountType.jiben) {
                            if (StringUtils.isBlank(pbcSyncListDto.getAccountKey()) || StringUtils.equals(pbcSyncListDto.getAccountKey(), accountKey)) {
                                needPush = false;
                            }
                        } else {
                            if (StringUtils.isBlank(pbcSyncListDto.getAcctAccountKey()) || StringUtils.equals(pbcSyncListDto.getAcctAccountKey(), acctAccountKey)) {
                                needPush = false;
                            }
                        }
                        //核准类数据的变更，只有变更账户名称、法人姓名、法人证件类型和号码 人行才会下发新的 开户许可证号
                        //其他核准类的变更需要判断是否在待核准列表删除来查询是否成功
                        //如果存在则直接跳过检查下一条
                        if (pbcAmsService.existAccountInCheckList(pbcAccountDto, acctNo)) {
                            continue;
                        }
                    }
                    pbcSyncListDto.setAccountKey(accountKey);
                    pbcSyncListDto.setAcctAccountKey(acctAccountKey);
                    pbcSyncListDto.setSyncStatus(CompanyIfType.Yes);
                    //保存账户
                    log.info("账号：" + pbcSyncListDto.getAcctNo() + "人行已核准...");
                    pbcSyncListDto.setAmsAccountInfo(amsAccountInfo);
                } else {
                    continue;
                }
            } catch (Exception e) {
                log.error("人行查询异常", e);
            }

            //更新本地数据
            try {
                if (amsAccountInfo != null) {
                    final AmsAccountInfo pbcAccountInfo = amsAccountInfo;
                    transactionUtils.executeInNewTransaction(new TransactionCallback() {
                        @Override
                        public void execute() throws Exception {
                            updateAccount(pbcSyncListDto, pbcAccountInfo);
                        }
                    });

                }
            } catch (Exception e) {
                log.error("更新本地数据异常", e);
            }

            //推送核心
            try {
                if (amsAccountInfo != null) {
                    //执行推送方法
                    if (needPush) {
                        log.info("人行amsAccountInfo：{}",amsAccountInfo);
                        log.info("人行推送核心json：{}",amsAccountInfoJson);
                        writeTxtList.add(pbcSyncListDto);
                        String accountKey = amsAccountInfo.getAcctType() == AccountType.jiben ? amsAccountInfo.getAccountKey() : amsAccountInfo.getAccountLicenseNo();
                        Object obj = amsHzPushService.push(pbcSyncListDto.getAcctNo(), accountKey);
                        Object objAll = amsHzPushService.pushAll(DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis()), JSON.toJSONString(amsAccountInfo));
                        log.info("推送完成");
                    }
                    //保存推送状态为yes
                    pbcSyncListDto.setIsPush(CompanyIfType.Yes);
                    pbcSyncListService.savePbcSyncList(pbcSyncListDto);
                }
            } catch (Exception e) {
                log.error("推送核心异常", e);
            }


        }

        //获取list集合  存放txt文件到目录下 实现类去读取文件解析
        if (CollectionUtils.isNotEmpty(writeTxtList)) {
            log.info("人行本次核准数量为:" + writeTxtList.size() + "笔...");
            File file = new File(pushCoreFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File batchFile = new File(pushCoreFilePath + File.separator + System.currentTimeMillis() + ".txt");
            try {
                log.info("人行本次核推送文件名称:" + file.getName());
                if (!batchFile.exists()) {
                    batchFile.createNewFile();
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(batchFile, true));
                for (PbcSyncListDto pbcSyncListDto : writeTxtList) {
                    String writeString = "";
                    AmsAccountInfo amsAccountInfo = pbcSyncListDto.getAmsAccountInfo();
                    String accountKey = amsAccountInfo.getAcctType() == AccountType.jiben ? amsAccountInfo.getAccountKey() : amsAccountInfo.getAccountLicenseNo();
                    writeString = amsAccountInfo.getAcctNo() + "," + accountKey + "," + DateUtils.DateToStr(new Date(), "yyyy-MM-dd");
                    //临时非临时户推送临时户的到期日
                    if (amsAccountInfo.getAcctType() == AccountType.feilinshi || amsAccountInfo.getAcctType() == AccountType.linshi) {
                        writeString += "," + amsAccountInfo.getEffectiveDate();
                    }
                    writer.write(writeString);
                    writer.newLine();
                    writer.flush();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //推送
            log.info("人行核准文件路径 :" + batchFile.getAbsolutePath());
            try {
                Object obj = amsHzPushService.pushCoreFile(batchFile.getAbsolutePath());
            } catch (Exception e) {
                log.error("推送文件路径异常", e);
                for (PbcSyncListDto pbcSyncListDto : writeTxtList) {
                    log.info("人行核准文件推送失败");
                    //保存推送状态为yes
                    pbcSyncListDto.setIsPush(CompanyIfType.No);
                    pbcSyncListService.savePbcSyncList(pbcSyncListDto);
                }
            }
        }
    }

    @Override
    public void checkAndCancelHeZhunPushCore() {
        //取到取消核准为推送成功的账户
        List<PbcSyncListDto> pbcSyncList = pbcSyncListService.getCancelHeZhunList();

        //拿到一个批次人行已核准的账户数据(批量)
        List<PbcSyncListDto> writeTxtList = new ArrayList<>(16);

        for (final PbcSyncListDto pbcSyncListDto : pbcSyncList) {
            //推送核心
            try {
                writeTxtList.add(pbcSyncListDto);
                String accountKey = pbcSyncListDto.getAccountKey();
                Object obj = amsHzPushService.push(pbcSyncListDto.getAcctNo(), accountKey);
                Object objAll = amsHzPushService.pushAll(DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis()), JSON.toJSONString(pbcSyncListDto));
                log.info("推送完成");
                //保存推送状态为yes
                pbcSyncListDto.setIsPush(CompanyIfType.Yes);
                pbcSyncListService.savePbcSyncList(pbcSyncListDto);
            } catch (Exception e) {
                log.error("推送核心异常", e);
                pbcSyncListDto.setIsPush(CompanyIfType.No);
                pbcSyncListService.savePbcSyncList(pbcSyncListDto);
            }
        }

        //获取list集合  存放txt文件到目录下 实现类去读取文件解析
        if (CollectionUtils.isNotEmpty(writeTxtList)) {
            log.info("人行本次核准数量为:" + writeTxtList.size() + "笔...");
            File file = new File(pushCancelHeZhunCoreFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File batchFile = new File(pushCancelHeZhunCoreFilePath + File.separator + System.currentTimeMillis() + ".txt");
            try {
                log.info("人行本次核推送文件名称:" + file.getName());
                if (!batchFile.exists()) {
                    batchFile.createNewFile();
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(batchFile, true));
                for (PbcSyncListDto pbcSyncListDto : writeTxtList) {
                    String writeString = "";
                    String accountKey = pbcSyncListDto.getAccountKey();
                    writeString = pbcSyncListDto.getAcctNo() + "," + accountKey + "," + DateUtils.DateToStr(new Date(), "yyyy-MM-dd");
                    //临时非临时户推送临时户的到期日
                    if (StringUtils.isNotBlank(pbcSyncListDto.getEffectiveDate())) {
                        writeString += "," + pbcSyncListDto.getEffectiveDate();
                    }
                    writer.write(writeString);
                    writer.newLine();
                    writer.flush();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //推送
            log.info("人行核准文件路径 :" + batchFile.getAbsolutePath());
            try {
                Object obj = amsHzPushService.pushCoreFile(batchFile.getAbsolutePath());
            } catch (Exception e) {
                log.error("推送文件路径异常", e);
                for (PbcSyncListDto pbcSyncListDto : writeTxtList) {
                    log.info("人行核准文件推送失败");
                    //保存推送状态为yes
                    pbcSyncListDto.setIsPush(CompanyIfType.No);
                    pbcSyncListService.savePbcSyncList(pbcSyncListDto);
                }
            }
        }
    }

    public void updateAccount(PbcSyncListDto pbcSyncListDto, AmsAccountInfo amsAccountInfo) throws Exception {
        Long billId = pbcSyncListDto.getBillId();
        String accountKey = pbcSyncListDto.getAccountKey();
        String acctAccountKey = pbcSyncListDto.getAcctAccountKey();
        AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getOne(billId);
        if (accountBillsAllInfo != null) {
            if (amsAccountInfo.getAcctType() == AccountType.jiben) {
                //更新客户信息
                //日志表
                if (accountBillsAllInfo.getCustomerLogId() != null) {
                    CustomerPublicLogInfo customerPublicLogInfo = customerPublicLogService.getOne(accountBillsAllInfo.getCustomerLogId());
                    if (customerPublicLogInfo != null) {
                        customerPublicLogInfo.setAccountKey(accountKey);
                        customerPublicLogService.save(customerPublicLogInfo);
                    }
                }
                //中间表
                CustomerPublicMidInfo customerPublicMidInfo = customerPublicMidService.getByBillId(accountBillsAllInfo.getId());
                if (customerPublicMidInfo != null) {
                    customerPublicMidInfo.setAccountKey(accountKey);
                    customerPublicMidService.save(customerPublicMidInfo);
                } else if (StringUtils.isNotBlank(accountBillsAllInfo.getCustomerNo())) {
                    //主表
                    CustomerPublicInfo customerPublicInfo = customerPublicService.getByCustomerNo(accountBillsAllInfo.getCustomerNo());
                    if (customerPublicInfo != null) {
                        customerPublicInfo.setAccountKey(accountKey);
                        customerPublicService.save(customerPublicInfo);
                    }
                }

                //账户主表信息保存accountKey
                if(accountBillsAllInfo.getAccountId() != null){
                    AccountsAllInfo accountsAllInfo = accountsAllService.findByRefBillId(billId);
                    if(accountsAllInfo != null){
                        accountsAllInfo.setAccountKey(accountKey);
                        accountsAllService.save(accountsAllInfo);
                    }
                }
            } else {
                //更新账户信息中的账户核准号字段
                AccountPublicInfo accountPublicInfo = accountPublicService.findByAccountId(accountBillsAllInfo.getAccountId());
                accountPublicInfo.setAccountLicenseNo(acctAccountKey);
                accountPublicService.save(accountPublicInfo);
            }
            //修改最终状态，核准类的核准时间与核准状态在此方法中包含
            allBillsPublicService.updateFinalStatusById(billId);
        }
    }



}
