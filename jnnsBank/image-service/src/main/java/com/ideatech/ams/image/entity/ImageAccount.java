package com.ideatech.ams.image.entity;

import com.ideatech.ams.image.enums.IsUpload;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 *保存账户最新影像
 */
@Entity
@Data
public class ImageAccount extends BaseMaintainablePo implements Serializable {
    private static final long serialVersionUID = 5454155825314635342L;
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
     * 账户表的ID
     */
    private Long acctId;
    /**
     * 流水ID
     */
    private Long refBillId;
    /**
     * 证件编号
     */
    private String number;
    /**
     * 证件到期日
     */
    private String expireDateStr;
    /**
     * 是否已经上传到影像平台
     */
    @Enumerated(EnumType.STRING)
    private IsUpload isUpload=IsUpload.FALSE;

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
