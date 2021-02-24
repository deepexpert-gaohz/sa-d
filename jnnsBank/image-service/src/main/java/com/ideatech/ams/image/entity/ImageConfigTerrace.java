package com.ideatech.ams.image.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 影像平台配置
 */
@Entity
@Table(name = "IMAGE_CONFIG_TERRACE")
@Data
public class ImageConfigTerrace extends BaseMaintainablePo implements Serializable {
    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635342L;
    /**
     * 影像平台id
     */
    private String ip;
    /**
     * 影像平台端口
     */
    private String socketPort;
    /**
     * 影像平台登录名
     */
    private String username;
    /**
     * 影像平台登录密码
     */
    private String password;

    private String serverName;
    private String groupName;
    private String coreGroupName;

    private String STARTCOLUMN;

    private String modelCode;

    private String filePartName;

    /**
     * 扩展字段1
     */
    private String string001;

    /**
     * 扩展字段2
     */
    private String string002;

    /**
     * 扩展字段3
     */
    private String string003;

    /**
     * 扩展字段4
     */
    private String string004;

    /**
     * 扩展字段5
     */
    private String string005;

}
