package com.ideatech.ams.risk.modelKind.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @auther zhuqr
 * @date ${date} ${time}
 */
@Data
public class RiskTypeDto {
    private Long id;

    private String typeName;

    private String remakes;

    private String parentId;

    private String parentIds; //所有上级编号
}
