package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dao.CompanyPartnerLogDao;
import com.ideatech.ams.customer.dto.CompanyPartnerLogInfo;
import com.ideatech.ams.customer.entity.CompanyPartnerLog;
import com.ideatech.common.converter.ConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author jzh
 * @date 2019/3/27.
 */
@Service
@Transactional
public class CompanyPartnerLogServiceImpl implements CompanyPartnerLogService {

    @Autowired
    private CompanyPartnerLogDao companyPartnerLogDao;

    @Override
    public void save(List<CompanyPartnerLogInfo> companyPartnerLogInfoList) {
        if (0==companyPartnerLogInfoList.size()){
            return;
        }
        companyPartnerLogDao.save(ConverterService.convertToList(companyPartnerLogInfoList, CompanyPartnerLog.class));
    }

    @Override
    public List<CompanyPartnerLogInfo> getAllByCustomerPublicLogId(Long customerPublicLogId) {
        if (customerPublicLogId==null){
            return null;
        }
        List<CompanyPartnerLog> companyPartnerLogList = companyPartnerLogDao.findAllByCustomerPublicLogId(customerPublicLogId);
        return ConverterService.convertToList(companyPartnerLogList,CompanyPartnerLogInfo.class);
    }
}
