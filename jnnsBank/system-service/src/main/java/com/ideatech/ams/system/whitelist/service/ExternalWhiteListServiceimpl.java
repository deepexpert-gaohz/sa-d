package com.ideatech.ams.system.whitelist.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.whitelist.dao.WhiteListDao;
import com.ideatech.ams.system.whitelist.dto.WhiteListDto;
import com.ideatech.ams.system.whitelist.entity.WhiteListPo;
import com.ideatech.ams.system.whitelist.enums.WhiteListEntrySource;
import com.ideatech.common.converter.ConverterService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j
public class ExternalWhiteListServiceimpl implements ExternalWhiteListService{

    @Autowired
    private WhiteListDao whiteListDao;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public void whiteLististSave(List<String> list) {
        log.info("白名单接口调用保存开始......");
        for(String whiteList : list){
            String name = "";
            try{
                if(whiteList.contains(",")){
                    name = whiteList.split(",")[0];
                    String organCode = whiteList.split(",")[1];
                    WhiteListDto whiteListDto = ConverterService.convert(whiteListDao.findByEntNameAndOrganCode(name,organCode),WhiteListDto.class);
                    if(whiteListDto != null){
                        continue;
                    }else{
                        whiteListDto = new WhiteListDto();
                        WhiteListPo whiteListPo = new WhiteListPo();
                        OrganizationDto organizationDto = organizationService.findByCode(organCode);
                        if(organizationDto != null){
                            log.info("开始保存企业名称：" + name + "，机构号：" + organCode);
                            whiteListDto.setEntName(name);
                            whiteListDto.setSource(WhiteListEntrySource.CORE);
                            whiteListDto.setOrganCode(organCode);
                            whiteListDto.setOrgId(organizationDto.getId());
                            whiteListDto.setOrganFullId(organizationDto.getFullId());
                            whiteListDto.setOrgName(organizationDto.getName());
                            BeanUtils.copyProperties(whiteListDto,whiteListPo);
                            whiteListDao.save(whiteListPo);
                        }

                        log.info("白名单企业接口保存结束......");
                    }
                }
            }catch (Exception e){
                log.error("企业" + name + "保存失败......",e);
            }
        }
    }

    @Override
    public void whiteLististSave(String name, String organCode) {
        if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(organCode)){
            WhiteListPo whiteListPo = whiteListDao.findByEntNameAndOrganCode(name,organCode);
            if(whiteListPo == null){
                OrganizationDto organizationDto = organizationService.findByCode(organCode);
                whiteListPo = whiteListDao.findByEntNameAndOrgId(name,organizationDto.getId());
            }

            if(whiteListPo != null){
                //如果存在白名单并且是删除状态，该白名单状态修改为正常并保存
                if("delete".equals(whiteListPo.getStatus())){
                    whiteListPo.setStatus("normal");
                    whiteListDao.save(whiteListPo);
                }else{
                    log.info("机构"+organCode+"白名单企业"+name+"已存在......");
                }
            }else{
                whiteListPo = new WhiteListPo();
                OrganizationDto organizationDto = organizationService.findByCode(organCode);
                if(organizationDto != null){
                    log.info("开始保存企业名称：" + name + "，机构号：" + organCode);
                    whiteListPo.setEntName(name);
                    whiteListPo.setSource(WhiteListEntrySource.CORE);
                    whiteListPo.setOrganCode(organCode);
                    whiteListPo.setOrgId(organizationDto.getId());
                    whiteListPo.setOrganFullId(organizationDto.getFullId());
                    whiteListPo.setOrgName(organizationDto.getName());
                    whiteListPo.setStatus("normal");
                    whiteListDao.save(whiteListPo);
                }
                log.info("白名单企业接口保存结束......");
            }
            log.info("白名单企业接口保存结束......");
        }
    }

    @Override
    public WhiteListDto searchWhiteObj(String name,String organCode) {
        log.info("机构"+organCode+"开始通过接口查询企业：" + name);
        WhiteListDto whiteListDto = null;
        if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(organCode)){
            try{
                whiteListDto = ConverterService.convert(whiteListDao.findByEntNameAndOrganCode(name,organCode),WhiteListDto.class);
                if(whiteListDto != null){
                    return whiteListDto;
                }
            }catch (Exception e){
                log.error("企业" + name + "查询失败......",e);
                return new WhiteListDto();
            }
        }else{
            log.error("企业名称跟机构号不能为空！");
        }
        return new WhiteListDto();
    }

    @Override
    public String searchWhiteList(List<String> list) {
        JSONArray jsonArray = new JSONArray();
        if(CollectionUtils.isNotEmpty(list)){
            log.info("开始接口批量查询白名单列表，查询数量：" + list.size());
            for(String name : list){
                try{
                    WhiteListDto dto = new WhiteListDto();
                    String jsonString = "";
                    log.info("开始查询企业名称：" + name);
                    WhiteListPo po = whiteListDao.findByEntNameAndStatus(name,"normal");
                    if(po != null){
                        BeanUtils.copyProperties(po,dto);
                        jsonString = JSONObject.toJSONString(dto);
                        JSONObject object = JSONObject.parseObject(jsonString);
                        jsonArray.add(object);
                    }
                }catch (Exception e){
                    log.error("企业：" + name + "查询失败。。。。。。",e);
                }
            }
        }
        return jsonArray.toJSONString();
    }

    @Override
    public void whiteListDel(String name,String organCode) {
        if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(organCode)){
            WhiteListPo whiteListPo = whiteListDao.findByEntNameAndOrganCode(name,organCode);
            if(whiteListPo == null){
                OrganizationDto organizationDto = organizationService.findByCode(organCode);
                whiteListPo = whiteListDao.findByEntNameAndOrgId(name,organizationDto.getId());
                if(whiteListPo != null){
                    log.info("白名单企业存在，进行删除......");
                    whiteListPo.setStatus("delete");
                    whiteListDao.save(whiteListPo);
                }
            }else{
                log.info("白名单企业存在，进行删除......");
                whiteListPo.setStatus("delete");
                whiteListDao.save(whiteListPo);
            }
        }
    }
}
