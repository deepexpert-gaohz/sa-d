package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    void insertBatch(Long saidInfoId,List<EmployeeDto> employeeDtoList);

}
