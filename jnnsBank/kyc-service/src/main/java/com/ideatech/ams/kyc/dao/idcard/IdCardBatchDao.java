package com.ideatech.ams.kyc.dao.idcard;

import com.ideatech.ams.kyc.entity.idcard.IdCardBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface IdCardBatchDao extends JpaRepository<IdCardBatch, Long> ,JpaSpecificationExecutor<IdCardBatch> {
    IdCardBatch findByIdCardNameAndIdCardNo(String idCardName,String idCardNo);

}
