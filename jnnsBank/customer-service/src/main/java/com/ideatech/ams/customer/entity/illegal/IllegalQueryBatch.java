package com.ideatech.ams.customer.entity.illegal;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 违法失信查询批次
 *
 */
@Table(name = "yd_illegal_query_batch")
@Entity
@Data
public class IllegalQueryBatch extends BaseMaintainablePo {

    /**
     * 批次号
     */
    private String illegalbatchNo;

    /**
     * 批次数量
     */
    private int batchNum;

    /**
     * 批次时间
     *  yyyy-mm-dd
     */
    private String batchDate;

    /**
     * 是否完成
     */
    private Boolean process;

    /**
     * 机构FULLID
     */
    private String organFullId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小(Byte)
     */
    private Long fileSize;
}
