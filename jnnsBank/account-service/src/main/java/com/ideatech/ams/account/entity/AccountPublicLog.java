package com.ideatech.ams.account.entity;

import com.ideatech.ams.account.enums.*;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 对公账户日志表
 * @author
 */

@Entity
@Table(name = "ACCOUNT_PUBLIC_LOG")
@Data
public class AccountPublicLog extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     *
     */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_ACCOUNT_PUBLIC_LOG";

    /**
     * 业务经办人证件号码
     */
    @Column(length = 50)
    private String operatorIdcardNo;
    /**
     * 业务经办人证件有效日期
     */
    @Column(length = 10)
    private String operatorIdcardDue;
    /**
     * 业务经办人联系电话
     */
    @Column(length = 50)
    private String operatorTelephone;
    /**
     * 备注
     */
    @Column(length = 255)
    private String remark;
    /**
     * 关联单据ID
     */
    @Column(length = 22)
    private Long refBillId;
    /**
     * 扩展字段1
     */
    @Column(length = 255)
    private String string001;
    /**
     * 扩展字段2
     */
    @Column(length = 255)
    private String string002;
    /**
     * 扩展字段3
     */
    @Column(length = 255)
    private String string003;
    /**
     * 扩展字段4
     */
    @Column(length = 255)
    private String string004;
    /**
     * 扩展字段5
     */
    @Column(length = 255)
    private String string005;
    /**
     * 扩展字段6
     */
    @Column(length = 255)
    private String string006;
    /**
     * 扩展字段7
     */
    @Column(length = 255)
    private String string007;
    /**
     * 扩展字段8
     */
    @Column(length = 255)
    private String string008;
    /**
     * 扩展字段9
     */
    @Column(length = 255)
    private String string009;
    /**
     * 扩展字段10
     */
    @Column(length = 255)
    private String string010;
    /**
     * 前一笔日志记录ID
     */
    @Column(length = 22)
    private Long preLogId;
    /**
     * 账户ID
     */
    @Column(length = 22)
    private Long accountId;
    /**
     * 账号
     */
    @Column(length = 32)
    private String acctNo;

    /**
     * 账户名称
     */
    @Column(length = 255)
    private String acctName;

    /**
     * 账户简称
     */
    @Column(length = 255)
    private String acctShortName;

    /**
     * 账号核准号
     */
    @Column(length = 30)
    private String accountLicenseNo;

    /**
     * 账户分类(1个人2对公3金融)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private AccountClass accountClass;
    /**
     * 账户状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private AccountStatus accountStatus;
    /**
     * 币种
     */
    @Column(length = 10)
    private String currencyType;
    /**
     * 账户开户日期
     */
    @Column(length = 10)
    private String acctCreateDate;
    /**
     * 账户激活日期
     */
    @Column(length = 10)
    private String acctActiveDate;
    /**
     * 账户有效期(临时账户)
     */
    @Column(length = 10)
    private String effectiveDate;
    /**
     * 开户原因
     */
    @Column(length = 255)
    private String acctCreateReason;
    /**
     * 账户销户日期
     */
    @Column(length = 10)
    private String cancelDate;
    /**
     * 销户原因
     */
    @Column(length = 255)
    private String acctCancelReason;
    /**
     * 久悬日期
     */
    @Column(length = 10)
    private String acctSuspenDate;
    /**
     * 开户银行金融机构编码
     */
    @Column(length = 14)
    private String bankCode;
    /**
     * 开户银行名称
     */
    @Column(length = 100)
    private String bankName;
    /**
     * 客户ID
     *
     */
    @Column(length = 22)
    private Long customerId;
    /**
     * 客户号
     */
    @Column(length = 50)
    private String customerNo;
    /**
     * 完整机构ID
     */
    private String organFullId;
    /**
     * 账户性质大类(1基本户、2一般户、3专用户、4临时户)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private AcctBigType acctBigType;
    /**
     * 账户性质(1基本存款账户 2预算单位专用存款账户 3临时机构临时存款账户 4非临时机构临时存款账户 5特殊单位专用存款账户 6一般存款账户 7 非预算单位专用存款账户)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CompanyAcctType acctType;
    /**
     * 存款人类别
     */
    @Column(length = 30)
    private String depositorType;
    /**
     * 开户证明文件种类1
     */
    @Column(length = 30)
    private String acctFileType;
    /**
     * 开户证明文件编号1
     */
    @Column(length = 100)
    private String acctFileNo;
    /**
     * 开户证明文件种类2
     */
    @Column(length = 30)
    private String acctFileType2;
    /**
     * 开户证明文件编号2
     */
    @Column(length = 100)
    private String acctFileNo2;
    /**
     * 账户名称构成方式
     */
    @Column(length = 50)
    private String accountNameFrom;
    /**
     * 账户前缀
     */
    @Column(length = 100)
    private String saccprefix;
    /**
     * 账户后缀
     */
    @Column(length = 100)
    private String saccpostfix;
    /**
     * 资金性质
     */


    @Column(length = 255)
    private String capitalProperty;
    /**
     * 取现标识
     */
    @Column(length = 30)
    private String enchashmentType;
    /**
     * 资金管理人姓名
     */
    @Column(length = 100)
    private String fundManager;
    /**
     * 资金管理人身份证种类
     */
    @Column(length = 30)
    private String fundManagerIdcardType;
    /**
     * 资金管理人身份证编号
     */
    @Column(length = 50)
    private String fundManagerIdcardNo;
    /**
     * 资金管理人证件到期日
     */
    @Column(length = 10)
    private String fundManagerIdcardDue;
    /**
     * 资金管理人联系电话
     */
    @Column(length = 50)
    private String fundManagerTelephone;
    /**
     * 内设部门名称
     */
    @Column(length = 100)
    private String insideDeptName;
    /**
     * 内设部门负责人名称
     */
    @Column(length = 50)
    private String insideLeadName;
    /**
     * 内设部门负责人身份种类
     */
    @Column(length = 50)
    private String insideLeadIdcardType;
    /**
     * 内设部门负责人身份编号
     */
    @Column(length = 50)
    private String insideLeadIdcardNo;
    /**
     * 负责人证件到期日
     */
    @Column(length = 10)
    private String insideLeadIdcardDue;
    /**
     * 内设部门联系电话
     */
    @Column(length = 50)
    private String insideTelephone;
    /**
     * 内设部门邮编
     */
    @Column(length = 6)
    private String insideZipcode;
    /**
     * 内设部门地址
     */
    @Column(length = 100)
    private String insideAddress;
    /**
     * 非临时项目部名称
     */
    @Column(length = 255)
    private String nontmpProjectName;
    /**
     * 非临时负责人姓名
     */
    @Column(length = 100)
    private String nontmpLegalName;
    /**
     * 非临时联系电话
     */
    @Column(length = 50)
    private String nontmpTelephone;
    /**
     * 非临时邮政编码
     */
    @Column(length = 6)
    private String nontmpZipcode;
    /**
     * 非临时地址
     */
    @Column(length = 100)
    private String nontmpAddress;
    /**
     * 非临时身份证件种类
     */
    @Column(length = 30)
    private String nontmpLegalIdcardType;
    /**
     * 非临时身份证件编号
     */
    @Column(length = 50)
    private String nontmpLegalIdcardNo;
    /**
     * 非临时身份证件到期日
     */
    @Column(length = 10)
    private String nontmpLegalIdcardDue;
    /**
     * 业务经办人姓名
     */
    @Column(length = 100)
    private String operatorName;
    /**
     * 业务经办人证件类型
     */
    @Column(length = 30)
    private String operatorIdcardType;

    /**
     * 经办人到期日是否超期
     */
    @Column(length = 10)
    private Boolean isOperatorIdcardDue;

    /**
     * 序号
     */
    @Column(length = 10)
    private Long sequence;

    /**
     * 本地异地标识(0本地、1异地)
     */
    private OpenAccountSiteType openAccountSiteType;

    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
        return "";
    }

    public void Validate() {
        // [关联单据ID]的必填验证
        if (this.getRefBillId() == null) {
            throw new RuntimeException("[关联单据ID]不能为空");
        }
        // [创建人 ]的必填验证
        if (this.getCreatedBy() == null) {
            throw new RuntimeException("[创建人 ]不能为空");
        }
        // [日志ID]的必填验证
        if (this.getId() == null) {
            throw new RuntimeException("[日志ID]不能为空");
        }
        // [账户ID]的必填验证
        if (this.getAccountId() == null) {
            throw new RuntimeException("[账户ID]不能为空");
        }
        // [账户分类(1个人2对公3金融)]的必填验证
        if (this.getAccountClass() == null) {
            throw new RuntimeException("[账户分类(1个人2对公3金融)]不能为空");
        }
        // [账户状态]的必填AccountsAll.java:154验证
        /*
         * if (this.getStatus() == null || this.getStatus().equals("")) { throw new
         * RuntimeException("[账户状态]不能为空"); }
         */
    }
}
