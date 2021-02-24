package com.ideatech.ams.system.proof.dao.spec;

import com.ideatech.ams.system.proof.dto.ProofReportDto;
import com.ideatech.ams.system.proof.entity.ProofReport;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class ProofReportSpec extends IdeaSimpleSpecification<ProofReport, ProofReportDto> {
    /**
     * @param condition
     */
    public ProofReportSpec(ProofReportDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<ProofReport> queryWraper) {
        addStartsWidthCondition(queryWraper, "organFullId");
        addLikeCondition(queryWraper, "acctNo");
        addLikeCondition(queryWraper, "acctName");
        addLikeCondition(queryWraper, "entname");
        addLikeCondition(queryWraper, "phone");
        addEqualsCondition(queryWraper, "acctType");
        addEqualsCondition(queryWraper, "type");
        addLikeCondition(queryWraper, "openBankName");
        addEqualsCondition(queryWraper, "kycFlag");
        addLikeCondition(queryWraper, "proofBankName");
        addLikeCondition(queryWraper, "username");
        addBetweenCondition(queryWraper,"beginDate","endDate","dateTime");
        addInCondition(queryWraper,"typeList","type");
    }
}
