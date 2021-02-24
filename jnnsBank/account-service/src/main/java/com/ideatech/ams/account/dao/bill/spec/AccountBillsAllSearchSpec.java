package com.ideatech.ams.account.dao.bill.spec;

import com.ideatech.ams.account.dto.AccountBillsAllSearchInfo;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class AccountBillsAllSearchSpec extends IdeaSimpleSpecification<AccountBillsAll, AccountBillsAllSearchInfo> {

    /**
     * @param condition
     */
    public AccountBillsAllSearchSpec(AccountBillsAllSearchInfo condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<AccountBillsAll> queryWraper) {
        addLikeCondition(queryWraper, "acctNo");
        addLikeCondition(queryWraper, "billNo");
        addStartsWidthCondition(queryWraper, "organFullId");
        addEqualsCondition(queryWraper, "billType");
        addNotEqualsCondition(queryWraper, "billTypeEx", "billType");
        addEqualsCondition(queryWraper, "status");
        addEqualsCondition(queryWraper, "createdBy");
        addEqualsCondition(queryWraper, "accountId");
        addEqualsCondition(queryWraper, "pbcSyncStatus");
        addEqualsCondition(queryWraper, "imgaeSyncStatus");
        addEqualsCondition(queryWraper, "eccsSyncStatus");
        addEqualsCondition(queryWraper, "fromSource");
        addEqualsCondition(queryWraper, "acctType");
        addEqualsCondition(queryWraper, "pbcSyncMethod");
        addInCondition(queryWraper, "pbcSyncStatuses", "pbcSyncStatus");
        addInCondition(queryWraper, "eccsSyncStatuses", "eccsSyncStatus");
        addInCondition(queryWraper, "pbcCheckStatuses", "pbcCheckStatus");
        addInCondition(queryWraper, "statuses", "status");
        addInCondition(queryWraper, "createdByes", "createdBy");
        addInCondition(queryWraper, "organFullIdList", "organFullId");
        addEqualsCondition(queryWraper, "pbcCheckStatus");
        addLikeCondition(queryWraper, "depositorName");
//        addLikeCondition(queryWraper, "bankCode");//人行机构号
        addBetweenConditionDate(queryWraper, "beginDate", "endDate", "createdDate");//申请时间
        addBetweenCondition(queryWraper, "pbcCheckBeginDate", "pbcCheckEndDate", "pbcCheckDate");//人行审核日期
        addBetweenCondition(queryWraper, "billBeginDate", "billEndDate", "billDate");//单据日期

        String[] pbcSyncTime = {"syncBeginDate", "syncEndDate", "pbcSyncTime"};
        String[] eccsSyncTime = {"syncBeginDate", "syncEndDate", "eccsSyncTime"};
        addBetweenOrCondition(queryWraper, pbcSyncTime, eccsSyncTime);
//        addBetweenCondition(queryWraper, "pbcSyncBeginDate", "pbcSyncEndDate", "pbcSyncTime");//账管上报时间
//        addBetweenCondition(queryWraper, "eccsSyncBeginDate", "eccsSyncEndDate", "eccsSyncTime");//信用代码上报时间

        addInIncludeNullCondition(queryWraper,"whiteLists","whiteList");
        addEqualsCondition(queryWraper, "openAccountSiteType");
        addInIncludeNullCondition(queryWraper,"imgaeSyncStatuses","imgaeSyncStatus");
    }

}
