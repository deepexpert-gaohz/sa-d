package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.LicenseNullify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Repository
public interface LicenseNullifyDao extends JpaRepository<LicenseNullify,Long> {

    List<LicenseNullify> findAllByRegisterInformationLogId(Long registerInformationLogId);
}
