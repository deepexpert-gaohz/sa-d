package com.ideatech.ams.system.announcement.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 公告
 * @author jzh
 * @date 2019/2/25.
 */
@Data
@Entity
@Table(name = "yd_sys_announcement")
@Where(clause = "yd_deleted = 0")
public class Announcement extends BaseMaintainablePo {
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

    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;
}
