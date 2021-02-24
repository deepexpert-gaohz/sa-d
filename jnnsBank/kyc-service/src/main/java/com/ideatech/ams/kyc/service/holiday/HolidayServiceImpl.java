package com.ideatech.ams.kyc.service.holiday;

import com.ideatech.ams.kyc.dao.holiday.HolidayDao;
import com.ideatech.ams.kyc.dao.spec.HolidaySpec;
import com.ideatech.ams.kyc.dto.holiday.HolidayDto;
import com.ideatech.ams.kyc.entity.holiday.HolidayPo;
import com.ideatech.ams.kyc.enums.HolidayTypeEnum;
import com.ideatech.ams.kyc.service.SaicRequestService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.service.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class HolidayServiceImpl extends BaseServiceImpl<HolidayDao, HolidayPo, HolidayDto> implements HolidayService {

    static final String[] PARSE_PATTERNS = {"yyyy-MM-dd", "yyyyMMdd", "yyyy年MM月dd日"};

    @Autowired
    private SaicRequestService saicRequestService;

    @Override
    public boolean isWorkday(String dateStr) throws Exception {
        Date date = DateUtils.parseDate(dateStr, PARSE_PATTERNS);
        String dateStrdb = DateFormatUtils.ISO_DATE_FORMAT.format(date);
        List<HolidayPo> holidayList = getBaseDao().findByDateStr(dateStrdb);
        if (CollectionUtils.isNotEmpty(holidayList)) {
            return holidayList.get(0).getHolidayType() == HolidayTypeEnum.HOLIDAY_WORK;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
        }
    }

    @Override
    public boolean isHoliday(String dateStr) throws Exception {
        Date date = DateUtils.parseDate(dateStr, PARSE_PATTERNS);
        String dateStrdb = DateFormatUtils.ISO_DATE_FORMAT.format(date);
        List<HolidayPo> holidayList = getBaseDao().findByDateStr(dateStrdb);
        if (CollectionUtils.isNotEmpty(holidayList)) {
            return holidayList.get(0).getHolidayType() == HolidayTypeEnum.WORKDAY_REST;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
        }
    }

    @Override
    public Date addWorkday(String dateStr, int days) throws Exception {
        Date date = DateUtils.parseDate(dateStr, PARSE_PATTERNS);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int i = 1;
        int absDays = Math.abs(days);
        int step = (absDays == days ? 1 : -1);
        while (i <= absDays) {
            calendar.add(Calendar.DATE, step);

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            String dateStrdb = DateFormatUtils.ISO_DATE_FORMAT.format(calendar);

            List<HolidayPo> holidayList = getBaseDao().findByDateStr(dateStrdb);
            if (CollectionUtils.isNotEmpty(holidayList)) {
                if (holidayList.get(0).getHolidayType() == HolidayTypeEnum.HOLIDAY_WORK) {
                    i++;
                }
            } else {
                if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                    i++;
                }
            }
        }
        return calendar.getTime();
    }

    @Override
    public Date addHoliday(String dateStr, int days) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HolidayDto create(HolidayDto info) {
        long count = getBaseDao().countByDateStr(info.getDateStr());
        if (count > 0) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "该日期已存在！");
        }
        HolidayPo holiday = new HolidayPo();
        holiday.setDateStr(info.getDateStr());
        holiday.setHolidayType(info.getHolidayType());
        getBaseDao().save(holiday);
        return info;
    }

    @Override
    public Page<HolidayDto> query(HolidayDto info, Pageable pageable) {
        Page<HolidayPo> holidays = getBaseDao().findAll(new HolidaySpec(info), pageable);
        return new PageImpl<HolidayDto>(ConverterService.convertToList(holidays.getContent(), HolidayDto.class), pageable, holidays.getTotalElements());
    }

    @Override
    public void importExcel(File file) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sync(int year) {
        List<HolidayDto> holidayDtoList = saicRequestService.getHolidayDto(year);
        for (HolidayDto holidayDto : holidayDtoList) {
            try {
                create(holidayDto);
            } catch (BizServiceException e) {
                //ignore
            }
        }
    }

}
