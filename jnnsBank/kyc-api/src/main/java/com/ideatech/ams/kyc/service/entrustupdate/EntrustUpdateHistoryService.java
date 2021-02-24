package com.ideatech.ams.kyc.service.entrustupdate;

import com.ideatech.ams.kyc.dto.entrustupdate.EntrustUpdateHistoryDto;
import com.ideatech.common.service.BaseService;

import java.util.List;

public interface EntrustUpdateHistoryService extends BaseService<EntrustUpdateHistoryDto> {

    /**
     * 返回该企业委托更新过程中的记录
     * @param companyName
     * @param updateStatus
     * @return
     */
    List<EntrustUpdateHistoryDto> listByCompanyNameAndUpdateStatus(String companyName, Boolean updateStatus);
}
