package com.ideatech.ams.kyc.executor;

import com.ideatech.ams.customer.dto.SaicMonitorDto;
import com.ideatech.ams.customer.enums.SaicMonitorEnum;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.kyc.dto.KycSearchHistoryDto;
import com.ideatech.ams.kyc.dto.SaicInfoDto;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.service.SaicInfoService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Data
@Slf4j
public class SaicInfoQueryExecutor implements Callable {

    private SaicInfoService saicInfoService;

    private String batchNo;

    private String username;

    private String organFullId;

    private Long organId;

    private List<KycSearchHistoryDto> kycSearchHistoryDtoList;

    private PlatformTransactionManager transactionManager;

    private SaicMonitorService saicMonitorService;

    public SaicInfoQueryExecutor(List<KycSearchHistoryDto> kycSearchHistoryDtoList) {
        this.kycSearchHistoryDtoList = kycSearchHistoryDtoList;
    }

    @Override
    public Object call() throws Exception {
        return save(kycSearchHistoryDtoList, batchNo);

    }

    private List<Long> save(List<KycSearchHistoryDto> kycSearchHistoryDtoList, String batchNo) {
        List<Long> saicInfoList = new ArrayList<>();
        SaicMonitorDto saicMonitorDto = null;
        for(KycSearchHistoryDto kycSearchHistoryDto : kycSearchHistoryDtoList) {
            try {
                TransactionDefinition definition = new DefaultTransactionDefinition(
                        TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                TransactionStatus transaction = transactionManager.getTransaction(definition);

                SaicInfoDto saicInfoBase = saicInfoService.getSaicInfoBase(SearchType.REAL_TIME, username, URLDecoder.decode(kycSearchHistoryDto.getEntname(), "UTF-8"),
                        organFullId, batchNo);


                if(saicInfoBase != null && saicInfoBase.getId() != null) {
                    saicMonitorDto = saicMonitorService.getSaicMonitor(username,organId,kycSearchHistoryDto.getEntname(), saicInfoBase.getId(),StringUtils.isNotBlank(saicInfoBase.getUnitycreditcode()) ? saicInfoBase.getUnitycreditcode() : saicInfoBase.getRegistno(), SaicMonitorEnum.KYC);
                    saicMonitorService.save(saicMonitorDto);
                    saicInfoList.add(saicInfoBase.getId());
                }else{
                    saicMonitorDto = saicMonitorService.getSaicMonitor(username,organId,kycSearchHistoryDto.getEntname(), null,"", SaicMonitorEnum.KYC);
                    saicMonitorService.save(saicMonitorDto);
                }

                transactionManager.commit(transaction);
            } catch (UnsupportedEncodingException e) {
                log.error("企业名称解码异常");
                e.printStackTrace();
            }
        }

        return saicInfoList;
    }

}
