package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dao.CompanyPartnerMidDao;
import com.ideatech.ams.customer.dto.CompanyPartnerMidInfo;
import com.ideatech.ams.customer.entity.CompanyPartnerMid;
import com.ideatech.common.converter.ConverterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jzh
 * @date 2019/3/20.
 */

@Service
@Slf4j
public class CompanyPartnerMidServiceImpl implements CompanyPartnerMidService {

    @Autowired
    private CompanyPartnerMidDao companyPartnerMidDao;

    @Override
    public void save(CompanyPartnerMidInfo companyPartnerMidInfo) {
        if (companyPartnerMidInfo==null){
            return;
        }
        companyPartnerMidDao.save(ConverterService.convert(companyPartnerMidInfo, CompanyPartnerMid.class));
    }

    @Override
    public void save(List<CompanyPartnerMidInfo> companyPartnerMidInfoList) {
        if (companyPartnerMidInfoList==null){
            return;
        }
        List<CompanyPartnerMid> companyPartnerMidList = ConverterService.convertToList(companyPartnerMidInfoList,CompanyPartnerMid.class);
        companyPartnerMidDao.save(companyPartnerMidList);
    }

    @Override
    public CompanyPartnerMidInfo getOne(Long id) {
        CompanyPartnerMid companyPartnerMid = companyPartnerMidDao.findOne(id);
        return ConverterService.convert(companyPartnerMid,CompanyPartnerMidInfo.class);
    }

    @Override
    public List<CompanyPartnerMidInfo> getAllByCustomerPublicMidId(Long customerPublicMidId) {
        List<CompanyPartnerMid> companyPartnerMidList = companyPartnerMidDao.findAllByCustomerPublicMidIdOrderByString001Asc(customerPublicMidId);
        return ConverterService.convertToList(companyPartnerMidList,CompanyPartnerMidInfo.class);
    }

    @Override
    public void deleteByCustomerPublicMidId(Long customerPublicMidId) {
        if (customerPublicMidId==null){
            return;
        }
        companyPartnerMidDao.deleteAllByCustomerPublicMidId(customerPublicMidId);
    }

    @Override
    public void deleteByMidId(Long midId) {
        companyPartnerMidDao.deleteByCustomerPublicMidId(midId);
    }
}
