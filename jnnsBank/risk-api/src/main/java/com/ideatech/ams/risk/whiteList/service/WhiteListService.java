package com.ideatech.ams.risk.whiteList.service;


import com.ideatech.ams.risk.tableManager.dto.TableInfoSearchDto;
import com.ideatech.ams.risk.whiteList.dto.WhiteListDto;
import com.ideatech.ams.risk.whiteList.dto.WhiteListSearchDto;

/**
 * @Author: yinjie
 * @Date: 2019/5/27 10:10
 * @description
 */
public interface WhiteListService {


    //新建，修改
    void saveWhiteList(WhiteListDto whiteListDto);

    //根据id查询
    WhiteListDto findWhiteListDtoById(Long id);

    //删除
    void delWhiteListDto(Long id);

    //accountId重复验证
    WhiteListDto findByAccountId(String accountId);

    //socialUnifiedCode重复验证
    WhiteListDto findBySocialUnifiedCode(String socialUnifiedCode);

    //搜索功能
    WhiteListSearchDto searchWhiteListDto(WhiteListSearchDto whiteListSearchDto);

}
