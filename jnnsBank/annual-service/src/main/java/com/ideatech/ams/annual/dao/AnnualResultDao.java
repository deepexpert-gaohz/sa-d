package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.AnnualResult;
import com.ideatech.ams.annual.enums.ForceStatusEnum;
import com.ideatech.ams.annual.enums.ResultStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 *
 * @author van
 * @date 15:59 2018/8/8
 */
@Repository
public interface AnnualResultDao extends JpaRepository<AnnualResult, Long>, JpaSpecificationExecutor<AnnualResult> {

	List<AnnualResult> findByTaskIdAndOrganFullIdAndResult(Long taskId, String organFullId,
			ResultStatusEnum resultStatusEnum);

	@Query("select organFullId from AnnualResult where taskId = ?1 group by organFullId")
	List<String> findOrganFullIdByTaskId(Long taskId);

	List<AnnualResult> findByTaskId(Long taskId);

	AnnualResult findByTaskIdAndAcctNo(Long taskId, String acctNo);

	List<AnnualResult> findByTaskIdAndDepositorName(Long taskId, String depositorName);

	long countByTaskId(Long taskId);

	@Modifying
	@Query("delete from AnnualResult where taskId = ?1")
	void deleteByTaskId(Long taskId);

	long countByTaskIdAndResultNot(Long taskId, ResultStatusEnum result);

	long countByTaskIdAndMatch(Long taskId, Boolean match);

	long countByTaskIdAndForceStatus(Long taskId, ForceStatusEnum forceStatus);

	@Modifying
	@Query("update AnnualResult set unilateral = null,match = null,abnormal = '',black=null,saicStatus=null,result= 'INIT',pbcSubmitStatus=null,pbcSubmitter = '',pbcSubmitDate='',pbcSubmitErrorMsg='',forceStatus='INIT',deleted=false,compareResult ='',dataProcessPerson = null,dataProcessStatus = 'WAIT_PROCESS' where taskId = ?1")
	void cleanResult(Long taskId);

	List<AnnualResult> findByAcctNoOrderByCreatedDateDesc(String acctNo);

	@Modifying
	@Query("UPDATE AnnualResult SET unilateral = NULL, match = NULL, abnormal = '', black = NULL, saicStatus = NULL, result = 'INIT', pbcSubmitStatus = NULL, pbcSubmitter = '', pbcSubmitDate = '', pbcSubmitErrorMsg = '', forceStatus = 'INIT', compareResult = '' WHERE taskId = ?1 AND ( pbcSubmitStatus IS NULL OR pbcSubmitStatus <> 'SUCCESS' ) AND ( result IS NULL OR result <> 'PASS' ) AND ( forceStatus IS NULL OR forceStatus <> 'SUCCESS' ) AND ( forceStatus IS NULL OR forceStatus <> 'AGAIN_SUCCESS' ) AND ( dataProcessStatus IS NULL OR dataProcessStatus <> 'SUCCESS')")
	void cleanUnSuccessResult(Long taskId);

}
