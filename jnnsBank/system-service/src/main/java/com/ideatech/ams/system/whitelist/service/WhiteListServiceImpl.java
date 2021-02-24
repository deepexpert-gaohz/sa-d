package com.ideatech.ams.system.whitelist.service;

import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.whitelist.dao.WhiteListDao;
import com.ideatech.ams.system.whitelist.dto.WhiteListDto;
import com.ideatech.ams.system.whitelist.dto.WhiteListSearchDto;
import com.ideatech.ams.system.whitelist.entity.WhiteListPo;
import com.ideatech.ams.system.whitelist.enums.WhiteListEntrySource;
import com.ideatech.ams.system.whitelist.spec.WhiteListSearchSpec;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WhiteListServiceImpl implements WhiteListService{

    @Autowired
    private WhiteListDao whiteListDao;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public List<WhiteListDto> list() {
        WhiteListDto whiteListDto = new WhiteListDto();
        List<String> list = new ArrayList<>();
        list.add(null);
        list.add("normal");
        return ConverterService.convertToList(whiteListDao.findAll(new WhiteListSearchSpec(whiteListDto)),WhiteListDto.class);
    }

    @Override
    public WhiteListSearchDto search(WhiteListSearchDto dto) {
        WhiteListDto whiteListDto = new WhiteListDto();
        BeanUtils.copyProperties(dto,whiteListDto);
        if(dto.getSource() == WhiteListEntrySource.ALL){
            whiteListDto.setSource(null);
        }
        whiteListDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        //增加状态查询正常normal 跟 状态为空不是delete的数据
        List<String> list = new ArrayList<>();
        list.add(null);
        list.add("normal");
        whiteListDto.setStatusList(list);

        Page<WhiteListPo> all = whiteListDao.findAll(new WhiteListSearchSpec(whiteListDto), new PageRequest(Math.max(dto.getOffset() - 1, 0), dto.getLimit()));
        List<WhiteListDto> dtos = ConverterService.convertToList(all.getContent(), WhiteListDto.class);
        dto.setList(dtos);
        dto.setTotalRecord(all.getTotalElements());
        dto.setTotalPages(all.getTotalPages());
        return dto;
    }

    @Override
    public ResultDto create(WhiteListDto dto) {
        ResultDto resultDto = new ResultDto();
        if(StringUtils.isNotBlank(dto.getEntName())){
            //很具organId查找机构信息
            OrganizationDto organizationDto = organizationService.findById(dto.getOrgId());
            WhiteListPo whiteListPo = whiteListDao.findByEntNameAndOrgId(dto.getEntName(),dto.getOrgId());
            if(whiteListPo != null){
                //如果存在白名单
                if("normal".equals(whiteListPo.getStatus())){
                    resultDto.setMessage("该机构已存在"+dto.getEntName()+"白名单企业，无需重复添加...... ");
                    resultDto.setCode(ResultCode.NACK);
                    return resultDto;
                }
                //状态是 移除  修改为正常
                if("delete".equals(whiteListPo.getStatus())){
                    whiteListPo.setStatus("normal");
                    if(StringUtils.isBlank(whiteListPo.getOrganCode())){
                        whiteListPo.setOrganCode(organizationDto.getCode());
                    }
                    whiteListDao.save(whiteListPo);
                    resultDto.setMessage("保存成功");
                    resultDto.setCode(ResultCode.ACK);
                    return resultDto;
                }
            }
            //不存在  新建
            WhiteListPo po = new WhiteListPo();
            BeanUtils.copyProperties(dto,po);
            if(organizationDto != null){
                po.setOrgId(organizationDto.getId());
                po.setOrgName(organizationDto.getName());
                po.setOrganFullId(organizationDto.getFullId());
                po.setOrganCode(organizationDto.getCode());
                po.setStatus("normal");
            }
            whiteListDao.save(po);
            resultDto.setMessage("保存成功");
            resultDto.setCode(ResultCode.ACK);
        }
        return resultDto;
    }

    @Override
    public ResultDto update(WhiteListDto dto) {
        ResultDto resultDto = new ResultDto();
        WhiteListPo whiteListPo = whiteListDao.findOne(dto.getId());
        if (whiteListPo == null) {
            resultDto.setMessage("系统无法找到该条数据......");
            resultDto.setCode(ResultCode.NACK);
            return resultDto;
        }
        ConverterService.convert(dto,whiteListPo);
        whiteListDao.save(whiteListPo);
        resultDto.setMessage("保存成功");
        resultDto.setCode(ResultCode.ACK);
        return resultDto;
    }

    @Override
    public void delete(Long id) {
        WhiteListPo whiteListPo = whiteListDao.findOne(id);
        if(whiteListPo == null){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "系统无法找到该条数据......");
        }else{
            //软删除。
            whiteListPo.setStatus("delete");
            whiteListDao.save(whiteListPo);
        }
//        whiteListDao.delete(id);
    }

    @Override
    public WhiteListDto getById(Long id) {
        WhiteListPo whiteListPo = whiteListDao.findOne(id);
        if(whiteListPo == null){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "系统无法找到该条数据......");
        }
        WhiteListDto dto = ConverterService.convert(whiteListPo,WhiteListDto.class);
        return dto;
    }

    @Override
    public WhiteListDto getByEntName(String name) {
        WhiteListPo po = whiteListDao.findByEntNameAndStatus(name,"normal");
        if(po != null){
            WhiteListDto dto = ConverterService.convert(po,WhiteListDto.class);
            return dto;
        }
        return null;
    }

    @Override
    public WhiteListDto getByEntnameAndOrgId(String name, Long orgId) {
        if (orgId != null) {
            WhiteListPo po = whiteListDao.findByEntNameAndOrgId(name, orgId);
            if (po != null) {
                WhiteListDto dto = ConverterService.convert(po, WhiteListDto.class);
                return dto;
            }
        }
        return null;
    }

    @Override
    public long count() {
        long count = whiteListDao.count();
        return count;
    }
}
