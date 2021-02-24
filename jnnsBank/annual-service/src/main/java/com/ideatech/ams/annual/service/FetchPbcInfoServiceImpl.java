package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.FetchPbcDao;
import com.ideatech.ams.annual.dto.FetchPbcInfoDto;
import com.ideatech.ams.annual.entity.FetchPbcInfo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 *
 * @author van
 * @date 20:24 2018/8/8
 */
@Service
@Transactional
public class FetchPbcInfoServiceImpl extends BaseServiceImpl<FetchPbcDao, FetchPbcInfo, FetchPbcInfoDto> implements FetchPbcInfoService {

	@Override
	public List<FetchPbcInfoDto> getAll(Long taskId) {
		return ConverterService.convertToList(getBaseDao().findByAnnualTaskId(taskId), FetchPbcInfoDto.class);
	}

	@Override
	public List<FetchPbcInfoDto> findByCollectAccountId(Long collectAccountId) {
		return ConverterService.convertToList(getBaseDao().findByCollectAccountId(collectAccountId), FetchPbcInfoDto.class);
	}

	@Override
	public void deleteByTaskId(Long taskId) {
		getBaseDao().deleteByAnnualTaskId(taskId);
	}

	@Override
	public List<FetchPbcInfoDto> findByOrganFullId(String organFullId) {
		return ConverterService.convertToList(getBaseDao().findByOrganFullId(organFullId), FetchPbcInfoDto.class);
	}
}
