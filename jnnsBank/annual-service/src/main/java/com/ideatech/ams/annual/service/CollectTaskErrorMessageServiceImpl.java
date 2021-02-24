package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.CollectTaskErrorMessageDao;
import com.ideatech.ams.annual.dao.CompareRuleDao;
import com.ideatech.ams.annual.dto.*;
import com.ideatech.ams.annual.entity.CollectTaskErrorMessage;
import com.ideatech.ams.annual.entity.CompareRule;
import com.ideatech.ams.annual.entity.PbcCollectAccount;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * @Description 任务采集的错误
 * @Author wanghongjie
 * @Date 2018/10/8
 **/
@Service
public class CollectTaskErrorMessageServiceImpl extends BaseServiceImpl<CollectTaskErrorMessageDao, CollectTaskErrorMessage, CollectTaskErrorMessageDto> implements CollectTaskErrorMessageService {

    @Override
    public List<CollectTaskErrorMessageDto> findByTaskIdAndAnnualTaskId(Long taskId, Long annualTaskId) {
        return ConverterService.convertToList(getBaseDao().findByTaskIdAndAnnualTaskId(taskId,annualTaskId), CollectTaskErrorMessageDto.class);
    }


    @Override
    public CollectErrorMessageSearchDto search(final CollectErrorMessageSearchDto collectErrorMessageSearchDto, final Long taskId, final Long annualTaskId) {
        Specification<CollectTaskErrorMessage> specification = new Specification<CollectTaskErrorMessage>() {
            @Override
            public Predicate toPredicate(Root<CollectTaskErrorMessage> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

//                if(collectErrorMessageSearchDto != null){
//                }

                if (null != collectErrorMessageSearchDto) {
                    if (StringUtils.isNotBlank(collectErrorMessageSearchDto.getBankName())) {
                        expressions.add(cb.like(root.<String>get("bankName"), "%" + collectErrorMessageSearchDto.getBankName() + "%"));
                    }

                    if (StringUtils.isNotBlank(collectErrorMessageSearchDto.getPbcCode())) {
                        expressions.add(cb.like(root.<String>get("pbcCode"), "%" + collectErrorMessageSearchDto.getPbcCode() + "%"));
                    }

                   /* if (collectErrorMessageSearchDto.getOrganizationId() != null) {
                        expressions.add(cb.equal(root.<Long>get("organizationId"), collectErrorMessageSearchDto.getOrganizationId()));
                    }*/

                }
//
                expressions.add(cb.equal(root.<Long>get("taskId"),taskId));
                expressions.add(cb.equal(root.<Long>get("annualTaskId"),annualTaskId));
                return predicate;
            }
        };
        Page<CollectTaskErrorMessage> all = getBaseDao().findAll(specification, new PageRequest(Math.max(collectErrorMessageSearchDto.getOffset(), 0), collectErrorMessageSearchDto.getLimit(),new Sort(new Sort.Order(Sort.Direction.ASC, "createdDate"))));
        List<CollectTaskErrorMessageDto> coreCollectionDtoList = ConverterService.convertToList(all.getContent(), CollectTaskErrorMessageDto.class);
        collectErrorMessageSearchDto.setList(coreCollectionDtoList);
        collectErrorMessageSearchDto.setTotalRecord(all.getTotalElements());
        collectErrorMessageSearchDto.setTotalPages(all.getTotalPages());
        return collectErrorMessageSearchDto;
    }
}
