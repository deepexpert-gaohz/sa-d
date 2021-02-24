package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.ServiceInformation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jzh
 * @date 2019/7/31.
 */
public interface ServiceInformationDao extends JpaRepository<ServiceInformation,Long> {
    ServiceInformation findTopByBusinessAcceptTimeLogId(Long businessAcceptTimeLogId);
}
