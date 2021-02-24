package com.ideatech.ams.annual.dto.poi;

import lombok.Data;

@Data
public class AnnualStatisticsPoi {

    /**
     * 机构
     */
    private String name;

    /**
     * 账号总数
     */
    private Integer annualTotalCount;

    /**
     * 通过总数
     */
    private Integer annualPassCount;

    /**
     * 通过率
     */
    private String annualPassRate;

}
