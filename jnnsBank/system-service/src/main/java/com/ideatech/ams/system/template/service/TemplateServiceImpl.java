package com.ideatech.ams.system.template.service;

import com.ideatech.ams.system.template.dao.TemplateDao;
import com.ideatech.ams.system.template.dao.spec.TemplatePoSpec;
import com.ideatech.ams.system.template.dto.TemplateDto;
import com.ideatech.ams.system.template.dto.TemplateSearchDto;
import com.ideatech.ams.system.template.entity.TemplatePo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.CompanyAcctType;
import com.ideatech.common.enums.DepositorType;
import com.ideatech.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liangding
 * @create 2018-07-10 下午7:48
 **/
@Service
@Slf4j
public class TemplateServiceImpl extends BaseServiceImpl<TemplateDao, TemplatePo, TemplateDto> implements TemplateService {
    @Override
    public TemplateSearchDto search(final TemplateSearchDto searchDto) {
        Specification<TemplatePo> specification = new Specification<TemplatePo>() {
            @Override
            public Predicate toPredicate(Root<TemplatePo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if (searchDto != null) {
                    if (searchDto.getBillType() != null && searchDto.getBillType() != BillType.ALL) {
                        expressions.add(cb.equal(root.get("billType"), searchDto.getBillType()));
                    }

                    if (searchDto.getDepositorType() != null && searchDto.getDepositorType() != DepositorType.ALL) {
                        expressions.add(cb.equal(root.get("depositorType"), searchDto.getDepositorType()));
                    }
                    if (searchDto.getAcctType() != null) {
                        expressions.add(cb.equal(root.get("acctType"), searchDto.getAcctType()));
                    }
                    if (StringUtils.isNotEmpty(searchDto.getTemplateName())) {
                        expressions.add(cb.like(root.<String>get("templateName"), "%" + searchDto.getTemplateName() + "%"));
                    }
                }
                return predicate;
            }
        };

        Page<TemplatePo> all = getBaseDao().findAll(specification, new PageRequest(Math.max(searchDto.getOffset() - 1, 0), searchDto.getLimit(),
                new Sort(Sort.Direction.DESC, "lastUpdateDate")));
        List<TemplateDto> dtos = ConverterService.convertToList(all.getContent(), TemplateDto.class);
        searchDto.setList(dtos);
        searchDto.setTotalRecord(all.getTotalElements());
        searchDto.setTotalPages(all.getTotalPages());
        return searchDto;
    }

    @Override
    public List<String> listTemplateName(BillType billType, DepositorType depositorType) {
        List<String> templateNames = getBaseDao().listTemplateNameByBillTypeAndDepositorType(billType, depositorType);
        if (CollectionUtils.isEmpty(templateNames)) {
            // 如果指定的单据类型和存款人类别下没有配置打印内容，则从存款人类别=ALL下查询打印内容
            templateNames = getBaseDao().listTemplateNameByBillTypeAndDepositorType(billType, DepositorType.ALL);
        }
        return templateNames;
    }

    @Override
    public List<String> listTemplate(BillType billType, DepositorType depositorType, CompanyAcctType acctType) {
        TemplateSearchDto dto = new TemplateSearchDto();
        List<String> result = new ArrayList<>();
        dto.setAcctType(acctType);
        dto.setBillType(billType);
        dto.setDepositorType(DepositorType.ALL);
        List<TemplatePo> data = getBaseDao().findAll(new TemplatePoSpec(dto));
        for (TemplatePo temp:data) {
            result.add(temp.getTemplateName());
        }
        if(CollectionUtils.isEmpty(result)){
            result = getBaseDao().listTemplateNameByBillTypeAndDepositorType(billType, DepositorType.ALL);
        }
        return result;
    }

    @Override
    public TemplateDto findByBillTypeAndDepositorTypeAndTemplateName(BillType billType, DepositorType depositorType, String templateName) {
        List<TemplatePo> byBillTypeAndDepositorTypeAndTemplateName = getBaseDao().findByBillTypeAndDepositorTypeAndTemplateName(billType, depositorType, templateName);
        if (CollectionUtils.isEmpty(byBillTypeAndDepositorTypeAndTemplateName)) {
            byBillTypeAndDepositorTypeAndTemplateName = getBaseDao().findByBillTypeAndDepositorTypeAndTemplateName(billType, DepositorType.ALL, templateName);
        }
        if (CollectionUtils.isNotEmpty(byBillTypeAndDepositorTypeAndTemplateName)) {
            return ConverterService.convert(byBillTypeAndDepositorTypeAndTemplateName.get(0), TemplateDto.class);
        }
        return null;
    }

    @Override
    public TemplateDto findByBillTypeAndDepositorType(BillType billType, DepositorType depositorType) {
        List<TemplatePo> byBillTypeAndDepositorType = getBaseDao().findByBillTypeAndDepositorType(billType, depositorType);

        if (CollectionUtils.isNotEmpty(byBillTypeAndDepositorType)) {
            return ConverterService.convert(byBillTypeAndDepositorType.get(0), TemplateDto.class);
        }
        return null;
    }

    @Override
    public TemplateDto findByTemplateName(String templateName) {

        List<TemplatePo> templatePoList = getBaseDao().findByTemplateName(templateName);
        if (CollectionUtils.isNotEmpty(templatePoList)) {
            return ConverterService.convert(templatePoList.get(0), TemplateDto.class);
        }
        return null;
    }
}
