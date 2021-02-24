package com.ideatech.ams.account.executor;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.account.dao.bill.AccountBillsAllDao;
import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.service.AccountPublicService;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.customer.dto.CustomerPublicInfo;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.dto.CustomersAllInfo;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.customer.service.CustomersAllService;
import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.ams.pbc.utils.AtomicLongUtils;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.util.BeanValueUtils;
import com.ideatech.common.util.DateUtil;
import com.ideatech.common.util.DateUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 人行数据覆盖核心数据（核心流水更新、账户对公更新、账户主表更新、客户主表、客户对公表）
 * @author jzh
 * @date 2019-09-24.
 */

@Data
@Slf4j
public class CoreBillsUpdateExecutor implements Callable {

    private PbcAmsService pbcAmsService;

    private DictionaryService dictionaryService;

    private OrganizationService organizationService;

    private ConfigService configService;

    private AccountPublicService accountPublicService;

    private AccountsAllService accountsAllService;

    private AccountBillsAllDao accountBillsAllDao;

    private AccountBillsAllService accountBillsAllService;

    private CustomerPublicService customerPublicService;

    private CustomerPublicLogService customerPublicLogService;

    private CustomersAllService customersAllService;

    private List<AccountBillsAll> accountBillsAllList;

    /**
     * 是否启用人行采集限制机制
     */
    private Boolean pbcCollectionLimitUse;

    /**
     * 人行采集数量控制
     */
    private Long pbcCollectionLimitNum;
    //覆盖人行执行时间
    private String pbcCoverCoreTime;
    //是否使用时间暂停方式
    private boolean pbcCoverCoreTimeUse;


