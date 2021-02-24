package com.ideatech.ams.mivs.spec;

import com.ideatech.ams.mivs.dto.AnnouncementInformationConfirmLogDto;
import com.ideatech.ams.mivs.entity.AnnouncementInformationConfirmLog;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * @author jzh
 * @date 2019-09-03.
 */
public class AnnouncementInformationConfirmLogSpec extends IdeaSimpleSpecification<AnnouncementInformationConfirmLog, AnnouncementInformationConfirmLogDto> {
    /**
     * @param condition
     */
    public AnnouncementInformationConfirmLogSpec(AnnouncementInformationConfirmLogDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<AnnouncementInformationConfirmLog> queryWraper) {
        //TODO 多条件查询
        addLikeCondition(queryWraper, "msgId");
        addLikeCondition(queryWraper, "instgDrctPty");
        addLikeCondition(queryWraper, "instgPty");
        addLikeCondition(queryWraper, "msgCntt");

        addLikeCondition(queryWraper, "orgName");
        addLikeCondition(queryWraper, "createdUserName");
        addBetweenCondition(queryWraper, "beginDate", "endDate", "createTime");//操作时间
        addStartsWidthCondition(queryWraper, "organFullId");//权限控制
    }
}
