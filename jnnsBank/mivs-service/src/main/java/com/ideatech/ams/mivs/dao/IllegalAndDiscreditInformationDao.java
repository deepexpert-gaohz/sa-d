package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.IllegalAndDiscreditInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Repository
public interface IllegalAndDiscreditInformationDao extends JpaRepository<IllegalAndDiscreditInformation,Long> {

    List<IllegalAndDiscreditInformation> findAllByRegisterInformationLogId(Long registerInformationLogId);
}
