package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.AnnualResultDao;
import com.ideatech.ams.annual.dao.AnnualStatisticsDao;
import com.ideatech.ams.annual.dao.spec.AnnualResultSpec;
import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.annual.dto.AnnualStatisticsDto;
import com.ideatech.ams.annual.entity.AnnualStatistics;
import com.ideatech.ams.annual.enums.ResultStatusEnum;
import com.ideatech.ams.annual.spi.AnnualResultProcessor;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class AnnualStatisticsServiceImpl implements AnnualStatisticsService {

    @Autowired
    private AnnualStatisticsDao annualStatisticsDao;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AnnualResultDao annualResultDao;

    @Autowired
    private Map<String, AnnualResultProcessor> annualResultProcessors;

    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 添加更新年检统计数据
     * @param taskId
     */
    @Override
    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void saveStatistics(Long taskId) {
        AnnualStatisticsDto info = null;
        AnnualStatistics annualStatistics = null;

        TransactionDefinition definition = new DefaultTransactionDefinition(
                TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);

        //生成结果前先清空年检统计结果
        //annualStatisticsDao.deleteAll();
        annualStatisticsDao.deleteByTaskId(taskId);

        List<OrganizationDto> organizationDtos = organizationService.listAll();
        int i = 0;
        for(OrganizationDto organ : organizationDtos) {
            i++;
            info = getCompareStatisticsInfo(taskId, organ.getFullId());
            annualStatistics = new AnnualStatistics();
            annualStatistics.setOrganId(organ.getId());
            annualStatistics.setTaskId(taskId);
            annualStatistics.setCount(info.getCount());
            annualStatistics.setSuccess(info.getSuccess());
            annualStatisticsDao.save(annualStatistics);

            if(i >0 && i%20 ==0) {
                try {
                    log.info("已经保存年检统计数据" + i + "条");
                    transactionManager.commit(transaction);
                } catch (Exception e) {
                    if (!transaction.isCompleted()) {
                        transactionManager.rollback(transaction);
                    }
                    log.error("保存年检统计数据失败", e);
                } finally {
                    transaction = transactionManager.getTransaction(definition);
                }
            }
        }
        transactionManager.commit(transaction);
    }

    /**
     * 年检统计通过数量更新，包含父级机构
     * @param taskId
     * @param organCode
     */
    @Override
    public void updateStatisticsSuccess(Long taskId, String organCode, String type) {
        OrganizationDto organ = organizationService.findByCode(organCode);
        Long orgId = organ.getId();
        int organLevel = StringUtils.countMatches(organ.getFullId(), "-");

        for(int i = 0; i <= organLevel; i++) {
            if(i > 0) {
                organ = organizationService.findById(organ.getParentId());
                orgId = organ.getId();
            }
            AnnualStatistics annualStatistics = null;
            try {
                annualStatistics = annualStatisticsDao.findByTaskIdAndOrganId(taskId, orgId);
            } catch (Exception e) {
                log.error("更新年检统计结果时，机构id为{}的统计数据存在多条", orgId, e);
                continue;
            }
            if (annualStatistics == null) {
                log.error("更新年检统计结果时，未找到机构id为{}的统计数据", orgId);
                continue;
            }
            if("add".equalsIgnoreCase(type)) {
                annualStatistics.setSuccess(annualStatistics.getSuccess() + 1);
            } else {
                annualStatistics.setSuccess(annualStatistics.getSuccess() - 1);
            }
            annualStatisticsDao.save(annualStatistics);
        }
    }

    @Override
    public void subStatisticsCount(Long taskId, String orgCode) {
        log.info("开始批量删除机构号为" + orgCode + "年检结果统计数据");
        OrganizationDto organ = organizationService.findByCode(orgCode);
        Long orgId = organ.getId();
        int organLevel = StringUtils.countMatches(organ.getFullId(), "-");
        log.info("对应的完整机构号为" + organ.getFullId());

        for(int i = 0; i <= organLevel; i++) {
            if(i > 0) {
                organ = organizationService.findById(organ.getParentId());
                orgId = organ.getId();
            }
            AnnualStatistics annualStatistics = null;
            try {
                annualStatistics = annualStatisticsDao.findByTaskIdAndOrganId(taskId, orgId);
            } catch (Exception e) {
                log.error("更新年检统计结果时，机构id为{}的统计数据存在多条", orgId, e);
                continue;
            }
            log.info("更新前年检结果统计数量为" + annualStatistics.getCount());
            annualStatistics.setCount(annualStatistics.getCount() - 1);
            annualStatisticsDao.save(annualStatistics);
        }
    }

    /**
     * 计算年检统计数据
     * @param taskId
     * @param organFullId
     * @return
     */
    private AnnualStatisticsDto getCompareStatisticsInfo(Long taskId, String organFullId) {
        AnnualResultDto condition = new AnnualResultDto();
        condition.setOrganFullId(organFullId);
        condition.setTaskId(taskId);
        List<ResultStatusEnum> results = new ArrayList<>();
        results.add(ResultStatusEnum.PASS);
        results.add(ResultStatusEnum.FAIL);
        condition.setResults(results);

        //年检总数
        long count = annualResultDao.count(new AnnualResultSpec(condition));

        //年检成功的数量
        AnnualResultProcessor ard_success_arp = annualResultProcessors.get("systemSuccess" + "AnnualResultProcessor");
        long success = ard_success_arp.count(condition);

        //强制年检成功的数量
        AnnualResultProcessor ard_manualSuccess_arp = annualResultProcessors.get("manualSuccess" + "AnnualResultProcessor");
        long manualSuccess = ard_manualSuccess_arp.count(condition);

        return new AnnualStatisticsDto(count, success + manualSuccess);
    }

}
