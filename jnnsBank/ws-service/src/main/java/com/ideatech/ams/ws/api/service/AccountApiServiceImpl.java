package com.ideatech.ams.ws.api.service;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.dto.AccountPublicLogInfo;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.account.service.AccountChangeSummaryService;
import com.ideatech.ams.account.service.AccountPublicLogService;
import com.ideatech.ams.account.service.AccountPublicService;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.core.Map2DomainService;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.dto.CustomerPublicMidInfo;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.customer.service.CustomerPublicMidService;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.util.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class AccountApiServiceImpl implements AccountApiService {

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private AccountPublicService accountPublicService;

    @Autowired
    private CustomerPublicLogService customerPublicLogService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private AmsMainService amsMainService;

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private Map2DomainService map2DomainService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountChangeSummaryService accountChangeSummaryService;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private CustomerPublicMidService customerPublicMidService;

    @Autowired
    private AccountPublicLogService accountPublicLogService;

    @Autowired
    private PbcAmsService pbcAmsService;

    @Autowired
    private OrganizationService organizationService;

    /**
     * 根据流水号获得本地客户账户信息
     * @param billNo
     * @return
     */
    @Override
    public ObjectRestResponse<AllBillsPublicDTO> getAccountInfo(String billNo) {
        if(StringUtils.isBlank(billNo)) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("流水号为空");
        }

        AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getByBillNo(billNo);
        if(accountBillsAllInfo == null) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("本地流水数据为空");
        }

        Long billId = accountBillsAllInfo.getId();
        AllBillsPublicDTO allBillsPublicDTO = allBillsPublicService.findOne(billId);

        if(allBillsPublicDTO != null) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(true).result(allBillsPublicDTO);
        }

        return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("本地账户数据为空");
    }

    /**
     * 根据流水号更新本地客户号和账号
     * @param billNo
     * @param acctNo
     * @param customerNo
     * @return
     */
    @Override
    public ObjectRestResponse<AllBillsPublicDTO> updateBillsInfo(String billNo, String acctNo, String customerNo) {
        if(StringUtils.isBlank(billNo)) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("流水号不能为空");
        }
        if(StringUtils.isBlank(acctNo)) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("账号不能为空");
        }
        if(StringUtils.isBlank(customerNo)) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("客户号不能为空");
        }

        //校验
        AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getByBillNo(billNo);
        if(accountBillsAllInfo == null) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("本地流水数据为空");
        }

        Boolean isRepeatAcctNo = accountBillsAllService.isCheckAcctNoAndCustomerNo(acctNo, customerNo, billNo);
        if(!isRepeatAcctNo) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("其他账户信息包含此账号或客户号,无法变更");
        }

        if((accountBillsAllInfo.getBillType() != BillType.ACCT_INIT && accountBillsAllInfo.getBillType() != BillType.ACCT_OPEN)
                || (accountBillsAllInfo.getBillType() == BillType.ACCT_OPEN && (accountBillsAllInfo.getPbcSyncStatus() != CompanySyncStatus.weiTongBu
                || (accountBillsAllInfo.getEccsSyncStatus() != CompanySyncStatus.buTongBu && accountBillsAllInfo.getEccsSyncStatus() != CompanySyncStatus.weiTongBu)))) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("账户信息已上报过人行,无法变更");
        }

        //更新表字段
        accountBillsAllInfo.setAcctNo(acctNo);
        accountBillsAllInfo.setCustomerNo(customerNo);
        accountBillsAllService.save(accountBillsAllInfo);

        List<AccountPublicLogInfo> accountPublicLogInfoList = accountPublicLogService.findByAccountId(accountBillsAllInfo.getAccountId());
        if(CollectionUtils.isNotEmpty(accountPublicLogInfoList)){
            AccountPublicLogInfo accountPublicLogInfo = accountPublicLogInfoList.get(0);
            accountPublicLogInfo.setAcctNo(acctNo);
            accountPublicLogInfo.setCustomerNo(customerNo);
            accountPublicLogService.save(accountPublicLogInfo);
        }


        AccountsAllInfo accountsAllInfo = accountsAllService.getOne(accountBillsAllInfo.getAccountId());
        if(accountsAllInfo != null) {
            accountsAllInfo.setAcctNo(acctNo);
            accountsAllInfo.setCustomerNo(customerNo);
            accountsAllService.save(accountsAllInfo);
        }

        return new ObjectRestResponse<AllBillsPublicDTO>().rel(true).result(accountsAllInfo).msg("更新客户号和账号成功");

    }

    /**
     * 根据流水号更新本地账号
     * @param billNo
     * @param acctNo
     * @return
     */
    @Override
    public ObjectRestResponse<AllBillsPublicDTO> updateBillsAccount(String billNo, String acctNo) {
        if(StringUtils.isBlank(billNo)) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("流水号不能为空");
        }
        if(StringUtils.isBlank(acctNo)) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("账号不能为空");
        }

        //校验
        AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getByBillNo(billNo);
        if(accountBillsAllInfo == null) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("本地流水数据为空");
        }

        Long count = accountBillsAllService.isCheckBillNoAndAcctNo(billNo, acctNo);
        if(count == 1) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("流水信息的账号与变更账号一致,无需变更");
        } else if(count > 1) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("流水号和账号对应多条流水信息,无法变更");
        }

        if((accountBillsAllInfo.getBillType() != BillType.ACCT_INIT && accountBillsAllInfo.getBillType() != BillType.ACCT_OPEN)
                || (accountBillsAllInfo.getBillType() == BillType.ACCT_OPEN && (accountBillsAllInfo.getPbcSyncStatus() != CompanySyncStatus.weiTongBu
                || (accountBillsAllInfo.getEccsSyncStatus() != CompanySyncStatus.buTongBu && accountBillsAllInfo.getEccsSyncStatus() != CompanySyncStatus.weiTongBu)))) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("账户信息已上报过人行,无法变更");
        }

        //更新表字段
        accountBillsAllInfo.setAcctNo(acctNo);
        accountBillsAllService.save(accountBillsAllInfo);

        List<AccountPublicLogInfo> accountPublicLogInfoList = accountPublicLogService.findByAccountId(accountBillsAllInfo.getAccountId());
        if(CollectionUtils.isNotEmpty(accountPublicLogInfoList)){
            AccountPublicLogInfo accountPublicLogInfo = accountPublicLogInfoList.get(0);
            accountPublicLogInfo.setAcctNo(acctNo);
            accountPublicLogService.save(accountPublicLogInfo);
        }


        AccountsAllInfo accountsAllInfo = accountsAllService.getOne(accountBillsAllInfo.getAccountId());
        if(accountsAllInfo != null) {
            accountsAllInfo.setAcctNo(acctNo);
            accountsAllService.save(accountsAllInfo);
        }

        return new ObjectRestResponse<AllBillsPublicDTO>().rel(true).result(accountsAllInfo).msg("更新账号成功");

    }

    /**
     * 保存变更流水
     * @param organCode
     * @param acctNo
     * @param acctType
     * @param billsPublic
     * @return
     */
    @Override
    public ResultDto saveChangeBills(String organCode, String acctNo, CompanyAcctType acctType, AllBillsPublicDTO billsPublic) {
        if(StringUtils.isBlank(organCode) || StringUtils.isBlank(acctNo) || acctType == null) {
            return ResultDtoFactory.toApiError(ResultCode.PARAM_NOT_COMPLETE.code(), ResultCode.PARAM_NOT_COMPLETE.message(), null);
        }
        if(billsPublic == null) {
            return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), ResultCode.PARAM_IS_BLANK.message(), null);
        }

        ResultDto resultDto = null;
        String code = "";
        String msg = "";
        AllBillsPublicDTO allBillsPublicDTO = new AllBillsPublicDTO();
        Map<String, String> oldMap = null;
        Map<String, String> newMap = null;
        Map<String, Object> beforeChangeFieldValueMap = new HashMap<>();
        Map<String, Object> afterChangeFieldValueMap = new HashMap<>();
        List<String> changFieldNameList = new ArrayList<>();
        String oldValue = "";
        String newValue = "";
