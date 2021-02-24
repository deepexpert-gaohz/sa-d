package com.ideatech.ams.ws.dto;

import lombok.Data;

/**
 * 影像批次号写入信息
 * @Author yang
 * @Date 2019/8/23 9:26
 * @Version 1.0
 */
@Data
public class ImageBatchInfo {
    /**
     * 流水id
     */
    private Long billsId;
    /**
     * 图片影像批次号
     */
    private String imgBatchNumber;
    /**
     * 视频影像批次号
     */
    private String videoBatchNumber;

    /**
     * 影像类型code
     */
    private String docCode;
    /**
     * 渠道号
     */
    private String chanlNo;
    /**
     * 图片影像路径
     */
    private String imgPath;
    /**
     * 视频影像路径
     */
    private String videoPath;
    /**
     * 图片影像名称
     */
    private String  imgFileName;
    /**
     * 视频影像名称
     */
    private String  videoFileName;

}
