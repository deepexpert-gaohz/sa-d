package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.SaicBeneficiaryDto;
import com.ideatech.ams.annual.dto.SaicBeneficiarySearchDto;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.service.BaseService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

public interface SaicBeneficiarySerivce extends BaseService<SaicBeneficiaryDto> {
    PagingDto<SaicBeneficiaryDto> query(SaicBeneficiarySearchDto searchDto, PagingDto pagingDto);

    List<SaicBeneficiaryDto> queryAll(SaicBeneficiarySearchDto searchDto);

    /**
     * 导出excel
     * @param sbdList 受益人比对数据
     * @return
     */
    HSSFWorkbook exportExcel(List<SaicBeneficiaryDto> sbdList);

    /**
     * 导出excel
     * @param sbdList 受益人企业工商采集数据
     * @return
     */
    HSSFWorkbook exportCollectExcel(List<SaicBeneficiaryDto> sbdList);

    /**
     * 导出excel
     * @param sbdList 受益人名称比对导出excel
     * @return
     */
    HSSFWorkbook exportBeneficiaryNameExcel(List<SaicBeneficiaryDto> sbdList);
}
