package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.entity.ComparePbcInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Description 人行账管系统账户DAO层
 * @Author wanghongjie
 * @Date 2019/2/15
 **/
@Repository
public interface ComparePbcInfoDao extends JpaRepository<ComparePbcInfo,Long>, JpaSpecificationExecutor<ComparePbcInfo> {
    ComparePbcInfo findById(Long id);
    ComparePbcInfo findFirstByAcctNoOrderByCreatedDateDesc(String acctNo);
}
