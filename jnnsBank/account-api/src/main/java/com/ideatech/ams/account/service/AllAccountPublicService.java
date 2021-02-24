package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dto.AllAccountPublicDTO;
import com.ideatech.ams.account.dto.AllAccountPublicSearchDTO;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;

public interface AllAccountPublicService {

    ObjectRestResponse<AllAccountPublicDTO> getDetailsByAccountId(Long accountId);

    ObjectRestResponse<AllAccountPublicDTO> getDetails(Long dataId);

    TableResultResponse<AllAccountPublicDTO> queryLastBills(AllAccountPublicDTO info, Pageable pageable);

    TableResultResponse<AllAccountPublicSearchDTO> query(AllAccountPublicSearchDTO info, String certainOrganFullId, Pageable pageable);

    TableResultResponse<AllAccountPublicSearchDTO> query(Long id, Pageable pageable);

    TableResultResponse<AllAccountPublicDTO> queryStockList(AllAccountPublicDTO info, Pageable pageable);

    IExcelExport query(AllAccountPublicSearchDTO info);
}
