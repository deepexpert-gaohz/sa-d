package com.ideatech.ams.risk.model.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

@Data
public class ModelsExtend extends BaseMaintainablePo {

    private String modelId;		// 模型编号
    private String modelsId;	//models表中id
    private String name;		//
    private String logic;		// 业务逻辑
    /*private ModelType typeId = new ModelType();		// 风险类型
    private ModelRule ruleId = new ModelRule();		// 风险类型
    private ModelLevel levelId = new ModelLevel();		// 风险等级*/
    private String mdesc;		// 模型描述
    private String status;		// 模型状态
    //	private ModelDept dept = new ModelDept();		// 提出部门
   // private SysDept sysDept;
    private String raiseName;	//提出人
    private String raiseTime;//提出时间
    private String tableName;		// 数据表名
    private String procName;		// 过程名称

    private String dataImg;//数据流图
    private String modelProc; //模型代码
    private String disModel;//模型分配状态
    private Float tjjg;//统计结果
    //private User user;//用户
    //private Office office = new Office();//机构

    private String modelName;
    private String ogId;//add zhudd 机构 备用

    private String checkedListId;//add by yangcq 用户保存已勾选的checkbox，用于翻页功能
    private String signFlag;//add by mengxp 用户勾选指定模型需求功能，（去重，误报，风险关系图）,1表示已勾选

    private String minDate;  //开始时间
    private String maxDate;   //结束时间

}
