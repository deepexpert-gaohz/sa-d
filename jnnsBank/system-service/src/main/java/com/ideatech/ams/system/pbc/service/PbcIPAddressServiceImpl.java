package com.ideatech.ams.system.pbc.service;

import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dao.PbcAccountDao;
import com.ideatech.ams.system.pbc.dao.PbcIPAddressDao;
import com.ideatech.ams.system.pbc.dao.spec.PbcIPAddressSpec;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.dto.PbcIPAddressDto;
import com.ideatech.ams.system.pbc.entity.PbcAccountPo;
import com.ideatech.ams.system.pbc.entity.PbcIPAddressPo;
import com.ideatech.ams.system.pbc.enums.EAccountStatus;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PbcIPAddressServiceImpl implements PbcIPAddressService {

    @Autowired
    private PbcIPAddressDao pbcIPAddressDao;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Override
    public void save(PbcIPAddressDto pbcIPAddressDto) {
        PbcIPAddressPo pbcIPAddress = pbcIPAddressDao.findOne(pbcIPAddressDto.getId());
        pbcIPAddress.setProvinceName(pbcIPAddressDto.getProvinceName());
        pbcIPAddress.setIsAnnualSubmit(pbcIPAddressDto.getIsAnnualSubmit());

        pbcIPAddressDao.save(pbcIPAddress);
    }

    @Override
    public TableResultResponse<PbcIPAddressDto> query(PbcIPAddressDto dto, Pageable pageable) {
        PbcIPAddressPo pbcIPAddressPo = null;
        Set<String> set = new HashSet<>();
        List<PbcAccountDto> pbcAccountList = pbcAccountService.listByIpAndAccountType(EAccountType.AMS);

        if(CollectionUtils.isNotEmpty(pbcAccountList)) {
            for(PbcAccountDto pbcAccountDto : pbcAccountList) {
                set.add(pbcAccountDto.getIp());
            }
        }

        if(CollectionUtils.isNotEmpty(set)) {
            for(String ip : set) {
                pbcIPAddressPo = pbcIPAddressDao.findByIp(ip);
                if(pbcIPAddressPo == null) {  //人行ip列表不存在该ip,则插入
                    pbcIPAddressPo = new PbcIPAddressPo();
                    pbcIPAddressPo.setIp(ip);
                    pbcIPAddressPo.setIsAnnualSubmit(false);  //默认年检不提交人行
                    pbcIPAddressDao.save(pbcIPAddressPo);
                }

            }
        }

        List<PbcIPAddressPo> pbcIPAddressList = pbcIPAddressDao.findAll();
        if(CollectionUtils.isNotEmpty(pbcIPAddressList)) {
            for (PbcIPAddressPo pbcIPAddress : pbcIPAddressList) {
                List<PbcAccountDto> pbcAccountDtos = pbcAccountService.listByIp(pbcIPAddress.getIp());
                //人行ip表有数据,人行账户表没数据,要从人行ip表删除
                if(CollectionUtils.isEmpty(pbcAccountDtos)) {
                    pbcIPAddressDao.delete(pbcIPAddress.getId());
                }
            }
        }

        Page<PbcIPAddressPo> page = pbcIPAddressDao.findAll(new PbcIPAddressSpec(dto), pageable);
        long count = pbcIPAddressDao.count(new PbcIPAddressSpec(dto));
        List<PbcIPAddressPo> content = page.getContent();

        List<PbcIPAddressDto> pbcIPAddressDtos = ConverterService.convertToList(content, PbcIPAddressDto.class);

        return new TableResultResponse<>((int)count, pbcIPAddressDtos);
    }

    @Override
    public PbcIPAddressDto getPbcIPAddress(Long id) {
        PbcIPAddressPo pbcIPAddress = pbcIPAddressDao.findOne(id);

        return ConverterService.convert(pbcIPAddress, PbcIPAddressDto.class);
    }

    @Override
    public PbcIPAddressDto getByPbcIPAddress(String ip) {
        PbcIPAddressPo pbcIPAddress = pbcIPAddressDao.findByIp(ip);
        return ConverterService.convert(pbcIPAddress, PbcIPAddressDto.class);
    }


}
