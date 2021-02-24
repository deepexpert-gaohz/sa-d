package com.ideatech.ams.annual.service;

import com.ideatech.ams.account.vo.AnnualAccountVo;
import com.ideatech.ams.annual.dto.CoreCollectResultSearchDto;
import com.ideatech.ams.annual.dto.CoreCollectionDto;
import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.ams.annual.enums.CollectType;
import com.ideatech.ams.annual.vo.CoreCollectionExcelRowVo;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.List;

public interface CoreCollectionService {
    /**
	 * 根据任务获取所有数据
	 * @param taskId
	 * @return
	 */
	List<CoreCollectionDto> getAll(Long taskId);
	CoreCollectionDto findById(Long id);
    void collect(Long taskId);
	void collectReset(Long taskId);
    void collect(List<CoreCollectionExcelRowVo> dataList,Long taskId,CollectType collectType);

    void deleteByTaskId(Long taskId);
	CoreCollectResultSearchDto search(final CoreCollectResultSearchDto coreCollectResultSearchDto,final Long taskId);
	void clearFuture();
	void endFuture();
	IExcelExport generateAnnualCompanyReport();

	/**
	 * 采集外部数据的接口
	 * @param annualTaskId 年检任务id
	 * @param accountsAllList 外部数据
	 */
	void collectOut(Long annualTaskId, List<AnnualAccountVo> accountsAllList);

	/**
	 * 重复采集核心外部数据的接口
	 * @param annualTaskId 年检任务id
	 * @param accountsAllList 外部数据
	 */
	void collectOutReset(Long annualTaskId, List<AnnualAccountVo> accountsAllList);


	/**
	 * 查询状态不是参数带入状态的数据
	 * @param state
	 * @return
	 */
	int countByCollectStateNot(Long coreAnnualTaskId,CollectState... state);


}
