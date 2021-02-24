package com.ideatech.ams.compare.processor;

import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.vo.AnnualAccountVo;
import com.ideatech.ams.compare.dto.CompareCollectTaskDto;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.common.exception.EacException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import java.util.*;

/**
 * @Description AMS账管系统数据
 * @Author wanghongjie
 * @Date 2019/2/12
 **/
@Slf4j
@Component
public class AmsOnlineCollectionProcessor extends AbstractOnlineCollectionProcessor {

    @Autowired
    private AccountsAllService accountsAllService;

    @Override
    public void initCollect(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto) throws Exception  {
        log.info("比对管理--账管系统在线采集初始化开始");
        //创建新任务
        createNewCollectTask(compareTaskId,dataSourceDto,compareCollectTaskDto);
        log.info("比对管理--账管系统在线采集初始化结束");

    }

    @Override
    public void initResetCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        log.info("比对管理--账管系统在线再次采集初始化开始");
        //创建新任务
        resetNewCollectTask(compareTaskId,dataSourceDto,compareCollectTaskDto);
        log.info("比对管理--账管系统在线再次采集初始化结束");
    }

    @Override
    public void processCollect(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        List<AnnualAccountVo> annualAccountVos = readyForCollection(compareTaskId, dataSourceDto, compareCollectTaskDto);
        collect(compareTaskId,dataSourceDto,compareCollectTaskDto,annualAccountVos);
    }

    @Override
    public void processResetCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        List<AnnualAccountVo> annualAccountVos = readyForResetCollection(compareTaskId, dataSourceDto, compareCollectTaskDto);
        collect(compareTaskId,dataSourceDto,compareCollectTaskDto,annualAccountVos);
    }

    @Override
    public void finishCollect(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        log.info("比对管理--账管系统在线采集完成开始");
        finishCollectTask(compareCollectTaskDto.getId(), Propagation.REQUIRES_NEW);
        log.info("比对管理--账管系统在线采集完成结束");
    }


    /**
     * 采集前置数据源
     * @param compareTaskId
     * @param dataSourceDto
     * @param compareCollectTaskDto
     * @return
     * @throws Exception
     */
    private List<AnnualAccountVo> readyForCollection(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto)throws Exception {
        log.info("比对管理--账管系统在线采集正式开始");

        //获取任务创建人的所在机构，再根据机构获取本地账管账户数据-start
        String organFullId = compareTaskService.findById(compareTaskId).getOrganFullId();
        List<AnnualAccountVo> accountsAllList = accountsAllService.getAnnualAccountsAll(null, organFullId);
        //获取任务创建人的所在机构，再根据机构获取本地账管账户数据-end

        if(CollectionUtils.isNotEmpty(accountsAllList)) {//list不为空
            log.info("初始化数量：" + accountsAllList.size());
            initCountCollectTask(compareCollectTaskDto.getId(), accountsAllList.size());
            return accountsAllList;
        }else{
            throw new EacException("账管系统在线采集数据为空");
        }
    }

    private List<AnnualAccountVo> readyForResetCollection(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto)throws Exception {
        log.info("查询历史导入数据并删除......");
        compareDataService.delCompareData(compareTaskId, dataSourceDto);
        //删除采集任务明细表
        compareCollectRecordService.deleteData(compareTaskId, dataSourceDto.getCode());
        return readyForCollection(compareTaskId,dataSourceDto,compareCollectTaskDto);
    }
        /**
         * 采集动作
         * @param compareTaskId
         * @param dataSourceDto
         * @param compareCollectTaskDto
         * @throws Exception
         */
    private void collect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto,List<AnnualAccountVo> accountsAllList) throws Exception{
        int successed = 0;
        int failed = 0;
        DataSourceDto dataSourceDtoNew = new DataSourceDto();
        dataSourceDtoNew.setDomain(dataSourceDto.getDataType().getDomain());
        dataSourceDtoNew.setDataType(dataSourceDto.getDataType());
        dataSourceDtoNew.setCode(dataSourceDto.getCode());
        for(AnnualAccountVo accountVo : accountsAllList) {
            String registeredCapital = null;
            if(accountVo.getRegisteredCapital()!=null){
                registeredCapital = accountVo.getRegisteredCapital().toString();
            }
            try{
                CompareDataDto compareData = new CompareDataDto();
                String[] ignoreProperties = {"id","registeredCapital"};
                BeanUtils.copyProperties(accountVo, compareData,ignoreProperties);
                compareData.setAcctType(accountVo.getAcctType().getValue());
                compareData.setRegisteredCapital(registeredCapital);
                //注册地址set人行上报字段
                compareData.setRegAddress(accountVo.getRegFullAddress());
                compareData.setAccountStatus(accountVo.getAccountStatus().getFullName());
                compareData.setEnddate(accountVo.getBusinessLicenseDue());
                compareData.setEconomyIndustry(accountVo.getEconomyIndustryName());
                compareData.setTaskId(compareTaskId);
                if(compareData.getOrganCode() != null){
                    saveSuccessCombine(compareData,compareCollectTaskDto.getId(),dataSourceDtoNew,Propagation.REQUIRES_NEW);
                    successed++;
                }else{
                    log.error("账管系统在线采集失败：该条数据["+accountVo+"]行内机构号为空");
                    String errorMsg = "账管系统在线采集失败：该条数据行内机构号为空";
                    saveFailedRecord(compareData,compareCollectTaskDto.getId(),errorMsg,dataSourceDto,Propagation.REQUIRES_NEW);
                    failed++;
                }

                if(successed+failed>20){
                    updatedCount(compareCollectTaskDto.getId(),successed,failed,Propagation.REQUIRES_NEW);
                    successed=0;
                    failed = 0;
                }
            }catch (Exception e){
                log.error("保存对象["+accountVo+"失败]:",e);
            }
        }

        if(successed+failed>0){
            try{
                updatedCount(compareCollectTaskDto.getId(),successed,failed,Propagation.REQUIRES_NEW);
            }catch (Exception e){
                log.error("更新数量报错:",e);
            }
        }
        log.info("比对管理--账管系统在线采集正式结束");
    }


}
