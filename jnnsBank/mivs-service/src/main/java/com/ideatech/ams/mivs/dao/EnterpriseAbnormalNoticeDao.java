package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.EnterpriseAbnormalNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author jzh
 * @date 2019/7/25.
 */

@Repository
public interface EnterpriseAbnormalNoticeDao extends JpaRepository<EnterpriseAbnormalNotice,Long> , JpaSpecificationExecutor<EnterpriseAbnormalNotice> {

}
