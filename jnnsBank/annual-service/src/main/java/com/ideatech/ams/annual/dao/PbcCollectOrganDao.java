package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.PbcCollectOrgan;
import com.ideatech.ams.annual.enums.CollectState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PbcCollectOrganDao extends JpaRepository<PbcCollectOrgan, Long>, JpaSpecificationExecutor<PbcCollectOrgan> {

	List<PbcCollectOrgan> findByCollectBatch(String collectBatch);

	List<PbcCollectOrgan> findByCollectBatchAndCollectState(String collectBatch, CollectState collectState);
	
	List<PbcCollectOrgan> findByCollectBatchAndCollectStateNot(String collectBatch, CollectState collectState);
	
	@Query("select MAX(collectBatch) from PbcCollectOrgan")
	String getMaxCollectBatch();

	long countByCollectStateNotIn(CollectState... collectState);
}
