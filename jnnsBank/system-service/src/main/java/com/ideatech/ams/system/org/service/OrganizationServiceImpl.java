package com.ideatech.ams.system.org.service;

import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.dto.OrganRegisterDto;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.dto.OrganizationSearchDto;
import com.ideatech.ams.system.org.dto.OrganizationSyncDto;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.system.org.enums.SyncType;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.common.annotation.InterfaceLog;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.BeanValueUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author liangding
 * @create 2018-04-26 上午11:32
 **/
@Service
@Slf4j
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    @Value("${apply.task.syncScheduletiming.flag}")
    private boolean syncFlag;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private OrganizationSyncService organizationSyncService;

    @Autowired
    private OrganRegisterService organRegisterService;

    @Override
    public OrganizationDto save(OrganizationDto organizationDto) {
        boolean insertFlag = true;
        String originalPbcCode = null;
        OrganizationPo organizationPo = new OrganizationPo();
        if (organizationDto.getId() != null) {
            organizationPo = organizationDao.findOne(organizationDto.getId());
            if(null == organizationPo){
                organizationPo = new OrganizationPo();
            }else {
            	organizationDto.setFullId(organizationPo.getFullId());
            	if(!StringUtils.equals(organizationDto.getPbcCode(),organizationPo.getPbcCode())){
                    originalPbcCode = organizationPo.getPbcCode();
                }
                insertFlag = false;
            }
        }
        ConverterService.convert(organizationDto, organizationPo);
        organizationDao.save(organizationPo);

        //修改取消核准表pbcCode
        OrganRegisterDto organRegisterDto = organRegisterService.findByOrganId(organizationDto.getId());
        if(organRegisterDto != null){
            organRegisterDto.setPbcCode(organizationDto.getPbcCode());
            organRegisterService.save(organRegisterDto);
        }

        OrganizationPo organizationParentPo =organizationDao.findOne(organizationPo.getParentId());
        if(organizationParentPo!= null && organizationParentPo.getFullId() != null) {
            organizationPo.setFullId(organizationParentPo.getFullId()+"-"+organizationPo.getId());
            organizationDao.save(organizationPo);
        }
        OrganizationDto organizationDtoNew = ConverterService.convert(organizationPo, OrganizationDto.class);
        if(syncFlag){
            OrganizationSyncDto organizationSyncDto;
            if(insertFlag){//新增
                organizationSyncDto = organizationSyncService.convertOrganizationSyncDto(organizationDtoNew, SyncType.INSERT,originalPbcCode);
            }else{
                organizationSyncDto = organizationSyncService.convertOrganizationSyncDto(organizationDtoNew, SyncType.UPDATE,originalPbcCode);
            }
            organizationSyncDto.setId(null);
            organizationSyncService.save(organizationSyncDto);
        }
        return organizationDtoNew;
    }

    @Override
    public OrganizationDto findById(Long id) {
        OrganizationPo organizationPo = organizationDao.findOne(id);
        OrganizationDto organizationDto = new OrganizationDto();
        BeanCopierUtils.copyProperties(organizationPo, organizationDto);
        return organizationDto;
    }

    @Override
    public OrganizationSearchDto search(final OrganizationSearchDto organizationSearchDto) {
        Specification<OrganizationPo> specification = new Specification<OrganizationPo>() {
            @Override
            public Predicate toPredicate(Root<OrganizationPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if (organizationSearchDto != null) {
                    if (StringUtils.isNotBlank(organizationSearchDto.getName())) {
                        expressions.add(cb.like(root.<String>get("name"), "%" + organizationSearchDto.getName() + "%"));
                    }
                }

                expressions.add(cb.equal(root.get("deleted"), Boolean.FALSE));

                return predicate;
            }
        };

        Page<OrganizationPo> all = organizationDao.findAll(specification, new PageRequest(organizationSearchDto.getOffset() - 1, organizationSearchDto.getLimit()));
        organizationSearchDto.setList(ConverterService.convertToList(all.getContent(), OrganizationDto.class));
        organizationSearchDto.setTotalRecord(all.getTotalElements());
        organizationSearchDto.setTotalPages(all.getTotalPages());
        return organizationSearchDto;
    }

    @Override
    public List<OrganizationDto> searchChild(Long parentId, String name) {
        List<OrganizationPo> organizationPos = null;
        if(StringUtils.isNotBlank(name)) {
            OrganizationPo parentOrg = organizationDao.findOne(parentId);
            organizationPos = organizationDao.findByFullIdLikeAndNameLikeOrderByLastUpdateDateAsc(parentOrg.getFullId()+"-%", "%" + name + "%");
        } else {
            organizationPos = organizationDao.findByParentIdOrderByLastUpdateDateAsc(parentId);
        }
        return ConverterService.convertToList(organizationPos, OrganizationDto.class);
    }

    @Override
    public List<OrganizationDto> searchChildByNameAndCode(Long parentId, String name, String code) {
        List<OrganizationPo> organizationPos = null;
        if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(code)) {
            OrganizationPo parentOrg = organizationDao.findOne(parentId);
            organizationPos = organizationDao.findByFullIdLikeAndNameLikeAndCodeLikeOrderByLastUpdateDateAsc(parentOrg.getFullId()+"%", "%" + name + "%", "%" + code + "%");
        } else if(StringUtils.isNotBlank(name)) {
            OrganizationPo parentOrg = organizationDao.findOne(parentId);
            organizationPos = organizationDao.findByFullIdLikeAndNameLikeOrderByLastUpdateDateAsc(parentOrg.getFullId()+"%", "%" + name + "%");
        } else if(StringUtils.isNotBlank(code)) {
            OrganizationPo parentOrg = organizationDao.findOne(parentId);
            organizationPos = organizationDao.findByFullIdLikeAndCodeLikeOrderByLastUpdateDateAsc(parentOrg.getFullId()+"%", "%" + code + "%");
        }

        return ConverterService.convertToList(organizationPos, OrganizationDto.class);
    }

    @Override
    public List<OrganizationDto> listDescendant(Long orgId) {
        OrganizationPo parentOrg = organizationDao.findOne(orgId);
        List<OrganizationPo> byFullIdLike = organizationDao.findByFullIdLike(parentOrg.getFullId() + "%");
        return ConverterService.convertToList(byFullIdLike, OrganizationDto.class);
    }

    @Override
    public void delete(Long id) {
        if(syncFlag){
            OrganizationDto byId = findById(id);
            OrganizationSyncDto organizationSyncDto = organizationSyncService.convertOrganizationSyncDto(byId, SyncType.DELETE,null);
            organizationSyncDto.setId(null);
            organizationSyncService.save(organizationSyncDto);
        }
        organizationDao.delete(id);
        organRegisterService.delete(id);
    }

    @Override
    public void save(List<OrganizationDto> organizationDtos) {
        List<OrganizationPo> organizationPos = ConverterService.convertToList(organizationDtos, OrganizationPo.class);
        for (OrganizationPo organizationPo : organizationPos) {
            organizationDao.save(organizationPo);
        }
    }

    @Override
    public OrganizationDto findByCode(String code) {
        OrganizationPo organizationPo = organizationDao.findByCode(code);
        return ConverterService.convert(organizationPo, OrganizationDto.class);
    }

    @Override
    public OrganizationDto findByOrganFullId(String organFullId) {
        OrganizationPo organizationPo = organizationDao.findByFullId(organFullId);
        OrganizationDto organizationDto = new OrganizationDto();
        BeanCopierUtils.copyProperties(organizationPo, organizationDto);
        return organizationDto;
    }

    @Override
    public List<OrganizationDto> findByOrganFullIdStartsWidth(String organFullId) {
        List<OrganizationDto>  odList = new ArrayList<>();
        List<OrganizationPo> opList = organizationDao.findByFullIdLike(organFullId + "%");
        for (OrganizationPo organizationPo : opList){
            OrganizationDto organizationDto = new OrganizationDto();
            BeanCopierUtils.copyProperties(organizationPo, organizationDto);
            odList.add(organizationDto);
        }
        return odList;
    }

    @Override
    public List<OrganizationDto> findByNameLikeAndOrganFullIdStartsWidth(String name, String organFullId) {
        List<OrganizationDto>  odList = new ArrayList<>();
        List<OrganizationPo> opList = organizationDao.findByNameLikeAndFullIdLike("%" + name + "%", organFullId + "%");
        for (OrganizationPo organizationPo : opList){
            OrganizationDto organizationDto = new OrganizationDto();
            BeanCopierUtils.copyProperties(organizationPo, organizationDto);
            odList.add(organizationDto);
        }
        return odList;
    }

    @Override
    public List<OrganizationDto> findByPbcCodeLike(String pbcCode) {
        List<OrganizationPo> organizationPo = organizationDao.findByPbcCodeLike("%" + pbcCode + "%");
        List<OrganizationDto> odList = new ArrayList<>();
        for (OrganizationPo op : organizationPo) {
            OrganizationDto organizationDto = new OrganizationDto();
            BeanCopierUtils.copyProperties(op, organizationDto);
            odList.add(organizationDto);
        }
        return odList;
    }

    @Override
    public OrganizationDto findTopPbcCode(String pbcCode) {
        OrganizationDto dto = null;
        List<OrganizationPo> list = organizationDao.findByPbcCodeOrderByFullIdAsc(pbcCode);
        if(CollectionUtils.isNotEmpty(list)) {
            dto = new OrganizationDto();
            BeanValueUtils.copyProperties(list.get(0), dto);
        }
        return dto;
    }

    @Override
    public List<OrganizationDto> listAll() {
        List<OrganizationPo> all = organizationDao.findAll();
        return ConverterService.convertToList(all, OrganizationDto.class);
    }

    @Override
    public List<OrganizationDto> getAllLeafByCache() {
        List<OrganizationDto> organInfoList = new ArrayList<OrganizationDto>();
        List<OrganizationPo> listOrgain = organizationDao.findAll();
        OrganizationDto organInfo = null;
        for (OrganizationPo organ : listOrgain) {
            //机构有人行2级账号的加入采集机构列表
            PbcAccountDto pbcAccountDto = pbcAccountService.getAnnualPbcAccountByOrganId(organ.getId(), EAccountType.AMS);
            if(pbcAccountDto !=null){
                organInfo = new OrganizationDto();
                BeanUtils.copyProperties(organ, organInfo);
                organInfoList.add(organInfo);
            }
        }
        return organInfoList;
    }

    @Override
    public Set<String> findCodeAllInSet() {
        return organizationDao.findCodeAllInSet();
    }

    @Override
    public Map<String, OrganizationDto> findAllInMap() {
        List<OrganizationPo> all = organizationDao.findAll();
        HashMap<String, OrganizationDto> map = new HashMap<>();
        for(OrganizationPo organizationPo : all){
            if(organizationPo !=null){
                OrganizationDto organizationDto = new OrganizationDto();
                BeanUtils.copyProperties(organizationPo,organizationDto);
                map.put(organizationDto.getFullId(),organizationDto);
            }
        }
        return map;
    }

    @Override
    public OrganizationDto findByName(String name) {
        OrganizationPo organizationPo = organizationDao.findByName(name);
        return ConverterService.convert(organizationPo, OrganizationDto.class);
    }

    @Override
    public List<OrganizationDto> findByNameLike(String name) {
        List<OrganizationPo> opList = organizationDao.findByNameLike("%" + name + "%");
        List<OrganizationDto> odList = new ArrayList<>();
        for (OrganizationPo op : opList) {
            OrganizationDto od = new OrganizationDto();
            BeanUtils.copyProperties(op, od);
            odList.add(od);
        }
        return odList;
    }

    @Override
    public List<OrganizationDto> findByCodeLikeAndNameLike(String code, String name) {
        List<OrganizationPo> opList = new ArrayList<>();
        if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(name)) {
            opList = organizationDao.findByCodeLikeAndNameLike("%" + code + "%", "%" + name + "%");
        } else if (StringUtils.isNotBlank(code) && StringUtils.isBlank(name)) {
            opList = organizationDao.findByCodeLike("%" + code + "%");
        } else if (StringUtils.isBlank(code) && StringUtils.isNotBlank(name)) {
            opList = organizationDao.findByNameLike("%" + name + "%");
        }

        List<OrganizationDto> odList = new ArrayList<>();
        for (OrganizationPo op : opList) {
            OrganizationDto od = new OrganizationDto();
            BeanUtils.copyProperties(op, od);
            odList.add(od);
        }
        return odList;
    }

    @Override
    public List<OrganizationDto> findByOrganFullIdLike(String organfullid) {
        return ConverterService.convertToList(organizationDao.findByFullIdLike(organfullid),OrganizationDto.class);
    }

    @Override
    public ResultDto cancelHeZhun(String[] ids) {
        ResultDto resultDto = new ResultDto();
        if(ids != null){
            for(String orgId : ids){
                Long organId = Long.parseLong(orgId);
                OrganizationDto organizationDto = findById(organId);
                List<OrganizationDto> organizationDtos = findByOrganFullIdLike(organizationDto.getFullId());
                for (OrganizationDto organizationDto1 : organizationDtos){
                    OrganRegisterDto organRegisterDto = organRegisterService.findByOrganId(organizationDto1.getId());
                    if(organRegisterDto != null){
                        continue;
                    }else {
                        organRegisterDto = new OrganRegisterDto();
                    }
                    if(organizationDto != null){
                        organRegisterDto.setPbcCode(organizationDto.getPbcCode());
                        organRegisterDto.setOrganId(organId);
                        organRegisterDto.setName(organizationDto.getName());
                        organRegisterDto.setFullId(organizationDto.getFullId());
                        organRegisterService.save(organRegisterDto);
                    }
                }
            }
            resultDto.setCode(ResultCode.ACK);
            resultDto.setMessage("保存成功");
        }
        return resultDto;
    }

    @Override
    public ResultDto cancelHeZhunDel(String[] ids) {
        ResultDto resultDto = new ResultDto();
        if(ids != null){
            for(String orgId : ids){
                Long organId = Long.parseLong(orgId);
                OrganRegisterDto organRegisterDto = organRegisterService.findByIdAndOrgFullIdLike(organId,SecurityUtils.getCurrentOrgFullId());
                if(organRegisterDto != null){
                    organRegisterService.del(Long.parseLong(orgId));
                    resultDto.setCode(ResultCode.ACK);
                }else{
                    resultDto.setCode(ResultCode.NACK);
                }
            }
        }
        return resultDto;
    }

    @Override
    public OrganizationDto findByCodeAndIdNot(String code, Long id) {
        OrganizationPo organizationPo = organizationDao.findByCodeAndIdNot(code, id);
        return ConverterService.convert(organizationPo, OrganizationDto.class);
    }

    @Override
    public OrganizationDto findByInstitutionCodeAndIdNot(String institutionCode, Long id) {
        OrganizationPo organ = organizationDao.findByInstitutionCodeAndIdNot(institutionCode, id);
        return ConverterService.convert(organ, OrganizationDto.class);
    }

    @Override
    public OrganizationDto findByInstitutionCode(String institutionCode) {
        OrganizationPo organ = organizationDao.findByInstitutionCode(institutionCode);
        return ConverterService.convert(organ, OrganizationDto.class);
    }

    @Override
    public OrganizationDto findByParentIdLike(String parentId) {
        List<OrganizationPo> byFullIdLike = organizationDao.findByFullIdLike(parentId);
        if (byFullIdLike!=null){
            return  ConverterService.convert(byFullIdLike.get(0), OrganizationDto.class);
        }
        return null;
    }

    @Override
    public List<OrganizationDto> findAllByFullId(String fullId) {
        return ConverterService.convertToList(organizationDao.findAllByFullId(fullId), OrganizationDto.class);
    }

}
