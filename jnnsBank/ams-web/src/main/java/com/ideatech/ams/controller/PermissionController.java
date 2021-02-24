package com.ideatech.ams.controller;

import com.ideatech.ams.system.permission.dto.PermissionDto;
import com.ideatech.ams.system.permission.dto.PermissionSearchDto;
import com.ideatech.ams.system.permission.service.PermissionService;
import com.ideatech.ams.system.role.dto.RoleDto;
import com.ideatech.ams.system.role.service.RoleService;
import com.ideatech.ams.system.trace.aop.OperateLog;
import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.ams.vo.AuthorityMenuTreeVo;
import com.ideatech.ams.vo.MenuTreeVo;
import com.ideatech.ams.vo.NotAuthorityMenuTreeVo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.SecurityUtils;
import com.ideatech.common.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author liangding
 * @create 2018-05-06 下午7:51
 **/
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/menu/list")
    public ResultDto menuList(String title){
        return ResultDtoFactory.toAckData(permissionService.findMenusByTitle(title));
    }

    @GetMapping("/menu/{id}")
    public ResultDto findMenuById(@PathVariable("id") Long id){
        return ResultDtoFactory.toAckData(permissionService.findMenuById(id));
    }

    @OperateLog(operateModule = OperateModule.PERMISSION,operateType = OperateType.UPDATE)
    @PutMapping("/menu/{id}")
    public ResultDto updateMenu(@PathVariable("id") Long id, PermissionDto permissionDto){
        permissionDto.setId(id);
        permissionService.save(permissionDto);
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.PERMISSION,operateType = OperateType.DELETE)
    @DeleteMapping("/menu/{id}")
    public ResultDto deleteMenu(@PathVariable("id") Long id){
        permissionService.deleteMenu(id);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/menu/all")
    public ResultDto allMenu(){
        return ResultDtoFactory.toAckData(permissionService.findAllMenus());
    }

    @OperateLog(operateModule = OperateModule.PERMISSION,operateType = OperateType.INSERT)
    @PostMapping("/menu/")
    public ResultDto saveMenu(PermissionDto permissionDto) {
        permissionService.save(permissionDto);
        return ResultDtoFactory.toAck();
    }

    /**
     * 没有权限的菜单树 菜单管理
     * @param title
     * @return
     */
    @GetMapping("/menu/tree")
    public ResultDto getMenuTree(String title) {
        List<PermissionDto> allMenus;
        allMenus = permissionService.findMenusByTitle(title);
        return ResultDtoFactory.toAckData(buildMenuTree(allMenus));
    }

    /**
     * 有权限的菜单树 权限管理
     * @param roleId
     * @return
     */
    @GetMapping("/menu/authorityTree")
    public ResultDto getMenuAuthorityTree(Long roleId) {
        SecurityUtils.UserInfo currentUser = SecurityUtils.getCurrentUser();
        RoleDto roleDto = roleService.findById(currentUser.getRoleId());
        List<PermissionDto> allMenus;
        Map<Long,PermissionDto> authMap;
        if (roleId==null){
            return ResultDtoFactory.toNack("roleId为空，获取失败。");
        }
        if (roleDto.getLevel().equals("0")){
            //admin可以查看所有菜单
            allMenus = permissionService.findAllMenus();
            authMap = permissionService.getPermissionsMap(roleId,"menu");
        }else{
            //非admin只能看自己拥有的权限对应的菜单
            Long[] roleIds = new Long[]{currentUser.getRoleId()};
            allMenus = permissionService.findMenusByRoleIds(Arrays.asList(roleIds));
            authMap = permissionService.getPermissionsMap(roleId,"menu");
        }
        return ResultDtoFactory.toAckData(buildAuthorityMenuTree(allMenus,authMap));

    }


    private List<AuthorityMenuTreeVo> buildAuthorityMenuTree(List<PermissionDto> menus,Map<Long,PermissionDto> authMenus) {
        List<AuthorityMenuTreeVo> menuTree = new ArrayList<>();
        for (PermissionDto menu : menus) {
            AuthorityMenuTreeVo menuTreeVo = new AuthorityMenuTreeVo(menu.getIcon(), menu.getTitle());
            menuTreeVo.setId(menu.getId());
            menuTreeVo.setParentId(menu.getParentId());
            if (authMenus.get(menu.getId())!=null){
                Map<String,Object> objectMap = new HashMap<>();
                objectMap.put("checked", true);
                menuTreeVo.setState(objectMap);
                menuTreeVo.setChecked(true);
            }
            menuTree.add(menuTreeVo);
        }
        return TreeUtil.bulid(menuTree, -1L);
    }

    @GetMapping("/element/page")
    public ResultDto elementPage(PermissionSearchDto permissionSearchDto) {

        SecurityUtils.UserInfo currentUser = SecurityUtils.getCurrentUser();
        RoleDto roleDto = roleService.findById(currentUser.getRoleId());
        if (roleDto.getLevel().equals("0")){
            return ResultDtoFactory.toAckData(permissionService.searchElement(permissionSearchDto));
        }else{
            return ResultDtoFactory.toAckData(permissionService.searchElementByRoleId(permissionSearchDto,currentUser.getRoleId()));
        }

    }

    @GetMapping("/element/list")
    public ResultDto elementList(Long parentId,Long roleId) {
        if (parentId==null||roleId==null){
            return ResultDtoFactory.toNack("parentId或roleId为空");
        }
        Map<Long,PermissionDto> authMap;
        List<PermissionDto> allElements;
        SecurityUtils.UserInfo currentUser = SecurityUtils.getCurrentUser();
        RoleDto roleDto = roleService.findById(currentUser.getRoleId());
        if (roleDto.getLevel().equals("0")){
            //admin可以查看所有按钮
            allElements = permissionService.findElementsByParentId(parentId);
            authMap = permissionService.getPermissionsMap(roleId,"button");
        }else{
            //非admin只能看自己拥有的权限对应的按钮
            allElements = permissionService.findElementsByRoleIdAndParentId(currentUser.getRoleId(),parentId);
            authMap = permissionService.getPermissionsMap(roleId,"button");
        }
        return ResultDtoFactory.toAckData(checked(allElements,authMap));
    }

    @OperateLog(operateModule = OperateModule.PERMISSION,operateType = OperateType.INSERT,operateContent = "按钮新建")
    @PostMapping("/element/")
    public ResultDto saveElement(PermissionDto permissionDto) {
        permissionService.save(permissionDto);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/element/{id}")
    public ResultDto findElementById(@PathVariable("id") Long id) {
        PermissionDto permissionDto = permissionService.findElementById(id);
        return ResultDtoFactory.toAckData(permissionDto);
    }

    @OperateLog(operateModule = OperateModule.PERMISSION,operateType = OperateType.UPDATE,operateContent = "按钮修改",cover = true)
    @PutMapping("/element/{id}")
    public ResultDto updateElement(@PathVariable("id") Long id, PermissionDto permissionDto) {
        permissionDto.setId(id);
        permissionService.save(permissionDto);
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.PERMISSION,operateType = OperateType.DELETE,operateContent = "按钮删除",cover = true)
    @DeleteMapping("/element/{id}")
    public ResultDto deleteElement(@PathVariable("id") Long id) {
        permissionService.deleteElement(id);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/element/code")
    public Boolean findByCode(String code) {
        return permissionService.findByCode(code);
    }

    @GetMapping("/menu/up/{id}")
    public ResultDto up(@PathVariable("id") Long id){
        permissionService.move(id,true);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/menu/down/{id}")
    public ResultDto down(@PathVariable("id") Long id){
        permissionService.move(id,false);
        return ResultDtoFactory.toAck();
    }

    /**
     * 菜单管理模块使用 ，没有权限的树。
     * @param menus
     * @return
     */
    private List<NotAuthorityMenuTreeVo> buildMenuTree(List<PermissionDto> menus) {
        List<NotAuthorityMenuTreeVo> menuTree = new ArrayList<>();
        for (PermissionDto menu : menus) {
            NotAuthorityMenuTreeVo menuTreeVo = new NotAuthorityMenuTreeVo();
            ConverterService.convert(menu,menuTreeVo);
            menuTree.add(menuTreeVo);
        }
        return TreeUtil.bulid(menuTree, -1L);
    }

    /**
     * 权限管理模块使用 ，按钮列表。
     * @param all 当前用户角色可以管理配置的按钮权限
     * @param checked 目标角色拥有的按钮权限
     * @return
     */
    private List<PermissionDto> checked(List<PermissionDto> all,Map<Long,PermissionDto> checked) {
        for (int i = 0;i<all.size();i++){
            if (checked.get(all.get(i).getId())!=null){
                all.get(i).setChecked(true);
            }
        }
        return all;
    }
}
