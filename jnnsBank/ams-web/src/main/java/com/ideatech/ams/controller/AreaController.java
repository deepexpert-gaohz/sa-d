package com.ideatech.ams.controller;

import com.ideatech.ams.system.area.dto.AreaDto;
import com.ideatech.ams.system.area.service.AreaService;
import com.ideatech.common.msg.ObjectRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by hammer on 2018/2/11.
 */
@RestController
@RequestMapping("/area")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ObjectRestResponse getAreaData(AreaDto area) {
        return new ObjectRestResponse<List<AreaDto>>().result(areaService.selectProvince(area)).rel(true);
    }

    @RequestMapping(value = "/province", method = RequestMethod.GET)
    public ObjectRestResponse getProvinceByFullid(String fullid) {
        return new ObjectRestResponse().result(areaService.selectProvinceByBank(fullid)).rel(true);
    }

    @RequestMapping(value = "/city", method = RequestMethod.GET)
    public ObjectRestResponse getCityByFullidAndProvince(String fullid, String province) {
        return new ObjectRestResponse().result(areaService.selectCityByBankAndProvince(fullid, province)).rel(true);
    }

    @RequestMapping(value = "/area", method = RequestMethod.GET)
    public ObjectRestResponse getAreaByFullidAndCity(String fullid, String city) {
        return new ObjectRestResponse().result(areaService.selectAreaByBankAndCity(fullid, city)).rel(true);
    }

    @RequestMapping(value = "/registLists", method = RequestMethod.GET)
    public ObjectRestResponse getRegistListsByCode(String areaCode) {
        return new ObjectRestResponse().result(areaService.getRegistLists(areaCode)).rel(true);
    }
}


