package com.ideatech.ams.compare.service;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.compare.dao.CompareResultDao;
import com.ideatech.ams.compare.dao.data.*;
import com.ideatech.ams.compare.dao.spec.CompareResultSpec;
import com.ideatech.ams.compare.dto.*;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultDetailDto;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultDetailListDto;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultFieldDto;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.entity.CompareResult;
import com.ideatech.ams.compare.entity.data.*;
import com.ideatech.ams.compare.enums.CompareExportType;
import com.ideatech.ams.compare.enums.DataSourceEnum;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;

@Service
@Transactional
@Slf4j
public class CompareResultServiceImpl extends BaseServiceImpl<CompareResultDao, CompareResult, CompareResultDto> implements CompareResultService {

    @Autowired
    private CompareTaskService compareTaskService;

    @Autowired
    private CompareRuleDataSourceService compareRuleDataSourceService;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private CompareDefineService compareDefineService;

    @Autowired
    private CompareFieldService compareFieldService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CompareRuleFieldsService compareRuleFieldsService;

    @Autowired
    private CompareData1Repository compareData1Repository;

    @Autowired
    private CompareData2Repository compareData2Repository;

    @Autowired
    private CompareData3Repository compareData3Repository;

    @Autowired
    private CompareData4Repository compareData4Repository;

    @Autowired
    private CompareData5Repository compareData5Repository;

    @Autowired
    private CompareData6Repository compareData6Repository;

    @Autowired
    private CompareData7Repository compareData7Repository;

    @Autowired
    private CompareData8Repository compareData8Repository;

    @Autowired
    private CompareData9Repository compareData9Repository;

    @Override
    public CompareResultDto getOne(Long id) {
        CompareResultDto dto = null;
        CompareResult compareResult = getBaseDao().findOne(id);

        if(compareResult != null) {
            dto = new CompareResultDto();
            BeanCopierUtils.copyProperties(compareResult, dto);
        }

        return dto;
    }


    @Override
    public Map<String, Object> getCompareResultFieldStruct(Long taskId) {
        Map<String, Object> map = new HashMap<>();
        Set<Long> set = new HashSet<>();

        //获取比对规则勾选字段列表
        List<CompareDefineDto> compareDefineList = compareDefineService.getCompareDefineList(taskId);
        if(CollectionUtils.isNotEmpty(compareDefineList)) {
            for(CompareDefineDto dto : compareDefineList) {
                set.add(dto.getCompareFieldId());
            }

            for(Long id : set) {
                CompareFieldDto compareFieldDto = compareFieldService.findById(id);
                map.put(compareFieldDto.getField(), compareFieldDto.getName());
            }
        }

//        if(map.get("acctNo") != null) {
//            map.remove("acctNo");
//        }
        return map;
    }

    @Override
    public Map<String, Object> getCompareResultFieldsStruct(CompareTaskDto task) {
        Map<String, Object> map = new HashMap<>();

        //获取比对规则勾选字段列表
        List<CompareRuleFieldsDto> compareRuleFieldsList = compareRuleFieldsService.findByCompareRuleId(task.getCompareRuleId());
        if(CollectionUtils.isNotEmpty(compareRuleFieldsList)) {
            for(CompareRuleFieldsDto dto : compareRuleFieldsList) {
                CompareFieldDto compareFieldDto = compareFieldService.findById(dto.getCompareFieldId());
                map.put(compareFieldDto.getField(), compareFieldDto.getName());
            }
        }

        return map;
    }

    @Override
    public long countByTaskId(Long taskId) {
        return getBaseDao().countByCompareTaskId(taskId);
    }

