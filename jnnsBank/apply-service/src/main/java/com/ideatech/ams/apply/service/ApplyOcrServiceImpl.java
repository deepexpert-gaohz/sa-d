package com.ideatech.ams.apply.service;

import com.ideatech.ams.apply.dao.ApplyOcrDao;
import com.ideatech.ams.apply.dto.ApplyOcrDto;
import com.ideatech.ams.apply.entity.ApplyOcr;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.util.BeanCopierUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ApplyOcrServiceImpl implements ApplyOcrService {

    @Autowired
    private ApplyOcrDao applyOcrDao;


    @Override
    public List<ApplyOcrDto> getApplyOcrList(String applyId, String docCode) {
        List<ApplyOcr> list = null;
        if(StringUtils.isNotBlank(docCode)) {
            list = applyOcrDao.findByApplyidAndDoccode(applyId, docCode);
        } else {
            list = applyOcrDao.findByApplyid(applyId);
        }

        List<ApplyOcrDto> listDto = new ArrayList<>();
        ApplyOcrDto applyOcrDto = null;
        if(list != null && list.size() != 0) {
            for(ApplyOcr ApplyOcr : list) {
                applyOcrDto = new ApplyOcrDto();
                BeanCopierUtils.copyProperties(ApplyOcr, applyOcrDto);
                listDto.add(applyOcrDto);
            }

            return listDto;
        }

        return listDto;
    }

    @Override
    public void saveApply(ApplyOcrDto applyOcrDto) {
        ApplyOcr applyOcr = new ApplyOcr();
        BeanUtils.copyProperties(applyOcrDto,applyOcr);
        applyOcrDao.save(applyOcr);
    }

    @Override
    public ApplyOcrDto findByFilename(String filename) {
        ApplyOcr applyOcr = applyOcrDao.findByFilename(filename);
        if(applyOcr != null){
            ApplyOcrDto applyOcrDto = new ApplyOcrDto();
            BeanUtils.copyProperties(applyOcr,applyOcrDto);
            return applyOcrDto;
        }
        return null;
    }

    @Override
    public ApplyOcrDto findByFilenameAndApplyid(String filename,String applyid) {
        ApplyOcr applyOcr = applyOcrDao.findByFilenameAndApplyid(filename,applyid);
        if(applyOcr != null){
            ApplyOcrDto applyOcrDto = new ApplyOcrDto();
            BeanUtils.copyProperties(applyOcr,applyOcrDto);
            return applyOcrDto;
        }
        return null;
    }

    @Override
    public ApplyOcrDto findByApplyidAndCurNum(String applyid, Integer curNum) {
        ApplyOcr applyOcr = applyOcrDao.findByApplyidAndCurNum(applyid, curNum);
        if(applyOcr != null){
            ApplyOcrDto applyOcrDto = new ApplyOcrDto();
            BeanUtils.copyProperties(applyOcr,applyOcrDto);
            return applyOcrDto;
        }
        return null;
    }

    @Override
    public List<ApplyOcrDto> findByApplyidOrderByCurNumAsc(String applyid) {
        List<ApplyOcr> applyOcrs = applyOcrDao.findByApplyidOrderByCurNumAsc(applyid);
        List<ApplyOcrDto> applyOcrDtos = new ArrayList<ApplyOcrDto>();
        if(applyOcrs.size() >0){
            applyOcrDtos = ConverterService.convertToList(applyOcrs,ApplyOcrDto.class);
        }
        return applyOcrDtos;
    }


}
