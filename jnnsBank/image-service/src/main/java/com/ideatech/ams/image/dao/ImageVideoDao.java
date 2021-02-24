package com.ideatech.ams.image.dao;

import com.ideatech.ams.image.entity.ImageVideo;
import com.ideatech.common.enums.CompanyIfType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageVideoDao extends JpaRepository<ImageVideo,Long>, JpaSpecificationExecutor<ImageVideo> {
    List<ImageVideo> findByBillsIdAndSyncStatus(Long billsId,CompanyIfType syncStatus);

    List<ImageVideo> findBySyncStatus(CompanyIfType syncStatus);

    List<ImageVideo> findByBillsId(Long billsId);

    List<ImageVideo> findByApplyid(String applyid);

    List<ImageVideo> findByDepositorNameAndSyncStatus(String depositorName,CompanyIfType syncStatus);
}
