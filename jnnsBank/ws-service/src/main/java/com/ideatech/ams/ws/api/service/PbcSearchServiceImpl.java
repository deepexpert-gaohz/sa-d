package com.ideatech.ams.ws.api.service;


import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.AmsResetPrintLogDto;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.AmsResetPrintLogService;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.pbc.dto.*;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.PbcMockService;
import com.ideatech.ams.pbc.service.ams.cancel.AmsResetJiBenPrintService;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.proof.dto.ProofReportDto;
import com.ideatech.ams.system.proof.enums.ProofType;
import com.ideatech.ams.system.proof.service.ProofReportService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class PbcSearchServiceImpl implements PbcSearchService {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private PbcAccountService pbcAccountService;
    @Autowired
    private PbcAmsService pbcAmsService;
    @Autowired
    private AmsMainService amsMainService;
    @Autowired
    private AllBillsPublicService allBillsPublicService;
    @Autowired
    private AmsResetJiBenPrintService amsResetJiBenPrintService;
    @Autowired
    private AccountsAllService accountsAllService;
    @Autowired
    private AccountBillsAllService accountBillsAllService;
    @Autowired
    private AmsResetPrintLogService amsResetPrintLogService;
    @Value("${ams.company.writeMoney}")
    private boolean writeMoney;
    @Autowired
    private ConfigService configService;
    @Autowired
    private ProofReportService proofReportService;
    @Autowired
    private UserService userService;
    @Autowired
    private PbcMockService pbcMockService;
    @Override
    public ResultDto searchAllAccount(String depositorName, String accountKey, String selectPwd, String organCode) {
        return searchAllAccountCommon(depositorName, accountKey, selectPwd, organCode, "");
    }

    @Override
    public ResultDto searchAllAccount(String depositorName, String accountKey, String selectPwd, String organCode, String username) {
        if(StringUtils.isBlank(username)) {
            return ResultDtoFactory.toNack("用户名不能为空");
        }
        UserDto userDto = userService.findByUsername(username);
        if(userDto == null) {
            return ResultDtoFactory.toNack("用户名不存在");
        }
        return searchAllAccountCommon(depositorName, accountKey, selectPwd, organCode, username);
    }

    @Override
    public ResultDto jiBenUniqueCheck(AmsJibenUniqueCheckCondition amsJibenUniqueCheckCondition, String organCode) {
        if (StringUtils.isBlank(organCode)) {
            ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), ResultCode.PARAM_IS_BLANK.message());
        }

        String errorMessage = pbcAmsService.jiBenUniqueCheck(amsJibenUniqueCheckCondition, organCode);

        if (StringUtils.isBlank(errorMessage)) {
            return ResultDtoFactory.toApiSuccess("");
        } else {
            return ResultDtoFactory.toApiError(ResultCode.DATA_ALREADY_EXISTED.code(), errorMessage, null);
        }
    }

    @Override
    public ResultDto jiBenResetDetails(String acctNo,String pbcCode) {
        AmsAccountInfo amsAccountInfo = null;
        try {
            OrganizationDto organizationDto = organizationService.findByCode(pbcCode);
            if(organizationDto == null) {
                return ResultDtoFactory.toApiError(EErrorCode.ORGAN_NOTCONFIG.getErrorCode(), "根据机构号无法找到对应机构!", null);
            }
            PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCodeByCancelHeZhun(pbcCode, EAccountType.AMS);
            PbcUserAccount pbcUserAccount = allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto);
            LoginAuth auth = amsMainService.amsLogin(pbcUserAccount);
            amsAccountInfo = amsResetJiBenPrintService.resetJiBenPrintFristStep(auth,acctNo,organizationDto.getPbcCode(),organizationDto.getName());
        } catch (Exception e) {
            if(e instanceof SyncException){
                log.error("基本户补打查询详情异常:{}",e.getMessage());
                return ResultDtoFactory.toApiError(EErrorCode.ORGAN_NOTCONFIG.getErrorCode(), ((SyncException) e).getCode(), e);
            }
            return ResultDtoFactory.toApiError(EErrorCode.ORGAN_NOTCONFIG.getErrorCode(), e.getMessage(), e);
        }
        return ResultDtoFactory.toApiSuccess(amsAccountInfo);
    }

    @Override
    public ResultDto jiBenResetPrint(String acctNo,String pbcCode){

        //获取人行4级操作员
        log.info("基本户补打开始");
        OrganizationDto organizationDto = organizationService.findByCode(pbcCode);
        PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCodeByCancelHeZhun(pbcCode, EAccountType.AMS);
        PbcUserAccount pbcUserAccount = allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto);
        //登录
        LoginAuth auth = amsMainService.amsLogin(pbcUserAccount);
        AmsAccountInfo amsAccountInfo = null;
        AmsResetPrintInfo amsResetPrintInfo = new AmsResetPrintInfo();
        String[] print = null;
        try {
            //调用补打接口
            log.info("调用补打接口");
            amsAccountInfo = amsResetJiBenPrintService.resetJiBenPrintFristStep(auth,acctNo,organizationDto.getPbcCode(),organizationDto.getName());
            print = amsResetJiBenPrintService.resetJiBenPrintSecondStep(auth,amsAccountInfo.getFileType1En());
            //新的核准号跟基本户编号找到后进行保存
            if(print.length > 0){
                log.info("基本户核准号：" + print[0] + ";基本户编号：" + print[1]);
                AccountsAllInfo accountsAllInfo = accountsAllService.findByAcctNo(acctNo);
                AmsResetPrintLogDto amsResetPrintLogDto = new AmsResetPrintLogDto();
                //增加补打基本户信息日志   记录原始数据跟新数据的区别
                if(accountsAllInfo != null){
                    amsResetPrintLogDto.setAcctNo(acctNo);
                    amsResetPrintLogDto.setUsername(SecurityUtils.getCurrentUsername());
                    amsResetPrintLogDto.setOrganCode(pbcCode);
                    amsResetPrintLogDto.setAccountKeyOld(accountsAllInfo.getAccountKey());
                    amsResetPrintLogDto.setAccountKeyNew(print[0]);
                    amsResetPrintLogDto.setOpenKeyOld(accountsAllInfo.getOpenKey());
                    amsResetPrintLogDto.setOpenKeyNew(print[1]);
                    amsResetPrintLogDto.setDepositorName(amsAccountInfo.getDepositorName());
                    amsResetPrintLogDto.setLegalName(amsAccountInfo.getLegalName());
                    amsResetPrintLogService.save(amsResetPrintLogDto);
                }else{
                    //新增
                    amsResetPrintLogDto.setAcctNo(acctNo);
                    amsResetPrintLogDto.setUsername(SecurityUtils.getCurrentUsername());
                    amsResetPrintLogDto.setOrganCode(pbcCode);
                    amsResetPrintLogDto.setAccountKeyNew(print[0]);
                    amsResetPrintLogDto.setOpenKeyNew(print[1]);
                    amsResetPrintLogDto.setSelectPwdNew(print[2]);
                    amsResetPrintLogService.save(amsResetPrintLogDto);
                }
                //修改accountsAll中基本户查询密码等
                if(accountsAllInfo != null){
                    //根据accountsAll 找到最新流水
                    Long refId = accountsAllInfo.getRefBillId();
                    AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getOne(refId);
                    if(accountBillsAllInfo != null){
                        accountBillsAllInfo.setAccountKey(print[0]);
                        accountBillsAllInfo.setOpenKey(print[1]);
                        //当编号进行变更后  增加变更基本户编号的日期
                        if(StringUtils.isNotBlank(print[0])){
                            accountBillsAllInfo.setString006(DateUtils.DateToStr(new Date(),"yyyy-MM-dd"));
                        }
                        accountBillsAllService.save(accountBillsAllInfo);
                    }

                    accountsAllInfo.setAccountKey(print[0]);
                    accountsAllInfo.setOpenKey(print[1]);
                    //当编号进行变更后  增加变更基本户编号的日期
                    if(StringUtils.isNotBlank(print[0])){
                        accountsAllInfo.setString006(DateUtils.DateToStr(new Date(),"yyyy-MM-dd"));
                    }
                    accountsAllService.save(accountsAllInfo);
                }
                //反显到前端页面进行展现并打印使用
                amsResetPrintInfo.setAccountKey(print[0]);
                amsResetPrintInfo.setOpenKey(print[1]);
                amsResetPrintInfo.setSelectPwd(accountsAllInfo.getSelectPwd());
                amsResetPrintInfo.setAcctNo(acctNo);
                amsResetPrintInfo.setLegalName(amsAccountInfo.getLegalName());
                amsResetPrintInfo.setDepositorName(amsAccountInfo.getDepositorName());
                amsResetPrintInfo.setPbcCode(pbcCode);
            }else{
                throw new BizServiceException(EErrorCode.ORGAN_NOTCONFIG, "基本户补打编号获取失败");
            }
        } catch (Exception e) {
            throw new BizServiceException(EErrorCode.ORGAN_NOTCONFIG, "基本户补打查询详情异常：" + e);
        }
        return ResultDtoFactory.toApiSuccess(amsResetPrintInfo);
    }

    private ResultDto searchAllAccountCommon(String depositorName, String accountKey, String selectPwd, String organCode, String username) {
        OrganizationDto organizationDto = validateOrgan(organCode);
        PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullId(organizationDto.getFullId(), EAccountType.AMS);
        if (pbcAccountDto == null) {
            return ResultDtoFactory.toApiError(ResultCode.ORGAN_NOT_CONFIG_SYNC_USER.code(), "该机构未配置人行用户名密码或用户名密码不正确");
            //throw new BizServiceException(EErrorCode.ORGAN_NOTCONFIG, "该机构未配置人行用户名密码或用户名密码不正确");
        }
        //校验开户许可证的合法性
        if(StringUtils.isNotBlank(accountKey)){
            if (!accountKey.startsWith("J") && !accountKey.startsWith("j")) {
                return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_INVALID.code(), "基本户开户许可证请已J开头！");
            }
            if (accountKey.length() != 14) {
                return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_INVALID.code(), "基本户开户许可证长度不正确！");
            }
        }
        try {
            List<AmsPrintInfo> data = pbcAmsService.searchAllAccount(pbcAccountDto,depositorName,accountKey,selectPwd);
            if(writeMoney){
                ProofReportDto accountProofReportDto = new ProofReportDto();
                if(StringUtils.isBlank(username)) {
                    username =  SecurityUtils.getCurrentUsername();
                    if(StringUtils.isBlank(username)){
                        UserDto userDto = userService.findById(2L);
                        accountProofReportDto.setTypeDetil("接口方式查询存款人所有相关账户");
                        accountProofReportDto.setOrganFullId(organizationDto.getFullId());
                        accountProofReportDto.setUsername(userDto.getUsername());
                        accountProofReportDto.setProofBankName(organizationDto.getName());
                    }else{
                        accountProofReportDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                        accountProofReportDto.setUsername(username);
                        accountProofReportDto.setTypeDetil("账管系统查询存款人所有相关账户");
                        accountProofReportDto.setProofBankName(organizationDto.getName());
                    }
                } else {
                    accountProofReportDto.setTypeDetil("接口方式查询存款人所有相关账户");
                    accountProofReportDto.setOrganFullId(organizationDto.getFullId());
                    accountProofReportDto.setUsername(username);
                    accountProofReportDto.setProofBankName(organizationDto.getName());
                }

                ConfigDto configDto = configService.findOneByConfigKey("pbcMoney");
                if(configDto!=null){
                    accountProofReportDto.setPrice(configDto.getConfigValue());
                }else{
                    accountProofReportDto.setPrice("0");
                }
                accountProofReportDto.setType(ProofType.PBC);
                accountProofReportDto.setEntname(depositorName);
                accountProofReportDto.setAccountKey(accountKey);
                accountProofReportDto.setDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
                proofReportService.save(accountProofReportDto);
            }
            return ResultDtoFactory.toApiSuccess(data);
        }catch (Exception e){
            log.error("查询失败："+e.getMessage());
            return ResultDtoFactory.toApiError(ResultCode.PBC_CHECKDETAIL_FAILURE.code(), e.getMessage(), e);
        }
    }

    /**
     * 校验机构信息
     *
     * @param organCode
     * @throws BizServiceException
     */
    private OrganizationDto validateOrgan(String organCode) throws BizServiceException {
        OrganizationDto organizationDto = organizationService.findByCode(organCode);
        if (organizationDto == null) {
            throw new BizServiceException(EErrorCode.ORGAN_NOTCONFIG, "未配置机构");
        }
        return organizationDto;
    }
}
