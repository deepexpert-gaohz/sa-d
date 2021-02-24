package com.ideatech.ams.mivs.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author jzh
 * @date 2019/7/25.
 * MessageHeader传输对象，通知报文使用
 */

@Data
public class MessageHeaderDto {

    private Long id;
    private String lastUpdateBy;
    private Date lastUpdateDate;
    private Date createdDate;
    private String createdBy;

    private String beginDate;

    private String endDate;

    /**
     * 报文标识号
     */
    private String msgId;

    /**
     * 报文发送时间
     */
    private String creDtTm;

    /**
     * 发起直接参与机构
     */
    private String instgDrctPty;

    /**
     * 发起参与机构
     */
    private String instgPty;

    /**
     * 接收直接参与机构
     */
    private String instdDrctPty;

    /**
     * 接收参与机构
     */
    private String instdPty;

    /**
     * 扩展字段
     */
    private String String001;
    private String String002;
    private String String003;
    private String String004;
    private String String005;
    private String String006;
    private String String007;
    private String String008;
    private String String009;
    private String String010;
    private String String011;
    private String String012;
    private String String013;
    private String String014;
    private String String015;
}
