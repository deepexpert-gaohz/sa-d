package com.ideatech.ams.risk.tradeRisk.dto;

import lombok.Data;

/**
 * @author yangwz
 * @Description 营业执照证件、法人身份证等证件到期，账户还在发生交易行为
 * @date 2019-10-30 21:41
 */

@Data
public class Risk2009Dto{
    private String cjrq;//采集日期

    private String riskId;// 风险编号

    private double riskAmt;// 风险金额

    private String khId;// 客户编号

    private String rkhName;// 客户名称

    private String orgId;// 下发机构
    private String orgName;//机构名称
    private String khtybh;//客户统一编号
    private String zzjgdm;//统一社会信用代码
    private String khmc;//客户名称
    private String nbjgh;//机构号
    private String yxjgmc;//机构名称
    private String ckzh;//存款账号
    private String qyzt;//企业状态
    private String hxjyrq;//核心交易日期
    private String jdbz;//借贷标志
    private double jyje;//交易金额
    private String dfhhm;//对方行名
    private String yt;//用途
    private String jyqu;//交易渠道
}
