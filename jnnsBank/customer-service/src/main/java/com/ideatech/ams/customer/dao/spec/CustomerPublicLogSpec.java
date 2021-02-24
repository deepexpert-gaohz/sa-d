package com.ideatech.ams.customer.dao.spec;

import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.entity.CustomerPublicLog;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class CustomerPublicLogSpec extends IdeaSimpleSpecification<CustomerPublicLog, CustomerPublicLogInfo> {
    /**
     * @param condition
     */
    public CustomerPublicLogSpec(CustomerPublicLogInfo condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<CustomerPublicLog> queryWraper) {
        addStartsWidthCondition(queryWraper, "organFullId");
        addLikeCondition(queryWraper, "depositorName");
        addLikeCondition(queryWraper, "legalName");
        addEqualsCondition(queryWraper, "legalIdcardDue");

    }
}
