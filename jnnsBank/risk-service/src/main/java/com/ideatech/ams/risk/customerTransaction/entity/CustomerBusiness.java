package com.ideatech.ams.risk.customerTransaction.entity;


import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="customer_business")
@Getter
@Setter
@Generated
public class CustomerBusiness  extends BaseMaintainablePo {
    private String status; //返回结果状态
    private String message;//返回结果消息

    //工商数据信息
    private String name;//单位名称
    private String historyName;//历史名称
    private String registNo;//注册号
    private String unityCreditCode;//统一社会信用代码
    private String organizationType;//单位类型
    private String legalPerson;//法定代表人
    private String registFund;//注册资本
    private String openDate;//成立日期
    private String startDate;//营业期限起始日期
    private String endDate;//营业期限终止日期
    private String registOrgan;//登记机关
    private String licenseDate;//核准日期
    private String state;//登记状态
    private String address;//住所地址
    @Column(length=1000)
    private String scope;//经营范围
    private String revokeDate;//注销或吊销日期
    private String isOnStock;//是否上市
    private String priIndustry;//国民经济行业分类大类名称
    private String subIndustry;//国民经济行业分类小类名称
    private String legalPersonSurname;//法人姓
    private String legalPersonName;//法人名
    private String registCapital;//注册资金
    private String registCurrency;//注册资金币种
    private String industryCategoryCode;//行业门类代码
    private String industryLargeClassCode;//行业大类代码
    private String typeCode;//企业类型代码
    private String country;//国家
    private String province;//省
    private String city;//市
    private String area;//区/县
    @Column(length=2000)
    private String changes;//变更信息
    private Boolean ischanges;//是否变更

    private String organFullId;//机构fullId
    private String corporateBank;//法人机构行标识
    private String orgCode;


}
