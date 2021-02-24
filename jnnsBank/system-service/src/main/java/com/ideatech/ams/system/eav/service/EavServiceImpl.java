package com.ideatech.ams.system.eav.service;

import com.ideatech.ams.system.eav.dao.EavDao;
import com.ideatech.ams.system.eav.dto.EavDto;
import com.ideatech.ams.system.eav.entity.EavPo;
import com.ideatech.ams.system.meta.dao.MetaAttrDao;
import com.ideatech.ams.system.meta.dao.MetaDocDao;
import com.ideatech.ams.system.meta.entity.MetaAttrPo;
import com.ideatech.ams.system.meta.entity.MetaDocPo;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.service.BaseServiceImpl;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liangding
 * @create 2018-07-23 下午5:45
 **/
@Service
public class EavServiceImpl extends BaseServiceImpl<EavDao, EavPo, EavDto> implements EavService{
    @Autowired
    private MetaDocDao metaDocDao;

    @Autowired
    private MetaAttrDao metaAttrDao;

    @Override
    public Map<String, String> findByEntityIdAndDocCode(Long entityId, String docCode) {
        MetaDocPo byCode = metaDocDao.findByCode(docCode);
        List<EavPo> byEntityIdAndDocId = getBaseDao().findByEntityIdAndDocId(entityId, byCode.getId());
        Map<String, String> attrs = new HashMap<>();
        for (EavPo eavPo : byEntityIdAndDocId) {
            MetaAttrPo one = metaAttrDao.findOne(eavPo.getAttrId());
            attrs.put(one.getName(), eavPo.getValue());
        }
        return attrs;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void save(Long entityId, String docCode, Map<String, String> attrs) {
        if (MapUtils.isEmpty(attrs)) {
            return;
        }
        MetaDocPo byCode = metaDocDao.findByCode(docCode);
        getBaseDao().deleteByEntityIdAndDocId(entityId, byCode.getId());

        for (String s : attrs.keySet()) {
            EavPo eavPo = new EavPo();
            eavPo.setDocId(byCode.getId());
            MetaAttrPo byDocIdAndName = metaAttrDao.findByDocIdAndName(byCode.getId(), s);
            if (byDocIdAndName == null) {
                throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, String.format("文档类型[%s]下未配置[%s]属性", byCode.getCode(), s));
            }
            eavPo.setAttrId(byDocIdAndName.getId());
            eavPo.setEntityId(entityId);
            eavPo.setValue(attrs.get(s));
            getBaseDao().save(eavPo);
        }
    }

}
