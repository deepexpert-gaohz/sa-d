package com.ideatech.ams.compare.spi.dataImporter;

import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.compare.dto.*;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.enums.CollectState;
import com.ideatech.ams.compare.enums.CollectTaskState;
import com.ideatech.ams.compare.enums.CompareState;
import com.ideatech.ams.compare.enums.DataSourceEnum;
import com.ideatech.ams.compare.executor.CompareDataImporterExecutor;
import com.ideatech.ams.compare.service.*;
import com.ideatech.ams.compare.vo.CompareFieldExcelRowVo;
import com.ideatech.ams.system.dict.dto.OptionDto;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.excel.util.ImportExcel;
import com.ideatech.common.excel.util.PoiExcelUtils;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractDataImporter implements DataImporter {

    @Autowired
    private CompareRuleFieldsService compareRuleFieldsService;

    @Autowired
    private CompareFieldService compareFieldService;

    @Autowired
    private CompareDataService compareDataService;

    @Autowired
    private CompareCollectRecordService compareCollectRecordService;

    @Autowired
    private CompareCollectTaskService compareCollectTaskService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private CompareTaskService compareTaskService;

    @Autowired
    protected TransactionUtils transactionUtils;

    @Value("${compare.import.coreTransform.use:false}")
    private Boolean coreTransform;

    @Value("${compare.import.executorNum:10}")
    private int compareImportExecutorNum;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private CompareDataConvertService compareDataConvertService;

    private Map<String, String> industryCodeDicMap = new HashMap<>();

    private Map<String, String> depositorTypeDicMap = new HashMap<>();

    private Map<String, String> fileTypeDicMap = new HashMap<>();

    private Map<String, String> acctTypeDicMap = new HashMap<>();

    private Map<String, String> accountStatusDicMap = new HashMap<>();

    private Map<String, String> legalIdcardTypeDicMap = new HashMap<>();

    private List<Future<Integer>> futureList;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void importData(final CompareTaskDto compareTaskDto, DataSourceDto dataSourceDto, MultipartFile file) {

        try{
            log.info("修改比对任务状态为采集中......");
            if(compareTaskDto.getState() != CompareState.COLLECTING){
                //多个数据源同时导入数据时新开事务
                transactionUtils.executeInNewTransaction(new TransactionCallback() {
                    @Override
                    public void execute() throws Exception {
                        compareTaskDto.setState(CompareState.COLLECTING);
                        compareTaskService.save(compareTaskDto);
                    }
                });
            }

            CompareCollectTaskDto compareCollectTaskDto = compareCollectTaskService.findByCompareTaskIdAndDataSourceId(compareTaskDto.getId(), dataSourceDto.getId());
            if (compareCollectTaskDto == null) {
                compareCollectTaskDto = new CompareCollectTaskDto();
            }
            compareCollectTaskDto.setName(dataSourceDto.getName() + "数据导入");
            compareCollectTaskDto.setStartTime(DateUtils.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
            compareCollectTaskDto.setCollectTaskType(DataSourceEnum.str2enum(dataSourceDto.getDataType().getValue()));
            compareCollectTaskDto.setCollectStatus(CollectTaskState.collecting);
            compareCollectTaskDto.setCompareTaskId(compareTaskDto.getId());
            compareCollectTaskDto.setDataSourceId(dataSourceDto.getId());
            compareCollectTaskService.saveCompareCollectTask(compareCollectTaskDto);
            ResultDto dto = new ResultDto();

            //循环读取sheet内容
            Workbook workbook = null;
            String filenameExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            //根据后缀判断是03还是07版本excel
            if (filenameExtension.equalsIgnoreCase("xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if (filenameExtension.equalsIgnoreCase("xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            }

            Integer failCount = 0;
            Map<String, OrganizationDto> orgMap = null;
            List<CompareFieldExcelRowVo> dataList = null;
            List<CompareFieldExcelRowVo> allDataList = new ArrayList<>();
            for(int i = 0 ; i < workbook.getNumberOfSheets(); i ++) {
                Thread.sleep(1000 * 1);
                ImportExcel importExcel = new ImportExcel(file, 0, i);
                if (importExcel.getRow(0) == null) { //sheet页面的非空判断
                    continue;
                }

                if (importExcel.getRow(0).getPhysicalNumberOfCells() == 0) {
                    throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "导入失败，错误的模板");
                } else {
                    //查找比对规则
                    Long compareRuleId = compareTaskDto.getCompareRuleId();
                    //获取比对规则字段
                    List<CompareRuleFieldsDto> compareRuleFieldsDtoList = compareRuleFieldsService.findByCompareRuleId(compareRuleId);
                    orgMap = getOrganMap();
                    log.info("正在获取比对规则字段......");
                    if (CollectionUtils.isEmpty(compareRuleFieldsDtoList)) {
                        throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "无法找到比对规则字段......");
                    }
                    List<String> compareRuleFields = new ArrayList<String>();
                    for (CompareRuleFieldsDto dto1 : compareRuleFieldsDtoList) {
                        compareRuleFields.add(compareFieldService.findById(dto1.getCompareFieldId()).getName());
                    }
                    //读取excel第一行
                    log.info("正在读取导入比对文件第一列比对字段......");

                    CommonsMultipartFile cFile = (CommonsMultipartFile) file;
                    DiskFileItem fileItem = (DiskFileItem) cFile.getFileItem();
                    InputStream inputStream = fileItem.getInputStream();
                    List<String> firstLine = PoiExcelUtils.readFistLine(inputStream, filenameExtension);
                    //比对规则字段跟导入字段进行比较
                    log.info("正在比较比对字段规则跟导入字段......");
                    for (String fieldName : compareRuleFields) {
                        if (!firstLine.contains(fieldName)) {
                            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "导入模板字段跟规则比对勾选字段不一致，请核对后再次导入.....");
                        }
                    }
                    log.info("解析比对导入文件......");
                    //excel进行解析
                    dataList = importExcel.getDataList(CompareFieldExcelRowVo.class);
                    allDataList.addAll(dataList);

                    Set<String> organCodeList = new HashSet<>();
                    if (CollectionUtils.isNotEmpty(dataList)) {
                        for (CompareFieldExcelRowVo compareFieldExcelRowVo : dataList) {
                            organCodeList.add(compareFieldExcelRowVo.getOrganCode());
                        }
                    }
                    if (CollectionUtils.isNotEmpty(organCodeList)) {
                        for (String organCode : organCodeList) {
                            OrganizationDto organizationDto = orgMap.get(organCode);
                            if (organizationDto == null) {
                                throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "机构号：" + organCode + "不存在，请检查后再次导入......");
                            }
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(allDataList)) {
                    if (i == 0) {  //excel多个sheet遍历第一次循环时删除
                        log.info("查询历史导入数据并删除......");
                        compareDataService.delCompareData(compareTaskDto.getId(), dataSourceDto);
                        //删除采集任务明细表
                        compareCollectRecordService.deleteData(compareTaskDto.getId(), dataSourceDto.getCode());
                    }
                    log.info("插入比对数据......");

                    //准备字典转换数据
                    if (coreTransform && dataSourceDto.getDataType() == DataSourceEnum.CORE) {
                        log.info("准备字典转换数据.......");
                        readyDicMap();
                        compareDataConvertService.update();
                    }
                }
            }

            clearFuture();
            Long startTime = System.currentTimeMillis();
            Map<String, List<CompareFieldExcelRowVo>> batchTokens = getBatchTokens(allDataList);
            if (MapUtils.isNotEmpty(batchTokens) && batchTokens.size() > 0) {
                CompareDataImporterExecutor executor = null;
                for (String batch : batchTokens.keySet()) {
                    executor = new CompareDataImporterExecutor(batchTokens.get(batch));
                    executor.setCompareDataService(compareDataService);
                    executor.setCompareCollectRecordService(compareCollectRecordService);
                    executor.setCoreTransform(coreTransform);
                    executor.setDataSourceDto(dataSourceDto);
                    executor.setIndustryCodeDicMap(industryCodeDicMap);
                    executor.setDepositorTypeDicMap(depositorTypeDicMap);
                    executor.setFileTypeDicMap(fileTypeDicMap);
                    executor.setAcctTypeDicMap(acctTypeDicMap);
                    executor.setAccountStatusDicMap(accountStatusDicMap);
                    executor.setLegalIdcardTypeDicMap(legalIdcardTypeDicMap);
                    executor.setCompareTaskDto(compareTaskDto);
                    executor.setOrgMap(orgMap);
                    executor.setCompareCollectTaskDto(compareCollectTaskDto);
                    executor.setFailCount(failCount);
                    futureList.add(taskExecutor.submit(executor));
                }

                try {
                    failCount = valiCollectCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            log.info("数据插入全部线程执行结束");
            System.gc();

            Long endTime = System.currentTimeMillis();
            log.info("数据插入用时：" + (endTime - startTime));
            dto.setCode(ResultCode.ACK);
            dto.setMessage("导入成功");
            log.info("插入成功......");

            compareCollectTaskDto = compareCollectTaskService.findByCompareTaskIdAndDataSourceId(compareTaskDto.getId(), dataSourceDto.getId());
            if (compareCollectTaskDto != null) {
                compareCollectTaskDto.setCollectStatus(CollectTaskState.done);
                compareCollectTaskDto.setCount(allDataList.size());
                compareCollectTaskDto.setFailed(failCount);
                compareCollectTaskDto.setEndTime(DateUtils.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
                compareCollectTaskDto.setIsCompleted(CompanyIfType.Yes);
                compareCollectTaskService.saveCompareCollectTask(compareCollectTaskDto);
            }
            log.info("保存compareCollectTaskDto返回resultDto");
        }catch (Exception e) {
            log.error("导入失败，",e);
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }

    private Map<String, List<CompareFieldExcelRowVo>> getBatchTokens(List<CompareFieldExcelRowVo> allDataList) {
        Map<String, List<CompareFieldExcelRowVo>> returnMap = new HashMap<>(16);
        if (allDataList != null && allDataList.size() > 0) {
            int allLeafSum = allDataList.size();
            int tokensNum = (allLeafSum / compareImportExecutorNum) + 1;
            int num = 0;
            int batchNum = 0;
            List<CompareFieldExcelRowVo> batchTokens = new ArrayList<>();
            for (CompareFieldExcelRowVo compareFieldExcelRowVo : allDataList) {

                if (num > 0 && num % tokensNum == 0) {
                    batchNum++;
                    returnMap.put("第" + batchNum + "线程", batchTokens);
                    batchTokens = new ArrayList<>();
                }
                batchTokens.add(compareFieldExcelRowVo);
                num++;
            }
            returnMap.put("第" + (batchNum + 1) + "线程", batchTokens);
        }
        return returnMap;
    }


    public Map<String, OrganizationDto> getOrganMap() {
        Map<String, OrganizationDto> map = new HashMap<>();
        List<OrganizationDto> list = organizationService.listAll();
        for (OrganizationDto organizationDto : list) {
            map.put(organizationDto.getCode(), organizationDto);
        }
        return map;
    }

    @Override
    public void importData(CompareTaskDto task, DataSourceDto dataSource, String fileName) throws Exception {
//        return null;
    }

    /**
     * 准备各个字典项
     */
    private void readyDicMap(){

        //清除字典转换数据
        clearDicMap();

        //行业归属
        List<OptionDto> industryCode = dictionaryService.findOptionsByDictionaryName("core2pbc-industryCode");
        for (OptionDto option : industryCode) {
            industryCodeDicMap.put(option.getName(), option.getValue());
        }

        //存款人类别
        List<OptionDto> depositorType = dictionaryService.findOptionsByDictionaryName("core2pbc-depositorType");
        for (OptionDto option : depositorType) {
            depositorTypeDicMap.put(option.getName(), option.getValue());
        }

        //证明文件种类
        List<OptionDto> fileType = dictionaryService.findOptionsByDictionaryNameStartWith("core2pbc-fileType-");
        for (OptionDto option : fileType) {
            fileTypeDicMap.put(option.getName(), option.getValue());
        }

        //账户性质
        List<OptionDto> acctType = dictionaryService.findOptionsByDictionaryName("core2pbc-acctType");
        for (OptionDto option : acctType) {
            acctTypeDicMap.put(option.getName(), option.getValue());
        }

        //账户状态
        List<OptionDto> accountStatus = dictionaryService.findOptionsByDictionaryName("core2pbc-accountStatus");
        for (OptionDto option : accountStatus) {
            accountStatusDicMap.put(option.getName(), option.getValue());
        }

        //证件种类
        List<OptionDto> legalIdcardType = dictionaryService.findOptionsByDictionaryName("core2pbc-legalIdcardType");
        for (OptionDto option : legalIdcardType) {
            legalIdcardTypeDicMap.put(option.getName(), option.getValue());
        }

    }


    /**
     * 清除字典信息
     */
    private void clearDicMap() {
        industryCodeDicMap.clear();
        depositorTypeDicMap.clear();
        fileTypeDicMap.clear();
        acctTypeDicMap.clear();
        accountStatusDicMap.clear();
        legalIdcardTypeDicMap.clear();
    }

    /**
     * 判断采集是否完成
     *
     * @param
     * @throws Exception
     */
    private Integer valiCollectCompleted() throws Exception {
        Integer failCount = 0;
        while(futureList.size()>0){
            for (Iterator<Future<Integer>> iterator = futureList.iterator(); iterator.hasNext();) {
                Future<Integer> future = iterator.next();
                if(future.isDone()){
                    failCount += future.get();
                    iterator.remove();
                }
            }
            // 暂停10s
            TimeUnit.SECONDS.sleep(10);
        }

        return failCount;
    }

    private void clearFuture(){
        if(futureList !=null && futureList.size()>0){
            for (Iterator<Future<Integer>> iterator = futureList.iterator(); iterator.hasNext();) {
                Future<Integer> future = iterator.next();
                if(future.isDone()){
                    iterator.remove();
                }else{
                    future.cancel(true);
                    iterator.remove();
                }
            }
        }
        futureList = new ArrayList<>();
    }
}
