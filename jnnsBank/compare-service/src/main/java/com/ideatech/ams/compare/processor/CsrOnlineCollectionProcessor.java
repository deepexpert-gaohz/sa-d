package com.ideatech.ams.compare.processor;

import com.ideatech.ams.compare.dto.CompareCollectTaskDto;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.customer.dto.CustomerPublicInfo;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.common.exception.EacException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;

/**
 * 客户信息采集数据
 * @author jzh
 * @date 2019/6/25.
 */

@Slf4j
@Component
public class CsrOnlineCollectionProcessor extends AbstractOnlineCollectionProcessor {

    @Autowired
    private CustomerPublicService customerPublicService;

    /**
     * 第一次在线采集的初始化动作
     * @param compareTaskId
     * @param dataSourceDto
     * @param compareCollectTaskDto
     * @throws Exception
     */
    @Override
    public void initCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        log.info("比对管理--客户在线采集初始化开始");
        //创建新任务
        createNewCollectTask(compareTaskId,dataSourceDto,compareCollectTaskDto);
        log.info("比对管理--客户在线采集初始化结束");
    }

    /**
     * 再次在线采集的初始化
     * @param compareTaskId
     * @param dataSourceDto
     * @param compareCollectTaskDto
     * @throws Exception
     */
    @Override
    public void initResetCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        log.info("比对管理--客户在线再次采集初始化开始");
        //创建新任务
        resetNewCollectTask(compareTaskId,dataSourceDto,compareCollectTaskDto);
        log.info("比对管理--客户在线再次采集初始化结束");
    }

    /**
     * 第一次在线采集的采集动作
     * @param compareTaskId
     * @param dataSourceDto
     * @param compareCollectTaskDto
     * @throws Exception
     */
    @Override
    public void processCollect(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        List<CustomerPublicInfo> customerPublicInfoList = readyForCollection(compareTaskId, dataSourceDto, compareCollectTaskDto);
        collect(compareTaskId,dataSourceDto,compareCollectTaskDto,customerPublicInfoList);
    }

    /**
     * 再次在线采集的采集动作
     * @param compareTaskId
     * @param dataSourceDto
     * @param compareCollectTaskDto
     * @throws Exception
     */
    @Override
    public void processResetCollect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        List<CustomerPublicInfo> customerPublicInfoList = readyForResetCollection(compareTaskId, dataSourceDto, compareCollectTaskDto);
        collect(compareTaskId,dataSourceDto,compareCollectTaskDto,customerPublicInfoList);
    }

    /**
     * 第一次/再次在线采集的完成动作
     * @param compareTaskId
     * @param dataSourceDto
     * @param compareCollectTaskDto
     * @throws Exception
     */
    @Override
    public void finishCollect(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        log.info("比对管理--客户在线采集完成开始");
        finishCollectTask(compareCollectTaskDto.getId(), Propagation.REQUIRES_NEW);
        log.info("比对管理--客户在线采集完成结束");
    }


    /**
     * 采集前置数据源
     * @param compareTaskId
     * @param dataSourceDto
     * @param compareCollectTaskDto
     * @return
     * @throws Exception
     */
    private List<CustomerPublicInfo> readyForCollection(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto)throws Exception {
        log.info("比对管理--客户在线采集正式开始");
        List<CustomerPublicInfo> customerPublicInfoList = customerPublicService.findAll();
        if(CollectionUtils.isNotEmpty(customerPublicInfoList)) {//map不为空
            log.info("初始化数量：" + customerPublicInfoList.size());//客户数量
            initCountCollectTask(compareCollectTaskDto.getId(), customerPublicInfoList.size());
            return customerPublicInfoList;
        }else{
            throw new EacException("客户在线采集数据为空");
        }
    }

    private List<CustomerPublicInfo> readyForResetCollection(Long compareTaskId, DataSourceDto dataSourceDto,CompareCollectTaskDto compareCollectTaskDto)throws Exception {
        log.info("查询历史导入数据并删除......");
        compareDataService.delCompareData(compareTaskId, dataSourceDto);
        //删除采集任务明细表
        compareCollectRecordService.deleteData(compareTaskId, dataSourceDto.getCode());
        return readyForCollection(compareTaskId,dataSourceDto,compareCollectTaskDto);
    }
    /**
     * 具体采集动作
     * @param compareTaskId
     * @param dataSourceDto
     * @param compareCollectTaskDto
     * @throws Exception
     */
    private void collect(Long compareTaskId, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto,List<CustomerPublicInfo> customerPublicInfoList) throws Exception{
        int successed = 0;
        int failed = 0;
        DataSourceDto dataSourceDtoNew = new DataSourceDto();
        dataSourceDtoNew.setDomain(dataSourceDto.getDomain());
        dataSourceDtoNew.setDataType(dataSourceDto.getDataType());
        dataSourceDtoNew.setCode(dataSourceDto.getCode());
        for(CustomerPublicInfo customerPublicInfo : customerPublicInfoList) {
            CompareDataDto compareData = new CompareDataDto();
            try{
                //比对字段
                compareData.setAcctNo(customerPublicInfo.getId().toString());//客户id当作帐号
                compareData.setDepositorName(customerPublicInfo.getDepositorName());//存款人名称（客户名称）
                compareData.setOrgCode(customerPublicInfo.getOrgCode());//组织机构代码
                compareData.setLegalName(customerPublicInfo.getLegalName());//法人姓名
                compareData.setRegNo(customerPublicInfo.getRegNo());//工商注册编号
                compareData.setRegAddress(customerPublicInfo.getRegAddress());//注册地址
                compareData.setRegisteredCapital(customerPublicInfo.getRegisteredCapital()==null?null:customerPublicInfo.getRegisteredCapital().toString());//注册资金
                compareData.setOpendate(customerPublicInfo.getSetupDate());//成立日期
                compareData.setEnddate(customerPublicInfo.getBusinessLicenseDue());//营业执照到期日
                compareData.setOrganFullId(customerPublicInfo.getOrganFullId());//组织机构ID，权限控制

                compareData.setTaskId(compareTaskId);

                saveSuccessCombine(compareData,compareCollectTaskDto.getId(),dataSourceDtoNew,Propagation.REQUIRES_NEW);
                successed++;


                if(successed+failed>20){
                    updatedCount(compareCollectTaskDto.getId(),successed,failed,Propagation.REQUIRES_NEW);
                    successed=0;
                    failed = 0;
                }
            }catch (Exception e){
                log.error("保存对象["+customerPublicInfo+"失败]:",e);
                saveFailedRecord(compareData,compareCollectTaskDto.getId(),e.getMessage(),dataSourceDto,Propagation.REQUIRES_NEW);
                failed++;
            }
        }

        if(successed+failed>0){
            try{
                updatedCount(compareCollectTaskDto.getId(),successed,failed,Propagation.REQUIRES_NEW);
            }catch (Exception e){
                log.error("更新数量报错:",e);
            }
        }
        log.info("比对管理--客户在线采集正式结束");
    }
}
