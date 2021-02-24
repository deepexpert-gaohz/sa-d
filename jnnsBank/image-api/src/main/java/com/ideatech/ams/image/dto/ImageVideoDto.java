package com.ideatech.ams.image.dto;

import com.ideatech.ams.image.enums.BusinessTypeEnum;
import com.ideatech.ams.image.enums.RecordTypeEnum;
import com.ideatech.ams.image.enums.StoreType;
import com.ideatech.common.dto.BaseMaintainableDto;
import com.ideatech.common.enums.CompanyAcctType;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class ImageVideoDto extends BaseMaintainableDto {
    private Long id;
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
     * 视频保存路径
     */
    private String filePath;
    /**
     * 是否上传影像平台
     */
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
    private CompanyAcctType acctType;
    /**
     * 账号
     */
    private String acctNo;

    /**
     *存款人名称
     */
    private String depositorName;
    /**
     * 法人姓名
     */
    private String legalName;
    /**
     * 工商注册编号
     */
    private String regNo;
    /**
     * 预约编号
     */
    private String applyid;
    /**
     * 视频存储方式
     */
    private StoreType type;

    private String dateTime;

    private String organFullId;
    /**
     * 操作人
     */
    private String username;

    private String beginDate;
    private String endDate;
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
     * 双录系统会议ID
     */
    private String callId;

    /**
     * 业务类型
     */
    private BusinessTypeEnum businessType;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 短信内容
     */
    private String message;

    /**
     * 双录方式
     */
    private RecordTypeEnum recordType;
}
