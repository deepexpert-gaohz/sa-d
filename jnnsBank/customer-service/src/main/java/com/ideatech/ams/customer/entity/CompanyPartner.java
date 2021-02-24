package com.ideatech.ams.customer.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 企业股东/高管信息
 *
 * @author RJQ
 *
 */
@Entity
@Table(name = "COMPANY_PARTNER")
@Data
public class CompanyPartner extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_COMPANY_PARTNER";

    /*
     *
     *  private Long companyPartnerMidId;
     */
    /**
     * 客户ID
     */
    @Column(length = 14)
    private Long customerId;
    /**
     * 企业名称
     */
    @Column(length = 20)
    private String name;
    /**
     * 高管/股东
     */
    @Column(length = 20)
    private String partnerType;
    /**
     * 关系人类型；选1-高管时；类型包括： 实际控制人、董事长、总经理（主要负责人）、财务负责人、监事长、法定代表人 选 2-股东时；类型包括：自然人、机构
     */
    @Column(length = 100)
    private String roleType;
    /**
     * 证件类型
     */
    @Column(length = 20)
    private String idcardType;
    /**
     * 证件编号
     */
    @Column(length = 50)
    private String idcardNo;
    /**
     * 联系电话
     */
    @Column(length = 50)
    private String partnerTelephone;
    /**
     * 最新关联单据ID
     */
    @Column(length = 22)
    private Long refBillId;
    /**
     * 扩展字段1
     */
    @Column(length = 255)
    private String string001;
    /**
     * 扩展字段2
     */
    @Column(length = 255)
    private String string002;
    /**
     * 扩展字段3
     */
    @Column(length = 255)
    private String string003;
    /**
     * 扩展字段4
     */
    @Column(length = 255)
    private String string004;
    /**
     * 扩展字段5
     */
    @Column(length = 255)
    private String string005;
    /**
     * 对公客户信息
     */
    private Long customerPublicId;

    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
        return "";
    }

    public void Validate() {
        // [主键ID]的必填验证
        if (this.getId() == null) {
            throw new RuntimeException("[主键ID]不能为空");
        }
        // [客户ID]的必填验证
        /*
         * if (this.getCustomerId() == null) { throw new RuntimeException("[客户ID]不能为空"); }
         */
        // [最新关联单据ID]的必填验证
        if (this.getRefBillId() == null) {
            throw new RuntimeException("[最新关联单据ID]不能为空");
        }
        // [创建人 ]的必填验证
        if (this.getCreatedBy() == null) {
            throw new RuntimeException("[创建人 ]不能为空");
        }
    }
}
