package com.ideatech.ams.compare.executor;

import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.processor.OnlineCollectionProcessor;
import com.ideatech.common.util.ApplicationContextUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * @Description 采集的控制层线程
 * @Author wanghongjie
 * @Date 2018/8/13
 **/
@Data
@Slf4j
public class CompareCollectControllerExecutor implements Runnable {

    private Long taskId;

    private DataSourceDto dataSourceDto;

    private OnlineCollectionProcessor onlineCollectionProcessor;

    @Override
    public void run() {
        try {
            onlineCollectionProcessor.onlineCollect(taskId,dataSourceDto);
        } catch (Exception e) {
            log.error("在线采集["+dataSourceDto.getDataType().getValue()+"]失败：",e);
        }
    }
}
