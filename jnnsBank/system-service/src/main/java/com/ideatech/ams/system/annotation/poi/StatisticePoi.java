package com.ideatech.ams.system.annotation.poi;

import lombok.Data;

import java.sql.Clob;

@Data
public class StatisticePoi {

    /**
     * 接口调用类型
     */
    private String tranType;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 接口调用结果
     */
    private String processResult;

    /**
     * 接口调用日期
     */
    private String tranDate;

    /**
     * 报送类型
     */
    private String billType;

    /**
     * 传入参数
     */
    private Clob requestMsg;

    /**
     * 返回参数
     */
    private Clob responseMsg;

    /**
     * 错误信息
     */
    private String errorCode;

}
