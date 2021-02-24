package com.ideatech.ams.dto.saic;

import lombok.Data;

@Data
public class Stockholders {
    private String   name;   //股东名称
    private String   type;    //股东类型
    private String  strType;    //股东类型
    private String  identifyType;   //证照/证件类型
    private String  identifyNo;      //证照/证件号码
    private String  investType;      //认缴出资方式
    private String  subconam;        //认缴出资金额
    private String   conDate;        //认缴出资日期
    private String  realType;        //实缴出资方式
    private String   realAmount;     //实缴出资额
    private String  realDate;        //实缴出资日期
    private String   regCapCur;       //币种
    private String  fundedRatio;      //出资比例

}
