package com.ideatech.ams.compare.spec;

import com.ideatech.ams.compare.dto.CustomerAbnormalSearchDto;
import com.ideatech.ams.compare.entity.CompareResultSaicCheck;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * @author jzh
 * @date 2019/6/13.
 */


public class CustomerAbnormalSpec extends IdeaSimpleSpecification<CompareResultSaicCheck, CustomerAbnormalSearchDto> {

    public CustomerAbnormalSpec(CustomerAbnormalSearchDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<CompareResultSaicCheck> queryWraper) {

        addStartsWidthCondition(queryWraper, "organFullId");
        addLikeCondition(queryWraper,"organName");
        addLikeCondition(queryWraper,"depositorName");
        addLikeCondition(queryWraper,"code");

        addEqualsCondition(queryWraper,"illegal");
        addEqualsCondition(queryWraper,"changeMess");
        addEqualsCondition(queryWraper,"businessExpires");
        addEqualsCondition(queryWraper,"abnormalState");
        addEqualsCondition(queryWraper,"changed");

        //增加多条件搜索
        addEqualsCondition(queryWraper,"abnormal");
        addEqualsCondition(queryWraper,"compareTaskId");

        addBetweenCondition(queryWraper, "beginDate", "endDate", "abnormalTime");
    }
}
