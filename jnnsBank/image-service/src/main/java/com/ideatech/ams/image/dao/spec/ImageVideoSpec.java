package com.ideatech.ams.image.dao.spec;

import com.ideatech.ams.image.dto.ImageVideoDto;
import com.ideatech.ams.image.entity.ImageVideo;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class ImageVideoSpec extends IdeaSimpleSpecification<ImageVideo, ImageVideoDto> {
    /**
     * @param condition
     */
    public ImageVideoSpec(ImageVideoDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<ImageVideo> queryWraper) {
        addEqualsCondition(queryWraper,"batchNumber");
        addEqualsCondition(queryWraper,"acctNo");
        addEqualsCondition(queryWraper,"depositorName");
        addEqualsCondition(queryWraper,"legalName");
        addEqualsCondition(queryWraper,"acctType");
        addEqualsCondition(queryWraper,"regNo");
        addEqualsCondition(queryWraper,"applyid");
        addEqualsCondition(queryWraper,"recordsNo");
        addBetweenCondition(queryWraper,"beginDate","endDate","dateTime");
        addStartsWidthCondition(queryWraper,"organFullId");
        addEqualsCondition(queryWraper,"businessType");
        addEqualsCondition(queryWraper,"customerName");
        addEqualsCondition(queryWraper,"username");
        addEqualsCondition(queryWraper,"recordType");
    }
}
