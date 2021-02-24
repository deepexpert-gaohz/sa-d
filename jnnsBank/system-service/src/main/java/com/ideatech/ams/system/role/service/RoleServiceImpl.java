package com.ideatech.ams.system.role.service;

import com.ideatech.ams.system.role.dao.RoleDao;
import com.ideatech.ams.system.role.dto.RoleDto;
import com.ideatech.ams.system.role.entity.RolePo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.util.SecurityUtils;
import com.ideatech.common.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liangding
 * @create 2018-05-05 下午9:37
 **/
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public RoleDto findById(Long roleId) {
        RolePo rolePo = roleDao.findOne(roleId);
        return ConverterService.convert(rolePo, RoleDto.class);
    }

    @Override
    public List<RoleDto> findAll() {
        List<RolePo> all = roleDao.findAll();
        return ConverterService.convertToList(all, RoleDto.class);
    }

    @Override
    public List<RoleDto> findByName(String name) {
        List<RolePo> rolePos = null;
        if (StringUtils.isNotBlank(name)) {
            rolePos = roleDao.findAllByNameLike("%" + name + "%");
        } else {
            Long roleId = SecurityUtils.getCurrentUser().getRoleId();
            rolePos = getByLevel(roleId);
        }
        return ConverterService.convertToList(rolePos, RoleDto.class);
    }

    @Override
    public List<RoleDto> findByNameEquals(String name) {
        List<RolePo> rolePos = null;
        if (StringUtils.isNotBlank(name)) {
            rolePos = roleDao.findByName(name);
        } else {
            Long roleId = SecurityUtils.getCurrentUser().getRoleId();
            rolePos = getByLevel(roleId);
        }
        return ConverterService.convertToList(rolePos, RoleDto.class);
    }

    @Override
    public void save(RoleDto roleDto) {
        roleDto.setName(roleDto.getName().trim());
        roleDto.setCode(roleDto.getCode().trim());
        roleDto.setLevel(roleDto.getLevel().trim());
        RolePo rolePo = new RolePo();
        if (roleDto.getId() != null) {
            rolePo = roleDao.findOne(roleDto.getId());
            if (rolePo == null) {
                rolePo = new RolePo();
            }
        }
        ConverterService.convert(roleDto, rolePo);
        roleDao.save(rolePo);
    }

    @Override
    public void delete(Long id) {
        roleDao.delete(id);
    }

    @Override
    public RoleDto findByCode(String code) {
        RolePo byCode = roleDao.findByCode(code);
        return ConverterService.convert(byCode, RoleDto.class);
    }

    @Override
    public List<RoleDto> findbyLevel(Long roleId) {
        List<RolePo> levelList = new ArrayList<RolePo>();
        List<RolePo> list = roleDao.findAll();
        RolePo rolePo = roleDao.findOne(roleId);
        //超级管理员能看到所有
        if("0".equals(rolePo.getLevel())){
            return ConverterService.convertToList(list, RoleDto.class);
        }else{
            for(RolePo rolePo1 : list){
                if(StringUtils.isNotBlank(rolePo1.getLevel())){
                    if(Integer.parseInt(rolePo1.getLevel()) > Integer.parseInt(rolePo.getLevel()) || rolePo1.getName().equals(rolePo.getName())){
                        levelList.add(rolePo1);
                    }
                }
            }
            return ConverterService.convertToList(levelList, RoleDto.class);
        }
    }

    public List<RolePo> getByLevel(Long roleId){
        List<RolePo> levelList = new ArrayList<RolePo>();
        List<RolePo> list = roleDao.findAll();
        RolePo rolePo = roleDao.findOne(roleId);
        if("0".equals(rolePo.getLevel()) || "admin".equals(rolePo.getCode())){
            return list;
        }else {
            for (RolePo rolePo1 : list) {
                if (StringUtils.isNotBlank(rolePo1.getLevel())) {
                    if (Integer.parseInt(rolePo1.getLevel()) > Integer.parseInt(rolePo.getLevel())) {
                        levelList.add(rolePo1);
                    }
                }
            }
        }
        return levelList;
    }
}
