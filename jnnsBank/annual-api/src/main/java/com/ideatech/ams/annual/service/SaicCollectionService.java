package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.FetchSaicInfoDto;
import com.ideatech.ams.annual.dto.SaicCollectResultSearchDto;
import com.ideatech.ams.annual.enums.CollectType;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.List;

public interface SaicCollectionService {
    List<FetchSaicInfoDto> getAll(Long taskId);
    void collect(Long annualTaskId);

    /**
     * 采集
     * @param collectType
     * @param annualTaskId
     */
    void collect(CollectType collectType, Long annualTaskId);
    /**
     * 失败重新采集
     * @param collectType
     * @param annualTaskId
     */
    void collectReset(CollectType collectType,Long annualTaskId);

    /**
     * 文件导入采集
     * @param collectType
     * @param annualTaskId
     */
    void collectByFile(CollectType collectType,Long annualTaskId);
    /**
     * 判断工商采集任务的状态
     *
     * 0 - 核心导入未完成
     * 1 - 人行数据未解析完成
     * 2 - 工商采集已经开启
     * 3 - 可以导入文件采集
     * 4 - 可以在线采集
     * 5 - 未配置工商采集任务
     * 6 - 其他，不可做任何
     *
     * @return
     */
    int checkSaicCollectTaskStatus(Long taskId);
    /**
     * 年检企业列表excel
     * @param taskId
     * @return
     */
    IExcelExport generateAnnualCompanyReport(Long taskId);
    /**
     * 计算导入文件的数量
     * @return
     */
    Integer saicFileCounts();
    /**
     * 清空文件
     * @return
     */
    void clearFiles();

    SaicCollectResultSearchDto search(final SaicCollectResultSearchDto saicCollectResultSearchDto, final Long taskId);

    void clearFuture();

    void endFuture();
}
