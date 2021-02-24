package com.ideatech.ams.mivs.dto.tad;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author jzh
 * @date 2019/7/18.
 * 纳税信息联网核查申请报文传输对象
 */

@Data
public class TaxInformationApplyDto {

    private Long id;

    /**
     * CompanyName
     * 单位名称
     * <CoNm>  [1..1] Max300Text
     */
    @NotBlank(message="单位名称不能为空")
    @Length(max = 300, message = "单位名称长度超过300")
    private String coNm;

    /**
     * UniformSocialCreditCode
     * 统一社会信用代码
     * <UniSocCdtCd>  [1..1] Max18Text 企业客户的统一社会信用代码
     */
    @Length(max = 18, message = "统一社会信用代码长度超过18")
    private String uniSocCdtCd;

    /**
     * TaxpayerIdentificationNumber
     * 纳税人识别号
     * <TxpyrIdNb>  [1..1] Max20Text 企业客户的纳税人识别号
     */
    @Length(max = 20, message = "纳税人识别号长度超过20")
    private String txpyrIdNb;

    /**
     * OperatorName
     * 操作员姓名
     * <OpNm>  [1..1] Max140Text 参与机构核查人员姓名
     */
    @NotBlank(message="操作员姓名不能为空")
    @Length(max = 140, message = "操作员姓名长度超过300")
    private String opNm;
}
