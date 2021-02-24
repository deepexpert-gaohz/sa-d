package com.ideatech.ams.annual.service;

import com.alibaba.fastjson.JSONArray;
import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.annual.dto.AnnualTaskDto;
import com.ideatech.ams.annual.dto.CompareFieldsDto;
import com.ideatech.ams.annual.dto.CompareRuleDto;
import com.ideatech.ams.annual.enums.TaskStatusEnum;
import com.ideatech.common.dto.TreeTable;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.service.BaseService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *	比对任务服务类
 * @author van
 * @date 16:03 2018/8/8
 */
public interface AnnualTaskService extends BaseService<AnnualTaskDto> {

	/**
	 * 任务开始
	 * @param taskId
	 * @return
	 */
	void start(Long taskId);

	Long getAnnualCompareTaskId();

    TreeTable getStatisticsInfo(Long nodeId, Long organId, Long taskId);

    List<Map<String, Object>> getStatisticsInfos(Long organId, Long taskId);

    Long initAnnualTask();

	/**
	 * 更新统计条数
	 * @param taskId
	 * @param annualResult
	 */
	void updateNum(Long taskId, Boolean annualResult);

	/**
	 * 更新处理成功数量
	 * @param taskId
	 */
	void updateLoopNumByTask(Long taskId);

	void updateStatus(Long taskId, TaskStatusEnum taskStatusEnum);

	void reset(Long taskId);

	/**
	 * 根据任务id统计条数
	 * @param taskId
	 */
	void updateNumByTask(Long taskId);

    void compareCompleted(Long taskId);

    IExcelExport exportXLS(Long organId, Long taskId);

	/**
	 * 全部重新年检
	 * @param ardList 重新年检集合
	 * @param taskId 年检任务id
	 */
	void annualAgainAll(List<AnnualResultDto> ardList, Long taskId);

	void clearFuture();

	/**
	 * 获取历史年检数据
	 */
	JSONArray getAnnualHistory();

	/**
	 * 获取比对规则
	 * @param taskId
	 * @return
	 */
	Map<String, Map<String, CompareRuleDto>> getaAssemblyCompareRuleMap(Long taskId);

	/**
	 * 年检比对
	 * annualResultDto 采集结果数据
	 * judgeBusinessLicenseExpired 工商营业执照到期是否影响主逻辑
	 * compareFieldsDtoList 比对字段
	 * compareRuleDtoMap 详细比对规则
	 */
	void annualResultComparison(AnnualResultDto annualResultDto,
								 Boolean judgeBusinessLicenseExpired,
								 List<CompareFieldsDto> compareFieldsDtoList,
								 Map<String, Map<String, CompareRuleDto>> compareRuleDtoMap);

	/**
	 * 创建定时采集工商数据任务
	 */
	boolean createSaicTimedTask(Long annualTaskId);

    Boolean getIsAnnualSubmit(String organFullId);

	/**
	 * 重置未成功数据
	 * @param taskId
	 */
	void resetUnSuccess(Long taskId);

	Set<String> annualCheckAmsPassword();
}
