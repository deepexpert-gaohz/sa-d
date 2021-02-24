package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.*;
import com.ideatech.ams.account.service.AccountPublicService;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.bill.BillNoSeqService;
import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.account.service.pbc.PbcEccsService;
import com.ideatech.ams.account.validate.AllPublicAccountValidate;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.image.dto.ImageAllInfo;
import com.ideatech.ams.image.dto.ImageVideoDto;
import com.ideatech.ams.image.enums.StoreType;
import com.ideatech.ams.image.service.ImageAllService;
import com.ideatech.ams.image.service.ImageVideoService;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsCheckResultInfo;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.pbc.utils.DateUtils;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.configuration.service.AccountConfigureService;
import com.ideatech.ams.system.org.dto.OrganRegisterDto;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganRegisterService;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.system.whitelist.dto.WhiteListDto;
import com.ideatech.ams.system.whitelist.service.WhiteListService;
import com.ideatech.ams.ws.dto.ImageBatchInfo;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.annotation.InterfaceLog;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class PbcApiServiceImpl implements PbcApiService {

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private PbcAmsService pbcAmsService;

    @Autowired
    private PbcEccsService pbcEccsService;

    @Autowired
    private AmsMainService amsMainService;

    @Autowired
    private BillNoSeqService billNoSeqService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountPublicService accountPublicService;

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private WhiteListService whiteListService;

    @Autowired
    private TransactionUtils transactionUtils;


    @Autowired
    private ImageAllService imageAllService;

    @Autowired
    private CustomerPublicLogService customerPublicLogService;

    @Autowired
    private OrganRegisterService organRegisterService;

    @Autowired
    private AccountConfigureService accountConfigureService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private Map<String, AllPublicAccountValidate> validateMap;
    @Autowired
    private ImageVideoService imageVideoService;

    /**
     * 是否基于存量数据。否则变更、久悬、销户 对于无存量数据的情况下，直接新建客户账户信息
     */
    @Value("${ams.company.datenbestand:true}")
    private Boolean datenbestand;

    //是否上报信用代码证
    @Value("${ams.company.pbc.eccs}")
    private Boolean syncEccs;

    /**
     * 人行上报模式
     */
    @Value("${ams.company.daibulu.syncModel:auto}")
    private String syncPbcModel;

    /**
     * 接口页面校验逻辑完善，接口校验后置，已进入待补录的方式处理
     */
    @Value("${ams.company.interfaceValidate:false}")
    private Boolean interfaceValidate;

    /**
     * 接口页面校验逻辑完善，接口校验前置，已返回错误信息方式返回前台
     */
    @Value("${ams.company.interfaceValidateBefore:false}")
    private Boolean interfaceValidateBefore;

    /**
     * 接口内部校验：项目组提出的一些校验逻辑增加。
     */
    @Value("${ams.company.pbcApiValidate:false}")
    private Boolean pbcApiValidate;

    /**
     * 接口是否直接进入待审核，默认false，数据进入待补录
     */
    @Value("${ams.company.dataToCheck:false}")
    private Boolean dataToCheck;

    /**
     * 没有变更上报字段的情况下是否需要上报人行：一般户等
     */
    @Value("${ams.company.noChangeToSync:false}")
    private Boolean noChangeToSync;

    /**
     * 数据覆盖。如果使用的是覆盖流水方式，打开此配置后，会将之前库里存的数据覆盖本次报送为空的字段
     * 次配置默认关闭
     */
    @Value("${ams.company.data-coverage:false}")
    private Boolean dataCoverage;

    /**
     * 变更检查证明文件种类以及编号
     */
    @Value("${ams.company.changeCheckFileType:false}")
    private Boolean changeCheckFileType;

    @Value("${ams.image.video.type}")
    private String type;


    private final String DEFAULT_CODE = ResultCode.SYSTEM_BUSY.code();

    private final String DEFAULT_MESSAGE = ResultCode.SYSTEM_BUSY.message();

    /**
     * 开户校验
     *
     * @param accountKey
     * @param regAreaCode
     * @param organCode
     * @return
     */
    @Override
    @InterfaceLog
    public ResultDto checkPbcInfo(String accountKey, String regAreaCode, String organCode) {
        return checkPbcInfoCommon(accountKey, regAreaCode, organCode, "");
    }

    @Override
    @InterfaceLog
    public ResultDto checkPbcInfo(String accountKey, String regAreaCode, String organCode, String username) {
        if(StringUtils.isBlank(username)) {
            return ResultDtoFactory.toNack("用户名不能为空");
        }
        UserDto userDto = userService.findByUsername(username);
        if (userDto == null) {
            return ResultDtoFactory.toNack("用户名不存在");
        }

        return checkPbcInfoCommon(accountKey, regAreaCode, organCode, username);
    }

    @Override
    @InterfaceLog
    public ResultDto syncPbc(String organCode, final AllBillsPublicDTO billsPublic, Boolean needSync) {
        return syncPbc(organCode, billsPublic, needSync, false);
    }


    /**
     * 校验机构信息
     *
     * @param organCode
     * @throws BizServiceException
     */
    private OrganizationDto validateOrgan(String organCode) throws BizServiceException {
        OrganizationDto organizationDto = organizationService.findByCode(organCode);
        if (organizationDto == null) {
            throw new BizServiceException(EErrorCode.ORGAN_NOTCONFIG, "未找到机构,机构号："+organCode);
        }
        return organizationDto;
    }

    /**
     * 校验基本户开户的存款人类别、操作类型、账户性质
     * @param billsPublic
     */
   private void validateThree(AllBillsPublicDTO billsPublic){
       if(billsPublic.getBillType()==null){
           throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "操作类型不能为空");
       }
       if(billsPublic.getBillType()==BillType.ACCT_OPEN && billsPublic.getAcctType()==CompanyAcctType.jiben
               && StringUtils.isBlank(billsPublic.getDepositorType())){
           throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "基本户开户存款人类别不能为空");
       }

    }
    private UserDto getUserDto(AllBillsPublicDTO billsPublicDto){
        UserDto userDto = null;
        try {
            if(billsPublicDto != null && StringUtils.isNotBlank(billsPublicDto.getCreatedBy())){
                log.info("传入的柜员号：{}",billsPublicDto.getCreatedBy());
                userDto = userService.findByUsername(billsPublicDto.getCreatedBy());
            }
        }catch (Exception e){
            log.error("根据传入的柜员号查询异常：{}",e);
        }
        if(userDto==null){
            userDto = userService.findVirtualUser();
        }
        return userDto;
    }
    /**
     * 开户
     *
     * @param organCode
     * @param billsPublic
     * @return
     */
    @Override
    @InterfaceLog
    public ResultDto syncPbc(String organCode, AllBillsPublicDTO billsPublic) {
        return syncPbc(organCode, billsPublic, true);
    }

    @Override
    @InterfaceLog
    public ResultDto reenterablePbcSync(String organCode, AllBillsPublicDTO billsPublic) {
        return syncPbc(organCode, billsPublic, true, true);
    }

    @Override
    @InterfaceLog
    public ResultDto reenterablePbcSync(String organCode, AllBillsPublicDTO billsPublic, String flag) {
        if("0".equals(flag)){
            ResultDto dto = syncPbc(organCode, billsPublic, true, true);
            AllBillsPublicDTO billsPublic2 = (AllBillsPublicDTO)dto.getData();
            if(StringUtils.isNotBlank(billsPublic.getImageTempNo())){
                Long acctBillsId=null;
                Long customerId =null;
                Long acctId =null;
                if(billsPublic2.getId()!=null){
                    acctBillsId=billsPublic2.getId();
                }
                if(billsPublic2.getCustomerLogId()!=null){
                    CustomerPublicLogInfo log = customerPublicLogService.getOne(billsPublic2.getCustomerLogId());
                    if(log!=null){
                        customerId = log.getCustomerId();
                    }
                }
                if(billsPublic2.getAccountId()!=null){
                    acctId = billsPublic2.getAccountId();
                }
                imageAllService.setCustomerIdAndAcctIdByImageTempNo(billsPublic.getImageTempNo(),acctBillsId,customerId,acctId);
            }
            return dto;
        }else{
            return syncPbc(organCode, billsPublic, true, true);
        }
    }

    @Override
    @InterfaceLog
    public ResultDto reenterablePbcSync(String organCode, AllBillsPublicDTO billsPublic, Boolean syncAms, Boolean syncEccs) {
        return sync(organCode, billsPublic, true, true, syncAms, syncEccs);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDto submitPbc(String billNo, Boolean validateStatus) {
        UserDto userDto = userService.findVirtualUser();
        AllBillsPublicDTO billsPublic = null;
        try {
            billsPublic = allBillsPublicService.findFullInfoByBillNo(billNo);
            if (billsPublic == null) {
                return ResultDtoFactory.toApiError(ResultCode.NO_DATA_EXIST.code(), ResultCode.NO_DATA_EXIST.message());
            }

            if (validateStatus) {
                if (billsPublic.getStatus() != BillStatus.APPROVED) {
                    return ResultDtoFactory.toApiError(ResultCode.BILL_STATUS_ERROR.code(), ResultCode.BILL_STATUS_ERROR.message());
                }
            }

            //根据上报状态以及是否需要上报入参来判断
            boolean isSyncAms = allBillsPublicService.getSyncStatus(EAccountType.AMS, billsPublic);
            boolean isSyncEccs = allBillsPublicService.getSyncStatus(EAccountType.ECCS, billsPublic);
            if (isSyncAms || isSyncEccs) {
                allBillsPublicService.syncAndUpdateStaus(isSyncAms, isSyncEccs, billsPublic, userDto, CompanySyncOperateType.autoSyncType);
            } else {
                //直接结束该流水
                allBillsPublicService.updateFinalStatus(billsPublic, userDto.getId());
            }
            return ResultDtoFactory.toApiSuccess(billsPublic);
        } catch (Exception e) {
            ResultCode pbcResultCode = null;
            if (e instanceof BizServiceException) {
                pbcResultCode = ResultCode.getResultCodeByErrorCode(((BizServiceException) e).getError());
            }
            log.error("数据上报异常", e);

            if (billsPublic != null) {
                allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
            }
            return ResultDtoFactory.toApiError(pbcResultCode == null ? ResultCode.SYNC_FAILURE.code() : pbcResultCode.code(), e.getMessage());
        }
    }

    @Override
    public ResultDto reenterablePbcSync(String organCode, AllBillsPublicDTO billsPublic, ImageBatchInfo batchInfo) {
        ResultDto dto = syncPbc(organCode, billsPublic, true, true);
        if(!StringUtils.equals("1",dto.getCode())){
            log.info("人行数据上报失败，不做关联影像关联处理直接返回");
            return dto;
        }
        AllBillsPublicDTO billsPublic2 = (AllBillsPublicDTO)dto.getData();
        try {
            if(batchInfo!=null){
                UserDto userDto = userService.findById(2L);
                OrganizationDto organizationDto = organizationService.findById(userDto.getOrgId());
                if(StringUtils.isNotBlank(batchInfo.getImgBatchNumber())){
                    log.info("图片影像批次{}开始做关联",batchInfo.getImgBatchNumber());
                    ImageAllInfo imageAllInfo = new ImageAllInfo();
                    imageAllInfo.setBatchNumber(batchInfo.getImgBatchNumber());
                    if(StringUtils.isBlank(batchInfo.getDocCode())){
                        imageAllInfo.setDocCode("0");
                    }else{
                        imageAllInfo.setDocCode(batchInfo.getDocCode());
                    }
                    imageAllInfo.setBillsId(billsPublic2.getId());
                    imageAllInfo.setImgPath(batchInfo.getImgPath());
                    imageAllInfo.setFileName(batchInfo.getImgFileName());
                    imageAllInfo.setChanlNo(batchInfo.getChanlNo());
                    imageAllInfo.setSyncStatus(CompanyIfType.Yes);
                    imageAllService.save(imageAllInfo);
                }
                if(StringUtils.isNotBlank(batchInfo.getVideoBatchNumber())){
                    log.info("视频影像批次{}开始做关联",batchInfo.getImgBatchNumber());
                    ImageVideoDto imageVideoDto = new ImageVideoDto();
                    imageVideoDto.setBatchNumber(batchInfo.getVideoBatchNumber());
                    imageVideoDto.setBillsId(billsPublic2.getId());
                    imageVideoDto.setFilePath(batchInfo.getVideoPath());
                    imageVideoDto.setFileName(batchInfo.getVideoFileName());
                    imageVideoDto.setChanlNo(batchInfo.getChanlNo());
                    imageVideoDto.setSyncStatus(CompanyIfType.Yes);
                    imageVideoDto.setDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
                    imageVideoDto.setOrganFullId(organizationDto.getFullId());
                    imageVideoDto.setUsername(userDto.getUsername());
                    imageVideoDto.setType(StoreType.valueOf(type));
                    imageVideoDto.setDepositorName(billsPublic2.getDepositorName());
                    imageVideoDto.setAcctNo(billsPublic2.getAcctNo());
                    imageVideoDto.setLegalName(billsPublic2.getLegalName());
                    imageVideoDto.setAcctType(com.ideatech.common.enums.CompanyAcctType.valueOf(billsPublic2.getAcctType().name()));
                    imageVideoDto.setRegNo(billsPublic2.getRegNo());
                    imageVideoService.save(imageVideoDto);
                }
            }
            return dto;
        }catch (Exception e){
            log.error("人行上报成功，影像关联失败，失败原因："+e.getMessage());
            return ResultDtoFactory.toApiError(ResultCode.IMAGE_RELEV_FAIL.code() , "人行上报成功，影像关联失败，失败原因："+e.getMessage());
        }
    }

    @Override
    @InterfaceLog
    public ResultDto syncPbc(String organCode, AllBillsPublicDTO billsPublic, Boolean needSync, Boolean reenterable) {
        return sync(organCode, billsPublic, needSync, reenterable, true, true);
    }


    public void validate(AllBillsPublicDTO billsPublic) throws Exception {
        //验证座机手机
//        if(StringUtils.isNotBlank(billsPublic.getTelephone()) && !RegexUtils.isPhoneNumber(billsPublic.getTelephone())){
//            throw new RuntimeException("请填写正确的电话号码！");
//        }
        //验证工商注册号
        if(StringUtils.isNotBlank(billsPublic.getRegNo()) && (billsPublic.getRegNo().length() != 15 && billsPublic.getRegNo().length() != 18)){
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "工商注册号长度为15位或18位！");
        }
        //验证身份证
//        if(StringUtils.isNotBlank(billsPublic.getLegalIdcardType()) && billsPublic.getLegalIdcardType().equals("1")){
//            if(StringUtils.isNotBlank(billsPublic.getLegalIdcardNo()) && StringUtils.isNotBlank(IDCardUtils.IDCardValidate(billsPublic.getLegalIdcardNo()))){
//                throw new RuntimeException("请录入正确的法定代表人或负责人身份证件编号！");
//            }
//        }
        //验证组织机构代码
        if(StringUtils.isNotBlank(billsPublic.getOrgCode()) && billsPublic.getOrgCode().length() != 9){
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "组织机构代码长度不正确，应为9位！");
        }
        //证明文件到期日
        if(StringUtils.isNotBlank(billsPublic.getFileDue())){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if (format.parse(billsPublic.getFileDue()).getTime() < System.currentTimeMillis()) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "证明文件日期不能小于当前日期！");
            }
        }
        //校验信用代码证是否填写

        if(billsPublic.getAcctType() != null && billsPublic.getAcctType() == CompanyAcctType.jiben){
            if(syncEccs){
                if(StringUtils.isBlank(billsPublic.getBusinessScopeEccs())){
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请填写信用代码证经营范围字段！");
                }
            }
        }
    }

    public ResultDto sync(final String organCode, AllBillsPublicDTO billsPublicDto, Boolean needSync, Boolean reenterable, Boolean syncAms, Boolean syncEccs) {

        boolean dataSave = true;
        //2019-01-02 由于单独报送信用代码时，历史数据可能不满足信用代码的上报，仍需要
        //如果仅仅报送信用代码则直接跳过数据保存，直接上报
        /*if (syncEccs && !syncAms) {
            //1.查找本账号是否有在流程中的流水
            AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.findLatestUnfinishedByAcctNo(billsPublicDto.getAcctNo(), billsPublicDto.getBillType());
            if (accountBillsAllInfo != null) {
                billsPublicDto = allBillsPublicService.findOne(accountBillsAllInfo.getId());
                dataSave = false;
            }
        }*/
        printSystemConfigure();
        if(needSync){
            //需要同步的数据进行校验存款人类别，如果无需上报则可以进入到待补录里面进行补录存款人类别
            try {
                validateThree(billsPublicDto);
            }catch (Exception e){
                log.error("校验出错：{}",e);
                return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(),e.getMessage());
            }
        }

        final AllBillsPublicDTO billsPublic = billsPublicDto;

        log.info("1、billsPublic：" + billsPublic);
        billsPublic.setAcctShortName(billsPublic.getDepositorName());
        OrganizationDto organizationDto = null;
        try{
            organizationDto = validateOrgan(organCode);
        }catch (BizServiceException e){
            return ResultDtoFactory.toApiError(ResultCode.ORGAN_NOT_CONFIG.code(),e.getMessage());
        }

        //先查询是否是白名单数据   如果不是根据账号先查找是否有原白名单流水信息  如果有的话就删除
        WhiteListDto whiteListDto = whiteListService.getByEntnameAndOrgId(billsPublicDto.getDepositorName(), organizationDto.getId());
        //白名单为空  或者  存在白名单但是是移除了的数据
        if (whiteListDto == null || (whiteListDto != null && "delete".equals(whiteListDto.getStatus()))) {
            List<AccountBillsAllInfo> accountBillsAllInfoList = accountBillsAllService.findByAcctNo(billsPublicDto.getAcctNo());
            if (CollectionUtils.isNotEmpty(accountBillsAllInfoList)) {
                for (AccountBillsAllInfo accountBillsAllInfo : accountBillsAllInfoList) {
                    if ("1".equals(accountBillsAllInfo.getWhiteList())) {
                        billsPublicDto.setWhiteList("0");
                        break;
                    }
                }
            }
        }

        //接口方式数据直接获取虚拟用户
        final UserDto userDto = getUserDto(billsPublicDto);
        //验证必要参数
        if (StringUtils.isBlank(organCode) || billsPublic == null) {
            return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), ResultCode.PARAM_IS_BLANK.message());
        }
        //盛京银行校验进入待补录，校验往后移动
        try{
            billsPublic.validateAcctNo();
            if (pbcApiValidate) {
                validate(billsPublic);
            }
        } catch (Exception e) {
            log.error("数据保存异常", e);
            return ResultDtoFactory.toApiError(ResultCode.DATA_SAVE_FAILURE.code(), e.getMessage() + ",数据默认进入待补录.", e);
        }

        //校验上报对象是否符合上报规则
        //前置校验逻辑，校验不通过通过接口直接返回错误信息
        log.info("纯接口上报是否增加前台校验逻辑：" + interfaceValidateBefore);
        if(interfaceValidateBefore){
            //启用页面校验机制需要设置开户日期，临时，非临时会去比较开户日期跟有效日期
            if(billsPublic.getBillType() == BillType.ACCT_OPEN && StringUtils.isBlank(billsPublic.getAcctCreateDate())){
                billsPublic.setAcctCreateDate(DateUtils.DateToStr(new Date(),"yyyy-MM-dd"));
            }
            try {
                checkDataColumn(billsPublic);
            }catch (Exception e) {
                log.error("数据校验异常：" + e.getMessage());
                return ResultDtoFactory.toApiError(ResultCode.BUSINESS_ERROR.code(), "数据校验异常：" + e.getMessage() + "");
            }
        }
        if(changeCheckFileType){
            try {
                checkChangeDataColumn(billsPublic);
            } catch (Exception e) {
                log.error("数据校验异常：" + e.getMessage());
                return ResultDtoFactory.toApiError(ResultCode.BUSINESS_ERROR.code(), "数据校验异常：" + e.getMessage() + "");
            }
        }

        //判断该机构是否走取消核准接口
        try{
            log.info("接口模式查询是否取消核准账户");
            allBillsPublicService.setCancelHeZhun(billsPublic);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultDtoFactory.toApiError(ResultCode.PBC_QUERY_ERROR.code(), e.getMessage(), e);
        }

        if (dataSave) {
            // 处理账户名称
            allBillsPublicService.setAcctName(billsPublic);

            //保存
            try {
                //如果需要覆盖上次未完成流水
                if (reenterable) {
                    AccountBillsAllInfo latestUnfinishedByAcctNo = accountBillsAllService.findLatestUnfinishedByAcctNo(billsPublic.getAcctNo(), billsPublic.getBillType());
                    log.info("2、latestUnfinishedByAcctNo :" + latestUnfinishedByAcctNo);
                    //增加账号未完成流水
                    if(latestUnfinishedByAcctNo == null){
                        latestUnfinishedByAcctNo = accountBillsAllService.findLatestUnFinishedByAcctNo(billsPublic.getAcctNo());
                        if(latestUnfinishedByAcctNo != null){
                            return ResultDtoFactory.toApiError(ResultCode.UN_FINISHED_BILL.code(), "账号" + latestUnfinishedByAcctNo.getAcctNo() + "，流水编号：" + billsPublic.getBillNo() + "，业务类型：" + latestUnfinishedByAcctNo.getBillType().getValue() + "有未完成的操作流水！");
                        }
                    }
                    //如果人行上报成功且流水未完成则抛出异常。不允许执行同一种类型的相同操作
                    //核准类账户人行同步成功，未审核，流水状态为未完成
                    if(latestUnfinishedByAcctNo != null){
                        if(latestUnfinishedByAcctNo.getAcctType().isHeZhun() && latestUnfinishedByAcctNo.getFinalStatus() == CompanyIfType.No
                                && latestUnfinishedByAcctNo.getPbcSyncStatus() == CompanySyncStatus.tongBuChengGong
                                && latestUnfinishedByAcctNo.getPbcCheckStatus() == CompanyAmsCheckStatus.WaitCheck
                                && (latestUnfinishedByAcctNo.getEccsSyncStatus() == CompanySyncStatus.tongBuChengGong
                                || latestUnfinishedByAcctNo.getEccsSyncStatus() == CompanySyncStatus.buTongBu )){
                            return ResultDtoFactory.toApiError(ResultCode.UN_FINISHED_BILL.code(), "账户：" + latestUnfinishedByAcctNo.getAcctNo() + billsPublic.getBillType().getValue() + "业务人行状态未审核，请确认后再进行操作！");
                        }
                    }

                    if (latestUnfinishedByAcctNo != null) {
                        billsPublic.setId(latestUnfinishedByAcctNo.getId());
                        //流水数据初始化
                        allBillsPublicService.setSyncStatusByOldBill(latestUnfinishedByAcctNo, billsPublic);
                        //使用库中数据覆盖billsPublic
                        if (dataCoverage) {
                            AllBillsPublicDTO dbDto = allBillsPublicService.findByBillId(latestUnfinishedByAcctNo.getId());
                            allBillsPublicService.dataCoverage(dbDto, billsPublic);
                        }
                        AccountsAllInfo accountsAllInfo = accountsAllService.findByRefBillId(latestUnfinishedByAcctNo.getId());
                        if (accountsAllInfo != null) {
                            billsPublic.setAccountStatus(accountsAllInfo.getAccountStatus());
                        }
                    }
                }

                //如果是基于存量的数据，需要判断数据库中是否有账户
                if (datenbestand) {
                    if (billsPublic.getBillType() == BillType.ACCT_CHANGE || billsPublic.getBillType() == BillType.ACCT_REVOKE || billsPublic.getBillType() == BillType.ACCT_SUSPEND) {
                        AccountsAllInfo accountsAllInfo = accountsAllService.findByAcctNo(billsPublic.getAcctNo());
                        log.info("accountsAllInfo:" + accountsAllInfo);
                        if (accountsAllInfo == null) {
                            return ResultDtoFactory.toApiError(ResultCode.ACCT_DATA_NOT_EXIST.code(), ResultCode.ACCT_DATA_NOT_EXIST.message() + "无法" + billsPublic.getBillType().getValue());
                        } else {
                            if(billsPublic.getBillType() == BillType.ACCT_REVOKE) {  //久悬户支持销户操作
                                if(accountsAllInfo.getAccountStatus() != AccountStatus.normal &&
                                        accountsAllInfo.getAccountStatus() != AccountStatus.suspend) {
                                    return ResultDtoFactory.toApiError(ResultCode.ACCT_DATA_NOT_NORMAL.code(), ResultCode.ACCT_DATA_NOT_NORMAL.message() + "无法" + billsPublic.getBillType().getValue());
                                }
                            } else {  //变更、久悬操作
                                if (accountsAllInfo.getAccountStatus() != AccountStatus.normal) {
                                    return ResultDtoFactory.toApiError(ResultCode.ACCT_DATA_NOT_NORMAL.code(), ResultCode.ACCT_DATA_NOT_NORMAL.message() + "无法" + billsPublic.getBillType().getValue());
                                }
                            }
                        }
                    }
                }


                //如果billType为空  根据账号去人行查询  如果人行存在就把billType设为变更  不存在则开户  人行接口报错则返回错误信息
                if(billsPublic != null && billsPublic.getBillType() == null){
                    log.info("BillType为空，根据账号" + billsPublic.getAcctNo() + "查询人行信息");
                    AmsAccountInfo amsInfo = null;
                    try {
                        amsInfo = pbcAmsService.getAmsAccountInfoByAcctNo(organCode, billsPublic.getAcctNo());
                    } catch (Exception e) {
                        log.error("根据账号去人行查询异常", e);
                        return ResultDtoFactory.toApiError(ResultCode.PBC_CHECKDETAIL_FAILURE.code(),  "人行登录查询异常：",e);
                    }
                    if(amsInfo != null){
                        billsPublic.setBillType(BillType.ACCT_CHANGE);
                    }else{
                        billsPublic.setBillType(BillType.ACCT_OPEN);
                    }
                }
                //校验账户：如果是开户，如果存在存量且相同的账号不让开户
                if(billsPublic.getBillType()==BillType.ACCT_OPEN){
                    //增加开户日期
                    if(StringUtils.isBlank(billsPublic.getAcctCreateDate())){
                        billsPublic.setAcctCreateDate(DateUtils.DateToStr(new Date(),"yyyy-MM-dd"));
                    }

                    AccountsAllInfo accounts= accountsAllService.findByAcctNo(billsPublic.getAcctNo());
                    if(accounts!=null && StringUtils.equals("1",accounts.getString003())){
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "该账户是存量账户，不可开户！");
                    }
                }
                //校验销户时销户原因是否为空
                if(billsPublic.getBillType() == BillType.ACCT_REVOKE){
                    if(StringUtils.isEmpty(billsPublic.getAcctCancelReason())){
                        //没有销户原因返回错误提示
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "账户销户，销户原因不能为空！");
                    }
                }
                allBillsPublicService.initBillsPublic(billsPublic, organizationDto, BillFromSource.CORE);
                transactionUtils.executeInNewTransaction(new TransactionCallback() {
                    @Override
                    public void execute() throws Exception {
                        AllBillsPublicDTO originalBills = new AllBillsPublicDTO();

                        //获取人行数据
                        AmsAccountInfo amsAccountInfo = null;
                        try {
                            amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(organCode, billsPublic.getAcctNo());
                        } catch (Exception e) {
                            log.error("人行登录查询异常", e);
                        }
                        //变更操作时，比对账户类型是否有变更
                        if (billsPublic.getBillType() == BillType.ACCT_CHANGE
                                && amsAccountInfo != null && amsAccountInfo.getAcctType() != null
                                && !amsAccountInfo.getAcctType().name().equals(billsPublic.getAcctType().name())) {
                            if (amsAccountInfo.getAcctType() == null) {
                                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "变更接口的账户类型错误！人行账户账户类型为空");
                            } else {
                                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "变更接口的账户类型错误！人行账户账户类型为：" + amsAccountInfo.getAcctType().getFullName());
                            }
                        }
                        //久悬和销户时，需要根据账号去人行查询获取企业名称并赋值
                        if (billsPublic.getBillType().equals(BillType.ACCT_REVOKE) || billsPublic.getBillType().equals(BillType.ACCT_SUSPEND)) {
                            if (StringUtils.isBlank(billsPublic.getDepositorName()) && amsAccountInfo != null) {
                                billsPublic.setDepositorName(amsAccountInfo.getDepositorName());
                            }
                        }
                        //变更且有存量数据时获取旧数据比对
                        if (billsPublic.getBillType() == BillType.ACCT_CHANGE && datenbestand) {
                            originalBills = allBillsPublicService.changeCompareWithOriginal(billsPublic);
                        }
                        //变更时如果不基于存量，需要根据账号去查找账户数据做比对，如果没传就用原来的
                         if(billsPublic.getBillType() == BillType.ACCT_CHANGE && (!datenbestand)){
                             AccountPublicInfo accountPublicInfo = accountPublicService.findByAcctNo(billsPublic.getAcctNo());
                             if(accountPublicInfo!=null){
                                 AllBillsPublicDTO allBillsPublicDTO = new AllBillsPublicDTO();
                                 String[] ignoreProperties = {"id", "createdDate", "createdBy","lastUpdateBy","lastUpdateDate"};
                                 BeanUtils.copyProperties(accountPublicInfo, allBillsPublicDTO, ignoreProperties);
                                 allBillsPublicService.dataCoverage(allBillsPublicDTO, billsPublic);
                             }
                         }
                        log.info("3.1、billsPublic :" + billsPublic);
                        Long billId = allBillsPublicService.save(billsPublic, userDto, true);
                        log.info("3.2、billsPublic :" + billsPublic);
                        billsPublic.setId(billId);
                        //变更类型为"acctType", "bankCode", "depositorType", "acctBigType", "organCode", "regAreaCode" 会存在先销户在开户的情况
                        if (billsPublic.getBillType() == BillType.ACCT_CHANGE) {
//                            PrintUtils.printObjectColumn(billsPublic);
                            allBillsPublicService.changeCompareWithOld(originalBills,billsPublic);
                        }
                    }
                });
            } catch (Exception e) {
                log.error("数据保存异常", e);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultDtoFactory.toApiError(ResultCode.DATA_SAVE_FAILURE.code(), e.getMessage(), e);
            }

            //如果是变更，有可能会变更人行上报状态，信用代码证上报状态，人行审核状态，所以在这里做一步这3个字段的保存
            if(billsPublic.getBillType() == BillType.ACCT_CHANGE){
                log.info("变更后进行状态的变更...");
                accountBillsAllService.updatePbcEccsCheckStatus(billsPublic.getId(),billsPublic.getPbcSyncStatus(),billsPublic.getEccsSyncStatus(),billsPublic.getPbcCheckStatus());
            }

            //本地账户数据存在时账户性质小类赋值
            if(billsPublic.getBillType() == BillType.ACCT_CHANGE || billsPublic.getBillType() == BillType.ACCT_SUSPEND
                    || billsPublic.getBillType() == BillType.ACCT_REVOKE) {
                if(StringUtils.isNotBlank(billsPublic.getAcctNo())) {
                    AccountPublicInfo info = accountPublicService.findByAcctNo(billsPublic.getAcctNo());
                    if(info != null) {
                        billsPublic.setAcctType(info.getAcctType());
                    } else {
                        if(StringUtils.isBlank(billsPublic.getAcctName())) {
                            billsPublic.setAcctName(billsPublic.getDepositorName());
                        }
                    }
                }
            }

            //校验上报对象是否符合上报规则
            log.info("纯接口上报是否增加前台校验逻辑（后置）：" + interfaceValidate);
            if(interfaceValidate){
                try {
                    checkDataColumn(billsPublic);
                }catch (Exception e) {
                    log.error("数据校验异常：" + e.getMessage());
                    allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
                    return ResultDtoFactory.toApiError(ResultCode.BUSINESS_ERROR.code(), "数据校验异常：" + e.getMessage() + ",数据默认进入待补录");
                }
            }

            log.info("查询接口上报模式");
            //判断接口上报模式
