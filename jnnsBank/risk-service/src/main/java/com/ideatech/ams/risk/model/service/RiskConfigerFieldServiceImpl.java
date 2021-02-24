package com.ideatech.ams.risk.model.service;

import com.ideatech.ams.risk.model.dao.RiskConfigerFieldDao;
import com.ideatech.ams.risk.model.dto.RiskConfigerFieldDto;
import com.ideatech.ams.risk.model.entity.Model;
import com.ideatech.ams.risk.model.entity.RiskConfigerField;
import com.ideatech.ams.risk.util.RiskUtil;
import com.ideatech.common.converter.ConverterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class RiskConfigerFieldServiceImpl implements RiskConfigerFieldService {
    @Autowired
    private RiskConfigerFieldDao modelConfigerFieldDao;


    @Override
    public List<RiskConfigerFieldDto> findAll() {
        List<RiskConfigerField> list =  modelConfigerFieldDao.findAll ();
        return  ConverterService.convertToList(list, RiskConfigerFieldDto.class);
    }

    @Override
    public RiskConfigerFieldDto findByField(String field,String code) {
        RiskConfigerField riskConfigerField = modelConfigerFieldDao.findByField(field);
        return  ConverterService.convert (riskConfigerField, RiskConfigerFieldDto.class );
    }

    @Override
    public void save(RiskConfigerFieldDto riskConfigerFieldDto) {
        RiskConfigerField riskConfigerField = new RiskConfigerField();

        ConverterService.convert(riskConfigerFieldDto,riskConfigerField);
        //riskConfigerField.setCorporateBank ( RiskUtil.getOrganizationCode() );
        modelConfigerFieldDao.save ( riskConfigerField );
    }

}
