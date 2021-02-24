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
public class Risk2022Dto {
    private String cjrq;//采集日期

    private String riskId;// 风险编号

    private double riskAmt;// 风险金额

    private String khId;// 客户编号

    private String rkhName;// 客户名称

    private String orgId;// 下发机构
    /**
     * 客户统一编号
     */
    private String  khtybh;
    /**
     *客户名称
     */
    private String  khmc;
    /**
     *内部机构号
     */
    private String  nbjgh;
    /**
     *机构名称
     */
    private String  yxjgmc;
    /**
     *存款账号
     */
    private String  ckzh;
    /**
     *存款账号类型
     */
    private String  ckzhlx;
    /**
     *开户日期
     */
    private String  khrq;
    /**
     *营业执照有效截止日期
     */
    private char  yyzzyxjzrq;
    /**
     *成立日期
     */
    private char  clrq;
    private String yt;//用途
    private String jyqu;//交易渠道
}
