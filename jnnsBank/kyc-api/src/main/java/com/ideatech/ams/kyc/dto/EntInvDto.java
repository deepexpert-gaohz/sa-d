package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class EntInvDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 注销⽇期
     */
    private String candate;

    /**
     * 出资⽅式
     */
    private String conform;

    /**
     * 企业总数量
     */
    private String binvvamount;

    /**
     * 注册地址⾏政区编号
     */
    private String regorgcode;

    /**
     * 认缴出资币种
     */
    private String congrocur;

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
     * 认缴出资额(万元)
     */
    private String subconam;

    /**
     * 开业日期
     */
    private String esdate;

    /**
     * 出资比例
     */
    private String fundedratio;

    /**
     * 法定代表⼈姓名
     */
    private String name;

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