package com.ideatech.ams.account.dao;

import com.ideatech.ams.account.dto.AllAccountPublicDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface AllAccountPublicDao {

    AllAccountPublicDTO findById(Long id);

    Page<AllAccountPublicDTO> findPage(String sql, Map<String, Object> param, Pageable pageable);
}
