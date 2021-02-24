package com.ideatech.ams.system.batch.spec;

import com.ideatech.ams.system.batch.dto.BatchSearchDto;
import com.ideatech.ams.system.batch.entity.BatchPo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class BatchSpec extends IdeaSimpleSpecification<BatchPo, BatchSearchDto> {
    /**
     * @param condition
     */
    public BatchSpec(BatchSearchDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<BatchPo> queryWraper) {
        addLikeCondition(queryWraper, "batchNo");
        addInCondition(queryWraper, "type");
        addBetweenCondition(queryWraper,"processTimeStart","processTimeEnd","processTime");
    }

}
