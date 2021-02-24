package com.ideatech.ams.customer.dao.spec;

import com.ideatech.ams.customer.dto.illegal.IllegalQueryDto;
import com.ideatech.ams.customer.entity.illegal.IllegalQuery;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class IllegalQuerySpec extends IdeaSimpleSpecification<IllegalQuery, IllegalQueryDto> {
    /**
     * @param condition
     */
    public IllegalQuerySpec(IllegalQueryDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<IllegalQuery> queryWraper) {
        addEqualsCondition(queryWraper, "illegalQueryBatchId");
        addEqualsCondition(queryWraper, "illegalStatus");
        addEqualsCondition(queryWraper, "changemess");
        addLikeCondition(queryWraper, "organCode");
        addLikeCondition(queryWraper, "regNo");
        addLikeCondition(queryWraper, "companyName");
        addLikeCondition(queryWraper, "saicStatus");
        //addStartsWidthCondition(queryWraper,"organFullId");
        addEqualsCondition(queryWraper, "fileDueExpired");
    }
}
