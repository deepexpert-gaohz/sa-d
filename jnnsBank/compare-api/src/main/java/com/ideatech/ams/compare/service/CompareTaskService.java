package com.ideatech.ams.compare.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.compare.dto.*;
import com.ideatech.ams.compare.enums.CompareState;
import com.ideatech.ams.compare.enums.TaskRate;
import com.ideatech.ams.compare.enums.TaskType;
import com.ideatech.ams.compare.vo.CompareFieldExcelRowVo;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CompareTaskService extends BaseService<CompareTaskDto> {

    /**
     * 保存
     * @param dto
     */
    void saveTask(CompareTaskDto dto);

    /**
     * 查询
     * @param dto
     * @return
     */
    CompareTaskSearchDto search(CompareTaskSearchDto dto,Pageable pageable);

    /**
     * 比对任务弹出框详情查询
     * @param taskId
     * @return
     */
    CompareTaskDto getDetails(Long taskId);

    /**
     * 比对任务弹出框比对规则查询
     * @param taskId
     * @return
     */
    CompareRuleDto getCompareRuleDetails(Long taskId);

    /**
     *
     * 比对任务弹出框数据源查询
     * @param taskId
     * @return
     */
    JSONArray getCompareDataSourceDetails(Long taskId);

    /**
     * 比对任务开始，此处基于所有数据都以准备完毕
     *
     * @param taskId
     */
    void start(Long taskId);

    /**
     * 修改比对状态
     * @param taskDto
     * @param compareState
     */
    void changeTaskStatus(CompareTaskDto taskDto, CompareState compareState);

    /**
     * 结束任务
     * @param taskDto
     * @param processedNum
     */
    void finishTask(CompareTaskDto taskDto, int processedNum);

    /**
     * 统计导入总数
     * @param taskId
     * @return
     */
    String getCompareTaskCount(Long taskId) throws Exception;

    /**
     * 导入数据
     *
     * @param taskId
     * @param dataSourceId
     * @param
     * @throws Exception
     */
    void importData(Long taskId, Long dataSourceId, MultipartFile file) throws Exception;

    /**
     * 比对工商采集excel导出
     * @param taskId
     * @return
     */
    IExcelExport exportSaicCompareData(Long taskId);

    /**
     * 导入明细表头查询
     * @param taskId
     * @return
     */
    JSONObject dataDetailColumns(Long taskId);

    /**
     * 导入详情查看
     * @param taskId
     * @param dataSourceId
     * @param pageable
     * @return
     */
    TableResultResponse<JSONObject> dataDetailList(Long taskId, Long dataSourceId, Pageable pageable);

    /**
     * 查询除工商外的数据源是否有导入数据
     * @param taskId
     * @return
     */
    ResultDto checkDataSourceImporter(Long taskId);

    /**
     * 导入明细结果查看
     * @param pageable
     * @return
     */
    TableResultResponse<CompareCollectRecordDto> importDetailList(CompareCollectRecordDto compareCollectRecordDto,Pageable pageable);

    /**
     *
     * @param taskType
     * @param state
     * @return
     */
    List<CompareTaskDto> findByTaskTypeAndStateIn(TaskType taskType, CompareState... state);

    /**
     * 根据taskId获得CompareRuleDataSourceDto
     * @param taskId
     */
    Map<Long, CompareRuleDataSourceDto> findCompareRuleDataSourceDtoMapByTaskId(Long taskId);

    /**
     *
     * @param taskId
     * @param dataSourceId
     * @return
     */
    List<DataSourceDto> findParentDataSourceDtoByTaskIdAndDataSourceId(Long taskId,Long dataSourceId);

    /**
     * 根据taskId获得CompareRuleDataSourceDto
     * @param taskId
     * @param count
     */
    void updateTaskCount(Long taskId, int count);


    /**
     * 根据规则找出哪些比对任务使用了
     * @param ruleId
     * @return
     */
    List<CompareTaskDto> findByCompareRuleId(Long ruleId);

    /**
     * 从application作用域中获取比对条数以及状态
     * @param taskId
     * @return
     */
    CompareTaskDto findByTaskIdFromApplication(Long taskId);


    /**
     * 比对线程重置
     * @param taskId
     * @return
     */
    ResultDto comapreReset(Long taskId);

    /**
     * 比对线程停止
     */
    ResultDto compareShutDown(Long taskId);


    /**
     * 比对任务比对之前检查数据源数据是否都已经导入成功
     * @param taskId
     * @return
     */
    ResultDto doCompareBefore(Long taskId);

    /**
     * 查找创建任务的用户跟登录用户进行比较
     * @param taskId
     * @return
     */
    ResultDto checkCompareTaskUser(Long taskId);

    /**
     * 删除比对任务
     * @param taskId
     */
    void deleteTask(Long taskId);

    /**
     * 根据比对导入的情况判断比对任务的任务状态
     * @param taskId
     */
    void updateCompareTaskState(Long taskId);

//    void dataCompareImport(List<CompareFieldExcelRowVo> dataList,CompareTaskDto compareTaskDto,CompareDataService compareDataService,DataSourceDto dataSourceDto,CompareCollectRecordService CompareCollectRecordService
//                            ,Map<String, OrganizationDto> orgMap,CompareCollectRecordService compareCollectRecordService,CompareCollectTaskDto compareCollectTaskDto);

    /**
     * 创建工商异常校验任务
     * @return 任务id
     */
    Long createSaicCheakTask(TaskType taskType, TaskRate taskRate ,String startTime);

    /**
     * 删除之前配置的定时任务（删除工商异常校验使用）
     */
    boolean deleteTimingTaskByNameLikeAndCreateName(String taskName,String createName);

    /**
     * 将工商返回的工商状态中文数据进行分类
     */
    String getSaicStateForState(String state);
}
