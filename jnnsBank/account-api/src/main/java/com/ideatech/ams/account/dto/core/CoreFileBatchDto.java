package com.ideatech.ams.account.dto.core;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * 核心t+1文件记录
 * @author van
 */
@Data
public class CoreFileBatchDto extends BaseMaintainableDto {

    private Long id;

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
