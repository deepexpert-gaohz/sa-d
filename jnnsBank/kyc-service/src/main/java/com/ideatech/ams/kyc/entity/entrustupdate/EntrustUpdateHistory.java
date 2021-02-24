package com.ideatech.ams.kyc.entity.entrustupdate;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 委托更新记录
 */
@Entity
@Data
@Table(name = "yd_saicinfo_entrustUpdate")
public class EntrustUpdateHistory extends BaseMaintainablePo {
    /**
     * 企业名称
     */
    @Column(length = 200)
    private String companyName;

    /**
     * 是否委托更新成功 true-成功 默认false
     */
    @Column(length = 10)
    private Boolean updateStatus = Boolean.FALSE;

}
