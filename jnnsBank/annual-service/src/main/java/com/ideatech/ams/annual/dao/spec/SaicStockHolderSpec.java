package com.ideatech.ams.annual.dao.spec;

import com.ideatech.ams.annual.dto.SaicStockHolderSearchDto;
import com.ideatech.ams.annual.entity.SaicStockHolderPo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class SaicStockHolderSpec extends IdeaSimpleSpecification<SaicStockHolderPo, SaicStockHolderSearchDto> {
    public SaicStockHolderSpec(SaicStockHolderSearchDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<SaicStockHolderPo> queryWraper) {
        addEqualsCondition(queryWraper, "batchNo");
        addEqualsCondition(queryWraper, "customerNo");
        addLikeCondition(queryWraper, "customerName");
        addEqualsCondition(queryWraper, "acctNo");
        addLikeCondition(queryWraper, "organCode");
        addEqualsCondition(queryWraper, "isSame");
    }
}
