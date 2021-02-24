package com.ideatech.ams.account.service.pbc;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.OpenAccountSiteType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.event.PushCoreDataEvent;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.pbc.dto.*;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.ams.pbc.enums.AmsAnnualResultStatus;
import com.ideatech.ams.pbc.enums.SyncAcctType;
import com.ideatech.ams.pbc.enums.SyncOperateType;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.PbcMockService;
import com.ideatech.ams.pbc.service.ams.cancel.AmsJibenOpenBeiAnService;
import com.ideatech.ams.pbc.spi.AmsAnnualMainService;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.pbc.spi.SyncValidater;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.proof.dto.ProofReportDto;
import com.ideatech.ams.system.proof.enums.ProofType;
import com.ideatech.ams.system.proof.service.ProofReportService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(noRollbackFor = {SyncException.class,BizServiceException.class})
@Slf4j
public class PbcAmsServiceImpl implements PbcAmsService {
    @Value("${ams.company.writeMoney}")
    private boolean writeMoney;
    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private AmsMainService amsMainService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private AmsAnnualMainService amsAnnualMainService;

    @Autowired
    private PbcMockService pbcMockService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AmsJibenOpenBeiAnService amsJibenOpenBeiAnService;

    @Autowired
    private SyncValidater amsJibenOpenSyncValidater;

    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private ProofReportService proofReportService;
    @Autowired
    private UserService userService;

    @Value("${ams.pbcDataLog:false}")
    private Boolean pbcDataLog;

    @Value("${ams.company.cancelPushCoreData:false}")
    private Boolean cancelPushCoreData;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    final private static String[] NUMBER_CN = new String[]{"零","壹","贰","叁","肆","伍","陆","柒","捌","玖","拾"};

    @Override
    public AmsCheckResultInfo checkPbcByAccountKeyAndRegAreaCode(String orgCode, String accountKey, String regAreaCode) throws Exception {
        return checkPbcByAccountKeyAndRegAreaCodeCommon(orgCode, accountKey, regAreaCode, "");
    }

    @Override
    public AmsCheckResultInfo checkPbcByAccountKeyAndRegAreaCode(String orgCode, String accountKey, String regAreaCode, String username) throws Exception {
        return checkPbcByAccountKeyAndRegAreaCodeCommon(orgCode, accountKey, regAreaCode, username);
    }

