package com.ideatech.ams.customer.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class CustomerPersonalInfo extends BaseMaintainableDto {


    private Long id;

    /**
     * 发证机关所在地的地区代码
     */


    private String cardAreaCode;
    /**
     * 国籍
     */


    private String country;
    /**
     * 联系电话
     */


    private String telephone;
    /**
     * 邮政编码
     */


    private String zipcode;
    /**
     * 地址(省市区)
     */


    private String address;
    /**
     * 详细地址
     */


    private String addressDetail;
    /**
     * 性别
     */


    private String sex;
    /**
     * 代理人名称
     */


    private String agentName;
    /**
     * 代理人身份证件种类
     */


    private String agentIdType;
    /**
     * 代理人身份证件号码
     */


    private String agentIdNo;
    /**
     * 代理人国籍
     */


    private String agentCountry;
    /**
     * 代理人电话
     */


    private String agentTelephone;
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
     * 扩展字段6
     */


    private String string006;
    /**
     * 扩展字段7
     */


    private String string007;
    /**
     * 扩展字段8
     */


    private String string008;
    /**
     * 扩展字段9
     */


    private String string009;
    /**
     * 扩展字段10
     */


    private String string010;

    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
        return "";
    }

    public void Validate() {
        // [客户ID]的必填验证
        // if (this.getCustomerId() == null) {
        // throw new RuntimeException("[客户ID]不能为空");
        // }
        // [创建人 ]的必填验证
        if (this.getCreatedBy() == null) {
            throw new RuntimeException("[创建人 ]不能为空");
        }
    }
}
