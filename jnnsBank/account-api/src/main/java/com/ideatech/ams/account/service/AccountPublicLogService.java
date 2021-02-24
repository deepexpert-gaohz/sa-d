package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.dto.AccountPublicLogInfo;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.List;

/**
 * @author vantoo
 * @date 16:16 2018/5/28
 */
public interface AccountPublicLogService {

    AccountPublicLogInfo getOne(Long id);

    List<AccountPublicLogInfo> findByAccountId(Long accountId);

    AccountPublicLogInfo findByRefBillId(Long refBillId);

    AccountPublicLogInfo findByRefBillIdLast(Long refBillId);

    AccountPublicLogInfo findByAccountIdBackup(Long accountId);

    //保存操作
    void save(AccountPublicLogInfo accountPublicLogInfo);

    //保存或者更新操作
    void saveOrUpdate(AccountPublicLogInfo accountPublicLogInfo);

    AccountPublicLogInfo getMaxSeq(Long accountId);

    int deleteByAccountIdAndRefBillId(Long accountId, Long refBillId);

    void delete(Long id);

    List<AccountPublicLogInfo> findByOrganFullIdLike(String organFullId);

    List<AccountPublicLogInfo> findByCustomerNo(String customerNo);

    Long countOperatorIdcardDueBefore(String afterDateStr, String beforeDate, String organFullId, Boolean operatorOverConfigEnabled);

    PagingDto<AccountPublicLogInfo> listOperatorIdcardDueBefore(Boolean isOperatorIdcardDue, String operatorIdcardDue, String afterDate, String beforeDate,
                                                             String organFullId, PagingDto pagingDto) throws Exception;

    PagingDto<AccountPublicLogInfo> listOperatorIdcardDueBefore1(Boolean isOperatorIdcardDue, String operatorIdcardDue, String afterDate, String beforeDate,
                                                                String organFullId, PagingDto pagingDto) throws Exception;
    /**
     * 导出经办人到期信息
     */
    IExcelExport exportOperatorIdcardDueBefore(Boolean isOperatorIdcardDue, String operatorIdcardDue, String afterDate, String beforeDate,String organFullId) throws Exception;

    /**
     * 经办人证件到期超期查询（在定时短信提醒时使用）
     * @param afterDate
     * @param beforeDate
     * @param operatorOverConfigEnabled
     * @return
     */
    List<AccountPublicInfo> getOperatorDueAndOver(String afterDate, String beforeDate, Boolean operatorOverConfigEnabled);
}
