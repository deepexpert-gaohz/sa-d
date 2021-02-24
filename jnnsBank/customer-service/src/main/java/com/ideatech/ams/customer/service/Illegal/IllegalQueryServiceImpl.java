package com.ideatech.ams.customer.service.Illegal;

import com.ideatech.ams.customer.dao.illegal.IllegalQueryBatchDao;
import com.ideatech.ams.customer.dao.illegal.IllegalQueryDao;
import com.ideatech.ams.customer.dao.spec.IllegalQueryBatchSpec;
import com.ideatech.ams.customer.dao.spec.IllegalQuerySpec;
import com.ideatech.ams.customer.dto.illegal.IllegalQueryBatchDto;
import com.ideatech.ams.customer.dto.illegal.IllegalQueryDto;
import com.ideatech.ams.customer.dto.poi.IllegalQueryPoi;
import com.ideatech.ams.customer.entity.illegal.IllegalQuery;
import com.ideatech.ams.customer.entity.illegal.IllegalQueryBatch;
import com.ideatech.ams.customer.enums.illegal.IllegalQueryExpiredStatus;
import com.ideatech.ams.customer.enums.illegal.IllegalQueryStatus;
import com.ideatech.ams.customer.executor.IllegalQueryDtoExecutor;
import com.ideatech.ams.customer.executor.IllegalQueryExecutor;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.customer.service.illegal.IllegalQueryErrorService;
import com.ideatech.ams.customer.service.illegal.IllegalQueryService;
import com.ideatech.ams.customer.service.poi.IllegalQueryExport;
import com.ideatech.ams.kyc.dto.*;
import com.ideatech.ams.kyc.service.SaicRequestService;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class IllegalQueryServiceImpl implements IllegalQueryService {

    @Value("${saic.url.illegalQuery}")
    private String illegalQueryIDPUrl;

    @Value("${ams.illegalImport.executor.num:10}")
    private int illegalImportExecutorNum;

    @Autowired
    private IllegalQueryDao illegalQueryDao;

    @Autowired
    private IllegalQueryBatchDao illegalQueryBatchDao;

    @Autowired
    private SaicRequestService saicRequestService;

    @Autowired
    private IllegalQueryService illegalQueryService;

    @Autowired
    private IllegalQueryErrorService illegalQueryErrorService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private OrganizationService organizationService;

    private List<Future<Long>> futureList;

    @Autowired
    private SaicMonitorService saicMonitorService;


    @Override
    public void save(IllegalQueryDto illegalQueryDto) {
        IllegalQuery illegalQuery = new IllegalQuery();
        BeanUtils.copyProperties(illegalQueryDto,illegalQuery);
        illegalQueryDao.save(illegalQuery);
    }

    @Override
    public Long saveIllegalQueryBatch(IllegalQueryBatchDto illegalQueryBatchDto) {
        IllegalQueryBatch illegalQueryBatch = null;
        if (illegalQueryBatchDto.getId() != null) {
            illegalQueryBatch = illegalQueryBatchDao.findOne(illegalQueryBatchDto.getId());
        }
        if (illegalQueryBatch != null) {
            BeanUtils.copyProperties(illegalQueryBatchDto, illegalQueryBatch);
        } else {
            illegalQueryBatch = new IllegalQueryBatch();
            BeanUtils.copyProperties(illegalQueryBatchDto, illegalQueryBatch);
        }
        Long illegalQueryBatchId = illegalQueryBatchDao.save(illegalQueryBatch).getId();
        BeanUtils.copyProperties(illegalQueryBatch, illegalQueryBatchDto);//为解决新建时存在createBy和createDate为null。
        return illegalQueryBatchId;
    }

    @Override
    public void illegalCheck(Long batchId) {
        //根据batchId去数据库查询该批次的数据
        List<IllegalQuery> queryList = illegalQueryDao.findByIllegalQueryBatchId(batchId);
        List<IllegalQueryDto> queryDtoList = ConverterService.convertToList(queryList,IllegalQueryDto.class);
        clearFuture();
        Set<IllegalQueryDto> tokens = new HashSet<IllegalQueryDto>(16);
        for (IllegalQueryDto illegalQueryDto : queryDtoList) {
            tokens.add(illegalQueryDto);
        }
        if (CollectionUtils.isNotEmpty(tokens) && tokens.size() > 0) {
            Map<String, Set<IllegalQueryDto>> batchTokens = getBatchTokens(tokens);
            if (MapUtils.isNotEmpty(batchTokens) && batchTokens.size() > 0) {
                IllegalQueryExecutor executor = null;
                for (String batch : batchTokens.keySet()) {
                    executor = new IllegalQueryExecutor(batchTokens.get(batch));
                    executor.setIllegalQueryDao(illegalQueryDao);
                    executor.setSaicRequestService(saicRequestService);
                    executor.setTransactionManager(transactionManager);
                    executor.setOrganId(SecurityUtils.getCurrentUser().getOrgId());
                    executor.setUserName(SecurityUtils.getCurrentUsername());
                    executor.setSaicMonitorService(saicMonitorService);
                    executor.setUserName(SecurityUtils.getCurrentUsername());
                    executor.setOrganId(SecurityUtils.getCurrentUser().getOrgId());
                    futureList.add(taskExecutor.submit(executor));
                }
            }
            try {
                valiCollectCompleted();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("全部线程执行结束");
        log.info("保存批量查询完成结果为：true");
        IllegalQueryBatch illegalQueryBatch = illegalQueryBatchDao.findOne(batchId);
        illegalQueryBatch.setProcess(true);
        illegalQueryBatchDao.save(illegalQueryBatch);
        System.gc();
    }

    @Override
    public IllegalQueryDto illegalQueryCheck(Long id, String keyWord) {
        boolean flag = true;
        boolean isChangemess = false;

        IllegalQueryStatus illegalStatus;
        IllegalQueryDto illegalQueryDto = null;
//        String oldSaicStatus = "";

        try {
            keyWord = URLDecoder.decode(keyWord, "UTF-8");

            IllegalQuery illegalQuery = illegalQueryDao.findOne(id);

//            OutIllegalQueryDto outIllegalQueryDto = saicRequestService.getOutIllegalQueryDto(keyWord);
//            List<IllegalQueriesDto> dtoList = outIllegalQueryDto.getItems();
//            if (CollectionUtils.isNotEmpty(dtoList)) {
//                illegalStatus = IllegalQueryStatus.ILLEGAL;
//                illegalQuery.setIllegalStatus(IllegalQueryStatus.ILLEGAL);
//            } else {
//                illegalStatus = IllegalQueryStatus.NORMAL;
//                illegalQuery.setIllegalStatus(IllegalQueryStatus.NORMAL);
//            }

            illegalQuery.setIsChangemess(false);
            //增加工商状态的查询
            String saicStatus = "";
//            oldSaicStatus = illegalQuery.getSaicStatus();
            SaicIdpInfo saicIdpInfo = saicRequestService.getSaicInfoExact(keyWord);
            if(saicIdpInfo != null){
                if(StringUtils.isNotBlank(saicIdpInfo.getState())){
                    saicStatus = saicIdpInfo.getState();
                    illegalQuery.setSaicStatus(saicStatus);
                }
                //查询是否有经营异常
                if(CollectionUtils.isNotEmpty(saicIdpInfo.getChangemess())){
                    List<ChangeMessDto> changeMessDtos = saicIdpInfo.getChangemess();
                    for(ChangeMessDto ChangeMessDto : changeMessDtos){
                        if(StringUtils.isNotBlank(ChangeMessDto.getOutdate()) && StringUtils.isNotBlank(ChangeMessDto.getOutreason())){
                            isChangemess = false;
                        }
                    }
                    if(!isChangemess){
                        illegalQuery.setChangemess(IllegalQueryStatus.NORMAL);
                    }else{
                        isChangemess = true;
                        illegalQuery.setChangemess(IllegalQueryStatus.CHANGEMESS);
                    }
                }else{
                    illegalQuery.setChangemess(IllegalQueryStatus.NORMAL);
                }
                //营业执照到期日
                if(StringUtils.isNotBlank(saicIdpInfo.getEnddate())){
                    illegalQuery.setFileEndDate(saicIdpInfo.getEnddate());
                }

                //严重违法
                List<IllegalDto> dtoList = saicIdpInfo.getIllegals();
                if (CollectionUtils.isNotEmpty(dtoList)) {
                    illegalQuery.setIllegalStatus(IllegalQueryStatus.ILLEGAL);
                } else {
                    illegalQuery.setIllegalStatus(IllegalQueryStatus.NORMAL);
                }

            }else{
                illegalQuery.setSaicStatus("未查到");
                illegalQuery.setChangemess(IllegalQueryStatus.EMPTY);
                illegalQuery.setFileEndDate("未查到");
            }
            illegalQuery.setIsChangemess(isChangemess);

//            if(illegalQuery.getIllegalStatus() != illegalStatus || !oldSaicStatus.equals(saicStatus)) {  //校验结果变化时更新状态
                illegalQueryDao.save(illegalQuery);
//            }

            illegalQueryDto = ConverterService.convert(illegalQuery, IllegalQueryDto.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return illegalQueryDto;
    }

    @Override
    public TableResultResponse<IllegalQueryBatchDto> queryBatch(IllegalQueryBatchDto dto, Pageable pageable) {
        if (StringUtils.isBlank(dto.getBeginDate())) {
            dto.setBeginDate("00000000");
        }
        if(StringUtils.isBlank(dto.getEndDate())) {
            dto.setEndDate("99999999");
        }
        dto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        Page<IllegalQueryBatch> page = illegalQueryBatchDao.findAll(new IllegalQueryBatchSpec(dto), pageable);
        long count = illegalQueryBatchDao.count(new IllegalQueryBatchSpec(dto));
        List<IllegalQueryBatch> list = page.getContent();

        List<IllegalQueryBatchDto> listDto = ConverterService.convertToList(list, IllegalQueryBatchDto.class);

        return new TableResultResponse<IllegalQueryBatchDto>((int) count, listDto);
    }

    @Override
    public IExcelExport exportIllegalQueryExcel(IllegalQueryDto dto) {
        //先根据ID查询出该批次的详细信息
        List<IllegalQuery> illegalQueryList = illegalQueryDao.findAll(new IllegalQuerySpec(dto));
        List<IllegalQueryDto> illegalQueryDtoList = ConverterService.convertToList(illegalQueryList, IllegalQueryDto.class);

        IExcelExport illegalQueryExport = new IllegalQueryExport();
        List<IllegalQueryPoi> recordSaicPoiList = new ArrayList<IllegalQueryPoi>();


        for(IllegalQueryDto illegalQueryDto : illegalQueryDtoList){
            IllegalQueryPoi illegalQueryPoi = new IllegalQueryPoi();
            BeanUtils.copyProperties(illegalQueryDto,illegalQueryPoi);
            if(illegalQueryDto.getIllegalStatus() != null) {
                illegalQueryPoi.setIllegalStatus(illegalQueryDto.getIllegalStatus().getValue());
            } else {
                illegalQueryPoi.setIllegalStatus("");
            }
            if(illegalQueryDto.getChangemess() != null) {
                illegalQueryPoi.setChangemess(illegalQueryDto.getChangemess().getValue());
            } else {
                illegalQueryPoi.setChangemess("");
            }
            if(illegalQueryDto.getFileDueExpired() != null) {
                illegalQueryPoi.setFileDueExpired(illegalQueryDto.getFileDueExpired().getValue());
            } else {
                illegalQueryPoi.setFileDueExpired("");
            }
            recordSaicPoiList.add(illegalQueryPoi);
        }
        illegalQueryExport.setPoiList(recordSaicPoiList);
        return illegalQueryExport;
    }

    @Override
    public TableResultResponse<IllegalQueryDto> query(IllegalQueryDto dto, Pageable pageable) {
        Page<IllegalQuery> page = illegalQueryDao.findAll(new IllegalQuerySpec(dto), pageable);
        long count = illegalQueryDao.count(new IllegalQuerySpec(dto));
        List<IllegalQuery> list = page.getContent();

        List<IllegalQueryDto> listDto = ConverterService.convertToList(list, IllegalQueryDto.class);

        return new TableResultResponse<IllegalQueryDto>((int)count, listDto);
    }

    @Override
    public void saveIllegalQuery(List<IllegalQueryDto> illegalQueryDtoList, List<String> regNoList, Long batchId, List<Integer> strNum) {
        clearFuture();

        Map<String, List<IllegalQueryDto>> illegalQueryTokens = getIllegalQueryTokens(illegalQueryDtoList);
        if (MapUtils.isNotEmpty(illegalQueryTokens) && illegalQueryTokens.size() > 0) {
            IllegalQueryDtoExecutor executor = null;
            for (String batch : illegalQueryTokens.keySet()) {
                executor = new IllegalQueryDtoExecutor(illegalQueryTokens.get(batch), regNoList, strNum);

                executor.setIllegalQueryService(illegalQueryService);
                executor.setIllegalQueryErrorService(illegalQueryErrorService);
                executor.setIllegalQueryDao(illegalQueryDao);
                executor.setBatchId(batchId);
                executor.setTransactionManager(transactionManager);
                executor.setOrganizationService(organizationService);
                futureList.add(taskExecutor.submit(executor));
            }

            try {
                valiCollectCompleted();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.info("全部线程执行结束");
        System.gc();

    }

    @Override
    public Boolean checkIllegalStatus(Long batchId) {
        List<IllegalQuery> list = illegalQueryDao.findByIllegalQueryBatchIdAndAndIllegalStatusIsNull(batchId);

        if(CollectionUtils.isNotEmpty(list)) {
            return false;
        }

        return true;
    }

    @Override
    public long getIllegalQueryNum(Long batchId) {
        return illegalQueryDao.countByIllegalQueryBatchId(batchId);

    }

    @Override
    public Boolean checkIllegalExpired(Long batchId) {
        Boolean res = false;
        List<IllegalQueryDto> list = ConverterService.convertToList(illegalQueryDao.findByIllegalQueryBatchId(batchId),IllegalQueryDto.class);
        for (IllegalQueryDto illegalQueryDto : list ){
            if(illegalQueryDto.getFileDueExpired() == IllegalQueryExpiredStatus.EXPIRED){
                res = true;
                break;
            }
        }
        return res;
    }

    /**
     * 判断采集是否完成
     *
     * @param
     * @throws Exception
     */
    private void valiCollectCompleted() throws Exception {
        while(futureList.size()>0){
            for (Iterator<Future<Long>> iterator = futureList.iterator(); iterator.hasNext();) {
                Future<Long> future = iterator.next();
                if(future.isDone()){
                    iterator.remove();
                }
            }
            // 暂停1分钟
            TimeUnit.MILLISECONDS.sleep(10000);
        }
    }

    private Map<String, Set<IllegalQueryDto>> getBatchTokens(Set<IllegalQueryDto> tokens) {
        Map<String, Set<IllegalQueryDto>> returnMap = new HashMap<String, Set<IllegalQueryDto>>(16);
        if (tokens != null && tokens.size() > 0) {
            int allLeafSum = tokens.size();
            int tokensNum = (allLeafSum / illegalImportExecutorNum) + 1;
            int num = 0;
            int batchNum = 0;
            Set<IllegalQueryDto> batchTokens = new HashSet<IllegalQueryDto>();
            for (IllegalQueryDto illegalQueryDto : tokens) {
                if (num > 0 && num % tokensNum == 0) {
                    batchNum++;
                    returnMap.put("第" + batchNum + "线程", batchTokens);
                    batchTokens = new HashSet<IllegalQueryDto>();
                }
                batchTokens.add(illegalQueryDto);
                num++;
            }
            returnMap.put("第" + (batchNum + 1) + "线程", batchTokens);
        }
        return returnMap;
    }

    private Map<String, List<IllegalQueryDto>> getIllegalQueryTokens(List<IllegalQueryDto> tokens) {
        Map<String, List<IllegalQueryDto>> returnMap = new HashMap<>(16);
        if (tokens != null && tokens.size() > 0) {
            int allLeafSum = tokens.size();
            int tokensNum = (allLeafSum / illegalImportExecutorNum) + 1;
            int num = 0;
            int batchNum = 0;
            List<IllegalQueryDto> batchTokens = new ArrayList<>();
            for (IllegalQueryDto illegalQueryDto : tokens) {
                if (num > 0 && num % tokensNum == 0) {
                    batchNum++;
                    returnMap.put("第" + batchNum + "线程", batchTokens);
                    batchTokens = new ArrayList<>();
                }
                batchTokens.add(illegalQueryDto);
                num++;
            }
            returnMap.put("第" + (batchNum + 1) + "线程", batchTokens);
        }
        return returnMap;
    }

    private void clearFuture(){
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

}
