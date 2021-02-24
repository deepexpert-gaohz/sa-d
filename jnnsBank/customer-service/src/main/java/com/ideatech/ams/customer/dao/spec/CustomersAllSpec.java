package com.ideatech.ams.customer.dao.spec;

import com.ideatech.ams.customer.dto.CustomersAllInfo;
import com.ideatech.ams.customer.entity.CustomersAll;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class CustomersAllSpec extends IdeaSimpleSpecification<CustomersAll, CustomersAllInfo> {
    /**
     * @param condition
     */
    public CustomersAllSpec(CustomersAllInfo condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<CustomersAll> queryWraper) {
        addLikeCondition(queryWraper, "customerNo");
        addLikeCondition(queryWraper, "depositorName");
    }
}
