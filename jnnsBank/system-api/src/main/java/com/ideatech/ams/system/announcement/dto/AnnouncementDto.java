package com.ideatech.ams.system.announcement.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * @author jzh
 * @date 2019/2/25.
 */
@Data
public class AnnouncementDto{

    private Long id;

    //公告标题
    private String title;

    //公告内容
    private String content;

    //公告生成时间
    private String noticeDate;

    /**
     * 机构名称
     */
    private String organName;

    /**
     * 完整机构ID organFullId
     */
    private String organfullId;

    private String username;
}
