package com.ideatech.ams.dto.SaicQuery;

import com.ideatech.ams.domain.zhjn.ZhjnCustomerInfo;
import com.ideatech.ams.dto.ZhjnCustomerDto;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class DeabbeatSpec extends IdeaSimpleSpecification<DeabbeatInfo, Deabbeat> {


    /**
     * @param condition
     */
    public DeabbeatSpec(Deabbeat condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<DeabbeatInfo> queryWraper) {


        addLikeCondition(queryWraper, "cid");
        addLikeCondition(queryWraper, "court");







    }
}