    @Override
    public TableResultResponse query(CompareResultDto dto, Pageable pageable) throws Exception {
//        List<Map<String, String>> recordList = new ArrayList<>();
//        Map<String, String> recordMap = null;

        Long organId = dto.getOrganId();
        if(organId == null) {
            dto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        } else {
            OrganizationDto organ = organizationService.findById(organId);
            dto.setOrganFullId(organ.getFullId());
        }

        Page<CompareResult> page = getBaseDao().findAll(new CompareResultSpec(dto), pageable);
        long count = getBaseDao().count(new CompareResultSpec(dto));

        List<CompareResultDto> compareResultDtos = ConverterService.convertToList(page.getContent(), CompareResultDto.class);

        //根据比对任务获取几方数据源
//        if(CollectionUtils.isNotEmpty(compareResultDtos)) {
//            for(CompareResultDto compareResultDto : compareResultDtos) {
//                CompareTaskDto compareTaskDto = compareTaskService.findById(compareResultDto.getCompareTaskId());
//                List<CompareRuleDataSourceDto> dataSourceList = compareRuleDataSourceService.findByCompareRuleId(compareTaskDto.getCompareRuleId());
//                if(CollectionUtils.isNotEmpty(dataSourceList)) {
//                    for(CompareRuleDataSourceDto compareRuleDataSourceDto :dataSourceList){
//                        recordMap = new HashMap<>();
//
//                        if(compareRuleDataSourceDto.getActive()){
//                            DataSourceDto dataSourceDto = dataSourceService.findById(compareRuleDataSourceDto.getDataSourceId());
//                            Map<String, CompareDataDto> compareDatasMap = getCompareData(dataSourceDto, compareResultDto.getCompareTaskId(), compareResultDto.getAccount());
//                            if(compareDatasMap.keySet().iterator().hasNext()) {
//                                String key = compareDatasMap.keySet().iterator().next();
//                                CompareDataDto compareDataDto = compareDatasMap.get(key);
//                                Map<String, String> compareDataMap = BeanUtils.describe(compareDataDto);
//                                Map<String, String> compareResultMap = BeanUtils.describe(compareResultDto);
//                                recordMap.putAll(compareDataMap);
//                                recordMap.putAll(compareResultMap);
//
//                                recordMap.put("acctNo", compareResultDto.getAccount());
//                                recordList.add(recordMap);
//                            }
//                            break;
//                        }
//                    }
//                }
//            }
//        }

        return new TableResultResponse<>((int)count, compareResultDtos);
    }

    /**
     * 每一条比对结果详情信息
     * @param dto
     */
    @Override
    public CompareResultDetailDto detail(CompareResultDto dto) throws Exception {
        CompareResultDetailDto compareResultDetailDto = new CompareResultDetailDto();
        List<CompareResultDetailListDto> compareResultDetailListDtos = new ArrayList<>();

        CompareResultDto compareResultDto = getOne(dto.getId());

        compareResultDetailListDtos = getCompareResultData(compareResultDto, compareResultDetailDto, compareResultDetailListDtos);

        //不同数据源字段值不同的标红
//        setIsEquals(compareResultDetailListDtos);

        compareResultDetailDto.setList(compareResultDetailListDtos);

        return compareResultDetailDto;
    }

