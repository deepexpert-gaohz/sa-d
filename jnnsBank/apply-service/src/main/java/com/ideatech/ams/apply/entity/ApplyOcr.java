package com.ideatech.ams.apply.entity;

import com.ideatech.ams.apply.dto.OcrInfo;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 预约影像表
 * @author
 */
@Entity
@Table(name = "yd_applyocr")
@Data
public class ApplyOcr extends BaseMaintainablePo {

    public ApplyOcr(){

    }

    public ApplyOcr(OcrInfo info, String type, String url){

        this.setApplyid(info.getApplyId());
        this.setDoccode(type);
        this.setCreatedDate(new Date());
        url=url.replace(" ", "");
        this.setImgpath(url);
        this.setFilename(url.substring(url.lastIndexOf("/") + 1));
    }

    /**
     * 预约编号
     */
    @Column(name = "yd_applyid")
    private String applyid;

    /**
     * 影像类别码
     */
    @Column(name = "yd_doccode")
    private String doccode;

    /**
     * 影像文件名
     */
    @Column(name = "yd_filename")
    private String filename;

    /**
     * 影像路径
     */
    @Column(name = "yd_imgpath")
    private String imgpath;

    /**
     * 当前图片是第几张
     */
    @Column(name = "yd_curnum")
    private Integer curNum;

}