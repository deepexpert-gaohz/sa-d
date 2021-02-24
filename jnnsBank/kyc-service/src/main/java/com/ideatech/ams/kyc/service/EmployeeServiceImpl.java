package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dao.EmployeeDao;
import com.ideatech.ams.kyc.dto.EmployeeDto;
import com.ideatech.ams.kyc.entity.Employee;
import com.ideatech.common.util.BeanCopierUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    @Override
    public void insertBatch(Long saidInfoId,List<EmployeeDto> employeeDtoList){
        Employee employee = null;

        int size = employeeDtoList.size();
        if(size == 0){
            return;
        }

        //设置主键
        for (EmployeeDto employeeDto: employeeDtoList) {
            employee = new Employee();
            BeanCopierUtils.copyProperties(employeeDto, employee);
//            employee.setId(Calendar.getInstance().getTimeInMillis());
            employee.setSaicinfoId(saidInfoId);
            employeeDao.save(employee);
        }

    }

}
