package com.ideatech.ams.service;

import com.ideatech.ams.domain.zhjn.*;
import com.ideatech.ams.dto.ZhjnCustomerDto;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;

/**
 * 智慧江南接口
 *
 * @auther yfy
 * @create 2020年8月6日15:14:45
 **/
public interface ZhjnCustomerService {

    void testAdd(ZhjnCustomerInfo info,ZhjnCustomerInfo info2);

    /**
     * 经办人办理
     *
     * @param req
     */
    ResultDto clerkCustomer(ZhjnClerkReq req);

    /**
     * 有权人审核
     *
     * @param req
     */
    ResultDto checkCustomer(ZhjnCheckReq req);

    /**
     * 有权人列表
     *
     * @param req
     */
    ResultDto getCustomerByCheck(ZhjnCheckListReq req);

    /**
     * 经办人列表
     *
     * @param req
     */
    ResultDto getCustomerByClerk(ZhjnClerkListReq req);

    TableResultResponse<ZhjnCustomerDto> query(ZhjnCustomerDto dto, Pageable pageable);

    TableResultResponse<ZhjnCustomerDto> query1(ZhjnCustomerDto dto, Pageable pageable);
}
