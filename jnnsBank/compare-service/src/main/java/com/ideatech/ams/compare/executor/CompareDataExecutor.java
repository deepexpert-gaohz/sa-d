package com.ideatech.ams.compare.executor;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.compare.dto.*;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultDetails;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultDetailsField;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.entity.DataSource;
import com.ideatech.ams.compare.service.DataSourceService;
import com.ideatech.ams.compare.spi.comparator.Comparator;
import com.ideatech.ams.system.blacklist.dto.BlackListEntryDto;
import com.ideatech.ams.system.blacklist.service.BlackListService;
import com.ideatech.common.util.BeanCopierUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 比对线程
 *
 * @author fantao
 */
@Slf4j
@Setter
public class CompareDataExecutor implements Runnable {
    protected TransactionUtils transactionUtils;

    protected BlackListService blackListService;

    protected DataSourceService dataSourceService;

    public CompareDataExecutor(CompareTaskDto taskDto, Map<String, Map<Long, CompareDataDto>> dataMaps, BlockingQueue<CompareResultDto> resultQueue, AtomicInteger compareNum) {
        this.compareTaskDto = taskDto;
        this.compareDatas = dataMaps;
        this.resultQueue = resultQueue;
        this.compareNum = compareNum;
    }

    /**
     * 保存比对结果的队列
     */
    private BlockingQueue<CompareResultDto> resultQueue;

    /**
     * 计数器
     */
    private AtomicInteger compareNum;

    /**
     * 本线程比对内容
     */
    private Map<String, Map<Long, CompareDataDto>> compareDatas;

    private CompareTaskDto compareTaskDto;

    private String taskName;

    /**
     * 比对规则涉及的字段
     */
    private List<CompareRuleFieldsDto> compareRuleFieldsDtos;

    /**
     * 比对规则涉及的数据源
     */
    private List<CompareRuleDataSourceDto> compareRuleDataSourceDtos;

    /**
     * 此任务比对规则涉及的字段详细规则
     */
    private List<CompareDefineDto> compareDefineDtos;

    /**
     * 字段对应的规则
     * K,字段
     * V,规则
     */
    private Map<Long, List<CompareDefineDto>> compareDefineMaps;

    /**
     * 比对器
     */
    private Map<String, Comparator> comparators;

    @Override
    public void run() {
        log.info("线程" + taskName + "开始进行比对......");
        try {
            if (compareTaskDto != null && compareTaskDto.getId() > 0) {
                doCompare();
            }
        } catch (Exception e) {
            log.error("线程" + taskName + "数据比对异常", e);
        }
        log.info("线程" + taskName + "比对结束......");
    }

    protected void doCompare() {
        if (compareDatas != null && compareDatas.size() > 0) {
            int processNum = 0;
            for (Map.Entry<String, Map<Long, CompareDataDto>> compareData : compareDatas.entrySet()) {
                String acctNo = compareData.getKey();
                if (MapUtils.isEmpty(compareData.getValue())) {
                    continue;
                }
                processNum++;
                if (processNum > 0 && processNum % 100 == 0) {
                    log.info("线程" + taskName + "已经比对" + processNum + "条账户");
                }
                try {
                    CompareResultDto compareResultDto = new CompareResultDto(compareTaskDto.getId(), acctNo);
                    //开始比对
                    CompareResultDetails resultDetails = compare(compareData.getValue());
                    getCompareResult(compareResultDto, compareTaskDto, compareData.getValue(), resultDetails);

                    //TODO 插入orgFullid
                    compareResultDto.setMatch(resultDetails.isMatch());
                    //除了比对以外的特殊处理resultDetails
                    postProcess(compareResultDto, compareData.getValue());
                    //插入比对结果到队列中，定时任务循环插入数据库
                    putCompareResult(compareResultDto);
                } catch (Exception e) {
                    log.error("账号" + acctNo + "比对异常", e);
                }
                //更新处理数
                putProcessedNum();
            }
        }
    }

    private void getCompareResult(CompareResultDto result, CompareTaskDto compareTaskDto, Map<Long, CompareDataDto> compareData, CompareResultDetails resultDetails) throws Exception {
        Boolean isCheckBlacklist = compareTaskDto.getIsCheckBlacklist() == null ? false : compareTaskDto.getIsCheckBlacklist();
        JSONObject json = new JSONObject(new LinkedHashMap<String, Object>());
//        result.setRowContent(resultDetails.toRowContent());
//        result.setColumnColor(StringUtils.replace(StringUtils.replace(resultDetails.toColumnColor(), "false", "false"), "true", "true"));
        result.setColumnColor(resultDetails.toColumnColor());

        json.put("match", resultDetails.toColumnColor());
        json.put("data", resultDetails.toRowDetail());
        result.setDetails(json.toJSONString());
        result.setAlone(getAloneDataSource(compareData));


        if(isCheckBlacklist) {
            String name = resultDetails.getName();
            StringBuffer isBlacklist = new StringBuffer();

            if (StringUtils.isNotBlank(name)) {
                String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(name, CompareResultDetails.COLUMN_TOKEN);
                for(String item : items) {
                    List<BlackListEntryDto> blacklist = blackListService.findByName(item);
                    if(CollectionUtils.isNotEmpty(blacklist)) {
                        isBlacklist.append("1" + CompareResultDetails.COLUMN_TOKEN);
                    } else {
                        isBlacklist.append("0" + CompareResultDetails.COLUMN_TOKEN);
                    }

                }

                result.setIsBlacklist(StringUtils.removeEnd(isBlacklist.toString(), CompareResultDetails.COLUMN_TOKEN));
            }
        }

    }

