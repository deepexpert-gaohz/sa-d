package com.ideatech.ams.kyc.dto.entrustupdate;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class EntrustUpdateHistoryDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 是否委托更新成功 true-成功 默认false
     */
    private Boolean updateStatus = Boolean.FALSE;

}
