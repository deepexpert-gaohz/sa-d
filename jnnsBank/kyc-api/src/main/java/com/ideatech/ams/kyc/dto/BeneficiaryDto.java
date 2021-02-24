package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class BeneficiaryDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 出资比例
     */
    private String capital;

    /**
     * capitalPercent
     */
    private String capitalpercent;

    /**
     * 证件到期日
     */
    private String idcarddue;

    /**
     * 证件号码
     */
    private String identifyno;

    /**
     * 证件类型
     */
    private String identifytype;

    /**
     * 受益人姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 受益人类型
     */
    private String type;

    /**
     * 出资链
     */
    private String capitalchain;

}