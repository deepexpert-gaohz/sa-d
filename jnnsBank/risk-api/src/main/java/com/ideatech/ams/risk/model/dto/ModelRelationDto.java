package com.ideatech.ams.risk.model.dto;

import lombok.Data;

@Data
public class ModelRelationDto {
    private String bmc;		// bmc
    private String bms;		// bms
    private String parentId;		// parent_id
    private String relyName;		// rely_name
    private String mTable;		// 当前表名称
    private String mFiled;		// 当前表字段
    private String rTable;		// 依赖表
    private String rField;		// 依赖表字段
    private String fFlag;		// 字段用户类型
    private String level;		//层级
    /*风险数据来源*/
    private String source;
    //add by yangcq 给依赖关系表添加中文名称
    private String mTableCn;
    private String rTableCn;

    private String pid;
    private String east;//来源系统
}
