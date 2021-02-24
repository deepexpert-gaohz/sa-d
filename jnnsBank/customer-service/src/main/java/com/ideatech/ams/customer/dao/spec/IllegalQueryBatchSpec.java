package com.ideatech.ams.customer.dao.spec;

import com.ideatech.ams.customer.dto.illegal.IllegalQueryBatchDto;
import com.ideatech.ams.customer.entity.illegal.IllegalQueryBatch;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;
import com.ideatech.common.util.SecurityUtils;

public class IllegalQueryBatchSpec extends IdeaSimpleSpecification<IllegalQueryBatch, IllegalQueryBatchDto> {
    /**
     * @param condition
     */
    public IllegalQueryBatchSpec(IllegalQueryBatchDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<IllegalQueryBatch> queryWraper) {
        addLikeCondition(queryWraper, "illegalbatchNo");
        addBetweenCondition(queryWraper, "beginDate", "endDate", "batchDate");

        //organFullId为后加字段，IllegalQueryBatch原始存在数据时organFullId为null,
        // 通过正常的机构过滤，原始数据会被过滤，现在原始数据默认在根机构下显示（根机构查询不加过滤）。
        if (!"1".equals(SecurityUtils.getCurrentOrgFullId())){
            addStartsWidthCondition(queryWraper,"organFullId");
        }
    }
}
