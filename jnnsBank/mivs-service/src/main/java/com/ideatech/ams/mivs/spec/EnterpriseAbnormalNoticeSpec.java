package com.ideatech.ams.mivs.spec;

import com.ideatech.ams.mivs.dto.end.EnterpriseAbnormalNoticeDto;
import com.ideatech.ams.mivs.entity.EnterpriseAbnormalNotice;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

/**
 * @author jzh
 * @date 2019/7/25.
 */
public class EnterpriseAbnormalNoticeSpec extends IdeaSimpleSpecification<EnterpriseAbnormalNotice, EnterpriseAbnormalNoticeDto> {
    /**
     * @param condition
     */
    public EnterpriseAbnormalNoticeSpec(EnterpriseAbnormalNoticeDto condition) {
        super(condition);
    }

    @Override
    protected void addCondition(QueryWraper<EnterpriseAbnormalNotice> queryWraper) {
        //统一社会信用代码
        addLikeCondition(queryWraper, "uniSocCdtCd");
        //单位名称
        addLikeCondition(queryWraper, "coNm");
        //姓名
        addLikeCondition(queryWraper, "nm");
        //手机号码
        addLikeCondition(queryWraper, "phNb");

        //异常核查类型
        addEqualsCondition(queryWraper,"abnmlType");
        //报文标识号
        addLikeCondition(queryWraper, "msgId");
        //报文发送时间
        addBetweenCondition(queryWraper, "beginDate", "endDate", "creDtTm");
    }
}
