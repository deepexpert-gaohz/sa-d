package com.ideatech.ams.system.configuration.dao;

import com.ideatech.ams.system.configuration.entity.AccountConfigurePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountConfigureDao extends JpaRepository<AccountConfigurePo, Long>, JpaSpecificationExecutor<AccountConfigurePo> {

    AccountConfigurePo findByAcctTypeAndDepositorTypeAndOperateType(String acctType,String depositorName,String operateType);

    AccountConfigurePo findByAcctTypeAndOperateType(String acctType,String operateType);
}
