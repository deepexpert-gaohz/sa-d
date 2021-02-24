package com.ideatech.ams.customer.service;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.customer.dao.SaicMonitorDao;
import com.ideatech.ams.customer.dto.SaicMonitorDto;
import com.ideatech.ams.customer.dto.SaicStateDto;
import com.ideatech.ams.customer.entity.SaicMonitorPo;
import com.ideatech.ams.customer.enums.SaicMonitorEnum;
import com.ideatech.ams.customer.poi.SaicMonitorExport;
import com.ideatech.ams.customer.poi.SaicMonitorPoi;
import com.ideatech.ams.customer.spec.SaicMonitorSpec;
import com.ideatech.ams.customer.vo.SaicMonitorVo;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.dto.TreeTable;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.*;

@Service
//@Transactional
@Slf4j
public class SaicMonitorServiceImpl extends BaseServiceImpl<SaicMonitorDao, SaicMonitorPo, SaicMonitorDto> implements SaicMonitorService {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SaicMonitorDao saicMonitorDao;

    @Autowired
    private HttpRequest httpRequest;

    /**
     * 基本工商数据接口
     */
    @Value("${saic.url.stateUrl:http://120.27.165.115/cloudidp}")
    private String stateUrl;

    @Override
    public TreeTable query(Long pid, Long organId) {
        TreeTable result = new TreeTable();
        List<OrganizationDto> organChilds = null;

        if (pid == null) {
            OrganizationDto parent = organizationService.findById(organId);

            Map<String, Object> row = buildRow(parent);

            List<Map<String, Object>> rows = new ArrayList<>();
            organChilds = organizationService.searchChild(organId, "");

            for (OrganizationDto child : organChilds) {
                rows.add(buildRow(child));
            }
            row.put("rows", rows);

            result.getRows().add(row);
        } else {
            result.setPid(pid.toString());
            organChilds = organizationService.searchChild(pid, "");

            for (OrganizationDto childOrgan : organChilds) {
                result.getRows().add(buildRow(childOrgan));
            }
        }

        return result;
    }

    @Override
    public TableResultResponse<SaicMonitorDto> queryPage(SaicMonitorDto dto, Pageable pageable) {
        OrganizationDto organizationDto = null;
        if(dto.getOrganId() != null){
            organizationDto = organizationService.findById(dto.getOrganId());
        }else{
            organizationDto = organizationService.findById(1L);
        }
        SaicMonitorVo saicMonitorVo = new SaicMonitorVo();
        BeanUtils.copyProperties(dto,saicMonitorVo);
        saicMonitorVo.setOrganFullId(organizationDto.getFullId());
        Page<SaicMonitorPo> page = saicMonitorDao.findAll(new SaicMonitorSpec(saicMonitorVo), pageable);
        long count = saicMonitorDao.count(new SaicMonitorSpec(saicMonitorVo));
        List<SaicMonitorPo> list = page.getContent();
        List<SaicMonitorDto> listDto = ConverterService.convertToList(list, SaicMonitorDto.class);

        return new TableResultResponse<SaicMonitorDto>((int) count, listDto);
    }

    @Override
    public IExcelExport exportSaicMonitorExcel(SaicMonitorDto dto) {
        SaicMonitorVo saicMonitorVo = new SaicMonitorVo();
        BeanUtils.copyProperties(dto,saicMonitorVo);
        OrganizationDto organizationDto = null;
        if(dto.getOrganId() != null){
            organizationDto = organizationService.findById(dto.getOrganId());
            saicMonitorVo.setOrganFullId(organizationDto.getFullId());
        }
        List<SaicMonitorPo> list = saicMonitorDao.findAll(new SaicMonitorSpec(saicMonitorVo));
        List<SaicMonitorDto> saicMonitorDtos = ConverterService.convertToList(list, SaicMonitorDto.class);
        IExcelExport saicMonitorExport = new SaicMonitorExport();
        List<SaicMonitorPoi> saicMonitorPois = new ArrayList<>();

        String[] ig = new String[]{"checkType"};
        for(SaicMonitorDto saicMonitorDto : saicMonitorDtos){
            SaicMonitorPoi saicMonitorPoi = new SaicMonitorPoi();
            BeanUtils.copyProperties(saicMonitorDto,saicMonitorPoi,ig);
            saicMonitorPoi.setCheckType(saicMonitorDto.getCheckType().getValue());
            saicMonitorPois.add(saicMonitorPoi);
        }
        saicMonitorExport.setPoiList(saicMonitorPois);
        return saicMonitorExport;
    }

    @Override
    public SaicMonitorDto getSaicMonitor(String userName, Long organId,String keyWord, Long saicInfoId, String regNo,SaicMonitorEnum saicMonitorEnum) {
        log.info(keyWord + "开始存入工商统计表......");
        SaicMonitorDto saicMonitorDto = new SaicMonitorDto();
        OrganizationDto organizationDto = organizationService.findById(organId);
        saicMonitorDto.setCheckType(saicMonitorEnum);
        saicMonitorDto.setUserName(userName);
        saicMonitorDto.setCreateTime(DateUtils.DateToStr(new Date(),"yyyy-MM-dd"));
        saicMonitorDto.setCompanyName(keyWord);
        saicMonitorDto.setOrganName(organizationDto.getName());
        saicMonitorDto.setOrganFullId(organizationDto.getFullId());
        saicMonitorDto.setRegNo(regNo);
        if(saicInfoId != null){
            saicMonitorDto.setSaicInfoId(saicInfoId);
            saicMonitorDto.setStatus("success");
        }else{
            saicMonitorDto.setSaicInfoId(null);
            saicMonitorDto.setStatus("fail");
        }
        return saicMonitorDto;
    }

    @Override
    public SaicStateDto getState() {

        Long startTime = System.currentTimeMillis();
        SaicStateDto saicStateDto = new SaicStateDto();

        try {
            RestTemplate restTemplate = httpRequest.getRestTemplate(stateUrl);
            httpRequest.checkRestTemplate(restTemplate);
            ResponseEntity responseEntity =  restTemplate.getForEntity(stateUrl,String.class);

            if (200==responseEntity.getStatusCodeValue()){
                saicStateDto.setState(true);
                Long speed = System.currentTimeMillis()-startTime;
                saicStateDto.setSpeed(speed);
            }else {
                saicStateDto.setState(false);
                saicStateDto.setSpeed(0L);
                log.warn("请求失败，IDP服务器异常");
            }
        } catch (Exception e) {
            log.warn("获取IDP网络状态失败！");
            saicStateDto.setState(false);
            saicStateDto.setSpeed(0L);
            return saicStateDto;
        }
        return saicStateDto;
    }

    private Map<String,Object> buildRow(OrganizationDto organ) {
        SaicMonitorVo saicMonitorVo = new SaicMonitorVo();
        saicMonitorVo.setOrganFullId(organ.getFullId());
        long saicCount = 0;

        Map<String, Object> row = new HashMap<String, Object>();
        row.put("id", organ.getId().toString());

        List<OrganizationDto> organChilds = organizationService.searchChild(organ.getId(), "");

        if (CollectionUtils.isNotEmpty(organChilds)) {
            row.put("hasRows", "1");
            row.put("name", organ.getName());
        } else {
            row.put("name", organ.getName());
        }
        saicCount = saicMonitorDao.count(new SaicMonitorSpec(saicMonitorVo));

        row.put("num1", saicCount);
        row.put("orgCode", organ.getCode());
        return row;
    }
}