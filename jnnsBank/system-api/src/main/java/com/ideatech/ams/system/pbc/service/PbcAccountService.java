package com.ideatech.ams.system.pbc.service;

import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountStatus;
import com.ideatech.ams.system.pbc.enums.EAccountType;

import java.util.List;

public interface PbcAccountService {
    void save(PbcAccountDto pbcAccountDto);

    void uploadSave(PbcAccountDto pbcAccountDto);

    List<PbcAccountDto> listByOrgId(Long orgId);

    void delete(Long id);

    List<PbcAccountDto> listByOrgIdAndType(Long orgId, EAccountType accountType);

    List<PbcAccountDto> listByOrgCodeAndType(String orgCode, EAccountType accountType);

    /**
     * 后期加上多个账户，调用提高效率
     *
     * @param organId
     * @return
     */
    PbcAccountDto getAnnualPbcAccountByOrganId(Long organId, EAccountType accountType);

    PbcAccountDto getPbcAccountByOrganId(Long organId, EAccountType accountType);

    PbcAccountDto getPbcAccountByOrganCode(String organCode, EAccountType accountType);

    PbcAccountDto getPbcAccountByOrganFullId(String organFullId, EAccountType accountType);

    PbcAccountDto getPbcAccountByOrganFullIdByCancelHeZhun(String organFullId, EAccountType accountType);

    PbcAccountDto getPbcAccountByOrganCodeByCancelHeZhun(String organCode, EAccountType accountType);

    PbcAccountDto getById(Long id);

    void enable(Long id);

    void disable(Long id);

    List<PbcAccountDto> listByType(EAccountType type);

    List<PbcAccountDto> listAllNewAccount();

    /**
     * 获取查询相邻机构可用的人行账号信息
     * @param organCode
     * @param accountType
     * @return
     */
    PbcAccountDto getAdjacentByCode(String organCode, EAccountType accountType);

    List<PbcAccountDto> listByIpAndAccountType(EAccountType accountType);

    List<PbcAccountDto> listByIp(String ip);
}
