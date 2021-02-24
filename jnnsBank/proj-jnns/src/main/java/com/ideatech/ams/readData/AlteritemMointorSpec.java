package com.ideatech.ams.readData;



import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;


public class AlteritemMointorSpec extends IdeaSimpleSpecification<AlteritemMointorInfo,AlteritemMointor> {

    /**
     * @param condition
     */
    public AlteritemMointorSpec(AlteritemMointor condition) {
        super(condition);
    }


    @Override
    protected void addCondition(QueryWraper<AlteritemMointorInfo> queryWraper) {

        addLikeCondition(queryWraper, "customerId");
        addLikeCondition(queryWraper, "creditNo");
        addLikeCondition(queryWraper, "companyName");
        addEqualsCondition(queryWraper, "alterItem");
        addEqualsCondition(queryWraper, "organFullId");
        addEqualsCondition(queryWraper, "isWarning");

    }
}
