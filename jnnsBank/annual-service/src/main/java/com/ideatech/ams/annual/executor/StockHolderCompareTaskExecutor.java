package com.ideatech.ams.annual.executor;

import com.ideatech.ams.annual.dto.CoreStockHolderDto;
import com.ideatech.ams.annual.dto.SaicStockHolderDto;
import com.ideatech.ams.annual.service.CoreStockHolderService;
import com.ideatech.ams.annual.service.SaicStockHolderService;
import com.ideatech.ams.customer.dto.SaicMonitorDto;
import com.ideatech.ams.customer.enums.SaicMonitorEnum;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.kyc.dto.StockHolderDto;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.system.batch.service.BatchService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.ApplicationContextUtil;
import com.ideatech.common.util.SecurityUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.client.ResourceAccessException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class StockHolderCompareTaskExecutor implements Runnable {
    private String batchNo;

    private CoreStockHolderService coreStockHolderService;

    private SaicStockHolderService saicStockHolderService;

    private BatchService batchService;

    private SaicInfoService saicInfoService;

    private SaicMonitorService saicMonitorService;

    private String userName;

    private Long organId;

    public StockHolderCompareTaskExecutor(String batchNo) {
        this.batchNo = batchNo;
    }

    @Override
    public void run() {
        // 遍历核心导入信息
        List<CoreStockHolderDto> byBatchNo = coreStockHolderService.findByBatchNo(batchNo);
        List<SaicStockHolderDto> saicStockHolderDtos = new ArrayList<>(byBatchNo.size());

        // 查询工商信息，比对
        for (CoreStockHolderDto coreStockHolderDto : byBatchNo) {
            String customerName = coreStockHolderDto.getCustomerName();
            SaicIdpInfo saicInfoFull = null;
            try {
                saicInfoFull = saicInfoService.getSaicInfoFull(SearchType.EXACT, userName, customerName, SecurityUtils.getCurrentOrgFullId());
            } catch (Exception e) {
                log.error(customerName + "数据查询异常，继续下一条数据查询......");
            }
            if (saicInfoFull != null) {

                SaicMonitorDto saicMonitorDto = new SaicMonitorDto();
                log.info(customerName + "控股股东查询工商数据保存进工商统计表......");
                saicMonitorDto = saicMonitorService.getSaicMonitor(userName,organId,customerName,saicInfoFull.getId(),StringUtils.isNotBlank(saicInfoFull.getUnitycreditcode()) ? saicInfoFull.getUnitycreditcode() : saicInfoFull.getRegistno(), SaicMonitorEnum.KYC);
                saicMonitorService.save(saicMonitorDto);

                log.info("企业" + saicInfoFull.getName() + "查询到数据");
                List<StockHolderDto> stockholders = saicInfoFull.getStockholders();
                //针对有出资比例的数据
                final Double[] stockHolderRatio = {null};
                StockHolderDto stockHolder = (StockHolderDto) CollectionUtils.find(stockholders, new Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        StockHolderDto stockHolderDto = (StockHolderDto) object;
                        if (StringUtils.isNotBlank(stockHolderDto.getFundedratio())) {
                            try {
                                log.info("转换...");
                                Number ratio = NumberFormat.getPercentInstance().parse(stockHolderDto.getFundedratio());
                                if (ratio.doubleValue() > 0.5D) {
                                    stockHolderRatio[0] = ratio.doubleValue();
                                    return true;
                                }
                            } catch (ParseException e) {
                                // ignore exception
                            }
                        }
                        return false;
                    }
                });

                if (stockHolder == null) {
                    //针对没有出资比例的数据, 根据出资额度计算
                    Double total = 0D;
                    for (StockHolderDto stockholder : stockholders) {
                        try {
                            if (StringUtils.isNotEmpty(stockHolder.getSubconam())) {
                                Double subconam = Double.parseDouble(stockholder.getSubconam());
                                total += subconam;
                            }
                        } catch (Exception e) {
                            // ignore exception
                        }
                    }
                    if (total != 0D) {
                        final Double finalTotal = total;
                        stockHolder = (StockHolderDto) CollectionUtils.find(stockholders, new Predicate() {
                            @Override
                            public boolean evaluate(Object object) {
                                StockHolderDto stockHolderDto = (StockHolderDto) object;
                                try {
                                    if (StringUtils.isNotEmpty(stockHolderDto.getSubconam())) {
                                        Double subconam = Double.parseDouble(stockHolderDto.getSubconam());
                                        if (subconam / finalTotal > 0.5D) {
                                            stockHolderRatio[0] = subconam / finalTotal;
                                            return true;
                                        }
                                    }
                                } catch (Exception e) {
                                    // ignore exception
                                }
                                return false;
                            }
                        });
                    }
                }

                SaicStockHolderDto saicStockHolderDto = ConverterService.convert(coreStockHolderDto, SaicStockHolderDto.class);
                saicStockHolderDto.setIsSame(null);
                saicStockHolderDto.setStockHolderName(null);
                saicStockHolderDto.setStockHolderRatio(null);
                saicStockHolderDto.setCoreStockHolderName(coreStockHolderDto.getStockHolderName());
                saicStockHolderDto.setCoreStockHolderRatio(coreStockHolderDto.getStockHolderRatio());
                saicStockHolderDto.setIsSame(Boolean.FALSE);
                if (stockHolder != null) {
                    saicStockHolderDto.setStockHolderName(stockHolder.getName());
                    saicStockHolderDto.setStockHolderRatio(stockHolderRatio[0]);
                    log.info("---------------" + coreStockHolderDto.getStockHolderName());
                    if (StringUtils.equals(coreStockHolderDto.getStockHolderName(), saicStockHolderDto.getStockHolderName()) && coreStockHolderDto.getStockHolderRatio() != null
                            && Math.abs(coreStockHolderDto.getStockHolderRatio() - saicStockHolderDto.getStockHolderRatio()) < 0.0001D) {
                        saicStockHolderDto.setIsSame(Boolean.TRUE);
                    }
                }

                saicStockHolderDtos.add(saicStockHolderDto);
            } else {

                SaicMonitorDto saicMonitorDto = new SaicMonitorDto();
                log.info(customerName + "控股股东查询工商数据为空保存进工商统计表......");
                saicMonitorDto = saicMonitorService.getSaicMonitor(userName,organId,customerName,null,"",SaicMonitorEnum.KYC);
                saicMonitorService.save(saicMonitorDto);

                //工商系统未查到企业 ,工商信息不保存
                log.info("企业" + coreStockHolderDto.getCustomerName() + "无数据");
                SaicStockHolderDto saicStockHolderDto = ConverterService.convert(coreStockHolderDto, SaicStockHolderDto.class);
                saicStockHolderDto.setIsSame(null);
                saicStockHolderDto.setStockHolderName(null);
                saicStockHolderDto.setStockHolderRatio(null);
                saicStockHolderDto.setCoreStockHolderName(coreStockHolderDto.getStockHolderName());
                saicStockHolderDto.setCoreStockHolderRatio(coreStockHolderDto.getStockHolderRatio());
                saicStockHolderDto.setIsSame(Boolean.FALSE);
                saicStockHolderDtos.add(saicStockHolderDto);
            }
        }
        // 保存结果
        saicStockHolderService.insert(saicStockHolderDtos);
        log.info("查询结束...");
        batchService.finishBatch(batchNo);
    }
}
