package com.ideatech.ams.customer.service;


import com.ideatech.ams.customer.dto.SaicMonitorDto;
import com.ideatech.ams.customer.dto.SaicStateDto;
import com.ideatech.ams.customer.enums.SaicMonitorEnum;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.TreeTable;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

public interface SaicMonitorService extends BaseService<SaicMonitorDto> {

    TreeTable query(Long nodeId, Long organId);

    TableResultResponse<SaicMonitorDto> queryPage(SaicMonitorDto dto, Pageable pageable);

    IExcelExport exportSaicMonitorExcel(SaicMonitorDto dto);

    SaicMonitorDto getSaicMonitor(String userName, Long organId, String keyWord, Long saicInfoId, String regNo,SaicMonitorEnum saicMonitorEnum);

    /**
     * 获取工商接口状态
     * @return
     */
    SaicStateDto getState();
}
