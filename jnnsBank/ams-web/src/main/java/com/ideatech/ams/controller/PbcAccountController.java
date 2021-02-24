package com.ideatech.ams.controller;

import com.ideatech.ams.kyc.service.idcard.IdCardComperService;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.LoginStatus;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.pbc.spi.EccsMainService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.sm4.service.EncryptPwdService;
import com.ideatech.ams.system.trace.aop.OperateLog;
import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liangding
 * @create 2018-05-17 上午12:10
 **/
@RestController
@RequestMapping("/pbc/account")
public class PbcAccountController {
    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private EccsMainService eccsMainService;

    @Autowired
    private AmsMainService amsMainService;

    @Autowired
    private IdCardComperService idCardComperService;

    @Autowired
    private EncryptPwdService encryptPwdService;

    @GetMapping("/")
    public ResultDto listByOrgId(Long orgId) {
        return ResultDtoFactory.toAckData(pbcAccountService.listByOrgId(orgId));
    }

    @GetMapping("/{id}")
    public ResultDto getById(@PathVariable("id") Long id) {
        return ResultDtoFactory.toAckData(pbcAccountService.getById(id));
    }

    @OperateLog(operateModule = OperateModule.ORGANIZATION, operateType = OperateType.UPDATE,operateContent = "人行账号修改",cover = true)
    @PostMapping("/{id}")
    public ResultDto update(@PathVariable("id") Long id, PbcAccountDto pbcAccountDto) {
        pbcAccountDto.setId(id);
        pbcAccountDto.setPassword(encryptPwdService.encryptPwd(pbcAccountDto.getPassword()));
        pbcAccountService.save(pbcAccountDto);
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.ORGANIZATION, operateType = OperateType.INSERT,operateContent = "人行账号新建")
    @PostMapping("/")
    public ResultDto save(PbcAccountDto pbcAccountDto) {
        pbcAccountDto.setPassword(encryptPwdService.encryptPwd(pbcAccountDto.getPassword()));
        pbcAccountService.save(pbcAccountDto);
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.ORGANIZATION, operateType = OperateType.DELETE,operateContent = "人行账号删除")
    @DeleteMapping("/{id}")
    public ResultDto deleteById(@PathVariable("id") Long id) {
        pbcAccountService.delete(id);
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.ORGANIZATION, operateType = OperateType.ENABLE,operateContent = "人行账号启用",cover = true)
    @PutMapping("/{id}/enable")
    public ResultDto enable(@PathVariable Long id) {
        pbcAccountService.enable(id);
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.ORGANIZATION, operateType = OperateType.DISABLE,operateContent = "人行账号禁用",cover = true)
    @PutMapping("/{id}/disable")
    public ResultDto disable(@PathVariable Long id) {
        pbcAccountService.disable(id);
        return ResultDtoFactory.toAck();
    }

    @PutMapping("/validate")
    public ResultDto validate(PbcAccountDto pbcAccountDto) {
        String errorMessage = null;
        switch (pbcAccountDto.getAccountType()) {
            case AMS:
                PbcUserAccount amsAccount = new PbcUserAccount();
                amsAccount.setLoginIp(pbcAccountDto.getIp());
                amsAccount.setLoginPassWord(pbcAccountDto.getPassword());
                amsAccount.setLoginUserName(pbcAccountDto.getAccount());
                LoginAuth loginAuth = amsMainService.amsLogin(amsAccount);
                if (loginAuth.getLoginStatus() == LoginStatus.Success) {
                    return ResultDtoFactory.toAck();
                } else {
                    errorMessage = loginAuth.getLoginStatus().getFullName();
                }
                break;
            case ECCS:
                PbcUserAccount eccsAccount = new PbcUserAccount();
                eccsAccount.setLoginIp(pbcAccountDto.getIp());
                eccsAccount.setLoginPassWord(pbcAccountDto.getPassword());
                eccsAccount.setLoginUserName(pbcAccountDto.getAccount());
                LoginAuth loginAuth1 = eccsMainService.eccsLogin(eccsAccount);
                if (loginAuth1.getLoginStatus() == LoginStatus.Success) {
                    return ResultDtoFactory.toAck();
                } else {
                    errorMessage = loginAuth1.getLoginStatus().getFullName();
                }
                break;
            case PICP:
                if (idCardComperService.login(pbcAccountDto.getIp(), pbcAccountDto.getAccount(), pbcAccountDto.getPassword())) {
                    return ResultDtoFactory.toAck();
                }
                break;
            default:
        }
        if (StringUtils.isEmpty(errorMessage)) {
            errorMessage = "登陆失败";
        }
        return ResultDtoFactory.toNack(errorMessage);
    }

    @PutMapping("/change")
    public ResultDto changeECCSPassword(PbcAccountDto pbcAccountDto) {
        Long id = pbcAccountDto.getId();
        PbcAccountDto byId = pbcAccountService.getById(id);
        try {
            eccsMainService.modifyEccsPwd(byId.getIp(), byId.getAccount(), byId.getPassword(), pbcAccountDto.getPassword());
        } catch (Exception e) {
            return ResultDtoFactory.toNack(e.getMessage());
        }
        return ResultDtoFactory.toAck();
    }

    /*@GetMapping("/modifyEccsPwd")
    public ResultDto modifyEccsPwd(String ip,String accountName,String oldPass,String newPass) {
        try {
            eccsMainService.modifyEccsPwd(ip, accountName, oldPass, newPass);
        } catch (Exception e) {
            return ResultDtoFactory.toNack(e.getMessage());
        }
        return ResultDtoFactory.toAck();
    }*/

}
