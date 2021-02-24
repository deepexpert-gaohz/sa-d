package com.ideatech.ams.domain;

import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * 业务影像流水表
 */
@Data
@Entity
public class JnnsImageBillAll extends BaseMaintainablePo implements Serializable {
    private static final long serialVersionUID = 4214870702757570454L;
    /**
     * 账号
     */
    @Column(length = 50)
    private String acctNo;

    /**
     * 客户号
     */
    @Column(length = 50)
    private String customerNo;

    /**
     * 流水ID
     */
    @Column(length = 50)
    private String billId;

    /**
     * 影像批次号
     */
    @Column(length = 100)
    private String imageCode;

    /**
     * 行内流水号
     */
    @Column(length = 100)
    private String jnBillId;

    /**
     * 操作类型
     */
    @Column(length = 100)
    private BillType billType;

    /**
     * 账户状态
     */
    @Column(length = 100)
    private AccountStatus accountStatus;

    /**
     * 核心机构号
     */
    @Column(length = 100)
    private String organCode;

    /**
     * organFullId
     */
    @Column(length = 100)
    private String organFullId;

    /**
     * 业务日期
     */
    @Column(length = 100)
    private String saveImageDate;


    /**
     * 是否标志
     */
    @Column(length = 100)
    private String shifoubz;


    /**
     * 查询日期
     */
    @Column(length = 100)
    private String   busiStartDate;
}
