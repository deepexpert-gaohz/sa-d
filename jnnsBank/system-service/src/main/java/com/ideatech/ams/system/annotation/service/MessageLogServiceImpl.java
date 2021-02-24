package com.ideatech.ams.system.annotation.service;

import com.alibaba.druid.support.spring.stat.annotation.Stat;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.system.annotation.dao.MessageLogDao;
import com.ideatech.ams.system.annotation.dto.MessageLogDto;
import com.ideatech.ams.system.annotation.entity.MessageLog;
import com.ideatech.ams.system.annotation.poi.StatisticeExport;
import com.ideatech.ams.system.annotation.poi.StatisticePoi;
import com.ideatech.ams.system.annotation.spec.MessageLogSpec;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class MessageLogServiceImpl extends BaseServiceImpl<MessageLogDao, MessageLog, MessageLogDto> implements MessageLogService{

    @Autowired
    private MessageLogDao messageLogDao;

    @Override
    public TableResultResponse<MessageLogDto> queryPage(MessageLogDto dto, Pageable pageable) {

        Page<MessageLog> page = messageLogDao.findAll(new MessageLogSpec(dto), pageable);
        long count = messageLogDao.count(new MessageLogSpec(dto));
        List<MessageLog> list = page.getContent();

        List<MessageLogDto> listDto = ConverterService.convertToList(list, MessageLogDto.class);

        return new TableResultResponse<MessageLogDto>((int) count, listDto);
    }

    @Override
    public void saveMessageLog(MessageLogDto messageLogDto) {
        MessageLog messageLog = new MessageLog();
        BeanUtils.copyProperties(messageLogDto,messageLog);
        messageLogDao.saveAndFlush(messageLog);
    }

    @Override
    public JSONObject findByDate(MessageLogDto dto) {

        JSONObject json = new JSONObject();
        Long successCount = null;
        Long failCount = null;
        if(StringUtils.equalsIgnoreCase("success",dto.getProcessResult())){
            successCount = messageLogDao.count(new MessageLogSpec(dto));
            json.put("successCount",successCount);
            json.put("failCount",0);
            return json;
        }else if(StringUtils.equalsIgnoreCase("fail",dto.getProcessResult())){
            failCount = messageLogDao.count(new MessageLogSpec(dto));
            json.put("successCount",0);
            json.put("failCount",failCount);
            return json;
        }else{
            //成功数量
            dto.setProcessResult("SUCCESS");
            successCount = messageLogDao.count(new MessageLogSpec(dto));
            //失败数量
            dto.setProcessResult("FAIL");
            failCount = messageLogDao.count(new MessageLogSpec(dto));
            json.put("successCount",successCount);
            json.put("failCount",failCount);
            return json;
        }
    }

    @Override
    public IExcelExport exportStatisticePoiExcel(MessageLogDto dto) {

        List<MessageLog> list = messageLogDao.findAll(new MessageLogSpec(dto));
        List<MessageLogDto> illegalQueryDtoList = ConverterService.convertToList(list, MessageLogDto.class);
        IExcelExport illegalQueryExport = new StatisticeExport();
        List<StatisticePoi> recordSaicPoiList = new ArrayList<StatisticePoi>();


        for(MessageLogDto messageLogDto : illegalQueryDtoList){
            StatisticePoi statisticePoi = new StatisticePoi();
            BeanUtils.copyProperties(messageLogDto,statisticePoi);
            if(StringUtils.isNotBlank(statisticePoi.getTranType())){
                if("AMSCheck".equals(statisticePoi.getTranType())){
                    statisticePoi.setTranType("人行查询");
                }
                if("AMSSync".equals(statisticePoi.getTranType())){
                    statisticePoi.setTranType("人行报送");
                }
                if(("SAIC").equals(statisticePoi.getTranType())){
                    statisticePoi.setTranType("工商查询");
                }
            }
            if(StringUtils.isNotBlank(statisticePoi.getBillType())){
                if("ACCT_OPEN".equals(statisticePoi.getBillType())){
                    statisticePoi.setBillType("开户");
                }
                if("ACCT_CHANGE".equals(statisticePoi.getBillType())){
                    statisticePoi.setBillType("变更");
                }
                if("ACCT_REVOKE".equals(statisticePoi.getBillType())){
                    statisticePoi.setBillType("销户");
                }
                if("ACCT_SUSPEND".equals(statisticePoi.getBillType())){
                    statisticePoi.setBillType("久悬");
                }
            }
            if(StringUtils.isNotBlank(statisticePoi.getProcessResult())){
                if("SUCCESS".equals(statisticePoi.getProcessResult())){
                    statisticePoi.setProcessResult("成功");
                }
                if("FAIL".equals(statisticePoi.getProcessResult())){
                    statisticePoi.setProcessResult("失败");
                }
            }
            recordSaicPoiList.add(statisticePoi);
        }
        illegalQueryExport.setPoiList(recordSaicPoiList);
        return illegalQueryExport;
    }
}
