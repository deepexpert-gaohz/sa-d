package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.CompareTaskDto;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.common.dto.ResultDto;
import org.springframework.web.multipart.MultipartFile;

public interface DataImporter {

    void importData(CompareTaskDto task, DataSourceDto dataSource, MultipartFile file);

    void importData(CompareTaskDto task, DataSourceDto dataSource, String fileName) throws Exception;
}
