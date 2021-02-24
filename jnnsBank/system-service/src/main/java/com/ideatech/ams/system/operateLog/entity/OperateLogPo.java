package com.ideatech.ams.system.operateLog.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 操作记录
 */
@Entity
@Table(name = "sys_operate_log",indexes = {@Index(name = "Operate_Log_ai_idx",columnList = "refBillId")})
@Data
public class OperateLogPo extends BaseMaintainablePo implements Serializable {
    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635341L;
    /**
     * 审核失败原因
     */
    @Column(length = 1000)
    private String failMsg;
    /**
     * 机构号
     */
    @Column(length = 15)
    private String organCode;

    /**
     * 关联单据ID
     */
    @Column(length = 22)
    private Long refBillId;
    /**
     * 操作人
     */
    @Column(length = 30)
    private String operateName;
    /**
     * 操作时间
     */
    @Column(length = 50)
    private String operateDate;
    /**
     * 操作类型
     */
    @Column(length = 50)
    private String operateType;
}
