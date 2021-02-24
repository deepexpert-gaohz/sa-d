package com.ideatech.ams.risk.tradeRisk.dto;

import lombok.Data;

@Data
public class Risk2011Dto {
   private String cjrq;//采集日期
   private String riskId;// 风险编号
   private double riskAmt;// 风险金额
   private String khId;// 客户编号
   private String rkhName;// 客户名称
   private String orgId;// 下发机构
   private String orgName;//下发机构
   private String khtybh;//机构名称
   private String khmc ;//客户统一编号
   private String nbjgh;//客户名称
   private String yxjgmc;//机构号
   private String ckzh;//机构名称
   private String hxjyrq;//存款账号
   private String jdbz;//核心交易日期
   private Double jyje;//借贷标志
   private String dfxh;//交易金额
   private String dfzh;//对方行号
   private String dfhm;//对方账号
   private String dfxzqh;//对方户名
   private String dfhhm;//对方行名
   private String yt;//用途
   private String jyqu;//交易渠道
}
