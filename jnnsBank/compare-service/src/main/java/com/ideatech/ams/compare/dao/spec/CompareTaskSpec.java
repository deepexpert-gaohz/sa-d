package com.ideatech.ams.compare.dao.spec;

import com.ideatech.ams.compare.dto.CompareTaskSearchDto;
import com.ideatech.ams.compare.entity.CompareTask;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class CompareTaskSpec extends IdeaSimpleSpecification<CompareTask, CompareTaskSearchDto> {
    /**
     * @param condition
     */
    public CompareTaskSpec(CompareTaskSearchDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<CompareTask> queryWraper) {
        addLikeCondition(queryWraper,"name");
        addLikeCondition(queryWraper,"organFullId");
        addEqualsCondition(queryWraper,"state");
        addEqualsCondition(queryWraper,"taskType");
        addLikeCondition(queryWraper,"createName");
        addBetweenConditionDate(queryWraper, "beginDate", "endDate", "createdDate");//创建时间
        addInCondition(queryWraper,"fullIdList","organFullId");
        addNotEqualsCondition(queryWraper,"createdBy");
    }
}
