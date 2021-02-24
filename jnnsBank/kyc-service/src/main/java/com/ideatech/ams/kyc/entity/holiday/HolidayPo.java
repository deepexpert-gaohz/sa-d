package com.ideatech.ams.kyc.entity.holiday;

import com.ideatech.ams.kyc.enums.HolidayTypeEnum;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * 节假日
 */
@Data
@Entity
@Table(name = "holiday")
@SQLDelete(sql = "update yd_holiday set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class HolidayPo extends BaseMaintainablePo {

    /**
     * 节假日类型
     */
    @Enumerated(EnumType.STRING)
    private HolidayTypeEnum holidayType;

    /**
     * 日期格式：yyyy-MM-dd
     */
    private String dateStr;

    /**
     * 删除状态
     */
    private Boolean deleted = Boolean.FALSE;

}
