package com.ideatech.ams.apply.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.ideatech.ams.apply.cryptography.*;
import com.ideatech.ams.apply.dto.RSACode;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.dto.OrganizationSyncDto;
import com.ideatech.ams.system.org.enums.SyncType;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.org.service.OrganizationSyncService;
import com.ideatech.ams.system.org.vo.DeleteSyncOrg;
import com.ideatech.ams.system.org.vo.InsertSyncOrg;
import com.ideatech.ams.system.org.vo.SyncOrgVo;
import com.ideatech.ams.system.org.vo.UpdateSyncOrg;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.util.BeanUtil;
import com.ideatech.common.util.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 机构同步
 * @Author wanghongjie
 * @Date 2019/2/22
 **/
@Service
@Slf4j
public class SynchronizeOrgServiceImpl implements SynchronizeOrgService {

    @Value("${apply.url.insertSync}")
    private String insertSyncUrl;

    @Value("${apply.url.updateSync}")
    private String updateSyncUrl;

    @Value("${apply.url.deleteSync}")
    private String deleteSyncUrl;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationSyncService organizationSyncService;

    @Autowired
    private CompanyPreOpenAccountEntService companyPreOpenAccountEntService;

    @Autowired
    private RSACode rsaCode;

    @Autowired
    private HttpRequest httpRequest;

    @Autowired
    private SynchronizeOrgService synchronizeOrgService;

    @Autowired
    private UserService userService;

    @Override
    public String syncOrg(OrganizationSyncDto organizationSyncDto) {
        String syncUrl = syncUrl(organizationSyncDto);
        if(StringUtils.isBlank(syncUrl)){
            log.error("同步数据状态为空: [{}]",organizationSyncDto);
            return "同步数据状态为空";
        }
        String errorMsg = null;
        CryptoInput cryptoInput = new CryptoInput();
        SyncOrgVo syncOrgVo = convertSyncOrgVo(organizationSyncDto);
        cryptoInput.setRawBizContext(JSON.toJSONString(syncOrgVo));
        cryptoInput.setThirdPartyOrgCode(companyPreOpenAccountEntService.getRSACodeOrganId(rsaCode));
        CryptoOutput cryptoOutput =new CryptoOutput();
        CryptoResultDto cryptoResultDto = new CryptoResultDto();
        String code=null;
        try {
            code = synchronizeOrgService.send(cryptoInput, cryptoOutput,syncUrl,cryptoResultDto);
        }catch(RuntimeException e) {
            log.error("调用状态接口，远程访问失败,"+e.getMessage()+","+syncOrgVo);
            return "远程调用接口失败";
        }
        if(StringUtils.isBlank(code) || !"000000".equals(code)) {
            organizationSyncDto.setSyncFinishStatus(true);
            organizationSyncDto.setSyncSuccessStatus(false);
            errorMsg = "同步失败";
            if(StringUtils.isNotBlank(cryptoResultDto.getMsg())){
                organizationSyncDto.setErrorMsg(cryptoResultDto.getMsg());
                errorMsg = cryptoResultDto.getMsg();
            }
            organizationSyncService.save(organizationSyncDto);
        }else {
            organizationSyncDto.setSyncFinishStatus(true);
            organizationSyncDto.setSyncSuccessStatus(true);
            organizationSyncService.save(organizationSyncDto);
        }
        return errorMsg;
    }

    /**
     * 根据SyncType返回请求
     * @param organizationSyncDto
     * @return
     */
    private String syncUrl(OrganizationSyncDto organizationSyncDto){
        if(organizationSyncDto.getSyncType() == SyncType.INSERT){
            return insertSyncUrl;
        }else if(organizationSyncDto.getSyncType() == SyncType.UPDATE){
            return updateSyncUrl;
        }else if(organizationSyncDto.getSyncType() == SyncType.DELETE){
            return deleteSyncUrl;
        }else{
            return "";
        }
    }

