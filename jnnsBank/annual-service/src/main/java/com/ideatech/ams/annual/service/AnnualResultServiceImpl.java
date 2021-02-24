package com.ideatech.ams.annual.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.annual.dao.AnnualResultDao;
import com.ideatech.ams.annual.dto.*;
import com.ideatech.ams.annual.dto.poi.AnnualFailPoi;
import com.ideatech.ams.annual.dto.poi.AnnualManualSuccessPoi;
import com.ideatech.ams.annual.dto.poi.AnnualSystemSuccessPoi;
import com.ideatech.ams.annual.dto.poi.AnnualWaitingProcessPoi;
import com.ideatech.ams.annual.entity.AnnualResult;
import com.ideatech.ams.annual.entity.FetchPbcInfo;
import com.ideatech.ams.annual.enums.DataProcessStatusEnum;
import com.ideatech.ams.annual.enums.ForceStatusEnum;
import com.ideatech.ams.annual.enums.PbcSubmitStatusEnum;
import com.ideatech.ams.annual.enums.ResultStatusEnum;
import com.ideatech.ams.annual.poi.AnnualFailExport;
import com.ideatech.ams.annual.poi.AnnualManualSuccessExport;
import com.ideatech.ams.annual.poi.AnnualSystemSuccessExport;
import com.ideatech.ams.annual.poi.AnnualWaitingProcessExport;
import com.ideatech.ams.annual.spi.AnnualResultProcessor;
import com.ideatech.ams.annual.util.AnnualResultMsgFormat;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.kyc.service.SaicRequestService;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.enums.AmsAnnualResultStatus;
import com.ideatech.ams.pbc.utils.NumberUtils;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.ws.api.service.AnnualResultApiService;
import com.ideatech.ams.ws.api.service.AnnualResultNoticeApiService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 *
 *
 * @author van
 * @date 9:35 2018/8/9
 */
@Service
@Slf4j
public class AnnualResultServiceImpl extends BaseServiceImpl<AnnualResultDao, AnnualResult, AnnualResultDto> implements AnnualResultService {

//	@Value("${ams.annual.submit:false}")
//	private Boolean needSubmit;

	@Value("${ams.annual.saicLocal:true}")
	private Boolean saicLocal;//工商采集是否采集本地数据

	@Value("${ams.annual.noticeMessage:年检结果通知短信模版}")
	private String noticeMessage;

    @Value("${ams.annual.business-license-expired:false}")
    private Boolean businessLicenseExpired;

    @Value("${ams.annual.exportNum:50000}")
    private Integer exportNum;


    @Autowired
    private Map<String, AnnualResultProcessor> annualResultProcessors;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PbcAmsService pbcAmsService;

    @Autowired
    private AnnualResultApiService annualResultApiService;

	@Autowired
	private AnnualResultNoticeApiService annualResultNoticeApiService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private CollectConfigService collectConfigService;

	@Autowired
	private SaicInfoService saicInfoService;

	@Autowired
	private SaicRequestService saicRequestService;

	@Autowired
	private AnnualTaskService annualTaskService;

    @Autowired
    private CompareFieldsService compareFieldsService;

	@Autowired
	private AnnualStatisticsService annualStatisticsService;

	@Autowired
	private AccountsAllService accountsAllService;

	@Value("${import.file.split:|!}")
	private String split;

	@Override
	public void updateAnnualResultData(Long taskId, CoreCollectionDto coreDataDto,Map<String, OrganizationDto> organInMap) {
		String acctNo = coreDataDto.getAcctNo();
		String organFullId = coreDataDto.getOrganFullId();
		String organPbcCode = "";
		AnnualResult annualResult = getBaseDao().findByTaskIdAndAcctNo(taskId, acctNo);
		if (annualResult == null) {
			annualResult = new AnnualResult();
			annualResult.setAcctNo(acctNo);
			annualResult.setTaskId(taskId);
			//整合核心跟人行的数据时   核心单边数据设置账户性质
			annualResult.setAcctType(CompanyAcctType.str2enum(coreDataDto.getAcctType()));
		}

		boolean isAlreadySuccess = annualResult.getPbcSubmitStatus() == PbcSubmitStatusEnum.SUCCESS || annualResult.getResult() == ResultStatusEnum.PASS
				|| annualResult.getForceStatus() == ForceStatusEnum.SUCCESS || annualResult.getForceStatus() == ForceStatusEnum.AGAIN_SUCCESS
				|| annualResult.getDataProcessStatus() == DataProcessStatusEnum.SUCCESS;
		if (isAlreadySuccess) {
			log.info("年检结果已处理，不更新核心数据");
			return;
		}

		if (StringUtils.isBlank(annualResult.getOrganFullId())) {
			annualResult.setOrganFullId(organFullId);
		}
		if (StringUtils.isBlank(annualResult.getOrganPbcCode())) {
			if (StringUtils.isNotBlank(organFullId)) {
				if(organInMap.containsKey(organFullId)){
					organPbcCode = organInMap.get(organFullId).getPbcCode();
				}
//				OrganizationDto organizationDto = organizationService.findByOrganFullId(organFullId);
//				if (organizationDto != null) {
//					organPbcCode = organizationDto.getPbcCode();
//				}
			}
			annualResult.setOrganPbcCode(organPbcCode);
		}
		//插入网点机构号
		if(StringUtils.isBlank(annualResult.getOrganCode())){
			if (null!=organInMap.get(organFullId)){
				annualResult.setOrganCode(organInMap.get(organFullId).getCode());
			}
		}
		annualResult.setDepositorName(coreDataDto.getDepositorName());
		annualResult.setCoreData(transformData2AnnualData(coreDataDto));
		getBaseDao().save(annualResult);
	}

