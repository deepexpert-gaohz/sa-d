package com.ideatech.ams.annual.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.core.Map2DomainService;
import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.annual.dao.AnnualStatisticsDao;
import com.ideatech.ams.annual.dao.AnnualTaskDao;
import com.ideatech.ams.annual.dto.*;
import com.ideatech.ams.annual.dto.poi.AnnualStatisticsPoi;
import com.ideatech.ams.annual.entity.AnnualStatistics;
import com.ideatech.ams.annual.entity.AnnualTask;
import com.ideatech.ams.annual.enums.*;
import com.ideatech.ams.annual.executor.AnnualAgainTaskExecutor;
import com.ideatech.ams.annual.executor.AnnualResultTaskExecutor;
import com.ideatech.ams.annual.executor.CollectControllerExecutor;
import com.ideatech.ams.annual.poi.AnnualStatisticsExport;
import com.ideatech.ams.annual.service.export.AnnualResultExportService;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.kyc.dto.ChangeMessDto;
import com.ideatech.ams.kyc.dto.ReportDto;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.AmsAnnualResultStatus;
import com.ideatech.ams.pbc.enums.LoginStatus;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.system.blacklist.service.BlackListService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.dto.PbcIPAddressDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.pbc.service.PbcIPAddressService;
import com.ideatech.ams.ws.api.service.AnnualResultApiService;
import com.ideatech.ams.ws.api.service.AnnualResultNoticeApiService;
import com.ideatech.common.constant.IdeaConstant;
import com.ideatech.common.dto.TreeTable;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.enums.SaicStatusEnum;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.ideatech.ams.kyc.util.SaicUtils.isIllegal;

/**
 *
 *
 * @author van
 * @date 19:43 2018/8/8
 */
@Service
@Transactional
@Slf4j
public class AnnualTaskServiceImpl extends BaseServiceImpl<AnnualTaskDao, AnnualTask, AnnualTaskDto> implements AnnualTaskService {

	@Autowired
	private AnnualResultService annualResultService;

	@Autowired
	private ThreadPoolTaskExecutor annualExecutor;

	@Autowired
	private BlackListService blackListService;

	@Autowired
	private CompareService compareService;

	@Autowired
	private CompareFieldsService compareFieldsService;

	@Autowired
	private CompareRuleService compareRuleService;

	@Autowired
	private PbcAmsService pbcAmsService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private AnnualStatisticsDao annualStatisticsDao;

	@Autowired
	private AnnualTaskService annualTaskService;

	@Autowired
	private AnnualStatisticsService annualStatisticsService;

	@Autowired
    private PlatformTransactionManager transactionManager;

	@Autowired
    private AnnualResultExportService annualResultExportService;

	@Autowired
	private Map2DomainService map2DomainService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private AnnualResultApiService annualResultApiService;

	@Autowired
	private AccountsAllService accountsAllService;

	@Autowired
	private CustomerPublicLogService customerPublicLogService;

	@Autowired
	private AnnualResultNoticeApiService annualResultNoticeApiService;

	@Autowired
	private CollectConfigService collectConfigService;

	private List<Future<Long>> futureList;

	@PersistenceContext
	private EntityManager entityManager;

//	@Value("${ams.annual.submit:false}")
//	private Boolean needSubmit;

	@Value("${ams.annual.business-license-expired:false}")
	private Boolean businessLicenseExpired;


	@Autowired
	private SaicCollectionService saicCollectionService;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Autowired
	protected TransactionUtils transactionUtils;

	@Autowired
	private PbcIPAddressService pbcIPAddressService;

	@Autowired
	private PbcAccountService pbcAccountService;

	//工商定时采集线程
	private ScheduledFuture<?> future;

	@Autowired
	private AmsMainService amsMainService;

    @Override
    public Long getAnnualCompareTaskId() {
		Integer year = Calendar.getInstance().get(Calendar.YEAR);
		TaskTypeEnum type = TaskTypeEnum.ANNUAL;
		AnnualTaskDto dto = new AnnualTaskDto();

		List<AnnualTask> taskList = getBaseDao().findByYearAndType(year, type);
		if (taskList != null && taskList.size() > 0) {
			BeanUtils.copyProperties(taskList.get(0), dto);
			return dto.getId();
		}

		return null;
	}

    @Override
    public TreeTable getStatisticsInfo(Long pid, Long organId, Long taskId) {
		TreeTable result = new TreeTable();
        List<OrganizationDto> organChilds = null;

        if (pid == null) {
            OrganizationDto parent = organizationService.findById(organId);

            Map<String, Object> row = buildRow(taskId, parent);

            List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
            organChilds = organizationService.searchChild(organId, "");

            for (OrganizationDto child : organChilds) {
                rows.add(buildRow(taskId, child));
            }
            row.put("rows", rows);

            result.getRows().add(row);

        } else {
            result.setPid(pid.toString());
            organChilds = organizationService.searchChild(pid, "");

            for (OrganizationDto childOrgan : organChilds) {
                result.getRows().add(buildRow(taskId, childOrgan));
            }
        }

        return result;
    }

	@Override
	public IExcelExport exportXLS(Long organId, Long taskId) {
		List<Map<String, Object>> rows = new ArrayList<>();
		IExcelExport annualStatisticsExport = new AnnualStatisticsExport();
		List<AnnualStatisticsPoi> statisticsPoiList = new ArrayList<>();

		rows = getStatisticsInfos(organId, taskId);

		if(CollectionUtils.isNotEmpty(rows)) {
			for(Map<String, Object> map : rows) {
				AnnualStatisticsPoi statisticsPoi = new AnnualStatisticsPoi();
				statisticsPoi.setName(String.valueOf(map.get("name")));
				if(map.get("annualTotalCount") != null) {
					statisticsPoi.setAnnualTotalCount(Integer.parseInt(map.get("annualTotalCount").toString()));
				}
				if(map.get("annualPassCount") != null) {
					statisticsPoi.setAnnualPassCount(Integer.parseInt(map.get("annualPassCount").toString()));
				}
				statisticsPoi.setAnnualPassRate(String.valueOf(map.get("annualPassRate")));

				statisticsPoiList.add(statisticsPoi);
			}

		}

		annualStatisticsExport.setPoiList(statisticsPoiList);
		return annualStatisticsExport;
	}

