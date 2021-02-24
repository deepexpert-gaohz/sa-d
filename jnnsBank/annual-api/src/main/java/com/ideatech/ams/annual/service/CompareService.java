package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.annual.dto.CompareDataDto;
import com.ideatech.ams.annual.dto.CompareFieldsDto;
import com.ideatech.ams.annual.dto.CompareRuleDto;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author van
 * @date 19:52 2018/8/9
 */
public interface CompareService {

	/**
	 * 比对实现
	 * @param annualResultDto
	 * @param compareFieldsDtoList
	 * @param compareRuleDtoMap
	 * @return
	 */
	Boolean compare(AnnualResultDto annualResultDto, List<CompareFieldsDto> compareFieldsDtoList, Map<String, Map<String, CompareRuleDto>> compareRuleDtoMap);

	Map<String, CompareDataDto> getCompareDatas(AnnualResultDto annualResultDto);

}
