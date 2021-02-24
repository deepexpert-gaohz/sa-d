package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class StockHolderDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 出资日期
     */
    private String condate;

    /**
     * 出资比例
     */
    private String fundedratio;

    /**
     * 名称
     */
    private String name;

    /**
     * 币种
     */
    private String regcapcur;

    /**
     * 股东类型
     */
    private String strtype;

    /**
     * 认缴出资额
     */
    private String subconam;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 证件到期日
     */
    private String idcarddue;

    /**
     * 证件号码
     */
    private String idcardno;

    /**
     * 证件类型
     */
    private String idcardtype;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 实缴出资额
     */
    private String realamount;

    /**
     * 实缴出资日期
     */
    private String realdate;

    /**
     * 实缴出资方式
     */
    private String realtype;

    /**
     * 认缴出资方式
     */
    private String investtype;

}