package com.ideatech.ams.system.area.dao;

import com.ideatech.ams.system.area.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AreaDao extends JpaRepository<Area, Long>, JpaSpecificationExecutor<Area> {


    List<Area> findByLevelAndAreaCodeLike(Integer level, String areaCode);

    List<Area> findByLevel(Integer level);

    List<Area> findByAreaCode(String areaCode);
}
