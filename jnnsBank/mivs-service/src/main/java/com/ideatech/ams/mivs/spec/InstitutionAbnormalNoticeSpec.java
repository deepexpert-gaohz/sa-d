package com.ideatech.ams.mivs.spec;

import com.ideatech.ams.mivs.dto.ind.InstitutionAbnormalNoticeDto;
import com.ideatech.ams.mivs.entity.InstitutionAbnormalNotice;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * @author jzh
 * @date 2019/7/24.
 */
public class InstitutionAbnormalNoticeSpec extends IdeaSimpleSpecification<InstitutionAbnormalNotice, InstitutionAbnormalNoticeDto> {
    /**
     * @param condition
     */
    public InstitutionAbnormalNoticeSpec(InstitutionAbnormalNoticeDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<InstitutionAbnormalNotice> queryWraper) {
        //银行营业网点或非银行支付机构行号
        addLikeCondition(queryWraper, "orgnlInstgPty");
        //异常核查类型
        addEqualsCondition(queryWraper,"abnmlType");
        //报文标识号
        addLikeCondition(queryWraper, "msgId");
        //报文发送时间
        addBetweenCondition(queryWraper, "beginDate", "endDate", "creDtTm");
    }
}
