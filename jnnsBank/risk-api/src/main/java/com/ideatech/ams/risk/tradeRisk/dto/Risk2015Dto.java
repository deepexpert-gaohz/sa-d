package com.ideatech.ams.risk.tradeRisk.dto;

import lombok.Data;

/**
 * @author yangwz
 * @Description
 * @date 2019-10-30 22:04
 */
@Data
public class Risk2015Dto {

    private String cjrq;//采集日期

    private String riskId;// 风险编号

    private double riskAmt;// 风险金额

    private String khId;// 客户编号

    private String rkhName;// 客户名称

    private String orgId;// 下发机构

    private String orgName;//机构名称

    private String khtybh;// 客户统一编号

    private String zzjgdm;// 统一社会信用代码

    private String yyzzyxjzrq;// 营业执照有效截止日期

    private String khmc;// 客户名称

    private String nbjgh;// 机构号

    private String yxjgmc;// 机构名称

    private String frdbmc;// 法人

    private String zjhm;// 证件号码

    private String yxq;// 有效期

    private String ckzh;// 存款账号

    private String hxjyrq;// 核心交易日期

    private String jdbz;// 借贷标志

    private double jyje;// 交易金额
    private String yt;//用途
    private String jyqu;//交易渠道
}