    @Override
    public Object call() throws Exception {

        //是否模拟人行返回
        boolean flag = false;
        Long successNum = 0L;
        Long failNum = 0L;

        if (configService.findByKey("pbcIgnore").size()>0){
            flag = true;
            log.info("人行数据覆盖核心数据线程{}-模拟人行返回",Thread.currentThread().getName());
        }
        if(pbcCoverCoreTimeUse){
            log.info("使用时间暂停方式人行覆盖核心：{},时间设置：{}",pbcCoverCoreTimeUse,pbcCoverCoreTime);
            int i=0;
            while (true){
                if(i>accountBillsAllList.size()-1){
                    log.info("覆盖完成，跳出循环");
                    break;
                }
                if(isExecute(pbcCoverCoreTime) && i<=accountBillsAllList.size()-1 ){
                    AccountBillsAll accountBillsAll= accountBillsAllList.get(i);
                    try {
                        AmsAccountInfo amsAccountInfo = null;
                        AmsAccountInfo changeAmsAccountInfo = null;
                        OrganizationDto orgDto = null;

                        if (accountBillsAllDao.countByAcctNo(accountBillsAll.getAcctNo())>1){
                            log.info("账户{}已经在账管系统发生流水，跳过人行数据覆盖核心数据操作",accountBillsAll.getAcctNo());
                            failNum++;
                            i++;
                            continue;
                        }

                        if (StringUtils.isNotBlank(accountBillsAll.getOrganFullId())) {

                            orgDto = organizationService.findByOrganFullId(accountBillsAll.getOrganFullId());

                            if (orgDto != null) {

                                //模拟人行返回
                                if (flag){
                                    log.info("存量导入模拟人行返回数据");
                                    amsAccountInfo = JSON.parseObject("{\"accountKey\":\"J3052011001201\",\"accountLicenseNo\":\"J3052011001201\",\"accountStatus\":\"normal\",\"acctCreateDate\":\"2012-07-27\",\"acctName\":\"昆山谛镨科自动化科技有限公司\",\"acctNo\":\"3052244012015000000911\",\"acctType\":\"jiben\",\"bankCode\":\"314305200193\",\"bankName\":\"昆山农村商业银行股份有限公司张浦支行\",\"businessScope\":\"电子专用设备、测试仪器、工模具、精密在线测试仪器及设备的研发、制造、销售及上门维护；五金机电、电子材料、合成板的销售；货物及技术的进口业务。（依法须经批准的项目，经相关部门批准后方可开展经营活动）\",\"cancelDate\":\"-\",\"depositorName\":\"昆山谛镨科自动化科技有限公司\",\"depositorType\":\"企业法人\",\"fileNo\":\"91320583050268697R\",\"fileNo2\":\"\",\"fileType\":\"工商营业执照\",\"fileType2\":\"\",\"industryCode\":\"制造业\",\"legalIdcardNo\":\"04391030\",\"legalIdcardType\":\"港、澳、台居民通行证\",\"legalName\":\"赖俊宏\",\"noTaxProve\":\"\",\"orgCode\":\"050268697\",\"parAccountKey\":\"\",\"parCorpName\":\"\",\"parLegalIdcardNo\":\"\",\"parLegalIdcardType\":\"\",\"parLegalName\":\"\",\"parOrgCode\":\"\",\"regAddress\":\"张浦镇永燃路115号（1号、2号房）\",\"regAreaCode\":\"305200\",\"regCurrencyType\":\"人民币\",\"registeredCapital\":\"3000000.00\",\"stateTaxRegNo\":\"91320583050268697R\",\"taxRegNo\":\"91320583050268697R\",\"telephone\":\"13862667599\",\"zipCode\":\"215321\"}",AmsAccountInfo.class);
                                }else {
                                    log.info("存量导入查询人行信息");
                                    amsAccountInfo = getAmsDetails(orgDto.getCode(),accountBillsAll.getAcctNo());
                                }
                                //通过销户接口回去法定代表人信息
                                if(amsAccountInfo != null && StringUtils.isBlank(amsAccountInfo.getLegalType())){
                                    String[] cancelHeZhunType = {"企业法人", "非法人企业", "有字号的个体工商户", "无字号的个体工商户", "01", "02", "13", "14"};
                                    //包含说明是取消核准的账户   在不判断该地区是不是已经上取消核准的前提下
                                    try{
                                        if(Arrays.asList(cancelHeZhunType).contains(amsAccountInfo.getDepositorType())){
                                            //通过变更接口查询法人类型数据--方法名不变，方法里面逻辑已做修改
                                            changeAmsAccountInfo = getRevokeAmsDetails(orgDto,accountBillsAll.getAcctNo(),amsAccountInfo.getAcctType().toString());
                                        }
                                    }catch (Exception e){
                                        if(e.getMessage().indexOf("核准企业类存款账户，请到人民银行办理撤销业务") > -1){
                                            log.info("账户：{}，不是取消核准账户或该地区不是取消核准地区。",accountBillsAll.getAcctNo());
                                        }
                                    }
                                }

                                //更新核心流水、账户主表、账户对公表、客户主表、客户对公表
                                if (amsAccountInfo != null && StringUtils.isNotBlank(amsAccountInfo.getDepositorName())) {
                                    //获取销户接口的法定代表人信息
                                    if(changeAmsAccountInfo != null && StringUtils.isNotBlank(changeAmsAccountInfo.getDepositorName())){
                                        log.info("存量导入setLegalType");
                                        amsAccountInfo.setLegalType(changeAmsAccountInfo.getLegalType());
                                    }
                                    log.info(JSON.toJSONString(amsAccountInfo));
                                    //更新核心流水等信息
                                    log.info("存量数据更新流水信息开始..............");
                                    updateBills(amsAccountInfo,accountBillsAll);
                                    successNum++;
                                    i++;
                                    log.info("账号为{}，人行数据覆盖核心数据成功（更新核心流水等信息）", accountBillsAll.getAcctNo());
                                } else {
                                    failNum++;
                                    i++;
                                    log.warn("账号为{}，未查询到人行数据，跳过人行数据覆盖核心数据操作", accountBillsAll.getAcctNo());
                                }

                            } else {
                                failNum++;
                                i++;
                                log.warn("{}错误的organFullId{}，跳过人行数据覆盖核心数据操作",accountBillsAll.getAcctNo(),accountBillsAll.getOrganFullId());
                            }
                        } else {
                            failNum++;
                            i++;
                            log.warn("{}未找到organFullId数据，跳过人行数据覆盖核心数据操作。",accountBillsAll.getAcctNo());
                        }
                    } catch (Exception e) {
                        failNum++;
                        i++;
                        log.error("更新核心流水等信息异常，账号为{}，organFullId为{}", accountBillsAll.getAcctNo(), accountBillsAll.getOrganFullId(),e);
                    }
                }
            }
        }else{
            for (AccountBillsAll accountBillsAll: accountBillsAllList) {
                try {
                    AmsAccountInfo amsAccountInfo = null;
                    AmsAccountInfo changeAmsAccountInfo = null;
                    OrganizationDto orgDto = null;

                    if (accountBillsAllDao.countByAcctNo(accountBillsAll.getAcctNo())>1){
                        log.info("账户{}已经在账管系统发生流水，跳过人行数据覆盖核心数据操作",accountBillsAll.getAcctNo());
                        failNum++;
                        continue;
                    }

                    if (StringUtils.isNotBlank(accountBillsAll.getOrganFullId())) {

                        orgDto = organizationService.findByOrganFullId(accountBillsAll.getOrganFullId());

                        if (orgDto != null) {

                            //模拟人行返回
                            if (flag){
                                log.info("存量导入模拟人行返回数据");
                                amsAccountInfo = JSON.parseObject("{\"accountKey\":\"J3052011001201\",\"accountLicenseNo\":\"J3052011001201\",\"accountStatus\":\"normal\",\"acctCreateDate\":\"2012-07-27\",\"acctName\":\"昆山谛镨科自动化科技有限公司\",\"acctNo\":\"3052244012015000000911\",\"acctType\":\"jiben\",\"bankCode\":\"314305200193\",\"bankName\":\"昆山农村商业银行股份有限公司张浦支行\",\"businessScope\":\"电子专用设备、测试仪器、工模具、精密在线测试仪器及设备的研发、制造、销售及上门维护；五金机电、电子材料、合成板的销售；货物及技术的进口业务。（依法须经批准的项目，经相关部门批准后方可开展经营活动）\",\"cancelDate\":\"-\",\"depositorName\":\"昆山谛镨科自动化科技有限公司\",\"depositorType\":\"企业法人\",\"fileNo\":\"91320583050268697R\",\"fileNo2\":\"\",\"fileType\":\"工商营业执照\",\"fileType2\":\"\",\"industryCode\":\"制造业\",\"legalIdcardNo\":\"04391030\",\"legalIdcardType\":\"港、澳、台居民通行证\",\"legalName\":\"赖俊宏\",\"noTaxProve\":\"\",\"orgCode\":\"050268697\",\"parAccountKey\":\"\",\"parCorpName\":\"\",\"parLegalIdcardNo\":\"\",\"parLegalIdcardType\":\"\",\"parLegalName\":\"\",\"parOrgCode\":\"\",\"regAddress\":\"张浦镇永燃路115号（1号、2号房）\",\"regAreaCode\":\"305200\",\"regCurrencyType\":\"人民币\",\"registeredCapital\":\"3000000.00\",\"stateTaxRegNo\":\"91320583050268697R\",\"taxRegNo\":\"91320583050268697R\",\"telephone\":\"13862667599\",\"zipCode\":\"215321\"}",AmsAccountInfo.class);
                            }else {
                                log.info("存量导入查询人行信息");
                                amsAccountInfo = getAmsDetails(orgDto.getCode(),accountBillsAll.getAcctNo());
                            }
                            //通过销户接口回去法定代表人信息
                            if(amsAccountInfo != null && StringUtils.isBlank(amsAccountInfo.getLegalType())){
                                String[] cancelHeZhunType = {"企业法人", "非法人企业", "有字号的个体工商户", "无字号的个体工商户", "01", "02", "13", "14"};
                                //包含说明是取消核准的账户   在不判断该地区是不是已经上取消核准的前提下
                                try{
                                    if(Arrays.asList(cancelHeZhunType).contains(amsAccountInfo.getDepositorType())){
                                        //通过变更接口查询法人类型数据--方法名不变，方法里面逻辑已做修改
                                        changeAmsAccountInfo = getRevokeAmsDetails(orgDto,accountBillsAll.getAcctNo(),amsAccountInfo.getAcctType().toString());
                                    }
                                }catch (Exception e){
                                    if(e.getMessage().indexOf("核准企业类存款账户，请到人民银行办理撤销业务") > -1){
                                        log.info("账户：{}，不是取消核准账户或该地区不是取消核准地区。",accountBillsAll.getAcctNo());
                                    }
                                }
                            }

                            //更新核心流水、账户主表、账户对公表、客户主表、客户对公表
                            if (amsAccountInfo != null && StringUtils.isNotBlank(amsAccountInfo.getDepositorName())) {
                                //获取销户接口的法定代表人信息
                                if(changeAmsAccountInfo != null && StringUtils.isNotBlank(changeAmsAccountInfo.getDepositorName())){
                                    log.info("存量导入setLegalType");
                                    amsAccountInfo.setLegalType(changeAmsAccountInfo.getLegalType());
                                }
                                log.info(JSON.toJSONString(amsAccountInfo));
                                //更新核心流水等信息
                                log.info("存量数据更新流水信息开始..............");
                                updateBills(amsAccountInfo,accountBillsAll);
                                successNum++;
                                log.info("账号为{}，人行数据覆盖核心数据成功（更新核心流水等信息）", accountBillsAll.getAcctNo());
                            } else {
                                failNum++;
                                log.warn("账号为{}，未查询到人行数据，跳过人行数据覆盖核心数据操作", accountBillsAll.getAcctNo());
                            }

                        } else {
                            failNum++;
                            log.warn("{}错误的organFullId{}，跳过人行数据覆盖核心数据操作",accountBillsAll.getAcctNo(),accountBillsAll.getOrganFullId());
                        }
                    } else {
                        failNum++;
                        log.warn("{}未找到organFullId数据，跳过人行数据覆盖核心数据操作。",accountBillsAll.getAcctNo());
                    }
                } catch (Exception e) {
                    failNum++;
                    log.error("更新核心流水等信息异常，账号为{}，organFullId为{}", accountBillsAll.getAcctNo(), accountBillsAll.getOrganFullId(),e);
                }
            }
        }
        log.info("{}，更新核心流水等信息成功数量：{}", Thread.currentThread().getName(),successNum);
        log.info("{}，更新核心流水等信息失败数量：{}", Thread.currentThread().getName(),failNum);
        return System.currentTimeMillis();
    }

