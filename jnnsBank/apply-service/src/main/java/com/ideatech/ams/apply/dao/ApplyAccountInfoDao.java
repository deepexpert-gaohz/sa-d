package com.ideatech.ams.apply.dao;

import com.ideatech.ams.apply.entity.ApplyAccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApplyAccountInfoDao extends JpaRepository<ApplyAccountInfo, Long>, JpaSpecificationExecutor<ApplyAccountInfo> {
}
