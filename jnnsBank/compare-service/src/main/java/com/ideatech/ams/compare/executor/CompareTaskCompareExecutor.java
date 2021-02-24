package com.ideatech.ams.compare.executor;

import com.ideatech.ams.compare.service.CompareTaskService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CompareTaskCompareExecutor implements Runnable {

    private CompareTaskService compareTaskService;

    private Long taskId;

    @Override
    public void run() {
        try {
            compareTaskService.start(taskId);
        }catch (Exception e) {
            log.error("比对任务出错......" + e.getMessage());
        }
    }
}
