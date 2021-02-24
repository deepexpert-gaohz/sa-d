package com.ideatech.ams.customer.dao;

import com.ideatech.ams.customer.dto.CustomerTuneSearchHistoryDto;
import com.ideatech.ams.customer.entity.CustomerTuneSearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CustomerTuneSearchHistoryDao extends JpaRepository<CustomerTuneSearchHistory, Long>, JpaSpecificationExecutor<CustomerTuneSearchHistory> {
//    Page<CustomerTuneSearchHistory> findByType(String type, Pageable pageable);
    List<CustomerTuneSearchHistory> findByOrganFullIdLike(String organFullId);

}
