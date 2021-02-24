package com.ideatech.ams.system.org.dao;

import com.ideatech.ams.system.org.entity.OrganizationPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public interface OrganizationDao extends JpaRepository<OrganizationPo, Long>, JpaSpecificationExecutor<OrganizationPo> {
    List<OrganizationPo> findByParentIdAndNameLike(Long parentId, String name);

    List<OrganizationPo> findByFullIdLikeAndNameLikeOrderByLastUpdateDateAsc(String parentFullId, String name);

    List<OrganizationPo> findByFullIdLikeAndCodeOrderByLastUpdateDateAsc(String parentFullId, String code);

    List<OrganizationPo> findByFullIdLikeAndCodeLikeOrderByLastUpdateDateAsc(String parentFullId, String code);

    List<OrganizationPo> findByFullIdLikeAndNameLikeAndCodeOrderByLastUpdateDateAsc(String parentFullId, String name, String code);

    List<OrganizationPo> findByFullIdLikeAndNameLikeAndCodeLikeOrderByLastUpdateDateAsc(String parentFullId, String name, String code);

    List<OrganizationPo> findByParentIdOrderByLastUpdateDateAsc(Long parentId);

    OrganizationPo findByCode(String code);

    OrganizationPo findByFullId(String fullId);



    List<OrganizationPo> findByPbcCode(String pbcCode);

    List<OrganizationPo> findByPbcCodeLike(String pbcCode);

    List<OrganizationPo> findByPbcCodeOrderByFullIdAsc(String pbcCode);

    List<OrganizationPo> findByFullIdLike(String fullIdPrefix);

    @Query("select code from OrganizationPo")
    Set<String> findCodeAllInSet();


    @Query("SELECT Distinct(op.pbcCode) FROM OrganizationPo op WHERE op.pbcCode is not null")
    List<String> findDistinctPbcCode();

    OrganizationPo findByName(String name);

    List<OrganizationPo> findByNameLike(String name);

    List<OrganizationPo> findByNameLikeAndFullIdLike(String name, String fullIdPrefix);

    OrganizationPo findById(Long id);

    List<OrganizationPo> findByCodeLike(String code);

    List<OrganizationPo> findByCodeLikeAndNameLike(String code, String name);

    OrganizationPo findByInstitutionCodeAndIdNot(String institutionCode, Long id);

    OrganizationPo findByInstitutionCode(String institutionCode);

    OrganizationPo findByCodeAndIdNot(String code, Long id);



    @Query("select a.code from OrganizationPo  a where a.fullId = ?1")
    String findCodeByFullId(String organFullId);



    @Query("select a.fullId  from OrganizationPo  a where a.code = ?1")
    String  findFullIdByCode(String code);


    @Query("select a.code from OrganizationPo  a where a.fullId = ?1")
    @Transactional
    String findCodeByOrgFullId(String organFullId);


    /**
     * @Description 
     * @author yangwz
     * @date 2020/9/1 10:03
     */
    @Query(value = "select * from yd_sys_organization where yd_full_id like CONCAT(?1,'%')",nativeQuery = true)
    List<OrganizationPo> findAllByFullId(String fullId);





}
