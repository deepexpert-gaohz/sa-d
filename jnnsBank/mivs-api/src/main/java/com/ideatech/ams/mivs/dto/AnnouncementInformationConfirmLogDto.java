package com.ideatech.ams.mivs.dto;

import lombok.Data;

/**
 * @author jzh
 * @date 2019-09-02.
 */

@Data
public class AnnouncementInformationConfirmLogDto extends ExtendDto{

    private Long id;

    /**
     * 原报文标识号
     */
    private String msgId;

    /**
     * 原发起直接参与机构
     */
    private String instgDrctPty;

    /**
     * 原发起参与机构
     */
    private String instgPty;

    /**
     * 附加信息
     */
    private String msgCntt;
}
