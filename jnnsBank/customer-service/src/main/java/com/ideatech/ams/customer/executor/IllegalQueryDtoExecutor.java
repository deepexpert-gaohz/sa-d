package com.ideatech.ams.customer.executor;

import com.ideatech.ams.customer.dao.illegal.IllegalQueryDao;
import com.ideatech.ams.customer.dto.illegal.IllegalQueryDto;
import com.ideatech.ams.customer.dto.illegal.IllegalQueryErrorDto;
import com.ideatech.ams.customer.service.illegal.IllegalQueryErrorService;
import com.ideatech.ams.customer.service.illegal.IllegalQueryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Slf4j
public class IllegalQueryDtoExecutor implements Callable {

    private IllegalQueryService illegalQueryService;

    private IllegalQueryErrorService illegalQueryErrorService;

    private List<IllegalQueryDto> illegalQueryDtoList;

    private List<String> regNoList;

    private IllegalQueryDao illegalQueryDao;

    private Long batchId;

    private List<Integer> strNum;

    private ConcurrentHashMap<Object, Object> concurrentHashMap = new ConcurrentHashMap<>();

    private PlatformTransactionManager transactionManager;

    private OrganizationService organizationService;

    public IllegalQueryDtoExecutor(List<IllegalQueryDto> illegalQueryDtoList, List<String> regNoList, List<Integer> strNum){
        this.illegalQueryDtoList = illegalQueryDtoList;
        this.regNoList = regNoList;
        this.strNum = strNum;
    }

    @Override
    public Object call() throws Exception {
        log.info(Thread.currentThread().getName()+"开始");
        try {
            if(illegalQueryDtoList != null){
                save(illegalQueryDtoList, regNoList, strNum);
            }
        }catch (Exception e){
            log.error("保存导入的excel数据异常",e);
        }
        log.info(Thread.currentThread().getName()+"结束");
        return System.currentTimeMillis();
    }

    private void save(List<IllegalQueryDto> illegalQueryDtoList, List<String> regNoList, List<Integer> strNum) {
        ResultDto dto = new ResultDto();
        Integer chongfu = 0;
        Integer noFound = 0;
//        Integer errorNum = 0;
        log.info("批次中单个线程处理的条数："+illegalQueryDtoList.size());
        for (IllegalQueryDto illegalQueryDto : illegalQueryDtoList) {
            if (StringUtils.isNotBlank(illegalQueryDto.getRegNo()) || StringUtils.isNotBlank(illegalQueryDto.getCompanyName())) {
                //机构号不存在时不执行
                OrganizationDto organizationDto = organizationService.findByCode(illegalQueryDto.getOrganCode());
                if(organizationDto == null) {

                    IllegalQueryErrorDto illegalQueryErrorDto = ConverterService.convert(illegalQueryDto, IllegalQueryErrorDto.class);
                    illegalQueryErrorDto.setIllegalQueryBatchId(batchId);
                    illegalQueryErrorDto.setErrorMessage("未查询到所在机构...");
                    illegalQueryErrorService.save(illegalQueryErrorDto);

                    synchronized (IllegalQueryDtoExecutor.class) {
                        noFound = strNum.get(1);
                        strNum.set(1, ++noFound);
                    }
                    continue;
                }

                TransactionDefinition definition = new DefaultTransactionDefinition(
                        TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                TransactionStatus transaction = transactionManager.getTransaction(definition);
                try {
                    if (addIfAbsent(regNoList, illegalQueryDto.getCompanyName() + "," + illegalQueryDto.getOrganCode()) == null) {  //根据企业名称进行去重
                        //                    regNoList.add(illegalQueryDto.getCompanyName());
                        illegalQueryDto.setIllegalQueryBatchId(batchId);
                        //保存organFullId
                        OrganizationDto organ = organizationService.findByCode(illegalQueryDto.getOrganCode());
                        illegalQueryDto.setOrganFullId(organ.getFullId());
                        illegalQueryService.save(illegalQueryDto);
                    } else {
                        //重复数据保存进数据库
                        IllegalQueryErrorDto illegalQueryErrorDto = ConverterService.convert(illegalQueryDto, IllegalQueryErrorDto.class);
                        illegalQueryErrorDto.setIllegalQueryBatchId(batchId);
                        illegalQueryErrorDto.setErrorMessage("重复企业...");
                        illegalQueryErrorService.save(illegalQueryErrorDto);

                        synchronized (IllegalQueryDtoExecutor.class) {
                            chongfu = strNum.get(0);
                            strNum.set(0, ++chongfu);
                        }
                    }

                    transactionManager.commit(transaction);
                    log.info("企业名称 " + illegalQueryDto.getCompanyName() + " 保存成功");
                } catch (Exception e){
                    log.error("保存导入的excel数据异常", e);
                    transactionManager.rollback(transaction);
                }
            }

            //没有企业名称跟注册号略过
            if (StringUtils.isBlank(illegalQueryDto.getRegNo()) && StringUtils.isBlank(illegalQueryDto.getCompanyName())) {

                IllegalQueryErrorDto illegalQueryErrorDto = ConverterService.convert(illegalQueryDto, IllegalQueryErrorDto.class);
                illegalQueryErrorDto.setIllegalQueryBatchId(batchId);
                illegalQueryErrorDto.setErrorMessage("没有企业名称跟注册号...");
                illegalQueryErrorService.save(illegalQueryErrorDto);

                synchronized (IllegalQueryDtoExecutor.class) {
                    noFound = strNum.get(1);
                    strNum.set(1, ++noFound);
                }
                continue;
            }
        }
    }

    private String addIfAbsent(List list, String str){
        synchronized (IllegalQueryDtoExecutor.class){
            if(list.contains(str)){//包含返回原来对象
                return str;
            }else{//不包含就塞进去，然后返回null
                list.add(str);
                return null;
            }
        }

    }

}
