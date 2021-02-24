package com.ideatech.ams.customer.dao.spec;

import com.ideatech.ams.customer.dto.CustomerPublicInfo;
import com.ideatech.ams.customer.entity.CustomerPublic;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class CustomerPublicSpec extends IdeaSimpleSpecification<CustomerPublic, CustomerPublicInfo> {
    /**
     * @param condition
     */
    public CustomerPublicSpec(CustomerPublicInfo condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<CustomerPublic> queryWraper) {
        addEqualsCondition(queryWraper, "legalIdcardDue");
        addBetweenCondition(queryWraper, "legalIdcardBeginDate", "legalIdcardEndDate", "legalIdcardDue");
        addStartsWidthCondition(queryWraper, "organFullId");
        addLikeCondition(queryWraper, "depositorName");
        addLikeCondition(queryWraper, "legalName");
    }

}
