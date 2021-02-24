package com.ideatech.ams.mivs.dto.rrd;

import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/22.
 * 历史变更信息，属于企业/个体户登记信息核查内容。
 */

@Data
public class ChangeInformationDto {

    private Long id;
    private Long registerInformationLogId;

    /**
     * --------ChangeItem
     *           变更事项	<ChngItm>	[0..1]	Max128Text
     */
    private String chngItm;

    /**
     * --------BeforeChange
     *           变更前内容	<BfChng>	[0..1]	Max4000Text
     */
    private String bfChng;

    /**
     * --------AfterChange
     *           变更后内容	<AftChng>	[0..1]	Max4000Text
     */
    private String aftChng;


    /**
     * --------DateOfChange
     *           变更日期	<DtOfChng>	[0..1]	ISODate
     */
    private String dtOfChng;

}
