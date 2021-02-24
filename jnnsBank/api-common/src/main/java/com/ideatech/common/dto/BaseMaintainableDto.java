package com.ideatech.common.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author liangding
 * @create 2018-05-03 下午5:05
 **/
@Data
public class BaseMaintainableDto extends BaseDto {
    private String lastUpdateBy;
    private Date lastUpdateDate;
}
