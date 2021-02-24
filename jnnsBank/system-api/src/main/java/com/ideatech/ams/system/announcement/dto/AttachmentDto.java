package com.ideatech.ams.system.announcement.dto;

import lombok.Data;

/**
 * @author jzh
 * @date 2019/2/27.
 */

@Data
public class AttachmentDto {

    private Long id;

    private Long announcementId;

    //公告附件路径
    private String fullPath;

    //公告附件名称
    private String filename;


    //公告附件描述
    //private String description;

    //公告附件大小，单位KB
    private Long size;
}
