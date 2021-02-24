package com.ideatech.ams.mivs.dto.rrd;

import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/22.
 *
 * 个体户照面信息部分。根据企业/个体户核查类型，此部分或企业照面信息部分在每个分片报文中均出现
 */

@Data
public class BasicInformationOfSelfEmployedPeopleDto {

    private Long id;
    private Long registerInformationLogId;

    /**
     * --------TradeName
     * 字号名称	<TraNm>	[0..1]	Max256Text
     */
    private String traNm;

    /**
     * --------UniformSocialCreditCode
     *           统一社会信用代码	<UniSocCdtCd>	[1..1]	Max18Text
     *           企业客户的统一社会信用代码
     * 禁止中文
     */
    private String uniSocCdtCd;

    /**
     * --------CompanyType
     *           市场主体类型	<CoTp>	[0..1]	Max128Text
     */
    private String coTp;

    /**
     * --------OperationLocation
     *           经营场所	<OpLoc>	[0..1]	Max200Text
     */
    private String opLoc;

    /**
     * --------FundsAmount
     *           资金数额	<FdAmt>	[0..1]	ActiveCurrencyAndAmount
     */
    private String fdAmt;

    /**
     * --------DateOfRegistration
     *           成立日期	<DtReg>	[0..1]	ISODate
     */
    private String dtReg;

    /**
     * --------RegistrationStatus
     *           登记状态	<RegSts>	[0..1]	Max128Text
     */
    private String regSts;


    /**
     *--------Name
     *           经营者姓名	<Nm>	[0..1]	Max200Text
     */
    private String nm;

    /**
     *--------RegistrationAuthority
     *           登记机关	<RegAuth>	[0..1]	Max128Text
     */
    private String regAuth;

    /**
     *--------BusinessScope
     *           经营范围	<BizScp>	[0..1]	Max3000Text
     */
    private String bizScp;

    /**
     * --------DateOfApproval
     *           核准日期	<DtAppr>	[0..1]	ISODate
     */
    private String dtAppr;
}
