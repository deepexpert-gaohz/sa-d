/**
 *
 */
package com.ideatech.ams.compare.dao.data;

import com.ideatech.ams.compare.entity.data.CompareData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author zhailiang
 *
 */
@NoRepositoryBean
public interface CompareDataRepository<T extends CompareData> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    List<T> findByTaskId(Long id);

    List<T> findByOrganFullId(String organFullId);

    Page<T> findByTaskId(Long id, Pageable pageable);

    T findByTaskIdAndAcctNo(Long taskId, String acctNo);

    @Query("SELECT t.acctNo FROM #{#entityName} t WHERE t.taskId = ?1")
    List<String> getAcctNoByTaskId(Long taskId);

    @Query("SELECT t.acctNo,t.depositorName FROM #{#entityName} t WHERE t.taskId = ?1")
    List<Object[]> getAcctNoAndDepositorNameByTaskId(Long taskId);

    List<T> countByTaskId(Long id);

    @Transactional
    List<T> deleteByTaskId(Long id);

    List<T> countByTaskIdAndAcctNo(Long taskId, String acctNo);

    List<T> findByTaskIdAndDepositorName(Long taskId, String depositorName);

    @Query("delete from #{#entityName} t where t.organFullId=?1")
    @Modifying
    @Transactional
    void deleteByOrganFullId(String organFullId);


}