	@Override
	public void updateAnnualResultData(Long taskId, FetchPbcInfoDto pbcDataDto) {
		String acctNo = pbcDataDto.getAcctNo();
		String organFullId = pbcDataDto.getOrganFullId();
		String organPbcCode = "";
		OrganizationDto organizationDto = null;
		AnnualResult annualResult = getBaseDao().findByTaskIdAndAcctNo(taskId, acctNo);
		if (annualResult == null) {
			annualResult = new AnnualResult();
			annualResult.setAcctNo(acctNo);
			annualResult.setTaskId(taskId);
			//增加账户性质字段
			annualResult.setAcctType(CompanyAcctType.str2enum(pbcDataDto.getAcctType().getFullName()));
		}

		boolean isAlreadySuccess = annualResult.getPbcSubmitStatus() == PbcSubmitStatusEnum.SUCCESS || annualResult.getResult() == ResultStatusEnum.PASS
				|| annualResult.getForceStatus() == ForceStatusEnum.SUCCESS || annualResult.getForceStatus() == ForceStatusEnum.AGAIN_SUCCESS
				|| annualResult.getDataProcessStatus() == DataProcessStatusEnum.SUCCESS;
		if (isAlreadySuccess) {
			log.info("年检结果已处理，不更新人行数据");
			return;
		}

		if (StringUtils.isBlank(annualResult.getOrganFullId())) {
			annualResult.setOrganFullId(organFullId);
		}
		if (StringUtils.isBlank(annualResult.getOrganPbcCode())) {
			if (StringUtils.isNotBlank(organFullId)) {
				organizationDto = organizationService.findByOrganFullId(organFullId);
				if (organizationDto != null) {
					organPbcCode = organizationDto.getPbcCode();
				}
			}
			annualResult.setOrganPbcCode(organPbcCode);
		}
		//插入网点机构号
		if(StringUtils.isBlank(annualResult.getOrganCode())){
			annualResult.setOrganCode(organizationDto.getCode());
		}
		annualResult.setDepositorName(pbcDataDto.getDepositorName());
		annualResult.setPbcData(transformData2AnnualData(pbcDataDto));
		getBaseDao().save(annualResult);
	}

	@Override
	public void updateAnnualResultData(Long taskId, FetchSaicInfoDto saicDataDto) {
		if (saicDataDto != null) {
			String depositorName = saicDataDto.getCustomerName();
			List<AnnualResult> annualResults = getBaseDao().findByTaskIdAndDepositorName(taskId, depositorName);
			for (AnnualResult annualResult : annualResults) {
				boolean isAlreadySuccess = annualResult.getPbcSubmitStatus() == PbcSubmitStatusEnum.SUCCESS || annualResult.getResult() == ResultStatusEnum.PASS
						|| annualResult.getForceStatus() == ForceStatusEnum.SUCCESS || annualResult.getForceStatus() == ForceStatusEnum.AGAIN_SUCCESS
						|| annualResult.getDataProcessStatus() == DataProcessStatusEnum.SUCCESS;
				if (isAlreadySuccess) {
					log.info("年检结果已处理，不更新工商数据");
					continue;
				}
				if (annualResult != null) {
					annualResult.setSaicData(saicDataDto.getIdpJsonStr());
					getBaseDao().save(annualResult);
				}
			}
		}
	}

	@Override
	public TableResultResponse<AnnualResultDto> queryByCode(AnnualResultDto condition, String code, Pageable pageable) {
		AnnualResultProcessor annualResultProcessor = annualResultProcessors.get(code + "AnnualResultProcessor");
		if (annualResultProcessor == null) {
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "未找到年检处理器:" + code + "AnnualResultProcessor");
		}

		condition.setOrganFullId(SecurityUtils.getCurrentOrgFullId());

		Page<AnnualResultDto> items = annualResultProcessor.query(condition, pageable);

