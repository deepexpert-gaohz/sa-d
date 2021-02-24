package com.ideatech.ams.kyc.dao.spec;

import com.ideatech.ams.kyc.dto.holiday.HolidayDto;
import com.ideatech.ams.kyc.entity.holiday.HolidayPo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class HolidaySpec extends IdeaSimpleSpecification<HolidayPo, HolidayDto> {

    public HolidaySpec(HolidayDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<HolidayPo> queryWraper) {
        addLikeCondition(queryWraper, "dateStr");
        addEqualsCondition(queryWraper, "holidayType");
    }

}
