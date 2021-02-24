package com.ideatech.ams.compare.spi.dataImporter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.ideatech.ams.compare.dao.data.CompareDataRepository;
import com.ideatech.ams.compare.dao.jpa.CompareRepositoryFinder;
import com.ideatech.ams.compare.dto.CompareCollectRecordDto;
import com.ideatech.ams.compare.dto.CompareCollectTaskDto;
import com.ideatech.ams.compare.dto.CompareTaskDto;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.entity.data.CompareData;
import com.ideatech.ams.compare.enums.CollectState;
import com.ideatech.ams.compare.enums.CollectTaskState;
import com.ideatech.ams.compare.enums.DataSourceEnum;
import com.ideatech.ams.compare.service.*;
import com.ideatech.ams.compare.spi.SaicDataTransformation;
import com.ideatech.ams.kyc.dto.OutInfoOne;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.kyc.dto.SaicInfoDto;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.*;

@Component("saicCompareDataImporter")
@Slf4j
public class SaicCompareDataImporter extends AbstractDataImporter {

    public static final String FILE_EXTENSION[] = {"json", "JSON"};

    @Autowired
    private SaicInfoService saicInfoService;

    @Autowired
    private CompareCollectRecordService compareCollectRecordService;

    @Autowired
    private CompareRepositoryFinder compareRepositoryFinder;

    @Autowired
    protected Map<String, DataTransformation> dataTransformationMap;

    @Autowired
    private CompareCollectTaskService compareCollectTaskService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CompareDataService compareDataService;

    @Autowired
    private DataSourceService dataSourceService;

//    @Autowired
//    private CompareDataConvertService compareDataConvertService;

