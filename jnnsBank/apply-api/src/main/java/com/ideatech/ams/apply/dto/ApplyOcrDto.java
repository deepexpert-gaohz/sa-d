package com.ideatech.ams.apply.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.util.Date;

@Data
public class ApplyOcrDto extends BaseMaintainableDto {

    public ApplyOcrDto(){

    }

    public ApplyOcrDto(OcrInfo info, String type, String url){

        this.setApplyid(info.getApplyId());
        this.setDoccode(type);
        this.setCreatedDate(new Date());
        url=url.replace(" ", "");
        this.setImgpath(url);
        this.setFilename(url.substring(url.lastIndexOf("/") + 1));
    }

    private Long id;

    /**
     * 预约编号
     */
    private String applyid;

    /**
     * 影像类别码
     */
    private String doccode;

    /**
     * 影像文件名
     */
    private String filename;

    /**
     * 影像路径
     */
    private String imgpath;

    /**
     * 当前图片是第几张
     */
    private Integer curNum;

}