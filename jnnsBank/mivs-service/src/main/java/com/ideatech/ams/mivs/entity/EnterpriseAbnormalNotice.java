package com.ideatech.ams.mivs.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019/7/25.
 * 5.9	企业异常核查通知报文
 */

@Entity
@Data
@Table(name = "mivs_enterprise_abnormal")
public class EnterpriseAbnormalNotice extends MessageHeader {

    /**
     * 单位名称
     */
    @Column(length = 600)
    private String coNm;

    /**
     * 统一社会信用代码
     */
    @Column(length = 36)
    private String uniSocCdtCd;

    /**
     * 手机号码
     */
    @Column(length = 26)
    private String phNb;

    /**
     * 姓名
     */
    @Column(length = 280)
    private String nm;


    /**
     * 异常核查类型
     */
    @Column(length = 8)
    private String abnmlType;

    /**
     * 异常内容说明
     */
    @Column(length = 1000)
    private String dESC;

    /**
     * 扩展字段
     */
    private String String001;
    private String String002;
    private String String003;
    private String String004;
    private String String005;
    private String String006;
    private String String007;
    private String String008;
    private String String009;
    private String String010;
    private String String011;
    private String String012;
    private String String013;
    private String String014;
    private String String015;
}
