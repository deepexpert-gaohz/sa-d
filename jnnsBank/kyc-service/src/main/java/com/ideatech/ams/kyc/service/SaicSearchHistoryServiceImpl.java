package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dao.SaicSearchHistoryDao;
import com.ideatech.ams.kyc.dto.SaicSearchHistoryDto;
import com.ideatech.ams.kyc.dto.SaicSearchHistorySearchDto;
import com.ideatech.ams.kyc.entity.SaicSearchHistory;
import com.ideatech.ams.kyc.enums.SearchStatus;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.util.DateUtils;
import com.ideatech.common.converter.ConverterService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SaicSearchHistoryServiceImpl implements SaicSearchHistoryService {

    @Autowired
    private SaicSearchHistoryDao saicSearchHistoryDao;

    @Override
    public void save(String name, String username, String url, SearchStatus searchStatus, SearchType searchType, String orgfullid) {
        //保存数据查询历史信息
        SaicSearchHistory saicSearchHistory = new SaicSearchHistory();
//        saicSearchHistory.setId(Calendar.getInstance().getTimeInMillis());
        saicSearchHistory.setCreatedDate(new Date());
        saicSearchHistory.setEntname(name);
        saicSearchHistory.setQuerydate(DateUtils.getCurrentDate());
        saicSearchHistory.setUsername(username);

        saicSearchHistory.setQueryresult(searchStatus.name());
        saicSearchHistory.setSearchtype(searchType.name());
        saicSearchHistory.setSearchurl(url);
        // 增加了查询人所在的组织机构的记录
        saicSearchHistory.setOrgfullid(orgfullid);

        saicSearchHistoryDao.save(saicSearchHistory);
    }

    @Override
    public SaicSearchHistorySearchDto search(final SaicSearchHistorySearchDto saicSearchHistorySearchDto) {
        Specification<SaicSearchHistory> specification = new Specification<SaicSearchHistory>() {
            @Override
            public Predicate toPredicate(Root<SaicSearchHistory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if (null != saicSearchHistorySearchDto) {
                    if (StringUtils.isNotBlank(saicSearchHistorySearchDto.getKey())) {
                        Predicate usernamePredicate = cb.like(root.<String>get("username"), "%" + saicSearchHistorySearchDto.getKey() + "%");
                        Predicate cnamePredicate = cb.like(root.<String>get("cname"), "%" + saicSearchHistorySearchDto.getKey() + "%");
                        expressions.add(cb.or(usernamePredicate, cnamePredicate));
                    }
                    if (saicSearchHistorySearchDto.getOrgId() != null) {
                        expressions.add(cb.equal(root.get("orgId"), saicSearchHistorySearchDto.getOrgId()));
                    }
                }

                return predicate;
            }
        };
        Page<SaicSearchHistory> all = saicSearchHistoryDao.findAll(specification, new PageRequest(Math.max(saicSearchHistorySearchDto.getOffset(), 0), saicSearchHistorySearchDto.getLimit(),new Sort(new Sort.Order(Sort.Direction.ASC, "querydate"))));
        List<SaicSearchHistoryDto> saicSearchHistoryDtos = ConverterService.convertToList(all.getContent(), SaicSearchHistoryDto.class);
        saicSearchHistorySearchDto.setList(saicSearchHistoryDtos);
        saicSearchHistorySearchDto.setTotalRecord(all.getTotalElements());
        saicSearchHistorySearchDto.setTotalPages(all.getTotalPages());
        return saicSearchHistorySearchDto;
    }
}
