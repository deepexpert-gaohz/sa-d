package com.ideatech.ams.compare.processor;

import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.annual.dto.PbcAccountExcelInfo;
import com.ideatech.ams.annual.service.PbcAccountCollectionService;
import com.ideatech.ams.annual.service.PbcFileExcelService;
import com.ideatech.ams.compare.dto.CompareCollectRecordDto;
import com.ideatech.ams.compare.dto.CompareCollectTaskDto;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.enums.CollectState;
import com.ideatech.ams.compare.executor.ComparePbcCollectTaskExecutor;
import com.ideatech.ams.compare.service.ComparePbcInfoService;
import com.ideatech.ams.compare.service.CompareRuleDataSourceService;
import com.ideatech.ams.compare.service.CompareRuleService;
import com.ideatech.ams.compare.service.DataTransformation;
import com.ideatech.ams.compare.vo.CompareCollectRecordVo;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import java.util.*;

/**
 * @Description 比对管理--人行在线采集
 * @Author wanghongjie
 * @Date 2019/2/11
 **/
@Slf4j
@Component
public class PbcOnlineCollectionProcessor extends AbstractOnlineCollectionProcessor{

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    @Qualifier("pbcOnlineCollectionProcessor")
    private OnlineCollectionProcessor onlineCollectionProcessor;

    @Autowired
    @Qualifier("pbcDataTransformation")
    private DataTransformation dataTransformation;

    @Autowired
    private PbcAmsService pbcAmsService;

    @Autowired
    private TransactionUtils transactionUtils;

    @Autowired
    private ThreadPoolTaskExecutor compareExecutor;

    @Autowired
    private ComparePbcInfoService comparePbcInfoService;

    @Autowired
    private CompareRuleDataSourceService compareRuleDataSourceService;

    @Autowired
    private CompareRuleService compareRuleService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private PbcAccountCollectionService pbcAccountCollectionService;

    @Autowired
    private PbcFileExcelService pbcFileExcelService;

    //采集线程数
    @Value("${compare.online.pbc.executorNum}")
    private int amsAccountExecutorNum;

    //重采次数
    @Value("${compare.online.pbc.retryLimit}")
    private int retryLimit;

    //是否用本地有效期内的数据
    @Value("${compare.online.pbc.useLocal}")
    private boolean useLocal;

    /**
     * 是否启用人行限制采集机制
     */
    @Value("${ams.company.pbcCollectionLimit.use:false}")
    private Boolean pbcCollectionLimitUse;

    /**
     * 人行限制采集数量
     */
    @Value("${ams.company.pbcCollectionLimit.num:10000}")
    private Long pbcCollectionLimitNum;

    @Override
    public void initCollect(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        log.info("比对管理--人行在线采集初始化开始");
        //创建新任务
        createNewCollectTask(compareTaskId,dataSourceDto,compareCollectTaskDto);
        log.info("比对管理--人行在线采集初始化结束");
    }

