package com.ideatech.ams.account.dao.spec;

import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.entity.AccountPublic;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class AccountPublicSpec extends IdeaSimpleSpecification<AccountPublic, AccountPublicInfo> {
    /**
     * @param condition
     */
    public AccountPublicSpec(AccountPublicInfo condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<AccountPublic> queryWraper) {
        addStartsWidthCondition(queryWraper, "organFullId");
        addEqualsCondition(queryWraper, "operatorIdcardDue");

    }
}
