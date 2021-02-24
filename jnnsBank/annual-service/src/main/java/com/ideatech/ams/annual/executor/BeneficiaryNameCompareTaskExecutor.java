package com.ideatech.ams.annual.executor;

import com.ideatech.ams.annual.dto.CoreBeneficiaryDto;
import com.ideatech.ams.annual.dto.SaicBeneficiaryDto;
import com.ideatech.ams.annual.service.CoreBeneficiarySerivce;
import com.ideatech.ams.annual.service.SaicBeneficiarySerivce;
import com.ideatech.ams.kyc.dto.BeneficiaryDto;
import com.ideatech.ams.kyc.dto.OutBeneficiaryDto;
import com.ideatech.ams.kyc.dto.SaicInfoDto;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.service.BeneficiaryService;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.system.batch.service.BatchService;
import com.ideatech.common.util.ApplicationContextUtil;
import com.ideatech.common.util.SecurityUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.converters.StringArrayConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class BeneficiaryNameCompareTaskExecutor implements Runnable {
    private String batchNo;

    private CoreBeneficiarySerivce coreBeneficiarySerivce;

    private SaicBeneficiarySerivce saicBeneficiarySerivce;

    private BatchService batchService;

    private SaicInfoService saicInfoService;

    private BeneficiaryService beneficiaryService;

    private Boolean flag;

    int count = 1;

    private  List<SaicBeneficiaryDto> failSaicBeneficiaryDtos = new ArrayList<>();

    public BeneficiaryNameCompareTaskExecutor(String batchNo) {
        this.batchNo = batchNo;
        coreBeneficiarySerivce = ApplicationContextUtil.getBean(CoreBeneficiarySerivce.class);
        saicBeneficiarySerivce = ApplicationContextUtil.getBean(SaicBeneficiarySerivce.class);
        batchService = ApplicationContextUtil.getBean(BatchService.class);
//        saicInfoService = ApplicationContextUtil.getBean(SaicInfoService.class);
        beneficiaryService = ApplicationContextUtil.getBean(BeneficiaryService.class);
        this.flag = true;
    }

    public BeneficiaryNameCompareTaskExecutor(String batchNo ,Boolean flag) {
        this.batchNo = batchNo;
        coreBeneficiarySerivce = ApplicationContextUtil.getBean(CoreBeneficiarySerivce.class);
        saicBeneficiarySerivce = ApplicationContextUtil.getBean(SaicBeneficiarySerivce.class);
        batchService = ApplicationContextUtil.getBean(BatchService.class);
//        saicInfoService = ApplicationContextUtil.getBean(SaicInfoService.class);
        beneficiaryService = ApplicationContextUtil.getBean(BeneficiaryService.class);
        this.flag=flag;
    }

    @Override
    public void run() {
        // 遍历核心导入信息
        List<CoreBeneficiaryDto> byBatchNo = coreBeneficiarySerivce.findByBatchNo(batchNo);
        List<SaicBeneficiaryDto> saicBeneficiaryDtos = new ArrayList<>(byBatchNo.size());
//        List<SaicBeneficiaryDto> failSaicBeneficiaryDtos = new ArrayList<>(byBatchNo.size());
        log.info("查询受益人集合总数：" + byBatchNo.size() + "条。");

        int num = 0;

        // 查询工商信息，比对
        for (CoreBeneficiaryDto coreBeneficiaryDto : byBatchNo) {

            num++;
            log.info("查询受益人集合数量第" + num + "条......");

            try {
                String customerName = coreBeneficiaryDto.getCustomerName();
                SaicBeneficiaryDto saicBeneficiaryDto = new SaicBeneficiaryDto();
                saicBeneficiaryDto.setCustomerNo(coreBeneficiaryDto.getCustomerNo());
                saicBeneficiaryDto.setCustomerName(coreBeneficiaryDto.getCustomerName());
                saicBeneficiaryDto.setAcctNo(coreBeneficiaryDto.getAcctNo());
                saicBeneficiaryDto.setBatchNo(coreBeneficiaryDto.getBatchNo());
                saicBeneficiaryDto.setCoreBeneficiaryName1(coreBeneficiaryDto.getBeneficiaryName1());
                saicBeneficiaryDto.setCoreBeneficiaryName2(coreBeneficiaryDto.getBeneficiaryName2());
                saicBeneficiaryDto.setCoreBeneficiaryName3(coreBeneficiaryDto.getBeneficiaryName3());
                saicBeneficiaryDto.setCoreBeneficiaryName4(coreBeneficiaryDto.getBeneficiaryName4());
                saicBeneficiaryDto.setCoreLegalName(coreBeneficiaryDto.getLegalName());
                saicBeneficiaryDto.setIsSame(Boolean.TRUE);
                saicBeneficiaryDto.setOrganCode(coreBeneficiaryDto.getOrganCode());
                saicBeneficiaryDtos.add(saicBeneficiaryDto);
                toCompare(saicBeneficiaryDto);
//                SaicInfoDto saicInfoBase = null;
//                List<BeneficiaryDto> beneficiaryDtos = null;
//                OutBeneficiaryDto outBeneficiaryDto = beneficiaryService.getOutBeneficiaryDtoBySaicInfoId(SecurityUtils.getCurrentUsername(), SecurityUtils.getCurrentOrgFullId(),customerName);
//                //reason没有值说明能够查询到
//                if(outBeneficiaryDto != null && StringUtils.isEmpty(outBeneficiaryDto.getReason())){
//                    //集合有值说明能查询到受益人信息
//                    if(CollectionUtils.isNotEmpty(outBeneficiaryDto.getFinalBeneficiary())){
//                        beneficiaryDtos = outBeneficiaryDto.getFinalBeneficiary();
//                    }
//                }
//
//
//                if (CollectionUtils.isNotEmpty(beneficiaryDtos)) {
//                    //受益人查询到之后查询工商信息
//                    saicInfoBase = saicInfoService.getSaicInfoBase(SearchType.EXACT, SecurityUtils.getCurrentUsername(), customerName, SecurityUtils.getCurrentOrgFullId());
//                    saicBeneficiaryDto.setLegalName(saicInfoBase.getLegalperson());
//                    log.info("---------------" + saicInfoBase.getName());
//                    //插入受益人数据
//                    beneficiaryService.insertBeneficiary(saicInfoBase.getId(),beneficiaryDtos);
//
//                    if (flag){
//                        // 开始筛选25%以上
//                        CollectionUtils.filter(beneficiaryDtos, new Predicate() {
//                            @Override
//                            public boolean evaluate(Object object) {
//                                BeneficiaryDto beneficiaryDto = (BeneficiaryDto) object;
//                                try {
//                                    if (StringUtils.isNotEmpty(beneficiaryDto.getCapitalpercent())) {
//                                        Double parse = Double.valueOf(beneficiaryDto.getCapitalpercent());
//                                        if (parse.doubleValue() > 0.25) {
//                                            return true;
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    // ignore exception
//                                }
//                                return false;
//                            }
//                        });
//                    }
//
//                    // 填充受益人信息
//                    if (beneficiaryDtos.size() > 0) {
//                        saicBeneficiaryDto.setBeneficiaryName1(beneficiaryDtos.get(0).getName());
//                        this.swopBeneficiary(1, beneficiaryDtos.get(0).getName(), saicBeneficiaryDto);
//
//                    }
//                    if (beneficiaryDtos.size() > 1) {
//                        saicBeneficiaryDto.setBeneficiaryName2(beneficiaryDtos.get(1).getName());
//                        this.swopBeneficiary(2, beneficiaryDtos.get(1).getName(), saicBeneficiaryDto);
//
//                    }
//                    if (beneficiaryDtos.size() > 2) {
//                        saicBeneficiaryDto.setBeneficiaryName3(beneficiaryDtos.get(2).getName());
//                        this.swopBeneficiary(3, beneficiaryDtos.get(2).getName(), saicBeneficiaryDto);
//
//                    }
//                    if (beneficiaryDtos.size() > 3) {
//                        saicBeneficiaryDto.setBeneficiaryName4(beneficiaryDtos.get(3).getName());
//                        this.swopBeneficiary(4, beneficiaryDtos.get(3).getName(), saicBeneficiaryDto);
//                    }
//
//                    // 开始比对
//                    List<String> coreBeneficiary = new ArrayList<>();
//                    List<String> saicBeneficiary = new ArrayList<>();
//                    if (StringUtils.isNotEmpty(coreBeneficiaryDto.getBeneficiaryName1())) {
//                        coreBeneficiary.add(coreBeneficiaryDto.getBeneficiaryName1());
//                    }
//                    if (StringUtils.isNotEmpty(coreBeneficiaryDto.getBeneficiaryName2())) {
//                        coreBeneficiary.add(coreBeneficiaryDto.getBeneficiaryName2());
//                    }
//                    if (StringUtils.isNotEmpty(coreBeneficiaryDto.getBeneficiaryName3())) {
//                        coreBeneficiary.add(coreBeneficiaryDto.getBeneficiaryName3());
//                    }
//                    if (StringUtils.isNotEmpty(coreBeneficiaryDto.getBeneficiaryName4())) {
//                        coreBeneficiary.add(coreBeneficiaryDto.getBeneficiaryName4());
//                    }
//                    if (StringUtils.isNotEmpty(saicBeneficiaryDto.getBeneficiaryName1())) {
//                        saicBeneficiary.add(saicBeneficiaryDto.getBeneficiaryName1());
//                    }
//                    if (StringUtils.isNotEmpty(saicBeneficiaryDto.getBeneficiaryName2())) {
//                        saicBeneficiary.add(saicBeneficiaryDto.getBeneficiaryName2());
//                    }
//                    if (StringUtils.isNotEmpty(saicBeneficiaryDto.getBeneficiaryName3())) {
//                        saicBeneficiary.add(saicBeneficiaryDto.getBeneficiaryName3());
//                    }
//                    if (StringUtils.isNotEmpty(saicBeneficiaryDto.getBeneficiaryName4())) {
//                        saicBeneficiary.add(saicBeneficiaryDto.getBeneficiaryName4());
//                    }
//                    if (coreBeneficiary.size() == saicBeneficiary.size()) {
//                        for(String coreName : coreBeneficiary){
//                            boolean res = false;
//                            for(String saicName : saicBeneficiary){
//                                if(coreName.equals(saicName)){
//                                    res = true;
//                                    break;
//                                }
//                            }
//                            if(!res){
//                                saicBeneficiaryDto.setIsSame(Boolean.FALSE);
//                                break;
//                            }
//                        }
//                    }else{
//                        saicBeneficiaryDto.setIsSame(Boolean.FALSE);
//                    }
//
//                }else{
//                    if(StringUtils.isNotBlank(outBeneficiaryDto.getReason())){
//                        log.info(customerName + "受益人正在计算，继续执行下一条=================");
//                        saicBeneficiaryDto.setQueryStatus(Boolean.FALSE);
//                        failSaicBeneficiaryDtos.add(saicBeneficiaryDto);
//                    }else{
//                        saicBeneficiaryDto.setQueryStatus(Boolean.TRUE);
//                    }
//                    saicBeneficiaryDto.setIsSame(Boolean.FALSE);
//                }
            } catch (Exception e) {
                log.error(coreBeneficiaryDto.getCustomerName() + "数据查询异常，继续下一条数据查询......", e);
            }
        }

        if(CollectionUtils.isNotEmpty(failSaicBeneficiaryDtos)){
            toCompareFialList(failSaicBeneficiaryDtos);
        }
        // 保存结果
        saicBeneficiarySerivce.insert(saicBeneficiaryDtos);
        log.info("受益人查询结束...");
        // 更新任务状态
        batchService.finishBatch(batchNo);
    }

    public void toCompareFialList(List<SaicBeneficiaryDto> failDtos){
        try {
            log.info("1分钟后重新采集...");
            Thread.sleep(1000 * 60 * 1);
            log.info("第" + count + "次重新查询正在计算的受益人数据...");
            log.info("重新采集数量为{}",failDtos.size());
            List<SaicBeneficiaryDto> failList = new ArrayList<>();
            for(SaicBeneficiaryDto s : failDtos){
                failList.add(s);
            }
            failSaicBeneficiaryDtos.clear();
            if(CollectionUtils.isNotEmpty(failList)){
                for(SaicBeneficiaryDto saicBeneficiaryDto : failList){
                    toCompare(saicBeneficiaryDto);
                }
                if(CollectionUtils.isNotEmpty(failSaicBeneficiaryDtos)){
                    count++;
                    toCompareFialList(failSaicBeneficiaryDtos);
                }
            }
        } catch (InterruptedException e) {
            log.error("数据查询异常......", e);
        }
    }

    private void toCompare(SaicBeneficiaryDto saicBeneficiaryDto){
        try{
            SaicInfoDto saicInfoBase = null;
            List<BeneficiaryDto> beneficiaryDtos = null;
            OutBeneficiaryDto outBeneficiaryDto = null;
            //受益人列表为空  查询IDP接口
            if(CollectionUtils.isEmpty(saicBeneficiaryDto.getBeneficiaryDtos())){
                outBeneficiaryDto = beneficiaryService.getOutBeneficiaryDtoBySaicInfoId(SecurityUtils.getCurrentUsername(), SecurityUtils.getCurrentOrgFullId(),saicBeneficiaryDto.getCustomerName());
                //reason没有值说明能够查询到
                if(outBeneficiaryDto != null && StringUtils.isEmpty(outBeneficiaryDto.getReason())){
                    //集合有值说明能查询到受益人信息
                    if(CollectionUtils.isNotEmpty(outBeneficiaryDto.getFinalBeneficiary())){
                        beneficiaryDtos = outBeneficiaryDto.getFinalBeneficiary();
                        saicBeneficiaryDto.setBeneficiaryDtos(beneficiaryDtos);
                    }
                }
            }else{
                beneficiaryDtos = saicBeneficiaryDto.getBeneficiaryDtos();
            }

            if (CollectionUtils.isNotEmpty(beneficiaryDtos)) {

                if (flag){
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
                }

                // 填充受益人信息
                if (beneficiaryDtos.size() > 0) {
                    saicBeneficiaryDto.setBeneficiaryName1(beneficiaryDtos.get(0).getName());
                    this.swopBeneficiary(1, beneficiaryDtos.get(0).getName(), saicBeneficiaryDto);

                }
                if (beneficiaryDtos.size() > 1) {
                    saicBeneficiaryDto.setBeneficiaryName2(beneficiaryDtos.get(1).getName());
                    this.swopBeneficiary(2, beneficiaryDtos.get(1).getName(), saicBeneficiaryDto);

                }
                if (beneficiaryDtos.size() > 2) {
                    saicBeneficiaryDto.setBeneficiaryName3(beneficiaryDtos.get(2).getName());
                    this.swopBeneficiary(3, beneficiaryDtos.get(2).getName(), saicBeneficiaryDto);

                }
                if (beneficiaryDtos.size() > 3) {
                    saicBeneficiaryDto.setBeneficiaryName4(beneficiaryDtos.get(3).getName());
                    this.swopBeneficiary(4, beneficiaryDtos.get(3).getName(), saicBeneficiaryDto);
                }

                // 开始比对
                List<String> coreBeneficiary = new ArrayList<>();
                List<String> saicBeneficiary = new ArrayList<>();
                if (StringUtils.isNotEmpty(saicBeneficiaryDto.getCoreBeneficiaryName1())) {
                    coreBeneficiary.add(saicBeneficiaryDto.getCoreBeneficiaryName1());
                }
                if (StringUtils.isNotEmpty(saicBeneficiaryDto.getCoreBeneficiaryName2())) {
                    coreBeneficiary.add(saicBeneficiaryDto.getCoreBeneficiaryName2());
                }
                if (StringUtils.isNotEmpty(saicBeneficiaryDto.getCoreBeneficiaryName3())) {
                    coreBeneficiary.add(saicBeneficiaryDto.getCoreBeneficiaryName3());
                }
                if (StringUtils.isNotEmpty(saicBeneficiaryDto.getCoreBeneficiaryName4())) {
                    coreBeneficiary.add(saicBeneficiaryDto.getCoreBeneficiaryName4());
                }
                if (StringUtils.isNotEmpty(saicBeneficiaryDto.getBeneficiaryName1())) {
                    saicBeneficiary.add(saicBeneficiaryDto.getBeneficiaryName1());
                }
                if (StringUtils.isNotEmpty(saicBeneficiaryDto.getBeneficiaryName2())) {
                    saicBeneficiary.add(saicBeneficiaryDto.getBeneficiaryName2());
                }
                if (StringUtils.isNotEmpty(saicBeneficiaryDto.getBeneficiaryName3())) {
                    saicBeneficiary.add(saicBeneficiaryDto.getBeneficiaryName3());
                }
                if (StringUtils.isNotEmpty(saicBeneficiaryDto.getBeneficiaryName4())) {
                    saicBeneficiary.add(saicBeneficiaryDto.getBeneficiaryName4());
                }
                if (coreBeneficiary.size() == saicBeneficiary.size()) {
                    for(String coreName : coreBeneficiary){
                        boolean res = false;
                        for(String saicName : saicBeneficiary){
                            if(coreName.equals(saicName)){
                                res = true;
                                break;
                            }
                        }
                        if(!res){
                            saicBeneficiaryDto.setIsSame(Boolean.FALSE);
                            break;
                        }
                    }
                }else{
                    saicBeneficiaryDto.setIsSame(Boolean.FALSE);
                }

                //受益人查询到之后查询工商信息
                saicInfoBase = saicInfoService.getSaicInfoBase(SearchType.EXACT, SecurityUtils.getCurrentUsername(), saicBeneficiaryDto.getCustomerName(), SecurityUtils.getCurrentOrgFullId());
                if(saicInfoBase != null){
                    saicBeneficiaryDto.setLegalName(saicInfoBase.getLegalperson());
                    log.info("---------------" + saicInfoBase.getName());
                    //插入受益人数据
                    beneficiaryService.insertBeneficiary(saicInfoBase.getId(),beneficiaryDtos);
                }else {
                    log.info(saicBeneficiaryDto.getCustomerName() + "工商数据未找到，等待下次轮训...");
                    failSaicBeneficiaryDtos.add(saicBeneficiaryDto);
                }

            }else{
                //受益人对象为空并且受益人集合为空
                if(outBeneficiaryDto == null && CollectionUtils.isEmpty(saicBeneficiaryDto.getBeneficiaryDtos())){
                    log.info(saicBeneficiaryDto.getCustomerName() + "无法找到相关受益人信息");
                }else{
                    if(outBeneficiaryDto != null && StringUtils.isNotBlank(outBeneficiaryDto.getReason())){
                        log.info(saicBeneficiaryDto.getCustomerName() + "受益人正在计算，继续执行下一条=================");
                        failSaicBeneficiaryDtos.add(saicBeneficiaryDto);
                    }
                }
                saicBeneficiaryDto.setIsSame(Boolean.FALSE);
            }
        }catch (Exception e){
            log.error(saicBeneficiaryDto.getCustomerName() + "数据查询异常，继续下一条数据查询......", e);
        }
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
