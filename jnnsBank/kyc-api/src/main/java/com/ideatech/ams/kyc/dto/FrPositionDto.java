package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class FrPositionDto extends BaseMaintainableDto {

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
     * 是否法定代表人
     */
    private String lerepsign;

    /**
     * 法定代表人名称
     */
    private String name;

    /**
     * 职务
     */
    private String position;

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
     * 序列号 来自IDP
     */
    private Integer index;

}