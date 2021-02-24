package com.ideatech.ams.apply.cryptography;

import lombok.Data;

/**
 * @Description 预约记录的影像资料
 * @Author wanghongjie
 * @Date 2018/10/21
 **/
@Data
public class CryptoImagesMessage {
    /**
     * 预约编号
     */
    String openAcctTransNo;
    /**
     * 页号，如果第一次请求请传1，之后在总页数范围内请求下一页
     */
    Integer reqPageNum;
    /**
     * 	预约类型
     * 	ezhanghu(预约编号是易账户提供的) / nanjingbank(预约编号是行方提供的)
     */
    String transNoType;
}
