package com.ideatech.ams.annual.dao.spec;

import com.ideatech.ams.annual.dto.TelecomValidationSearchDto;
import com.ideatech.ams.annual.entity.TelecomValidationPo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class TelecomValidationSpec extends IdeaSimpleSpecification<TelecomValidationPo, TelecomValidationSearchDto> {
    public TelecomValidationSpec(TelecomValidationSearchDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<TelecomValidationPo> queryWraper) {
        addEqualsCondition(queryWraper, "batchNo");
        addEqualsCondition(queryWraper, "customerNo");
        addLikeCondition(queryWraper, "customerName");
        addLikeCondition(queryWraper, "bankCode");
        addEqualsCondition(queryWraper, "acctNo");
    }
}
