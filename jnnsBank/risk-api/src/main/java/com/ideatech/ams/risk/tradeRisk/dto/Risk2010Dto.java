package com.ideatech.ams.risk.tradeRisk.dto;

import lombok.Data;

/**
 * @author yangwz
 * @Description 对公客户企业已经被注销或者吊销，但仍存在账户交易行为(对公客户主动提醒)
 * @date 2019-10-30 21:41
 */

@Data
public class Risk2010Dto{
    private String cjrq;//采集日期

    private String riskId;// 风险编号

    private double riskAmt;// 风险金额

    private String khId;// 客户编号

    private String rkhName;// 客户名称

    private String orgId;// 下发机构
    private String orgName;//机构名称
    private String khtybh;//客户统一编号
    private String khmc;//客户名称
    private String nbjgh;//机构号
    private String yxjgmc;//机构名称
    private String ckzh;//存款账号
    private String hxjyrq;//核心交易日期
    private String jdbz;//借贷标志
    private double jyje;//交易金额
    private String dfzh;//对方账号
    private String dfhm;//对方户名
    private String dfhhm;//对方行名
    private String yt;//用途
    private String jyqu;//交易渠道

}
