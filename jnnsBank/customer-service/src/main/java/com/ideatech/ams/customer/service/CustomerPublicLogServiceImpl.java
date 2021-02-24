package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dao.CustomerPublicLogDao;
import com.ideatech.ams.customer.dao.spec.CustomerPublicLogSpec;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.entity.CustomerPublicLog;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

/**
 * @author vantoo
 * @date 15:49 2018/5/25
 */
@Service
@Transactional
@Slf4j
public class CustomerPublicLogServiceImpl implements CustomerPublicLogService {

    @Autowired
    private CustomerPublicLogDao customerPublicLogDao;

    @Override
    public CustomerPublicLogInfo getOne(Long id) {
        return po2dto(customerPublicLogDao.findOne(id));
    }

    @Override
    public void save(CustomerPublicLogInfo customerPublicLogInfo) {
        CustomerPublicLog customerPublicLog = null;
        if (customerPublicLogInfo.getId() != null) {
            customerPublicLog = customerPublicLogDao.findOne(customerPublicLogInfo.getId());
        }
        if (customerPublicLog == null) {
            customerPublicLog = new CustomerPublicLog();
        }
        BeanCopierUtils.copyProperties(customerPublicLogInfo, customerPublicLog);
        CustomerPublicLog custPublicLog = customerPublicLogDao.save(customerPublicLog);
        customerPublicLogInfo.setId(custPublicLog.getId());
    }

    @Override
    public CustomerPublicLogInfo getMaxSeq(Long customerId) {
        return po2dto(customerPublicLogDao.findTop1ByCustomerIdOrderBySequenceDesc(customerId));
    }

    @Override
    public CustomerPublicLogInfo getByCustomerNo(String customerNo) {
        return po2dto(customerPublicLogDao.findByCustomerNo(customerNo));
    }

    @Override
    public Map<Long, CustomerPublicLogInfo> findAllInMap() {
        List<CustomerPublicLog> all = customerPublicLogDao.findAll();
        Map<Long, CustomerPublicLogInfo> map = new HashMap<>();
        for(CustomerPublicLog customerPublicLog : all){
            CustomerPublicLogInfo customerPublicLogInfo = po2dto(customerPublicLog);
            if(customerPublicLogInfo !=null){
                map.put(customerPublicLogInfo.getId(),customerPublicLogInfo);
            }
        }
        return map;
    }

    @Override
    public List<CustomerPublicLogInfo> getByCustomerId(Long customerId) {
        List<CustomerPublicLog> customerPublicLogs = customerPublicLogDao.findByCustomerId(customerId);
        return ConverterService.convertToList(customerPublicLogs,CustomerPublicLogInfo.class);
    }

    @Override
    public List<CustomerPublicLogInfo> getByDepositorName(String depositorName) {
        List<CustomerPublicLog> customerPublicLogs = customerPublicLogDao.findByDepositorName(depositorName);
        return ConverterService.convertToList(customerPublicLogs,CustomerPublicLogInfo.class);
    }

    private CustomerPublicLogInfo po2dto(CustomerPublicLog customerPublicLog) {
        if (customerPublicLog != null) {
            CustomerPublicLogInfo info = new CustomerPublicLogInfo();
            BeanCopierUtils.copyProperties(customerPublicLog, info);
            return info;
        } else {
            return null;
        }
    }


    @Override
    public List<CustomerPublicLogInfo> findByOrganFullIdLike(String organFullId) {
        return ConverterService.convertToList(customerPublicLogDao.findByOrganFullIdLike(organFullId),CustomerPublicLogInfo.class);
    }
}
