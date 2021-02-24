package com.ideatech.ams.apply.dao;

import com.ideatech.ams.apply.entity.ApplyOcr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApplyOcrDao extends JpaRepository<ApplyOcr, Long>, JpaSpecificationExecutor<ApplyOcr> {
    List<ApplyOcr> findByApplyidAndDoccode(String applyId, String docCode);

    List<ApplyOcr> findByApplyid(String applyId);

    List<ApplyOcr> findByApplyidOrderByCurNumAsc(String applyId);

    ApplyOcr findByFilename(String filename);

    ApplyOcr findByFilenameAndApplyid(String filename,String applyid);

    ApplyOcr findByApplyidAndCurNum(String applyid, Integer curNum);
}
