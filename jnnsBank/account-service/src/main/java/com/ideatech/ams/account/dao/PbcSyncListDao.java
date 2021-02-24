package com.ideatech.ams.account.dao;

import com.ideatech.ams.account.entity.PbcSyncList;
import com.ideatech.common.enums.CompanyIfType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author vantoo
 * @date 17:56 2018/5/20
 */
@Repository
public interface PbcSyncListDao extends JpaRepository<PbcSyncList, Long>, JpaSpecificationExecutor<PbcSyncList> {

    List<PbcSyncList> findBySyncStatus(CompanyIfType companyIfType);

	List<PbcSyncList> findByIsPush(CompanyIfType companyIfType);

    List<PbcSyncList> findBySyncStatusAndIsPush(CompanyIfType sync, CompanyIfType push);

    List<PbcSyncList> findByOrganFullIdLike(String organFullId);

    List<PbcSyncList> findByCancelHeZhunAndIsPush(Boolean cancelHeZhun,CompanyIfType push);

}
