package com.ideatech.ams.risk.modelFlow.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="model_flow_configer")
@Data
@SQLDelete(sql = "update yd_model_flow_configer set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class ModelFlowConfiger extends BaseMaintainablePo {
    private String modelId;//模型编号
    private String isAuto;// is '是否自动分配（1:是,0:否）'
    private String flowKey;//流程编码
    private String  flowUserId;//流程发起人id
    private String auditUserType;//用户类型(001：机构，002：用户，003：角色)
    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;
}
