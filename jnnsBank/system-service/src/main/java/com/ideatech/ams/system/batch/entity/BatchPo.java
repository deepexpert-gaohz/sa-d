package com.ideatech.ams.system.batch.entity;

import com.ideatech.ams.system.batch.enums.BatchTypeEnum;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 批处理
 * @author fantao
 * @create 2018年10月17日15:33:05
 **/
@Data
@Entity
@Table(name="sys_batch")
public class BatchPo extends BaseMaintainablePo {

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 业务量, 本批次数据条数
     */
    private Long txCount;

    /**
     * 文件大小(Byte)
     */
    private Long fileSize;

    /**
     * 处理时间
     */
    private String processTime;

    /**
     * 是否处理
     */
    private Boolean process;

    /**
     * 批处理类型
     */
    private BatchTypeEnum type;

}
