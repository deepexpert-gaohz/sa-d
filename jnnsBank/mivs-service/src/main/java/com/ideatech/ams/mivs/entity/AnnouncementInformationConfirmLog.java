package com.ideatech.ams.mivs.entity;


import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019-09-02
 * mivs.333.001.01	公告信息确认报文	参与机构->MIVS
 */

@Entity
@Data
@Table(name = "mivs_announcement_log")
public class AnnouncementInformationConfirmLog extends ExtendEntity {

    /**
     * 原报文标识号
     */
    private String msgId;

    /**
     * 原发起直接参与机构
     */
    private String instgDrctPty;

    /**
     * 原发起参与机构
     */
    private String instgPty;

    /**
     * 附加信息
     */
    @Column(length = 1000)
    private String msgCntt;

}
