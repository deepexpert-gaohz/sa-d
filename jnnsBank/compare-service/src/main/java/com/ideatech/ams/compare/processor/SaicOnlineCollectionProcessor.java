package com.ideatech.ams.compare.processor;

import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.compare.dto.CompareCollectRecordDto;
import com.ideatech.ams.compare.dto.CompareCollectTaskDto;
import com.ideatech.ams.compare.dto.CompareTaskDto;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.enums.CollectState;
import com.ideatech.ams.compare.executor.CompareSaicCollectTaskExecutor;
import com.ideatech.ams.compare.service.CompareCollectTaskService;
import com.ideatech.ams.compare.service.CompareTaskService;
import com.ideatech.ams.compare.service.DataTransformation;
import com.ideatech.ams.compare.vo.CompareCollectRecordVo;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.kyc.service.SaicRequestService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.exception.EacException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;

import java.util.*;

/**
 * @Description 比对管理--工商在线采集
 * @Author wanghongjie
 * @Date 2019/2/14
 **/
@Slf4j
@Component
public class SaicOnlineCollectionProcessor extends AbstractOnlineCollectionProcessor  {
    @Autowired
    @Qualifier("saicOnlineCollectionProcessor")
    private OnlineCollectionProcessor onlineCollectionProcessor;

    @Autowired
    private CompareCollectTaskService compareCollectTaskService;

    @Autowired
    private TransactionUtils transactionUtils;

    @Autowired
    private SaicRequestService saicRequestService;

    @Autowired
    private SaicInfoService saicInfoService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ThreadPoolTaskExecutor compareExecutor;

    @Autowired
    @Qualifier("saicDataTransformation")
    private DataTransformation dataTransformation;

    //采集线程数
    @Value("${compare.online.saic.executorNum}")
    private int amsAccountExecutorNum;

    //重采次数
    @Value("${compare.online.saic.retryLimit}")
    private int retryLimit;

    //是否用本地有效期内的数据
    @Value("${compare.online.saic.useLocal}")
    private boolean useLocal;

    @Autowired
    private SaicMonitorService saicMonitorService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompareTaskService compareTaskService;

    @Override
    public void initCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        log.info("比对管理--工商在线采集初始化开始");
        //创建新任务
        createNewCollectTask(compareTaskId,dataSourceDto,compareCollectTaskDto);
        log.info("比对管理--工商在线采集初始化结束");
    }

    @Override
    public void initResetCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        log.info("比对管理--工商在线再次采集初始化开始");
        //创建新任务
        resetNewCollectTask(compareTaskId,dataSourceDto,compareCollectTaskDto);
        log.info("比对管理--工商在线再次采集初始化结束");
    }

    @Override
    public void processCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        Set<CompareCollectRecordVo> compareCollectRecordVos = readyForCollection(compareTaskId, dataSourceDto, compareCollectTaskDto);
        collect(compareTaskId,dataSourceDto,compareCollectTaskDto,compareCollectRecordVos);
    }

    @Override
    public void processResetCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        Set<CompareCollectRecordVo> compareCollectRecordVos = readyForResetCollection(compareTaskId, dataSourceDto, compareCollectTaskDto);
        collect(compareTaskId,dataSourceDto,compareCollectTaskDto,compareCollectRecordVos);
    }

    @Override
    public void finishCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        log.info("比对管理--工商在线采集完成开始");
        finishCollectTask(compareCollectTaskDto.getId(), Propagation.REQUIRES_NEW);
        log.info("比对管理--工商在线采集完成结束");
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
        log.info("比对管理--工商在线采集正式开始");
        List<DataSourceDto> parentDataSourceDtoLists = compareTaskService.findParentDataSourceDtoByTaskIdAndDataSourceId(compareTaskId, dataSourceDto.getId());
        if(parentDataSourceDtoLists.size()==0){//无采集数据来源
            throw new EacException("工商在线采集必须基于其他数据来源");
        }

        List<CompareCollectTaskDto> compareCollectTaskDtos = new ArrayList<CompareCollectTaskDto>();
        for (DataSourceDto dsDto : parentDataSourceDtoLists) {
            CompareCollectTaskDto compareCollectTaskDtoAms = compareCollectTaskService.findByCompareTaskIdAndDataSourceId(compareTaskId, dsDto.getId());
            if (compareCollectTaskDtoAms != null) {
                compareCollectTaskDtos.add(compareCollectTaskDtoAms);
            }
        }
        if(compareCollectTaskDtos.size()>0) {
            Set<CompareCollectRecordVo> compareCollectRecordVosSet = mixCompareCollectRecordDto(compareCollectTaskDtos, compareTaskId);
            //更新采集总数
            initCountCollectTask(compareCollectTaskDto.getId(), compareCollectRecordVosSet.size());
            return compareCollectRecordVosSet;
        }else{
            throw new EacException("工商在线采集必须基于其他数据来源");
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
        Map<String, Set<CompareCollectRecordVo>> batchTokens = getBatchTokens(compareCollectRecordVosSet);
        DataSourceDto dataSourceDtoNew = new DataSourceDto();
        dataSourceDtoNew.setDomain(dataSourceDto.getDataType().getDomain());
        dataSourceDtoNew.setDataType(dataSourceDto.getDataType());
        dataSourceDtoNew.setCode(dataSourceDto.getCode());

        CompareTaskDto compareTaskDto = compareTaskService.findById(compareTaskId);
        UserDto userDto = userService.findById(Long.parseLong(compareTaskDto.getCreatedBy()));
        clearFuture();
        for (Map.Entry<String,Set<CompareCollectRecordVo>> entry : batchTokens.entrySet()) {
            String key = entry.getKey();
            Set<CompareCollectRecordVo> compareCollectRecordDtos = entry.getValue();
            CompareSaicCollectTaskExecutor compareSaicCollectTaskExecutor = new CompareSaicCollectTaskExecutor(compareCollectRecordDtos);
            compareSaicCollectTaskExecutor.setCollectTaskId(compareCollectTaskDto.getId());
            compareSaicCollectTaskExecutor.setCompareTaskId(compareTaskId);
            compareSaicCollectTaskExecutor.setBatch(key);
            compareSaicCollectTaskExecutor.setRetryLimit(retryLimit);
            compareSaicCollectTaskExecutor.setSaicLocal(useLocal);
            compareSaicCollectTaskExecutor.setTransactionManager(transactionManager);
            compareSaicCollectTaskExecutor.setSaicRequestService(saicRequestService);
            compareSaicCollectTaskExecutor.setSaicInfoService(saicInfoService);
            compareSaicCollectTaskExecutor.setCompareCollectTaskService(compareCollectTaskService);
            compareSaicCollectTaskExecutor.setOnlineCollectionProcessor(onlineCollectionProcessor);
            compareSaicCollectTaskExecutor.setTransactionUtils(transactionUtils);
            compareSaicCollectTaskExecutor.setDataTransformation(dataTransformation);
            compareSaicCollectTaskExecutor.setDataSourceDto(dataSourceDtoNew);
            compareSaicCollectTaskExecutor.setSaicMonitorService(saicMonitorService);
            compareSaicCollectTaskExecutor.setUserDto(userDto);
            futureList.add(compareExecutor.submit(compareSaicCollectTaskExecutor));
        }
        valiCollectCompleted();
        log.info("全部线程执行结束");
        System.gc();
        log.info("比对管理--工商在线采集正式结束");
    }
}
