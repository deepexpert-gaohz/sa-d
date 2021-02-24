package com.ideatech.ams.annual.service;

import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.account.vo.AnnualAccountVo;
import com.ideatech.ams.annual.dao.AnnualTaskDao;
import com.ideatech.ams.annual.dao.CollectTaskDao;
import com.ideatech.ams.annual.dao.CoreCollectionDao;
import com.ideatech.ams.annual.dto.CollectTaskDto;
import com.ideatech.ams.annual.dto.CoreCollectResultSearchDto;
import com.ideatech.ams.annual.dto.CoreCollectionDto;
import com.ideatech.ams.annual.dto.poi.AnnualCorePoi;
import com.ideatech.ams.annual.entity.AnnualTask;
import com.ideatech.ams.annual.entity.CollectTask;
import com.ideatech.ams.annual.entity.CoreCollection;
import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.ams.annual.enums.CollectTaskState;
import com.ideatech.ams.annual.enums.CollectType;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.ams.annual.executor.CoreCollectTaskExecutor;
import com.ideatech.ams.annual.service.poi.AnnualCoreRecordExport;
import com.ideatech.ams.annual.vo.CoreCollectionExcelRowVo;
import com.ideatech.ams.customer.service.CustomersAllService;
import com.ideatech.ams.pbc.utils.NumberUtils;
import com.ideatech.ams.system.dict.dto.OptionDto;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Description 核心数据采集
 * @Author wanghongjie
 * @Date 2018/8/8
 **/
@Service
@Transactional
@Slf4j
public class CoreCollectionServiceImpl implements CoreCollectionService{

    @Autowired
    private TransactionUtils transactionUtils;

    @Autowired
    private AnnualTaskDao annualTaskDao;

    @Autowired
    private CollectTaskDao collectTaskDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private CoreCollectionDao coreCollectionDao;

    @Autowired
    private ThreadPoolTaskExecutor annualExecutor;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private CustomersAllService customersAllService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private AnnualResultService annualResultService;

    private Map<String,String> pbc2cnLegalIdcardTypeDicMap = new HashMap<>();

    private Map<String,String> pbc2cnRegCurrencyTypeDicMap = new HashMap<>();

    private List<Future<Long>> futureList;

    private boolean endFutureFlag;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void collect(Long annualTaskId) {
         LocalCollect(annualTaskId,CollectType.AFRESH);
    }

