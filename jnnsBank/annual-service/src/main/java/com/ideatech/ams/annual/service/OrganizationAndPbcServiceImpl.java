package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.OrganizationAndPbcDto;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 构造组织对象包含人行和信用机构的信息
 * @Author wanghongjie
 * @Date 2018/8/8
 **/
@Service
public class OrganizationAndPbcServiceImpl implements OrganizationAndPbcService{
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Override
    public OrganizationAndPbcDto findByFullId(String fullId) {
        OrganizationAndPbcDto organizationAndPbcDto = null;
        OrganizationDto organizationDto = organizationService.findByOrganFullId(fullId);
        if(organizationDto != null){
            organizationAndPbcDto = new OrganizationAndPbcDto();
            BeanUtils.copyProperties(organizationDto,organizationAndPbcDto);
            List<PbcAccountDto> pbcList = pbcAccountService.listByOrgIdAndType(organizationDto.getId(), EAccountType.AMS);
            List<PbcAccountDto> eccsList = pbcAccountService.listByOrgIdAndType(organizationDto.getId(), EAccountType.ECCS);
            if(pbcList.size()>0){
                PbcAccountDto pbcAccountPo = pbcList.get(0);
                copyFromPbc(organizationAndPbcDto,pbcAccountPo);
            }
            if(eccsList.size()>0){
                PbcAccountDto eccsAccountPo = eccsList.get(0);
                copyFromEccs(organizationAndPbcDto,eccsAccountPo);
            }
        }
        return organizationAndPbcDto;
    }

    @Override
    public OrganizationAndPbcDto findById(Long id) {
        OrganizationAndPbcDto organizationAndPbcDto = null;
        OrganizationDto organizationDto = organizationService.findById(id);
        if(organizationDto != null){
            organizationAndPbcDto = new OrganizationAndPbcDto();
            BeanUtils.copyProperties(organizationDto,organizationAndPbcDto);
            List<PbcAccountDto> pbcList = pbcAccountService.listByOrgIdAndType(organizationDto.getId(), EAccountType.AMS);
            List<PbcAccountDto> eccsList = pbcAccountService.listByOrgIdAndType(organizationDto.getId(), EAccountType.ECCS);
            if(pbcList.size()>0){
                PbcAccountDto pbcAccountPo = pbcList.get(0);
                copyFromPbc(organizationAndPbcDto,pbcAccountPo);
            }
            if(eccsList.size()>0){
                PbcAccountDto eccsAccountPo = eccsList.get(0);
                copyFromEccs(organizationAndPbcDto,eccsAccountPo);
            }
        }
        return organizationAndPbcDto;
    }
    /**
     * @Description 复制人行信息
     * @Author wanghongjie
     * @Date 2018/8/8
     **/
    private void copyFromPbc(OrganizationAndPbcDto organizationAndPbcDto,PbcAccountDto pbcAccountDto){
        organizationAndPbcDto.setPbcIp(pbcAccountDto.getIp());
        organizationAndPbcDto.setPbcUsername(pbcAccountDto.getAccount());
        organizationAndPbcDto.setPbcPassword(pbcAccountDto.getPassword());
        organizationAndPbcDto.setPbcLoginStatus(pbcAccountDto.getAccountStatus());
        organizationAndPbcDto.setPbcEnabled(pbcAccountDto.getEnabled());
    }

    /**
     * @Description 复制信用机构信息
     * @Author wanghongjie
     * @Date 2018/8/8
     **/
    private void copyFromEccs(OrganizationAndPbcDto organizationAndPbcDto,PbcAccountDto pbcAccountDto){
        organizationAndPbcDto.setEccsIp(pbcAccountDto.getIp());
        organizationAndPbcDto.setEccsUsername(pbcAccountDto.getAccount());
        organizationAndPbcDto.setEccsPassword(pbcAccountDto.getPassword());
        organizationAndPbcDto.setEccsLoginStatus(pbcAccountDto.getAccountStatus());
        organizationAndPbcDto.setEccsEnabled(pbcAccountDto.getEnabled());
    }
}
