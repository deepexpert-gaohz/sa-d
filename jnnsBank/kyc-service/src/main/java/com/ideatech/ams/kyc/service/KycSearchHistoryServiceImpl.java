package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dao.KycSearchHistoryDao;
import com.ideatech.ams.kyc.dto.KycSearchHistoryDto;
import com.ideatech.ams.kyc.dto.KycSearchHistorySearchDto;
import com.ideatech.ams.kyc.dto.poi.SaicHistoryPoi;
import com.ideatech.ams.kyc.entity.KycSearchHistory;
import com.ideatech.ams.kyc.service.poi.SaicHistoryPoiExport;
import com.ideatech.ams.kyc.util.DateUtils;
import com.ideatech.ams.system.operateLog.dto.OperateLogDto;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.util.BeanCopierUtils;
import org.apache.commons.collections.CollectionUtils;
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

@Service
@Transactional
public class KycSearchHistoryServiceImpl implements KycSearchHistoryService {

    @Autowired
    private KycSearchHistoryDao kycSearchHistoryDao;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public void save(KycSearchHistoryDto kycSearchHistoryDto) {
        KycSearchHistory kycSearchHistory = new KycSearchHistory();
        BeanCopierUtils.copyProperties(kycSearchHistoryDto, kycSearchHistory);
        kycSearchHistory.setQuerydate(DateUtils.getCurrentDate());
        //保存数据查询历史信息
        kycSearchHistoryDao.save(kycSearchHistory);
    }

    @Override
    public KycSearchHistorySearchDto search(final KycSearchHistorySearchDto kycSearchHistorySearchDto) {
        Specification<KycSearchHistory> specification = new Specification<KycSearchHistory>() {
            @Override
            public Predicate toPredicate(Root<KycSearchHistory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if (null != kycSearchHistorySearchDto) {
                    if (StringUtils.isNotBlank(kycSearchHistorySearchDto.getKey())) {
                        Predicate usernamePredicate = cb.like(root.<String>get("username"), "%" + kycSearchHistorySearchDto.getKey() + "%");
                        Predicate cnamePredicate = cb.like(root.<String>get("entname"), "%" + kycSearchHistorySearchDto.getKey() + "%");
                        expressions.add(cb.or(usernamePredicate, cnamePredicate));
                    }
                    if (StringUtils.isNotEmpty(kycSearchHistorySearchDto.getOrgFullId())) {
                        expressions.add(cb.like(root.<String>get("orgfullid"), kycSearchHistorySearchDto.getOrgFullId() + "%"));
                    }
                    if(kycSearchHistorySearchDto.getBeginDate()!=null){
                        expressions.add(cb.greaterThanOrEqualTo(root.<String>get("querydate"),kycSearchHistorySearchDto.getBeginDate()+" 00:00:00"));
                    }
                    if(kycSearchHistorySearchDto.getEndDate()!=null)
                    expressions.add(cb.lessThanOrEqualTo(root.<String>get("querydate"),kycSearchHistorySearchDto.getEndDate()+" 23:59:59"));
                    if (StringUtils.isNotBlank(kycSearchHistorySearchDto.getFlag())) {
                        if (kycSearchHistorySearchDto.getFlag().equals("1")) {
                            expressions.add(cb.isNotNull(root.get("saicinfoId")));
                        } else if (kycSearchHistorySearchDto.getFlag().equals("0")) {
                            expressions.add(cb.isNull(root.get("saicinfoId")));
                        }
                    }

                    if (StringUtils.isNotEmpty(kycSearchHistorySearchDto.getBatchNo())) {
                        expressions.add(cb.equal(root.<String>get("batchNo"), kycSearchHistorySearchDto.getBatchNo()));
                    }
                }

                return predicate;
            }
        };
//        Sort.Direction direction, String property
        String property="querydate";
        if(kycSearchHistorySearchDto.getColumn()!=null){
            property=kycSearchHistorySearchDto.getColumn();
        }
        Sort.Direction direction = Sort.Direction.DESC;
        if("asc".equals(kycSearchHistorySearchDto.getOrderStr())){
            direction = Sort.Direction.ASC;
        }
        Page<KycSearchHistory> all = kycSearchHistoryDao.findAll(specification, new PageRequest(Math.max(kycSearchHistorySearchDto.getOffset(), 0), kycSearchHistorySearchDto.getLimit(),new Sort(new Sort.Order(direction, property))));
        List<KycSearchHistoryDto> kycSearchHistoryDtos = ConverterService.convertToList(all.getContent(), KycSearchHistoryDto.class);
        kycSearchHistorySearchDto.setList(kycSearchHistoryDtos);
        kycSearchHistorySearchDto.setTotalRecord(all.getTotalElements());
        kycSearchHistorySearchDto.setTotalPages(all.getTotalPages());
        return kycSearchHistorySearchDto;
    }

