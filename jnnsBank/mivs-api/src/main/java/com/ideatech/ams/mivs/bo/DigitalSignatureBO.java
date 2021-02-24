package com.ideatech.ams.mivs.bo;

import com.ideatech.ams.mivs.annotation.FixedLength;
import com.ideatech.ams.mivs.enums.MsgHeaderFieldTypeEnum;
import lombok.Data;

/**
 * 数字签名域
 *
 * @author fantao
 * @date 2019-07-04 17:27
 */
@Data
public class DigitalSignatureBO {

    /**
     * 起始标识
     */
    @FixedLength(index = 0, length = 3, type = MsgHeaderFieldTypeEnum.X)
    private String beginFlag;

    /**
     * 数字签名内容
     */
    @FixedLength(index = 4, length = -1, type = MsgHeaderFieldTypeEnum.OTHER)
    private String digitalSignature;

    /**
     * 结束标识
     */
    @FixedLength(index = -1, length = 3, type = MsgHeaderFieldTypeEnum.X)
    private String endFlag;

}