//            List<ConfigDto> configDtoList = configService.findByKey("syncStatus");
            //修改接口上报模式，根据业务的操作类型进行区分
            List<ConfigDto> configDtoList = null;
            if(billsPublic.getBillType() == BillType.ACCT_OPEN){
                configDtoList = configService.findByKey("openSyncStatus");
            }
            if(billsPublic.getBillType() == BillType.ACCT_CHANGE){
                configDtoList = configService.findByKey("changeSyncStatus");
            }
            if(billsPublic.getBillType() == BillType.ACCT_SUSPEND){
                configDtoList = configService.findByKey("suspendSyncStatus");
            }
            if(billsPublic.getBillType() == BillType.ACCT_REVOKE){
                configDtoList = configService.findByKey("revokeSyncStatus");
            }
            if(billsPublic.getBillType() == BillType.ACCT_EXTENSION){
                configDtoList = configService.findByKey("ExtensionSyncStatus");
            }

            //只针对取消核准机构进行业务的审核  1、接口上报模式是cancelHeZhunOrgan进行判断
            if(CollectionUtils.isNotEmpty(configDtoList) && "cancelHeZhunOrgan".equals(configDtoList.get(0).getConfigValue())){
                //判断是不是取消核准机构
                OrganRegisterDto organRegisterDto = null;
                if (StringUtils.isNotBlank(billsPublic.getOrganCode())) {
                    organRegisterDto = organRegisterService.queryByOrganCode(billsPublic.getOrganCode());
                } else {
                    organRegisterDto = organRegisterService.query(billsPublic.getBankCode());
                }
                //不是取消核准维持原接口
                if(organRegisterDto == null){
                    log.info("纯接口配置为：cancelHeZhunOrgan，机构不是取消核准机构，维持原有接口上报模式");
                    allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.APPROVED, userDto.getId(), "");
                }else{
                    log.info("纯接口配置为：cancelHeZhunOrgan，机构是取消核准机构，数据进入审核模式");
                    log.info("接口数据是否直接进去待审核：" + dataToCheck);
                    if(dataToCheck){
                        log.info("数据进入待审核列表");
                        allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.APPROVING, userDto.getId(), "");
                        return ResultDtoFactory.toApiSuccess("接口开启审核模式，数据默认进入待审核", billsPublic);
                    }else{
                        log.info("数据进入待补录列表");
                        allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
                        return ResultDtoFactory.toApiSuccess("接口开启审核模式，数据默认进入待补录", billsPublic);
                    }
                }
            }else{
                //如果上报接口模式是空或者上报模式是autoSync  维持原有上报
                if(CollectionUtils.isEmpty(configDtoList) || (CollectionUtils.isNotEmpty(configDtoList) && "autoSync".equals(configDtoList.get(0).getConfigValue()))){
                    log.info("维持原有接口上报模式");
                    allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.APPROVED, userDto.getId(), "");
                } else {
                    //所有接口进来的数据先进去待审核列表
                    //autoSync：维持原来的上报模式   beforeCheckSync ： 所以数据进去待审核 ；  cancelHeZhunSync：取消核准数据进去待审核
                    ConfigDto configDto = configDtoList.get(0);
                    log.info("接口上报模式为" + configDto == null ? "" : configDto.getConfigValue());
                    if("beforeCheckSync".equals(configDto.getConfigValue())){
                        log.info("接口数据是否直接进去待审核：" + dataToCheck);
                        if(dataToCheck){
                            log.info("数据进入待审核列表");
                            allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.APPROVING, userDto.getId(), "");
                            return ResultDtoFactory.toApiSuccess("接口开启审核模式，数据默认进入待审核", billsPublic);
                        }else{
                            log.info("数据进入待补录列表");
                            allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
                            return ResultDtoFactory.toApiSuccess("接口开启审核模式，数据默认进入待补录", billsPublic);
                        }
                    }
                    if("cancelHeZhunSync".equals(configDto.getConfigValue())){
                        log.info("判断取消核准进入待审核列表。。。");
                        if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()){
                            log.info(billsPublic.getDepositorName() + "是取消核准制数据");
                            log.info("接口数据是否直接进去待审核：" + dataToCheck);
                            if(dataToCheck){
                                log.info("数据进入待审核列表");
                                allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.APPROVING, userDto.getId(), "");
                                return ResultDtoFactory.toApiSuccess("针对取消核准数据开启审核模式，数据默认进入待审核", billsPublic);
                            }else{
                                log.info("数据进入待补录列表");
                                allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
                                return ResultDtoFactory.toApiSuccess("针对取消核准数据开启审核模式，数据默认进入待补录", billsPublic);
                            }
                        }else{
                            log.info(billsPublic.getDepositorName() + "不是取消核准制数据，维持原有接口上报模式");
                            allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.APPROVED, userDto.getId(), "");
                        }
                    }
                }
            }
        }

        //无需上报的数据直接更新状态并结束流水
        boolean realSync = true;
        //核准类销户无需上报
        if (billsPublic.getAcctType().isHeZhun() && billsPublic.getBillType() == BillType.ACCT_REVOKE) {
            if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun() && (billsPublic.getAcctType() == CompanyAcctType.jiben || billsPublic.getAcctType() == CompanyAcctType.feilinshi)){
                realSync = true;
            }else{
                realSync = false;
            }
        }

        if (billsPublic.getPbcSyncStatus() == CompanySyncStatus.buTongBu && billsPublic.getEccsSyncStatus() == CompanySyncStatus.buTongBu) {
            realSync = false;
        }

        if (!realSync) {
            //数据都无需上报（不需要上报的字段变更）
            try {
                //不需要上报的字段变更上报人行
                if(noChangeToSync && (billsPublic.getAcctType() == CompanyAcctType.yiban)){// || billsPublic.getAcctType() == CompanyAcctType.feiyusuan
                    pbcAmsService.amsAccountSyncChangeAgain(organizationDto,billsPublic.getAcctNo());
                    billsPublic.setPbcSyncStatus(CompanySyncStatus.tongBuChengGong);
                    allBillsPublicService.updateSyncStatusAndFinishBill(billsPublic, userDto.getId());
                }else{
                    allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.APPROVED, userDto.getId(), "");
                    allBillsPublicService.updateSyncStatusAndFinishBill(billsPublic, userDto.getId());
                }
            } catch (Exception e) {
                if(noChangeToSync){
                    log.error("不需要上报的字段变更上报人行异常", e);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResultDtoFactory.toApiError(ResultCode.UPDATE_BILL_STATUS_FAILURE.code(), e.getMessage());
                }else{
                    log.error("无需上报数据更新最终状态异常", e);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResultDtoFactory.toApiError(ResultCode.UPDATE_BILL_STATUS_FAILURE.code(), e.getMessage());
                }
            }
            return ResultDtoFactory.toApiSuccess(billsPublic);
        }

        try {
            if (needSync) {
                //根据上报状态以及是否需要上报入参来判断
                boolean isSyncAms = allBillsPublicService.getSyncStatus(EAccountType.AMS, billsPublic) && syncAms;
                boolean isSyncEccs = allBillsPublicService.getSyncStatus(EAccountType.ECCS, billsPublic) && syncEccs;
                if(isSyncAms || isSyncEccs) {
                    allBillsPublicService.syncAndUpdateStaus(isSyncAms, isSyncEccs, billsPublic, userDto, CompanySyncOperateType.autoSyncType);
                }else {
                    allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
                }
            } else {
                //数据无需报送调用此方法直接进入待补录
                allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
                return ResultDtoFactory.toApiError(ResultCode.BUSINESS_ERROR.code(), "数据默认进入待补录");
            }
            return ResultDtoFactory.toApiSuccess(billsPublic);
        } catch (Exception e) {
            ResultCode pbcResultCode = null;
            if (e instanceof BizServiceException) {
                pbcResultCode = ResultCode.getResultCodeByErrorCode(((BizServiceException) e).getError());
            }
            log.error("数据上报异常", e);
            log.info("查询日志:pbcApiService：accountKey:" + billsPublic.getAccountKey() + ";selectPwd:" + billsPublic.getSelectPwd() + ";openKey:" + billsPublic.getOpenKey());
            allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultDtoFactory.toApiError(pbcResultCode == null ? ResultCode.SYNC_FAILURE.code() : pbcResultCode.code(), e.getMessage());
        }
    }

    private void printSystemConfigure(){
        //打印系统配置项
        log.info("是否上报信用代码证:" + syncEccs);
        log.info("是否基于存量数据：" + datenbestand);
        log.info("人行上报模式：" + syncPbcModel);
    }

    private void checkDataColumn(AllBillsPublicDTO billsPublic) throws Exception {
        log.info("开户前校验是否满足人行上报条件");
        if(billsPublic.getBillType() == BillType.ACCT_OPEN || billsPublic.getBillType() == BillType.ACCT_CHANGE){
            AllPublicAccountValidate allPublicAccountValidate = validateMap.get(billsPublic.takeValidateName());
            allPublicAccountValidate.validate(billsPublic);
        }
    }

    private void checkChangeDataColumn(AllBillsPublicDTO billsPublic) throws Exception {
        log.info("变更账户证明文件变更校验");
        if(billsPublic.getBillType() == BillType.ACCT_CHANGE){
            if(billsPublic.getAcctType() == CompanyAcctType.jiben || billsPublic.getAcctType() == CompanyAcctType.linshi || billsPublic.getAcctType() == CompanyAcctType.teshu){
                if((StringUtil.isBlank(billsPublic.getFileType()) && StringUtils.isNotBlank(billsPublic.getFileNo()))
                        || (StringUtils.isNotBlank(billsPublic.getFileType()) && StringUtils.isBlank(billsPublic.getFileNo()))
                        || (StringUtils.isBlank(billsPublic.getFileType2()) && StringUtils.isNotBlank(billsPublic.getFileNo2()))
                        || (StringUtils.isNotBlank(billsPublic.getFileType2()) && StringUtils.isBlank(billsPublic.getFileNo2()))){
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "变更时证明文件类型，编号请同时填写");
                }
                if(billsPublic.getAcctType() == CompanyAcctType.jiben){
                    //基本户证明文件1种类类型值判断
                    String[] fileType = new String[] { "01", "02", "03", "04"};
                    if(StringUtils.isNotBlank(billsPublic.getFileType())){
                        if (!ArrayUtils.contains(fileType, billsPublic.getFileType())) {
                            log.info("基本户证明文件1种类当前值：" + billsPublic.getFileType());
                            log.info("基本户证明文件1种类类型值应为：\"01\", \"02\", \"03\", \"04\"");
                            throw new SyncException("基本户证明文件1种类类型值不正确");
                        }
                    }

                    //基本户证明文件2种类类型值判断
                    String[] fileType2 = new String[] {"02", "03", "04"};
                    if(StringUtils.isNotBlank(billsPublic.getFileType2())){
                        if (!ArrayUtils.contains(fileType2, billsPublic.getFileType2())) {
                            log.info("基本户证明文件2种类当前值：" + billsPublic.getFileType2());
                            log.info("基本户证明文件2种类类型值应为：\"02\", \"03\", \"04\"");
                            throw new SyncException("基本户证明文件2种类类型值不正确");
                        }
                    }
                }

                if(billsPublic.getAcctType() == CompanyAcctType.linshi){
                    //证明文件种类1类型值判断
                    String[] fileType = new String[] { "01", "09"};
                    if(com.ideatech.common.utils.StringUtils.isNotBlank(billsPublic.getFileType())){
                        if (!ArrayUtils.contains(fileType, billsPublic.getFileType())) {
                            log.info("临时户证明文件种类1证件类型当前值：" + billsPublic.getParLegalIdcardType());
                            log.info("临时户证明文件种类1证件类型值应为：\"01\", \"09\"");
                            throw new SyncException("临时户证明文件种类1证件类型值不正确");
                        }
                    }
                }
                if(billsPublic.getAcctType() == CompanyAcctType.teshu){
                    //特殊户证明文件1种类类型值判断
                    String[] fileType = new String[] { "12", "17"};
                    if(com.ideatech.common.utils.StringUtils.isNotBlank(billsPublic.getFileType())){
                        if (!ArrayUtils.contains(fileType, billsPublic.getFileType())) {
                            log.info("特殊户证明文件1种类类型值当前值：" + billsPublic.getFileType());
                            log.info("特殊户证明文件1种类类型值应为：\"12\", \"17\"");
                            throw new SyncException("特殊户证明文件1种类类型值不正确");
                        }
                    }

                    //特殊户证明文件2种类类型值判断
                    String[] fileType2 = new String[] {"13", "17"};
                    if(com.ideatech.common.utils.StringUtils.isNotBlank(billsPublic.getFileType2())){
                        if (!ArrayUtils.contains(fileType2, billsPublic.getFileType2())) {
                            log.info("特殊户证明文件2种类类型值当前值：" + billsPublic.getFileType2());
                            log.info("特殊户证明文件2种类类型值应为：\"13\", \"17\"");
                            throw new SyncException("特殊户证明文件2种类类型值不正确");
                        }
                    }
                }
            }
            if(billsPublic.getAcctType() == CompanyAcctType.feilinshi){
                if((StringUtils.isBlank(billsPublic.getAcctFileType()) && StringUtils.isNotBlank(billsPublic.getAcctFileNo()))
                        || (StringUtils.isNotBlank(billsPublic.getAcctFileType()) && StringUtils.isBlank(billsPublic.getAcctFileNo()))){
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "变更时证明文件类型，编号请同时填写");
                }
                //非临时户证明文件1种类类型值判断
                String[] fileType = new String[] { "14", "15", "16"};
                if(com.ideatech.common.utils.StringUtils.isNotBlank(billsPublic.getAcctFileType())){
                    if (!ArrayUtils.contains(fileType, billsPublic.getAcctFileType())) {
                        log.info("非临时户证明文件1种类类型当前值：" + billsPublic.getAcctFileType());
                        log.info("非临时户证明文件1种类类型值应为：\"14\", \"15\", \"16\"");
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"非临时户证明文件1种类类型值不正确");
                    }
                }
            }
            if(billsPublic.getAcctType() == CompanyAcctType.yusuan || billsPublic.getAcctType() == CompanyAcctType.feiyusuan){
                if((StringUtils.isBlank(billsPublic.getAcctFileType()) && StringUtils.isNotBlank(billsPublic.getAcctFileNo()))
                        || (StringUtils.isNotBlank(billsPublic.getAcctFileType()) && StringUtils.isBlank(billsPublic.getAcctFileNo()))
                        || (StringUtils.isBlank(billsPublic.getAcctFileType2()) && StringUtils.isNotBlank(billsPublic.getAcctFileNo2()))
                        || (StringUtils.isNotBlank(billsPublic.getAcctFileType2()) && StringUtils.isBlank(billsPublic.getAcctFileNo2()))){
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "变更时证明文件类型，编号请同时填写");
                }
                if(com.ideatech.common.utils.StringUtils.isNotBlank(billsPublic.getAcctFileType2())){
                    //证明文件1类型判断
                    String[] accountFileType2 = new String[] { "08"};
                    if (!ArrayUtils.contains(accountFileType2, billsPublic.getAcctFileType2())) {
                        log.info("预算户证明文件2类型值当前值：" + billsPublic.getAcctFileType2());
                        log.info("预算户证明文件2类型值应为：\"08\"");
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"预算户开户证明文件2类型值不正确");
                    }
                }
                // 证明文件1类型判断
                if (com.ideatech.common.utils.StringUtils.isNotBlank(billsPublic.getAcctFileType())) {
                    //证明文件1类型判断
                    String[] accountFileType = new String[] { "09", "10","11"};
                    if (!ArrayUtils.contains(accountFileType, billsPublic.getAcctFileType())) {
                        log.info("预算户证明文件1类型值当前值：" + billsPublic.getAcctFileType());
                        log.info("预算户证明文件1类型值应为：\"09\", \"10\",\"11\"");
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"预算户开户证明文件1类型值不正确");
                    }
                }
            }
            if(billsPublic.getAcctType() == CompanyAcctType.yiban){
                if(StringUtils.isNotBlank(billsPublic.getAcctFileNo()) && StringUtils.isBlank(billsPublic.getAcctFileType())){
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "变更时证明文件类型，编号请同时填写");
                }
                if("06".equals(billsPublic.getAcctFileType())){
                    if(StringUtils.isBlank(billsPublic.getAcctFileNo())){
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "一般户变更证明文件类型为借款合同是，证明文件编号请同时填写");
                    }
                }
                //判断开户证明文件种类下拉框值校验
                String[] accountFileType = new String[] { "06", "07"};
                if(com.ideatech.common.utils.StringUtils.isNotBlank(billsPublic.getAcctFileType())){
                    if (!ArrayUtils.contains(accountFileType, billsPublic.getAcctFileType())) {
                        log.info("开户证明文件编号类型值当前值：" + billsPublic.getAcctFileType());
                        log.info("开户证明文件编号类型值应为：\"06\", \"07\"");
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "开户证明文件编号类型值不正确");
                    }
                }
            }
        }
    }

    private ResultDto checkPbcInfoCommon(String accountKey, String regAreaCode, String organCode, String username) {
        if (StringUtils.isBlank(accountKey) || StringUtils.isBlank(regAreaCode) || StringUtils.isBlank(organCode)) {
            return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), ResultCode.PARAM_IS_BLANK.message(), null);
        }
        String code = "";
        String msg = "";
        try {
            PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(organCode, EAccountType.AMS);
            if (pbcAccountDto != null) {
                AmsCheckResultInfo amsCheckResultInfo = pbcAmsService.checkPbcByAccountKeyAndRegAreaCode(organCode, accountKey, regAreaCode, username);
                if (amsCheckResultInfo.isCheckPass()) {
                    return ResultDtoFactory.toApiSuccess(amsCheckResultInfo.getAmsAccountInfo());
                } else {
                    code = ResultCode.PBC_VALIDATION_NOT_PASS.code();
                    msg = amsCheckResultInfo.getNotPassMessage();
                }
            } else {
                List<PbcAccountDto> pbcAccountDtos = pbcAccountService.listByOrgCodeAndType(organCode, EAccountType.AMS);
                if(pbcAccountDtos == null || pbcAccountDtos.size() ==0) {
                    code = ResultCode.ORGAN_NOT_CONFIG_SYNC_USER.code();
                    msg = ResultCode.ORGAN_NOT_CONFIG_SYNC_USER.message();
                } else {
                    code = ResultCode.NO_VALID_PBCACCOUNT.code();
                    msg = ResultCode.NO_VALID_PBCACCOUNT.message();
                }
            }
        } catch (Exception e) {
            code = ResultCode.PBC_VALIDATION_NOT_PASS.code();
            msg = e.getMessage();
        }

        return ResultDtoFactory.toApiError(code, msg, null);
    }
}
