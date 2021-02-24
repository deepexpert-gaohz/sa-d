package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dao.ReportDao;
import com.ideatech.ams.kyc.dto.ReportDto;
import com.ideatech.ams.kyc.dto.StockHolderDto;
import com.ideatech.ams.kyc.entity.ChangeRecord;
import com.ideatech.ams.kyc.entity.Report;
import com.ideatech.ams.kyc.entity.StockHolder;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.util.BeanCopierUtils;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 年报
 * @Author wanghongjie
 * @Date 2018/9/28
 **/
@Service
@Transactional
public class ReportServiceImpl implements ReportService{

    @Autowired
    private ReportDao reportDao;

    @Override
    public void insertBatch(final Long saicInfoId, List<ReportDto> reportDtos) {
        List<Report> reports = ConverterService.convertToList(reportDtos, Report.class);
        CollectionUtils.forAllDo(reports, new Closure() {
            @Override
            public void execute(Object input) {
                ((Report) input).setSaicinfoId(saicInfoId);
            }
        });
        reportDao.save(reports);
    }

    @Override
    public List<ReportDto> findBySaicInfoId(Long saicInfoId) {
        List<ReportDto> dtoList = new ArrayList<ReportDto>();
        List<Report> list = reportDao.findBySaicinfoIdOrderByReleasedateAsc(saicInfoId);
        ReportDto reportDto = null;
        //设置主键
        for (Report report : list) {
            reportDto = new ReportDto();
            BeanCopierUtils.copyProperties(report, reportDto);
            dtoList.add(reportDto);
        }
        return dtoList;
    }
}