    /**
     * 人行调用查询详细信息  如果人行关闭睡眠15分钟，死循环查询人行
     *
     */
    public AmsAccountInfo getAmsDetails(String organCode,String acctNo) throws Exception{
        AmsAccountInfo amsAccountInfo = null;
        try {
            if(pbcCollectionLimitUse && !AtomicLongUtils.isPause(pbcCollectionLimitNum)){
                log.info("方法：线程人行查询数量：" + AtomicLongUtils.al.getAndIncrement());
                amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(organCode, acctNo);//获取人行数据
            }else{
                amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(organCode, acctNo);//获取人行数据
            }
        }catch (Exception e){
            if(e.getMessage().indexOf("人行账管系统服务已关闭") > -1){
                log.info("人行账管系统关闭，线程睡眠15分钟后再次查询。");
                Thread.sleep(1000 * 60 * 15);
                return getAmsDetails(organCode,acctNo);
            } else if(e.getMessage().indexOf("客户机登录日期跟服务器日期不为同一天") > -1){
                log.info("人行登录异常，线程睡眠15分钟后再次登录查询。");
                Thread.sleep(1000 * 60 * 15);
                return getAmsDetails(organCode,acctNo);
            } else if(e.getMessage().indexOf("未知异常") > -1){
                log.info("人行登录未知异常，线程睡眠15分钟后再次登录查询。");
                Thread.sleep(1000 * 60 * 15);
                return getAmsDetails(organCode,acctNo);
            } else {
                throw e;
            }
        }
        return amsAccountInfo;
    }

