package com.ideatech.ams.system.trace.spec;

import com.ideatech.ams.system.trace.dto.UserTraceSearchDTO;
import com.ideatech.ams.system.trace.entity.UserTrace;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * @author jzh
 * @date 2019-11-04.
 */
public class UserTraceSpec extends IdeaSimpleSpecification<UserTrace, UserTraceSearchDTO> {

    public UserTraceSpec(UserTraceSearchDTO condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<UserTrace> queryWraper) {
        addStartsWidthCondition(queryWraper, "organFullId");
        addLikeCondition(queryWraper,"username");
        addEqualsCondition(queryWraper,"operateModule");
        addEqualsCondition(queryWraper,"operateType");
        addEqualsCondition(queryWraper,"operateResult");
        addBetweenConditionDate(queryWraper,"beginDate","endDate","operateDate");
    }
}
