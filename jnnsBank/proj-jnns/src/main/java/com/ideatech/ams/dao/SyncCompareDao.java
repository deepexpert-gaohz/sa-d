package com.ideatech.ams.dao;

import com.ideatech.ams.domain.SyncCompare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 上报账户实时比对报表dao类
 *
 * @auther zoulang
 * @create 2018-11-29 9:40 AM
 **/

@Repository
public interface SyncCompareDao extends JpaRepository<SyncCompare, Long>, JpaSpecificationExecutor<SyncCompare> {

    SyncCompare findByAcctNo(String acctNo);

    SyncCompare findTopByAcctNoAndKaixhubzAndBusinessDateOrderByLastUpdateDateDesc(String acctNo, String kaixhubz, String BusinessDate);
//    SyncCompare findByAcctNoAndkaixhubz(String acctNo, String Kaixhubz);

    @Transactional
    @Modifying
    @Query(value = "delete from yd_Sync_Compare s where s.yd_acct_Open_Date=?1 and s.yd_kaixhubz!=03",nativeQuery = true)
    void deleteByAcctOpenDate(String acctOpenDate);



    @Transactional
    @Modifying
    @Query(value = "delete from yd_sync_compare s where s.yd_acct_no=?1",nativeQuery = true)
    void deleteByAcctNo(String acctNo);

}
