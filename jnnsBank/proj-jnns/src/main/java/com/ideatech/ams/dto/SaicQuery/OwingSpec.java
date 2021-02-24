package com.ideatech.ams.dto.SaicQuery;

import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class OwingSpec extends IdeaSimpleSpecification<OwingInfo, Owing> {

    /**
     * @param condition
     */
    public OwingSpec(Owing condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<OwingInfo> queryWraper) {


        addLikeCondition(queryWraper, "cid");
        addLikeCondition(queryWraper, "name");
        addLikeCondition(queryWraper, "organ");







    }
}
