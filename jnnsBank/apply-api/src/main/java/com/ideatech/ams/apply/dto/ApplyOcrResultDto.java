package com.ideatech.ams.apply.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description 前端预约影像的记录
 * @Author wanghongjie
 * @Date 2018/10/25
 **/
@Data
public class ApplyOcrResultDto {
    private Integer totalNum;
    private Integer curNum;
    private Integer syncNum;
    private List<String> fileNames;
}
