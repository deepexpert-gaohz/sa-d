package com.ideatech.ams.dao;

import com.ideatech.ams.domain.JnnsImageAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2019/4/24.
 */
public interface JnnsImageAllDao extends JpaRepository<JnnsImageAll, Long>, JpaSpecificationExecutor<JnnsImageAll> {

    List<JnnsImageAll>  findByBillId(String billId);

    @Transactional
    @Modifying
    @Query(value = "delete from yd_jnns_image_all s where s.yd_bill_id=?1",nativeQuery = true)
    void deleteAllByBillId(String acctBillsId);


}
