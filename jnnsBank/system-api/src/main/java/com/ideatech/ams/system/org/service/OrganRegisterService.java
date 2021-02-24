package com.ideatech.ams.system.org.service;

import com.ideatech.ams.system.org.dto.OrganRegisterDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

public interface OrganRegisterService extends BaseService<OrganRegisterDto> {

    /**
     * 根据12位行号进行查询
     * @param pbcCode
     * @return
     */
    OrganRegisterDto query(String pbcCode);

    /**
     * 根基短机构号查找出机构ID进行查询
     * @param organCode
     * @return
     */
    OrganRegisterDto queryByOrganCode(String organCode);

    /**
     * 根据机构ID查询
     * @param orgId
     * @return
     */
    OrganRegisterDto findByOrganId(Long orgId);

    OrganRegisterDto findByIdAndOrgFullIdLike(Long id,String fullId);

    OrganRegisterDto findByOrganFullId(String fullId);

    void del(Long id);

    Boolean getOrganRegisterIsNull(String organFullId);

    Boolean getOrganRegisterFlagByBankCode(String bankCode);

    TableResultResponse<OrganRegisterDto> queryList(OrganRegisterDto organRegisterDto,Pageable pageable);

    void delete(Long orgId);
}
