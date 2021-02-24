package com.ideatech.ams.risk.modelKind.service;

import com.ideatech.ams.risk.modelKind.dao.RiskTypeDao;
import com.ideatech.ams.risk.modelKind.dto.RiskTypeSearchDto;
import com.ideatech.ams.risk.modelKind.dto.RiskTypeDto;
import com.ideatech.ams.risk.modelKind.entity.RiskType;
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
 * @auther zhuqr
 * @date ${date} ${time}
 */
@Service
public class RiskTypeServiceImpl implements RiskTypeService {

    @Autowired
    RiskTypeDao riskTypeDao;

    @Override
    public RiskTypeSearchDto search(final RiskTypeSearchDto riskTypeSearchDto) {
        Specification specification = new Specification() {
            List<Predicate> predicates = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder cb) {
                if(StringUtils.isNotBlank(riskTypeSearchDto.getTypeName())){
                    predicates.add(cb.like(root.<String>get("typeName").as(String.class),"%"+ riskTypeSearchDto.getTypeName()+"%"));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
        Pageable pageable = new PageRequest(Math.max(riskTypeSearchDto.getOffset()-1, 0), riskTypeSearchDto.getLimit());
        Page all = riskTypeDao.findAll(specification, pageable);
        List<RiskTypeDto> list = ConverterService.convertToList(all.getContent(), RiskTypeDto.class);
        riskTypeSearchDto.setList(list);
        riskTypeSearchDto.setTotalPages(all.getTotalPages());
        riskTypeSearchDto.setTotalRecord(all.getTotalElements());
        return riskTypeSearchDto;
    }

    @Override
    public void save(RiskTypeDto riskTypeDto) {
        RiskType riskType = new RiskType();
        if(riskTypeDto.getId() != null){
             riskType = riskTypeDao.findOne(riskTypeDto.getId());
            if(riskType == null){
                riskType = new RiskType();
            }
        }
        ConverterService.convert(riskTypeDto,riskType);
       riskTypeDao.save(riskType);

    }

    @Override
    public RiskTypeDto getById(Long id) {
        RiskType one = riskTypeDao.findOne(id);
        RiskTypeDto riskTypeDto = new RiskTypeDto();
        return ConverterService.convert(one,riskTypeDto);
    }

    @Override
    public void delType(Long id) {
        riskTypeDao.delete(id);
    }

    @Override
    public RiskTypeDto findByTypeName(String typeName) {
        RiskType byTypeName = riskTypeDao.findByTypeName(typeName);
        RiskTypeDto riskTypeDto = new RiskTypeDto();
        return ConverterService.convert(byTypeName,riskTypeDto);
    }


    public List<RiskTypeDto> findAll(){
        List<RiskType> all = riskTypeDao.findAll();
        return ConverterService.convertToList(all,RiskTypeDto.class);
    }


}
