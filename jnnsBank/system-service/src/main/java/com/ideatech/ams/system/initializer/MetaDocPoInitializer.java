package com.ideatech.ams.system.initializer;

import com.ideatech.ams.system.meta.dao.MetaDocDao;
import com.ideatech.ams.system.meta.entity.MetaDocPo;
import com.ideatech.common.constant.DataInitializerConstant;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MetaDocPoInitializer extends AbstractDataInitializer {

    @Autowired
    private MetaDocDao metaDocDao;

    @Override
    protected void doInit() throws Exception {
        createMetaDocDao(1856723744L, "customer_doc", "客户信息", Boolean.FALSE);
    }

    private void createMetaDocDao(Long id, String code, String name, Boolean deleted) {
        if (String.valueOf(id).length() > 14) {
            id = Long.valueOf(String.valueOf(id).substring(0, 14));
        }
        MetaDocPo metaDocPo = new MetaDocPo();
        metaDocPo.setId(id);
        metaDocPo.setCode(code);
        metaDocPo.setName(name);
        metaDocPo.setDeleted(deleted);
        metaDocDao.save(metaDocPo);
    }

    @Override
    protected boolean isNeedInit() {
        return metaDocDao.findAll().size() == 0;
    }

    @Override
    public Integer getIndex() {
        return DataInitializerConstant.AUTH_INITIALIZER_INDEX;
    }
}
