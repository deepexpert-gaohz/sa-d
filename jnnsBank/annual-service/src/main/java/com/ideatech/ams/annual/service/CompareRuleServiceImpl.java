package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.CompareRuleDao;
import com.ideatech.ams.annual.dto.CompareRuleDto;
import com.ideatech.ams.annual.entity.CompareRule;
import com.ideatech.ams.annual.enums.CompareFieldEnum;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 *
 * @author van
 * @date 23:31 2018/8/9
 */
@Service
public class CompareRuleServiceImpl extends BaseServiceImpl<CompareRuleDao, CompareRule, CompareRuleDto> implements CompareRuleService {

	@Autowired
	private CompareFieldsService compareFieldsService;

	@Override
	public List<CompareRuleDto> listCompareRulesByTakId(Long taskId) {
		return ConverterService.convertToList(getBaseDao().findByTaskId(taskId), CompareRuleDto.class);
	}

	@Override
	public boolean saveCompareRules(CompareRuleDto compareRuleDto) {
		CompareRule compareRule = ConverterService.convert(compareRuleDto,CompareRule.class);
		if (compareRule.getId()!=null){
			compareRule.setVersionCt(getBaseDao().findOne(compareRule.getId()).getVersionCt());
		}
		if (getBaseDao().save(compareRule)!=null){
			return true;
		}
		return false;
	}

	/**
	 * 添加更新年检比对字段默认规则
	 * @param taskId 年检id
	 */
	@Override
	public void saveDefaultCompareRules(Long taskId) {
		String lockField = compareFieldsService.getLockField();//获取只读年检比对字段
		createCompareRule(DataSourceEnum.CORE, CompareFieldEnum.ACCT_NO, true, true, taskId, lockField);
		createCompareRule(DataSourceEnum.PBC, CompareFieldEnum.ACCT_NO, true, true, taskId, lockField);
		createCompareRule(DataSourceEnum.SAIC, CompareFieldEnum.ACCT_NO, true, true, taskId, lockField);

		createCompareRule(DataSourceEnum.CORE, CompareFieldEnum.DEPOSITOR_NAME, true, false, taskId, lockField);
		createCompareRule(DataSourceEnum.PBC, CompareFieldEnum.DEPOSITOR_NAME, true, false, taskId, lockField);
		createCompareRule(DataSourceEnum.SAIC, CompareFieldEnum.DEPOSITOR_NAME, true, false, taskId, lockField);

		createCompareRule(DataSourceEnum.CORE, CompareFieldEnum.LEGAL_NAME, true, false, taskId, lockField);
		createCompareRule(DataSourceEnum.PBC, CompareFieldEnum.LEGAL_NAME, true, false, taskId, lockField);
		createCompareRule(DataSourceEnum.SAIC, CompareFieldEnum.LEGAL_NAME, true, false, taskId, lockField);

		createCompareRule(DataSourceEnum.CORE, CompareFieldEnum.REG_NO, true, false, taskId, lockField);
		createCompareRule(DataSourceEnum.PBC, CompareFieldEnum.REG_NO, true, false, taskId, lockField);
		createCompareRule(DataSourceEnum.SAIC, CompareFieldEnum.REG_NO, true, false, taskId, lockField);

		createCompareRule(DataSourceEnum.CORE, CompareFieldEnum.REG_CAPITAL, true, false, taskId, lockField);
		createCompareRule(DataSourceEnum.PBC, CompareFieldEnum.REG_CAPITAL, true, false, taskId, lockField);
		createCompareRule(DataSourceEnum.SAIC, CompareFieldEnum.REG_CAPITAL, true, false, taskId, lockField);

		createCompareRule(DataSourceEnum.CORE, CompareFieldEnum.REG_ADDRESS, true, false, taskId, lockField);
		createCompareRule(DataSourceEnum.PBC, CompareFieldEnum.REG_ADDRESS, true, false, taskId, lockField);
		createCompareRule(DataSourceEnum.SAIC, CompareFieldEnum.REG_ADDRESS, true, false, taskId, lockField);
	}

	private void createCompareRule(DataSourceEnum dataSourceEnum, CompareFieldEnum compareFieldEnum, boolean active, boolean nullpass, Long taskId, String lockField) {
		CompareRule compareRule = getBaseDao().findByDataSourceEnumAndCompareFieldEnumAndTaskId(dataSourceEnum, compareFieldEnum, taskId);
		if (compareRule != null) {//更新
			if (StringUtils.isNotBlank(lockField) && lockField.contains(compareFieldEnum.getField())) {//对只读字段进行更新，避免前端因为只读不能修改数据
				compareRule.setActive(active);
				compareRule.setNullpass(nullpass);
				getBaseDao().save(compareRule);
			}
		} else {//新增
			CompareRule cr = new CompareRule();
			cr.setDataSourceEnum(dataSourceEnum);
			cr.setCompareFieldEnum(compareFieldEnum);
			cr.setActive(active);
			cr.setTaskId(taskId);
			cr.setNullpass(nullpass);
			getBaseDao().save(cr);
		}
	}

	@Override
	public CompareRuleDto findCompareRulesByfieldsAndDataSource(Long taskId, CompareFieldEnum compareFieldEnum, DataSourceEnum dataSource) {

		CompareRule compareRule = getBaseDao().findByDataSourceEnumAndCompareFieldEnumAndTaskId(dataSource,compareFieldEnum,taskId);
		if(compareRule!=null){
			return ConverterService.convert(compareRule,CompareRuleDto.class);
		}
		return null;
	}
}
