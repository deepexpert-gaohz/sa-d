package com.ideatech.ams.risk.model.domain;

/**
 * @Author: yinjie
 * @Date: 2019/5/5 9:17
 */
public class ModelCountDo {

    private Long id;
    private String cjrq;//数据日期
    //private String name;
    // 流程名称是什么字段啊,
    private String flowName;
    private String modelName;//模型名称
    private String riskId;//风险点编号
    private String orgName;//机构名称
    private String khId;//客户编号
    private String khName;//客户名称
    private String riskAmt;//风险金额

    public ModelCountDo(Long id, String cjrq, String modelName, String riskId, String orgName, String khId, String khName, String riskAmt) {
        this.id = id;
        this.cjrq = cjrq;
        this.modelName = modelName;
        this.riskId = riskId;
        this.orgName = orgName;
        this.khId = khId;
        this.khName = khName;
        this.riskAmt = riskAmt;
    }
}
