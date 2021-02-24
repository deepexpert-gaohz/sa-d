package com.ideatech.ams.system.org.service;

import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.dto.OrganizationSearchDto;
import com.ideatech.common.dto.ResultDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OrganizationService {
    OrganizationDto save(OrganizationDto organizationPo);

    OrganizationDto findById(Long id);

    OrganizationSearchDto search(OrganizationSearchDto organizationSearchDto);

    List<OrganizationDto> searchChild(Long parentId, String name);

    List<OrganizationDto> searchChildByNameAndCode(Long parentId, String name, String code);

    List<OrganizationDto> listDescendant(Long orgId);

    void delete(Long id);

    void save(List<OrganizationDto> organizationDtos);

    OrganizationDto findByCode(String Code);

    OrganizationDto findByName(String name);

    List<OrganizationDto> findByNameLike(String name);

    List<OrganizationDto> findByCodeLikeAndNameLike(String code, String name);

    OrganizationDto findByOrganFullId(String organFullId);

    List<OrganizationDto> findByOrganFullIdStartsWidth(String organFullId);

    List<OrganizationDto> findByNameLikeAndOrganFullIdStartsWidth(String name, String organFullId);

    List<OrganizationDto> findByPbcCodeLike(String pbcCode);

    OrganizationDto findTopPbcCode(String pbcCode);

    List<OrganizationDto> listAll();

    List<OrganizationDto> getAllLeafByCache();
    
    /**
    *@Description: 获取code的集合
    *@Param: []
    *@return: java.util.Set<java.lang.String>
    *@Author: wanghongjie
    *@date: 2018/9/16
    */
    Set<String> findCodeAllInSet();

    Map<String,OrganizationDto> findAllInMap();

    List<OrganizationDto> findByOrganFullIdLike(String organfullid);

    ResultDto cancelHeZhun(String[] ids);

    ResultDto cancelHeZhunDel(String[] ids);

    OrganizationDto findByCodeAndIdNot(String code, Long id);

    OrganizationDto findByInstitutionCodeAndIdNot(String institutionCode, Long id);

    OrganizationDto findByInstitutionCode(String institutionCode);

    OrganizationDto findByParentIdLike(String parentId);

    /**
     * @Description 获得当前机构下的所有机构
     * @author yangwz
     * @date 2020/9/1 10:01
     */
    List<OrganizationDto> findAllByFullId(String fullId);

}
