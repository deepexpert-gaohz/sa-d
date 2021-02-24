package com.ideatech.ams.compare.dto.compareresultdetail;

import lombok.Data;

import java.util.List;

@Data
public class CompareResultDetailListDto {
    /**
     * 数据源名称
     */
    private String name;

    /**
     * 账号
     */
    private String account;

    private List<CompareResultFieldDto> fieldList;
}
