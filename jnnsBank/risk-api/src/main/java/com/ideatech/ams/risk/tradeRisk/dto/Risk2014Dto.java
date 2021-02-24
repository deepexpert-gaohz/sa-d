package com.ideatech.ams.risk.tradeRisk.dto;

import lombok.Data;

@Data
public class Risk2014Dto  {
   private  String cjrq;//采集日期
   private  String riskId;// 风险编号
   private  double riskAmt;// 风险金额
   private  String khId;// 客户编号
   private  String rkhName;// 客户名称
   private  String orgId;// 下发机构
   private  String orgName;//机构名称
   private  String khtybh;//客户统一编号
   private  String zzjgdm;//统一社会信用代码
   private  String khmc;//客户名称
   private  Double zczb;//注册资本
   private  String nbjgh;//机构号
   private  String yxjgmc;//机构名称
   private  String dfjyje;//贷交易金额
   private  Double jfjyje;//借交易金额
   private  String jcksrq;//检测开始日期
   private String yt;//用途
   private String jyqu;//交易渠道
}
