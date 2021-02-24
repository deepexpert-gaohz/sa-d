package com.ideatech.ams.apply.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CompanyPreOpenAccountEntSaicDto extends BaseMaintainableDto {

	/**
     * 存款人名称
     */
    private String depositorName;

    /**
     * 存款人类别
     */
    private String depositorType;
    
    /**
     * 机构英文名称
     */
    private String orgEnName;
    /**
     * 注册省份
     */
    private String regProvince;
    /**
     * 注册地区省份中文名
     */
    private String regProvinceChname;
    /**
     * 注册城市
     */
    private String regCity;
    /**
     * 注册城市中文名
     */
    private String regCityChname;
    /**
     * 注册地区
     */
    private String regArea;
    /**
     * 注册地区中文名
     */
    private String regAreaChname;
    /**
     * 注册详细地址
     */
    private String regAddress;
    /**
     * 完整注册地址(与营业执照一致)
     */
    private String regFullAddress;
    /**
     * 注册地地区代码
     */
    private String regAreaCode;
    /**
     * 行业归属
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
     * 证明文件1种类(工商注册类型)
     */
    private String fileType;
    /**
     * 证明文件1编号(工商注册号)
     */
    private String fileNo;
    /**
     * 证明文件2种类(工商注册类型)
     */
    private String fileType2;
    /**
     * 证明文件2编号
     */
    private String fileNo2;
    /**
     * 证明文件1设立日期
     */
    private String fileSetupDate;
    /**
     * 证明文件1到期日
     */
    private String fileDue;
    /**
     * 营业执照号码
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
     * 注册资本币种
     */
    private String regCurrencyType;
    /**
     * 注册资金（元）
     */
    private BigDecimal registeredCapital;
    /**
     * 经营（业务）范围
     */
    private String businessScope;
    /**
     * 法人类型（法定代表人、单位负责人）
     */
    private String legalType;
    /**
     * 法人姓名
     */
    private String legalName;
    /**
     * 法人证件类型
     */
    private String legalIdcardType;
    /**
     * 法人证件编号
     */
    private String legalIdcardNo;
    /**
     * 法人证件到期日
     */
    private String legalIdcardDue;
    /**
     * 法人联系电话
     */
    private String legalTelephone;
    /**
     * 预约信息id
     */
    private Long companyPreOpenAcccountId;

    /**
     * 受理按钮点击时间
     */
    private String acceptTimes;

}
