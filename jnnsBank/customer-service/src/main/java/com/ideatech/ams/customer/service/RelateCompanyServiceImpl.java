package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dao.RelateCompanyDao;
import com.ideatech.ams.customer.dto.RelateCompanyInfo;
import com.ideatech.ams.customer.entity.RelateCompany;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.util.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
public class RelateCompanyServiceImpl implements RelateCompanyService{

	@Autowired
	private RelateCompanyDao relateCompanyDao;
	
	@Override
	public void save(RelateCompanyInfo relateCompanyInfo) {
		RelateCompany relateCompany = null;
        if (relateCompanyInfo.getId() != null) {
        	relateCompany = relateCompanyDao.findOne(relateCompanyInfo.getId());
        }
        if (relateCompany == null) {
        	relateCompany = new RelateCompany();
        }
        BeanCopierUtils.copyProperties(relateCompanyInfo, relateCompany);
        RelateCompany relate = relateCompanyDao.save(relateCompany);
        relateCompanyInfo.setId(relate.getId()); 
	}

	@Override
	public RelateCompanyInfo getOne(Long id) {
		return po2dto(relateCompanyDao.findOne(id));
	}

    @Override
    public void save(List<RelateCompanyInfo> infos) {
        relateCompanyDao.save(ConverterService.convertToList(infos, RelateCompany.class));
    }

    @Override
    public List<RelateCompanyInfo> getAllByCustomerId(Long customerId) {
        return ConverterService.convertToList(relateCompanyDao.findAllByCustomerIdOrderByString001Asc(customerId), RelateCompanyInfo.class);
    }

    @Override
    public List<RelateCompanyInfo> getAllByCustomerPublicId(Long customerPublicId) {
        List<RelateCompany> relateCompanyList = relateCompanyDao.findAllByCustomerPublicIdOrderByString001Asc(customerPublicId);
        return ConverterService.convertToList(relateCompanyList, RelateCompanyInfo.class);
    }


    private RelateCompanyInfo po2dto(RelateCompany relateCompany) {
        if (relateCompany != null) {
        	RelateCompanyInfo info = new RelateCompanyInfo();
            BeanCopierUtils.copyProperties(relateCompany, info);
            return info;
        } else {
            return null;
        }
    }
}
