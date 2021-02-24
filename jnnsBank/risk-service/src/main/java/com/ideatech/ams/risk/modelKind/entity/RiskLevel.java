package com.ideatech.ams.risk.modelKind.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;


/**
 * @auther zhuqr
 * @date ${date} ${time}
 */
@Entity
@Table(name = "risk_model_level")
@Data
@SQLDelete(sql = "update yd_risk_model_level set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class RiskLevel extends BaseMaintainablePo {

    private String levelName;//风险等级

    private String remakes; //备注信息

    private String delFlag; //删除标记

    private String parentId; //上级编号

    private String parentIds; //所有上级编号

    private Boolean deleted = Boolean.FALSE;//删除标记







}
