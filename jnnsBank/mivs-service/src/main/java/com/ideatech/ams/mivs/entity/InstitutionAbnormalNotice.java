package com.ideatech.ams.mivs.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019/7/24.
 * 5.8	机构异常核查通知报文
 */

@Entity
@Data
@Table(name = "mivs_institution_abnormal")
public class InstitutionAbnormalNotice extends MessageHeader {

    /**
     * 银行营业网点或非银行支付机构行号
     */
    @Column(length = 28)
    private String orgnlInstgPty;

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
