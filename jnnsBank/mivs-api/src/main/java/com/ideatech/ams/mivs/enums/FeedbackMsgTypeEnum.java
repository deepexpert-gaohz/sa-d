package com.ideatech.ams.mivs.enums;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 * @author jzh
 * @date 2019-08-06.
 */

@Getter
public enum FeedbackMsgTypeEnum {

    PHONE_NUMBER("手机号码核查结果疑义反馈","phoneNumber"),
    TAX_INFORMATION("纳税信息核查结果疑义反馈","taxInformation"),
    REGISTER_INFORMATION("登记信息核查结果疑义反馈","registerInformation");

    private String name;

    private String value;

    FeedbackMsgTypeEnum(String name,String value){
        this.name = name;
        this.value = value;
    }

    public static FeedbackMsgTypeEnum str2enum(String feedbackString) {
        if (StringUtils.isBlank(feedbackString)) {
            return null;
        }
        if (feedbackString.equals("手机号码核查结果疑义反馈") || feedbackString.equals("phoneNumber")) {
            return FeedbackMsgTypeEnum.PHONE_NUMBER;
        } else if (feedbackString.equals("纳税信息核查结果疑义反馈") || feedbackString.equals("taxInformation")) {
            return FeedbackMsgTypeEnum.TAX_INFORMATION;
        } else if (feedbackString.equals("登记信息核查结果疑义反馈") || feedbackString.equals("registerInformation")) {
            return FeedbackMsgTypeEnum.REGISTER_INFORMATION;
        } else {
            return null;
        }
    }
}
