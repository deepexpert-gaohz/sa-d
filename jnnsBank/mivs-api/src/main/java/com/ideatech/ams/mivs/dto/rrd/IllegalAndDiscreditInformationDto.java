package com.ideatech.ams.mivs.dto.rrd;

import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/22.
 * 严重违法失信信息，属于企业登记信息核查内容。
 */

@Data
public class IllegalAndDiscreditInformationDto {

    private Long id;
    private Long registerInformationLogId;

    /***
     * --------IllegalAndDiscreditCause
     *           列入事由或情形	<IllDscrtCause>	[0..1]	Max128Text
     */
    private String illDscrtCause;

    /**
     * --------AbnormalDate
     *           列入日期	<AbnmlDate>	[0..1]	ISODate
     */
    private String abnmlDate;

    /**
     *--------AbnormalCauseDecisionAuthority
     *           列入决定机关	<AbnmlCauseDcsnAuth>	[0..1]	Max128Text
     */
    private String abnmlCauseDcsnAuth;

    /**
     *--------RemoveCause
     *           移出事由	<RmvCause>	[0..1]	Max128Text
     */
    private String rmvCause;

    /**
     * --------RemoveCauseDecisionAuthority
     *           移出决定机关	<RmvCauseDcsnAuth>	[0..1]	Max128Text
     */
    private String rmvCauseDcsnAuth;

    /***
     * --------RemoveDate
     *           移出日期	<RmvDate>	[0..1]	ISODate
     */
    private String rmvDate;
}
