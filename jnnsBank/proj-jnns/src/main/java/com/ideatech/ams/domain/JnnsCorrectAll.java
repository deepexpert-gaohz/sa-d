package com.ideatech.ams.domain;

import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;


/**
 * 业务冲正记录表
 */
@Data
@Entity
public class JnnsCorrectAll extends BaseMaintainablePo implements Serializable {

    private static final long serialVersionUID = 4214870702757570454L;

    /**
     * 账号
     */
    @Column(length = 50)
    private String acctNo;

    /**
     * 企业名称
     */
    @Column(length = 50)
    private String acctName;

    /**
     * 客户号
     */
    @Column(length = 50)
    private String customerNo;

    /**
     * 客户名称
     */
    @Column(length = 50)
    private String customerName;

    /**
     * 行内流水号
     */
    @Column(length = 100)
    private String jnBillId;

    /**
     * 流水ID
     */
    @Column(length = 50)
    private String billId;

    /**
     * 操作类型
     */
    @Column(length = 100)
    private BillType billType;

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
     * 账户类型
     */
    @Column(length = 100)
    private CompanyAcctType acctType;

    /**
     * 账户状态
     */
    @Column(length = 100)
    private AccountStatus accountStatus;
}
