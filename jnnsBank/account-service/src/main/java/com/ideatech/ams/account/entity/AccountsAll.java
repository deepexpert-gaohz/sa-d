package com.ideatech.ams.account.entity;

import com.ideatech.ams.account.enums.AccountClass;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.OpenAccountSiteType;
import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 账户主表
 * @author clxry
 *
 */
@Entity
@Data
@Table(name = "ACCOUNTS_ALL",
        indexes = {@Index(name = "accounts_all_rbi_idx",columnList = "refBillId"),
                @Index(name = "accounts_all_cli_idx",columnList = "customerLogId"),
                @Index(name = "accounts_all_an_idx",columnList = "acctNo")})
public class AccountsAll extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_ACCOUNTS_ALL";

    /*
     * marked for 逻辑控制关联
     * @ManyToOne
     * @JoinColumn(name="CUSTOMERID") private CustomersAll customersAll;
     */
    /**
     * 客户id
     */
    @Column(length = 14)
    private Long customerLogId;

    /*
     * marked for 逻辑控制关联
     * @OneToMany(mappedBy = "accountsAll", cascade = CascadeType.REMOVE) private Set<BillsAll>
     * billsAlls = new HashSet<>();
     */
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
     * 账户简名
     */
    @Column(length = 255)
    private String acctShortName;
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
     * 人行机构编码
     */
    @Column(length = 14)
    private String bankCode;
    /**
     * 开户银行名称
     */
    @Column(length = 100)
    private String bankName;

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
     * 关联单据ID
     */
    @Column(length = 22)
    private Long refBillId;
    /**
     * 备注
     */
    @Column(length = 255)
    private String remark;
    //新增字段(用于列表条件筛选)
    /**
     * 账户性质(1基本存款账户 2预算单位专用存款账户 3临时机构临时存款账户 4非临时机构临时存款账户 5特殊单位专用存款账户 6一般存款账户 7 非预算单位专用存款账户)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CompanyAcctType acctType;

    /**
     * 是否为存量账户（1：是，null：不是）
     */
    @Column(length = 1)
    private String string003;

    /**
     * 影像补录状态（1：已补录，0：未补录）
     */
    @Column(length = 1)
    private String string004;

    /**
     * 是否白名单 （1：白名单）
     */
    @Column(length = 1)
    private String whiteList;

    /**
     * 账户有效期(临时账户)是否超期
     */
    @Column(length = 10)
    private Boolean isEffectiveDateOver;
    /**
     * 取消核准的标志位
     */
    @Column(length = 10)
    private Boolean cancelHeZhun;

    /**
     * 查询密码
     */
    private String selectPwd;

    /**
     * 开户许可证号
     */
    private String accountKey;

    /**
     * 开户许可证号（新）（取消核准）
     */
    private String openKey;
    /**
     * 本异地标识
     */
    private OpenAccountSiteType openAccountSiteType;
    /**
     * 原基本开户许可证号
     */
    private String oldAccountKey;

    /**
     * 扩展字段--保存基本户编号生成日期
     */
    private String string006;

    /**
     * 操作状态
     */
    private String ckeckStatus;


    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
        return "";
    }

    public void validate() {
        // [账户ID]的必填验证
        if (this.getId() == null) {
            throw new RuntimeException("[账户ID]不能为空");
        }
        // [账户分类(1个人2对公3金融)]的必填验证
        if (this.getAccountClass() == null) {
            throw new RuntimeException("[账户分类(1个人2对公3金融)]不能为空");
        }
        // [账户状态]的必填验证
        if (this.getAccountStatus() == null) {
            throw new RuntimeException("[账户状态]不能为空");
        }
        // [关联单据ID]的必填验证
        if (this.getRefBillId() == null) {
            throw new RuntimeException("[关联单据ID]不能为空");
        }
        // [创建人 ]的必填验证
        if (this.getCreatedBy() == null) {
            throw new RuntimeException("[创建人 ]不能为空");
        }

        if (this.getCreatedDate() == null) {
            throw new RuntimeException("[账户开户日期 ]不能为空");
        }

        if (StringUtils.isBlank(this.getBankCode())) {
            throw new RuntimeException("[开户银行人行编码 ]不能为空");
        }

        if (StringUtils.isBlank(this.getBankName())) {
            throw new RuntimeException("[开户银行 ]不能为空");
        }

    }
}
