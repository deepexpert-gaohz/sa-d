package com.ideatech.ams.ws.api.restful;

import com.ideatech.ams.account.service.core.CompanyImportListener;
import com.ideatech.ams.ws.api.service.BillOperateLockApiService;
import com.ideatech.common.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bill")
@Slf4j
public class BillApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillApiController.class);

    @Autowired
    private BillOperateLockApiService billOperateLockApiService;

    @Autowired
    private CompanyImportListener preproccesser;

    @Value(("${import.file.pbcCoverCore:false}"))
    private boolean pbcCoverCore;

    /**
     * 上锁
     *
     * @param billId 流水id
     */
    @RequestMapping(value = "/billLock", method = RequestMethod.GET)
    public ResultDto billLock(Long billId) {
        return billOperateLockApiService.billLock(billId);
    }

    /**
     * 解锁
     *
     * @param billId 流水id
     */
    @RequestMapping(value = "/billUnLock", method = RequestMethod.GET)
    public ResultDto billUnLock(Long billId) {
        return billOperateLockApiService.billUnLock(billId);
    }

    @RequestMapping(value = "/pbcDataCover", method = RequestMethod.GET)
    public void pbdDataCover() {
        try{
            //除补全外：账户状态、法定代表人、法人证件类型、币种、资金转换BigDecimal、行业归属、邮政编码、账户名称构成方式、资金性质、上级法人证件类型等
            if (pbcCoverCore){
                log.info("======================核心流水更新（人行覆盖核心）继续。======================");
                Long startTime = System.currentTimeMillis();
                preproccesser.afterListener();
                Long endTime = System.currentTimeMillis();
                log.info("======================核心流水更新（人行覆盖核心）结束。======================");
                log.info("核心流水更新（人行覆盖核心）总耗时{}秒",(endTime - startTime) / 1000);
            }
        }catch (Exception e){
            log.error("核心流水更新（人行覆盖核心）异常。", e);
        }
    }

}
