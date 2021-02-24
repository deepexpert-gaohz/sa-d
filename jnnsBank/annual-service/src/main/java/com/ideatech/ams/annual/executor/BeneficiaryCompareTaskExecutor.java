package com.ideatech.ams.annual.executor;

import com.ideatech.ams.annual.dto.CoreBeneficiaryDto;
import com.ideatech.ams.annual.dto.SaicBeneficiaryDto;
import com.ideatech.ams.annual.service.CoreBeneficiarySerivce;
import com.ideatech.ams.annual.service.SaicBeneficiarySerivce;
import com.ideatech.ams.customer.dto.SaicMonitorDto;
import com.ideatech.ams.customer.enums.SaicMonitorEnum;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.kyc.dto.BeneficiaryDto;
import com.ideatech.ams.kyc.dto.SaicInfoDto;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.service.BeneficiaryService;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.system.batch.service.BatchService;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.ApplicationContextUtil;
import com.ideatech.common.util.SecurityUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.client.ResourceAccessException;

import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class BeneficiaryCompareTaskExecutor implements Runnable {
    private String batchNo;

    private CoreBeneficiarySerivce coreBeneficiarySerivce;

    private SaicBeneficiarySerivce saicBeneficiarySerivce;

    private BatchService batchService;

    private SaicInfoService saicInfoService;

    private BeneficiaryService beneficiaryService;

    private SaicMonitorService saicMonitorService;

    private String userName;

    private Long organId;


    public BeneficiaryCompareTaskExecutor(String batchNo) {
        this.batchNo = batchNo;
        coreBeneficiarySerivce = ApplicationContextUtil.getBean(CoreBeneficiarySerivce.class);
        saicBeneficiarySerivce = ApplicationContextUtil.getBean(SaicBeneficiarySerivce.class);
        batchService = ApplicationContextUtil.getBean(BatchService.class);
        saicInfoService = ApplicationContextUtil.getBean(SaicInfoService.class);
        beneficiaryService = ApplicationContextUtil.getBean(BeneficiaryService.class);
        saicMonitorService = ApplicationContextUtil.getBean(SaicMonitorService.class);
    }

    @Override
    public void run() {
        // 遍历核心导入信息
        List<CoreBeneficiaryDto> byBatchNo = coreBeneficiarySerivce.findByBatchNo(batchNo);
        List<SaicBeneficiaryDto> saicBeneficiaryDtos = new ArrayList<>(byBatchNo.size());

        int num = 0;

        // 查询工商信息，比对
        for (CoreBeneficiaryDto coreBeneficiaryDto : byBatchNo) {

            num++;
            log.info("查询受益人集合数量第" + num + "条......");

            try {
                String customerName = coreBeneficiaryDto.getCustomerName();
                SaicInfoDto saicInfoBase = null;

                saicInfoBase = saicInfoService.getSaicInfoBase(SearchType.EXACT, SecurityUtils.getCurrentUsername(), customerName, SecurityUtils.getCurrentOrgFullId());

                SaicBeneficiaryDto saicBeneficiaryDto = new SaicBeneficiaryDto();
                saicBeneficiaryDto.setCustomerNo(coreBeneficiaryDto.getCustomerNo());
                saicBeneficiaryDto.setCustomerName(coreBeneficiaryDto.getCustomerName());
                saicBeneficiaryDto.setAcctNo(coreBeneficiaryDto.getAcctNo());
                saicBeneficiaryDto.setBatchNo(coreBeneficiaryDto.getBatchNo());
                saicBeneficiaryDto.setCoreBeneficiaryName1(coreBeneficiaryDto.getBeneficiaryName1());
                saicBeneficiaryDto.setCoreBeneficiaryRatio1(coreBeneficiaryDto.getBeneficiaryRatio1());
                saicBeneficiaryDto.setCoreBeneficiaryName2(coreBeneficiaryDto.getBeneficiaryName2());
                saicBeneficiaryDto.setCoreBeneficiaryRatio2(coreBeneficiaryDto.getBeneficiaryRatio2());
                saicBeneficiaryDto.setCoreBeneficiaryName3(coreBeneficiaryDto.getBeneficiaryName3());
                saicBeneficiaryDto.setCoreBeneficiaryRatio3(coreBeneficiaryDto.getBeneficiaryRatio3());
                saicBeneficiaryDto.setCoreBeneficiaryName4(coreBeneficiaryDto.getBeneficiaryName4());
                saicBeneficiaryDto.setCoreBeneficiaryRatio4(coreBeneficiaryDto.getBeneficiaryRatio4());
                saicBeneficiaryDto.setCoreLegalName(coreBeneficiaryDto.getLegalName());
                saicBeneficiaryDto.setIsSame(Boolean.FALSE);
                saicBeneficiaryDto.setOrganCode(coreBeneficiaryDto.getOrganCode());
                saicBeneficiaryDtos.add(saicBeneficiaryDto);

                if (saicInfoBase != null) {

                    SaicMonitorDto saicMonitorDto = new SaicMonitorDto();
                    log.info(coreBeneficiaryDto.getCustomerName() + "受益人查询工商数据保存进工商统计表......");
                    saicMonitorDto = saicMonitorService.getSaicMonitor(userName,organId,coreBeneficiaryDto.getCustomerName(),saicInfoBase.getId(),StringUtils.isNotBlank(saicInfoBase.getUnitycreditcode()) ? saicInfoBase.getUnitycreditcode() : saicInfoBase.getRegistno(), SaicMonitorEnum.KYC);
                    saicMonitorService.save(saicMonitorDto);

                    saicBeneficiaryDto.setLegalName(saicInfoBase.getLegalperson());
                    log.info("---------------" + saicInfoBase.getName());

                    List<BeneficiaryDto> beneficiaryDtos = beneficiaryService.getBeneficiaryListBySaicInfoId(SecurityUtils.getCurrentUsername(), saicInfoBase.getId(), SecurityUtils.getCurrentOrgFullId());

                    if (CollectionUtils.isEmpty(beneficiaryDtos)) {
                        log.info(saicInfoBase.getName() + "受益人集合为空，继续执行下一条=================");
                        continue;
                    }
                    // 开始筛选25%以上
                    CollectionUtils.filter(beneficiaryDtos, new Predicate() {
                        @Override
                        public boolean evaluate(Object object) {
                            BeneficiaryDto beneficiaryDto = (BeneficiaryDto) object;
                            try {
                                if (StringUtils.isNotEmpty(beneficiaryDto.getCapitalpercent())) {
                                    Double parse = Double.valueOf(beneficiaryDto.getCapitalpercent());
                                    if (parse.doubleValue() > 0.25) {
                                        return true;
                                    }
                                }
                            } catch (Exception e) {
                                // ignore exception
                            }
                            return false;
                        }
                    });

                    // 填充受益人信息
                    if (beneficiaryDtos.size() > 0) {
                        saicBeneficiaryDto.setBeneficiaryName1(beneficiaryDtos.get(0).getName());
                        this.swopBeneficiary(1, beneficiaryDtos.get(0).getName(), saicBeneficiaryDto);
                        try {
                            saicBeneficiaryDto.setBeneficiaryRatio1(Double.valueOf(beneficiaryDtos.get(0).getCapitalpercent()).doubleValue());
                        } catch (Exception e) {
                            // ignore
                        }

                    }
                    if (beneficiaryDtos.size() > 1) {
                        saicBeneficiaryDto.setBeneficiaryName2(beneficiaryDtos.get(1).getName());
                        this.swopBeneficiary(2, beneficiaryDtos.get(1).getName(), saicBeneficiaryDto);
                        try {
                            saicBeneficiaryDto.setBeneficiaryRatio2(Double.valueOf(beneficiaryDtos.get(1).getCapitalpercent()).doubleValue());
                        } catch (Exception e) {
                            // ignore
                        }

                    }
                    if (beneficiaryDtos.size() > 2) {
                        saicBeneficiaryDto.setBeneficiaryName3(beneficiaryDtos.get(2).getName());
                        this.swopBeneficiary(3, beneficiaryDtos.get(2).getName(), saicBeneficiaryDto);
                        try {
                            saicBeneficiaryDto.setBeneficiaryRatio3(Double.valueOf(beneficiaryDtos.get(2).getCapitalpercent()).doubleValue());
                        } catch (Exception e) {
                            // ignore
                        }

                    }
                    if (beneficiaryDtos.size() > 3) {
                        saicBeneficiaryDto.setBeneficiaryName1(beneficiaryDtos.get(3).getName());
                        this.swopBeneficiary(4, beneficiaryDtos.get(3).getName(), saicBeneficiaryDto);
                        try {
                            saicBeneficiaryDto.setBeneficiaryRatio4(Double.valueOf(beneficiaryDtos.get(3).getCapitalpercent()).doubleValue());
                        } catch (Exception e) {
                            // ignore
                        }

                    }

                    // 开始比对
                    Map<String, Double> coreBeneficiary = new HashMap<>();
                    Map<String, Double> saicBeneficiary = new HashMap<>();
                    if (StringUtils.isNotEmpty(coreBeneficiaryDto.getBeneficiaryName1())) {
                        coreBeneficiary.put(coreBeneficiaryDto.getBeneficiaryName1(), coreBeneficiaryDto.getBeneficiaryRatio1());
                    }
                    if (StringUtils.isNotEmpty(coreBeneficiaryDto.getBeneficiaryName2())) {
                        coreBeneficiary.put(coreBeneficiaryDto.getBeneficiaryName2(), coreBeneficiaryDto.getBeneficiaryRatio2());
                    }
                    if (StringUtils.isNotEmpty(coreBeneficiaryDto.getBeneficiaryName3())) {
                        coreBeneficiary.put(coreBeneficiaryDto.getBeneficiaryName3(), coreBeneficiaryDto.getBeneficiaryRatio3());
                    }
                    if (StringUtils.isNotEmpty(coreBeneficiaryDto.getBeneficiaryName4())) {
                        coreBeneficiary.put(coreBeneficiaryDto.getBeneficiaryName4(), coreBeneficiaryDto.getBeneficiaryRatio4());
                    }
                    if (StringUtils.isNotEmpty(saicBeneficiaryDto.getBeneficiaryName1())) {
                        saicBeneficiary.put(saicBeneficiaryDto.getBeneficiaryName1(), saicBeneficiaryDto.getBeneficiaryRatio1());
                    }
                    if (StringUtils.isNotEmpty(saicBeneficiaryDto.getBeneficiaryName2())) {
                        saicBeneficiary.put(saicBeneficiaryDto.getBeneficiaryName2(), saicBeneficiaryDto.getBeneficiaryRatio2());
                    }
                    if (StringUtils.isNotEmpty(saicBeneficiaryDto.getBeneficiaryName3())) {
                        saicBeneficiary.put(saicBeneficiaryDto.getBeneficiaryName3(), saicBeneficiaryDto.getBeneficiaryRatio3());
                    }
                    if (StringUtils.isNotEmpty(saicBeneficiaryDto.getBeneficiaryName4())) {
                        saicBeneficiary.put(saicBeneficiaryDto.getBeneficiaryName4(), saicBeneficiaryDto.getBeneficiaryRatio4());
                    }
                    if (coreBeneficiary.size() == saicBeneficiary.size()) {
                        for (BeneficiaryDto beneficiaryDto : beneficiaryDtos) {
                            Double corePercentage = coreBeneficiary.get(beneficiaryDto.getName());
                            if (corePercentage == null) {
                                break;
                            }
                            Double saicPercentage = saicBeneficiary.get(beneficiaryDto.getName());
                            if (saicPercentage == null) {
                                break;
                            }
                            if (Math.abs(saicPercentage.doubleValue() - corePercentage.doubleValue()) > 0.0001D) {
                                break;
                            }
                            coreBeneficiary.remove(beneficiaryDto.getName());
                        }
                        if (coreBeneficiary.isEmpty()) {
                            saicBeneficiaryDto.setIsSame(Boolean.TRUE);
                        }
                    }
                }else{
                    SaicMonitorDto saicMonitorDto = new SaicMonitorDto();
                    log.info(coreBeneficiaryDto.getCustomerName() + "受益人查询工商数据保存进工商统计表......");
                    saicMonitorDto = saicMonitorService.getSaicMonitor(userName,organId,coreBeneficiaryDto.getCustomerName(),null,"",SaicMonitorEnum.KYC);
                    saicMonitorService.save(saicMonitorDto);
                }
            } catch (Exception e) {
                log.error(coreBeneficiaryDto.getCustomerName() + "数据查询异常，继续下一条数据查询......", e);
            }
        }
        // 保存结果
        saicBeneficiarySerivce.insert(saicBeneficiaryDtos);
        log.info("受益人查询结束...");
        // 更新任务状态
        batchService.finishBatch(batchNo);
    }

    /**
     * 根据工商受益人的排列顺序，更改核心受益人的排列顺序
     *
     * @param suffix          工商受益人下标（beneficiaryName1中的1）
     * @param beneficiaryName 工商受益人名字
     * @param sbd             受益人对象
     */
    private void swopBeneficiary(int suffix, String beneficiaryName, SaicBeneficiaryDto sbd) {
        Class<?> cls = sbd.getClass();
        try {
            //工商受益人所在位置对应的核心受益人名称
            String coreBeneficiaryName_gs = (String) cls.getMethod("getCoreBeneficiaryName" + suffix).invoke(sbd);
            //工商受益人所在位置对应的核心受益人比例
            Double coreBeneficiaryRatio_gs = (Double) cls.getMethod("getCoreBeneficiaryRatio" + suffix).invoke(sbd);
            for (int i = 1; i <= 4; i++) {
                String coreBeneficiaryName = (String) cls.getMethod("getCoreBeneficiaryName" + i).invoke(sbd);
                Double coreBeneficiaryRatio = (Double) cls.getMethod("getCoreBeneficiaryRatio" + i).invoke(sbd);
                if (coreBeneficiaryName != null && coreBeneficiaryName.equals(beneficiaryName)) {
                    cls.getMethod("setCoreBeneficiaryName" + suffix, String.class).invoke(sbd, coreBeneficiaryName);
                    cls.getMethod("setCoreBeneficiaryRatio" + suffix, Double.class).invoke(sbd, coreBeneficiaryRatio);

                    cls.getMethod("setCoreBeneficiaryName" + i, String.class).invoke(sbd, coreBeneficiaryName_gs);
                    cls.getMethod("setCoreBeneficiaryRatio" + i, Double.class).invoke(sbd, coreBeneficiaryRatio_gs);
                    break;
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
