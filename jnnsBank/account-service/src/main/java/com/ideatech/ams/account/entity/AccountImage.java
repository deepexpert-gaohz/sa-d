package com.ideatech.ams.account.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户证件表
 * @author houxianghua
 */
@Entity
@Table(name = "ACCOUNTS_IMAGE")
@Data
public class AccountImage extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_ACCOUNTS_IMAGE";

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
    @Column(length = 1000)
    private String filePath;

    /**
     * 证件类型
     */
    private String fileType;

    /**
     * 流水号
     */
    private Long acctBillsId;

    /**
     * 批次号
     */
    @Column(length = 1000)
    private String batchNo;

    /**
     * 证件编号
     */
    @Column(length = 50)
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
     * 上传状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private CompanyIfType syncStatus;

}
