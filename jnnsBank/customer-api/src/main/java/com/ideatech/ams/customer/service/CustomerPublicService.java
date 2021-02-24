package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dto.CustomerAllResponse;
import com.ideatech.ams.customer.dto.CustomerPublicInfo;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @author vantoo
 * @date 15:48 2018/5/25
 */
public interface CustomerPublicService {

    CustomerPublicInfo getOne(Long id);

    void save(CustomerPublicInfo customerPublicInfo);

    CustomerPublicInfo getByCustomerNo(String customerNo);

    CustomerPublicInfo getByCustomerId(Long customerId);

    CustomerPublicInfo getByDepositorName(String depositorName);

    Map<Long,CustomerPublicInfo> findAllInMap();

    Long countFileDueBefore(String afterDate, String beforeDate, String organFullId, Boolean fileOverConfigEnabled);

    Long countLegalIdcardDueBefore(String afterDateStr, String beforeDateStr, String organFullId, Boolean legalOverConfigEnabled);

    @Transactional
    PagingDto<CustomerAllResponse> listCustDueBefore(String type, CustomerPublicInfo customerPublicInfo, String afterDateStr, String beforeDateStr, String organFullId, PagingDto pagingDto) throws Exception;

    @Transactional
    PagingDto<CustomerAllResponse> listCustDueBefore1(String type, CustomerPublicInfo customerPublicInfo, String afterDateStr, String beforeDateStr, String organFullId, PagingDto pagingDto) throws Exception;
    /**
     * 法人证件和证明文件种类到期提醒是否超期字段的更新
     */
    void updateCustomerNoticeDue(String type);

    IExcelExport exportCustDueBefore(String type, CustomerPublicInfo customerPublicInfo, String afterDateStr, String beforeDateStr, String organFullId) throws Exception;

    /**
     * 法人证件到期超期查询（在定时短信提醒时使用）
     * @param afterDateStr
     * @param beforeDateStr
     * @param legalOverConfigEnabled
     * @return
     */
    List<CustomerAllResponse> getLegalDueAndOver(String afterDateStr, String beforeDateStr, Boolean legalOverConfigEnabled);

    /**
     * 证件文件到期超期查询（在定时短信提醒时使用）
     * @param afterDateStr
     * @param beforeDateStr
     * @param fileOverConfigEnabled
     * @return
     */
    List<CustomerAllResponse> getFileDueAndOver(String afterDateStr, String beforeDateStr, Boolean fileOverConfigEnabled);

    List<CustomerPublicInfo> findAll();

}
