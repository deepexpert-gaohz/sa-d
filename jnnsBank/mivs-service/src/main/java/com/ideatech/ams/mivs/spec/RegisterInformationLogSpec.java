package com.ideatech.ams.mivs.spec;

import com.ideatech.ams.mivs.dto.RegisterInformationLogDto;
import com.ideatech.ams.mivs.entity.RegisterInformationLog;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * @author jzh
 * @date 2019/7/31.
 */
public class RegisterInformationLogSpec extends IdeaSimpleSpecification<RegisterInformationLog, RegisterInformationLogDto> {
    /**
     * @param condition
     */
    public RegisterInformationLogSpec(RegisterInformationLogDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<RegisterInformationLog> queryWraper) {
        addLikeCondition(queryWraper, "entNm");//企业名称
        addLikeCondition(queryWraper, "traNm");//字号名称
        addLikeCondition(queryWraper, "uniSocCdtCd");//统一社会信用代码
        addLikeCondition(queryWraper, "orgName");
        addLikeCondition(queryWraper, "createdUserName");
        addBetweenCondition(queryWraper, "beginDate", "endDate", "createTime");//操作时间
        addStartsWidthCondition(queryWraper, "organFullId");//权限控制
    }
}
