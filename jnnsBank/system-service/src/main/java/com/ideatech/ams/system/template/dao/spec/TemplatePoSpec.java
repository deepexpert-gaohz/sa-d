package com.ideatech.ams.system.template.dao.spec;

import com.ideatech.ams.system.template.dto.TemplateSearchDto;
import com.ideatech.ams.system.template.entity.TemplatePo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class TemplatePoSpec extends IdeaSimpleSpecification<TemplatePo, TemplateSearchDto> {
    /**
     * @param condition
     */
    public TemplatePoSpec(TemplateSearchDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<TemplatePo> queryWraper) {
        addEqualsCondition(queryWraper, "billType");
        addEqualsCondition(queryWraper, "depositorType");
        addEqualsCondition(queryWraper, "acctType");
    }
}
