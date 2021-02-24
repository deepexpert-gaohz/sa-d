package com.ideatech.ams.account.spec;

import com.ideatech.ams.account.dto.BatchSuspendSearchDto;
import com.ideatech.ams.account.entity.BatchSuspendPo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class BatchSuspendSpec extends IdeaSimpleSpecification<BatchSuspendPo, BatchSuspendSearchDto> {
    /**
     * @param condition
     */
    public BatchSuspendSpec(BatchSuspendSearchDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<BatchSuspendPo> queryWraper) {
        addLikeCondition(queryWraper, "depositorName");
        addEqualsCondition(queryWraper, "acctType");
        addEqualsCondition(queryWraper, "syncStatus");
        addStartsWidthCondition(queryWraper,"organFullId");
        addEqualsCondition(queryWraper, "batchNo");
    }

}
