package com.ideatech.ams.system.meta.service;

import com.ideatech.ams.system.meta.dto.MetaAttrDto;
import com.ideatech.ams.system.meta.dto.MetaDocDto;

import java.util.List;

public interface MetaDataService {
    List<MetaDocDto> listDocs();

    void save(MetaDocDto metaDocDto);

    MetaDocDto findMetaDocById(Long id);

    void deleteDoc(Long id);

    List<MetaAttrDto> listAttrByDocId(Long id);

    void saveAttr(MetaAttrDto metaAttrDto);

    void deleteAttr(Long attrId);

    MetaAttrDto findAttrById(Long attrId);
}
