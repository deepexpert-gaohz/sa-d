package com.ideatech.ams.risk.model.entity;


import com.google.common.collect.Lists;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "risk_model_field")
@SQLDelete(sql = "update yd_risk_model_field set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class ModelField  extends BaseMaintainablePo {
    private String modelId;		// 模型编号
    private String fieldsEn;		// 模型字段
    private String fieldsZh;		// 模型字段中文
    private Integer showFlag;		// 是否显示1:显示 0:不显示
    private Integer orderFlag;			//数据排序 1:排序 0:不排序
    private Integer exportFlag;		// 是否导出
    private Integer status;			// 字段状态 0:不可删字段  1:可删字段
    private Boolean deleted = Boolean.FALSE;//删除标记
}