    //采集失败数据再次采集
    @Override
    public void collectReset(Long annualTaskId) {
        //找出状态不是success的数据
        //重新进行采集
        endFutureFlag = false;
        int totalCount = 0;
        int fail = 0;
        log.info("查询核心采集任务");
//        List<CollectTask> collectTaskList = collectTaskDao.findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(DataSourceEnum.CORE,annualTaskId);
        List<CollectTaskDto> collectTaskDtoList = ConverterService.convertToList(collectTaskDao.findLastTaskByTypeAndAnnualTaskIdAnd(DataSourceEnum.CORE, annualTaskId),CollectTaskDto.class);
        CollectTaskDto collectTaskDto = null;
        if(collectTaskDtoList != null && collectTaskDtoList.size()>0){
            collectTaskDto = collectTaskDtoList.get(0);
        }
        if (collectTaskDto != null) {
            log.info("采集任务状态设置为开始");
            saveTaskCollecting(collectTaskDto.getId());
            log.info("开始采集本地核心数据");

            //初始化数据字典
            initDicMap();

            //查找今年需年检的本地账管数据
            Calendar cal=Calendar.getInstance();
            String lastYearLastDay = (cal.get(Calendar.YEAR)-1)+"-12-31";
            List<AnnualAccountVo> accountsAllList = accountsAllService.getAnnualAccountsAll(lastYearLastDay, null);

            //检查是否第一次采集数据时意外终止，导致核心采集数据跟账管需年检数据数量不一致
            List<CoreCollection> coreCollections = coreCollectionDao.findAll();
            int annualSize = accountsAllList.size();
            int coreAnnualSize = coreCollections.size();
            Set<String> coreAcctNo = new HashSet<>();

            //核心采集的数量跟账管需年检的数据量不对等并且核心数据少于账管需年检数据
            if(annualSize != coreAnnualSize && coreAnnualSize < annualSize) {
                //记录账号字段信息
                for (CoreCollection coreCollection : coreCollections) {
                    coreAcctNo.add(coreCollection.getAcctNo());
                }
            }

            String[] ignoreProperties = {"id"};
            //找出采集失败的数据
            List<CoreCollection> fialCoreCollection = coreCollectionDao.findByAnnualTaskIdAndCollectStateNotIn(annualTaskId,CollectState.success,CollectState.noNeed);
            if(CollectionUtils.isNotEmpty(fialCoreCollection)) {
                try{
                    log.info("核心数据重新采集数量："+(fialCoreCollection.size() + coreAcctNo.size()));
//                    initCountCollectTask(collectTaskDto.getId(),fialCoreCollection.size());
                }catch(Exception e){
                    log.error("初始化数量失败:",e);
                }
                //循环核心采集失败的数据
                for(CoreCollection failCoreCollection : fialCoreCollection) {
                    try{
                        //循环accountsAllList系统需年检的数据，根据账号重新进行采集
                        for(AnnualAccountVo annualAccountVo : accountsAllList){
                            //根据账号进行查找
                            if(StringUtils.equals(failCoreCollection.getAcctNo(),annualAccountVo.getAcctNo())){
                                coreCollectionDao.delete(failCoreCollection);
                                CoreCollection coreCollection = new CoreCollection();
                                //账号一样进行赋值
                                coreCollection.setAnnualTaskId(annualTaskId);
                                coreCollection.setCollectTaskId(collectTaskDto.getId());
                                BeanUtils.copyProperties(annualAccountVo, coreCollection,ignoreProperties);
                                coreCollection.setCollectState(CollectState.success);
                                if(annualAccountVo.getAccountStatus() != null) {
                                    coreCollection.setAccountStatus(annualAccountVo.getAccountStatus().toString());
                                }
                                if(annualAccountVo.getRegisteredCapital() != null){
                                    coreCollection.setRegisteredCapital(NumberUtils.formatCapital(annualAccountVo.getRegisteredCapital()));
                                }
                                if(null!=coreCollection.getOrganCode()){
                                    saveCoreCollectionSuccessed(coreCollection);
                                    totalCount++;
                                }else {
                                    log.error("核心数据导入失败：该条数据行内机构号为空");
                                    String errorMsg = "核心数据导入失败：该条数据行内机构号为空";
                                    saveCoreCollectionError(errorMsg,coreCollection);
                                    fail++;
                                }


                                if(totalCount>20){
                                    updatedCount(collectTaskDto.getId(),totalCount,fail);
                                    totalCount=0;
                                    fail = 0;
                                }
                            }
                        }
                    }catch (Exception e){
                        log.error("保存对象["+failCoreCollection+"失败]:",e);
                    }
                    //本地采集失败数据采集完毕
                    log.info("本地账管核心失败数据重新采集完成！");
                }
                //核心采集的数量跟账管需年检的数据量不对等并且核心数据少于账管需年检数据
                if(annualSize != coreAnnualSize && coreAnnualSize < annualSize){
                    for(AnnualAccountVo annualAccountVo: accountsAllList){
                        //说明核心
                        if(!coreAcctNo.contains(annualAccountVo.getAcctNo())){
                            CoreCollection coreCollection = new CoreCollection();
                            try{
                                coreCollection.setAnnualTaskId(annualTaskId);
                                coreCollection.setCollectTaskId(collectTaskDto.getId());
                                BeanUtils.copyProperties(annualAccountVo, coreCollection);
                                coreCollection.setCollectState(CollectState.success);
                                if(annualAccountVo.getAccountStatus() != null) {
                                    coreCollection.setAccountStatus(annualAccountVo.getAccountStatus().toString());
                                }
                                if(annualAccountVo.getRegisteredCapital() != null){
                                    annualAccountVo.setRegisteredCapital(NumberUtils.formatCapital(annualAccountVo.getRegisteredCapital()));
                                }
                                if(null!=coreCollection.getOrganCode()){
                                    saveCoreCollectionSuccessed(coreCollection);
                                    totalCount++;
                                }else {
                                    log.error("核心数据导入失败：该条数据行内机构号为空");
                                    String errorMsg = "核心数据导入失败：该条数据行内机构号为空";
                                    saveCoreCollectionError(errorMsg,coreCollection);
                                    fail++;
                                }
                                if(totalCount>20){
                                    updatedCount(collectTaskDto.getId(),totalCount,fail);
                                    totalCount=0;
                                    fail = 0;
                                }
                            }catch (Exception e){
                                log.error("保存对象["+coreCollection+"失败]:",e);
                            }
                        }
                    }
                }

                if(totalCount>0||fail>0){
                    try{
                        updatedCount(collectTaskDto.getId(),totalCount,fail);
                    }catch (Exception e){
                        log.error("更新数量报错:",e);
                    }
                }
            }

            try{
                finishCollectTask(collectTaskDto.getId());
            }catch (Exception e){
                log.error("采集核心数据结束异常：",e);
            }
            log.info("结束采集核心数据");
        } else {
            throw new EacException("当前没有采集核心任务，请先创建任务。。");
        }

    }

    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void collect(List<CoreCollectionExcelRowVo> dataList,Long annualTaskId, CollectType collectType) {
        endFutureFlag = false;
        CollectTask collectTask = null;
        // 如果不是继续采集，则重新生成采集列表
//        if (collectType != CollectType.CONTINUE) {
//            createNewCollectTask(collectType,annualTaskId);
//        }

        if(collectType == CollectType.CONTINUE){
            List<CollectTask> collectTaskList = collectTaskDao.findLastTaskByTypeAndAnnualTaskIdAnd(DataSourceEnum.CORE,annualTaskId);
            if(CollectionUtils.isNotEmpty(collectTaskList)){
                collectTask = collectTaskList.get(0);
            } else {
                log.error("未找到历史采集记录，无法重新采集核心数据");
            }
        }else{
            List<CollectTask> collectTaskList = collectTaskDao.findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(DataSourceEnum.CORE,annualTaskId);
            if (CollectionUtils.isNotEmpty(collectTaskList)) {
                collectTask = collectTaskList.get(0);
            }
        }

        //没有创建采集任务  新建
        if (collectTask == null) {
            collectTask = createNewCollectTask(collectType, annualTaskId);
        }

        if (collectTask != null) {
            try{
                //清除线程
                clearFuture();
                // 采集任务状态设置为开始
                saveTaskCollecting(collectTask.getId());
                // 初始化采集的数量
                initCountCollectTask(collectTask.getId(),dataList.size());
                //开始核心采集任务
                startCoreInfo(dataList,annualTaskId,collectTask.getId(), collectType);
                //完成采集任务
                finishCollectTask(collectTask.getId());
            }catch (Exception e){
                log.error("工商数据采集异常", e);
            }
        } else {
            throw new EacException("当前没有采集核心任务，请先创建任务。。");
        }
    }