		return new TableResultResponse<AnnualResultDto>((int)items.getTotalElements(), items.getContent());
	}

	@Override
	public TableResultResponse<AnnualResultDto> querySuccessByCode(AnnualResultDto condition, String code,
			Pageable pageable) {


		return null;
	}

	@Override
	public ResultDto<AnnualResultDto> submitAnnualAccount(Long resultId) {

//		Boolean checkAcctType = false;
//		String[] acctType = null;
		//新增配置项  是否根据配置的账户性质去年检
//		if(checkAcctType == null){

		Boolean checkAcctType = null;
		String[] acctType = null;

		ConfigDto annualcheckAcctType = configService.findOneByConfigKey("annualcheckAcctType");
        if(annualcheckAcctType == null){
            checkAcctType = false;
        }else if(annualcheckAcctType != null && Boolean.valueOf(annualcheckAcctType.getConfigValue())){
            checkAcctType = true;
            ConfigDto annualAcctType = configService.findOneByConfigKey("annualAcctType");
            if(annualAcctType != null && StringUtils.isNotBlank(annualAcctType.getConfigValue())){
                acctType = annualAcctType.getConfigValue().split("\\|!");
            }
        } else {
            checkAcctType = false;
        }
//		}
		log.info("是否根据账户性质进行年检提交：" + checkAcctType);


		AnnualResult annualResult = getBaseDao().findOne(resultId);
		if (annualResult==null){
			return ResultDtoFactory.toAck(resultId+"年检结果不存在。");
		}
        String username = SecurityUtils.getCurrentUsername();
        if(StringUtils.isBlank(username)){
            //接口修改时没有登录用户，直接使用虚拟用户
            username = "virtual";
        }

        //判断账户性质是否需要上报人行
        if(checkAcctType){
        	//如果根据账户性质来上报人行的话  getAnnualResult的账户性质
			CompanyAcctType accType = annualResult.getAcctType();
			if(!Arrays.asList(acctType).contains(accType.toString())){
				log.info("账户性质【" +accType.toString() + "】无需提交人行年检！");
				return ResultDtoFactory.toAck("该账户性质不允许提交人行年检，请确认后再进行提交！");
			}
		}

		boolean success = false;
        //机构页面配置是否该地区人行打标记
		Boolean needSubmit = annualTaskService.getIsAnnualSubmit(annualResult.getOrganFullId());
		if (!needSubmit) {
			//不去人行打标记、直接修改数据状态
			log.info("{}不去人行打标记、直接修改数据状态",annualResult.getAcctNo());
			//防止重复提交
			if (annualResult.getPbcSubmitStatus() == PbcSubmitStatusEnum.NO_NEED_SUBMIT && annualResult.getResult() == ResultStatusEnum.PASS ) {
				log.info("{}已提交该数据，请勿重复提交",annualResult.getAcctNo());
				return ResultDtoFactory.toAck("已提交该数据，请勿重复提交");
			}
			//过滤已经打过标记且通过年检的账户
			if (annualResult.getPbcSubmitStatus() == PbcSubmitStatusEnum.SUCCESS && annualResult.getResult() == ResultStatusEnum.PASS ) {
				log.info("{}已提交该数据，请勿重复提交",annualResult.getAcctNo());
				return ResultDtoFactory.toAck("已提交该数据，请勿重复提交");
			}
			//无需提交的直接成功
			annualResult.setPbcSubmitStatus(PbcSubmitStatusEnum.NO_NEED_SUBMIT);
			annualResult.setForceStatus(ForceStatusEnum.SUCCESS);
			annualResult.setPbcSubmitter(SecurityUtils.getCurrentUsername());
			annualResult.setPbcSubmitDate(DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis()));
			//增加年检数据处理情况
			annualResult.setDataProcessStatus(DataProcessStatusEnum.SUCCESS);
			annualResult.setDataProcessPerson(username);
			annualResult.setDataProcessDate(DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis()));
			annualResult.setResult(ResultStatusEnum.PASS);
			success = true;
		} else {
			//去人行打标记
			try {
				//防止重复提交
				if (annualResult.getPbcSubmitStatus() == PbcSubmitStatusEnum.SUCCESS && annualResult.getResult() == ResultStatusEnum.PASS) {
					log.info("{}已提交该数据，请勿重复提交",annualResult.getAcctNo());
					return ResultDtoFactory.toAck("已提交该数据，请勿重复提交");
				}
				AmsAnnualResultStatus amsAnnualResultStatus = pbcAmsService.sumitAnnualAccount(annualResult.getOrganFullId(), annualResult.getAcctNo());
				//成功和已年检的都为成功
				if (amsAnnualResultStatus == AmsAnnualResultStatus.Success || amsAnnualResultStatus == AmsAnnualResultStatus.AlreadyAnnual || amsAnnualResultStatus == AmsAnnualResultStatus.NewAccount) {
					//从待处理、年检失败的列表中，进行打标记的需要更新强制状态，系统成功和手动成功的不需要更新。
					if (annualResult.getResult() != ResultStatusEnum.PASS){
						annualResult.setForceStatus(ForceStatusEnum.SUCCESS);
					}
					annualResult.setPbcSubmitStatus(PbcSubmitStatusEnum.SUCCESS);
					annualResult.setPbcSubmitter(SecurityUtils.getCurrentUsername());
					annualResult.setPbcSubmitDate(DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis()));
                    annualResult.setPbcSubmitErrorMsg(null);
					//增加年检数据处理情况
					annualResult.setDataProcessStatus(DataProcessStatusEnum.SUCCESS);
					annualResult.setDataProcessPerson(username);
					annualResult.setDataProcessDate(DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis()));
					annualResult.setResult(ResultStatusEnum.PASS);
					annualResultApiService.submitPbcFinished(annualResult.getAcctNo());
					success = true;
				} else {
					annualResult.setForceStatus(ForceStatusEnum.INIT);
					annualResult.setPbcSubmitStatus(PbcSubmitStatusEnum.FAIL);
					annualResult.setPbcSubmitErrorMsg(amsAnnualResultStatus.getFullName());
					//增加年检数据处理情况
					annualResult.setDataProcessStatus(DataProcessStatusEnum.FAIL);
					annualResult.setDataProcessPerson(SecurityUtils.getCurrentUsername());
					annualResult.setDataProcessDate(DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis()));
					annualResult.setResult(ResultStatusEnum.FAIL);
				}
			} catch (Exception e) {

				log.warn("id={}，acctNo={}年检失败：",resultId,annualResult.getAcctNo(),e);
				if (e instanceof BizServiceException) {
				    //根据人行提交状态判断人行和年检结果，判断异常是否是核心标记的抛出的异常
				    if (PbcSubmitStatusEnum.SUCCESS.equals(annualResult.getPbcSubmitStatus()) && ResultStatusEnum.PASS.equals(annualResult.getResult())){
				        annualResult.setPbcSubmitErrorMsg("核心标记失败："+e.getMessage());
                    }else {
                        annualResult.setPbcSubmitErrorMsg(e.getMessage());
                    }
				} else {
					annualResult.setPbcSubmitErrorMsg("其他未知异常");
				}
                annualResult.setForceStatus(ForceStatusEnum.INIT);
                annualResult.setResult(ResultStatusEnum.FAIL);
                annualResult.setDataProcessStatus(DataProcessStatusEnum.FAIL);
                annualResult.setDataProcessPerson(SecurityUtils.getCurrentUsername());
                annualResult.setDataProcessDate(DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis()));
                success = false;
			}
		}
		getBaseDao().save(annualResult);

        //强制年检成功时，统计结果对应机构新增一条成功数
		if (success && StringUtils.isNotBlank(annualResult.getOrganFullId())) {
			annualStatisticsService.updateStatisticsSuccess(annualResult.getTaskId(), annualResult.getOrganCode(), "add");
		}
		return ResultDtoFactory.toAckData(ConverterService.convert(annualResult, AnnualResultDto.class));
	}

	/**
	 * 重新年检
	 * @param resultId 年检结果id
	 * @return 重新年检是否成功
	 */
	@Override
	public AnnualResultDto annualAgain(Long resultId) {
		AnnualResult annualResult = getBaseDao().findOne(resultId);
		AnnualResultDto ard = new AnnualResultDto();
		BeanCopierUtils.copyProperties(annualResult, ard);
		return this.annualAgain(ard);
	}

	/**
	 * 重新年检
	 * @param ard 年检结果
	 * @return 重新年检是否成功
	 */
	@Override
	public AnnualResultDto annualAgain(AnnualResultDto ard) {
		AnnualResult annualResult = getBaseDao().findOne(ard.getId());

		//设置异常情况为空（防止重复出现异常情况提示）
		annualResult.setAbnormal("");

		//重新获取人行数据
		String pbcData = "";
		try {
			AmsAccountInfo accountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(annualResult.getOrganCode(), annualResult.getAcctNo());
			if (accountInfo != null) {
				FetchPbcInfo dataAccount = new FetchPbcInfo();
				BeanUtils.copyProperties(accountInfo, dataAccount);
				dataAccount.setOrganFullId(annualResult.getOrganFullId());
				dataAccount.setAccountStatus(accountInfo.getAccountStatus());
				dataAccount.setParDate(DateUtils.getNowDateShort("yyyy-MM-dd hh:mm:ss"));
				dataAccount.setIndusRegArea(dataAccount.getRegAddress());
				dataAccount.setAnnualTaskId(annualResult.getTaskId());
//			dataAccount.setCollectTaskId(collectTaskId);
//			dataAccount.setCollectAccountId(collectAccountDto.getId());
				if (StringUtils.isNotBlank(dataAccount.getRegisteredCapital())) {
					BigDecimal bigDecimal = NumberUtils.formatCapital(NumberUtils.convertCapital(dataAccount.getRegisteredCapital()));
					dataAccount.setRegisteredCapital(bigDecimal.toString());
				}
				FetchPbcInfoDto fetchPbcInfoDto = new FetchPbcInfoDto();
				BeanUtils.copyProperties(dataAccount, fetchPbcInfoDto);
				pbcData = transformData2AnnualData(fetchPbcInfoDto);
			}
			log.info("客户重新年检前人行数据为：{}", annualResult.getPbcData());
			log.info("客户重新年检后人行数据为：{}", pbcData);
			annualResult.setPbcData(pbcData);
		} catch (Exception e) {
			log.info("重新年检时，年检结果表id为{}的数据，获取人行数据失败", annualResult.getId());
			e.printStackTrace();
		}

		//判断是否需要重新采集工商数据
		List<ConfigDto> cdList = configService.findByKey("annualSaicAgainEnabled");
		String config = "false";
		if (cdList.size() > 0) {
			config = cdList.get(0).getConfigValue();
		}
		if("true".equals(config)){
			SaicIdpInfo saicIdpInfo = null;
			if (saicLocal) {//采集本地数据
				saicIdpInfo = saicInfoService.getSaicInfoBaseLocalJustSaic(annualResult.getDepositorName());
			}
			String idpJsonStr;
			if (saicIdpInfo == null) {
				idpJsonStr = saicRequestService.getSaicInfoExactJson(annualResult.getDepositorName());//根据存款人名称查询工商信息
				if (StringUtils.isBlank(idpJsonStr)) {//若取不到工商数据，则根据核心数据中的工商注册号进行查询
					String regNo = "";//工商注册号
					if (StringUtils.isNotBlank(annualResult.getCoreData())) {
						JSONObject jsonObject = JSON.parseObject(annualResult.getCoreData());
						regNo = (String) jsonObject.get("regNo");
					}
					if (StringUtils.isNotBlank(regNo)) {
						idpJsonStr = saicRequestService.getSaicInfoExactJson(regNo);
					}
				}
				if (StringUtils.isNotBlank(idpJsonStr)) {
					saicIdpInfo = JSON.parseObject(idpJsonStr, SaicIdpInfo.class);
				}
			}
			if (saicIdpInfo != null) {
				if (StringUtils.isNotBlank(saicIdpInfo.getRegistfund())) {//资金转化
					saicIdpInfo.setRegistfund(NumberUtils.formatCapital(NumberUtils.convertCapital(saicIdpInfo.getRegistfund())).toString());
				}
				String saicData = JSON.toJSONString(saicIdpInfo);

				log.info("客户重新年检前工商数据为：{}", annualResult.getSaicData());
				log.info("客户重新年检后工商数据为：{}", saicData);
				annualResult.setSaicData(saicData);

			}
		}

		//更新年检结果表中人行、工商数据（核心数据为导入数据，不需要重新采集）
		getBaseDao().save(annualResult);

		//获取比对字段
		List<CompareFieldsDto> compareFieldsDtoList = compareFieldsService.listCompareRulesByTakId(annualResult.getTaskId());
		if (CollectionUtils.isEmpty(compareFieldsDtoList)) {
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "该任务未设置比对字段");
		}
		//获取比对规则
		Map<String, Map<String, CompareRuleDto>> compareRuleDtoMap = annualTaskService.getaAssemblyCompareRuleMap(annualResult.getTaskId());

		AnnualResultDto annualResultDto = new AnnualResultDto();
		BeanUtils.copyProperties(annualResult, annualResultDto);
		//重新比对
		annualTaskService.annualResultComparison(annualResultDto,
				businessLicenseExpired,
				compareFieldsDtoList,
				compareRuleDtoMap);
		//修改强制年检状态
        AnnualResult annualResult_new = getBaseDao().findOne(annualResult.getId());
        if (ResultStatusEnum.PASS.equals(annualResult_new.getResult())) {//若重新年检成功，则设置强制手动年检标识为“重新年检成功”，为前端查询做标识
            this.updateForceStatus(annualResult_new.getId(), ForceStatusEnum.AGAIN_SUCCESS);
			annualResult_new.setForceStatus(ForceStatusEnum.AGAIN_SUCCESS);
        }

        //重新年检成功时，统计结果对应机构新增一条成功数
		if(annualResult.getForceStatus() == ForceStatusEnum.AGAIN_SUCCESS && StringUtils.isNotBlank(annualResult.getOrganFullId())) {
			annualStatisticsService.updateStatisticsSuccess(annualResult.getTaskId(), annualResult.getOrganCode(), "add");
		}

		annualResultDto = new AnnualResultDto();
		BeanCopierUtils.copyProperties(annualResult_new, annualResultDto);
        return annualResultDto;
	}

	@Override
	public void annualAgainAll(AnnualResultDto condition, String code) {
		AnnualResultProcessor annualResultProcessor = annualResultProcessors.get(code + "AnnualResultProcessor");
		if (annualResultProcessor == null) {
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "未找到年检处理器:" + code + "AnnualResultProcessor");
		}
		condition.setOrganFullId(SecurityUtils.getCurrentOrgFullId());

		List<AnnualResultDto> ardList = annualResultProcessor.queryAll(condition);//所有待年检数据

