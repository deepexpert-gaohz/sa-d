package com.ideatech.ams.mivs.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019/7/25.
 * 公告信息报文实体
 */

@Entity
@Data
@Table(name = "mivs_announcement")
public class AnnouncementInformation extends MessageHeader {

    /**
     * 回复标识
     * RPLY:需要回复
     * NRPL:无需回复
     */
    @Column(length = 8)
    private String rplyFlag;

    /**
     * 信息内容
     */
    @Column(length = 1000)
    private String msgCntt;

    /**
     * 扩展字段
     */
    private String String001;
    private String String002;
    private String String003;
    private String String004;
    private String String005;
    private String String006;
    private String String007;
    private String String008;
    private String String009;
    private String String010;
    private String String011;
    private String String012;
    private String String013;
    private String String014;
    private String String015;
}
