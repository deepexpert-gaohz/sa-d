package com.ideatech.ams.system.batch.dto;

import com.ideatech.ams.system.batch.enums.BatchTypeEnum;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * 批处理
 *
 * @author fantao
 * @create 2018年10月17日15:33:30
 **/
@Data
public class BatchDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小(Byte)
     */
    private Long fileSize;

    /**
     * 业务量, 本批次数据条数
     */
    private Long txCount;

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