//		执行重新年检任务
		annualTaskService.annualAgainAll(ardList, condition.getTaskId());
	}

	@Override
	public Long countAnnualResltByTaskId(Long id) {
		return getBaseDao().countByTaskId(id);
	}

	@Override
	@Transactional(timeout = 180)
	public void deleteAnnualResultByTaskId(Long taskId) {
		getBaseDao().deleteByTaskId(taskId);
	}

	@Override
	public void cleanAnnualResultByTaskId(Long taskId) {
		getBaseDao().cleanResult(taskId);
//		List<AnnualResult> annualResults =  getBaseDao().findByTaskId(taskId);
//		for (AnnualResult annualResult : annualResults) {
//			annualResult.setUnilateral(null);
//			annualResult.setMatch(null);
//			annualResult.setAbnormal("");
//			annualResult.setBlack(null);
//			annualResult.setSaicStatus(null);
//			annualResult.setResult(ResultStatusEnum.INIT);
//			annualResult.setPbcSubmitStatus(null);
//			annualResult.setPbcSubmitter("");
//			annualResult.setPbcSubmitDate("");
//			annualResult.setPbcSubmitErrorMsg("");
//			annualResult.setForceStatus(ForceStatusEnum.INIT);
//			annualResult.setDeleted(Boolean.FALSE);
//			annualResult.setCompareResult("");
//		}
//		getBaseDao().save(annualResults);
	}

	@Override
	public Long[] countProcessedAndPassedNum(Long taskId) {
		return new Long[]{countAnnualResltByTaskId(taskId),
				getBaseDao().countByTaskIdAndResultNot(taskId, ResultStatusEnum.INIT),
				getBaseDao().countByTaskIdAndMatch(taskId, true)};
	}

	@Override
	public Long[] countForceStatusAndPassedNum(Long taskId) {
		return new Long[]{getBaseDao().countByTaskIdAndForceStatus(taskId,ForceStatusEnum.SUCCESS),
				getBaseDao().countByTaskIdAndMatch(taskId, true)};
	}

	@Override
	public List<AnnualResultDto> listAnnualsByOrgan(Long taskId, String organFullId) {
		return ConverterService.convertToList(getBaseDao().findByTaskIdAndOrganFullIdAndResult(taskId, organFullId, ResultStatusEnum.INIT), AnnualResultDto.class);
	}

	@Override
	public List<AnnualResultDto> listAnnualsByOrganUnProcess(Long taskId, String organFullId) {
		return ConverterService.convertToList(getBaseDao().findByTaskIdAndOrganFullIdAndResult(taskId, organFullId, ResultStatusEnum.INIT), AnnualResultDto.class);
	}

	@Override
	public List<String> listOrgansByTaskId(Long taskId) {
		return getBaseDao().findOrganFullIdByTaskId(taskId);
	}

	@Override
	public void updateAnnualResult(AnnualResultDto annualResultDto) {
		AnnualResult annualResult = getBaseDao().findOne(annualResultDto.getId());
		BeanCopierUtils.copyProperties(annualResultDto, annualResult);
		getBaseDao().save(annualResult);
	}

	private String transformData2AnnualData(Object object) {
		if (object == null) {
			return "";
		}
		if (object instanceof CoreCollectionDto) {
			return JSON.toJSONString(object);
		} else if (object instanceof FetchPbcInfoDto) {
			return JSON.toJSONString(object);
		} else if (object instanceof FetchSaicInfoDto) {
			if (object != null) {
				return JSON.toJSONString(((FetchSaicInfoDto) object).getIdpJsonStr());
			}
		}
		return "";
	}

	@Override
	public boolean submitAnnualDataProcess(Long id) {
		//修改该数据的处理状态为 正在处理
		boolean result = false;
		AnnualResult annualResult = getBaseDao().findOne(id);
		if(annualResult != null){
			if(StringUtils.isNotBlank(annualResult.getDataProcessPerson())){
				result = true;
			}else{
				annualResult.setDataProcessStatus(DataProcessStatusEnum.PROCESSING);
				annualResult.setDataProcessPerson(SecurityUtils.getCurrentUser().getUsername());
				annualResult.setDataProcessDate(DateUtils.DateToStr(new Date(),"yyyyMMdd"));
				result = false;
			}
		}
		getBaseDao().save(annualResult);
		return result;
	}

	@Override
	public void dataProcessRecall(Long id) {
//修改该数据的处理状态为 正在处理
		AnnualResult annualResult = getBaseDao().findOne(id);
		if(annualResult != null){
			annualResult.setDataProcessStatus(DataProcessStatusEnum.WAIT_PROCESS);
			annualResult.setDataProcessPerson("");
			annualResult.setDataProcessDate("");
		}
		getBaseDao().save(annualResult);
	}

	@Override
	public boolean checkSelf(Long id) {
		boolean result = false;
		AnnualResult annualResult = getBaseDao().findOne(id);
		if(SecurityUtils.getCurrentUsername().equals(annualResult.getDataProcessPerson())){
			result = true;
		}
		return result;
	}

	@Override
	public void sendMessageNotice(AnnualResultDto annualResultDto, String legalTelephone) {
		String formatMessage = AnnualResultMsgFormat.formatMessage(noticeMessage, annualResultDto);

		annualResultNoticeApiService.annualResultMessageNotice(annualResultDto, formatMessage, legalTelephone);

	}

    @Override
    public void updateForceStatus(Long id, ForceStatusEnum forceStatusEnum) {
        AnnualResult annualResult = getBaseDao().findOne(id);
        annualResult.setForceStatus(forceStatusEnum);
        getBaseDao().save(annualResult);
    }

	@Override
	public List<IExcelExport> exportExcel(AnnualResultDto condition, String code) {
		AnnualResultProcessor annualResultProcessor = annualResultProcessors.get(code + "AnnualResultProcessor");
		if (annualResultProcessor == null) {
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "未找到年检处理器:" + code + "AnnualResultProcessor");
		}

		condition.setOrganFullId(SecurityUtils.getCurrentOrgFullId());

		List<AnnualResultDto> items = annualResultProcessor.queryAll(condition);
		List<IExcelExport> iExcelExportList = new ArrayList<>();
		IExcelExport iExcelExport = null;
		int sheetNum = 1;
		int num = 0;
		switch(code){
			case "waitingProcess":
				List<AnnualWaitingProcessPoi> annualWaitingProcessPoiList = new ArrayList<AnnualWaitingProcessPoi>();
				for (AnnualResultDto annualResultDto : items) {
					//5w数据一个sheet页
					if (num>=exportNum){
						iExcelExport = new AnnualWaitingProcessExport();
						((AnnualWaitingProcessExport) iExcelExport).setTitle(iExcelExport.getTitle() + sheetNum);
						iExcelExport.setPoiList(annualWaitingProcessPoiList);
						annualWaitingProcessPoiList = new ArrayList<>();
						iExcelExportList.add(iExcelExport);
						sheetNum++;
						num = 0;
					}
					AnnualWaitingProcessPoi a = new AnnualWaitingProcessPoi();
					BeanUtils.copyProperties(annualResultDto, a);
					String data = annualResultDto.getAbnormal();
					if (null != data) {
						data = setData(data);
						a.setAbnormal(data);
					}
					a.setUnilateral(annualResultDto.getUnilateral().getName());
					a.setSaicStatus(annualResultDto.getSaicStatus().getName());
					a.setDataProcessStatus(annualResultDto.getDataProcessStatus().getName());
					a.setMatch(Boolean.TRUE.equals(annualResultDto.getMatch()) ? "一致" : "不一致");
					a.setAcctType(annualResultDto.getAcctType().getValue());
					annualWaitingProcessPoiList.add(a);
					num++;
				}
				iExcelExport = new AnnualWaitingProcessExport();
				((AnnualWaitingProcessExport) iExcelExport).setTitle(iExcelExport.getTitle() + sheetNum);
				iExcelExport.setPoiList(annualWaitingProcessPoiList);
				iExcelExportList.add(iExcelExport);
				break;
			case "systemSuccess":
				List<AnnualSystemSuccessPoi> annualSystemSuccessPoiList = new ArrayList<AnnualSystemSuccessPoi>();
				for (AnnualResultDto annualResultDto : items) {
					//5w数据一个sheet页
					if (num >= exportNum) {
						iExcelExport = new AnnualSystemSuccessExport();
						((AnnualSystemSuccessExport) iExcelExport).setTitle(iExcelExport.getTitle() + sheetNum);
						iExcelExport.setPoiList(annualSystemSuccessPoiList);
						annualSystemSuccessPoiList = new ArrayList<>();
						iExcelExportList.add(iExcelExport);
						sheetNum++;
						num = 0;
					}
					AnnualSystemSuccessPoi annualSystemSuccessPoi = new AnnualSystemSuccessPoi();
					BeanUtils.copyProperties(annualResultDto, annualSystemSuccessPoi);
					annualSystemSuccessPoi.setResult(annualResultDto.getResult().getName());
					annualSystemSuccessPoi.setMatch(Boolean.TRUE.equals(annualResultDto.getMatch()) ? "一致" : "不一致");
					annualSystemSuccessPoi.setAcctType(annualResultDto.getAcctType().getValue());
					annualSystemSuccessPoiList.add(annualSystemSuccessPoi);
					num++;
				}
				iExcelExport = new AnnualSystemSuccessExport();
				((AnnualSystemSuccessExport) iExcelExport).setTitle(iExcelExport.getTitle() + sheetNum);
				iExcelExport.setPoiList(annualSystemSuccessPoiList);
				iExcelExportList.add(iExcelExport);
				break;
			case "manualSuccess":
				List<AnnualManualSuccessPoi> annualManualSuccessPoiList = new ArrayList<AnnualManualSuccessPoi>();

				for (AnnualResultDto annualResultDto : items) {
					//5w数据一个sheet页
					if (num >= exportNum) {
						iExcelExport = new AnnualManualSuccessExport();
						((AnnualManualSuccessExport) iExcelExport).setTitle(iExcelExport.getTitle() + sheetNum);
						iExcelExport.setPoiList(annualManualSuccessPoiList);
						annualManualSuccessPoiList = new ArrayList<>();
						iExcelExportList.add(iExcelExport);
						sheetNum++;
						num = 0;
					}
					AnnualManualSuccessPoi a = new AnnualManualSuccessPoi();
					BeanUtils.copyProperties(annualResultDto, a);
					a.setAcctType(annualResultDto.getAcctType().getValue());
					String data = annualResultDto.getAbnormal();
					if (null != data) {
						data = setData(data);
						a.setAbnormal(data);
					}
					annualManualSuccessPoiList.add(a);
					num++;
				}
				iExcelExport = new AnnualManualSuccessExport();
				((AnnualManualSuccessExport) iExcelExport).setTitle(iExcelExport.getTitle() + sheetNum);
				iExcelExport.setPoiList(annualManualSuccessPoiList);
				iExcelExportList.add(iExcelExport);
				break;
			case "fail":
				List<AnnualFailPoi> annualFailPoiList = new ArrayList<AnnualFailPoi>();

				for (AnnualResultDto annualResultDto : items) {
					//5w数据一个sheet页
					if (num >= exportNum) {
						iExcelExport = new AnnualFailExport();
						((AnnualFailExport) iExcelExport).setTitle(iExcelExport.getTitle() + sheetNum);
						iExcelExport.setPoiList(annualFailPoiList);
						annualFailPoiList = new ArrayList<>();
						iExcelExportList.add(iExcelExport);
						sheetNum++;
						num = 0;
					}
					AnnualFailPoi a = new AnnualFailPoi();
					BeanUtils.copyProperties(annualResultDto, a);
					a.setAcctType(annualResultDto.getAcctType().getValue());
					String data = annualResultDto.getAbnormal();
					if (null != data) {
						data = setData(data);
						a.setAbnormal(data);
					}
					annualFailPoiList.add(a);
					num++;
				}
				iExcelExport = new AnnualFailExport();
				((AnnualFailExport) iExcelExport).setTitle(iExcelExport.getTitle() + sheetNum);
				iExcelExport.setPoiList(annualFailPoiList);
				iExcelExportList.add(iExcelExport);
				break;
			default:
				break;
		}
		return iExcelExportList;
	}

	@Override
	public AnnualResultDto annualResultSearch(String acctNo) {
		List<AnnualResult> list = getBaseDao().findByAcctNoOrderByCreatedDateDesc(acctNo);
		AnnualResultDto ard = null;
		if(CollectionUtils.isNotEmpty(list) || list.size()>0){
			ard = new AnnualResultDto();
			BeanCopierUtils.copyProperties(list.get(0), ard);
		}
		return ard;
	}

	@Override
	public void batchDelete(Long[] ids) {
		for (Long id : ids) {
			AnnualResultDto dto = findById(id);
			annualStatisticsService.subStatisticsCount(dto.getTaskId(), dto.getOrganCode());
			AnnualTaskDto annualTaskDto = annualTaskService.findById(dto.getTaskId());
			annualTaskDto.setSum(annualTaskDto.getSum() - 1);
			annualTaskDto.setProcessedNum(annualTaskDto.getProcessedNum() - 1);
			annualTaskService.save(annualTaskDto);
			dto.setDeleted(Boolean.TRUE);
			save(dto);
		}
	}

	@Override
	public String noCheckAnnual(Long[] ids) {
		//无需年检--针对在年检过程中被久悬或者被撤销的账户的一个处理逻辑
		//根据账号在账管中（本地账管，不是人行账管）查询是不是已久悬或者已注销，对这批年检数据进行账户状态的变更，新增无需年检tab展现；
		String msg = "";
		for (Long id : ids) {
			AnnualResultDto dto = findById(id);
			String acctNo = dto.getAcctNo();
			//账户主表数据
			AccountsAllInfo accountsAllInfo = accountsAllService.findByAcctNo(acctNo);
			if(accountsAllInfo != null){
				if(accountsAllInfo.getAccountStatus() == AccountStatus.suspend || accountsAllInfo.getAccountStatus() == AccountStatus.revoke){
					dto.setResult(ResultStatusEnum.NO_CHECK_ANNUAL);
					annualStatisticsService.subStatisticsCount(dto.getTaskId(), dto.getOrganCode());
					AnnualTaskDto annualTaskDto = annualTaskService.findById(dto.getTaskId());
					annualTaskDto.setSum(annualTaskDto.getSum() - 1);
					annualTaskDto.setProcessedNum(annualTaskDto.getProcessedNum() - 1);
					annualTaskService.save(annualTaskDto);
					save(dto);
				}else{
					return "该数据状态为【" + accountsAllInfo.getAccountStatus().getFullName() + "】，无法执行该操作！";
				}
			}else{
				return "本账管没有账号为【" + acctNo + "】的数据，无法执行该操作！";
			}
		}
		return null;
	}

	@Override
	public void cleanUnSuccessAnnualResult(Long taskId) {
		getBaseDao().cleanUnSuccessResult(taskId);
	}

	/**
	 * 替换data数据
	 * @param data
	 */
	private String setData(String data){
		if(data.indexOf("INIT") != -1) {
			data = data.replace("INIT", "未采集");
		}
		if(data.indexOf("NOT_FOUND") != -1) {
			data = data.replace("NOT_FOUND", "未找到");
		}
		if(data.indexOf("CANCEL") != -1) {
			data = data.replace("CANCEL", "注销撤销");
		}
		if(data.indexOf("REVOKE") != -1) {
			data = data.replace("REVOKE", "吊销");
		}
		if(data.indexOf("ABNORMAL_OPERATION") != -1) {
			data = data.replace("ABNORMAL_OPERATION", "经营异常");
		}
		if(data.indexOf("NO_ANNUAL_REPORT") != -1) {
			data = data.replace("NO_ANNUAL_REPORT", "无年报");
		}
		if(data.indexOf("BUSINESS_LICENSE_EXPIRED") != -1) {
			data = data.replace("BUSINESS_LICENSE_EXPIRED", "营业执照到期");
		}
		if(data.indexOf("BLACK") != -1) {
			data = data.replace("BLACK", "黑名单");
		}
		if(data.indexOf("ILLEGAL") != -1) {
			data = data.replace("ILLEGAL", "严重违法");
		}
		return data;
	}
}