    @Override
    public void importData(CompareTaskDto compareTaskDto, DataSourceDto dataSourceDto, MultipartFile file) {

        try {

            if (FilenameUtils.isExtension(file.getOriginalFilename(), FILE_EXTENSION)) {

                //1、校验
                //起先决数据源的作用，为解决工商json数据没有帐号的问题，需要有别的采集任务完成。
                //仍存在一些问题，若别的数据选工商数据为先决数据源。

                List<CompareCollectTaskDto> collectTaskDtoList = compareCollectTaskService.findByCompareTaskId(compareTaskDto.getId());
                if (collectTaskDtoList!=null && collectTaskDtoList.size()>0){
                    boolean flag = false;//是否有采集任务完成
                    for (CompareCollectTaskDto compareCollectTaskDto1:collectTaskDtoList){
                        if (compareCollectTaskDto1.getCollectStatus()==CollectTaskState.done){
                            flag = true;
                            break;
                        }
                    }
                    if (!flag){
                        throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "JSON格式文件导入需先采集其他数据源......");
                    }
                }else {
                    throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "JSON格式文件导入需先采集其他数据源......");
                }

                //2.1、初始化
                log.info("--开始导入工商采集文件[" + file.getOriginalFilename() + "]--");
                int failCount = 0;//失败数量
                int count = 0;//总数
                int times = 0;//导入次数。20次更新一次采集任务状态
                Long startTime1 = System.currentTimeMillis();//开始时间1
                Long startTime2 = 0L;//开始时间2
                Long endTime = 0L;//结束时间
                SaicIdpInfo saicIdpInfo = null;//工商IDP数据对象
                SaicInfoDto saicInfoDto = null;//工商DTO数据对象
                SaicDataTransformation dataTransformation = new SaicDataTransformation();//工商DTO数据转换工具
//                compareDataConvertService.update();


                //2.2、新建工商采集任务
                CompareCollectTaskDto compareCollectTaskDto = compareCollectTaskService.findByCompareTaskIdAndCollectTaskType(compareTaskDto.getId(), DataSourceEnum.SAIC);
                if (compareCollectTaskDto == null) {
                    compareCollectTaskDto = new CompareCollectTaskDto();
                }
                compareCollectTaskDto.setName(dataSourceDto.getName() + "数据导入");
                compareCollectTaskDto.setStartTime(DateUtils.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
                compareCollectTaskDto.setCollectTaskType(DataSourceEnum.SAIC);
                compareCollectTaskDto.setCollectStatus(CollectTaskState.collecting);
                compareCollectTaskDto.setCompareTaskId(compareTaskDto.getId());
                compareCollectTaskDto.setDataSourceId(dataSourceDto.getId());
                compareCollectTaskDto.setFailed(0);
                compareCollectTaskDto.setCount(0);
                compareCollectTaskService.saveCompareCollectTask(compareCollectTaskDto);

                //3.1、根据taskId删除之前存入CompareData的数据。重置式导入，若需要增量式导入则要修改逻辑
                CompareDataRepository<CompareData> repository = compareRepositoryFinder.getRepository(dataSourceDto.getDomain());
                List<CompareData> list = repository.findByTaskId(compareTaskDto.getId());
                if (CollectionUtils.isNotEmpty(list)) {
                    repository.deleteByTaskId(compareTaskDto.getId());
                }
                //3.2、删除之前的采集记录
                log.info("3.2、删除之前的采集记录:compareCollectTaskId:{},dataSourceCode:{}",compareCollectTaskDto.getId(),dataSourceDto.getCode());
                compareCollectRecordService.deleteData(compareCollectTaskDto.getId(),dataSourceDto.getCode());


                //4、处理文件
                CommonsMultipartFile cFile = (CommonsMultipartFile) file;
                DiskFileItem fileItem = (DiskFileItem) cFile.getFileItem();
                JSONReader reader = new JSONReader(new InputStreamReader(fileItem.getInputStream(), "UTF-8"));
                reader.startArray();

                //4.1、处理先决数据源的，采用年检工商导入方式startFetchSaicInfoByFile()，不能确定一个acctNo对应一个DepositorName或者工商的name
                List<CompareDataDto> compareDataDtoList = getCompareDataList(compareTaskDto.getId());
                Map<String, CompareDataDto> compareDataMap = new HashMap<>();
                for (CompareDataDto compareDataDto1 : compareDataDtoList) {
                    compareDataMap.put(compareDataDto1.getDepositorName(), compareDataDto1);
                }

                //5.0、读取文件
                while (reader.hasNext()) {
                    try {
                        //开始时间戳2
                        startTime2 = System.currentTimeMillis();

                        //5.1、获取IDP返回的工商信息
                        OutInfoOne outInfoOne = reader.readObject(OutInfoOne.class);

                        if (StringUtils.equalsIgnoreCase(outInfoOne.getStatus(), "success")) {
                            saicIdpInfo = outInfoOne.getResult();

//                            有必要保存导入的工商数据么？
//                            saicInfoService.saveSaicInfo(saicIdpInfo);

                            //5.1.0、IDP无效数据校验
                            if (saicIdpInfo.getName()==null||"".equals(saicIdpInfo.getName())){
                                log.warn("工商导入失败，无效记录："+JSON.toJSONString(saicIdpInfo));
                                continue;
                            }


                            CompareDataDto compareDataDto = new CompareDataDto();//比对数据DTO对象
                            try {

                                saicInfoDto = ConverterService.convert(saicIdpInfo,SaicInfoDto.class);
                                dataTransformation.dataTransformation(compareDataDto, saicInfoDto);

                                //5.1.1判断是否单边
                                if (compareDataMap.containsKey(saicInfoDto.getName())){

                                    //5.1.2、保存CompareData数据
                                    CompareDataDto compareDataDto1 = compareDataMap.get(saicInfoDto.getName());
                                    compareDataDto.setAcctNo(compareDataDto1.getAcctNo());
                                    compareDataDto.setOrganCode(compareDataDto1.getOrganCode());
                                    compareDataDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                                    compareDataDto.setTaskId(compareTaskDto.getId());
                                    compareDataService.saveCompareData(compareDataDto,dataSourceDto);

                                    //5.1.3、保存CompareCollectRecord采集成功记录
                                    CompareCollectRecordDto compareCollectRecordDto = new CompareCollectRecordDto();
                                    compareCollectRecordDto.setAcctNo(compareDataDto.getAcctNo());
                                    compareCollectRecordDto.setDepositorName(saicIdpInfo.getName());
                                    compareCollectRecordDto.setRegNo(StringUtils.isBlank(saicIdpInfo.getUnitycreditcode()) ? saicIdpInfo.getUnitycreditcode() : saicIdpInfo.getRegistno());
                                    compareCollectRecordDto.setCompareTaskId(compareTaskDto.getId());
                                    compareCollectRecordDto.setCollectState(CollectState.success);
                                    compareCollectRecordDto.setDataSourceType(dataSourceDto.getCode());
                                    compareCollectRecordDto.setCollectTaskId(compareCollectTaskDto.getId());
                                    compareCollectRecordService.saveCompareCollectRecord(compareCollectRecordDto);

                                }else {
                                    //5.2、保存CompareCollectRecord采集失败记录（单边）
                                    CompareCollectRecordDto compareCollectRecordDto = new CompareCollectRecordDto();
                                    compareCollectRecordDto.setDepositorName(saicIdpInfo.getName());
                                    compareCollectRecordDto.setRegNo(StringUtils.isBlank(saicIdpInfo.getUnitycreditcode()) ? saicIdpInfo.getUnitycreditcode() : saicIdpInfo.getRegistno());
                                    compareCollectRecordDto.setCompareTaskId(compareTaskDto.getId());
                                    compareCollectRecordDto.setCollectState(CollectState.fail);
                                    compareCollectRecordDto.setDataSourceType(dataSourceDto.getCode());
                                    compareCollectRecordDto.setCollectTaskId(compareCollectTaskDto.getId());
                                    String jsonString = JSON.toJSONString(saicIdpInfo);
                                    compareCollectRecordDto.setFailReason("工商导入失败：工商单边数据" + (jsonString.length()>500?JSON.toJSONString(saicIdpInfo).substring(0,500):JSON.toJSONString(saicIdpInfo)));
                                    compareCollectRecordService.saveCompareCollectRecord(compareCollectRecordDto);
                                    failCount++;
                                }

                            } catch (Exception e) {

                                //5.3、保存CompareCollectRecord采集失败记录
                                e.printStackTrace();
                                CompareCollectRecordDto compareCollectRecordDto = compareCollectRecordService.findByCompareTaskIdAndAcctNoAndDataSourceType(compareTaskDto.getId(), compareDataDto.getAcctNo(), dataSourceDto.getCode());
                                if (compareCollectRecordDto == null) {
                                    compareCollectRecordDto = new CompareCollectRecordDto();
                                }
                                compareCollectRecordDto.setAcctNo(compareDataDto.getAcctNo());
                                compareCollectRecordDto.setDepositorName(saicIdpInfo.getName());
                                compareCollectRecordDto.setRegNo(StringUtils.isBlank(saicIdpInfo.getUnitycreditcode()) ? saicIdpInfo.getUnitycreditcode() : saicIdpInfo.getRegistno());
                                compareCollectRecordDto.setCompareTaskId(compareTaskDto.getId());
                                compareCollectRecordDto.setCollectState(CollectState.fail);
                                compareCollectRecordDto.setDataSourceType(dataSourceDto.getCode());
                                compareCollectRecordDto.setCollectTaskId(compareCollectTaskDto.getId());
                                String jsonString = JSON.toJSONString(saicIdpInfo);
                                compareCollectRecordDto.setFailReason("工商导入失败：获取数据" + (jsonString.length()>500?JSON.toJSONString(saicIdpInfo).substring(0,500):JSON.toJSONString(saicIdpInfo)));
                                compareCollectRecordService.saveCompareCollectRecord(compareCollectRecordDto);
                                failCount++;
                            }

                            //结束时间戳
                            endTime = System.currentTimeMillis();
                            log.info(saicInfoDto.getName()+",工商数据插入用时：" + (endTime - startTime2)+"ms");

                            //5.4、更新采集任务状态
                            times++;
                            count++;
                            if (times>19){
                                compareCollectTaskDto.setCount(count);
                                compareCollectTaskDto.setFailed(failCount);
                                compareCollectTaskService.saveCompareCollectTask(compareCollectTaskDto);
                                times=0;
                            }
                        } else {
                            log.warn("获得工商信息失败，内容为" + reader.readString());
                        }

                    } catch (Exception e) {
                        log.error("获得工商信息异常，内容为" + reader.readString());
                        e.printStackTrace();
                    }
                }
                reader.endArray();
                reader.close();

                //结束时间戳
                endTime = System.currentTimeMillis();
                log.info("工商json数据导入用时：" + (endTime - startTime1)+"ms");

                //6、最后更新采集任务状态
                compareCollectTaskDto.setCollectStatus(CollectTaskState.done);
                compareCollectTaskDto.setCount(count);
                compareCollectTaskDto.setFailed(failCount);
                compareCollectTaskDto.setEndTime(DateUtils.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
                compareCollectTaskDto.setIsCompleted(CompanyIfType.Yes);
                compareCollectTaskService.saveCompareCollectTask(compareCollectTaskDto);
                log.info("比对工商JSON数据导入有效总数："+count+"，导入失败："+failCount);

            } else {
                super.importData(compareTaskDto, dataSourceDto, file);
            }

        } catch (BizServiceException e) {
            if (e.getMessage().contains("JSON格式文件导入需先采集其他数据源")){
                log.error("比对工商JSON数据导入失败", e);
                throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "JSON格式文件导入需先采集其他数据源......");
            }else {
                log.error("比对工商EXCEL数据导入失败", e);
                throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "导入失败......");
            }
        } catch (Exception e) {
            log.error("比对工商数据导入失败", e);
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "导入失败......");
        }
    }

    /**
     * 获取采集记录的compareDataDtoList，作为先决数据源，
     * @return
     */
    private List<CompareDataDto> getCompareDataList(Long taskId) {
        Set<String> acctNoSet = new HashSet<>();
        List<CompareDataDto> compareDataDtoList = new ArrayList<>();

        //获取所有已完成的采集任务
        List<CompareCollectTaskDto> collectTaskDtoList = compareCollectTaskService.findByCompareTaskId(taskId);
        if (collectTaskDtoList!=null && collectTaskDtoList.size()>0){
            for (CompareCollectTaskDto compareCollectTaskDto:collectTaskDtoList){
                if (compareCollectTaskDto.getCollectStatus()==CollectTaskState.done){

                    //获取所有的CompareDataDto存到compareDataDtoList
                    List<CompareDataDto> list = compareDataService.getCompareData(taskId, dataSourceService.findById(compareCollectTaskDto.getDataSourceId()));
                    for (CompareDataDto compareDataDto: list){
                        //去重导入
                        if (!acctNoSet.contains(compareDataDto.getAcctNo()) && StringUtils.isNotBlank(compareDataDto.getDepositorName())){
                            acctNoSet.add(compareDataDto.getAcctNo());
                            compareDataDtoList.add(compareDataDto);
                        }
                    }
                }
            }
        }

        return compareDataDtoList;
    }
}