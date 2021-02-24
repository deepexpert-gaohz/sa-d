package com.ideatech.ams.ws.api.service;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.customer.dto.CustomerTuneSearchHistoryDto;
import com.ideatech.ams.customer.dto.SaicMonitorDto;
import com.ideatech.ams.customer.enums.CustomerTuneSearchEntranceType;
import com.ideatech.ams.customer.enums.CustomerTuneSearchType;
import com.ideatech.ams.customer.enums.SaicMonitorEnum;
import com.ideatech.ams.customer.service.CustomerTuneSearchHistoryService;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.kyc.dto.*;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.service.*;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.annotation.InterfaceLog;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class SaicApiServiceImpl implements SaicApiService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SaicApiServiceImpl.class);

	@Autowired
	private SaicInfoService saicInfoService;

	@Autowired
	private StockHolderService stockHolderService;

	@Autowired
	private BeneficiaryService beneficiaryService;

	@Autowired
	private ManagementService managementService;

	@Autowired
	private ChangeRecordService changeRecordService;

	@Autowired
	private ChangeMessService changeMessService;

	@Autowired
	private IllegalService illegalService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private BaseAccountService baseAccountService;

	@Autowired
	private EquityShareService equityShareService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private SaicMonitorService saicMonitorService;

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerTuneSearchHistoryService customerTuneSearchHistoryService;

	@Value("${ams.customerManagerSearch.saicByName.use:false}")
	private Boolean saicByNameFlag;

	@Override
//	@InterfaceLog
	public ResultDto checkSaicInfo(String name, String key) {
		if (StringUtils.isBlank(name) || StringUtils.isBlank(key)) {
			return ResultDtoFactory
					.toApiError(ResultCode.PARAM_IS_BLANK.code(), ResultCode.PARAM_IS_BLANK.message(), null);
		}

		SaicIdpInfo saicIdpInfo = saicInfoService.getSaicInfoFull(SearchType.REAL_TIME, name, key, null);
		UserDto userDto = userService.findByUsername(name);
		if (userDto==null){
			//纯接口调用使用虚拟用户
			userDto = userService.findById(2L);
		}

		return insertSaicInfo(key, saicIdpInfo, userDto, name);
	}

	@Override
	public ResultDto checkSaicInfo(String name, String key, String organCode) {
		if (StringUtils.isBlank(name) || StringUtils.isBlank(key)) {
			return ResultDtoFactory
					.toApiError(ResultCode.PARAM_IS_BLANK.code(), ResultCode.PARAM_IS_BLANK.message(), null);
		}

		SaicIdpInfo saicIdpInfo = saicInfoService.getSaicInfoFull(SearchType.REAL_TIME, name, key, null);
		UserDto userDto = userService.findByUsername(name);
		if (userDto==null){
			//纯接口调用使用虚拟用户
			userDto = userService.findById(2L);
		}

		if(StringUtils.isNotBlank(organCode)) {
			OrganizationDto organDto = organizationService.findByCode(organCode);
			if(organDto != null) {
				userDto.setOrgId(organDto.getId());
				userDto.setOrgName(organDto.getName());
			}
		}

		return insertSaicInfo(key, saicIdpInfo, userDto, name);
	}

	@Override
//	@InterfaceLog
	public ResultDto querySaicLocal(String name) {
		if (StringUtils.isBlank(name)) {
			return ResultDtoFactory
					.toApiError(ResultCode.PARAM_IS_BLANK.code(), ResultCode.PARAM_IS_BLANK.message(), null);
		}
		SaicInfoDto saicInfoDto = new SaicInfoDto();
		SaicIdpInfo saicIdpInfo = saicInfoService.getSaicInfoBaseLocal(name);
		SaicMonitorDto saicMonitorDto = new SaicMonitorDto();
		//纯接口调用使用虚拟用户
		UserDto userDto = userService.findById(2L);
		ConverterService.convert(saicIdpInfo, saicInfoDto);
		if (saicIdpInfo != null) {
			log.info(name + "查询成功，接口查询工商保存工商统计表");
			saicMonitorDto = saicMonitorService.getSaicMonitor(userDto.getUsername(),userDto.getOrgId(),name,saicIdpInfo.getId(),StringUtils.isNotBlank(saicIdpInfo.getUnitycreditcode()) ? saicIdpInfo.getUnitycreditcode() : saicIdpInfo.getRegistno(),SaicMonitorEnum.KYC);
			saicMonitorService.save(saicMonitorDto);
			return ResultDtoFactory.toApiSuccess(saicInfoDto);
		} else {
			log.info(name + "查询失败，接口查询工商保存工商统计表");
			saicMonitorDto = saicMonitorService.getSaicMonitor(userDto.getUsername(),userDto.getOrgId(),name,null,"",SaicMonitorEnum.KYC);
			saicMonitorService.save(saicMonitorDto);
			return ResultDtoFactory
					.toApiError(ResultCode.NO_DATA_EXIST.code(), ResultCode.NO_DATA_EXIST.message(), null);
		}
	}

	@Override
