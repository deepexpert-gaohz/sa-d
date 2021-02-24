package com.ideatech.ams.mivs.spec;

import com.ideatech.ams.mivs.dto.MobilePhoneNumberLogDto;
import com.ideatech.ams.mivs.entity.MobilePhoneNumberLog;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * @author jzh
 * @date 2019/7/29.
 */
public class MobilePhoneNumberLogSpec  extends IdeaSimpleSpecification<MobilePhoneNumberLog, MobilePhoneNumberLogDto> {
    /**
     * @param condition
     */
    public MobilePhoneNumberLogSpec(MobilePhoneNumberLogDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<MobilePhoneNumberLog> queryWraper) {
        addLikeCondition(queryWraper, "mobNb");
        addLikeCondition(queryWraper, "nm");
        addLikeCondition(queryWraper, "orgName");
        addLikeCondition(queryWraper, "createdUserName");
        addBetweenCondition(queryWraper, "beginDate", "endDate", "createTime");//操作时间
        addStartsWidthCondition(queryWraper, "organFullId");//权限控制
    }
}
