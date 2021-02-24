package com.ideatech.ams.dto;

import lombok.Data;

@Data
public class JnnsPbc {
    /**
     * 基本户许可证号
     */
    private String accountKey;

    /**
     * 注册地区代码
     */
    private String regAreaCode;

    /**
     * 行内机构号
     */
    private String organCode;

}