	@Override
	public void deleteByTaskId(Long taskId) {
		coreCollectionDao.deleteByAnnualTaskId(taskId);
	}

    /**
     * 采集任务状态设置为开始
     * @param collectTaskId 核心采集任务ID
     */
	private void saveTaskCollecting(Long collectTaskId){
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        CollectTask collectTask = collectTaskDao.findById(collectTaskId);
        collectTask.setCollectStatus(CollectTaskState.collecting);
        collectTaskDao.save(collectTask);
        transactionManager.commit(transaction);
    }


	@Override
	public List<CoreCollectionDto> getAll(Long taskId) {
		List<CoreCollection> list = coreCollectionDao.findByAnnualTaskId(taskId);
        List<CoreCollectionDto> dtoList = new ArrayList<CoreCollectionDto>();
		if (CollectionUtils.isNotEmpty(list)) {
			CoreCollectionDto dto = null;
			for (CoreCollection info : list) {
				dto = new CoreCollectionDto();
				BeanCopierUtils.copyProperties(info, dto);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}


    /**
     * 年检企业列表excel
     * @return
     */
    @Override
    public IExcelExport generateAnnualCompanyReport(){
        IExcelExport excelExport = new AnnualCoreRecordExport();
        List<AnnualCorePoi> recordPoiList = new ArrayList<AnnualCorePoi>();
        AnnualCorePoi annualCorePoi = new AnnualCorePoi();
        recordPoiList.add(annualCorePoi);
        excelExport.setPoiList(recordPoiList);
        return excelExport;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void collectOut(Long annualTaskId, List<AnnualAccountVo> accountsAllList) {
        endFutureFlag = false;
        int totalCount = 0;
        int fail = 0;
        CollectTask collectTask = null;

        if (collectTaskDao.findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(DataSourceEnum.CORE, annualTaskId).size() == 0) {
            createNewCollectTask(null, annualTaskId);
        }
        List<CollectTask> collectTaskList = collectTaskDao.findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(DataSourceEnum.CORE,annualTaskId);
        if (CollectionUtils.isNotEmpty(collectTaskList)) {
            collectTask = collectTaskList.get(0);
        }
        if (collectTask != null) {

            // 采集任务状态设置为开始
            saveTaskCollecting(collectTask.getId());

            log.info("开始采集外部核心数据");

            if(CollectionUtils.isNotEmpty(accountsAllList)) {
                try{
                    log.info("初始化数量："+accountsAllList.size());
                    initCountCollectTask(collectTask.getId(),accountsAllList.size());
                }catch(Exception e){
                    log.error("初始化数量失败:",e);
                }
                for(AnnualAccountVo accountVo : accountsAllList) {
                    CoreCollection coreCollection = new CoreCollection();
                    try{
                        coreCollection.setAnnualTaskId(annualTaskId);
                        coreCollection.setCollectTaskId(collectTask.getId());
                        BeanUtils.copyProperties(accountVo, coreCollection);
                        coreCollection.setCollectState(CollectState.success);
                        if(accountVo.getAccountStatus() != null) {
                            coreCollection.setAccountStatus(accountVo.getAccountStatus().toString());
                        }
                        if(accountVo.getRegisteredCapital() != null){
                            accountVo.setRegisteredCapital(NumberUtils.formatCapital(accountVo.getRegisteredCapital()));
                        }
                        if(null!=coreCollection.getOrganCode()){
                            saveCoreCollectionSuccessed(coreCollection);
                            totalCount++;
                        }else {
                            log.error("外部核心数据导入失败：该条数据行内机构号为空");
                            String errorMsg = "外部核心数据导入失败：该条数据行内机构号为空";
                            saveCoreCollectionError(errorMsg,coreCollection);
                            fail++;
                        }


                        if(totalCount>20){
                            updatedCount(collectTask.getId(),totalCount,fail);
                            totalCount=0;
                            fail = 0;
                        }
                    }catch (Exception e){
                        log.error("保存对象["+coreCollection+"失败]:",e);
                    }

                }

                if(totalCount>0||fail>0){
                    try{
                        updatedCount(collectTask.getId(),totalCount,fail);
                    }catch (Exception e){
                        log.error("更新数量报错:",e);
                    }
                }
            }else {
                log.info("采集外部核心数据为空");
            }

            try{
                finishCollectTask(collectTask.getId());
            }catch (Exception e){
                log.error("采集外部核心数据结束异常：",e);
            }
            log.info("结束外部采集核心数据");
        } else {
            throw new EacException("当前没有采集核心任务，请先创建任务。。");
        }

    }

    @Override
    public void collectOutReset(Long annualTaskId, List<AnnualAccountVo> accountsAllList) {
        //找出状态不是success的数据
        //重新进行采集
        endFutureFlag = false;
        int totalCount = 0;
        int fail = 0;
        CollectTask collectTask = null;
        log.info("查询核心采集任务");
        List<CollectTask> collectTaskList = collectTaskDao.findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(DataSourceEnum.CORE,annualTaskId);
        if (CollectionUtils.isNotEmpty(collectTaskList)) {
            collectTask = collectTaskList.get(0);
        }
        if (collectTask != null) {
            log.info("采集任务状态设置为开始");
            saveTaskCollecting(collectTask.getId());
            log.info("开始采集本地核心数据");

            //检查是否第一次采集数据时意外终止，导致核心采集数据跟账管需年检数据数量不一致
            List<CoreCollection> coreCollections = coreCollectionDao.findAll();
            int annualSize = accountsAllList.size();
            int coreAnnualSize = coreCollections.size();
            Set<String> coreAcctNo = new HashSet<>();

            //核心采集的数量跟账管需年检的数据量不对等并且核心数据少于账管需年检数据
            if(annualSize != coreAnnualSize && coreAnnualSize < annualSize) {
                //记录账号字段信息
                for (CoreCollection coreCollection : coreCollections) {
                    coreAcctNo.add(coreCollection.getAcctNo());
                }
            }

            //找出采集失败的数据
            List<CoreCollection> fialCoreCollection = coreCollectionDao.findByAnnualTaskIdAndCollectStateNotIn(annualTaskId,CollectState.success,CollectState.noNeed);
            if(CollectionUtils.isNotEmpty(fialCoreCollection)) {
                try{
                    log.info("核心数据重新采集数量："+fialCoreCollection.size() + coreAcctNo.size());
                    initCountCollectTask(collectTask.getId(),fialCoreCollection.size());
                }catch(Exception e){
                    log.error("初始化数量失败:",e);
                }
                //循环核心采集失败的数据
                for(CoreCollection failCoreCollection : fialCoreCollection) {
                    try{
                        //循环accountsAllList系统需年检的数据，根据账号重新进行采集
                        for(AnnualAccountVo annualAccountVo : accountsAllList){
                            //根据账号进行查找
                            if(StringUtils.equals(failCoreCollection.getAcctNo(),annualAccountVo.getAcctNo())){
                                coreCollectionDao.delete(failCoreCollection);
                                CoreCollection coreCollection = new CoreCollection();
                                //账号一样进行赋值
                                coreCollection.setAnnualTaskId(annualTaskId);
                                coreCollection.setCollectTaskId(collectTask.getId());
                                BeanUtils.copyProperties(annualAccountVo, coreCollection);
                                coreCollection.setCollectState(CollectState.success);
                                if(annualAccountVo.getAccountStatus() != null) {
                                    coreCollection.setAccountStatus(annualAccountVo.getAccountStatus().toString());
                                }
                                if(annualAccountVo.getRegisteredCapital() != null){
                                    annualAccountVo.setRegisteredCapital(NumberUtils.formatCapital(annualAccountVo.getRegisteredCapital()));
                                }
                                if(null!=coreCollection.getOrganCode()){
                                    saveCoreCollectionSuccessed(coreCollection);
                                    totalCount++;
                                }else {
                                    log.error("核心数据导入失败：该条数据行内机构号为空");
                                    String errorMsg = "核心数据导入失败：该条数据行内机构号为空";
                                    saveCoreCollectionError(errorMsg,coreCollection);
                                    fail++;
                                }


                                if(totalCount>20){
                                    updatedCount(collectTask.getId(),totalCount,fail);
                                    totalCount=0;
                                    fail = 0;
                                }
                            }
                        }
                    }catch (Exception e){
                        log.error("保存对象["+failCoreCollection+"失败]:",e);
                    }
                    //本地采集失败数据采集完毕
                    log.info("本地账管核心失败数据重新采集完成！");
                }
                //核心采集的数量跟账管需年检的数据量不对等并且核心数据少于账管需年检数据
                if(annualSize != coreAnnualSize && coreAnnualSize < annualSize){
                    for(AnnualAccountVo annualAccountVo: accountsAllList){
                        //说明核心
                        if(!coreAcctNo.contains(annualAccountVo.getAcctNo())){
                            CoreCollection coreCollection = new CoreCollection();
                            try{
                                coreCollection.setAnnualTaskId(annualTaskId);
                                coreCollection.setCollectTaskId(collectTask.getId());
                                BeanUtils.copyProperties(annualAccountVo, coreCollection);
                                coreCollection.setCollectState(CollectState.success);
                                if(annualAccountVo.getAccountStatus() != null) {
                                    coreCollection.setAccountStatus(annualAccountVo.getAccountStatus().toString());
                                }
                                if(annualAccountVo.getRegisteredCapital() != null){
                                    annualAccountVo.setRegisteredCapital(NumberUtils.formatCapital(annualAccountVo.getRegisteredCapital()));
                                }
                                if(null!=coreCollection.getOrganCode()){
                                    saveCoreCollectionSuccessed(coreCollection);
                                    totalCount++;
                                }else {
                                    log.error("核心数据导入失败：该条数据行内机构号为空");
                                    String errorMsg = "核心数据导入失败：该条数据行内机构号为空";
                                    saveCoreCollectionError(errorMsg,coreCollection);
                                    fail++;
                                }
                                if(totalCount>20){
                                    updatedCount(collectTask.getId(),totalCount,fail);
                                    totalCount=0;
                                    fail = 0;
                                }
                            }catch (Exception e){
                                log.error("保存对象["+coreCollection+"失败]:",e);
                            }
                        }
                    }
                }

                if(totalCount>0||fail>0){
                    try{
                        updatedCount(collectTask.getId(),totalCount,fail);
                    }catch (Exception e){
                        log.error("更新数量报错:",e);
                    }
                }
            }

            try{
                finishCollectTask(collectTask.getId());
            }catch (Exception e){
                log.error("采集核心数据结束异常：",e);
            }
            log.info("结束采集核心数据");
        } else {
            throw new EacException("当前没有采集核心任务，请先创建任务。。");
        }
    }

    @Override
    public int countByCollectStateNot(Long coreAnnualTaskId,CollectState... state) {
        return coreCollectionDao.findByAnnualTaskIdAndCollectStateNotIn(coreAnnualTaskId,CollectState.success,CollectState.noNeed).size();
    }

    @Override
    public CoreCollectResultSearchDto search(final CoreCollectResultSearchDto coreCollectResultSearchDto,final Long taskId) {
        Specification<CoreCollection> specification = new Specification<CoreCollection>() {
            @Override
            public Predicate toPredicate(Root<CoreCollection> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if (null != coreCollectResultSearchDto) {

                    SecurityUtils.UserInfo current = SecurityUtils.getCurrentUser();
                    Long orgId = current.getOrgId();
                    OrganizationDto organizationDto = organizationService.findById(orgId);

                    if(coreCollectResultSearchDto.getCollectState() != null){
                        expressions.add(cb.equal(root.get("collectState"),coreCollectResultSearchDto.getCollectState()));
                    }

                    if (StringUtils.isNotBlank(coreCollectResultSearchDto.getAcctNo())) {
                        expressions.add(cb.like(root.<String>get("acctNo"), "%" + coreCollectResultSearchDto.getAcctNo() + "%"));
                    }

                    if (StringUtils.isNotBlank(coreCollectResultSearchDto.getDepositorName())) {
                        expressions.add(cb.like(root.<String>get("depositorName"), "%" + coreCollectResultSearchDto.getDepositorName() + "%"));
                    }
                    if (StringUtils.isNotBlank(coreCollectResultSearchDto.getOrganCode())) {
                        expressions.add(cb.like(root.<String>get("organCode"), "%" + coreCollectResultSearchDto.getOrganCode() + "%"));
                    }
                    if (StringUtils.isNotBlank(coreCollectResultSearchDto.getLegalName())) {
                        expressions.add(cb.like(root.<String>get("legalName"), "%" + coreCollectResultSearchDto.getLegalName() + "%"));
                    }
                    if (organizationDto.getParentId() != -1){//当前用户不为根机构用户时，核心数据进行过滤。
                        expressions.add(cb.like(root.<String>get("organFullId"),"%"+organizationDto.getFullId()+"%"));
                    }

                }

                expressions.add(cb.equal(root.<Long>get("annualTaskId"),taskId));
                return predicate;
            }
        };
        Page<CoreCollection> all = coreCollectionDao.findAll(specification, new PageRequest(Math.max(coreCollectResultSearchDto.getOffset(), 0), coreCollectResultSearchDto.getLimit(),new Sort(new Sort.Order(Sort.Direction.ASC, "createdDate"))));
        List<CoreCollectionDto> coreCollectionDtoList = ConverterService.convertToList(all.getContent(), CoreCollectionDto.class);
        coreCollectResultSearchDto.setList(coreCollectionDtoList);
        coreCollectResultSearchDto.setTotalRecord(all.getTotalElements());
        coreCollectResultSearchDto.setTotalPages(all.getTotalPages());
        return coreCollectResultSearchDto;
    }

    @Override
    public CoreCollectionDto findById(Long id) {
        CoreCollectionDto coreCollectionDto = new CoreCollectionDto();
        CoreCollection coreCollection = coreCollectionDao.findOne(id);
        if(coreCollection != null){
            BeanUtils.copyProperties(coreCollection,coreCollectionDto);
        }
        return coreCollectionDto;
    }

//	private void cleanOldCollectTask() {
//        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//        TransactionStatus transaction = transactionManager.getTransaction(definition);
//        // 开始新任务前需要删除原先采集任务列表
//        cleanAllCollectList();
//        transactionManager.commit(transaction);
//    }

    private void cleanAllCollectList() {
        try {
            log.info("开始删除核心采集列表");
            coreCollectionDao.deleteAllInBatch();
            log.info("删除核心采集列表结束");
        } catch (Exception e) {
            log.error("删除核心采集列表异常", e);
        }
    }

    private void startCoreInfo(List<CoreCollectionExcelRowVo> dataList,Long annualTaskId,Long collectTaskId, CollectType collectType) throws Exception {
        AnnualTask annualTask = annualTaskDao.findById(annualTaskId);
        CoreCollectTaskExecutor executor = new CoreCollectTaskExecutor(dataList);
        executor.setCoreCollectionDao(coreCollectionDao);
        executor.setAnnualTask(annualTask);
        executor.setCollectTaskId(collectTaskId);
        executor.setCollectTaskDao(collectTaskDao);
        executor.setOrganizationService(organizationService);
        executor.setTransactionUtils(transactionUtils);
        executor.setTransactionManager(transactionManager);
        executor.setDictionaryService(dictionaryService);
        executor.setCollectType(collectType);
        executor.setAnnualResultService(annualResultService);
        futureList.add(annualExecutor.submit(executor));


        valiCollectCompleted(collectTaskId);
        log.info("全部线程执行结束");
        dataList.clear();
        System.gc();
    }

    private CollectTask createNewCollectTask(CollectType collectType,Long taskId) {
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);

        // 开始新任务前需要删除原先采集任务列表
        cleanAllCollectList();
        // 结束之前所有的采集任务
        finishAllCollectTask();

        AnnualTask annualTask = annualTaskDao.findById(taskId);
        List<CollectTask> lastTaskByTypeAndAnnualTaskIdAndNotCompleted = collectTaskDao.findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(DataSourceEnum.CORE, taskId);
        if(lastTaskByTypeAndAnnualTaskIdAndNotCompleted.size()>0 ){
            for(CollectTask collectTask : lastTaskByTypeAndAnnualTaskIdAndNotCompleted){
                if(collectTask.getCollectStatus() == CollectTaskState.fail){
                    collectTask.setCollectStatus(CollectTaskState.init);
                    collectTask.setCount(0);
                    collectTask.setProcessed(0);
                    collectTask.setSuccessed(0);
                    collectTask.setFailed(0);
                    collectTaskDao.save(collectTask);
                    transactionManager.commit(transaction);
                    return collectTask;
                }
            }
        }

        // 开启一个采集任务
        CollectTask collectTask = new CollectTask();
        collectTask.setName(annualTask.getYear()+"年检核心数据采集任务");
        collectTask.setCollectTaskType(DataSourceEnum.CORE);
        collectTask.setAnnualTaskId(taskId);
        collectTask.setCollectStatus(CollectTaskState.init);
        collectTask.setStartTime(DateUtils.getDateTime());
        collectTask.setExceptionReason(null);
        collectTask.setCount(0);
        collectTask.setProcessed(0);
        collectTask.setSuccessed(0);
        collectTask.setFailed(0);
        collectTaskDao.save(collectTask);

        transactionManager.commit(transaction);

        return collectTask;
    }


    private void finishAllCollectTask() {
        try {
            List<CollectTask> collectTaskList = collectTaskDao.findByCollectTaskType(DataSourceEnum.CORE);
            for (CollectTask collectTask : collectTaskList) {
                collectTask.setCollectStatus(CollectTaskState.done);
//                collectTask.setSuccessed(collectTask.getCount());
                collectTask.setIsCompleted(CompanyIfType.Yes);
                collectTaskDao.save(collectTask);
            }
        } catch (Exception e) {
            log.error("修改采集任务异常", e);
        }
    }


    /**
     * 初始化采集任务的采集数量
     * @throws Exception
     */
    private void initCountCollectTask(final Long collectTaskId,final Integer count) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {

            @Override
            public void execute() throws Exception {
                CollectTask collectTask = collectTaskDao.findById(collectTaskId);
                collectTask.setCount((collectTask.getCount() == null ? 0 : collectTask.getCount()) + count);
                collectTaskDao.save(collectTask);
            }
        });
    }

