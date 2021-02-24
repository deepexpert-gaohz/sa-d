package com.ideatech.ams.apply.dao.spec;

import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.entity.CompanyPreOpenAccountEnt;
import com.ideatech.common.entity.spec.IdeaSimpleSpecification;
import com.ideatech.common.entity.spec.QueryWraper;

public class CompanyPreOpenAccountEntSpec extends IdeaSimpleSpecification<CompanyPreOpenAccountEnt, CompanyPreOpenAccountEntDto> {

	public CompanyPreOpenAccountEntSpec(CompanyPreOpenAccountEntDto condition) {
		super(condition);
	}

	@Override
	protected void addCondition(QueryWraper<CompanyPreOpenAccountEnt> queryWraper) {
		addStartsWidthCondition(queryWraper, "organfullid");
		addBetweenCondition(queryWraper, "beginDate", "endDate", "applytime");//预约时间
		addLikeCondition(queryWraper, "name");//企业名称
		addLikeCondition(queryWraper, "branch");//开户支行
		addEqualsCondition(queryWraper, "type");//开户性质
		addEqualsCondition(queryWraper, "status");//预约状态
		addInCondition(queryWraper, "statuses", "status");//预约状态
		addLikeCondition(queryWraper, "applyid");//预约编号
		addEqualsCondition(queryWraper, "phone");//预约手机号
		addEqualsCondition(queryWraper, "operator");//预约人员
		addBetweenConditionDate(queryWraper, "beginDateApply", "endDateApply", "createdDate");//申请时间
		addEqualsCondition(queryWraper, "accepter");//接洽操作员
		addBetweenCondition(queryWraper, "beginDateAccept", "endDateAccept", "acceptTimes");//接洽时间
		addEqualsCondition(queryWraper, "billType");//接洽操作员
		addBetweenCondition(queryWraper, "createdDateStart", "createdDateEnd", "applytime");//预约时间(预约未及时处理列表) 使用createdDate存在问题
		addLessThanCondition(queryWraper,"createdDate");
	}

}
