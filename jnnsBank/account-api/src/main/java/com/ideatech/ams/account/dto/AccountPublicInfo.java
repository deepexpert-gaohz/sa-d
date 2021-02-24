package com.ideatech.ams.account.dto;

import com.ideatech.ams.account.enums.AcctBigType;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.OpenAccountSiteType;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class AccountPublicInfo extends BaseMaintainableDto {

    /**
     * id
     */
    private Long id;

    /**
     * 账户ID
     */


//    private AccountsAll accountsAll;
    private Long accountId;
    /**
     * 账户类型（1类 Ⅱ类 Ⅲ类） 非预算单位专用存款账户)
     */


    private String acctCategory;
    /**
     * 账户性质大类(1基本户、2一般户、3专用户、4临时户)
     */


    private AcctBigType acctBigType;

    /**
     * 账户性质(1基本存款账户 2预算单位专用存款账户 3临时机构临时存款账户 4非临时机构临时存款账户 5特殊单位专用存款账户 6一般存款账户 7 非预算单位专用存款账户)
     */


    private CompanyAcctType acctType;

    /**
     * 账户许可核准号
     */
    private String accountLicenseNo;

    /**
     * 存款人类别
     */
    private String depositorType;

    /**
     * 证明文件1
     */
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


    private String saccprefix;
    /**
     * 账户后缀
     */


    private String saccpostfix;
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
     * 经办人到期日是否超期
     */
    private Boolean isOperatorIdcardDue;
    /**
     * 备注
     */


    private String remark;

    /**
     * 完整机构ID
     */
    private String organFullId;

    /**
     * 本地异地标识(0本地、1异地)
     */
    private OpenAccountSiteType openAccountSiteType;

    private String string001;
    /**
     * 扩展字段2
     */


    private String string002;
    /**
     * 存量字段的标志位 (1:存量数据，null：非存量数据)
     * 扩展字段3
     *
     */
    private String string003;
    /**
     * 存量数据的影像补录状态的标志位 （1：已补录，0：未补录）
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

    private String currencyType;
    private String currency0;
    private String currency1;
    private String nationality;
    public void Validate() {
        // [账户ID]的必填验证
        // if (this.getAccountId() == null) {
        // throw new RuntimeException("[账户ID]不能为空");
        // }
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
