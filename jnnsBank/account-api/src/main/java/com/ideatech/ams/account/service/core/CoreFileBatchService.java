package com.ideatech.ams.account.service.core;

import com.ideatech.ams.account.dto.core.CoreFileBatchDto;
import com.ideatech.common.service.BaseService;

import java.io.File;


public interface CoreFileBatchService extends BaseService<CoreFileBatchDto> {

    /**
     * 创建文件批次
     * @param file
     */
    Long create(File file);

}
