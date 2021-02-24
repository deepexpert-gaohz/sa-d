package com.ideatech.ams.system.user.dao;

import com.ideatech.ams.system.user.entity.UserPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends JpaRepository<UserPo, Long>, JpaSpecificationExecutor<UserPo> {
    UserPo findByUsername(String username);

    List<UserPo> findByOrgId(Long orgId);

    List<UserPo> findByRoleId(Long roleId);

    UserPo findByIdAndPassword(Long userId, String password);

    @Query("from UserPo user where user.cname like ?1 and user.orgId in (select id from OrganizationPo where fullId like ?2)")
    Page<UserPo> searchWithNameAndOrgRange(String name, String orgFullId, Pageable pageable);

    @Query("from UserPo user where user.orgId in (select id from OrganizationPo where fullId like ?1)")
    Page<UserPo> searchWithOrgRange(String orgFullId, Pageable pageable);

    Page<UserPo> findByCnameLikeAndOrgId(String cname, Long orgId, Pageable pageable);

    Page<UserPo> findByOrgId(Long orgId, Pageable pageable);

    Page<UserPo> findByCnameLike(String cname, Pageable pageable);

    List<UserPo> findUserIdByUsernameContaining(String username);

    List<UserPo> findByCnameLike(String cname);

    @Query(nativeQuery = true, value = "select * from yd_sys_user where yd_id = ?1")
    UserPo findAllById(Long id);

    List<UserPo> findByEnabledAndUsernameNotIn(Boolean enabled, String... userName);

}
