package com.ideatech.ams.ws.api.service;


import com.ideatech.ams.pbc.dto.EccsAccountInfo;
import com.ideatech.ams.pbc.dto.EccsSearchCondition;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.spi.EccsMainService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.common.msg.ObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class EccsApiServiceImpl implements EccsApiService {
    @Autowired
    private EccsMainService eccsMainService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PbcAccountService pbcAccountService;


    /**
     * 机构信用代码信息查询
     * @param bankCode  开户银行金融机构编码
     * @param condition 机构信用代码查询条件
     * @return
     */
    @Override
    public ObjectRestResponse<EccsAccountInfo> getEccsAccountInfoByCondition(String bankCode, EccsSearchCondition condition) throws Exception {
        EccsAccountInfo eccsAccountInfo = null;

        if(condition == null) {
            return new ObjectRestResponse<EccsAccountInfo>().rel(false).msg("查询条件不能为空");
        }

        if(StringUtils.isBlank(bankCode)) {
            return new ObjectRestResponse<EccsAccountInfo>().rel(false).msg("银行机构代码不能为空");
        }

        OrganizationDto organ = organizationService.findByCode(bankCode);
        if(organ == null) {
            return new ObjectRestResponse<EccsAccountInfo>().rel(false).msg("银行机构代码对应的机构不存在");
        }

        PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullId(organ.getFullId(), EAccountType.ECCS);
        if(pbcAccountDto == null) {
            return new ObjectRestResponse<EccsAccountInfo>().rel(false).msg("该机构未配置信用机构用户名和密码");
        } else {
            if(StringUtils.isBlank(pbcAccountDto.getIp()) || StringUtils.isBlank(pbcAccountDto.getAccount())
                    || StringUtils.isBlank(pbcAccountDto.getPassword())) {
                return new ObjectRestResponse<EccsAccountInfo>().rel(false).msg("该机构配置的信用机构代码证的人行账号信息不完整");
            }

            PbcUserAccount eccsUserAccount = new PbcUserAccount();
            eccsUserAccount.setLoginIp(pbcAccountDto.getIp());
            eccsUserAccount.setLoginUserName(pbcAccountDto.getAccount());
            eccsUserAccount.setLoginPassWord(pbcAccountDto.getPassword());

            try {
                eccsAccountInfo = eccsMainService.getEccsAccountInfoByCondition(eccsUserAccount, condition);
            } catch (Exception e) {
                e.printStackTrace();
                return new ObjectRestResponse<EccsAccountInfo>().rel(false).msg(e.getMessage());
            }
        }

        if(eccsAccountInfo == null) {
            return new ObjectRestResponse<EccsAccountInfo>().rel(false).msg("机构信用代码证系统账户信息返回为空");
        }

        return new ObjectRestResponse<EccsAccountInfo>().rel(true).result(eccsAccountInfo);
    }

}
