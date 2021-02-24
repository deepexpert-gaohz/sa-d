package com.ideatech.ams.compare.executor;

import com.ideatech.ams.compare.dto.CompareCollectRecordDto;
import com.ideatech.ams.compare.dto.CompareCollectTaskDto;
import com.ideatech.ams.compare.dto.CompareTaskDto;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.enums.CollectState;
import com.ideatech.ams.compare.enums.DataSourceEnum;
import com.ideatech.ams.compare.service.CompareCollectRecordService;
import com.ideatech.ams.compare.service.CompareDataService;
import com.ideatech.ams.compare.vo.CompareFieldExcelRowVo;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Data
@Slf4j
public class CompareDataImporterExecutor implements Callable {

    private CompareDataService compareDataService;

    private CompareCollectRecordService compareCollectRecordService;

    private List<CompareFieldExcelRowVo> dataList;

    private Boolean coreTransform;

    private DataSourceDto dataSourceDto;

    private Map<String, String> industryCodeDicMap;

    private Map<String, String> depositorTypeDicMap;

    private Map<String, String> fileTypeDicMap;

    private Map<String, String> acctTypeDicMap;

    private Map<String, String> accountStatusDicMap;

    private Map<String, String> legalIdcardTypeDicMap;

    private CompareTaskDto compareTaskDto;

    private Map<String, OrganizationDto> orgMap;

    private CompareCollectTaskDto compareCollectTaskDto;

    private Integer failCount;

    public CompareDataImporterExecutor(List<CompareFieldExcelRowVo> dataList) {
        this.dataList = dataList;
    }

    @Override
    public Object call() throws Exception {
        for (CompareFieldExcelRowVo compareFieldExcelRowVo : dataList) {

            log.info("正在导入数据：" + compareFieldExcelRowVo.getAcctNo());
            CompareDataDto compareDataDto = new CompareDataDto();
            try {
                BeanUtils.copyProperties(compareFieldExcelRowVo, compareDataDto);

                //开启转换同时是核心数据情况下
                if (coreTransform && dataSourceDto.getDataType() == DataSourceEnum.CORE) {
                    //字典转换 （此步骤核心编码转人行编码，后续保存时人行转中文）
                    log.info("进行字典转换前:" + compareDataDto);
                    coreTransform(compareDataDto);
                    log.info("进行字典转换后:" + compareDataDto);
                }

                compareDataDto.setTaskId(compareTaskDto.getId());
                compareDataDto.setOrganFullId(orgMap.get(compareDataDto.getOrganCode()).getFullId());
                compareDataService.saveCompareData(compareDataDto, dataSourceDto);
                //保存导入状态
                saveCompareCollectRecordDto(compareDataDto, compareTaskDto, CollectState.success, dataSourceDto, compareCollectTaskDto);
            } catch (Exception e) {
                //保存导入状态
                failCount++;
                saveCompareCollectRecordDto(compareDataDto, compareTaskDto, CollectState.fail, dataSourceDto, compareCollectTaskDto);
                log.error("保存导入的excel数据异常", e);
            }
        }

        return failCount;
    }

    public void saveCompareCollectRecordDto(CompareDataDto compareDataDto, CompareTaskDto compareTaskDto, CollectState collectState, DataSourceDto dataSourceDto, CompareCollectTaskDto compareCollectTaskDto) {
        CompareCollectRecordDto compareCollectRecordDto = new CompareCollectRecordDto();
        compareCollectRecordDto.setAcctNo(compareDataDto.getAcctNo());
        compareCollectRecordDto.setDepositorName(compareDataDto.getDepositorName());
        compareCollectRecordDto.setRegNo(compareDataDto.getRegNo());
        compareCollectRecordDto.setCompareTaskId(compareTaskDto.getId());
        compareCollectRecordDto.setCollectState(collectState);
        compareCollectRecordDto.setDataSourceType(dataSourceDto.getCode());
        compareCollectRecordDto.setCollectTaskId(compareCollectTaskDto.getId());
        compareCollectRecordDto.setOrganCode(compareDataDto.getOrganCode());
        compareCollectRecordDto.setOrganFullId(compareDataDto.getOrganFullId());
        if (collectState == CollectState.success) {
            compareCollectRecordDto.setFailReason("数据导入成功");
        } else {
            compareCollectRecordDto.setFailReason("数据导入失败");
        }
        compareCollectRecordService.saveCompareCollectRecord(compareCollectRecordDto);
    }

    /**
     * 字典转换
     * @param compareDataDto
     */
    private void coreTransform(CompareDataDto compareDataDto) {
        //行业归属
        if (industryCodeDicMap.containsKey(compareDataDto.getIndustryCode())){
            compareDataDto.setIndustryCode(industryCodeDicMap.get(compareDataDto.getIndustryCode()));
        }
        //存款人类别
        if (depositorTypeDicMap.containsKey(compareDataDto.getDepositorType())){
            compareDataDto.setDepositorType(depositorTypeDicMap.get(compareDataDto.getDepositorType()));
        }
        //证明文件种类
        if (fileTypeDicMap.containsKey(compareDataDto.getFileType())){
            compareDataDto.setFileType(fileTypeDicMap.get(compareDataDto.getFileType()));
        }
        //账户性质
        if (acctTypeDicMap.containsKey(compareDataDto.getAcctType())){
            compareDataDto.setAcctType(acctTypeDicMap.get(compareDataDto.getAcctType()));
        }
        //账户状态
        if (accountStatusDicMap.containsKey(compareDataDto.getAccountStatus())){
            compareDataDto.setAccountStatus(accountStatusDicMap.get(compareDataDto.getAccountStatus()));
        }
        //证件种类
        if (legalIdcardTypeDicMap.containsKey(compareDataDto.getLegalIdcardType())){
            compareDataDto.setLegalIdcardType(legalIdcardTypeDicMap.get(compareDataDto.getLegalIdcardType()));
        }
    }

}
