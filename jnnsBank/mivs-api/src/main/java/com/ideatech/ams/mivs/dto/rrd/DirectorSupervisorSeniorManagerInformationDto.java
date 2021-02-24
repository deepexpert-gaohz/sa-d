package com.ideatech.ams.mivs.dto.rrd;

import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/22.
 * 董事监事及高管信息，属于企业登记信息核查内容。
 */

@Data
public class DirectorSupervisorSeniorManagerInformationDto {

    private Long id;
    private Long registerInformationLogId;

    /***
     * --------Name
     *           姓名	<Nm>	[0..1]	Max200Text
     */
    private String nm;

    /**
     * --------Position
     *           职务	<Posn>	[0..1]	Max128Text
     */
    private String posn;


}
