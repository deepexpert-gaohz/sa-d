package com.ideatech.ams.mivs.dto.rrd;

import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/22.
 *
 * 企业股东及出资信息部分，属于企业登记信息核查内容。
 */

@Data
public class CompanyShareholdersAndFundingInformationDto {

    private Long id;
    private Long registerInformationLogId;

    /**
     * --------NaturalPersonFlag
     *           自然人标识	<NatlPrsnFlag>	[0..1]	NatlPrsnFlagCode
     * (Max4Text)	NATL：自然人
     * UNTL：非自然人
     */
    private String natlPrsnFlag;

    /**
     * --------InvestorName
     *           投资人名称	<InvtrNm>	[0..1]	Max200Text
     */
    private String invtrNm;

    /***
     * --------InvestorId
     *           投资人证件号码或证件编号	<InvtrId>	[0..1]	Max50Text
     *           该字段对于自然人投资人为投资人证件号码，对于非自然人投资人则为证件编号
     * 禁止中文
     */
    private String invtrId;

    /**
     * --------SubscriptionCapitalContributionAmount
     *           认缴出资额	<SubscrCptlConAmt>	[0..1]
     *           ActiveCurrencyAndAmount
     */
    private String subscrCptlConAmt;

    /**
     *--------ActualContributionAmount
     *           实缴出资额	<ActlCptlConAmt>	[0..1]	ActiveCurrencyAndAmount
     */
    private String actlCptlConAmt;

    /**
     * --------SubscriptionCapitalContributionForm
     *           认缴出资方式	<SubscrCptlConFm>	[0..1]	Max200Text
     */
    private String subscrCptlConFm;

    /**
     * --------SubscriptionCapitalContributionDate
     *           认缴出资日期	<SubscrCptlConDt>	[0..1]	ISODate
     */
    private String subscrCptlConDt;


}
