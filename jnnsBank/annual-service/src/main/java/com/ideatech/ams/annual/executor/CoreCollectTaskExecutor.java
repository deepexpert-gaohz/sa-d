package com.ideatech.ams.annual.executor;

import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.annual.dao.CollectTaskDao;
import com.ideatech.ams.annual.dao.CoreCollectionDao;
import com.ideatech.ams.annual.dto.CollectConfigDto;
import com.ideatech.ams.annual.dto.CoreCollectionDto;
import com.ideatech.ams.annual.entity.AnnualTask;
import com.ideatech.ams.annual.entity.CollectTask;
import com.ideatech.ams.annual.entity.CoreCollection;
import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.ams.annual.enums.CollectTaskState;
import com.ideatech.ams.annual.enums.CollectType;
import com.ideatech.ams.annual.service.AnnualResultService;
import com.ideatech.ams.annual.vo.CoreCollectionExcelRowVo;
import com.ideatech.ams.pbc.utils.NumberUtils;
import com.ideatech.ams.system.dict.dto.OptionDto;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.BeanUtil;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.ReflectionUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * 核心数据采集线程
 * 
 * @author wanghongjie
 *
 */
@Data
@Slf4j
public class CoreCollectTaskExecutor implements Callable {

	public CoreCollectTaskExecutor(List<CoreCollectionExcelRowVo> dataList) {
		this.dataList = dataList;
	}

    private List<CoreCollectionExcelRowVo> dataList;

	private CoreCollectionDao coreCollectionDao;

    private TransactionUtils transactionUtils;

	private AnnualTask annualTask;

	private Long collectTaskId;

	private CollectTaskDao collectTaskDao;

	private OrganizationService organizationService;

    private PlatformTransactionManager transactionManager;

    private Integer successedCount;

    private Integer failedCount;

    private DictionaryService dictionaryService;

    private CollectType collectType;

    private AnnualResultService annualResultService;

    private Map<String,String> core2pbcLegalIdcardTypeDicMap = new HashMap<>();

    private Map<String,String> pbc2cnLegalIdcardTypeDicMap = new HashMap<>();

    private Map<String,String> core2pbcRegCurrencyTypeDicMap = new HashMap<>();

    private Map<String,String> pbc2cnRegCurrencyTypeDicMap = new HashMap<>();

    private Map<String,String> organMap = new HashMap<>();

    private Map<String, OrganizationDto> organFullIdMap = new HashMap<>();

    @Override
    public Object call() throws Exception {
        if (dataList != null && dataList.size()>0) {
            //初始化数据字典
            initDicMap();
            //初始化机构数据
            initOrganMap();
            collectData();
        }
        return System.currentTimeMillis();
    }

//	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void collectData() {
		log.info("开始采集核心数据");

