package com.ideatech.ams.mivs.dto.rrd;

import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/22.
 * 营业执照作废声明，属于企业登记信息核查内
 */

@Data
public class LicenseNullifyDto {

    private Long id;
    private Long registerInformationLogId;

    /**
     * --------OriginalOrCopy
     *           正副本标识
     *           <OrgnlOrCp>	[0..1]
     *           OriginalOrCopyCode(Max4Text)
     * ORCY：正副本
     * ORGN：正本
     * COPY：副本
     */
    private String orgnlOrCp;

    /**
     *--------LicenseNullifyStatementContent
     *           声明内容	<LicNullStmCntt>	[0..1]	Max2000Text
     */
    private String licNullStmCntt;

    /**
     * --------LicenseNullifyStatementDate
     *           声明日期	<LicNullStmDt>	[0..1]	ISODate
     */
    private String licNullStmDt;

    /**
     * --------ReplacementStatus
     *           补领标识	<RplSts>	[0..1]
     *           ReplacementCode(Max4Text)
     * RPLC：补领
     * NULL：未补领
     */
    private String rplSts;

    /**
     * --------ReplacementDate
     *           补领日期	<RplDt>	[0..1]	ISODate
     */
    private String rplDt;

    /**
     *--------LicenseCopyNumber
     *           营业执照副本编号	<LicCpNb>	[0..1]	Max50Text
     */
    private String licCpNb;
}