    /**
     * 判断比对数据所存储的compareData
     * @param dataSourceDto
     * @param taskId
     * @param acctNo
     * @return
     */
    private Map<String, CompareDataDto> getCompareData(DataSourceDto dataSourceDto, Long taskId, String acctNo) {
        CompareDataDto compareDataDto = new CompareDataDto();
        Map<String, CompareDataDto> map = new HashMap<>();

        if(dataSourceDto.getDataType() == DataSourceEnum.AMS) {
            CompareData1 compareData1 = compareData1Repository.findByTaskIdAndAcctNo(taskId, acctNo);
            BeanCopierUtils.copyProperties(compareData1, compareDataDto);
            map.put(DataSourceEnum.AMS.name(), compareDataDto);
        } else if(dataSourceDto.getDataType() == DataSourceEnum.PBC) {
            CompareData2 compareData2 = compareData2Repository.findByTaskIdAndAcctNo(taskId, acctNo);
            BeanCopierUtils.copyProperties(compareData2, compareDataDto);
            map.put(DataSourceEnum.PBC.name(), compareDataDto);
        } else if(dataSourceDto.getDataType() == DataSourceEnum.SAIC) {
            CompareData3 compareData3 = compareData3Repository.findByTaskIdAndAcctNo(taskId, acctNo);
            BeanCopierUtils.copyProperties(compareData3, compareDataDto);
            map.put(DataSourceEnum.SAIC.name(), compareDataDto);
        } else if(dataSourceDto.getDataType() == DataSourceEnum.CORE) {
            CompareData4 compareData4 = compareData4Repository.findByTaskIdAndAcctNo(taskId, acctNo);
            BeanCopierUtils.copyProperties(compareData4, compareDataDto);
            map.put(DataSourceEnum.CORE.name(), compareDataDto);
        } else if(dataSourceDto.getDataType() == DataSourceEnum.OTHER) {
            switch (dataSourceDto.getDomain()) {
                case "CompareData5":
                    CompareData5 compareData5 = compareData5Repository.findByTaskIdAndAcctNo(taskId, acctNo);
                    BeanCopierUtils.copyProperties(compareData5, compareDataDto);
                    break;
                case "CompareData6":
                    CompareData6 compareData6 = compareData6Repository.findByTaskIdAndAcctNo(taskId, acctNo);
                    BeanCopierUtils.copyProperties(compareData6, compareDataDto);
                    break;
                case "CompareData7":
                    CompareData7 compareData7 = compareData7Repository.findByTaskIdAndAcctNo(taskId, acctNo);
                    BeanCopierUtils.copyProperties(compareData7, compareDataDto);
                    break;
                case "CompareData8":
                    CompareData8 compareData8 = compareData8Repository.findByTaskIdAndAcctNo(taskId, acctNo);
                    BeanCopierUtils.copyProperties(compareData8, compareDataDto);
                    break;
                case "CompareData9":
                    CompareData9 compareData9 = compareData9Repository.findByTaskIdAndAcctNo(taskId, acctNo);
                    BeanCopierUtils.copyProperties(compareData9, compareDataDto);
                    break;
            }

            map.put(dataSourceDto.getDataType().name(), compareDataDto);
        }

        return map;
    }

