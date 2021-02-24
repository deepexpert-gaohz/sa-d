package com.ideatech.ams.customer.entity.illegal;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 违法失信查询错误表
 */
@Entity
@Data
@Table(name = "illegal_query_error")
public class IllegalQueryError extends BaseMaintainablePo {

    /**
     * 企业名称
     *
     */
    private String companyName;

    /**
     * 注册号
     */
    private String regNo;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 关联批次号
     */
    private Long illegalQueryBatchId;

}