//        PbcAccountDto pbcAccountDto = null;
        PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(organCode, EAccountType.AMS);
        if (pbcAccountDto != null) {
            try {
                if (billsPublic.getAcctType() == CompanyAcctType.tempAcct || billsPublic.getAcctType() == CompanyAcctType.specialAcct) {
                    if (billsPublic.getBillType() != BillType.ACCT_OPEN) {
                        //查询人行返回对象
                        AmsAccountInfo amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(pbcAccountDto, billsPublic.getAcctNo());
                        if (amsAccountInfo.getAcctType() != null) {
                            //根据人行对象账户性质转换billsPublic小类对象
                            billsPublic.setAcctType(CompanyAcctType.valueOf(amsAccountInfo.getAcctType().name()));
                        }
                    }
                }
            } catch (Exception e) {
                log.error("人行查询失败", e);
                code = ResultCode.PBC_CHECKDETAIL_FAILURE.code();
                msg = e.getMessage();
                resultDto = ResultDtoFactory.toApiError(code, msg, null);
            }

            if (billsPublic.getAcctType() != null) {
                AccountsAllInfo accountsAll = accountsAllService.findByAcctNo(acctNo);
                if(accountsAll != null) {
                    BeanCopierUtils.copyProperties(accountsAll, allBillsPublicDTO);

                    AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getOne(accountsAll.getRefBillId());
                    AccountPublicInfo accountPublic = accountPublicService.findByAccountId(accountsAll.getId());
                    CustomerPublicLogInfo customerPublicLogInfo = customerPublicLogService.getOne(accountsAll.getCustomerLogId());
                    CustomerPublicMidInfo customerPublicMidInfo = customerPublicMidService.getOne(accountsAll.getCustomerLogId());

                    if(accountBillsAllInfo != null) {
                        BeanCopierUtils.copyProperties(accountBillsAllInfo, allBillsPublicDTO);
                    }
                    if(accountPublic != null) {
                        BeanCopierUtils.copyProperties(accountPublic, allBillsPublicDTO);
                    }
                    if(customerPublicLogInfo != null) {
                        BeanCopierUtils.copyProperties(customerPublicLogInfo, allBillsPublicDTO);
                    } else {
                        if(customerPublicMidInfo != null) {
                            BeanCopierUtils.copyProperties(customerPublicMidInfo, allBillsPublicDTO);
                        }
                    }

                    try {
                        oldMap = BeanUtils.describe(allBillsPublicDTO);
                        newMap = BeanUtils.describe(billsPublic);

//                        if(StringUtils.isNotBlank(allBillsPublicDTO.getBusinessScope())) {
//                            oldMap.put("businessScope", allBillsPublicDTO.getBusinessScope());
//                        }
//                        if(StringUtils.isNotBlank(allBillsPublicDTO.getBusinessScopeEccs())) {
//                            oldMap.put("businessScopeEccs", allBillsPublicDTO.getBusinessScopeEccs());
//                        }
//                        if(StringUtils.isNotBlank(billsPublic.getBusinessScope())) {
//                            newMap.put("businessScope", allBillsPublicDTO.getBusinessScope());
//                        }
//                        if(StringUtils.isNotBlank(billsPublic.getBusinessScopeEccs())) {
//                            newMap.put("businessScopeEccs", allBillsPublicDTO.getBusinessScopeEccs());
//                        }

                    } catch (Exception e) {
                        log.error("对象转化为map失败", e);
                        code = ResultCode.OBJ_CONVERT_FAIL.code();
                        msg = e.getMessage();
                        resultDto = ResultDtoFactory.toApiError(code, msg, null);
                    }

                    List<String> changeFields = new ArrayList<>(newMap.keySet());
                    List<String> ignoreFidlds = new ArrayList<String>();
                    ignoreField(ignoreFidlds);
                    changeFields.remove(ignoreFidlds);

//                    List<String> changeFields = getChangeFields(billsPublic.getAcctType());
                    if(changeFields != null && changeFields.size() != 0) {
                        for(String field : changeFields) {
                            oldValue = oldMap.get(field) == null ? "" : oldMap.get(field).toString();
                            newValue = newMap.get(field) == null ? "" : newMap.get(field).toString();

                            if (StringUtils.isNotBlank(newValue) && !oldValue.equals(newValue)) {
                                beforeChangeFieldValueMap.put(field, oldValue);
                                oldMap.put(field, newValue);
                                afterChangeFieldValueMap.put(field, newValue);
                                changFieldNameList.add(field);
                            }

                        }
                    }

                    OrganizationDto organizationDto = organizationService.findByCode(organCode);
                    UserDto userDto = null;
                    if(organizationDto != null){
                        List<UserDto> virtualUser = userService.findByOrgId(organizationDto.getId());
                        if(CollectionUtils.isNotEmpty(virtualUser)){
                            for(UserDto userDto1 : virtualUser){
                                if(userDto1.getEnabled()){
                                    userDto = userDto1;
                                    break;
                                }
                            }
                        }else{
                            code = ResultCode.ACCTNO_NOT_FOUND.code();
                            msg = organizationDto.getCode() + "该机构无可用用户；请检查后再进行操作！";
                            resultDto = ResultDtoFactory.toApiError(code, msg, null);
                            return resultDto;
                        }
                    }else{
                        code = ResultCode.ACCTNO_NOT_FOUND.code();
                        msg = organizationDto.getCode() + "无此机构；请检查后再进行操作！";
                        resultDto = ResultDtoFactory.toApiError(code, msg, null);
                        return resultDto;
                    }


                    Blank(oldMap);
                    oldMap.put("billType", "ACCT_CHANGE");
                    oldMap.put("status", "APPROVED");
                    oldMap.put("action", "saveForm");
                    oldMap.put("method", "saveChangeBills");
                    oldMap.put("lastUpdateBy",userDto.getId()+"");


                    allBillsPublicDTO = allBillsPublicService.submit(userDto.getId(), oldMap);

                    resultDto = ResultDtoFactory.toApiError(ResultCode.SUCCESS.code(), "存量变更成功", null);

                    if(changFieldNameList != null && changFieldNameList.size() != 0) {
                        accountChangeSummaryService.saveAccountChangeSummary(changFieldNameList, beforeChangeFieldValueMap , afterChangeFieldValueMap, allBillsPublicDTO);
                    }
                    try {
                        allBillsPublicService.updateFinalStatus(allBillsPublicDTO,userDto.getId());
                    } catch (Exception e) {
                        log.error("结束流水失败：", e);
                    }
                } else {
                    code = ResultCode.ACCTNO_NOT_FOUND.code();
                    msg = ResultCode.ACCTNO_NOT_FOUND.message();
                    resultDto = ResultDtoFactory.toApiError(code, msg, null);
                }
            }


        } else {
            List<PbcAccountDto> pbcAccountDtos = pbcAccountService.listByOrgCodeAndType(organCode, EAccountType.AMS);
            if(pbcAccountDtos == null || pbcAccountDtos.size() == 0) {
                code = ResultCode.PBCACCOUNT_NOT_FOUND.code();
                msg = ResultCode.PBCACCOUNT_NOT_FOUND.message();
            } else {
                code = ResultCode.NO_VALID_PBCACCOUNT.code();
                msg = ResultCode.NO_VALID_PBCACCOUNT.message();
            }
            resultDto = ResultDtoFactory.toApiError(code, msg, null);
        }

        return resultDto;

    }

    /**
     * 根据账户性质+存款人名称，返回开户许可证编号和存款人查询密码
     * 基本户返回开户许可证编号和存款人查询密码，非临时机构临时存款账户返回开户许可证编号
     *
     * @param acctType      账户性质
     * @param depositorName 存款人名称
     * @return result：{
     *     acctName：账户名称
     *     acctNo：账户号码
     *     bankName：开户银行
     *     legalName：法定代表人/单位负责人
     *     accountKey：开户许可证编号
     *     openKey：开户许可证号（新）（取消核准）（基本存款账户编号）
     *     selectPwd：查询密码
     * }
     */
    @Override
    public ObjectRestResponse getAccountByAcctTypeDepositorName(String acctType, String depositorName){
        AccountsAllInfo aai = accountsAllService.findByAcctTypeAndDepositorName(CompanyAcctType.str2enum(acctType), depositorName);
        return this.getAccountInit(aai);
    }

    /**
     * 根据账号，返回开户许可证编号和存款人查询密码
     * 基本户返回开户许可证编号和存款人查询密码，非临时机构临时存款账户返回开户许可证编号
     *
     * @param acctNo        账号
     * @return result：{
     *     acctName：账户名称
     *     acctNo：账户号码
     *     bankName：开户银行
     *     legalName：法定代表人/单位负责人
     *     accountKey：开户许可证编号
     *     openKey：开户许可证号（新）（取消核准）（基本存款账户编号）
     *     selectPwd：查询密码
     * }
     */
    @Override
    public ObjectRestResponse getAccountByAcctNo(String acctNo){
        AccountsAllInfo aai = accountsAllService.findByAcctNo(acctNo);
        return this.getAccountInit(aai);
    }

    private ObjectRestResponse getAccountInit(AccountsAllInfo aai) {
        if (aai == null) {
            return new ObjectRestResponse<>().rel(false).msg("未找到该账户信息");
        } else {
            JSONObject obj = new JSONObject();
            obj.put("acctName", aai.getAcctName());//账户名称
            obj.put("acctNo", aai.getAcctNo());//账户号码
            obj.put("bankName", aai.getBankName());//开户银行
            obj.put("legalName", "");//法定代表人/单位负责人
            if (aai.getRefBillId() != null) {
                AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getOne(aai.getRefBillId());
                if (accountBillsAllInfo != null && accountBillsAllInfo.getCustomerLogId() != null) {
                    CustomerPublicLogInfo customerPublicLogInfo = customerPublicLogService.getOne(accountBillsAllInfo.getCustomerLogId());
                    //如果该数据最终状态未完成，但是人行上报成功  查询CustomerPublicMidInfo表
                    if(customerPublicLogInfo == null){
                        CustomerPublicMidInfo customerPublicMidInfo = customerPublicMidService.getByBillId(accountBillsAllInfo.getId());
                        if(customerPublicMidInfo != null && StringUtils.isNotBlank(customerPublicMidInfo.getLegalName())){
                            obj.put("legalName", customerPublicMidInfo.getLegalName());//法定代表人/单位负责人
                        }
                    }else{
                        if (customerPublicLogInfo != null && StringUtils.isNotBlank(customerPublicLogInfo.getLegalName())) {
                            obj.put("legalName", customerPublicLogInfo.getLegalName());//法定代表人/单位负责人
                        }
                    }
                }
            }
            obj.put("accountKey", aai.getAccountKey());//开户许可证编号
            obj.put("openKey", aai.getOpenKey());//开户许可证号（新）（取消核准）（基本存款账户编号）
            obj.put("selectPwd", aai.getSelectPwd());//查询密码
            AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getOne(aai.getRefBillId());
            if(accountBillsAllInfo!=null){
                obj.put("accountLicenseNo", accountBillsAllInfo.getAccountLicenseNo());//非临时存款账户编号
            }else{
                obj.put("accountLicenseNo", "");
            }
            return new ObjectRestResponse<>().rel(true).result(obj);
        }
    }

    private List<String> getChangeFields(CompanyAcctType acctType) {
        List<String> changeFieldsList = null;

        if(acctType == CompanyAcctType.jiben) {
            changeFieldsList = new ArrayList<String>(){{add("acctType"); add("acctNo");add("depositorName");add("fileType");add("fileNo");add("zipCode");
                    add("telephone");add("regCurrencyType");add("taxRegNo");add("stateTaxRegNo");add("orgCode");add("legalName");add("legalIdcardType");
                    add("legalIdcardNo");add("regFullAddress");add("regAddress");add("businessScope");add("businessScopeEccs");
                    add("parCorpName");add("parAccountKey");add("parOrgCode");add("parLegalName");add("parLegalIdcardType");add("parLegalIdcardNo");}};

        } else if(acctType == CompanyAcctType.yiban) {
            changeFieldsList = new ArrayList<String>(){{add("acctType"); add("acctNo");add("accountKey");add("regType");add("regNo");}};
        } else if(acctType == CompanyAcctType.yusuan) {
            changeFieldsList = new ArrayList<String>(){{add("acctType"); add("acctNo");add("accountKey");add("fileType");add("fileNo");
                    add("fileType2");add("fileNo2");add("capitalProperty");add("saccpostfix");add("saccprefix");add("fundManager");
                    add("fundManagerIdcardType");add("fundManagerIdcardNo");add("insideDeptName");add("insideLeadName");add("insideLeadIdcardType");
                    add("insideLeadIdcardNo");add("insideTelephone");add("insideZipcode");add("insideAddress");}};

        } else if(acctType == CompanyAcctType.feiyusuan) {
            changeFieldsList = new ArrayList<String>(){{add("acctType"); add("acctNo");add("accountKey");add("fileType");add("fileNo");
                add("capitalProperty");add("accountNameFrom");add("fundManager");
                add("fundManagerIdcardType");add("fundManagerIdcardNo");add("insideDeptName");add("insideLeadName");add("insideLeadIdcardType");
                add("insideLeadIdcardNo");add("insideTelephone");add("insideZipcode");add("insideAddress");}};

        } else if(acctType == CompanyAcctType.linshi) {
            changeFieldsList = new ArrayList<String>(){{add("acctType"); add("acctNo");add("depositorName");add("fileType");add("fileNo");
                add("zipCode");add("telephone");add("regCurrencyType");add("registeredCapital");add("stateTaxRegNo");add("taxRegNo");
                add("orgCode");add("legalName");add("legalIdcardType");add("legalIdcardNo");add("regArea");
                add("businessScope");add("parCorpName");add("parAccountKey");add("parOrgCode");add("parLegalIdcardType");add("parLegalIdcardNo");}};

        } else if(acctType == CompanyAcctType.feilinshi) {
            changeFieldsList = new ArrayList<String>(){{add("acctType"); add("acctNo");add("nontmpProjectName");add("nontmpLegalName");
            add("nontmpLegalIdcardType");add("nontmpLegalIdcardNo");add("nontmpTelephone");add("nontmpZipcode");add("nontmpAddress");
            add("fileType");add("fileNo");add("acctCreateReason");}};

        } else if(acctType == CompanyAcctType.teshu) {
            changeFieldsList = new ArrayList<String>(){{add("acctType"); add("acctNo");add("depositorName");add("fileType");
                add("fileNo");add("fileType2");add("fileNo2");add("zipCode");add("telephone");add("regCurrencyType");add("registeredCapital");
                add("stateTaxRegNo");add("taxRegNo");add("orgCode");add("legalName");add("legalIdcardType");add("legalIdcardNo");add("regFullAddress");
                add("businessScope");add("parCorpName");add("parAccountKey");add("parLegalIdcardType");add("parLegalIdcardNo");}};

        }

        return changeFieldsList;
    }

    private void Blank(Map<String, String> oldMap) {
        oldMap.put("billNo", "");
        oldMap.put("billDate", "");
        oldMap.put("approver", "");
        oldMap.put("approveDate", "");
        oldMap.put("approveDesc", "");
        oldMap.put("pbcCheckStatus", "");
        oldMap.put("pbcCheckDate", "");
        oldMap.put("customerNo", "");
        oldMap.put("id", "");
        oldMap.put("description", "");
        oldMap.put("pbcSyncStatus", "buTongBu");
        oldMap.put("eccsSyncStatus", "buTongBu");
        oldMap.put("pbcSyncError", "");
        oldMap.put("pbcOperator", "");
        oldMap.put("pbcSyncTime", "");
        oldMap.put("pbcSyncCheck", "");
        oldMap.put("pbcSyncMethod", "");
        oldMap.put("eccsSyncError", "");
        oldMap.put("eccsOperator", "");
        oldMap.put("eccsSyncTime", "");
        oldMap.put("eccsSyncCheck", "");
        oldMap.put("acctIsFromCore", "");
        oldMap.put("coreDataCompleted", "");
        oldMap.put("finalStatus", "");
        oldMap.put("handingMark", "");
        oldMap.put("initFullStatus", "");
        oldMap.put("initRemark", "");
        oldMap.put("fromSource", "");

    }

    private void ignoreField(List<String> ignoreFidlds) {
        ignoreFidlds.add("billNo");
        ignoreFidlds.add("billType");
        ignoreFidlds.add("billDate");
        ignoreFidlds.add("approver");
        ignoreFidlds.add("approveDate");
        ignoreFidlds.add("approveDesc");
        ignoreFidlds.add("pbcCheckStatus");
        ignoreFidlds.add("pbcCheckDate");
        //ignoreFidlds.add("customerNo");
        //ignoreFidlds.add("depositorName");
        ignoreFidlds.add("id");
        ignoreFidlds.add("description");
        ignoreFidlds.add("pbcSyncStatus");
        ignoreFidlds.add("pbcSyncError");
        ignoreFidlds.add("pbcOperator");
        ignoreFidlds.add("pbcSyncTime");
        ignoreFidlds.add("pbcSyncCheck");
        ignoreFidlds.add("pbcSyncMethod");
        ignoreFidlds.add("eccsSyncError");
        ignoreFidlds.add("eccsOperator");
        ignoreFidlds.add("eccsSyncTime");
        ignoreFidlds.add("eccsSyncCheck");
        ignoreFidlds.add("acctIsFromCore");
        ignoreFidlds.add("coreDataCompleted");
        ignoreFidlds.add("finalStatus");
        ignoreFidlds.add("handingMark");
        ignoreFidlds.add("initFullStatus");
        ignoreFidlds.add("initRemark");
        ignoreFidlds.add("fromSource");
        ignoreFidlds.add("acctNo");
        //ignoreFidlds.add("bankCode");
        ignoreFidlds.add("refBillId");
        ignoreFidlds.add("refCustomerBillId");
        ignoreFidlds.add("originalBillId");
        ignoreFidlds.add("recId");
        ignoreFidlds.add("acctLogId");
        ignoreFidlds.add("accountId");
        ignoreFidlds.add("customerLogId");
        ignoreFidlds.add("custLogId");
        ignoreFidlds.add("custOrganFullId");
        ignoreFidlds.add("acctOrgFullid");

        //新增忽略列表
        ignoreFidlds.add("status");
        ignoreFidlds.add("eccsSyncStatus");
        ignoreFidlds.add("midId");
        ignoreFidlds.add("acctId");
        ignoreFidlds.add("imageTempNo");
        ignoreFidlds.add("cancelHeZhun");
        ignoreFidlds.add("imageBatchNo");
        ignoreFidlds.add("aLLAccountData");
        ignoreFidlds.add("oldAccountKey");
        ignoreFidlds.add("organFullId");
        ignoreFidlds.add("organCode");
        ignoreFidlds.add("changeFields");
        ignoreFidlds.add("string006");
    }


}
