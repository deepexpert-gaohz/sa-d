package com.ideatech.ams.kyc.dao.entrustupdate;

import com.ideatech.ams.kyc.entity.entrustupdate.EntrustUpdateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntrustUpdateHistoryDao extends JpaRepository<EntrustUpdateHistory, Long>, JpaSpecificationExecutor<EntrustUpdateHistory> {

    List<EntrustUpdateHistory> findByCompanyNameAndUpdateStatus(String companyName, Boolean updateStatus);

}
