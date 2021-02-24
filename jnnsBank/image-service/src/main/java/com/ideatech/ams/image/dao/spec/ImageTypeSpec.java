package com.ideatech.ams.image.dao.spec;

import com.ideatech.ams.image.dto.ImageTypeInfo;
import com.ideatech.ams.image.entity.ImageType;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class ImageTypeSpec extends IdeaSimpleSpecification<ImageType, ImageTypeInfo> {
    /**
     * @param condition
     */
    public ImageTypeSpec(ImageTypeInfo condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<ImageType> queryWraper) {
        addEqualsCondition(queryWraper,"acctType");
        addLikeCondition(queryWraper, "imageName");
        addEqualsCondition(queryWraper,"operateType");
        addLikeCondition(queryWraper,"depositorType");
        addEqualsCondition(queryWraper,"choose");
    }
}
