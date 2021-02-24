package com.ideatech.ams.risk.whiteList.service;

import com.ideatech.ams.risk.whiteList.dao.riskWhiteDao;
import com.ideatech.ams.risk.whiteList.dto.WhiteListDto;
import com.ideatech.ams.risk.whiteList.dto.WhiteListSearchDto;
import com.ideatech.ams.risk.whiteList.entity.WhiteList;
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
 * @Author: yinjie
 * @Date: 2019/5/27 9:58
 * @description
 */

@Service
public class WhiteListServiceImp implements WhiteListService{

    @Autowired
    riskWhiteDao riskWhiteDao;

    @Override
    public void saveWhiteList(WhiteListDto whiteListDto) {
        WhiteList whiteList = new WhiteList();
        if(null != whiteListDto.getId()){
            whiteList = riskWhiteDao.findOne(whiteListDto.getId());
            if(whiteList == null){
                whiteList = new WhiteList();
            }
        }
        ConverterService.convert(whiteListDto,whiteList);
        riskWhiteDao.save(whiteList);
    }

    @Override
    public WhiteListDto findWhiteListDtoById(Long id) {
        WhiteListDto whiteListDto = new WhiteListDto();
        WhiteList one = riskWhiteDao.findOne(id);
        return ConverterService.convert(one,whiteListDto);
    }

    @Override
    public void delWhiteListDto(Long id) {
        riskWhiteDao.delete(id);
    }

    @Override
    public WhiteListDto findByAccountId(String accountId) {
        WhiteListDto whiteListDto = new WhiteListDto();
        WhiteList byAccountId = riskWhiteDao.findByAccountId(accountId);
        return ConverterService.convert(byAccountId,whiteListDto);
    }

    @Override
    public WhiteListDto findBySocialUnifiedCode(String socialUnifiedCode) {
        return null;
    }

    @Override
    public WhiteListSearchDto searchWhiteListDto(final WhiteListSearchDto whiteListSearchDto) {
        Specification<WhiteList> specification = new Specification<WhiteList>() {
            List<Predicate> predicateList = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder cb) {
                if(StringUtils.isNotBlank(whiteListSearchDto.getAccountId())){
                    predicateList.add(cb.like(root.<String>get("accountId").as(String.class),"%"+whiteListSearchDto.getAccountId()+"%"));
                }
                if(StringUtils.isNotBlank(whiteListSearchDto.getAccountName())){
                    predicateList.add(cb.like(root.<String>get("accountName").as(String.class),"%"+whiteListSearchDto.getAccountName()+"%"));
                }
                if(StringUtils.isNotBlank(whiteListSearchDto.getSocialUnifiedCode())){
                    predicateList.add(cb.like(root.<String>get("socialUnifiedCode").as(String.class),"%"+whiteListSearchDto.getSocialUnifiedCode()+"%"));
                }
                return cb.and(predicateList.toArray(new Predicate[0]));
            }
        };
        Pageable pageable = new PageRequest(Math.max(whiteListSearchDto.getOffset()-1, 0), whiteListSearchDto.getLimit());
        Page<WhiteList> all = riskWhiteDao.findAll(specification, pageable);
        List<WhiteList> content = all.getContent();
        final List<WhiteListDto> whiteListDtos = ConverterService.convertToList(content, WhiteListDto.class);
        whiteListSearchDto.setList(whiteListDtos);
        whiteListSearchDto.setTotalPages(all.getTotalPages());
        whiteListSearchDto.setTotalRecord(all.getTotalElements());
        return whiteListSearchDto;
    }
}
