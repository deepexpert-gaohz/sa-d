package com.ideatech.ams.risk.model.entity;


import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.entity.util.Comment;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author: yinjie
 * @Date: 2019/5/5 16:37
 */


@Entity
@Table(name = "risk_model_count")
@Data
@SQLDelete(sql = "update yd_risk_model_count set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class ModelCount extends BaseMaintainablePo {

    private String cjrq;//采集日期

    private String cn;//数据条数

    private String khId;//客户id

    private String khName;//客户姓名

    private String modelName;//模型名称

    private String modelId;//模型id

    private String riskId;//风险id

    private String riskTable;//模型表

    private Boolean deleted = Boolean.FALSE;//删除标记

    private String fullId;//机构完整id

    private String orgId;//机构编号

    private String orgName;//机构名称

    private String status;//状态

    private String riskAmt;//风险金额

    private String riskDesc;//风险描述

    private String accountNo;//账号
}
