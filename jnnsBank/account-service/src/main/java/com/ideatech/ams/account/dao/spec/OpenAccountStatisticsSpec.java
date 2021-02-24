package com.ideatech.ams.account.dao.spec;

import com.ideatech.ams.account.dto.OpenAccountStatisticsSearchDto;
import com.ideatech.ams.account.view.OpenAccountStatisticsView;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class OpenAccountStatisticsSpec extends IdeaSimpleSpecification<OpenAccountStatisticsView, OpenAccountStatisticsSearchDto> {
    /**
     * @param condition
     */
    public OpenAccountStatisticsSpec(OpenAccountStatisticsSearchDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<OpenAccountStatisticsView> queryWraper) {
        addEqualsCondition(queryWraper, "depositorType");
        addEqualsCondition(queryWraper, "acctType");
        addBetweenCondition(queryWraper, "beginDate", "endDate", "pbcSyncTime");//上报时间
        addBetweenConditionDate(queryWraper, "beginDateApply", "endDateApply", "createdDate");//申请时间
        addStartsWidthCondition(queryWraper,"organFullId");
        addLikeCondition(queryWraper, "acctNo");
        addLikeCondition(queryWraper, "organCode");
        addLikeCondition(queryWraper, "depositorName");
        addEqualsCondition(queryWraper, "openAccountSiteType");
        addInCondition(queryWraper, "userIdList","createdBy");
    }

}
