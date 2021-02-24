package com.ideatech.ams.customer.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;
import org.apache.commons.lang.StringUtils;


@Data
public class CompanyPartnerInfo extends BaseMaintainableDto implements Comparable<CompanyPartnerInfo>{

    /**
     * id
     */
    private Long id;

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

    /**
     * 对公客户信息
     */
    private Long customerPublicId;

    @Override
    public int compareTo(CompanyPartnerInfo o) {
        if(StringUtils.isNotBlank(o.getString001()) && StringUtils.isNotBlank(this.getString001())){
            return -(Integer.parseInt(o.getString001()) - Integer.parseInt(this.getString001()));
        }else{
            return 0;
        }
    }
}