    /**
     * 人行调用查询取消核准详细信息  如果人行关闭睡眠15分钟，死循环查询人行
     */
    public AmsAccountInfo getRevokeAmsDetails(OrganizationDto organizationDto,String acctNo,String acctType) throws Exception{
        AmsAccountInfo amsAccountInfo = null;
        try {
            if(pbcCollectionLimitUse && !AtomicLongUtils.isPause(pbcCollectionLimitNum)){
                log.info("方法：线程人行查询数量：" + AtomicLongUtils.al.getAndIncrement());
//                amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNoAndAcctTypeToRevoke(organizationDto,acctNo,acctType);//获取销户详细数据
                log.info("使用变更接口查询法人类型");
                try{
                    //增加查询条件
                    AllAcct allAcct = new AllAcct();
                    allAcct.setAcctNo(acctNo);
                    allAcct.setBankCode(organizationDto.getPbcCode());
                    allAcct.setBankName(organizationDto.getName());
                    amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNoFromChangeHtml(organizationDto, allAcct);

                }catch (Exception e){
                    log.info("账号：" + acctNo + "查询人行详情失败！" + e.getMessage());
                }
            }else{
//                amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNoAndAcctTypeToRevoke(organizationDto,acctNo,acctType);//获取销户详细数据
                log.info("使用变更接口查询法人类型");
                try{
                    //增加查询条件
                    AllAcct allAcct = new AllAcct();
                    allAcct.setAcctNo(acctNo);
                    allAcct.setBankCode(organizationDto.getPbcCode());
                    allAcct.setBankName(organizationDto.getName());
                    amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNoFromChangeHtml(organizationDto, allAcct);

                }catch (Exception e){
                    log.info("账号：" + acctNo + "查询人行详情失败！" + e.getMessage());
                }
            }
        }catch (Exception e){
            if(e.getMessage().indexOf("人行账管系统服务已关闭") > -1){
                log.info("人行账管系统关闭，线程睡眠15分钟后再次查询。");
                Thread.sleep(1000 * 60 * 15);
                return getAmsDetails(organizationDto.getCode(),acctNo);
            } else if(e.getMessage().indexOf("客户机登录日期跟服务器日期不为同一天") > -1){
                log.info("人行登录异常，线程睡眠15分钟后再次登录查询。");
                Thread.sleep(1000 * 60 * 15);
                return getAmsDetails(organizationDto.getCode(),acctNo);
            } else if(e.getMessage().indexOf("未知异常") > -1){
                log.info("人行登录未知异常，线程睡眠15分钟后再次登录查询。");
                Thread.sleep(1000 * 60 * 15);
                return getAmsDetails(organizationDto.getCode(),acctNo);
            } else if(e.getMessage().indexOf("核准企业类存款账户，请到人民银行办理撤销业务") > -1){
                log.info("核准企业类存款账户，请到人民银行办理撤销业务，该账户不是取消核准或该地区不是取消核准地区");
                throw e;
            } else {
                throw e;
            }
        }
        return amsAccountInfo;
    }

