package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 工商变更
 */
@Table(name = "yd_saicinfo_changerecord")
@Entity
@Data
public class ChangeRecord extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 变更时间
     */
    @Column(name = "yd_changedate")
    private String changedate;

    /**
     * 变更项
     */
    @Column(name = "yd_type")
    private String type;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

    /**
     * 变更后内容
     */
	@Column(name = "yd_aftercontent")
    @Lob
    private String aftercontent;

    /**
     * 变更前内容
     */
	@Column(name = "yd_beforecontent")
    @Lob
    private String beforecontent;

}