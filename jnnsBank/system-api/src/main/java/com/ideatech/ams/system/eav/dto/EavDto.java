package com.ideatech.ams.system.eav.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liangding
 * @create 2018-07-23 下午5:16
 **/
@Data
public class EavDto implements Serializable {
    private Long docId;
    private Long attrId;
    private Long entityId;
    private String name;
    private String value;
}
