package com.ideatech.ams.apply.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 账户预约表
 * @author
 *
 */

@Table(name = "apply_account_info")
@Data
@Entity
public class ApplyAccountInfo extends BaseMaintainablePo {

    /**
     * 账户id
     */
    @Column(name = "open_account_log_id")
    private Long openAccoutLogId;

    /**
     * 账号 （开户完成后才能填入此项）
     */
    @Column(name = "acct_no")
    private String acctNo;

    /**
     * 账户类型 一般户 基本户
     */
    @Column(name = "acct_type")
    private String acctType;

    /**
     * 开户银行代码
     */
    @Column(name = "bank_code")
    private String bankCode;

    /**
     * 开户银行名称
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * 开户银行日期
     */
    @Column(name = "acct_create_date")
    private String acctCreateDate;

    /**
     * 开户证明文件种类
     */
    @Column(name = "acct_file_type")
    private String acctFileType;

    /**
     * 开户证明文件种类编号
     */
    @Column(name = "acct_file_no")
    private String acctFileNo;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;


}