package com.ideatech.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author liangding
 * @create 2018-06-07 下午4:43
 **/
@Data
public class ExtendedDto implements Serializable {
    private Map<String, Object> ext;

    private String docType;
}
