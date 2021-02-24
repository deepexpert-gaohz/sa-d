package com.ideatech.ams.system.annotation.service;


import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.system.annotation.dto.MessageLogDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

public interface MessageLogService extends BaseService<MessageLogDto> {

    TableResultResponse<MessageLogDto> queryPage(MessageLogDto dto, Pageable pageable);

    void saveMessageLog(MessageLogDto messageLogDto);

    JSONObject findByDate(MessageLogDto dto);

    IExcelExport exportStatisticePoiExcel(MessageLogDto dto);
}
