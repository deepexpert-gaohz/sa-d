package com.ideatech.ams.mivs.spec;

import com.ideatech.ams.mivs.dto.TaxInformationLogDto;
import com.ideatech.ams.mivs.entity.TaxInformationLog;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * @author jzh
 * @date 2019/7/29.
 */
public class TaxInformationLogSpec extends IdeaSimpleSpecification<TaxInformationLog, TaxInformationLogDto> {
    /**
     * @param condition
     */
    public TaxInformationLogSpec(TaxInformationLogDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<TaxInformationLog> queryWraper) {
        addLikeCondition(queryWraper, "coNm");
        addLikeCondition(queryWraper, "orgName");
        addLikeCondition(queryWraper, "createdUserName");
        addBetweenCondition(queryWraper, "beginDate", "endDate", "createTime");//操作时间
        addStartsWidthCondition(queryWraper, "organFullId");//权限控制
    }
}
