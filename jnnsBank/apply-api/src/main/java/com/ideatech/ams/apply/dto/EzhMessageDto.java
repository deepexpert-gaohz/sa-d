package com.ideatech.ams.apply.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class EzhMessageDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 通知提醒内容
     */
    private String message;

    /**
     * 通知提醒手机号
     */
    private String phone;

    /**
     * 通知提醒类别：1-预约人通知短信,2-柜员通知短信
     */
    private String type;

    /**
     * 短信发送是否成功
     */
    private Boolean checkPass;

    /**
     * 发送失败原因
     */
    private String errorMessage;

    /**
     * 接口返回值
     */
    private String returnValue;

    /**
     * 预约编号
     */
    private String applyId;

}
