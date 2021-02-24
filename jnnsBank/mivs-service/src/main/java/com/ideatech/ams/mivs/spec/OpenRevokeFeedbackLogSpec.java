package com.ideatech.ams.mivs.spec;

import com.ideatech.ams.mivs.dto.OpenRevokeFeedbackLogDto;
import com.ideatech.ams.mivs.entity.OpenRevokeFeedbackLog;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * @author jzh
 * @date 2019-08-08.
 */
public class OpenRevokeFeedbackLogSpec  extends IdeaSimpleSpecification<OpenRevokeFeedbackLog, OpenRevokeFeedbackLogDto> {
    /**
     * @param condition
     */
    public OpenRevokeFeedbackLogSpec(OpenRevokeFeedbackLogDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<OpenRevokeFeedbackLog> queryWraper) {
        addLikeCondition(queryWraper, "entNm");
        addLikeCondition(queryWraper, "traNm");
        addLikeCondition(queryWraper, "uniSocCdtCd");
        addEqualsCondition(queryWraper, "acctSts");
        addEqualsCondition(queryWraper, "chngDt");

        addLikeCondition(queryWraper, "orgName");
        addLikeCondition(queryWraper, "createdUserName");
        addBetweenCondition(queryWraper, "beginDate", "endDate", "createTime");//操作时间
        addStartsWidthCondition(queryWraper, "organFullId");//权限控制
    }
}
