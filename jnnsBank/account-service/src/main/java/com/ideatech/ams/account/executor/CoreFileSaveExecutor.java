package com.ideatech.ams.account.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dao.core.CorePublicAccountDao;
import com.ideatech.ams.account.dao.core.CorePublicAccountErrorDao;
import com.ideatech.ams.account.dao.core.CorePublicAccountFinishDao;
import com.ideatech.ams.account.entity.CorePublicAccount;
import com.ideatech.ams.account.entity.CorePublicAccountError;
import com.ideatech.ams.account.entity.CorePublicAccountFinish;
import com.ideatech.ams.account.service.core.AmsCoreAccountService;
import com.ideatech.ams.account.service.core.DefaultCoreDataImportListener;
import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.ams.pbc.utils.AtomicLongUtils;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.BeanValueUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @Description 核心初始化文件导入的多线程处理
 * @Author wanghongjie
 * @Date 2018/9/17
 **/
@Data
@Slf4j
public class CoreFileSaveExecutor implements Callable{

    private Set<String> organSet;

    private Set<String> tokens;

    private String batch;

    private AmsCoreAccountService amsCoreAccountService;

    private OrganizationService organizationService;

    private PbcAmsService pbcAmsService;

    private String lineEndPrefixNative;

    private String splitNative;

    private String[] methodNames;

    private CorePublicAccountErrorDao corePublicAccountErrorDao;

    private CorePublicAccountDao corePublicAccountDao;

    private CorePublicAccountFinishDao corePublicAccountFinishDao;

    private PlatformTransactionManager transactionManager;

    private Map<String, Integer> fieldLengthForStringMap;

    private boolean pbcCoverCore;

    private TransactionUtils transactionUtils;

    private DictionaryService dictionaryService;

    //是否启用人行采集限制机制
    private Boolean pbcCollectionLimitUse;

    //人行采集数量控制
    private Long pbcCollectionLimitNum;

