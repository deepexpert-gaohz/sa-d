package com.ideatech.ams.account.dao.core;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ideatech.ams.account.entity.CorePublicAccount;
import com.ideatech.common.enums.CompanyIfType;

/**
 * @author wanghongjie
 *
 * @version 2018-06-21 19:47
 */
@Repository
public interface CorePublicAccountDao extends JpaRepository<CorePublicAccount, Long>, JpaSpecificationExecutor<CorePublicAccount> {

	@Query("from CorePublicAccount c where c.acctNo = ?1")
	CorePublicAccount findbyAcctNo(String acctNo);

	List<CorePublicAccount> findByHandleStatusOrderByCreatedDateDesc(CompanyIfType yesono, Pageable pageable);

	long countByHandleStatus(CompanyIfType yesono);

	long countByAcctNoAndIdNot(String acctNo, Long id);

	CorePublicAccount findFirstByAcctNoAndIdNotOrderByCreatedDateDesc(String acctNo, Long id);
	
	CorePublicAccount findFirstByAcctNoOrderByCreatedDateDesc(String acctNo);

	CorePublicAccount findById(Long id);

	List<CorePublicAccount> findByBankCode(String bankCode);

}
