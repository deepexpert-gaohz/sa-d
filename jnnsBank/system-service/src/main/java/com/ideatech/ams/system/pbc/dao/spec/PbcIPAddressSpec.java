package com.ideatech.ams.system.pbc.dao.spec;

import com.ideatech.ams.system.pbc.dto.PbcIPAddressDto;
import com.ideatech.ams.system.pbc.entity.PbcIPAddressPo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class PbcIPAddressSpec extends IdeaSimpleSpecification<PbcIPAddressPo, PbcIPAddressDto> {
    /**
     * @param condition
     */
    public PbcIPAddressSpec(PbcIPAddressDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<PbcIPAddressPo> queryWraper) {
        addEqualsCondition(queryWraper, "ip");
        addLikeCondition(queryWraper, "provinceName");

    }
}
