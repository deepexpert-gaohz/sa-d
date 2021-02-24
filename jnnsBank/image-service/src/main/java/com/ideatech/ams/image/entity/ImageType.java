package com.ideatech.ams.image.entity;


import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.ams.image.enums.BillType;
import com.ideatech.ams.image.enums.CompanyAcctType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 影像类型
 */
@Entity
@Table(name = "ImageType")
@Data
public class ImageType extends BaseMaintainablePo implements Serializable {
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_ImageType";
    /**
     * 账户类型
     */
    @Enumerated(EnumType.STRING)
    private CompanyAcctType acctType;
    /**
     * 业务操作类型
     */
    @Enumerated(EnumType.STRING)
    private BillType operateType;
    /**
     * 存款人性质
     */
    private String depositorType;
    /**
     * 存款人性质码值
     */
    private String depositorTypeCode;
    /**
     * 影像种类名称
     */
    private String imageName;
    /**
     * 影像种类value
     */
    private String value;
    /**
     * 是否必选
     * 0 不是必选 ；1 必选
     */
    private String choose;

    /**
     * 扩展字段1
     */
    private String string01;

    /**
     * 扩展字段2
     */
    private String string02;

    /**
     * 扩展字段3
     */
    private String string03;

    /**
     * 扩展字段4
     */
    private String string04;

    /**
     * 扩展字段5
     */
    private String string05;
}
