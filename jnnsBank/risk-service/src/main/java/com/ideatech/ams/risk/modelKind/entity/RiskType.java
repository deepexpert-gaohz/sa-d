package com.ideatech.ams.risk.modelKind.entity;

import com.ideatech.ams.risk.model.entity.Model;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * @auther zhuqr
 * @date ${date} ${time}
 */
@Entity
@Table(name = "risk_model_type")
@Data
@SQLDelete(sql = "update yd_risk_model_type set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class RiskType extends BaseMaintainablePo {

    private String typeName;//风险类型

    private String remakes; //备注信息

    private String delFlag; //删除标记

    private String parentId; //上级编号

    private String parentIds; //所有上级编号

    private Boolean deleted = Boolean.FALSE;//删除标记



}
