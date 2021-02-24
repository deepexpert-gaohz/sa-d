package com.ideatech.ams.system.user.service;

import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.dto.UserSearchDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.List;

public interface UserService {
    UserDto findByUsername(String username);

    String findPasswordByUserId(Long userId);

    void changePassword(Long userId, String password);

    UserDto save(UserDto userDto);

    UserDto findById(Long id);

    UserDto findAllById(Long id);//查询包括被逻辑删除的用户信息

    /**
     * 分页查询用户
     * 查询条件：
     * name： 用户姓名
     * orgId: 用户机构ID
     * orgFullId: 用户机构fullId
     * 如果传递了用户机构ID，则只查询该机构下的用户
     * 如果用户机构ID为空，用户机构fullID不为空，则查询该机构及其下属机构的用户
     * @param userSearchDto
     * @return
     */
    UserSearchDto search(UserSearchDto userSearchDto);

    void deleteById(Long id);

    void unbind(Long userId, Long orgId);

    List<UserDto> findByRoleId(Long id);

    void save(List<UserDto> userDtos);

    List<UserDto> findByOrgId(Long id);

    void updateOrg(Long id, List<Long> userIds);

    UserDto findVirtualUser();

    void grant(Long userId, Long roleId);

    void disable(Long userId);

    void enable(Long userId);

    UserSearchDto findByUsernameList(List<String> nameList,final UserSearchDto userSearchDto);

    List<Long> findUserIdByLikeUsername(String username);

    List<Long> findUserIdByLikeCName(String cname);

    IExcelExport exportUserByOrgId(Long orgId);

    void pwdUpdateDate();
}
