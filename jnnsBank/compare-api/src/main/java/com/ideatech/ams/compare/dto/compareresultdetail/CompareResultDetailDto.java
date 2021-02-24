package com.ideatech.ams.compare.dto.compareresultdetail;

import lombok.Data;

import java.util.List;

/**
 * 比对结果详情展示类
 */
@Data
public class CompareResultDetailDto {
    private String code;

    private List<CompareResultFieldDto> fieldList;

    private List<CompareResultDetailListDto> list;

}
