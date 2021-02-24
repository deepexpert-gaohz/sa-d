package com.ideatech.ams.kyc.service.holiday;

import com.ideatech.ams.kyc.dto.holiday.HolidayDto;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.Date;

public interface HolidayService extends BaseService<HolidayDto> {

    /**
     * 判断是否为工作日
     *
     * @param dateStr
     * @return
     */
    boolean isWorkday(String dateStr) throws Exception;

    /**
     * 判断是否为节假日
     *
     * @param dateStr
     * @return
     */
    boolean isHoliday(String dateStr) throws Exception;

    /**
     * 获取当前日期指定天数工作日后的日期
     *
     * @param dateStr
     * @param days
     * @return
     */
    Date addWorkday(String dateStr, int days) throws Exception;

    /**
     * 获取当前日期指定天数节假日后的日期
     *
     * @param dateStr
     * @param days
     * @return
     */
    Date addHoliday(String dateStr, int days) throws Exception;

    HolidayDto create(HolidayDto info);

    Page<HolidayDto> query(HolidayDto info, Pageable pageable);

    void importExcel(File file);

    void sync(int year);

}
