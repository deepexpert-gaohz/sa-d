package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.FetchSaicInfoDto;
import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.common.service.BaseService;

import java.util.List;

/**
 *
 *
 * @author van
 * @date 20:23 2018/8/8
 */
public interface FetchSaicInfoService extends BaseService<FetchSaicInfoDto> {

	/**
	 * 根据任务获取所有数据
	 * @param taskId
	 * @return
	 */
	List<FetchSaicInfoDto> getAll(Long taskId);
	/**
	 * 查询状态不为特定值的数据
	 *
	 * @param taskId
	 * @param collectState
	 * @return
	 */
	List<FetchSaicInfoDto> findByCollectTaskIdAndCollectStateNot(Long taskId, CollectState collectState);

	/**
	 * 查询状态为特定值的数据
	 *
	 * @param taskId
	 * @param collectState
	 * @return
	 */
	List<FetchSaicInfoDto> findByCollectTaskIdAndCollectState(Long taskId, CollectState collectState);

	/**
	 * 删除全部数据
	 *
	 */
	void deleteAllInBatch();
	/**
	 * 工商导入采集结束后，更新其他初始化状态的为失败
	 *
	 */
	void updateRemaining(Long collectTaskId,Long annualTaskId,String failReason);

	void deleteByTaskId(Long taskId);
}