    /**
     * 更新核心流水表、账户主表、账户对公表、客户主表、客户对公表、客户对公日志表（正常情况不允许修改）
     * 参考CoreFileSaveExecutor原逻辑中的人行覆盖核心数据
     * @param amsAccountInfo
     * @param accountBillsAll
     */
    private void updateBills(AmsAccountInfo amsAccountInfo, AccountBillsAll accountBillsAll) throws Exception {

        //账户主表
        AccountsAllInfo accountsAllInfo = accountsAllService.getOne(accountBillsAll.getAccountId());
        //账户对公表
        AccountPublicInfo accountPublicInfo = accountPublicService.findByAccountId(accountBillsAll.getAccountId());
        //客户主表
        CustomersAllInfo customersAllInfo = customersAllService.findByCustomerNo(accountBillsAll.getCustomerNo());
        //客户对公表
        CustomerPublicInfo customerPublicInfo = null;
        //客户对公日志表
        CustomerPublicLogInfo customerPublicLogInfo = null;


        if (accountsAllInfo==null){
            accountsAllInfo = new AccountsAllInfo();
        }
        if (accountPublicInfo==null){
            accountPublicInfo = new AccountPublicInfo();
        }
        if (customersAllInfo==null){
            customersAllInfo = new CustomersAllInfo();
        }else {
            customerPublicInfo = customerPublicService.getByCustomerId(customersAllInfo.getId());
            customerPublicLogInfo = customerPublicLogService.getOne(accountBillsAll.getCustomerLogId());
        }
        if (customerPublicInfo==null){
            customerPublicInfo = new CustomerPublicInfo();
            customerPublicInfo.setCustomerId(customersAllInfo.getId());
        }
        if (customerPublicLogInfo==null){
            customerPublicLogInfo = new CustomerPublicLogInfo();
            customerPublicLogInfo.setRefBillId(accountBillsAll.getId());
        }

        //注册地址（详细地址）人行getRegAddress映射到本地账管的RegFullAddress
        if (StringUtils.isBlank(customerPublicInfo.getRegFullAddress())||StringUtils.isBlank(customerPublicLogInfo.getRegFullAddress())){
//            accountBillsAll.setRegFullAddress(amsAccountInfo.getRegAddress());
//            accountsAllInfo.setRegFullAddress(amsAccountInfo.getRegAddress());
//            accountPublicInfo.setRegFullAddress(amsAccountInfo.getRegAddress());
//            customersAllInfo.setRegFullAddress(amsAccountInfo.getRegAddress());
            customerPublicInfo.setRegFullAddress(amsAccountInfo.getRegAddress());
            customerPublicLogInfo.setRegFullAddress(amsAccountInfo.getRegAddress());
        }

        if(StringUtils.isBlank(customerPublicInfo.getRegAddress())){
            //事先填充，防止下一步被人行数据覆盖
            //工商注册地址，账管的RegAddress，上报信用机构使用。
            customerPublicInfo.setRegAddress("regAddress");
            customerPublicLogInfo.setRegAddress("regAddress");
        }

        //人行数据补全核心数据（为空的字段取人行的数据进行填充）
        BeanValueUtils.copyPropertiesForEmpty(amsAccountInfo, accountBillsAll);
        BeanValueUtils.copyPropertiesForEmpty(amsAccountInfo, accountsAllInfo);
        BeanValueUtils.copyPropertiesForEmpty(amsAccountInfo, accountPublicInfo);
        BeanValueUtils.copyPropertiesForEmpty(amsAccountInfo, customersAllInfo);
        BeanValueUtils.copyPropertiesForEmpty(amsAccountInfo, customerPublicInfo);

        if ("regAddress".equals(customerPublicInfo.getRegAddress())){
            //回填数据 见286行
            customerPublicInfo.setRegAddress("");
            customerPublicLogInfo.setRegAddress("");
        }

        //搜索“配置开关打开则转换”，原逻辑初始化流水时,import=true 则进行大类转小类
        //现有逻辑：人行覆盖核心时，也进行大类转小类。
        accountBillsAll.setAcctType(CompanyAcctType.valueOf(amsAccountInfo.getAcctType().name()));
        accountsAllInfo.setAcctType(CompanyAcctType.valueOf(amsAccountInfo.getAcctType().name()));
        accountPublicInfo.setAcctType(CompanyAcctType.valueOf(amsAccountInfo.getAcctType().name()));
//        customersAllInfo.setAcctType(CompanyAcctType.valueOf(amsAccountInfo.getAcctType().name()));
        customerPublicInfo.setAcctType(CompanyAcctType.valueOf(amsAccountInfo.getAcctType().name()).name());
//        customerPublicLogInfo.setAcctType(CompanyAcctType.valueOf(amsAccountInfo.getAcctType().name()));


        //设置账户状态
        accountsAllInfo.setAccountStatus(AccountStatus.str2enum(amsAccountInfo.getAccountStatus().getFullName()));


        //通用部分赋值 客户对公表
        seturrencyValue(amsAccountInfo,accountBillsAll,accountsAllInfo,accountPublicInfo,customerPublicInfo,customersAllInfo,customerPublicLogInfo);

        //账户名称构成方式
        if(StringUtils.isNotBlank(amsAccountInfo.getAccountNameFrom())){
//            accountBillsAll.setAccountNameFrom(dictionaryService.transalteLike("账户构成方式",amsAccountInfo.getAccountNameFrom()));
//            accountsAllInfo.setAccountNameFrom(dictionaryService.transalteLike("账户构成方式",amsAccountInfo.getAccountNameFrom()));
            accountPublicInfo.setAccountNameFrom(dictionaryService.transalteLike("账户构成方式",amsAccountInfo.getAccountNameFrom()));
//            customerPublicInfo.setAccountNameFrom(dictionaryService.transalteLike("账户构成方式",amsAccountInfo.getAccountNameFrom()));
//            customersAllInfo.setAccountNameFrom(dictionaryService.transalteLike("账户构成方式",amsAccountInfo.getAccountNameFrom()));
        }
        //资金性质
        if(StringUtils.isNotBlank(amsAccountInfo.getCapitalProperty())){
//            accountBillsAll.setCapitalProperty(dictionaryService.transalteLike("资金性质",amsAccountInfo.getCapitalProperty()));
//            accountsAllInfo.setCapitalProperty(dictionaryService.transalteLike("资金性质",amsAccountInfo.getCapitalProperty()));
            accountPublicInfo.setCapitalProperty(dictionaryService.transalteLike("资金性质",amsAccountInfo.getCapitalProperty()));
//            customerPublicInfo.setCapitalProperty(dictionaryService.transalteLike("资金性质",amsAccountInfo.getCapitalProperty()));
//            customersAllInfo.setCapitalProperty(dictionaryService.transalteLike("资金性质",amsAccountInfo.getCapitalProperty()));
        }

        //根据账户类型赋值
        if(amsAccountInfo.getAcctType() == AccountType.jiben){
            updateJibenFile(amsAccountInfo,accountBillsAll,accountsAllInfo,accountPublicInfo,customerPublicInfo,customersAllInfo,customerPublicLogInfo);
            //存款人类型
            if(StringUtils.isNotBlank(amsAccountInfo.getDepositorType())){
                accountBillsAll.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)",amsAccountInfo.getDepositorType()));
//                accountsAllInfo.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)",amsAccountInfo.getDepositorType()));
                accountPublicInfo.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)",amsAccountInfo.getDepositorType()));
//                customerPublicInfo.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)",amsAccountInfo.getDepositorType()));
//                customersAllInfo.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)",amsAccountInfo.getDepositorType()));
            }
        }



        if(amsAccountInfo.getAcctType() == AccountType.linshi){
            if(StringUtils.isNotBlank(amsAccountInfo.getFileType())){
//                accountBillsAll.setFileType(dictionaryService.transalteLike("证明文件1种类(临时户)",amsAccountInfo.getFileType()));
//                accountsAllInfo.setFileType(dictionaryService.transalteLike("证明文件1种类(临时户)",amsAccountInfo.getFileType()));
//                accountPublicInfo.setFileType(dictionaryService.transalteLike("证明文件1种类(临时户)",amsAccountInfo.getFileType()));
                customerPublicInfo.setFileType(dictionaryService.transalteLike("证明文件1种类(临时户)",amsAccountInfo.getFileType()));
//                customersAllInfo.setFileType(dictionaryService.transalteLike("证明文件1种类(临时户)",amsAccountInfo.getFileType()));
            }

            //临时户账户许可证号
            if(StringUtils.isNotBlank(amsAccountInfo.getAccountLicenseNo())){
                accountBillsAll.setAccountLicenseNo(amsAccountInfo.getAccountLicenseNo());
//                accountsAllInfo.setAccountLicenseNo(amsAccountInfo.getAccountLicenseNo());
                accountPublicInfo.setAccountLicenseNo(amsAccountInfo.getAccountLicenseNo());
//                customerPublicInfo.setAccountLicenseNo(amsAccountInfo.getAccountLicenseNo());
//                customersAllInfo.setAccountLicenseNo(amsAccountInfo.getAccountLicenseNo());
            }
        }

        if(amsAccountInfo.getAcctType() == AccountType.teshu){
            if(StringUtils.isNotBlank(amsAccountInfo.getFileType())){

//                accountBillsAll.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(特殊户)",amsAccountInfo.getAccountFileType()));
//                accountsAllInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(特殊户)",amsAccountInfo.getAccountFileType()));
                accountPublicInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(特殊户)",amsAccountInfo.getAccountFileType()));
//                customerPublicInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(特殊户)",amsAccountInfo.getAccountFileType()));
//                customersAllInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(特殊户)",amsAccountInfo.getAccountFileType()));
            }
        }

        if(amsAccountInfo.getAcctType() == AccountType.yiban){
            if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileType())){

//                accountBillsAll.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(一般户)",amsAccountInfo.getAccountFileType()));
//                accountsAllInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(一般户)",amsAccountInfo.getAccountFileType()));
                accountPublicInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(一般户)",amsAccountInfo.getAccountFileType()));
//                customerPublicInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(一般户)",amsAccountInfo.getAccountFileType()));
//                customersAllInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(一般户)",amsAccountInfo.getAccountFileType()));
            }
            //基本户种类赋值
            updateJibenFile(amsAccountInfo,accountBillsAll,accountsAllInfo,accountPublicInfo,customerPublicInfo,customersAllInfo,customerPublicLogInfo);
        }

        if(amsAccountInfo.getAcctType() == AccountType.feiyusuan || amsAccountInfo.getAcctType() == AccountType.yusuan){
            if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileType())){
//                accountBillsAll.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(专用户)",amsAccountInfo.getAccountFileType()));
//                accountsAllInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(专用户)",amsAccountInfo.getAccountFileType()));
                accountPublicInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(专用户)",amsAccountInfo.getAccountFileType()));
//                customerPublicInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(专用户)",amsAccountInfo.getAccountFileType()));
//                customersAllInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(专用户)",amsAccountInfo.getAccountFileType()));
            }
            if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileType2())){

//                accountBillsAll.setAcctFileType2(dictionaryService.transalteLike("证明文件2种类(专用户)",amsAccountInfo.getAccountFileType2()));
//                accountsAllInfo.setAcctFileType2(dictionaryService.transalteLike("证明文件2种类(专用户)",amsAccountInfo.getAccountFileType2()));
                accountPublicInfo.setAcctFileType2(dictionaryService.transalteLike("证明文件2种类(专用户)",amsAccountInfo.getAccountFileType2()));
//                customerPublicInfo.setAcctFileType2(dictionaryService.transalteLike("证明文件2种类(专用户)",amsAccountInfo.getAccountFileType2()));
//                customersAllInfo.setAcctFileType2(dictionaryService.transalteLike("证明文件2种类(专用户)",amsAccountInfo.getAccountFileType2()));
            }
            if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileNo())){
//                accountBillsAll.setAcctFileNo(amsAccountInfo.getAccountFileNo());
//                accountsAllInfo.setAcctFileNo(amsAccountInfo.getAccountFileNo());
                accountPublicInfo.setAcctFileNo(amsAccountInfo.getAccountFileNo());
//                customerPublicInfo.setAcctFileNo(amsAccountInfo.getAccountFileNo());
//                customersAllInfo.setAcctFileNo(amsAccountInfo.getAccountFileNo());
            }
            if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileNo2())){

//                accountBillsAll.setAcctFileNo2(amsAccountInfo.getAccountFileNo2());
//                accountsAllInfo.setAcctFileNo2(amsAccountInfo.getAccountFileNo2());
                accountPublicInfo.setAcctFileNo2(amsAccountInfo.getAccountFileNo2());
//                customerPublicInfo.setAcctFileNo2(amsAccountInfo.getAccountFileNo2());
//                customersAllInfo.setAcctFileNo2(amsAccountInfo.getAccountFileNo2());
            }
            //基本户证明文件种类赋值
            updateJibenFile(amsAccountInfo,accountBillsAll,accountsAllInfo,accountPublicInfo,customerPublicInfo,customersAllInfo,customerPublicLogInfo);
        }

        if(amsAccountInfo.getAcctType() == AccountType.feilinshi){
            if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileType())){
//                accountBillsAll.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(非临时)",amsAccountInfo.getAccountFileType()));
//                accountsAllInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(非临时)",amsAccountInfo.getAccountFileType()));
                accountPublicInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(非临时)",amsAccountInfo.getAccountFileType()));
//                customerPublicInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(非临时)",amsAccountInfo.getAccountFileType()));
//                customersAllInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(非临时)",amsAccountInfo.getAccountFileType()));
//                customerPublicLogInfo.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(非临时)",amsAccountInfo.getAccountFileType()));
            }
            //基本户证明文件种类赋值
            updateJibenFile(amsAccountInfo,accountBillsAll,accountsAllInfo,accountPublicInfo,customerPublicInfo,customersAllInfo,customerPublicLogInfo);
        }

        //修改为人行覆盖成功
        accountBillsAll.setString005("1");

        //保存前再次判断该账户是否发生别的流水。
        if (accountBillsAllDao.countByAcctNo(accountBillsAll.getAcctNo())>1){
            log.info("再次判断：账户{}已经在账管系统发生流水，跳过人行数据覆盖核心数据操作",accountBillsAll.getAcctNo());
            throw new Exception("已经在账管系统发生流水，跳过人行数据覆盖核心数据操作。");
        }

        accountPublicService.save(accountPublicInfo);
        accountsAllService.save(accountsAllInfo);
        customersAllService.save(customersAllInfo);

        customerPublicInfo.setCustomerId(customersAllInfo.getId());
        customerPublicService.save(customerPublicInfo);
        String[] ignoreProperties1 = {"id", "depositorName"};
        BeanUtils.copyProperties(customersAllInfo, customerPublicLogInfo, ignoreProperties1);
        BeanUtils.copyProperties(customerPublicInfo, customerPublicLogInfo, ignoreProperties1);
        //设置customerId
        customerPublicLogInfo.setCustomerId(customerPublicInfo.getCustomerId());
        customerPublicLogService.save(customerPublicLogInfo);

        accountBillsAll.setCustomerLogId(customerPublicLogInfo.getId());
        accountBillsAllDao.save(accountBillsAll);
    }

    //通用部分赋值
    private void seturrencyValue(AmsAccountInfo amsAccountInfo,AccountBillsAll accountBillsAll,AccountsAllInfo accountsAllInfo,AccountPublicInfo accountPublicInfo,CustomerPublicInfo customerPublicInfo,CustomersAllInfo customersAllInfo,CustomerPublicLogInfo customerPublicLogInfo){
        //法定代表人
        if(StringUtils.isNotBlank(amsAccountInfo.getLegalType())){
//            accountBillsAll.setLegalType(dictionaryService.transalteLike("法人类型",amsAccountInfo.getLegalType()));
//            accountsAllInfo.setLegalType(dictionaryService.transalteLike("法人类型",amsAccountInfo.getLegalType()));
//            accountPublicInfo.setLegalType(dictionaryService.transalteLike("法人类型",amsAccountInfo.getLegalType()));
            customerPublicInfo.setLegalType(dictionaryService.transalteLike("法人类型",amsAccountInfo.getLegalType()));
//            customersAllInfo.setLegalType(dictionaryService.transalteLike("法人类型",amsAccountInfo.getLegalType()));
            customerPublicLogInfo.setLegalType(dictionaryService.transalteLike("法人类型",amsAccountInfo.getLegalType()));
        }
        //法人证件类型
        if(StringUtils.isNotBlank(amsAccountInfo.getLegalIdcardType())){
//            accountBillsAll.setLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getLegalIdcardType()));
//            accountsAllInfo.setLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getLegalIdcardType()));
//            accountPublicInfo.setLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getLegalIdcardType()));
            customerPublicInfo.setLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getLegalIdcardType()));
