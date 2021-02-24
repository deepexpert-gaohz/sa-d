package com.ideatech.ams.apply.cryptography;

import com.ideatech.common.enums.CompanyAcctType;
import com.ideatech.common.enums.DepositorType;
import lombok.Data;

/**
 * 预约单修改传参 除了标记'必填',其它都是选填参数
 */
@Data
public class CryptoEditApplyAcctVo {
    /**
     * 预约编号(必填)
     */
    private String applyId;

    /**
     * 预约开户行网点银行的12位人行联行号(必填)
     */
    private String organId;

    /**
     * 创建人(必填) 银行内部的人员  预约受理人员
     */
    private String firstSupplyOperator;

    /**
     * 银行通知客户临柜时间(必填)
     */
    private String bankApplyTime;

    //账户信息
    /**
     * 账号
     */
    private String acctNo;

    /**
     * 账户性质
     */
    private CompanyAcctType acctType;

    /**
     * 账户名称
     */
    private String acctName;

    /**
     * 业务支行网点名称
     */
    private String bankName;

    /**
     * 业务支行网点十二位人行机构号
     */
    private String bankCode;

    /**
     * 申请开户原因
     * 1-建筑施工及安装 2-从事临时经营活动
     */
    private String acctCreateReason;

    /**
     * 开户日期(yyyy-MM-dd)
     */
    private String acctCreateDate;

    /**
     * 临时户有效期(yyyy-MM-dd)
     */
    private String effectiveDate;

    /**
     * 存款人名称
     */
    private String depositorName;

    /**
     * 存款人类别(01-企业法人 02-非法人企业 03-机关.....)
     */
    private String depositorType;

    /**
     * 机构英文名称
     */
    private String orgEnName;

