package com.ideatech.ams.account.dao.spec;

import com.ideatech.ams.account.dto.SyncHistoryDto;
import com.ideatech.ams.account.entity.SyncHistoryPo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class SyncHistoryPoSpec extends IdeaSimpleSpecification<SyncHistoryPo, SyncHistoryDto> {
    /**
     * @param condition
     */
    public SyncHistoryPoSpec(SyncHistoryDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<SyncHistoryPo> queryWraper) {
        addStartsWidthCondition(queryWraper, "organFullId");
        addLikeCondition(queryWraper, "acctNo");
        addLikeCondition(queryWraper, "organName");
        addLikeCondition(queryWraper, "organCode");
        addLikeCondition(queryWraper, "syncName");
        addEqualsCondition(queryWraper, "acctType");
        addEqualsCondition(queryWraper, "billType");
        addEqualsCondition(queryWraper, "syncType");
        addEqualsCondition(queryWraper, "syncStatus");

        addBetweenCondition(queryWraper,"beginDate","endDate","syncDateTime");
    }
}
