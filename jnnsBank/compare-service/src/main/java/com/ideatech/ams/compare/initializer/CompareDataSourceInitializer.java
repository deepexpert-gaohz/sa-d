package com.ideatech.ams.compare.initializer;

import com.ideatech.ams.compare.dao.DataSourceDao;
import com.ideatech.ams.compare.entity.DataSource;
import com.ideatech.ams.compare.enums.CollectType;
import com.ideatech.ams.compare.enums.DataSourceEnum;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.system.user.dao.UserDao;
import com.ideatech.ams.system.user.entity.UserPo;
import com.ideatech.common.constant.DataInitializerConstant;
import com.ideatech.common.initializer.AbstractDataInitializer;
import com.ideatech.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CompareDataSourceInitializer extends AbstractDataInitializer {
    @Autowired
    private DataSourceDao dataSourceDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private OrganizationDao organizationDao;

    @Override
    protected void doInit() throws Exception {
        create();
    }

    private void create() {
        UserPo user = userDao.findByUsername("admin");
        OrganizationPo org = organizationDao.findById(user.getOrgId());
        DataSource data = new DataSource();
        data.setId(1001L);
        data.setCreatedBy(String.valueOf(user.getId()));
        data.setCreatedDate(new Date());
        data.setName("本账管系统数据");
        data.setCollectType(CollectType.ONLINE);
        data.setOrganFullId(org.getFullId());
        data.setDataType(DataSourceEnum.AMS);
        data.setCode(StringUtils.lowerCase(DataSourceEnum.AMS.name()));
        data.setDomain(DataSourceEnum.AMS.getDomain());
        data.setCode("ams");
        dataSourceDao.save(data);

        data = new DataSource();
        data.setId(1002L);
        data.setCreatedBy(String.valueOf(user.getId()));
        data.setCreatedDate(new Date());
        data.setName("人行数据");
        data.setCollectType(CollectType.ONLINE);
        data.setOrganFullId(org.getFullId());
        data.setDataType(DataSourceEnum.PBC);
        data.setCode(StringUtils.lowerCase(DataSourceEnum.PBC.name()));
        data.setDomain(DataSourceEnum.PBC.getDomain());
        data.setCode("pbc");
        dataSourceDao.save(data);

        data = new DataSource();
        data.setId(1003L);
        data.setCreatedBy(String.valueOf(user.getId()));
        data.setCreatedDate(new Date());
        data.setName("工商数据");
        data.setCollectType(CollectType.ONLINE);
        data.setOrganFullId(org.getFullId());
        data.setDataType(DataSourceEnum.SAIC);
        data.setCode(StringUtils.lowerCase(DataSourceEnum.SAIC.name()));
        data.setDomain(DataSourceEnum.SAIC.getDomain());
        data.setCode("saic");
        dataSourceDao.save(data);

        data = new DataSource();
        data.setId(1004L);
        data.setCreatedBy(String.valueOf(user.getId()));
        data.setCreatedDate(new Date());
        data.setName("核心数据");
        data.setCollectType(CollectType.IMPORT);
        data.setOrganFullId(org.getFullId());
        data.setDataType(DataSourceEnum.CORE);
        data.setCode(StringUtils.lowerCase(DataSourceEnum.CORE.name()));
        data.setDomain(DataSourceEnum.CORE.getDomain());
        data.setCode("core");
        dataSourceDao.save(data);

        /**
         * 工商异动使用   需在线采集
         */
        data = new DataSource();
        data.setId(1005L);
        data.setCreatedBy("2");
        data.setCreatedDate(new Date());
        data.setName("账管异动数据");
        data.setCollectType(CollectType.ONLINE);
        data.setOrganFullId("1");
        data.setDataType(DataSourceEnum.AMS);
        data.setDomain(DataSourceEnum.AMS.getDomain());
        data.setCode("ams");
        dataSourceDao.save(data);

        data = new DataSource();
        data.setId(1006L);
        data.setCreatedBy("2");
        data.setCreatedDate(new Date());
        data.setName("工商异动数据");
        data.setCollectType(CollectType.ONLINE);
        data.setOrganFullId("1");
        data.setDataType(DataSourceEnum.SAIC);
        data.setDomain(DataSourceEnum.SAIC.getDomain());
        data.setCode("saic");
        dataSourceDao.save(data);

        data = new DataSource();
        data.setId(1007L);
        data.setCreatedBy("2");
        data.setCreatedDate(new Date());
        data.setName("客户异动数据");
        data.setCollectType(CollectType.ONLINE);
        data.setOrganFullId("1");
        data.setDataType(DataSourceEnum.OTHER);
        data.setDomain("CompareData5");
        data.setCode("csr");
        dataSourceDao.save(data);
    }

    ;

    @Override
    protected boolean isNeedInit() {
        return dataSourceDao.findAll().size() == 0;
    }

    @Override
    public Integer getIndex() {
        return DataInitializerConstant.OPTION_INITIALIZER_INDEX;
    }
}
