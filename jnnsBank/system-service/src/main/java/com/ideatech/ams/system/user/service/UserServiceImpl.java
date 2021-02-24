package com.ideatech.ams.system.user.service;

import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.system.role.dao.RoleDao;
import com.ideatech.ams.system.role.dto.RoleDto;
import com.ideatech.ams.system.role.entity.RolePo;
import com.ideatech.ams.system.user.dao.UserDao;
import com.ideatech.ams.system.user.domain.UserDo;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.dto.UserSearchDto;
import com.ideatech.ams.system.user.entity.UserPo;
import com.ideatech.ams.system.user.poi.SysUserExport;
import com.ideatech.ams.system.user.poi.SysUserPoi;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author liangding
 * @create 2018-05-04 上午10:38
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private OrganizationDao organizationDao;

    @PersistenceContext
    private EntityManager em; //注入EntityManager

    @Override
    public UserDto findByUsername(String username) {
        UserPo userPo = userDao.findByUsername(username);
        return ConverterService.convert(userPo, UserDto.class);
    }

    @Override
    public String findPasswordByUserId(Long userId) {
        UserPo one = userDao.findOne(userId);
        if (one != null) {
            return one.getPassword();
        }
        return null;
    }

    @Override
    public void changePassword(Long userId, String password) {
        UserPo one = userDao.findOne(userId);
        if (one != null) {
            one.setPassword(password);
            if(!"admin".equals(one.getUsername()) && !"virtual".equals(one.getUsername())) {
                one.setPwdUpdateDate(new Date());
            }

        }
        userDao.save(one);
    }

    @Override
    public UserDto save(UserDto userDto) {
        UserPo userPo = new UserPo();
        String oldPassword = null;
        Date oldPwdUpdateDate = null;

        if (null != userDto.getId()) {
            userPo = userDao.findOne(userDto.getId());
            if(null == userPo){
                userPo = new UserPo();
            }
            oldPassword = userPo.getPassword();
            oldPwdUpdateDate = userPo.getPwdUpdateDate();
        }
        ConverterService.convert(userDto, userPo);
        if (StringUtils.isBlank(userPo.getPassword())) {  //修改用户操作
            userPo.setPassword(oldPassword);
            userPo.setPwdUpdateDate(oldPwdUpdateDate);
        } else {  //新增用户操作
            if(!"virtual".equals(userPo.getUsername())) {
                userPo.setPwdUpdateDate(new Date());
            }
        }
        userDao.save(userPo);
        ConverterService.convert(userPo, userDto);
        return userDto;
    }

    @Override
    public UserDto findById(Long id) {
        UserPo userPo = userDao.findOne(id);
        UserDto userDto = new UserDto();
        BeanCopierUtils.copyProperties(userPo, userDto);
        userDto.setPassword(null);
        return userDto;
    }

    @Override
    public UserDto findAllById(Long id) {
        UserPo userPo = userDao.findAllById(id);
        UserDto userDto = new UserDto();
        BeanCopierUtils.copyProperties(userPo, userDto);
        userDto.setPassword(null);
        return userDto;
    }

    @Override
    public UserSearchDto search(final UserSearchDto userSearchDto) {
        userSearchDto.setOrgFullId(SecurityUtils.getCurrentOrgFullId());
        Pageable pageable = new PageRequest(Math.max(userSearchDto.getOffset() - 1, 0), userSearchDto.getLimit());
        String sql = "select new com.ideatech.ams.system.user.domain.UserDo(t1,t2) from UserPo t1, OrganizationPo t2 where t1.orgId=t2.id ";
        String countSql = "select count(*) from UserPo t1, OrganizationPo t2 where t1.orgId=t2.id ";
        if (StringUtils.isNotBlank(userSearchDto.getName())) {
            sql += " and t1.cname like ?1 ";
            countSql += " and t1.cname like ?1 ";
        }
        if (userSearchDto.getEnabled() != null) {
            sql += " and t1.enabled = ?2 ";
            countSql += " and t1.enabled = ?2 ";
        }
        if (userSearchDto.getOrgId() != null) {
            sql += " and t1.orgId = ?3 ";
            countSql += " and t1.orgId = ?3 ";
        }
        if (userSearchDto.getRoleId() != null) {
            sql += " and t1.roleId = ?4 ";
            countSql += " and t1.roleId = ?4 ";
        }
        if (userSearchDto.getOrgFullId() != null) {
            sql += " and t2.fullId like ?5 ";
            countSql += " and t2.fullId like ?5 ";
        }
        if (StringUtils.isNotBlank(userSearchDto.getOrgName())) {
            sql += " and t2.name like ?6 ";
            countSql += " and t2.name like ?6 ";
        }
        if(StringUtils.isNotBlank(userSearchDto.getUsername())){
            sql += "and t1.username like ?7";
            countSql += "and t1.username like ?7";
        }

        Query query = em.createQuery(sql);
        Query queryCount = em.createQuery(countSql);

        if (StringUtils.isNotBlank(userSearchDto.getName())) {
            query.setParameter(1, "%" + userSearchDto.getName() + "%");
            queryCount.setParameter(1, "%" + userSearchDto.getName() + "%");
        }
        if (userSearchDto.getEnabled() != null) {
            query.setParameter(2, userSearchDto.getEnabled());
            queryCount.setParameter(2, userSearchDto.getEnabled());
        }
        if (userSearchDto.getOrgId() != null) {
            query.setParameter(3, userSearchDto.getOrgId());
            queryCount.setParameter(3, userSearchDto.getOrgId());
        }
        if (userSearchDto.getRoleId() != null) {
            query.setParameter(4, userSearchDto.getRoleId());
            queryCount.setParameter(4, userSearchDto.getRoleId());
        }
        if (StringUtils.isNotBlank(userSearchDto.getOrgFullId())) {
            query.setParameter(5, userSearchDto.getOrgFullId() + "%");
            queryCount.setParameter(5, userSearchDto.getOrgFullId() + "%");
        }
        if (StringUtils.isNotBlank(userSearchDto.getOrgName())) {
            query.setParameter(6, "%" + userSearchDto.getOrgName() + "%");
            queryCount.setParameter(6, "%" + userSearchDto.getOrgName() + "%");
        }
        if (StringUtils.isNotBlank(userSearchDto.getUsername())){
            query.setParameter(7,"%" + userSearchDto.getUsername() + "%");
            queryCount.setParameter(7,"%" + userSearchDto.getUsername() + "%");
        }

        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        //查询
        List<UserDo> list = query.getResultList();
        Long count = (Long) queryCount.getSingleResult();

        List<UserDto> userDtoList = new ArrayList<>();

        for(UserDo ud :list){
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(ud.getUserPo(), userDto);
            userDto.setOrgName(ud.getOrganizationPo().getName());
            userDtoList.add(userDto);
        }

        userSearchDto.setList(userDtoList);
        userSearchDto.setTotalRecord(count);
        userSearchDto.setTotalPages((int) Math.ceil(count.intValue()/userSearchDto.getLimit()));
        return userSearchDto;
    }

    @Override
    public void deleteById(Long id) {
        userDao.delete(id);
    }

    @Override
    public void unbind(Long userId, Long orgId) {
        UserPo one = userDao.findOne(userId);
        if (!orgId.equals(one.getOrgId())) {
            throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, "该用户不属于此机构");
        }
        one.setOrgId(null);
        userDao.save(one);
    }

    @Override
    public List<UserDto> findByRoleId(Long id) {
        List<UserPo> byRoleId = userDao.findByRoleId(id);
        return ConverterService.convertToList(byRoleId, UserDto.class);
    }

    @Override
    public void save(List<UserDto> userDtos) {
        List<UserPo> userPos = ConverterService.convertToList(userDtos, UserPo.class);
        userDao.save(userPos);
    }

    @Override
    public List<UserDto> findByOrgId(Long id) {
        List<UserPo> byOrgId = userDao.findByOrgId(id);
        return ConverterService.convertToList(byOrgId, UserDto.class);
    }

    @Override
    public void updateOrg(Long id, List<Long> userIds) {
        for (Long userId : userIds) {
            UserPo userPo = userDao.findOne(userId);
            userPo.setOrgId(id);
            userDao.save(userPo);
        }
    }

    @Override
    public UserDto findVirtualUser() {
        return findByUsername("virtual");
    }

    @Override
    public void grant(Long userId, Long roleId) {
        UserPo userPo = userDao.findOne(userId);
        if (userPo != null) {
            userPo.setRoleId(roleId);
            userDao.save(userPo);
        }
    }

    @Override
    public void disable(Long userId) {
        UserPo one = userDao.findOne(userId);
        one.setEnabled(Boolean.FALSE);
        if(!"virtual".equals(one.getUsername()) && !"admin".equals(one.getUsername())) {
            one.setPwdUpdateDate(null);
        }

        userDao.save(one);
    }

    @Override
    public void enable(Long userId) {
        UserPo one = userDao.findOne(userId);
        one.setEnabled(Boolean.TRUE);
        if(!"virtual".equals(one.getUsername()) && !"admin".equals(one.getUsername())) {
            one.setPwdUpdateDate(new Date());
        }

        userDao.save(one);
    }

    @Override
    public UserSearchDto findByUsernameList(final List<String> nameList,final UserSearchDto userSearchDto) {
        Pageable pageable = new PageRequest(Math.max(userSearchDto.getOffset() - 1, 0), userSearchDto.getLimit());
        Specification<UserPo> specification = new Specification<UserPo>() {
            @Override
            public Predicate toPredicate(Root<UserPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if(nameList.size()>0){
                    List<Predicate> list = new ArrayList<>();
                    CriteriaBuilder.In<String> usernameIn = cb.in(root.<String>get("username"));
                    for (String username : nameList) {
                        usernameIn.value(username);
                    }
                    list.add(usernameIn);

                    Predicate[] p = new Predicate[list.size()];
                    Predicate and = cb.and(list.toArray(p));
                    expressions.add(and);
                }

                if (null != userSearchDto) {
                    if (StringUtils.isNotBlank(userSearchDto.getName())) {
                        Predicate cnamePredicate = cb.like(root.<String>get("cname"), "%" + userSearchDto.getName() + "%");
                        expressions.add(cnamePredicate);
                    }

                    if (StringUtils.isNotBlank(userSearchDto.getUsername())) {
                        Predicate usernamePredicate = cb.like(root.<String>get("username"), "%" + userSearchDto.getUsername() + "%");
                        expressions.add(usernamePredicate);

                    }
                }

                return predicate;
            }
        };

        Page<UserPo> all = userDao.findAll(specification, new PageRequest(Math.max(userSearchDto.getOffset() - 1, 0), userSearchDto.getLimit()));
        List<UserDto> userDtos = ConverterService.convertToList(all.getContent(), UserDto.class);
        userSearchDto.setList(userDtos);
        userSearchDto.setTotalRecord(all.getTotalElements());
        userSearchDto.setTotalPages(all.getTotalPages());
        return userSearchDto;
    }

    @Override
    public List<Long> findUserIdByLikeUsername(String username) {
        if (StringUtils.isNotBlank(username)) {
            List<UserPo> userList = userDao.findUserIdByUsernameContaining("%" + username + "%");
            List<Long> idList = new ArrayList<>();
            for (UserPo user : userList) {
                idList.add(user.getId());
            }
            if(CollectionUtils.isEmpty(idList)){
                userList = userDao.findByCnameLike("%" + username + "%");
                for (UserPo user : userList) {
                    idList.add(user.getId());
                }
            }
            return idList;
        } else {
            return null;
        }
    }

    @Override
    public List<Long> findUserIdByLikeCName(String cname) {
        if (StringUtils.isNotBlank(cname)) {
            List<UserPo> userList = userDao.findByCnameLike("%" + cname + "%");
            List<Long> idList = new ArrayList<>();
            for (UserPo user : userList) {
                idList.add(user.getId());
            }
            return idList;
        } else {
            return null;
        }
    }

    @Override
    public IExcelExport exportUserByOrgId(Long orgId) {

        //1、根据id获取组织机构对象
        OrganizationPo organizationPo = organizationDao.findById(orgId);
        //2.1、根据fullid获取组织机构及其子机构
        List<OrganizationPo> organizationPoList = organizationDao.findByFullIdLike("%"+organizationPo.getFullId()+"%");
        //2.2、获取所有角色
        List<RolePo> rolePoList = roleDao.findAll();
        //3.1、获取所有用户
        List<UserPo> userPoList = userDao.findAll();
        //3.2、声明对象
        IExcelExport sysUserExport = new SysUserExport();
        List<SysUserPoi> sysUserPoiList = new ArrayList<SysUserPoi>();

        //3.3、遍历2中获取的组织机构和3.1中的用户，进行比较。
        for (OrganizationPo organization : organizationPoList) {
//            log.info("********organization:"+organization.getId()+":"+organization.toString());
            for(Iterator<UserPo> it = userPoList.iterator(); it.hasNext();){
                //该用户在该机构内
                UserPo userPo = it.next();
                if (null!=userPo.getOrgId() && userPo.getOrgId().equals(organization.getId())){
                    log.info(userPo.toString());
                    SysUserPoi sysUserPoi = new SysUserPoi();
                    BeanUtils.copyProperties(userPo,sysUserPoi);
                    //设置角色
                    for (RolePo rolePo : rolePoList){
                        if (rolePo.getId().equals(userPo.getRoleId())){
                            sysUserPoi.setRoleName(rolePo.getName());
                        }
                    }
                    //设置所属机构
                    if (organization.getName()!=null){
                        sysUserPoi.setOrgName(organization.getName());
                    }
                    sysUserPoiList.add(sysUserPoi);
                    it.remove();
                }
            }
        }
        //3.4、设置POIList
        sysUserExport.setPoiList(sysUserPoiList);
        return sysUserExport;
    }

    @Override
    public void pwdUpdateDate() {
        List<UserPo> list = userDao.findByEnabledAndUsernameNotIn(true, "admin", "virtual");

        if(CollectionUtils.isNotEmpty(list)) {
            for(UserPo user : list) {
                if(user.getPwdUpdateDate() == null) {
                    user.setPwdUpdateDate(new Date());
                    userDao.save(user);
                }
            }
        }
    }

}
