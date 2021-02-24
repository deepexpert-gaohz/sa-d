package com.ideatech.ams.annual.executor;

import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.annual.service.AnnualResultService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 重新年检线程
 */
@Data
@Slf4j
public class AnnualAgainTaskExecutor implements Callable {

    private AnnualResultService annualResultService;

    private List<AnnualResultDto> AnnualResultDtoList;//需要重新年检的年检结果集合

    private String organName;//所属机构名称

    public AnnualAgainTaskExecutor(AnnualResultService annualResultService, List<AnnualResultDto> AnnualResultDtoList) {
        this.annualResultService = annualResultService;
        this.AnnualResultDtoList = AnnualResultDtoList;
    }

    @Override
    public Object call() throws Exception {
        log.info("机构:" + organName + ",重新年检开始");
        for (AnnualResultDto ard : AnnualResultDtoList) {
            try {
                annualResultService.annualAgain(ard);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("重新年检异常", e);
            }
        }
//        Thread.sleep(1000);
        log.info("机构:" + organName + ",重新年检结束");
        return System.currentTimeMillis();
    }
}
