package com.ideatech.ams.account.entity;

import com.ideatech.ams.account.enums.AcctBigType;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BatchSuspendSourceEnum;
import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import javax.persistence.*;

/**
 * 批量久悬任务表
 * @author van
 * @date 2018/7/14 21:05
 *
 */
@Data
@Entity
@Table(name = "yd_batch_suspend")
public class BatchSuspendPo extends BaseMaintainablePo {

    /**
     * 流水信息
     */
    private Long billId;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 客户号
     */
    private String customerNo;

    /**
     * 存款人名称
     */
    private String depositorName;

    /**
     * 行内机构号
     */
    private String organCode;

    /**
     * 机构FullId
     */
    private String organFullId;

    /**
     * 账户性质
     */
    @Enumerated(EnumType.STRING)
    private CompanyAcctType acctType;

    /**
     * 账户性质大类
     */
    @Enumerated(EnumType.STRING)
    private AcctBigType acctBigType;

    /**
     * 是否已上报
     */
    @Enumerated(EnumType.STRING)
    private CompanyIfType syncStatus;

    /**
     * 是否处理完成
     */
    @Enumerated(EnumType.STRING)
    private CompanyIfType processed;

    /**
     * 批量久悬类型
     */
    @Enumerated(EnumType.STRING)
    private BatchSuspendSourceEnum type;

    /**
     * 错误信息
     */
    @Lob
    private String errorMessage;

}
