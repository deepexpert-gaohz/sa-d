package com.ideatech.ams.readData;

import lombok.Data;

@Data
public class AlteritemMointor {

    private  String customerId;//客户号
    private  String creditNo;//统一社会信用号
    private  String companyName;//企业名称
    private  String isWarning;//是否预警
    private  String alterYest;//昨日变更时间
    private  String alterNow;//今日变更时间
    private  String alterItem;//变更项
    private  String alterBefore;//变更前
    private  String alterAfter;//变更后
    private  String ds;//预警来源
    private  String dataDt;//数据日期
    private  String etlDate;//跑批日期



    private  String fullId;//
    private  String code;//支行code
    private  String organFullId;//机构号

    private String warnTime;//预警时间

}
