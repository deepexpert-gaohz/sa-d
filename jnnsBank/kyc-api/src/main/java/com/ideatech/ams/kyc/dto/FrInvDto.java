package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class FrInvDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 注销日期
     */
    private String candate;

    /**
     * 认缴出资币种
     */
    private String currency;

    /**
     * 企业(机构)名称
     */
    private String entname;

    /**
     * 企业状态
     */
    private String entstatus;

    /**
     * 企业(机构)类型
     */
    private String enttype;

    /**
     * 开业日期
     */
    private String esdate;

    /**
     * 出资比例
     */
    private String fundedratio;

    /**
     * 注册资本(万元)
     */
    private String regcap;

    /**
     * 注册资本币种
     */
    private String regcapcur;

    /**
     * 注册号
     */
    private String regno;

    /**
     * 登记机关
     */
    private String regorg;

    /**
     * 吊销日期
     */
    private String revdate;

    /**
     * 认缴出资额(万元)
     */
    private String subconam;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

}