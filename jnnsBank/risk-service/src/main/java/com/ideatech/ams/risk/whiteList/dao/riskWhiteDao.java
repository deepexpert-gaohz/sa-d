package com.ideatech.ams.risk.whiteList.dao;


import com.ideatech.ams.risk.whiteList.entity.WhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author: yinjie
 * @Date: 2019/5/27 9:58
 * @description
 */
public interface riskWhiteDao extends JpaRepository<WhiteList,Long>, JpaSpecificationExecutor<WhiteList> {

    //accountId重复验证
    WhiteList findByAccountId(String accountId);
}
