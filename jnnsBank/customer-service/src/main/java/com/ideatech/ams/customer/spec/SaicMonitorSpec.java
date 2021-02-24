package com.ideatech.ams.customer.spec;

import com.ideatech.ams.customer.entity.SaicMonitorPo;
import com.ideatech.ams.customer.vo.SaicMonitorVo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class SaicMonitorSpec extends IdeaSimpleSpecification<SaicMonitorPo, SaicMonitorVo> {

    /**
     * @param condition
     */
    public SaicMonitorSpec(SaicMonitorVo condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<SaicMonitorPo> queryWraper) {
        addStartsWidthCondition(queryWraper, "organFullId");
        addLikeCondition(queryWraper,"companyName");
        addLikeCondition(queryWraper,"regNo");
        addEqualsCondition(queryWraper,"checkType");
        addBetweenCondition(queryWraper, "beginDate", "endDate", "createTime");
    }
}
