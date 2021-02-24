package com.ideatech.ams.account.dao;

import com.ideatech.ams.account.entity.AccountChangeItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountChangeItemDao extends JpaRepository<AccountChangeItem, Long>, JpaSpecificationExecutor<AccountChangeItem> {
    AccountChangeItem findByChangeSummaryIdAndColumnName(Long id, String key);
    List<AccountChangeItem> findByChangeSummaryId(Long id);
}
