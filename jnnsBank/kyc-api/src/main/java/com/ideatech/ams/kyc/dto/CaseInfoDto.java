package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class CaseInfoDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 案由
     */
    private String casereason;

    /**
     * 案件结果
     */
    private String caseresult;

    /**
     * 案发时间
     */
    private String casetime;

    /**
     * 案件类型
     */
    private String casetype;

    /**
     * 执行类别
     */
    private String exesort;

    /**
     * 主要违法事实
     */
    private String illegfact;

    /**
     * 处罚金额
     */
    private String penam;

    /**
     * 处罚机关
     */
    private String penauth;

    /**
     * 处罚依据
     */
    private String penbasis;

    /**
     * 处罚决定书签发日期
     */
    private String pendesissdate;

    /**
     * 处罚执行情况
     */
    private String penexest;

    /**
     * 处罚结果
     */
    private String penresult;

    /**
     * 处罚种类
     */
    private String pentype;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

}