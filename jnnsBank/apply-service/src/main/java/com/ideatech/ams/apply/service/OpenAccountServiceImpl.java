package com.ideatech.ams.apply.service;

import com.ideatech.ams.apply.dao.ApplyAccountInfoDao;
import com.ideatech.ams.apply.dao.ApplyCustomerPublicMidDao;
import com.ideatech.ams.apply.dao.OpenAccountLogDao;
import com.ideatech.ams.apply.dto.ApplyCustomerPublicMidDto;
import com.ideatech.ams.apply.entity.ApplyAccountInfo;
import com.ideatech.ams.apply.entity.ApplyCustomerPublicMid;
import com.ideatech.ams.apply.entity.OpenAccountLog;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class OpenAccountServiceImpl implements OpenAccountService {

    @Autowired
    private OpenAccountLogDao openAccountLogDao;

    @Autowired
    private ApplyCustomerPublicMidDao customerPublicMidDao;

    @Autowired
    private ApplyAccountInfoDao accountInfoDao;

    @Override
    public ApplyCustomerPublicMidDto getCustomerInfoBySourceId(Long sourceId) {
        OpenAccountLog openAccountLog = openAccountLogDao.findOne(sourceId);

        if (openAccountLog == null) {
            return null;
        } else {
            return getCustomerInfoDetails(openAccountLog.getId());
        }
    }

    public ApplyCustomerPublicMidDto getCustomerInfoDetails(Long openAccountLogId) {
        ApplyAccountInfo aiResult = accountInfoDao.findOne(openAccountLogId);
        ApplyCustomerPublicMid cpmResult = customerPublicMidDao.findOne(openAccountLogId);

        ApplyCustomerPublicMidDto result = new ApplyCustomerPublicMidDto();
        BeanUtils.copyProperties(aiResult, result);
        BeanUtils.copyProperties(cpmResult, result);
        result.setOpenAccountLogId(openAccountLogId);
        result.setAccountInfoId(aiResult.getId());
        result.setCustomerPublicMidId(cpmResult.getId());
        return result;

    }

}
