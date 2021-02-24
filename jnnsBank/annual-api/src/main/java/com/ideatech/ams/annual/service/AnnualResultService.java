package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.annual.dto.CoreCollectionDto;
import com.ideatech.ams.annual.dto.FetchPbcInfoDto;
import com.ideatech.ams.annual.dto.FetchSaicInfoDto;
import com.ideatech.ams.annual.enums.ForceStatusEnum;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author van
 * @date 16:03 2018/8/8
 */
public interface AnnualResultService extends BaseService<AnnualResultDto> {

	/**
	 * 根据核心数据创建比对结果
	 * @param taskId
	 * @param coreDataDto
	 */
	void updateAnnualResultData(Long taskId, CoreCollectionDto coreDataDto, Map<String, OrganizationDto> organInMap);

	/**
	 * 更新人行数据到比对结果表
	 * @param taskId
	 * @param pbcDataDto
	 */
	void updateAnnualResultData(Long taskId, FetchPbcInfoDto pbcDataDto);

	/**
	 * 更新工商数据到比对结果
	 * @param taskId
	 * @param saicDataDto
	 */
	void updateAnnualResultData(Long taskId, FetchSaicInfoDto saicDataDto);

	/**
	 * 根据机构展示结果
	 * @param taskId
	 * @param organFullId
	 * @return
	 */
	List<AnnualResultDto> listAnnualsByOrgan(Long taskId, String organFullId);

	/**
	 * 根据机构展示失败的结果
	 * @param taskId
	 * @param organFullId
	 * @return
	 */
	List<AnnualResultDto> listAnnualsByOrganUnProcess(Long taskId, String organFullId);

	List<String> listOrgansByTaskId(Long taskId);

	void updateAnnualResult(AnnualResultDto annualResultDto);

	TableResultResponse<AnnualResultDto> queryByCode(AnnualResultDto condition, String code, Pageable pageable);

	TableResultResponse<AnnualResultDto> querySuccessByCode(AnnualResultDto condition, String code, Pageable pageable);

	ResultDto<AnnualResultDto> submitAnnualAccount(Long id);

    AnnualResultDto annualAgain(Long id);

    AnnualResultDto annualAgain(AnnualResultDto ard);

	/**
	 * 全部重新年检
	 */
	void annualAgainAll(AnnualResultDto condition, String code);

	Long countAnnualResltByTaskId(Long id);

	void deleteAnnualResultByTaskId(Long taskId);

	/**
	 * 清理结果表，重置状态
	 * @param taskId
	 */
	void cleanAnnualResultByTaskId(Long taskId);

	/**
	 * 根据id统计已处理以及比对一致的数据
	 * @param taskId
	 * @return
	 */
	Long[] countProcessedAndPassedNum(Long taskId);

	/**
	 * 根据id统计手工成功和比对一致数据
	 * @param taskId
	 * @return
	 */
	Long[] countForceStatusAndPassedNum(Long taskId);

	/**
	 * 根据id修改处理状态
	 * @param id
	 * @return
	 */
	boolean submitAnnualDataProcess(Long id);

	/**
	 * 根据id回退处理状态
	 * @param id
	 * @return
	 */
	void dataProcessRecall(Long id);

	/**
	 * 查询是否本用户
	 * @param id
	 * @return
	 */
	boolean checkSelf(Long id);

	/**
	 * 年检结果短信通知
	 * @param annualResultDto
	 * @param legalTelephone
	 */
    void sendMessageNotice(AnnualResultDto annualResultDto, String legalTelephone);

    void updateForceStatus(Long id, ForceStatusEnum forceStatusEnum);

	/**
	 * 年检结果相关列表数据 EXCEL导出
	 * @return
	 */
	List<IExcelExport> exportExcel(AnnualResultDto condition, String code);

	/**
	 * 根据账号查询最近年检结果
	 * @param acctNo
	 * @return
	 */
	AnnualResultDto annualResultSearch(String acctNo);

	/**
	 * 重置年检结果，仅未成功
	 * @param taskId
	 */
	void cleanUnSuccessAnnualResult(Long taskId);

	/**
	 * 年检待处理批量删除
	 */
    void batchDelete(Long[] ids);

	/**
	 *  无需年检账户提交
	 * @param ids
	 */
	String noCheckAnnual(Long[] ids);

}
