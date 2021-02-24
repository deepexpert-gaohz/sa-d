package com.ideatech.ams.system.annotation.dto;

import com.ideatech.common.util.ClobUtils;
import lombok.Data;
import java.sql.Clob;

@Data
public class MessageLogDto {

    private static final int DEFAULT_CLOB_COLUMN_LENGTH = 64000;

    private static final int DEFAULT_MAX_CHAR_COLUMN_LENGTH = 4000;


    private Long id;
    /**
     * 流水号
     */
    private Long billId;

    /**
     * 数据操作类型：
     *      ACCT_OPEN("新开户"),
     *
     *     ACCT_CHANGE("变更"),
     *
     *     ACCT_SUSPEND("久悬"),
     *
     *     ACCT_SEARCH("查询"),
     *
     *     ACCT_REVOKE("销户"),
     */
    private String billType;

    /**
     * 存款人姓名
     */
    private String depositorName;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 账户性质
     */
    private String acctType;

    /**
     * 交易类型  AMS:人行  ECCS：机构  SAIC：工商
     */
    private String tranType;

    /**
     * 查询状态
     */
    private String resultStatus;

    /**
     * 报送结果
     */
    private String processResult;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 操作类型 AMS:人行  ECCS：机构  SAIC：工商
     */
    private String operationType;

    /**
     * 请求方法
     */
    private String reqMethod;

    /**
     * 请求报文
     */
    private Clob requestMsg;

    /**
     * 响应报文
     */
    private Clob responseMsg;

    /**
     * 交易码
     */
    private String tranCode;

    /**
     * 渠道号
     */
    private String trenchNo;

    /**
     * 报文类型
     */
    private String messageType;

    /**
     * 机构号
     */
    private String orgCode;

    /**
     * 交易日期
     */
    private String tranDate;

    /**
     * 交易时间
     */
    private String tranTime;

    /**
     * 按时间统计
     */
    private String dateType;

    /**
     * 开始时间
     */
    private String beginDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 备用字段1
     */
    private String string001;
    /**
     * 备用字段2
     */
    private String string002;
    /**
     * 备用字段3
     */
    private String string003;
    /**
     * 备用字段4
     */
    private String string004;
    /**
     * 备用字段5
     */
    private String string005;

    public String getRequestMsg() {
        return ClobUtils.clob2Str(requestMsg);
    }

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = ClobUtils.str2Clob(requestMsg);
    }

    public String getResponseMsg() {
        return ClobUtils.clob2Str(responseMsg);
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = ClobUtils.str2Clob(responseMsg);
    }

}
