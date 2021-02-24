package com.ideatech.ams.mivs.dto.trd;

import com.ideatech.ams.mivs.dto.ReplyMsgDto;
import lombok.Data;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/18.
 * 纳税信息联网核查应答报文传输对象
 */

@Data
public class TaxInformationReplyDto implements ReplyMsgDto {

    /**
     * Result
     * 纳税信息核查结果
     * <Rslt>  [1..1] (Max4Text)
     */
    private String rslt;

    /**
     *DataResourceDate
     * 数据源日期
     * <DataResrcDt>  [1..1] ISODate
     */
    private String dataResrcDt;

    /**
     * TaxPaymentInformation
     * <TxpmtInf>  [0..n]
     * 当“纳税信息核查结果”为“MCHD”时填写
     */
    private List<TaxPaymentInformationDto> txpmtInf;

}
