package com.ideatech.ams.risk.tableManager.service;


import com.ideatech.ams.risk.tableManager.dto.TableInfoDto;
import com.ideatech.ams.risk.tableManager.dto.TableInfoSearchDto;

public interface TableInfoService {
     void saveTableInfo(TableInfoDto tableInfoDto);//新建，修改

     TableInfoSearchDto searchTableInfoDto(TableInfoSearchDto tableInfoSearchDto);

     TableInfoDto findTableInfoDtoById(Long id);//根据id查询

     void delTableInfo(Long id);//删除

     TableInfoDto findByEname(String cname);



}
