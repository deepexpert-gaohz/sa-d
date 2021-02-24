package com.ideatech.ams.system.annotation.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.util.ClobUtils;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Clob;

/**
 * 操作日志
 */
@Table(name = "message_log")
@Data
@Entity
public class MessageLog extends BaseMaintainablePo {

    private static final int DEFAULT_CLOB_COLUMN_LENGTH = 64000;

    private static final int DEFAULT_MAX_CHAR_COLUMN_LENGTH = 4000;

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
    @Column(length = DEFAULT_MAX_CHAR_COLUMN_LENGTH)
    private String errorCode;

    /**
     *
     */
    private String operationType;

    /**
     * 请求方法
     */
    private String reqMethod;

    /**
     * 请求报文
     */
    @Column(length = DEFAULT_CLOB_COLUMN_LENGTH)
    private Clob requestMsg;

    /**
     * 响应报文
     */
    @Column(length = DEFAULT_CLOB_COLUMN_LENGTH)
    private Clob responseMsg;

    /**
     * 交易类型  AMS:人行  ECCS：机构  SAIC：工商
     */
    private String tranType;
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
     * 交易日期  年月日
     */
    private String tranDate;

    /**
     * 交易时间  年月日是分秒
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
