package com.ideatech.ams.mivs.dto;

import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/29.
 */

@Data
public class DefaultMsgDto implements ReplyMsgDto{

    /**
     * 自定义返回码
     * 000001:无法立即获取结果，采用异步获取时返回。
     * 000002:正常发送。
     * 000003:发送失败。
     */
    private String msgCode;

    public DefaultMsgDto (String msgCode){
        this.msgCode = msgCode;
    }
}
