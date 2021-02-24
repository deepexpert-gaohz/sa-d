package com.ideatech.ams.service;

import com.ideatech.ams.dto.SaicQuery.BreakLaw;
import com.ideatech.ams.dto.SaicQuery.Deabbeat;
import com.ideatech.ams.dto.SaicQuery.Owing;
import com.ideatech.ams.dto.SaicQuery.SaicQuery;
import com.ideatech.ams.dto.ZhjnCustomerDto;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;

public interface JnnsSaicTestService {

    SaicQuery doQuery() ;


    /**
     * 查询老赖分页
     * @param dto
     * @param pageable
     * @return
     */
    @Transactional
    TableResultResponse<Deabbeat> queryDeabbeat(Deabbeat dto, Pageable pageable);

    /**
     * 查询欠税分页
     * @param dto
     * @param pageable
     * @return
     */
    @Transactional
    TableResultResponse<Deabbeat> queryDeabbeat1(Deabbeat dto, Pageable pageable);
    @Transactional
    TableResultResponse<Owing> queryOwing(Owing dto, Pageable pageable);

    @Transactional
    TableResultResponse<Owing> queryOwing1(Owing dto, Pageable pageable);
    /**
     * 查询违法
     * @param dto
     * @param pageable
     * @return
     */
    @Transactional
    TableResultResponse<BreakLaw> queryBreakLaw(BreakLaw dto, Pageable pageable);

    @Transactional
    TableResultResponse<BreakLaw> queryBreakLaw1(BreakLaw dto, Pageable pageable);
}
