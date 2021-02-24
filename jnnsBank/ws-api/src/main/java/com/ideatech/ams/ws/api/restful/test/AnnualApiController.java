package com.ideatech.ams.ws.api.restful.test;

import com.ideatech.ams.account.vo.AnnualAccountVo;
import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.annual.service.AnnualTaskService;
import com.ideatech.ams.annual.service.CoreCollectionService;
import com.ideatech.ams.ws.api.service.AnnualResultApiService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author jzh
 * @date 2019/3/21.
 */

@RestController
@RequestMapping(value = "/api/test/annualTask")
@Slf4j
public class AnnualApiController {

    @Autowired
    private CoreCollectionService coreCollectionService;

    @Autowired
    private AnnualTaskService annualTaskService;
    @Autowired
    private AnnualResultApiService annualResultApiService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private MyService myService;

    @RequestMapping("/core/start")
    public ResultDto coreStart() {

        Long taskId = annualTaskService.getAnnualCompareTaskId();
        MyCollectControllerExecutor collectControllerExecutor = new MyCollectControllerExecutor();
        collectControllerExecutor.setCoreCollectionService(coreCollectionService);
        collectControllerExecutor.setMyService(myService);
        collectControllerExecutor.setAnnualTaskId(taskId);
        taskExecutor.execute(collectControllerExecutor);

        return ResultDtoFactory.toAck("开始采集");

    }
    @RequestMapping("/annualResultSearch")
    public ResultDto testSearch(String acctNo) throws Exception {
        AnnualResultDto dto = annualResultApiService.annualResultSearch(acctNo);
        ResultDto resultDto = new ResultDto();
        if(dto!=null){
            resultDto.setCode("1");
            resultDto.setData(dto);
            resultDto.setMessage("查询成功");
        }else{
            resultDto.setCode("2");
            resultDto.setMessage("没有查询到数据");
        }
        return resultDto;
    }
    @RequestMapping("/testUpdate")
    public  ResultDto testUpdate(String acctNo) throws Exception {
        return annualResultApiService.updateAnnualResult(acctNo);
    }
    /**
     * 说明：返回List<AnnualAccountVo>数据的接口
     * 需要自己实现
     */
    @Service
    class MyService {
        public List<AnnualAccountVo> getList(){
            return null;
        }
    }

    /**
     * 说明：采集线程
     * 需要自己实现
     */

    @Data
    class MyCollectControllerExecutor implements Runnable {

        private CoreCollectionService coreCollectionService;

        private MyService myService;

        private Long annualTaskId;

        @Override
        public void run() {
            List<AnnualAccountVo> accountsAllList = myService.getList();
            coreCollectionService.collectOut(annualTaskId,accountsAllList);
        }
    }
}
