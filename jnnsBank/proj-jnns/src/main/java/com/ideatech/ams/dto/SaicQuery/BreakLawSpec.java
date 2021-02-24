package com.ideatech.ams.dto.SaicQuery;

import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class BreakLawSpec extends IdeaSimpleSpecification<BreakLawInfo, BreakLaw> {

    /**
     * @param condition
     */
    public BreakLawSpec(BreakLaw condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<BreakLawInfo> queryWraper) {


        addLikeCondition(queryWraper, "cid");
        addLikeCondition(queryWraper, "court");







    }
}
