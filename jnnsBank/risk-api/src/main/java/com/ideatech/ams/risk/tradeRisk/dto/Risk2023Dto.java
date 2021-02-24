package com.ideatech.ams.risk.tradeRisk.dto;

import com.ideatech.common.entity.DealBase;
import lombok.Data;

@Data
/**
 * 短期内 频繁收付的大额存款、整存零付或零收整付且金额大致相当的账户
 */
public class Risk2023Dto extends DealBase {
    private String  stat_date;//日期
    private String  status;//状态
    private String  start_time;//开始时间
    private String  end_time;//结束时间
    private String  org_name;//机构名称
    private String  khtybh;//客户统一编号
    private String  khmc;//客户名称
    private String  nbjgh;//机构号
    private String  yxjgmc;//机构名称
    private String  ckzh;//存款账号
    private String  hxjyrq;//核心交易日期
    private String  jdbz;//借贷标志
    private String  jyje;//交易金额
    private String  dfzh;//对方账号
    private String  dfhm;//对方户名
    private String dfhhm;//对方行名
    private String yt;//用途
    private String jyqu;//交易渠道
}
