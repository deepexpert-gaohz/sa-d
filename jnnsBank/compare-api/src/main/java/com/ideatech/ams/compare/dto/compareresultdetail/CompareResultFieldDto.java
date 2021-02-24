package com.ideatech.ams.compare.dto.compareresultdetail;

import lombok.Data;

@Data
public class CompareResultFieldDto {
    /**
     * 字段中文名
     */
    private String fieldName;

    /**
     * 字段英文名
     */
    private String fieldNameEng;

    /**
     * 字段值
     */
    private Object fieldValue;

    /**
     * 字段值是否匹配
     */
    private Boolean isEquals;

    /**
     *  是否空算过
     */
    private Boolean nullpass;

}
