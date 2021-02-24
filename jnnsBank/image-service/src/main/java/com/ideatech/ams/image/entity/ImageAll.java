package com.ideatech.ams.image.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
/**
 * 影像表
 */
@Entity
@Table(name = "IMAGE_ALL")
@Data
public class ImageAll extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_IMAGE_ALL";
    /**
     * 流水id
     */
    private Long billsId;

    /**
     * 账户id
     */
    private Long acctId;
    /**
     * 客户id
     */
    private Long customerId;
    /**
     * 传给影像平台后存储批次号
     */
    private String batchNumber;

    /**
     * 影像类型
     */
    private String docCode;
    /**
     * 影像名称
     */
    private String fileName;
    /**
     *影像中文名称
     */
    private String fileNmeCN;
    /**
     * 影像路径
     */
    @Column(length = 1000)
    private String imgPath;

    /**
     * 证件编号
     */
    private String fileNo;
    /**
     * 证件到期日
     */
    private String expireDate;
    /**
     * 是否上传影像平台
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private CompanyIfType syncStatus = CompanyIfType.No;
    /**
     *图片格式
     */
    private String fileFormat;
    /**
     * 临时编号，用于接口调用做关联使用
     */
    private String tempNo;
    /**
     * 渠道号
     */
    private String chanlNo;
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
