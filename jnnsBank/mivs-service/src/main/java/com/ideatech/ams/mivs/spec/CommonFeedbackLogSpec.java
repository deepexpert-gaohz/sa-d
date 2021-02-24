package com.ideatech.ams.mivs.spec;

import com.ideatech.ams.mivs.dto.CommonFeedbackLogDto;
import com.ideatech.ams.mivs.entity.CommonFeedbackLog;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * @author jzh
 * @date 2019-08-06.
 */
public class CommonFeedbackLogSpec extends IdeaSimpleSpecification<CommonFeedbackLog, CommonFeedbackLogDto> {
    /**
     * @param condition
     */
    public CommonFeedbackLogSpec(CommonFeedbackLogDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<CommonFeedbackLog> queryWraper) {
        //addLikeCondition(queryWraper, "msgLogId");
        addEqualsCondition(queryWraper, "msgLogId");
        addLikeCondition(queryWraper, "contactNm");
        addLikeCondition(queryWraper, "contactNb");
        addEqualsCondition(queryWraper,"msgType");
        addLikeCondition(queryWraper, "orgName");
        addLikeCondition(queryWraper, "createdUserName");
        addBetweenCondition(queryWraper, "beginDate", "endDate", "createTime");//操作时间
        addStartsWidthCondition(queryWraper, "organFullId");//权限控制
    }
}