    /**
     * 更新成功数量和失败数量
     * @throws Exception
     */
    private void updatedCount(final Long taskId,final Integer successed,final Integer failed) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {

            @Override
            public void execute() throws Exception {
                CollectTask collectTask = collectTaskDao.findById(taskId);
                collectTask.setSuccessed(collectTask.getSuccessed()+successed);
//                collectTask.setFailed(collectTask.getFailed()+failed);
                collectTask.setFailed(failed);
                collectTaskDao.save(collectTask);
            }
        });
    }
    /**
     * 完成采集任务
     * @throws Exception
     */
    private void finishCollectTask(final Long collectTaskId) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {

            @Override
            public void execute() throws Exception {
                CollectTask collectTask = collectTaskDao.findById(collectTaskId);
                collectTask.setEndTime(DateUtils.getDateTime());
                collectTask.setProcessed(collectTask.getCount());
                if(collectTask.getSuccessed() == 0){
                    collectTask.setIsCompleted(CompanyIfType.No);
                    collectTask.setCollectStatus(CollectTaskState.fail);
                    collectTask.setExceptionReason("核心数据采集成功的数量为0");
                }else{
                    collectTask.setIsCompleted(CompanyIfType.Yes);
                    collectTask.setCollectStatus(CollectTaskState.done);
                }
                collectTaskDao.save(collectTask);
            }
        });
    }

    /**
     * 采集本地账户客户信息
     * @param annualTaskId
     * @param collectType
     */
