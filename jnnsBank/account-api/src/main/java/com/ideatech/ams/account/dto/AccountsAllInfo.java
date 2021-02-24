package com.ideatech.ams.account.dto;

import com.ideatech.ams.account.enums.AccountClass;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.OpenAccountSiteType;
import com.ideatech.common.dto.BaseMaintainableDto;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;

@Data
public class AccountsAllInfo extends BaseMaintainableDto {

    /**
     * id
     */
    private Long id;

    /**
     * 客户id
     */
    private Long customerLogId;

    /**
     * 账号
     */
    private String acctNo;
    /**
     * 账户名称
     */
    private String acctName;

    /**
     * 账户简名
     */
    private String acctShortName;
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
    private String regCurrencyType;
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
    private String cancelDate;
    /**
     * 销户原因
     */
    private String acctCancelReason;
    /**
     * 久悬日期
     */
    private String acctSuspenDate;
    /**
     * 开户银行金融机构编码
     */
    private String bankCode;
    /**
     * 开户银行名称
     */
    private String bankName;

    /**
     * 客户号
     */
    private String customerNo;
    /**
     * 完整机构ID
     */
    private String organFullId;
    /**
     * 关联单据ID
     */
    private Long refBillId;
    /**
     * 备注
     */
    private String remark;
    //新增字段(用于列表条件筛选)
    /**
     * 账户性质(1基本存款账户 2预算单位专用存款账户 3临时机构临时存款账户 4非临时机构临时存款账户 5特殊单位专用存款账户 6一般存款账户 7 非预算单位专用存款账户)
     */
    private CompanyAcctType acctType;
    /**
     * 开户开始时间
     */
    private String beginDateAcctCreate;
    /**
     * 开户结束时间
     */
    private String endDateAcctCreate;

    /**
     * 是否为存量账户（1：是，null：不是）
     */
    private String string003;

    /**
     * 影像补录状态（1：已补录，0：未补录）
     */
    private String string004;

    /**
     * 扩展字段--保存基本户编号生成日期
     */
    private String string006;

    /**
     * 是否白名单
     */
    private String whiteList;

    /**
     * 账户有效期(临时账户)是否超期
     */
    private Boolean isEffectiveDateOver;

    /**
     * 是否取消核准标上报标志位
     */
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
     * 操作状态
     */
    private String ckeckStatus;



    /**
     * 开户许可证号（新）（取消核准）（基本存款账户编号）
     */
    private String openKey;
    /**
     * 本异地标识
     */
    private OpenAccountSiteType openAccountSiteType;

    private String oldAccountKey;

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
            throw new RuntimeException("[开户银行金融编码 ]不能为空");
        }

        if (StringUtils.isBlank(this.getBankName())) {
            throw new RuntimeException("[开户银行 ]不能为空");
        }

    }
}
