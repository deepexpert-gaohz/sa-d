package com.ideatech.ams.mivs.dto.ind;

import com.ideatech.ams.mivs.dto.MessageHeaderDto;
import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/24.
 * 机构异常核查通知报文传输对象
 */

@Data
public class InstitutionAbnormalNoticeDto extends MessageHeaderDto {

    /**
     * 银行营业网点或非银行支付机构行号
     */
    private String orgnlInstgPty;

    /**
     * 异常核查类型
     */
    private String abnmlType;

    /**
     * 异常内容说明
     */
    private String dESC;
}
