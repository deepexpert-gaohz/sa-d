package com.ideatech.ams.account.dao;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface AllBillsPublicDao {

    Page<AllBillsPublicDTO> findPage(String sql, Map<String, Object> param, Pageable pageable);
    
    Page findPageMap(String sql, Map<String, Object> param, Pageable pageable);

    AllBillsPublicDTO findById(Long id);

    Long getCount(String sql, Map<String, Object> param);

    List<AllBillsPublicDTO> findUpdateAcctTypeList();

}