    /**
     * 获取比对结果详情
     * @param compareResult
     * @param compareResultDetailDto
     * @param compareResultDetailListDtos
     * @throws Exception
     */
    @Override
    public List<CompareResultDetailListDto> getCompareResultData(CompareResultDto compareResult, CompareResultDetailDto compareResultDetailDto, List<CompareResultDetailListDto> compareResultDetailListDtos) {
        CompareResultFieldDto compareResultFieldDto = null;
        CompareResultFieldDto compareResultFieldDto2 = null;
        CompareResultDetailListDto compareResultDetailListDto = null;
        List<CompareResultFieldDto> compareResultFieldDtoList = new ArrayList<>();
        List<CompareResultFieldDto> compareResultFieldDtoList2 = null;
        compareResultDetailListDtos = new ArrayList<>();

        Long compareTaskId = compareResult.getCompareTaskId();
        CompareTaskDto compareTaskDto = compareTaskService.findById(compareTaskId);
        Long compareRuleId = compareTaskDto.getCompareRuleId();

        //获取表头字段信息
        Map<String, Object> compareResultFieldStruct = getCompareResultFieldsStruct(compareTaskDto);
        if(MapUtils.isNotEmpty(compareResultFieldStruct)) {
            for(Map.Entry<String, Object> entry : compareResultFieldStruct.entrySet()) {
                compareResultFieldDto = new CompareResultFieldDto();
                compareResultFieldDto.setFieldName(String.valueOf(entry.getValue()));
                compareResultFieldDto.setFieldNameEng(entry.getKey());
                compareResultFieldDtoList.add(compareResultFieldDto);
            }
        }

        compareResultDetailDto.setFieldList(compareResultFieldDtoList);

        JSONObject detailJson = JSONObject.parseObject(compareResult.getDetails());
        JSONObject dataJson = detailJson.getJSONObject("data");
        JSONObject matchJson = detailJson.getJSONObject("match");
//        String[] colors = StringUtils.splitByWholeSeparatorPreserveAllTokens(matchJson, ",");

        for(String dataSourceName : dataJson.keySet()) {
            int i = 0;
            DataSourceDto dataSourceDto = dataSourceService.findByName(dataSourceName);
            JSONObject filedsJson = dataJson.getJSONObject(dataSourceName);

//            Map<String, CompareDataDto> compareDataMap = getCompareData(dataSourceDto, compareResult.getCompareTaskId(), compareResult.getAccount());
//            CompareDataDto compareDataDto = compareDataMap.get(dataSourceDto.getDataType().name());

            compareResultFieldDtoList2 = new ArrayList<>();
            for(CompareResultFieldDto resultFieldDto : compareResultFieldDtoList) {
                compareResultFieldDto2 = new CompareResultFieldDto();
                BeanCopierUtils.copyProperties(resultFieldDto, compareResultFieldDto2);
                compareResultFieldDtoList2.add(compareResultFieldDto2);
            }

            for(CompareResultFieldDto resultField : compareResultFieldDtoList2) {
                resultField.setFieldValue(filedsJson.get(resultField.getFieldNameEng()) == null ? "" : filedsJson.get(resultField.getFieldNameEng()));
                Long compareFieldId = compareFieldService.findByField(resultField.getFieldNameEng()).getId();
                CompareDefineDto compareDefineDto =
                        compareDefineService.findByCompareRuleIdAndDataSourceIdAndCompareFieldId(compareRuleId, dataSourceDto.getId(), compareFieldId);
                resultField.setIsEquals(Boolean.valueOf(matchJson.get(resultField.getFieldNameEng()).toString()));
                resultField.setNullpass(compareDefineDto.getNullpass());

                i++;
            }

            compareResultDetailListDto = new CompareResultDetailListDto();
            compareResultDetailListDto.setFieldList(compareResultFieldDtoList2);
            compareResultDetailListDto.setName(dataSourceDto.getName());  //数据源名称
            compareResultDetailListDto.setAccount(compareResult.getAccount());  //账号

            compareResultDetailListDtos.add(compareResultDetailListDto);
        }

//        List<CompareRuleDataSourceDto> dataSourceList = compareRuleDataSourceService.findByCompareRuleId(compareTaskDto.getCompareRuleId());
//
//        //遍历数据源对应的多方数据展示
//        if(CollectionUtils.isNotEmpty(dataSourceList)) {
//            for(CompareRuleDataSourceDto compareRuleDataSourceDto : dataSourceList) {
//                Long dataSourceId = compareRuleDataSourceDto.getDataSourceId();
//
//                if(compareRuleDataSourceDto.getActive()){
//                    DataSourceDto dataSourceDto = dataSourceService.findById(compareRuleDataSourceDto.getDataSourceId());
//                    Map<String, CompareDataDto> compareDataMap = getCompareData(dataSourceDto, compareResult.getCompareTaskId(), compareResult.getAccount());
//
//                    CompareDataDto compareDataDto = compareDataMap.get(dataSourceDto.getDataType().name());
//
//                    compareResultFieldDtoList2 = new ArrayList<>();
//                    for(CompareResultFieldDto resultFieldDto : compareResultFieldDtoList) {
//                        compareResultFieldDto2 = new CompareResultFieldDto();
//                        BeanCopierUtils.copyProperties(resultFieldDto, compareResultFieldDto2);
//                        compareResultFieldDtoList2.add(compareResultFieldDto2);
//                    }
//                    //遍历compareResultFieldList2,每个数据源字段属性的赋值
//                    for(CompareResultFieldDto resultField : compareResultFieldDtoList2) {
////                      CompareFieldDto compareFieldDto = compareFieldService.findByName(resultField.getFieldName());
//                        //获取指定字段名的字段值
//                        Field declaredField = null;
//                        try {
//                            declaredField = compareDataDto.getClass().getDeclaredField(resultField.getFieldNameEng());
//                            declaredField.setAccessible(true);
//                            resultField.setFieldValue(declaredField.get(compareDataDto) == null ? "": declaredField.get(compareDataDto));
//
//                            Long compareFieldId = compareFieldService.findByField(resultField.getFieldNameEng()).getId();
//                            CompareDefineDto compareDefineDto =
//                                    compareDefineService.findByCompareRuleIdAndDataSourceIdAndCompareFieldId(compareRuleId, dataSourceId, compareFieldId);
//                            resultField.setNullpass(compareDefineDto.getNullpass());
//                        } catch (Exception e) {
//                            log.error("未找到该字段");
//                            e.printStackTrace();
//                        }
//                    }
//
//                    compareResultDetailListDto = new CompareResultDetailListDto();
//                    compareResultDetailListDto.setFieldList(compareResultFieldDtoList2);
//                    compareResultDetailListDto.setName(dataSourceDto.getName());  //数据源名称
//                    compareResultDetailListDto.setAccount(compareResult.getAccount());  //账号
//
//                    compareResultDetailListDtos.add(compareResultDetailListDto);
//                }
//            }
//        }

        return compareResultDetailListDtos;
    }

