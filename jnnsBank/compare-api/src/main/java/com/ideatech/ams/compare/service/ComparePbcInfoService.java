package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.ComparePbcInfoDto;

public interface ComparePbcInfoService {
    ComparePbcInfoDto findById(Long id);
    void saveComparePbcInfo(ComparePbcInfoDto comparePbcInfoDto);

    /**
     * 获取有效期内的人行本地数据
     * @param acctNo
     * @return
     */
    ComparePbcInfoDto getComparePbcInfoBaseLocal(String acctNo);
}