//            customersAllInfo.setLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getLegalIdcardType()));
            customerPublicLogInfo.setLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getLegalIdcardType()));
        }
        //币种
        if(StringUtils.isNotBlank(amsAccountInfo.getRegCurrencyType())){
//            accountBillsAll.setRegCurrencyType(dictionaryService.transalteLike("注册币种",amsAccountInfo.getRegCurrencyType()));
//            accountsAllInfo.setRegCurrencyType(dictionaryService.transalteLike("注册币种",amsAccountInfo.getRegCurrencyType()));
//            accountPublicInfo.setRegCurrencyType(dictionaryService.transalteLike("注册币种",amsAccountInfo.getRegCurrencyType()));
            customerPublicInfo.setRegCurrencyType(dictionaryService.transalteLike("注册币种",amsAccountInfo.getRegCurrencyType()));
//            customersAllInfo.setRegCurrencyType(dictionaryService.transalteLike("注册币种",amsAccountInfo.getRegCurrencyType()));
            customerPublicLogInfo.setRegCurrencyType(dictionaryService.transalteLike("注册币种",amsAccountInfo.getRegCurrencyType()));
        }
        //资金转换BigDecimal
        if(StringUtils.isNotBlank(amsAccountInfo.getRegisteredCapital())){
//            accountBillsAll.setRegisteredCapital(new BigDecimal(amsAccountInfo.getRegisteredCapital()));
//            accountsAllInfo.setRegisteredCapital(new BigDecimal(amsAccountInfo.getRegisteredCapital()));
//            accountPublicInfo.setRegisteredCapital(new BigDecimal(amsAccountInfo.getRegisteredCapital()));
            customerPublicInfo.setRegisteredCapital(new BigDecimal(amsAccountInfo.getRegisteredCapital()));
//            customersAllInfo.setRegisteredCapital(new BigDecimal(amsAccountInfo.getRegisteredCapital()));
            customerPublicLogInfo.setRegisteredCapital(new BigDecimal(amsAccountInfo.getRegisteredCapital()));
        }
        //行业归属
        if(StringUtils.isNotBlank(amsAccountInfo.getIndustryCode())){
//            accountBillsAll.setIndustryCode(dictionaryService.transalteLike("行业归属",amsAccountInfo.getIndustryCode()));
//            accountsAllInfo.setIndustryCode(dictionaryService.transalteLike("行业归属",amsAccountInfo.getIndustryCode()));
//            accountPublicInfo.setIndustryCode(dictionaryService.transalteLike("行业归属",amsAccountInfo.getIndustryCode()));
            customerPublicInfo.setIndustryCode(dictionaryService.transalteLike("行业归属",amsAccountInfo.getIndustryCode()));
//            customersAllInfo.setIndustryCode(dictionaryService.transalteLike("行业归属",amsAccountInfo.getIndustryCode()));
            customerPublicLogInfo.setIndustryCode(dictionaryService.transalteLike("行业归属",amsAccountInfo.getIndustryCode()));
        }
        //邮政编码
        if(StringUtils.isNotBlank(amsAccountInfo.getZipCode())){
//            accountBillsAll.setZipcode(amsAccountInfo.getZipCode());
//            accountsAllInfo.setZipcode(amsAccountInfo.getZipCode());
//            accountPublicInfo.setZipcode(amsAccountInfo.getZipCode());
            customerPublicInfo.setZipcode(amsAccountInfo.getZipCode());
//            customersAllInfo.setZipcode(amsAccountInfo.getZipCode());
            customerPublicLogInfo.setZipcode(amsAccountInfo.getZipCode());
        }
        //上级法人证件类型
        if(StringUtils.isNotBlank(amsAccountInfo.getParLegalIdcardType())){
//            accountBillsAll.setParLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getParLegalIdcardType()));
//            accountsAllInfo.setParLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getParLegalIdcardType()));
//            accountPublicInfo.setParLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getParLegalIdcardType()));
            customerPublicInfo.setParLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getParLegalIdcardType()));
