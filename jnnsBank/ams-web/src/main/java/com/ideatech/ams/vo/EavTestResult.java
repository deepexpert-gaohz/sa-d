package com.ideatech.ams.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ideatech.common.eav.EavJsonSerializer;
import com.ideatech.common.eav.ExtendedAttribute;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liangding
 * @create 2018-07-23 下午6:51
 **/
@Data
@JsonSerialize(using = EavJsonSerializer.class)
public class EavTestResult {

    @ExtendedAttribute
    private Map<String, String> ext = new HashMap<>();

    private List<String> stringList;

    private Long id;

}