    @Override
    public IExcelExport searchAll(final KycSearchHistorySearchDto kycSearchHistorySearchDto) {
        IExcelExport iExcelExport = new SaicHistoryPoiExport();
        List<SaicHistoryPoi> saicHistoryPois = new ArrayList<>();

        Specification<KycSearchHistory> specification = new Specification<KycSearchHistory>() {
            @Override
            public Predicate toPredicate(Root<KycSearchHistory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if (null != kycSearchHistorySearchDto) {
                    if (StringUtils.isNotBlank(kycSearchHistorySearchDto.getKey())) {
                        Predicate usernamePredicate = cb.like(root.<String>get("username"), "%" + kycSearchHistorySearchDto.getKey() + "%");
                        Predicate cnamePredicate = cb.like(root.<String>get("entname"), "%" + kycSearchHistorySearchDto.getKey() + "%");
                        expressions.add(cb.or(usernamePredicate, cnamePredicate));
                    }
                    if (StringUtils.isNotEmpty(kycSearchHistorySearchDto.getOrgFullId())) {
                        expressions.add(cb.like(root.<String>get("orgfullid"), kycSearchHistorySearchDto.getOrgFullId() + "%"));
                    }
                    if(kycSearchHistorySearchDto.getBeginDate()!=null){
                        expressions.add(cb.greaterThanOrEqualTo(root.<String>get("querydate"),kycSearchHistorySearchDto.getBeginDate()+" 00:00:00"));
                    }
                    if(kycSearchHistorySearchDto.getEndDate()!=null)
                        expressions.add(cb.lessThanOrEqualTo(root.<String>get("querydate"),kycSearchHistorySearchDto.getEndDate()+" 23:59:59"));
                    if (StringUtils.isNotBlank(kycSearchHistorySearchDto.getFlag())) {
                        if (kycSearchHistorySearchDto.getFlag().equals("1")) {
                            expressions.add(cb.isNotNull(root.get("saicinfoId")));
                        } else if (kycSearchHistorySearchDto.getFlag().equals("0")) {
                            expressions.add(cb.isNull(root.get("saicinfoId")));
                        }
                    }

                    if (StringUtils.isNotEmpty(kycSearchHistorySearchDto.getBatchNo())) {
                        expressions.add(cb.equal(root.<String>get("batchNo"), kycSearchHistorySearchDto.getBatchNo()));
                    }
                }

                return predicate;
            }
        };
        String property="querydate";
        if(kycSearchHistorySearchDto.getColumn()!=null){
            property=kycSearchHistorySearchDto.getColumn();
        }
        Sort.Direction direction = Sort.Direction.DESC;
        if("asc".equals(kycSearchHistorySearchDto.getOrderStr())){
            direction = Sort.Direction.ASC;
        }
        List<KycSearchHistoryDto> kycSearchHistoryDtos = ConverterService.convertToList(kycSearchHistoryDao.findAll(specification), KycSearchHistoryDto.class);
        SaicHistoryPoi saicHistoryPoi = null;
        for(KycSearchHistoryDto kyc : kycSearchHistoryDtos){
            if(StringUtils.isNotBlank(kyc.getUsername())){
                UserDto byUsername = userService.findByUsername(kyc.getUsername());
                if(byUsername != null){
                    kyc.setCname(byUsername.getCname());
                }else{
                    kyc.setCname(kyc.getUsername());
                }
            }
            if(StringUtils.isNotBlank(kyc.getOrgfullid())){
                OrganizationDto byOrganFullId = organizationService.findByOrganFullId(kyc.getOrgfullid());
                if(byOrganFullId != null){
                    kyc.setOrgfullname(byOrganFullId.getName());
                }else{
                    kyc.setOrgfullname(kyc.getOrgfullid());
                }
            }
            saicHistoryPoi = new SaicHistoryPoi();
            BeanUtils.copyProperties(kyc,saicHistoryPoi);
            saicHistoryPois.add(saicHistoryPoi);
        }
        iExcelExport.setPoiList(saicHistoryPois);
        return iExcelExport;
    }

    @Override
    public List<KycSearchHistoryDto> findByEntName(String entname) {
        List<KycSearchHistoryDto> list = new ArrayList<>();
        List<KycSearchHistory> data = kycSearchHistoryDao.findByEntnameOrderByQuerydateDesc(entname);
        if(CollectionUtils.isNotEmpty(data)){
            list = ConverterService.convertToList(data, KycSearchHistoryDto.class);
        }
        return list;
    }
}
