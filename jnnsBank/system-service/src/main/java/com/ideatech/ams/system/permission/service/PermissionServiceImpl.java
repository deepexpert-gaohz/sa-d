package com.ideatech.ams.system.permission.service;

import com.ideatech.ams.system.permission.dao.PermissionDao;
import com.ideatech.ams.system.permission.dao.RolePermissionDao;
import com.ideatech.ams.system.permission.dto.PermissionDto;
import com.ideatech.ams.system.permission.dto.PermissionSearchDto;
import com.ideatech.ams.system.permission.entity.PermissionPo;
import com.ideatech.ams.system.permission.entity.RolePermissionPo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

/**
 * @author liangding
 * @create 2018-05-06 下午7:48
 **/
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public List<PermissionDto> findMenusByRoleId(Long roleId) {
        List<RolePermissionPo> byRoleId = rolePermissionDao.findByRoleId(roleId);
        Collection<Long> collect = CollectionUtils.collect(byRoleId, new Transformer() {
            @Override
            public Object transform(Object input) {
                RolePermissionPo po = (RolePermissionPo) input;
                return po.getPermissionId();
            }
        });
        collect = deleteRep(collect);
        List<PermissionPo> all = permissionDao.findALlByIdInAndPermissionTypeOrderByOrderNumAscTitleAsc(collect, "menu");
        return ConverterService.convertToList(all, PermissionDto.class);
    }

    @Override
    public List<PermissionDto> findMenusByRoleIds(Collection<Long> roleId) {
        List<RolePermissionPo> byRoleId = rolePermissionDao.findByRoleIdIn(roleId);
        Collection<Long> collect = CollectionUtils.collect(byRoleId, new Transformer() {
            @Override
            public Object transform(Object input) {
                RolePermissionPo po = (RolePermissionPo) input;
                return po.getPermissionId();
            }
        });
        collect = deleteRep(collect);
        List<PermissionPo> all = permissionDao.findALlByIdInAndPermissionTypeOrderByOrderNumAscTitleAsc(collect, "menu");

        if(CollectionUtils.isNotEmpty(all)) {
            for(Iterator it = all.iterator(); it.hasNext();) {
                PermissionPo permission = (PermissionPo)it.next();
                if("indexHtml".equals(permission.getCode())) {
                    it.remove();
                }
            }
        }

        return ConverterService.convertToList(all, PermissionDto.class);
    }

    @Override
    public List<PermissionDto> findMenusByTitle(String title) {
        PermissionPo permissionPo = new PermissionPo();
        permissionPo.setTitle(title);
        permissionPo.setPermissionType("menu");
        Example<PermissionPo> example = Example.of(permissionPo,
                ExampleMatcher.matching()
                        .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains())
        );
        Sort sort = new Sort(Sort.Direction.ASC, "orderNum");
        List<PermissionPo> all = permissionDao.findAll(example,sort);
        return ConverterService.convertToList(all, PermissionDto.class);
    }

    @Override
    public List<PermissionDto> findAllMenus() {
        PermissionPo permissionPo = new PermissionPo();
        permissionPo.setPermissionType("menu");
        Example<PermissionPo> example = Example.of(permissionPo);
        List<PermissionPo> all = permissionDao.findAll(example);
        return ConverterService.convertToList(all, PermissionDto.class);
    }

    @Override
    public PermissionSearchDto searchElement(PermissionSearchDto permissionSearchDto) {
        PermissionPo permissionPo = new PermissionPo();
        permissionPo.setParentId(permissionSearchDto.getParentId());
        permissionPo.setPermissionType("button");
        Example<PermissionPo> example = Example.of(permissionPo);
        Page<PermissionPo> all = permissionDao.findAll(example, new PageRequest(Math.max(permissionSearchDto.getOffset() - 1, 0), permissionSearchDto.getLimit()));
        permissionSearchDto.setList(ConverterService.convertToList(all.getContent(), PermissionDto.class));
        permissionSearchDto.setTotalPages(all.getTotalPages());
        permissionSearchDto.setTotalRecord(all.getTotalElements());
        return permissionSearchDto;
    }

    @Override
    public PermissionSearchDto searchElementByRoleId(PermissionSearchDto permissionSearchDto,Long roleId) {
        List<RolePermissionPo> byRoleId = rolePermissionDao.findByRoleId(roleId);
        Collection<Long> collect = CollectionUtils.collect(byRoleId, new Transformer() {
            @Override
            public Object transform(Object input) {
                RolePermissionPo po = (RolePermissionPo) input;
                return po.getPermissionId();
            }
        });
        collect = deleteRep(collect);
        Long parentId = permissionSearchDto.getParentId();
        Page<PermissionPo> all = null;
        if (parentId == null){
            all = permissionDao.findAllByIdInAndPermissionTypeOrderByOrderNumAscTitleAsc(
                    new PageRequest(Math.max(permissionSearchDto.getOffset() - 1, 0), permissionSearchDto.getLimit()),
                    collect, "button");
        }else {
            all = permissionDao.findAllByIdInAndPermissionTypeAndParentIdOrderByOrderNumAscTitleAsc(
                    new PageRequest(Math.max(permissionSearchDto.getOffset() - 1, 0), permissionSearchDto.getLimit()),
                    collect, "button",parentId);
        }

        permissionSearchDto.setList(ConverterService.convertToList(ConverterService.convertToList(all.getContent(), PermissionDto.class), PermissionDto.class));
        permissionSearchDto.setTotalPages(all.getTotalPages());
        permissionSearchDto.setTotalRecord(all.getTotalElements());
        return permissionSearchDto;

    }

    @Override
    public void save(PermissionDto permissionDto) {
        PermissionPo permissionPo = new PermissionPo();
        if (null != permissionDto.getId()) {
            permissionPo = permissionDao.findOne(permissionDto.getId());
            if (permissionPo == null) {
                permissionPo = new PermissionPo();
            }
        }
        ConverterService.convert(permissionDto, permissionPo);
        permissionDao.save(permissionPo);
    }

    @Override
    public PermissionDto findMenuById(Long id) {
        PermissionPo permissionPo = new PermissionPo();
        permissionPo.setId(id);
        permissionPo.setPermissionType("menu");
        Example<PermissionPo> example = Example.of(permissionPo);
        return ConverterService.convert(permissionDao.findOne(example), PermissionDto.class);
    }

    @Override
    public PermissionDto findElementById(Long id) {
        PermissionPo permissionPo = new PermissionPo();
        permissionPo.setId(id);
        permissionPo.setPermissionType("button");
        Example<PermissionPo> example = Example.of(permissionPo);
        return ConverterService.convert(permissionDao.findOne(example), PermissionDto.class);
    }

    @Override
    public void deleteMenu(Long id) {
        permissionDao.delete(id);
    }

    @Override
    public void deleteElement(Long id) {
        permissionDao.delete(id);
    }

    @Override
    public List<PermissionDto> findElementsByRoleId(Long id) {
        List<RolePermissionPo> byRoleId = rolePermissionDao.findByRoleId(id);
        Collection<Long> collect = CollectionUtils.collect(byRoleId, new Transformer() {
            @Override
            public Object transform(Object input) {
                RolePermissionPo po = (RolePermissionPo) input;
                return po.getPermissionId();
            }
        });
        collect = deleteRep(collect);
        List<PermissionPo> all = permissionDao.findALlByIdInAndPermissionTypeOrderByOrderNumAscTitleAsc(collect, "button");
        return ConverterService.convertToList(all, PermissionDto.class);
    }

    @Override
    public List<PermissionDto> findElementsByParentId(Long parentId) {
        PermissionPo permissionPo = new PermissionPo();
        permissionPo.setPermissionType("button");
        permissionPo.setParentId(parentId);
        Example<PermissionPo> example = Example.of(permissionPo);
        List<PermissionPo> all = permissionDao.findAll(example);
        return ConverterService.convertToList(all, PermissionDto.class);
    }

    @Override
    public List<PermissionDto> findElementsByRoleIdAndParentId(Long parentId, Long id) {
        List<RolePermissionPo> byRoleId = rolePermissionDao.findByRoleId(id);
        Collection<Long> collect = CollectionUtils.collect(byRoleId, new Transformer() {
            @Override
            public Object transform(Object input) {
                RolePermissionPo po = (RolePermissionPo) input;
                return po.getPermissionId();
            }
        });
        collect = deleteRep(collect);
        List<PermissionPo> all = permissionDao.findALlByIdInAndPermissionTypeOrderByOrderNumAscTitleAsc(collect, "button");
        for(Iterator<PermissionPo> it = all.iterator(); it.hasNext();){
            PermissionPo permissionPo = it.next();
            if (!parentId.equals(permissionPo.getParentId())){
                it.remove();
            }
        }
        return ConverterService.convertToList(all, PermissionDto.class);
    }

    @Override
    public void addPermission(Long id, Long elementId) {
        List<RolePermissionPo> list = rolePermissionDao.findByRoleIdAndPermissionId(id,elementId);
        if(CollectionUtils.isEmpty(list) || list.size()==0){
            RolePermissionPo rolePermissionPo = new RolePermissionPo();
            rolePermissionPo.setRoleId(id);
            rolePermissionPo.setPermissionId(elementId);
            rolePermissionDao.save(rolePermissionPo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removePermission(Long id, Long elementId) {
        rolePermissionDao.deleteByRoleIdAndPermissionId(id, elementId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleMenu(Long id, List<Long> ids) {
        List<PermissionDto> menusByRoleId = findMenusByRoleId(id);
        Collection<Long> collect = CollectionUtils.collect(menusByRoleId, new Transformer() {
            @Override
            public Object transform(Object input) {
                PermissionDto permissionDto = (PermissionDto) input;
                ;
                return permissionDto.getId();
            }
        });
        for (Long aLong : collect) {
            rolePermissionDao.deleteByRoleIdAndPermissionId(id, aLong);
        }

        for (Long aLong : ids) {
            RolePermissionPo rolePermissionPo = new RolePermissionPo();
            rolePermissionPo.setRoleId(id);
            rolePermissionPo.setPermissionId(aLong);
            rolePermissionDao.save(rolePermissionPo);
        }
    }

    @Override
    public Boolean findByCode(String code) {
        Long id = null;
        List<PermissionPo> list = permissionDao.findByCode(code);

        if(CollectionUtils.isNotEmpty(list)) {
            id = list.get(0).getId();
        }

        if(id == null) {
            return false;
        }

        List<RolePermissionPo> rolePermissionList = rolePermissionDao.findByRoleIdAndPermissionId(SecurityUtils.getCurrentUser().getRoleId(), id);

        if(CollectionUtils.isNotEmpty(rolePermissionList)) {
            return true;
        }
        return false;
    }

    @Override
    public void addPermissions(Long id, Long[] elementIds) {
        if(elementIds != null && elementIds.length != 0) {
            for(Long elementId : elementIds) {
                RolePermissionPo rolePermission = rolePermissionDao.findByPermissionIdAndRoleId(elementId, id);
                if(rolePermission == null) {
                    RolePermissionPo rolePermissionPo = new RolePermissionPo();
                    rolePermissionPo.setRoleId(id);
                    rolePermissionPo.setPermissionId(elementId);
                    rolePermissionDao.save(rolePermissionPo);
                }

            }
        }
    }

    @Transactional
    @Override
    public void removePermissions(Long id, Long[] elementIds) {
        if(elementIds != null && elementIds.length != 0) {
            for(Long elementId : elementIds) {
                rolePermissionDao.deleteByRoleIdAndPermissionId(id, elementId);
            }
        }
    }

    @Override
    public void move(Long id, boolean up) {
        PermissionPo permissionPo = new PermissionPo();
        permissionPo.setId(id);
        permissionPo.setPermissionType("menu");
        Example<PermissionPo> example = Example.of(permissionPo);
        PermissionPo menu = permissionDao.findOne(example);
        Long temp= menu.getOrderNum();
        List<PermissionPo> childs = permissionDao.findByParentIdOrderByOrderNumAsc(menu.getParentId());
        for (int i = 0; i < childs.size(); i++) {
            PermissionPo current = childs.get(i);
            if (current.getId().equals(id)) {
                if (up) {
                    if (i != 0) {
                        PermissionPo pre = childs.get(i - 1);
                        menu.setOrderNum(pre.getOrderNum());
                        pre.setOrderNum(temp);
                        permissionDao.save(pre);
                    }else{
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "已在最顶层，无法移动！");
                    }
                } else {
                    if (i != childs.size() - 1) {
                        PermissionPo next = childs.get(i + 1);
                        menu.setOrderNum(next.getOrderNum());
                        next.setOrderNum(temp);
                        permissionDao.save(next);
                    }else{
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "已在最底层，无法移动！");
                    }
                }
            }
        }
        permissionDao.save(menu);
    }

    @Override
    public Map<Long, PermissionDto> getPermissionsMap(Long roleId ,String type) {
        List<RolePermissionPo> byRoleId = rolePermissionDao.findByRoleId(roleId);
        Collection<Long> collect = CollectionUtils.collect(byRoleId, new Transformer() {
            @Override
            public Object transform(Object input) {
                RolePermissionPo po = (RolePermissionPo) input;
                return po.getPermissionId();
            }
        });
        collect = deleteRep(collect);
        Map<Long, PermissionDto> map = new HashMap<>();
        if (type==null){
            return map;
        }
        List<PermissionPo> all = permissionDao.findALlByIdInAndPermissionTypeOrderByOrderNumAscTitleAsc(collect, type);
        for (PermissionPo p : all){
            map.put(p.getId(),ConverterService.convert(p,PermissionDto.class));
        }
        return map;
    }

    private Collection<Long> deleteRep(Collection<Long> collect){
        Set<Long> uniqueResult = new HashSet(collect);
        Collection<Long> result = CollectionUtils.collect(uniqueResult, new Transformer() {
            @Override
            public Object transform(Object input) {
                return (Long) input;
            }
        });
        return result;
    }

}
