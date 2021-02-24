package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dao.BatchSuspendDao;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.BatchSuspendDto;
import com.ideatech.ams.account.dto.BatchSuspendSearchDto;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.entity.BatchSuspendPo;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.AcctBigType;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.*;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.account.spec.BatchSuspendSpec;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.system.batch.enums.BatchTypeEnum;
import com.ideatech.ams.system.batch.service.BatchService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.dict.dto.OptionDto;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
@Slf4j
public class BatchSuspendServiceImpl extends BaseServiceImpl<BatchSuspendDao, BatchSuspendPo, BatchSuspendDto> implements BatchSuspendService {

    private final static String TMP_EXTENSION = ".tmp";

    private final static String FINISH_EXTENSION = ".finish";

    @Autowired
    BatchSuspendDao batchSuspendDao;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    AllBillsPublicService allBillsPublicService;

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    TransactionUtils transactionUtils;

    @Autowired
    UserService userService;

    @Autowired
    private BatchService batchService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private CustomerPublicLogService customerPublicLogService;

    @Value("${import.file.batch-suspend.extension}")
    private String extension;

    @Value("${import.file.batch-suspend.folder.begin}")
    private String beginFolder;

    @Value("${import.file.batch-suspend.folder.finish}")
    private String finishFolder;

    @Value("${import.file.batch-suspend.split}")
    private String split;

    @Value("${import.file.batch-suspend.charset}")
    private String charset;

    @Autowired
    private ConfigService configService;

    private Map<String, Integer> headMap;

    private Map<String, String> fieldMap;

    private Map<String, OrganizationDto> organMap;

    private Map<String, OrganizationDto> organFullIdMap;

    private Map<String, String> acctTypeDicMap;

    private Map<String, String> acctBigTypeDicMap;

    private final static String BATCH_SUSPEND_AUTO_SYNC = "batchSuspendAutoSyncEnabled";

