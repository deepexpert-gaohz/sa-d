package com.ideatech.ams.account.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 变更详细表
 *
 * @author vantoo
 * @date 2018/5/16 13:53
 *
 */
@Entity
@Data
public class AccountChangeItem extends BaseMaintainablePo {

    /**
     * 变更表
     */
    private Long changeSummaryId;

    /**
     * 变更项字段英文名
     */
    private String columnName;
    /**
     * 变更项字段中文名
     */
    private String columnCName;
    /**
     * 字段变跟前值
     */
    @Column(length = 1000)
    private String oldValue;
    /**
     * 字段变更后值
     */
    @Column(length = 1000)
    private String newValue;


}
