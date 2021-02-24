package com.ideatech.ams.customer.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 关联企业信息
 *
 * @author RJQ
 *
 */
@Entity
@Table(name = "RELATE_COMPANY")
@Data
public class RelateCompany extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_RELATE_COMPANY";

    /**
     * 客户ID
     */
    @Column(length = 14)
    private Long customerId;

    /**
     * 关联企业名称
     */
    @Column(length = 200)
    private String relateCompanyName;
    /**
     * 企业证件类型
     */
    @Column(length = 200)
    private String companyCertificateType;
    /**
     * 企业证件号码
     */
    @Column(length = 50)
    private String companyCertificateNo;
    /**
     * 企业证件名称
     */
    @Column(length = 200)
    private String companyCertificateName;
    /**
     * 组织机构代码
     */
    @Column(length = 100)
    private String companyOrgCode;
    /**
     * 法人姓名
     */
    @Column(length = 20)
    private String companyLegalName;
    /**
     * 机构信用代码
     */
    @Column(length = 100)
    private String companyOrgEccsNo;
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
     * 客户ID
     */
    /*
     *
     *
     * @Column(length = 22) private Long customerId;
     */

    /**
     * 对公客户信息
     */


//    @ManyToOne
//    /* (cascade = { CascadeType.ALL }, fetch = FetchType.LAZY) */
//    @JoinColumn(name = "publicId", referencedColumnName = "yd_id")
    private Long customerPublicId;

    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
        return "";
    }

    public void Validate() {
        // [最新关联单据ID]的必填验证
        if (this.getRefBillId() == null) {
            throw new RuntimeException("[最新关联单据ID]不能为空");
        }
        // [创建人 ]的必填验证
        if (this.getCreatedBy() == null) {
            throw new RuntimeException("[创建人 ]不能为空");
        }
        // [主键ID]的必填验证
        if (this.getId() == null) {
            throw new RuntimeException("[主键ID]不能为空");
        }
        // [客户ID]的必填验证
        /*
         * if (this.getCustomerId() == null) { throw new RuntimeException("[客户ID]不能为空"); }
         */
    }
}
