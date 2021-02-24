package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dao.CompareFieldDao;
import com.ideatech.ams.compare.dto.CompareFieldDto;
import com.ideatech.ams.compare.entity.CompareField;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CompareFieldServiceImpl extends BaseServiceImpl<CompareFieldDao, CompareField, CompareFieldDto> implements CompareFieldService {

    @Override
    public TableResultResponse query(CompareFieldDto dto, Pageable pageable) {

        Page<CompareField> page = getBaseDao().findAll(pageable);
        long count = getBaseDao().count();
        return new TableResultResponse((int)count,page.getContent());
    }

    @Override
    public List<CompareFieldDto> getAll() {
        List<CompareField> list = getBaseDao().findAll(new Sort(Sort.Direction.ASC, "id"));
        return ConverterService.convertToList(list, CompareFieldDto.class);
    }

    @Override
    public CompareFieldDto findByName(String name) {
        CompareField compareField = new CompareField();
        List<CompareField> compareFieldList = getBaseDao().findByName(name);

        if(CollectionUtils.isNotEmpty(compareFieldList)) {
            compareField = compareFieldList.get(0);
        }
        return ConverterService.convert(compareField, CompareFieldDto.class);
    }

    @Override
    public CompareFieldDto findByField(String field) {
        CompareField compareField = new CompareField();
        List<CompareField> compareFieldList = getBaseDao().findByField(field);

        if(CollectionUtils.isNotEmpty(compareFieldList)) {
            compareField = compareFieldList.get(0);
        }
        return ConverterService.convert(compareField, CompareFieldDto.class);
    }

}
