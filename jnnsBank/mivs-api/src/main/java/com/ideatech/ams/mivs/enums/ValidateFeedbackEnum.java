package com.ideatech.ams.mivs.enums;

import lombok.Getter;

/**
 * @author jzh
 * @date 2019-08-07.
 */

@Getter
public enum ValidateFeedbackEnum {
    UNKNOW("无该疑义反馈类型"),NULL("无报文记录"),TRUE("有应答报文返回"),FALSE("无应答报文返回");
    private String value;
    ValidateFeedbackEnum(String value){
        this.value=value;
    }
}
