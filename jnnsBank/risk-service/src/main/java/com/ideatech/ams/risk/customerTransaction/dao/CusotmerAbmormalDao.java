package com.ideatech.ams.risk.customerTransaction.dao;

import com.ideatech.ams.risk.customerTransaction.entity.CustomerAbnormal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CusotmerAbmormalDao extends JpaRepository<CustomerAbnormal,Long>, JpaSpecificationExecutor<CustomerAbnormal> {
    @Modifying
    @Query("update CustomerAbnormal a set a.message = ?1 where a.id = ?2")
    void updateMessageById(String message, Long id);
    void deleteByDepositorNameIn(List<String> name);
    void deleteByOrganFullId(String orgFullId);
    List<CustomerAbnormal> findByOrganFullIdLike(String orgFullId);
    List<CustomerAbnormal> findByOrganFullIdLikeAndIllegal(String orgFullId, Boolean isIllegal);
    List<CustomerAbnormal> findByOrganFullIdLikeAndChangeMess(String orgFullId, Boolean isIllegal);
    List<CustomerAbnormal> findByOrganFullIdLikeAndBusinessExpires(String orgFullId, Boolean isIllegal);
    List<CustomerAbnormal> findByOrganFullIdLikeAndAbnormalState(String orgFullId, Boolean isIllegal);
    List<CustomerAbnormal> findByOrganFullIdLikeAndChanged(String orgFullId, Boolean isIllegal);
}
