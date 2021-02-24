package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.TelecomValidationDto;
import com.ideatech.ams.annual.dto.TelecomValidationSearchDto;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.service.BaseService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

public interface TelecomValidationSerivce extends BaseService<TelecomValidationDto> {
    List<TelecomValidationDto> findByBatchNo(String batchNo);

    PagingDto<TelecomValidationDto> query(TelecomValidationSearchDto searchDto, PagingDto pagingDto);

    List<TelecomValidationDto> queryAll(TelecomValidationSearchDto searchDto);

    /**
     * 导出excel
     * @param tvdList 电信三要素校验数据
     * @return
     */
    HSSFWorkbook exportExcel(List<TelecomValidationDto> tvdList);

    List<TelecomValidationDto> findSameDto(String name,String tel,String idCardNo,String batchNo);

}
