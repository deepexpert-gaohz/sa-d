package com.ideatech.ams.risk.modelKind.service;

import com.ideatech.ams.risk.modelKind.dao.RiskLevelDao;
import com.ideatech.ams.risk.modelKind.dto.RiskLevelDto;
import com.ideatech.ams.risk.modelKind.dto.RiskLevelSearchDto;
import com.ideatech.ams.risk.modelKind.dto.RiskTypeDto;
import com.ideatech.ams.risk.modelKind.entity.RiskLevel;
import com.ideatech.common.converter.ConverterService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

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
public class RiskLevelServiceImpl implements RiskLevelService {

    @Autowired
    RiskLevelDao riskLevelDao;


    @Override
    public void save(RiskLevelDto riskLevelDto) {
        RiskLevel riskLevel = new RiskLevel();
        if(null != riskLevelDto.getId()){
            riskLevel = riskLevelDao.findOne(riskLevelDto.getId());
            if(riskLevel == null){
                riskLevel = new RiskLevel();
            }
        }
        RiskLevel convert = ConverterService.convert(riskLevelDto, riskLevel);
        riskLevelDao.save(convert);
    }

    @Override
    public RiskLevelSearchDto search(final RiskLevelSearchDto riskLevelSearchDto) {
        Specification specification = new Specification() {
            List<Predicate> predicates = new ArrayList<>(); //所有的断言
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(riskLevelSearchDto.getLevelName())){
                    predicates.add(cb.like(root.get("levelName").as(String.class),"%"+riskLevelSearchDto.getLevelName()+"%"));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
        Pageable pageable = new PageRequest(Math.max(riskLevelSearchDto.getOffset()-1, 0), riskLevelSearchDto.getLimit());
        Page all = riskLevelDao.findAll(specification, pageable);
        List<RiskLevelDto> list = ConverterService.convertToList(all.getContent(), RiskLevelDto.class);
        riskLevelSearchDto.setList(list);
        riskLevelSearchDto.setTotalPages(all.getTotalPages());
        riskLevelSearchDto.setTotalRecord(all.getTotalElements());
        return riskLevelSearchDto;
    }

    @Override
    public RiskLevelDto findById(Long id) {

        RiskLevel one = riskLevelDao.findOne(id);
        RiskLevelDto riskLevelDto = new RiskLevelDto();
        return ConverterService.convert(one,riskLevelDto);
    }

    @Override
    public void delLevel(Long id) {
        riskLevelDao.delete(id);
    }

    @Override
    public RiskLevelDto findByLevelName(String leveLName) {
        RiskLevelDto riskLevelDto = new RiskLevelDto();
        RiskLevel byLevelName = riskLevelDao.findByLevelName(leveLName);
        return ConverterService.convert(byLevelName,riskLevelDto);
    }

    public List<RiskLevelDto> findAll(){
        List<RiskLevel> all = riskLevelDao.findAll();
        return ConverterService.convertToList(all,RiskLevelDto.class);
    }



}
