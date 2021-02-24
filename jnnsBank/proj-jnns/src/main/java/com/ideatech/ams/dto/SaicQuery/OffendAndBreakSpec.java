package com.ideatech.ams.dto.SaicQuery;

import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class OffendAndBreakSpec extends IdeaSimpleSpecification<OffendAndBreakInfo, OffenderAndBreakTLaw> {


    /**
     * @param condition
     */
    public OffendAndBreakSpec(OffenderAndBreakTLaw condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<OffendAndBreakInfo> queryWraper) {


       /* addLikeCondition(queryWraper, "cid");
        addLikeCondition(queryWraper, "name");
        addLikeCondition(queryWraper, "organ");*/







    }
}
