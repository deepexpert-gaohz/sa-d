package com.ideatech.ams.mivs.spec;

import com.ideatech.ams.mivs.dto.ad.AnnouncementInformationDto;
import com.ideatech.ams.mivs.dto.end.EnterpriseAbnormalNoticeDto;
import com.ideatech.ams.mivs.entity.AnnouncementInformation;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * @author jzh
 * @date 2019/7/25.
 */
public class AnnouncementInformationSpec extends IdeaSimpleSpecification<AnnouncementInformation, AnnouncementInformationDto> {
    /**
     * @param condition
     */
    public AnnouncementInformationSpec(AnnouncementInformationDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<AnnouncementInformation> queryWraper) {
        addLikeCondition(queryWraper, "msgId");
        addEqualsCondition(queryWraper,"rplyFlag");
        addLikeCondition(queryWraper, "msgCntt");
        addBetweenCondition(queryWraper, "beginDate", "endDate", "creDtTm");
    }
}
