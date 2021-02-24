package com.ideatech.ams.customer.dto.illegal;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class IllegalQueryBatchDto extends BaseMaintainableDto {

    /**
     * ID
     */
    private Long id;
    /**
     * 批次号
     */
    private String illegalbatchNo;

    /**
     * 批次数量
     */
    private int batchNum;
    /**
     * 批此时间
     */
    private String batchDate;

    /**
     * 开始日期
     */
    private String beginDate;

    /**
     * 结束日期
     */
    private String endDate;

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
