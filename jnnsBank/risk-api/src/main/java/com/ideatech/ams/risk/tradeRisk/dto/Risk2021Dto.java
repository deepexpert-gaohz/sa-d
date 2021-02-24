package com.ideatech.ams.risk.tradeRisk.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
/**
 * create by yangcq 20191030 于西安神州数码科技园
 */
public class Risk2021Dto {
    private String cjrq;//采集日期

    private String riskId;// 风险编号

    private double riskAmt;// 风险金额

    private String khId;// 客户编号

    private String rkhName;// 客户名称

    private String orgId;// 下发机构
    /**
     *客户统一编号
     */
    private String khtybh;
    /**
     *客户名称
     */
    private String khmc;
    /**
     *内部机构号
     */
    private String nbjgh;
    /**
     *机构名称
     */
    private String yxjgmc;
    /**
     *存款账号
     */
    private String ckzh;
    /**
     *核心交易日期
     */
    private String hxjyrq;
    /**
     *借贷标志
     */
    private String jdbz;
    /**
     *交易金额
     */
    private Double jyje;
    /**
     *对方账号
     */
    private String dfzh;
    /**
     *对方户名
     */
    private String dfhm;
    private String dfhhm;//对方行名
    private String yt;//用途
    private String jyqu;//交易渠道
}
