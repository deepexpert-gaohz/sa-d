package com.ideatech.ams.mivs.dto.bnd;

import lombok.Data;

/**
 * @author jzh
 * @date 2019-09-11.
 */

@Data
public class BATNServiceInformationDto {

    private Long id;

    private Long noticeId;

    /**
     * SystemIndicator
     * 核查系统标识
     * <SysInd>  [1..1] SystemTypeCode (Max4Text)
     */
    private String sysInd;

    /**
     * ServiceIndicator
     * 被查询日期受理业务状态
     * <SvcInd>  [1..1] ServiceCode (Max4Text)
     */
    private String svcInd;

    /**
     * OpenTime
     * 被查询日期起始受理时间
     * <SysOpTm>  [0..1]  ISODateTime
     * 禁止中文；在“被查询日期受理业务状态”为 ENBL：正常的情况下填写。
     */
    private String nxtSysOpTm;

    /**
     * CloseTime
     * 被查询日期结束受理时间
     * <SysClTm>  [0..1]  ISODateTime
     * 禁止中文；在“被查询日期受理业务状态”为 ENBL：正常的情况下填写。
     */
    private String nxtSysClTm;
}
