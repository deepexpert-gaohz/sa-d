package com.ideatech.ams.compare.dao.spec;

import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.entity.DataSource;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class DataSourceSpec extends IdeaSimpleSpecification<DataSource, DataSourceDto> {
    /**
     * @param condition
     */
    public DataSourceSpec(DataSourceDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<DataSource> queryWraper) {
        addLikeCondition(queryWraper,"name");
        addLikeCondition(queryWraper,"organFullId");
        addEqualsCondition(queryWraper,"collectType");
        addInCondition(queryWraper,"createdByList","createdBy");
        addInCondition(queryWraper,"fullIdList","organFullId");
        addBetweenConditionDate(queryWraper, "beginDate", "endDate", "createdDate");//创建时间
        addNotEqualsCondition(queryWraper,"createdBy");//过滤虚拟用户创建的数据
    }
}
