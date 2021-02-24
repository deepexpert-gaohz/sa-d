package com.ideatech.ams.system.org.service;

import com.ideatech.ams.system.org.dao.OrganRegisterDao;
import com.ideatech.ams.system.org.dto.OrganRegisterDto;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.entity.OrganRegisterPo;
import com.ideatech.ams.system.org.spec.OrganRegisterSearchSpec;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrganRegisterServiceImpl extends BaseServiceImpl<OrganRegisterDao, OrganRegisterPo,OrganRegisterDto> implements OrganRegisterService{

    @Autowired
    private OrganRegisterDao organRegisterDao;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public OrganRegisterDto query(String pbcCode) {
        List<OrganRegisterDto> list = ConverterService.convertToList(organRegisterDao.findByPbcCode(pbcCode),OrganRegisterDto.class);
        if(CollectionUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    @Override
    public OrganRegisterDto queryByOrganCode(String organCode) {
        OrganizationDto organizationDto = organizationService.findByCode(organCode);
        if(organizationDto != null){
            return ConverterService.convert(organRegisterDao.findByOrganId(organizationDto.getId()),OrganRegisterDto.class);
        }
        return null;
    }

    @Override
    public OrganRegisterDto findByOrganId(Long orgId) {
        OrganRegisterDto organRegisterDto = ConverterService.convert(organRegisterDao.findByOrganId(orgId),OrganRegisterDto.class);
        return organRegisterDto;
    }

    @Override
    public OrganRegisterDto findByIdAndOrgFullIdLike(Long id,String fullId) {
        OrganRegisterDto organRegisterDto = ConverterService.convert(organRegisterDao.findByIdAndFullIdStartingWith(id,fullId),OrganRegisterDto.class);
        return organRegisterDto;
    }

    @Override
    public OrganRegisterDto findByOrganFullId(String fullId) {
        return ConverterService.convert(organRegisterDao.findByFullId(fullId), OrganRegisterDto.class);
    }

    @Override
    public void del(Long id) {
        organRegisterDao.delete(id);
    }

    @Override
    public Boolean getOrganRegisterIsNull(String organFullId) {
        OrganizationDto organizationDto = organizationService.findByOrganFullId(organFullId);

        OrganRegisterPo organRegister = organRegisterDao.findByOrganId(organizationDto.getId());
        if(organRegister == null) {
            return true;
        }

        return false;
    }

    @Override
    public Boolean getOrganRegisterFlagByBankCode(String bankCode) {
        List<OrganRegisterPo> organRegister = organRegisterDao.findByPbcCode(bankCode);
        if(CollectionUtils.isNotEmpty(organRegister)) {
            return true;
        }
        return false;
    }

    @Override
    public TableResultResponse<OrganRegisterDto> queryList(OrganRegisterDto organRegisterDto,Pageable pageable) {
        if(StringUtils.isBlank(organRegisterDto.getFullId())){
            organRegisterDto.setFullId(SecurityUtils.getCurrentOrgFullId());
        }
        Page<OrganRegisterPo> page = organRegisterDao.findAll(new OrganRegisterSearchSpec(organRegisterDto),pageable);
        long count = organRegisterDao.count(new OrganRegisterSearchSpec(organRegisterDto));
        List<OrganRegisterPo> list = page.getContent();
        List<OrganRegisterDto> listDto = ConverterService.convertToList(list, OrganRegisterDto.class);
        return new TableResultResponse<OrganRegisterDto>((int)count, listDto);
    }

    @Override
    public void delete(Long orgId) {
        organRegisterDao.deleteByOrganId(orgId);
    }
}
