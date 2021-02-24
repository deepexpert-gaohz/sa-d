package com.ideatech.ams.compare.executor;

import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.processor.OnlineCollectionProcessor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 再次采集的控制层线程
 * @Author wanghongjie
 * @Date 2018/8/13
 **/
@Data
@Slf4j
public class CompareResetCollectControllerExecutor implements Runnable {

    private Long taskId;

    private DataSourceDto dataSourceDto;

    private OnlineCollectionProcessor onlineCollectionProcessor;

    @Override
    public void run() {
        try {
            onlineCollectionProcessor.resetOnlineCollect(taskId,dataSourceDto);
        } catch (Exception e) {
            log.error("再次在线采集["+dataSourceDto.getDataType().getValue()+"]失败：",e.getMessage());
        }
    }
}
