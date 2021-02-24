package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.pbc.dto.AmsJibenUniqueCheckCondition;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DefaultAmsResetJiBenPrintServiceImpl implements DefaultAmsResetJiBenPrintService {

    @Autowired
    private PbcSearchService pbcSearchService;

    @Override
    public ResultDto jiBenResetDetails(String acctNo, String pbcCode) {
        String code = "";
        String msg = "";
        if (StringUtils.isBlank(acctNo)) {
            return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), "账号不能为空!", null);
        }
        if (StringUtils.isBlank(pbcCode)) {
            return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), "人行机构号不能为空!", null);
        }

        try {
            log.info("接口模式调用基本户补打校验");
            return pbcSearchService.jiBenResetDetails(acctNo, pbcCode);
        } catch (Exception e) {
            if (e.getMessage().contains("Connection timed out")) {
                code = ResultCode.NETWORK_CONNECTION_ERROR.code();
                msg = ResultCode.NETWORK_CONNECTION_ERROR.message();
            } else {
                code = ResultCode.PBC_CHECKDETAIL_FAILURE.code();
                msg = e.getMessage();
            }
        }

        return ResultDtoFactory.toApiError(code, msg, null);
    }

    @Override
    public ResultDto jiBenResetPrint(String acctNo, String pbcCode) {
        String code = "";
        String msg = "";

        try {
            log.info("接口模式调用基本户补打反显新的编号以及注册号");
            return pbcSearchService.jiBenResetPrint(acctNo,pbcCode);
        }catch (Exception e){
            if(e.getMessage().contains("Connection timed out")){
                code = ResultCode.NETWORK_CONNECTION_ERROR.code();
                msg = ResultCode.NETWORK_CONNECTION_ERROR.message();
            }else{
                code = ResultCode.PBC_CHECKDETAIL_FAILURE.code();
                msg = e.getMessage();
            }
        }
        return ResultDtoFactory.toApiError(code, msg, null);
    }
}
