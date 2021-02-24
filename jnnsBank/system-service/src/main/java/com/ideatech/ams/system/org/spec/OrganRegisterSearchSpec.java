package com.ideatech.ams.system.org.spec;

import com.ideatech.ams.system.org.dto.OrganRegisterDto;
import com.ideatech.ams.system.org.entity.OrganRegisterPo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class OrganRegisterSearchSpec extends IdeaSimpleSpecification<OrganRegisterPo, OrganRegisterDto> {


    /**
     * @param condition
     */
    public OrganRegisterSearchSpec(OrganRegisterDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<OrganRegisterPo> queryWraper) {
        addLikeCondition(queryWraper,"name");
        addLikeCondition(queryWraper,"pbcCode");
        addLikeCondition(queryWraper,"fullId");
    }
}
