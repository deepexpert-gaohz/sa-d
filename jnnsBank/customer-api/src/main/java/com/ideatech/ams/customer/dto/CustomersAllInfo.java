package com.ideatech.ams.customer.dto;

import com.ideatech.ams.customer.enums.CustomerType;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * 客户信息基础表
 *
 * @author RJQ
 */

@Data
public class CustomersAllInfo extends BaseMaintainableDto {
    /**
     * id
     */
    private Long id;
    /**
     * 客户号
     */
    private String customerNo;
    /**
     * 存款人名称
     */
    private String depositorName;
    /**
     * 客户类型(1个人2对公)
     */
    
    private CustomerType customerClass;
    /**
     * 证件类型
     */
    private String credentialType;
    /**
     * 证件号码
     */
    private String credentialNo;
    /**
     * 证件到期日
     */
    private String credentialDue;
    /**
     * 机构fullId
     */
    private String organFullId;
    /**
     * 最新关联单据ID
     */
    private Long refBillId;

    private Long refCustomerBillId;

    /**
     * 最新日志表ID
     */
    private Long refCustomerLogId;

    private String organName;
}

