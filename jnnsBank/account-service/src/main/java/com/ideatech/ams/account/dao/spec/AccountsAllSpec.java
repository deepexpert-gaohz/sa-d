package com.ideatech.ams.account.dao.spec;

import com.ideatech.ams.account.dto.AccountsAllSearchInfo;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class AccountsAllSpec extends IdeaSimpleSpecification<AccountsAll, AccountsAllSearchInfo> {

    /**
     * @param condition
     */
    public AccountsAllSpec(AccountsAllSearchInfo condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<AccountsAll> queryWraper) {
        addStartsWidthCondition(queryWraper, "organFullId");
        addLikeCondition(queryWraper, "acctNo");
        addLikeCondition(queryWraper, "acctName");
        addLikeCondition(queryWraper, "bankCode");
        addLikeCondition(queryWraper, "customerNo");
        addEqualsCondition(queryWraper, "customerLogId");
        addInCondition(queryWraper, "customerLogIdList", "customerLogId");
        addEqualsCondition(queryWraper, "acctType");
        addEqualsCondition(queryWraper, "accountStatus");//账户状态
        addEqualsCondition(queryWraper, "certainOrganFullId", "organFullId");
        addLikeCondition(queryWraper, "bankName");//开户行
        addLikeCondition(queryWraper, "bankCode");//人行机构号
        addBetweenCondition(queryWraper, "beginDateAcctCreate", "endDateAcctCreate", "acctCreateDate");//开户时间
        addInIncludeNullCondition(queryWraper, "string003s", "string003");
        addInIncludeNullCondition(queryWraper,"whiteLists","whiteList");

        addInCondition(queryWraper,"acctTypes", "acctType");
        addEqualsCondition(queryWraper, "openAccountSiteType");

    }
}
