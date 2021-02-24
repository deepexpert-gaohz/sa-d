package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.BusinessAcceptTimeNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author jzh
 * @date 2019-09-11.
 */

@Repository
public interface BusinessAcceptTimeNoticeDao extends JpaRepository<BusinessAcceptTimeNotice,Long>, JpaSpecificationExecutor<BusinessAcceptTimeNotice> {
}
