package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.AnnualTaskDao;
import com.ideatech.ams.annual.dao.PbcCollectAccountDao;
import com.ideatech.ams.annual.dto.*;
import com.ideatech.ams.annual.entity.AnnualTask;
import com.ideatech.ams.annual.entity.CoreCollection;
import com.ideatech.ams.annual.entity.PbcCollectAccount;
import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Author wanghongjie
 * @Date 2018/8/13
 **/
@Service
@Transactional
@Slf4j
public class PbcCollectAccountServiceImpl extends BaseServiceImpl<PbcCollectAccountDao, PbcCollectAccount, PbcCollectAccountDto> implements  PbcCollectAccountService{

    @Override
    public List<PbcCollectAccountDto> findByCollectOrganId(Long id) {
        return ConverterService.convertToList(getBaseDao().findByCollectOrganId(id), PbcCollectAccountDto.class);
    }

    @Override
    public List<PbcCollectAccountDto> findByCollectOrganIdAndCollectStateNot(Long id, CollectState... state) {
        return ConverterService.convertToList(getBaseDao().findByCollectOrganIdAndCollectStateNotIn(id,state), PbcCollectAccountDto.class);
    }

    @Override
    public PbcCollectAccountDto findByAcctNo(String acctNo) {
       return ConverterService.convert(getBaseDao().findByAcctNo(acctNo),PbcCollectAccountDto.class);
    }

    @Override
    public List<PbcCollectAccountDto> findByAnnualTaskId(Long annualTaskId) {
        return ConverterService.convertToList(getBaseDao().findByAnnualTaskId(annualTaskId), PbcCollectAccountDto.class);
    }

    @Override
    public PbcCollectResultSearchDto search(final PbcCollectResultSearchDto pbcCollectResultSearchDto, final Long taskId) {
        Specification<PbcCollectAccount> specification = new Specification<PbcCollectAccount>() {
            @Override
            public Predicate toPredicate(Root<PbcCollectAccount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if (null != pbcCollectResultSearchDto) {
                    if (StringUtils.isNotBlank(pbcCollectResultSearchDto.getAcctNo())) {
                        expressions.add(cb.like(root.<String>get("acctNo"), "%" + pbcCollectResultSearchDto.getAcctNo() + "%"));
                    }
                    if (StringUtils.isNotBlank(pbcCollectResultSearchDto.getDepositorName())) {
                        expressions.add(cb.like(root.<String>get("depositorName"), "%" + pbcCollectResultSearchDto.getDepositorName() + "%"));
                    }

                    if(pbcCollectResultSearchDto.getCollectState() != null){
                        expressions.add(cb.equal(root.get("collectState"),pbcCollectResultSearchDto.getCollectState()));
                    }
                }

                expressions.add(cb.equal(root.<Long>get("annualTaskId"),taskId));
                return predicate;
            }
        };
        Page<PbcCollectAccount> all = getBaseDao().findAll(specification, new PageRequest(Math.max(pbcCollectResultSearchDto.getOffset(), 0), pbcCollectResultSearchDto.getLimit(),new Sort(new Sort.Order(Sort.Direction.ASC, "createdDate"))));
        List<PbcCollectAccountDto> coreCollectionDtoList = ConverterService.convertToList(all.getContent(), PbcCollectAccountDto.class);
        pbcCollectResultSearchDto.setList(coreCollectionDtoList);
        pbcCollectResultSearchDto.setTotalRecord(all.getTotalElements());
        pbcCollectResultSearchDto.setTotalPages(all.getTotalPages());
        return pbcCollectResultSearchDto;
    }
    @Override
    public void deleteAll() {
        getBaseDao().deleteAll();
    }

    @Override
    public Long count() {
        return getBaseDao().count();
    }

    @Override
    public long countByCollectStateNot(CollectState... state) {
        return getBaseDao().countByCollectStateNotIn(state);
    }
}
