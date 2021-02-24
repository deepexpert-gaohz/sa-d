package com.ideatech.ams.annual.executor;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.annual.dto.CompareFieldsDto;
import com.ideatech.ams.annual.dto.CompareRuleDto;
import com.ideatech.ams.annual.enums.*;
import com.ideatech.ams.annual.service.AnnualResultService;
import com.ideatech.ams.annual.service.AnnualTaskService;
import com.ideatech.ams.annual.service.CompareService;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.kyc.dto.ChangeMessDto;
import com.ideatech.ams.kyc.dto.ReportDto;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.pbc.enums.AmsAnnualResultStatus;
import com.ideatech.ams.system.blacklist.service.BlackListService;
import com.ideatech.ams.ws.api.service.AnnualResultApiService;
import com.ideatech.ams.ws.api.service.AnnualResultNoticeApiService;
import com.ideatech.common.enums.SaicStatusEnum;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author van
 * @date 14:55 2018/8/9
 */
@Slf4j
@Getter
@Setter
public class AnnualResultTaskExecutor implements Runnable {

	private List<AnnualResultDto> resultDtoList;

	private Boolean needSubmit;

	/**
	 * 工商营业执照到期是否影响主逻辑
	 */
	private Boolean judgeBusinessLicenseExpired;

	private BlackListService blackListService;

	private AnnualResultService annualResultService;

	private CompareService compareService;

	private PbcAmsService pbcAmsService;

	private AnnualTaskService annualTaskService;

	private PlatformTransactionManager transactionManager;

	/**
	 * 比对字段是否使用
	 */
	private List<CompareFieldsDto> compareFieldsDtoList;

	/**
	 * 详细比对规则
	 */
	private Map<String, Map<String, CompareRuleDto>> compareRuleDtoMap;


	private AnnualResultApiService annualResultApiService;

	private AccountsAllService accountsAllService;

	private CustomerPublicLogService customerPublicLogService;

	private AnnualResultNoticeApiService annualResultNoticeApiService;

	public AnnualResultTaskExecutor(List<AnnualResultDto> resultDtoList) {
		this.resultDtoList = resultDtoList;
	}

	@Override
	public void run() {

		if (CollectionUtils.isNotEmpty(resultDtoList)) {
			//年检整体处理逻辑
			for (AnnualResultDto annualResultDto : resultDtoList) {
				if (annualResultDto != null) {
					log.info("-----开始年检账号{}-----", annualResultDto.getAcctNo());
					try {
						annualTaskService.annualResultComparison(annualResultDto,
								judgeBusinessLicenseExpired,
								compareFieldsDtoList,
								compareRuleDtoMap);
					}catch (Exception e){
						log.error("-----账号{}年检处理异常-----",annualResultDto.getAcctNo(),e);
						annualResultDto.setResult(ResultStatusEnum.FAIL);
						annualResultService.save(annualResultDto);
					}
					log.info("-----账号{}年检处理结束-----", annualResultDto.getAcctNo());
				}
			}
		}
	}

}
