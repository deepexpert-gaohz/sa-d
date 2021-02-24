package com.ideatech.ams.risk.model.entity;

import javax.persistence.*;


import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;


@Entity
@Table(name="risk_models")
@Getter
@Setter
@SQLDelete(sql = "update yd_risk_models set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class Model  extends BaseMaintainablePo{
    /**
     * 模型编号
     */

    private String modelId;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 业务逻辑
     */
    private String logic;

    /**
     * 数据库名称
     */
    private String tableName;

    /**
     * 风险类型
     */
    private String typeId;

    /**
     * 风险规则
     */
    private String ruleId;

    /**
     *风险等级
     */
    private String levelId;

    /**
     * 过程名称
     */
    private String procName;

    /**
     * 模型状态
     */
    private String status;

    /**
     * 模型描述
     */
    private String mdesc;

    /**
     *备注信息
     */
    private String remake;

    /**
     *删除标记
     */
    private String dalFalg;

    /**
     *数据流图
     */
    private String dataimg;

    /**
     *提出人
     */
    private String raiseName;

    /**
     *表维护
     */
    private String tableWf;

    /**
     *存储过程
     */
    private String modelproc;

    /**
     *提出部门
     */
    private String deptId;

    /**
     *风险数据类型
     */
    private String signFlag;

    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;



}
