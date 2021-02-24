package com.ideatech.ams.ws.api.restful;

import com.ideatech.ams.account.dto.PbcSyncListDto;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsCheckResultInfo;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author vantoo
 * @date 14:44 2018/5/20
 */
@RestController
@RequestMapping("/api/pbc")
public class PbcApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PbcApiController.class);

    @Autowired
    private AmsMainService amsMainService;

    @Autowired
    private PbcAmsService pbcAmsService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    private final String DEFAULT_CODE = ResultCode.SYSTEM_BUSY.code();

    private final String DEFAULT_MESSAGE = ResultCode.SYSTEM_BUSY.message();

    @GetMapping("/check")
    public ResultDto checkPbcInfo(String accountKey, String regAreaCode, String organCode) {
        if (StringUtils.isBlank(accountKey) || StringUtils.isBlank(regAreaCode) || StringUtils.isBlank(organCode)) {
            return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), ResultCode.PARAM_IS_BLANK.message(), null);
        }

        String code = "";
        String msg = "";
        try {
            PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(organCode, EAccountType.AMS);
            if (pbcAccountDto != null) {
                AmsCheckResultInfo amsCheckResultInfo = pbcAmsService.checkPbcByAccountKeyAndRegAreaCode(organCode, accountKey, regAreaCode);
//                AmsCheckResultInfo amsCheckResultInfo = amsMainService.checkPbcByAccountKeyAndRegAreaCode(allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto), accountKey, regAreaCode);
                if (amsCheckResultInfo.isCheckPass()) {
                    return ResultDtoFactory.toApiSuccess(amsCheckResultInfo.getAmsAccountInfo());
                } else {
                    code = ResultCode.PBC_VALIDATION_NOT_PASS.code();
                    msg = amsCheckResultInfo.getNotPassMessage();
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
        } catch (Exception e) {
            code = ResultCode.PBC_VALIDATION_NOT_PASS.code();
            msg = e.getMessage();
        }
        return ResultDtoFactory.toApiError(code, msg, null);

    }

    @RequestMapping("/sync")
    public ResultDto syncPbcAms(String organCode, AllBillsPublicDTO billsPublic) {
        if (StringUtils.isBlank(organCode) || billsPublic == null) {
            return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), ResultCode.PARAM_IS_BLANK.message(), null);
        }

        String code = "";
        String msg = "";
        PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(organCode, EAccountType.AMS);
        if (pbcAccountDto != null) {
            try {
                AllAcct allAcct = allBillsPublicService.allBillsPublic2AllAcctPbc(billsPublic);
                amsMainService.amsAccountSync(allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto), allAcct);
                allBillsPublicService.save(billsPublic, null, true);
                return ResultDtoFactory.toApiSuccess("");
            } catch (Exception e) {
                //未完成流水特殊需要将详细信息返回到接口
                if (e instanceof BizServiceException) {
                    if (((BizServiceException) e).getError() == EErrorCode.BILL_UN_FINISHED) {
                        code = ResultCode.UN_FINISHED_BILL.code();
                        msg = ResultCode.UN_FINISHED_BILL.message();
                        return ResultDtoFactory.toApiError(code, msg, null);
                    }
                }
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

    @RequestMapping("/checkHz")
    public ResultDto checkHz() {
        List<PbcSyncListDto> pbcSyncListDtos = allBillsPublicService.checkPbcSync();
        if (CollectionUtils.isNotEmpty(pbcSyncListDtos)) {
            return ResultDtoFactory.toApiSuccess(pbcSyncListDtos);
        }
        return ResultDtoFactory.toApiSuccess("");
    }



}
