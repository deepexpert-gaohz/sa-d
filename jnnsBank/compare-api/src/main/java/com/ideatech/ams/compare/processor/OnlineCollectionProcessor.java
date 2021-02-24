package com.ideatech.ams.compare.processor;

import com.ideatech.ams.compare.dto.CompareCollectTaskDto;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.enums.DataSourceEnum;
import com.ideatech.ams.compare.vo.CompareCollectRecordVo;
import org.springframework.transaction.annotation.Propagation;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Description 比对管理--在线采集接口
 * @Author wanghongjie
 * @Date 2019/2/11
 **/
public interface OnlineCollectionProcessor {
    /**
     *  采集初始化
     */
    void initCollect(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto) throws Exception;

    /**
     * 再次采集初始化
     * @param compareTaskId
     * @param dataSourceDto
     * @param compareCollectTaskDto
     * @throws Exception
     */
    void initResetCollect(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto) throws Exception;
    /**
     * 采集进行
     */
    void processCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception;

    /**
     * 再次采集进行
     * @param compareTaskId
     * @param dataSourceDto
     * @param compareCollectTaskDto
     * @throws Exception
     */
    void processResetCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception;

    /**
     * 采集结束
     */
    void finishCollect(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto) throws Exception ;
    /**
     * 清理线程
     */
    void clearFuture();

    /**
     * 判断线程是否结束
     * @throws Exception
     */
    void valiCollectCompleted() throws Exception;

    /**
     * 创建采集任务
     * @param compareTaskId
     * @param dataSourceDto
     */
    void createNewCollectTask(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto) throws Exception;

    /**
     * 再次采集任务
     * @param compareTaskId
     * @param dataSourceDto
     * @param compareCollectTaskDto
     * @throws Exception
     */
    void resetNewCollectTask(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto) throws Exception;

    /**
     * 判断任务是否能开启
     * @param compareTaskId
     * @param dataSourceEnum
     * @return
     */
    boolean checkCollectTask(Long compareTaskId, DataSourceDto dataSourceEnum) throws Exception ;

    /**
     * 判断任务是否能开启--再次采集
     * @param compareTaskId
     * @param dataSourceEnum
     * @return
     */
    boolean checkResetCollectTask(Long compareTaskId, DataSourceDto dataSourceEnum) throws Exception ;

    /**
     * 采集步骤
     * @param compareTaskId
     * @param dataSourceDto
     */
    void onlineCollect(Long compareTaskId, DataSourceDto dataSourceDto) throws Exception;

    /**
     * 再次采集
     * @param compareTaskId
     * @param dataSourceDto
     * @throws Exception
     */
    void resetOnlineCollect(Long compareTaskId, DataSourceDto dataSourceDto) throws Exception;
    /**
     * 保存成功记录
     * @param compareData
     * @param collectTaskId
     * @throws Exception
     */
    void saveSuccessedRecord(final CompareDataDto compareData, final Long collectTaskId,final DataSourceDto dataSourceDto, Propagation propagation) throws Exception;

    /**
     * 保存成功记录
     * @param compareData
     * @param collectTaskId
     * @param errorMsg
     * @throws Exception
     */
    void saveFailedRecord(final CompareDataDto compareData,final Long collectTaskId,final String errorMsg,final DataSourceDto dataSourceDto, Propagation propagation) throws Exception;
    /**
     * 根据DataSource保存CompareData
     * @param compareData
     * @param dataSourceDto
     * @throws Exception
     */
    void saveCompareData(final CompareDataDto compareData,final DataSourceDto dataSourceDto, Propagation propagation) throws Exception;

    /**
     * 成功记录操作
     * @param compareData
     * @param collectTaskId
     * @param dataSourceDto
     */
    void saveSuccessCombine(final CompareDataDto compareData, final Long collectTaskId, final DataSourceDto dataSourceDto, Propagation propagation) throws Exception;

}
