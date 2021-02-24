package com.ideatech.ams.account.dao.spec;

import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.dto.AccountPublicLogInfo;
import com.ideatech.ams.account.entity.AccountPublic;
import com.ideatech.ams.account.entity.AccountPublicLog;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class AccountPublicLogSpec extends IdeaSimpleSpecification<AccountPublicLog, AccountPublicLogInfo> {
    /**
     * @param condition
     */
    public AccountPublicLogSpec(AccountPublicLogInfo condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<AccountPublicLog> queryWraper) {
        addStartsWidthCondition(queryWraper, "organFullId");
        addBetweenCondition(queryWraper, "operatorIdcardBeginDate", "operatorIdcardEndDate", "operatorIdcardDue");
        addEqualsCondition(queryWraper, "operatorIdcardDue");

    }
}
