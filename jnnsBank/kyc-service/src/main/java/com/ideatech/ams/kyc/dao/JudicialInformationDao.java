package com.ideatech.ams.kyc.dao;

import com.ideatech.ams.kyc.entity.JudicialInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JudicialInformationDao extends JpaRepository<JudicialInformation, Long>, JpaSpecificationExecutor<JudicialInformation> {

    /**
     * 根据案号进行查询
     * @param caseNo
     * @return
     */
    JudicialInformation findByCaseNo(String caseNo);
}
