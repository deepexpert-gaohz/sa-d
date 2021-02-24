package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.pbc.dto.EccsAccountInfo;
import com.ideatech.ams.pbc.dto.EccsSearchCondition;
import com.ideatech.common.msg.ObjectRestResponse;

public interface EccsApiService {
    ObjectRestResponse<EccsAccountInfo> getEccsAccountInfoByCondition(String bankCode, EccsSearchCondition condition) throws Exception;
}
