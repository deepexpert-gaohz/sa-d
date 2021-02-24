package com.ideatech.ams.risk.rely.service;

import com.ideatech.ams.risk.rely.dao.ModelRelyDao;
import com.ideatech.ams.risk.rely.dto.ModelRelyChilderDto;
import com.ideatech.ams.risk.rely.dto.ModelRelyDto;
import com.ideatech.ams.risk.rely.dto.ModelRelyMapDto;
import com.ideatech.ams.risk.rely.dto.ModelRelySearchDto;
import com.ideatech.ams.risk.rely.entity.ModelRely;
import com.ideatech.common.converter.ConverterService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 依赖表的关系
 */
@Service
public class ModelRelyServiceImpl implements ModelRelyService {

    @Autowired
    ModelRelyDao modelRelyDao;

    @Override
    public void save(ModelRelyDto modelRelyDto) {
        ModelRely modelRely = new ModelRely();
        if(modelRelyDto.getId() != null){
            modelRely = modelRelyDao.findOne(modelRelyDto.getId());
            if(modelRely == null){
                modelRely = new ModelRely();
            }
        }
        ConverterService.convert(modelRelyDto,modelRely);
        modelRelyDao.save(modelRely);
    }

    @Override
    public ModelRelySearchDto search(final ModelRelySearchDto modelRelySearchDto) {
        Specification specification = new Specification() {
            List<Predicate> predicates = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder cb) {
                if(StringUtils.isNotBlank(modelRelySearchDto.getModelTable())){
                    predicates.add(cb.like(root.<String>get("modelTable").as(String.class),"%"+modelRelySearchDto.getModelTable()+"%"));
                }
                if(StringUtils.isNotBlank(modelRelySearchDto.getRelyTable())){
                    predicates.add(cb.like(root.<String>get("relyTable").as(String.class),"%"+modelRelySearchDto.getRelyTable()+"%"));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
        Pageable pageable = new PageRequest(Math.max(modelRelySearchDto.getOffset()-1, 0), modelRelySearchDto.getLimit());

        Page<ModelRely> all = modelRelyDao.findAll(specification, pageable);
        List<ModelRely> content = all.getContent();
        List<ModelRelyDto> modelRelyDtos = ConverterService.convertToList(content, ModelRelyDto.class);
        modelRelySearchDto.setTotalPages(all.getTotalPages());
        modelRelySearchDto.setTotalRecord(all.getTotalElements());
        modelRelySearchDto.setList(modelRelyDtos);
        return modelRelySearchDto;
    }

    @Override
    public void deleteById(Long id) {
        modelRelyDao.delete(id);
    }

    @Override
    public ModelRelyMapDto findByModelTable(String mTable) {
        List<ModelRely> byMTable = modelRelyDao.findByModelTable(mTable);
        List<ModelRelyChilderDto> childerDtos1 = ConverterService.convertToList(byMTable, ModelRelyChilderDto.class);
        ModelRelyMapDto modelRelyMapDto = new ModelRelyMapDto();
        modelRelyMapDto.setModelTable(mTable);
        modelRelyMapDto.setChildren(childerDtos1);
        return modelRelyMapDto;
    }

    @Override
    public ModelRelyDto findModelRelyDtoByModelTable(String modelTable) {
        List<ModelRely> byModelTable = modelRelyDao.findByModelTable(modelTable);
        List<ModelRelyDto> modelRelyDtos = ConverterService.convertToList(byModelTable, ModelRelyDto.class);
        return modelRelyDtos.get(0);
    }

    @Override
    public ModelRelyDto getModelRelyById(Long id) {
        ModelRelyDto modelRelyDto = new ModelRelyDto();
        ModelRely one = modelRelyDao.findOne(id);
        return ConverterService.convert(one,modelRelyDto);
    }


}
