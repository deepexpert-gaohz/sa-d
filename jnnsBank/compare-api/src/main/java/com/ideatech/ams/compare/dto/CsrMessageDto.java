package com.ideatech.ams.compare.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

import java.util.Date;

/**
 * @author jzh
 * @date 2019/6/28.
 */

@Data
public class CsrMessageDto extends PagingDto<CsrMessageDto> {

    private Long id;

    private Date createdDate;

    /**
     * 存款人名称
     */
    private String depositorName;

    /**
     * 通知提醒手机号
     */
    private String phone;

    /**
     * 短信发送是否成功
     */
    private Boolean checkPass;

    /**
     * 发送失败原因
     */
    private String errorMessage;

    /**
     * 通知提醒内容
     */
    private String message;

    /**
     * 备用
     */
    private String type;
}
