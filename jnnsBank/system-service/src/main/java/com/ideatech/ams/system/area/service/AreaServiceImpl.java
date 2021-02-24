package com.ideatech.ams.system.area.service;

import com.ideatech.ams.system.area.dao.AreaDao;
import com.ideatech.ams.system.area.dto.AreaDto;
import com.ideatech.ams.system.area.entity.Area;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.BeanValueUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;

    @Override
    public List<AreaDto> selectProvince(AreaDto area) {
        String areaCode = area.getAreaCode();
        Integer level = area.getLevel();
        if (level == 1) {
            area.setAreaCode(null);
            area.setLevel(1);

        } else if (level == 2) {
            area.setAreaCode(areaCode == null ? null : areaCode.substring(0, 2) + "%");
            area.setLevel(2);

        } else if (level == 3) {
            area.setAreaCode(areaCode == null ? null : areaCode.substring(0, 4) + "%");
            area.setLevel(3);

        }

        List<Area> areaList = null;
        if (StringUtils.isNotBlank(area.getAreaCode())) {
            areaList = areaDao.findByLevelAndAreaCodeLike(area.getLevel(), area.getAreaCode());
        } else {
            areaList = areaDao.findByLevel(area.getLevel());

        }

        AreaDto areaDto = null;
        List<AreaDto> areaDtoList = new ArrayList<>();
        if (areaList != null && areaList.size() > 0) {
            for (Area a : areaList) {
                areaDto = new AreaDto();
                BeanCopierUtils.copyProperties(a, areaDto);
                if (null == areaDto.getRegCode()) {
                    areaDto.setRegCode("");
                }
                areaDtoList.add(areaDto);
            }
        }

        return areaDtoList;
    }

    @Override
    public String getAreaNameByAreaCode(String areaCode) {
        String returnResult = "";
        if (StringUtils.isBlank(areaCode)) {
            return "";
        }
        List<Area> areaList = areaDao.findByAreaCode(areaCode);
        if (areaList != null && areaList.size() > 0) {
            for (Area area : areaList) {
                if (area.getAreaName().contains("市辖区")) {
                    continue;
                }
                returnResult = area.getAreaName();
            }
        }
        return returnResult;
    }

    @Override
    public List<AreaDto> getRegistAreaCode(String areaCode) {
        List<Area> areaList;
        List<AreaDto> areaInfoList;

        areaList = areaDao.findByAreaCode(areaCode);
        areaInfoList = new ArrayList<AreaDto>();
        AreaDto areaInfo = null;
        for (Area area : areaList) {
            areaInfo = new AreaDto();
            BeanUtils.copyProperties(area, areaInfo);
            areaInfoList.add(areaInfo);
        }
        return areaInfoList;
    }

    @Override
    public Object selectProvinceByBank(String fullid) {
//        organizationDao.findByFullIdLike();

        return null;
    }

    @Override
    public Object selectCityByBankAndProvince(String fullid, String province) {


        return null;
    }

    @Override
    public Object selectAreaByBankAndCity(String fullid, String city) {


        return null;
    }

    @Override
    public AreaDto[] getRegistLists(String areaCode) {
        AreaDto[] array = new AreaDto[3];
        if (StringUtils.isNotBlank(areaCode) && areaCode.length() == 6) {//地区
            List<Area> areaArea = areaDao.findByLevelAndAreaCodeLike(3, areaCode);
            if (areaArea != null && areaArea.size() > 0) {
                AreaDto areaDtoArea = new AreaDto();
                BeanUtils.copyProperties(areaArea.get(0), areaDtoArea);
                array[2] = areaDtoArea;
            } else {
                array[2] = null;
            }
            List<Area> areaCity = areaDao.findByLevelAndAreaCodeLike(2, areaCode.substring(0, 4) + "00");
            if (areaCity != null && areaCity.size() > 0) {//市
                AreaDto areaDtoCity = new AreaDto();
                BeanUtils.copyProperties(areaCity.get(0), areaDtoCity);
                array[1] = areaDtoCity;
            } else {
                array[1] = null;

            }
            List<Area> areaPro = areaDao.findByLevelAndAreaCodeLike(1, areaCode.substring(0, 2) + "0000");
            if (areaPro != null && areaPro.size() > 0) {//省
                AreaDto areaDtoPro = new AreaDto();
                BeanUtils.copyProperties(areaPro.get(0), areaDtoPro);
                array[0] = areaDtoPro;
            } else {
                array[0] = null;
            }
        }
        return array;
    }

    @Override
    public List<AreaDto> findAll() {
        List<AreaDto> areaDtoList = new ArrayList<>();
        List<Area> list = areaDao.findAll();
        if (CollectionUtils.isNotEmpty(list)) {
            AreaDto areaDto;
            for (Area area : list) {
                areaDto = new AreaDto();
                BeanValueUtils.copyProperties(area, areaDto);
                areaDtoList.add(areaDto);
            }
        }
        return areaDtoList;
    }
}
