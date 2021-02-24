package com.ideatech.ams.annual.dao.spec;

import com.ideatech.ams.annual.dto.SaicBeneficiarySearchDto;
import com.ideatech.ams.annual.entity.SaicBeneficiaryPo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class SaicBeneficiarySpec extends IdeaSimpleSpecification<SaicBeneficiaryPo, SaicBeneficiarySearchDto> {
    public SaicBeneficiarySpec(SaicBeneficiarySearchDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<SaicBeneficiaryPo> queryWraper) {
        addEqualsCondition(queryWraper, "batchNo");
        addEqualsCondition(queryWraper, "customerNo");
        addLikeCondition(queryWraper, "customerName");
        addEqualsCondition(queryWraper, "acctNo");
        addLikeCondition(queryWraper, "organCode");
        addEqualsCondition(queryWraper, "isSame");
    }
}
