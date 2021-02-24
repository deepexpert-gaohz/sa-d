package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.CompareFieldsDto;
import com.ideatech.ams.annual.enums.CompareFieldEnum;
import com.ideatech.common.service.BaseService;

import java.util.List;

/**
 *
 *
 * @author van
 * @date 23:29 2018/8/9
 */
public interface CompareFieldsService extends BaseService<CompareFieldsDto> {

	List<CompareFieldsDto> listCompareRulesByTakId(Long taskId);

	boolean saveCompareRules(CompareFieldsDto compareFieldsDto);

	void saveDefaultCompareFieldsRules(Long taskId);

	/**
	 * 获取年检只读字段配置
	 * @return
	 */
	String getLockField();

	/**
	 * 设置页面年检比对字段只读
	 *
	 * @param field  字段名称
	 * @param taskId 年检id
	 */
	void setLock(CompareFieldEnum field,Long taskId);
}
