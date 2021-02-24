package com.ideatech.ams.kyc.dao.spec;

import com.ideatech.ams.kyc.dto.SaicInfoDto;
import com.ideatech.ams.kyc.entity.SaicInfo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class SaicInfoSpec extends IdeaSimpleSpecification<SaicInfo, SaicInfoDto> {

    /**
     * @param condition
     */
    public SaicInfoSpec(SaicInfoDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<SaicInfo> queryWraper) {

    }
}
