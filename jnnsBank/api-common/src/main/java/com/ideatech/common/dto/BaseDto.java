package com.ideatech.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liangding
 * @create 2018-05-03 下午5:04
 **/
@Data
public class BaseDto implements Serializable {
    private Date createdDate;

    private String createdBy;

}
