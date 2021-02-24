package com.ideatech.ams.customer.dao.spec;

import com.ideatech.ams.customer.dto.neecompany.FreshCompanyDto;
import com.ideatech.ams.customer.entity.newcompany.FreshCompany;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class FreshCompanySpec extends IdeaSimpleSpecification<FreshCompany, FreshCompanyDto> {
    /**
     * @param condition
     */
    public FreshCompanySpec(FreshCompanyDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<FreshCompany> queryWraper) {
        addLikeCondition(queryWraper,"name");
        addLikeCondition(queryWraper,"address");
        addLikeCondition(queryWraper,"unityCreditCode");
        addLikeCondition(queryWraper,"provinceName");
        addLikeCondition(queryWraper,"cityName");
        addLikeCondition(queryWraper,"areaName");
        addBetweenCondition(queryWraper, "beginDate", "endDate", "openDate");

    }

}
