package com.ideatech.ams.compare.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;


/**
 * 比对结果按机构统计
 */
@Entity
@Data
public class CompareStatistics extends BaseMaintainablePo {
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