//            customersAllInfo.setParLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getParLegalIdcardType()));
            customerPublicLogInfo.setParLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getParLegalIdcardType()));
        }
    }

    //基本户证明文件种类赋值
    private void updateJibenFile(AmsAccountInfo amsAccountInfo,AccountBillsAll accountBillsAll,AccountsAllInfo accountsAllInfo,AccountPublicInfo accountPublicInfo,CustomerPublicInfo customerPublicInfo,CustomersAllInfo customersAllInfo,CustomerPublicLogInfo customerPublicLogInfo){
        if(StringUtils.isNotBlank(amsAccountInfo.getFileType())){
//            accountBillsAll.setFileType(dictionaryService.transalteLike("证明文件1种类(基本户)",amsAccountInfo.getFileType()));
//            accountsAllInfo.setFileType(dictionaryService.transalteLike("证明文件1种类(基本户)",amsAccountInfo.getFileType()));
//            accountPublicInfo.setFileType(dictionaryService.transalteLike("证明文件1种类(基本户)",amsAccountInfo.getFileType()));
            customerPublicInfo.setFileType(dictionaryService.transalteLike("证明文件1种类(基本户)",amsAccountInfo.getFileType()));
//            customersAllInfo.setFileType(dictionaryService.transalteLike("证明文件1种类(基本户)",amsAccountInfo.getFileType()));
            customerPublicLogInfo.setFileType(dictionaryService.transalteLike("证明文件1种类(基本户)",amsAccountInfo.getFileType()));
        }
        if(StringUtils.isNotBlank(amsAccountInfo.getFileType2())){
//            accountBillsAll.setFileType2(dictionaryService.transalteLike("证明文件2种类(基本户)",amsAccountInfo.getFileType2()));
//            accountsAllInfo.setFileType2(dictionaryService.transalteLike("证明文件2种类(基本户)",amsAccountInfo.getFileType2()));
//            accountPublicInfo.setFileType2(dictionaryService.transalteLike("证明文件2种类(基本户)",amsAccountInfo.getFileType2()));
            customerPublicInfo.setFileType2(dictionaryService.transalteLike("证明文件2种类(基本户)",amsAccountInfo.getFileType2()));
//            customersAllInfo.setFileType2(dictionaryService.transalteLike("证明文件2种类(基本户)",amsAccountInfo.getFileType2()));
            customerPublicLogInfo.setFileType2(dictionaryService.transalteLike("证明文件2种类(基本户)",amsAccountInfo.getFileType2()));
        }
        //存款人类型
        if(StringUtils.isNotBlank(amsAccountInfo.getDepositorType())){
            accountBillsAll.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)",amsAccountInfo.getDepositorType()));
//            accountsAllInfo.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)",amsAccountInfo.getDepositorType()));
            accountPublicInfo.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)",amsAccountInfo.getDepositorType()));
