package com.ideatech.ams.system.batch.dto;

import com.ideatech.ams.system.batch.enums.BatchTypeEnum;
import com.ideatech.common.dto.PagingDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批处理
 *
 * @author fantao
 * @create 2018年10月17日15:33:30
 **/
@Data
public class BatchSearchDto extends PagingDto<BatchDto> implements Serializable {

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 文件名
     */
    private String fileName;


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
    private List<BatchTypeEnum> type;

    /**
     * 处理时间开始
     */
    private String processTimeStart;

    /**
     * 处理时间结束
     */
    private String processTimeEnd;

}
