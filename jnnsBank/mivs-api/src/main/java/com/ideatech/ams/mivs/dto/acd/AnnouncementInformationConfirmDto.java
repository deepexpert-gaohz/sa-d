package com.ideatech.ams.mivs.dto.acd;


import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author jzh
 * @date 2019/7/23.
 * mivs.333.001.01	公告信息确认报文	参与机构->MIVS
 */

@Data
public class AnnouncementInformationConfirmDto {

    private Long id;

    /**
     * 公告信息报文
     */
    private Long announcementInformationId;

    /**
     * 原报文标识号
     */
    @NotBlank(message="原报文标识号不能为空")
    private String msgId;

    /**
     * 原发起直接参与机构
     */
    @NotBlank(message="原发起直接参与机构不能为空")
    private String instgDrctPty;

    /**
     * 原发起参与机构
     */
    @NotBlank(message="原发起参与机构不能为空")
    private String instgPty;

    /**
     * 附加信息
     */
    @NotBlank(message="附加信息不能为空")
    private String msgCntt;
}