//            customerPublicInfo.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)",amsAccountInfo.getDepositorType()));
//            customersAllInfo.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)",amsAccountInfo.getDepositorType()));
//            customerPublicLogInfo.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)",amsAccountInfo.getDepositorType()));
        }
    }

    private boolean isExecute(String time){
        try {
            if(StringUtils.isNotBlank(time)){
                int week = DateUtils.getWeekOfDate(new Date());
                boolean isJob=isJob(week);
                //String[] array = time.split(",");

                if(isJob){
                    //工作时间的执行时间
                    boolean isFirst= isEx(time);
                    return isFirst;
                }else{
                   /* //当前日期是周6或者周日，配置文件没有配置周末的执行时间，默认24小时执行
                    if((array.length==1 || (array.length>=2 && StringUtils.isBlank(array[1])))){
                        return true;
                    }
                    //当前日期是周6或者周日，配置文件配置周末的执行时间,在配置时间内执行
                    if(array.length>=2 && StringUtils.isNotBlank(array[1])){
                        boolean isFirst= isEx(array[1]);
                        return isFirst;
                    }*/
                   //周末24小时都执行
                    return true;
                }
            }else{
                log.info("时间段配置为空，不执行");
                return false;
            }
        }catch (Exception e){
            log.error("判断人行覆盖核心数据执行时间异常：{}",e);
        }
        return false;
    }

    /**
     *  是否是工作时间
     * @return
     */
    private boolean isJob(int w){
        if(w==1 || w==7){
            return false;
        }else{
            return true;
        }
    }
    private boolean isEx(String datestr){
        //SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        try {
            String year = DateUtils.getNowDateShort();
            String[] DateStr= datestr.split("-");
            if(StringUtils.isBlank(DateStr[0]) || StringUtils.isBlank(DateStr[1])){
                log.info("时间配置格式错误：{}",datestr);
                return false;
            }
            Date startDate = DateUtils.parse(year +" "+ DateStr[0],"yyyy-MM-dd HH:mm");
            Date endDate = DateUtils.parse(year +" "+ DateStr[1],"yyyy-MM-dd HH:mm");
            boolean isFirst= DateUtil.isBetween(new Date(),startDate,endDate);
            return !isFirst;
        }catch (Exception e){
            log.error("时间比较异常：",e);
            log.info("时间段配置：{}",datestr);
        }
        return false;
    }
}