    /**
     * 获取比对数据的数据一致性，如果某个账号是某个数据源的单边数据，则将该账号的比对结果中的单边数据源属性设为该数据源。
     *
     * @param compareData
     * @return
     */
    private String getAloneDataSource(Map<Long, CompareDataDto> compareData) {
        DataSource aloneDataSource = new DataSource();
        boolean alone = true;
        for (Long dataSourceId : compareData.keySet()) {
            CompareDataDto data = compareData.get(dataSourceId);
            DataSourceDto dataSourceDto = dataSourceService.findById(dataSourceId);

            if (data != null && aloneDataSource == null && alone) {
                BeanCopierUtils.copyProperties(dataSourceDto, aloneDataSource);
            } else if (data != null && aloneDataSource != null) {
                aloneDataSource = null;
                alone = false;
            }
        }

        if (aloneDataSource != null) {
            return aloneDataSource.getName() + "单边";
        }
        return null;
    }

    /**
     * 获得比对数据的organFullId;
     *
     * @param compareTaskDto
     * @param compareData
     * @return
     */
    private String getFullOrganId(CompareTaskDto compareTaskDto, Map<Long, CompareDataDto> compareData) {
        String result = null;
        Set<Long> keys = compareData.keySet();

        for (Long dataSourceId : keys) {
            CompareDataDto data = compareData.get(dataSourceId);
            if (data != null) {
                result = data.getOrganFullId();
                if (StringUtils.isNotBlank(result)) {
                    break;
                }
            }
        }
        if (StringUtils.isBlank(result)) {
            result = compareTaskDto.getOrganFullId();
        }

        return result;
    }

    /**
     * 后置处理
     * 添加机构号
     * 添加企业名称
     * @param compareResultDto
     */
    private void postProcess(CompareResultDto compareResultDto, Map<Long, CompareDataDto> compareData) {
        //1.保存机构信息
        for (CompareRuleDataSourceDto compareRuleDataSourceDto : compareRuleDataSourceDtos) {
            CompareDataDto compareDataDto = compareData.get(compareRuleDataSourceDto.getDataSourceId());
            if (compareDataDto != null) {
                if(StringUtils.isNotBlank(compareDataDto.getOrganFullId())) {
                    compareResultDto.setOrganFullId(compareDataDto.getOrganFullId());
                }
                if (StringUtils.isNotBlank(compareDataDto.getDepositorName())) {
                    compareResultDto.setDepositorName(compareDataDto.getDepositorName());
                }
                if(StringUtils.isNotBlank(compareDataDto.getOrganFullId()) && StringUtils.isNotBlank(compareDataDto.getDepositorName())) {
                    break;
                }
            }

        }
    }

    /**
     * 按字段比对
     */
    private CompareResultDetails compare(Map<Long, CompareDataDto> compareData) throws Exception {
        CompareResultDetails resultDetails = new CompareResultDetails();

        for (CompareRuleDataSourceDto compareRuleDataSourceDto : compareRuleDataSourceDtos) {
            if (compareRuleDataSourceDto.getActive()) {
                resultDetails.addCategory(compareRuleDataSourceDto.getDataSourceDto().getName());
            }
        }

        for (CompareRuleFieldsDto compareRuleFieldsDto : compareRuleFieldsDtos) {
            Comparator comparator = comparators.get(compareRuleFieldsDto.getCompareFieldDto().getField() + Comparator.class.getSimpleName());
            if (comparator == null) {
                comparator = comparators.get("defaultComparator");
            }
            CompareResultDetailsField compareResultDetailsField =
                    comparator.compare(compareDefineMaps.get(compareRuleFieldsDto.getCompareFieldId()), compareRuleFieldsDto, compareData);
            resultDetails.addItem(compareResultDetailsField);
        }

        return resultDetails;
    }


    /**
     * 放入比对结果
     *
     * @param compareResultDto
     */
    private void putCompareResult(CompareResultDto compareResultDto) {
        try {
            resultQueue.put(compareResultDto);
        } catch (InterruptedException e) {
            log.error("比对结果插入队列异常", e);
        }

    }

    /**
     * 递增任务的处理数
     */
    private void putProcessedNum() {
        compareNum.getAndIncrement();
    }

}
