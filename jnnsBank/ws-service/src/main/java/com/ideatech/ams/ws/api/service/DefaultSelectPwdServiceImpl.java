package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.service.PbcMockService;
import com.ideatech.ams.pbc.service.ams.cancel.AmsSelectPwdResetService;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganRegisterService;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DefaultSelectPwdServiceImpl implements DefaultSelectPwdService {
    @Autowired
    private AmsSelectPwdResetService amsSelectPwdResetService;
    @Autowired
    private AccountsAllService accountsAllService;
    @Autowired
    private PbcAccountService pbcAccountService;
    @Autowired
    private AmsMainService amsMainService;
    @Autowired
    private AllBillsPublicService allBillsPublicService;
    @Autowired
    private PbcMockService pbcMockService;
    @Autowired
    private OrganRegisterService organRegisterService;
    @Autowired
    private OrganizationService organizationService;

    @Override
    public ResultDto resetSelectPwd(String accountKey, String selectPwd, String organCode){
        try {
//            String[] data = selectPwdService.resetSelectPwd(accountKey,selectPwd);

            String[] arry = new String[2];
            if(StringUtils.isBlank(accountKey)){
                throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "基本开户许可证号不能为空");
            } else if (!accountKey.substring(0, 1).equalsIgnoreCase("J")) {
                throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "开户许可证不正确,应以J开头");
            } else if (accountKey.length() != 14) {
                throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "开户许可证长度不正确，应14位");
            }
            if(StringUtils.isBlank(selectPwd)){
                throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "新查询密码不能为空");
            }
            if(StringUtils.isBlank(organCode)){
                throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "行内机构号不能为空");
            }

            OrganizationDto organ = organizationService.findByCode(organCode);
            if(organ == null){
                throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "机构号对应机构不存在");
            }

            boolean isture =  organRegisterService.getOrganRegisterFlagByBankCode(organ.getPbcCode());
            if (!isture){
                throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "当前登录用户所在机构不是取消核准机构，请确认后重试");
            }
            try {
                PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullIdByCancelHeZhun(organ.getFullId(), EAccountType.AMS);
                if (pbcAccountDto == null) {
                    throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "该机构未配置人行用户名密码或用户名密码不正确");
                }

                //登录挡板开启，默认返回成功
                if (pbcMockService.isLoginMockOpen()) {
                    log.info("登录挡板开启，默认重置成功");
                }else{
                    LoginAuth auth = amsMainService.amsLogin(allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto));
                    String[] res = amsSelectPwdResetService.resetSelectPwd(auth,accountKey,selectPwd);
                    if (ArrayUtils.isNotEmpty(res)) {
                        if(StringUtils.isBlank(res[0])){
                            log.info("密码重置失败，未知异常，请联系管理员");
                            throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "密码重置失败，未知异常，请联系管理员");
                        }
                    }
                }
                List<AccountsAllInfo> infos = accountsAllService.findByAccountKey(accountKey,CompanyAcctType.jiben);
                arry[1]=selectPwd;
                if(CollectionUtils.isNotEmpty(infos)){
                    for (AccountsAllInfo info : infos) {
                        if(StringUtils.isNotBlank(info.getAcctName())){
                            arry[0] = info.getAcctName();
                        }else{
                            arry[0]=accountKey;
                        }
                        log.info("更新数据库查询密码");
                        info.setSelectPwd(selectPwd);
                        accountsAllService.save(info);
                    }
                }else{
                    //存款人名称
                    arry[0]=accountKey;
                }

            }catch (Exception e) {
                log.error("密码重置失败，失败信息：", e);
                if (e.getMessage().contains("Connection timed out")) {
                    throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "网络不通");
                } else {
                    throw new BaseException(EErrorCode.PBC_QUERY_ERROR, e.getMessage());
                }
            }

            return ResultDtoFactory.toApiSuccess(arry);
        }catch (Exception e){
            return ResultDtoFactory.toApiError(ResultCode.ORGAN_NOT_CONFIG_SYNC_USER.code(), e.getMessage());
        }

    }
}
