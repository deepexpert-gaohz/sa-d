package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.entity.CompareResultSaicCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jzh
 * @date 2019/6/5.
 */

@Repository
public interface CompareResultSaicCheckDao extends JpaRepository<CompareResultSaicCheck,Long> , JpaSpecificationExecutor<CompareResultSaicCheck> {

    /**
     * 根据客户名称（存款人名称）获取最新的工商异动信息
     * @param depositorName
     * @return
     */
    CompareResultSaicCheck findTopByDepositorNameOrderByCreatedDateDesc(String depositorName);

    /**
     * 根据任务id获取当前的所有的客户异动信息
     * @param compareTaskId
     * @return
     */
    List<CompareResultSaicCheck> findAllByCompareTaskId(Long compareTaskId);

    /**
     * 异动处理
     * @param processState 处理后的处理状态
     * @param processer    处理人
     * @param processTime  处理时间
     * @param ids          要处理的id集合
     */
    @Modifying
    @Query("update CompareResultSaicCheck a set a.processState = ?1, a.processer = ?2, a.processTime = ?3 where a.id in ?4")
    void process(String processState, String processer, String processTime, Long[] ids);

    /**
     * 修改发送记录
     */
    @Modifying
    @Query("update CompareResultSaicCheck a set a.message = ?1 where a.id = ?2")
    void chageMessage(String message, Long id);
}
