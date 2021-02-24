package com.ideatech.ams.system.initializer;

import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.system.role.dao.RoleDao;
import com.ideatech.ams.system.role.entity.RolePo;
import com.ideatech.ams.system.user.dao.UserDao;
import com.ideatech.ams.system.user.entity.UserPo;
import com.ideatech.common.constant.DataInitializerConstant;
import com.ideatech.common.constant.IdeaConstant;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author liangding
 * @create 2018-05-25 下午11:20
 **/
@Component
public class AuthInitializer extends AbstractDataInitializer {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void doInit() throws Exception {
        RolePo defaultRole = new RolePo("0", "默认角色", Boolean.TRUE, Boolean.FALSE,"9");
        roleDao.save(defaultRole);
        RolePo agentRole = new RolePo("1", "经办人", Boolean.TRUE, Boolean.FALSE,"9");
        roleDao.save(agentRole);
        RolePo reviewerRole = new RolePo("2", "复核人", Boolean.TRUE, Boolean.FALSE,"9");
        roleDao.save(reviewerRole);
        RolePo reportRole = new RolePo("3", "报备人", Boolean.TRUE, Boolean.FALSE,"9");
        roleDao.save(reportRole);
        RolePo adminRole = new RolePo("4", "系统管理员", Boolean.TRUE, Boolean.FALSE,"1");
        roleDao.save(adminRole);
        RolePo suRole = new RolePo("admin", "超级管理员", Boolean.TRUE, Boolean.FALSE,"0");
        roleDao.save(suRole);
        RolePo fenRole = new RolePo("5", "分行审核员", Boolean.TRUE, Boolean.FALSE,"9");
        roleDao.save(fenRole);
        RolePo zongRole = new RolePo("6", "总行审核员", Boolean.TRUE, Boolean.FALSE,"9");
        roleDao.save(zongRole);

        OrganizationPo organizationPo = new OrganizationPo("组织机构根节点", "1", IdeaConstant.ORG_ROOT_CODE, "", "", "", -1L, Boolean.FALSE,null,null,null,null,null,null,null,null,null);
        organizationPo.setId(1L);
        organizationDao.save(organizationPo);

        //new BCryptPasswordEncoder(12) 修改为passwordEncoder
        UserPo adminUser = new UserPo("admin", "管理员", passwordEncoder.encode("admin"), "", Boolean.TRUE, Boolean.FALSE, null, Boolean.FALSE, organizationPo.getId(), suRole.getId(),"","",null);
        adminUser.setId(1L);
        userDao.save(adminUser);
        UserPo virtualUser = new UserPo("virtual", "虚拟用户", passwordEncoder.encode("123456"), "", Boolean.TRUE, Boolean.FALSE, null, Boolean.FALSE, organizationPo.getId(), null,"","",null);
        virtualUser.setId(2L);
        userDao.save(virtualUser);


    }

    @Override
    protected boolean isNeedInit() {
        return organizationDao.findAll().size() == 0;
    }

    @Override
    public Integer getIndex() {
        return DataInitializerConstant.AUTH_INITIALIZER_INDEX;
    }
}
