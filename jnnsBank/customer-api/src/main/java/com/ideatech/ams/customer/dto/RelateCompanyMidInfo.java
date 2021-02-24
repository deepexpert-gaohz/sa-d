package com.ideatech.ams.customer.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.util.Date;


@Data
public class RelateCompanyMidInfo extends BaseMaintainableDto {

    /**
     * id
     */
    private Long id;

    /**
     * 关联ID
     */


    
    private Long relateId;
    /**
     * 客户ID
     */


    
    private Long customerId;
    /**
     * 关联企业名称
     */


    
    private String relateCompanyName;
    /**
     * 企业证件类型
     */


    
    private String companyCertificateType;
    /**
     * 企业证件号码
     */


    
    private String companyCertificateNo;
    /**
     * 企业证件名称
     */


    
    private String companyCertificateName;
    /**
     * 组织机构代码
     */


    
    private String companyOrgCode;
    /**
     * 法人姓名
     */


    
    private String companyLegalName;
    /**
     * 机构信用代码
     */


    
    private String companyOrgEccsNo;
    /**
     * 最新关联单据ID
     */


    
    private Long refBillId;
    /**
     * 扩展字段1
     */


    
    private String string001;
    /**
     * 扩展字段2
     */


    
    private String string002;
    /**
     * 扩展字段3
     */


    
    private String string003;
    /**
     * 扩展字段4
     */


    
    private String string004;
    /**
     * 扩展字段5
     */


    
    private String string005;


    //  private CustomerPublicMid customerPublicMid;
    private Long customerPublicMidId;

    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
        return "";
    }

    public void Validate() {
        // [关联ID]的必填验证
        if (this.getRelateId() == null) {
            throw new RuntimeException("[关联ID]不能为空");
        }
        // [客户ID]的必填验证
        if (this.getCustomerId() == null) {
            throw new RuntimeException("[客户ID]不能为空");
        }
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
