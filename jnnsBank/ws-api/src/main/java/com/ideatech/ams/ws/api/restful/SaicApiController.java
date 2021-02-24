package com.ideatech.ams.ws.api.restful;

import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vantoo
 * @date 14:44 2018/5/20
 */
@RestController
@RequestMapping("/api/saic")
public class SaicApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PbcApiController.class);

    @Autowired
    private SaicInfoService saicInfoService;

    @GetMapping("/check")
    public ResultDto checkSaicInfo(String name ,String key) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(key)) {
            return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), ResultCode.PARAM_IS_BLANK.message(), null);
        }

        SaicIdpInfo saicIdpInfo = saicInfoService.getSaicInfoFull(SearchType.EXACT, name, key, null);
        if (saicIdpInfo != null) {
            return ResultDtoFactory.toApiSuccess(saicIdpInfo);
        } else {
            return ResultDtoFactory.toApiError(ResultCode.NO_DATA_EXIST.code(), ResultCode.NO_DATA_EXIST.message(), null);
        }

    }

}
