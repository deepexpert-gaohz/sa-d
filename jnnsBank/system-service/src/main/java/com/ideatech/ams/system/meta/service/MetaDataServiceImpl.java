package com.ideatech.ams.system.meta.service;

import com.ideatech.ams.system.meta.dao.MetaAttrDao;
import com.ideatech.ams.system.meta.dao.MetaDocDao;
import com.ideatech.ams.system.meta.dto.MetaAttrDto;
import com.ideatech.ams.system.meta.dto.MetaDocDto;
import com.ideatech.ams.system.meta.entity.MetaAttrPo;
import com.ideatech.ams.system.meta.entity.MetaDocPo;
import com.ideatech.common.converter.ConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liangding
 * @create 2018-06-08 下午3:13
 **/
@Service
public class MetaDataServiceImpl implements MetaDataService {
    @Autowired
    private MetaDocDao metaDocDao;

    @Autowired
    private MetaAttrDao metaAttrDao;

    @Override
    public List<MetaDocDto> listDocs() {
        List<MetaDocPo> all = metaDocDao.findAll();
        return ConverterService.convertToList(all, MetaDocDto.class);
    }

    @Override
    public void save(MetaDocDto metaDocDto) {
        MetaDocPo metaDocPo = new MetaDocPo();
        if (metaDocDto.getId() != null) {
            metaDocPo = metaDocDao.findOne(metaDocDto.getId());
            if (metaDocPo == null) {
                metaDocPo = new MetaDocPo();
            }
        }
        ConverterService.convert(metaDocDto, metaDocPo);
        metaDocDao.save(metaDocPo);
    }

    @Override
    public MetaDocDto findMetaDocById(Long id) {
        MetaDocPo one = metaDocDao.findOne(id);
        return ConverterService.convert(one, MetaDocDto.class);
    }

    @Override
    public void deleteDoc(Long id) {
        metaDocDao.delete(id);
    }

    @Override
    public List<MetaAttrDto> listAttrByDocId(Long id) {
        List<MetaAttrPo> byDocId = metaAttrDao.findByDocId(id);
        return ConverterService.convertToList(byDocId, MetaAttrDto.class);
    }

    @Override
    public void saveAttr(MetaAttrDto metaAttrDto) {
        MetaAttrPo metaAttrPo = new MetaAttrPo();
        if (metaAttrDto.getId() != null) {
            metaAttrPo = metaAttrDao.findOne(metaAttrDto.getId());
            if (metaAttrPo == null) {
                metaAttrPo = new MetaAttrPo();
            }
        }
        ConverterService.convert(metaAttrDto, metaAttrPo);
        metaAttrDao.save(metaAttrPo);
    }

    @Override
    public void deleteAttr(Long attrId) {
        metaAttrDao.delete(attrId);
    }

    @Override
    public MetaAttrDto findAttrById(Long attrId) {
        MetaAttrPo one = metaAttrDao.findOne(attrId);
        return ConverterService.convert(one, MetaAttrDto.class);
    }

}