    //不同数据源字段值不同的标红(isEquals赋值)   循环比对，第一个空算过下一个循环
    @Override
    public void setIsEquals(List<CompareResultDetailListDto> compareResultDetailListDtos) {
        CompareResultDetailListDto compareResultDetailListDto1 = compareResultDetailListDtos.get(0);

        //同个数据源的不同字段遍历
        for(int j = 0; j < compareResultDetailListDto1.getFieldList().size(); j++) {

            for(int k = 0; k < compareResultDetailListDtos.size(); k++) {
                CompareResultFieldDto resultFieldDto1 = compareResultDetailListDtos.get(k).getFieldList().get(j);

                for (int i = k + 1; i < compareResultDetailListDtos.size(); i++) {
                    CompareResultFieldDto resultFieldDto2 = compareResultDetailListDtos.get(i).getFieldList().get(j);

                    //某个数据源字段值不统一时，所有数据源的该字段都标红
                    if((resultFieldDto1.getFieldValue() == null || "".equals(resultFieldDto1.getFieldValue())) && (resultFieldDto1.getNullpass() != null && resultFieldDto1.getNullpass()) ||
                            (resultFieldDto2.getFieldValue() == null || "".equals(resultFieldDto2.getFieldValue())) && (resultFieldDto2.getNullpass() != null && resultFieldDto2.getNullpass())) {
                        continue;
                    }

                    resultFieldDto1.setFieldValue(resultFieldDto1.getFieldValue() == null ? "" : resultFieldDto1.getFieldValue());
                    resultFieldDto2.setFieldValue(resultFieldDto2.getFieldValue() == null ? "" : resultFieldDto2.getFieldValue());

                    if (!resultFieldDto1.getFieldValue().equals(resultFieldDto2.getFieldValue())) {
                        resultFieldDto1.setIsEquals(false);

                        //该列对应的所有字段都标红
                        for (int h = 0; h < compareResultDetailListDtos.size() - 1; h++) {
                            CompareResultFieldDto resultFieldDto3 = compareResultDetailListDtos.get(h + 1).getFieldList().get(j);
                            resultFieldDto3.setIsEquals(false);
                        }
                        break;
                    }

                }
            }
        }
    }

    @Override
    public List<CompareResultDto> findAllByCondition(CompareResultDto dto) {
        List<CompareResult> list = getBaseDao().findAll(new CompareResultSpec(dto), new Sort(Sort.Direction.DESC, "lastUpdateDate"));

        List<CompareResultDto> compareResultDtos = ConverterService.convertToList(list, CompareResultDto.class);

        return compareResultDtos;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<CompareResultDto> findAllByConditionNew(CompareResultDto dto) {
        List<CompareResult> list = getBaseDao().findAll(new CompareResultSpec(dto), new Sort(Sort.Direction.DESC, "lastUpdateDate"));

        List<CompareResultDto> compareResultDtos = ConverterService.convertToList(list, CompareResultDto.class);

        return compareResultDtos;
    }

    @Override
    public long count(CompareResultDto condition) {
        return getBaseDao().count(new CompareResultSpec(condition));
    }

    @Override
    public CompareResultDto findByTaskIdAndDepositorName(Long compareTaskId, String depositorName) {
        CompareResultDto dto = null;
        CompareResult compareResult = getBaseDao().findByCompareTaskIdAndDepositorName(compareTaskId, depositorName);
        if(compareResult != null) {
            dto = new CompareResultDto();
            BeanCopierUtils.copyProperties(compareResult, dto);
        }
        return dto;
    }

    @Override
    public void deleteAllByCompareTaskId(Long compareTaskId) {
        CompareResultDto dto = new CompareResultDto();
        dto.setCompareTaskId(compareTaskId);
        List<CompareResult> compareResultList = getBaseDao().findAll(new CompareResultSpec(dto), new Sort(Sort.Direction.DESC, "lastUpdateDate"));
        getBaseDao().delete(compareResultList);
    }
}
