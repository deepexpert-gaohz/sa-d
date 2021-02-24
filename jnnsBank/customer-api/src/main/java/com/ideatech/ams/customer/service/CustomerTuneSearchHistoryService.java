package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dto.CustomerTuneSearchHistoryDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 客户综合尽调service
 */
public interface CustomerTuneSearchHistoryService extends BaseService<CustomerTuneSearchHistoryDto> {

//    /**
//     * 添加
//     */
//    void save(CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto);

    /**
     * 分页查询
     *
     * @param dto      查询参数
     * @param pageable 分页参数
     * @return
     */
    TableResultResponse<CustomerTuneSearchHistoryDto> query(CustomerTuneSearchHistoryDto dto, Pageable pageable);


    List<CustomerTuneSearchHistoryDto> findByOrganFullIdLike(String organFullId);
}
