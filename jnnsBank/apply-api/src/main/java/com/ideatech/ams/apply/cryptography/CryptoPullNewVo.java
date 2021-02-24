package com.ideatech.ams.apply.cryptography;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CryptoPullNewVo implements Serializable {
    private static final long serialVersionUID = -1565962539314151813L;

    /**
     * 易账户内部生成的预约编号
     */
    private String applyId;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 预约类型
     */
    private String billType;

    /**
     * 预约类型（基本户/一般户）
     */
    private String type;

    /**
     * 开户行银行名称
     */
    private String bank;

    /**
     * 开户行网点名称
     */
    private String branch;

    /**
     * 客户预约办理时间
     */
    private String applyDate;

    /**
     * 预约人员
     */
    private String operator;

    /**
     * 预约手机
     */
    private String phone;

    /**
     * 银行联行号
     */
    private String organId;

    /**
     * 基本户开户许可核准号
     */
    private String accountKey;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 销户理由
     */
    private String cancelReason;

    /**
     * 状态
     */
    private String status;

    /**
     * 是否有影像
     */
    private String hasocr;

    /**
     * 银行受理时间
     */
    private String bankDate;

    /**
     * 银行受理时间(时分秒)
     */
    private String times;

    /**
     * 回执信息(回退回执、失败回执)
     */
    private String note;

}
