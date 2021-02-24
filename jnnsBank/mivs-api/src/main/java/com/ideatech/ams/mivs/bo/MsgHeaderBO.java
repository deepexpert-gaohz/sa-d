package com.ideatech.ams.mivs.bo;

import com.ideatech.ams.mivs.annotation.FixedLength;
import com.ideatech.ams.mivs.enums.MsgHeaderFieldTypeEnum;
import lombok.Data;

/**
 * 报文头业务对象
 *
 * @author fantao
 * @date 2019-07-04 16:17
 */
@Data
public class MsgHeaderBO {

    /**
     * 起始标识
     */
    @FixedLength(index = 0, length = 3, type = MsgHeaderFieldTypeEnum.X)
    private String beginFlag;

    /**
     * 版本号
     */
    @FixedLength(index = 3, length = 2, type = MsgHeaderFieldTypeEnum.N)
    private String versionID;

    /**
     * 报文发起人
     */
    @FixedLength(index = 5, length = 14, type = MsgHeaderFieldTypeEnum.X)
    private String origSender;

    /**
     * 发送系统号
     */
    @FixedLength(index = 19, length = 4, type = MsgHeaderFieldTypeEnum.X)
    private String origSenderSID;

    /**
     * 报文接收人
     */
    @FixedLength(index = 23, length = 14, type = MsgHeaderFieldTypeEnum.X)
    private String origReceiver;

    /**
     * 接收系统号
     */
    @FixedLength(index = 37, length = 4, type = MsgHeaderFieldTypeEnum.X)
    private String origReceiverSID;

    /**
     * 报文发起日期
     */
    @FixedLength(index = 41, length = 8, type = MsgHeaderFieldTypeEnum.D)
    private String origSendDate;

    /**
     * 报文发起时间
     */
    @FixedLength(index = 49, length = 6, type = MsgHeaderFieldTypeEnum.T)
    private String origSendTime;

    /**
     * 起始标识
     */
    @FixedLength(index = 55, length = 3, type = MsgHeaderFieldTypeEnum.X)
    private String structType;

    /**
     * 报文类型代码
     */
    @FixedLength(index = 58, length = 20, type = MsgHeaderFieldTypeEnum.X)
    private String mesgType;

    /**
     * 通信级标识号
     */
    @FixedLength(index = 78, length = 20, type = MsgHeaderFieldTypeEnum.X)
    private String mesgID;

    /**
     * 通信级参考号
     */
    @FixedLength(index = 98, length = 20, type = MsgHeaderFieldTypeEnum.X, must = false)
    private String mesgRefID;

    /**
     * 报文优先级
     */
    @FixedLength(index = 118, length = 1, type = MsgHeaderFieldTypeEnum.N)
    private String mesgPriority;

    /**
     * 报文传输方向
     */
    @FixedLength(index = 119, length = 1, type = MsgHeaderFieldTypeEnum.X)
    private String mesgDirection;

    /**
     * (保留域）
     */
    @FixedLength(index = 120, length = 9, type = MsgHeaderFieldTypeEnum.X, must = false)
    private String reserve;

    /**
     * 结束标识
     */
    @FixedLength(index = 129, length = 3, type = MsgHeaderFieldTypeEnum.X)
    private String endFlag;


    MsgHeaderBO() {
        this.beginFlag = "{H:";
        this.versionID = "02";
        this.origSenderSID = "MIVS";
        this.origReceiverSID = "MIVS";
        this.structType = "XML";
        this.mesgPriority = "3";
        this.endFlag = "}\r\n";
    }

}
