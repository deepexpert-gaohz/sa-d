package com.ideatech.ams.account.dao.spec;

import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.vo.AccountStatisticsInfoVo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class AccountsAllStatisticsInfoSpec extends IdeaSimpleSpecification<AccountsAll, AccountStatisticsInfoVo> {
    /**
     * @param condition
     */
    public AccountsAllStatisticsInfoSpec(AccountStatisticsInfoVo condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<AccountsAll> queryWraper) {
        addStartsWidthCondition(queryWraper, "organFullId");
        addEqualsCondition(queryWraper, "accountStatus");
        addEqualsCondition(queryWraper, "acctType");
        addInIncludeNullCondition(queryWraper, "string003s", "string003");

    }

}