//    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void LocalCollect(Long annualTaskId, CollectType collectType) {
        endFutureFlag = false;
        int totalCount = 0;
        int fail = 0;
        CollectTask collectTask = null;
        // 如果不是继续采集，则重新生成采集列表
        if (collectType != CollectType.CONTINUE) {
            log.info("创建核心采集任务");
            createNewCollectTask(collectType,annualTaskId);
        }

        //初始化数据字典
        initDicMap();

        log.info("查询核心采集任务");
        List<CollectTask> collectTaskList = collectTaskDao.findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(DataSourceEnum.CORE,annualTaskId);
        if (CollectionUtils.isNotEmpty(collectTaskList)) {
            collectTask = collectTaskList.get(0);
        }
        if (collectTask != null) {
            log.info("采集任务状态设置为开始");
            saveTaskCollecting(collectTask.getId());

            log.info("开始采集本地核心数据");

//            TransactionDefinition definition = new DefaultTransactionDefinition(
//                    TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//            TransactionStatus transaction = null;

            Calendar cal=Calendar.getInstance();
            String lastYearLastDay = (cal.get(Calendar.YEAR)-1)+"-12-31";
            List<AnnualAccountVo> accountsAllList = accountsAllService.getAnnualAccountsAll(lastYearLastDay, null);

            if(CollectionUtils.isNotEmpty(accountsAllList)) {
                try{
                    log.info("初始化数量："+accountsAllList.size());
                    initCountCollectTask(collectTask.getId(),accountsAllList.size());
                }catch(Exception e){
                    log.error("初始化数量失败:",e);
                }
                for(AnnualAccountVo accountVo : accountsAllList) {
                    CoreCollection coreCollection = new CoreCollection();
                    try{
                        coreCollection.setAnnualTaskId(annualTaskId);
                        coreCollection.setCollectTaskId(collectTask.getId());
                        BeanUtils.copyProperties(accountVo, coreCollection);
                        coreCollection.setCollectState(CollectState.success);
                        coreCollection.setAcctType(accountVo.getAcctType().getValue());
                        if(accountVo.getAccountStatus() != null) {
                            coreCollection.setAccountStatus(accountVo.getAccountStatus().toString());
                        }
                        if(accountVo.getRegisteredCapital() != null){
                            accountVo.setRegisteredCapital(NumberUtils.formatCapital(accountVo.getRegisteredCapital()));
                        }
                        if(null!=coreCollection.getOrganCode()){
                            saveCoreCollectionSuccessed(coreCollection);
                            totalCount++;
                        }else {
                            log.error("核心数据导入失败：该条数据行内机构号为空");
                            String errorMsg = "核心数据导入失败：该条数据行内机构号为空";
                            saveCoreCollectionError(errorMsg,coreCollection);
                            fail++;
                        }


                        if(totalCount>20){
                            updatedCount(collectTask.getId(),totalCount,fail);
                            totalCount=0;
                            fail = 0;
                        }
                    }catch (Exception e){
                        log.error("保存对象["+coreCollection+"失败]:",e);
                    }

                }

                if(totalCount>0||fail>0){
                    try{
                        updatedCount(collectTask.getId(),totalCount,fail);
                    }catch (Exception e){
                        log.error("更新数量报错:",e);
                    }
                }
            }

            try{
                finishCollectTask(collectTask.getId());
            }catch (Exception e){
                log.error("采集核心数据结束异常：",e);
            }
            log.info("结束采集核心数据");
        } else {
            throw new EacException("当前没有采集核心任务，请先创建任务。。");
        }

    }


    /**
     *
     * @param dataList
     */
    private void iteratorExcelRow(List<CoreCollectionExcelRowVo> dataList){
        Iterator<CoreCollectionExcelRowVo> iterator = dataList.iterator();
        while(iterator.hasNext()){
            CoreCollectionExcelRowVo core = iterator.next();
            if(core == null || StringUtils.isNotBlank(core.getAcctNo()) || StringUtils.isNotBlank(core.getOrganCode())){
                iterator.remove();
            }
        }
    }


    @Override
    public void clearFuture(){
        if(futureList !=null && futureList.size()>0){
            for (Iterator<Future<Long>> iterator = futureList.iterator(); iterator.hasNext();) {
                Future<Long> future = iterator.next();
                if(future.isDone()){
                    iterator.remove();
                }else{
                    future.cancel(true);
                    iterator.remove();
                }
            }
        }

        futureList = new ArrayList<Future<Long>>();
    }

    /**
     * 正常的数据存储
     * @param coreCollection
     */
    private void saveCoreCollectionSuccessed(final CoreCollection coreCollection) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {

            @Override
            public void execute() throws Exception {

                //法人证件种类 core2pbc && pbc2cn
                if (coreCollection.getLegalIdcardType()!=null && pbc2cnLegalIdcardTypeDicMap.containsKey(coreCollection.getLegalIdcardType())){
                    coreCollection.setLegalIdcardType(pbc2cnLegalIdcardTypeDicMap.get(coreCollection.getLegalIdcardType()));
                }

                //注册币种 core2pbc && pbc2cn
                if (coreCollection.getRegCurrencyType() != null && pbc2cnRegCurrencyTypeDicMap.containsKey(coreCollection.getRegCurrencyType())){
                    coreCollection.setRegCurrencyType(pbc2cnRegCurrencyTypeDicMap.get(coreCollection.getRegCurrencyType()));
                }

                coreCollectionDao.save(coreCollection);
            }
        });
    }

    /**
     * 初始化字典转换map
     */
    private void initDicMap(){

        pbc2cnLegalIdcardTypeDicMap.clear();
        pbc2cnRegCurrencyTypeDicMap.clear();

        //法人证件种类 legalIdcardTypeValue2Item
        List<OptionDto> legalIdcardType2 = dictionaryService.findOptionsByDictionaryName("legalIdcardTypeValue2Item");
        for (OptionDto option : legalIdcardType2) {
            pbc2cnLegalIdcardTypeDicMap.put(option.getName(), option.getValue());
        }

        //注册币种//regCurrencyType2Item
        List<OptionDto> regCurrencyType2 = dictionaryService.findOptionsByDictionaryName("regCurrencyType2Item");
        for (OptionDto option : regCurrencyType2) {
            pbc2cnRegCurrencyTypeDicMap.put(option.getName(), option.getValue());
        }
    }

    /**
     * 错误的数据存储
     * @param coreCollection
     */
    private void saveCoreCollectionError(final String errorMsg,final CoreCollection coreCollection) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {

            @Override
            public void execute() throws Exception {
                coreCollection.setCollectState(CollectState.fail);
                coreCollection.setFailReason(StringUtils.substring(errorMsg,0,2000));
                coreCollectionDao.save(coreCollection);
            }
        });
    }

    /**
     * 判断采集是否完成
     *
     * @param taskId
     * @throws Exception
     */
    private void valiCollectCompleted(final Long taskId) throws Exception {
        while(futureList.size()>0){
            for (Iterator<Future<Long>> iterator = futureList.iterator(); iterator.hasNext();) {
                Future<Long> future = iterator.next();
                if(future.isDone()){
                    iterator.remove();
                }
            }
            // 暂停10秒
            TimeUnit.SECONDS.sleep(10);
        }
    }


    @Override
    public void endFuture(){
        endFutureFlag = true;
    }

}
