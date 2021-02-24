package com.ideatech.ams.compare.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class CompareStatisticsDto extends BaseMaintainableDto {

    private Long id;

    public CompareStatisticsDto() {
    }

    public CompareStatisticsDto(long count, long success) {
        this.count = count;
        this.success = success;
    }

    /**
     * 机构id
     */
    private Long organId;

    /**
     * 比对任务id
     */
    private Long compareTaskId;

    /**
     * 总数
     */
    private Long count;
    /**
     * 成功数
     */
    private Long success;

}
