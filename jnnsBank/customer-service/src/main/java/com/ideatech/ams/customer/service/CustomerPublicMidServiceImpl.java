package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dao.CustomerPublicMidDao;
import com.ideatech.ams.customer.dto.CustomerPublicMidInfo;
import com.ideatech.ams.customer.entity.CustomerPublicMid;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.util.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author vantoo
 * @date 15:49 2018/5/25
 */
@Service
@Transactional
@Slf4j
public class CustomerPublicMidServiceImpl implements CustomerPublicMidService {

    @Autowired
    private CustomerPublicMidDao customerPublicMidDao;

    @Override
    public CustomerPublicMidInfo getOne(Long id) {
        CustomerPublicMid customerPublicMid = customerPublicMidDao.findOne(id);
        return po2dto(customerPublicMid);
    }

    @Override
    public void save(CustomerPublicMidInfo customerPublicMidInfo) {
        CustomerPublicMid customerPublicMid = null;
        if (customerPublicMidInfo.getId() != null) {
            customerPublicMid = customerPublicMidDao.findOne(customerPublicMidInfo.getId());
        }
        if (customerPublicMid == null) {
            customerPublicMid = new CustomerPublicMid();
        }
        BeanCopierUtils.copyProperties(customerPublicMidInfo, customerPublicMid);
        CustomerPublicMid custPublicMid = customerPublicMidDao.save(customerPublicMid);
        customerPublicMidInfo.setId(custPublicMid.getId());
    }

    @Override
    public CustomerPublicMidInfo getByCustomerNo(String customerNo) {
        CustomerPublicMid customerPublicMid = customerPublicMidDao.findByCustomerNo(customerNo);
        return po2dto(customerPublicMid);
    }

    @Override
    public CustomerPublicMidInfo getByBillId(Long billId) {
        return ConverterService.convert(customerPublicMidDao.findFirstByRefBillIdOrderByIdDesc(billId), CustomerPublicMidInfo.class);
    }

    @Override
    public void delete(Long id) {
        customerPublicMidDao.delete(id);
    }

    private CustomerPublicMidInfo po2dto(CustomerPublicMid customerPublicMid) {
        if (customerPublicMid != null) {
            CustomerPublicMidInfo customerPublicMidInfo = new CustomerPublicMidInfo();
            BeanCopierUtils.copyProperties(customerPublicMid, customerPublicMidInfo);
            return customerPublicMidInfo;
        } else {
            return null;
        }
    }

    @Override
    public List<CustomerPublicMidInfo> findByOrganFullIdLike(String organFullId) {
        return ConverterService.convertToList(customerPublicMidDao.findByOrganFullIdLike(organFullId),CustomerPublicMidInfo.class);
    }

    @Override
    public List<CustomerPublicMidInfo> findByCustomerId(Long customerId) {
        return ConverterService.convertToList(customerPublicMidDao.findByCustomerId(customerId),CustomerPublicMidInfo.class);
    }
}
