package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.CompareRuleDto;
import com.ideatech.ams.annual.enums.CompareFieldEnum;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.common.service.BaseService;

import java.util.List;

/**
 *
 *
 * @author van
 * @date 23:29 2018/8/9
 */
public interface CompareRuleService extends BaseService<CompareRuleDto> {

	List<CompareRuleDto> listCompareRulesByTakId(Long taskId);

	boolean saveCompareRules(CompareRuleDto compareRuleDto);

	/**
	 * 添加更新年检比对字段默认规则
	 * @param taskId 年检id
	 */
	void saveDefaultCompareRules(Long taskId);

	CompareRuleDto findCompareRulesByfieldsAndDataSource(Long taskId, CompareFieldEnum compareFieldEnum, DataSourceEnum dataSource);

}
