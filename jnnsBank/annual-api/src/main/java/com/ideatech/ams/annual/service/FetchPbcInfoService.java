package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.FetchPbcInfoDto;
import com.ideatech.common.service.BaseService;

import java.util.List;

/**
 *
 *
 * @author van
 * @date 20:20 2018/8/8
 */
public interface FetchPbcInfoService extends BaseService<FetchPbcInfoDto> {

	/**
	 * 根据任务获取所有数据
	 * @param taskId
	 * @return
	 */
	List<FetchPbcInfoDto> getAll(Long taskId);

	List<FetchPbcInfoDto> findByCollectAccountId(Long collectAccountId);

	void deleteByTaskId(Long taskId);

	List<FetchPbcInfoDto> findByOrganFullId(String organFullId);
}
