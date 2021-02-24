package com.ideatech.ams.mivs.spec;

import com.ideatech.ams.mivs.dto.BusinessAcceptTimeLogDto;
import com.ideatech.ams.mivs.dto.bnd.BusinessAcceptTimeNoticeDto;
import com.ideatech.ams.mivs.entity.BusinessAcceptTimeLog;
import com.ideatech.ams.mivs.entity.BusinessAcceptTimeNotice;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * @author jzh
 * @date 2019/7/29.
 */
public class BusinessAcceptTimeNoticeSpec extends IdeaSimpleSpecification<BusinessAcceptTimeNotice, BusinessAcceptTimeNoticeDto> {
    /**
     * @param condition
     */
    public BusinessAcceptTimeNoticeSpec(BusinessAcceptTimeNoticeDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<BusinessAcceptTimeNotice> queryWraper) {
        addEqualsCondition(queryWraper,"curSysDt");
        addEqualsCondition(queryWraper, "nxtSysDt");
        addLikeCondition(queryWraper, "msgId");
        addBetweenCondition(queryWraper, "beginDate", "endDate", "creDtTm");//操作时间
    }
}
