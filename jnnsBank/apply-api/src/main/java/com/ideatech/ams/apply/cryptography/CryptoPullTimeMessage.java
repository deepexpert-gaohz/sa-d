package com.ideatech.ams.apply.cryptography;

import lombok.Data;

@Data
public class CryptoPullTimeMessage {
    /**
     * 开始日期
     */
    private String startDateTime;

    /**
     * 结束日期
     */
    private String endDateTime;

    /**
     * 网点银行机构12位人行联行号，用英文逗号隔开
     */
    private String organIds;
}
