package com.ideatech.ams.compare.executor;

import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.compare.dao.CompareStatisticsDao;
import com.ideatech.ams.compare.dao.spec.CompareResultSpec;
import com.ideatech.ams.compare.dto.CompareResultDto;
import com.ideatech.ams.compare.dto.CompareStatisticsDto;
import com.ideatech.ams.compare.dto.CompareTaskDto;
import com.ideatech.ams.compare.entity.CompareStatistics;
import com.ideatech.ams.compare.entity.CompareTask;
import com.ideatech.ams.compare.service.CompareResultService;
import com.ideatech.ams.compare.service.CompareTaskService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.concurrent.Callable;

@Data
@Slf4j
public class CompareStatisticsExecutor implements Callable {

    private List<OrganizationDto> organList;

    private CompareTaskDto compareTask;

    private CompareStatisticsDao compareStatisticsDao;

    protected PlatformTransactionManager transactionManager;

    private CompareTaskService compareTaskService;

    private CompareResultService compareResultService;

    public CompareStatisticsExecutor(CompareTaskDto task, List<OrganizationDto> organList) {
        this.compareTask = task;
        this.organList = organList;
    }

    @Override
    public Object call() throws Exception {
        if(CollectionUtils.isNotEmpty(organList)) {
            for(OrganizationDto organ : organList) {
                try {
                    saveStatistics(compareTask.getId(), organ);
                } catch (Exception e) {
                    log.error("机构" + organ.getName() + "比对统计异常", e);
                }
            }
        }

        return System.currentTimeMillis();
    }

    private void saveStatistics(final Long taskId, final OrganizationDto organ) throws Exception {
        TransactionDefinition definition = new DefaultTransactionDefinition(
                TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);

        CompareStatisticsDto info = getCompareStatisticsInfo(taskId, organ.getFullId());
        CompareStatistics statistics = compareStatisticsDao.findByCompareTaskIdAndOrganId(taskId, organ.getId());

        if (statistics == null) {
            statistics = new CompareStatistics();
            statistics.setOrganId(organ.getId());
            statistics.setCompareTaskId(taskId);
        }

        try {
            statistics.setCount(info.getCount());
            statistics.setSuccess(info.getSuccess());
            compareStatisticsDao.save(statistics);

            transactionManager.commit(transaction);
            log.info("比对结果统计保存成功");
        } catch (Exception e) {
            log.error("比对结果统计保存异常", e);
            if(!transaction.isCompleted()){
                try {
                    transactionManager.rollback(transaction);
                }catch (Exception e1){
                    log.error("比对结果统计保存异常，回滚失败", e);
                }

            }
        }

    }

    private CompareStatisticsDto getCompareStatisticsInfo(Long taskId, String organFullId) {
        CompareResultDto condition = new CompareResultDto();
        condition.setCompareTaskId(taskId);
        condition.setOrganFullId(organFullId);

        long count = compareResultService.count(condition);
        condition.setMatch(true);
        long success = compareResultService.count(condition);
        return new CompareStatisticsDto(count, success);
    }

}
