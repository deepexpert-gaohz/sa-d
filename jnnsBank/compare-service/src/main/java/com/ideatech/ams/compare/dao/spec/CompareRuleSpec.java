package com.ideatech.ams.compare.dao.spec;

import com.ideatech.ams.compare.dto.CompareRuleSearchDto;
import com.ideatech.ams.compare.entity.CompareRule;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * @author jzh
 * @date 2019/1/16.
 */
public class CompareRuleSpec extends IdeaSimpleSpecification<CompareRule, CompareRuleSearchDto> {

    /**
     * @param condition
     */
    public CompareRuleSpec(CompareRuleSearchDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<CompareRule> queryWraper) {
        addBetweenCondition(queryWraper,"beginDate","endDate","createTime");
        addLikeCondition(queryWraper,"name");
        addLikeCondition(queryWraper,"creater");
        addLikeCondition(queryWraper,"organFullId");
        addInCondition(queryWraper,"fullIdList","organFullId");
        addNotEqualsCondition(queryWraper,"createdBy");
    }
}
