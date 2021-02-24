package com.ideatech.ams.annual.dao.spec;

import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.annual.entity.AnnualResult;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class AnnualResultSpec extends IdeaSimpleSpecification<AnnualResult, AnnualResultDto> {
    /**
     * @param condition
     */
    public AnnualResultSpec(AnnualResultDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<AnnualResult> queryWraper) {
        addStartsWidthCondition(queryWraper, "organFullId");
        addEqualsCondition(queryWraper, "pbcSubmitStatus");
        addEqualsCondition(queryWraper, "dataProcessStatus");
        addEqualsCondition(queryWraper, "forceStatus");
        addInCondition(queryWraper, "forceStatusList", "forceStatus");
        addEqualsCondition(queryWraper, "result");
        addEqualsCondition(queryWraper, "match");
        addEqualsCondition(queryWraper, "dataProcessDate");
        addEqualsCondition(queryWraper, "taskId");

        addLikeCondition(queryWraper, "acctNo");
        addLikeCondition(queryWraper, "dataProcessPerson");
        addLikeCondition(queryWraper, "depositorName");
        addLikeCondition(queryWraper, "organPbcCode");
        addLikeCondition(queryWraper, "organCode");
        addLikeCondition(queryWraper, "abnormal");
        addLikeCondition(queryWraper, "pbcSubmitErrorMsg");

//        addInCondition(queryWraper, "saicStatuses", "saicStatus");
        addInIncludeNullCondition(queryWraper, "saicStatuses", "saicStatus");
        addInCondition(queryWraper, "unilateral", "unilateral");
        addInCondition(queryWraper, "pbcSubmitStatuses", "pbcSubmitStatus");
        addInOrNullCondition(queryWraper, "pbcSubmitStatusesAndNull", "pbcSubmitStatus");
        addEqualsCondition(queryWraper, "deleted");
        addInCondition(queryWraper, "results", "result");
//        addInCondition(queryWraper, "matches", "match");
        addEqualsCondition(queryWraper, "acctType");

    }
}
