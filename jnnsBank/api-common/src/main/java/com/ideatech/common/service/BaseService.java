package com.ideatech.common.service;

import java.util.List;

public interface BaseService<DTO>  {
    DTO findById(Long id);

    DTO save(DTO dto);

    void deleteById(Long id);

    List<DTO> list();

    void insert(DTO dto);

    void insert(List<DTO> dto);

    DTO update(DTO dto);
}
