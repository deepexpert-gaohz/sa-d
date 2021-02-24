package com.ideatech.ams.risk.account.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.Serializable;


@Data
public class AccountPublic extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     *
     */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_ACCOUNT_PUBLIC";

    /**
     * 账户ID
     */
    /*
     *
     *
     * @Column(length = 22) private Long accountId;
     */
    private Long accountId;
    /**
     * 账户性质大类(1基本户、2一般户、3专用户、4临时户)
     */
    //@Enumerated(EnumType.STRING)
    @Column(length = 50)
   // private AcctBigType acctBigType;
    private String acctBigType;

    /**
     * 账户性质(1基本存款账户 2预算单位专用存款账户 3临时机构临时存款账户 4非临时机构临时存款账户 5特殊单位专用存款账户 6一般存款账户 7 非预算单位专用存款账户)
     */
    //@Enumerated(EnumType.STRING)
    @Column(length = 50)
    private String acctType;
    //private CompanyAcctType acctType;

    /**
     * 账户许可核准号
     */
    @Column(length = 30)
    private String accountLicenseNo;

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
     * 内设部门地址
     */
    @Column(length = 100)
    private String insideAddress;
    /**
     * 内设部门邮编
     */
    @Column(length = 6)
    private String insideZipcode;
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
    @Column(length = 500)
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
     * 完整机构ID
     */
    private String organFullId;

    /**
     * 本地异地标识(0本地、1异地)
     */
   // private OpenAccountSiteType openAccountSiteType;
     private Integer openAccountSiteType;

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

    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
        return "";
    }

    public void Validate() {
        // [账户ID]的必填验证
        //		if (this.getAccountId() == null) {
        //			throw new RuntimeException("[账户ID]不能为空");
        //		}
        if (this.getAcctType() == null) {
            throw new RuntimeException("[账户性质 ]不能为空");
        }

        if (StringUtils.isBlank(this.getAcctFileType())) {
            throw new RuntimeException("[开户证明文件种类1 ]不能为空");
        }

        if (StringUtils.isBlank(this.getAcctFileNo())) {
            throw new RuntimeException("[开户证明文件编号1 ]不能为空");
        }

        // [创建人 ]的必填验证
        if (this.getCreatedBy() == null) {
            throw new RuntimeException("[创建人 ]不能为空");
        }
    }
}

