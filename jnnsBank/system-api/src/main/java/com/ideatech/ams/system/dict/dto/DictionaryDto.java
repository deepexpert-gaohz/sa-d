package com.ideatech.ams.system.dict.dto;

import lombok.Data;

import java.util.List;

/**
 * @author liangding
 * @create 2018-05-24 下午4:36
 **/
@Data
public class DictionaryDto {
    private String name;
    private Long id;
    private List<OptionDto> options;
}