		successedCount=0;
		failedCount=0;
        //找出该年检id已存在的账号  防止多sheet重复导入账号的数据
        //根据采集任务id找
		List<CoreCollection> coreCollectionDtos = coreCollectionDao.findByCollectTaskId(collectTaskId);
        HashSet<String> acctNoSet = new HashSet<>();
        if(CollectionUtils.isNotEmpty(coreCollectionDtos)){
            for(CoreCollection coreCollection : coreCollectionDtos){
                acctNoSet.add(coreCollection.getAcctNo());
            }
        }
        Map<String, Integer> stringIntegerMap = BeanUtil.fieldMapForErrorAccount(CoreCollection.class);
        if(dataList !=null && dataList.size()>0){
            //初始化数据字典
            initDicMap();
            for( CoreCollectionExcelRowVo core:dataList){
                log.info("采集企业{}核心数据成功", core.getDepositorName());
                try{
                    saveCoreCollection(core,acctNoSet,stringIntegerMap);
                }catch (Exception e){
                    log.error("核心数据导入失败：["+core+"]",e);
                    try{
                        saveCoreCollectionError(core,"核心数据导入失败"+e.getMessage(),null);
                        failedCount++;
                    }catch (Exception e1){
                        log.error("核心数据导入失败,保存失败：["+core+"]",e);
                    }
                }
                if(successedCount + failedCount >20){
                    updatedCount();
                }
            }
        }
        if(successedCount + failedCount >0){
            updatedCount();
        }
		log.info("结束采集核心数据");
	}


    /**
     * 核心数据存储
     * @param core
     * @param acctNoSet
     * @param stringIntegerMap
     * @throws Exception
     */
    private void saveCoreCollection(final CoreCollectionExcelRowVo core,final HashSet<String> acctNoSet,final Map<String, Integer> stringIntegerMap) throws Exception {
        CoreCollection coreCollection = new CoreCollection();
        CoreCollectionExcelRowVo coreNew = core;
        String result = checkStringLength(coreNew, stringIntegerMap);
        String errorMsg;
        if(result !=null){//判断字段长度是否符合
            log.error("核心数据对象["+core+"],字段长度异常：{}",result);
            BeanUtils.copyProperties(coreNew,coreCollection);
            errorMsg = "字段长度异常：{}"+result;
            saveCoreCollectionError(coreNew,errorMsg,null);
            failedCount++;
            return;
        }
        boolean exist = false;
        if(core != null && StringUtils.isNotBlank(core.getAcctNo()) && StringUtils.isNotBlank(core.getOrganCode())){
//            OrganizationDto organizationDto = organizationService.findByCode(core.getOrganCode());
            core.setDepositorName(core.getDepositorName().trim());
            BeanUtils.copyProperties(core,coreCollection);

            //法人证件种类 core2pbc && pbc2cn
            if (coreCollection.getLegalIdcardType() != null && core2pbcLegalIdcardTypeDicMap.containsKey(coreCollection.getLegalIdcardType())){
                if (pbc2cnLegalIdcardTypeDicMap.containsKey(core2pbcLegalIdcardTypeDicMap.get(coreCollection.getLegalIdcardType()))){
                    coreCollection.setLegalIdcardType(pbc2cnLegalIdcardTypeDicMap.get(core2pbcLegalIdcardTypeDicMap.get(coreCollection.getLegalIdcardType())));
                }
            }

            //注册币种 core2pbc && pbc2cn
            if (coreCollection.getRegCurrencyType() != null && core2pbcRegCurrencyTypeDicMap.containsKey(coreCollection.getRegCurrencyType())){
                if (pbc2cnRegCurrencyTypeDicMap.containsKey(core2pbcRegCurrencyTypeDicMap.get(coreCollection.getRegCurrencyType()))){
                    coreCollection.setRegCurrencyType(pbc2cnRegCurrencyTypeDicMap.get(core2pbcRegCurrencyTypeDicMap.get(coreCollection.getRegCurrencyType())));
                }
            }


            if(core != null && StringUtils.isNotBlank(core.getRegisteredCapitalStr())){
                try{
                    BigDecimal bigDecimal = NumberUtils.formatCapital(NumberUtils.convertCapital(core.getRegisteredCapitalStr()));
                    coreCollection.setRegisteredCapital(bigDecimal);
                }catch (Exception e){
                    log.error("注册资金转化出错：["+core+"]",e);
                    errorMsg = "注册资金转化出错,注册资金为："+core.getRegisteredCapitalStr();
                    saveCoreCollectionError(core,errorMsg,null);
                    failedCount++;
                    return;
                }
            }
            if (MapUtils.isNotEmpty(organMap) && organMap.containsKey(core.getOrganCode())) {
                //log.info("找到对应的行内机构号->"+core.getOrganCode());
                coreCollection.setOrganFullId(organMap.get(core.getOrganCode()));
                //账号不存在且不为重新导入
                if (!acctNoSet.contains(core.getAcctNo())) {
                    acctNoSet.add(core.getAcctNo());
                    saveCoreCollectionSuccessed(coreCollection);
                }else{
                    if(collectType == CollectType.CONTINUE){
                        log.error("更新继续采集的数据，账号{}", core.getAcctNo());
                        updateCoreCollectionSuccessed(coreCollection);
                        //重复数据不再叠加计数
                        exist = true;
                    }else{
                        errorMsg = "账号重复，导入失败";
                        log.error("账号重复，导入失败-{}",coreCollection);
                        saveCoreCollectionError(core,errorMsg,coreCollection);
                    }
                }
            }else{
                log.error("未找到对应的行内机构号->"+core.getOrganCode());
                errorMsg = "无法找到对应的行内机构号，导入失败";
                saveCoreCollectionError(core,errorMsg,coreCollection);
            }
        }else{
            log.error("核心数据导入失败：该条数据为空或者账号或者行内机构号为空-{}",core);
            errorMsg = "核心数据导入失败：该条数据为空或者账号或者行内机构号为空";
            saveCoreCollectionError(core,errorMsg,null);
        }
        if (coreCollection.getCollectState() == CollectState.success) {
            if (!exist) {
                successedCount++;
            }
        } else {
            failedCount++;
        }
    }




    /**
     * 正常的数据存储
     * @param coreCollection
     */
    private void saveCoreCollectionSuccessed(final CoreCollection coreCollection) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {

            @Override
            public void execute() throws Exception {
                coreCollection.setAnnualTaskId(annualTask.getId());
                coreCollection.setCollectTaskId(collectTaskId);
                coreCollection.setCollectState(CollectState.success);
                coreCollectionDao.save(coreCollection);

                //如果是继续采集且是之前未保存的数据，则保存到annualResult表
                if (collectType == CollectType.CONTINUE) {
                    log.info("核心数据继续采集,开始更新结果表核心数据");
                    CoreCollectionDto coreCollectionDto = new CoreCollectionDto();
                    BeanCopierUtils.copyProperties(coreCollection, coreCollectionDto);
                    annualResultService.updateAnnualResultData(annualTask.getId(), coreCollectionDto, organFullIdMap);
                }
            }
        });
    }

    /**
     * 更新数据并且更新结果表
     * @param coreCollection
     */
    private void updateCoreCollectionSuccessed(final CoreCollection coreCollection) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {
            @Override
            public void execute() throws Exception {
                CoreCollection dbCoreCollection = null;
                //找到原始数据，仅在成功列表里找
                List<CoreCollection> coreCollections = coreCollectionDao.findByCollectTaskIdAndAcctNoAndCollectState(collectTaskId, coreCollection.getAcctNo(), CollectState.success);
                if (CollectionUtils.isNotEmpty(coreCollections)) {
                    if (coreCollections.size() > 1) {
                        log.error("更新核心采集数据，采集Id{}，采集账号{}数据存在多条", collectTaskId, coreCollection.getAcctNo());
                    }
                    dbCoreCollection = coreCollections.get(0);
                }
                if (dbCoreCollection == null) {
                    dbCoreCollection = new CoreCollection();
                    log.info("未找到需要更新的核心数据，可能是原数据导入失败后重新导入", coreCollection);
                }
                coreCollection.setId(dbCoreCollection.getId());
                coreCollection.setAnnualTaskId(annualTask.getId());
                coreCollection.setCollectTaskId(collectTaskId);
                coreCollection.setCollectState(CollectState.success);
                BeanCopierUtils.copyProperties(coreCollection, dbCoreCollection);
                coreCollectionDao.save(dbCoreCollection);

                //如果是继续采集，则保存更新annualResult数据
                if (collectType == CollectType.CONTINUE) {
                    log.info("核心数据继续采集,开始更新结果表核心数据");
                    CoreCollectionDto coreCollectionDto = new CoreCollectionDto();
                    BeanCopierUtils.copyProperties(dbCoreCollection, coreCollectionDto);
                    annualResultService.updateAnnualResultData(annualTask.getId(), coreCollectionDto, organFullIdMap);
                }
            }
        });
    }

    /**
     * 报异常时的数据存储
     * @param core
     * @param errorMsg
     * @param coreCollection
     */
    private void saveCoreCollectionError(final CoreCollectionExcelRowVo core,final String errorMsg,final CoreCollection coreCollection) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {

            @Override
            public void execute() throws Exception {
                CoreCollection coreCollectionNew = null;
                if(coreCollection == null){
                    coreCollectionNew = new CoreCollection();
                    BeanUtils.copyProperties(core,coreCollectionNew);
                }else{
                    coreCollectionNew = coreCollection;
                }
                coreCollectionNew.setAnnualTaskId(annualTask.getId());
                coreCollectionNew.setCollectTaskId(collectTaskId);
                coreCollectionNew.setCollectState(CollectState.fail);
                coreCollectionNew.setFailReason(StringUtils.substring(errorMsg,0,2000));
                coreCollectionDao.save(coreCollectionNew);
            }
        });
    }

    /**
     * 更新采集的成功和失败数量
     * @throws Exception
     */
    private void updatedCount(){
        synchronized (CoreCollectTaskExecutor.class){
            TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus transaction = transactionManager.getTransaction(definition);
            try{
                CollectTask collectTask = collectTaskDao.findById(collectTaskId);
                collectTask.setSuccessed(collectTask.getSuccessed()+successedCount);
                collectTask.setFailed(collectTask.getFailed()+failedCount);
                collectTaskDao.save(collectTask);
                transactionManager.commit(transaction);
                successedCount=0;
                failedCount=0;
            }catch (Exception e){
                if(transaction.isCompleted()){
                    log.error("更新采集的成功和失败数量--提交时失败",e);
                }else{
                    log.error("更新采集的成功和失败数量--操作时失败，进行回滚",e);
                    try{
                        transactionManager.rollback(transaction);
                    }catch (Exception e1){
                        log.error("更新采集的成功和失败数量--操作时失败，进行回滚，回滚失败",e);
                    }
                }
            }
        }
    }


    /**
     * 更新采集任务
     * @throws Exception
     */
    private void updateCollectTask() throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {

            @Override
            public void execute() throws Exception {
                Long successedCount = coreCollectionDao.countAllByCollectState(CollectState.success);
                Long failCount = coreCollectionDao.countAllByCollectState(CollectState.fail);
                Long totalCount = coreCollectionDao.count();
                CollectTask collectTask = collectTaskDao.findById(collectTaskId);
                collectTask.setCount(totalCount.intValue());
                collectTask.setProcessed(successedCount.intValue()+failCount.intValue());
                collectTask.setSuccessed(successedCount.intValue());
                collectTask.setFailed(failCount.intValue());
                collectTask.setEndTime(DateUtils.getDateTime());
                if(successedCount.intValue() == 0){
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
     * 判断字符长度
     * @param core
     * @param stringIntegerMap
     * @return
     */
    private String checkStringLength(CoreCollectionExcelRowVo core,Map<String, Integer> stringIntegerMap){
        Map<String, Field> fieldMap = ReflectionUtil.getFieldObjMap(core);
        Iterator<Map.Entry<String, Field>> iterator = fieldMap.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (iterator.hasNext()){
            Map.Entry<String, Field> next = iterator.next();
            String key = next.getKey();//字段大写
            Field field = next.getValue();
            String fieldName = field.getName();//字段原来的名字
            Method getMethod = ReflectionUtil.getGetter(CoreCollectionExcelRowVo.class, field);
            String value = null;
            try {
                value = (String) getMethod.invoke(core);
            } catch (Exception e) {
                log.error("错误使用反射get方法: {}", e, key);
            }
            if(stringIntegerMap.containsKey(key) && StringUtils.isNotBlank(value) && StringUtils.length(value) > stringIntegerMap.get(key)){
                if(StringUtils.isNotBlank(sb.toString())){
                    sb.append(",");
                }
                sb.append("字段["+key+"]长度超过["+stringIntegerMap.get(key)+"]");
                ReflectionUtil.invokeSetter(core,fieldName,StringUtils.substring(value,0,stringIntegerMap.get(key)));
            }
        }
        if(StringUtils.isBlank(sb.toString())){
            return null;
        }else{
            return sb.toString();
        }
    }

    /**
     * 初始化字典转换map
     */
    private void initDicMap(){

        core2pbcLegalIdcardTypeDicMap.clear();
        pbc2cnLegalIdcardTypeDicMap.clear();
        core2pbcRegCurrencyTypeDicMap.clear();
        pbc2cnRegCurrencyTypeDicMap.clear();

        //法人证件种类 legalIdcardTypeValue2Item
        List<OptionDto> legalIdcardType = dictionaryService.findOptionsByDictionaryName("core2pbc-legalIdcardType");
        for (OptionDto option : legalIdcardType) {
            core2pbcLegalIdcardTypeDicMap.put(option.getName(), option.getValue());
        }

        List<OptionDto> legalIdcardType2 = dictionaryService.findOptionsByDictionaryName("legalIdcardTypeValue2Item");
        for (OptionDto option : legalIdcardType2) {
            pbc2cnLegalIdcardTypeDicMap.put(option.getName(), option.getValue());
        }

        //注册币种//regCurrencyType2Item
        List<OptionDto> regCurrencyType = dictionaryService.findOptionsByDictionaryName("core2pbc-regCurrencyType");
        for (OptionDto option : regCurrencyType) {
            core2pbcRegCurrencyTypeDicMap.put(option.getName(), option.getValue());
        }

        List<OptionDto> regCurrencyType2 = dictionaryService.findOptionsByDictionaryName("regCurrencyType2Item");
        for (OptionDto option : regCurrencyType2) {
            pbc2cnRegCurrencyTypeDicMap.put(option.getName(), option.getValue());
        }
    }

    private void initOrganMap() {
        organMap.clear();
        List<OrganizationDto> organizationDtos = organizationService.listAll();
        for (OrganizationDto organizationDto : organizationDtos) {
            organMap.put(organizationDto.getCode(), organizationDto.getFullId());
        }
        organFullIdMap.clear();
        organFullIdMap = organizationService.findAllInMap();
    }
}
