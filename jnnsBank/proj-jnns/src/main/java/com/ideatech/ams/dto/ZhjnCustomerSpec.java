package com.ideatech.ams.dto;

import com.ideatech.ams.domain.zhjn.ZhjnCustomerInfo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class ZhjnCustomerSpec extends IdeaSimpleSpecification<ZhjnCustomerInfo, ZhjnCustomerDto> {
    /**
     * @param condition
     */
    public ZhjnCustomerSpec(ZhjnCustomerDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<ZhjnCustomerInfo> queryWraper) {

        addStartsWidthCondition(queryWraper, "clerkNo");
        addLikeCondition(queryWraper, "clerkName");
        addLikeCondition(queryWraper, "checkNo");
        addLikeCondition(queryWraper, "checkName");
        addEqualsCondition(queryWraper, "bankCode");






    }
}
