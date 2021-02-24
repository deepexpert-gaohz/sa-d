package com.ideatech.ams.system.proof.dao;

import com.ideatech.ams.system.proof.entity.ProofReport;
import com.ideatech.ams.system.proof.enums.ProofType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProofReportDao extends JpaRepository<ProofReport, Long>, JpaSpecificationExecutor<ProofReport> {
    ProofReport findByAcctNoAndType(String acctNo, ProofType type);
}
