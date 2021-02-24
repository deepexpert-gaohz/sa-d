package com.ideatech.ams.initializer;

import com.ideatech.ams.service.JnnsDataInitService;
import com.ideatech.ams.system.dict.dao.OptionDao;
import com.ideatech.ams.system.dict.entity.OptionPo;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.user.dao.UserDao;
import com.ideatech.common.constant.DataInitializerConstant;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zoulang
 * @create 2019-06-16 上午9:52
 **/
@Component
public class JnnsUsersInitializer extends AbstractDataInitializer {

    @Autowired
    JnnsDataInitService jnnsDataInitService;


    @Autowired
    UserDao userDao;

    @Override
    protected void doInit() throws Exception {
        jnnsDataInitService.uploadUser();
    }

    @Override
    protected boolean isNeedInit() {
        return userDao.findAll().size() < 10;
    }

    @Override
    public Integer getIndex() {
        return DataInitializerConstant.DICT_INITIALIZER_INDEX + 3;
    }
}
