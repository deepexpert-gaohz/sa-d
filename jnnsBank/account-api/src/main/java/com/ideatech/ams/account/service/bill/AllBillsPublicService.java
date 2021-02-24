package com.ideatech.ams.account.service.bill;

import com.ideatech.ams.account.dto.AccountBillsAllSearchInfo;
import com.ideatech.ams.account.dto.AllBillsPublicSearchDTO;
import com.ideatech.ams.account.dto.PbcSyncListDto;
import com.ideatech.ams.account.dto.ReportStatisticsForDateDTO;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.bill.BillFromSource;
import com.ideatech.ams.account.enums.bill.BillStatus;
import com.ideatech.ams.account.enums.bill.CompanySyncOperateType;
import com.ideatech.ams.apply.cryptography.CryptoAddApplyAcctVo;
import com.ideatech.ams.apply.cryptography.CryptoEditApplyAcctVo;
import com.ideatech.ams.apply.cryptography.CryptoModifyAcctKeyMessage;
import com.ideatech.ams.apply.enums.ApplyEnum;
import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author vantoo
 * @date 15:43 2018/5/18
 */
public interface AllBillsPublicService {

	Long save(AllBillsPublicDTO billsPublic, UserDto userDto, Boolean isValidate) throws Exception;

	Long getRecId();

	AllAcct allBillsPublic2AllAcct(AllBillsPublicDTO dto);

	AllAcct allBillsPublic2AllAcctPbc(AllBillsPublicDTO dto) throws Exception;

	List<PbcSyncListDto> checkPbcSync();

	PbcUserAccount systemPbcUser2PbcUser(PbcAccountDto pbcAccountDto);

	/**
	 * 提交的form信息校验
	 * @param
	 * @return
	 */
	void toValidate(Map<String, String> formData) throws Exception;

	/**
	 * @author jogy.he
	 * @description 更新流水的最终状态
	 * @param id
	 *            流水的id
	 * @return
	 */
	int updateFinalStatus(Long id);
	/**
	 * 更新流水状态为最终状态(正常走完全周期的流水)
	 * @param billsPublic
	 * @param userId
	 */
	void updateFinalStatus(AllBillsPublicDTO billsPublic, Long userId) throws Exception;
	/**
	 * 更新流水状态为最终状态(正常走完全周期的流水)
	 * @param billId
	 */
	void updateFinalStatusById(Long billId) throws Exception;

	/**
	 * 流水列表
	 * @param info
	 * @param pageable
	 * @param code
	 * @return
	 */
	TableResultResponse<AllBillsPublicDTO> query(AllBillsPublicDTO info, Pageable pageable, String code);

	TableResultResponse<AllBillsPublicSearchDTO> query(final AccountBillsAllSearchInfo accountBillsAllSearchInfo, final String code, Pageable pageable);

	/**
	 * 根据账户账号获取流水列表
	 *
	 * @param acctNo 账号
	 */
	TableResultResponse<AllBillsPublicSearchDTO> query(String acctNo, Pageable pageable);

	AllBillsPublicDTO submit(Long currentUserId, Map<String,String> formData);

	ObjectRestResponse<AllBillsPublicDTO> getOneDetails(Long id, String billType);

	AllBillsPublicDTO findOne(Long id);

	/**
	 * 流水数量
	 * @return
	 */
	List<String> getBillsCounts();

	/**
	 * 流水数量 增加白名单的查询
	 * @return
	 */
	List<String> getBillsCounts(String whiteList);

	String getCounts();

	/**
	 * 更新审核状态
	 * @param billsPublic
	 * @param status
	 * @param userId
	 * @param description
	 */
	void updateApproveStatus(AllBillsPublicDTO billsPublic, BillStatus status, Long userId, String description);

	Map<String,Object> synchronizeData(Map<String,String> formData, AllBillsPublicDTO allBillsPublic);

	AllBillsPublicDTO submitRecord(Long userId, Map<String, String> formData, String recordType);

	void writeLog(Map<String, String> formData,Long refBillId);
	/**
	 * 根据信息上报并且更新上报后的状态
	 * @param isSyncAms
	 * @param isSyncEccs
	 * @param billsPublic
	 */
	void syncAndUpdateStaus(Boolean isSyncAms, Boolean isSyncEccs, AllBillsPublicDTO billsPublic, UserDto userDto, CompanySyncOperateType syncOperateType) throws Exception;

