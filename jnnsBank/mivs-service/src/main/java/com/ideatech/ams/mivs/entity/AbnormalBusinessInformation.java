package com.ideatech.ams.mivs.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019/7/31.
 *
 * 异常经营信息，属于企业登记信息核查内容
 */

@Entity
@Data
@Table(name = "mivs_register_info_abnormal")
public class AbnormalBusinessInformation extends BaseMaintainablePo {

    private Long registerInformationLogId;

    /**
     * --------AbnormalCause
     *           列入经营异常名录原因类型	<AbnmlCause>	[0..1]	Max200Text
     */
    private String abnmlCause;


    /**
     *--------AbnormalDate
     *           列入日期	<AbnmlDate>	[0..1]	ISODate
     */

    private String abnmlDate;

    /**
     * --------AbnormalCauseDecisionAuthority
     *           列入决定机关	<AbnmlCauseDcsnAuth>	[0..1]	Max128Text
     */
    private String abnmlCauseDcsnAuth;

    /**
     * --------RemoveCause
     *           移出经营异常名录原因	<RmvCause>	[0..1]	Max200Text
     */
    private String rmvCause;

    /***
     * --------RemoveDate
     *           移出日期	<RmvDate>	[0..1]	ISODate
     */
    private String rmvDate;

    /**
     * --------RemoveCauseDecisionAuthority
     *           移出决定机关	<RmvCauseDcsnAuth>	[0..1]	Max128Text
     */
    private String rmvCauseDcsnAuth;
}