    /**
     * 注册地址省份(省市区数据字典)
     */
    private String regProvince;
    /**
     * 注册地址市(省市区数据字典)
     */
    private String regCity;
    /**
     * 注册地址区(省市区数据字典)
     */
    private String regArea;
    /**
     * 注册地址详细地址
     */
    private String regAddress;
    /**
     * 工商注册地址
     */
    private String regFullAddress;
    /**
     * 注册地地区代码
     */
    private String regAreaCode;
    /**
     * 注册地邮编
     */
    private String regZipcode;
    /**
     * 行业归属(A-A：农、林、牧、渔业 B-B：采矿业 C-C：制造业.....)
     */
    private String industryCode;
    /**
     * 登记部门
     */
    private String regOffice;
    /**
     * 工商注册类型
     */
    private String regType;
    /**
     * 工商注册编号
     */
    private String regNo;
    /**
     * 证明文件1种类
     */
    private String fileType;
    /**
     * 证明文件1编号
     */
    private String fileNo;
    /**
     * 证明文件2种类
     */
    private String fileType2;
    /**
     * 证明文件2编号
     */
    private String fileNo2;
    /**
     * 开户证明文件种类
     */
    private String acctFileType;
    /**
     * 开户证明文件种类编号
     */
    private String acctFileNo;
    /**
     * 证明文件成立日期
     */
    private String fileSetupDate;
    /**
     * 证明文件到期日
     */
    private String fileDue;
    /**
     * 营业执照编号
     */
    private String businessLicenseNo;
    /**
     * 营业执照到期日
     */
    private String businessLicenseDue;
    /**
     * 未标明注册资金
     */
    private String isIdentification;
    /**
     * 资金性质
     */
    private String capitalProperty;
    /**
     * 注册资本币种
     */
    private String regCurrencyType;
    /**
     * 注册资本(元)
     */
    private String registeredCapital;
    /**
     * 经营范围
     */
    private String businessScope;
    //法人代表信息
    /**
     * 法人类型
     */
    private String legalType;
    /**
     * 姓名
     */
    private String legalName;
    /**
     * 证件类型
     */
    private String legalIdcardType;
    /**
     * 证件号码
     */
    private String legalIdcardNo;
    /**
     * 证件到期日
     */
    private String legalIdcardDue;
    /**
     * 联系电话
     */
    private String legalTelephone;
    /**
     * 法人地址
     */
    private String legalAddress;
    /**
     * 固定电话
     */
    private String legalFixedTel;
    /**
     * 邮箱
     */
    private String legalEmail;
    //组织机构代码证信息
    /**
     * 组织机构代码
     */
    private String orgCode;
    /**
     * 组织机构证到期日
     */
    private String orgCodeDue;
    //税务登记信息
    /**
     * 纳税人识别号（国税）
     */
    private String stateTaxRegNo;
    /**
     * 国税证件到期日
     */
    private String stateTaxDue;
    /**
     * 纳税人识别号（地税）
     */
    private String taxRegNo;
    /**
     * 地税证件到期日
     */
    private String taxDue;
    //联系信息
    /**
     * 联系电话
     */
    private String telephone;
    /**
     * 邮政编号
     */
    private String zipcode;
    /**
     * 与注册地是否一致
     */
    private String isSameRegistArea;
    /**
     * 注册地址省份
     */
    private String workProvince;
    /**
     * 注册地址市
     */
    private String workCity;
    /**
     * 注册地址区
     */
    private String workArea;
    /**
     * 办公地址详细地址
     */
    private String workAddress;
    /**
     * 财务主管姓名
     */
    private String financeName;
    /**
     * 财务主管电话
     */
    private String financeTelephone;
    /**
     * 财务主管证件类型
     */
    private String financeIdcardType;
    /**
     * 财务主管证件号码
     */
    private String financeIdcardNo;
    /**
     * 对账联系人姓名
     */
    private String billContactName;
    /**
     * 对账联系人姓名手机
     */
    private String billContactMobile;
    /**
     * 对账邮编
     */
    private String billZipcode;
    /**
     * 对账单邮寄地址
     */
    private String billAddress;
    //上级机构信息
    /**
     * 机构名称
     */
    private String parCorpName;
    /**
     * 基本户开户许可核准号
     */
    private String parAccountKey;
    /**
     * 统一社会信用代码
     */
    private String parUnitycreditcode;
    /**
     * 组织机构代码
     */
    private String parOrgCode;
    /**
     * 机构信用代码
     */
    private String parOrgEccsNo;
    /**
     * 机构信用证件到期日
     */
    private String parOrgEccsDue;
    /**
     * 登记注册号类型
     */
    private String parRegType;
    /**
     * 登记注册号码
     */
    private String parRegNo;
    /**
     * 法人类型
     */
    private String parLegalType;
    /**
     * 法人姓名
     */
    private String parLegalName;
    /**
     * 法人证件类型
     */
    private String parLegalIdcardType;
    /**
     * 法人证件号码
     */
    private String parLegalIdcardNo;
    /**
     * 法人证件到期日
     */
    private String parLegalIdcardDue;
    /**
     * 法人手机号码
     */
    private String parLegalTelephone;
    /**
     * 邮箱
     */
    private String parLegalEmail;
    //授权经办人信息
    /**
     * 姓名
     */
    private String authOperator1Name;
    /**
     * 联系电话
     */
    private String authOperator1Phone;
    /**
     * 证件类型
     */
    private String authOperator1IdcardType;
    /**
     * 证件号码
     */
    private String authOperator1IdcardNo;
    //建筑工及安装单位信息
    /**
     * 项目名称
     */
    private String nontmpProjectName;
    /**
     * 负责人姓名
     */
    private String nontmpLegalName;
    /**
     * 证件类型
     */
    private String nontmpLegalIdcardType;
    /**
     * 证件号码
     */
    private String nontmpLegalIdcardNo;
    /**
     * 联系电话
     */
    private String nontmpTelephone;
    /**
     * 邮政编码
     */
    private String nontmpZipcode;
    /**
     * 地址
     */
    private String nontmpAddress;
    //内设部门信息
    /**
     * 内设部门名称
     */
    private String insideDeptName;
    /**
     * 负责人姓名
     */
    private String insideLeadName;
    /**
     * 证件类型
     */
    private String insideLeadIdcardType;
    /**
     * 证件号码
     */
    private String insideLeadIdcardNo;
    /**
     * 联系电话
     */
    private String insideTelephone;
    /**
     * 邮政编码
     */
    private String insideZipcode;
    /**
     * 地址
     */
    private String insideAddress;
}
