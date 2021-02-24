package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.service.PbcMockService;
import com.ideatech.ams.pbc.service.ams.cancel.AmsSelectPwdResetService;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.system.org.service.OrganRegisterService;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BaseException;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class SelectPwdServiceImpl implements SelectPwdService{
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
    public String[] resetSelectPwd(String accountKey, String pwd) {
        String[] arry = new String[2];
        if(StringUtils.isBlank(accountKey)){
            throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "基本开户许可证号不能为空");
        } else if (!accountKey.substring(0, 1).equalsIgnoreCase("J")) {
            throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "开户许可证不正确,应以J开头");
        } else if (accountKey.length() != 14) {
            throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "开户许可证长度不正确，应14位");
        }
        if(StringUtils.isBlank(pwd)){
            throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "新查询密码不能为空");
        }

       String organCode = organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId()).getPbcCode();
        boolean isture =  organRegisterService.getOrganRegisterFlagByBankCode(organCode);
        if (!isture){
            throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "当前登录用户所在机构不是取消核准机构，请确认后重试");
        }
       try {
           PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullIdByCancelHeZhun(SecurityUtils.getCurrentOrgFullId(), EAccountType.AMS);
           if (pbcAccountDto == null) {
               throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "该机构未配置人行用户名密码或用户名密码不正确");
           }


           //登录挡板开启，默认返回成功
           if (pbcMockService.isLoginMockOpen()) {
               log.info("登录挡板开启，默认重置成功");
           }else{
               LoginAuth auth = amsMainService.amsLogin(allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto));
               String[] res = amsSelectPwdResetService.resetSelectPwd(auth,accountKey,pwd);
               if (ArrayUtils.isNotEmpty(res)) {
                   if(StringUtils.isBlank(res[0])){
                       log.info("密码重置失败，未知异常，请联系管理员");
                       throw new BaseException(EErrorCode.PBC_QUERY_ERROR, "密码重置失败，未知异常，请联系管理员");
                   }
               }
           }
           List<AccountsAllInfo> infos = accountsAllService.findByAccountKey(accountKey,CompanyAcctType.jiben);
           arry[1]=pwd;
           if(CollectionUtils.isNotEmpty(infos)){
               for (AccountsAllInfo info : infos) {
                   if(StringUtils.isNotBlank(info.getAcctName())){
                       arry[0] = info.getAcctName();
                   }else{
                       arry[0]=accountKey;
                   }
                   log.info("更新数据库查询密码");
                   info.setSelectPwd(pwd);
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
        return arry;
    }
}