    @Override
    public Object call() throws Exception {
        int size = tokens.size();
       if(size>0){
           CorePublicAccountError corePublicAccountError = new CorePublicAccountError();
//           TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//           TransactionStatus transaction = transactionManager.getTransaction(definition);
           int index=0;
           log.info("{}-开始处理导入文件，需要导入的数量为" +size + "条账户",batch);
           for(String token : tokens){
               if(StringUtils.isNotBlank(token)){
                   log.info("当前正在处理：" + token);
                   if(lineEndPrefixNative != null && lineEndPrefixNative.length()>0 && StringUtils.endsWith(token,lineEndPrefixNative)){
                       token = StringUtils.removeEnd(token,lineEndPrefixNative);
                   }
                   String[] strs = StringUtils.splitByWholeSeparatorPreserveAllTokens(token, splitNative);
                   CorePublicAccount corePublicAccount  = new CorePublicAccount();
                   HashMap<String, Integer> inValidLengthForString = amsCoreAccountService.arrayToCorePublicAccount(methodNames, strs, corePublicAccount, fieldLengthForStringMap);
                   AmsAccountInfo amsAccountInfo = null;
                   AmsAccountInfo revokeAmsAccountInfo = null;
                   OrganizationDto orgDto = null;
//                   if (pbcCoverCore) {
//                       try {
//                           if (StringUtils.isNotBlank(corePublicAccount.getOrganFullId())) {
//                               //这里的参数corePublicAccount.getOrganFullId()实际为organCode值
//                               orgDto = organizationService.findByCode(corePublicAccount.getOrganFullId());
//                               if (orgDto != null) {
//                                   amsAccountInfo = getAmsDetails(orgDto.getCode(),corePublicAccount.getAcctNo());
////                                   amsAccountInfo = JSON.parseObject("{\"accountKey\":\"L1260000181101\",\"accountLicenseNo\":\"L1260000181101\",\"accountStatus\":\"normal\",\"acctCreateDate\":\"2019-04-28\",\"acctName\":\"河北森野园林工程有限公司承秦高速秦皇岛段ＬＨ２合同项目经理部\",\"acctNo\":\"6600120100000167503\",\"acctType\":\"feilinshi\",\"bankCode\":\"313126066016\",\"bankName\":\"沧州银行股份有限公司秦皇岛分行\",\"businessScope\":\"园林绿化工程的设计与施工，屋顶绿化工程施工。\",\"cancelDate\":\"-\",\"depositorName\":\"河北森野园林工程有限公司\",\"depositorType\":\"企业法人\",\"fileNo\":\"91130100685706283R\",\"fileNo2\":\"\",\"fileType\":\"工商营业执照\",\"fileType2\":\"\",\"industryCode\":\"水利、环境和公共设施管理业\",\"legalIdcardNo\":\"132337197612310010\",\"legalIdcardType\":\"身份证\",\"legalName\":\"王胜军\",\"noTaxProve\":\"\",\"orgCode\":\"685706283\",\"parAccountKey\":\"\",\"parCorpName\":\"\",\"parLegalIdcardNo\":\"\",\"parLegalIdcardType\":\"\",\"parLegalName\":\"\",\"parOrgCode\":\"\",\"regAddress\":\"石家庄市长安区中山东路11号乐汇城1-2-1220\",\"regAreaCode\":\"121000\",\"regCurrencyType\":\"人民币\",\"registeredCapital\":\"10500000.00\",\"stateTaxRegNo\":\"91130100685706283R\",\"taxRegNo\":\"91130100685706283R\",\"telephone\":\"13832158669\",\"zipCode\":\"050000\"}",AmsAccountInfo.class);
//                                   //通过销户接口回去法定代表人信息
//                                   if(amsAccountInfo != null && StringUtils.isBlank(amsAccountInfo.getLegalType())){
//                                       String[] cancelHeZhunType = {"企业法人", "非法人企业", "有字号的个体工商户", "无字号的个体工商户", "01", "02", "13", "14"};
//                                       //包含说明是取消核准的账户   在不判断该地区是不是已经上取消核准的前提下
//                                       try{
//                                           if(Arrays.asList(cancelHeZhunType).contains(amsAccountInfo.getDepositorType())){
//                                               revokeAmsAccountInfo = getRevokeAmsDetails(orgDto,corePublicAccount.getAcctNo(),amsAccountInfo.getAcctType().toString());
//                                           }
//                                       }catch (Exception e){
//                                           if(e.getMessage().indexOf("核准企业类存款账户，请到人民银行办理撤销业务") > -1){
//                                               log.info("该账户不是取消核准账户或该地区不是取消核准地区");
//                                           }
//                                       }
//                                   }
//                                   if (amsAccountInfo != null && StringUtils.isNotBlank(amsAccountInfo.getDepositorName())) {
//                                       //获取销户接口的法定代表人信息
//                                       if(revokeAmsAccountInfo != null && StringUtils.isNotBlank(revokeAmsAccountInfo.getDepositorName())){
//                                           amsAccountInfo.setLegalType(revokeAmsAccountInfo.getLegalType());
//                                       }
//                                       log.info(JSON.toJSONString(amsAccountInfo));
//                                       BeanValueUtils.copyPropertiesForEmpty(amsAccountInfo, corePublicAccount);//人行数据补全核心数据为null或为""的值
//                                       setFileType(amsAccountInfo,corePublicAccount);
//                                       log.info("账号为{}，人行数据覆盖核心数据成功", corePublicAccount.getAcctNo());
//                                   } else {
//                                       log.info("账号为{}，未查询到人行数据，跳过人行数据覆盖核心数据操作", corePublicAccount.getAcctNo());
//                                   }
//                               } else {
//                                   log.info("{}错误的organFullId，跳过人行数据覆盖核心数据操作", token);
//                                   saveCorePublicErrorAccount(corePublicAccount,token,"organFullId对用机构未找到，跳过人行数据覆盖核心数据操作");
//                                   continue;
//                               }
//                           } else {
//                               log.info("{}未找到organFullId数据，跳过人行数据覆盖核心数据操作", token);
//                               saveCorePublicErrorAccount(corePublicAccount,token,"organFullId为空，跳过人行数据覆盖核心数据操作");
//                               continue;
//                           }
//                       } catch (Exception e) {
//                           log.error("人行登录查询异常：", e);
//                           log.error("{}-人行登录查询异常，账号为{}，organFullId为{}", batch, corePublicAccount.getAcctNo(), corePublicAccount.getOrganFullId());
//                           //保存error表
//                           saveCorePublicErrorAccount(corePublicAccount,token,e.getMessage());
//                           continue;
//                       }
//                   }
                   if (corePublicAccount != null) {
//                       if (index > 0 && index % 20 == 0) {
//                           try{
//                               log.debug("{}-已经核心数据初始化处理" +index + "条账户",batch);
//                               if(index % 1000 ==0){
//                                   log.info("{}-已经核心数据初始化处理" +index + "条账户",batch);
//                               }
//                               transactionManager.commit(transaction);
//                           }catch(Exception e){
//                               log.error(batch+"-批量提交出错",e);
//                           }finally {
//                               transaction = transactionManager.getTransaction(definition);
//                           }
//                       }
                       try{
                           String dateStr = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd");
                           corePublicAccount.setDataDate(dateStr);

                           // 赋值给Error Bean
                           corePublicAccountError = new CorePublicAccountError();
                           BeanCopierUtils.copyProperties(corePublicAccount, corePublicAccountError);
                           corePublicAccountError.setId(null);
                           corePublicAccountError.setErrorToken(token);

                           if(inValidLengthForString.size() >0){
                               checkElementValid(corePublicAccountError,inValidLengthForString);
                               log.debug("未通过元素校验--" + corePublicAccountError.toString());
                               continue;
                           }

                           if (!checkRequiredValue(corePublicAccount, corePublicAccountError)) {
                               log.debug("未通过必填项验证--" + corePublicAccountError.toString());
                               continue;
                           }

                           if (!checkOrganAndAcctNo(corePublicAccount, corePublicAccountError, organSet)) {
                               log.debug("未通过必填项验证--" + corePublicAccountError.toString());
                               continue;
                           }

//                           if (!checkElementValid(corePublicAccountError)) {
//                               log.debug("未通过元素校验--" + corePublicAccountError.toString());
//                               continue;
//                           }
                           corePublicAccount.setHandleStatus(CompanyIfType.No);
                           corePublicAccountDao.save(corePublicAccount);
                       }catch(Exception e){
                           log.error("核心数据初始化数据" + token + "处理异常", e);
//                           transactionManager.rollback(transaction);
//                           transaction = transactionManager.getTransaction(definition);
                       }finally {
                           index++;
                       }
                   }
               }
           }
//           transactionManager.commit(transaction);
           log.info("{}-完成处理导入文件，导入的数量为" +index + "条账户",batch);
       }
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
//            String json = "{\"accountKey\":\"L3910000861601\",\"accountLicenseNo\":\"L3910000861601\",\"accountStatus\":\"normal\",\"acctCreateDate\":\"2017-03-10\",\"acctName\":\"福州市推进新型城镇化工作联席会议办公室\",\"acctNo\":\"100055580090010001\",\"acctType\":\"linshi\",\"bankCode\":\"313391080015\",\"bankName\":\"福建海峡银行股份有限公司营业部\",\"businessScope\":\"统筹推进全市新型城镇化规划实施等\",\"cancelDate\":\"-\",\"depositorName\":\"福州市推进新型城镇化工作联席会议办公室\",\"depositorType\":\"临时机构\",\"fileNo\":\"榕委办[2017]7号\",\"fileNo2\":\"\",\"fileType\":\"主管部门批文\",\"fileType2\":\"\",\"industryCode\":\"公共管理和社会组织\",\"legalIdcardNo\":\"350102196702240419\",\"legalIdcardType\":\"身份证\",\"legalName\":\"游通铃\",\"noTaxProve\":\"\",\"orgCode\":\"\",\"parAccountKey\":\"J3910002129107\",\"parCorpName\":\"福州市发展和改革委员会\",\"parLegalIdcardNo\":\"350102196702240419\",\"parLegalIdcardType\":\"身份证\",\"parLegalName\":\"游通铃\",\"parOrgCode\":\"\",\"regAddress\":\"福建省福州市仓山区南江滨西大道193号东部办公区1座12层\",\"regAreaCode\":\"391000\",\"regCurrencyType\":\"\",\"registeredCapital\":\"\",\"stateTaxRegNo\":\"\",\"taxRegNo\":\"\",\"telephone\":\"87111675\",\"zipCode\":\"350000\"}";
//            JSONObject jsonObject = JSONObject.parseObject(json);
//            amsAccountInfo = JSON.toJavaObject(jsonObject,AmsAccountInfo.class);
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
     * 人行调用查询详细信息  如果人行关闭睡眠15分钟，死循环查询人行
     *
     */
    public AmsAccountInfo getRevokeAmsDetails(OrganizationDto organizationDto,String acctNo,String acctType) throws Exception{
        AmsAccountInfo amsAccountInfo = null;
        try {
            if(pbcCollectionLimitUse && !AtomicLongUtils.isPause(pbcCollectionLimitNum)){
                log.info("方法：线程人行查询数量：" + AtomicLongUtils.al.getAndIncrement());
                amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNoAndAcctTypeToRevoke(organizationDto,acctNo,acctType);//获取销户详细数据
            }else{
                amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNoAndAcctTypeToRevoke(organizationDto,acctNo,acctType);//获取销户详细数据
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
     *@Description: 校验必填项
     *@Param: [corePublicAccount, corePublicAccountError]
     *@return: boolean
     *@Author: wanghongjie
     *@date: 2018/9/16
     */
    public boolean checkRequiredValue(CorePublicAccount corePublicAccount,CorePublicAccountError corePublicAccountError){
        if(StringUtils.isBlank(corePublicAccount.getAcctNo())){
            saveErrorAccount(corePublicAccountError, "账号不能为空");
            return false;
        }else if(StringUtils.isBlank(corePublicAccount.getCustomerNo())){
            saveErrorAccount(corePublicAccountError, "客户号不能为空");
            return false;
        }else if(StringUtils.isBlank(corePublicAccount.getDepositorName())){
            saveErrorAccount(corePublicAccountError, "存款人名称不能为空");
            return false;
        }else if(StringUtils.isBlank(corePublicAccount.getOrganFullId())){
            saveErrorAccount(corePublicAccountError, "核心机构的代码不能为空");
            return false;
        }else if(StringUtils.isBlank(corePublicAccount.getAcctBigType())){
            saveErrorAccount(corePublicAccountError, "账户性质大类不能为空");
            return false;
        }else{
            return true;
        }
    }


    /**
     *@Description: 判断机构号是否存在、账号是否重复
     *@Param: [corePublicAccount, corePublicAccountError, organSet, acctNoSet]
     *@return: boolean
     *@Author: wanghongjie
     *@date: 2018/9/16
     */
    private boolean checkOrganAndAcctNo(CorePublicAccount corePublicAccount,CorePublicAccountError corePublicAccountError,Set<String> organSet){
        String acctNo = corePublicAccount.getAcctNo();
        String organFullId = corePublicAccount.getOrganFullId();
        if(!organSet.contains(organFullId)){
            saveErrorAccount(corePublicAccountError, "无法找到对应的核心机构代码");
            return false;
        }else{
            if(DefaultCoreDataImportListener.coreFileMaps.contains(acctNo)){//已经含有该账号
                saveErrorAccount(corePublicAccountError, "该账号已存在，无法重复导入");
                return false;
            }else{
                DefaultCoreDataImportListener.coreFileMaps.putIfAbsent(acctNo,acctNo);
                return true;
            }
        }
    }

    /**
     * 字段长度有误，保存CorePublicAccountError表里
     * @param corePublicAccountError
     * @param inValidLengthForString
     */
    private void checkElementValid(CorePublicAccountError corePublicAccountError,HashMap<String, Integer> inValidLengthForString){
        Iterator<Map.Entry<String, Integer>> iterator = inValidLengthForString.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        String sbStr;
        while(iterator.hasNext()){
            Map.Entry<String, Integer> next = iterator.next();
            String key = next.getKey();
            Integer value = next.getValue();
            sb.append("该账号的"+key+"字段过长,超过"+value+"个字符；");
            if(StringUtils.length(sb.toString()) > 2000){
                sbStr = StringUtils.substring(sb.toString(),0,2000);
                break;
            }
        }
        sbStr = sb.toString();
        saveErrorAccount(corePublicAccountError, sbStr);
    }


    private void saveErrorAccount(CorePublicAccountError corePublicAccountError, String errorMsg) {
        corePublicAccountError.setErrorReason(errorMsg);
        corePublicAccountErrorDao.save(corePublicAccountError);
    }

    private void saveCorePublicErrorAccount(CorePublicAccount corePublicAccount,String token, String errorMsg) {
        CorePublicAccountError corePublicAccountError = new CorePublicAccountError();
        BeanCopierUtils.copyProperties(corePublicAccount, corePublicAccountError);
        corePublicAccountError.setId(null);
        corePublicAccountError.setErrorToken(token);
        saveErrorAccount(corePublicAccountError, errorMsg);
    }

    private void saveFinishAccount(CorePublicAccountFinish corePublicAccountFinish){
        corePublicAccountFinishDao.save(corePublicAccountFinish);
    }

    private void setFileType(AmsAccountInfo amsAccountInfo,CorePublicAccount corePublicAccount){

        //设置账户状态
        corePublicAccount.setAccountStatus(amsAccountInfo.getAccountStatus().toString());

        //通用部分赋值
        seturrencyValue(amsAccountInfo,corePublicAccount);

        //根据账户类型赋值
        if(amsAccountInfo.getAcctType() == AccountType.jiben){
            getJibenFile(amsAccountInfo,corePublicAccount);
            //存款人类型
            if(StringUtils.isNotBlank(amsAccountInfo.getDepositorType())){
                corePublicAccount.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)",amsAccountInfo.getDepositorType()));
            }
        }
        if(amsAccountInfo.getAcctType() == AccountType.linshi){
            if(StringUtils.isNotBlank(amsAccountInfo.getFileType())){
                corePublicAccount.setFileType(dictionaryService.transalteLike("证明文件1种类(临时户)",amsAccountInfo.getFileType()));
            }
            //临时户账户许可证号
            if(StringUtils.isNotBlank(amsAccountInfo.getAccountLicenseNo())){
                corePublicAccount.setAccountLicenseNo(amsAccountInfo.getAccountLicenseNo());
            }
        }
        if(amsAccountInfo.getAcctType() == AccountType.teshu){
            if(StringUtils.isNotBlank(amsAccountInfo.getFileType())){
                corePublicAccount.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(特殊户)",amsAccountInfo.getAccountFileType()));
            }
        }
        if(amsAccountInfo.getAcctType() == AccountType.yiban){
            if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileType())){
                corePublicAccount.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(一般户)",amsAccountInfo.getAccountFileType()));
            }
            //基本户种类赋值
            getJibenFile(amsAccountInfo,corePublicAccount);
        }
        if(amsAccountInfo.getAcctType() == AccountType.feiyusuan || amsAccountInfo.getAcctType() == AccountType.yusuan){
            if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileType())){
                corePublicAccount.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(专用户)",amsAccountInfo.getAccountFileType()));
            }
            if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileType2())){
                corePublicAccount.setAcctFileType2(dictionaryService.transalteLike("证明文件2种类(专用户)",amsAccountInfo.getAccountFileType2()));
            }
            if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileNo())){
                corePublicAccount.setAcctFileNo(amsAccountInfo.getAccountFileNo());
            }
            if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileNo2())){
                corePublicAccount.setAcctFileNo2(amsAccountInfo.getAccountFileNo2());
            }
            //基本户证明文件种类赋值
            getJibenFile(amsAccountInfo,corePublicAccount);
        }
        if(amsAccountInfo.getAcctType() == AccountType.feilinshi){
            if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileType())){
                corePublicAccount.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(非临时)",amsAccountInfo.getAccountFileType()));
            }
            //基本户证明文件种类赋值
            getJibenFile(amsAccountInfo,corePublicAccount);
        }
    }

    private void seturrencyValue(AmsAccountInfo amsAccountInfo,CorePublicAccount corePublicAccount){
        //法定代表人
        if(StringUtils.isNotBlank(amsAccountInfo.getLegalType())){
            corePublicAccount.setLegalType(dictionaryService.transalteLike("法人类型",amsAccountInfo.getLegalType()));
        }
        //法人证件类型
        if(StringUtils.isNotBlank(amsAccountInfo.getLegalIdcardType())){
            corePublicAccount.setLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getLegalIdcardType()));
        }
        //币种
        if(StringUtils.isNotBlank(amsAccountInfo.getRegCurrencyType())){
            corePublicAccount.setRegCurrencyType(dictionaryService.transalteLike("注册币种",amsAccountInfo.getRegCurrencyType()));
        }
        //资金转换BigDecimal
        if(StringUtils.isNotBlank(amsAccountInfo.getRegisteredCapital())){
            corePublicAccount.setRegisteredCapital(new BigDecimal(amsAccountInfo.getRegisteredCapital()));
        }
        //行业归属
        if(StringUtils.isNotBlank(amsAccountInfo.getIndustryCode())){
            corePublicAccount.setIndustryCode(dictionaryService.transalteLike("行业归属",amsAccountInfo.getIndustryCode()));
        }
        //邮政编码
        if(StringUtils.isNotBlank(amsAccountInfo.getZipCode())){
            corePublicAccount.setZipcode(amsAccountInfo.getZipCode());
        }
        //账户名称构成方式
        if(StringUtils.isNotBlank(amsAccountInfo.getAccountNameFrom())){
            corePublicAccount.setAccountNameFrom(dictionaryService.transalteLike("账户构成方式",amsAccountInfo.getAccountNameFrom()));
        }
        //资金性质
        if(StringUtils.isNotBlank(amsAccountInfo.getCapitalProperty())){
            corePublicAccount.setCapitalProperty(dictionaryService.transalteLike("资金性质",amsAccountInfo.getCapitalProperty()));
        }
        //上级法人证件类型
        if(StringUtils.isNotBlank(amsAccountInfo.getParLegalIdcardType())){
            corePublicAccount.setParLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getParLegalIdcardType()));
        }
    }

    private void getJibenFile(AmsAccountInfo amsAccountInfo,CorePublicAccount corePublicAccount){
        if(StringUtils.isNotBlank(amsAccountInfo.getFileType())){
            corePublicAccount.setFileType(dictionaryService.transalteLike("证明文件1种类(基本户)",amsAccountInfo.getFileType()));
        }
        if(StringUtils.isNotBlank(amsAccountInfo.getFileType2())){
            corePublicAccount.setFileType2(dictionaryService.transalteLike("证明文件2种类(基本户)",amsAccountInfo.getFileType2()));
        }
        //存款人类型
        if(StringUtils.isNotBlank(amsAccountInfo.getDepositorType())){
            corePublicAccount.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)",amsAccountInfo.getDepositorType()));
        }
    }
}