	/**
	 * 全部重新年检
	 * @param ardList 重新年检集合
	 * @param taskId 年检任务id
	 */
	@Override
	public void annualAgainAll(List<AnnualResultDto> ardList, final Long taskId) {

		//清除线程
		clearFuture();
		//按机构多线程处理，避免人行提交异常
		Map<String, List<AnnualResultDto>> annualResultDtoMap = new HashMap<>();
		for (AnnualResultDto ard : ardList) {
			if (ard.getOrganFullId() != null) {
				if (annualResultDtoMap.containsKey(ard.getOrganFullId())) {
					annualResultDtoMap.get(ard.getOrganFullId()).add(ard);
				} else {
					List<AnnualResultDto> ardOrganList = new ArrayList<>();
					ardOrganList.add(ard);
					annualResultDtoMap.put(ard.getOrganFullId(), ardOrganList);
				}
			}

		}

		//修改年检任务表中年检状态为“重新年检进行中”
		annualTaskService.updateStatus(taskId, TaskStatusEnum.AGAIN_PROCESSING);

		for (String organFullId : annualResultDtoMap.keySet()) {
			OrganizationDto organ = organizationService.findByOrganFullId(organFullId);
			AnnualAgainTaskExecutor annualAgainTaskExecutor = new AnnualAgainTaskExecutor(annualResultService, annualResultDtoMap.get(organFullId));
			annualAgainTaskExecutor.setOrganName(organ.getName());
			futureList.add(annualExecutor.submit(annualAgainTaskExecutor));
		}
		new Thread() {//新建一个线程检测 重新年检线程是否结束
			public void run() {
				try {
					waitFutureOver();
					log.info("全部重新年检线程执行结束");
					//修改年检任务表中年检状态为“重新年检完成”
					annualTaskService.updateStatus(taskId, TaskStatusEnum.AGAIN_FINISH);
					//更新年检统计数据
					annualStatisticsService.saveStatistics(taskId);
					//更新年检任务表中的年检通过数据（由年检任务页面js循环获取年检状态时，自动更新，这里不做处理）
				} catch (Exception e) {
					e.printStackTrace();
					log.info("检测重新年检任务是否完成出现异常");
				}
			}
		}.start();
	}

	/**
	 * 等待重新年检完成
	 *
	 * @throws Exception
	 */
	private void waitFutureOver() throws Exception {
		while (futureList.size() > 0) {
			for (Iterator<Future<Long>> iterator = futureList.iterator(); iterator.hasNext(); ) {
				Future<Long> future = iterator.next();
				if (future.isDone()) {
					iterator.remove();
				}
			}
			// 暂停1分钟
//			System.out.println("等待开始");
//			TimeUnit.SECONDS.sleep(3);//3秒
			TimeUnit.MINUTES.sleep(1);
//			System.out.println("等待结束");
		}
	}

	/**
	 * 清除重新年检线程
	 */
	@Override
	public void clearFuture() {
		if (futureList != null && futureList.size() > 0) {
			for (Iterator<Future<Long>> iterator = futureList.iterator(); iterator.hasNext(); ) {
				Future<Long> future = iterator.next();
				if (future.isDone()) {
					iterator.remove();
				} else {
					future.cancel(true);
					iterator.remove();
				}
			}
		}
		futureList = new ArrayList<Future<Long>>();
	}

    @Override
    public List<Map<String, Object>> getStatisticsInfos(Long organId, Long taskId) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        String annualTotalCount = "";
        String annualPassCount = "";
        String annualPassRate = "";

        List<OrganizationDto> organizationList = organizationService.listDescendant(organId);

        if(organizationList != null && organizationList.size() > 0) {
            for (OrganizationDto organization : organizationList) {
                map = new HashMap<>();

                map.put("id", organization.getId());
                map.put("parentId", organization.getParentId());
                map.put("name", organization.getName());

				AnnualStatistics statistics = null;
				try {
					statistics = annualStatisticsDao.findByTaskIdAndOrganId(taskId, organization.getId());
				} catch (Exception e) {
					log.error("更新年检统计结果时，机构id为{}的统计数据存在多条", organization.getId(), e);
				}

                if (statistics != null && statistics.getCount() != 0L) {
                    annualTotalCount = String.valueOf(statistics.getCount());
                    annualPassCount = String.valueOf(statistics.getSuccess());
                    BigDecimal rate = new BigDecimal(statistics.getSuccess()).divide(new BigDecimal(statistics.getCount()), 2, RoundingMode.HALF_UP);
                    annualPassRate = rate.multiply(new BigDecimal(100)).toString() + "%";
                } else {
                    annualTotalCount = "0";
                    annualPassCount = "0";
                    annualPassRate = "0%";
                }

                map.put("annualTotalCount", annualTotalCount);
                map.put("annualPassCount", annualPassCount);
                map.put("annualPassRate", annualPassRate);

                list.add(map);

            }
        }

