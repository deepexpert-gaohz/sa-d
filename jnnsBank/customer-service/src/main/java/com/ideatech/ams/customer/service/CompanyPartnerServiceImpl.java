package com.ideatech.ams.customer.service;

import javax.transaction.Transactional;

import com.ideatech.common.converter.ConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ideatech.ams.customer.dao.CompanyPartnerDao;
import com.ideatech.ams.customer.dto.CompanyPartnerInfo;
import com.ideatech.ams.customer.entity.CompanyPartner;
import com.ideatech.common.util.BeanCopierUtils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 
 * @author wanghongjie
 *
 * @version 2018-06-20 11:23
 */
@Service
@Transactional
@Slf4j
public class CompanyPartnerServiceImpl implements CompanyPartnerService{

	@Autowired
	private CompanyPartnerDao companyPartnerDao;
	
	@Override
	public void save(CompanyPartnerInfo companyPartnerInfo) {
		CompanyPartner companyPartner = null;
        if (companyPartnerInfo.getId() != null) {
        	companyPartner = companyPartnerDao.findOne(companyPartnerInfo.getId());
        }
        if (companyPartner == null) {
        	companyPartner = new CompanyPartner();
        }
        BeanCopierUtils.copyProperties(companyPartnerInfo, companyPartner);
        CompanyPartner partner = companyPartnerDao.save(companyPartner);
        companyPartnerInfo.setId(partner.getId()); 
	}

	@Override
	public CompanyPartnerInfo getOne(Long id) {
		return po2dto(companyPartnerDao.findOne(id));
	}

    @Override
    public List<CompanyPartnerInfo> getAllByCustomerPublicId(Long customerPublicId) {
	    List<CompanyPartner> companyPartnerList = companyPartnerDao.findAllByCustomerPublicId(customerPublicId);
        return ConverterService.convertToList(companyPartnerList,CompanyPartnerInfo.class);
    }

    @Override
    public void save(List<CompanyPartnerInfo> companyPartnerInfoList) {
        if (companyPartnerInfoList==null){
            return;
        }
        companyPartnerDao.save(ConverterService.convertToList(companyPartnerInfoList,CompanyPartner.class));
    }


    private CompanyPartnerInfo po2dto(CompanyPartner companyPartner) {
        if (companyPartner != null) {
        	CompanyPartnerInfo info = new CompanyPartnerInfo();
            BeanCopierUtils.copyProperties(companyPartner, info);
            return info;
        } else {
            return null;
        }
    }
}
