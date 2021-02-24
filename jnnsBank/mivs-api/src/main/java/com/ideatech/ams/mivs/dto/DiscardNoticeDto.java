package com.ideatech.ams.mivs.dto;

import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/19.
 * 报文丢弃通知报文传输对象
 */

@Data
public class DiscardNoticeDto implements ReplyMsgDto{

    /**
     *  报文发起人
     */
    private String origSndr;

    /**
     * 报文发起日期
     */
    private String origSndDt;

    /**
     * 报文类型代码
     */
    private String mt;

    /**
     * 通信级标识号
     */
    private String msgId;

    /**
     * 通信级参考号
     */
    private String msgRefId;

    /**
     * 业务处理码
     */
    private String prcCd;

    /**
     * 业务拒绝信息
     */
    private String rjctInf;
}
