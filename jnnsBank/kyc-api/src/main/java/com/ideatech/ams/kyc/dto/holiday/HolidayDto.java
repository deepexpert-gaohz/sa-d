package com.ideatech.ams.kyc.dto.holiday;

import com.ideatech.ams.kyc.enums.HolidayTypeEnum;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * 节假日
 */
@Data
public class HolidayDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 节假日类型
     */
    private HolidayTypeEnum holidayType;

    /**
     * 日期格式：yyyy-MM-dd
     */
    private String dateStr;

}
