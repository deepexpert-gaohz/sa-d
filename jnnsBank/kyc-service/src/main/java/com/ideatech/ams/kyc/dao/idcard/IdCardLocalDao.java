package com.ideatech.ams.kyc.dao.idcard;


import com.ideatech.ams.kyc.entity.idcard.IdCardLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdCardLocalDao extends JpaRepository<IdCardLocal, Long>, JpaSpecificationExecutor<IdCardLocal> {

	IdCardLocal findByIdCardNoAndIdCardName(String idCardNo, String idCardName);
	
	//20180117新增 根据id查询
	IdCardLocal findById(Long id);

	List<IdCardLocal> findByOrganFullIdLike(String organFullId);
}
