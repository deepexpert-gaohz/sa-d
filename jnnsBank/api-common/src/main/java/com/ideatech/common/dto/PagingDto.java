package com.ideatech.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liangding
 * @create 2018-05-03 下午5:06
 **/
@Data
public class PagingDto<T> implements Serializable {
    // 每页显示记录数
    private Integer           limit         = 20;
    // 当前页数
    private Integer           offset      = 1;
    // 总记录数
    private Long              totalRecord = 0L;
    // 总页数
    private Integer           totalPages = 0;
    // 数据List
    private List<T> list = new ArrayList<>();
}
