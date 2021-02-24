package com.ideatech.ams.image.dto;

import lombok.Data;

/**
 * 菊风双录系统根据会议id返回的会议视频信息
 * @author jzh
 * @date 2020-01-08.
 */

@Data
public class VideoFileDTO {

    private Long id;

    private String fileId;

    private String fileName;

    private String fileUrl;

    private Long mid;

    private String createTime;
}

