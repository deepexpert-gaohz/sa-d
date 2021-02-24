package com.ideatech.ams.system.announcement.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 公告附件
 * @author jzh
 * @date 2019/2/26.
 */
@Entity
@Data
@Table(name = "yd_sys_attachment")
public class Attachment extends BaseMaintainablePo {

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
