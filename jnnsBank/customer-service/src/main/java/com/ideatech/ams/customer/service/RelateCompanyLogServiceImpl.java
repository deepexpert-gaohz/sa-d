package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dao.RelateCompanyLogDao;
import com.ideatech.ams.customer.dto.RelateCompanyLogInfo;
import com.ideatech.ams.customer.entity.RelateCompanyLog;
import com.ideatech.common.converter.ConverterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author van
 * @date 20:51 2018/5/29
 */
@Service
@Slf4j
public class RelateCompanyLogServiceImpl implements RelateCompanyLogService {

    @Autowired
    private RelateCompanyLogDao relateCompanyLogDao;

    @Override
    public void deleteByMidId(Long midId) {
        if (midId == null) {
            return;
        }
        relateCompanyLogDao.deleteAllByCustomerPublicLogId(midId);
    }

    @Override
    public void save(List<RelateCompanyLogInfo> infos) {
        relateCompanyLogDao.save(ConverterService.convertToList(infos, RelateCompanyLog.class));
    }

    @Override
    public List<RelateCompanyLogInfo> getAllByCustomerId(Long customerId) {
        return ConverterService.convertToList(relateCompanyLogDao.findAllByCustomerIdOrderByString001Asc(customerId), RelateCompanyLogInfo.class);
    }

    @Override
    public List<RelateCompanyLogInfo> getAllByCustomerPublicLogId(Long customerPublicLogId) {
        return ConverterService.convertToList(relateCompanyLogDao.findAllByCustomerPublicLogIdOrderByString001Asc(customerPublicLogId), RelateCompanyLogInfo.class);
    }
}
