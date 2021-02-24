package com.ideatech.ams.mivs.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019/7/31.
 *
 * 历史变更信息，属于企业/个体户登记信息核查内容。
 */


@Entity
@Data
@Table(name = "mivs_register_info_change")
public class ChangeInformation extends BaseMaintainablePo {

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
