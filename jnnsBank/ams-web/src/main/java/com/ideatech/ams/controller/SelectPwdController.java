package com.ideatech.ams.controller;

import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.SelectPwdService;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.dto.CustomersAllInfo;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.customer.service.CustomersAllService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.template.dto.TemplateDto;
import com.ideatech.ams.system.template.service.TemplateService;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BaseException;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.PdfGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/selectPwd")
@Slf4j
public class SelectPwdController {
    @Autowired
    private TemplateService templateService;

    @Autowired
    private SelectPwdService selectPwdService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private CustomerPublicLogService customerPublicLogService;

    @Autowired
    private CustomersAllService customersAllService;

    @PostMapping("/reset")
    public ResultDto resetSelectPwd(String accountKey, String selectPwd){
        try {
            String[] data = selectPwdService.resetSelectPwd(accountKey,selectPwd);
            return ResultDtoFactory.toApiSuccess(data);
        }catch (Exception e){
            return ResultDtoFactory.toApiError(ResultCode.ORGAN_NOT_CONFIG_SYNC_USER.code(), e.getMessage());
        }

    }
    @GetMapping("/getPrintPreview")
    public ResponseEntity<byte[]> getTemplateNameList(String depositorName, String selectPwd) throws Exception{
        Map<String, Object> describe = new HashMap<>();
        //检查是否是J开头的核准号  是的话通过最新流水查询存款人名称
        if(StringUtils.isNotBlank(depositorName) && (depositorName.startsWith("j") || depositorName.startsWith("J"))){
            //先根据开户许可证查找accountsAll   在通过logid查找customerid  最后找到存款人名称
            List<AccountsAllInfo> accountsAllInfo = accountsAllService.findByAccountKey(depositorName, CompanyAcctType.jiben);
            CustomerPublicLogInfo customerPublicLogInfo = null;
            CustomersAllInfo customersAllInfo = null;
            if(CollectionUtils.isNotEmpty(accountsAllInfo)){
                AccountsAllInfo accountsAllInfo1 = accountsAllInfo.get(0);
                if(accountsAllInfo1 != null){
                    customerPublicLogInfo = customerPublicLogService.getOne(accountsAllInfo1.getCustomerLogId());
                    if(customerPublicLogInfo != null){
                        customersAllInfo = customersAllService.findOne(customerPublicLogInfo.getCustomerId());
                        if(customersAllInfo != null){
                            depositorName = customersAllInfo.getDepositorName();
                        }
                    }
                }
            }
        }
        describe.put("cancelHeZhunSelectPwd",selectPwd);//查询密码
        describe.put("cancelHeZhunDepositorName",depositorName);//存款人名称
        TemplateDto byId = templateService.findByTemplateName("取消核准基本户交易密码-反面");
        if(byId==null){
            byte[] template = null;
            throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "未找到打印模板，模板名称：取消核准基本户交易密码-反面");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        return new ResponseEntity<byte[]>(PdfGenerator.generate(byId.getTemplaeContent(), describe), headers, HttpStatus.OK);
    }

    @GetMapping("/getResetPrintPreview")
    public ResponseEntity<byte[]> getResetPrintPreview(String depositorName, String selectPwd, String accountKey, String acctNo, String legalName, String openKey, String pbcCode,String templateName) throws Exception{
        Map<String, Object> describe = new HashMap<>();
        describe.put("acctName",depositorName);
        describe.put("depositorName",depositorName);
        describe.put("selectPwd",selectPwd);
        describe.put("accountKey",accountKey);
        describe.put("acctNo",acctNo);
        describe.put("legalName",legalName);
        describe.put("openKey",openKey);
        describe.put("bankCode",pbcCode);

        describe.put("cancelHeZhunAcctName",depositorName);//账户名称
        describe.put("cancelHeZhunDepositorName",depositorName);//存款人名称
        describe.put("cancelHeZhunSelectPwd",selectPwd);//查询密码
        describe.put("cancelHeZhunAccountKey",accountKey);//基本户开户许可证
        describe.put("cancelHeZhunAcctNo",acctNo);//账号
        describe.put("cancelHeZhunLegalName",legalName);//法人姓名
        describe.put("cancelHeZhunBankCode",pbcCode);//银行代码

        if(StringUtils.isNotBlank(pbcCode)){
            OrganizationDto organizationDto = organizationService.findByCode(pbcCode);
            if(organizationDto != null){
                describe.put("bankName",organizationDto.getName());
            }
        }
        TemplateDto byId = templateService.findByBillTypeAndDepositorTypeAndTemplateName(BillType.ACCT_OPEN, null, templateName);
//        TemplateDto byId = templateService.findByTemplateName("取消核准基本户交易密码-反面");
        if(byId==null){
            byte[] template = null;
            throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "未找到打印模板，模板名称：取消核准基本户交易密码-反面");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        return new ResponseEntity<byte[]>(PdfGenerator.generate(byId.getTemplaeContent(), describe), headers, HttpStatus.OK);
    }

}
