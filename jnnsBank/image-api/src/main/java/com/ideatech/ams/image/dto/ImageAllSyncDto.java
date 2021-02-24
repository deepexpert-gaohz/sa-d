package com.ideatech.ams.image.dto;

import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import java.io.File;
import java.util.Date;
import java.util.List;

@Data
public class ImageAllSyncDto {
    private Long id;
    /**
     * 创建日期
     */
    private Date createdDate;
    /**
     * 创建人员
     */
    private String createdBy;
    /**
     * 最后修改人员
     */
    private String lastUpdateBy;

    /**
     * 最后修改日期
     */
    private Date lastUpdateDate;
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
     * 影像类型code
     */
    private String docCode;

    /**
     * 影像类型名称
     */
    private String docName;

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
    private String imgPath;
    /**
     *图片格式
     */
    private String fileFormat;
    /**
     * 临时编号，用于接口调用做关联使用
     */
    private String tempNo;

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
    private CompanyIfType syncStatus = CompanyIfType.No;
    /**
     * 影像file
     */
    private File file;
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
