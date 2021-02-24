package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class PunishBreakDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 年龄
     */
    private String ageclean;

    /**
     * 省份
     */
    private String areanameclean;

    /**
     * 法定代表人/负责人姓名
     */
    private String businessentity;

    /**
     * 身份证号码
     */
    private String cardnum;

    /**
     * 案号
     */
    private String casecode;

    /**
     * 执行法院
     */
    private String courtname;

    /**
     * 失信被执行人为具体情形
     */
    private String disrupttypename;

    /**
     * 生效法律文书确定的义务
     */
    private String duty;

    /**
     * 关注次数
     */
    private String focusnumber;

    /**
     * 执行依据文号
     */
    private String gistid;

    /**
     * 做出执行依据单位
     */
    private String gistunit;

    /**
     * 被执行人姓名/名称
     */
    private String inameclean;

    /**
     * 被执行人的履情况
     */
    private String performance;

    /**
     * 已履行
     */
    private String performedpart;

    /**
     * 公布时间
     */
    private String publishdateclean;

    /**
     * 立案时间
     */
    private String regdateclean;

    /**
     * 性别
     */
    private String sexyclean;

    /**
     * 失信人类型
     */
    private String type;

    /**
     * 未履行
     */
    private String unperformpart;

    /**
     * 身份证原始发地
     */
    private String ysfzd;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

}