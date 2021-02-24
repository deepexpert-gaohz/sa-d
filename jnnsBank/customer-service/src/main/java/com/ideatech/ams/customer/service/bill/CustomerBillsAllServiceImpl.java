package com.ideatech.ams.customer.service.bill;

import com.ideatech.ams.customer.dao.bill.CustomerBillsAllDao;
import com.ideatech.ams.customer.dto.bill.CustomerBillsAllInfo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.CompanyIfType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author vantoo
 * @date 10:01 2018/5/25
 */
@Service
@Transactional
@Slf4j
public class CustomerBillsAllServiceImpl implements CustomerBillsAllService {

    @Autowired
    private CustomerBillsAllDao customerBillsAllDao;

    @Override
    public void save(CustomerBillsAllInfo customerBillsAllInfo) {

    }

    @Override
    public long countUnfinishedByCustomerId(Long customerId) {
        return customerBillsAllDao.countByFinalStatusAndCustomerLogId(CompanyIfType.No, customerId);
    }

    @Override
    public long countUnfinishedByCustomerNo(String customerNo) {
        return customerBillsAllDao.countByFinalStatusAndCustomerNo(CompanyIfType.No, customerNo);
    }

    @Override
    public List<CustomerBillsAllInfo> findByOrganFullIdLike(String organFullId) {
        return ConverterService.convertToList(customerBillsAllDao.findByOrganFullIdLike(organFullId),CustomerBillsAllInfo.class);
    }

    @Override
    public List<CustomerBillsAllInfo> findByCustomerId(Long customerId) {
        return ConverterService.convertToList(customerBillsAllDao.findByCustomerLogId(customerId),CustomerBillsAllInfo.class);
    }
}
