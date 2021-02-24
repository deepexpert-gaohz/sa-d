package com.ideatech.ams.risk.rely.entity;


import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "risk_model_rely")
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update yd_risk_model_rely set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class ModelRely extends BaseMaintainablePo {
    private String modelTable;//被依赖表
    private String relyTable;//依赖表
    private String modelTableCn;//被依赖表名
    private String relyTableCn;//依赖表名
    private Boolean deleted = Boolean.FALSE;//删除标记
}
