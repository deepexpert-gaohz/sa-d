package com.ideatech.ams.system.blacklist.service;

import com.ideatech.ams.system.blacklist.dao.BlackListEntryDao;
import com.ideatech.ams.system.blacklist.dto.BlackListEntryDto;
import com.ideatech.ams.system.blacklist.dto.BlackListSearchDto;
import com.ideatech.ams.system.blacklist.entity.BlackListEntryPo;
import com.ideatech.ams.system.blacklist.enums.BlackListResultEnum;
import com.ideatech.ams.system.blacklist.enums.EBlackListEntrySource;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * @author liangding
 * @create 2018-06-26 上午11:06
 **/
@Service
public class BlackListServiceImpl implements BlackListService{
    @Autowired
    private BlackListEntryDao blackListEntryDao;

    @Override
    public List<BlackListEntryDto> list() {
        List<BlackListEntryPo> all = blackListEntryDao.findAll();
        return ConverterService.convertToList(all, BlackListEntryDto.class);
    }

    @Override
    public ResultDto create(BlackListEntryDto blackListEntryDto) {
        List<BlackListEntryPo> blackListEntryPos = blackListEntryDao.findByEntName(blackListEntryDto.getEntName());
        if(CollectionUtils.isNotEmpty(blackListEntryPos)){
            return ResultDtoFactory.toNack("企业名称已存在，无需重复添加");
        }else{
            BlackListEntryPo blackListEntryPo = ConverterService.convert(blackListEntryDto, BlackListEntryPo.class);
            blackListEntryPo.setId(null);
            blackListEntryDao.save(blackListEntryPo);
            return ResultDtoFactory.toAck("保存成功");
        }
    }

    @Override
    public void update(BlackListEntryDto blackListEntryDto) {
        BlackListEntryPo po = blackListEntryDao.findOne(blackListEntryDto.getId());
        if (po == null) {
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST);
        }
        ConverterService.convert(blackListEntryDto, po);
        blackListEntryDao.save(po);
    }

    @Override
    public void delete(Long id) {
        BlackListEntryPo po = blackListEntryDao.findOne(id);
        if (po == null) {
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST);
        }
        blackListEntryDao.delete(po);
    }

    @Override
    public BlackListEntryDto getById(Long id) {
        BlackListEntryPo po = blackListEntryDao.findOne(id);
        if (po == null) {
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST);
        }
        return ConverterService.convert(po, BlackListEntryDto.class);
    }

    @Override
    public BlackListSearchDto search(final BlackListSearchDto blackListSearchDto) {
        Specification<BlackListEntryPo> specification = new Specification<BlackListEntryPo>() {
            @Override
            public Predicate toPredicate(Root<BlackListEntryPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if (null != blackListSearchDto) {
                    if (StringUtils.isNotEmpty(blackListSearchDto.getEntName())) {
                        expressions.add(cb.like(root.<String>get("entName"), "%" + blackListSearchDto.getEntName() + "%"));
                    }
                    if (blackListSearchDto.getSource() != null && blackListSearchDto.getSource() != EBlackListEntrySource.ALL) {
                        expressions.add(cb.equal(root.get("source"), blackListSearchDto.getSource()));
                    }
                    if (StringUtils.isNotEmpty(blackListSearchDto.getLevel())) {
                        expressions.add(cb.like(root.<String>get("level"), blackListSearchDto.getLevel()));
                    }
                    if (StringUtils.isNotEmpty(blackListSearchDto.getType())) {
                        expressions.add(cb.like(root.<String>get("type"), blackListSearchDto.getType()));
                    }
                }
                return predicate;
            }
        };

        Page<BlackListEntryPo> all = blackListEntryDao.findAll(specification, new PageRequest(Math.max(blackListSearchDto.getOffset() - 1, 0), blackListSearchDto.getLimit()));
        List<BlackListEntryDto> dtos = ConverterService.convertToList(all.getContent(), BlackListEntryDto.class);
        blackListSearchDto.setList(dtos);
        blackListSearchDto.setTotalRecord(all.getTotalElements());
        blackListSearchDto.setTotalPages(all.getTotalPages());
        return blackListSearchDto;
    }

    @Override
    public List<BlackListEntryDto> findByName(String entName) {
        List<BlackListEntryPo> all = blackListEntryDao.findByEntName(entName);
        return ConverterService.convertToList(all, BlackListEntryDto.class);

    }

    @Override
    public BlackListResultEnum findByNameMixWhite(String entName) {
        List<BlackListEntryDto> byName = findByName(entName);
        if(CollectionUtils.isNotEmpty(byName)){
            for(BlackListEntryDto blackListEntryDto : byName){
                if(blackListEntryDto!=null && blackListEntryDto.getIsWhite()!=null && blackListEntryDto.getIsWhite()){
                    return BlackListResultEnum.WHITE;
                }
            }
            return BlackListResultEnum.BLACK;
        }else{
            return BlackListResultEnum.NORMAL;
        }
    }


}
