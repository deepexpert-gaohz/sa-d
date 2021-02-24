package com.ideatech.ams.risk.model.entity;


import lombok.Data;

@Data
public class RiskData {
    private String riskId;  //识别编号
    private String orgId;   //机构编号
    private String khId;    //客户编号
    private String cjrq;     //采集日期
    private String ofield;   //其他字段
    private String tableName; //表名
    private String minDate;
    private String maxDate;
    private String officeName; //机构名称
    private String officeFen;//所属分行
    private String modelId;//模型id
    private String ogId;// 机构编号 备用
    private String havaCjrq;//是否有cjrq字段
    /** add dt 员工监控 **/
    private String conditions;   //条件字段
    private String gh;   //工号
    private String params;   //参数

    private String dataBase;//跨用户数据库名

    //add by yangcq 20170311 添加统计结果 字段
    private int tjjg;
    //add by yangcq 20170311 添加模型名称 字段
    private String name ;
    private String dataSnField;//排序字段
    private String showFlagField;//显示字段
}
