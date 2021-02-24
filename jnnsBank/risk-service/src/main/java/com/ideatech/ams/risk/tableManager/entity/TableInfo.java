package com.ideatech.ams.risk.tableManager.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 源表管理
 */
@Data
@Entity
@Table(name = "ds_table_info")
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update yd_ds_table_info set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class TableInfo  extends BaseMaintainablePo {
    private String cname;//中文名称
    private String xtly;//系统来源
    private String ename;//英文名称
    private String bz;//备注
    private Boolean deleted = Boolean.FALSE;//删除标记
}
