package com.ideatech.ams.risk.model.dto;


import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class ModelFieldDto {

    private Long id;
    private String modelId;		// 模型表名
    private String fieldsEn;		// 模型字段
    private String fieldsZh;		// 模型字段中文
    private Integer showFlag;		// 是否显示0:显示 1:不显示
    private Integer orderFlag;			//数据排序 0:排序 1:不排序
    private Integer exportFlag;		// 是否导出0:导出 1：不导出
    private Integer status;			// 字段状态 0:不可删字段  1:可删字段
}
