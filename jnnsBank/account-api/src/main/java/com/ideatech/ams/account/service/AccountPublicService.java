package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.common.dto.PagingDto;

import java.util.List;
import java.util.Map;

/**
 * @author vantoo
 * @date 16:16 2018/5/28
 */
public interface AccountPublicService {

    AccountPublicInfo findByAccountId(Long accountId);

    List<AccountPublicInfo> findListByAccountId(Long accountId);

    AccountPublicInfo findByAcctNo(String acctNo);

    void save(AccountPublicInfo accountPublicInfo);

    void updateAcctType(Long accountId, CompanyAcctType acctType, Long refbillId);

    void updateAccountKey(AmsAccountInfo info);

    Map<Long,AccountPublicInfo> findAllInMap();

    void deleteByAccountId(Long accountId);

    List<AccountPublicInfo> findByOrganFullId(String organFullId);

    boolean deleteAccountsAll(Long id);

    /**
     * 取消核准保存accountLicenseNo
     * @return
     */
    int updateCancelHezhun(Long accountId, String accountLicenseNo);
}
