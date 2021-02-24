package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dto.SaicSearchHistorySearchDto;
import com.ideatech.ams.kyc.enums.SearchStatus;
import com.ideatech.ams.kyc.enums.SearchType;

public interface SaicSearchHistoryService {
    /**
     * 保存客户尽调查询历史记录
     * @param name
     * @param username
     * @param realTimeUrl
     * @param searchStatus
     * @param searchType
     * @param orgfullid
     */
    void save(String name, String username, String realTimeUrl, SearchStatus searchStatus, SearchType searchType, String orgfullid);

    /**
     * 分页查询客户尽调历史记录
     * @param saicSearchHistorySearchDto
     * @return
     */
    SaicSearchHistorySearchDto search(SaicSearchHistorySearchDto saicSearchHistorySearchDto);
}

