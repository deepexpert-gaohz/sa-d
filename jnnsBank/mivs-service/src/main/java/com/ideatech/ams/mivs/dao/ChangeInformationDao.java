package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.ChangeInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Repository
public interface ChangeInformationDao extends JpaRepository<ChangeInformation,Long> {

    List<ChangeInformation> findAllByRegisterInformationLogId(Long registerInformationLogId);
}
