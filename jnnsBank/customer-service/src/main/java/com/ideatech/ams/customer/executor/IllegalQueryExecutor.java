package com.ideatech.ams.customer.executor;

import com.ideatech.ams.customer.dao.illegal.IllegalQueryDao;
import com.ideatech.ams.customer.dto.SaicMonitorDto;
import com.ideatech.ams.customer.dto.illegal.IllegalQueryDto;
import com.ideatech.ams.customer.entity.illegal.IllegalQuery;
import com.ideatech.ams.customer.enums.SaicMonitorEnum;
import com.ideatech.ams.customer.enums.illegal.IllegalQueryExpiredStatus;
import com.ideatech.ams.customer.enums.illegal.IllegalQueryStatus;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.kyc.dto.*;
import com.ideatech.ams.kyc.service.SaicRequestService;
import com.ideatech.common.util.DateUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

@Data
@Slf4j
public class IllegalQueryExecutor implements Callable {

    public IllegalQueryExecutor(Set<IllegalQueryDto> token) {
        this.token = token;
    }

    private Set<IllegalQueryDto> token;

    private SaicRequestService saicRequestService;

    private IllegalQueryDao illegalQueryDao;

    private PlatformTransactionManager transactionManager;

    private SaicMonitorService saicMonitorService;

    private String userName;

    private Long organId;

    @Override
    public Object call() throws Exception {
        log.info(Thread.currentThread().getName()+"批量查询开始开始");
        try {
            if (token != null) {
                doCheck(token);
            }
        } catch (Exception e) {
            log.error("严重违法查询异常", e);
        }
        log.info(Thread.currentThread().getName()+"批量查询结束结束");
        return System.currentTimeMillis();
    }

    public void doCheck(Set<IllegalQueryDto> token) {


        //首先根据注册号去查询
        for (IllegalQueryDto illegalQueryDto : token) {
            TransactionDefinition definition = new DefaultTransactionDefinition(
                    TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus transaction = transactionManager.getTransaction(definition);

            try {
                IllegalQuery illegalQuery = illegalQueryDao.findOne(illegalQueryDto.getId());
                String regNo = illegalQueryDto.getRegNo();
                String companyName = illegalQueryDto.getCompanyName();
                SaicIdpInfo saicIdpInfo = null;
                if(StringUtils.isNotBlank(companyName) && StringUtils.isNotBlank(regNo)) {
                    saicIdpInfo = saicRequestService.getSaicInfoExact(companyName);
                    if(saicIdpInfo == null || StringUtils.isBlank(saicIdpInfo.getName())){
                        saicIdpInfo = saicRequestService.getSaicInfoExact(regNo);
                    }
                } else {
                    if(StringUtils.isNotBlank(companyName)) {
                        saicIdpInfo = saicRequestService.getSaicInfoExact(companyName);
                    } else if(StringUtils.isNotBlank(regNo)) {
                        saicIdpInfo = saicRequestService.getSaicInfoExact(regNo);
                    }
                }

                log.info(companyName + "批量违法查询校验调用工商数据保存进工商统计表......");
                SaicMonitorDto saicMonitorDto = new SaicMonitorDto();
                if(saicIdpInfo != null) {
                    saicMonitorDto = saicMonitorService.getSaicMonitor(userName,organId,companyName, saicIdpInfo.getId(),regNo, SaicMonitorEnum.ILLEGAL);
                    saicMonitorService.save(saicMonitorDto);
                }else{
                    saicMonitorDto = saicMonitorService.getSaicMonitor(userName,organId,companyName, null,regNo, SaicMonitorEnum.ILLEGAL);
                    saicMonitorService.save(saicMonitorDto);
                }

                if(illegalQueryDto.getIllegalStatus() == null) {
                    if(saicIdpInfo != null){
                        List<IllegalDto> list = saicIdpInfo.getIllegals();
                        if(CollectionUtils.isNotEmpty(list)){
                            illegalQueryDto.setIllegalStatus(IllegalQueryStatus.ILLEGAL);
                        }else{
                            illegalQueryDto.setIllegalStatus(IllegalQueryStatus.NORMAL);
                        }
                    }else {
                        illegalQueryDto.setIllegalStatus(IllegalQueryStatus.EMPTY);
                    }
                }

                BeanUtils.copyProperties(illegalQueryDto, illegalQuery);
                //查询工商状态并保存
                String saicStatus = "";
                boolean isChangemess = false;
                if(saicIdpInfo != null && StringUtils.isNotBlank(saicIdpInfo.getName())){
                    if(StringUtils.isNotBlank(saicIdpInfo.getState())){
                        saicStatus = saicIdpInfo.getState();
                    }
                    illegalQuery.setSaicStatus(saicStatus);
                    //营业执照到期日
                    if(StringUtils.isNotBlank(saicIdpInfo.getEnddate())){
                        illegalQuery.setFileEndDate(saicIdpInfo.getEnddate());
                        //如果日期小于当前日期，工商状态为证件到期
                        String endDate = saicIdpInfo.getEnddate();
                        endDate = endDate.replace("年","").replace("月","").replace("日","");
                        String nowDate = DateUtils.DateToStr(new Date(),"yyyyMMdd");
                        Integer res = nowDate.compareTo(endDate);
                        if(res > 0){
                            illegalQuery.setFileDueExpired(IllegalQueryExpiredStatus.EXPIRED);
                        }else{
                            illegalQuery.setFileDueExpired(IllegalQueryExpiredStatus.NOTEXPIRED);
                        }
                    }
                    //经营异常
                    if(CollectionUtils.isNotEmpty(saicIdpInfo.getChangemess())){
                        List<ChangeMessDto> changeMessDtos = saicIdpInfo.getChangemess();
                        for(ChangeMessDto ChangeMessDto : changeMessDtos){
                            if(StringUtils.isNotBlank(ChangeMessDto.getOutdate()) && StringUtils.isNotBlank(ChangeMessDto.getOutreason())){
                                isChangemess = false;
                            }
                        }
                        if(!isChangemess){
                            illegalQuery.setChangemess(IllegalQueryStatus.NORMAL);
                        }else{
                            illegalQuery.setChangemess(IllegalQueryStatus.CHANGEMESS);
                        }
                    }else{
                        illegalQuery.setChangemess(IllegalQueryStatus.NORMAL);
                    }
                }else{
                    illegalQuery.setSaicStatus("未查到");
                    illegalQuery.setChangemess(IllegalQueryStatus.EMPTY);
                    illegalQuery.setFileEndDate("未查到");
                    illegalQuery.setFileDueExpired(IllegalQueryExpiredStatus.EMPTY);
                }
                illegalQueryDao.save(illegalQuery);
                transactionManager.commit(transaction);
                log.info("违法数据查询结果保存成功");
            } catch (Exception e) {
                log.error("查询违法数据异常", e);
                transactionManager.rollback(transaction);
            }
        }
    }

    private OutIllegalQueryDto setStatus(String str, IllegalQueryDto illegalQueryDto) {
        OutIllegalQueryDto outIllegalQueryDto = saicRequestService.getOutIllegalQueryDto(str);
        if(outIllegalQueryDto != null){
            if("EMPTY".equals(outIllegalQueryDto.getIllegalStatus())) {
                illegalQueryDto.setIllegalStatus(IllegalQueryStatus.EMPTY);
            }
        }else{
            outIllegalQueryDto = new OutIllegalQueryDto();
        }
        return outIllegalQueryDto;
    }

}
