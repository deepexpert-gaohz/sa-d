package com.ideatech.ams.kyc.dao.idcard;

import com.ideatech.ams.kyc.entity.idcard.IdCheckLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdCheckLogDao extends JpaRepository<IdCheckLog, Long>, JpaSpecificationExecutor<IdCheckLog> {

    List<IdCheckLog> findByOrganFullIdLike(String organFullId);
}
