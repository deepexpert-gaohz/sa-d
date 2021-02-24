package com.ideatech.ams.system.annotation.spec;

import com.ideatech.ams.system.annotation.dto.MessageLogDto;
import com.ideatech.ams.system.annotation.entity.MessageLog;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class MessageLogSpec extends IdeaSimpleSpecification<MessageLog, MessageLogDto> {
    /**
     * @param condition
     */
    public MessageLogSpec(MessageLogDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<MessageLog> queryWraper) {
        addLikeCondition(queryWraper, "acctNo");
        addEqualsCondition(queryWraper, "processResult");
        addEqualsCondition(queryWraper, "operateType");
        addEqualsCondition(queryWraper, "tranType");
        addBetweenCondition(queryWraper, "beginDate", "endDate", "tranDate");

    }
}
