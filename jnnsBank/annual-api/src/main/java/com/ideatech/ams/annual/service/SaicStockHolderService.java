package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.SaicStockHolderDto;
import com.ideatech.ams.annual.dto.SaicStockHolderSearchDto;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.service.BaseService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

public interface SaicStockHolderService extends BaseService<SaicStockHolderDto> {
    PagingDto<SaicStockHolderDto> query(SaicStockHolderSearchDto searchDto, PagingDto pagingDto);

    List<SaicStockHolderDto> queryAll(SaicStockHolderSearchDto searchDto);

    /**
     * 导出excel
     * @param sshdList 控股股东比对数据
     * @return
     */
    HSSFWorkbook exportExcel(List<SaicStockHolderDto> sshdList);
}
