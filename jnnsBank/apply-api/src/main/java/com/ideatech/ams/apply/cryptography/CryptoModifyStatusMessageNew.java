package com.ideatech.ams.apply.cryptography;

import com.ideatech.ams.apply.enums.ApplyEnum;
import lombok.Data;

/**
 * 修改预约状态(新格式)
 */
@Data
public class CryptoModifyStatusMessageNew {
    /**
     * 预约编号
     */
    private String applyId;

    /**
     * 预约开户行网点银行
     */
    private String organId;

    /**
     * 可传状态：
     * UnComplete：待受理  SUCCESS：受理成功  FAIL：受理退回  REGISTER_SUCCESS：业务办理成功  REGISTER_FAIL：业务办理失败
     */
    private ApplyEnum status;

    /**
     * 回执信息（回退回执、失败回执）
     */
    private String note;
}
