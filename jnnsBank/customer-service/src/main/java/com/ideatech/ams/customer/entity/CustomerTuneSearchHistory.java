package com.ideatech.ams.customer.entity;


import com.ideatech.ams.customer.enums.CustomerTuneSearchEntranceType;
import com.ideatech.ams.customer.enums.CustomerTuneSearchType;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 客户综合尽调查询历史
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "yd_customer_tune_history")
@Data
public class CustomerTuneSearchHistory extends BaseMaintainablePo {

    /**
     * 机构ID
     */
    private Long organId;

    /**
     * 机构FullID
     */
    private String organFullId;

    /**
     * 客户综合尽调查询分类
     */
    private CustomerTuneSearchType type;

    /**
     * 客户综合尽调查询入口类型
     * （如：运营商校验，除了“客户综合尽调”这个入口点击查询，记录日志以外，还有其他地方可以进行运营商校验）
     */
    private CustomerTuneSearchEntranceType entranceType;


    /**
     * 账号
     */
    private String acctNo;

    /**
     * 存款人名称
     */
    private String depositorName;

    /**
     * 人行用户名
     */
    private String pbcAccount;


    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 身份证
     */
    private String cardNo;

    /**
     * 查询验证结果
     */
    private String result;


    /**
     * 企业名称
     */
    private String customerName;

    /**
     * 组织机构代码
     */
    private String orgCode;

    /**
     * 开户许可证核准号
     */
    private String accountKey;


    /**
     * 查询关联id
     */
    private Long refId;

    /**
     * 核查人员类型
     */
    private String idCardedType;

    /**
     * 注册地（住所地）地区代码
     */
    private String regAreaCode;

    /**
     * 存款人类别
     */
    private String depositorType;

    /**
     * 工商营业执照注册号
     */
    private String fileNo;

    /**
     * 法人证件类型
     */
    private String legalIdcardTypeAms;

    /**
     * 法人证件编号
     */
    private String lgalIdcardNo;

    /**
     * 银行机构代码
     */
    private String bankCode;

    /**
     * 银行机构名称
     */
    private String bankName;


}
