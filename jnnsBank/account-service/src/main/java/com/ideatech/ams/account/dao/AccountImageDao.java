package com.ideatech.ams.account.dao;

import com.ideatech.ams.account.entity.AccountImage;
import com.ideatech.common.enums.CompanyIfType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by houxianghua on 2018/11/9.
 */
@Repository
public interface AccountImageDao extends JpaRepository<AccountImage, Long>, JpaSpecificationExecutor<AccountImage> {
    List<AccountImage> findByAcctId(Long acctId);
    List<AccountImage> findByAcctIdAndAcctBillsId(Long acctId, Long acctBillsId);
    List<AccountImage> findByAcctBillsId(Long acctBillsId);
    List<AccountImage> findByTempId(String tempId);
    List<AccountImage> findByAcctIdAndAcctBillsIdAndFileType(Long acctId, Long acctBillsId, String fileType);
    AccountImage findByBatchNo(Long batchNo);
    List<AccountImage> findTop10BySyncStatus(CompanyIfType companyIfType);

    List<AccountImage> findByAcctIdIn(List<Long> acctIds);
}
