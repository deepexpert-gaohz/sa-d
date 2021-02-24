package com.ideatech.ams.controller.holiday;

import com.ideatech.ams.controller.BaseController;
import com.ideatech.ams.kyc.dto.holiday.HolidayDto;
import com.ideatech.ams.kyc.service.holiday.HolidayService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;

@Controller
@RequestMapping("/holiday")
@Slf4j
public class HolidayController extends BaseController<HolidayService, HolidayDto> {

    @GetMapping("/page")
    public ResultDto query(HolidayDto holidayDto){
        return ResultDtoFactory.toAckData(getBaseService().query(holidayDto,null));
    }

    @GetMapping("/sync")
    public ResultDto sync() {
        Calendar calendar = Calendar.getInstance();
        getBaseService().sync(calendar.get(Calendar.YEAR));
        return ResultDtoFactory.toAck();
    }

}
