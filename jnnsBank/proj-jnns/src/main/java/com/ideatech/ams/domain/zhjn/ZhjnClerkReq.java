package com.ideatech.ams.domain.zhjn;

import lombok.Data;

import java.util.Date;

/**
 * 经办人办理请求体参数
 *
 * @auther yfy
 * @create 2020-8-6
 **/

@Data
public class ZhjnClerkReq {

    private String orderId;
    private String location;
    private String imageNo;

}
