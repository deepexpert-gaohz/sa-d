package com.ideatech.ams.mivs.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author jzh
 * @date 2019/7/25.
 * 公共消息头
 */

@Data
@MappedSuperclass
public class MessageHeader extends BaseMaintainablePo {
    /**
     * 报文标识号
     */
    @Column(length = 70)
    private String msgId;

    /**
     * 报文发送时间
     */
    @Column(length = 40)
    private String creDtTm;

    /**
     * 发起直接参与机构
     */
    @Column(length = 28)
    private String instgDrctPty;

    /**
     * 发起参与机构
     */
    @Column(length = 28)
    private String instgPty;

    /**
     * 接收直接参与机构
     */
    @Column(length = 28)
    private String instdDrctPty;

    /**
     * 接收参与机构
     */
    @Column(length = 28)
    private String instdPty;
}
