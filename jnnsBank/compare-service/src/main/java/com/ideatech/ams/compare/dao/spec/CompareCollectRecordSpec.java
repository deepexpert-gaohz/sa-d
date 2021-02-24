package com.ideatech.ams.compare.dao.spec;

import com.ideatech.ams.compare.dto.CompareCollectRecordDto;
import com.ideatech.ams.compare.entity.CompareCollectRecord;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class CompareCollectRecordSpec extends IdeaSimpleSpecification<CompareCollectRecord, CompareCollectRecordDto> {
    /**
     * @param condition
     */
    public CompareCollectRecordSpec(CompareCollectRecordDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<CompareCollectRecord> queryWraper) {
        addEqualsCondition(queryWraper, "compareTaskId");
        addEqualsCondition(queryWraper, "collectState");
        addEqualsCondition(queryWraper, "dataSourceType");
    }
}
