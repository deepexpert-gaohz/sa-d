package com.ideatech.ams.system.dict.dto;

import lombok.Data;

/**
 * @author liangding
 * @create 2018-05-24 下午4:36
 **/
@Data
public class OptionDto {
    private Long id;
    private Long dictionaryId;
    private String name;
    private String value;
}
