package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.CompanyShareholdersAndFundingInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Repository
public interface CompanyShareholdersAndFundingInformationDao extends JpaRepository<CompanyShareholdersAndFundingInformation,Long> {

    List<CompanyShareholdersAndFundingInformation> findAllByRegisterInformationLogId(Long registerInformationLogId);
}
