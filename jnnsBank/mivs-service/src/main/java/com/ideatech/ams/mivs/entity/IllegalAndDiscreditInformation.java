package com.ideatech.ams.mivs.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019/7/31.
 *
 * 严重违法失信信息，属于企业登记信息核查内容。
 */

@Entity
@Data
@Table(name = "mivs_register_info_illegal")
public class IllegalAndDiscreditInformation extends BaseMaintainablePo {

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