    public BatchSuspendServiceImpl() {
        fieldMap = new CaseInsensitiveMap();
        ReflectionUtils.doWithFields(BatchSuspendDto.class, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                fieldMap.put(field.getName(), field.getName());
            }
        });
    }

    @Override
    public Boolean isProcessing(String batchNo) {
        return batchSuspendDao.countByBatchNoAndProcessed(batchNo, CompanyIfType.No) > 0;
    }

    @Override
    public BatchSuspendDto isProcessing() {
        BatchSuspendPo batchSuspendPo = batchSuspendDao.findFirstByOrderByBatchNoDesc();
        BatchSuspendDto batchSuspendDto = new BatchSuspendDto();
        ConverterService.convert(batchSuspendPo, batchSuspendDto);
        if (batchSuspendDto != null && StringUtils.isNotBlank(batchSuspendDto.getBatchNo())) {
            if (isProcessing(batchSuspendDto.getBatchNo())) {
                return batchSuspendDto;
            }
        }
        return null;
    }

    @Override
    public ObjectRestResponse getProcessByBatchNo(String batchNo) {
        Map<String, Object> map = new HashMap<>();
        List<BatchSuspendPo> batchSuspendPoList = batchSuspendDao.findByBatchNo(batchNo);
        long process = batchSuspendPoList.size();
        long processed = 0;
        long success = 0;
        long fail = 0;
        for (BatchSuspendPo batchSuspendPo : batchSuspendPoList) {
            if (batchSuspendPo.getProcessed() == CompanyIfType.Yes) {
                processed++;
            }
            if (batchSuspendPo.getSyncStatus() == CompanyIfType.Yes) {
                success++;
            }
        }
        fail = processed - success;
        map.put("process", process);
        map.put("processed", processed);
        map.put("success", success);
        map.put("fail", fail);
        return new ObjectRestResponse<>().rel(true).result(map);
    }

    @Override
    public void doProcess(String batchNo) {
        List<BatchSuspendPo> batchSuspendPoList = batchSuspendDao.findByBatchNoAndProcessed(batchNo, CompanyIfType.No);
        if (CollectionUtils.isNotEmpty(batchSuspendPoList)) {
            for (BatchSuspendPo batchSuspendPo : batchSuspendPoList) {
                AllBillsPublicDTO billsPublic = new AllBillsPublicDTO();
                billsPublic.setDepositorName(batchSuspendPo.getDepositorName());
                billsPublic.setAcctNo(batchSuspendPo.getAcctNo());
                billsPublic.setCustomerNo(batchSuspendPo.getCustomerNo());
                billsPublic.setBillType(BillType.ACCT_SUSPEND);
                billsPublic.setAcctType(batchSuspendPo.getAcctType());
                billsPublic.setAcctBigType(batchSuspendPo.getAcctBigType());
                billsPublic.setOrganFullId(batchSuspendPo.getOrganFullId());
                billsPublic.setOrganCode(batchSuspendPo.getOrganCode());
                //此处先存行内机构号，后续流水处理会转换为人行机构号
                billsPublic.setBankCode(batchSuspendPo.getOrganCode());
                //企业名称从存款人名称复制
                billsPublic.setAcctName(batchSuspendPo.getDepositorName());
                //如果小类无值,从账户性质大类赋值小类
                allBillsPublicService.acctBigType2AcctType(billsPublic);
                try {
                    boolean realSync = true;
                    List<ConfigDto> tpoAutoSyncConfig = configService.findByKey(BATCH_SUSPEND_AUTO_SYNC);
                    if (CollectionUtils.isNotEmpty(tpoAutoSyncConfig)) {
                        realSync = Boolean.valueOf(tpoAutoSyncConfig.get(0).getConfigValue()).booleanValue();
                    }
                    sync(billsPublic, batchSuspendPo, realSync);
                    batchSuspendPo.setSyncStatus(CompanyIfType.Yes);
                } catch (Exception e) {
                    log.error("自动报送久悬处理异常", e);
                    batchSuspendPo.setErrorMessage(e.getMessage());
                    batchSuspendPo.setSyncStatus(CompanyIfType.No);
                } finally {
                    batchSuspendPo.setProcessed(CompanyIfType.Yes);
                    batchSuspendDao.save(batchSuspendPo);
                }
            }
        }

    }

    @Override
    public void finishByBatchNo(String batchNo) {
        batchSuspendDao.updateProcessedByBatchNo(batchNo);
    }

    @Override
    public void process() throws Exception {
        File beginFolderFile = new File(beginFolder);
        log.info("查找久悬文件......");
        if (!beginFolderFile.exists()) {
            log.info("文件夹无数据....");
            beginFolderFile.mkdirs();
            return;
        }
        for (File file : beginFolderFile.listFiles(new IdeaFileFilter())) {
            log.info("开始处理久悬数据...");
            File tmpFile = changeFile2Tmp(file);
            //开始处理修改文件
            String batchNo = batchService.createBatch(file.getName(), FileUtils.sizeOfAsBigInteger(tmpFile).longValue(), BatchTypeEnum.BATCH_SUSPEND);
            List<BatchSuspendDto> batchSuspendDtos = readTxtFile(tmpFile);
            initSave(batchNo, BatchSuspendSourceEnum.BATCH_FILE, batchSuspendDtos);
            doProcess(batchNo);
            batchService.finishBatch(batchNo);
            //移除文件
            removeFile(tmpFile);
        }
    }

    @Override
    public List<BatchSuspendDto> readTxtFile(File file) {
        if (MapUtils.isNotEmpty(headMap)) {
            headMap.clear();
        }
        LineIterator iterator = null;
        List<BatchSuspendDto> batchSuspendDtoList = new ArrayList<>(16);
        BatchSuspendDto batchSuspendDto = null;
        int lineNum = 0;
        try {
            iterator = FileUtils.lineIterator(file, charset);
            while (iterator.hasNext()) {
                lineNum++;
                String line = iterator.nextLine();
                String[] data = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, split);
                //读取表头
                if (MapUtils.isEmpty(headMap)) {
                    headMap = new HashMap<>(16);
                    for (int i = 0; i < data.length; i++) {
                        String field = StringUtils.trim(data[i]);
                        headMap.put(field, i);
                    }
                    continue;
                } else {
                    //读取文件
                    try {
                        batchSuspendDto = readLine2Bean(data);
                    } catch (Exception e) {
                        log.error("批量久悬数据文件" + file.getName() + "第" + (lineNum) + "行内容转换出错，未保存！", e);
                        continue;
                    }
                    //开始处理逐条数据
                    batchSuspendDtoList.add(batchSuspendDto);
                }
            }
        } catch (IOException e) {
            log.error("文件处理异常", e);
        } finally {
            iterator.close();
        }
        return batchSuspendDtoList;
    }

    @Override
    public void initSave(String batchNo, BatchSuspendSourceEnum source, List<BatchSuspendDto> batchSuspendDtos) {
        setOrganMap();
        log.info("获取账户久悬数据账号集合");
        Set<String> acctNoList = accountsAllService.findAcctNoFromAccountStatus(AccountStatus.suspend);
        for (BatchSuspendDto batchSuspendDto : batchSuspendDtos) {
            //账管已久悬数据无需重复添加  CN-225
            if(acctNoList.contains(batchSuspendDto.getAcctNo())){
                log.info("账号：" + batchSuspendDto.getAcctNo() + "已久悬，无需重复添加;");
                continue;
            }
            String organCode = batchSuspendDto.getOrganCode();
            if (organMap.containsKey(organCode)) {
                batchSuspendDto.setOrganFullId(organMap.get(organCode).getFullId());
            }
            //基于页面选择批量久悬时用到
            if(StringUtils.isNotBlank(batchSuspendDto.getOrganFullId()) && StringUtils.isBlank(batchSuspendDto.getOrganCode())){
                if(organFullIdMap.containsKey(batchSuspendDto.getOrganFullId())){
                    batchSuspendDto.setOrganCode(organFullIdMap.get(batchSuspendDto.getOrganFullId()).getCode());
                }
            }
            batchSuspendDto.setBatchNo(batchNo);
            batchSuspendDto.setType(source);
            batchSuspendDto.setSyncStatus(CompanyIfType.No);
            batchSuspendDto.setProcessed(CompanyIfType.No);
            save(batchSuspendDto);
        }
    }

    @Override
    public TableResultResponse query(BatchSuspendSearchDto searchDto, Pageable pageable) {
        setOrganMap();
        Page<BatchSuspendPo> page = getBaseDao().findAll(new BatchSuspendSpec(searchDto), pageable);

        List<BatchSuspendDto> batchSuspendDtoList = new ArrayList<>(16);
        BatchSuspendDto batchSuspendDto;
        //处理查询机构名称
        for (BatchSuspendPo batchSuspendPo : page.getContent()) {
            batchSuspendDto = new BatchSuspendDto();
            BeanCopierUtils.copyProperties(batchSuspendPo, batchSuspendDto);
            if (organMap.containsKey(batchSuspendDto.getOrganCode())) {
                batchSuspendDto.setOrganName(organMap.get(batchSuspendDto.getOrganCode()).getName());
            }
            batchSuspendDtoList.add(batchSuspendDto);
        }
        TableResultResponse response = new TableResultResponse((int) page.getTotalElements(), batchSuspendDtoList);
        return response;
    }

    @Override
    public void submitSync(Long[] ids, Long[] billIds) {
        String errorMsg = "";
        for (int i = 0; i < ids.length; i++) {
            BatchSuspendDto dto = null;
            try {
                Long id = ids[i];
                Long billId = billIds[i];
                //先判断本地上报状态是否正确
                dto = findById(id);
                if (dto.getSyncStatus() == CompanyIfType.Yes) {
                    continue;
                }
                AllBillsPublicDTO billsPublic = allBillsPublicService.findOne(billId);
                //补齐机构数据
                setOrganData(billsPublic, dto.getOrganCode());
                allBillsPublicService.syncAndUpdateStaus(true, false, billsPublic, userService.findById(SecurityUtils.getCurrentUserId()), CompanySyncOperateType.personSyncType);
                dto.setSyncStatus(CompanyIfType.Yes);
                save(dto);
            } catch (Exception e) {
                log.error("久悬页面提交报送异常", e);
                errorMsg = e.getMessage();
                if (dto != null) {
                    dto.setErrorMessage(errorMsg);
                    save(dto);
                }
                continue;
            }
        }

        if (StringUtils.isNotBlank(errorMsg)) {
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, errorMsg);
        }
    }

    private void setOrganData(AllBillsPublicDTO billsPublic, String organCode) {
        setOrganMap();
        OrganizationDto organizationDto = organMap.get(organCode);
        if (organizationDto != null) {
            billsPublic.setBankCode(organizationDto.getPbcCode());
        }
    }

    private void setOrganMap() {
        if (MapUtils.isEmpty(organMap)) {
            organMap = new HashMap<>(16);
            organFullIdMap = new HashMap<>(16);
            List<OrganizationDto> orgList = organizationService.listAll();
            for (OrganizationDto organizationDto : orgList) {
                organMap.put(organizationDto.getCode(), organizationDto);
                organFullIdMap.put(organizationDto.getFullId(), organizationDto);
            }
        }
    }

    private void setDicMap() {
        if (MapUtils.isEmpty(acctTypeDicMap)) {
            acctTypeDicMap = new HashMap<>(16);
            //获取操作类型字典
            List<OptionDto> acctTypeOptions = dictionaryService.listOptionByDictName("core2pbc-acctType");
            for (OptionDto acctTypeOption : acctTypeOptions) {
                acctTypeDicMap.put(acctTypeOption.getName(), acctTypeOption.getValue());
            }
        }
        if (MapUtils.isEmpty(acctBigTypeDicMap)) {
            acctBigTypeDicMap = new HashMap<>(16);
            //获取操作类型字典
            List<OptionDto> options = dictionaryService.listOptionByDictName("core2pbc-acctBigType");
            for (OptionDto option : options) {
                acctBigTypeDicMap.put(option.getName(), option.getValue());
            }
        }
    }


    private BatchSuspendDto readLine2Bean(String[] data) throws InvocationTargetException, IllegalAccessException {
        setDicMap();
        BatchSuspendDto batchSuspendDto = new BatchSuspendDto();
        for (Map.Entry<String, Integer> headEntry : headMap.entrySet()) {
            String field = headEntry.getKey();
            int index = headEntry.getValue();
            String value = StringUtils.trimToEmpty(data[index]);
            if (StringUtils.isNotBlank(value)) {
                if (StringUtils.equalsIgnoreCase(field, "acctType")) {
                    if (MapUtils.isNotEmpty(acctTypeDicMap)) {
                        try {
                            value = acctTypeDicMap.get(value);
                            BeanUtils.setProperty(batchSuspendDto, fieldMap.get(field), CompanyAcctType.valueOf(value));
                        } catch (Exception e) {
                            log.error("批量久悬acctType字段映射赋值异常", e);
                        }
                    }
                } else if (StringUtils.equalsIgnoreCase(field, "acctBigType")) {
                    if (MapUtils.isNotEmpty(acctBigTypeDicMap)) {
                        try {
                            value = acctBigTypeDicMap.get(value);
                            BeanUtils.setProperty(batchSuspendDto, fieldMap.get(field), AcctBigType.valueOf(value));
                        } catch (Exception e) {
                            log.error("批量久悬acctBigType字段映射赋值异常", e);
                        }
                    }
                } else {
                    //去除空格
                    BeanUtils.setProperty(batchSuspendDto, fieldMap.get(field), value);
                }
            }
        }
        return batchSuspendDto;
    }

    private File changeFile2Tmp(File file) {
        File tmpFile = new File(file.getParent(), FilenameUtils.getBaseName(file.getName()) + TMP_EXTENSION);
        file.renameTo(tmpFile);
        return tmpFile;
    }

    private void removeFile(File file) {
        File finishFile = new File(file.getParent(), FilenameUtils.getBaseName(file.getName()) + "-" + System.currentTimeMillis() + FINISH_EXTENSION);
        file.renameTo(finishFile);
        try {
            FileUtils.moveFileToDirectory(finishFile, new File(finishFolder), true);
        } catch (IOException e) {
            log.error("文件移除异常", e);
        }
    }

    class IdeaFileFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return (FilenameUtils.isExtension(name, extension));
        }
    }

    protected void sync(final AllBillsPublicDTO billsPublic, final BatchSuspendPo batchSuspendPo, Boolean sync) throws Exception {
        //保存
        OrganizationDto organizationDto = organizationService.findByOrganFullId(billsPublic.getOrganFullId());
        String currentUserName = SecurityUtils.getCurrentUsername();
        UserDto userDto = null;
        if (StringUtils.isNotBlank(currentUserName)) {
            userDto = userService.findByUsername(currentUserName);
        } else {
            userDto = userService.findVirtualUser();
        }

        final UserDto userDtoFinal = userDto;

        if (organizationDto == null) {
            throw new BizServiceException(EErrorCode.ORGAN_NOTCONFIG, "未配置机构");
        }
        allBillsPublicService.initBillsPublic(billsPublic, organizationDto, BillFromSource.CORE);
        initCustomerPublic(billsPublic);

        transactionUtils.executeInNewTransaction(new TransactionCallback() {
            @Override
            public void execute() throws Exception {
                Long billId = allBillsPublicService.save(billsPublic, userDtoFinal, true);
                billsPublic.setId(billId);
                batchSuspendPo.setBillId(billId);
            }
        });
        if (sync) {
            allBillsPublicService.syncAndUpdateStaus(true, false, billsPublic, userDto, CompanySyncOperateType.autoSyncType);
        } else {
            allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
        }
    }

    @Override
    public List<BatchSuspendDto> findByOrganFullIdLike(String organFullId) {
        return ConverterService.convertToList(batchSuspendDao.findByOrganFullIdLike(organFullId), BatchSuspendDto.class);
    }

    /**
     * 批量久悬初始化客户信息
     * @param billsPublic
     */
    private void initCustomerPublic(AllBillsPublicDTO billsPublic) {
        AccountsAllInfo accountInfo = accountsAllService.findByAcctNo(billsPublic.getAcctNo());
        if(accountInfo != null) {
            CustomerPublicLogInfo customerPublicLogInfo = customerPublicLogService.getOne(accountInfo.getCustomerLogId());
            if(customerPublicLogInfo != null) {
                String[] ignoreProperties = {"id", "createdDate", "createdBy", "lastUpdateBy", "lastUpdateDate", "acctType", "operatorIdcardDue", "accountKey", "string006"};
                org.springframework.beans.BeanUtils.copyProperties(customerPublicLogInfo, billsPublic, ignoreProperties);
            }
        }
    }

}