    /**
     * 机构同步对象
     * @param organizationSyncDto
     * @return
     */
    private SyncOrgVo convertSyncOrgVo(OrganizationSyncDto organizationSyncDto){
        if(organizationSyncDto.getSyncType() == SyncType.INSERT){
            InsertSyncOrg insertSyncOrg = new InsertSyncOrg();
            BeanUtils.copyProperties(organizationSyncDto,insertSyncOrg);
            insertSyncOrg.setAfterAmsFullid(organizationSyncDto.getAfterFullId());
            insertSyncOrg.setCode(organizationSyncDto.getPbcCode());
            insertSyncOrg.setContactList(organizationSyncDto.getMobile());
            insertSyncOrg.setCnapsCode(organizationSyncDto.getPbcCode());
            UserDto user = userService.findById(Long.parseLong(organizationSyncDto.getLastUpdateBy()));
            if(user !=null){
                insertSyncOrg.setCrtName(user.getCname());
                insertSyncOrg.setCrtUser(user.getUsername());
            }else{
                insertSyncOrg.setCrtName("noUser");
                insertSyncOrg.setCrtUser("noUser");
            }
            OrganizationDto parentOrg = organizationService.findById(organizationSyncDto.getParentId());
            if(parentOrg != null){
                insertSyncOrg.setParentCnapsCode(parentOrg.getPbcCode());
            }
            return insertSyncOrg;
        }else if(organizationSyncDto.getSyncType() == SyncType.UPDATE){
            UpdateSyncOrg updateSyncOrg = new UpdateSyncOrg();
            BeanUtils.copyProperties(organizationSyncDto,updateSyncOrg);
            updateSyncOrg.setAfterAmsFullid(organizationSyncDto.getAfterFullId());
            updateSyncOrg.setCode(organizationSyncDto.getPbcCode());
            updateSyncOrg.setContactList(organizationSyncDto.getMobile());
            updateSyncOrg.setCnapsCode(organizationSyncDto.getPbcCode());
            updateSyncOrg.setOriginalCnapsCode(organizationSyncDto.getOriginalPbcCode());
            UserDto user = userService.findById(Long.parseLong(organizationSyncDto.getLastUpdateBy()));
            if(user !=null){
                updateSyncOrg.setUpdName(user.getCname());
                updateSyncOrg.setUpdUser(user.getUsername());
            }else{
                updateSyncOrg.setUpdName("noUser");
                updateSyncOrg.setUpdUser("noUser");
            }
            return updateSyncOrg;
        }else if(organizationSyncDto.getSyncType() == SyncType.DELETE){
            DeleteSyncOrg deleteSyncOrg = new DeleteSyncOrg();
            BeanUtils.copyProperties(organizationSyncDto,deleteSyncOrg);
            deleteSyncOrg.setCnapsCode(organizationSyncDto.getPbcCode());
            UserDto user = userService.findById(Long.parseLong(organizationSyncDto.getLastUpdateBy()));
            OrganizationDto parentOrg = organizationService.findById(organizationSyncDto.getParentId());
            if(parentOrg != null){
                deleteSyncOrg.setParentCnapsCode(parentOrg.getPbcCode());
            }
            return deleteSyncOrg;
        }else{
            return null;
        }
    }

    @EnableSendCryptography
    public String send(CryptoInput cryptoInput, CryptoOutput cryptoOutput,String url,CryptoResultDto cryptoResultDto){
        RestTemplate restTemplate = httpRequest.getRestTemplate(url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        CryptoVo cryptoVo = new CryptoVo();
        BeanUtils.copyProperties(cryptoInput, cryptoVo);

        log.info("发送请求参数[{}]",JSON.toJSONString(cryptoVo));
        HttpEntity<String> request = new HttpEntity<>(JSON.toJSONString(cryptoVo), headers);
        httpRequest.checkRestTemplate(restTemplate);
        ObjectRestResponse<CryptoVo> response = null;
        try {
            response = restTemplate.postForObject(url, request, ObjectRestResponse.class);
        }catch(RuntimeException e) {
            log.error("同步易账户的机构异常，"+e.getMessage());
            throw new EacException("同步易账户的机构异常,"+e.getMessage());
        }

        if (response == null) {
            log.error("发送消息异常,返回为NULL");
            throw new EacException("发送消息异常");
        }
        String code = response.getCode();
        if(StringUtils.isNotBlank(code) && "000000".equals(code)) {
            log.info("返回的结果消息："+response.getMsg()+",返回的Code为"+response.getCode());
        }else {
            log.info("返回的结果消息："+response.getMsg()+",返回的Code为"+response.getCode());
            cryptoResultDto.setMsg(response.getMsg());
        }
        return code;
    }
}
