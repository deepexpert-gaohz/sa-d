package com.ideatech.ams.dao.spec;

import com.ideatech.ams.domain.SyncCompare;
import com.ideatech.ams.dto.SyncCompareInfo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * 分页查询条件
 *
 * @auther ideatech
 * @create 2018-11-29 11:08 AM
 **/
public class SyncCompareSpec extends IdeaSimpleSpecification<SyncCompare, SyncCompareInfo> {

    public SyncCompareSpec(SyncCompareInfo condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<SyncCompare> queryWraper) {
        addStartsWidthCondition(queryWraper, "organFullId");
        addLikeCondition(queryWraper, "acctNo");
        addLikeCondition(queryWraper, "organCode");
        addLikeCondition(queryWraper, "depositorName");
        addInCondition(queryWraper,"acctTypeList","acctType");
        addInCondition(queryWraper,"pbcStartsList","pbcStarts");
        addInCondition(queryWraper,"eccsStartsList","eccsStarts");
        addEqualsCondition(queryWraper, "pbcStarts");
        addEqualsCondition(queryWraper, "eccsStarts");
        addEqualsCondition(queryWraper, "kaixhubz");
        addBetweenCondition(queryWraper, "businessbeginDate", "businessendDate", "businessDate");
    }
}
