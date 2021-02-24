package com.ideatech.ams.system.org.service;

import com.ideatech.ams.system.org.dao.OrganizationSyncDao;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.dto.OrganizationSearchDto;
import com.ideatech.ams.system.org.dto.OrganizationSyncDto;
import com.ideatech.ams.system.org.dto.OrganizationSyncSearchDto;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.system.org.entity.OrganizationSyncPo;
import com.ideatech.ams.system.org.enums.SyncType;
import com.ideatech.common.converter.ConverterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * @Description 机构同步表
 * @Author wanghongjie
 * @Date 2019/2/22
 **/
@Service
@Slf4j
@Transactional
public class OrganizationSyncServiceImpl implements OrganizationSyncService{

    @Autowired
    private OrganizationSyncDao organizationSyncDao;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public void save(OrganizationSyncDto organizationSyncDto) {
        OrganizationSyncPo organizationSyncPo = null;
        if(organizationSyncDto != null && organizationSyncDto.getId() != null){
            organizationSyncPo = organizationSyncDao.findOne(organizationSyncDto.getId());
            if(organizationSyncPo == null){
                organizationSyncPo = ConverterService.convert(organizationSyncDto, OrganizationSyncPo.class);
            }else{
                BeanUtils.copyProperties(organizationSyncDto,organizationSyncPo);
            }
        }else{
            organizationSyncPo = ConverterService.convert(organizationSyncDto, OrganizationSyncPo.class);
        }
        organizationSyncDao.save(organizationSyncPo);
    }

    @Override
    public OrganizationSyncDto convertOrganizationSyncDto(OrganizationDto organizationDto, SyncType syncType,String originalPbcCode){
        OrganizationSyncDto organizationSyncDto = new OrganizationSyncDto();
        BeanUtils.copyProperties(organizationDto,organizationSyncDto);
        organizationSyncDto.setSyncType(syncType);
        if(syncType == SyncType.INSERT){//新增
            if(organizationDto.getId() != null){
                OrganizationDto parent = organizationService.findById(organizationDto.getParentId());
                organizationSyncDto.setAfterFullId(parent.getFullId());
            }
        }else if(syncType == SyncType.UPDATE){//更新
            organizationSyncDto.setBeforeFullId(organizationDto.getFullId());
            organizationSyncDto.setAfterFullId(organizationDto.getFullId());
            if(StringUtils.isNotBlank(originalPbcCode)){
                organizationSyncDto.setOriginalPbcCode(originalPbcCode);
            }
        }else if(syncType == SyncType.DELETE){
            if(organizationDto.getId() != null){
                OrganizationDto parent = organizationService.findById(organizationDto.getParentId());
                organizationSyncDto.setAfterFullId(parent.getFullId());
            }
        }
        return organizationSyncDto;
    }

    @Override
    public List<OrganizationSyncDto> findBySyncFinishStatusOrderByCreatedDateAsc(Boolean syncStatus) {
        return ConverterService.convertToList(organizationSyncDao.findBySyncFinishStatusOrderByCreatedDateAsc(syncStatus),OrganizationSyncDto.class);
    }

    @Override
    public OrganizationSyncSearchDto search(final OrganizationSyncSearchDto organizationSyncSearchDto) {
        Page<OrganizationSyncPo> all = organizationSyncDao.findAll(new PageRequest(Math.max(organizationSyncSearchDto.getOffset()-1, 0), organizationSyncSearchDto.getLimit(),new Sort(Sort.Direction.DESC, "createdDate")));
        organizationSyncSearchDto.setList(ConverterService.convertToList(all.getContent(), OrganizationSyncDto.class));
        organizationSyncSearchDto.setTotalRecord(all.getTotalElements());
        organizationSyncSearchDto.setTotalPages(all.getTotalPages());
        return organizationSyncSearchDto;
    }

    @Override
    public OrganizationSyncDto findById(Long id) {
        OrganizationSyncPo one = organizationSyncDao.findOne(id);
        return ConverterService.convert(one,OrganizationSyncDto.class);
    }
}
