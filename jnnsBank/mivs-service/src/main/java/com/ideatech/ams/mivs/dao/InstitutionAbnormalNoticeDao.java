package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.InstitutionAbnormalNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author jzh
 * @date 2019/7/24.
 */

@Repository
public interface InstitutionAbnormalNoticeDao extends JpaRepository<InstitutionAbnormalNotice,Long>, JpaSpecificationExecutor<InstitutionAbnormalNotice> {

}