	Boolean syncAndUpdateStaus2(Boolean isSyncAms, Boolean isSyncEccs, AllBillsPublicDTO billsPublic, UserDto userDto, CompanySyncOperateType syncOperateType) throws Exception;

	/**
	 * 初始化流水信息
	 * @param billsPublic
	 * @param organizationDto
	 * @param billFromSource
	 */
	void initBillsPublic(AllBillsPublicDTO billsPublic, OrganizationDto organizationDto, BillFromSource billFromSource);

	/**
	 * 根据旧的流水数据设置目前流水信息
	 * @param accountBillsAllInfo
	 * @param billsPublicDTO
	 */
	void setSyncStatusByOldBill(AccountBillsAllInfo accountBillsAllInfo, AllBillsPublicDTO billsPublicDTO);

	// 设置账户名称
	void setAcctName(AllBillsPublicDTO entity);

	AllBillsPublicDTO conversion(AllBillsPublicDTO info1);

	/**
	 * 基于存量变更的流水和客户主表进行字段比较和覆盖
	 * @param billsPublic
	 */
//    void changeCompare(AllBillsPublicDTO billsPublic);


	/**
	 * 根据系统类型获取当前上报状态
	 * @param userType
	 * @return
	 */
	Boolean getSyncStatus(EAccountType userType, AllBillsPublicDTO billsPublic);

	/**
	 * 人行变更上报的字段
	 *
	 * @param account
	 * @param changedFieldList
	 */
	List<String> getChangeFieldsNeedPbcSync(AllBillsPublicDTO account);

	/**
	 * 信用机构变更上报的字段
	 *
	 * @param account
	 * @param changedFieldList
	 */
	List<String> getChangeFieldsNeedEccsSync(AllBillsPublicDTO account);

	/**
	 * 人行变更必须上报的字段
	 *
	 * @param account
	 * @param changedFieldList
	 */
	List<String> getChangeFieldMustPbcSync(AllBillsPublicDTO account);

	/**
	 * 信用机构变更上报的字段
	 *
	 * @param account
	 * @param changedFieldList
	 */
	List<String> getChangeFieldEccsSync(AllBillsPublicDTO account);

	/**
	 * 针对变更，没有传递过来的字段用存量的数据填充，并返回存量的数据
	 * @param billsPublic
	 * @return
	 */
	AllBillsPublicDTO changeCompareWithOriginal(AllBillsPublicDTO billsPublic);

	/**
	 *
	 * @param allBillsPublicDTO
	 * @param billsPublic
	 * 方法修改：原无返回对象  void   修改后：返回变更字段List
	 */
	List<String> changeCompareWithOld(AllBillsPublicDTO allBillsPublicDTO,AllBillsPublicDTO billsPublic);

	void acctBigType2AcctType(AllBillsPublicDTO billsPublic);

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	void sendModifyStatus(AllBillsPublicDTO dataAccount, String status);

	/**
	 * 修改状态新接口
	 * @param applyId
	 * @param status
	 * @param pbcCode
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	void sendModifyStatus(String applyId, ApplyEnum status, String pbcCode);

	/**
	 * 新增预约单接口--预约新模式
	 * @param applyAcctVo
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	String pushApplyAddAcct(CryptoAddApplyAcctVo applyAcctVo, OrganizationDto organDto);

	/**
	 * 修改预约单接口--预约新模式
	 * @param applyAcctVo
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	void pushApplyEditAcct(CryptoEditApplyAcctVo applyAcctVo);

	/**
	 * 推送核准号--预约新接口
	 * @param acctKeyMessage
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	void pushApplyAcctKey(CryptoModifyAcctKeyMessage acctKeyMessage);

	Map<String,String> reject(Long currentUserId, Long formId, Map<String,String> formData) throws EacException;

	/**
	 * 审核通过
	 * @param currentUserId
	 * @param formId
	 * @param formData
	 * @return
	 * @throws EacException
	 */
	Map<String,String> verifyPass(Long currentUserId, Long formId, Map<String,String> formData) throws EacException;

	void setCondition(String code, AccountBillsAllInfo accountBillsAllInfo);

	List<String> addBillCounts(final String code, final AccountBillsAllSearchInfo accountBillsAllInfo, List<String> billAllCountsList);

	AllBillsPublicDTO findByBillId(Long billId);

	/**
	 * 装饰AllBillsPublic对象
	 *
	 * @param accountId
	 * @param refBillId
	 * @param customerLogId
	 * @param createdDate
	 * @param allBillsPublicDTO
	 */
	void convertAllBillsPublic(Long accountId, Long refBillId, Long customerLogId, Date createdDate, AllBillsPublicDTO allBillsPublicDTO);

