package com.ideatech.ams.account.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;

/**
 * Created by houxianghua on 2018/11/9.
 */
@Data
public class AccountImageInfo extends BaseMaintainableDto {

    private Long id;

    /**
     * 账户id
     */
    private Long acctId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件存储路径
     */
    private String filePath;

    /**
     * 证件类型id
     */
    private String fileType;

    /**
     * 流水号
     */
    private Long acctBillsId;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 证件编号
     */
    private String fileNo;

    /**
     * 证件到期日
     */
    private String maturityDate;

    /**
     * 临时的ID
     */
    private String tempId;

    /**
     * 图片类型ID
     */
    private Long imageTypeId;

    /**
     * 文件的base64详情
     */
    private List<String> fileDetail;
    /**
     * 上传状态
     */
    private CompanyIfType syncStatus;
//    /**
//     * 证件类型名称
//     */
//    private Integer fileTypeName;

    /**
     * 图片类型ID
     */
    private String docCode;

    /**
     * 图片类型名称
     */
    private String docName;
}
