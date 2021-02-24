package com.ideatech.ams.mivs.dto.trd;

import com.ideatech.ams.mivs.dto.ReplyMsgDto;
import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/18.
 *
 */

@Data
public class TaxPaymentInformationDto {

    private Long id;
    private Long taxInformationLogId;

    /**
     * TaxAuthorityCode
     * 税务机关代码
     * <TxAuthCd>  [1..1] TxAuthCd (Max11Text)
     */
    private String txAuthCd;

    /**
     * TaxAuthorityName
     * 税务机关名称
     * <TxAuthNm>  [1..1] Max300Text
     */
    private String txAuthNm;

    /**
     * TaxpayerStatus
     * 纳税人状态
     * <TxpyrSts>  [1..1] TaxpayerStatusCode (Max4Text)
     */
    private String txpyrSts;
}
