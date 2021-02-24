package com.ideatech.ams.kyc.dao.holiday;

import com.ideatech.ams.kyc.entity.holiday.HolidayPo;
import com.ideatech.ams.kyc.enums.HolidayTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolidayDao extends JpaRepository<HolidayPo, Long>, JpaSpecificationExecutor<HolidayPo> {

    long countByHolidayTypeAndDateStr(HolidayTypeEnum type, String dateStr);

    List<HolidayPo> findByDateStr(String dateStr);

    long countByDateStr(String dateStr);

    long countByDateStrLike(String dateStr);
}
