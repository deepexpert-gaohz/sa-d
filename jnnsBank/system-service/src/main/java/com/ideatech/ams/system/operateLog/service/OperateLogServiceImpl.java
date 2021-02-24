package com.ideatech.ams.system.operateLog.service;

import com.ideatech.ams.system.operateLog.dao.OperateLogDao;
import com.ideatech.ams.system.operateLog.dto.OperateLogDto;
import com.ideatech.ams.system.operateLog.entity.OperateLogPo;

import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BaseException;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OperateLogServiceImpl extends BaseServiceImpl<OperateLogDao, OperateLogPo, OperateLogDto> implements OperateLogService {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;
    @Override
    public TableResultResponse<OperateLogDto> query(Long refBillId, Pageable pageable) {
        List<OperateLogDto> logDtoList = new ArrayList<>();
        List<OperateLogPo> logPoList = getBaseDao().findByRefBillIdOrderByLastUpdateDate(refBillId,pageable);
        Long count = getBaseDao().countByRefBillId(refBillId);
        if (CollectionUtils.isNotEmpty(logPoList)) {
            logDtoList = ConverterService.convertToList(logPoList, OperateLogDto.class);
        }
        return  new TableResultResponse<OperateLogDto>(count.intValue(), logDtoList);
    }

    @Override
    public TableResultResponse<OperateLogDto> query(Long refBillId) {
        List<OperateLogDto> logDtoList = new ArrayList<>();
        List<OperateLogPo> logPoList = getBaseDao().findByRefBillIdOrderByLastUpdateDate(refBillId);
        if (CollectionUtils.isNotEmpty(logPoList)) {
            logDtoList = ConverterService.convertToList(logPoList, OperateLogDto.class);
        }
        return  new TableResultResponse<OperateLogDto>(logPoList.size(), logDtoList);
    }

    @Override
    public OperateLogDto updateAndSave(OperateLogDto logDto) {
        try {
            if(logDto.getRefBillId()!=null && StringUtils.isNotBlank(logDto.getOperateType())){
                log.info("记录操作日志");
                SecurityUtils.UserInfo current = SecurityUtils.getCurrentUser();
                Long userId;
                if(current!=null){
                    userId = current.getId();
                }else{
                    userId = 2l;
                }
                UserDto userDto = userService.findById(userId);
                OrganizationDto organizationDto = organizationService.findById(userDto.getOrgId());
                OperateLogPo operateLogPo = new OperateLogPo();
                operateLogPo.setRefBillId(logDto.getRefBillId());
                operateLogPo.setOrganCode(organizationDto.getCode());
                operateLogPo.setOperateName(userDto.getUsername()+"-"+userDto.getCname());
                operateLogPo.setOperateType(logDto.getOperateType());
                operateLogPo.setOperateDate(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
                if(StringUtils.equals("rejectForm",logDto.getOperateType()) || StringUtils.equals("verifyNotPass",logDto.getOperateType())){
                    operateLogPo.setFailMsg(logDto.getFailMsg());
                }
                operateLogPo = getBaseDao().save(operateLogPo);
                logDto.setId(operateLogPo.getId());
            }else{
                log.info("记录操作日志，流水或者操作类型为空");
            }
        }catch (Exception e){
            log.error("记录操作日志异常：{}",e);
        }
        return logDto;
    }
}
