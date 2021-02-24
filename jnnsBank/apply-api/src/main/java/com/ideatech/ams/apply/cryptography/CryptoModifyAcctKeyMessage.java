package com.ideatech.ams.apply.cryptography;

import lombok.Data;

@Data
public class CryptoModifyAcctKeyMessage {
    /**
     * 预约编号
     */
    private String applyId;

    /**
     * 预约开户行网点银行12位人行联行号
     */
    private String organId;

    /**
     * 基本开户许可核准号
     */
    private String accountKey;

}