//	@InterfaceLog
	public ResultDto querySaicFull(String keyword) {
		JSONObject result = new JSONObject();
		Long saicId = null;
		SaicMonitorDto saicMonitorDto = new SaicMonitorDto();
		//纯接口调用使用虚拟用户
		UserDto userDto = userService.findById(2L);
		//基本信息
		try {
			SaicIdpInfo saicIdpInfo = saicInfoService
					.getSaicInfoFull(SearchType.REAL_TIME, SecurityUtils.getCurrentUsername(),
							URLDecoder.decode(keyword, "UTF-8"), SecurityUtils.getCurrentOrgFullId());
			if (saicIdpInfo != null) {
				log.info(keyword + "查询成功，接口查询工商保存工商统计表");
				saicId = saicIdpInfo.getId();
				saicMonitorDto = saicMonitorService.getSaicMonitor(userDto.getUsername(),userDto.getOrgId(),keyword,saicIdpInfo.getId(),StringUtils.isNotBlank(saicIdpInfo.getUnitycreditcode()) ? saicIdpInfo.getUnitycreditcode() : saicIdpInfo.getRegistno(),SaicMonitorEnum.KYC);
				saicMonitorService.save(saicMonitorDto);
			}else{
				log.info(keyword + "查询失败，接口查询工商保存工商统计表");
				saicMonitorDto = saicMonitorService.getSaicMonitor(userDto.getUsername(),userDto.getOrgId(),keyword,null,"",SaicMonitorEnum.KYC);
				saicMonitorService.save(saicMonitorDto);
			}
			result.put("saicIdpInfo", saicIdpInfo);
		} catch (Exception e) {
			log.error("工商基础信息查询异常", e);
		}
		//股权结构
		try {
			JSONObject resultJson = null;
			if (saicId != null) {
				resultJson = equityShareService.getEquityShareTreeJsonObject(SecurityUtils.getCurrentUsername(), saicId,
						SecurityUtils.getCurrentOrgFullId());
			}

			result.put("equityShare", resultJson);
		} catch (Exception e) {
			log.error("股权结构信息查询异常", e);
		}
		//基本户履历
		try {
			List<BaseAccountDto> baseAccountList = null;
			if (saicId != null) {
				baseAccountList = baseAccountService
						.getBaseAccountListBySaicInfoId(SecurityUtils.getCurrentUsername(), saicId,
								SecurityUtils.getCurrentOrgFullId());
			}
			result.put("baseAccount", baseAccountList);
		} catch (Exception e) {
			log.error("基本户履历信息查询异常", e);
		}
		//受益人
		try {
			List<BeneficiaryDto> beneficiaryList = null;
			if (saicId != null) {
				beneficiaryList = beneficiaryService
						.getBeneficiaryListBySaicInfoId(SecurityUtils.getCurrentUsername(), saicId,
								SecurityUtils.getCurrentOrgFullId());
			}
			result.put("beneficiary", beneficiaryList);
		} catch (Exception e) {
			log.error("受益人信息查询异常", e);
		}
		//实际控制人
		try {
			JSONObject jsonObject = null;
			if (saicId != null) {
				SupplementQueryDto supplementQueryVo = new SupplementQueryDto();
				supplementQueryVo.setSaicInfoId(saicId);
				supplementQueryVo.setType("ultimateOwner");
				jsonObject = saicInfoService.getCompanyPeopleInformation(supplementQueryVo);
			}
			result.put("supplement", jsonObject);
		} catch (Exception e) {
			log.error("实际控制人查询异常", e);
		}
		//董监高人员
		try {
			JSONObject resultJson = null;
			if (saicId != null) {
				resultJson = managementService.getManagersInfoBySaicInfoIdInLocal(saicId);
			}
			result.put("managers", resultJson);
		} catch (Exception e) {
			log.error("董监高人员信息查询异常", e);
		}
		//股东出资信息
		try {
			List<StockHolderDto> stockHolderList = null;
			if (saicId != null) {
				stockHolderList = stockHolderService.findBySaicInfoId(saicId);
			}
			result.put("stockHolder", stockHolderList);
		} catch (Exception e) {
			log.error("股东出资信息查询异常", e);
		}
		//工商信息变更
		try {
			List<ChangeRecordDto> changes = null;
			if (saicId != null) {
				changes = changeRecordService.findBySaicInfoId(saicId);
			}
			result.put("changes", changes);
		} catch (Exception e) {
			log.error("工商信息变更查询异常", e);
		}
		//经营异常信息
		try {
			List<ChangeMessDto> changeMessList = null;
			if (saicId != null) {
				changeMessList = changeMessService.findBySaicInfoId(saicId);
			}
			result.put("changeMess", changeMessList);
		} catch (Exception e) {
			log.error("经营异常信息查询异常", e);
		}
		//严重违法信息
		try {
			List<IllegalDto> illegalList = null;
			if (saicId != null) {
				illegalList = illegalService.findBySaicInfoId(saicId);
			}
			result.put("illegal", illegalList);
		} catch (Exception e) {
			log.error("严重违法信息查询异常", e);
		}
		//工商年报
		try {
			List<ReportDto> saicReportList = null;
			if (saicId != null) {
				saicReportList = reportService.findBySaicInfoId(saicId);
			}
			result.put("saicReport", saicReportList);
		} catch (Exception e) {
			log.error("工商年报查询异常", e);
		}
		return ResultDtoFactory.toApiSuccess(result);
	}

	public ResultDto queryIsKyc(String name) {
		try {
			boolean b = saicInfoService.queryIsKyc(name);
			return ResultDtoFactory.toApiSuccess(b);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询是否进行过客户尽调异常", e);
			return ResultDtoFactory.toApiError("0", "查询是否进行过客户尽调异常");
		}
	}

	private ResultDto insertSaicInfo(String key, SaicIdpInfo saicIdpInfo, UserDto userDto, String name) {
		SaicMonitorDto saicMonitorDto = null;

		if (saicByNameFlag){
			// 接口模式下，记录查询。
			try {
				CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto = new CustomerTuneSearchHistoryDto();
				customerTuneSearchHistoryDto.setCustomerName(key);
				customerTuneSearchHistoryDto.setRefId(saicIdpInfo != null ? saicIdpInfo.getId(): null);

				customerTuneSearchHistoryDto.setType(CustomerTuneSearchType.SAIC_BY_NAME);
				customerTuneSearchHistoryDto.setEntranceType(CustomerTuneSearchEntranceType.INTERFACE_SAIC_BY_NAME);
				customerTuneSearchHistoryDto.setOrganFullId(organizationService.findById(userDto.getOrgId()).getFullId());
				customerTuneSearchHistoryDto.setOrganId(userDto.getOrgId());
				customerTuneSearchHistoryDto.setCreatedBy(userDto.getId().toString());
				customerTuneSearchHistoryDto.setLastUpdateBy(userDto.getId().toString());

				customerTuneSearchHistoryService.save(customerTuneSearchHistoryDto);
			} catch (Exception e) {
				log.warn("记录工商基本信息查询日志失败", e.getMessage());
			}
		}

		if (saicIdpInfo != null) {
			log.info(name + "查询成功，接口查询工商保存工商统计表");
			saicMonitorDto = saicMonitorService.getSaicMonitor(userDto.getUsername(),userDto.getOrgId(),saicIdpInfo.getName(),saicIdpInfo.getId(),StringUtils.isNotBlank(saicIdpInfo.getUnitycreditcode()) ? saicIdpInfo.getUnitycreditcode() : saicIdpInfo.getRegistno(),SaicMonitorEnum.KYC);
			saicMonitorService.save(saicMonitorDto);

			return ResultDtoFactory.toApiSuccess(saicIdpInfo);
		} else {
			log.info(name + "查询失败，接口查询工商保存工商统计表");
			saicMonitorDto = saicMonitorService.getSaicMonitor(userDto.getUsername(),userDto.getOrgId(),key,null,"",SaicMonitorEnum.KYC);
			saicMonitorService.save(saicMonitorDto);

			return ResultDtoFactory
					.toApiError(ResultCode.NO_DATA_EXIST.code(), ResultCode.NO_DATA_EXIST.message(), null);
		}
	}

}
