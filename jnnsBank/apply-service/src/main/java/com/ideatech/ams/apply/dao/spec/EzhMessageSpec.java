package com.ideatech.ams.apply.dao.spec;

import com.ideatech.ams.apply.dto.EzhMessageDto;
import com.ideatech.ams.apply.entity.EzhMessage;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class EzhMessageSpec extends IdeaSimpleSpecification<EzhMessage, EzhMessageDto> {

    /**
     * @param condition
     */
    public EzhMessageSpec(EzhMessageDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<EzhMessage> queryWraper) {
        addLikeCondition(queryWraper, "phone");
        addEqualsCondition(queryWraper, "checkPass");
        addEqualsCondition(queryWraper, "type");

    }
}
