package com.ideatech.ams.compare.dao.spec;

import com.ideatech.ams.compare.dto.CompareResultDto;
import com.ideatech.ams.compare.entity.CompareResult;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class CompareResultSpec extends IdeaSimpleSpecification<CompareResult, CompareResultDto> {
    /**
     * @param condition
     */
    public CompareResultSpec(CompareResultDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<CompareResult> queryWraper) {
        addEqualsCondition(queryWraper, "compareTaskId");
        addEqualsCondition(queryWraper, "match");
        addStartsWidthCondition(queryWraper, "organFullId");
    }
}
