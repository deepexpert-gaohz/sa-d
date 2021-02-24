package com.ideatech.ams.system.pbc.dao;

import com.ideatech.ams.system.pbc.entity.PbcAccountPo;
import com.ideatech.ams.system.pbc.enums.EAccountStatus;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PbcAccountDao extends JpaRepository<PbcAccountPo, Long>, JpaSpecificationExecutor<PbcAccountPo> {
    List<PbcAccountPo> findByOrOrgId(Long orgId);

    List<PbcAccountPo> findByOrgIdAndAccountType(Long orgId, EAccountType accountType);

    List<PbcAccountPo> findByAccountType(EAccountType type);

    PbcAccountPo findByOrgIdAndAccountTypeAndAccount(Long orgId, EAccountType accountType, String account);

    List<PbcAccountPo> findByAccountStatusAndEnabled(EAccountStatus status, Boolean enabled);

    List<PbcAccountPo> findByIpAndAccountTypeAndAccount(String ip,EAccountType accountType,String account);

//    @Query("select ip from PbcAccountPo where accountType = ?1 group by ip")
    List<PbcAccountPo> findByAccountTypeAndAccountStatusAndEnabledAndDeletedAndAccountStartsWith(EAccountType accountType,
                                              EAccountStatus accountStatus, Boolean enabled, Boolean deleted, String account);

    List<PbcAccountPo> findByIp(String ip);

}
