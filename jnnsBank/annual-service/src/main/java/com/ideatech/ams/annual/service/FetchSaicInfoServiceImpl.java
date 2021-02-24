package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.FetchSaicDao;
import com.ideatech.ams.annual.dto.FetchSaicInfoDto;
import com.ideatech.ams.annual.entity.FetchSaicInfo;
import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 *
 * @author van
 * @date 20:25 2018/8/8
 */
@Service
@Transactional
public class FetchSaicInfoServiceImpl extends BaseServiceImpl<FetchSaicDao, FetchSaicInfo, FetchSaicInfoDto> implements FetchSaicInfoService {

	@Override
	public List<FetchSaicInfoDto> getAll(Long taskId) {
		return ConverterService.convertToList(getBaseDao().findByCollectTaskIdAndCollectState(taskId, CollectState.success), FetchSaicInfoDto.class);
	}

	@Override
	public List<FetchSaicInfoDto> findByCollectTaskIdAndCollectStateNot(Long taskId, CollectState collectState) {
		return ConverterService.convertToList(getBaseDao().findByCollectTaskIdAndCollectStateNot(taskId, collectState), FetchSaicInfoDto.class);
	}

	@Override
	public List<FetchSaicInfoDto> findByCollectTaskIdAndCollectState(Long taskId, CollectState collectState) {
		return ConverterService.convertToList(getBaseDao().findByCollectTaskIdAndCollectState(taskId, collectState), FetchSaicInfoDto.class);

	}

	@Override
	public void deleteAllInBatch() {
		getBaseDao().deleteAllInBatch();
	}

	@Override
	public void updateRemaining(Long collectTaskId,Long annualTaskId,String failReason) {
		List<FetchSaicInfo> byCollectTaskIdAndCollectState = getBaseDao().findByCollectTaskIdAndCollectState(collectTaskId, CollectState.init);
		for (FetchSaicInfo fetchSaicInfo : byCollectTaskIdAndCollectState){
			fetchSaicInfo.setCollectState(CollectState.fail);
			fetchSaicInfo.setAnnualTaskId(annualTaskId);
			fetchSaicInfo.setFailReason(failReason);
			getBaseDao().save(fetchSaicInfo);
		}
	}

	@Override
	public void deleteByTaskId(Long taskId) {
		getBaseDao().deleteByAnnualTaskId(taskId);
	}


}
