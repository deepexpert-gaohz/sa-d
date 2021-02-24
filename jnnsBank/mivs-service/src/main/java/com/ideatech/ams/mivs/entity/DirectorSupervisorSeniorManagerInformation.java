package com.ideatech.ams.mivs.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019/7/31.
 * 董事监事及高管信息，属于企业登记信息核查内容。
 */

@Entity
@Data
@Table(name = "mivs_register_info_manager")
public class DirectorSupervisorSeniorManagerInformation extends BaseMaintainablePo {

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
