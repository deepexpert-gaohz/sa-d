package com.ideatech.ams.apply.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.ideatech.common.entity.BaseMaintainablePo;

import lombok.Data;

/**
 * 预约补录信息
 * @author wanghongjie
 *
 * @version 2018-07-14 17:55
 */
@Entity
@Table(name = "yd_companypre_saic")
@Data
public class CompanyPreOpenAccountEntSaic extends BaseMaintainablePo {

    /**
     * 存款人名称
     */
	@Column(length = 255)
    private String depositorName;

    /**
     * 存款人类别
     */
	@Column(length = 255)
    private String depositorType;
    
    /**
     * 机构英文名称
     */
	@Column(length = 255)
    private String orgEnName;
    /**
     * 注册省份
     */
	@Column(length = 255)
    private String regProvince;
    /**
     * 注册地区省份中文名
     */
	@Column(length = 255)
    private String regProvinceChname;
    /**
     * 注册城市
     */
	@Column(length = 255)
    private String regCity;
    /**
     * 注册城市中文名
     */
	@Column(length = 255)
    private String regCityChname;
    /**
     * 注册地区
     */
	@Column(length = 255)
    private String regArea;
    /**
     * 注册地区中文名
     */
	@Column(length = 255)
    private String regAreaChname;
    /**
     * 注册详细地址
     */
	@Column(length = 255)
    private String regAddress;
    /**
     * 完整注册地址(与营业执照一致)
     */
	@Column(length = 255)
    private String regFullAddress;
    /**
     * 注册地地区代码
     */
	@Column(length = 255)
    private String regAreaCode;
    /**
     * 行业归属
     */
	@Column(length = 255)
    private String industryCode;
    /**
     * 登记部门
     */
	@Column(length = 255)
    private String regOffice;
    /**
     * 工商注册类型
     */
	@Column(length = 255)
    private String regType;
    /**
     * 工商注册编号
     */
	@Column(length = 255)
    private String regNo;
    /**
     * 证明文件1种类(工商注册类型)
     */
	@Column(length = 255)
    private String fileType;
    /**
     * 证明文件1编号(工商注册号)
     */
	@Column(length = 255)
    private String fileNo;
    /**
     * 证明文件2种类(工商注册类型)
     */
	@Column(length = 255)
    private String fileType2;
    /**
     * 证明文件2编号
     */
	@Column(length = 255)
    private String fileNo2;
    /**
     * 证明文件1设立日期
     */
	@Column(length = 255)
    private String fileSetupDate;
    /**
     * 证明文件1到期日
     */
	@Column(length = 255)
    private String fileDue;
    /**
     * 营业执照号码
     */
	@Column(length = 255)
    private String businessLicenseNo;
    /**
     * 营业执照到期日
     */
	@Column(length = 255)
    private String businessLicenseDue;
    /**
     * 未标明注册资金
     */
	@Column(length = 255)
    private String isIdentification;
    /**
     * 注册资本币种
     */
	@Column(length = 255)
    private String regCurrencyType;
    /**
     * 注册资金（元）
     */
	@Column(length = 255)
    private BigDecimal registeredCapital;
    /**
     * 经营（业务）范围
     */
    @Lob
    private String businessScope;
    /**
     * 法人类型（法定代表人、单位负责人）
     */
	@Column(length = 255)
    private String legalType;
    /**
     * 法人姓名
     */
	@Column(length = 255)
    private String legalName;
    /**
     * 法人证件类型
     */
	@Column(length = 255)
    private String legalIdcardType;
    /**
     * 法人证件编号
     */
	@Column(length = 255)
    private String legalIdcardNo;
    /**
     * 法人证件到期日
     */
	@Column(length = 255)
    private String legalIdcardDue;
    /**
     * 法人联系电话
     */
	@Column(length = 255)
    private String legalTelephone;
    /**
     * 预约信息id
     */
    @Column(length = 20)
    private Long companyPreId;

    /**
     * 受理按钮点击时间
     */
    @Column(length = 50)
    private String acceptTimes;
}