	/**
	 * 更新最终状态，并更新人行的上报状态
	 * 一般情况供无需上报的账户使用
	 * @param billsPublic
	 * @param userId
	 */
	void updateSyncStatusAndFinishBill(AllBillsPublicDTO billsPublic, Long userId) throws Exception;

	/**
	 * 判断客户号或者客户名称对应的基本户是否已经存在
	 * true : 没有对应的基本户
	 * false : 有对应的基本户
	 * @param customerNo
	 * @param depositorName
	 * @return
	 */
	boolean checkJibenByCustomerNoOrDepositorName(String customerNo,String depositorName);

	/**
	 * 首页新增账户和新增流水列表
	 * @param accountBillsAllSearchInfo
	 * @param code
	 * @param pageable
	 * @return
	 */
	TableResultResponse<AllBillsPublicSearchDTO> listForBills(AccountBillsAllSearchInfo accountBillsAllSearchInfo, String code, Pageable pageable);

	AllBillsPublicDTO printChang(Long billid,AllBillsPublicDTO info);

	String checkSync();

	/**
	 * 开户成功的数据统计详情（需要上报的数据）
	 *
	 * @param acctNo              账号
	 * @param kernelOrgCode       网点机构号
	 * @param depositorName       存款人名称
	 * @param openAccountSiteType 本异地标识
	 * @param createdBy           申请人
	 * @param depositorType       账户性质
	 * @param acctType            存款人类别
	 * @param beginDate           上报开始时间
	 * @param endDate             上报结束时间
	 * @param beginDateApply      申请开始时间
	 * @param endDateApply        申请结束时间
	 * @param organFullId         权限控制机构fullId
	 * @param pageable            分页标签
	 */
	TableResultResponse<AllBillsPublicSearchDTO> openStatisticsDetailList(String acctNo, String kernelOrgCode, String depositorName, String openAccountSiteType, String createdBy,
																		  String depositorType, String acctType, String organFullId, String beginDate, String endDate,
																		  Date beginDateApply, Date endDateApply, Pageable pageable);

	/**
	 * 当日报送数据列表
	 */
	TableResultResponse<ReportStatisticsForDateDTO> statisticsForDateList(String startDate,String endDate,String organFullId, Pageable pageable);

	/**
	 * 查询指定日期报送数据
	 * @param date yyyy-MM-dd
	 * @param organFullId
	 */
	ReportStatisticsForDateDTO statisticsForDate(String date, String organFullId);

	TableResultResponse<AllBillsPublicSearchDTO> statisticsForDateDetailList(AccountBillsAllSearchInfo accountBillsAllSearchInfo, Pageable pageable);

	/**
	 * 判断基本户非临时是不是取消核准业务
	 * @param info
	 */
	void setCancelHeZhun(AllBillsPublicDTO info);

	/**
	 * 根据旧的数据设置目前流水信息
	 * @param dbBillsPublicDTO
	 * @param billsPublicDTO
	 */
	void dataCoverage(AllBillsPublicDTO dbBillsPublicDTO, AllBillsPublicDTO billsPublicDTO);

	/**
	 * 根据流水号获取流水+账户+客户所有信息
	 * @param billNo
	 * @return
	 */
	AllBillsPublicDTO findFullInfoByBillNo(String billNo);

	/**
	 * 导出当日报送数据
	 * @param accountBillsAllSearchInfo
	 * @return
	 */
	IExcelExport statisticsForDateDetailsExport(AccountBillsAllSearchInfo accountBillsAllSearchInfo);

	/**
	 * 导出开销户统计报送数据
	 * @param accountBillsAllSearchInfo
	 * @return
	 */
	IExcelExport statisticsForKXHtailsExport(AccountBillsAllSearchInfo accountBillsAllSearchInfo);

	/**
	 * 预约新模式人行对人行上报结果的数据处理
	 * @param isSyncSuccess
	 * @param billsPublic
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	void applyFinalStatusUpdate(Boolean isSyncSuccess, AllBillsPublicDTO billsPublic);

	/**
	 * 获取需要重新开户的变更字段集合
	 *
	 * @param allBillsPublicDTO
	 * @param billsPublic
	 * @return
	 */
	List<String> getNeedOpenFiledList(AllBillsPublicDTO allBillsPublicDTO, AllBillsPublicDTO billsPublic);
}
