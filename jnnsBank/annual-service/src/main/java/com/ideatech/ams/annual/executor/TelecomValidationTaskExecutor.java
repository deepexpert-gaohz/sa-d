package com.ideatech.ams.annual.executor;

import com.ideatech.ams.annual.dto.TelecomValidationDto;
import com.ideatech.ams.annual.service.TelecomValidationSerivce;
import com.ideatech.ams.kyc.dto.CarrierOperatorDto;
import com.ideatech.ams.kyc.service.CarrierOperatorService;
import com.ideatech.ams.system.batch.service.BatchService;
import com.ideatech.common.util.ApplicationContextUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@Data
@Slf4j
public class TelecomValidationTaskExecutor implements Runnable {
    private String batchNo;

    private TelecomValidationSerivce telecomValidationSerivce;

    private BatchService batchService;

    private CarrierOperatorService carrierOperatorService;


    public TelecomValidationTaskExecutor(String batchNo) {
        this.batchNo = batchNo;
        telecomValidationSerivce = ApplicationContextUtil.getBean(TelecomValidationSerivce.class);
        batchService = ApplicationContextUtil.getBean(BatchService.class);
        carrierOperatorService = ApplicationContextUtil.getBean(CarrierOperatorService.class);
    }

    @Override
    public void run() {
        // 遍历核心导入信息
        List<TelecomValidationDto> byBatchNo = telecomValidationSerivce.findByBatchNo(batchNo);

        // 查询运营商信息
        for (TelecomValidationDto telecomValidationDto : byBatchNo) {
            CarrierOperatorDto carrierOperatorDto = new CarrierOperatorDto();
            carrierOperatorDto.setName(telecomValidationDto.getName());
            carrierOperatorDto.setCardno(telecomValidationDto.getIdNo());
            carrierOperatorDto.setMobile(telecomValidationDto.getMobile());
            String reason = "";
            try {

                List<TelecomValidationDto> list  = telecomValidationSerivce.findSameDto(telecomValidationDto.getName(),telecomValidationDto.getIdNo(),telecomValidationDto.getMobile(),batchNo);
                if(CollectionUtils.isNotEmpty(list)){
                    for(TelecomValidationDto telecomValidationDto1 : list){
                        if(StringUtils.isNotBlank(telecomValidationDto1.getResult())){
                            telecomValidationDto.setResult(telecomValidationDto1.getResult());
                            telecomValidationSerivce.update(telecomValidationDto);
                            break;
                        }
                    }
                    if(StringUtils.isNotBlank(telecomValidationDto.getResult())){
                        continue;
                    }
                }

                CarrierOperatorDto carrierOperatorResult = carrierOperatorService.getCarrierOperatorResult(carrierOperatorDto);
                if(carrierOperatorResult != null){
                    if (StringUtils.equals("success", carrierOperatorResult.getStatus())) {
                        telecomValidationDto.setResult(carrierOperatorResult.getResult());
                    } else {
                        reason = carrierOperatorDto.getReason();
                        if (StringUtils.isBlank(reason)) {
                            reason = carrierOperatorResult.getResult();
                        }
                        telecomValidationDto.setResult("查询失败:" + reason);
                    }
                }else{
                    telecomValidationDto.setResult("查询失败");
                }
            } catch (Exception e) {
                log.error("批量校验运营商出错",e);
                if (StringUtils.isEmpty(telecomValidationDto.getResult())) {
                    telecomValidationDto.setResult("查询失败:" + reason);
//                    telecomValidationDto.setResult(e.getMessage()+e.toString());
                }
            }
            try {
                telecomValidationSerivce.update(telecomValidationDto);
            } catch (Exception e) {
                log.error("保存批量校验运营商结果出错", e);
            }
        }
        // 更新任务状态
        batchService.finishBatch(batchNo);
        log.info("电信运营商校验结束");
    }
}
