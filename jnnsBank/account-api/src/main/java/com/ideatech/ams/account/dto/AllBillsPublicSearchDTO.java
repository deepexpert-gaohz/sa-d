package com.ideatech.ams.account.dto;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

/**
 * Created by houxianghua on 2018/11/28.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AllBillsPublicSearchDTO extends AllBillsPublicDTO {

    /**
     * 网点机构号（核心机构号）
     */
    private String kernelOrgCode;

    /**
     * 操作类型（中文名）
     */
    private String billTypeStr;

    /**
     * 创建人（中文名）
     */
    private String createdName;

    /**
     * 变更记录json字符串
     */
    private String changeRecordJsonStr;

    /**
     * 账户名称
     */
    private String acctName;
    /**
     * 影像上报状态
     */
    private CompanySyncStatus imgaeSyncStatus;
    /**
     *
     * 下载状态
     */
    @Column(length = 255)
    private String downloadstatus;

    /**
     *
     * 证明文件1
     */
    @Column(length = 255)
    private String fileNo;


    /**
     *
     * 影像上传状态
     */
    @Column(length = 255)
    private String uploadstatus;



}
