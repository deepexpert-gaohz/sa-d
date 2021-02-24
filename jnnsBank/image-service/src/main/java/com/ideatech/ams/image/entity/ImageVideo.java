package com.ideatech.ams.image.entity;


import com.ideatech.ams.image.enums.BusinessTypeEnum;
import com.ideatech.ams.image.enums.RecordTypeEnum;
import com.ideatech.ams.image.enums.StoreType;
import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.enums.CompanyAcctType;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 双录视频关联
 */
@Entity
@Table(name = "IMAGE_VIDEO")
@Data
public class ImageVideo extends BaseMaintainablePo implements Serializable {
    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635342L;
    /**
     * 双录编号
     */
    private String recordsNo;
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
     * 保存路径
     */
    @Column(length = 1000)
    private String filePath;
    /**
     * 是否上传影像平台
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private CompanyIfType syncStatus;

    /**
     *视频格式
     */
    private String fileFormat;
    /**
     * 视频名称
     */
    private String fileName;
    /**
     * 账户性质
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CompanyAcctType acctType;

    /**
     * 账号
     */
    @Column(length = 32)
    private String acctNo;

    /**
     *存款人名称
     */
    @Column(length = 100)
    private String depositorName;
    /**
     * 法人姓名
     */
    @Column(length = 50)
    private String legalName;
    /**
     * 工商注册编号
     */
    @Column(length = 100)
    private String regNo;
    /**
     * 预约编号
     */
    private String applyid;
    /**
     * 视频存储方式
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StoreType type;
    /**
     * 双录时间
     */
    @Column(length = 20)
    private String dateTime;

    private String organFullId;
    /**
     * 操作人
     */
    @Column(length = 20)
    private String username;
    /**
     * 渠道号
     */
    private String chanlNo;
    /**
     * 差错类型
     */
    private String vErrorCode;
    /**
     * 差错类型
     */
    private String vErrorValue;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 双录视频来源
     */
    private String source;

    /**
     * 业务类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private BusinessTypeEnum businessType;

    /**
     * 客户姓名
     */
    @Column(length = 32)
    private String customerName;

    /**
     * 人脸识别结果
     * -1： 未认证；0： 认证中；
     * 1： 认证通过；2： 认证不通过、3 无需认证
     */
    @Column(length = 2)
    private String faceResult;

    /**
     * 双录方式
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RecordTypeEnum recordType;
}