    @Override
    public void initResetCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        log.info("比对管理--人行在线再次采集初始化开始");
        //创建新任务
        resetNewCollectTask(compareTaskId,dataSourceDto,compareCollectTaskDto);
        log.info("比对管理--人行在线再次采集初始化结束");
    }

    @Override
    public void processCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        //根据是否有先决数据源采集
        List<DataSourceDto> parentDataSourceDtoLists = compareTaskService.findParentDataSourceDtoByTaskIdAndDataSourceId(compareTaskId, dataSourceDto.getId());
        Set<CompareCollectRecordVo> compareCollectRecordVos = null;
        if(parentDataSourceDtoLists.size()==0){//无采集数据来源
            log.info("比对管理--人行在线采集开始-无先决数据源");
            compareCollectRecordVos = readyForCollectionWithNull(compareCollectTaskDto);
        }else {
            log.info("比对管理--人行在线采集开始-有先决数据源");
            compareCollectRecordVos = readyForCollection(compareTaskId, dataSourceDto, compareCollectTaskDto);
        }
        collect(compareTaskId,dataSourceDto,compareCollectTaskDto,compareCollectRecordVos);
    }

    @Override
    public void processResetCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        Set<CompareCollectRecordVo> compareCollectRecordVos = readyForResetCollection(compareTaskId, dataSourceDto, compareCollectTaskDto);
        collect(compareTaskId,dataSourceDto,compareCollectTaskDto,compareCollectRecordVos);
    }

    @Override
    public void finishCollect(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        log.info("比对管理--人行在线采集完成开始");
        finishCollectTask(compareCollectTaskDto.getId(), Propagation.REQUIRES_NEW);
        log.info("比对管理--人行在线采集完成结束");
    }

    /**
     * 采集前置数据源
     * @param compareTaskId
     * @param dataSourceDto
     * @param compareCollectTaskDto
     * @return
     * @throws Exception
     */
    private Set<CompareCollectRecordVo> readyForCollection(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto)throws Exception {
        log.info("比对管理--人行在线采集正式开始");
        List<DataSourceDto> parentDataSourceDtoLists = compareTaskService.findParentDataSourceDtoByTaskIdAndDataSourceId(compareTaskId, dataSourceDto.getId());
        if(parentDataSourceDtoLists.size()==0){//无采集数据来源
            throw new EacException("人行在线采集必须基于其他数据来源");
        }

        List<CompareCollectTaskDto> compareCollectTaskDtos = new ArrayList<CompareCollectTaskDto>();
        for (DataSourceDto dsDto : parentDataSourceDtoLists) {
            CompareCollectTaskDto compareCollectTaskDtoAms = compareCollectTaskService.findByCompareTaskIdAndDataSourceId(compareTaskId, dsDto.getId());
            if (compareCollectTaskDtoAms != null) {
                compareCollectTaskDtos.add(compareCollectTaskDtoAms);
            }
        }

        if(compareCollectTaskDtos.size()>0){
            Set<CompareCollectRecordVo> compareCollectRecordVosSet = mixCompareCollectRecordDto(compareCollectTaskDtos, compareTaskId);
            //更新采集总数
            initCountCollectTask(compareCollectTaskDto.getId(),compareCollectRecordVosSet.size());
            return compareCollectRecordVosSet;
        }else{
            throw new EacException("人行在线采集必须基于其他数据来源");
        }
    }

    /**
     * 再次采集
     * @param compareTaskId
     * @param dataSourceDto
     * @param compareCollectTaskDto
     * @return
     * @throws Exception
     */
    private Set<CompareCollectRecordVo> readyForResetCollection(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto)throws Exception {
        log.info("比对管理--人行在线采集正式开始");
        CompareCollectTaskDto compareCollectTaskDtoOld = compareCollectTaskService.findById(compareCollectTaskDto.getId());
        List<CompareCollectRecordDto> compareCollectRecordDtoList = compareCollectRecordService.findByCollectTaskIdAndCompareTaskIdAndCollectState(compareCollectTaskDto.getId(), compareTaskId, CollectState.fail);
        initCountCollectTask(compareCollectTaskDto.getId(),compareCollectTaskDtoOld.getCount());
        Set<CompareCollectRecordVo> compareCollectRecordVos = new HashSet<>();
        compareCollectRecordVos.addAll(ConverterService.convertToList(compareCollectRecordDtoList,CompareCollectRecordVo.class));
        return compareCollectRecordVos;
    }

    /**
     * 采集动作
     * @param compareTaskId
     * @param dataSourceDto
     * @param compareCollectTaskDto
     * @throws Exception
     */
    private void collect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto,Set<CompareCollectRecordVo> compareCollectRecordVosSet) throws Exception{
        boolean pbcIgnore;
        List<ConfigDto> pbcIgnoreList = configService.findByKey("pbcIgnore");
        if(pbcIgnoreList.size()>0){
            pbcIgnore = true;
        }else{
            pbcIgnore = false;
        }
        Map<String, Set<CompareCollectRecordVo>> batchTokens = getBatchTokens(compareCollectRecordVosSet);
        DataSourceDto dataSourceDtoNew = new DataSourceDto();
        dataSourceDtoNew.setDomain(dataSourceDto.getDataType().getDomain());
        dataSourceDtoNew.setDataType(dataSourceDto.getDataType());
        dataSourceDtoNew.setCode(dataSourceDto.getCode());
        dataSourceDtoNew.setPbcStartTime(dataSourceDto.getPbcStartTime());
        dataSourceDtoNew.setPbcEndTime(dataSourceDto.getPbcEndTime());
        clearFuture();
        for (Map.Entry<String,Set<CompareCollectRecordVo>> entry : batchTokens.entrySet()) {
            Set<CompareCollectRecordVo> compareCollectRecordDtos = entry.getValue();
            ComparePbcCollectTaskExecutor comparePbcCollectTaskExecutor = new ComparePbcCollectTaskExecutor(compareCollectRecordDtos);
            comparePbcCollectTaskExecutor.setAccountsAllService(accountsAllService);
            comparePbcCollectTaskExecutor.setCollectTaskId(compareCollectTaskDto.getId());
            comparePbcCollectTaskExecutor.setCompareCollectRecordService(compareCollectRecordService);
            comparePbcCollectTaskExecutor.setCompareCollectTaskService(compareCollectTaskService);
            comparePbcCollectTaskExecutor.setCompareTaskId(compareTaskId);
            comparePbcCollectTaskExecutor.setDataSourceDto(dataSourceDtoNew);
            comparePbcCollectTaskExecutor.setOnlineCollectionProcessor(onlineCollectionProcessor);
            comparePbcCollectTaskExecutor.setOrganizationService(organizationService);
            comparePbcCollectTaskExecutor.setPbcAmsService(pbcAmsService);
            comparePbcCollectTaskExecutor.setTransactionUtils(transactionUtils);
            comparePbcCollectTaskExecutor.setComparePbcInfoService(comparePbcInfoService);
            comparePbcCollectTaskExecutor.setUseLocal(useLocal);
            comparePbcCollectTaskExecutor.setDataTransformation(dataTransformation);
            comparePbcCollectTaskExecutor.setRetryLimit(retryLimit);
            comparePbcCollectTaskExecutor.setPbcIgnore(pbcIgnore);
            comparePbcCollectTaskExecutor.setToken(entry.getKey());
            comparePbcCollectTaskExecutor.setPbcCollectionLimitUse(pbcCollectionLimitUse);
            comparePbcCollectTaskExecutor.setPbcCollectionLimitNum(pbcCollectionLimitNum);
            futureList.add(compareExecutor.submit(comparePbcCollectTaskExecutor));
        }
        valiCollectCompleted();
        log.info("全部线程执行结束");
        System.gc();
        log.info("比对管理--人行在线采集正式结束");
    }


    /**
     * 人行采集无先决数据源的情况下
     * 采集机构下所有账户信息作为前置数据(相当于ams或saic的采集记录)
     * @return
     */
    private Set<CompareCollectRecordVo> readyForCollectionWithNull(CompareCollectTaskDto compareCollectTaskDto) {

        Set<CompareCollectRecordVo> compareCollectRecordVoSet = new HashSet<>();
        String[] startDateArray = { "1900-01-01", "2006-01-01", "2009-01-01", "2013-01-01", "2016-01-01" };
        String[] endDateArray = { "2005-12-31", "2008-12-31", "2012-12-31", "2015-12-31", "2099-12-31" };


        //判断是否有pbc挡板
        List<ConfigDto> pbcIgnoreList = configService.findByKey("pbcIgnore");
        if(pbcIgnoreList.size()>0){
            log.info("比对管理--人行采集无先决数据源，模拟采集账户。");
            CompareCollectRecordVo compareCollectRecordVo = new CompareCollectRecordVo();
            compareCollectRecordVo.setAcctNo("1234567891");//帐号
            compareCollectRecordVo.setOrganCode("root");//行内机构号
            compareCollectRecordVoSet.add(compareCollectRecordVo);
            compareCollectRecordVo = new CompareCollectRecordVo();
            compareCollectRecordVo.setAcctNo("1234567892");//帐号
            compareCollectRecordVo.setOrganCode("root");//行内机构号
            compareCollectRecordVoSet.add(compareCollectRecordVo);
            compareCollectRecordVo = new CompareCollectRecordVo();
            compareCollectRecordVo.setAcctNo("1234567893");//帐号
            compareCollectRecordVo.setOrganCode("root");//行内机构号
            compareCollectRecordVoSet.add(compareCollectRecordVo);
            compareCollectRecordVo = new CompareCollectRecordVo();
            compareCollectRecordVo.setAcctNo("1234567894");//帐号
            compareCollectRecordVo.setOrganCode("root");//行内机构号
            compareCollectRecordVoSet.add(compareCollectRecordVo);
            compareCollectRecordVo = new CompareCollectRecordVo();
            compareCollectRecordVo.setAcctNo("1234567895");//帐号
            compareCollectRecordVo.setOrganCode("root");//行内机构号
            compareCollectRecordVoSet.add(compareCollectRecordVo);
            //更新采集总数
            try {
                initCountCollectTask(compareCollectTaskDto.getId(),compareCollectRecordVoSet.size());
            } catch (Exception e) {
                log.warn("更新采集总数失败");
                e.printStackTrace();
            }
            return compareCollectRecordVoSet;
        }

        List<OrganizationDto> organizationDtoList = organizationService.listAll();
        log.info("比对管理--需要采集{}个机构下的人行账户数据。",organizationDtoList.size());

        for (OrganizationDto organizationDto : organizationDtoList){
            for (int i = 0; i < startDateArray.length; i++) {
                if (endDateArray[i].equals("2099-12-31")) {
                    endDateArray[i] = DateUtils.getNowDateShort();
                }
                try {
                    //采集相应时间内所有该机构下的人行账户信息。
                    String amsFilePath = pbcAccountCollectionService.downRHAccount(organizationDto.getId(), startDateArray[i], endDateArray[i]);
                    List<PbcAccountExcelInfo> tempList = pbcFileExcelService.getPbcInfoXlsAccounts(amsFilePath);

                    //数据封装
                    for (PbcAccountExcelInfo pbcAccountExcelInfo:tempList){
                        //正常的账户进行比对
                        if("正常".equals(pbcAccountExcelInfo.getAcctStatus())){
                            CompareCollectRecordVo compareCollectRecordVo = new CompareCollectRecordVo();
                            compareCollectRecordVo.setAcctNo(pbcAccountExcelInfo.getAcctNo());//帐号
                            compareCollectRecordVo.setOrganCode(organizationDto.getPbcCode());//行内机构号
                            compareCollectRecordVoSet.add(compareCollectRecordVo);
                        }
                    }
                }catch (Exception e){
                    log.warn("比对管理--采集{}机构,{}到{}人行账户数据出错。",organizationDto.getId(),startDateArray[i],endDateArray[i]);
                    //TODO 重新采集
                    e.printStackTrace();
                }
            }
            log.info("比对管理--{}机构人行账户先决采集数据完成。",organizationDto.getId());
        }

        //更新采集总数
        try {
            initCountCollectTask(compareCollectTaskDto.getId(),compareCollectRecordVoSet.size());
        } catch (Exception e) {
            log.warn("更新采集总数失败");
            e.printStackTrace();
        }
        return compareCollectRecordVoSet;
    }
}