        return list;
    }

    @Override
    public Long initAnnualTask() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Integer year = calendar.get(Calendar.YEAR);
        List<AnnualTask> annualTaskList = getBaseDao().findByYearAndType(year, TaskTypeEnum.ANNUAL);
        if(annualTaskList.size()>0){
            return annualTaskList.get(0).getId();
        }else{
            AnnualTask annualTask = new AnnualTask();
            annualTask.setStatus(TaskStatusEnum.INIT);
            annualTask.setYear(year);
            annualTask.setType(TaskTypeEnum.ANNUAL);
            annualTask.setPassedNum(0L);
            annualTask.setProcessedNum(0L);
            annualTask.setSum(0L);
            getBaseDao().save(annualTask);
            if(configService.findByKey(IdeaConstant.ANNUAL_LOOP_CONFIG).size()==0){
				ConfigDto configDto = new ConfigDto();
				configDto.setConfigKey(IdeaConstant.ANNUAL_LOOP_CONFIG);
				configDto.setConfigValue("0");
				configService.save(configDto);
			}
			//自动创建默认年检字段规则
			compareFieldsService.saveDefaultCompareFieldsRules(annualTask.getId());
            return annualTask.getId();
        }
    }

	@Override
	public void updateNum(Long taskId, Boolean annualResult) {
		AnnualTask annualTask = getBaseDao().findById(taskId);
		if (annualTask.getSum() == null || annualTask.getSum() == 0) {
			annualTask.setSum(annualResultService.countAnnualResltByTaskId(taskId));
		}
		annualTask.setProcessedNum(annualTask.getProcessedNum() + 1);
		if (annualResult) {
			annualTask.setPassedNum(annualTask.getPassedNum() + 1);
		}
		getBaseDao().save(annualTask);
	}

	@Override
	public void updateStatus(Long taskId, TaskStatusEnum taskStatusEnum) {
		AnnualTask annualTask = getBaseDao().findById(taskId);
		annualTask.setStatus(taskStatusEnum);
		getBaseDao().save(annualTask);
	}

	@Override
	public void reset(Long taskId) {
		annualResultService.cleanAnnualResultByTaskId(taskId);
		AnnualTask annualTask = getBaseDao().findById(taskId);
		annualTask.setSum(0L);
		annualTask.setPassedNum(0L);
		annualTask.setProcessedNum(0L);
		annualTask.setStatus(TaskStatusEnum.INIT);
		getBaseDao().save(annualTask);
		List<ConfigDto> configDtos = configService.findByKey(IdeaConstant.ANNUAL_LOOP_CONFIG);
		if(configDtos.size()>0){
			ConfigDto configDto = configDtos.get(0);
			if("1".equals(configDto.getConfigValue())){
				configDto.setConfigValue("0");
				configService.save(configDto);
			}
		}
	}

	@Override
	public void updateNumByTask(Long taskId) {
		Long[] nums = annualResultService.countProcessedAndPassedNum(taskId);
		if (nums != null && nums.length == 3) {
			AnnualTask annualTask = getBaseDao().findById(taskId);
			annualTask.setSum(nums[0]);
			annualTask.setProcessedNum(nums[1]);
			annualTask.setPassedNum(nums[2]);
			//如果处理完了直接完成
			if ((nums[0].equals(0l) || nums[0].equals(nums[1])) && annualTask.getStatus() == TaskStatusEnum.PROCESSING) {
				annualTask.setStatus(TaskStatusEnum.FINISH);
			}
			getBaseDao().save(annualTask);
		}
	}

	@Override
	public void updateLoopNumByTask(Long taskId){
		Long[] nums = annualResultService.countForceStatusAndPassedNum(taskId);
		if (nums != null && nums.length == 2) {
			AnnualTask annualTask = getBaseDao().findById(taskId);
			annualTask.setPassedNum(nums[0]+nums[1]);
			getBaseDao().save(annualTask);
		}
	}

	private Map<String,Object> buildRow(Long taskId, OrganizationDto organ) {
        Map<String, Object> row = new HashMap<String, Object>();
        row.put("id", organ.getId().toString());

        List<OrganizationDto> organChilds = organizationService.searchChild(organ.getId(), "");

        if (CollectionUtils.isNotEmpty(organChilds)) {
            row.put("hasRows", "1");
			row.put("name", organ.getName());
        } else {
			row.put("name", organ.getName());
        }

		AnnualStatistics statistics = null;
		try {
			statistics = annualStatisticsDao.findByTaskIdAndOrganId(taskId, organ.getId());
		} catch (Exception e) {
			log.error("更新年检统计结果时，机构id为{}的统计数据存在多条", organ.getId(), e);
		}

        if (statistics != null && statistics.getCount() != 0L) {
			row.put("annualTotalCount", statistics.getCount());
			row.put("annualPassCount", statistics.getSuccess());
            BigDecimal rate = new BigDecimal(statistics.getSuccess()).divide(new BigDecimal(statistics.getCount()), 2, RoundingMode.HALF_UP);
			row.put("annualPassRate", rate.multiply(new BigDecimal(100)).toString() + "%");
        } else {
			row.put("annualTotalCount", "0");
			row.put("annualPassCount", "0");
			row.put("annualPassRate", "0%");
        }

        return row;
    }

	@Override
	public void start(Long taskId) {
		//如果当前任务状态是完成，则为重新比对
		boolean reStart = getBaseDao().findById(taskId).getStatus() == TaskStatusEnum.FINISH;
		if (reStart) {
			log.info("开始重置未完成年检数据");
			resetUnSuccess(taskId);
		}
		Boolean isAnnualSubmit = false;
		//从结果表中取出所有该任务的机构
		List<String> organFullIds = annualResultService.listOrgansByTaskId(taskId);
		updateStatus(taskId, TaskStatusEnum.PROCESSING);
		//获取比对规则和比对字段
		List<CompareFieldsDto> compareFieldsDtoList = compareFieldsService.listCompareRulesByTakId(taskId);
		if (CollectionUtils.isEmpty(compareFieldsDtoList)) {
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "该任务未设置比对字段");
		}

		Map<String, Map<String, CompareRuleDto>> compareRuleDtoMap = this.getaAssemblyCompareRuleMap(taskId);
		//按机构多线程处理，避免人行提交异常
		for (String organFullId : organFullIds) {
			List<AnnualResultDto> results = annualResultService.listAnnualsByOrgan(taskId, organFullId);
			log.info("年检线程，机构{}中，有{}条待年检数据", organFullId, results.size());
			AnnualResultTaskExecutor resultTask = new AnnualResultTaskExecutor(results);
			resultTask.setBlackListService(blackListService);
			resultTask.setJudgeBusinessLicenseExpired(businessLicenseExpired);
			//机构管理页面按照机构配置年检是否提交
			isAnnualSubmit = getIsAnnualSubmit(organFullId);
			resultTask.setNeedSubmit(isAnnualSubmit);

			resultTask.setAnnualResultService(annualResultService);
			resultTask.setCompareService(compareService);
			resultTask.setCompareFieldsDtoList(compareFieldsDtoList);
			resultTask.setCompareRuleDtoMap(compareRuleDtoMap);
			resultTask.setPbcAmsService(pbcAmsService);
			resultTask.setAnnualTaskService(annualTaskService);
			resultTask.setTransactionManager(transactionManager);
			resultTask.setAnnualResultApiService(annualResultApiService);
			resultTask.setAccountsAllService(accountsAllService);
			resultTask.setCustomerPublicLogService(customerPublicLogService);
			resultTask.setAnnualResultNoticeApiService(annualResultNoticeApiService);
			annualExecutor.execute(resultTask);
		}

	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void compareCompleted(final Long taskId){
		while (annualExecutor.getActiveCount() != 0) {
			try {
				// 暂停30s
				Thread.sleep(1000 * 30 * 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try{
			transactionUtils.executeInNewTransaction(new TransactionCallback() {
				@Override
				public void execute() throws Exception {
					//任务状态结束
					annualTaskService.updateStatus(taskId, TaskStatusEnum.FINISH);
				}
			});
			//统计
			log.info("开始统计结果");
			annualStatisticsService.saveStatistics(taskId);
		}catch (Exception e){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			log.error("统计结果异常");
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"统计结异常");
		}
	}

	private Map assemblyCompareRuleMap(List<CompareRuleDto> compareRuleDtoList) {
    	if(CollectionUtils.isEmpty(compareRuleDtoList)){
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "该任务未设置比对规则");
		}
		//处理比对规则为map形式
		Map<String, Map<String, CompareRuleDto>> compareRuleDtoMap = new HashMap<>(3);
		Map<String, CompareRuleDto> map1 = new HashMap<>(9);
		Map<String, CompareRuleDto> map2 = new HashMap<>(9);
		Map<String, CompareRuleDto> map3 = new HashMap<>(9);
		for (CompareRuleDto compareRuleDto : compareRuleDtoList) {
			if (compareRuleDto.getDataSourceEnum() == DataSourceEnum.CORE) {
				map1.put(compareRuleDto.getCompareFieldEnum().getField(), compareRuleDto);
			} else if (compareRuleDto.getDataSourceEnum() == DataSourceEnum.PBC) {
				map2.put(compareRuleDto.getCompareFieldEnum().getField(), compareRuleDto);
			} else if (compareRuleDto.getDataSourceEnum() == DataSourceEnum.SAIC) {
				map3.put(compareRuleDto.getCompareFieldEnum().getField(), compareRuleDto);
			}
		}
		compareRuleDtoMap.put(DataSourceEnum.CORE.name(), map1);
		compareRuleDtoMap.put(DataSourceEnum.PBC.name(), map2);
		compareRuleDtoMap.put(DataSourceEnum.SAIC.name(), map3);
		return compareRuleDtoMap;
	}



	/**
	 * 获取历史年检数据
	 */
	@Override
	public JSONArray getAnnualHistory() {
		JSONArray jsonArray = new JSONArray();
		List maxRows = this.findMaxCreatedDateGroupByYear();
		List minRows = this.findMinCreatedDateGroupByYear();
		for (Object obj : maxRows) {
			Map row = (Map) obj;
			if (Integer.valueOf(row.get("YD_YEAR").toString())
					.equals(Calendar.getInstance().get(Calendar.YEAR))) {//剔除当前年份年检数据
				continue;
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", row.get("YD_ID"));
			jsonObject.put("year", row.get("YD_YEAR"));
			jsonObject.put("passedNum", row.get("YD_PASSED_NUM"));
			jsonObject.put("sum", row.get("YD_SUM"));
			//设置开始时间为那一年第一次年检的时间
			for (Object obj2 : minRows) {
				Map row2 = (Map) obj2;
				if (row.get("YD_YEAR").equals(row2.get("YD_YEAR"))) {
					jsonObject.put("createdDate", row2.get("YD_CREATED_DATE"));
					break;
				}
			}
			//计算通过率
			jsonObject.put("passingRate", 0d);
			if (row.get("YD_PASSED_NUM") != null && row.get("YD_SUM") != null && Integer.valueOf(row.get("YD_SUM").toString()) != 0) {
				double passingRate = Double.valueOf(row.get("YD_PASSED_NUM").toString()) / Double.valueOf(row.get("YD_SUM").toString()) * 100;
				jsonObject.put("passingRate", passingRate);
			}
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	/**
	 * 获取比对规则
	 * @param taskId
	 * @return
	 */
	@Override
	public Map<String, Map<String, CompareRuleDto>> getaAssemblyCompareRuleMap(Long taskId) {
		Map<String, Map<String, CompareRuleDto>> compareRuleDtoMap = assemblyCompareRuleMap(compareRuleService.listCompareRulesByTakId(taskId));
		return compareRuleDtoMap;
	}

	/**
	 * 获取每一年第一次年检的时间
	 *
	 * @return
	 */
	private List findMinCreatedDateGroupByYear() {
//		Query query = entityManager.createNativeQuery("SELECT\n" +
//				"\ta.yd_id,\n" +
//				"\tb.yd_year,\n" +
//				"\tb.yd_created_date \n" +
//				"FROM\n" +
//				"\tyd_annual_task a,\n" +
//				"\t( SELECT yd_year, min( yd_created_date ) AS yd_created_date FROM yd_annual_task GROUP BY yd_year ) b \n" +
//				"WHERE\n" +
//				"\ta.yd_year = b.yd_year \n" +
//				"\tAND a.yd_created_date = b.yd_created_date ORDER BY yd_year DESC");
		Query query = entityManager.createNativeQuery("SELECT yd_year AS YD_YEAR, min( yd_created_date ) AS YD_CREATED_DATE FROM yd_annual_task GROUP BY yd_year");
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List rows = query.getResultList();
		return rows;
	}

	/**
	 * 获取每一年最后一次年检的任务id
	 *
	 * @return
	 */
	private List findMaxCreatedDateGroupByYear() {
		Query query = entityManager.createNativeQuery("SELECT\n" +
				"\ta.yd_id AS YD_ID,\n" +
				"\ta.yd_passed_num AS YD_PASSED_NUM,\n" +
				"\ta.yd_sum AS YD_SUM,\n" +
				"\tb.yd_year AS YD_YEAR\n" +
				"FROM\n" +
				"\tyd_annual_task a,\n" +
				"\t( SELECT yd_year, max( yd_created_date ) AS yd_created_date FROM yd_annual_task GROUP BY yd_year ) b \n" +
				"WHERE\n" +
				"\ta.yd_year = b.yd_year \n" +
				"\tAND a.yd_created_date = b.yd_created_date ORDER BY yd_year DESC");
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List rows = query.getResultList();
		return rows;
	}


    /**
     * 年检比对
     * annualResultDto 采集结果数据
     * judgeBusinessLicenseExpired 工商营业执照到期是否影响主逻辑
     * compareFieldsDtoList 比对字段
     * compareRuleDtoMap 详细比对规则
     */
    @Override
    public void annualResultComparison(AnnualResultDto annualResultDto,
                                        Boolean judgeBusinessLicenseExpired,
                                        List<CompareFieldsDto> compareFieldsDtoList,
                                        Map<String, Map<String, CompareRuleDto>> compareRuleDtoMap) {
        //1.单边处理
        isUnilateral(annualResultDto);
        //2.工商数据处理
        processSaicData(annualResultDto, judgeBusinessLicenseExpired);
        //3.比对
        compareData(annualResultDto, compareFieldsDtoList, compareRuleDtoMap);
        //4.黑名单
        isBlack(annualResultDto);
        //5.根据前面步骤判断结果
        setResultStatus(annualResultDto);
        //6.提交人行
        if (annualResultDto.getPbcSubmitStatus() == PbcSubmitStatusEnum.WAIT_SUBMIT) {
            submitPbc(annualResultDto);
        }
        //7.保存
        updateAnnualResult(annualResultDto);
        //8.更新条目以及状态
//        updateTask(annualResultDto);
    }

    /**
     * 单边数据
     * @param annualResultDto
     */
    private void isUnilateral(AnnualResultDto annualResultDto) {
        String coreData = annualResultDto.getCoreData();
        String pbcData = annualResultDto.getPbcData();
        annualResultDto.setUnilateral(UnilateralTypeEnum.NONE);
        if (StringUtils.equals(coreData, pbcData)) {
            annualResultDto.setUnilateral(UnilateralTypeEnum.NONE);
        } else {
            if (StringUtils.isBlank(coreData)) {
                annualResultDto.setUnilateral(UnilateralTypeEnum.PBC);
            }
            if (StringUtils.isBlank(pbcData)) {
                annualResultDto.setUnilateral(UnilateralTypeEnum.CORE);
            }
        }
		/*//直接不一致
		if (annualResultDto.getUnilateral() != UnilateralTypeEnum.NONE) {
			log.info(annualResultDto.getAcctNo()+"数据为{}", annualResultDto.getUnilateral().getName());
			annualResultDto.setMatch(false);
		}*/
    }

    /**
     * 处理工商数据
     * @param annualResultDto
     */
    private void processSaicData(AnnualResultDto annualResultDto, Boolean judgeBusinessLicenseExpired) {
        String saicData = annualResultDto.getSaicData();
        StringBuilder abnormalSb = new StringBuilder(annualResultDto.getAbnormal() == null ? "" : annualResultDto.getAbnormal());
        //1.是否存在
        if (StringUtils.isBlank(saicData)) {
            appendAbnormal(abnormalSb, AbnormalStatusEnum.NOT_FOUND);
            log.info("账号{}工商未找到", annualResultDto.getAcctNo());
            annualResultDto.setSaicStatus(SaicStatusEnum.NOTFOUND);
			/*//直接不一致
			annualResultDto.setMatch(false);*/
        } else {
            //2.工商状态是否正常
            SaicIdpInfo saicIdpInfo = JSON.parseObject(saicData, SaicIdpInfo.class);
            SaicStatusEnum saicStatusEnum = SaicStatusEnum.saicState2Enum(saicIdpInfo.getState());
            log.info("账号{}工商状态{}", annualResultDto.getAcctNo(), saicStatusEnum.getName());
            annualResultDto.setSaicStatus(saicStatusEnum);
            if (saicStatusEnum == SaicStatusEnum.REVOKE) {
                appendAbnormal(abnormalSb, AbnormalStatusEnum.REVOKE);
            } else if (saicStatusEnum == SaicStatusEnum.CANCEL) {
                appendAbnormal(abnormalSb, AbnormalStatusEnum.CANCEL);
            }
            //3.营业执照到日期是否到期
            if (judgeBusinessLicenseExpired && isBusinessLicenseExpired(saicIdpInfo.getEnddate())) {
                appendAbnormal(abnormalSb, AbnormalStatusEnum.BUSINESS_LICENSE_EXPIRED);
                log.info("账号{}营业执照到日期到期", annualResultDto.getAcctNo());
            }
            //4.经营异常
            if (isAbnormalOperation(saicIdpInfo.getChangemess())) {
                appendAbnormal(abnormalSb, AbnormalStatusEnum.ABNORMAL_OPERATION);
                log.info("账号{}数据经营异常", annualResultDto.getAcctNo());
            }
            //5.年报
            if (isNoAnnualReport(saicIdpInfo.getReports())) {
                appendAbnormal(abnormalSb, AbnormalStatusEnum.NO_ANNUAL_REPORT);
                log.info("账号{}工商无年报", annualResultDto.getAcctNo());
            }
            //6.严重违法
			if (isIllegal(saicIdpInfo.getIllegals())){
				appendAbnormal(abnormalSb,AbnormalStatusEnum.ILLEGAL);
				log.info("账号{}数据严重违法", annualResultDto.getAcctNo());
			}
        }
        annualResultDto.setAbnormal(abnormalSb.toString());
    }

    /**
     * 比对
     * @param annualResultDto
     */
    private void compareData(AnnualResultDto annualResultDto, List<CompareFieldsDto> compareFieldsDtoList, Map<String, Map<String, CompareRuleDto>> compareRuleDtoMap) {
        //只比对未必对的数据
		//重新比对时也进行字段比对
//        if (annualResultDto.getMatch() == null) {
            boolean match = compareService.compare(annualResultDto ,compareFieldsDtoList, compareRuleDtoMap);
            annualResultDto.setMatch(match);
            log.info("账号{}比对结果{}", annualResultDto.getAcctNo(), match ? "一致" : "不一致");
//        }
    }


    /**
     * 是否黑名单
     * @param annualResultDto
     */
    private void isBlack(AnnualResultDto annualResultDto) {
        StringBuilder abnormalSb = new StringBuilder(annualResultDto.getAbnormal() == null ? "" : annualResultDto.getAbnormal());
        Boolean black = CollectionUtils.isNotEmpty(blackListService.findByName(annualResultDto.getDepositorName()));
        annualResultDto.setBlack(black);
        if (black) {
            appendAbnormal(abnormalSb, AbnormalStatusEnum.BLACK);
            log.info("账号{}为黑名单数据", annualResultDto.getAcctNo());
        }
        annualResultDto.setAbnormal(abnormalSb.toString());
    }


    /**
     * 根据
     * 1.单边
     * 2.比对
     * 3.异常状态
     * 判断最终结果
     * 设置状态值
     * @param annualResultDto
     */
    private void setResultStatus(AnnualResultDto annualResultDto) {
        Boolean pass = annualResultDto.getUnilateral() == UnilateralTypeEnum.NONE && annualResultDto.getMatch() && StringUtils.isBlank(annualResultDto.getAbnormal());
		log.info("账号{}年检最终结果为{}", annualResultDto.getAcctNo(),pass);

		Boolean	needSubmit = getIsAnnualSubmit(annualResultDto.getOrganFullId());

        if (!needSubmit) {
            annualResultDto.setPbcSubmitStatus(PbcSubmitStatusEnum.NO_NEED_SUBMIT);
            log.info("提交人行开关关闭，提交人行状态为[无需上报]");
        }
        if (pass) {
            annualResultDto.setResult(ResultStatusEnum.PASS);
            if (needSubmit) {
                annualResultDto.setPbcSubmitStatus(PbcSubmitStatusEnum.WAIT_SUBMIT);
            }else{
                annualResultDto.setPbcSubmitter("系统自动");
            }
            log.info("账号{}年检通过", annualResultDto.getAcctNo());
        } else {
            annualResultDto.setResult(ResultStatusEnum.FAIL);
            log.info("账号{}年检未通过", annualResultDto.getAcctNo());
        }
    }

    /**
     * 提交人行
     * @param annualResultDto
     */
    private void submitPbc(AnnualResultDto annualResultDto) {
        try {
            AmsAnnualResultStatus amsAnnualResultStatus = pbcAmsService.sumitAnnualAccount(annualResultDto.getOrganFullId(), annualResultDto.getAcctNo());
            //人行已经年检的数据认为成功
            if (amsAnnualResultStatus == AmsAnnualResultStatus.Success || amsAnnualResultStatus == AmsAnnualResultStatus.AlreadyAnnual|| amsAnnualResultStatus == AmsAnnualResultStatus.NewAccount) {
                annualResultDto.setPbcSubmitStatus(PbcSubmitStatusEnum.SUCCESS);
                annualResultDto.setPbcSubmitter("系统自动");
                annualResultApiService.submitPbcFinished(annualResultDto.getAcctNo());
                log.info("账户{}提交人行成功", annualResultDto.getAcctNo());
            } else {
                annualResultDto.setPbcSubmitStatus(PbcSubmitStatusEnum.FAIL);
                annualResultDto.setPbcSubmitErrorMsg(amsAnnualResultStatus.getFullName());
                log.info("账户{}提交人行失败", annualResultDto.getAcctNo());
            }
        } catch (Exception e) {
            annualResultDto.setPbcSubmitStatus(PbcSubmitStatusEnum.FAIL);
            log.info("账户" + annualResultDto.getAcctNo() + "提交人行异常", e);
            if (e instanceof BizServiceException) {
                annualResultDto.setPbcSubmitErrorMsg(e.getMessage());
            } else {
                annualResultDto.setPbcSubmitErrorMsg("其他未知异常");
            }
        }
    }

    /**
     * 是否证件到期
     * @param businessLicenseDateStr
     */
    private Boolean isBusinessLicenseExpired(String businessLicenseDateStr) {
        Date date = new Date();
        if (StringUtils.isNotBlank(businessLicenseDateStr)) {
            try {
                Date businessLicenseDate = DateUtils.parseDate(businessLicenseDateStr, new String[]{"yyyy年MM月dd日"});
                return !DateUtils.isSameDay(businessLicenseDate, date) && DateUtil.isBefore(businessLicenseDate, date);
            } catch (Exception e) {
                //不做处理
            }
        }
        return null;
    }

    /**
     * 是否经营异常
     * 有一条未移除则异常
     * @param changeMessDtoList
     * @return
     */
    private Boolean isAbnormalOperation(List<ChangeMessDto> changeMessDtoList) {
        if (CollectionUtils.isNotEmpty(changeMessDtoList)) {
            for (ChangeMessDto changeMessDto : changeMessDtoList) {
                if (StringUtils.isBlank(changeMessDto.getOutdate())) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 是否无年报
     * @param reportDtoList
     * @return
     */
    private Boolean isNoAnnualReport(List<ReportDto> reportDtoList) {
        if (CollectionUtils.isNotEmpty(reportDtoList)) {
            //TODO 目前只判断是否有年报，不判断是否有今年年报
			/*for (ReportDto reportDto : reportDtoList) {

			}*/
            return false;
        } else {
            return true;
        }
    }

    /**
     * 添加异常类型
     * @param abnormalSb
     * @param statusEnum
     */
    private void appendAbnormal(StringBuilder abnormalSb, AbnormalStatusEnum statusEnum) {
        abnormalSb.append(statusEnum).append(AbnormalStatusEnum.ABNORMAL_SEPARATOR);
    }


    private void updateAnnualResult(AnnualResultDto annualResultDto) {
        List<PbcSubmitStatusEnum> strs = new ArrayList<>();
        strs.add(PbcSubmitStatusEnum.SUCCESS);
        strs.add(PbcSubmitStatusEnum.NO_NEED_SUBMIT);
        strs.add(PbcSubmitStatusEnum.WAIT_SUBMIT);

        //保存之前去掉异常情况最后的逗号
        String abnormal = annualResultDto.getAbnormal();

        if (StringUtils.endsWith(abnormal, AbnormalStatusEnum.ABNORMAL_SEPARATOR)) {
            abnormal = StringUtils.removeEnd(abnormal, AbnormalStatusEnum.ABNORMAL_SEPARATOR);
        }
        annualResultDto.setAbnormal(abnormal);
        annualResultService.updateAnnualResult(annualResultDto);

        //项目现场开放年检结果通知接口
        annualResultNotice(annualResultDto);

        //项目现场开放年检结果短信接口(年检失败或年检待处理)
        annualResultMessageNotice(annualResultDto, strs);

    }

	//@Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void annualResultNotice(AnnualResultDto annualResultDto) {
        String acctNo = annualResultDto.getAcctNo();
        String legalTelephone = getLegalTelephone(acctNo);

        synchronized(AnnualResultTaskExecutor.class) {
            //TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            //TransactionStatus transaction = transactionManager.getTransaction(definition);
            try {
                annualResultNoticeApiService.annualResultNotice(acctNo, annualResultDto.getDepositorName(),
                        annualResultDto.getCreatedDate(), annualResultDto.getResult().getName(), legalTelephone);
            } catch (Exception e) {
                log.error("年检结果通知接口出错", e);
            }

            //transactionManager.commit(transaction);
        }
    }

	//@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void annualResultMessageNotice(AnnualResultDto annualResultDto, List<PbcSubmitStatusEnum> strs) {
        //年检失败或年检待处理
        if((annualResultDto.getPbcSubmitStatus() == PbcSubmitStatusEnum.FAIL && annualResultDto.getForceStatus() == ForceStatusEnum.INIT)
                || (annualResultDto.getResult() == ResultStatusEnum.FAIL && annualResultDto.getForceStatus() == ForceStatusEnum.INIT
                && (annualResultDto.getPbcSubmitStatus() == null || strs.contains(annualResultDto.getPbcSubmitStatus())))) {

            synchronized(AnnualResultTaskExecutor.class) {
                //TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                //TransactionStatus transaction = transactionManager.getTransaction(definition);

                try {
                    String acctNo = annualResultDto.getAcctNo();
                    String legalTelephone = getLegalTelephone(acctNo);

                    if (StringUtils.isNotBlank(legalTelephone)) {
                        //短信发送接口开放
                        annualResultService.sendMessageNotice(annualResultDto, legalTelephone);
                    }

                } catch (Exception e) {
                    log.error("年检结果通知接口出错", e);
                }

                //transactionManager.commit(transaction);
            }

        }
    }

    /**
     * 根据账号获取法人联系电话
     * @param acctNo
     * @return
     */
    private String getLegalTelephone(String acctNo) {
        String legalTelephone = "";

        if(StringUtils.isNotBlank(acctNo)) {
            AccountsAllInfo byAcctNo = accountsAllService.findByAcctNo(acctNo);
            if(byAcctNo != null) {
                CustomerPublicLogInfo customerPublicLogInfo = customerPublicLogService.getOne(byAcctNo.getCustomerLogId());
                if(customerPublicLogInfo != null) {
                    legalTelephone = customerPublicLogInfo.getLegalTelephone();
                }
            }

        }

        return legalTelephone;
    }

    private void updateTask(AnnualResultDto annualResultDto){
        synchronized(AnnualResultTaskExecutor.class){
            TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus transaction = transactionManager.getTransaction(definition);
            annualTaskService.updateNum(annualResultDto.getTaskId(), annualResultDto.getResult() == ResultStatusEnum.PASS);
            annualTaskService.updateStatus(annualResultDto.getTaskId(), TaskStatusEnum.FINISH);
            transactionManager.commit(transaction);
        }
    }


	/**
	 * 创建定时采集工商数据任务
	 */
	@Override
	public boolean createSaicTimedTask(final Long taskId) {
		CollectConfigDto ccd = collectConfigService.findByAnnualTaskId(taskId);//获取工商采集配置
		int saicCollectTaskStatus = saicCollectionService.checkSaicCollectTaskStatus(taskId);//获取工商采集状态
		if (saicCollectTaskStatus == 7) {//定时执行时间在当前时间之前，直接进行采集
			saicCollectStart(taskId);
			return true;
		} else if (saicCollectTaskStatus == 8) {//定时执行时间在当前时间之后，则创建定时采集任务
			try {
				Date saicDate = com.ideatech.common.util.DateUtils.parse(ccd.getSaicStartDate(), "yyyy-MM-dd HH:mm:ss");
				//拼接CronTrigger表达式
				Calendar saicCa = Calendar.getInstance();
				assert saicDate != null;
				saicCa.setTime(saicDate);
				String cronTriggerStr = "";//CronTrigger表达式
				cronTriggerStr += saicCa.get(Calendar.SECOND) + " ";//秒
				cronTriggerStr += saicCa.get(Calendar.MINUTE) + " ";//分
				cronTriggerStr += saicCa.get(Calendar.HOUR_OF_DAY) + " ";//时
				cronTriggerStr += saicCa.get(Calendar.DAY_OF_MONTH) + " ";//日
				cronTriggerStr += (saicCa.get(Calendar.MONTH) + 1) + " ";//月
				cronTriggerStr += "?";
				final int year = saicCa.get(Calendar.YEAR);
				log.info("删除之前的定时工商采集任务");
				if (future != null) {
					future.cancel(true);
				}
				log.info("创建定时工商采集任务，采集开始时间为：{}", ccd.getSaicStartDate());
				future = threadPoolTaskScheduler.schedule(new Runnable() {
					@Override
					public void run() {
						if (Calendar.getInstance().get(Calendar.YEAR) == year) {//指定年份相同才开始采集
							log.info("定时开始工商采集");
							saicCollectStart(taskId);
							future.cancel(true);
							log.info("定时工商采集结束");
						}
					}
				}, new CronTrigger(cronTriggerStr));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	@Override
	public Boolean getIsAnnualSubmit(String organFullId) {
		OrganizationDto organ = organizationService.findByOrganFullId(organFullId);
		PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganId(organ.getId(), EAccountType.AMS);
		if (pbcAccountDto != null) {
			PbcIPAddressDto dto = pbcIPAddressService.getByPbcIPAddress(pbcAccountDto.getIp());
			if (dto != null && dto.getIsAnnualSubmit()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void resetUnSuccess(Long taskId) {
		annualResultService.cleanUnSuccessAnnualResult(taskId);
		log.info("清理未完成年检结果成功");
		AnnualTask annualTask = getBaseDao().findById(taskId);
		Long[] nums = annualResultService.countProcessedAndPassedNum(taskId);
		annualTask.setSum(nums[0]);
		annualTask.setProcessedNum(nums[1]);
		annualTask.setPassedNum(nums[2]);
		annualTask.setStatus(TaskStatusEnum.INIT);
		getBaseDao().save(annualTask);
	}

	@Override
	public Set<String> annualCheckAmsPassword() {
		Set<String> amsLoginErrorList = new HashSet<>();
		Map<Long, OrganizationDto> orgMap = getOrganMap();
		List<PbcAccountDto> accounts = pbcAccountService.listByType(EAccountType.AMS);
		for (PbcAccountDto account : accounts) {
			try {
			    if(account.getAccount().startsWith("2")){
			        log.info("年检采集检查人行账号{}",account.getAccount());
                    PbcUserAccount pbcUserAccount = new PbcUserAccount();
                    pbcUserAccount.setLoginIp(account.getIp());
                    pbcUserAccount.setLoginPassWord(account.getPassword());
                    pbcUserAccount.setLoginUserName(account.getAccount());
                    LoginAuth loginAuth = amsMainService.amsLogin(pbcUserAccount);
                    log.info("检查人行用户名密码账户登录状态：" + loginAuth.getLoginStatus().getFullName());
//                    account.setAccountStatus(loginAuth.getLoginStatus() == LoginStatus.Success ? EAccountStatus.VALID : EAccountStatus.INVALID);
                    //人行登录不成功  记录下来
                    if(loginAuth.getLoginStatus() != LoginStatus.Success){
                        amsLoginErrorList.add(orgMap.get(account.getOrgId()).getName());
                    }
                }
			} catch (Exception e) {
				log.error("年检采集检查人行用户登录情况异常," + account, e);
			}
		}
		return amsLoginErrorList;
	}


	public Map<Long, OrganizationDto> getOrganMap() {
		Map<Long, OrganizationDto> map = new HashMap<>();
		List<OrganizationDto> list = organizationService.listAll();
		for (OrganizationDto organizationDto : list) {
			map.put(organizationDto.getId(), organizationDto);
		}
		return map;
	}
	/**
	 * 开始工商采集
	 */
	private void saicCollectStart(Long taskId) {
		CollectControllerExecutor collectControllerExecutor = new CollectControllerExecutor();
		collectControllerExecutor.setSaicCollectionService(saicCollectionService);
		collectControllerExecutor.setCollectType(CollectType.AFRESH);
		collectControllerExecutor.setTaskId(taskId);
		collectControllerExecutor.setSaicType(0);//在线采集
		taskExecutor.execute(collectControllerExecutor);
	}

}
