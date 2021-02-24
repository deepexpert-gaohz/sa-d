package com.ideatech.ams.mivs.dto.ad;

import com.ideatech.ams.mivs.dto.MessageHeaderDto;
import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/25.
 */

@Data
public class AnnouncementInformationDto extends MessageHeaderDto {

    private Long id;

    /**
     * 回复标识 ReplyFlagCode（Max4Text）
     * RPLY:需要回复
     * NRPL:无需回复
     */
    private String rplyFlag;

    /**
     * 信息内容
     */
    private String msgCntt;

}
