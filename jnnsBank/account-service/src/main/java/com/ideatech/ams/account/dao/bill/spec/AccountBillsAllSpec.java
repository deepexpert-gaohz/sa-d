package com.ideatech.ams.account.dao.bill.spec;

import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class AccountBillsAllSpec extends IdeaSimpleSpecification<AccountBillsAll, AccountBillsAllInfo> {

    /**
     * @param condition
     */
    public AccountBillsAllSpec(AccountBillsAllInfo condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<AccountBillsAll> queryWraper) {
        addLikeCondition(queryWraper, "acctNo");
        addStartsWidthCondition(queryWraper, "organFullId");
        addEqualsCondition(queryWraper, "billType");
        addNotEqualsCondition(queryWraper, "billTypeEx", "billType");
        addEqualsCondition(queryWraper, "status");
        addEqualsCondition(queryWraper, "pbcSyncStatus");
        addEqualsCondition(queryWraper, "eccsSyncStatus");
        addEqualsCondition(queryWraper, "fromSource");
        addEqualsCondition(queryWraper, "acctType");
        addInCondition(queryWraper, "pbcSyncStatuses", "pbcSyncStatus");
        addInCondition(queryWraper, "eccsSyncStatuses", "eccsSyncStatus");
        addInCondition(queryWraper, "statuses", "status");
        addEqualsCondition(queryWraper, "pbcCheckStatus");
        addEqualsCondition(queryWraper, "pbcSyncMethod");
        //人行信息是否覆盖核心
        addEqualsCondition(queryWraper, "string005");
        addLikeCondition(queryWraper, "depositorName");
        addInIncludeNullCondition(queryWraper,"whiteLists","whiteList");

    }

}
