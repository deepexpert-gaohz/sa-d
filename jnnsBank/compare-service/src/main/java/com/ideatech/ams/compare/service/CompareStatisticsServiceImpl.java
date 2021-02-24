package com.ideatech.ams.compare.service;


import com.ideatech.ams.compare.dao.CompareStatisticsDao;
import com.ideatech.ams.compare.dto.CompareStatisticsDto;
import com.ideatech.ams.compare.dto.CompareTaskDto;
import com.ideatech.ams.compare.entity.CompareStatistics;
import com.ideatech.ams.compare.executor.CompareStatisticsExecutor;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.dto.TreeTable;
import com.ideatech.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class CompareStatisticsServiceImpl extends BaseServiceImpl<CompareStatisticsDao, CompareStatistics, CompareStatisticsDto> implements CompareStatisticsService {

    @Value("${ams.compare.statistics-executor-num:10}")
    protected int compareStatisticsExecutorNum;

    @Autowired
    private CompareTaskService compareTaskService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CompareStatisticsDao compareStatisticsDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ThreadPoolTaskExecutor compareExecutor;

    @Autowired
    private CompareResultService compareResultService;

    private List<Future<Long>> futureList;

    @Override
    public void statistics(Long taskId) {
        CompareStatisticsExecutor executor = null;
        CompareTaskDto task = compareTaskService.findById(taskId);

        clearFuture();
        log.info("开始统计比对任务" + task.getName());

        getBaseDao().deleteByCompareTaskId(taskId);
        OrganizationDto root = organizationService.findByOrganFullId(task.getOrganFullId());

//        List<OrganizationDto> organs = organizationService.findByOrganFullIdStartsWidth(root.getFullId());
        List<OrganizationDto> organs = organizationService.listAll();

        Map<String, List<OrganizationDto>> batchMap = getExecutBatch(organs);
        if(MapUtils.isNotEmpty(batchMap)) {
            Set<String> keys = batchMap.keySet();

            for (String key : keys) {
                executor = getExecutor(batchMap.get(key), task);
                futureList.add(compareExecutor.submit(executor));
            }

            try {
                valiCollectCompleted();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.info("比对任务" + task.getName() + "统计完成");
    }

    @Override
    public TreeTable query(Long pid, Long organId, Long taskId) {
        TreeTable result = new TreeTable();
        List<OrganizationDto> organChilds = null;

        if (pid == null) {
            OrganizationDto parent = organizationService.findById(organId);

            Map<String, Object> row = buildRow(parent, taskId);

            List<Map<String, Object>> rows = new ArrayList<>();
            organChilds = organizationService.searchChild(organId, "");

            for (OrganizationDto child : organChilds) {
                rows.add(buildRow(child, taskId));
            }
            row.put("rows", rows);

            result.getRows().add(row);
        } else {
            result.setPid(pid.toString());
            organChilds = organizationService.searchChild(pid, "");

            for (OrganizationDto childOrgan : organChilds) {
                result.getRows().add(buildRow(childOrgan, taskId));
            }
        }

        return result;
    }

    private Map<String,Object> buildRow(OrganizationDto organ, Long taskId) {
        double passRate;
        String passRateStr;
        Map<String, Object> row = new HashMap<>();
        row.put("id", organ.getId().toString());

        List<OrganizationDto> organChilds = organizationService.searchChild(organ.getId(), "");

        if (CollectionUtils.isNotEmpty(organChilds)) {
            row.put("hasRows", "1");
            row.put("name", organ.getName());
        } else {
            row.put("name", organ.getName());
        }
        double totalCount = 0;
        double passCount = 0;
        CompareStatistics result = getBaseDao().findByCompareTaskIdAndOrganId(taskId, organ.getId());
        if(result!=null){
            totalCount = result.getCount();
            passCount = result.getSuccess();
        }
        if(totalCount == 0) {
            passRateStr = 0 + "%";
        } else {
            passRate = (passCount/totalCount) * 100;
            passRateStr = String.format("%.2f", passRate) + "%";
        }

        row.put("totalCount", totalCount);
        row.put("passCount", passCount);
        row.put("passRate", passRateStr);

        return row;
    }

    /**
     * 生成线程
     * @param organList
     * @param task
     * @return
     */
    private CompareStatisticsExecutor getExecutor(List<OrganizationDto> organList, CompareTaskDto task) {
        CompareStatisticsExecutor executor = new CompareStatisticsExecutor(task, organList);

        executor.setCompareStatisticsDao(compareStatisticsDao);
        executor.setCompareTaskService(compareTaskService);
        executor.setTransactionManager(transactionManager);
        executor.setCompareResultService(compareResultService);

        return executor;
    }

    private Map<String,List<OrganizationDto>> getExecutBatch(List<OrganizationDto> organList) {
        Map<String, List<OrganizationDto>> returnMap = new HashMap<>();
        if (organList != null && organList.size() > 0) {
            int allOrganSum = organList.size();
            int organBatchNum = (allOrganSum / compareStatisticsExecutorNum) + 1;
            int num = 0;
            int batchNum = 0;
            List<OrganizationDto> batchOrganList = new ArrayList<>();
            for (OrganizationDto organ : organList) {
                if (num > 0 && num % organBatchNum == 0) {
                    batchNum++;
                    returnMap.put("第" + batchNum + "线程", batchOrganList);
                    batchOrganList = new ArrayList<>();
                }
                batchOrganList.add(organ);
                num++;
            }
            returnMap.put("第" + (batchNum + 1) + "线程", batchOrganList);
        }

        return returnMap;
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
            TimeUnit.MILLISECONDS.sleep(10000);
        }
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
