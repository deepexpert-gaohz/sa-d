package com.ideatech.ams.account.dao;

import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.view.OpenAccountStatisticsView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author vantoo
 * @date 15:53 2018/5/28
 */
@Repository
public interface OpenAccountStatisticsDao extends JpaRepository<OpenAccountStatisticsView, Long>, JpaSpecificationExecutor<OpenAccountStatisticsView> {
}
