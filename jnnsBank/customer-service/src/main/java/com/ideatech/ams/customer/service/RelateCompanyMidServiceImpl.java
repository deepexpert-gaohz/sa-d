package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dao.RelateCompanyMidDao;
import com.ideatech.ams.customer.dto.RelateCompanyMidInfo;
import com.ideatech.ams.customer.entity.RelateCompanyMid;
import com.ideatech.common.converter.ConverterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author van
 * @date 20:52 2018/5/29
 */
@Service
@Slf4j
public class RelateCompanyMidServiceImpl implements RelateCompanyMidService {

    @Autowired
    private RelateCompanyMidDao relateCompanyMidDao;

    @Override
    public void deleteByMidId(Long midId) {
        if (midId == null) {
            return;
        }
        relateCompanyMidDao.deleteAllByCustomerPublicMidId(midId);
    }

    @Override
    public void save(List<RelateCompanyMidInfo> infos) {
        relateCompanyMidDao.save(ConverterService.convertToList(infos, RelateCompanyMid.class));
    }

    @Override
    public List<RelateCompanyMidInfo> getAllByCustomerId(Long customerId) {
        return ConverterService.convertToList(relateCompanyMidDao.findAllByCustomerIdOrderByString001Asc(customerId), RelateCompanyMidInfo.class);
    }

    @Override
    public List<RelateCompanyMidInfo> getAllByCustomerPublicMidId(Long customerPublicMidId) {
        return ConverterService.convertToList(relateCompanyMidDao.findAllByCustomerPublicMidIdOrderByString001Asc(customerPublicMidId), RelateCompanyMidInfo.class);
    }
}
