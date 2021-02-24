package com.ideatech.ams.risk.tradeRisk.dto;

import com.ideatech.common.entity.DealBase;
import lombok.Data;

/**
 * @author yangwz
 * @Description
 * @date 2019-11-02 10:42
 */
@Data
public class Risk2024Dto extends DealBase {
    private String orgName;//机构名称
    private String khtybh;//客户统一编号
    private String khmc;//客户名称
    private String nbjgh;//机构号
    private String ckzh;//存款账号
    private String ckzhlx;//账户类型
    private String hxjyrq;//核心交易日期
    private String scdhrq;//上次动户日期
    private String jdbz;//借贷标志
    private double jyje;//交易金额
    private String yxjgmc;//机构名称
    private String statDate;//日期
    private String status;//状态
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String yt;//用途
    private String jyqu;//交易渠道
}
