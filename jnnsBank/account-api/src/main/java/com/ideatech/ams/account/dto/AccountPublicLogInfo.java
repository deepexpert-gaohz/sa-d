package com.ideatech.ams.account.dto;

import com.ideatech.ams.account.enums.*;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;


@Data
public class AccountPublicLogInfo extends BaseMaintainableDto {

    /**
     * id
     */
    private Long id;

    /**
     * 业务经办人证件号码
     */


    private String operatorIdcardNo;
    /**
     * 业务经办人证件有效日期
     */


    private String operatorIdcardDue;
    /**
     * 业务经办人联系电话
     */


    private String operatorTelephone;
    /**
     * 备注
     */


    private String remark;
    /**
     * 关联单据ID
     */


    private Long refBillId;
    /**
     * 扩展字段1
     */


    private String string001;
    /**
     * 扩展字段2
     */


    private String string002;
    /**
     * 扩展字段3
     */


    private String string003;
    /**
     * 扩展字段4
     */


    private String string004;
    /**
     * 扩展字段5
     */


    private String string005;
    /**
     * 扩展字段6
     */


    private String string006;
    /**
     * 扩展字段7
     */


    private String string007;
    /**
     * 扩展字段8
     */


    private String string008;
    /**
     * 扩展字段9
     */


    private String string009;
    /**
     * 扩展字段10
     */


    private String string010;
    /**
     * 前一笔日志记录ID
     */


    private Long preLogId;

    /**
     * 账户ID
     */


    private Long accountId;
    /**
     * 账号
     */


    private String acctNo;

    /**
     * 账户名称
     */


    private String acctName;


    private String acctShortName;

    /**
     * 账号核准号
     */
    private String accountLicenseNo;

    /**
     * 账户分类(1个人2对公3金融)
     */


    private AccountClass accountClass;
    /**
     * 账户状态
     */
    private AccountStatus accountStatus;
    /**
     * 币种
     */


    private String currencyType;
    /**
     * 账户开户日期
     */


    private String acctCreateDate;
    /**
     * 账户激活日期
     */
    private String acctActiveDate;
    /**
     * 账户有效期(临时账户)
     */


    private String effectiveDate;
    /**
     * 开户原因
     */


    private String acctCreateReason;
    /**
     * 账户销户日期
     */


    private String acctCancelDate;
    /**
     * 销户原因
     */


    private String acctCancelReason;
    /**
     * 开户银行金融机构编码
     */


    private String bankCode;
    /**
     * 开户银行名称
     */


    private String bankName;
    /**
     * 客户ID
     */


    private Long customerId;
    /**
     * 客户号
     */


    private String customerNo;
    /**
     * 完整机构ID
     */
    private String organFullId;
    /**
     * 账户性质大类(1基本户、2一般户、3专用户、4临时户)
     */


    private AcctBigType acctBigType;
    /**
     * 账户性质(1基本存款账户 2预算单位专用存款账户 3临时机构临时存款账户 4非临时机构临时存款账户 5特殊单位专用存款账户 6一般存款账户 7 非预算单位专用存款账户)
     */


    private CompanyAcctType acctType;
    /**
     * 存款人类别
     */
    private String depositorType;
    /**
     * 开户证明文件种类1
     */


    private String acctFileType;
    /**
     * 开户证明文件编号1
     */


    private String acctFileNo;
    /**
     * 开户证明文件种类2
     */


    private String acctFileType2;
    /**
     * 开户证明文件编号2
     */


    private String acctFileNo2;
    /**
     * 账户名称构成方式
     */


    private String accountNameFrom;
    /**
     * 账户前缀
     */


    private String acctSuffix;
    /**
     * 账户后缀
     */


    private String acctPrefix;
    /**
     * 资金性质
     */


    private String capitalProperty;
    /**
     * 取现标识
     */


    private String enchashmentType;
    /**
     * 资金管理人姓名
     */

    private String fundManager;
    /**
     * 资金管理人身份证种类
     */


    private String fundManagerIdcardType;
    /**
     * 资金管理人身份证编号
     */


    private String fundManagerIdcardNo;
    /**
     * 资金管理人证件到期日
     */


    private String fundManagerIdcardDue;
    /**
     * 资金管理人联系电话
     */


    private String fundManagerTelephone;
    /**
     * 内设部门名称
     */


    private String insideDeptName;
    /**
     * 内设部门负责人名称
     */


    private String insideLeadName;
    /**
     * 内设部门负责人身份种类
     */


    private String insideLeadIdcardType;
    /**
     * 内设部门负责人身份编号
     */


    private String insideLeadIdcardNo;
    /**
     * 负责人证件到期日
     */


    private String insideLeadIdcardDue;
    /**
     * 内设部门联系电话
     */


    private String insideTelephone;
    /**
     * 内设部门邮编
     */


    private String insideZipcode;
    /**
     * 内设部门地址
     */


    private String insideAddress;
    /**
     * 非临时项目部名称
     */


    private String nontmpProjectName;
    /**
     * 非临时负责人姓名
     */


    private String nontmpLegalName;
    /**
     * 非临时联系电话
     */


    private String nontmpTelephone;
    /**
     * 非临时邮政编码
     */


    private String nontmpZipcode;
    /**
     * 非临时地址
     */


    private String nontmpAddress;
    /**
     * 非临时身份证件种类
     */


    private String nontmpLegalIdcardType;
    /**
     * 非临时身份证件编号
     */


    private String nontmpLegalIdcardNo;
    /**
     * 非临时身份证件到期日
     */


    private String nontmpLegalIdcardDue;
    /**
     * 业务经办人姓名
     */


    private String operatorName;
    /**
     * 业务经办人证件类型
     */


    private String operatorIdcardType;

    /**
     * 扩展字段1(用作条件查询)
     */
    private String operatorIdcardBeginDate;

    private String operatorIdcardEndDate;

    private String operatorIdcardDateOver;

    /**
     * 经办人到期日是否超期
     */
    private Boolean isOperatorIdcardDue;

    /**
     * 序号
     */


    private Long sequence;



    /**
     * zehngmingwenjan1
     */


    private String fileNo;

    /**
     * 本地异地标识(0本地、1异地)
     */
    private OpenAccountSiteType openAccountSiteType;




    private String checkStatus;

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
        // [账户状态]的必填验证
        if (this.getAccountStatus() == null) {
            throw new RuntimeException("[账户状态]不能为空");
        }
    }
}
