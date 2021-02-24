package com.ideatech.ams.customer.service.newcompany;

import com.ideatech.ams.customer.dto.neecompany.FreshCompanyDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;

public interface FreshCompanyService {
    TableResultResponse<FreshCompanyDto> query(FreshCompanyDto dto, Pageable pageable);

    void add(String provinceCode, String startDate, String endDate);

    /**
     * 删除time前的新注册企业信息。
     * @param time
     */
    void delete(String time);

    FreshCompanyDto detail(Long id);

    IExcelExport exportExcel();
}
