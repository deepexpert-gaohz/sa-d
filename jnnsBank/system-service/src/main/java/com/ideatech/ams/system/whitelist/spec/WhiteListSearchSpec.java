package com.ideatech.ams.system.whitelist.spec;

import com.ideatech.ams.system.whitelist.dto.WhiteListDto;
import com.ideatech.ams.system.whitelist.entity.WhiteListPo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class WhiteListSearchSpec extends IdeaSimpleSpecification<WhiteListPo, WhiteListDto> {


    /**
     * @param condition
     */
    public WhiteListSearchSpec(WhiteListDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<WhiteListPo> queryWraper) {
        addLikeCondition(queryWraper,"entName");
        addEqualsCondition(queryWraper,"source");
        addLikeCondition(queryWraper,"orgName");
        addLikeCondition(queryWraper,"organFullId");
        addInIncludeNullCondition(queryWraper,"statusList","status");
    }
}
