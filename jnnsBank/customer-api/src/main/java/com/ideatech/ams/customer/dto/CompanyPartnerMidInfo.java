package com.ideatech.ams.customer.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.util.Date;


@Data
public class CompanyPartnerMidInfo extends BaseMaintainableDto {

    /**
     * id
     */
    private Long id;

    /**
     * 伙伴ID
     */


    private Long partnerId;
    /**
     * 客户ID
     */


    private Long customerId;
    /**
     * 企业名称
     */


    private String name;
    /**
     * 高管/股东
     */


    private String partnerType;
    /**
     * 关系人类型；选1-高管时；类型包括： 实际控制人、董事长、总经理（主要负责人）、财务负责人、监事长、法定代表人 选 2-股东时；类型包括：自然人、机构
     */


    private String roleType;
    /**
     * 证件类型
     */


    private String idcardType;
    /**
     * 证件编号
     */


    private String idcardNo;
    /**
     * 联系电话
     */


    private String partnerTelephone;
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


    private Long customerPublicMidId;

    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
        return "";
    }

    public void Validate() {
        // [伙伴ID]的必填验证
        if (this.getPartnerId() == null) {
            throw new RuntimeException("[伙伴ID]不能为空");
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
