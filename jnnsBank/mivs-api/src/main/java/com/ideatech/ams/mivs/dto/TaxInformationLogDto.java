package com.ideatech.ams.mivs.dto;

import com.ideatech.ams.mivs.dto.trd.TaxPaymentInformationDto;
import lombok.Data;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/29.
 *
 */

@Data
public class TaxInformationLogDto extends ExtendDto {

    /**
     * CompanyName
     * 单位名称
     * <CoNm>  [1..1] Max300Text
     */
    private String coNm;

    /**
     * UniformSocialCreditCode
     * 统一社会信用代码
     * <UniSocCdtCd>  [1..1] Max18Text 企业客户的统一社会信用代码
     */
    private String uniSocCdtCd;

    /**
     * TaxpayerIdentificationNumber
     * 纳税人识别号
     * <TxpyrIdNb>  [1..1] Max20Text 企业客户的纳税人识别号
     */
    private String txpyrIdNb;

    /**
     * OperatorName
     * 操作员姓名
     * <OpNm>  [1..1] Max140Text 参与机构核查人员姓名
     */
    private String opNm;

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
