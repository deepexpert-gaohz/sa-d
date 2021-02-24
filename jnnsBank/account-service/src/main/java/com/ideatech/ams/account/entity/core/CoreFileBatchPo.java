package com.ideatech.ams.account.entity.core;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 核心t+1文件记录
 * @author van
 */
@Data
@Entity
@Table(name = "core_fileBatch")
public class CoreFileBatchPo extends BaseMaintainablePo {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小(Byte)
     */
    private Long fileSize;

    /**
     * 处理时间
     */
    private String processTime;

    /**
     * 是否全部处理
     */
    private Boolean processStatus;
}
