package com.ideatech.ams.mivs.spec;

import com.ideatech.ams.mivs.dto.BusinessAcceptTimeLogDto;
import com.ideatech.ams.mivs.entity.BusinessAcceptTimeLog;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * @author jzh
 * @date 2019/7/29.
 */
public class BusinessAcceptTimeLogSpec extends IdeaSimpleSpecification<BusinessAcceptTimeLog, BusinessAcceptTimeLogDto> {
    /**
     * @param condition
     */
    public BusinessAcceptTimeLogSpec(BusinessAcceptTimeLogDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<BusinessAcceptTimeLog> queryWraper) {
        addLikeCondition(queryWraper, "sysInd");
        addEqualsCondition(queryWraper,"queDt");
        addLikeCondition(queryWraper, "orgName");
        addLikeCondition(queryWraper, "createdUserName");
        addBetweenCondition(queryWraper, "beginDate", "endDate", "createTime");//操作时间
        addStartsWidthCondition(queryWraper, "organFullId");//权限控制
    }
}
