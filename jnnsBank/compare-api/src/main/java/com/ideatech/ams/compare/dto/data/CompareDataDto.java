package com.ideatech.ams.compare.dto.data;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * 比对字段
 *
 * @author vantoo组织机构代码
 * @date 2019-01-17 16:54
 */
@Data
public class CompareDataDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 比对任务id
     */
    private Long taskId;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 存款人名称
     */
    private String depositorName;

    /**
     * 机构代码
     */
    private String organCode;

    /**
     * 机构fullId
     */

    private String organFullId;

    /**
     * 法人姓名
     */
    private String legalName;

    /**
     * 工商注册号，注册号
     */
    private String regNo;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 地址
     */
    private String regAddress;

    /**
     * 注册资金
     */
    private String registeredCapital;

    /**
     * 账户状态
     */
    private String accountStatus;

    /**
     * 国税
     */
    private String stateTaxRegNo;

    /**
     * 地税
     */
    private String taxRegNo;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 账户性质（中文全称）
     */
    private String acctType;

    /**
     * 账户开户日期
     */
    private String acctCreateDate;

    /**
     * 开户许可证核准号
     */
    private String accountKey;

    /**
     * 账户开户许可证核准号（L与Z）
     */
    private String accountLicenseNo;

    /**
     * 证明文件类型
     */
    private String fileType;

    /**
     * 证明文件编号
     */
    private String fileNo;

    /**
     * 电话号码
     */
    private String telephone;

    /**
     * 邮政编码
     */
    private String zipcode;

    /**
     * 存款人类别
     */
    private String depositorType;

    /**
     * 法人身份证件种类
     */
    private String legalIdcardType;

    /**
     * 法人身份证件编号
     */
    private String legalIdcardNo;

    /**
     * 行业归属
     */
    private String industryCode;

    /**
     * 经济行业分类
     */
    private String economyIndustry;

    /**
     * 注册地区代码
     */
    private String regAreaCode;

    /**
     * 登记部门
     */
    private String regOffice;

    /**
     * 销户日期
     */
    private String cancelDate;

    /**
     * 组织机构代码
     */
    private String orgCode;

    /**
     * 营业期限到期日
     */
    private String enddate;

    /**
     * 成立日期
     */
    private String opendate;

    /**
     * 备用字段
     */
    private String string001;
    private String string002;
    private String string003;
    private String string004;
    private String string005;
    private String string006;
    private String string007;
    private String string008;
    private String string009;
    private String string010;
    private String string011;
    private String string012;
    private String string013;
    private String string014;
    private String string015;
}