    @Override
    public AmsCheckResultInfo checkPbcByAccountKeyAndRegAreaCodeForYiBanOpen(String orgCode, String accountKey, String regAreaCode) throws Exception {
        if (StringUtils.isEmpty(accountKey)) {
            throw new BizServiceException(EErrorCode.PBC_QUERY_PARAM_EMPTY, "基本户开户许可证不可为空！");
        }
        if (StringUtils.isEmpty(regAreaCode)) {
            throw new BizServiceException(EErrorCode.PBC_QUERY_PARAM_EMPTY, "注册地区代码不可为空！");
        }
        PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(orgCode, EAccountType.AMS);
        PbcUserAccount pbcUserAccount = validatePbcUser(pbcAccountDto);

        if (pbcMockService.isLoginMockOpen()) {
            AmsCheckResultInfo amsCheckResultInfo = new AmsCheckResultInfo();
            amsCheckResultInfo.setCheckPass(true);

            AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
            amsAccountInfo.setDepositorType("企业法人");
            amsAccountInfo.setDepositorName("测试");
            amsCheckResultInfo.setAmsAccountInfo(amsAccountInfo);
            return amsCheckResultInfo;
        }

        try {
            AmsCheckResultInfo amsCheckResultInfo = amsMainService.checkPbcByAccountKeyAndRegAreaCodeForYiBanOpen(pbcUserAccount, accountKey, regAreaCode);
            if(writeMoney){
                ProofReportDto accountProofReportDto = new ProofReportDto();
                String username =  SecurityUtils.getCurrentUsername();
                if(StringUtils.isBlank(username)){
                    UserDto userDto = userService.findById(2L);
                    OrganizationDto organizationDto = organizationService.findById(userDto.getOrgId());
                    accountProofReportDto.setUsername(userDto.getUsername());
                    accountProofReportDto.setOrganFullId(organizationDto.getFullId());
                    accountProofReportDto.setTypeDetil("接口方式_开户预校验");
                    accountProofReportDto.setProofBankName(organizationDto.getName());
                }else{
                    OrganizationDto organizationDto = organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId());
                    accountProofReportDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                    accountProofReportDto.setProofBankName(organizationDto.getName());
                    accountProofReportDto.setUsername(username);
                    accountProofReportDto.setTypeDetil("账管系统_开户预校验");
                }
                accountProofReportDto.setEntname(getPbcDepositorName(amsCheckResultInfo));

                ConfigDto configDto = configService.findOneByConfigKey("pbcMoney");
                if(configDto!=null){
                    accountProofReportDto.setPrice(configDto.getConfigValue());
                }else{
                    accountProofReportDto.setPrice("0");
                }
                accountProofReportDto.setType(ProofType.PBC);
                accountProofReportDto.setDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
                accountProofReportDto.setRegAreaCode(regAreaCode);
                accountProofReportDto.setAccountKey(accountKey);
                proofReportService.save(accountProofReportDto);
            }
            return amsCheckResultInfo;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void amsAccountSync(AllBillsPublicDTO billsPublic) throws Exception {
        PbcAccountDto pbcAccountDto = null;
        if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()){
            pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullIdByCancelHeZhun(billsPublic.getOrganFullId(), EAccountType.AMS);
        }else{
            pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullId(billsPublic.getOrganFullId(), EAccountType.AMS);
        }
        amsAccountSync(pbcAccountDto, billsPublic);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void amsAccountSync(PbcAccountDto pbcAccountDto, AllBillsPublicDTO billsPublic) throws Exception {
        log.info("机构完整id-------------" + billsPublic.getOrganFullId());
        PbcUserAccount pbcUserAccount = validatePbcUser(pbcAccountDto,billsPublic);
        if(StringUtils.isNotBlank(pbcUserAccount.getLoginUserName()) && StringUtils.isNotBlank(pbcUserAccount.getLoginPassWord())){
            log.info("人行上报用户：" + pbcUserAccount.getLoginUserName() + ":" + pbcUserAccount.getLoginPassWord());
        }
        //如果是核准类账户，营业执照到期日字段置空，防止把数据上报到人行
        if((billsPublic.getCancelHeZhun() != null && !billsPublic.getCancelHeZhun()) || billsPublic.getCancelHeZhun() == null){
            if(StringUtils.isNotBlank(billsPublic.getFileDue())){
                log.info("账户为非取消核准账户；字段fileDue值：" + billsPublic.getFileDue() + ",置空！");
                billsPublic.setFileDue("");
            }
        }
        //allBills对象转换人行上报对象
        AllAcct allAcct = allBillsPublicService.allBillsPublic2AllAcctPbc(billsPublic);
        amsMainService.amsAccountSync(pbcUserAccount, allAcct);
        log.info("获取销户时，人行返回的打印参数:"+ JSON.toJSONString(allAcct.getALLAccountData()));
        //更新本异地标识
        if ("1".equals(allAcct.getOpenAccountSiteType())) {
            billsPublic.setOpenAccountSiteType(OpenAccountSiteType.LOCAL);
        } else if ("2".equals(allAcct.getOpenAccountSiteType())) {
            billsPublic.setOpenAccountSiteType(OpenAccountSiteType.ALLOPATRIC);
        }

        billsPublic.setAllAccountData(allAcct.getALLAccountData());//获取销户时，人行返回的打印参数
        //保存查询密码   如果查询密码不为空，AllBillsPublicDTO 中set该字段，保存在accountsAll  accountBillsAll   selectPwd字段
        if (StringUtils.isNotBlank(allAcct.getSelectPwd())) {
            log.info("查询密码：" + allAcct.getSelectPwd());
            billsPublic.setSelectPwd(StringUtils.trim(allAcct.getSelectPwd()));
        }
        //保存开户许可证号
        if (StringUtils.isNotBlank(allAcct.getAccountKey())) {
            if(billsPublic.getBillType() == BillType.ACCT_CHANGE && billsPublic.getAcctType() == CompanyAcctType.jiben){
                if(StringUtils.isNotBlank(billsPublic.getAccountKey()) && StringUtils.isNotBlank(allAcct.getAccountKey())){
                    if(!StringUtils.equals(billsPublic.getAccountKey(),allAcct.getAccountKey().trim())){
                        log.info("原billsPublic基本存款账户编号：" + billsPublic.getAccountKey());
                        log.info("AllAcct基本存款账户编号：" + allAcct.getAccountKey());
                        billsPublic.setAccountKey(StringUtils.trim(allAcct.getAccountKey()));
                        if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()){
                            log.info("基本存款账户编号保存日期：" + DateUtils.DateToStr(new Date(),"yyyy年MM月dd日"));
                            billsPublic.setString006(DateUtils.DateToStr(new Date(),"yyyy年MM月dd日"));
                        }
                    }
                }else if(StringUtils.isBlank(billsPublic.getAccountKey()) && StringUtils.isNotBlank(allAcct.getAccountKey())){
                    log.info("原billsPublic基本存款账户编号：" + billsPublic.getAccountKey());
                    log.info("AllAcct基本存款账户编号：" + allAcct.getAccountKey());
                    billsPublic.setAccountKey(StringUtils.trim(allAcct.getAccountKey()));
                    if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()){
                        log.info("基本存款账户编号保存日期：" + DateUtils.DateToStr(new Date(),"yyyy年MM月dd日"));
                        billsPublic.setString006(DateUtils.DateToStr(new Date(),"yyyy年MM月dd日"));
                    }
                }
            }else{
                log.info("基本存款账户编号：" + allAcct.getAccountKey());
                billsPublic.setAccountKey(StringUtils.trim(allAcct.getAccountKey()));
                if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()){
                    log.info("基本存款账户编号保存日期：" + DateUtils.DateToStr(new Date(),"yyyy年MM月dd日"));
                    billsPublic.setString006(DateUtils.DateToStr(new Date(),"yyyy年MM月dd日"));
                }
            }
        }
        //临时户（L）核准号保存
        if (billsPublic.getAcctType() == CompanyAcctType.linshi || billsPublic.getAcctType() == CompanyAcctType.feilinshi) {
            log.info("非临时存款账户编号：" + allAcct.getAccountLicenseNo());
            billsPublic.setAccountLicenseNo(StringUtils.trim(allAcct.getAccountLicenseNo()));
        }
        //保存取消核准开户许可证号 不带字母
        if (StringUtils.isNotBlank(allAcct.getOpenKey())) {
            log.info("openKey：" + allAcct.getOpenKey());
            billsPublic.setOpenKey(StringUtils.trim(allAcct.getOpenKey()));
        }

        //取消核准推送核心
        try {
            if(cancelPushCoreData && billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun() == true){
                PushCoreDataEvent pushCoreDataEvent = new PushCoreDataEvent(billsPublic);
                pushCoreDataEvent.setAllBillsPublicDTO(billsPublic);
                applicationEventPublisher.publishEvent(pushCoreDataEvent);
            }
        }catch (Exception e){
            log.error("取消核准推送核心异常，",e);
        }
    }

    @Override
    public boolean deleteAccount(PbcAccountDto pbcAccountDto,String acctNo) throws Exception {
        log.info("上报删除核准类,账号为：{}" ,acctNo);
        /**
         * 人行挡板开启
         */
        if(pbcMockService.isSyncMockOpen()){
            return true;
        }
        if(StringUtils.isBlank(acctNo)){
            //转换成人行登录对象
            throw new BizServiceException(EErrorCode.PBC_QUERY_PARAM_EMPTY, "账号不能为空");
        }
        PbcUserAccount pbcUserAccount = validatePbcUser(pbcAccountDto);
        return amsMainService.deleteAcctNoInCheckList(pbcUserAccount,acctNo);
    }

    @Override
    public boolean existAccountInCheckList(PbcAccountDto pbcAccountDto, String acctNo) throws Exception {
        if (StringUtils.isBlank(acctNo)) {
            throw new BizServiceException(EErrorCode.PBC_QUERY_PARAM_EMPTY, "账号不能为空");
        }
        PbcUserAccount pbcUserAccount = validatePbcUser(pbcAccountDto);
        return amsMainService.existAccountInCheckList(pbcUserAccount, acctNo);
    }

    @Override
    public  List<AmsPrintInfo> searchAllAccount(PbcAccountDto pbcAccountDto, String depositorName, String accountKey, String selectPwd) throws Exception {
        PbcUserAccount pbcUserAccount = validatePbcUser(pbcAccountDto);
        AllAcct allAcct = new AllAcct();
        allAcct.setDepositorName(depositorName);
        allAcct.setAccountKey(accountKey);
        allAcct.setSelectPwd(selectPwd);
        amsMainService.searchAllAccount(pbcUserAccount,allAcct);
        List<AmsPrintInfo> aLLAccountData = allAcct.getALLAccountData();
        return  aLLAccountData;
    }

    @Override
    public String jiBenUniqueCheck(AmsJibenUniqueCheckCondition amsJibenUniqueCheckCondition, String organCode) {
        try {

            if (StringUtils.isBlank(amsJibenUniqueCheckCondition.getBankCode()) || StringUtils.isBlank(amsJibenUniqueCheckCondition.getBankName())) {
                OrganizationDto organDto = organizationService.findByCode(organCode);
                if (organDto != null) {
                    amsJibenUniqueCheckCondition.setBankCode(organDto.getPbcCode());
                    amsJibenUniqueCheckCondition.setBankName(organDto.getName());
                }
            }
            //校验参数是否满足
            amsJibenUniqueCheckCondition.validateQuery();

            //设置存款人类别
            if (StringUtils.isBlank(amsJibenUniqueCheckCondition.getDepositorType())) {
                amsJibenUniqueCheckCondition.setDepositorType("01");
            }

            String depositorName = amsJibenUniqueCheckCondition.getDepositorName();

            //设置企业名称
            if (StringUtils.isBlank(depositorName)) {
                depositorName = number2Cn(RandomStringUtils.randomNumeric(5)) + "有限公司";
            }

            if ("14".equals(amsJibenUniqueCheckCondition.getDepositorType())) {
                if (!StringUtils.startsWith(depositorName, "个体户")) {
                    depositorName = "个体户" + (depositorName);
                }
            }

            log.info("唯一性校验，查询设置的企业名称为{}", depositorName);
            amsJibenUniqueCheckCondition.setDepositorName(depositorName);

            //设置随机账号
            String randomAcctNo = RandomStringUtils.randomNumeric(12);
            if (StringUtils.isBlank(amsJibenUniqueCheckCondition.getAcctNo())) {
                amsJibenUniqueCheckCondition.setAcctNo(randomAcctNo);
            }
            amsJibenUniqueCheckCondition.setAcctCreateDate(DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis()));

            if (StringUtils.isBlank(amsJibenUniqueCheckCondition.getFileNo())) {
                amsJibenUniqueCheckCondition.setFileNo(randomAcctNo);
            }
            log.info("唯一性校验，查询设置的账号为{}", randomAcctNo + "");

            PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCodeByCancelHeZhun(organCode, EAccountType.AMS);
            PbcUserAccount pbcUserAccount = validatePbcFourUser(pbcAccountDto);
            LoginAuth auth = amsMainService.amsLogin(pbcUserAccount);

            AllAcct allAcct = ConverterService.convert(amsJibenUniqueCheckCondition, AllAcct.class);
            allAcct.setAcctType(SyncAcctType.jiben);
            allAcct.setOperateType(SyncOperateType.ACCT_OPEN);
            allAcct.setAcctCreateDate(DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis()));
            if(StringUtils.isNotBlank(amsJibenUniqueCheckCondition.getLgalIdcardNo())){
                allAcct.setLegalIdcardNo(amsJibenUniqueCheckCondition.getLgalIdcardNo());
            }

            amsJibenUniqueCheckCondition.validate();
            amsJibenOpenBeiAnService.openAccountFirstStep(auth, allAcct);
            return "";
        } catch (Exception e) {
            log.error("基本户唯一性校验异常", e);
            if (e instanceof SyncException) {
                return ((SyncException) e).getCode();
            }
            return e.getMessage();
        }
    }

    @Override
    public void amsAccountSyncChangeAgain(OrganizationDto organizationDto, String acctNo) throws Exception {
        //先根据账号查询人行账户信息
        AmsAccountInfo amsAccountInfo = null;
        try {
            amsAccountInfo = getAmsAccountInfoByAcctNo(organizationDto.getCode(),acctNo);
        }catch (Exception e){
            log.error("查询人行账户详细信息异常", e);
            throw new SyncException("查询人行账户详细信息异常",e);
        }
        AllBillsPublicDTO allBillsPublicDTO = new AllBillsPublicDTO();
        //人行信息转为AllBillsPublicDTO后报送人行
        if(amsAccountInfo != null && StringUtils.isNotBlank(amsAccountInfo.getDepositorName())){
            allBillsPublicDTO.setBankCode(organizationDto.getCode());//机构号
            allBillsPublicDTO.setBankName(organizationDto.getName());//机构名称
            allBillsPublicDTO.setAcctNo(acctNo);//账号
            allBillsPublicDTO.setBillType(BillType.ACCT_CHANGE);//业务类型
            allBillsPublicDTO.setAcctCreateDate(amsAccountInfo.getAcctCreateDate());//开户日期
            allBillsPublicDTO.setOrganFullId(organizationDto.getFullId());//fullId
            allBillsPublicDTO.setAcctName(amsAccountInfo.getAcctName());//账户名称
            allBillsPublicDTO.setDepositorName(amsAccountInfo.getDepositorName());//存款人名称
            if(amsAccountInfo.getAcctType() == AccountType.yiban){
                allBillsPublicDTO.setAcctType(CompanyAcctType.yiban);
                allBillsPublicDTO.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(一般户)",amsAccountInfo.getAccountFileType()));//证明文件种类转义
                allBillsPublicDTO.setAcctFileNo(amsAccountInfo.getAccountFileNo());//证明文件号码
                allBillsPublicDTO.setRemark(amsAccountInfo.getRemark());//备注
                amsAccountSync(allBillsPublicDTO);
            }
            if(amsAccountInfo.getAcctType() == AccountType.feiyusuan){
                allBillsPublicDTO.setAcctType(CompanyAcctType.feiyusuan);
                if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileType())){
                    allBillsPublicDTO.setAcctFileType(dictionaryService.transalteLike("证明文件1种类(专用户)",amsAccountInfo.getAccountFileType()));
                }
                if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileType2())){
                    allBillsPublicDTO.setAcctFileType2(dictionaryService.transalteLike("证明文件2种类(专用户)",amsAccountInfo.getAccountFileType2()));
                }
                if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileNo())){
                    allBillsPublicDTO.setAcctFileNo(amsAccountInfo.getAccountFileNo());
                }
                if(StringUtils.isNotBlank(amsAccountInfo.getAccountFileNo2())){
                    allBillsPublicDTO.setAcctFileNo2(amsAccountInfo.getAccountFileNo2());
                }
                //账户名称构成方式
                if(StringUtils.isNotBlank(amsAccountInfo.getAccountNameFrom())){
                    allBillsPublicDTO.setAccountNameFrom(dictionaryService.transalteLike("账户构成方式",amsAccountInfo.getAccountNameFrom()));
                }
                //资金性质
                if(StringUtils.isNotBlank(amsAccountInfo.getCapitalProperty())){
                    allBillsPublicDTO.setCapitalProperty(dictionaryService.transalteLike("资金性质",amsAccountInfo.getCapitalProperty()));
                }
                allBillsPublicDTO.setFundManager(amsAccountInfo.getMoneyManager());//资金管理人姓名
                allBillsPublicDTO.setFundManagerIdcardNo(amsAccountInfo.getMoneyManagerCno());//资金管理人身份证号码
                allBillsPublicDTO.setFundManagerIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getMoneyManagerCtype()));//证件类型
                allBillsPublicDTO.setFundManagerTelephone(amsAccountInfo.getMoneyManagerTelephone());//资金管理人电话
                allBillsPublicDTO.setInsideDeptName(amsAccountInfo.getInsideDepartmentName());//内设部门名称
                allBillsPublicDTO.setInsideLeadName(amsAccountInfo.getInsideSaccdepmanName());//内设部门负责人姓名
                allBillsPublicDTO.setInsideLeadIdcardType(dictionaryService.transalteLike("法人身份证件类型",amsAccountInfo.getInsideSaccdepmanKind()));//证件类型
                allBillsPublicDTO.setInsideLeadIdcardNo(amsAccountInfo.getInsideSaccdepmanNo());//证件号码
                allBillsPublicDTO.setInsideTelephone(amsAccountInfo.getInsideTelphone());//内设部门电话
                allBillsPublicDTO.setInsideZipcode(amsAccountInfo.getInsideZipCode());//内设部门邮编
                allBillsPublicDTO.setInsideAddress(amsAccountInfo.getInsideAddress());//内设部门地址
                amsAccountSync(allBillsPublicDTO);
            }
        }
    }

    /**
     * 数字转中文用来组装企业名称
     * @param number
     * @return
     */
    private String number2Cn(String number) {
        StringBuilder sb = new StringBuilder();
        for (char c : number.toCharArray()) {
            sb.append(NUMBER_CN[Integer.valueOf(c + "")]);
        }
        return sb.toString();
    }

    @Override
	public AmsAccountInfo getAmsAccountInfoByAcctNo(String orgCode, String acctNo) throws Exception {
		PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(orgCode, EAccountType.AMS);
		return getAmsAccountInfoByAcctNo(pbcAccountDto, acctNo);
	}

	@Override
	public AmsAccountInfo getAmsAccountInfoByAcctNo(Long organId, String acctNo) throws Exception {
		PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganId(organId, EAccountType.AMS);
        AmsAccountInfo amsAccountInfo = getAmsAccountInfoByAcctNo(pbcAccountDto, acctNo);
		return amsAccountInfo;
	}

	@Override
	public AmsAccountInfo getAmsAccountInfoByAcctNo(PbcAccountDto pbcAccountDto, String acctNo) throws Exception {
		if (StringUtils.isEmpty(acctNo)) {
			throw new BizServiceException(EErrorCode.PBC_QUERY_PARAM_EMPTY, "账号不可为空！");
		}
		PbcUserAccount pbcUserAccount = validatePbcUser(pbcAccountDto);
		return amsMainService.getAmsAccountInfoByAcctNo(pbcUserAccount, acctNo);
	}

    @Override
    public AmsAccountInfo getAmsAccountInfoByAcctNoAndAcctTypeToRevoke(OrganizationDto organizationDto, String acctNo, String acctType) throws Exception {
        PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullIdByCancelHeZhun(organizationDto.getFullId(), EAccountType.AMS);
        if (StringUtils.isEmpty(acctNo)) {
            throw new BizServiceException(EErrorCode.PBC_QUERY_PARAM_EMPTY, "账号不可为空！");
        }
        PbcUserAccount pbcUserAccount = validatePbcFourUser(pbcAccountDto);
        return amsMainService.getAmsAccountInfoByRevokeAcctNoAndAcctType(pbcUserAccount, acctNo,SyncAcctType.str2enum(acctType));
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoByAcctNoFromChangeHtml(OrganizationDto organizationDto, AllAcct allAcct) throws Exception {
        PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullIdByCancelHeZhun(organizationDto.getFullId(), EAccountType.AMS);
        if (StringUtils.isEmpty(allAcct.getAcctNo())) {
            throw new BizServiceException(EErrorCode.PBC_QUERY_PARAM_EMPTY, "账号不可为空！");
        }
        PbcUserAccount pbcUserAccount = validatePbcFourUser(pbcAccountDto);
        return amsMainService.getAmsAccountInfoByAcctNoFromChangeHtml(pbcUserAccount, allAcct);
    }

    @Override
	public AmsAnnualResultStatus sumitAnnualAccount(Long organId, String acctNo) throws Exception {
		PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganId(organId, EAccountType.AMS);
		return sumitAnnualAccount(pbcAccountDto, acctNo);
	}

	@Override
	public AmsAnnualResultStatus sumitAnnualAccount(String organFullId, String acctNo) throws Exception {
		PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullId(organFullId, EAccountType.AMS);
		return sumitAnnualAccount(pbcAccountDto, acctNo);
	}

	private AmsAnnualResultStatus sumitAnnualAccount(PbcAccountDto pbcAccountDto, String acctNo) throws Exception {
		if (StringUtils.isEmpty(acctNo)) {
			throw new BizServiceException(EErrorCode.PBC_QUERY_PARAM_EMPTY, "账号不可为空！");
		}
		PbcUserAccount pbcUserAccount = validatePbcUser(pbcAccountDto);
		LoginAuth auth = amsMainService.amsLogin(pbcUserAccount);
		return amsAnnualMainService.sumbitAnnual(auth, acctNo);
	}

	private PbcUserAccount validatePbcUser(PbcAccountDto pbcAccountDto) {
        if (pbcAccountDto == null) {
            //转换成人行登录对象
            throw new BizServiceException(EErrorCode.ORGAN_AMS_USER_NOTCONFIG, "该机构未维护对应人行账户！");
        }
        if (StringUtils.isEmpty(pbcAccountDto.getAccount()) || StringUtils.isEmpty(pbcAccountDto.getPassword())) {
            throw new BizServiceException(EErrorCode.ORGAN_AMS_USER_EMPTY, "人行账管用户名或密码为空！");
        } else {
            if (!pbcAccountDto.getAccount().startsWith("2")) {
                throw new BizServiceException(EErrorCode.ORGAN_AMS_USER_MUST2LEVEL, "人行账管用户必须为人行2级操作用户");
            }
        }
        if (StringUtils.isEmpty(pbcAccountDto.getIp())) {
            throw new BizServiceException(EErrorCode.ORGAN_AMS_USER_IP_EMPTY, "人行IP不可为空！");
        }
        return allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto);

    }

    private PbcUserAccount validatePbcFourUser(PbcAccountDto pbcAccountDto) {
        if (pbcAccountDto == null) {
            //转换成人行登录对象
            throw new BizServiceException(EErrorCode.ORGAN_AMS_USER_NOTCONFIG, "该机构未维护对应人行账户！");
        }
        if (StringUtils.isEmpty(pbcAccountDto.getAccount()) || StringUtils.isEmpty(pbcAccountDto.getPassword())) {
            throw new BizServiceException(EErrorCode.ORGAN_AMS_USER_EMPTY, "人行账管用户名或密码为空！");
        } else {
            if (!pbcAccountDto.getAccount().startsWith("4")) {
                throw new BizServiceException(EErrorCode.ORGAN_AMS_USER_MUST2LEVEL, "人行账管用户必须为人行4级操作用户");
            }
        }
        if (StringUtils.isEmpty(pbcAccountDto.getIp())) {
            throw new BizServiceException(EErrorCode.ORGAN_AMS_USER_IP_EMPTY, "人行IP不可为空！");
        }
        return allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto);
    }

    private PbcUserAccount validatePbcUser(PbcAccountDto pbcAccountDto,AllBillsPublicDTO billsPublic) {
        if (pbcAccountDto == null) {
            if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()){
                throw new BizServiceException(EErrorCode.ORGAN_AMS_USER_MUST2LEVEL, "请维护人行账管用户4级操作用户");
            }else{
                //转换成人行登录对象
                throw new BizServiceException(EErrorCode.ORGAN_AMS_USER_NOTCONFIG, "该机构未维护对应人行账户！");
            }
        }
        if (StringUtils.isEmpty(pbcAccountDto.getAccount()) || StringUtils.isEmpty(pbcAccountDto.getPassword())) {
            throw new BizServiceException(EErrorCode.ORGAN_AMS_USER_EMPTY, "人行账管用户名或密码为空！");
        } else {
            if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()){
                if (!pbcAccountDto.getAccount().startsWith("4")) {
                    throw new BizServiceException(EErrorCode.ORGAN_AMS_USER_MUST2LEVEL, "请维护人行账管用户4级操作用户");
                }
            }else{
                if (!pbcAccountDto.getAccount().startsWith("2")) {
                    throw new BizServiceException(EErrorCode.ORGAN_AMS_USER_MUST2LEVEL, "人行账管用户必须为人行2级操作用户");
                }
            }
        }
        if (StringUtils.isEmpty(pbcAccountDto.getIp())) {
            throw new BizServiceException(EErrorCode.ORGAN_AMS_USER_IP_EMPTY, "人行IP不可为空！");
        }
        return allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto);
    }

    private String getPbcDepositorName(AmsCheckResultInfo amsCheckResultInfo) {
        if(amsCheckResultInfo != null && amsCheckResultInfo.getAmsAccountInfo() != null) {
            return amsCheckResultInfo.getAmsAccountInfo().getDepositorName();
        }

        return "";
    }

    private AmsCheckResultInfo checkPbcByAccountKeyAndRegAreaCodeCommon(String orgCode, String accountKey, String regAreaCode, String username) throws Exception {
        if (StringUtils.isEmpty(accountKey)) {
            throw new BizServiceException(EErrorCode.PBC_QUERY_PARAM_EMPTY, "基本户开户许可证不可为空！");
        }
        if (StringUtils.isEmpty(regAreaCode)) {
            throw new BizServiceException(EErrorCode.PBC_QUERY_PARAM_EMPTY, "注册地区代码不可为空！");
        }

        PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(orgCode, EAccountType.AMS);
        PbcUserAccount pbcUserAccount = validatePbcUser(pbcAccountDto);

        if (pbcMockService.isLoginMockOpen()) {
            AmsCheckResultInfo amsCheckResultInfo = new AmsCheckResultInfo();
            amsCheckResultInfo.setCheckPass(true);
            AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
            amsAccountInfo.setDepositorType("01");
            amsAccountInfo.setDepositorName("浙江省易得融信软件有限公司");
            amsCheckResultInfo.setAmsAccountInfo(amsAccountInfo);
            return amsCheckResultInfo;
        }
        try {
            AmsCheckResultInfo amsCheckResultInfo = amsMainService.checkPbcByAccountKeyAndRegAreaCode(pbcUserAccount, accountKey, regAreaCode);
            if (pbcDataLog && amsCheckResultInfo != null) {
                log.info("开户许可证：" + accountKey + "。人行返回数据为：{}", JSON.toJSONString(amsCheckResultInfo));
            }
            if(writeMoney){
                ProofReportDto accountProofReportDto = new ProofReportDto();
                if(StringUtils.isBlank(username)) {
                    username =  SecurityUtils.getCurrentUsername();
                    if(StringUtils.isBlank(username)){
                        UserDto userDto = userService.findById(2L);
                        OrganizationDto organizationDto = organizationService.findById(userDto.getOrgId());
                        accountProofReportDto.setTypeDetil("接口方式_开户预校验");
                        accountProofReportDto.setOrganFullId(organizationDto.getFullId());
                        accountProofReportDto.setUsername(userDto.getUsername());
                        accountProofReportDto.setProofBankName(organizationDto.getName());
                    }else{
                        OrganizationDto organizationDto = organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId());
                        accountProofReportDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                        accountProofReportDto.setUsername(username);
                        accountProofReportDto.setTypeDetil("账管系统_开户预校验");
                        accountProofReportDto.setProofBankName(organizationDto.getName());
                    }
                } else {
                    OrganizationDto organizationDto = organizationService.findByCode(orgCode);
                    accountProofReportDto.setTypeDetil("接口方式_开户预校验");
                    accountProofReportDto.setOrganFullId(organizationDto.getFullId());
                    accountProofReportDto.setUsername(username);
                    accountProofReportDto.setProofBankName(organizationDto.getName());
                }

                accountProofReportDto.setEntname(getPbcDepositorName(amsCheckResultInfo));

                ConfigDto configDto = configService.findOneByConfigKey("pbcMoney");
                if(configDto!=null){
                    accountProofReportDto.setPrice(configDto.getConfigValue());
                }else{
                    accountProofReportDto.setPrice("0");
                }
                accountProofReportDto.setType(ProofType.PBC);
                accountProofReportDto.setAccountKey(accountKey);
                accountProofReportDto.setRegAreaCode(regAreaCode);
                accountProofReportDto.setDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
                proofReportService.save(accountProofReportDto);
            }
            return amsCheckResultInfo;
        } catch (Exception e) {
            //如果一般户和预算户都无法显示该基本户信息，则用其他机构进行查询
            //该企业(社会团体或事业单位等)通过接口无法显示基本户详细信息
            if (e instanceof SyncException && e.getMessage().contains("通过接口无法显示基本户详细信息")) {
                //用其他机构来查询本接口
                PbcUserAccount pbcUserOther = validatePbcUser(pbcAccountService.getAdjacentByCode(orgCode, EAccountType.AMS));
                AmsCheckResultInfo amsCheckResultInfo = amsMainService.checkPbcByAccountKeyAndRegAreaCode(pbcUserOther, accountKey, regAreaCode);
                if (pbcDataLog && amsCheckResultInfo != null) {
                    log.info("开户许可证：" + accountKey + "。人行返回数据为：{}", JSON.toJSONString(amsCheckResultInfo));
                }
                if(writeMoney){
                    ProofReportDto accountProofReportDto = new ProofReportDto();

                    if(StringUtils.isBlank(username)) {
                        username =  SecurityUtils.getCurrentUsername();
                        if(StringUtils.isBlank(username)){
                            UserDto userDto = userService.findById(2L);
                            OrganizationDto organizationDto = organizationService.findById(userDto.getOrgId());
                            accountProofReportDto.setTypeDetil("接口方式_开户预校验");
                            accountProofReportDto.setUsername(userDto.getUsername());
                            accountProofReportDto.setOrganFullId(organizationDto.getFullId());
                            accountProofReportDto.setProofBankName(organizationDto.getName());
                        }else{
                            OrganizationDto organizationDto = organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId());
                            accountProofReportDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                            accountProofReportDto.setUsername(username);
                            accountProofReportDto.setProofBankName(organizationDto.getName());
                            accountProofReportDto.setTypeDetil("账管系统_开户预校验");
                        }
                    } else {
                        OrganizationDto organizationDto = organizationService.findByCode(orgCode);
                        accountProofReportDto.setTypeDetil("接口方式_开户预校验");
                        accountProofReportDto.setUsername(username);
                        accountProofReportDto.setOrganFullId(organizationDto.getFullId());
                        accountProofReportDto.setProofBankName(organizationDto.getName());
                    }

                    accountProofReportDto.setEntname(getPbcDepositorName(amsCheckResultInfo));

                    ConfigDto configDto = configService.findOneByConfigKey("pbcMoney");
                    if(configDto!=null){
                        accountProofReportDto.setPrice(configDto.getConfigValue());
                    }else{
                        accountProofReportDto.setPrice("0");
                    }
                    accountProofReportDto.setType(ProofType.PBC);
                    accountProofReportDto.setRegAreaCode(regAreaCode);
                    accountProofReportDto.setAccountKey(accountKey);
                    accountProofReportDto.setDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
                    proofReportService.save(accountProofReportDto);
                }
                return amsCheckResultInfo;
            } else {
                throw e;
            }
        }

    }

}
