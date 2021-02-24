package com.ideatech.ams.customer.dto.neecompany;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class FreshCompanyConfigDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 启动开关
     */
    private Boolean unlimited;

    /**
     * 省份code
     */
    private String provinceCode;

    /**
     * 每天执行时间点
     */
//    private String times;

    /**
     * 开始日期
     */
    private String beginDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 任务执行周期
     */
//    private String excuteCycle;

    /**
     * 获取范围
     */
    private String selectRange;

    /**
     * 获取范围选择(按天数)
     */
    private String dayRange;

}
