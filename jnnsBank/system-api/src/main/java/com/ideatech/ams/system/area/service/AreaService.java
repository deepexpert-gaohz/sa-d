package com.ideatech.ams.system.area.service;

import com.ideatech.ams.system.area.dto.AreaDto;

import java.util.List;

public interface AreaService {
    List<AreaDto> selectProvince(AreaDto area);

    List<AreaDto> getRegistAreaCode(String areaCode);

    Object selectProvinceByBank(String fullid);

    Object selectCityByBankAndProvince(String fullid, String province);

    Object selectAreaByBankAndCity(String fullid, String city);

    String getAreaNameByAreaCode(String regCity);

    /**
     * 根据地区code获取省市区列表
     *
     * @param areaCode
     * @return
     */
    AreaDto[] getRegistLists(String areaCode);

    /**
     * 查找所有注册地地区代码
     *
     * @return
     */
    List<AreaDto> findAll();
}
