package com.ideatech.ams.customer.entity.illegal;

import com.ideatech.ams.customer.enums.illegal.IllegalQueryExpiredStatus;
import com.ideatech.ams.customer.enums.illegal.IllegalQueryStatus;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 违法失信查询明细
 *
 */
@Table(name = "yd_illegal_query")
@Entity
@Data
public class IllegalQuery extends BaseMaintainablePo {

    /**
     * 注册号
     */
    private String regNo;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 违法状态
     */
    private IllegalQueryStatus illegalStatus;

    /**
     * 工商状态
     */
    private String saicStatus;

    /**
     * 是否存在经营异常
     */
    private Boolean isChangemess;

    /**
     * 机构号
     */
    private String organCode;

    /**
     * 批次号
     */
    private Long illegalQueryBatchId;

    /**
     * 营业执照到期日
     */
    private String fileEndDate;

    /**
     * 是否有经营异常
     */
    private IllegalQueryStatus changemess;

    /**
     * 机构FULLID
     */
    private String organFullId;

    /**
     * 营业执照是否过期判断
     */
    private IllegalQueryExpiredStatus fileDueExpired;

}
