package com.ideatech.ams.account.dao.core;

import com.ideatech.ams.account.entity.CorePublicAccountFinish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
*@Description: 完成后的核心数据
*@Author: wanghongjie
*@date: 2018/9/18
*/
@Repository
public interface CorePublicAccountFinishDao extends JpaRepository<CorePublicAccountFinish, Long>, JpaSpecificationExecutor<CorePublicAccountFinish> {

    long countByAcctNo(String acctNo);

    CorePublicAccountFinish findByAcctNo(String acctNo);

}
