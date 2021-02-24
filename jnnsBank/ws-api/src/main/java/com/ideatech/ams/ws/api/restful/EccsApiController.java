package com.ideatech.ams.ws.api.restful;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.EccsSearchCondition;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.enums.SyncOperateType;
import com.ideatech.ams.pbc.spi.EccsMainService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.BeanCopierUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author vantoo
 * @date 14:44 2018/5/20
 */
@RestController
@RequestMapping("/api/eccs")
public class EccsApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PbcApiController.class);

    @Autowired
    private EccsMainService eccsMainService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @RequestMapping("/sync")
    public ResultDto syncEccs(String organCode, AllBillsPublicDTO billsPublic) {
        if (StringUtils.isBlank(organCode) || billsPublic == null) {
            return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), ResultCode.PARAM_IS_BLANK.message(), null);
        }
        String code = "";
        String msg = "";
        PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(organCode, EAccountType.ECCS);
        if (pbcAccountDto != null) {
            try {
                AllAcct allAcct = allBillsPublicService.allBillsPublic2AllAcct(billsPublic);
                PbcUserAccount pbcUserAccount = systemPbcUser2PbcUser(pbcAccountDto);
                if (allAcct.getOperateType() == SyncOperateType.ACCT_OPEN) {
                    eccsMainService.eccsAccountOpenSync(pbcUserAccount, allAcct);
                } else if (allAcct.getOperateType() == SyncOperateType.ACCT_CHANGE) {
                    EccsSearchCondition condition = new EccsSearchCondition();
                    BeanCopierUtils.copyProperties(allAcct, condition);
                    eccsMainService.eccsAccountChangeSync(pbcUserAccount, allAcct, condition);
                } else {
                    return ResultDtoFactory.toApiError(ResultCode.ECCS_NONSUPPORT_OPERATETYPE.code(), ResultCode.ECCS_NONSUPPORT_OPERATETYPE.message(), null);
                }
                return ResultDtoFactory.toApiSuccess("");
            } catch (Exception e) {
                code = ResultCode.SYNC_FAILURE.code();
                msg = e.getMessage();
            }
        } else {
            List<PbcAccountDto> pbcAccountDtos = pbcAccountService.listByOrgCodeAndType(organCode, EAccountType.AMS);
            if (pbcAccountDtos == null || pbcAccountDtos.size() == 0) {
                code = ResultCode.ORGAN_NOT_CONFIG_SYNC_USER.code();
                msg = ResultCode.ORGAN_NOT_CONFIG_SYNC_USER.message();
            } else {
                code = ResultCode.NO_VALID_PBCACCOUNT.code();
                msg = ResultCode.NO_VALID_PBCACCOUNT.message();
            }
        }
        return ResultDtoFactory.toApiError(code, msg, null);
    }

    private PbcUserAccount systemPbcUser2PbcUser(PbcAccountDto pbcAccountDto) {
        PbcUserAccount pbcUserAccount = new PbcUserAccount();
        pbcUserAccount.setLoginIp(pbcAccountDto.getIp());
        pbcUserAccount.setLoginUserName(pbcAccountDto.getAccount());
        pbcUserAccount.setLoginPassWord(pbcAccountDto.getPassword());
        return pbcUserAccount;
    }

}
