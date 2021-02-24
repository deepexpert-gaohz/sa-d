package com.ideatech.ams.account.service.bill;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dao.*;
import com.ideatech.ams.account.dao.bill.AccountBillsAllDao;
import com.ideatech.ams.account.dao.bill.spec.AccountBillsAllSearchSpec;
import com.ideatech.ams.account.dao.bill.spec.AccountBillsAllSpec;
import com.ideatech.ams.account.domain.CountDo;
import com.ideatech.ams.account.dao.spec.OpenAccountStatisticsSpec;
import com.ideatech.ams.account.dto.*;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.dto.poi.StatisticsForDatePoi;
import com.ideatech.ams.account.dto.poi.StatisticsForKXHPoi;
import com.ideatech.ams.account.entity.*;
import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.account.enums.*;
import com.ideatech.ams.account.enums.bill.*;
import com.ideatech.ams.account.executor.ApplyFinalStatusUpdateExecutor;
import com.ideatech.ams.account.service.*;
import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.account.service.pbc.PbcEccsService;
import com.ideatech.ams.account.service.poi.StatisticsForDatePoiExport;
import com.ideatech.ams.account.service.poi.StatisticsForKXHPoiExport;
import com.ideatech.ams.account.spi.AllPublicAccountFormProcessor;
import com.ideatech.ams.account.util.AcctCheckNullUtil;
import com.ideatech.ams.account.util.FormUtils;
import com.ideatech.ams.account.util.Map2DomainUtils;
import com.ideatech.ams.account.validate.AllPublicAccountValidate;
import com.ideatech.ams.account.view.ReportStatus;
import com.ideatech.ams.account.view.OpenAccountStatisticsView;
import com.ideatech.ams.apply.cryptography.CryptoAddApplyAcctVo;
import com.ideatech.ams.apply.cryptography.CryptoEditApplyAcctVo;
import com.ideatech.ams.apply.cryptography.CryptoModifyAcctKeyMessage;
import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.dto.RSACode;
import com.ideatech.ams.apply.enums.ApplyEnum;
import com.ideatech.ams.apply.service.CompanyPreOpenAccountEntService;
import com.ideatech.ams.customer.dto.*;
import com.ideatech.ams.customer.enums.CustomerType;
import com.ideatech.ams.customer.service.*;
import com.ideatech.ams.customer.service.bill.CustomerBillsAllService;
import com.ideatech.ams.kyc.dto.KycSearchHistoryDto;
import com.ideatech.ams.kyc.service.KycSearchHistoryService;
import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsCheckResultInfo;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.enums.SyncAcctType;
import com.ideatech.ams.pbc.enums.SyncOperateType;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.configuration.dto.AccountConfigureDto;
import com.ideatech.ams.system.configuration.service.AccountConfigureService;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.operateLog.dto.OperateLogDto;
import com.ideatech.ams.system.operateLog.service.OperateLogService;
import com.ideatech.ams.system.org.dto.OrganRegisterDto;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganRegisterService;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.proof.dto.ProofReportDto;
import com.ideatech.ams.system.proof.enums.ProofType;
import com.ideatech.ams.system.proof.service.ProofReportService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.system.whitelist.dto.WhiteListDto;
import com.ideatech.ams.system.whitelist.service.WhiteListService;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.entity.id.IdWorker;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.jpa.IdeaNamingStrategy;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author vantoo
 * @date 16:07 2018/5/18
 */
@Service
@Transactional
@Slf4j
public class AllBillsPublicServiceImpl implements AllBillsPublicService {

    @Autowired
    private PbcSyncListDao pbcSyncListDao;

    @Autowired
    private AmsMainService amsMainService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private CustomerBillsAllService customerBillsAllService;

    @Autowired
    private CustomerPublicMidService customerPublicMidService;

    @Autowired
    private AccountChangeSummaryService accountChangeSummaryService;

    @Autowired
    private CustomersAllService customersAllService;

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private AccountPublicService accountPublicService;

    @Autowired
    private AccountPublicLogService accountPublicLogService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private Map<String, AllPublicAccountValidate> validateMap;

    @Autowired
    private CustomerPublicService customerPublicService;

    @Autowired
    private CustomerPublicLogService customerPublicLogService;

    @Autowired
    private AllBillsPublicDao allBillsPublicDao;

    @Autowired
    private AccountsAllDao accountsAllDao;

    @Autowired
    private UserService userService;

    @Autowired
    private Map<String, AllPublicAccountFormProcessor> formProcessorMap;

    @Autowired
    private AllAccountPublicDao allAccountPublicDao;

    @Autowired
    private AccountPublicDao accountPublicDao;

    @Autowired
    private PbcAmsService pbcAmsService;

    @Autowired
    private PbcEccsService pbcEccsService;

    @Autowired
    private CompanyPartnerService companyPartnerService;

    @Autowired
    private CompanyPartnerMidService companyPartnerMidService;

    @Autowired
    private CompanyPartnerLogService companyPartnerLogService;

    @Autowired
    private RelateCompanyService relateCompanyService;

    @Autowired
    private RelateCompanyMidService relateCompanyMidService;

    @Autowired
    private RelateCompanyLogService relateCompanyLogService;

    @Autowired
    private BillNoSeqService billNoSeqService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private TransactionUtils transactionUtils;

    @Autowired
    private PbcSyncListService pbcSyncListService;

    @Autowired
    private OperateLogService operateLogService;

    @Value("${ams.company.pbc.eccs:true}")
    private boolean needSyncEccs;

    @Autowired
    private ConfigService configService;

    @Autowired
    private AcctNoGeneService acctNoGeneService;

    @Autowired
    private CoreOpenSyncService coreOpenSyncService;

    @Autowired
    private CoreChangeSyncService coreChangeSyncService;

    @Autowired
    private CompanyPreOpenAccountEntService companyPreOpenAccountEntService;

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private AccountBillsAllDao accountBillsAllDao;

    @Autowired
    private WhiteListService whiteListService;

    @Autowired
    private AccountConfigureService accountConfigureService;

    @Autowired
    private OrganRegisterService organRegisterService;

    @Autowired
    private AccountChangeSummaryDao accountChangeSummaryDao;

    @Autowired
    private AccountChangeItemDao accountChangeItemDao;

    @Autowired
    private OpenAccountStatisticsDao openAccountStatisticsDao;
    @Autowired
    private SyncHistoryService syncHistoryService;
    @Autowired
    private ProofReportService proofReportService;

    @Autowired
    private KycSearchHistoryService kycSearchHistoryService;
    @Value("${ams.company.writeMoney}")
    private boolean writeMoney;

    @PersistenceContext
    private EntityManager em; //注入EntityManager

    @Value("${ams.company.check:true}")
    private boolean isCheck;

    @Value("${ams.company.cancelHezhunCheckDate:false}")
    private boolean cancelHezhunCheckDate;

    @Value("${ams.company.changeToSaveAgain:false}")
    private boolean changeToSaveAgain;

    /**
     * 是否基于存量数据。否则变更、久悬、销户 对于无存量数据的情况下，直接新建客户账户信息
     */
    @Value("${ams.company.datenbestand:true}")
    private Boolean datenbestand;

    /**
     * 预约新接口模式是否启用：默认false不启用
     */
    @Value("${apply.newRule.flag:false}")
    private Boolean applyNewRuleFlag;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private RSACode rsaCode;

    @Override
    public Long save(AllBillsPublicDTO billsPublic, UserDto userDto, Boolean isValidate) throws Exception {
        if (userDto == null) {
            String currentUserName = SecurityUtils.getCurrentUsername();
            if (StringUtils.isNotBlank(currentUserName)) {
                userDto = userService.findByUsername(currentUserName);
            } else {
                userDto = userService.findVirtualUser();
            }
        }
        AccountBillsAllInfo accountBillsAllInfo = null;
        // 验证必要字段
        if (isValidate) { //保存时不校验
            //1.验证数据完整性
            billsPublic.validate();
        }

        //2.验证是否有未完成流水(为新增流水时，提前生成关联单号)
        if (billsPublic.getId() == null) {
            validateUnfinishedBill(billsPublic);
            if (billsPublic.getRecId() == null) {
                billsPublic.setRefBillId(getRecId());
            } else {
                billsPublic.setRefBillId(billsPublic.getRecId());
            }
            initBill(billsPublic);
        }

        //获取账户信息
        AccountsAllInfo accountsAllInfo = null;
        if (StringUtils.isNotBlank(billsPublic.getAcctNo())) {
            if (billsPublic.getId() == null) {
                //新增
                accountsAllInfo = accountsAllService.findByAcctNo(billsPublic.getAcctNo());
            } else {
                //审核并上报
                accountsAllInfo = accountsAllService.findByAcctNoAndIdNot(billsPublic.getAcctNo(), billsPublic.getAccountId());
                //审核并上报不改变账户所属机构
                AccountsAll accountOrg = accountsAllDao.findOne(billsPublic.getAcctId());
                if (accountOrg != null) {
                    billsPublic.setAcctOrgFullid(accountOrg.getOrganFullId());
                }
            }
            if (billsPublic.getAccountId() == null && accountsAllInfo != null) {
                billsPublic.setAccountId(accountsAllInfo.getId());
            }
        }

        //3.开户流水
        if (billsPublic.getBillType() == BillType.ACCT_OPEN) {
            if (accountsAllInfo != null) {
                //白名单状态赋值
                accountsAllInfo.setWhiteList(billsPublic.getWhiteList());
                if (accountsAllInfo.getAcctType() == CompanyAcctType.zengzi || accountsAllInfo.getAcctType() == CompanyAcctType.yanzi || "0".equals(accountsAllInfo.getWhiteList())) {
                    billsPublic.setAccountStatus(AccountStatus.notActive);
                }else{
                    if(accountsAllInfo.getAccountStatus() == AccountStatus.revoke) {
                        billsPublic.setAccountStatus(AccountStatus.notActive);
                    } else {
                        throw new RuntimeException("账户" + billsPublic.getAcctNo() + "已开户，请勿重复开户！");
                    }
                }
            }

            //判断该开户是本地开户还是异地开户
            if(billsPublic.getOpenAccountSiteType()!=null){
                billsPublic.setOpenAccountSiteType(billsPublic.getOpenAccountSiteType());
            }

            //3.1处理客户信息
            //TODO 客户信息独立维护的情况，此处不需要处理
            CustomerPublicMidInfo custPublicMidInfo = saveCustPublicMid(billsPublic, userDto.getId());
            if (custPublicMidInfo != null) {
                log.info("1.保存客户中间表信息,客户号:" + custPublicMidInfo.getCustomerNo() + ",客户名称:" + custPublicMidInfo.getDepositorName());
                // 3.2 处理账户信息
                billsPublic.setCustomerLogId(custPublicMidInfo.getCustomerLogId());
                billsPublic.setMidId(custPublicMidInfo.getId());
                AccountPublicInfo acctPublicInfo = saveAccountPublic(billsPublic, userDto.getId());

                log.info("2.保存账户信息,ID:" + acctPublicInfo.getAccountId());

                if (acctPublicInfo != null) {
                    // 1.3 处理流水信息
                    billsPublic.setAccountId(acctPublicInfo.getAccountId());
                    billsPublic.setFinalStatus(CompanyIfType.No);
                    accountBillsAllInfo = saveBillsAll(billsPublic, userDto.getId());
                    log.info("3.保存流水信息" + billsPublic.getBillNo());
                }

                //白名单审核通过后执行updateFinalStatus
                WhiteListDto whiteListDto = whiteListService.getByEntnameAndOrgId(billsPublic.getDepositorName(), userDto.getOrgId());
                if (whiteListDto != null && !"delete".equals(whiteListDto.getStatus()) && billsPublic.getStatus() == BillStatus.APPROVED) {
                    log.info("该账户为白名单账户，系统默认上报状态为不同步，执行updateFinalStatus....");
                    //更新上报状态
                    accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.PBC, CompanySyncStatus.buTongBu, CompanySyncOperateType.autoSyncType, "", userDto.getId());
                    accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.ECCS, CompanySyncStatus.buTongBu, CompanySyncOperateType.autoSyncType, "", userDto.getId());
                    updateFinalStatus(billsPublic, userDto.getId());
                }
            } else {
                throw new RuntimeException("处理客户信息临时表异常！");
            }

        } else if (billsPublic.getBillType() == BillType.ACCT_CHANGE) {
            //4.变更流水
            // 4.1 处理客户信息
            CustomerPublicMidInfo custPublicMidInfo = saveCustPublicMid(billsPublic, userDto.getId());
            if (custPublicMidInfo != null) {
                log.info("1.保存客户中间表信息,客户号:" + custPublicMidInfo.getCustomerNo() + ",客户名称:" + custPublicMidInfo.getDepositorName());
                // 标识流水的客户信息id
                billsPublic.setCustomerLogId(custPublicMidInfo.getId());
                billsPublic.setMidId(custPublicMidInfo.getId());
                if (billsPublic.getId() == null) {
                    // 流水为新增变更单，需将原账户信息留存到日志记录表
                    // 原先未在本系统开户
                    AccountPublicInfo accountPublicInfo = null;
                    if (billsPublic.getAccountId() != null && billsPublic.getAccountId() > 0) {
                        accountPublicInfo = accountPublicService.findByAccountId(billsPublic.getAccountId());
                    } else {
                        accountPublicInfo = accountPublicService.findByAcctNo(billsPublic.getAcctNo());
                    }
                    if (accountPublicInfo != null) {
                        billsPublic.setAccountId(accountPublicInfo.getAccountId());
                        billsPublic.setOpenAccountSiteType(accountPublicInfo.getOpenAccountSiteType());
                        // 保存日志记录
                        saveAccountLog(accountPublicInfo);
                        log.info("2.保存流水信息" + billsPublic.getBillNo());
                    }
                } else {
                    //流水的状态为“驳回”后，重新修改保存时
                    if (billsPublic.getStatus() == BillStatus.REJECT) {
                        // 检查该保存流水对应账户在日志中是否存在，存在则删除日志记录
                        // 因变更单在驳回的情况下会将账户信息放到日志中，待驳回重新编辑发起时，需将该笔在日志中删除
                        accountPublicLogService.deleteByAccountIdAndRefBillId(billsPublic.getAccountId(), billsPublic.getId());
                        log.info("2.删除账户日志信息" + billsPublic.getAcctNo());
                        // 需将原账户信息留存到日志记录表
                        AccountPublicInfo accountPublicInfo = accountPublicService.findByAccountId(billsPublic.getAccountId());
                        // 保存日志记录
                        saveAccountLog(accountPublicInfo);
                        log.info("3.保存账户日志信息,账户ID:" + accountPublicInfo.getAccountId());

                        //将流水状态重新至为“新建”
                        if ("yes".equalsIgnoreCase(billsPublic.getSubmitApproveFlag())) {
                            billsPublic.setStatus(BillStatus.APPROVING);
                        } else {
                            billsPublic.setStatus(BillStatus.NEW);
                        }
                    }
                }
                // 保存账户信息
                AccountPublicInfo acctPublicInfo = saveAccountPublic(billsPublic, userDto.getId());
                log.info("4.保存账户信息" + billsPublic.getAcctNo());

                if (acctPublicInfo != null) {
                    // 1.3 处理流水信息
                    billsPublic.setAccountId(acctPublicInfo.getAccountId());
                    accountBillsAllInfo = saveBillsAll(billsPublic, userDto.getId());
                    log.info("5.保存流水信息" + billsPublic.getBillNo());
                }

                //白名单审核通过后执行updateFinalStatus
                WhiteListDto whiteListDto = whiteListService.getByEntnameAndOrgId(billsPublic.getDepositorName(), userDto.getOrgId());
                if ((whiteListDto != null && !"delete".equals(whiteListDto.getStatus()))
                        //增加账户数据变更无需上报人行及代码证系统的判断
                        || ((billsPublic.getPbcSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getPbcSyncStatus() == CompanySyncStatus.buTongBu)
                        && (billsPublic.getEccsSyncStatus() ==CompanySyncStatus.tongBuChengGong || billsPublic.getEccsSyncStatus() == CompanySyncStatus.buTongBu))
                        && billsPublic.getStatus() == BillStatus.APPROVED && billsPublic.getId() != null) {
                    //更新上报状态
                    log.info("该账户为白名单或变更无需上报人行账管，系统默认上报状态为不同步，执行updateFinalStatus....");
                    accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.PBC, CompanySyncStatus.buTongBu, CompanySyncOperateType.autoSyncType, "", userDto.getId());
                    accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.ECCS, CompanySyncStatus.buTongBu, CompanySyncOperateType.autoSyncType, "", userDto.getId());
                    updateFinalStatus(billsPublic, userDto.getId());
                }
            } else {
                throw new RuntimeException("处理客户信息临时表异常！");
            }

        } else if (billsPublic.getBillType() == BillType.ACCT_REVOKE
                || billsPublic.getBillType() == BillType.ACCT_SUSPEND || billsPublic.getBillType()== BillType.ACCT_CLOSESUSPEND) {

            //先查询改账户在账管的账户状态，如果是销户的话return
            if(accountsAllInfo != null){
                if(billsPublic.getBillType() == BillType.ACCT_REVOKE) {  //久悬户支持销户操作
                    if(accountsAllInfo.getAccountStatus() != AccountStatus.normal &&
                            accountsAllInfo.getAccountStatus() != AccountStatus.suspend) {
                        throw new RuntimeException(ResultCode.ACCT_DATA_NOT_NORMAL.message() + "无法" + billsPublic.getBillType().getValue());
                    }
                }
            }
            // 5.销户、久悬
            // 5.1 处理客户信息到中间表（为留存当前流水的客户信息镜像，流水终态时直接处理到日志表中）
            CustomerPublicMidInfo custPublicMidInfo = saveCustPublicMid(billsPublic, userDto.getId());

            if (custPublicMidInfo != null) {
                log.info("1.保存客户中间表信息" + custPublicMidInfo.getDepositorName());
                billsPublic.setCustomerLogId(custPublicMidInfo.getCustomerLogId());
                billsPublic.setMidId(custPublicMidInfo.getId());
                Long accountId = billsPublic.getAccountId();
                AccountPublicInfo accountPublicInfo = null;
                if (accountId != null) {
                    accountPublicInfo = accountPublicService.findByAccountId(accountId);
                } else {
                    accountPublicInfo = accountPublicService.findByAcctNo(billsPublic.getAcctNo());
                }
                if(accountPublicInfo != null){
                    billsPublic.setOpenAccountSiteType(accountPublicInfo.getOpenAccountSiteType());
                }
                // 3.2 保存销户流水
                accountBillsAllInfo = saveBillsAll(billsPublic, userDto.getId());
                log.info("2.保存流水信息" + billsPublic.getBillNo());
                // 3.3 保存账户日志记录
                if (accountPublicInfo != null) {
                    accountId = accountPublicInfo.getAccountId();
                    billsPublic.setAccountId(accountId);
                    //变更大类账户性质为小类,在小类为空时才处理
                    if (accountPublicInfo.getAcctType() == null) {
                        if (accountPublicInfo.getAcctType() == CompanyAcctType.specialAcct || accountPublicInfo.getAcctType() == CompanyAcctType.tempAcct) {
                            if (billsPublic.getAcctType() != CompanyAcctType.specialAcct && billsPublic.getAcctType() != CompanyAcctType.tempAcct) {
                                accountPublicInfo.setAcctType(billsPublic.getAcctType());
                                accountPublicService.save(accountPublicInfo);
                            }
                        }
                    }
                    //销户时更新账户信息
                    ConfigDto configDto = configService.findOneByConfigKey("revokeEnabled");
                    if(configDto!=null && StringUtils.equals("true",configDto.getConfigValue())){
                        saveAccountPublic(billsPublic, userDto.getId());
                        log.info("更新账户信息：" + billsPublic.getAcctNo());
                    }
                    if (billsPublic.getId() == null) {
                        // 单据为新增变更单，需将原账户信息留存到历史记录表
                        // 保存日志记录
                        saveAccountLog(accountPublicInfo);
                        log.info("3.保存账户历史信息" + billsPublic.getAcctNo());
                    }
                } else {
                    // 保存账户信息
                    AccountPublicInfo acctPublicInfo = saveAccountPublic(billsPublic, userDto.getId());
                    accountId = acctPublicInfo.getAccountId();
                    log.info("4.创建账户信息" + billsPublic.getAcctNo());
                    billsPublic.setAccountId(accountId);
                }

                // 3.3 更新账户关联单（待申请审核通过后，更新账户状态）
                AccountsAllInfo accountsAll = accountsAllService.getOne(accountId);
                accountsAll.setAcctCancelReason(billsPublic.getAcctCancelReason());
                accountsAll.setCancelDate(billsPublic.getCancelDate());
                accountsAll.setBankCode(billsPublic.getBankCode());
                accountsAll.setBankName(billsPublic.getBankName());
                accountsAll.setRefBillId(accountBillsAllInfo.getId());
                accountsAllService.save(accountsAll);
                log.info("5.更新账户关联单信息" + billsPublic.getAcctNo());
                accountBillsAllInfo.setAccountId(accountId);
                accountBillsAllService.save(accountBillsAllInfo);
                log.info("6.更新账户信息");
                //白名单审核通过后执行updateFinalStatus   //核准类账户销户内部流转结束，线下人工上报
                WhiteListDto whiteListDto = whiteListService.getByEntnameAndOrgId(billsPublic.getDepositorName(), userDto.getOrgId());
                if ((whiteListDto != null && !"delete".equals(whiteListDto.getStatus()) && billsPublic.getStatus() == BillStatus.APPROVED)
                        || (billsPublic.getBillType() == BillType.ACCT_REVOKE && billsPublic.getAcctType().isHeZhun() && (billsPublic.getCancelHeZhun() == null || billsPublic.getCancelHeZhun() != true) && billsPublic.getStatus() == BillStatus.APPROVED)
                        //增加取消核准销户，同步成功或者不同步的结束
                        || (billsPublic.getBillType() == BillType.ACCT_REVOKE && billsPublic.getAcctType().isHeZhun() && (billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun() == true) && billsPublic.getStatus() == BillStatus.APPROVED
                        && (billsPublic.getPbcSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getPbcSyncStatus() == CompanySyncStatus.buTongBu))
                ) {
                    //更新上报状态
                    log.info("该账户为白名单或核准类销户数据，系统默认上报状态为不同步，执行updateFinalStatus....");
                    accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.PBC, CompanySyncStatus.buTongBu, CompanySyncOperateType.autoSyncType, "", userDto.getId());
                    accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.ECCS, CompanySyncStatus.buTongBu, CompanySyncOperateType.autoSyncType, "", userDto.getId());
                    updateFinalStatus(billsPublic, userDto.getId());
                }
            }

        } else if (billsPublic.getBillType() == BillType.ACCT_EXTENSION) {
            // 展期业务
            CustomerPublicMidInfo custPublicMidInfo = saveCustPublicMid(billsPublic, userDto.getId());

            if (custPublicMidInfo != null) {
                log.info("1.保存客户中间表信息" + custPublicMidInfo.getDepositorName());
                billsPublic.setCustomerLogId(custPublicMidInfo.getCustomerLogId());
                billsPublic.setMidId(custPublicMidInfo.getId());
                // 3.2 保存流水
                accountBillsAllInfo = saveBillsAll(billsPublic, userDto.getId());
                log.info("2.保存流水信息" + billsPublic.getBillNo());
                // 3.3 保存账户日志记录
                Long accountId = billsPublic.getAccountId();
                AccountPublicInfo accountPublicInfo = null;
                if (accountId != null) {
                    accountPublicInfo = accountPublicService.findByAccountId(accountId);
                } else {
                    accountPublicInfo = accountPublicService.findByAcctNo(billsPublic.getAcctNo());
                }

                if (accountPublicInfo != null) {
                    billsPublic.setAccountId(accountPublicInfo.getAccountId());
                    // 保存日志记录
                    saveAccountLog(accountPublicInfo);
                    log.info("3.保存账户历史信息" + billsPublic.getAcctNo());
                }

                // 保存账户信息
                AccountPublicInfo acctPublicInfo = saveAccountPublic(billsPublic, userDto.getId());
                log.info("4.保存账户信息" + billsPublic.getAcctNo());

                if (acctPublicInfo != null) {
                    // 1.3 处理流水信息
                    billsPublic.setAccountId(acctPublicInfo.getAccountId());
                    accountBillsAllInfo = saveBillsAll(billsPublic, userDto.getId());
                    log.info("5.保存流水信息" + billsPublic.getBillNo());
                }
                log.info("6.更新账户信息");
                //白名单审核通过后执行updateFinalStatus
                WhiteListDto whiteListDto = whiteListService.getByEntnameAndOrgId(billsPublic.getDepositorName(), userDto.getOrgId());
                if (whiteListDto != null && !"delete".equals(whiteListDto.getStatus()) && billsPublic.getStatus() == BillStatus.APPROVED) {
                    //更新上报状态
                    log.info("该账户为白名单数据，系统默认上报状态为不同步，执行updateFinalStatus....");
                    accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.PBC, CompanySyncStatus.buTongBu, CompanySyncOperateType.autoSyncType, "", userDto.getId());
                    accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.ECCS, CompanySyncStatus.buTongBu, CompanySyncOperateType.autoSyncType, "", userDto.getId());
                    updateFinalStatus(billsPublic, userDto.getId());
                }
            }

        } else if (billsPublic.getBillType() == BillType.ACCT_INIT) {
            boolean flag = false;
            //6.初始化信息流水
            //6.1 处理客户信息
            billsPublic.setFromSource(BillFromSource.INIT);
            billsPublic.setPbcSyncStatus(CompanySyncStatus.tongBuChengGong);
            if (billsPublic != null) {
                if (billsPublic.getAcctType() == CompanyAcctType.jiben && needSyncEccs && (billsPublic.getBillType() == BillType.ACCT_OPEN || billsPublic.getBillType() == BillType.ACCT_CHANGE)) {
                    billsPublic.setEccsSyncStatus(CompanySyncStatus.tongBuChengGong);
                } else {
                    billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
                }
            }

            if (billsPublic.getAcctNo() == null) {
                throw new EacException("账号不能为空:" + billsPublic.getAcctNo());
            }
            if (accountsAllService.countByAcctNo(billsPublic.getAcctNo()) > 0) {
                flag = true;
                AccountsAll byAcctNo = accountsAllDao.findByAcctNo(billsPublic.getAcctNo());
                //System.out.println("进入判断逻辑代码区："+flag+"账号为："+billsPublic.getAcctNo());

                String customerNo = byAcctNo.getCustomerNo();
                List<AccountBillsAllInfo> byAccountId = accountBillsAllService.findByAccountId(byAcctNo.getId());

                AccountBillsAllInfo byCustomerNo = null;
                for (AccountBillsAllInfo accountBillsAllInfo1 : byAccountId) {

                    if (accountBillsAllInfo1.getAccountId().equals(byAcctNo.getId()) && accountBillsAllInfo1.getBillType() == BillType.ACCT_INIT) {
                        byCustomerNo = accountBillsAllInfo1;
                    }

                }

                ///*****************************************************************

                // AccountBillsAllInfo byCustomerNo = accountBillsAllService.getByCustomerNo(customerNo);

                Long id = byAcctNo.getId();

                billsPublic.setId(byCustomerNo.getId());

                billsPublic.setAccountId(id);
            }
            if(billsPublic.getOpenAccountSiteType()!=null){
                billsPublic.setOpenAccountSiteType(billsPublic.getOpenAccountSiteType());
            }else{
                billsPublic.setOpenAccountSiteType(OpenAccountSiteType.LOCAL);
            }
            billsPublic.setPbcSyncTime("");
            billsPublic.setEccsSyncTime("");
            billsPublic.setPbcCheckDate("");
            //**存量可重复导入代码修改
            Long customerId = null;
            if (flag) {

                // System.out.println("update开始客户信息表处理****************************************");
                customerId = updateCustPublic(billsPublic, userDto.getId());
                // System.out.println("update结束客户信息表处理*******************************************");
            } else {
                customerId = insertCustPublic(billsPublic, userDto.getId());
            }
            // customerId = insertCustPublic(billsPublic, userDto.getId());
            //**存量可重复导入代码修改结束
            log.debug("1.新增客户信息" + billsPublic.getDepositorName() + "-" + customerId + "-" + billsPublic.getId());
            if (customerId != null && customerId > 0) {
                //6.2 处理账户信息
//                billsPublic.setCustomerId(customerId);
                billsPublic.setCustomerLogId(customerId);
                //可重复导入存量，注释掉accountId置空操作
                //billsPublic.setAccountId(null);
                //可重复导入存量，使用专用函数
                AccountPublicInfo acctPublicInfo = core2AmsDataSaveAccountPublic(billsPublic, userDto.getId());
                //AccountPublicInfo acctPublicInfo = saveAccountPublic(billsPublic, userDto.getId());
                log.debug("2.新增账户信息" + acctPublicInfo.getAccountId() + "-" + acctPublicInfo.getId());

                if (acctPublicInfo != null) {
                    //6.3 处理流水信息
                    billsPublic.setAccountId(acctPublicInfo.getAccountId());
                    //accountBillsAllInfo = saveBillsAll(billsPublic, userDto.getId());
                    //可重复导入存量专用流水表保存函数
                    accountBillsAllInfo = core2AmsDataSaveBillsAll(billsPublic, userDto.getId());

                    log.debug("3.新增初始化流水" + billsPublic.getBillNo() + "-" + accountBillsAllInfo.getId());
                }
            }
        } else {
            throw new RuntimeException("无效的单据类型！");
        }
        return accountBillsAllInfo.getId();
    }

    /**
     * 存量数据导入客户日志表覆盖专用函数
     *
     * @param custPubInfo
     * @return
     */
    private Long coreInitData2AmsSaveCustPublicLog(CustomerPublicInfo custPubInfo) {
        CustomerPublicLogInfo customerPublicLogInfo = new CustomerPublicLogInfo();
        String[] ignoreProperties = {"id"};
        //根据正式表数据，找到customersAll信息，结合后保存到日志表
        CustomersAllInfo customersAllInfo = customersAllService.findOne(custPubInfo.getCustomerId());
        BeanUtils.copyProperties(customersAllInfo, customerPublicLogInfo, ignoreProperties);
        String[] ignoreProperties1 = {"id", "depositorName"};
        BeanUtils.copyProperties(custPubInfo, customerPublicLogInfo, ignoreProperties1);
        //设置customerId
        customerPublicLogInfo.setCustomerId(custPubInfo.getCustomerId());
        CustomerPublicLogInfo hisCustomerPublicLogInfo = customerPublicLogService.getMaxSeq(customerPublicLogInfo.getCustomerId());
        Long sequence = 0L;
        if (hisCustomerPublicLogInfo != null) {
            //上一个日志表的id
            customerPublicLogInfo.setPreLogId(hisCustomerPublicLogInfo.getPreLogId());
            if (hisCustomerPublicLogInfo.getSequence() != null) {
                sequence = hisCustomerPublicLogInfo.getSequence();
            }
            customerPublicLogInfo.setId(hisCustomerPublicLogInfo.getId());
        }
        //序号，递增
        customerPublicLogInfo.setSequence(sequence);
        // 保存日志信息
        customerPublicLogService.save(customerPublicLogInfo);

        return customerPublicLogInfo.getId();
        //TODO 处理关联企业和股东信息
    }


    /**
     * 更新存量客户表的方法，用来支持存量可重复更新
     *
     * @param billsPublic
     * @param userId
     * @return
     */
    private Long updateCustPublic(AllBillsPublicDTO billsPublic, Long userId) {

        // 验证信息是否完整
        billsPublic.validate();
        Long customerId = null;
        // 1.1.2.1 设置基础表信息
        CustomersAllInfo customersAllInfo = new CustomersAllInfo();

        // 检查客户信息
        boolean isExist = false;

        CustomerPublicInfo customerPublicInfoOld = customerPublicService.getByCustomerNo(billsPublic.getCustomerNo());

        Long id1 = null;

        if (customerPublicInfoOld != null) {
            id1 = customerPublicInfoOld.getId();
            getCopyPropertiesNotNull(customerPublicInfoOld, customersAllInfo);
            getCopyPropertiesNotNull(billsPublic, customersAllInfo);
            customersAllInfo.setCreatedBy(userId + "");

        }
        CustomerPublicInfo customerPublicInfo = customerPublicInfoOld == null ? customerPublicService.getByDepositorName(billsPublic.getDepositorName()) : customerPublicInfoOld;
        if (customerPublicInfo == null) {
            customerPublicInfo = new CustomerPublicInfo();
        }
        getCopyPropertiesNotNull(billsPublic, customerPublicInfo);

        customerPublicInfo.setId(id1);

        if (customerPublicInfo != null && customerPublicInfo.getId() != null && customerPublicInfo.getId() > 0) {
            isExist = true;
            // 给客户Id赋值
            customersAllInfo.setId(customerPublicInfo.getCustomerId());

            //保存客户日志表
            customerId = coreInitData2AmsSaveCustPublicLog(customerPublicInfo);
        }

        // 保存主表信息
        /*CustomersAll customersAll = */
        customersAllService.save(customersAllInfo);
//      if (customersAll != null) {
        // 1.1.2.2 设置对公客户表信息
        CustomerPublicInfo custPublicInfo = customerPublicInfo;
        Long id = custPublicInfo.getId();

        getCopyPropertiesNotNull(billsPublic, custPublicInfo);
        custPublicInfo.setCreatedBy(userId + "");
        custPublicInfo.setCustomerId(customersAllInfo.getId());

        if (isExist) {
            custPublicInfo.setId(id);
        }
        // 保存对公表信息
        customerPublicService.save(custPublicInfo);

        // 1.1.2.2 处理股东信息
        if (billsPublic.getCompanyPartners() != null) {
            Set<CompanyPartnerInfo> companyPartner = billsPublic.getCompanyPartners();
            for (CompanyPartnerInfo cp : companyPartner) {
                cp.setCreatedBy(userId + "");
                cp.setCustomerId(custPublicInfo.getCustomerId());
                cp.setCustomerPublicId(custPublicInfo.getId());
                companyPartnerService.save(cp);
            }
        }
        // 1.1.2.3 处理关联企业信息
        if (billsPublic.getRelateCompanys() != null) {
            Set<RelateCompanyInfo> relateCompany = billsPublic.getRelateCompanys();
            for (RelateCompanyInfo rc : relateCompany) {
                rc.setCreatedBy(userId + "");
                rc.setCustomerId(custPublicInfo.getCustomerId());
                rc.setCustomerPublicId(custPublicInfo.getId());
                relateCompanyService.save(rc);
            }
        }

        if (!isExist) {
            customerId = coreInitData2AmsSaveCustPublicLog(custPublicInfo);
        }
        return customerId;
    }

    /**
     * 存量数据可重复导入改造专用函数
     *
     * @param billsPublic 流水信息,userId 操作人Id,isValidate是否对信息验证
     * @Descrpition 从流水保存账户信息
     */
    private AccountBillsAllInfo core2AmsDataSaveBillsAll(AllBillsPublicDTO billsPublic, Long userId) {
        // 验证信息是否完整
        billsPublic.validate();
        //数据中取消核准的标志位为空的时候查找该户是否取消核准
        if (billsPublic.getCancelHeZhun() == null) {
            //判断该机构是否走取消核准接口
            setCancelHeZhun(billsPublic);
        }
        // 判断流水信息
        if (billsPublic.getId() != null && billsPublic.getId() > 0) {
            // 更新流水操作
            AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getOne(billsPublic.getId());
            String[] ignoreProperties = {"id", "createdDate", "createdBy", "fromSource", "organFullId", "whiteList", "selectPwd", "openKey"};
            if (null != accountBillsAllInfo) {
                Long id = accountBillsAllInfo.getId();

                getCopyPropertiesNotNull(billsPublic, accountBillsAllInfo);
                log.info("存款人类别===="+billsPublic.getDepositorType());
                log.info("存款人类别====accountBillsAllInfo"+accountBillsAllInfo.getDepositorType());

                accountBillsAllInfo.setId(id);
            } else {
                accountBillsAllInfo = new AccountBillsAllInfo();
                BeanUtils.copyProperties(billsPublic, accountBillsAllInfo, ignoreProperties);
                log.info("存款人类别===="+billsPublic.getDepositorType());
                log.info("存款人类别====accountBillsAllInfo"+accountBillsAllInfo.getDepositorType());
            }


            // BeanUtils.copyProperties(billsPublic, accountBillsAllInfo, ignoreProperties);
            accountBillsAllInfo.setLastUpdateBy(userId + "");
            accountBillsAllInfo.setLastUpdateDate(new Date());

            //判断该机构是否走取消核准接口
            accountBillsAllInfo.setCancelHeZhun(billsPublic.getCancelHeZhun());
            accountBillsAllService.save(accountBillsAllInfo);
            return accountBillsAllInfo;
        } else {
            // 插入流水操作
            AccountBillsAllInfo accountBillsAllInfo = new AccountBillsAllInfo();
            BeanCopierUtils.copyProperties(billsPublic, accountBillsAllInfo);
            if (accountBillsAllInfo.getStatus() == null) {
                accountBillsAllInfo.setStatus(BillStatus.NEW);
            }
            accountBillsAllInfo.setId(billsPublic.getRefBillId());
            accountBillsAllInfo.setBillDate(DateFormatUtils.ISO_DATE_FORMAT.format(new Date()));
            accountBillsAllInfo.setCreatedBy(userId + "");
            accountBillsAllInfo.setCreatedDate(new Date());
            accountBillsAllInfo.setLastUpdateDate(new Date());

            //通过预约编号判断是否是预约端进来的账户（开户状态下的提交操作）
            if ("saveForm".equalsIgnoreCase(billsPublic.getAction())) {
                if (StringUtils.isNotBlank(billsPublic.getPreOpenAcctId()) && billsPublic.getBillType() == BillType.ACCT_OPEN) {
                    allBillsPublicService.sendModifyStatus(billsPublic, "BANK_PROCESSING");
                }
            }

            //判断该机构是否走取消核准接口
            accountBillsAllInfo.setCancelHeZhun(billsPublic.getCancelHeZhun());
            accountBillsAllService.save(accountBillsAllInfo);

            if (!isCheck) {   //无审核模式开户上报
                billsPublic.setId(accountBillsAllInfo.getId());
            }
            return accountBillsAllInfo;
        }

    }

    /**
     * 存量导入使用，省略空值复制
     *
     * @param source
     * @param target
     */
    public void getCopyPropertiesNotNull(Object source, Object target) {

        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));

    }


    /**
     * 获取对象属性中值为null的属性名称，存量重复导入特写
     *
     * @param source
     * @return
     */
    public String[] getNullPropertyNames(Object source) {


        final BeanWrapper src = new BeanWrapperImpl(source);

        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();

        for (PropertyDescriptor pd : pds) {

            Object srcValue = src.getPropertyValue(pd.getName());

            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }

        }

        String[] result = new String[emptyNames.size()];

        return emptyNames.toArray(result);


    }


    /**
     * @param billsPublic 流水信息,userId 操作人Id,isValidate是否对信息验证
     *                    存量数据导入可重复导入专用账户信息保存函数
     * @Descrpition 从流水保存账户信息
     */
    private AccountPublicInfo core2AmsDataSaveAccountPublic(AllBillsPublicDTO billsPublic, Long userId) {
        // 验证信息是否完整
        billsPublic.validate();
        //判断该机构是否走取消核准接口
        log.info("开始查询是否取消核准类上报账户......");
        OrganRegisterDto organRegisterDto = null;
        if (StringUtils.isNotBlank(billsPublic.getOrganCode())) {
            organRegisterDto = organRegisterService.queryByOrganCode(billsPublic.getOrganCode());
        } else {
            organRegisterDto = organRegisterService.query(billsPublic.getBankCode());
        }
        AccountConfigureDto accountConfigureDto = null;
        if (organRegisterDto != null) {
            accountConfigureDto = accountConfigureService.query(billsPublic.getAcctType().toString(), billsPublic.getDepositorType(), billsPublic.getBillType().toString());
            //dto保存，方便后面转allAcct时取值
            if (accountConfigureDto != null) {
                billsPublic.setCancelHeZhun(true);
            } else {
                billsPublic.setCancelHeZhun(false);
            }
        }

        // System.out.println("流水中的accoutId值为："+billsPublic.getAccountId());
        // 判断是更新还是新增
        if (billsPublic.getAccountId() != null && billsPublic.getAccountId() > 0) {
            // 更新操作
            // 1.更新accountAll
            String[] ignoreProperties = {"id", "createdDate", "createdBy"};
            AccountsAllInfo accountAllInfo = accountsAllService.getOne(billsPublic.getAccountId());
            if (null != accountAllInfo) {
                Long id = accountAllInfo.getId();
                Date createdDate = accountAllInfo.getCreatedDate();
                String createdBy = accountAllInfo.getCreatedBy();
                //使用此存量数据可重复导入专用copy函数，空值缺省保留原对象值
                getCopyPropertiesNotNull(billsPublic, accountAllInfo);
                //赋回不能更改值
                accountAllInfo.setId(id);
                accountAllInfo.setCreatedBy(createdBy);
                accountAllInfo.setCreatedDate(createdDate);
            } else {
                accountAllInfo = new AccountsAllInfo();
                BeanUtils.copyProperties(billsPublic, accountAllInfo, ignoreProperties);
            }

            //BeanUtils.copyProperties(billsPublic, accountAllInfo, ignoreProperties);
            // 设置账户Id
            accountAllInfo.setId(billsPublic.getAccountId());
            accountAllInfo.setOrganFullId(billsPublic.getAcctOrgFullid());
            accountAllInfo.setLastUpdateBy(userId + "");
            accountAllInfo.setLastUpdateDate(new Date());

            //判断该机构是否走取消核准接口
            if (accountConfigureDto != null) {
                accountAllInfo.setCancelHeZhun(true);
            } else {
                accountAllInfo.setCancelHeZhun(false);
            }
            // 保存主表
            accountsAllService.save(accountAllInfo);
            // 处理对公账户表
            AccountPublicInfo accountPublicInfo = accountPublicService.findByAccountId(accountAllInfo.getId());

            if (null != accountPublicInfo) {
                Long id = accountPublicInfo.getId();
                String createdBy = accountPublicInfo.getCreatedBy();
                Date createdDate = accountPublicInfo.getCreatedDate();
                //null缺省保留原值专用copy函数
                getCopyPropertiesNotNull(billsPublic, accountPublicInfo);
                //赋回不能更改值
                accountPublicInfo.setId(id);
                accountPublicInfo.setCreatedBy(createdBy);
                accountPublicInfo.setCreatedDate(createdDate);
            } else {
                accountPublicInfo = new AccountPublicInfo();
                BeanUtils.copyProperties(billsPublic, accountPublicInfo, ignoreProperties);
            }


            //BeanUtils.copyProperties(billsPublic, accountPublicInfo, ignoreProperties);
            accountPublicInfo.setLastUpdateBy(userId + "");
            accountPublicInfo.setLastUpdateDate(new Date());
            //将账户原有的大类设置进去
            AccountPublic acc = accountPublicDao.findByAccountId(accountAllInfo.getId());
            if (null != acc) {
                accountPublicInfo.setAcctBigType(acc.getAcctBigType());

            }
            // 保存
            accountPublicService.save(accountPublicInfo);

            //TODO 关联影像信息
            return accountPublicInfo;
        } else {
            // 新增操作
            // 1.新增accountAll
            String[] ignoreProperties = {"id", "createdDate", "createdBy"};
            AccountsAllInfo accountAllInfo = new AccountsAllInfo();
            BeanUtils.copyProperties(billsPublic, accountAllInfo, ignoreProperties);
            accountAllInfo.setCreatedBy(userId + "");
            accountAllInfo.setCreatedDate(new Date());
            //默认初始状态未激活
            if (billsPublic.getBillType() != BillType.ACCT_INIT) {
                if (billsPublic.getBillType() == BillType.ACCT_OPEN) {
                    accountAllInfo.setAccountStatus(AccountStatus.notActive);
                } else if (billsPublic.getBillType() == BillType.ACCT_CHANGE) {
                    accountAllInfo.setAccountStatus(AccountStatus.normal);
                } else if (billsPublic.getBillType() == BillType.ACCT_SUSPEND) {
                    accountAllInfo.setAccountStatus(AccountStatus.suspend);
                } else if (billsPublic.getBillType() == BillType.ACCT_REVOKE) {
                    accountAllInfo.setAccountStatus(AccountStatus.revoke);
                }
            }

            //判断该机构是否走取消核准接口
            if (accountConfigureDto != null) {
                accountAllInfo.setCancelHeZhun(true);
            } else {
                accountAllInfo.setCancelHeZhun(false);
            }
            // 保存主表
            accountsAllService.save(accountAllInfo);
            // 处理对公账户表
            AccountPublicInfo accountPublicInfo = new AccountPublicInfo();
            BeanUtils.copyProperties(billsPublic, accountPublicInfo, ignoreProperties);
            accountPublicInfo.setAccountId(accountAllInfo.getId());
            accountPublicInfo.setCreatedBy(userId + "");
            accountPublicInfo.setCreatedDate(new Date());
            // 保存
            accountPublicService.save(accountPublicInfo);

            //TODO 关联影像信息
            return accountPublicInfo;
        }
    }


    /**
     * 赋值默认值
     *
     * @param billsPublic
     */
    private void initBill(AllBillsPublicDTO billsPublic) {
        if (billsPublic != null) {
            billsPublic.setFinalStatus(CompanyIfType.No);
            if (billsPublic.getStatus() == null) {
                billsPublic.setStatus(BillStatus.NEW);
            }

            //取消核准判断前移
            //数据中取消核准的标志位为空的时候查找该户是否取消核准
            if(billsPublic.getCancelHeZhun() == null){
                setCancelHeZhun(billsPublic);
            }

            //人行审核状态
            billsPublic.setPbcCheckStatus(CompanyAmsCheckStatus.NoCheck);

            //核准类未同步
            if (billsPublic.getAcctType().isHeZhun() && (billsPublic.getBillType() == BillType.ACCT_OPEN || billsPublic.getBillType() == BillType.ACCT_CHANGE)) {
                //如果取消核准字段不是true的话就修改状态为待审核
                if(billsPublic.getCancelHeZhun() == null || (billsPublic.getCancelHeZhun() != null && !billsPublic.getCancelHeZhun())){
                    billsPublic.setPbcCheckStatus(CompanyAmsCheckStatus.WaitCheck);
                }
            }

            //默认未同步，后面需要修改实际状态（比如变更有些情况无需上报）
            if(billsPublic.getPbcSyncStatus() == null){
                billsPublic.setPbcSyncStatus(CompanySyncStatus.weiTongBu);
            }

            //核准类销户不上报
            if (billsPublic.getBillType() == BillType.ACCT_REVOKE && billsPublic.getAcctType().isHeZhun()) {
                //取消核准后   基本户，非临时可以进行销户操作
                if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun() && (billsPublic.getAcctType() == CompanyAcctType.jiben || billsPublic.getAcctType() == CompanyAcctType.feilinshi)){
                    billsPublic.setPbcSyncStatus(CompanySyncStatus.weiTongBu);
                }else{
                    billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
                }
            }

            billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
            log.info("信用代码证系统是否上报配置为：" + needSyncEccs);
            //信用代码只有开户和变更需要上报
            if (billsPublic.getAcctType() == CompanyAcctType.jiben && needSyncEccs &&  billsPublic.getBillType() == BillType.ACCT_CHANGE) {
                billsPublic.setEccsSyncStatus(CompanySyncStatus.weiTongBu);
            }
            //信用代码开户bu需要上报   20201016
            if (billsPublic.getAcctType() == CompanyAcctType.jiben && needSyncEccs && billsPublic.getBillType() == BillType.ACCT_OPEN ) {
                billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
            }

            //关于数据变更字段为非上报字段的处理   人行无需上报  无需审核
            if(billsPublic.getBillType() == BillType.ACCT_CHANGE){
                List<String> changeAmsSyncFieldList = getChangeFieldsNeedPbcSync(billsPublic);
                JSONObject jsonObject = JSON.parseObject(billsPublic.getChangeFields());
                if(jsonObject != null){
                    boolean pbcResult = CompareUtils.isSyncField(jsonObject.keySet(), changeAmsSyncFieldList);
                    boolean eccsResult = false;
                    if (billsPublic.getAcctType() == CompanyAcctType.jiben) {
                        eccsResult = CompareUtils.isSyncField(jsonObject.keySet(), getChangeFieldsNeedEccsSync(billsPublic));
                    }
                    if(!pbcResult){
                        billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
                    }
                    if(!eccsResult){
                        billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
                    }
                    if(!pbcResult && !eccsResult){
                        billsPublic.setPbcCheckStatus(CompanyAmsCheckStatus.NoCheck);
                    }
                }
            }

            // 业务情况不清楚，代码逻辑也不清楚，所以增加覆盖式操作。
            // 非临时机构临时存款账户 备案类（取消核准） 变更，有需要上报的字段时，设置PbcSyncStatus
            if(billsPublic.getCancelHeZhun()!=null && billsPublic.getCancelHeZhun() && billsPublic.getBillType()== BillType.ACCT_CHANGE && billsPublic.getAcctType() == CompanyAcctType.feilinshi){
                //(上报字段需求不明确)上报字段见单据TC-297
                String feilinshiAmsChangeSyncField[] = {"acctFileType", "acctFileNo","nontmpProjectName","nontmpLegalName","nontmpLegalIdcardType","nontmpLegalIdcardNo",
                        "nontmpZipcode","nontmpTelephone","nontmpAddress"};
                List<String> changeAmsSyncFieldList = new ArrayList<>(Arrays.asList(feilinshiAmsChangeSyncField));
                JSONObject jsonObject = JSON.parseObject(billsPublic.getChangeFields());
                if(jsonObject != null){
                    if(CompareUtils.isSyncField(jsonObject.keySet(), changeAmsSyncFieldList)){
                        billsPublic.setPbcSyncStatus(CompanySyncStatus.weiTongBu);
                        log.info("取消核准账户{}的{}流水，人行同步状态设置为weiTongBu",billsPublic.getAcctNo(),billsPublic.getId());
                    }
                }
                // 非临时机构临时存款账户 核准类 变更，有需要上报的字段时，设置PbcSyncStatus
            }else if (billsPublic.getCancelHeZhun()!=null && !billsPublic.getCancelHeZhun() && billsPublic.getBillType()== BillType.ACCT_CHANGE && billsPublic.getAcctType() == CompanyAcctType.feilinshi){
                //(上报字段需求不明确)上报字段见单据TC-297
                String feilinshiAmsChangeSyncField[] = {"acctCreateReason", "acctFileType","acctFileNo"};
                List<String> changeAmsSyncFieldList = new ArrayList<>(Arrays.asList(feilinshiAmsChangeSyncField));
                JSONObject jsonObject = JSON.parseObject(billsPublic.getChangeFields());
                if(jsonObject != null){
                    if(CompareUtils.isSyncField(jsonObject.keySet(), changeAmsSyncFieldList)){
                        billsPublic.setPbcSyncStatus(CompanySyncStatus.weiTongBu);
                        log.info("核准类账户{}的{}流水，人行同步状态设置为weiTongBu",billsPublic.getAcctNo(),billsPublic.getId());
                    }
                }
            }

            OrganizationDto organizationDto = organizationService.findByOrganFullId(billsPublic.getOrganFullId());
            if (organizationDto != null) {
                //白名单人行不审核
                WhiteListDto whiteListDto = whiteListService.getByEntnameAndOrgId(billsPublic.getDepositorName(), organizationDto.getId());
                //是白名单账户并且不是移除状态
                if (whiteListDto != null && !"delete".equals(whiteListDto.getStatus())) {
                    billsPublic.setPbcCheckStatus(CompanyAmsCheckStatus.NoCheck);
                    billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
                    billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
                    billsPublic.setWhiteList("1");
                }
            }

            //初始流水无需补录,后续处理初始流水时修改状态
            billsPublic.setInitFullStatus("0");
        }
    }

    /**
     * @param accountPublicInfo 对公账户信息
     * @author jogy.he
     * @Descrpition 从流水保存账户信息
     * 同一笔账户连接之前的序号
     */
    private void saveAccountLog(AccountPublicInfo accountPublicInfo) {
        if (accountPublicInfo != null) {
            AccountPublicLogInfo accountPublicLogInfo = new AccountPublicLogInfo();
            String[] ignoreProperties = {"id"};
            BeanUtils.copyProperties(accountPublicInfo, accountPublicLogInfo, ignoreProperties);
            AccountsAllInfo accountsAllInfo = accountsAllService.getOne(accountPublicInfo.getAccountId());
            if (accountsAllInfo != null) {
                BeanUtils.copyProperties(accountsAllInfo, accountPublicLogInfo, ignoreProperties);
                accountPublicLogInfo.setAccountId(accountsAllInfo.getId());
                // 获取之前日志记录id和最大seq
                AccountPublicLogInfo maxSeqInfo = accountPublicLogService.getMaxSeq(accountPublicLogInfo.getAccountId());
                Long sequence = 0L;
                if (maxSeqInfo != null) {
                    accountPublicLogInfo.setPreLogId(maxSeqInfo.getId());
                    if (maxSeqInfo.getSequence() != null) {
                        sequence = maxSeqInfo.getSequence();
                    }
                }
                accountPublicLogInfo.setSequence(sequence + 1);
                // 保存日志信息
                accountPublicLogService.save(accountPublicLogInfo);
            } else {
                throw new RuntimeException("账户主要信息不可以为空！");
            }
        } else {
            throw new RuntimeException("账户信息不可以为空！");
        }
    }


    /**
     * @param billsPublic 流水信息,userId 操作人Id,isValidate是否对信息验证
     * @author jogy.he
     * @Descrpition 从流水保存账户信息
     */
    private AccountBillsAllInfo saveBillsAll(AllBillsPublicDTO billsPublic, Long userId) {
        // 验证信息是否完整
        billsPublic.validate();
        //判断该机构是否走取消核准接口
        setCancelHeZhun(billsPublic);
        // 判断流水信息
        if (billsPublic.getId() != null && billsPublic.getId() > 0) {
            // 更新流水操作
            AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getOne(billsPublic.getId());
            String accountKey = accountBillsAllInfo.getAccountKey();
            String[] ignoreProperties = {"id", "createdDate", "createdBy", "fromSource", "organFullId","whiteList","selectPwd","openKey"};
            BeanUtils.copyProperties(billsPublic, accountBillsAllInfo, ignoreProperties);
            accountBillsAllInfo.setLastUpdateBy(userId + "");
            accountBillsAllInfo.setLastUpdateDate(new Date());
            if(billsPublic.getAcctType()==CompanyAcctType.jiben){
                //如果是补录方式  手工录入的许可证可编辑保存   其他按照原有逻辑进行保存
                if(StringUtils.isEmpty(billsPublic.getAction()) || "addInfoForm".equals(billsPublic.getAction())){
                    log.info("接口(按钮操作类型为空)或待补录页面补录基本户开户许可证，原开户许可证为{}，补录开户许可证为{}",accountKey,billsPublic.getAccountKey());
                }else{
                    //基本户的accountKey不会变
                    accountBillsAllInfo.setAccountKey(accountKey);
                }
            }
            //判断该机构是否走取消核准接口
            accountBillsAllInfo.setCancelHeZhun(billsPublic.getCancelHeZhun());
            accountBillsAllService.save(accountBillsAllInfo);
            return accountBillsAllInfo;
        } else {
            // 插入流水操作
            AccountBillsAllInfo accountBillsAllInfo = new AccountBillsAllInfo();
            BeanCopierUtils.copyProperties(billsPublic, accountBillsAllInfo);
            if (accountBillsAllInfo.getStatus() == null) {
                accountBillsAllInfo.setStatus(BillStatus.NEW);
            }
            accountBillsAllInfo.setId(billsPublic.getRefBillId());
            accountBillsAllInfo.setBillDate(DateFormatUtils.ISO_DATE_FORMAT.format(new Date()));
            accountBillsAllInfo.setCreatedBy(userId + "");
            accountBillsAllInfo.setCreatedDate(new Date());
            accountBillsAllInfo.setLastUpdateDate(new Date());

            //通过预约编号判断是否是预约端进来的账户（开户状态下的提交操作）
            if ("saveForm".equalsIgnoreCase(billsPublic.getAction())) {
                if (StringUtils.isNotBlank(billsPublic.getPreOpenAcctId()) && billsPublic.getBillType() == BillType.ACCT_OPEN && !applyNewRuleFlag) {
                    allBillsPublicService.sendModifyStatus(billsPublic, "BANK_PROCESSING");
                }
            }

            //判断该机构是否走取消核准接口
            accountBillsAllInfo.setCancelHeZhun(billsPublic.getCancelHeZhun());
            accountBillsAllService.save(accountBillsAllInfo);

            if (!isCheck) {   //无审核模式开户上报
                billsPublic.setId(accountBillsAllInfo.getId());
            }
            return accountBillsAllInfo;
        }

    }

    /**
     * @param billsPublic 流水信息,userId 操作人Id,isValidate是否对信息验证
     * @author jogy.he
     * @Descrpition 从流水保存账户信息
     */
    private AccountPublicInfo saveAccountPublic(AllBillsPublicDTO billsPublic, Long userId) {
        // 验证信息是否完整
        billsPublic.validate();
        //判断该机构是否走取消核准接口
        log.info("开始查询是否取消核准类上报账户......");
        setCancelHeZhun(billsPublic);
        // 判断是更新还是新增
        if (billsPublic.getAccountId() != null && billsPublic.getAccountId() > 0) {
            // 更新操作
            // 1.更新accountAll
            String[] ignoreProperties = {"id", "createdDate", "createdBy", "selectPwd"};
            AccountsAllInfo accountAllInfo = accountsAllService.getOne(billsPublic.getAccountId());
            AccountStatus accountStatus = accountAllInfo.getAccountStatus();
            String openkey = accountAllInfo.getOpenKey();
            String accountKey = accountAllInfo.getAccountKey();
            BeanUtils.copyProperties(billsPublic, accountAllInfo, ignoreProperties);
            // 设置账户Id
            accountAllInfo.setId(billsPublic.getAccountId());
            accountAllInfo.setOrganFullId(billsPublic.getAcctOrgFullid());
            accountAllInfo.setLastUpdateBy(userId + "");
            accountAllInfo.setLastUpdateDate(new Date());
            //先销户后开户的情况下   账户状态修改为流水数据中的状态
            if(billsPublic.getAccountStatus() != accountStatus){
                accountAllInfo.setAccountStatus(billsPublic.getAccountStatus());
            }else{
                accountAllInfo.setAccountStatus(accountStatus);
            }
            //判断该机构是否走取消核准接口
            accountAllInfo.setCancelHeZhun(billsPublic.getCancelHeZhun());
            if(billsPublic.getAcctType()==CompanyAcctType.jiben){
                if(StringUtils.isNotBlank(billsPublic.getAccountKey()) && !StringUtils.equals(billsPublic.getAccountKey(),accountKey)){
                    //当最新dto进来的数据accountKey有值并且跟原始值不相等的情况下用最新的
                    accountAllInfo.setAccountKey(billsPublic.getAccountKey());
                }else{
                    accountAllInfo.setAccountKey(accountKey);
                }

                if(StringUtils.isNotBlank(billsPublic.getOpenKey()) && !StringUtils.equals(billsPublic.getOpenKey(),openkey)){
                    //当最新dto进来的数据openKey有值并且跟原始值不相等的情况下用最新的
                    accountAllInfo.setOpenKey(billsPublic.getOpenKey());
                }else{
                    accountAllInfo.setOpenKey(openkey);
                }
            }
            // 保存主表
            accountsAllService.save(accountAllInfo);
            // 处理对公账户表
            AccountPublicInfo accountPublicInfo = accountPublicService.findByAccountId(accountAllInfo.getId());
            BeanUtils.copyProperties(billsPublic, accountPublicInfo, ignoreProperties);
            accountPublicInfo.setLastUpdateBy(userId + "");
            accountPublicInfo.setLastUpdateDate(new Date());
            //将账户原有的大类设置进去
            AccountPublic acc = accountPublicDao.findByAccountId(accountAllInfo.getId());
            accountPublicInfo.setAcctBigType(acc.getAcctBigType());
            // 保存
            accountPublicService.save(accountPublicInfo);
            //保存尽调关联信息
            if(writeMoney){
                ProofReportDto result= proofReportService.findByAcctNoAndType(accountAllInfo.getAcctNo(),ProofType.KYC);
                if(result==null) {
                    result = new ProofReportDto();
                    List<KycSearchHistoryDto> list = kycSearchHistoryService.findByEntName(accountAllInfo.getAcctName());
                    if (CollectionUtils.isNotEmpty(list)) {
                        KycSearchHistoryDto kycSearchHistoryDto = list.get(0);
                        OrganizationDto organizationDto = organizationService.findByOrganFullId(kycSearchHistoryDto.getOrgfullid());
                        result.setKycFlag(CompanyIfType.Yes);
                        result.setUsername(kycSearchHistoryDto.getUsername());
                        result.setDateTime(kycSearchHistoryDto.getQuerydate());
                        result.setProofBankName(organizationDto.getName());
                    } else {
                        result.setKycFlag(CompanyIfType.No);
                    }
                    result.setType(ProofType.KYC);
                    result.setAcctNo(accountAllInfo.getAcctNo());
                    result.setAcctName(accountAllInfo.getAcctName());
                    result.setAcctType(com.ideatech.ams.system.proof.enums.CompanyAcctType.valueOf(accountAllInfo.getAcctType().name()));
                    result.setOrganFullId(accountAllInfo.getOrganFullId());
                    result.setOpenBankName(accountAllInfo.getBankName());
                    proofReportService.save(result);
                }
            }
            //TODO 关联影像信息
            return accountPublicInfo;
        } else {
            // 新增操作
            // 1.新增accountAll
            String[] ignoreProperties = {"id", "createdDate", "createdBy"};
            AccountsAllInfo accountAllInfo = new AccountsAllInfo();
            BeanUtils.copyProperties(billsPublic, accountAllInfo, ignoreProperties);
            accountAllInfo.setCreatedBy(userId + "");
            accountAllInfo.setCreatedDate(new Date());
            //默认初始状态未激活
            if (billsPublic.getBillType() != BillType.ACCT_INIT) {
                if (billsPublic.getBillType() == BillType.ACCT_OPEN) {
                    accountAllInfo.setAccountStatus(AccountStatus.notActive);
                } else if (billsPublic.getBillType() == BillType.ACCT_CHANGE) {
                    accountAllInfo.setAccountStatus(AccountStatus.normal);
                } else if (billsPublic.getBillType() == BillType.ACCT_SUSPEND) {
                    accountAllInfo.setAccountStatus(AccountStatus.suspend);
                } else if (billsPublic.getBillType() == BillType.ACCT_REVOKE) {
                    accountAllInfo.setAccountStatus(AccountStatus.revoke);
                }
            }

            //判断该机构是否走取消核准接口
            accountAllInfo.setCancelHeZhun(billsPublic.getCancelHeZhun());
            // 保存主表
            accountsAllService.save(accountAllInfo);
            // 处理对公账户表
            AccountPublicInfo accountPublicInfo = new AccountPublicInfo();
            BeanUtils.copyProperties(billsPublic, accountPublicInfo, ignoreProperties);
            accountPublicInfo.setAccountId(accountAllInfo.getId());
            accountPublicInfo.setCreatedBy(userId + "");
            accountPublicInfo.setCreatedDate(new Date());
            // 保存
            accountPublicService.save(accountPublicInfo);

            //保存尽调关联信息
            if(writeMoney){
                ProofReportDto result= proofReportService.findByAcctNoAndType(accountAllInfo.getAcctNo(),ProofType.KYC);
                if(result==null){
                    result = new ProofReportDto();
                    List<KycSearchHistoryDto> list =kycSearchHistoryService.findByEntName(accountAllInfo.getAcctName());
                    if(CollectionUtils.isNotEmpty(list)){
                        KycSearchHistoryDto kycSearchHistoryDto = list.get(0);
                        OrganizationDto organizationDto = organizationService.findByOrganFullId(kycSearchHistoryDto.getOrgfullid());
                        result.setKycFlag(CompanyIfType.Yes);
                        result.setProofBankName(organizationDto.getName());
                        result.setUsername(kycSearchHistoryDto.getUsername());
                        result.setDateTime(kycSearchHistoryDto.getQuerydate());
                    }else{
                        result.setKycFlag(CompanyIfType.No);
                    }
                    result.setType(ProofType.KYC);
                    result.setAcctNo(accountAllInfo.getAcctNo());
                    result.setAcctName(accountAllInfo.getAcctName());
                    result.setAcctType(com.ideatech.ams.system.proof.enums.CompanyAcctType.valueOf(accountAllInfo.getAcctType().name()));
                    result.setOrganFullId(accountAllInfo.getOrganFullId());
                    result.setOpenBankName(accountAllInfo.getBankName());
                    proofReportService.save(result);
                }
            }
            //TODO 关联影像信息
            return accountPublicInfo;
        }
    }

    private CustomerPublicMidInfo saveCustPublicMid(AllBillsPublicDTO billsPublic, Long userId) {

        // 验证信息是否完整
        billsPublic.validate();

        // 判断中间表记录是否存在，如果存在则表示有在流程中的数据，需要同步
        if (billsPublic.getMidId() != null && billsPublic.getMidId() > 0) {
            // 编辑更新中间表记录
            CustomerPublicMidInfo custPubMidInfo = customerPublicMidService.getOne(billsPublic.getMidId());
            // 同步中间表信息
            String[] ignoreProperties = {"id", "createdBy", "createdDate", "customerId", "refBillId","customerClass"};
            BeanUtils.copyProperties(billsPublic, custPubMidInfo, ignoreProperties);
            custPubMidInfo.setId(billsPublic.getMidId());
            custPubMidInfo.setLastUpdateBy(userId + "");
            custPubMidInfo.setLastUpdateDate(new Date());

            // 保存修改中间表信息
            customerPublicMidService.save(custPubMidInfo);

            //判断股东信息与关联企业
            if (billsPublic.getCompanyPartners() != null) {
                //先删除，后保存股东中间表信息
                companyPartnerMidService.deleteByCustomerPublicMidId(custPubMidInfo.getId());
                List<CompanyPartnerMidInfo> companyPartnerMidInfoList = ConverterService.convertToList(billsPublic.getCompanyPartners(), CompanyPartnerMidInfo.class);
                for (CompanyPartnerMidInfo c : companyPartnerMidInfoList) {
                    c.setCustomerPublicMidId(custPubMidInfo.getId());
                    c.setCustomerId(custPubMidInfo.getCustomerId());
                }
                companyPartnerMidService.save(companyPartnerMidInfoList);
            }

            if (billsPublic.getRelateCompanys() != null) {
                relateCompanyMidService.deleteByMidId(custPubMidInfo.getId());
                List<RelateCompanyMidInfo> relateCompanyMidInfos = ConverterService.convertToList(billsPublic.getRelateCompanys(), RelateCompanyMidInfo.class);
                for (RelateCompanyMidInfo c : relateCompanyMidInfos) {
                    c.setCustomerPublicMidId(custPubMidInfo.getId());
                    c.setCustomerId(custPubMidInfo.getCustomerId());
                }
                relateCompanyMidService.save(relateCompanyMidInfos);
            }
            return custPubMidInfo;
        } else {
            //由于每个账户都有自己的customerLog，此处只要不存在logId都需要创建新的log记录，所以不判断客户表是否有数据
            // 新增记录，此处需要考虑是否是审核的提交
            boolean isExist = false;

            CustomerPublicMidInfo custPubMidInfo = new CustomerPublicMidInfo();
            BeanCopierUtils.copyProperties(billsPublic, custPubMidInfo);

            // 验证客户LOG是否已存在
            if (billsPublic.getCustomerLogId() != null && billsPublic.getCustomerLogId() > 0) {
                isExist = true;
            } else {
                if (billsPublic.getId() != null) {
                    //查询MID表有无该流水数据
                    CustomerPublicMidInfo midInfo = customerPublicMidService.getByBillId(billsPublic.getId());
                    if (midInfo != null) {
                        isExist = true;
                        custPubMidInfo.setId(midInfo.getId());
                        custPubMidInfo.setCustomerId(midInfo.getCustomerId());
                        custPubMidInfo.setCustomerLogId(midInfo.getCustomerLogId());
                    }
                }
            }
            // 客户信息不存在时、提前创建id,customerId和中间表id一致
            if (!isExist) {
                Long id = getRecId();
                custPubMidInfo.setId(id);
                custPubMidInfo.setCustomerId(id);
                custPubMidInfo.setCustomerLogId(id);
            }
            custPubMidInfo.setCreatedBy(userId + "");
            custPubMidInfo.setCreatedDate(new Date());
            custPubMidInfo.setCustomerClass(CustomerType.CORPORATE);

            // 保存中间表信息
            customerPublicMidService.save(custPubMidInfo);

            // 判断股东信息与关联企业
            if (billsPublic.getCompanyPartners() != null) {
                //先删除，后保存股东中间表信息
                companyPartnerMidService.deleteByCustomerPublicMidId(custPubMidInfo.getId());
                List<CompanyPartnerMidInfo> companyPartnerMidInfoList = ConverterService.convertToList(billsPublic.getCompanyPartners(), CompanyPartnerMidInfo.class);
                for (CompanyPartnerMidInfo c:companyPartnerMidInfoList){
                    c.setCustomerPublicMidId(custPubMidInfo.getId());
                    c.setCustomerId(custPubMidInfo.getCustomerId());
                }
                companyPartnerMidService.save(companyPartnerMidInfoList);
            }

            if (billsPublic.getRelateCompanys() != null) {
                relateCompanyMidService.deleteByMidId(custPubMidInfo.getId());
                List<RelateCompanyMidInfo> relateCompanyMidInfos = ConverterService.convertToList(billsPublic.getRelateCompanys(), RelateCompanyMidInfo.class);
                for (RelateCompanyMidInfo c : relateCompanyMidInfos) {
                    c.setCustomerPublicMidId(custPubMidInfo.getId());
                    c.setCustomerId(custPubMidInfo.getCustomerId());
                }
                relateCompanyMidService.save(relateCompanyMidInfos);
            }
            return custPubMidInfo;
        }
    }


    /**
     * @param billsPublic 流水信息表,userId 操作人Id,isValidate是否对信息验证
     * @author jogy.he
     * @Descrpition 从流水新增正式CustomerPublic表客户信息
     */
    private Long insertCustPublic(AllBillsPublicDTO billsPublic, Long userId) {
        /* try { */
        // 验证信息是否完整
        billsPublic.validate();
        Long customerId = null;

        // 1.1.2.1 设置基础表信息
        CustomersAllInfo customersAllInfo = new CustomersAllInfo();
        BeanUtils.copyProperties(billsPublic, customersAllInfo);
        customersAllInfo.setCreatedBy(userId + "");

//        CustomersAllInfo customersAllInfoOld = customersAllService.findByCustomerNo(billsPublic.getCustomerNo());
//
//        CustomersAllInfo customersAllInfoNew = customersAllInfoOld == null ? customersAllService.findByDepositorName(billsPublic.getDepositorName()) : customersAllInfoOld;
//
//        if(customersAllInfoNew != null){
//            customersAllInfo.setCustomerNo(customersAllInfoNew.getCustomerNo());
//            customersAllInfo.setDepositorName(customersAllInfoNew.getDepositorName());
//            customersAllInfo.setId(customersAllInfoNew.getId());
//        }

        // 检查客户信息
        boolean isExist = false;

        CustomerPublicInfo customerPublicInfoOld = customerPublicService.getByCustomerNo(billsPublic.getCustomerNo());

        CustomerPublicInfo customerPublicInfo = customerPublicInfoOld == null ? customerPublicService.getByDepositorName(billsPublic.getDepositorName()) : customerPublicInfoOld;

        if (customerPublicInfo != null && customerPublicInfo.getId() != null && customerPublicInfo.getId() > 0) {
            isExist = true;
            // 给客户Id赋值
            customersAllInfo.setId(customerPublicInfo.getCustomerId());

            //保存客户日志表
            customerId = saveCustPublicLog(customerPublicInfo);
        }

        // 保存主表信息
        /*CustomersAll customersAll = */
        customersAllService.save(customersAllInfo);
//      if (customersAll != null) {
        // 1.1.2.2 设置对公客户表信息
        CustomerPublicInfo custPublicInfo = new CustomerPublicInfo();
        BeanUtils.copyProperties(billsPublic, custPublicInfo);
        custPublicInfo.setCreatedBy(userId + "");
        custPublicInfo.setCustomerId(customersAllInfo.getId());
        if(custPublicInfo.getId() == null){
            custPublicInfo.setId(customersAllInfo.getId());
        }

        if (isExist) {
            custPublicInfo.setId(customerPublicInfo.getId());
        }
        // 保存对公表信息
        customerPublicService.save(custPublicInfo);

        // 1.1.2.2 处理股东信息
        if (billsPublic.getCompanyPartners() != null) {
            Set<CompanyPartnerInfo> companyPartner = billsPublic.getCompanyPartners();
            for (CompanyPartnerInfo cp : companyPartner) {
                cp.setCreatedBy(userId + "");
                cp.setCustomerId(custPublicInfo.getCustomerId());
                cp.setCustomerPublicId(custPublicInfo.getId());
                companyPartnerService.save(cp);
            }
        }
        // 1.1.2.3 处理关联企业信息
        if (billsPublic.getRelateCompanys() != null) {
            Set<RelateCompanyInfo> relateCompany = billsPublic.getRelateCompanys();
            for (RelateCompanyInfo rc : relateCompany) {
                rc.setCreatedBy(userId + "");
                rc.setCustomerId(custPublicInfo.getCustomerId());
                rc.setCustomerPublicId(custPublicInfo.getId());
                relateCompanyService.save(rc);
            }
        }

        if (!isExist) {
            customerId = saveCustPublicLog(custPublicInfo);
        }

        return customerId;

//        customerId = custPublicInfo.getCustomerId();
//        customerId = custPublicInfo.getId();
//      }
//      return customerId;
        /*
         * } catch (RuntimeException e) { e.printStackTrace(); return null; }
         */
    }

    @Override
    public Long getRecId() {
        return idWorker.nextId();
    }

    private void validateUnfinishedBill(AllBillsPublicDTO billsPublic) {
        if (StringUtils.isNotBlank(billsPublic.getAcctNo()) && accountBillsAllService.countUnfinishedByAcctNo(billsPublic.getAcctNo()) > 0) {
            throw new BizServiceException(EErrorCode.BILL_UN_FINISHED, "该账户存在未完成的操作流水！");
        }
        if (customerBillsAllService.countUnfinishedByCustomerNo(billsPublic.getCustomerNo()) > 0) {
            throw new BizServiceException(EErrorCode.BILL_UN_FINISHED, "该客户存在未完成的操作流水！");
        }
    }

    @Override
    public AllAcct allBillsPublic2AllAcct(AllBillsPublicDTO dto) {
        AllAcct allacct = new AllAcct();
        convert(dto, allacct);
        return allacct;
    }

    @Override
    public AllAcct allBillsPublic2AllAcctPbc(AllBillsPublicDTO dto) throws Exception{
        //2018年11月13日 添加变更字段的处理，在dto中去除非变更字段（但保留必要字段）
        AllAcct allacct = new AllAcct();
        convert(changeConvertPbcSyncField(dto), allacct);
        return allacct;
    }

    /**
     * 变更时，在转换人行上报对象时去除非变更字段
     * 在copy时忽略[所有字段-上报字段-必填字段]
     *
     * @param dto
     * @return
     */
    private AllBillsPublicDTO changeConvertPbcSyncField(AllBillsPublicDTO dto) throws Exception{
        List<String> ignoreFields = new ArrayList<>(16);
        //变更时是否走取消核准接口
        boolean cancelHeZhun = false;
        if(dto.getCancelHeZhun()!=null && dto.getCancelHeZhun()){
            cancelHeZhun =dto.getCancelHeZhun();
        }

        List<String> changeFields = accountChangeSummaryService.findAccountChangeFields(dto.getId());

        if(CollectionUtils.isNotEmpty(changeFields)){
            log.info("变更字段打印：" + changeFields);
        }
        //备案制(取消核准后增加基本户（4种账户性质）非临时)人行字段覆盖本地字段--排除变更字段的赋值
        if(dto.getBillType() == BillType.ACCT_CHANGE){
            if((dto.getAcctType() == CompanyAcctType.yiban || dto.getAcctType() == CompanyAcctType.feiyusuan) || (dto.getCancelHeZhun() != null && dto.getCancelHeZhun())){
                pbc2ConverDto(dto,changeFields);
                log.info("备案类变更dto转allAcct对象开始===========================");
                PrintUtils.printObjectColumn(dto);
                log.info("备案类变更dto转allAcct对象结束===========================");
            }
        }
        //核准类的变更可以之传变更字段，非核准类变更还是需要传所有字段
        if (dto.getBillType() == BillType.ACCT_CHANGE && dto.getAcctType().isHeZhun() && !cancelHeZhun) {
            List<String> mustFields = getChangeFieldMustPbcSync(dto);

            List<String> needSyncFields = getChangeFieldsNeedPbcSync(dto);

            if(dto.getAcctType() == CompanyAcctType.yusuan){
                //当变更字段中包含有证明文件2种类或证明文件2编号的变更  在上报人行是同时进行上报
                if(changeFields.contains("acctFileType2") || changeFields.contains("acctFileNo2")){
                    mustFields.add("acctFileType2");
                    mustFields.add("acctFileNo2");
                }
            }

            //变更记录表不为空，取到两者交集
            if (CollectionUtils.isNotEmpty(changeFields)) {
                needSyncFields.retainAll(changeFields);
            }

            try {
                ignoreFields = new ArrayList<>(org.apache.commons.beanutils.BeanUtils.describe(dto).keySet());
                ignoreFields.removeAll(mustFields);
                ignoreFields.removeAll(needSyncFields);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        AllBillsPublicDTO syncDto = new AllBillsPublicDTO();
        BeanUtils.copyProperties(dto, syncDto, ignoreFields.toArray(new String[ignoreFields.size()]));
        syncDto.setCancelHeZhun(cancelHeZhun);
        return syncDto;
    }

    @Override
    public List<PbcSyncListDto> checkPbcSync() {
        List<PbcSyncListDto> pbcSyncDtoList = new ArrayList<>(16);
        List<PbcSyncList> pbcSyncList = pbcSyncListDao.findBySyncStatus(CompanyIfType.No);

        List<PbcSyncList> waitPushSyncList = pbcSyncListDao.findBySyncStatusAndIsPush(CompanyIfType.Yes, CompanyIfType.No);
        for (PbcSyncList syncList : waitPushSyncList) {
            PbcSyncListDto pbcSyncListDto = new PbcSyncListDto();
            pbcSyncListDto.setAcctNo(syncList.getAcctNo());
            pbcSyncListDto.setAccountKey(syncList.getAccountKey());
            pbcSyncListDto.setAcctAccountKey(syncList.getAcctAccountKey());
            pbcSyncDtoList.add(pbcSyncListDto);
        }

        for (PbcSyncList syncList : pbcSyncList) {
            PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullId(syncList.getOrganFullId(), EAccountType.AMS);
            if (pbcAccountDto != null) {
                try {
                    AmsAccountInfo amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(pbcAccountDto, syncList.getAcctNo());
                    if (amsAccountInfo != null) {
                        PbcSyncListDto pbcSyncListDto = new PbcSyncListDto();
                        pbcSyncListDto.setAcctNo(syncList.getAcctNo());
                        pbcSyncListDto.setAccountKey(amsAccountInfo.getAccountKey());
                        pbcSyncListDto.setAcctAccountKey(amsAccountInfo.getAccountLicenseNo());
                        pbcSyncDtoList.add(pbcSyncListDto);

                        syncList.setSyncStatus(CompanyIfType.Yes);
                        syncList.setAccountKey(amsAccountInfo.getAccountKey());
                        syncList.setAcctAccountKey(amsAccountInfo.getAccountLicenseNo());
                        pbcSyncListDao.save(syncList);
                    }
                } catch (Exception e) {
                    log.info("根据账号查询人行异常");
                }
            }
        }
        return pbcSyncDtoList;
    }

    @Override
    public PbcUserAccount systemPbcUser2PbcUser(PbcAccountDto pbcAccountDto) {
        if (pbcAccountDto == null) {
            return null;
        }
        PbcUserAccount pbcUserAccount = new PbcUserAccount();
        pbcUserAccount.setLoginIp(pbcAccountDto.getIp());
        pbcUserAccount.setLoginUserName(pbcAccountDto.getAccount());
        pbcUserAccount.setLoginPassWord(pbcAccountDto.getPassword());
        return pbcUserAccount;
    }

    @Override
    public void toValidate(Map<String, String> formData) throws Exception {
        try {
            AllBillsPublicDTO dataAccount = (AllBillsPublicDTO) Map2DomainUtils.converter(formData, AllBillsPublicDTO.class);

            if (StringUtils.equals(formData.get("action"), "verifyForm")) { //审核上报前账号校验
                if (StringUtils.isBlank(dataAccount.getAcctNo())) {
                    String acctNo = acctNoGeneService.getAcctNo(dataAccount);  //获得账号的第三方接口
                    if (StringUtils.isNotBlank(acctNo)) {
                        formData.put("acctNo", acctNo);
                    } else {  //账号不存在时抛异常
                        throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST, "账号不存在");
                    }
                }
            }
            String key = dataAccount.takeValidateName();
            if(dataAccount.getBillType()!=null && dataAccount.getBillType()==BillType.ACCT_CLOSESUSPEND){
                key = "amsCloseSuspendAllAccountValidate";
            }
            AllPublicAccountValidate allPublicAccountValidate = validateMap.get(key);
            allPublicAccountValidate.validate(dataAccount);
        } catch (Exception e) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, e.getMessage());
        }
    }

    /**
     * @param id 流水的id工商注册类型
     * @return
     * @author jogy.he
     * @description 更新流水的最终状态
     */
    @Override
    public int updateFinalStatus(Long id) {
        int count = accountBillsAllService.updateFinalStatus(id);
        return count;
    }

    /**
     * 1.更新流失finalStatus状态
     * 2.处理客户信息：（开变户：将正式表保存到日志表-->中间表保存到正式表-->删除中间表信息） （销户久悬：将中间表保存到日志表-->删除中间表信息）
     * 3.更新账户状态（销户和久悬更新状态、时间及说明）
     *
     * @param billsPublic
     * @param userId
     */
    @Override
    public void updateFinalStatus(AllBillsPublicDTO billsPublic, Long userId) throws Exception {
        if (billsPublic.getId() != null) {

            // 1.更新流水finalStatus
            accountBillsAllService.updateFinalStatus(billsPublic.getId());
            log.info("1.更新流水" + billsPublic.getBillNo() + "finalStatus='Yes'");

            // 2.根据操作类型处理客户信息和账户状态
            if (billsPublic.getBillType() == BillType.ACCT_OPEN || billsPublic.getBillType() == BillType.ACCT_CHANGE) {
                //核准类更新人行核准状态(排除无需同步的情况)
                if (billsPublic.getAcctType().isHeZhun() && billsPublic.getPbcSyncStatus() == CompanySyncStatus.tongBuChengGong) {
                    //取消核准人行审核状态为无需审核
                    if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()){
                        accountBillsAllService.updatePbcCheckStatus(billsPublic.getId(), CompanyAmsCheckStatus.NoCheck);
                    }else{
                        accountBillsAllService.updatePbcCheckStatus(billsPublic.getId(), CompanyAmsCheckStatus.CheckPass);
                    }
                    //人行核准状态(增加无需同步的情况：不同步人行的就不用审核)
                }else if(billsPublic.getPbcSyncStatus() == CompanySyncStatus.buTongBu){
                    accountBillsAllService.updatePbcCheckStatus(billsPublic.getId(), CompanyAmsCheckStatus.NoCheck);
                }
                // 2.1 处理客户中间表，将正式表保存到日志表-->中间表存到日志表(更改账户关联的客户信息<日志表>)-->中间表保存到正式表-->删除中间表信息
                CustomerPublicInfo custPubInfo = null;
                //拿到正式表数据
                try {
                    if (StringUtils.isNotBlank(billsPublic.getCustomerNo())) {
                        custPubInfo = customerPublicService.getByCustomerNo(billsPublic.getCustomerNo());
                    }
                    if (custPubInfo == null) {
                        if (StringUtils.isNotBlank(billsPublic.getDepositorName())) {
                            custPubInfo = customerPublicService.getByDepositorName(billsPublic.getDepositorName());
                        }
                    }
                }catch (Exception e){
                    log.error("更新流水状态失败，客户数据异常:{}",e);
                    throw new BizServiceException(EErrorCode.BILL_UPDATE_FINALSTATUS_ERROR, "更新流水状态失败，客户数据异常");
                }

                //新增流水可能为空
//                if (custPubInfo == null) {
//                    throw new BizServiceException(EErrorCode.BILL_UPDATE_FINALSTATUS_ERROR, "更新最终状态时客户号或企业名称不能为空！");
//                }
                CustomerPublicMidInfo custPubMidInfo = null;
                //根据MidId去客户mid表中查询，如果没有则根据mid表里的refBillId查询
                if (billsPublic.getMidId() != null) {
                    custPubMidInfo = customerPublicMidService.getOne(billsPublic.getMidId());
                }
                if (custPubMidInfo == null) {
                    custPubMidInfo = customerPublicMidService.getByBillId(billsPublic.getId());
                }
                if (custPubMidInfo == null) {
                    throw new BizServiceException(EErrorCode.BILL_UPDATE_FINALSTATUS_ERROR, "更新最终状态时客户中间表不存在！");
                }
                // 存在原客户信息先保存日志
                if (custPubInfo != null) {
                    //2.1 将正式表保存到日志表
                    Long customerPublicLogId = saveCustPublicLog(custPubInfo);
                    //在saveCustPublicLog中同时处理了关联企业以及股东信息
                    //saveCompanyPartnerLog(customerPublicLogId,custPubInfo);
                    log.info("2.将原客户信息:" + billsPublic.getDepositorName() + "保存到日志表 ");
//                    custPubMidInfo.setCustomerId(custPubInfo.getId());
                    custPubMidInfo.setCustomerId(custPubInfo.getCustomerId());
                    log.info("2.1 从存在的客户正式表的CustomerId到客户中间表");
                }


                //2.2 复制最新的到日志表
                CustomerPublicLogInfo nowCustPubLogInfo = saveCustPublicLog(custPubMidInfo);
                //在saveCustPublicLog中同时处理了关联企业以及股东信息
                //saveCompanyPartnerLog(nowCustPubLogInfo.getId(),custPubMidInfo);
                accountBillsAllService.updateCustomerLogId(billsPublic.getId(), nowCustPubLogInfo.getId());
                log.info("3.将客户信息:" + billsPublic.getDepositorName() + "复制到日志表 ");

                // 2.3 从中间表保存客户信息
                saveCustPublic2(custPubMidInfo, userId, nowCustPubLogInfo.getId());
                //在saveCustPublic中同时处理了关联企业以及股东信息
                //saveCompanyPartner(custPubMidInfo.getCustomerId(),custPubMidInfo);
                log.info("4.将客户信息:" + custPubMidInfo.getDepositorName() + "从中间表保存到正式表 ");

                // 删除中间表的记录

                relateCompanyMidService.deleteByMidId(custPubMidInfo.getId());
                log.info("5.1 删除关联企业中间表信息:" + custPubMidInfo.getDepositorName());
                companyPartnerMidService.deleteByMidId(custPubMidInfo.getId());
                log.info("5.2 删除股东信息中间表信息:" + custPubMidInfo.getDepositorName());
                customerPublicMidService.delete(custPubMidInfo.getId());
                log.info("5.3 删除中间表客户信息:" + custPubMidInfo.getDepositorName());


                // 开户流水走到最终，要更新账户状态为normal
                AccountsAllInfo accountsAllInfo = accountsAllService.getOne(billsPublic.getAccountId());

                //如果是白名单用户，添加标志位，方便查询
                accountsAllInfo.setWhiteList(billsPublic.getWhiteList());
                //更新最新的log表id到account
                accountsAllInfo.setCustomerLogId(nowCustPubLogInfo.getId());

                accountsAllInfo.setLastUpdateBy(userId + "");
                accountsAllInfo.setLastUpdateDate(new Date());

                //开户和变更设置为正常状态
                accountsAllInfo.setAccountStatus(AccountStatus.normal);

                if (billsPublic.getBillType() == BillType.ACCT_OPEN) {

                    // 设置激活日期，报备类添加指定工作日“当前日期+指定工作日”
                    // 核准类等待人行核准以后添加指定工作日“当前日期+指定工作日”
                    if (billsPublic.getAcctType().equals(CompanyAcctType.yiban) || billsPublic.getAcctType().equals(CompanyAcctType.feiyusuan)) {
                        accountsAllInfo.setAcctActiveDate(accountBillsAllService.getActiveDate(new Date()));
                    } else {
                        //核准类需要检测到人行核准才设置该字段
                    }

                    log.info("5.更新账户:" + billsPublic.getAcctNo() + "状态为normal");

                    if (StringUtils.isNotBlank(billsPublic.getPreOpenAcctId()) && !applyNewRuleFlag) {
                        allBillsPublicService.sendModifyStatus(billsPublic, "REGISTER_SUCCESS");
                    }
                }

                //更新对公客户表accountKey
                CustomerPublicInfo customerPublicInfo = customerPublicService.getByCustomerId(custPubMidInfo.getCustomerId());
                if(customerPublicInfo!=null && StringUtils.isNotBlank(customerPublicInfo.getAccountKey()) && !customerPublicInfo.getAccountKey().equals(accountsAllInfo.getAccountKey())){
                    customerPublicInfo.setAccountKey(accountsAllInfo.getAccountKey());
                    customerPublicService.save(customerPublicInfo);
                }
                accountsAllService.save(accountsAllInfo);

            } else if (billsPublic.getBillType() == BillType.ACCT_REVOKE || billsPublic.getBillType() == BillType.ACCT_SUSPEND || billsPublic.getBillType()==BillType.ACCT_CLOSESUSPEND) {
                // 2.2 处理客户中间表，将中间表保存到日志表-->删除中间表信息
                CustomerPublicMidInfo custPubMidInfo = customerPublicMidService.getOne(billsPublic.getMidId());
                // 先保存日志
                CustomerPublicLogInfo nowCustPubLogInfo = saveCustPublicLog(custPubMidInfo);
                //在saveCustPublicLog中同时处理了关联企业以及股东信息
                //saveCompanyPartnerLog(nowCustPubLogInfo.getId(),custPubMidInfo);
                accountBillsAllService.updateCustomerLogId(billsPublic.getId(), nowCustPubLogInfo.getId());
                log.info("2.将中间表客户信息:" + custPubMidInfo.getDepositorName() + "保存到日志表 ");
                // 删除中间表的记录
                relateCompanyMidService.deleteByMidId(custPubMidInfo.getId());
                log.info("3.1 删除关联企业中间表信息:" + custPubMidInfo.getDepositorName());
                companyPartnerMidService.deleteByMidId(custPubMidInfo.getId());
                log.info("3.2 删除股东信息中间表信息:" + custPubMidInfo.getDepositorName());
                customerPublicMidService.delete(custPubMidInfo.getId());
                log.info("3.3 删除中间表客户信息:" + custPubMidInfo.getDepositorName());

                // 2.3更新账户状态
                AccountsAllInfo accountsAllInfo = accountsAllService.getOne(billsPublic.getAccountId());

                // 2.4重新关联midId
                accountsAllInfo.setCustomerLogId(nowCustPubLogInfo.getId());

                if (billsPublic.getBillType() == BillType.ACCT_REVOKE) {
//                    accountsAllInfo.setCancelDate(DateFormatUtils.ISO_DATE_FORMAT.format(new Date()));
                    if (accountsAllInfo.getCancelDate() == null) {
                        accountsAllInfo.setCancelDate(DateFormatUtils.ISO_DATE_FORMAT.format(new Date()));
                    }
                    accountsAllInfo.setAccountStatus(AccountStatus.revoke);
                    // 销户原因
                    if (accountsAllInfo.getAcctCancelReason() == null) {
                        accountsAllInfo.setAcctCancelReason(billsPublic.getDescription());
                    }
                }
                if(billsPublic.getBillType() == BillType.ACCT_SUSPEND){
                    accountsAllInfo.setAcctSuspenDate(DateFormatUtils.ISO_DATE_FORMAT.format(new Date()));
                    accountsAllInfo.setAccountStatus(AccountStatus.suspend);
                }
                if(billsPublic.getBillType()==BillType.ACCT_CLOSESUSPEND){
                    accountsAllInfo.setAccountStatus(AccountStatus.normal);
                }
                // 更新状态
                accountsAllInfo.setLastUpdateBy(userId + "");
                accountsAllInfo.setLastUpdateDate(new Date());

                accountsAllService.save(accountsAllInfo);
                log.info("4.更新账户状态:" + accountsAllInfo.getAcctNo() + "为" + accountsAllInfo.getAccountStatus());
            } else if (billsPublic.getBillType() == BillType.ACCT_EXTENSION) {
                //核准类更新人行核准状态(排除无需同步的情况)
                if (billsPublic.getAcctType().isHeZhun() && billsPublic.getPbcSyncStatus() == CompanySyncStatus.tongBuChengGong) {
                    //取消核准人行审核状态为无需审核
                    if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()){
                        accountBillsAllService.updatePbcCheckStatus(billsPublic.getId(), CompanyAmsCheckStatus.NoCheck);
                    }
                }
                // 2.2 处理客户中间表，将中间表保存到日志表-->删除中间表信息
                CustomerPublicMidInfo custPubMidInfo = customerPublicMidService.getOne(billsPublic.getMidId());
                // 先保存日志
                CustomerPublicLogInfo nowCustPubLogInfo = saveCustPublicLog(custPubMidInfo);
                //在saveCustPublicLog中同时处理了关联企业以及股东信息
                //saveCompanyPartnerLog(nowCustPubLogInfo.getId(),custPubMidInfo);
                accountBillsAllService.updateCustomerLogId(billsPublic.getId(), nowCustPubLogInfo.getId());
                log.info("2.将中间表客户信息:" + custPubMidInfo.getDepositorName() + "保存到日志表 ");
                // 删除中间表的记录
                relateCompanyMidService.deleteByMidId(custPubMidInfo.getId());
                log.info("3.1 删除关联企业中间表信息:" + custPubMidInfo.getDepositorName());
                companyPartnerMidService.deleteByMidId(custPubMidInfo.getId());
                log.info("3.2 删除股东信息中间表信息:" + custPubMidInfo.getDepositorName());
                customerPublicMidService.delete(custPubMidInfo.getId());
                log.info("3.3 删除中间表客户信息:" + custPubMidInfo.getDepositorName());

                // 开户流水走到最终，要更新账户状态为normal
                AccountsAllInfo accountsAllInfo = accountsAllService.getOne(billsPublic.getAccountId());

                //如果是白名单用户，添加标志位，方便查询
                accountsAllInfo.setWhiteList(billsPublic.getWhiteList());
                //更新最新的log表id到account
                accountsAllInfo.setCustomerLogId(nowCustPubLogInfo.getId());

                accountsAllInfo.setLastUpdateBy(userId + "");
                accountsAllInfo.setLastUpdateDate(new Date());

                //开户和变更设置为正常状态
                accountsAllInfo.setAccountStatus(AccountStatus.normal);

                accountsAllService.save(accountsAllInfo);
                log.info("4.更新账户状态:" + accountsAllInfo.getAcctNo() + "为" + accountsAllInfo.getAccountStatus());
            }

        } else {
            throw new BizServiceException(EErrorCode.BILL_UPDATE_FINALSTATUS_ERROR, "更新流水状态失败，缺少流水信息主键或客户临时信息主键！");
        }
    }

    @Override
    public void updateFinalStatusById(Long billId) throws Exception {
        //执行最终方法，修改状态
        AllBillsPublicDTO billsPublic = allBillsPublicService.findOne(billId);
        String currentUserName = SecurityUtils.getCurrentUsername();
        UserDto userDto;
        if (StringUtils.isNotBlank(currentUserName)) {
            userDto = userService.findByUsername(currentUserName);
        } else {
            userDto = userService.findVirtualUser();
        }
        allBillsPublicService.updateFinalStatus(billsPublic, userDto.getId());
    }


    @Override
    public TableResultResponse<AllBillsPublicSearchDTO> query(final AccountBillsAllSearchInfo accountBillsAllInfo, final String code, Pageable pageable) {
        Page<AccountBillsAll> page = null;
        long count;
        List<String> whiteLists = new ArrayList<String>();

        if ("dsbCheck".equalsIgnoreCase(code) || "dbllb".equalsIgnoreCase(code)) {
            Specification<AccountBillsAll> specification = new Specification<AccountBillsAll>() {

                @Override
                public Predicate toPredicate(Root<AccountBillsAll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                    Predicate predicate = cb.conjunction();
                    List<Expression<Boolean>> expressions = predicate.getExpressions();

                    expressions.add(cb.and(cb.like(root.<String>get("organFullId"), accountBillsAllInfo.getOrganFullId() + "%")));

                    if (StringUtils.isNotBlank(accountBillsAllInfo.getAcctNo())) {
                        expressions.add(cb.and(cb.equal(root.get("acctNo"), accountBillsAllInfo.getAcctNo())));
                    }
                    if (StringUtils.isNotBlank(accountBillsAllInfo.getBillNo())) {
                        expressions.add(cb.and(cb.equal(root.get("billNo"), accountBillsAllInfo.getBillNo())));
                    }
                    if ("1".equals(accountBillsAllInfo.getWhiteList())) {
                        expressions.add(cb.and(cb.equal(root.get("whiteList"), "1")));
                    }else{
                        Predicate whiteList = cb.equal(root.get("whiteList"), "0");
                        Predicate whiteList2 = cb.equal(root.get("whiteList"), "");
                        Predicate whiteList1 = cb.isNull(root.get("whiteList"));
                        expressions.add(cb.or(whiteList, whiteList1,whiteList2));
                    }
                    if (accountBillsAllInfo.getBillType() != null) {
                        expressions.add(cb.and(cb.equal(root.get("billType"), accountBillsAllInfo.getBillType())));
                    }

                    if (accountBillsAllInfo.getAcctType() != null) {
                        expressions.add(cb.and(cb.equal(root.get("acctType"), accountBillsAllInfo.getAcctType())));
                    }
                    if (accountBillsAllInfo.getOpenAccountSiteType() != null) {
                        expressions.add(cb.and(cb.equal(root.get("openAccountSiteType"), accountBillsAllInfo.getOpenAccountSiteType())));
                    }
                    if (accountBillsAllInfo.getPbcSyncMethod() != null) {
                        expressions.add(cb.and(cb.equal(root.get("pbcSyncMethod"), accountBillsAllInfo.getPbcSyncMethod())));
                    }

                    if (accountBillsAllInfo.getPbcCheckStatus() != null) {
                        expressions.add(cb.and(cb.equal(root.get("pbcCheckStatus"), accountBillsAllInfo.getPbcCheckStatus())));
                    }

                    if (StringUtils.isNotBlank(accountBillsAllInfo.getDepositorName())) {
                        expressions.add(cb.and(cb.like(root.<String>get("depositorName"), "%" + accountBillsAllInfo.getDepositorName() + "%")));
                    }

                    if (accountBillsAllInfo.getBeginDate() != null) {
                        expressions.add(cb.and(cb.greaterThanOrEqualTo(root.<Date>get("createdDate"), accountBillsAllInfo.getBeginDate())));
                    }

                    if (accountBillsAllInfo.getEndDate() != null) {
                        expressions.add(cb.and(cb.lessThanOrEqualTo(root.<Date>get("createdDate"), accountBillsAllInfo.getEndDate())));
                    }

                    if (accountBillsAllInfo.getPbcSyncBeginDate() != null) {
                        expressions.add(cb.and(cb.greaterThanOrEqualTo(root.<String>get("pbcSyncTime"), accountBillsAllInfo.getPbcSyncBeginDate())));
                    }

                    if (accountBillsAllInfo.getPbcSyncEndDate() != null) {
                        expressions.add(cb.and(cb.lessThanOrEqualTo(root.<String>get("pbcSyncTime"), accountBillsAllInfo.getPbcSyncEndDate())));
                    }

                    if (accountBillsAllInfo.getEccsSyncBeginDate() != null) {
                        expressions.add(cb.and(cb.greaterThanOrEqualTo(root.<String>get("eccsSyncTime"), accountBillsAllInfo.getEccsSyncBeginDate())));
                    }

                    if (accountBillsAllInfo.getEccsSyncEndDate() != null) {
                        expressions.add(cb.and(cb.lessThanOrEqualTo(root.<String>get("eccsSyncTime"), accountBillsAllInfo.getEccsSyncEndDate())));
                    }

                    if (accountBillsAllInfo.getCreatedByes() != null && accountBillsAllInfo.getCreatedByes().size() > 0) {
                        Path<String> createdByPath = root.get("createdBy");
                        CriteriaBuilder.In<String> in = cb.in(createdByPath);
                        for (String createdBy : accountBillsAllInfo.getCreatedByes()) {
                            in.value(createdBy);
                        }
                        expressions.add(cb.and(cb.and(in)));
                    }

                    if (accountBillsAllInfo.getOrganFullIdList() != null && accountBillsAllInfo.getOrganFullIdList().size() > 0) {
                        Path<String> organFullIdPath = root.get("organFullId");
                        CriteriaBuilder.In<String> in = cb.in(organFullIdPath);
                        for (String organFullId : accountBillsAllInfo.getOrganFullIdList()) {
                            in.value(organFullId);
                        }
                        expressions.add(cb.and(cb.and(in)));
                    }

                    if ("dsbCheck".equalsIgnoreCase(code)) {
                        expressions.add(cb.and(cb.equal(root.get("status"), accountBillsAllInfo.getStatus())));
//                        Predicate pbcSyncStatus = cb.equal(root.get("pbcSyncStatus"), accountBillsAllInfo.getPbcSyncStatus());
//                        Predicate eccsSyncStatus = cb.equal(root.get("eccsSyncStatus"), accountBillsAllInfo.getEccsSyncStatus());
//                        expressions.add(cb.or(pbcSyncStatus, eccsSyncStatus));
                    } else {
                        Predicate status = cb.and(cb.equal(root.get("status"), BillStatus.WAITING_SUPPLEMENT));

                        Predicate pbcSyncStatus = cb.equal(root.get("pbcSyncStatus"), accountBillsAllInfo.getPbcSyncStatus());
                        Predicate eccsSyncStatus = cb.equal(root.get("eccsSyncStatus"), accountBillsAllInfo.getEccsSyncStatus());

//                        Predicate dbl = cb.and(status, cb.or(pbcSyncStatus, eccsSyncStatus));
                        Predicate dbl = cb.or(status, pbcSyncStatus, eccsSyncStatus);

                        //加入待上报的数据
                        Predicate dsb = cb.and(cb.equal(root.get("status"), BillStatus.APPROVED));
                        Predicate dsbPbc = cb.equal(root.get("pbcSyncStatus"), CompanySyncStatus.weiTongBu);
                        Predicate dsbEccs = cb.equal(root.get("eccsSyncStatus"), CompanySyncStatus.weiTongBu);
                        dsb = cb.and(dsb, cb.or(dsbPbc, dsbEccs));

                        Predicate sdb_dbl = cb.or(dsb, dbl);
                        predicate = cb.and(predicate, sdb_dbl);
                    }

                    return predicate;
                }
            };

            page = accountBillsAllDao.findAll(specification, pageable);
            count = accountBillsAllDao.count(specification);
        } else {
            if ("cgsblb".equalsIgnoreCase(code) || "zhhztgCheck".equalsIgnoreCase(code)) {
                accountBillsAllInfo.setBillTypeEx(BillType.ACCT_INIT);
            }
            if(StringUtils.isNotBlank(accountBillsAllInfo.getWhiteList())){
                if("1".equals(accountBillsAllInfo.getWhiteList())){
                    whiteLists.add(accountBillsAllInfo.getWhiteList());
                    accountBillsAllInfo.setWhiteLists(whiteLists);
                }else{
                    whiteLists.add(accountBillsAllInfo.getWhiteList());
                    whiteLists.add(null);
                    whiteLists.add("");
                    accountBillsAllInfo.setWhiteLists(whiteLists);
                }
            }
            if(StringUtils.equals("syncImageList",accountBillsAllInfo.getSyncImageList())){
                List<CompanySyncStatus> imgaeSyncStatuses = new ArrayList<>();
                accountBillsAllInfo.setPbcSyncStatus(accountBillsAllInfo.getPbcSyncStatus());
                if(accountBillsAllInfo.getImgaeSyncStatus() == CompanySyncStatus.weiTongBu){
                    accountBillsAllInfo.setImgaeSyncStatus(null);
                    imgaeSyncStatuses.add(null);
                    imgaeSyncStatuses.add(CompanySyncStatus.weiTongBu);
                    accountBillsAllInfo.setImgaeSyncStatuses(imgaeSyncStatuses);
                }
            }
            page = accountBillsAllDao.findAll(new AccountBillsAllSearchSpec(accountBillsAllInfo), pageable);
            count = accountBillsAllDao.count(new AccountBillsAllSearchSpec(accountBillsAllInfo));
        }

        List<AccountBillsAll> accountBillsAllList = page.getContent();

        List<AllBillsPublicSearchDTO> accountVoList = this.billResultListInit(accountBillsAllList);

        return new TableResultResponse<AllBillsPublicSearchDTO>((int) count, accountVoList);
    }

    @Override
    public TableResultResponse<AllBillsPublicSearchDTO> query(String acctNo, Pageable pageable) {
        List<AllBillsPublicSearchDTO> accountVoList = new ArrayList<>();
        AccountsAll accountsAll = accountsAllDao.findByAcctNo(acctNo);
        List<AccountBillsAll> accountBillsAllList = accountBillsAllDao.findByAcctNoOrderByLastUpdateDate(acctNo, pageable);
        Long count = accountBillsAllDao.countByAcctNo(acctNo);

        if (CollectionUtils.isNotEmpty(accountBillsAllList)) {
            for (AccountBillsAll accountBilsAll : accountBillsAllList) {
                AllBillsPublicSearchDTO allBillsPublicSearchDTO = new AllBillsPublicSearchDTO();
                BeanUtils.copyProperties(accountBilsAll, allBillsPublicSearchDTO);
                if (allBillsPublicSearchDTO.getBillType() != null) {
                    allBillsPublicSearchDTO.setBillTypeStr(allBillsPublicSearchDTO.getBillType().getValue());
                }
                allBillsPublicSearchDTO.setLastUpdateDate(DateUtils.DateToStr(accountBilsAll.getLastUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
                try {
                    if (accountBilsAll.getCreatedBy() != null) {
                        UserDto user = userService.findById(Long.valueOf(accountBilsAll.getCreatedBy()));
                        if (user != null) {
                            allBillsPublicSearchDTO.setCreatedName(user.getUsername()+"-"+user.getCname());
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                //根据fullId获取核心机构号
                if (StringUtils.isNotBlank(accountBilsAll.getOrganFullId())) {
                    OrganizationDto od = organizationService.findByOrganFullId(accountBilsAll.getOrganFullId());
                    if (od != null) {
                        allBillsPublicSearchDTO.setKernelOrgCode(od.getCode());
                    }
                }

                JSONArray jsonArray = accountChangeSummaryService.findByAccountChangeItemsAll(accountBilsAll.getId());
                allBillsPublicSearchDTO.setChangeRecordJsonStr(jsonArray.toJSONString());
                allBillsPublicSearchDTO.setAcctName(accountsAll.getAcctName());
                accountVoList.add(allBillsPublicSearchDTO);
            }
        }
        return new TableResultResponse<AllBillsPublicSearchDTO>(count.intValue(), accountVoList);
    }


    /**
     * 取消YD_PUBLIC_BILLS_ALL_V视图
     * 已经废弃,使用query(final AccountBillsAllInfo accountBillsAllInfo, final String code, Pageable pageable)方法
     *
     * @param info
     * @param pageable
     * @param code
     * @return
     */
    @Override
    @Deprecated
    public TableResultResponse<AllBillsPublicDTO> query(AllBillsPublicDTO info, Pageable pageable, String code) {
        Page<AllBillsPublicDTO> page = null;
        Page<AllAccountPublicDTO> page2 = null;

        if ("dbllb".equals(code)) {  //待补录
            String[] dbllbFields = {"acctNo", "depositorName", "acctType", "billType", "bankCode", "createdDate", "pbcSyncStatus", "eccsSyncStatus"};
            boolean[] isLikes = {true, true, false, false, true, true, false, false};
            page = allBillsPublicDao.findPage(makeQuerySQL(info, dbllbFields, isLikes, code).toSQLString(), null,
                    pageable);
        } else if ("zhsqCheck".equals(code)) {  //账户申请
            String[] zhsqFields = {"acctNo", "depositorName", "acctType", "billType", "bankCode", "status", "createdDate", "createdBy"};
            boolean[] isLikes = {true, true, false, false, true, false, false, false};
            page = allBillsPublicDao.findPage(makeQuerySQL(info, zhsqFields, isLikes, code).toSQLString(), null,
                    pageable);
        } else if ("cgsblb".equals(code)) {  //成功上报
            String[] cgsblbFields = {"acctNo", "depositorName", "acctType", "billType", "bankCode", "status", "createdDate", "pbcSyncStatus", "eccsSyncStatus", "pbcCheckStatus", "pbcSyncMethod", "pbcSyncTime", "eccsSyncTime", "pbcCheckDate"};
            boolean[] isLikes = {true, true, false, false, true, false, false, false, false, false, false, false, false, false};
            page = allBillsPublicDao.findPage(makeQuerySQL(info, cgsblbFields, isLikes, code).toSQLString(), null,
                    pageable);
        } else if ("zhshCheck".equals(code)) {  //账户审核
            String[] zhshFields = {"acctNo", "depositorName", "acctType", "billType", "bankCode", "status", "pbcSyncStatus", "eccsSyncStatus", "createdDate", "createdBy", "pbcSyncMethod"};
            boolean[] isLikes = {true, true, false, false, true, false, false, false, false, false, false};
            page = allBillsPublicDao.findPage(makeQuerySQL(info, zhshFields, isLikes, code).toSQLString(), null,
                    pageable);
        } else if ("zhhztgChsynceck".equals(code)) {  //核准通过
            String[] zhhztgFields = {"acctNo", "depositorName", "acctType", "billType", "bankCode", "status", "createdDate", "pbcSyncStatus", "eccsSyncStatus", "pbcCheckStatus", "pbcSyncMethod", "pbcSyncTime", "eccsSyncTime", "pbcCheckDate"};
            boolean[] isLikes = {true, true, false, false, true, false, false, false, false, false, false, false, false, false};
            page = allBillsPublicDao.findPage(makeQuerySQL(info, zhhztgFields, isLikes, code).toSQLString(), null,
                    pageable);
        } else if ("dsbCheck".equals(code)) {  //待上报
            String[] zhshFields = {"acctNo", "depositorName", "acctType", "billType", "bankCode", "status", "pbcSyncStatus", "eccsSyncStatus", "createdDate", "createdBy", "pbcSyncMethod"};
            boolean[] isLikes = {true, true, false, false, true, false, false, false, false, false, false};
            page = allBillsPublicDao.findPage(makeQuerySQL(info, zhshFields, isLikes, code).toSQLString(), null,
                    pageable);
        }

        return new TableResultResponse<AllBillsPublicDTO>((int) page.getTotalElements(), page.getContent());
    }

    @Override
    public AllBillsPublicDTO submit(Long userId, Map<String, String> formData) {
        UserDto userInfo = userService.findById(userId);
        String key = FormUtils.getComponentName(formData.get("acctType"), formData.get("billType"));
        if(StringUtils.equals(formData.get("billType"),"ACCT_CLOSESUSPEND")){
            key="amsCloseSuspendFormProcessor";
        }
        AllPublicAccountFormProcessor formProcessor = formProcessorMap.get(key);
        if (formProcessor == null) {
            throw new RuntimeException("表单处理器" + FormUtils.getComponentName(formData.get("acctType"), formData.get("billType")) + "不存在");
        }

        AllBillsPublicDTO allBillsPublic = formProcessor.process(userInfo, formData);

        return allBillsPublic;
    }

    /**
     * 装饰AllBillsPublic对象
     *
     * @param accountId
     * @param refBillId
     * @param customerLogId
     * @param createdDate
     * @param allBillsPublicDTO
     */
    @Override
    public void convertAllBillsPublic(Long accountId, Long refBillId, Long customerLogId, Date createdDate, AllBillsPublicDTO allBillsPublicDTO) {

        //增加acctType字段  防止同账号先销户后开户后（一般户转基本户）账户性质覆盖时，流水中的账户性质被覆盖
        String[] ignoreProperties = {"id", "createdDate", "createdBy", "lastUpdateBy", "lastUpdateDate","accountKey","string006","cancelHeZhun","acctType"};
        //最新流水
        AccountBillsAll accountBillsAll = accountBillsAllDao.findOne(refBillId);
        AccountPublicLogInfo accountPublicLog = accountPublicLogService.findByRefBillIdLast(refBillId);
        AccountPublicInfo accountPublicInfo = accountPublicService.findByAccountId(accountId);
        //最新流水是变更   并且流水未完成  查找log表数据
        if (accountPublicLog != null && accountBillsAll.getBillType() == BillType.ACCT_CHANGE && accountBillsAll.getFinalStatus() == CompanyIfType.No) {
            BeanUtils.copyProperties(accountPublicLog, allBillsPublicDTO, ignoreProperties);
        } else {
            if (accountId != null) {
                //AccountPublicInfo accountPublicInfo = accountPublicService.findByAccountId(accountId);
                AccountsAllInfo accountsAllInfo = accountsAllService.getOne(accountId);
                if (accountPublicInfo != null && accountsAllInfo != null) {
                    BeanUtils.copyProperties(accountPublicInfo, allBillsPublicDTO, ignoreProperties);
                    BeanUtils.copyProperties(accountsAllInfo, allBillsPublicDTO, ignoreProperties);
                } else {
                    AccountPublicLogInfo accountPublicLogInfo = accountPublicLogService.getOne(accountId);
                    if(accountPublicLogInfo!=null){
                        BeanUtils.copyProperties(accountPublicLogInfo, allBillsPublicDTO, ignoreProperties);
                    }
                }
            }
        }
        if(accountPublicInfo != null){
            //将经办人的相关信息提取出来
            allBillsPublicDTO.setOperatorIdcardType(accountPublicInfo.getOperatorIdcardType());
            allBillsPublicDTO.setOperatorIdcardDue(accountPublicInfo.getOperatorIdcardDue());
            allBillsPublicDTO.setOperatorIdcardNo(accountPublicInfo.getOperatorIdcardNo());
            allBillsPublicDTO.setOperatorName(accountPublicInfo.getOperatorName());
            allBillsPublicDTO.setOperatorTelephone(accountPublicInfo.getOperatorTelephone());
            allBillsPublicDTO.setCurrency0(accountPublicInfo.getCurrency0());
            allBillsPublicDTO.setCurrency1(accountPublicInfo.getCurrency1());
        }
        //TODO 主表、日志表、中间表需完善
        if (customerLogId != null) {
            CustomerPublicLogInfo customerPublicLogInfo = customerPublicLogService.getOne(customerLogId);
            if (customerPublicLogInfo != null) {
                String[] ignoreProperties2 = {"id", "createdDate", "createdBy", "lastUpdateBy", "lastUpdateDate","operatorIdcardDue","accountKey","organFullId","string006"};
                BeanUtils.copyProperties(customerPublicLogInfo, allBillsPublicDTO, ignoreProperties2);
                //保存股东信息
                List<CompanyPartnerLogInfo> companyPartnerLogInfoList= companyPartnerLogService.getAllByCustomerPublicLogId(customerPublicLogInfo.getId());
                List<CompanyPartnerInfo> companyPartnerInfos = ConverterService.convertToList(companyPartnerLogInfoList, CompanyPartnerInfo.class);
                Set<CompanyPartnerInfo> companyPartnerInfoHashSet = new TreeSet<>();
                for(CompanyPartnerInfo c : companyPartnerInfos){
                    companyPartnerInfoHashSet.add(c);
                }
                allBillsPublicDTO.setCompanyPartners(companyPartnerInfoHashSet);

                //关联企业
                List<RelateCompanyLogInfo> relateCompanyLogInfos = relateCompanyLogService.getAllByCustomerPublicLogId(customerPublicLogInfo.getId());
                List<RelateCompanyInfo> relateCompanyInfos = ConverterService.convertToList(relateCompanyLogInfos, RelateCompanyInfo.class);
                Set<RelateCompanyInfo> relateCompanyInfoset = new TreeSet<>();
                for(RelateCompanyInfo c : relateCompanyInfos){
                    relateCompanyInfoset.add(c);
                }
                allBillsPublicDTO.setRelateCompanys(relateCompanyInfoset);

            } else {
                CustomerPublicMidInfo customerPublicMidInfo = customerPublicMidService.getOne(customerLogId);
                if (customerPublicMidInfo != null) {
                    BeanUtils.copyProperties(customerPublicMidInfo, allBillsPublicDTO, ignoreProperties);
                    allBillsPublicDTO.setMidId(customerPublicMidInfo.getId());
                    //保存股东信息
                    List<CompanyPartnerMidInfo> companyPartnerMidInfoList = companyPartnerMidService.getAllByCustomerPublicMidId(customerPublicMidInfo.getId());
                    List<CompanyPartnerInfo> companyPartnerInfoList = ConverterService.convertToList(companyPartnerMidInfoList, CompanyPartnerInfo.class);
                    Set<CompanyPartnerInfo> companyPartnerInfoHashSet = new TreeSet<>();
                    for(CompanyPartnerInfo c : companyPartnerInfoList){
                        companyPartnerInfoHashSet.add(c);
                    }
                    allBillsPublicDTO.setCompanyPartners(companyPartnerInfoHashSet);

                    //关联企业
                    List<RelateCompanyMidInfo> relateCompanyMidInfos = relateCompanyMidService.getAllByCustomerPublicMidId(customerPublicMidInfo.getId());
                    List<RelateCompanyInfo> relateCompanyInfos = ConverterService.convertToList(relateCompanyMidInfos, RelateCompanyInfo.class);
                    Set<RelateCompanyInfo> relateCompanyInfoset = new TreeSet<>();
                    for(RelateCompanyInfo c : relateCompanyInfos){
                        relateCompanyInfoset.add(c);
                    }
                    allBillsPublicDTO.setRelateCompanys(relateCompanyInfoset);
                }
            }
        }

        allBillsPublicDTO.setCreatedDate(DateFormatUtils.format(createdDate, "yyyy-MM-dd HH:mm:ss"));
        if (StringUtils.isNotBlank(allBillsPublicDTO.getCreatedBy())) {
            UserDto user = userService.findAllById(Long.parseLong(allBillsPublicDTO.getCreatedBy()));
            allBillsPublicDTO.setCreatedBy(user.getUsername()+"-"+user.getCname());
        }
        if (StringUtils.isNotBlank(allBillsPublicDTO.getLastUpdateBy())) {
            UserDto user = userService.findAllById(Long.parseLong(allBillsPublicDTO.getLastUpdateBy()));
            allBillsPublicDTO.setLastUpdateBy(user.getUsername()+"-"+user.getCname());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateSyncStatusAndFinishBill(AllBillsPublicDTO billsPublic, Long userId) throws Exception {
        accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.PBC, billsPublic.getPbcSyncStatus(), null, "", userId);
        accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.ECCS, billsPublic.getEccsSyncStatus(), null, "", userId);
        accountBillsAllService.updatePbcCheckStatus(billsPublic.getId(), billsPublic.getPbcCheckStatus());
        allBillsPublicService.updateFinalStatus(billsPublic, userId);
    }

    @Override
    public boolean checkJibenByCustomerNoOrDepositorName(String customerNo, String depositorName) {
        CustomersAllInfo customersAllInfo = customersAllService.findByCustomerNo(customerNo);
        if (customersAllInfo == null) {
            customersAllInfo = customersAllService.findByDepositorName(depositorName);
        }
        if (customersAllInfo == null) {
            return true;
        } else {
            Long customerId = customersAllInfo.getId();
            List<CustomerPublicLogInfo> customerPublicLogInfos = customerPublicLogService.getByCustomerId(customerId);
            for (CustomerPublicLogInfo customerPublicLogInfo : customerPublicLogInfos) {
                Long customerLogId = customerPublicLogInfo.getId();
                List<AccountsAllInfo> accountsAllInfos = accountsAllService.findByCustomerLogId(customerLogId);
                for (AccountsAllInfo accountsAllInfo : accountsAllInfos) {
                    if (accountsAllInfo.getAcctType() == CompanyAcctType.jiben) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public TableResultResponse<AllBillsPublicSearchDTO> listForBills(AccountBillsAllSearchInfo info, String code, Pageable pageable) {
        int dayBetween = 0;
        info.setOrganFullId(SecurityUtils.getCurrentOrgFullId());

        List<ConfigDto> list = configService.findByKey("acctStatisticsRange");
        if (CollectionUtils.isNotEmpty(list)) {
            ConfigDto dto = list.get(0);
            String dayBetweenStr = dto.getConfigValue();
            if ("beforeOneDay".equals(dayBetweenStr)) {
                dayBetween = 1;
            } else if ("beforeOneWeek".equals(dayBetweenStr)) {
                dayBetween = 7;
            } else if ("beforeOneMonth".equals(dayBetweenStr)) {
                dayBetween = 30;
            }
        }

        if ("addAcct".equals(code)) {
            info.setBillType(BillType.ACCT_OPEN);
        } else if("addBills".equals(code)) {
            info.setBillTypeEx(BillType.ACCT_INIT);
        }

        if (dayBetween == 0) {
            info.setBeginDate(null);
            info.setEndDate(null);
        } else {
            info.setBeginDate(DateUtils.dayBefore(new Date(), dayBetween));
            info.setEndDate(new Date());
        }

        Page<AccountBillsAll> page = accountBillsAllDao.findAll(new AccountBillsAllSearchSpec(info), pageable);
        long count = accountBillsAllDao.count(new AccountBillsAllSearchSpec(info));

        List<AccountBillsAll> accountBillsAllList = page.getContent();

        List<AllBillsPublicSearchDTO> accountVoList = this.billResultListInit(accountBillsAllList);

        return new TableResultResponse<>((int) count, accountVoList);
    }

    @Override
    public AllBillsPublicDTO findByBillId(Long billId) {
        AccountBillsAll accountBillsAll = accountBillsAllDao.findOne(billId);
        return convertBill2Full(accountBillsAll);
    }


    private AllBillsPublicDTO convertBill2Full(AccountBillsAll accountBillsAll) {
        if (accountBillsAll == null) {
            return null;
        }
        AllBillsPublicDTO allBillsPublicDTO = new AllBillsPublicDTO();
        BeanUtils.copyProperties(accountBillsAll, allBillsPublicDTO);
        allBillsPublicDTO.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        Long accountId = accountBillsAll.getAccountId();
        Long customerLogId = accountBillsAll.getCustomerLogId();
        Date createdDate = accountBillsAll.getCreatedDate();
        convertAllBillsPublic(accountId, accountBillsAll.getId(), customerLogId, createdDate, allBillsPublicDTO);
        return allBillsPublicDTO;
    }


    @Override
    public ObjectRestResponse<AllBillsPublicDTO> getOneDetails(Long id, String billType) {
        AllBillsPublicDTO info = findByBillId(id);

        if (info != null) {
            /*
             * if ("ACCT_SUSPEND".equals(billType) || "ACCT_REVOKE".equals(billType) ||
             * "ACCT_CHANGE".equals(billType)) { info.setId(null); }
             */
            if (info.getOriginalBillId() == null) {
                info.setOriginalBillId(id);
            }
            //处理创建时间后面的数字
            try {
                String createdDate = info.getCreatedDate();
                if (StringUtils.isNotBlank(createdDate) && StringUtils.length(createdDate) == 21) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                    //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                    Date date = sdf.parse(info.getCreatedDate());

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    info.setCreatedDate(format.format(date));
                }
            } catch (Exception e) {

                e.printStackTrace();
            }

            setAcctCreateDate(info);
            if ("ACCT_REVOKE".equals(billType)) { // 销户详情页字典转化
                conversion(info);
            }

            return new ObjectRestResponse<AllBillsPublicDTO>().rel(true).result(info);
        } else {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(true).result("");
        }
    }

    @Override
    public AllBillsPublicDTO findOne(Long id) {
        AllBillsPublicDTO info = findByBillId(id);

        return info;
    }
    @Override
    public AllBillsPublicDTO printChang(Long billid,AllBillsPublicDTO info) {
        String[] arry = {"acctName","regAddress","zipcode","telephone","registeredCapital","fileType","fileNo","businessScope",
                "legalName","legalIdcardType","legalIdcardNo","parAccountKey","parLegalName","parLegalIdcardType","parLegalIdcardNo"};
        for (String str:arry) {
            String value = accountChangeSummaryService.findIsChange(billid,str);
            //不为空证明有修改
            if(StringUtils.isNotBlank(value)){
                //打印新值
                if(StringUtils.equals("registeredCapital",str)){
                    info.setRegisteredCapital(new BigDecimal(value));
                }else{
                    ReflectionUtil.invokeSetter(info,str,value);
                }

            }else{
                if(StringUtils.equals("registeredCapital",str)){
                    info.setRegisteredCapital(new BigDecimal("0"));
                }else{
                    ReflectionUtil.invokeSetter(info,str,"");
                }

            }
        }
        return info;
    }

    @Override
    public TableResultResponse<AllBillsPublicSearchDTO> openStatisticsDetailList(String acctNo, String kernelOrgCode, String depositorName, String openAccountSiteType, String createdBy,
                                                                                 String depositorType, String acctType, String organFullId, String beginDate, String endDate,
                                                                                 Date beginDateApply, Date endDateApply, Pageable pageable) {
        OpenAccountStatisticsSearchDto oassd = new OpenAccountStatisticsSearchDto();
        if(StringUtils.isNotBlank(createdBy)){
            List<Long> userIdList = userService.findUserIdByLikeCName(createdBy);
            oassd.setUserIdList(userIdList);
        }
        oassd.setAcctNo(acctNo);
        oassd.setOrganCode(kernelOrgCode);
        oassd.setDepositorName(depositorName);
        oassd.setOpenAccountSiteType(OpenAccountSiteType.str2enum(openAccountSiteType));
        oassd.setDepositorType(depositorType);
        oassd.setAcctType(CompanyAcctType.str2enum(acctType));
        oassd.setOrganFullId(organFullId);
        if(StringUtils.isNotBlank(beginDate)){
            oassd.setBeginDate(beginDate);
        }
        if(StringUtils.isNotBlank(endDate)){
            oassd.setEndDate(endDate);
        }
        if (beginDateApply != null) {
            oassd.setBeginDateApply(beginDateApply);
        }
        if (endDateApply != null) {
            oassd.setEndDateApply(endDateApply);
        }
        Page<OpenAccountStatisticsView> page = openAccountStatisticsDao.findAll(new OpenAccountStatisticsSpec(oassd), pageable);

        List<OpenAccountStatisticsView> list = page.getContent();
        List<AllBillsPublicSearchDTO> abpsdList = new ArrayList<>();
        for(OpenAccountStatisticsView oasv: list){
            AllBillsPublicSearchDTO abpsd = new AllBillsPublicSearchDTO();
            BeanCopierUtils.copyProperties(oasv, abpsd);
            abpsd.setCreatedName(oasv.getUserName());
            abpsd.setCreatedDate(oasv.getBillDate());
            abpsd.setKernelOrgCode(oasv.getOrganCode());
            abpsdList.add(abpsd);
        }
        return new TableResultResponse<AllBillsPublicSearchDTO>((int)page.getTotalElements(), abpsdList);
    }

    @Override
    public void setCondition(String code, AccountBillsAllInfo accountBillsAllInfo) {
        List<BillStatus> statuses = null;
        List<CompanySyncStatus> eccsSyncStatuses = null;
        List<CompanySyncStatus> pbcSyncStatuses = null;

        accountBillsAllInfo.setOrganFullId(SecurityUtils.getCurrentOrgFullId());

        if ("zhsqCheck".equals(code)) {  //账户申请
            statuses = new ArrayList<>();
            statuses.add(BillStatus.NEW);
            statuses.add(BillStatus.APPROVING);
            statuses.add(BillStatus.REJECT);
            accountBillsAllInfo.setStatuses(statuses);
        } else if ("cgsblb".equals(code)) {  //成功上报

            //        sqlBuilder.eq("yd_status", "APPROVED");
//        sqlBuilder.eq("yd_pbc_sync_status", "tongBuChengGong");
//        eccsSyncStatuses = new ArrayList<>();
//        eccsSyncStatuses.add("buTongBu");
//        eccsSyncStatuses.add("tongBuChengGong");
//        sqlBuilder.in("yd_eccs_sync_status", eccsSyncStatuses);

            statuses = new ArrayList<>();
            eccsSyncStatuses = new ArrayList<>();
            pbcSyncStatuses = new ArrayList<>();
            accountBillsAllInfo.setStatus(BillStatus.APPROVED);

            pbcSyncStatuses.add(CompanySyncStatus.buTongBu);
            pbcSyncStatuses.add(CompanySyncStatus.tongBuChengGong);
            accountBillsAllInfo.setPbcSyncStatuses(pbcSyncStatuses);
            eccsSyncStatuses.add(CompanySyncStatus.buTongBu);
            eccsSyncStatuses.add(CompanySyncStatus.tongBuChengGong);
            accountBillsAllInfo.setEccsSyncStatuses(eccsSyncStatuses);
            accountBillsAllInfo.setBillTypeEx(BillType.ACCT_INIT);
        } else if ("zhshCheck".equals(code)) {  //待审核
            accountBillsAllInfo.setStatus(BillStatus.APPROVING);
        } else if ("dsbCheck".equals(code)) {  //待上报
            accountBillsAllInfo.setStatus(BillStatus.WAITING_REPORTING);
//            accountBillsAllInfo.setStatus(BillStatus.APPROVED);
//            accountBillsAllInfo.setPbcSyncStatus(CompanySyncStatus.weiTongBu);
//            accountBillsAllInfo.setEccsSyncStatus(CompanySyncStatus.weiTongBu);
        } else if ("zhhztgCheck".equals(code)) {  //核准通过
            accountBillsAllInfo.setPbcCheckStatus(CompanyAmsCheckStatus.CheckPass);
            accountBillsAllInfo.setBillTypeEx(BillType.ACCT_INIT);
        } else if ("dbllb".equals(code)) {  //待补录

//            sqlBuilder.eqAdd("yd_status", "WAITING_SUPPLEMENT");
//            sqlBuilder.orEq("yd_status", "APPROVED");
//            sqlBuilder.eqAdd("yd_eccs_sync_status", "tongBuShiBai");
//            sqlBuilder.orEquals("yd_pbc_sync_status", "tongBuShiBai");
            accountBillsAllInfo.setPbcSyncStatus(CompanySyncStatus.tongBuShiBai);
            accountBillsAllInfo.setEccsSyncStatus(CompanySyncStatus.tongBuShiBai);
        }
    }


    @Override
    public List<String> addBillCounts(final String code, final AccountBillsAllSearchInfo accountBillsAllInfo, List<String> billAllCountsList) {
        Specification<AccountBillsAll> specification = new Specification<AccountBillsAll>() {
            @Override
            public Predicate toPredicate(Root<AccountBillsAll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

//                expressions.add(cb.equal(root.get("organFullId"), accountBillsAllInfo.getOrganFullId()));
                expressions.add(cb.like(root.<String>get("organFullId"), accountBillsAllInfo.getOrganFullId() + "%"));

                if ("dsbCheck".equalsIgnoreCase(code)) {
                    expressions.add(cb.and(cb.equal(root.get("status"), accountBillsAllInfo.getStatus())));
//                    Predicate pbcSyncStatus = cb.equal(root.get("pbcSyncStatus"), accountBillsAllInfo.getPbcSyncStatus());
//                    Predicate eccsSyncStatus = cb.equal(root.get("eccsSyncStatus"), accountBillsAllInfo.getEccsSyncStatus());
//                    expressions.add(cb.or(pbcSyncStatus, eccsSyncStatus));
                } else if ("dbllb".equalsIgnoreCase(code)) {
                    /*Predicate status = cb.and(cb.equal(root.get("status"), BillStatus.WAITING_SUPPLEMENT));

                    Predicate pbcSyncStatus = cb.equal(root.get("pbcSyncStatus"), accountBillsAllInfo.getPbcSyncStatus());
                    Predicate eccsSyncStatus = cb.equal(root.get("eccsSyncStatus"), accountBillsAllInfo.getEccsSyncStatus());

                    Predicate or = cb.or(status, pbcSyncStatus, eccsSyncStatus);
                    predicate = cb.and(predicate, or);*/

                    Predicate status = cb.and(cb.equal(root.get("status"), BillStatus.WAITING_SUPPLEMENT));

                    Predicate pbcSyncStatus = cb.equal(root.get("pbcSyncStatus"), accountBillsAllInfo.getPbcSyncStatus());
                    Predicate eccsSyncStatus = cb.equal(root.get("eccsSyncStatus"), accountBillsAllInfo.getEccsSyncStatus());

//                    Predicate dbl = cb.and(status, cb.or(pbcSyncStatus, eccsSyncStatus));
                    Predicate dbl = cb.or(status, pbcSyncStatus, eccsSyncStatus);

                    //加入待上报的数据
                    Predicate dsb = cb.and(cb.equal(root.get("status"), BillStatus.APPROVED));
                    Predicate dsbPbc = cb.equal(root.get("pbcSyncStatus"), CompanySyncStatus.weiTongBu);
                    Predicate dsbEccs = cb.equal(root.get("eccsSyncStatus"), CompanySyncStatus.weiTongBu);
                    dsb = cb.and(dsb, cb.or(dsbPbc, dsbEccs));

                    Predicate sdb_dbl = cb.or(dsb, dbl);

                    Predicate whiteList = null;
                    if(CollectionUtils.isNotEmpty(accountBillsAllInfo.getWhiteLists())){
                        String whiteList1 = accountBillsAllInfo.getWhiteLists().get(0);
                        if("1".equals(whiteList1)){
                            whiteList = cb.and(cb.equal(root.get("whiteList"), whiteList1));
                            predicate = cb.and(predicate, sdb_dbl,whiteList);
                        }else{
                            Predicate whiteList2 = cb.equal(root.get("whiteList"), "0");
                            Predicate whiteList3 = cb.equal(root.get("whiteList"), "");
                            Predicate whiteList4 = cb.isNull(root.get("whiteList"));
                            predicate = cb.and(predicate, sdb_dbl,cb.or(whiteList2, whiteList3,whiteList4));
                        }
                    }else {
                        predicate = cb.and(predicate, sdb_dbl);
                    }
                }

                return predicate;
            }
        };

        Long count = accountBillsAllDao.count(specification);
        billAllCountsList.add(String.valueOf(count));

        return billAllCountsList;
    }

    @Override
    public List<String> getBillsCounts() {
        List<String> list = new ArrayList<>();
        AccountBillsAllSearchInfo accountBillsAllInfo = null;

        accountBillsAllInfo = new AccountBillsAllSearchInfo();

        //账户申请
        setCondition("zhsqCheck", accountBillsAllInfo);
        Long zhsqCount = accountBillsAllDao.count(new AccountBillsAllSpec(accountBillsAllInfo));
        list.add(String.valueOf(zhsqCount));

        //报备成功
        accountBillsAllInfo = new AccountBillsAllSearchInfo();
        setCondition("cgsblb", accountBillsAllInfo);
        Long cgsblbCount = accountBillsAllDao.count(new AccountBillsAllSpec(accountBillsAllInfo));
        list.add(String.valueOf(cgsblbCount));

        //待审核
        accountBillsAllInfo = new AccountBillsAllSearchInfo();
        setCondition("zhshCheck", accountBillsAllInfo);
        Long zhshCheckCount = accountBillsAllDao.count(new AccountBillsAllSpec(accountBillsAllInfo));
        list.add(String.valueOf(zhshCheckCount));

        //核准通过
        accountBillsAllInfo = new AccountBillsAllSearchInfo();
        setCondition("zhhztgCheck", accountBillsAllInfo);
        Long zhhztgCheckCount = accountBillsAllDao.count(new AccountBillsAllSpec(accountBillsAllInfo));
        list.add(String.valueOf(zhhztgCheckCount));

        //待上报
        accountBillsAllInfo = new AccountBillsAllSearchInfo();
        setCondition("dsbCheck", accountBillsAllInfo);
//        Long dsbCheckCheckCount = accountBillsAllDao.count(new AccountBillsAllSpec(accountBillsAllInfo));
        addBillCounts("dsbCheck", accountBillsAllInfo, list);
//        list.add(String.valueOf(dsbCheckCheckCount));

        //待补录
        accountBillsAllInfo = new AccountBillsAllSearchInfo();
        setCondition("dbllb", accountBillsAllInfo);
//        Long dbllbCheckCount = accountBillsAllDao.count(new AccountBillsAllSpec(accountBillsAllInfo));
        addBillCounts("dbllb", accountBillsAllInfo, list);
//        list.add(String.valueOf(dbllbCheckCount));

        //存量账户
        Long clzhCount = accountsAllDao.countByString003AndOrganFullIdLike("1", SecurityUtils.getCurrentOrgFullId());
        list.add(String.valueOf(clzhCount));

        return list;
    }

    @Override
    public List<String> getBillsCounts(String whiteList) {

        List<String> whiteLists = new ArrayList<String>();
        //增加白名单的查询条件
        if(StringUtils.isEmpty(whiteList) || "0".equals(whiteList)){
            //正常账户的查询条件   0  或者  null
            whiteLists.add("0");
            whiteLists.add(null);
            whiteLists.add("");
        }else{
            //白名单账户的查询条件
            whiteLists.add("1");
        }

        List<String> list = new ArrayList<>();

        AccountBillsAllSearchInfo accountBillsAllInfo = new AccountBillsAllSearchInfo();
        accountBillsAllInfo.setWhiteLists(whiteLists);
        //账户申请
        setCondition("zhsqCheck", accountBillsAllInfo);
        Long zhsqCount = accountBillsAllDao.count(new AccountBillsAllSpec(accountBillsAllInfo));
        list.add(String.valueOf(zhsqCount));

        //报备成功
        accountBillsAllInfo = new AccountBillsAllSearchInfo();
        accountBillsAllInfo.setWhiteLists(whiteLists);
        setCondition("cgsblb", accountBillsAllInfo);
        Long cgsblbCount = accountBillsAllDao.count(new AccountBillsAllSpec(accountBillsAllInfo));
        list.add(String.valueOf(cgsblbCount));

        //待审核
        accountBillsAllInfo = new AccountBillsAllSearchInfo();
        accountBillsAllInfo.setWhiteLists(whiteLists);
        setCondition("zhshCheck", accountBillsAllInfo);
        Long zhshCheckCount = accountBillsAllDao.count(new AccountBillsAllSpec(accountBillsAllInfo));
        list.add(String.valueOf(zhshCheckCount));

        //核准通过
        accountBillsAllInfo = new AccountBillsAllSearchInfo();
        accountBillsAllInfo.setWhiteLists(whiteLists);
        setCondition("zhhztgCheck", accountBillsAllInfo);
        Long zhhztgCheckCount = accountBillsAllDao.count(new AccountBillsAllSpec(accountBillsAllInfo));
        list.add(String.valueOf(zhhztgCheckCount));

        //待上报
        accountBillsAllInfo = new AccountBillsAllSearchInfo();
        accountBillsAllInfo.setWhiteLists(whiteLists);
        setCondition("dsbCheck", accountBillsAllInfo);
//        Long dsbCheckCheckCount = accountBillsAllDao.count(new AccountBillsAllSpec(accountBillsAllInfo));
        addBillCounts("dsbCheck", accountBillsAllInfo, list);
//        list.add(String.valueOf(dsbCheckCheckCount));

        //待补录
        accountBillsAllInfo = new AccountBillsAllSearchInfo();
        accountBillsAllInfo.setWhiteLists(whiteLists);
        setCondition("dbllb", accountBillsAllInfo);
//        Long dbllbCheckCount = accountBillsAllDao.count(new AccountBillsAllSpec(accountBillsAllInfo));
        addBillCounts("dbllb", accountBillsAllInfo, list);
//        list.add(String.valueOf(dbllbCheckCount));

        //存量账户
        Long clzhCount = accountsAllDao.countByString003AndOrganFullIdLike("1", SecurityUtils.getCurrentOrgFullId() + "%");
        list.add(String.valueOf(clzhCount));

        return list;
    }


    /**
     * 取消YD_PUBLIC_BILLS_ALL_V视图
     * 已经废弃，使用getBillsCounts()代替
     *
     * @return
     */
    @Override
    @Deprecated
    public String getCounts() {
        AllBillsPublicDTO info = new AllBillsPublicDTO();
        StringBuffer str = new StringBuffer();
        SqlBuilder sqlBuilder;
        List<String> statuses = new ArrayList<>();
        List<String> pbcSyncStatuses = null;
        List<String> eccsSyncStatuses = null;
        Long zhsqCount = 0L;
        Long cgsbount = 0L;
        Long zhshCount = 0L;
        Long zhhztgCount = 0L;
        Long dblCount = 0L;
        Long dsbCount = 0L;

        info.setOrganFullId(SecurityUtils.getCurrentOrgFullId());

        //账户申请
        sqlBuilder = new SqlBuilder();
        sqlBuilder.select("*").from("YD_PUBLIC_BILLS_ALL_V");
        sqlBuilder.startsWith("yd_organ_full_id", BeanValueUtils.getValue(info, "organFullId"));
        statuses.add("NEW");
        statuses.add("APPROVING");
        statuses.add("REJECT");
        sqlBuilder.in("yd_status", statuses);
        zhsqCount = allBillsPublicDao.getCount(sqlBuilder.toSQLString(), null);
        str.append(zhsqCount);
        str.append(",");

        //报备成功
        sqlBuilder = new SqlBuilder();
        sqlBuilder.select("*").from("YD_PUBLIC_BILLS_ALL_V");
        sqlBuilder.startsWith("yd_organ_full_id", BeanValueUtils.getValue(info, "organFullId"));
        sqlBuilder.eq("yd_status", "APPROVED");
        sqlBuilder.eq("yd_pbc_sync_status", "tongBuChengGong");
        eccsSyncStatuses = new ArrayList<>();
        eccsSyncStatuses.add("buTongBu");
        eccsSyncStatuses.add("tongBuChengGong");
        sqlBuilder.in("yd_eccs_sync_status", eccsSyncStatuses);
        cgsbount = allBillsPublicDao.getCount(sqlBuilder.toSQLString(), null);
        str.append(cgsbount);
        str.append(",");

        //待审核
        sqlBuilder = new SqlBuilder();
        sqlBuilder.select("*").from("YD_PUBLIC_BILLS_ALL_V");
        sqlBuilder.startsWith("yd_organ_full_id", BeanValueUtils.getValue(info, "organFullId"));
        sqlBuilder.eq("yd_status", "APPROVING");
        zhshCount = allBillsPublicDao.getCount(sqlBuilder.toSQLString(), null);
        str.append(zhshCount);
        str.append(",");

        //待上报
        sqlBuilder = new SqlBuilder();
        sqlBuilder.select("*").from("YD_PUBLIC_BILLS_ALL_V");
        sqlBuilder.startsWith("yd_organ_full_id", BeanValueUtils.getValue(info, "organFullId"));
        sqlBuilder.eq("yd_status", "APPROVED");
        sqlBuilder.eqAdd("yd_pbc_sync_status", "weiTongBu");
        sqlBuilder.orEquals("yd_eccs_sync_status", "weiTongBu");
        sqlBuilder.add(")");
        dsbCount = allBillsPublicDao.getCount(sqlBuilder.toSQLString(), null);
        str.append(dsbCount);
        str.append(",");

        //核准通过
//        sqlBuilder = new SqlBuilder();
//        sqlBuilder.select("*").from("YD_PUBLIC_BILLS_ALL_V");
//        sqlBuilder.startsWith("yd_organ_full_id", BeanValueUtils.getValue(info,   1   ·"organFullId"));
//        sqlBuilder.eq("yd_pbc_check_status", "CheckPass");
//        sqlBuilder.ne("yd_acct_type", "yiban");
//        sqlBuilder.ne("yd_acct_type", "feiyusuan");
//        zhhztgCount = allBillsPublicDao.getCount(sqlBuilder.toSQLString(), null);
//        str.append(zhhztgCount);


        // 待补录列表
        sqlBuilder = new SqlBuilder();
        //        sqlBuilder.eq("YD_FROMSOURCE", "CORE");
        sqlBuilder.select("*").from("YD_PUBLIC_BILLS_ALL_V");
        sqlBuilder.startsWith("yd_organ_full_id", BeanValueUtils.getValue(info, "organFullId"));
        sqlBuilder.eqAdd("yd_status", "WAITING_SUPPLEMENT");
        sqlBuilder.orEq("yd_status", "APPROVED");
        sqlBuilder.eqAdd("yd_eccs_sync_status", "tongBuShiBai");
        sqlBuilder.orEquals("yd_pbc_sync_status", "tongBuShiBai");
        sqlBuilder.add(")");
        sqlBuilder.add(")");
        sqlBuilder.add(")");
        dblCount = allBillsPublicDao.getCount(sqlBuilder.toSQLString(), null);
        str.append(dblCount);

        return str.toString();
    }

    /**
     * 开启新事务更新流水状态
     *
     * @param billsPublic
     * @param status
     * @param userId
     * @param description
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateApproveStatus(AllBillsPublicDTO billsPublic, BillStatus status, Long userId, String description) {
        if (billsPublic != null) {

            try {
                accountBillsAllService.updateBillStatus(billsPublic.getId(), status, userId, description);
                log.info("1.更新流水审核状态:" + billsPublic.getBillNo() + "为" + status);

                if (StringUtils.isNotBlank(billsPublic.getPreOpenAcctId()) && billsPublic.getBillType() == BillType.ACCT_OPEN && status == BillStatus.APPROVED && !applyNewRuleFlag) {
                    allBillsPublicService.sendModifyStatus(billsPublic, "BANK_SUCCESS");
                }

                // 如果单据被驳回,变更单要将原账户信息恢复，被驳回的信息放到历史记录
                if (status == BillStatus.REJECT) {
                    //流水信息改为新建
                    status = BillStatus.WAITING_SUPPLEMENT;
                    // 更新单据状态
                    accountBillsAllService.updateBillStatus(billsPublic.getId(), status, userId, description);
                    if (billsPublic.getBillType() == BillType.ACCT_CHANGE) {
//                        //找出原变更之前的数据
//                        AllBillsPublicDTO oldBillsPublicDTO = allBillsPublicService.changeCompareWithOriginal(billsPublic);
//
//                        //如果驳回，还原账户信息表  accountsAll accountPublic  客户中间表进行删除   删除变更字段表
//                        //流水ID
//                        Long billId = billsPublic.getId();
//
//                        //删除客户中间表   流水未完成，不会变更客户主表信息  即删除客户中间表即可
//                        CustomerPublicMidInfo customerPublicMidInfo = customerPublicMidService.getByBillId(billId);
//                        if(customerPublicMidInfo != null){
//                            customerPublicMidService.delete(customerPublicMidInfo.getId());
//                        }
//
//                        String[] allIgnoreProperties = {"id"};
//                        //覆盖账户信息表
//                        Long accountsId = billsPublic.getAccountId();
//                        if(accountsId != null){
//                            AccountsAllInfo accountsAllInfo = accountsAllService.getOne(accountsId);
//                            AccountPublicInfo accountPublicInfo = accountPublicService.findByAccountId(accountsId);
//                            if(accountsAllInfo != null && accountPublicInfo != null){
//                                BeanUtils.copyProperties(oldBillsPublicDTO, accountsAllInfo,allIgnoreProperties);
//                                BeanUtils.copyProperties(oldBillsPublicDTO, accountPublicInfo,allIgnoreProperties);
//                                accountsAllService.save(accountsAllInfo);
//                                accountPublicService.save(accountPublicInfo);
//                            }
//                        }
//
//                        //删除变更记录表
//                        AccountChangeSummary accountChangeSummary = accountChangeSummaryDao.findByRefBillId(billId);
//                        if(accountChangeSummary != null){
//                            List<AccountChangeItem> itemList = accountChangeItemDao.findByChangeSummaryId(accountChangeSummary.getId());
//                            if(CollectionUtils.isNotEmpty(itemList)){
//                                for(AccountChangeItem accountChangeItem : itemList){
//                                    accountChangeItemDao.delete(accountChangeItem);
//                                }
//                            }
//                        }
                    }
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                throw new BizServiceException(EErrorCode.BILL_UPDATE_APPROVESTATUS_ERROR, "更新审核状态失败！");

            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Map<String, Object> synchronizeData(Map<String, String> formData, AllBillsPublicDTO billsPublic) {
        Map<String, Object> resultMap = new HashMap<>();
        Long userId = SecurityUtils.getCurrentUserId();
        Boolean isSyncAms = StringUtils.equals(formData.get("isSyncAms"), "true");
        Boolean isSyncEccs = StringUtils.equals(formData.get("isSyncEccs"), "true");
        UserDto userDto;
        if(userId!=null){
            userDto = userService.findById(userId);
        }else{
            userDto = userService.findVirtualUser();
        }
        //是否需要结束最终流水
        Boolean isNeedUpdateFinalStatus = false;
        try {
            isNeedUpdateFinalStatus = allBillsPublicService.syncAndUpdateStaus2(isSyncAms, isSyncEccs, billsPublic, userDto, CompanySyncOperateType.personSyncType);
        } catch (Exception e) {
            log.error("上报人行或者机构信用代码系统异常",e);
            resultMap.put("submitResult", "fail");
            resultMap.put("submitMsg", e.getMessage());
            return resultMap;
        }

        try {
            if (isNeedUpdateFinalStatus) {
                updateFinalStatusLock(billsPublic, userDto);
            }
        } catch (Exception e) {
            log.error("上报成功，结束流水异常,数据进入待补录", e);
            resultMap.put("submitResult", "fail");
            resultMap.put("submitMsg", "上报成功，结束流水异常：" + e.getMessage() + ",数据进入待补录,可直接点击【报备成功】处理");
            allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
            return resultMap;
        }

        resultMap.put("submitResult", "success");
        resultMap.put("allAccountData", billsPublic.getAllAccountData());
        return resultMap;
    }

    private void updateFinalStatusLock(AllBillsPublicDTO billsPublic, UserDto userDto) throws Exception {
        final AllBillsPublicDTO allBillsPublicDTO = billsPublic;
        final UserDto userDto2= userDto;
        try {
            transactionUtils.executeInNewTransaction(new TransactionCallback() {
                @Override
                public void execute() throws Exception {
                    allBillsPublicService.updateFinalStatus(allBillsPublicDTO, userDto2.getId());
                }
            });

        } catch (Exception e) {
            if (e instanceof ObjectOptimisticLockingFailureException) {
                log.info("第一次乐观锁异常："+e.getMessage());
                try {
                    transactionUtils.executeInNewTransaction(new TransactionCallback() {
                        @Override
                        public void execute() throws Exception {
                            allBillsPublicService.updateFinalStatus(allBillsPublicDTO, userDto2.getId());
                        }
                    });
                }catch (Exception e2){
                    if (e instanceof ObjectOptimisticLockingFailureException) {
                        log.info("第二次乐观锁异常："+e.getMessage());
                        transactionUtils.executeInNewTransaction(new TransactionCallback() {
                            @Override
                            public void execute() throws Exception {
                                allBillsPublicService.updateFinalStatus(allBillsPublicDTO, userDto2.getId());
                            }
                        });
                    }else {
                        log.error("其他异常：" + e.getMessage());
                        throw new BizServiceException(EErrorCode.BILL_UPDATE_FINALSTATUS_ERROR, e.getMessage());
                    }
                }
            } else {
                log.error("其他异常：" + e.getMessage());
                throw new BizServiceException(EErrorCode.BILL_UPDATE_FINALSTATUS_ERROR, e.getMessage());
            }
        }
    }

    @Override
    public AllBillsPublicDTO submitRecord(Long userId, Map<String, String> formData, String recordType) {
        UserDto userInfo = userService.findById(userId);
        AllPublicAccountFormProcessor formProcessor = formProcessorMap.get(FormUtils.getComponentName(formData.get("acctType"), formData.get("billType")));
        if (formProcessor == null) {
            throw new RuntimeException("表单处理器" + FormUtils.getComponentName(formData.get("acctType"), formData.get("billType")) + "不存在");
        }

        AllBillsPublicDTO dataAccount;
        AcctCheckNullUtil checkNullUtil = new AcctCheckNullUtil();
        try {
            dataAccount = (AllBillsPublicDTO) Map2DomainUtils.converter(formData, AllBillsPublicDTO.class);
            if ("recordSaveForm".equals(recordType)) {// 补录保存
                // 基本户判断是否补录完成
                if ("jiben".equals(dataAccount.getAcctType().toString())) {
                    // 校验是否补录完成
                    AllBillsPublicDTO dto = checkNullUtil.jibenCheck(dataAccount);
                    // 1代表还需补录 ；0 代表补录完成
                    if ("1".equals(dto.getInitFullStatus())) {
                        formData.put("initFullStatus", "1");
                    }
                    if ("0".equals(dto.getInitFullStatus())) {
                        formData.put("initFullStatus", "2");
                    }
                }
                // 一般户判断是否补录完成
                if ("yiban".equals(dataAccount.getAcctType().toString())) {
                    // 校验是否补录完成
                    AllBillsPublicDTO dto = checkNullUtil.yibanCheck(dataAccount);
                    // 1代表还需补录 ；0 代表补录完成
                    if ("1".equals(dto.getInitFullStatus())) {
                        formData.put("initFullStatus", "1");
                    }
                    if ("0".equals(dto.getInitFullStatus())) {
                        formData.put("initFullStatus", "2");
                    }
                }
                // 预算户判断是否补录完成
                if ("yusuan".equals(dataAccount.getAcctType().toString())) {
                    // 校验是否补录完成
                    AllBillsPublicDTO dto = checkNullUtil.yusuanCheck(dataAccount);
                    // 1代表还需补录 ；0 代表补录完成
                    if ("1".equals(dto.getInitFullStatus())) {
                        formData.put("initFullStatus", "1");
                    }
                    if ("0".equals(dto.getInitFullStatus())) {
                        formData.put("initFullStatus", "2");
                    }
                }
                // 非预算户判断是否补录完成
                if ("feiyusuan".equals(dataAccount.getAcctType().toString())) {
                    // 校验是否补录完成
                    AllBillsPublicDTO dto = checkNullUtil.feiyusuanCheck(dataAccount);
                    // 1代表还需补录 ；0 代表补录完成
                    if ("1".equals(dto.getInitFullStatus())) {
                        formData.put("initFullStatus", "1");
                    }
                    if ("0".equals(dto.getInitFullStatus())) {
                        formData.put("initFullStatus", "2");
                    }
                }
                // 临时户判断是否补录完成
                if ("linshi".equals(dataAccount.getAcctType().toString())) {
                    // 校验是否补录完成
                    AllBillsPublicDTO dto = checkNullUtil.linshiCheck(dataAccount);
                    // 1代表还需补录 ；0 代表补录完成
                    if ("1".equals(dto.getInitFullStatus())) {
                        formData.put("initFullStatus", "1");
                    }
                    if ("0".equals(dto.getInitFullStatus())) {
                        formData.put("initFullStatus", "2");
                    }
                }
                // 非临时户判断是否补录完成
                if ("feilinshi".equals(dataAccount.getAcctType().toString())) {
                    // 校验是否补录完成
                    AllBillsPublicDTO dto = checkNullUtil.feilinshiCheck(dataAccount);
                    // 1代表还需补录 ；0 代表补录完成
                    if ("1".equals(dto.getInitFullStatus())) {
                        formData.put("initFullStatus", "1");
                    }
                    if ("0".equals(dto.getInitFullStatus())) {
                        formData.put("initFullStatus", "2");
                    }
                }
                // 特殊户判断是否补录完成
                if ("teshu".equals(dataAccount.getAcctType().toString())) {
                    // 校验是否补录完成
                    AllBillsPublicDTO dto = checkNullUtil.teshuCheck(dataAccount);
                    // 1代表还需补录 ；0 代表补录完成
                    if ("1".equals(dto.getInitFullStatus())) {
                        formData.put("initFullStatus", "1");
                    }
                    if ("0".equals(dto.getInitFullStatus())) {
                        formData.put("initFullStatus", "2");
                    }
                }

            }

            if ("recordSubmitForm".equals(recordType)) {// 补录提交
                formData.put("initFullStatus", "2");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return formProcessor.process(userInfo, formData);
    }

    /**
     * @param isSyncAms       是否需要上报人行
     * @param isSyncEccs      是否需要上报信用代码证
     * @param billsPublic     上报数据
     * @param userDto         操作人员
     * @param syncOperateType 账户报备方式
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = BizServiceException.class)
    public void syncAndUpdateStaus(Boolean isSyncAms, Boolean isSyncEccs, AllBillsPublicDTO billsPublic, UserDto userDto, CompanySyncOperateType syncOperateType) throws Exception {
        Boolean pbcSuccess = false;
        Boolean eccsSuccess = false;
        ResultDto resultDto = null;

        //是否上报失败
        boolean isPbcSyncFail = false;
        boolean isEccsSyncFail = false;

        if(billsPublic.getCancelHeZhun()==null){
            setCancelHeZhun(billsPublic);
        }
        //开变销进行取消核准字段是否有值的判断   久悬不进行判断
        if(billsPublic.getBillType() == BillType.ACCT_OPEN || billsPublic.getBillType() == BillType.ACCT_CHANGE || billsPublic.getBillType() == BillType.ACCT_REVOKE){
            if((billsPublic.getAcctType() == CompanyAcctType.jiben || billsPublic.getAcctType() == CompanyAcctType.feilinshi) && billsPublic.getCancelHeZhun()==null){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "取消核准标识不能为空！ 数据进入待补录。");
            }
        }
        //上报前开户和变更操作类型开放接口
        if (billsPublic.getBillType() == BillType.ACCT_OPEN) {
            resultDto = coreOpenSyncService.getCoreOpenSyncResult(billsPublic);
            updateCoreSyncStatus(resultDto, billsPublic);
        }

        if (billsPublic.getBillType() == BillType.ACCT_CHANGE) {
            resultDto = coreChangeSyncService.getCoreChangeSyncResult(billsPublic);
            updateCoreSyncStatus(resultDto, billsPublic);
        }

        //如果是取消核准的账户上报前判断日期是否是当前的日期，不是当前的日期进入待补修改日期后再次上报
        if(cancelHezhunCheckDate){
            if(billsPublic.getCancelHeZhun() != null  && billsPublic.getCancelHeZhun() && billsPublic.getBillType() == BillType.ACCT_OPEN){
                if(!StringUtils.equals(billsPublic.getAcctCreateDate(),DateUtils.DateToStr(new Date(),"yyyy-MM-dd"))){
                    updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "取消核准账户上报日期不是当天日期，请修改！");
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "取消核准账户日期不是当天日期，请修改！ 数据进入待补录。");
                }
            }
        }

        //上报前查看是否是大类  大类根据账号去人行查询小类并保存  基本户  一般户去处理
        //根据机构获取到人行上报用户名密码
//        PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullId(billsPublic.getOrganFullId(), EAccountType.AMS);
        PbcAccountDto pbcAccountDto = null;
        if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()){
            pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullIdByCancelHeZhun(billsPublic.getOrganFullId(), EAccountType.AMS);
        }else{
            pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullId(billsPublic.getOrganFullId(), EAccountType.AMS);
        }
        if (pbcAccountDto == null) {
//            updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
            throw new BizServiceException(EErrorCode.ORGAN_NOTCONFIG, "该机构未配置人行用户名密码或用户名密码不正确");
        }
        switchAcctType(pbcAccountDto, billsPublic, userDto);

        //设置账户名称
        setAcctName(billsPublic);

        //校验必须字段
        if (billsPublic.getAcctType() == null || !billsPublic.getAcctType().isPbcAcctType()) {
            updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
            throw new BizServiceException(EErrorCode.PBC_SYNC_ACCTTYPE_ERROR, "该账户性质无法上报");
        }
        String errorMessagePbc = "";
        String errorMessageEccs = "";
        //是否是白名单企业，白名单企业人行，机构信用不上报，内部流程
//        WhiteListDto dto = whiteListService.getByEntName(billsPublic.getDepositorName());
//        if(dto != null){
//            //白名单企业不上报人行，机构信用
//            billsPublic.setWhiteList("1");
//            billsPublic.setPbcCheckStatus(CompanyAmsCheckStatus.NoCheck);
//            //更改状态为审核完成
//            billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
//            billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
//
//            if (billsPublic.getStatus() != BillStatus.APPROVED) {
//                updateApproveStatus(billsPublic, BillStatus.APPROVED, userDto.getId(), "");
//            }
//            //更新上报状态
//            accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.PBC, CompanySyncStatus.buTongBu, syncOperateType, "", userDto.getId());
//            accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.ECCS, CompanySyncStatus.buTongBu, syncOperateType, "", userDto.getId());
//            updateFinalStatus(billsPublic, userDto.getId());
//        }else{

        billsPublic.setWhiteList("");
        //是否需要上报
        if (isSyncAms && billsPublic.getPbcSyncStatus() != CompanySyncStatus.tongBuChengGong) {
            //上报
            try {
                //同步人行
                pbcAmsService.amsAccountSync(billsPublic);
                pbcSuccess = true;
                isPbcSyncFail = false;
            } catch (Exception e) {
                //上报失败不直接返回，只是状态改为false
                log.error("上报人行失败:" + e.getMessage(), e);
                errorMessagePbc = "上报人行失败:" + e.getMessage();
                pbcSuccess = false;
                isPbcSyncFail = true;
            }
            //更改状态为审核完成
//            if (billsPublic.getStatus() != BillStatus.APPROVED) {
//                updateApproveStatus(billsPublic, BillStatus.APPROVED, userDto.getId(), "");
//            }
            CompanySyncStatus companySyncStatus;
            //更新人行上报状态
            if (pbcSuccess) {
                billsPublic.setPbcSyncStatus(CompanySyncStatus.tongBuChengGong);
                companySyncStatus = CompanySyncStatus.tongBuChengGong;
            } else {
                billsPublic.setPbcSyncStatus(CompanySyncStatus.tongBuShiBai);
                companySyncStatus = CompanySyncStatus.tongBuShiBai;
            }
            syncHistoryService.write(billsPublic,EAccountType.AMS,companySyncStatus,errorMessagePbc);

        }
        if (isSyncEccs && billsPublic.getEccsSyncStatus() != CompanySyncStatus.tongBuChengGong) {
            //上报信用代码证系统
            try {
                //账户性质不为空默认直接成功，在上报中异常体现失败，其余都是成功
                if (billsPublic.getAcctType() == CompanyAcctType.jiben) {
                    //开户变更销户需要上报信用代码
                    if (billsPublic.getBillType() == BillType.ACCT_OPEN || billsPublic.getBillType() == BillType.ACCT_CHANGE) {
                        pbcEccsService.eccsAccountSync(billsPublic);
                        eccsSuccess = true;
                        isEccsSyncFail = false;
                    }
                }
            } catch (Exception e) {
                log.error("上报信用代码失败:{}", e.getMessage(), e);
                errorMessageEccs = "上报信用代码失败:" + e.getMessage();
                eccsSuccess = false;
                isEccsSyncFail = true;
            }
            CompanySyncStatus companySyncStatus;
            if (eccsSuccess) {
                billsPublic.setEccsSyncStatus(CompanySyncStatus.tongBuChengGong);
                companySyncStatus = CompanySyncStatus.tongBuChengGong;
            } else {
                billsPublic.setEccsSyncStatus(CompanySyncStatus.tongBuShiBai);
                companySyncStatus = CompanySyncStatus.tongBuShiBai;
            }
            syncHistoryService.write(billsPublic,EAccountType.ECCS,companySyncStatus,errorMessageEccs);
        }
        if(changeToSaveAgain){
            if(billsPublic.getAccountId()!= null && billsPublic.getAcctId() == null){
                billsPublic.setAcctId(billsPublic.getAccountId());
            }
            if(StringUtils.isBlank(billsPublic.getAcctName())){
                setAcctName(billsPublic);
            }
            save(billsPublic,userDto,true);
        }
        //更改状态为审核完成
        if (billsPublic.getStatus() != BillStatus.APPROVED) {
            updateApproveStatus(billsPublic, BillStatus.APPROVED, userDto.getId(), "");
        }

        //更新上报状态
        accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.PBC, billsPublic.getPbcSyncStatus(), syncOperateType, "", userDto.getId());
        accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.ECCS, billsPublic.getEccsSyncStatus(), syncOperateType, "", userDto.getId());
        //修改存款人密码，编号和openkey
        accountBillsAllService.updateSelectPwd(billsPublic);

        //根据上报状态来判断是否更新最终态
        if (billsPublic.getAcctType().isHeZhun() && (billsPublic.getBillType() == BillType.ACCT_OPEN || billsPublic.getBillType() == BillType.ACCT_CHANGE || billsPublic.getBillType() == BillType.ACCT_EXTENSION || billsPublic.getBillType() == BillType.ACCT_REVOKE)) {
            //核准类的开户和变更需要经过人行的核准才可以修改完最终态

            //核准类开户或者变更加入此列表
            //取消核准后核准类账户直接最终状态
            if (billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()) {
                if(billsPublic.getAcctType() == CompanyAcctType.jiben || billsPublic.getAcctType() == CompanyAcctType.feilinshi){
                    if ((pbcSuccess || billsPublic.getPbcSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getPbcSyncStatus() == CompanySyncStatus.buTongBu)
                            && (eccsSuccess || billsPublic.getEccsSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getEccsSyncStatus() == CompanySyncStatus.buTongBu)) {
                        allBillsPublicService.updateFinalStatus(billsPublic, userDto.getId());
                    }
                }
            }else if(billsPublic.getBillType() == BillType.ACCT_REVOKE){
                //核准类销户内部完成流程
                allBillsPublicService.updateFinalStatus(billsPublic, userDto.getId());
            } else {
                pbcSyncListService.savePbcSyncList(billsPublic);
            }
            if(pbcSuccess){
                //预约结果返回(开户核准类上报成功)
                if (StringUtils.isNotBlank(billsPublic.getPreOpenAcctId()) && billsPublic.getBillType() == BillType.ACCT_OPEN && !applyNewRuleFlag) {
                    allBillsPublicService.sendModifyStatus(billsPublic, "COMPLETE");
                }
            }
        } else {
            if ((pbcSuccess || billsPublic.getPbcSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getPbcSyncStatus() == CompanySyncStatus.buTongBu)
                    && (eccsSuccess || billsPublic.getEccsSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getEccsSyncStatus() == CompanySyncStatus.buTongBu)) {
                allBillsPublicService.updateFinalStatus(billsPublic, userDto.getId());
            }
        }

        //预约新模式---上报失败时的预约单补录接口 接口发起
        if(applyNewRuleFlag && billsPublic.getBillType() != BillType.ACCT_SUSPEND) {
            applyFinalStatusUpdateExecutor(isPbcSyncFail, pbcSuccess, billsPublic);
        }

        if (isPbcSyncFail || isEccsSyncFail) {
            updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
            if (isPbcSyncFail && isEccsSyncFail && StringUtils.isNotBlank(errorMessagePbc) && StringUtils.isNotBlank(errorMessageEccs)) {
                throw new BizServiceException(EErrorCode.PBC_ECCS_SYNC_FAILURE, errorMessagePbc + "。" + errorMessageEccs);
            } else if (isPbcSyncFail && !isEccsSyncFail && StringUtils.isNotBlank(errorMessagePbc)) {
                throw new BizServiceException(EErrorCode.PBC_SYNC_FAILURE, errorMessagePbc + (isSyncEccs ? "。上报信用代码成功" : ""));
            } else if (!isPbcSyncFail && isEccsSyncFail && StringUtils.isNotBlank(errorMessageEccs)) {
                throw new BizServiceException(EErrorCode.ECCS_SYNC_FAILURE, (isSyncAms ? "上报人行成功。" : "") + errorMessageEccs);
            }
        }
//        }
    }

    /**
     * @param isSyncAms       是否需要上报人行
     * @param isSyncEccs      是否需要上报信用代码证
     * @param billsPublic     上报数据
     * @param userDto         操作人员
     * @param syncOperateType 账户报备方式
     * @return 返回是否需要结束流水
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = BizServiceException.class)
    public Boolean syncAndUpdateStaus2(Boolean isSyncAms, Boolean isSyncEccs, AllBillsPublicDTO billsPublic, UserDto userDto, CompanySyncOperateType syncOperateType) throws Exception {
        Boolean isNeedUpdateFinalStatus = false;

        Boolean pbcSuccess = false;
        Boolean eccsSuccess = false;
        ResultDto resultDto = null;

        //是否上报失败
        boolean isPbcSyncFail = false;
        boolean isEccsSyncFail = false;

        if(billsPublic.getCancelHeZhun()==null){
            setCancelHeZhun(billsPublic);
        }
        //开变销进行取消核准字段是否有值的判断   久悬不进行判断
        if(billsPublic.getBillType() == BillType.ACCT_OPEN || billsPublic.getBillType() == BillType.ACCT_CHANGE || billsPublic.getBillType() == BillType.ACCT_REVOKE){
            if((billsPublic.getAcctType() == CompanyAcctType.jiben || billsPublic.getAcctType() == CompanyAcctType.feilinshi) && billsPublic.getCancelHeZhun()==null){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "取消核准标识不能为空！ 数据进入待补录。");
            }
        }
        //上报前开户和变更操作类型开放接口
        if (billsPublic.getBillType() == BillType.ACCT_OPEN) {
            resultDto = coreOpenSyncService.getCoreOpenSyncResult(billsPublic);
            updateCoreSyncStatus(resultDto, billsPublic);
        }

        if (billsPublic.getBillType() == BillType.ACCT_CHANGE) {
            resultDto = coreChangeSyncService.getCoreChangeSyncResult(billsPublic);
            updateCoreSyncStatus(resultDto, billsPublic);
        }

        //如果是取消核准的账户上报前判断日期是否是当前的日期，不是当前的日期进入待补修改日期后再次上报
        if(cancelHezhunCheckDate){
            if(billsPublic.getCancelHeZhun() != null  && billsPublic.getCancelHeZhun() && billsPublic.getBillType() == BillType.ACCT_OPEN){
                if(!StringUtils.equals(billsPublic.getAcctCreateDate(),DateUtils.DateToStr(new Date(),"yyyy-MM-dd"))){
                    updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "取消核准账户上报日期不是当天日期，请修改！");
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "取消核准账户日期不是当天日期，请修改！ 数据进入待补录。");
                }
            }
        }

        //上报前查看是否是大类  大类根据账号去人行查询小类并保存  基本户  一般户去处理
        //根据机构获取到人行上报用户名密码
//        PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullId(billsPublic.getOrganFullId(), EAccountType.AMS);
        PbcAccountDto pbcAccountDto = null;
        if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()){
            pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullIdByCancelHeZhun(billsPublic.getOrganFullId(), EAccountType.AMS);
        }else{
            pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullId(billsPublic.getOrganFullId(), EAccountType.AMS);
        }
        if (pbcAccountDto == null) {
//            updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
            throw new BizServiceException(EErrorCode.ORGAN_NOTCONFIG, "该机构未配置人行用户名密码或用户名密码不正确");
        }
        switchAcctType(pbcAccountDto, billsPublic, userDto);

        //设置账户名称
        setAcctName(billsPublic);

        //校验必须字段
        if (billsPublic.getAcctType() == null || !billsPublic.getAcctType().isPbcAcctType()) {
            updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
            throw new BizServiceException(EErrorCode.PBC_SYNC_ACCTTYPE_ERROR, "该账户性质无法上报");
        }
        String errorMessagePbc = "";
        String errorMessageEccs = "";
        //是否是白名单企业，白名单企业人行，机构信用不上报，内部流程
//        WhiteListDto dto = whiteListService.getByEntName(billsPublic.getDepositorName());
//        if(dto != null){
//            //白名单企业不上报人行，机构信用
//            billsPublic.setWhiteList("1");
//            billsPublic.setPbcCheckStatus(CompanyAmsCheckStatus.NoCheck);
//            //更改状态为审核完成
//            billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
//            billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
//
//            if (billsPublic.getStatus() != BillStatus.APPROVED) {
//                updateApproveStatus(billsPublic, BillStatus.APPROVED, userDto.getId(), "");
//            }
//            //更新上报状态
//            accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.PBC, CompanySyncStatus.buTongBu, syncOperateType, "", userDto.getId());
//            accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.ECCS, CompanySyncStatus.buTongBu, syncOperateType, "", userDto.getId());
//            updateFinalStatus(billsPublic, userDto.getId());
//        }else{

        billsPublic.setWhiteList("");
        //是否需要上报
        if (isSyncAms && billsPublic.getPbcSyncStatus() != CompanySyncStatus.tongBuChengGong) {
            //上报
            try {
                //同步人行
                pbcAmsService.amsAccountSync(billsPublic);
                pbcSuccess = true;
                isPbcSyncFail = false;
            } catch (Exception e) {
                //上报失败不直接返回，只是状态改为false
                log.error("上报人行失败:" + e.getMessage(), e);
                errorMessagePbc = "上报人行失败:" + e.getMessage();
                pbcSuccess = false;
                isPbcSyncFail = true;
            }
            //更改状态为审核完成
//            if (billsPublic.getStatus() != BillStatus.APPROVED) {
//                updateApproveStatus(billsPublic, BillStatus.APPROVED, userDto.getId(), "");
//            }
            CompanySyncStatus companySyncStatus;
            //更新人行上报状态
            if (pbcSuccess) {
                billsPublic.setPbcSyncStatus(CompanySyncStatus.tongBuChengGong);
                companySyncStatus = CompanySyncStatus.tongBuChengGong;
            } else {
                billsPublic.setPbcSyncStatus(CompanySyncStatus.tongBuShiBai);
                companySyncStatus = CompanySyncStatus.tongBuShiBai;
            }
            syncHistoryService.write(billsPublic,EAccountType.AMS,companySyncStatus,errorMessagePbc);

        }
        if (isSyncEccs && billsPublic.getEccsSyncStatus() != CompanySyncStatus.tongBuChengGong) {
            //上报信用代码证系统
            try {
                //账户性质不为空默认直接成功，在上报中异常体现失败，其余都是成功
                if (billsPublic.getAcctType() == CompanyAcctType.jiben) {
                    //开户变更销户需要上报信用代码
                    if (billsPublic.getBillType() == BillType.ACCT_OPEN || billsPublic.getBillType() == BillType.ACCT_CHANGE) {
                        pbcEccsService.eccsAccountSync(billsPublic);
                        eccsSuccess = true;
                        isEccsSyncFail = false;
                    }
                }
            } catch (Exception e) {
                log.error("上报信用代码失败:{}", e.getMessage(), e);
                errorMessageEccs = "上报信用代码失败:" + e.getMessage();
                eccsSuccess = false;
                isEccsSyncFail = true;
            }
            CompanySyncStatus companySyncStatus;
            if (eccsSuccess) {
                billsPublic.setEccsSyncStatus(CompanySyncStatus.tongBuChengGong);
                companySyncStatus = CompanySyncStatus.tongBuChengGong;
            } else {
                billsPublic.setEccsSyncStatus(CompanySyncStatus.tongBuShiBai);
                companySyncStatus = CompanySyncStatus.tongBuShiBai;
            }
            syncHistoryService.write(billsPublic,EAccountType.ECCS,companySyncStatus,errorMessageEccs);
        }
        if(changeToSaveAgain){
            if(billsPublic.getAccountId()!= null && billsPublic.getAcctId() == null){
                billsPublic.setAcctId(billsPublic.getAccountId());
            }
            if(StringUtils.isBlank(billsPublic.getAcctName())){
                setAcctName(billsPublic);
            }
            save(billsPublic,userDto,true);
        }
        //更改状态为审核完成
        if (billsPublic.getStatus() != BillStatus.APPROVED) {
            updateApproveStatus(billsPublic, BillStatus.APPROVED, userDto.getId(), "");
        }

        //更新上报状态
        accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.PBC, billsPublic.getPbcSyncStatus(), syncOperateType, "", userDto.getId());
        accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.ECCS, billsPublic.getEccsSyncStatus(), syncOperateType, "", userDto.getId());
        //修改存款人密码，编号和openkey
        accountBillsAllService.updateSelectPwd(billsPublic);

        //根据上报状态来判断是否更新最终态
        if (billsPublic.getAcctType().isHeZhun() && (billsPublic.getBillType() == BillType.ACCT_OPEN || billsPublic.getBillType() == BillType.ACCT_CHANGE || billsPublic.getBillType() == BillType.ACCT_EXTENSION || billsPublic.getBillType() == BillType.ACCT_REVOKE)) {
            //核准类的开户和变更需要经过人行的核准才可以修改完最终态

            //核准类开户或者变更加入此列表
            //取消核准后核准类账户直接最终状态
            if (billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()) {
                if(billsPublic.getAcctType() == CompanyAcctType.jiben || billsPublic.getAcctType() == CompanyAcctType.feilinshi){
                    if ((pbcSuccess || billsPublic.getPbcSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getPbcSyncStatus() == CompanySyncStatus.buTongBu)
                            && (eccsSuccess || billsPublic.getEccsSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getEccsSyncStatus() == CompanySyncStatus.buTongBu)) {
                        isNeedUpdateFinalStatus = true;
                    }
                }
            }else if(billsPublic.getBillType() == BillType.ACCT_REVOKE){
                //核准类销户内部完成流程
                isNeedUpdateFinalStatus = true;
            } else {
                pbcSyncListService.savePbcSyncList(billsPublic);
            }
            if(pbcSuccess){
                //预约结果返回(开户核准类上报成功)
                if (StringUtils.isNotBlank(billsPublic.getPreOpenAcctId()) && billsPublic.getBillType() == BillType.ACCT_OPEN && !applyNewRuleFlag) {
                    allBillsPublicService.sendModifyStatus(billsPublic, "COMPLETE");
                }
            }
        } else {
            if ((pbcSuccess || billsPublic.getPbcSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getPbcSyncStatus() == CompanySyncStatus.buTongBu)
                    && (eccsSuccess || billsPublic.getEccsSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getEccsSyncStatus() == CompanySyncStatus.buTongBu)) {
                isNeedUpdateFinalStatus = true;
            }
        }

        //预约新模式---上报失败时的预约单补录接口  页面发起
        if(applyNewRuleFlag && billsPublic.getBillType() != BillType.ACCT_SUSPEND) {
            applyFinalStatusUpdateExecutor(isPbcSyncFail, pbcSuccess, billsPublic);
        }


        if (isPbcSyncFail || isEccsSyncFail) {
            updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
            if (isPbcSyncFail && isEccsSyncFail && StringUtils.isNotBlank(errorMessagePbc) && StringUtils.isNotBlank(errorMessageEccs)) {
                throw new BizServiceException(EErrorCode.PBC_ECCS_SYNC_FAILURE, errorMessagePbc + "。" + errorMessageEccs);
            } else if (isPbcSyncFail && !isEccsSyncFail && StringUtils.isNotBlank(errorMessagePbc)) {
                throw new BizServiceException(EErrorCode.PBC_SYNC_FAILURE, errorMessagePbc + (isSyncEccs ? "。上报信用代码成功" : ""));
            } else if (!isPbcSyncFail && isEccsSyncFail && StringUtils.isNotBlank(errorMessageEccs)) {
                throw new BizServiceException(EErrorCode.ECCS_SYNC_FAILURE, (isSyncAms ? "上报人行成功。" : "") + errorMessageEccs);
            }
        }
//        }

        return isNeedUpdateFinalStatus;
    }



    @Override
    public void initBillsPublic(AllBillsPublicDTO billsPublic, OrganizationDto organizationDto, BillFromSource billFromSource) {
        //来自
        billsPublic.setFromSource(billFromSource);
        BillTypeNo billTypeNo = BillTypeNo.valueOf(billsPublic.getBillType().name());
        //接口默认新增流水
        billsPublic.setBillNo(billNoSeqService.getBillNo(FastDateFormat.getInstance("yyyyMMdd").format(new Date()), organizationDto.getCode(), billTypeNo));
        //设置机构
        billsPublic.setOrganFullId(organizationDto.getFullId());
        billsPublic.setAcctOrgFullid(organizationDto.getFullId());
        billsPublic.setCustOrganFullId(organizationDto.getFullId());
        billsPublic.setBankCode(organizationDto.getPbcCode());
        billsPublic.setBankName(organizationDto.getName());
        //账户性质
        acctBigType2AcctType(billsPublic);
        //经营范围
        cutShortBusinessScope(billsPublic);

    }

    @Override
    public void setSyncStatusByOldBill(AccountBillsAllInfo accountBillsAllInfo, AllBillsPublicDTO billsPublic) {
        billsPublic.setFinalStatus(accountBillsAllInfo.getFinalStatus());
        billsPublic.setStatus(accountBillsAllInfo.getStatus());
        billsPublic.setPbcSyncStatus(accountBillsAllInfo.getPbcSyncStatus());
        billsPublic.setEccsSyncStatus(accountBillsAllInfo.getEccsSyncStatus());
        billsPublic.setInitFullStatus(accountBillsAllInfo.getInitFullStatus());
        billsPublic.setAccountId(accountBillsAllInfo.getAccountId());
        billsPublic.setAcctId(accountBillsAllInfo.getAccountId());
        billsPublic.setRefBillId(accountBillsAllInfo.getId());
        billsPublic.setPbcCheckStatus(accountBillsAllInfo.getPbcCheckStatus());
        billsPublic.setPbcCheckDate(accountBillsAllInfo.getPbcCheckDate());
        //新增取消核准
        if (accountBillsAllInfo.getCancelHeZhun() != null) {
            //基本户上报信用代码证系统无需传存款人类别CancelHeZhun为null  如果原数据有值的情况下用原有值   如果有存款人了类别   用最新判断的
            if(billsPublic.getCancelHeZhun() == null){
                billsPublic.setCancelHeZhun(accountBillsAllInfo.getCancelHeZhun());
            }
        }
        if (accountBillsAllInfo.getOpenAccountSiteType() != null) {
            billsPublic.setOpenAccountSiteType(accountBillsAllInfo.getOpenAccountSiteType());
        }
        if (StringUtils.isNotBlank(accountBillsAllInfo.getSelectPwd())) {
            billsPublic.setSelectPwd(accountBillsAllInfo.getSelectPwd());
        }
        //接口进来数据中有值的情况下用最新数据覆盖原有数据
        //旧数据有值，新数据没情况下用原有值覆盖
        if(StringUtils.isNotBlank(accountBillsAllInfo.getAccountKey()) && StringUtils.isBlank(billsPublic.getAccountKey())){
            log.info("旧数据accountBillsAllInfo中accountKey：{},新数据billsPublic中accountKey为空，用旧数据accountBillsAllInfo中accountKey覆盖！",accountBillsAllInfo.getAccountKey());
            billsPublic.setAccountKey(accountBillsAllInfo.getAccountKey());
        }else{
            //同步成功的情况下，accountKey是最新，或者不变；不进行替换动作
            if(billsPublic.getPbcSyncStatus() != CompanySyncStatus.tongBuChengGong){
                log.info("旧数据accountBillsAllInfo中accountKey：{},新数据billsPublic中accountKey:{}  用billsPublic中accountKey覆盖！",accountBillsAllInfo.getAccountKey(),billsPublic.getAccountKey());
            }
        }
//        if (StringUtils.isNotBlank(accountBillsAllInfo.getAccountKey())) {
//            billsPublic.setAccountKey(accountBillsAllInfo.getAccountKey());
//        }
        if (StringUtils.isNotBlank(accountBillsAllInfo.getOpenKey())) {
            billsPublic.setOpenKey(accountBillsAllInfo.getOpenKey());
        }
        if (StringUtils.isNotBlank(accountBillsAllInfo.getAccountLicenseNo())) {
            billsPublic.setAccountLicenseNo(accountBillsAllInfo.getAccountLicenseNo());
        }
    }


    @Override
    public AllBillsPublicDTO changeCompareWithOriginal(AllBillsPublicDTO billsPublic) {
        log.info("查询历史数据...");
        String acctNo = billsPublic.getAcctNo();
        String customerNo = billsPublic.getCustomerNo();
        String depositorName = billsPublic.getDepositorName();
        CustomerPublicInfo customerPublicInfo = null;
        CustomersAllInfo customersAllInfo = null;
        CustomerPublicLogInfo customerPublicLogInfo = null;
        AccountsAllInfo accountsAllInfo = null;
        AccountPublicInfo accountPublicInfo = null;
        AccountPublicLogInfo accountPublicLogInfo = null;
        AccountBillsAllInfo latestFinishedByAcctNo = null;
        if (StringUtils.isNotBlank(acctNo)) {
            //此处先去流水表中查找该账号的最后一条完成流水，然后根据流水id去accountLog表中拿数据
            latestFinishedByAcctNo = accountBillsAllService.findLatestFinishedByAcctNo(acctNo);
            if (latestFinishedByAcctNo != null) {
                accountPublicLogInfo = accountPublicLogService.findByRefBillId(latestFinishedByAcctNo.getId());
            }

            //LOG表为空则去主表取
            if (accountPublicLogInfo == null) {
                accountPublicInfo = accountPublicService.findByAcctNo(acctNo);
                accountsAllInfo = accountsAllService.findByAcctNo(acctNo);
            }

        } else {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "变更接口的账号为空");
        }
        //客户信息优先根据logId去客户日志表获取数据  log没有数据  去客户表主表获取客户数据
        if(latestFinishedByAcctNo.getCustomerLogId() != null){
            customerPublicLogInfo = customerPublicLogService.getOne(latestFinishedByAcctNo.getCustomerLogId());
        }

        if(customerPublicLogInfo == null){
            if (StringUtils.isNotBlank(customerNo)) {
                customerPublicInfo = customerPublicService.getByCustomerNo(customerNo);
                customersAllInfo = customersAllService.findByCustomerNo(billsPublic.getCustomerNo());
            }
            if ((customerPublicInfo == null || customersAllInfo == null) && StringUtils.isNotBlank(depositorName)) {
                customerPublicInfo = customerPublicService.getByDepositorName(depositorName);
                customersAllInfo = customersAllService.findByDepositorName(depositorName);
            }
            if (customerPublicInfo == null || customersAllInfo == null) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "变更接口的客户号和存款人为空");
            }
        }
        if ((customerPublicLogInfo != null || customerPublicInfo != null) && (accountPublicLogInfo != null || accountPublicInfo != null)) {
            AllBillsPublicDTO allBillsPublicDTO = new AllBillsPublicDTO();
            //log有数据从log赋值
            if (accountPublicLogInfo != null) {
                BeanUtils.copyProperties(accountPublicLogInfo, allBillsPublicDTO);
            } else {
                BeanUtils.copyProperties(accountsAllInfo, allBillsPublicDTO);
                BeanUtils.copyProperties(accountPublicInfo, allBillsPublicDTO);
            }
            if(customerPublicLogInfo != null){
                BeanUtils.copyProperties(customerPublicLogInfo, allBillsPublicDTO);
            }else{
                BeanUtils.copyProperties(customerPublicInfo, allBillsPublicDTO);
                BeanUtils.copyProperties(customersAllInfo, allBillsPublicDTO);
            }
            String[] oldNullStr = CompareUtils.getIsNotNullPropertyNames(billsPublic);
            List<String> ignoreFidlds = new ArrayList<String>();
            ignoreField(ignoreFidlds);
            String[] allIgnoreProperties = (String[]) ArrayUtils.addAll(oldNullStr, ignoreFidlds.toArray(new String[ignoreFidlds.size()]));
            BeanUtils.copyProperties(allBillsPublicDTO, billsPublic, allIgnoreProperties);

            //organCode 赋值
            String oldOrganFullId = "";
            if (accountsAllInfo != null) {
                oldOrganFullId = accountsAllInfo.getOrganFullId();
            } else if (accountPublicLogInfo != null) {
                oldOrganFullId = accountPublicLogInfo.getOrganFullId();
            }

            if (StringUtils.isNotBlank(oldOrganFullId)) {
                OrganizationDto organizationDto = organizationService.findByOrganFullId(oldOrganFullId);
                if (organizationDto != null) {
                    allBillsPublicDTO.setOrganCode(organizationDto.getCode());
                }
            }
            return allBillsPublicDTO;
        } else {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "变更接口无法查询到账户或客户信息");
        }
    }


//    @Override
//    public void changeCompare(AllBillsPublicDTO billsPublic) {
//    	String acctNo = billsPublic.getAcctNo();
//    	String customerNo = billsPublic.getCustomerNo();
//    	String depositorName = billsPublic.getDepositorName();
//    	CustomerPublicInfo customerPublicInfo = null;
//    	CustomersAllInfo customersAllInfo = null;
//    	AccountPublicLogInfo accountPublicLogInfo = null;
//    	if(StringUtils.isNotBlank(acctNo)) {
//    		AccountsAll accountsAll =accountsAllDao.findByAcctNo(acctNo);
//    		if(accountsAll != null) {
//    			accountPublicLogInfo = accountPublicLogService.findByAccountIdBackup(accountsAll.getId());
//    		}
//    	}else {
//    		throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "变更接口的账号为空");
//    	}
//    	if(StringUtils.isNotBlank(customerNo)) {
//    		customerPublicInfo = customerPublicService.getByCustomerNo(customerNo);
//    		customersAllInfo = customersAllService.findByCustomerNo(billsPublic.getCustomerNo());
//    	}else if(StringUtils.isNotBlank(depositorName)) {
//    		customerPublicInfo = customerPublicService.getByDepositorName(depositorName);
//    		customersAllInfo = customersAllService.findByDepositorName(depositorName);
//    	}else {
//    		throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "变更接口的客户号和存款人为空");
//    	}
//    	if(customerPublicInfo != null && accountPublicLogInfo != null) {
//    		changeCompareWithAccount(accountPublicLogInfo,customerPublicInfo,billsPublic,customersAllInfo);
//    	}else {
//    		throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "变更接口无法查询到账户或客户信息");
//    	}
//    }

    @Override
    public Boolean getSyncStatus(EAccountType userType, AllBillsPublicDTO billsPublic) {
        if (userType == EAccountType.AMS) {
            return billsPublic.getPbcSyncStatus() != CompanySyncStatus.buTongBu && billsPublic.getPbcSyncStatus() != CompanySyncStatus.tongBuChengGong;
        } else if (userType == EAccountType.ECCS) {
            return billsPublic.getEccsSyncStatus() != CompanySyncStatus.buTongBu && billsPublic.getEccsSyncStatus() != CompanySyncStatus.tongBuChengGong;
        } else {
            return null;
        }
    }

    @Override
    public void acctBigType2AcctType(AllBillsPublicDTO billsPublic) {
        if (billsPublic != null && billsPublic.getAcctBigType() != null && billsPublic.getAcctType() == null) {
            AcctBigType acctBigType = billsPublic.getAcctBigType();
            CompanyAcctType acctType = billsPublic.getAcctType();
            if (acctBigType == AcctBigType.jiben) {
                acctType = CompanyAcctType.jiben;
            } else if (acctBigType == AcctBigType.yiban) {
                acctType = CompanyAcctType.yiban;
            } else if (acctBigType == AcctBigType.linshi) {
                acctType = CompanyAcctType.tempAcct;
            } else if (acctBigType == AcctBigType.zhuanyong) {
                acctType = CompanyAcctType.specialAcct;
            } else if (acctBigType == AcctBigType.yanzi) {
                acctType = CompanyAcctType.yanzi;
            } else if (acctBigType == AcctBigType.zengzi) {
                acctType = CompanyAcctType.zengzi;
            }
            billsPublic.setAcctType(acctType);
        }
    }

    /**
     * 按字符截取中文加上【等】字
     *
     * @param billsPublic
     */
    private static void cutShortBusinessScope(AllBillsPublicDTO billsPublic) {
        String scope = billsPublic.getBusinessScopeEccs();
        try {
//            String charSet = "UTF-8";
            //人行数据最终以GBK编码传送
            String charSet = "GBK";
            String scope2 = subStr(scope, 390 - ("等".getBytes(charSet).length), charSet);
            if (!StringUtils.equals(scope, scope2)) {
                scope2 = scope2 + "等";
            }
            billsPublic.setBusinessScopeEccs(scope2);
        } catch (UnsupportedEncodingException e) {
            //ignore
            log.error("经营范围（信用代码证）截取异常。",e);
        }
    }

    public static String subStr(String str, int len, String charset) throws UnsupportedEncodingException {
        if (str == null || "".equals(str)) {
            return str;
        }
        byte[] br = new byte[len];
        byte[] bt = str.getBytes(charset);
        if (bt.length < len) {
            return str;
        }
        System.arraycopy(bt, 0, br, 0, len);
        String res = new String(br, charset);
        int resLen = res.length();
        if (str.substring(0, resLen).getBytes(charset).length > len) {
            res = str.substring(0, resLen - 1);
        }
        return res;
    }


    @Override
    public List<String> changeCompareWithOld(AllBillsPublicDTO allBillsPublicDTO, AllBillsPublicDTO billsPublic) {
        log.info("新老数据进行比对...");
        allBillsPublicDTO.setRefBillId(billsPublic.getRefBillId());
        Map<String, String> oldMap = null;
        Map<String, String> newMap = null;
        final Map<String, Object> beforeChangeFieldValueMap = new HashMap<>(16);
        final Map<String, Object> afterChangeFieldValueMap = new HashMap<>(16);
        List<String> sameChangeFieldValueSet = new ArrayList<>(16);
        final List<String> changFieldNameList = new ArrayList<>(16);

        //取消核准增加为标明注册资金的判断
        if(billsPublic.getBillType() == BillType.ACCT_CHANGE && (billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun())){
            //如果未标明注册资金的值选的“是”  则资金币种跟资金两个字段空置空
            if("1".equals(billsPublic.getIsIdentification())){
                billsPublic.setRegCurrencyType("");
                billsPublic.setRegisteredCapital(null);
            }
        }

        try {
            oldMap = org.apache.commons.beanutils.BeanUtils.describe(allBillsPublicDTO);
            newMap = org.apache.commons.beanutils.BeanUtils.describe(billsPublic);

        } catch (Exception e) {
            log.error("对象转化为map失败", e);
        }
        List<String> changeFields = new ArrayList<>(newMap.keySet());
        List<String> ignoreFidlds = new ArrayList<>(16);
        ignoreField(ignoreFidlds);
        changeFields.removeAll(ignoreFidlds);
        CompareUtils.compare(oldMap, newMap, changeFields, beforeChangeFieldValueMap, afterChangeFieldValueMap, sameChangeFieldValueSet, changFieldNameList, new HashMap<String, String>(), false);
        if (changFieldNameList != null && changFieldNameList.size() != 0) {
            //根据账户判断比对字段中是否有其他不是该账户的字段   不是该账户的字段剔除
            List<String> changeAmsSyncFieldList = getChangeFieldsNeedPbcSync(billsPublic);
            List<String> diff = new ArrayList<>();
            for(String changeField : changFieldNameList){
                if(!changeAmsSyncFieldList.contains(changeField)){
                    diff.add(changeField);
                }
            }
            changFieldNameList.removeAll(diff);
            accountChangeSummaryService.saveAccountChangeSummary(changFieldNameList, beforeChangeFieldValueMap, afterChangeFieldValueMap, allBillsPublicDTO);
            boolean pbcResult = CompareUtils.isSyncField(changFieldNameList, changeAmsSyncFieldList);
            boolean eccsResult = false;
            if (billsPublic.getAcctType() == CompanyAcctType.jiben) {
                eccsResult = CompareUtils.isSyncField(changFieldNameList, getChangeFieldsNeedEccsSync(billsPublic));
            }

            //久悬新开或者包含以上字段变更
            boolean beedNewOpen = (allBillsPublicDTO.getAccountStatus() == AccountStatus.revoke && billsPublic.getAccountStatus() == AccountStatus.normal) || isNeedNewOpen(changFieldNameList);

            if ((pbcResult || eccsResult) && !beedNewOpen) {//说明有需要上报的字段
                List<String> mustPbcSync = getChangeFieldMustPbcSync(billsPublic);
                List<String> changedAllList = new ArrayList<String>();
                if (pbcResult) {
                    changedAllList.addAll(changeAmsSyncFieldList);
                }
                if (eccsResult) {
                    List<String> changeAmsSyncEccsFieldList = getChangeFieldsNeedEccsSync(billsPublic);
                    changedAllList.removeAll(changeAmsSyncEccsFieldList);
                    changedAllList.addAll(changeAmsSyncEccsFieldList);
                }
                changFieldNameList.retainAll(changedAllList);
                changeFields.removeAll(changFieldNameList);
                changeFields.removeAll(mustPbcSync);
                AllBillsPublicDTO newBillForPbc = new AllBillsPublicDTO();
                BeanUtils.copyProperties(billsPublic, newBillForPbc, changeFields.toArray(new String[changeFields.size()]));

                CompareUtils.convertToOldValueFromMap(newBillForPbc, allBillsPublicDTO, mustPbcSync);
//                billsPublic = newBillForPbc;
                String[] arryNullFields = BeanValueUtils.getNullPropertyNames(newBillForPbc);
                BeanUtils.copyProperties(newBillForPbc, billsPublic,arryNullFields);
                log.info("变更上报的对象字段显示开始：");
                PrintUtils.printObjectColumn(billsPublic);
                log.info("变更上报的对象字段显示结束：");
                if (!pbcResult) {
                    billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
                }
                if (!eccsResult) {
                    billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
                }
            } else {
                billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
                billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
                billsPublic.setPbcCheckStatus(CompanyAmsCheckStatus.NoCheck);
            }
        }else{
            //没有任何变更字段无需上报
            billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
            billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
            billsPublic.setPbcCheckStatus(CompanyAmsCheckStatus.NoCheck);
        }
        return changFieldNameList;
    }

    /**
     * 判断是否包含需要销户再开的任一字段则返回真
     *
     * @param fields
     * @return
     */
    private boolean isNeedNewOpen(List<String> fields) {
        //一下字段需要先销户再开户，如果包含则直接无需同步
        List<String> needFields = Arrays.asList(new String[]{"depositorType", "regAreaCode"});
        List<String> needNewOpenFields = Arrays.asList(new String[]{"acctType", "bankCode", "depositorType", "acctBigType", "organCode", "regAreaCode"});
        //无存量数据变更判断注册地区代码跟存款人类别变更需要先肖后开
        if (!datenbestand) {
            for (String needNewOpenField : needFields) {
                if (fields.contains(needNewOpenField)) {
                    return true;
                }
            }
        } else {
            //有存量数据的情况下都判断
            for (String needNewOpenField : needNewOpenFields) {
                if (fields.contains(needNewOpenField)) {
                    return true;
                }
            }
        }

        return false;
    }


//    private void changeCompareWithAccount(AccountPublicLogInfo accountPublicLogInfo,CustomerPublicInfo customerPublicInfo,AllBillsPublicDTO billsPublic,CustomersAllInfo customersAllInfo) {
//    	final AllBillsPublicDTO allBillsPublicDTO = new AllBillsPublicDTO();
//    	String[] ignoreProperties = {"refBillId","refCustomerBillId","refCustomerLogId"};
//        BeanUtils.copyProperties(accountPublicLogInfo, allBillsPublicDTO,ignoreProperties);
//        BeanUtils.copyProperties(customerPublicInfo, allBillsPublicDTO,ignoreProperties);
//        BeanUtils.copyProperties(customersAllInfo, allBillsPublicDTO,ignoreProperties);
//        allBillsPublicDTO.setRefBillId(billsPublic.getRefBillId());
//
//        Map<String, String> oldMap = null;
//        Map<String, String> newMap = null;
//        final Map<String, Object> beforeChangeFieldValueMap = new HashMap<>();
//        final Map<String, Object> afterChangeFieldValueMap = new HashMap<>();
//        List<String> sameChangeFieldValueSet = new ArrayList<>();
//        final List<String> changFieldNameList = new ArrayList<>();
//        try {
//            oldMap = org.apache.commons.beanutils.BeanUtils.describe(allBillsPublicDTO);
//            newMap = org.apache.commons.beanutils.BeanUtils.describe(billsPublic);
//
//        } catch (Exception e) {
//            log.error("对象转化为map失败", e);
//        }
//        List<String> changeFields = new ArrayList<>(newMap.keySet());
//        List<String> ignoreFidlds = new ArrayList<String>();
//        ignoreField(ignoreFidlds);
//        changeFields.removeAll(ignoreFidlds);
//        CompareUtils.compare(oldMap, newMap, changeFields, beforeChangeFieldValueMap, afterChangeFieldValueMap, sameChangeFieldValueSet,changFieldNameList, new HashMap<String, String>(),false);
//        if(changFieldNameList != null && changFieldNameList.size() != 0) {
//	    	accountChangeSummaryService.saveAccountChangeSummary(changFieldNameList, beforeChangeFieldValueMap , afterChangeFieldValueMap, allBillsPublicDTO);
//	    	List<String> changeAmsSyncFieldList = getChangeFieldsNeedPbcSync(billsPublic);
//	    	boolean pbcResult = CompareUtils.isSyncField(changFieldNameList,changeAmsSyncFieldList);
//	    	boolean eccsResult = false;
//	    	if (billsPublic.getAcctType() == CompanyAcctType.jiben) {
//	    		eccsResult = CompareUtils.isSyncField(changFieldNameList,getChangeFieldsNeedEccsSync(billsPublic));
//	    	}
//	    	if(pbcResult || eccsResult) {//说明有需要上报的字段
//	    		List<String> mustPbcSync = getChangeFieldMustPbcSync(billsPublic);
//	    		List<String> changedAllList = new ArrayList<String>();
//	    		if(pbcResult) {
//		    		changedAllList.addAll(changeAmsSyncFieldList);
//	    		}
//	    		if (eccsResult) {
//	    			List<String> changeAmsSyncEccsFieldList =  getChangeFieldsNeedEccsSync(billsPublic);
//	    			changedAllList.removeAll(changeAmsSyncEccsFieldList);
//	    			changedAllList.addAll(changeAmsSyncEccsFieldList);
//	    		}
//	    		changFieldNameList.retainAll(changedAllList);
//	    		changeFields.removeAll(changFieldNameList);
//	    		changeFields.removeAll(mustPbcSync);
//	    		AllBillsPublicDTO newBillForPbc = new AllBillsPublicDTO();
//	    		BeanUtils.copyProperties(billsPublic, newBillForPbc, changeFields.toArray(new String[changeFields.size()]));
//
//		    	CompareUtils.convertToOldValueFromMap(newBillForPbc, allBillsPublicDTO, mustPbcSync);
//		    	billsPublic = newBillForPbc;
//		    	log.info("变更上报的对象字段："+billsPublic.toString());
//		    	if(!pbcResult) {
//		    		billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
//		    	}
//		    	if(!eccsResult) {
//		    		billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
//		    	}
//	    	}else {
//	    		billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
//	    		billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
//	    	}
//        }
//    }

    //    private void convertFromMap(AllBillsPublicDTO billsPublic,Set<String> sameChangeFieldValueSet){
//    	try {
//	    	for (String str : sameChangeFieldValueSet) {
//				org.apache.commons.beanutils.BeanUtils.setProperty(billsPublic, str, null);
//			}
//    	} catch (IllegalAccessException | InvocationTargetException e) {
//    		throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "变更接口比对信息错误");
//		}
//    }
    private void ignoreField(List<String> ignoreFidlds) {
        ignoreFidlds.add("billNo");
        ignoreFidlds.add("billType");
        ignoreFidlds.add("billDate");
        ignoreFidlds.add("approver");
        ignoreFidlds.add("approveDate");
        ignoreFidlds.add("approveDesc");
        ignoreFidlds.add("pbcCheckStatus");
        ignoreFidlds.add("pbcCheckDate");
        //ignoreFidlds.add("customerNo");
        //ignoreFidlds.add("depositorName");
        ignoreFidlds.add("id");
        ignoreFidlds.add("description");
        ignoreFidlds.add("pbcSyncStatus");
        ignoreFidlds.add("pbcSyncError");
        ignoreFidlds.add("pbcOperator");
        ignoreFidlds.add("pbcSyncTime");
        ignoreFidlds.add("pbcSyncCheck");
        ignoreFidlds.add("pbcSyncMethod");
        ignoreFidlds.add("eccsSyncError");
        ignoreFidlds.add("eccsOperator");
        ignoreFidlds.add("eccsSyncTime");
        ignoreFidlds.add("eccsSyncCheck");
        ignoreFidlds.add("acctIsFromCore");
        ignoreFidlds.add("coreDataCompleted");
        ignoreFidlds.add("finalStatus");
        ignoreFidlds.add("handingMark");
        ignoreFidlds.add("initFullStatus");
        ignoreFidlds.add("initRemark");
        ignoreFidlds.add("fromSource");
        ignoreFidlds.add("acctNo");
        //ignoreFidlds.add("bankCode");
        ignoreFidlds.add("refBillId");
        ignoreFidlds.add("refCustomerBillId");
        ignoreFidlds.add("originalBillId");
        ignoreFidlds.add("recId");
        ignoreFidlds.add("acctLogId");
        ignoreFidlds.add("accountId");
        ignoreFidlds.add("customerLogId");
        ignoreFidlds.add("custLogId");
        ignoreFidlds.add("custOrganFullId");
        ignoreFidlds.add("acctOrgFullid");

        //新增忽略列表
        ignoreFidlds.add("status");
        ignoreFidlds.add("eccsSyncStatus");
        ignoreFidlds.add("midId");
        ignoreFidlds.add("acctId");
        ignoreFidlds.add("imageTempNo");
        ignoreFidlds.add("cancelHeZhun");
        ignoreFidlds.add("imageBatchNo");
        ignoreFidlds.add("aLLAccountData");
        ignoreFidlds.add("oldAccountKey");
        ignoreFidlds.add("organFullId");
        ignoreFidlds.add("organCode");
        ignoreFidlds.add("changeFields");
        ignoreFidlds.add("string006");
    }


    /**
     * 人行变更上报的字段
     *
     * @param account
     */
    @Override
    public List<String> getChangeFieldsNeedPbcSync(AllBillsPublicDTO account) {
        List<String> changeAmsSyncFieldList = new ArrayList<>(16);
        //2018年11月13日 与现场确认后，基本户核准号改变了无需上报

        // 需要上报人行的变更字段

        if (account.getAcctType() == CompanyAcctType.jiben) {
            String jibenAmsChangeSyncField[] = {"bankCode", "depositorName", "acctShortName", "regFullAddress", "fileType", "fileNo", "fileType2", "fileNo2", "regCurrencyType", "registeredCapital", "businessScope", "legalName", "legalIdcardType",
                    "legalIdcardNo", "orgCode", "stateTaxRegNo", "taxRegNo", "telephone", "zipcode", "parCorpName", "parAccountKey", "parOrgCode", "parLegalName", "parLegalIdcardType", "parLegalIdcardNo", "legalType","parLegalType","nationality"};

            List<String> jibenAmsChangeSyncFieldList = new ArrayList<>(Arrays.asList(jibenAmsChangeSyncField));
            if(account.getCancelHeZhun() != null && account.getCancelHeZhun()) {
                log.info("===========================该账户为取消核准账户，增加取消核准后的变更字段：fileDue，industryCode，noTaxProve，isIdentification");
                jibenAmsChangeSyncFieldList.add("fileDue");
                jibenAmsChangeSyncFieldList.add("industryCode");//行业归属
                jibenAmsChangeSyncFieldList.add("noTaxProve");//无需录入国地税的证明
                jibenAmsChangeSyncFieldList.add("isIdentification");//未标明注册资金
            }
            changeAmsSyncFieldList.addAll(jibenAmsChangeSyncFieldList);
        } else if (account.getAcctType() == CompanyAcctType.yiban) {
            String yibanChangeSyncField[] = {"bankCode", "acctCreateDate", "acctFileType", "acctFileNo"};
            changeAmsSyncFieldList.addAll(Arrays.asList(yibanChangeSyncField));
        } else if (account.getAcctType() == CompanyAcctType.yusuan) {
            String yusuanChangeSyncField[] = {"bankCode", "acctCreateDate", "acctFileType", "acctFileNo", "acctFileType2", "acctFileNo2", "accountNameFrom", "saccprefix", "saccpostfix", "capitalProperty",  "fundManager",
                    "fundManagerIdcardType", "fundManagerIdcardNo", "insideDeptName", "insideLeadName", "insideLeadIdcardType", "insideLeadIdcardNo", "insideTelephone", "insideZipcode", "insideAddress"};//"enchashmentType"  预算去掉取现标识字段
            changeAmsSyncFieldList.addAll(Arrays.asList(yusuanChangeSyncField));
        } else if (account.getAcctType() == CompanyAcctType.feiyusuan) {
            //非预算增加证明文件种类，证明文件编号变更字段
            String feiyusuanChangeSyncField[] = {"bankCode", "bankName", "acctCreateDate", "accountNameFrom", "saccprefix", "saccpostfix", "capitalProperty",  "fundManager", "fundManagerIdcardType", "fundManagerIdcardNo", "insideDeptName",
                    "insideLeadName", "insideLeadIdcardType", "insideLeadIdcardNo", "insideTelephone", "insideZipcode", "insideAddress","acctFileType", "acctFileNo"};//"enchashmentType"  非预算去掉取现标识字段   非预算该字段由人民银行进行维护
            changeAmsSyncFieldList.addAll(Arrays.asList(feiyusuanChangeSyncField));
        } else if (account.getAcctType() == CompanyAcctType.linshi) {
            String linshiChangeSyncField[] = {"bankCode","depositorName", "acctShortName", "regFullAddress", "fileType", "fileNo", "regCurrencyType", "registeredCapital", "businessScope", "legalName", "legalIdcardType", "legalIdcardNo",
                    "orgCode", "stateTaxRegNo", "taxRegNo", "telephone", "zipcode", "parCorpName", "parAccountKey", "parOrgCode", "parLegalName", "parLegalIdcardType", "parLegalIdcardNo", "legalType"};
            changeAmsSyncFieldList.addAll(Arrays.asList(linshiChangeSyncField));//"effectiveDate",
        } else if (account.getAcctType() == CompanyAcctType.feilinshi) {
            //20191008  非临时增加变更字段：acctFileType  acctFileNo
            String feilinshiChangeSyncField[] = {"bankCode", "bankName", "acctCreateDate", "acctCreateReason","nontmpProjectName", "nontmpLegalName", "nontmpLegalIdcardType", "nontmpLegalIdcardNo", "nontmpZipcode", "nontmpTelephone",
                    "nontmpAddress","acctFileType","acctFileNo"};// "effectiveDate",
            changeAmsSyncFieldList.addAll(Arrays.asList(feilinshiChangeSyncField));
        } else if (account.getAcctType() == CompanyAcctType.teshu) {
            String teshuChangeSyncField[] = {"bankCode", "depositorName", "acctShortName", "regFullAddress", "fileType", "fileNo", "fileType2", "fileNo2", "businessLicenseNo", "businessLicenseDue", "regCurrencyType", "registeredCapital", "businessScope", "legalName", "legalIdcardType", "legalIdcardNo",
                    "orgCode", "stateTaxRegNo", "taxRegNo", "telephone", "zipcode", "parCorpName", "parOrgCode", "parLegalName", "parLegalIdcardType", "parLegalIdcardNo", "legalType"};
            changeAmsSyncFieldList.addAll(Arrays.asList(teshuChangeSyncField));
        }

        return changeAmsSyncFieldList;

    }

    /**
     * 人行变更必须上报的字段
     * 此字段从人行变更页面获取
     *
     * @param account
     */
    @Override
    public List<String> getChangeFieldMustPbcSync(AllBillsPublicDTO account) {
        List<String> mustPbcSync = new ArrayList<>(16);
        String basePbcSyncField[] = {"billType", "acctNo", "acctType", "bankCode"};
        mustPbcSync.addAll(Arrays.asList(basePbcSyncField));

        String pbcSyncField[] = {};
        if (account.getAcctType() == CompanyAcctType.jiben) {
            //无其他特殊字段
//            if(account.getBillType() == BillType.ACCT_CHANGE){
//                pbcSyncField = new String[]{"accountKey","orgCode","stateTaxRegNo","taxRegNo","orgEccsNo","regType","regNo"};
//            }
        } else if (account.getAcctType() == CompanyAcctType.yusuan) {
            pbcSyncField = new String[]{"accountNameFrom"};
        } else if (account.getAcctType() == CompanyAcctType.feilinshi) {
            pbcSyncField = new String[]{"acctCreateReason"};
        } else if (account.getAcctType() == CompanyAcctType.feiyusuan) {
            pbcSyncField = new String[]{"fileType", "fileNo", "capitalProperty", "accountNameFrom"};
        } else if (account.getAcctType() == CompanyAcctType.linshi) {
            //无其他特殊字段
        } else if (account.getAcctType() == CompanyAcctType.yiban) {
            pbcSyncField = new String[]{"acctFileType", "acctFileNo"};
        } else if (account.getAcctType() == CompanyAcctType.teshu) {
            //无其他特殊字段
        }
        mustPbcSync.addAll(Arrays.asList(pbcSyncField));
        return mustPbcSync;
    }

    /**
     * 信用机构变更上报的字段
     *
     * @param account
     */
    @Override
    public List<String> getChangeFieldsNeedEccsSync(AllBillsPublicDTO account) {
        List<String> changeEccsSyncFieldList = new ArrayList<String>();

        String jibenEccsChangeSyncField[] = {"depositorName", "orgEnName", "regCountry", "regProvince", "regCity", "regArea", "regAddress", "fileSetupDate", "fileDue", "registeredCapital", "businessScopeEccs",
                "legalIdcardNo", "orgCode", "workProvince",
                "workCity", "workArea", "workAddress", "telephone", "financeTelephone", "economyIndustryName"};

        changeEccsSyncFieldList.addAll(Arrays.asList(jibenEccsChangeSyncField));

        return changeEccsSyncFieldList;
    }


    /**
     * 信用机构变更上报的字段
     *
     * @param account
     */
    @Override
    public List<String> getChangeFieldEccsSync(AllBillsPublicDTO account) {
        List<String> mustEccsSync = new ArrayList<>(16);
        String[] mustEccsSyncField = {"billType", "acctNo", "acctType", "bankCode"};
        mustEccsSync.addAll(Arrays.asList(mustEccsSyncField));
        return mustEccsSync;
    }

    /**
     * 查询人行转换账户性质
     *
     * @param pbcAccountDto
     * @param billsPublic
     */
    private void switchAcctType(PbcAccountDto pbcAccountDto, AllBillsPublicDTO billsPublic, UserDto userDto) {
        try {
            //如果账户性质还未区分小类则先去人行查询并抓换一次
            if (billsPublic.getAcctType() == CompanyAcctType.tempAcct || billsPublic.getAcctType() == CompanyAcctType.specialAcct) {
                if (billsPublic.getBillType() != BillType.ACCT_OPEN) {
                    //查询人行返回对象
                    AmsAccountInfo amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(pbcAccountDto, billsPublic.getAcctNo());
                    if (amsAccountInfo != null && amsAccountInfo.getAcctType() != null) {
                        //根据人行对象账户性质转换billsPublic小类对象
                        billsPublic.setAcctType(CompanyAcctType.valueOf(amsAccountInfo.getAcctType().name()));
                    }
                }
            }
        } catch (Exception e) {
            log.info("查询人行转换账户性质错误；",e);
            updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
            throw new BizServiceException(EErrorCode.PBC_QUERY_ERROR, "人行查询失败");
        }
    }

    /**
     * @param logId 账户Id
     * @author jogy.he
     * @description 从日志记录表中恢复账户记录到正式表
     */
    private void recoverAcctFromLog(Long logId) {
        try {
            AccountPublicLogInfo accountPublicLogInfo = accountPublicLogService.getOne(logId);
            AccountsAllInfo accountAllInfo = new AccountsAllInfo();
            String[] ignoreProperties = {"id"};
            BeanUtils.copyProperties(accountPublicLogInfo, accountAllInfo, ignoreProperties);
            // 设置账户Id
            accountAllInfo.setId(accountPublicLogInfo.getAccountId());

            // 保存主表
            accountsAllService.save(accountAllInfo);
            if (accountAllInfo != null && accountAllInfo.getId() > 0) {
                // 处理对公账户表
                AccountPublicInfo accountPublicInfo = accountPublicService.findByAccountId(accountAllInfo.getId());
                BeanUtils.copyProperties(accountPublicLogInfo, accountPublicInfo, ignoreProperties);
                // 保存
                accountPublicService.save(accountPublicInfo);

                if (accountPublicInfo == null) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "从日志表恢复账户LogId:" + logId + "失败！");
                }
            } else {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "从日志表恢复账户LogId:" + logId + "失败！");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void setAcctCreateDate(AllBillsPublicDTO info) {
        if (StringUtils.isNotBlank(info.getAcctCreateDate())) {
            Date date = null;
            try {
                date = DateUtils.parse(info.getAcctCreateDate(), "yyyy-MM-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            info.setAcctCreateDate(DateUtils.DateToStr(date, "yyyy-MM-dd"));
        }

    }

    // 设置账户名称
    @Override
    public void setAcctName(AllBillsPublicDTO entity) {
        // 判断账户名称
        if (CompanyAcctType.yusuan.equals(entity.getAcctType())
                || CompanyAcctType.feiyusuan.equals(entity.getAcctType())) {// 预算
            // 账户名称内设部门
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getAccountNameFrom())
                    && entity.getAccountNameFrom().equals("1")) {
                if (org.apache.commons.lang3.StringUtils.isEmpty(entity.getInsideDeptName())) {
                    entity.setInsideDeptName("");
                }
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getDepositorName())) {
                    entity.setAcctName(entity.getDepositorName() + entity.getInsideDeptName());
                } else {
                    entity.setAcctName(entity.getInsideDeptName());
                }
            }
            // 账户名称加上前缀后缀
            else if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getAccountNameFrom())
                    && entity.getAccountNameFrom().equals("2")) {
                if (org.apache.commons.lang3.StringUtils.isEmpty(entity.getSaccpostfix())) {
                    entity.setSaccpostfix("");
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(entity.getSaccprefix())) {
                    entity.setSaccprefix("");
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(entity.getDepositorName())) {
                    entity.setDepositorName("");
                }
                if (entity.getAcctType().equals(CompanyAcctType.yusuan)
                        || entity.getAcctType().equals(CompanyAcctType.feiyusuan)) {
//                    entity.setAcctName(entity.getSaccprefix() + entity.getDepositorName()
//                            + entity.getSaccpostfix());
                    String acctName = entity.getDepositorName();
                    //如果已经包含前缀了就不拼接
                    if(StringUtils.isNotEmpty(entity.getSaccprefix())){
                        if(!acctName.contains(entity.getSaccprefix())){
                            acctName = entity.getSaccprefix() + acctName;
                        }
                    }
                    if(StringUtils.isNotEmpty(entity.getSaccpostfix())){
                        //如果已经包含前缀了就不拼接
                        if(!acctName.contains(entity.getSaccpostfix())){
                            acctName = acctName + entity.getSaccpostfix();
                        }
                    }
                    entity.setAcctName(acctName);
                }
            } else {
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getDepositorName())) {
                    entity.setAcctName(entity.getDepositorName());
                } else {
                    entity.setAcctName("");
                }
            }
        } else if (CompanyAcctType.feilinshi.equals(entity.getAcctType())) {// 非临时
            // 开户原因 建设部门
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getAcctCreateReason())
                    && entity.getAcctCreateReason().equals("1")) {
                if (org.apache.commons.lang3.StringUtils.isEmpty(entity.getNontmpProjectName())) {
                    entity.setNontmpProjectName("");
                }
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getDepositorName())) {
                    entity.setAcctName(entity.getDepositorName() + entity.getNontmpProjectName());
                } else {
                    entity.setAcctName("");
                }
            } else {
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getDepositorName())) {
                    entity.setAcctName(entity.getDepositorName());
                } else {
                    entity.setAcctName("");
                }
            }
        } else {
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getDepositorName())) {
                entity.setAcctName(entity.getDepositorName());
            } else {
                entity.setAcctName("");
            }
        }
    }

    @Override
    public AllBillsPublicDTO conversion(AllBillsPublicDTO info1) {
        if (StringUtils.isNotBlank(info1.getAcctFileType())) {
            if ("09".equals(info1.getAcctFileType()) || "10".equals(info1.getAcctFileType())
                    || "11".equals(info1.getAcctFileType())) {
                info1.setAcctFileType(
                        dictionaryService.transalte("accountFileTypeYuSuanValueItem", info1.getAcctFileType()));
            }
            if ("06".equals(info1.getAcctFileType()) || "17".equals(info1.getAcctFileType())) {
                info1.setAcctFileType(
                        dictionaryService.transalte("accountFileTypeYiBanValueItem", info1.getAcctFileType()));
            }
            if ("14".equals(info1.getAcctFileType()) || "15".equals(info1.getAcctFileType())
                    || "16".equals(info1.getAcctFileType())) {
                info1.setAcctFileType(dictionaryService.transalte("accountFileTypeFeiLinShiValueItem",
                        info1.getAcctFileType()));
            }
        }
        if (StringUtils.isNotBlank(info1.getRegType())) {
            info1.setAcctFileType(dictionaryService.transalte("regTypeValue2Item", info1.getRegType()));
        }
        if (StringUtils.isNotBlank(info1.getDepositorType())) {
            if ("50".equals(info1.getDepositorType()) || "51".equals(info1.getDepositorType())
                    || "52".equals(info1.getDepositorType())) {
                info1.setDepositorType(
                        dictionaryService.transalte("depositorTypeTeShuValueItem", info1.getDepositorType()));
            } else {
                info1.setDepositorType(
                        dictionaryService.transalte("depositorTypeValue2Item", info1.getDepositorType()));
            }
        }
        if (StringUtils.isNotBlank(info1.getLegalType())) {
            info1.setLegalType(dictionaryService.transalte("legalTypeValue2Item", info1.getLegalType()));
        }
        if (StringUtils.isNotBlank(info1.getParLegalType())) {
            info1.setParLegalType(dictionaryService.transalte("legalTypeValue2Item", info1.getParLegalType()));
        }
        if (StringUtils.isNotBlank(info1.getIsIdentification())) {
            info1.setIsIdentification(
                    dictionaryService.transalte("isIdentificationValue2Item", info1.getIsIdentification()));
        }
        if (StringUtils.isNotBlank(info1.getLegalIdcardType())) {
            info1.setLegalIdcardType(
                    dictionaryService.transalte("legalIdcardTypeValue2Item", info1.getLegalIdcardType()));
        }
        if (StringUtils.isNotBlank(info1.getParLegalIdcardType())) {
            info1.setParLegalIdcardType(
                    dictionaryService.transalte("legalIdcardTypeValue2Item", info1.getParLegalIdcardType()));
        }
        if (StringUtils.isNotBlank(info1.getOperatorIdcardType())){
            info1.setOperatorIdcardType(dictionaryService.transalte("legalIdcardTypeValue2Item", info1.getOperatorIdcardType()));
        }
        if (StringUtils.isNotBlank(info1.getAccountNameFrom())) {
            info1.setAccountNameFrom(
                    dictionaryService.transalte("accountNameFromTypeValueItem", info1.getAccountNameFrom()));
        }
        if (StringUtils.isNotBlank(info1.getFileType())) {
            if ("14".equals(info1.getFileType()) || "15".equals(info1.getFileType())
                    || "16".equals(info1.getFileType())) {
                info1.setFileType(
                        dictionaryService.transalte("fileTypeFeiLinShiValueItem", info1.getFileType()));
            }
            if (info1.getAcctType() == CompanyAcctType.linshi
                    && ("01".equals(info1.getFileType()) || "09".equals(info1.getFileType()))) {
                info1.setFileType(dictionaryService.transalte("fileTypeLinShiValueItem", info1.getFileType()));
            }
            if (info1.getAcctType() == CompanyAcctType.teshu || "12".equals(info1.getFileType())
                    || "17".equals(info1.getFileType())) {
                info1.setFileType(dictionaryService.transalte("fileTypeTeShuValueItem", info1.getFileType()));
            }
            if ((info1.getAcctType() == CompanyAcctType.jiben || info1.getAcctType() == CompanyAcctType.yiban
                    || info1.getAcctType() == CompanyAcctType.yusuan || info1.getAcctType() == CompanyAcctType.feiyusuan
                    || info1.getAcctType() == CompanyAcctType.feilinshi)
                    && ("01".equals(info1.getFileType()) || "02".equals(info1.getFileType())
                    || "03".equals(info1.getFileType()) || "04".equals(info1.getFileType()))) {
                info1.setFileType(dictionaryService.transalte("fileTypejiBenValueItem", info1.getFileType()));
            }
        }
        if (StringUtils.isNotBlank(info1.getFileType2())) {
            if ((info1.getAcctType() == CompanyAcctType.jiben || info1.getAcctType() == CompanyAcctType.yiban
                    || info1.getAcctType() == CompanyAcctType.yusuan || info1.getAcctType() == CompanyAcctType.feiyusuan
                    || info1.getAcctType() == CompanyAcctType.linshi)
                    && ("02".equals(info1.getFileType2()) || "03".equals(info1.getFileType2())
                    || "04".equals(info1.getFileType2()) || "08".equals(info1.getFileType2()))) {
                info1.setFileType2(
                        dictionaryService.transalte("fileTypejiBenValue2Item", info1.getFileType2()));
            }
            if (info1.getAcctType() == CompanyAcctType.teshu || "13".equals(info1.getFileType2())
                    || "17".equals(info1.getFileType2())) {
                info1.setFileType2(
                        dictionaryService.transalte("fileTypeTeShuValue2Item", info1.getFileType2()));
            }
        }
        if (StringUtils.isNotBlank(info1.getIndustryCode())) {
            info1.setIndustryCode(
                    dictionaryService.transalte("industryCodeValueItem", info1.getIndustryCode()));
        }
        if (StringUtils.isNotBlank(info1.getAcctCancelReason())) {
            info1.setAcctCancelReason(
                    dictionaryService.transalte("acctCancelReasonValueItem", info1.getAcctCancelReason()));
        }
        if (StringUtils.isNotBlank(info1.getRegCurrencyType())) {
            info1.setRegCurrencyType(
                    dictionaryService.transalte("regCurrencyType2Item", info1.getRegCurrencyType()));
        }

        if (StringUtils.isNotBlank(info1.getRegOffice())) {
            info1.setRegOffice(
                    dictionaryService.transalte("regOfficeValue2Item", info1.getRegOffice()));
        }
        if (StringUtils.isNotBlank(info1.getOrgType())) {
            info1.setOrgType(
                    dictionaryService.transalte("orgTypeValue2Item", info1.getOrgType()));
        }
        if (StringUtils.isNotBlank(info1.getRegType())) {
            info1.setRegType(
                    dictionaryService.transalte("regTypeValue2Item", info1.getRegType()));
        }
        if (StringUtils.isNotBlank(info1.getEconomyType())) {
            info1.setEconomyType(
                    dictionaryService.transalte("economyTypeValue2Item", info1.getEconomyType()));
        }
        /*
         * if(StringUtils.isNotBlank(info1.getCapitalProperty())){ String[] o =
         * info1.getCapitalProperty().split(","); info1.setCapitalProperty(o[1]); }
         * if(StringUtils.isNotBlank(info1.getBasicAccountStatus())){
         * info1.setBasicAccountStatus(info1.getBasicAccountStatus().split( ",")[1]); }
         * if(StringUtils.isNotBlank(info1.getOrgStatus())){
         * info1.setOrgStatus(info1.getOrgStatus().split(",")[1]); }
         * if(StringUtils.isNotBlank(info1.getCorpScale())){
         * info1.setCorpScale(info1.getCorpScale().split(",")[1]); }
         */
        return info1;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    public void sendModifyStatus(AllBillsPublicDTO dataAccount, String status) {
        if (StringUtils.isNotBlank(dataAccount.getPreOpenAcctId()) && dataAccount.getBillType() == BillType.ACCT_OPEN) {
            try {
                CompanyPreOpenAccountEntDto result = companyPreOpenAccountEntService.selectOne(Long.parseLong(dataAccount.getPreOpenAcctId()));
                if (result != null) {
                    companyPreOpenAccountEntService.sendModifyStatus(result.getId(), status);

                    result.setAcctOpenStatus(status);
                    CompanyPreOpenAccountEntDto dto = new CompanyPreOpenAccountEntDto();
                    BeanUtils.copyProperties(result, dto);
                    companyPreOpenAccountEntService.edit(dto);
                }
            } catch (Exception e) {
                log.error("改变状态失败");
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    public void sendModifyStatus(String applyId, ApplyEnum status, String pbcCode) {
        try {
            CompanyPreOpenAccountEntDto result = companyPreOpenAccountEntService.findByApplyid(applyId);
            if (result != null) {
                companyPreOpenAccountEntService.sendModifyStatus(result.getApplyid(), status, pbcCode, "");

                result.setStatus(status.getValue());
                result.setAcctOpenStatus(status.getValue());
                CompanyPreOpenAccountEntDto dto = new CompanyPreOpenAccountEntDto();
                BeanUtils.copyProperties(result, dto);
                companyPreOpenAccountEntService.edit(dto);
            }
        } catch (Exception e) {
            log.error("预约新接口修改预约状态失败");
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    public String pushApplyAddAcct(CryptoAddApplyAcctVo applyAcctVo, OrganizationDto organDto) {
        String applyId = "";
        CompanyPreOpenAccountEntDto dto = new CompanyPreOpenAccountEntDto();

        try {
            companyPreOpenAccountEntService.applyOpenValidater(applyAcctVo);
            log.info("新增预约单接口输入参数:" + JSON.toJSONString(applyAcctVo));
            String jsonStr = companyPreOpenAccountEntService.pushApplyAddAcct(applyAcctVo);
            JSONObject obj= (JSONObject) JSON.parse(jsonStr);//将字符串转为json对象
            applyId = String.valueOf(obj.get("applyId"));

            BeanValueUtils.copyProperties(applyAcctVo, dto);
            dto.setApplyid(applyId);
            dto.setOrganfullid(organDto.getFullId());
            dto.setOrganid(String.valueOf(organDto.getId()));
            dto.setBillType(applyAcctVo.getBillType().name());
            dto.setType(applyAcctVo.getType().name());
            dto.setApplyorganid(applyAcctVo.getOrganId());
            dto.setApplytime(applyAcctVo.getApplyDate());

            companyPreOpenAccountEntService.saveApply(dto);
        } catch (Exception e) {
            log.error("预约接口新增预约单失败:" + e.getMessage(), e);
        }

        return applyId;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    public void pushApplyEditAcct(CryptoEditApplyAcctVo applyAcctVo) {
        try {
            companyPreOpenAccountEntService.applyEditValidater(applyAcctVo);
            companyPreOpenAccountEntService.pushApplyEditAcct(applyAcctVo);
        } catch (Exception e) {
            log.error("预约接口修改预约单失败:" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    public void pushApplyAcctKey(CryptoModifyAcctKeyMessage acctKeyMessage) {
        try {
            CompanyPreOpenAccountEntDto result = companyPreOpenAccountEntService.findByApplyid(acctKeyMessage.getApplyId());
            if (result != null) {
                companyPreOpenAccountEntService.pushApplyAcctKey(acctKeyMessage);
            }
        } catch (Exception e) {
            log.error("预约新接口核准号推送失败");
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> reject(Long userId, Long formId, Map<String, String> formData) throws EacException {
        Map<String, String> result = new HashMap<String, String>();
        boolean success = true;
        StringBuffer errorMsg = new StringBuffer("");

        try {
            // 转换formData对象
            AllBillsPublicDTO allBillsPublic;
            try {
                allBillsPublic = (AllBillsPublicDTO) Map2DomainUtils.converter(formData, AllBillsPublicDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
                success = false;
                errorMsg.append("页面数据对象转换异常");
                throw new EacException("页面数据对象转换异常");
            }

            if (allBillsPublic.getId() != null && allBillsPublic.getId() > 0) {
                updateApproveStatus(allBillsPublic, BillStatus.REJECT, userId,
                        allBillsPublic.getDenyReason());
                writeLog(formData,allBillsPublic.getId());

                //预约开户信息驳回状态修改
                if (StringUtils.isNotBlank(allBillsPublic.getPreOpenAcctId()) && !applyNewRuleFlag) {
                    allBillsPublicService.sendModifyStatus(allBillsPublic, "REGISTER_FAIL");
                }
            } else {
                success = false;
                errorMsg.append("提交的流水ID不能为空");
                throw new EacException("提交的流水ID不能为空");
            }
        } catch (RuntimeException e) {
            // TODO: handle exception
            e.printStackTrace();
            success = false;
            throw new EacException(e.getMessage());
        }

        if (!success) {
            result.put("submitResult", errorMsg.toString());
        } else {
            result.put("submitResult", "success");

        }

        return result;
    }

    @Override
    public Map<String, String> verifyPass(Long userId, Long formId, Map<String, String> formData) throws EacException {
        Map<String, String> result = new HashMap<String, String>();
        boolean success = true;
        StringBuffer errorMsg = new StringBuffer("");

        try {
            if(formId != null){
                accountBillsAllService.updateBillStatus(formId, BillStatus.WAITING_REPORTING, userId, "");
                log.info("审核通过,等待上报，更新流水审核状态:流水号：" + formId + "为" + BillStatus.WAITING_REPORTING);
                writeLog(formData,formId);
            } else {
                success = false;
                errorMsg.append("提交的流水ID不能为空");
                throw new EacException("提交的流水ID不能为空");
            }
        } catch (RuntimeException e) {
            // TODO: handle exception
            e.printStackTrace();
            success = false;
            throw new EacException(e.getMessage());
        }

        if (!success) {
            result.put("submitResult", errorMsg.toString());
        } else {
            result.put("submitResult", "success");

        }
        return result;
    }


    /**
     * 取消YD_PUBLIC_BILLS_ALL_V视图
     *
     * @param info
     * @param fields
     * @param isLikes
     * @param code
     * @return
     */
    @Deprecated
    private SqlBuilder makeQuerySQL(AllBillsPublicDTO info, String[] fields, boolean[] isLikes, String code) {
        SqlBuilder sqlBuilder = new SqlBuilder();
        List<String> statuses = new ArrayList<>();
        List<String> pbcSyncStatuses = null;
        List<String> eccsSyncStatuses = null;

        info.setOrganFullId(SecurityUtils.getCurrentOrgFullId());

        sqlBuilder.select("*").from("YD_PUBLIC_BILLS_ALL_V");

        sqlBuilder.startsWith("yd_organ_full_id", BeanValueUtils.getValue(info, "organFullId"));

        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            String tableField = IdeaNamingStrategy.PREFIX + field;

            if ("yd_depositorName".equals(tableField)) {
                tableField = "yd_depositor_name";
            }
            if ("yd_acctNo".equals(tableField)) {
                tableField = "yd_acct_no";
            }
            if ("yd_acctType".equals(tableField)) {
                tableField = "yd_acct_type";
            }
            if ("yd_billType".equals(tableField)) {
                tableField = "yd_bill_type";
            }

            boolean isLike = isLikes[i];
            if (isLike) {
                sqlBuilder.likeByCondition(tableField, BeanValueUtils.getValue(info, field));
            } else {
                sqlBuilder.eq(tableField, BeanValueUtils.getValue(info, field));
            }
        }


        if ("dbllb".equals(code)) {
            // 待补录列表
            //        sqlBuilder.eq("YD_FROMSOURCE", "CORE");
            sqlBuilder.eqAdd("yd_status", "WAITING_SUPPLEMENT");
            sqlBuilder.orEq("yd_status", "APPROVED");
            sqlBuilder.eqAdd("yd_eccs_sync_status", "tongBuShiBai");
            sqlBuilder.orEquals("yd_pbc_sync_status", "tongBuShiBai");
            sqlBuilder.add(")");
            sqlBuilder.add(")");
            sqlBuilder.add(")");
        } else if ("zhsqCheck".equals(code)) {
            statuses.add("NEW");
            statuses.add("APPROVING");
            statuses.add("REJECT");
            sqlBuilder.in("yd_status", statuses);
        } else if ("cgsblb".equals(code)) {
            sqlBuilder.eq("yd_status", "APPROVED");

            sqlBuilder.eq("yd_pbc_sync_status", "tongBuChengGong");
            eccsSyncStatuses = new ArrayList<>();
            eccsSyncStatuses.add("buTongBu");
            eccsSyncStatuses.add("tongBuChengGong");
            sqlBuilder.in("yd_eccs_sync_status", eccsSyncStatuses);
        } else if ("zhshCheck".equals(code)) {
            sqlBuilder.eq("yd_status", "APPROVING");
        } else if ("zhhztgCheck".equals(code)) {
            sqlBuilder.eq("yd_pbc_check_status", "CheckPass");
            sqlBuilder.ne("yd_acct_type", "yiban");
            sqlBuilder.ne("yd_acct_type", "feiyusuan");
        } else if ("dsbCheck".equals(code)) {
            sqlBuilder.eq("yd_status", "WAITING_REPORTING");
//            sqlBuilder.eqAdd("yd_pbc_sync_status", "weiTongBu");
//            sqlBuilder.orEquals("yd_eccs_sync_status", "weiTongBu");
            sqlBuilder.add(")");
        }
        sqlBuilder.orderBy("yd_last_update_date desc");

        return sqlBuilder;
    }

    /**
     * @param custPubMidInfo 客户中间表,userId 操作人Id,isValidate是否对信息验证
     * @author jogy.he
     * @Descrpition 从中间表新增正式CustomerPublic表客户信息
     */
    private void saveCustPublic(CustomerPublicMidInfo custPubMidInfo, Long userId, Long nowLogId) {
        // 验证信息是否完整
        custPubMidInfo.validate();

        CustomerPublicInfo customerPublicInfo = null;
        CustomersAllInfo customersAllInfo = null;
        if (StringUtils.isNotBlank(custPubMidInfo.getCustomerNo())) {
            customerPublicInfo = customerPublicService.getByCustomerNo(custPubMidInfo.getCustomerNo());
            customersAllInfo = customersAllService.findByCustomerNo(custPubMidInfo.getCustomerNo());
        }
        if (customerPublicInfo==null && customersAllInfo ==null && custPubMidInfo.getCustomerId() != null) {
            customerPublicInfo = customerPublicService.getByCustomerId(custPubMidInfo.getCustomerId());
            customersAllInfo = customersAllService.findOne(custPubMidInfo.getCustomerId());
        }
        if (customerPublicInfo==null && customersAllInfo ==null && StringUtils.isNotBlank(custPubMidInfo.getDepositorName())) {
            customerPublicInfo = customerPublicService.getByDepositorName(custPubMidInfo.getDepositorName());
            customersAllInfo = customersAllService.findByDepositorName(custPubMidInfo.getDepositorName());
        }

        if (customerPublicInfo != null && customerPublicInfo.getId() != null && customerPublicInfo.getId() > 0) {
            // 更新原客户信息
            // 校验基础字段不能被更新
            /*if (!StringUtils.equals(customerPublicInfo.getAccountKey(),custPubMidInfo.getAccountKey())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "基本开户许可核准号不可变更！");
            }*/

            /*
             * if (!customerPublicInfo.getOrgEccsNo().equals(custPubMidInfo.getOrgEccsNo())) { throw new
             * IdeaException("机构信用代码不可变更！"); }
             */
            // 同步主表信息
            String[] ignoreProperties = {"id", "createdDate", "createdBy", "organFullId", "orgEccsNo"};
            BeanUtils.copyProperties(custPubMidInfo, customersAllInfo, ignoreProperties);
            // 保持原记录的栏位
            customersAllInfo.setLastUpdateBy(userId + "");
            customersAllInfo.setLastUpdateDate(new Date());
            customersAllInfo.setRefCustomerLogId(nowLogId);
            // 保存主表信息
            customersAllService.save(customersAllInfo);

            // 1.1.2.2 设置对公客户表信息
            CustomerPublicInfo custPublicInfo = new CustomerPublicInfo();

            Set<String> midIgnoreProperties = BeanValueUtils.getNullProNames(custPubMidInfo);
            midIgnoreProperties.add("id");
            midIgnoreProperties.add("createdDate");
            midIgnoreProperties.add("createdBy");
            midIgnoreProperties.add("organFullId");
            midIgnoreProperties.add("orgEccsNo");
            String[] result = new String[midIgnoreProperties.size()];
            //忽略为空的字段  防止对公客户表同个客户不同业务把null值覆盖进原来有值的字段
            BeanUtils.copyProperties(custPubMidInfo, customerPublicInfo,midIgnoreProperties.toArray(result));
//            custPublicInfo.setId(customerPublicInfo.getId());
//            custPublicInfo.setCreatedDate(customerPublicInfo.getCreatedDate());
//            custPublicInfo.setCreatedBy(customerPublicInfo.getCreatedBy());
            customerPublicInfo.setLastUpdateBy(userId + "");
            customerPublicInfo.setLastUpdateDate(new Date());
            customerPublicInfo.setCustomerId(customerPublicInfo.getId());
            // 保存对公表信息
            customerPublicService.save(customerPublicInfo);

            //股东关联企业
            saveRelateCompanysFromMid(customerPublicInfo.getId(), custPubMidInfo);
            saveCompanyPartner(customerPublicInfo.getId(), custPubMidInfo);

            //股东关联企业
            /*// 1.1.2.2 处理股东信息
            if (custPubMidInfo.getCompanyPartnersMid() != null) {
                Set<CompanyPartnerMid> companyPartnerMids = custPubMidInfo.getCompanyPartnersMid();
                StringBuilder idsBuilder = new StringBuilder();

                for (CompanyPartnerMid cpm : companyPartnerMids) {
                    CompanyPartner cp = new CompanyPartner();
                    BeanUtils.copyProperties(cpm, cp);
                    // 设置原记录Id
                    cp.setId(cpm.getPartnerId());
                    cp.setLastUpdateBy(userId);
                    cp.setLastUpdateDate(new Date());
                    cp.setCustomerPublic(custPublic);
                    companyPartnerService.save(cp);

                    idsBuilder.append("," + cp.getId());
                }

                //删除id未在传入数据范围内的记录,refBillId带入更新前流水id
                if (idsBuilder != null && idsBuilder.length() > 0) {
                    String ids = idsBuilder.substring(1).toString();
                    int count = companyPartnerService.deleteBatchByCustIdAndId(custPubMidInfo.getCustomerId(),
                            customerPublicInfo.getCustomersAll().getRefBillId(), ids);
                    logger.info("删除客户主表:" + custPubMidInfo.getCustomerId() + "流水："
                            + customerPublicInfo.getCustomersAll().getRefBillId() + "股东信息" + count + "笔记录");
                }

            }
            // 1.1.2.3 处理关联企业信息
            if (custPubMidInfo.getRelateCompanysMid() != null) {
                Set<RelateCompanyMid> relateCompanyMids = custPubMidInfo.getRelateCompanysMid();
                StringBuilder idsBuilder = new StringBuilder();

                for (RelateCompanyMid rcm : relateCompanyMids) {
                    RelateCompany rc = new RelateCompany();
                    BeanUtils.copyProperties(rcm, rc);
                    // 设置原记录Id
                    rc.setId(rcm.getRelateId());
                    rc.setLastUpdateBy(userId);
                    rc.setLastUpdateDate(new Date());
                    rc.setCustomerPublic(custPublic);
                    relateCompanyService.save(rc);
                    idsBuilder.append("," + rc.getId());
                }

                //删除id未在传入数据范围内的记录,refBillId带入更新前流水id
                if (idsBuilder != null && idsBuilder.length() > 0) {
                    String ids = idsBuilder.substring(1).toString();
                    int count = relateCompanyService.deleteBatchByCustIdAndId(custPubMidInfo.getCustomerId(),
                            customerPublicInfo.getCustomersAll().getRefBillId(), ids);

                    logger.info("删除客户主表:" + custPubMidInfo.getCustomerId() + "流水："
                            + customerPublicInfo.getCustomersAll().getRefBillId() + "关联企业" + count + "笔记录");
                }
            }*/
        } else {
            // 新增主表信息
            // 同步主表信息
            customersAllInfo = new CustomersAllInfo();
            String[] ignoreProperties = {"id", "lastUpdateBy", "lastUpdateDate"};
            BeanUtils.copyProperties(custPubMidInfo, customersAllInfo, ignoreProperties);

            customersAllInfo.setCreatedBy(userId + "");
            customersAllInfo.setCreatedDate(new Date());
            // 手工设置id,保存中间表带入的id
            customersAllInfo.setRefBillId(custPubMidInfo.getRefBillId());
            customersAllInfo.setId(custPubMidInfo.getCustomerId());
            customersAllInfo.setRefCustomerLogId(nowLogId);
            // 保存主表信息
            customersAllService.save(customersAllInfo);

            // 1.1.2.2 设置对公客户表信息
            CustomerPublicInfo custPublicInfo = new CustomerPublicInfo();
            BeanUtils.copyProperties(custPubMidInfo, custPublicInfo, ignoreProperties);
            custPublicInfo.setId(custPubMidInfo.getCustomerId());
            custPublicInfo.setCreatedBy(userId + "");
            custPublicInfo.setCreatedDate(new Date());
            custPublicInfo.setCustomerId(customersAllInfo.getId());
            // 保存对公表信息
            customerPublicService.save(custPublicInfo);

            //股东关联企业
            saveRelateCompanysFromMid(custPublicInfo.getId(), custPubMidInfo);
            saveCompanyPartner(custPublicInfo.getId(), custPubMidInfo);

            //股东、关联企业
            /*// 1.1.2.2 处理股东信息
            if (custPubMidInfo.getCompanyPartnersMid() != null) {
                Set<CompanyPartnerMid> companyPartnerMids = custPubMidInfo.getCompanyPartnersMid();
                for (CompanyPartnerMid cpm : companyPartnerMids) {
                    CompanyPartner cp = new CompanyPartner();
                    BeanUtils.copyProperties(cpm, cp, ignoreProperties);
                    cp.setId(cpm.getPartnerId());
                    cp.setCreatedBy(userId);
                    cp.setCreatedDate(new Date());
                    cp.setCustomerPublic(custPublic);
                    companyPartnerService.save(cp);
                }
            }
            // 1.1.2.3 处理关联企业信息
            if (custPubMidInfo.getRelateCompanysMid() != null) {
                Set<RelateCompanyMid> relateCompanyMids = custPubMidInfo.getRelateCompanysMid();
                for (RelateCompanyMid rcm : relateCompanyMids) {
                    RelateCompany rc = new RelateCompany();
                    BeanUtils.copyProperties(rcm, rc, ignoreProperties);
                    rc.setId(rcm.getRelateId());
                    rc.setCreatedBy(userId);
                    rc.setCreatedDate(new Date());
                    rc.setCustomerPublic(custPublic);
                    relateCompanyService.save(rc);
                }
            }*/
        }
    }

    private void saveCustPublic2(CustomerPublicMidInfo custPubMidInfo, Long userId, Long nowLogId) {
        // 验证信息是否完整
        custPubMidInfo.validate();
        log.info("开始处理客户主表CustomersAll");
        CustomersAllInfo customersAllInfo = null;
        if (StringUtils.isNotBlank(custPubMidInfo.getCustomerNo())) {
            customersAllInfo = customersAllService.findByCustomerNo(custPubMidInfo.getCustomerNo());
        }
        if ( customersAllInfo ==null && custPubMidInfo.getCustomerId() != null) {
            customersAllInfo = customersAllService.findOne(custPubMidInfo.getCustomerId());
        }
        if ( customersAllInfo ==null && StringUtils.isNotBlank(custPubMidInfo.getDepositorName())) {
            customersAllInfo = customersAllService.findByDepositorName(custPubMidInfo.getDepositorName());
        }
        if(customersAllInfo!=null && customersAllInfo.getId()!=null && customersAllInfo.getId()>0){
            log.info("客户主表CustomersAll更新，ID：{}",customersAllInfo.getId());
            String[] ignoreProperties = {"id", "createdDate", "createdBy", "organFullId", "orgEccsNo"};
            BeanUtils.copyProperties(custPubMidInfo, customersAllInfo, ignoreProperties);
            // 保持原记录的栏位
            customersAllInfo.setLastUpdateBy(userId + "");
            customersAllInfo.setLastUpdateDate(new Date());
            customersAllInfo.setRefCustomerLogId(nowLogId);
            // 保存主表信息
            customersAllService.save(customersAllInfo);
            log.info("客户主表CustomersAll处理结束");
        }else{
            log.info("客户主表CustomersAll新增，ID：{}",custPubMidInfo.getCustomerId());
            customersAllInfo = new CustomersAllInfo();
            String[] ignoreProperties = {"id", "lastUpdateBy", "lastUpdateDate"};
            BeanUtils.copyProperties(custPubMidInfo, customersAllInfo, ignoreProperties);

            customersAllInfo.setCreatedBy(userId + "");
            customersAllInfo.setCreatedDate(new Date());
            // 手工设置id,保存中间表带入的id
            customersAllInfo.setRefBillId(custPubMidInfo.getRefBillId());
            customersAllInfo.setId(custPubMidInfo.getCustomerId());
            customersAllInfo.setRefCustomerLogId(nowLogId);
            // 保存主表信息
            customersAllService.save(customersAllInfo);
            log.info("客户主表CustomersAll处理结束");
        }


        log.info("开始处理CustomerPublic");
        CustomerPublicInfo customerPublicInfo = null;
        if (StringUtils.isNotBlank(custPubMidInfo.getCustomerNo())) {
            customerPublicInfo = customerPublicService.getByCustomerNo(custPubMidInfo.getCustomerNo());
        }
        if (customerPublicInfo==null  && custPubMidInfo.getCustomerId() != null) {
            customerPublicInfo = customerPublicService.getByCustomerId(custPubMidInfo.getCustomerId());
        }
        if (customerPublicInfo==null  && StringUtils.isNotBlank(custPubMidInfo.getDepositorName())) {
            customerPublicInfo = customerPublicService.getByDepositorName(custPubMidInfo.getDepositorName());
        }

        if (customerPublicInfo != null && customerPublicInfo.getId() != null && customerPublicInfo.getId() > 0) {
            log.info("客户主表CustomerPublic更新，ID：{}",customerPublicInfo.getId());
            // 1.1.2.2 设置对公客户表信息
            Set<String> midIgnoreProperties = BeanValueUtils.getNullProNames(custPubMidInfo);
            midIgnoreProperties.add("id");
            midIgnoreProperties.add("createdDate");
            midIgnoreProperties.add("createdBy");
            midIgnoreProperties.add("organFullId");
            midIgnoreProperties.add("orgEccsNo");
            String[] result = new String[midIgnoreProperties.size()];
            //忽略为空的字段  防止对公客户表同个客户不同业务把null值覆盖进原来有值的字段
            BeanUtils.copyProperties(custPubMidInfo, customerPublicInfo,midIgnoreProperties.toArray(result));
            customerPublicInfo.setLastUpdateBy(userId + "");
            customerPublicInfo.setLastUpdateDate(new Date());
            customerPublicInfo.setCustomerId(customerPublicInfo.getCustomerId());
            // 保存对公表信息
            customerPublicService.save(customerPublicInfo);
            //股东关联企业
            saveRelateCompanysFromMid(customerPublicInfo.getId(), custPubMidInfo);
            saveCompanyPartner(customerPublicInfo.getId(), custPubMidInfo);
            log.info("客户主表CustomerPublic处理结束");
        } else {
            // 新增主表信息
            // 同步主表信息
            log.info("客户主表CustomerPublic新增，ID：{}",customersAllInfo.getId());
            String[] ignoreProperties = {"id", "lastUpdateBy", "lastUpdateDate"};
            // 1.1.2.2 设置对公客户表信息
            customerPublicInfo = new CustomerPublicInfo();
            BeanUtils.copyProperties(custPubMidInfo, customerPublicInfo, ignoreProperties);
            customerPublicInfo.setId(customersAllInfo.getId());
            customerPublicInfo.setCreatedBy(userId + "");
            customerPublicInfo.setCreatedDate(new Date());
            customerPublicInfo.setCustomerId(customersAllInfo.getId());
            // 保存对公表信息
            customerPublicService.save(customerPublicInfo);

            //股东关联企业
            saveRelateCompanysFromMid(customerPublicInfo.getId(), custPubMidInfo);
            saveCompanyPartner(customerPublicInfo.getId(), custPubMidInfo);
            log.info("客户主表CustomerPublic处理结束");
        }
    }

    /**
     * @param custPubMidInfo 客户中间表
     * @author jogy.he
     * @Descrpition 从中间表新增日志CustomerPublic表客户信息
     */
    private CustomerPublicLogInfo saveCustPublicLog(CustomerPublicMidInfo custPubMidInfo) {
        CustomerPublicLogInfo customerPublicLogInfo = new CustomerPublicLogInfo();
        String[] ignoreProperties = {"id"};
        BeanUtils.copyProperties(custPubMidInfo, customerPublicLogInfo, ignoreProperties);
        CustomerPublicLogInfo pastLogInfo = customerPublicLogService.getMaxSeq(custPubMidInfo.getCustomerId());
        Long sequence = 0L;
        if (pastLogInfo != null) {
            customerPublicLogInfo.setPreLogId(pastLogInfo.getId());
            if (pastLogInfo.getSequence() != null) {
                sequence = pastLogInfo.getSequence();
            }
        }
        customerPublicLogInfo.setSequence(sequence + 1);
        customerPublicLogService.save(customerPublicLogInfo);

        //关联企业，股东信息
        saveCompanyPartnerLog(customerPublicLogInfo.getId(), custPubMidInfo);
        saveRelateCompanysLogFromMid(customerPublicLogInfo.getId(), custPubMidInfo);

        //关联企业
        /*// 处理关联企业和股东信息
        if (custPubMidInfo.getCompanyPartnersMid() != null) {
            Set<CompanyPartnerMid> companyPartnerMids = custPubMidInfo.getCompanyPartnersMid();
            for (CompanyPartnerMid cpm : companyPartnerMids) {
                CompanyPartnerLog cpl = new CompanyPartnerLog();
                BeanUtils.copyProperties(cpm, cpl, ignoreProperties);
                // 设置原记录Id
                cpl.setCustomerPublicLog(custPublicLog);
                companyPartnerLogService.save(cpl);
            }
        }
        // 处理关联企业信息
        if (custPubMidInfo.getRelateCompanysMid() != null) {
            Set<RelateCompanyMid> relateCompanyMids = custPubMidInfo.getRelateCompanysMid();
            for (RelateCompanyMid rcm : relateCompanyMids) {
                RelateCompanyLog rcl = new RelateCompanyLog();
                BeanUtils.copyProperties(rcm, rcl, ignoreProperties);
                // 设置原记录Id
                rcl.setCustomerPublicLog(custPublicLog);
                relateCompanyLogService.save(rcl);
            }
        }*/
        return customerPublicLogInfo;
    }

    /**
     * @param custPubInfo 客户中间表
     * @author jogy.he
     * @Descrpition 从正式表新增日志CustomerPublic表客户信息
     */
    private Long saveCustPublicLog(CustomerPublicInfo custPubInfo) {
        CustomerPublicLogInfo customerPublicLogInfo = new CustomerPublicLogInfo();
        String[] ignoreProperties = {"id"};
        //根据正式表数据，找到customersAll信息，结合后保存到日志表
        CustomersAllInfo customersAllInfo = customersAllService.findOne(custPubInfo.getCustomerId());
        BeanUtils.copyProperties(customersAllInfo, customerPublicLogInfo, ignoreProperties);
        String[] ignoreProperties1 = {"id", "depositorName"};
        BeanUtils.copyProperties(custPubInfo, customerPublicLogInfo, ignoreProperties1);
        //设置customerId
        customerPublicLogInfo.setCustomerId(custPubInfo.getCustomerId());
        CustomerPublicLogInfo hisCustomerPublicLogInfo = customerPublicLogService.getMaxSeq(customerPublicLogInfo.getCustomerId());
        Long sequence = 0L;
        if (hisCustomerPublicLogInfo != null) {
            //上一个日志表的id
            customerPublicLogInfo.setPreLogId(hisCustomerPublicLogInfo.getId());
            if (hisCustomerPublicLogInfo.getSequence() != null) {
                sequence = hisCustomerPublicLogInfo.getSequence();
            }
        }
        //序号，递增
        customerPublicLogInfo.setSequence(sequence + 1);
        // 保存日志信息
        customerPublicLogService.save(customerPublicLogInfo);

        //处理关联企业和股东信息
        saveRelateCompanysLog(customerPublicLogInfo.getId(), custPubInfo);
        saveCompanyPartnerLog(customerPublicLogInfo.getId(), custPubInfo);

        return customerPublicLogInfo.getId();
    }

    public void convert(AllBillsPublicDTO dto, AllAcct allacct) {
        allacct.setAcctNo(dto.getAcctNo());// 账号
        allacct.setDepositorName(dto.getDepositorName());// 存款人名称
        allacct.setDepositorType(dto.getDepositorType());// 存款人类型
        allacct.setAcctCreateDate(dto.getAcctCreateDate());//  开户时间
        allacct.setLegalType(dto.getLegalType());// 1：法定代表人， 2：单位负责人
        allacct.setLegalName(dto.getLegalName());// 法人名称
        allacct.setLegalIdcardNo(dto.getLegalIdcardNo());// 法人证件编号
        allacct.setRegAddress(dto.getRegAddress());// 注册详细地址 报备信用机构
        allacct.setIndusRegArea(dto.getRegFullAddress());// 工商注册地址 报备人行
        allacct.setTelephone(dto.getTelephone());// 电话
        allacct.setZipCode(dto.getZipcode());// 邮政编码
        allacct.setOrgCode(dto.getOrgCode());// 组织机构代码
        allacct.setStateTaxRegNo(dto.getStateTaxRegNo());// 国税登记证号
        allacct.setTaxRegNo(dto.getTaxRegNo());// 地税登记证号
        allacct.setFileType(dto.getFileType());// 证明文件1种类
        allacct.setFileNo(dto.getFileNo());// 证明文件1编号
        allacct.setFileType2(dto.getFileType2());// 证明文件2种类
        allacct.setFileNo2(dto.getFileNo2());// 证明文件2编号
        allacct.setRegType(dto.getRegType());// 登记注册号类型
        allacct.setRegNo(dto.getRegNo());// 登记注册号码
        allacct.setBusinessScope(dto.getBusinessScope());// 经营（业务）范围（上报人行）
        allacct.setBusinessScopeEccs(dto.getBusinessScopeEccs());// 经营（业务）范围（上报信用机构）

        if (StringUtils.isNotBlank(dto.getBasicAcctRegArea())) {
            allacct.setRegAreaCode(dto.getBasicAcctRegArea());
        } else {
            allacct.setRegAreaCode(dto.getRegAreaCode());
        }
        //基本户、特殊户、临时户用的是regAreaCode
        if (allacct.getAcctType() == SyncAcctType.jiben || allacct.getAcctType() == SyncAcctType.teshu || allacct.getAcctType() == SyncAcctType.linshi) {
            if (StringUtils.isNotBlank(dto.getRegAreaCode())) {
                allacct.setRegAreaCode(dto.getRegAreaCode());
            }
        }

        // allacct.setRegAreaCode(dto.getBasicAcctRegArea());// 注册地区代码
        if (dto.getRegisteredCapital() != null) {
            allacct.setRegisteredCapital(String.valueOf(dto.getRegisteredCapital()));// 注册资金
        } else {
            allacct.setRegisteredCapital("");// 注册资金
        }
        allacct.setOrgEccsNo(dto.getOrgEccsNo());// 机构信用代码
        allacct.setIndustryName(dto.getIndustryCode());// 行业归属
        allacct.setRegOffice(dto.getRegOffice());// 登记部门
        allacct.setSetupDate(dto.getSetupDate());// 成立日期
        allacct.setTovoidDate(dto.getFileDue());// 证书到期日期
        allacct.setOrgType(dto.getOrgType());// 组织机构类别
        allacct.setOrgTypeDetail(dto.getOrgTypeDetail());// 组织机构类别细分
        allacct.setCorpScale(dto.getCorpScale());// 企业规模
        allacct.setEconomyIndustryCode(dto.getEconomyIndustryCode());// 经济行业分类
        allacct.setEconomyIndustryName(dto.getEconomyIndustryName());// 经济行业分类
        allacct.setWorkAddress(dto.getWorkAddress());// 办公（生产、经营）地址
        allacct.setOrgEnName(dto.getOrgEnName());// 机构英文名称
        allacct.setBankCardNo(dto.getBankCardNo());// 贷款卡编码
        allacct.setBankName(dto.getBankName());// 银行机构名称
        allacct.setIsIdentification(dto.getIsIdentification());// 未标明注册资金 1:未标明注册资金
        allacct.setNoTaxProve(dto.getNoTaxProve());// 无需办理税务登记证的文件或税务机关出具的证明
        allacct.setAccountKey(dto.getAccountKey());// 开户许可证号
        allacct.setIndustryCode(dto.getIndustryCode());// 行业代码
        allacct.setEconomyType(dto.getEconomyType());// 经济类型
        allacct.setOrgStatus(dto.getOrgStatus());// 机构状态
        allacct.setBasicAccountStatus(dto.getBasicAccountStatus());// 基本户状态
        allacct.setParCorpName(dto.getParCorpName());// 上级单位名称
        allacct.setFinanceTelephone(dto.getFinanceTelephone());// 财务联系电话
        allacct.setParAccountKey(dto.getParAccountKey());// 上级基本户开户许可核准号
        allacct.setParLegalType(dto.getParLegalType());// 上级法人或负责人
        allacct.setParLegalName(dto.getParLegalName());// 上级法人姓名
        allacct.setParLegalIdcardType(dto.getParLegalIdcardType());// 上级法人证件类型
        allacct.setParLegalIdcardNo(dto.getParLegalIdcardNo());// 上级法人证件号码
        allacct.setParOrgCode(dto.getParOrgCode());// 上级组织机构代码
        allacct.setParRegType(dto.getParRegType());// 上级登记注册类型
        allacct.setParRegNo(dto.getParRegNo());// 上级登记注册号码
        allacct.setParOrgEccsNo(dto.getParOrgEccsNo());// 上级信用机构信息代码
        allacct.setAccountNameFrom(dto.getAccountNameFrom());// 账户名称构成方式
        allacct.setCapitalProperty(dto.getCapitalProperty());// 预算-资金性质
        allacct.setEnchashmentType(dto.getEnchashmentType());// 预算_取现标识 0：否 1：是
        allacct.setMoneyManager(dto.getFundManager());// 预算_资金人姓名
        allacct.setMoneyManagerCtype(dto.getFundManagerIdcardType());// 预算_资金身份种类
        allacct.setMoneyManagerCno(dto.getFundManagerIdcardNo());// 预算_资金身份编号
        allacct.setInsideDepartmentName(dto.getInsideDeptName());// 预算_内设部门名称
        allacct.setInsideSaccdepmanName(dto.getInsideLeadName());// 预算_内设负责人名称
        allacct.setInsideSaccdepmanKind(dto.getInsideLeadIdcardType());// 预算_内设负责人身份种类
        allacct.setInsideSaccdepmanNo(dto.getInsideLeadIdcardNo());// 预算_内设身份编号
        allacct.setInsideTelphone(dto.getInsideTelephone());// 预算_内设电话
        allacct.setInsideZipCode(dto.getInsideZipcode());// 预算_内设编码
        allacct.setInsideAddress(dto.getInsideAddress());// 预算_内设地址
        allacct.setSaccprefix(dto.getSaccprefix());// 前缀
        allacct.setSaccpostfix(dto.getSaccpostfix());// 后缀
        allacct.setEffectiveDate(dto.getEffectiveDate());// 非临时_有效时间
        allacct.setCreateAccountReason(dto.getAcctCreateReason());// 非临时_申请开户原因
        allacct.setFlsProjectName(dto.getNontmpProjectName());// 非临时_项目部名称
        allacct.setFlsFzrAddress(dto.getNontmpAddress());// 非临时负责人地址
        allacct.setFlsFzrZipCode(dto.getNontmpZipcode());// 非临时负责人邮政编码
        allacct.setFlsFzrTelephone(dto.getNontmpTelephone());// 非临时负责人电话
        allacct.setFlsFzrLegalIdcardType(dto.getNontmpLegalIdcardType());// 非临时负责人证件类型
        allacct.setFlsFzrLegalIdcardNo(dto.getNontmpLegalIdcardNo());// 非临时负责人证件号码
        allacct.setFlsFzrLegalName(dto.getNontmpLegalName());// 非临时负责人姓名
        allacct.setRemark(dto.getRemark());// 备注
        allacct.setEconomyKind("");// 产业分类
        allacct.setAccountinfoimode("");// 资金管理人\内设部门种类
        allacct.setAcctName(dto.getAcctName());// 报备类_账户名
        allacct.setRegProvince(dto.getRegProvince());// 省份代码
        allacct.setRegCity(dto.getRegCity());// 城市代码
        allacct.setRegArea(dto.getRegArea());// 地区代码
        allacct.setRegProvinceCHName(dto.getRegProvinceChname());// 注册地区省份名
        allacct.setRegCityCHName(dto.getRegCityChname());// 注册地区城市名
        allacct.setRegAreaCHName(dto.getRegAreaChname());// 注册地区名称
        allacct.setWorkProvince(dto.getWorkProvince());// 办公省份代码
        allacct.setWorkCity(dto.getWorkCity());// 办公城市代码
        allacct.setWorkArea(dto.getWorkArea());// 办公地区代码
        allacct.setAccountFileType(dto.getAcctFileType());// 报备类证明文件1类型
        allacct.setAccountFileNo(dto.getAcctFileNo());// 报备类证明文件1编号
        allacct.setAccountFileType2(dto.getAcctFileType2());// 报备类证明文件2编号
        allacct.setAccountFileNo2(dto.getAcctFileNo2());// 报备类证明文件2类型
        allacct.setOldAccountKey(dto.getOldAccountKey());
        //本异地标识
        if(dto.getOpenAccountSiteType()==OpenAccountSiteType.ALLOPATRIC){
            allacct.setOpenAccountSiteType("2");
        }else{
            allacct.setOpenAccountSiteType("1");
        }
        // 账户性质
        String acctType = dictionaryService.transalte("acctType2Pbc", dto.getAcctType().name());
        allacct.setAcctType(SyncAcctType.valueOf(acctType));
        // 业务操作类型
        String syncOperateType = dictionaryService.transalte("operateType2Pbc", dto.getBillType().name());
        allacct.setOperateType(SyncOperateType.valueOf(syncOperateType));
        // 法人身份证件类型--人行账管系统
        allacct.setLegalIdcardTypeAms(dto.getLegalIdcardType());
        // 注册币种--人行账管系统
        String regCurrencyTypeAms = dictionaryService.transalte("data2amsRegCurrencyType", dto.getRegCurrencyType());
        allacct.setRegCurrencyTypeAms(regCurrencyTypeAms);
        // 省市区中文名
        // 基本户
        if (dto.getAcctType() == CompanyAcctType.jiben) {
            // 法人身份证件类型--机构信用代码系统
            String eccsLegalIdcardType = dictionaryService.transalte("data2eccsLegalIdcardType", dto.getLegalIdcardType());
            allacct.setLegalIdcardTypeEccs(eccsLegalIdcardType);
            // 注册币种--机构信用代码证系统
            allacct.setRegCurrencyTypeEccs(dto.getRegCurrencyType());
        }
        String pbcBankCode = dto.getBankCode();
        //转换为人行机构号
        OrganizationDto organizationDto = organizationService.findByCode(dto.getBankCode());
        if (organizationDto != null) {
            pbcBankCode = organizationDto.getPbcCode();
        }
        allacct.setBankCode(pbcBankCode);// 银行机构代码转人行结构代码
        if (dto.getBillType() != null && dto.getBillType() == BillType.ACCT_REVOKE) {
            allacct.setCancenReason(dto.getAcctCancelReason());// 撤销原因
        } else {
            allacct.setCancenReason("");
        }
        if(dto.getCancelHeZhun() != null && dto.getCancelHeZhun()){
            allacct.setCancelHeZhun(true);
        }else{
            allacct.setCancelHeZhun(false);
        }
        allacct.setNationality(dto.getNationality());

    }

    private void updateCoreSyncStatus(ResultDto resultDto, AllBillsPublicDTO billsPublic) {
        AccountBillsAllInfo accountBillsAll = accountBillsAllService.getOne(billsPublic.getId());
        if ("1".equals(resultDto.getCode())) {
            accountBillsAll.setCoreSyncStatus(CompanySyncStatus.tongBuChengGong);
            accountBillsAll.setCoreSyncTime(DateUtils.getNowDateShort("yyyy-MM-dd HH:mm:ss"));
            accountBillsAllService.save(accountBillsAll);
        } else if ("0".equals(resultDto.getCode())) {
            accountBillsAll.setCoreSyncStatus(CompanySyncStatus.tongBuShiBai);
            accountBillsAll.setCoreSyncTime(DateUtils.getNowDateShort("yyyy-MM-dd HH:mm:ss"));
            accountBillsAllService.save(accountBillsAll);
            throw new BizServiceException(EErrorCode.CORE_SYNC_FAILURE, resultDto.getMessage());
        }
    }

    public void saveSelectPwd(AllBillsPublicDTO billsPublic) {
        log.info("取消核准AccountsAllInfo保存基本户查询密码，开户许可证号");
        accountsAllService.updateCancelHezhun(billsPublic.getAccountId(),billsPublic.getSelectPwd(),billsPublic.getOpenKey(),billsPublic.getAccountKey());
    }


    private void saveCompanyPartnerLog(Long customerPublicLogId,CustomerPublicInfo custPubInfo){
        if (custPubInfo==null){
            return;
        }
        List<CompanyPartnerInfo> companyPartnerInfoList = companyPartnerService.getAllByCustomerPublicId(custPubInfo.getId());
        List<CompanyPartnerLogInfo> companyPartnerLogInfoList = ConverterService.convertToList(companyPartnerInfoList,CompanyPartnerLogInfo.class);
        for (CompanyPartnerLogInfo c : companyPartnerLogInfoList) {
            c.setId(null);
            c.setCustomerPublicLogId(customerPublicLogId);
        }
        companyPartnerLogService.save(companyPartnerLogInfoList);
        //TODO 股东中间表删除是否需要
    }

    private void saveCompanyPartnerLog(Long customerPublicLogId,CustomerPublicMidInfo customerPublicMidInfo){
        if (customerPublicMidInfo==null){
            return;
        }
        List<CompanyPartnerMidInfo> companyPartnerMidInfoList = companyPartnerMidService.getAllByCustomerPublicMidId(customerPublicMidInfo.getId());
        List<CompanyPartnerLogInfo> companyPartnerLogInfoList = ConverterService.convertToList(companyPartnerMidInfoList,CompanyPartnerLogInfo.class);
        for (CompanyPartnerLogInfo c : companyPartnerLogInfoList) {
            c.setId(null);
            c.setCustomerPublicLogId(customerPublicLogId);
        }
        companyPartnerLogService.save(companyPartnerLogInfoList);
        //TODO 股东中间表删除是否需要
    }

    private void saveCompanyPartner(Long customerPublicId ,CustomerPublicMidInfo customerPublicMidInfo){
        if (customerPublicId==null||customerPublicMidInfo==null){
            return;
        }
        List<CompanyPartnerMidInfo> companyPartnerMidInfoList = companyPartnerMidService.getAllByCustomerPublicMidId(customerPublicMidInfo.getId());
        List<CompanyPartnerInfo> companyPartnerInfoList = ConverterService.convertToList(companyPartnerMidInfoList,CompanyPartnerInfo.class);
        for (CompanyPartnerInfo c : companyPartnerInfoList) {
            c.setId(null);
            c.setCustomerPublicId(customerPublicId);
        }
        companyPartnerService.save(companyPartnerInfoList);
    }

    /**
     * 从主表保存关联企业到日志表
     * @param customerPublicLogId
     * @param custPubInfo
     */
    private void saveRelateCompanysLog(Long customerPublicLogId, CustomerPublicInfo custPubInfo) {
        if (custPubInfo == null) {
            return;
        }
        List<RelateCompanyInfo> relateCompanyInfos = relateCompanyService.getAllByCustomerPublicId(custPubInfo.getId());
        List<RelateCompanyLogInfo> relateCompanyLogInfos = ConverterService.convertToList(relateCompanyInfos, RelateCompanyLogInfo.class);
        for (RelateCompanyLogInfo c : relateCompanyLogInfos) {
            c.setId(null);
            c.setCustomerPublicLogId(customerPublicLogId);
        }
        relateCompanyLogService.save(relateCompanyLogInfos);
    }

    /**
     * 从中间表保存关联企业到日志表
     * @param customerPublicLogId
     * @param customerPublicMidInfo
     */
    private void saveRelateCompanysLogFromMid(Long customerPublicLogId, CustomerPublicMidInfo customerPublicMidInfo) {
        if (customerPublicMidInfo == null) {
            return;
        }
        List<RelateCompanyMidInfo> relateCompanyMidInfos = relateCompanyMidService.getAllByCustomerPublicMidId(customerPublicMidInfo.getId());
        List<RelateCompanyLogInfo> relateCompanyLogInfos = ConverterService.convertToList(relateCompanyMidInfos, RelateCompanyLogInfo.class);
        for (RelateCompanyLogInfo c : relateCompanyLogInfos) {
            c.setId(null);
            c.setCustomerPublicLogId(customerPublicLogId);
        }
        relateCompanyLogService.save(relateCompanyLogInfos);
    }

    /**
     * 从中间表保存关联企业信息到主表
     * @param customerPublicId
     * @param customerPublicMidInfo
     */
    private void saveRelateCompanysFromMid(Long customerPublicId, CustomerPublicMidInfo customerPublicMidInfo) {
        if (customerPublicId == null || customerPublicMidInfo == null) {
            return;
        }
        List<RelateCompanyMidInfo> relateCompanyMidInfos = relateCompanyMidService.getAllByCustomerPublicMidId(customerPublicMidInfo.getId());
        List<RelateCompanyInfo> relateCompanyInfos = ConverterService.convertToList(relateCompanyMidInfos, RelateCompanyInfo.class);
        for (RelateCompanyInfo c : relateCompanyInfos) {
            c.setId(null);
            c.setCustomerPublicId(customerPublicId);
        }
        relateCompanyService.save(relateCompanyInfos);
    }

    @Override
    public String checkSync() {
        List<ConfigDto> configList = configService.findByKey("syncStatus");
        if(CollectionUtils.isNotEmpty(configList)){
            ConfigDto configDto = configList.get(0);
            return configDto.getConfigValue();
        }
        return null;
    }

    public TableResultResponse<ReportStatisticsForDateDTO> statisticsForDateList(String startDate,String endDate,String organFullId, Pageable pageable) {

        List<CompanySyncStatus> allStatusList = new ArrayList<CompanySyncStatus>() {
            {
                add(CompanySyncStatus.weiTongBu);
                add(CompanySyncStatus.tongBuChengGong);
                add(CompanySyncStatus.tongBuShiBai);
            }
        };
        List<CompanySyncStatus> successStatusList = new ArrayList<CompanySyncStatus>() {
            {
                add(CompanySyncStatus.tongBuChengGong);
            }
        };
        Map<String, Object> resultMap = this.getStatisticsForDateAll(null, null, null,null, organFullId, pageable);
        int count = (int) resultMap.get("count");
        List<CountDo> cdList = (List<CountDo>) resultMap.get("result");
        List<String> dateList = new ArrayList<>();
        List<ReportStatisticsForDateDTO> rsfddList = new ArrayList<>();

        if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date start = sdf.parse(startDate);
                Date end = sdf.parse(endDate);

                Iterator<CountDo> iterator = cdList.iterator();
                while(iterator.hasNext()) {
                    CountDo cd = iterator.next();
                    Date cd1 = sdf.parse(cd.getGroupByStr());
                    Boolean startDate1 = (cd1.equals(start) || cd1.after(start));
                    Boolean endDate1 = (cd1.equals(end) || cd1.before(end));
                    if(!(startDate1 && endDate1)){
                        iterator.remove();
                    }
                }
            } catch (Exception e) {
                log.info("日期判断错误！",e);
                cdList = (List<CountDo>) resultMap.get("result");
            }
            count = cdList.size();
        }
        for (CountDo cd : cdList) {
            dateList.add(cd.getGroupByStr());
            ReportStatisticsForDateDTO rsfdd = new ReportStatisticsForDateDTO();
            rsfdd.setCreatedDate(cd.getGroupByStr());
            rsfdd.setAllNum(cd.getCount());
            rsfdd.setPbcNum(0L);//设置默认值
            rsfdd.setPbcNumAll(0L);
            rsfdd.setEccsNum(0L);
            rsfdd.setEccsNumAll(0L);
            rsfddList.add(rsfdd);
        }

        List<CountDo> pbcAllList = this.getStatisticsForDatePbcEccs(null, allStatusList, null, dateList, organFullId);
        List<CountDo> pbcList = this.getStatisticsForDatePbcEccs(null, successStatusList, null, dateList, organFullId);
        List<CountDo> eccsAllList = this.getStatisticsForDatePbcEccs(null, null, allStatusList, dateList, organFullId);
        List<CountDo> eccsList = this.getStatisticsForDatePbcEccs(null, null, successStatusList, dateList, organFullId);

        for (ReportStatisticsForDateDTO rsfdd : rsfddList) {
            for (CountDo cd : pbcAllList) {
                if (cd.getGroupByStr().equals(rsfdd.getCreatedDate())) {
                    rsfdd.setPbcNumAll(cd.getCount());
                }
            }
            for (CountDo cd : pbcList) {
                if (cd.getGroupByStr().equals(rsfdd.getCreatedDate())) {
                    rsfdd.setPbcNum(cd.getCount());
                }
            }
            for (CountDo cd : eccsAllList) {
                if (cd.getGroupByStr().equals(rsfdd.getCreatedDate())) {
                    rsfdd.setEccsNumAll(cd.getCount());
                }
            }
            for (CountDo cd : eccsList) {
                if (cd.getGroupByStr().equals(rsfdd.getCreatedDate())) {
                    rsfdd.setEccsNum(cd.getCount());
                }
            }
        }
        return new TableResultResponse<>(count, rsfddList);
    }

    @Override
    public ReportStatisticsForDateDTO statisticsForDate(String date, String organFullId) {
        List<CompanySyncStatus> allStatusList = new ArrayList<CompanySyncStatus>() {
            {
                add(CompanySyncStatus.weiTongBu);
                add(CompanySyncStatus.tongBuChengGong);
                add(CompanySyncStatus.tongBuShiBai);
            }
        };
        List<CompanySyncStatus> successStatusList = new ArrayList<CompanySyncStatus>() {
            {
                add(CompanySyncStatus.tongBuChengGong);
            }
        };

        CountDo resultAll = this.getStatisticsForDateSingle(null,null,null,date,organFullId);
        if (resultAll == null) {
            return null;
        } else {
            CountDo resultPbcAll = this.getStatisticsForDateSingle(null, allStatusList, null, date, organFullId);
            CountDo resultPbc = this.getStatisticsForDateSingle(null, successStatusList, null, date, organFullId);
            CountDo resultEccsAll = this.getStatisticsForDateSingle(null, null, allStatusList, date, organFullId);
            CountDo resultEccs = this.getStatisticsForDateSingle(null, null, successStatusList, date, organFullId);
            ReportStatisticsForDateDTO dto = new ReportStatisticsForDateDTO();
            dto.setCreatedDate(resultAll.getGroupByStr());
            dto.setAllNum(resultAll.getCount());
            dto.setPbcNumAll(resultPbcAll == null ? 0 : resultPbcAll.getCount());
            dto.setPbcNum(resultPbc == null ? 0 : resultPbc.getCount());
            dto.setEccsNumAll(resultEccsAll == null ? 0 : resultEccsAll.getCount());
            dto.setEccsNum(resultEccs == null ? 0 : resultEccs.getCount());
            return dto;
        }
    }

    @Override
    public TableResultResponse<AllBillsPublicSearchDTO> statisticsForDateDetailList(AccountBillsAllSearchInfo info, Pageable pageable) {
        Page<AccountBillsAll> page = accountBillsAllDao.findAll(new AccountBillsAllSearchSpec(info), pageable);
        long count = accountBillsAllDao.count(new AccountBillsAllSearchSpec(info));
        List<AccountBillsAll> accountBillsAllList = page.getContent();

        List<AllBillsPublicSearchDTO> accountVoList = this.billResultListInit(accountBillsAllList);
        return new TableResultResponse<>((int) count, accountVoList);
    }

    /**
     * 对流水列表信息进行包装，根据外键获取其他表信息
     */
    private List<AllBillsPublicSearchDTO> billResultListInit(List<AccountBillsAll> accountBillsAllList) {
        List<AllBillsPublicSearchDTO> accountVoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(accountBillsAllList)) {
            AllBillsPublicDTO allBillsPublicDTO = new AllBillsPublicDTO();

            for (AccountBillsAll accountBilsAll : accountBillsAllList) {
                BeanUtils.copyProperties(accountBilsAll, allBillsPublicDTO);
                allBillsPublicDTO.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                Long accountId = accountBilsAll.getAccountId();
                Long customerLogId = accountBilsAll.getCustomerLogId();
                Date createdDate = accountBilsAll.getCreatedDate();

                convertAllBillsPublic(accountId, accountBilsAll.getId(), customerLogId, createdDate, allBillsPublicDTO);
                //根据fullId获取核心机构号
                AllBillsPublicSearchDTO allBillsPublicSearchDTO = new AllBillsPublicSearchDTO();
                BeanUtils.copyProperties(allBillsPublicDTO, allBillsPublicSearchDTO);

                /**
                 * 江南银行赋值下载状态初始化
                 */
                if (accountBilsAll.getDownloadstatus()==null||accountBilsAll.getDownloadstatus().equals("")){
                    allBillsPublicSearchDTO.setDownloadstatus("未下载");
                }else {
                    allBillsPublicSearchDTO.setDownloadstatus("已下载");
                }


                /**
                 * 江南银行赋值下载状态初始化
                 */
                if (accountBilsAll.getUploadstatus()==null||accountBilsAll.getUploadstatus().equals("")){
                    allBillsPublicSearchDTO.setUploadstatus("未上传");
                }else {
                    allBillsPublicSearchDTO.setUploadstatus("已上传");
                }

                if (accountBilsAll.getImgaeSyncStatus() != null) {
                    allBillsPublicSearchDTO.setImgaeSyncStatus(accountBilsAll.getImgaeSyncStatus());
                }
                if (StringUtils.isNotBlank(allBillsPublicDTO.getOrganFullId())) {
                    OrganizationDto od = organizationService.findByOrganFullId(allBillsPublicDTO.getOrganFullId());
                    if (od != null) {
                        allBillsPublicSearchDTO.setKernelOrgCode(od.getCode());
                    }
                }
                accountVoList.add(allBillsPublicSearchDTO);
                allBillsPublicDTO = new AllBillsPublicDTO();
            }
        }
        return accountVoList;
    }

    @Override
    public void setCancelHeZhun(AllBillsPublicDTO billsPublic){
        //判断该机构是否走取消核准接口
        if(billsPublic.getBillType() != BillType.ACCT_INIT) {
//            if (billsPublic.getCancelHeZhun() == null || !billsPublic.getCancelHeZhun()) {
            OrganRegisterDto organRegisterDto = null;
            if (StringUtils.isNotBlank(billsPublic.getOrganCode())) {
                log.info("organCode取消核准机构查询");
                organRegisterDto = organRegisterService.queryByOrganCode(billsPublic.getOrganCode());
            } else {
                log.info("bankCode取消核准机构查询");
                organRegisterDto = organRegisterService.query(billsPublic.getBankCode());
            }
            AccountConfigureDto accountConfigureDto = null;
            AmsAccountInfo amsAccountInfo = null;
            if (organRegisterDto != null) {
                log.info("机构为取消核准机构");
                OrganizationDto organizationDto = organizationService.findById(organRegisterDto.getOrganId());
                //开户存款人类别传进来进行查询
                if (billsPublic.getAcctType() == CompanyAcctType.jiben) {
                    if(billsPublic.getBillType() == BillType.ACCT_OPEN){
                        log.info("基本户查询：acctType:" + billsPublic.getAcctType().toString() + ";depositorName:" + billsPublic.getDepositorType() + ";billType:" +billsPublic.getBillType().toString());
                        if(StringUtils.isNotBlank(billsPublic.getDepositorType())){
                            accountConfigureDto = accountConfigureService.query(billsPublic.getAcctType().toString(), billsPublic.getDepositorType(), billsPublic.getBillType().toString());
                            //dto保存，方便后面转allAcct时取值
                            if (accountConfigureDto != null) {
                                log.info("该账户为取消核准账户");
                                billsPublic.setCancelHeZhun(true);
                            } else {
                                log.info("该账户不是取消核准账户");
                                billsPublic.setCancelHeZhun(false);
                            }
                        }else{
                            billsPublic.setCancelHeZhun(null);
                        }
                    }else if(billsPublic.getBillType() == BillType.ACCT_CHANGE || billsPublic.getBillType() == BillType.ACCT_REVOKE){
                        //有存款人类别的情况下直接判断
                        if(StringUtils.isNotBlank(billsPublic.getDepositorType())){
                            String depositorType = StringUtils.trim(billsPublic.getDepositorType());
                            String[] cancelHeZhunType = {"企业法人", "非法人企业", "有字号的个体工商户", "无字号的个体工商户", "01", "02", "13", "14"};
                            billsPublic.setCancelHeZhun(Arrays.asList(cancelHeZhunType).contains(depositorType));
                        }else{
                            //变更销户查询人行信息进行判断是都是取消核准的账户
                            log.info("基本户账号：" + billsPublic.getAcctNo() + "进行" + billsPublic.getBillType() + ",查询人行信息");
                            try {
                                amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(organizationDto.getCode(),billsPublic.getAcctNo());
//                                    amsAccountInfo = JSON.parseObject("{\"accountKey\":\"J3910017538803\",\"accountLicenseNo\":\"J3910017538803\",\"accountStatus\":\"normal\",\"acctCreateDate\":\"2013-07-15\",\"acctName\":\"福州合众人力资源服务有限公司\",\"acctNo\":\"100050803350010001\",\"acctType\":\"jiben\",\"bankCode\":\"313391080015\",\"bankName\":\"福建海峡银行股份有限公司营业部\",\"businessScope\":\"人力资源服务；劳务派遣（不含涉外业务）；人力资源信息咨询；室内保洁服务；企业事务代理；家政服务（不含职业中介）；劳务分包；劳动政策咨询；劳务事务代理；装卸和搬运；货物运输代理服务；物业管理；仓储服务；快递服务；寄递服务（信件和其它具有信件性质的物品除外）；会议及展览服务；企业管理信息咨询；市场调查；财税信息咨询；企业营销策划；教育信息咨询；通信工程的设计、施工；计算机信息技术服务；通讯设备的批发、代购代销、维修；第二类增值电信业务中的信息服务业；提供法律信息咨询。（依法须经批准的项目，经相关部门批准后方可开展经营活动）\",\"cancelDate\":\"-\",\"depositorName\":\"福州合众人力资源服务有限公司\",\"depositorType\":\"有字号的个体工商户\",\"fileNo\":\"91350102MA2XNJQH25\",\"fileNo2\":\"\",\"fileType\":\"工商营业执照\",\"fileType2\":\"\",\"industryCode\":\"居民服务和其他服务业\",\"legalIdcardNo\":\"350102198507051937\",\"legalIdcardType\":\"身份证\",\"legalName\":\"周云峰\",\"noTaxProve\":\"\",\"orgCode\":\"MA2XNJQH2\",\"parAccountKey\":\"\",\"parCorpName\":\"\",\"parLegalIdcardNo\":\"\",\"parLegalIdcardType\":\"\",\"parLegalName\":\"\",\"parOrgCode\":\"\",\"regAddress\":\"福建省福州市鼓楼区温泉街道东大路36号花开富贵1#A座19层14E室\",\"regAreaCode\":\"391000\",\"regCurrencyType\":\"人民币\",\"registeredCapital\":\"2000000.00\",\"stateTaxRegNo\":\"91350102MA2XNJQH25\",\"taxRegNo\":\"91350102MA2XNJQH25\",\"telephone\":\"13625028113\",\"zipCode\":\"350000\"}",AmsAccountInfo.class);
                            } catch (Exception e) {
                                log.error("基本户账号：" + billsPublic.getAcctNo() + ",人行查询失败：" + e.getMessage());
                                throw new RuntimeException("基本户账号：" + billsPublic.getAcctNo() + ",人行查询失败：" + e.getMessage());
                            }
                            if(amsAccountInfo != null  ){
                                log.info("人行查询返回对象amsAccountInfo：" + JSON.toJSONString(amsAccountInfo));
                                if(StringUtils.isNotBlank(amsAccountInfo.getDepositorName())){
                                    log.info("基本户"+amsAccountInfo.getDepositorName()+"查询人行信息成功，基本户存款人类别：" + amsAccountInfo.getDepositorType());
                                    //人行数据能查到
                                    String depositorType = StringUtils.trim(amsAccountInfo.getDepositorType());
                                    String[] cancelHeZhunType = {"企业法人", "非法人企业", "有字号的个体工商户", "无字号的个体工商户", "01", "02", "13", "14"};
                                    billsPublic.setCancelHeZhun(Arrays.asList(cancelHeZhunType).contains(depositorType));
                                    if(StringUtils.isBlank(billsPublic.getDepositorType()) && billsPublic.getBillType() == BillType.ACCT_REVOKE){
                                        billsPublic.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)", depositorType));
                                    }
                                } else {
                                    log.info("该账户不是取消核准账户");
                                    billsPublic.setCancelHeZhun(null);
                                }
                            }else{
                                log.info("基本户查询开户为空，CancelHeZhun设置为空");
                                billsPublic.setCancelHeZhun(null);
                            }
                        }
                    }
                }
                if (billsPublic.getAcctType() == CompanyAcctType.feilinshi) {
                    log.info("非临时账户查询是否取消核准账户");
                    PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(organizationDto.getCode(), EAccountType.AMS);
                    if (pbcAccountDto != null) {
                        AmsCheckResultInfo amsCheckResultInfo = null;
                        //当基本户开户许可证跟开户地区代码不为空的情况去查询
                        if(StringUtils.isNotBlank(billsPublic.getAccountKey()) && StringUtils.isNotBlank(billsPublic.getBasicAcctRegArea())){
                            try {
                                log.info("非临时账户通过基本户许可证地区代码查询该基本户信息......");
                                amsCheckResultInfo = pbcAmsService.checkPbcByAccountKeyAndRegAreaCode(organizationDto.getCode(), billsPublic.getAccountKey(), billsPublic.getBasicAcctRegArea());
//                                amsCheckResultInfo = amsMainService.checkPbcByAccountKeyAndRegAreaCode(allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto), billsPublic.getAccountKey(), billsPublic.getRegAreaCode());
                            } catch (Exception e) {
                                log.error("基本存款账户编号：" + billsPublic.getAccountKey() + ",非临时取消核准基本户人行查询：" + e.getMessage());
                                throw new RuntimeException("基本存款账户编号：" + billsPublic.getAccountKey() + ",非临时取消核准基本户人行查询失败：" + e.getMessage());
                            }
                            //人行数据能查到
                            if (amsCheckResultInfo != null  ) {
                                if(amsCheckResultInfo.getAmsAccountInfo() != null && StringUtils.isNotBlank(amsCheckResultInfo.getAmsAccountInfo().getDepositorName())){
                                    log.info("人行查询返回对象amsAccountInfo：" + JSON.toJSONString(amsCheckResultInfo.getAmsAccountInfo()));
                                    log.info("非临时账户基本户"+amsCheckResultInfo.getAmsAccountInfo().getDepositorName()+"查询人行信息成功，基本户存款人类别：" + amsCheckResultInfo.getAmsAccountInfo().getDepositorType());
                                    //人行数据能查到
                                    String depositorType = StringUtils.trim(amsCheckResultInfo.getAmsAccountInfo().getDepositorType());
                                    String[] cancelHeZhunType = {"企业法人", "非法人企业", "有字号的个体工商户", "无字号的个体工商户", "01", "02", "13", "14"};
                                    boolean isCancelHeZhun = Arrays.asList(cancelHeZhunType).contains(depositorType);
                                    log.info("该账户是否取消核准账户:" + isCancelHeZhun);
                                    billsPublic.setCancelHeZhun(isCancelHeZhun);
                                } else {
                                    log.info("该账户不是取消核准账户");
                                    billsPublic.setCancelHeZhun(null);
                                }
                            } else {
                                log.info("非临时人行查询为空，CancelHeZhun设置为空");
                                billsPublic.setCancelHeZhun(null);
                            }
                        } else if(StringUtils.isNotEmpty(billsPublic.getAcctNo())){
                            try {
                                log.info("非临时账户通过账号查询该基本户信息......");
                                amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(organizationDto.getCode(), billsPublic.getAcctNo());//获取人行数据
//                                    amsAccountInfo = JSON.parseObject("{\"accountKey\":\"J3052011001201\",\"accountLicenseNo\":\"J3052011001201\",\"accountStatus\":\"normal\",\"acctCreateDate\":\"2012-07-27\",\"acctName\":\"昆山谛镨科自动化科技有限公司\",\"acctNo\":\"3052244012015000000911\",\"acctType\":\"jiben\",\"bankCode\":\"314305200193\",\"bankName\":\"昆山农村商业银行股份有限公司张浦支行\",\"businessScope\":\"电子专用设备、测试仪器、工模具、精密在线测试仪器及设备的研发、制造、销售及上门维护；五金机电、电子材料、合成板的销售；货物及技术的进口业务。（依法须经批准的项目，经相关部门批准后方可开展经营活动）\",\"cancelDate\":\"-\",\"depositorName\":\"昆山谛镨科自动化科技有限公司\",\"depositorType\":\"企业法人\",\"fileNo\":\"91320583050268697R\",\"fileNo2\":\"\",\"fileType\":\"工商营业执照\",\"fileType2\":\"\",\"industryCode\":\"制造业\",\"legalIdcardNo\":\"04391030\",\"legalIdcardType\":\"港、澳、台居民通行证\",\"legalName\":\"赖俊宏\",\"noTaxProve\":\"\",\"orgCode\":\"050268697\",\"parAccountKey\":\"\",\"parCorpName\":\"\",\"parLegalIdcardNo\":\"\",\"parLegalIdcardType\":\"\",\"parLegalName\":\"\",\"parOrgCode\":\"\",\"regAddress\":\"张浦镇永燃路115号（1号、2号房）\",\"regAreaCode\":\"305200\",\"regCurrencyType\":\"人民币\",\"registeredCapital\":\"3000000.00\",\"stateTaxRegNo\":\"91320583050268697R\",\"taxRegNo\":\"91320583050268697R\",\"telephone\":\"13862667599\",\"zipCode\":\"215321\"}",AmsAccountInfo.class);
                                if(amsAccountInfo != null && StringUtils.isNotEmpty(amsAccountInfo.getDepositorType())){
                                    log.info("人行查询返回对象amsAccountInfo：" + JSON.toJSONString(amsAccountInfo));
                                    log.info("非临时账户{}人行查询存款人类别为{}",billsPublic.getAcctNo(),amsAccountInfo.getDepositorType());
                                    String[] cancelHeZhunType = {"企业法人", "非法人企业", "有字号的个体工商户", "无字号的个体工商户", "01", "02", "13", "14"};
                                    boolean isCancelHeZhun = Arrays.asList(cancelHeZhunType).contains(amsAccountInfo.getDepositorType());
                                    log.info("该账户是否取消核准账户:" + isCancelHeZhun);
                                    billsPublic.setCancelHeZhun(isCancelHeZhun);
                                }else{
                                    log.info("非临时账户{}人行查询失败",billsPublic.getAcctNo());
                                    billsPublic.setCancelHeZhun(null);
                                }
                            } catch (Exception e) {
                                log.error("非临时账户{}根据账号查询人行失败：" + billsPublic.getAcctNo(),e.getMessage());
                                throw new RuntimeException("非临时账户查询取消核准根据账号查询人行失败：" + e.getMessage());
                            }
                        } else{
                            log.info("非临时上报基本户开户许可证或开户地区代码为空，无法查询该非临时户基本户存款人类别；");
                        }
                    }else{
                        log.info("人行2级用户不能为空");
                        throw new BizServiceException(EErrorCode.ORGAN_NOTCONFIG, "人行2级用户不能为空");
                    }
                }
            }else {
                log.info("机构为非取消核准机构");
                billsPublic.setCancelHeZhun(false);
            }
//            }else{
//                if(billsPublic.getCancelHeZhun() != null){
//                    log.info("查询是否取消核准字段不为空：cancelHezhun：" + billsPublic.getCancelHeZhun());
//                }
//            }
        }
    }

    @Override
    public void dataCoverage(AllBillsPublicDTO dbBillsPublicDTO, AllBillsPublicDTO billsPublicDTO) {
        if (dbBillsPublicDTO != null && billsPublicDTO != null) {
            BeanUtils.copyProperties(dbBillsPublicDTO, billsPublicDTO, CompareUtils.getIsNotNullAndEmptyPropertyNames(billsPublicDTO));
        }
    }

    @Override
    public AllBillsPublicDTO findFullInfoByBillNo(String billNo) {
        AccountBillsAll accountBillsAll = accountBillsAllDao.findByBillNo(billNo);
        return convertBill2Full(accountBillsAll);
    }

    @Override
    public IExcelExport statisticsForDateDetailsExport(AccountBillsAllSearchInfo accountBillsAllSearchInfo) {
        IExcelExport iExcelExport = new StatisticsForDatePoiExport();
        List<StatisticsForDatePoi> companyAccountPois = new ArrayList<>();

        List<AccountBillsAll> accountBillsAllList= accountBillsAllDao.findAll(new AccountBillsAllSearchSpec(accountBillsAllSearchInfo));
        List<AllBillsPublicSearchDTO> accountVoList = this.billResultListInit(accountBillsAllList);
        for(AllBillsPublicSearchDTO allBillsPublicSearchDTO : accountVoList){
            StatisticsForDatePoi statisticsForDatePoi = new StatisticsForDatePoi();
            BeanUtils.copyProperties(allBillsPublicSearchDTO,statisticsForDatePoi);
            statisticsForDatePoi.setOrganCode(allBillsPublicSearchDTO.getKernelOrgCode());
            statisticsForDatePoi.setBillType(allBillsPublicSearchDTO.getBillType().getValue());
            statisticsForDatePoi.setPbcSyncStatus(allBillsPublicSearchDTO.getPbcSyncStatus().getValue());
            statisticsForDatePoi.setEccsSyncStatus(allBillsPublicSearchDTO.getEccsSyncStatus().getValue());
            statisticsForDatePoi.setAcctType(allBillsPublicSearchDTO.getAcctType().getValue());
            companyAccountPois.add(statisticsForDatePoi);
        }
        iExcelExport.setPoiList(companyAccountPois);
        return iExcelExport;
    }

    /**
     *
     * @param accountBillsAllSearchInfo
     * @return
     */
    @Override
    public IExcelExport statisticsForKXHtailsExport(AccountBillsAllSearchInfo accountBillsAllSearchInfo) {
        IExcelExport iExcelExport = new StatisticsForKXHPoiExport();
        List<StatisticsForKXHPoi> companyAccountPois = new ArrayList<>();

        List<AccountBillsAll> accountBillsAllList= accountBillsAllDao.findAll(new AccountBillsAllSearchSpec(accountBillsAllSearchInfo));
        List<AllBillsPublicSearchDTO> accountVoList = this.billResultListInit(accountBillsAllList);
        for(AllBillsPublicSearchDTO allBillsPublicSearchDTO : accountVoList){
            StatisticsForKXHPoi statisticsForKXHPoi = new StatisticsForKXHPoi();
            BeanUtils.copyProperties(allBillsPublicSearchDTO,statisticsForKXHPoi);
            statisticsForKXHPoi.setOrganCode(allBillsPublicSearchDTO.getKernelOrgCode());
            statisticsForKXHPoi.setBillType(allBillsPublicSearchDTO.getBillType().getValue());
            statisticsForKXHPoi.setPbcSyncStatus(allBillsPublicSearchDTO.getPbcSyncStatus().getValue());
            statisticsForKXHPoi.setEccsSyncStatus(allBillsPublicSearchDTO.getEccsSyncStatus().getValue());
            statisticsForKXHPoi.setAcctType(allBillsPublicSearchDTO.getAcctType().getValue());
            statisticsForKXHPoi.setPbcSyncMethod(allBillsPublicSearchDTO.getPbcSyncMethod().getValue());
            statisticsForKXHPoi.setOpenAccountSiteType(allBillsPublicSearchDTO.getOpenAccountSiteType() == null ? "未知" : allBillsPublicSearchDTO.getOpenAccountSiteType().getValue());
            companyAccountPois.add(statisticsForKXHPoi);
        }
        iExcelExport.setPoiList(companyAccountPois);
        return iExcelExport;
    }

    /**
     * 上报统计查询jpa公共部分
     *
     * @param acctTypeList       要查询的账户类型（预留参数，为null时，查询所有）
     * @param pbcSyncStatusList  要查询的人行上报状态（预留参数，为null时，查询所有）
     * @param eccsSyncStatusList 要查询的信用代码证上报状态（预留参数，为null时，查询所有）
     * @param dateList           指定的日期（预留参数，为null时，查询所有）
     * @param organFullId        机构权限控制
     */
    private CriteriaQuery<CountDo> createStatisticsForDateJpaSelect(List<CompanyAcctType> acctTypeList,
                                                                    List<CompanySyncStatus> pbcSyncStatusList,
                                                                    List<CompanySyncStatus> eccsSyncStatusList,
                                                                    List<String> dateList,
                                                                    String organFullId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        //OrderSum指定了查询结果返回至自定义对象
        CriteriaQuery<CountDo> query = cb.createQuery(CountDo.class);
        Root<ReportStatus> root = query.from(ReportStatus.class);
        Path<String> organFullIdPath = root.get("organFullId");
        Path<String> createdDatePath = root.get("createdDate");

        List<Predicate> predicateList = new ArrayList<Predicate>();
        predicateList.add(cb.like(organFullIdPath, organFullId + "%"));
        if (dateList != null && dateList.size() > 0) {
            List<Predicate> list = new ArrayList<>();
            CriteriaBuilder.In<String> acctTypeIn = cb.in(createdDatePath);
            for (String acctType : dateList) {
                acctTypeIn.value(acctType);
            }
            list.add(acctTypeIn);
            Predicate[] p = new Predicate[list.size()];
            predicateList.add(cb.and(list.toArray(p)));
        }
        if (acctTypeList != null && acctTypeList.size() > 0) {
            List<Predicate> list = new ArrayList<>();
            CriteriaBuilder.In<CompanyAcctType> acctTypeIn = cb.in(root.<CompanyAcctType>get("acctType"));
            for (CompanyAcctType acctType : acctTypeList) {
                acctTypeIn.value(acctType);
            }
            list.add(acctTypeIn);
            Predicate[] p = new Predicate[list.size()];
            Predicate and = cb.and(list.toArray(p));
            predicateList.add(and);
        }
        if (pbcSyncStatusList != null && pbcSyncStatusList.size() > 0) {
            List<Predicate> list = new ArrayList<>();
            CriteriaBuilder.In<CompanySyncStatus> acctTypeIn = cb.in(root.<CompanySyncStatus>get("pbcSyncStatus"));
            for (CompanySyncStatus acctType : pbcSyncStatusList) {
                acctTypeIn.value(acctType);
            }
            list.add(acctTypeIn);
            Predicate[] p = new Predicate[list.size()];
            Predicate and = cb.and(list.toArray(p));
            predicateList.add(and);
        }
        if (eccsSyncStatusList != null && eccsSyncStatusList.size() > 0) {
            List<Predicate> list = new ArrayList<>();
            CriteriaBuilder.In<CompanySyncStatus> acctTypeIn = cb.in(root.<CompanySyncStatus>get("eccsSyncStatus"));
            for (CompanySyncStatus acctType : eccsSyncStatusList) {
                acctTypeIn.value(acctType);
            }
            list.add(acctTypeIn);
            Predicate[] p = new Predicate[list.size()];
            Predicate and = cb.and(list.toArray(p));
            predicateList.add(and);
        }
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        query.where(predicates);

        query.multiselect(createdDatePath, cb.count(root).as(Long.class));
        query.groupBy(createdDatePath);
        query.orderBy(cb.desc(createdDatePath));
        return query;
    }

    /**
     * 查询所有的报送统计数据
     *
     * @param acctTypeList       要查询的账户类型（预留参数，为null时，查询所有）
     * @param pbcSyncStatusList  要查询的人行上报状态（预留参数，为null时，查询所有）
     * @param eccsSyncStatusList 要查询的信用代码证上报状态（预留参数，为null时，查询所有）
     * @param dateList           指定的日期（预留参数，为null时，查询所有）
     * @param organFullId        机构权限控制
     * @param pageable           分页参数
     */
    private Map<String, Object> getStatisticsForDateAll(List<CompanyAcctType> acctTypeList,
                                                        List<CompanySyncStatus> pbcSyncStatusList,
                                                        List<CompanySyncStatus> eccsSyncStatusList,
                                                        List<String> dateList,
                                                        String organFullId,
                                                        Pageable pageable) {
        CriteriaQuery<CountDo> query = this.createStatisticsForDateJpaSelect(acctTypeList, pbcSyncStatusList, eccsSyncStatusList, dateList, organFullId);
        TypedQuery<CountDo> typedQuery = em.createQuery(query);
        List<CountDo> resultCount = typedQuery.getResultList();
        int count = resultCount.size();
        typedQuery.setFirstResult(pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<CountDo> result = typedQuery.getResultList();
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("result", result);
        return map;
    }

    /**
     * 查询人行和信用代码证报送统计数据
     *
     * @param acctTypeList       要查询的账户类型（预留参数，为null时，查询所有）
     * @param pbcSyncStatusList  要查询的人行上报状态
     * @param eccsSyncStatusList 要查询的信用代码证上报状态
     * @param dateList               指定的日期
     * @param organFullId        机构权限控制
     */
    private List<CountDo> getStatisticsForDatePbcEccs(List<CompanyAcctType> acctTypeList,
                                                      List<CompanySyncStatus> pbcSyncStatusList,
                                                      List<CompanySyncStatus> eccsSyncStatusList,
                                                      List<String> dateList,
                                                      String organFullId) {
        CriteriaQuery<CountDo> query = this.createStatisticsForDateJpaSelect(acctTypeList, pbcSyncStatusList, eccsSyncStatusList, dateList, organFullId);

        TypedQuery<CountDo> typedQuery = em.createQuery(query);
        List<CountDo> result = typedQuery.getResultList();
        return result;
    }

    /**
     * 查询指定日期的报送统计数据
     *
     * @param acctTypeList       要查询的账户类型（预留参数，为null时，查询所有）
     * @param pbcSyncStatusList  要查询的人行上报状态
     * @param eccsSyncStatusList 要查询的信用代码证上报状态
     * @param date               指定的日期
     * @param organFullId        机构权限控制
     */
    private CountDo getStatisticsForDateSingle(final List<CompanyAcctType> acctTypeList,
                                               final List<CompanySyncStatus> pbcSyncStatusList,
                                               final List<CompanySyncStatus> eccsSyncStatusList,
                                               String date,
                                               String organFullId) {
        List<String> dateList = new ArrayList<>();
        dateList.add(date);
        CriteriaQuery<CountDo> query = this.createStatisticsForDateJpaSelect(acctTypeList, pbcSyncStatusList, eccsSyncStatusList, dateList, organFullId);
        TypedQuery<CountDo> typedQuery = em.createQuery(query);
        CountDo result = null;
        try {
            result = typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return result;
    }
    /**
     * 记录操作日志
     * @param formData
     * @param refBillId
     */
    public void writeLog(Map<String, String> formData,Long refBillId){
        OperateLogDto operateLogDto = new OperateLogDto();
        operateLogDto.setRefBillId(refBillId);
        operateLogDto.setOperateType(formData.get("action"));
        operateLogDto.setFailMsg(formData.get("denyReason"));
        operateLogService.updateAndSave(operateLogDto);
    }

    public void pbc2ConverDto(AllBillsPublicDTO dto,List<String> changeFields) throws Exception{
        //变更账户人行信息补全除变更字段外的其余字段后上报人行
        OrganizationDto organizationDto = organizationService.findByOrganFullId(dto.getOrganFullId());
        if (organizationDto == null) {
            throw new BizServiceException(EErrorCode.ORGAN_NOTCONFIG, "未配置机构");
        }
        //备案类账户进行人行查询  覆盖除变更字段外其余字段  备案类账户全字段上传
        try{
            //找出变更字段，人行覆盖其他字段
            List<String> changeAmsSyncFieldList = null;
            //查询人行信息
            AmsAccountInfo amsAccountInfo = null;
            AmsAccountInfo changeAmsAccount = null;
            try {
                amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(organizationDto.getId(), dto.getAcctNo());
//                amsAccountInfo = JSON.parseObject("{\"accountKey\":\"J3910017538803\",\"accountLicenseNo\":\"J3910017538803\",\"accountStatus\":\"normal\",\"acctCreateDate\":\"2013-07-15\",\"acctName\":\"福州合众人力资源服务有限公司\",\"acctNo\":\"100050803350010001\",\"acctType\":\"jiben\",\"bankCode\":\"313391080015\",\"bankName\":\"福建海峡银行股份有限公司营业部\",\"businessScope\":\"(取消开户许可证核发)人力资源服务；劳务派遣（不含涉外业务）；人力资源信息咨询；室内保洁服务；企业事务代理；家政服务（不含职业中介）；劳务分包；劳动政策咨询；劳务事务代理；装卸和搬运；货物运输代理服务；物业管理；仓储服务；快递服务；寄递服务（信件和其它具有信件性质的物品除外）；会议及展览服务；企业管理信息咨询；市场调查；财税信息咨询；企业营销策划；教育信息咨询；通信工程的设计、施工；计算机信息技术服务；通讯设备的批发、代购代销、维修；第二类增值电信业务中的信息服务业；提供法律信息咨询。（依法须经批准的项目，经相关部门批准后方可开展经营活动）\",\"cancelDate\":\"-\",\"depositorName\":\"福州合众人力资源服务有限公司\",\"depositorType\":\"企业法人\",\"fileNo\":\"91350102MA2XNJQH25\",\"fileNo2\":\"\",\"fileType\":\"工商营业执照\",\"fileType2\":\"\",\"industryCode\":\"居民服务和其他服务业\",\"legalIdcardNo\":\"350102198507051937\",\"legalIdcardType\":\"身份证\",\"legalName\":\"周云峰\",\"noTaxProve\":\"\",\"orgCode\":\"MA2XNJQH2\",\"parAccountKey\":\"\",\"parCorpName\":\"\",\"parLegalIdcardNo\":\"\",\"parLegalIdcardType\":\"\",\"parLegalName\":\"\",\"parOrgCode\":\"\",\"regAddress\":\"福建省福州市鼓楼区温泉街道东大路36号花开富贵1#A座19层14E室\",\"regAreaCode\":\"391000\",\"regCurrencyType\":\"人民币\",\"registeredCapital\":\"2000000.00\",\"stateTaxRegNo\":\"91350102MA2XNJQH25\",\"taxRegNo\":\"91350102MA2XNJQH25\",\"telephone\":\"13625028113\",\"zipCode\":\"350000\"}",AmsAccountInfo.class);
                if(amsAccountInfo!= null && StringUtils.isBlank(amsAccountInfo.getLegalType())
                        && dto.getAcctType() == CompanyAcctType.jiben && (dto.getCancelHeZhun() != null && dto.getCancelHeZhun())){
                    log.info("使用变更接口查询法人类型");
                    try{
                        //增加查询条件
                        AllAcct allAcct = new AllAcct();
                        allAcct.setAcctNo(dto.getAcctNo());
                        allAcct.setBankCode(dto.getBankCode());
                        allAcct.setBankName(dto.getBankName());
                        changeAmsAccount = pbcAmsService.getAmsAccountInfoByAcctNoFromChangeHtml(organizationDto, allAcct);
                        if(changeAmsAccount!= null && StringUtils.isNotBlank(changeAmsAccount.getLegalType())){
                            log.info("法人类型：" + changeAmsAccount.getLegalType());
                            amsAccountInfo.setLegalType(dictionaryService.transalteLike("法人类型",changeAmsAccount.getLegalType()));
                        }
                        if(changeAmsAccount!= null && StringUtils.isNotBlank(changeAmsAccount.getParLegalType())){
                            log.info("上级法人类型：" + changeAmsAccount.getParLegalType());
                            amsAccountInfo.setParLegalType(dictionaryService.transalteLike("法人类型",changeAmsAccount.getParLegalType()));
                        }
                        if(changeAmsAccount!= null){
                            if(StringUtils.isNotBlank(changeAmsAccount.getTovoidDate())){
                                log.info("营业执照到期日：" + changeAmsAccount.getTovoidDate());
                                amsAccountInfo.setTovoidDate(changeAmsAccount.getTovoidDate());
                            }else{
                                log.info("营业执照到期日为空;" );
                                amsAccountInfo.setTovoidDate("");
                            }
                        }
                        if(changeAmsAccount!= null && StringUtils.isNotBlank(changeAmsAccount.getIsIdentification())){
                            log.info("未标明注册资金：" + changeAmsAccount.getIsIdentification());
                            amsAccountInfo.setIsIdentification(changeAmsAccount.getIsIdentification());
                        }
                    }catch (Exception e){
                        log.info("账号：" + dto.getAcctNo() + "使用变更接口查询人行详情失败！" + e.getMessage());
                        if(e.getMessage().contains("久悬")){
                            throw new BizServiceException(EErrorCode.PBC_SYNC_ACCTTYPE_ERROR,e.getMessage());
                        }
                    }
                }
            }catch (Exception e){
                log.info("账号：" + dto.getAcctNo() + "查询人行详情失败！" + e.getMessage());
                throw new BizServiceException(EErrorCode.PBC_SYNC_ACCTTYPE_ERROR,"账号：" + dto.getAcctNo() + "查询人行详情失败！"+e.getMessage());
            }

            if(amsAccountInfo != null && amsAccountInfo.getAccountStatus() != com.ideatech.ams.pbc.enums.AccountStatus.normal) {
                log.info("变更业务人行返回账户状态为非正常：" + JSON.toJSONString(amsAccountInfo));
                if(amsAccountInfo.getAccountStatus() == com.ideatech.ams.pbc.enums.AccountStatus.notExist) {
                    throw new BizServiceException(EErrorCode.PBC_QUERY_PARAM_EMPTY, "账号：" + dto.getAcctNo() + "查询人行不存在,无法进行变更！");
                } else {
                    throw new BizServiceException(EErrorCode.PBC_QUERY_PARAM_EMPTY, "账号：" + dto.getAcctNo() + "查询人行账户状态为非正常,无法进行变更！");
                }
            }
            //人行数据补t+1文件除变更字段外的字段
            if(amsAccountInfo != null){
                log.info("变更业务人行amsAccount对象查询打印：" + JSON.toJSONString(amsAccountInfo));
                if(StringUtils.isNotBlank(amsAccountInfo.getRegAddress())){
                    amsAccountInfo.setRegAddress(HtmlUtils.htmlUnescape(amsAccountInfo.getRegAddress()));
                }
                //根据账户性质获取变更上报字段
                changeAmsSyncFieldList = allBillsPublicService.getChangeFieldsNeedPbcSync(dto);
                try{
                    //去掉变更字段  其余字段已人行查询回来的值进行覆盖，确保跟人行保持一致
                    //另：法人类型，营业执照有效期，上级法人类型  amsAccountInfo人行详情无该字段  在dto本地有值的情况下保持原有值上报
                    changeAmsSyncFieldList.removeAll(changeFields);
                    //未标明注册资金
                    //说明变更字段是有未标明注册资金
                    if(changeFields.contains("isIdentification")){
                        //判断dto中的未标明注册资金是不是跟changeAmsAccount中的未标明注册资金一致
                        if(!dto.getIsIdentification().equals(changeAmsAccount.getIsIdentification())){
                            //说明两边不一致
                            if("1".equals(dto.getIsIdentification())){
                                //未标明注册资金勾选  币种跟资金置空
                                dto.setRegCurrencyType("");
                                dto.setRegisteredCapital(null);
                                //amsAccountInfo返回对象置空币种，注册资金
                                amsAccountInfo.setRegCurrencyType("");
                                amsAccountInfo.setRegisteredCapital("");
                            }
                        }
                    }
                    //人行obj转map  方便拿值
                    Map<String, String>  amsMap = org.apache.commons.beanutils.BeanUtils.describe(amsAccountInfo);

                    for(String converField : changeAmsSyncFieldList){
                        String pbcValue = amsMap.get(converField) == null ? "" : amsMap.get(converField).toString().trim();
                        if(StringUtils.isBlank(pbcValue)){
                            if("acctFileNo".equals(converField)){
                                pbcValue = amsMap.get("accountFileNo") == null ? "" : amsMap.get("accountFileNo").toString().trim();
                            }else if("acctFileType".equals(converField)){
                                pbcValue = amsMap.get("accountFileType") == null ? "" : amsMap.get("accountFileType").toString().trim();
                            }else if("acctFileNo2".equals(converField)){
                                pbcValue = amsMap.get("accountFileNo2") == null ? "" : amsMap.get("accountFileNo2").toString().trim();
                            }else if("regFullAddress".equals(converField)){
                                //人行字段为regAddress
                                pbcValue = amsMap.get("regAddress") == null ? "" : amsMap.get("regAddress").toString().trim();
                            }else if("zipcode".equals(converField)){
                                //人行字段为zipCode
                                pbcValue = amsMap.get("zipCode") == null ? "" : amsMap.get("zipCode").toString().trim();
                            }else if("registeredCapital".equals(converField)){
                                //基本户注册资金如果查询回来是""  默认为null 后台换allacct是会转为“” 继续下一个字段
                                dto.setRegisteredCapital(null);
                                continue;
                            }
                            //增加法人类型，营业执照有效期，上级法人类型字段判断  人行详情无法查询到  在dto本地有值的情况下保持原有值上报
                            else if("legalType".equals(converField)){
                                if(StringUtils.isNotBlank(dto.getLegalType()) && StringUtils.isBlank(pbcValue)){
                                    continue;
                                }
                            }else if("parLegalType".equals(converField)){
                                if(StringUtils.isNotBlank(dto.getParLegalType()) && StringUtils.isBlank(pbcValue)){
                                    continue;
                                }
                            }else if("fileDue".equals(converField)){
                                pbcValue = amsMap.get("tovoidDate") == null ? "" : amsMap.get("tovoidDate").toString().trim();
//                                //如果人行没有值的话覆盖本地账管营业执照有效期
//                                if(StringUtils.isNotBlank(dto.getFileDue()) && StringUtils.isBlank(pbcValue)){
//                                    continue;
//                                }
                            }else if("insideDeptName".equals(converField)){//内设部门名称
                                pbcValue = amsMap.get("insideDepartmentName") == null ? "" : amsMap.get("insideDepartmentName").toString();
                                if(StringUtils.isNotBlank(dto.getInsideDeptName()) && StringUtils.isBlank(pbcValue)){
                                    continue;
                                }
                            }else if("insideLeadName".equals(converField)){//内设部门负责人姓名
                                pbcValue = amsMap.get("insideSaccdepmanName") == null ? "" : amsMap.get("insideSaccdepmanName").toString();
                                if(StringUtils.isNotBlank(dto.getInsideLeadName()) && StringUtils.isBlank(pbcValue)){
                                    continue;
                                }
                            }else if("insideLeadIdcardType".equals(converField)){//内设部门负责人姓名证件种类
                                pbcValue = amsMap.get("insideSaccdepmanKind") == null ? "" : amsMap.get("insideSaccdepmanKind").toString();
                                if(StringUtils.isNotBlank(dto.getInsideLeadIdcardType()) && StringUtils.isBlank(pbcValue)){
                                    continue;
                                }
                            }else if("insideLeadIdcardNo".equals(converField)){//内设部门负责人证件编号
                                pbcValue = amsMap.get("insideSaccdepmanNo") == null ? "" : amsMap.get("insideSaccdepmanNo").toString();
                                if(StringUtils.isNotBlank(dto.getInsideLeadIdcardNo()) && StringUtils.isBlank(pbcValue)){
                                    continue;
                                }
                            }else if("insideTelephone".equals(converField)){//内设部门联系电话
                                pbcValue = amsMap.get("insideTelphone") == null ? "" : amsMap.get("insideTelphone").toString();
                                if(StringUtils.isNotBlank(dto.getInsideTelephone()) && StringUtils.isBlank(pbcValue)){
                                    continue;
                                }
                            }else if("insideZipcode".equals(converField)){//内设部门邮编
                                pbcValue = amsMap.get("insideZipCode") == null ? "" : amsMap.get("insideZipCode").toString();
                                if(StringUtils.isNotBlank(dto.getInsideZipcode()) && StringUtils.isBlank(pbcValue)){
                                    continue;
                                }
                            }else if("insideZipcode".equals(converField)){//内设部门邮编
                                pbcValue = amsMap.get("insideZipCode") == null ? "" : amsMap.get("insideZipCode").toString();
                                if(StringUtils.isNotBlank(dto.getInsideZipcode()) && StringUtils.isBlank(pbcValue)){
                                    continue;
                                }
                            }else if("fundManager".equals(converField)){//资金管理人姓名
                                pbcValue = amsMap.get("moneyManager") == null ? "" : amsMap.get("moneyManager").toString();
                                if(StringUtils.isNotBlank(dto.getFundManager()) && StringUtils.isBlank(pbcValue)){
                                    continue;
                                }
                            }else if("fundManagerIdcardType".equals(converField)){//资金管理人证件类型
                                pbcValue = amsMap.get("moneyManagerCtype") == null ? "" : amsMap.get("moneyManagerCtype").toString();
                                if(StringUtils.isNotBlank(dto.getFundManagerIdcardType()) && StringUtils.isBlank(pbcValue)){
                                    continue;
                                }
                            }else if("fundManagerIdcardNo".equals(converField)){//资金管理人证件号码
                                pbcValue = amsMap.get("moneyManagerCno") == null ? "" : amsMap.get("moneyManagerCno").toString();
                                if(StringUtils.isNotBlank(dto.getFundManagerIdcardNo()) && StringUtils.isBlank(pbcValue)){
                                    continue;
                                }
                            }
                        }
                        if("businessScope".equals(converField)){
                            if(StringUtils.isNotBlank(pbcValue) && pbcValue.contains("取消开户许可证核发")){
                                pbcValue = pbcValue.replace("(取消开户许可证核发)","");
                            }
                        }
                        //下拉框字段转义
                        pbcValue = getPbc2DtoValue(pbcValue,dto,converField);
                        //人行字段赋值给DTO
                        if("请选择省".equals(pbcValue)){
                            log.info("字段：" + converField + "PbcValue值为：" + pbcValue + "，pbcValue字段置空为空字符串！");
                            pbcValue = "";
                        }
                        BeanValueUtils.setValue(dto,converField,pbcValue);
                    }
                }catch (Exception e){
                    throw new BizServiceException(EErrorCode.PBC_SYNC_ACCTTYPE_ERROR,"备案类账户人行字段覆盖DTO字段错误,"+e.getMessage());
                }

            }
        }catch (Exception e){
            log.error("变更备案类账户:"+e);
            throw new Exception("变更备案类账户失败，" + e.getMessage());
        }
    }

    public String getPbc2DtoValue(String pbcValue,AllBillsPublicDTO dto,String converField){
        if(dto.getAcctType() == CompanyAcctType.yiban) {
            if("acctFileType".equals(converField)){
                if(StringUtils.isNotBlank(pbcValue)){
                    pbcValue = dictionaryService.transalteLike("证明文件1种类(一般户)",pbcValue);
                }
            }
        }
        if(dto.getAcctType() == CompanyAcctType.feiyusuan) {
            if("acctFileType".equals(converField)){
                if(StringUtils.isNotBlank(pbcValue)){
                    pbcValue = dictionaryService.transalteLike("证明文件1种类(专用户)",pbcValue);
                }
            }
            if("acctFileType2".equals(converField)){
                if(StringUtils.isNotBlank(pbcValue)){
                    pbcValue = dictionaryService.transalteLike("证明文件2种类(专用户)",pbcValue);
                }
            }
            //法人证件类型
            if("fundManagerIdcardType".equals(converField)){
                if(StringUtils.isNotBlank(pbcValue)){
                    pbcValue = dictionaryService.transalteLike("法人身份证件类型",pbcValue);
                }
            }
            if("insideLeadIdcardType".equals(converField)){
                if(StringUtils.isNotBlank(pbcValue)){
                    pbcValue = dictionaryService.transalteLike("法人身份证件类型",pbcValue);
                }
            }
        }
        if(dto.getAcctType() == CompanyAcctType.feilinshi) {
            if("acctFileType".equals(converField)){
                if(StringUtils.isNotBlank(pbcValue)){
                    pbcValue = dictionaryService.transalteLike("证明文件1种类(非临时)",pbcValue);
                }
            }
        }
        if("fileType".equals(converField)){
            if(StringUtils.isNotBlank(pbcValue)){
                pbcValue = dictionaryService.transalteLike("证明文件1种类(基本户)",pbcValue);
            }
        }
        if("fileType2".equals(converField)){
            if(StringUtils.isNotBlank(pbcValue)){
                pbcValue = dictionaryService.transalteLike("证明文件2种类(基本户)",pbcValue);
            }
        }
        //法人证件类型
        if("legalIdcardType".equals(converField)){
            if(StringUtils.isNotBlank(pbcValue)){
                pbcValue = dictionaryService.transalteLike("法人身份证件类型",pbcValue);
            }
        }
        //币种
        if("regCurrencyType".equals(converField)){
            if(StringUtils.isNotBlank(pbcValue)){
                pbcValue = dictionaryService.transalteLike("注册币种",pbcValue);
            }
        }
        //行业归属
        if("industryCode".equals(converField)){
            String value = null;
            if(StringUtils.isNotBlank(pbcValue)){
                for (String str:pbcValue.split(",")) {
                    if(StringUtils.isBlank(value)){
                        value= dictionaryService.transalteLike("行业归属",str);
                    }else{
                        value=value+","+ dictionaryService.transalteLike("行业归属",str);
                    }
                }
            }
            pbcValue = value;
        }
        //账户名称构成方式
        if("accountNameFrom".equals(converField)){
            if(StringUtils.isNotBlank(pbcValue)){
                pbcValue = dictionaryService.transalteLike("账户构成方式",pbcValue);
            }
        }
        //资金性质
        if("capitalProperty".equals(converField)){
            if(StringUtils.isNotBlank(pbcValue)){
                pbcValue = dictionaryService.transalteLike("资金性质",pbcValue);
            }
        }
        //上级法人证件类型
        if("parLegalIdcardType".equals(converField)){
            if(StringUtils.isNotBlank(pbcValue)){
                pbcValue = dictionaryService.transalteLike("法人身份证件类型",pbcValue);
            }
        }
        return pbcValue;
    }

    /**
     * 预约新模式人行对人行上报结果的数据处理
     * @param isSyncSuccess
     * @param billsPublic
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    public void applyFinalStatusUpdate(Boolean isSyncSuccess, AllBillsPublicDTO billsPublic) {
        Boolean flag = false;  //是否匹配到预约数据
        String preOpenAcctId = billsPublic.getPreOpenAcctId();
        String applyId = "";
        CompanyPreOpenAccountEntDto result = null;
        OrganizationDto organDto = null;

        if(StringUtils.isNotBlank(billsPublic.getOrganCode())) {
            organDto = organizationService.findByCode(billsPublic.getOrganCode());
        } else {
            organDto = organizationService.findByOrganFullId(billsPublic.getOrganFullId());
        }

        billsPublic.setApplyorganid(organDto.getPbcCode());

        try {
            if(StringUtils.isNotBlank(preOpenAcctId)) {
                result = companyPreOpenAccountEntService.selectOne(Long.valueOf(billsPublic.getPreOpenAcctId()));
                if(result != null) { //本地有预约数据
                    flag = true;
                    applyId = result.getApplyid();
                } else {
                    log.error("传入的预约编号无法找到预约信息");
                    return;
                }
            }
            if (isSyncSuccess) {  //上报成功
                if(!flag) {
                    CryptoAddApplyAcctVo cryptoAddApplyAcctVo = copyAddApplyAcctVo(billsPublic);
                    companyPreOpenAccountEntService.applyOpenValidater(cryptoAddApplyAcctVo);
                    applyId = pushApplyAddAcct(cryptoAddApplyAcctVo, organDto);
                }

                if(StringUtils.isNotBlank(applyId)) {
                    editApplyStatus(applyId, billsPublic, result, ApplyEnum.REGISTER_SUCCESS);
                }
            } else { //上报失败
                if(flag) { //匹配到预约数据
                    editApplyStatus(applyId, billsPublic, result, ApplyEnum.REGISTER_FAIL);
                }
            }
        } catch (Exception e) {
            log.error("预约账户补录接口调用异常", e);
        }
    }


    /**
     * 修改预约状态为开户成功或失败
     * @param applyId
     * @param billsPublic
     * @param result
     * @param status
     * @throws Exception
     */
    private void editApplyStatus(String applyId, AllBillsPublicDTO billsPublic, CompanyPreOpenAccountEntDto result, ApplyEnum status) throws Exception {
        if(result != null && !"SUCCESS".equalsIgnoreCase(result.getStatus())) {  //本地有预约数据
            log.error("受理成功的预约数据才能进行预约单修改");
            return;
        }

        if(result == null) {  //本地无预约数据强制更新成受理成功
            sendModifyStatus(applyId, ApplyEnum.SUCCESS, billsPublic.getApplyorganid());
        }
        CryptoModifyAcctKeyMessage acctKeyMessage = new CryptoModifyAcctKeyMessage();

        if (status == ApplyEnum.REGISTER_SUCCESS) { //开户成功操作修改预约单
            CryptoEditApplyAcctVo cryptoEditApplyAcctVo = copyEditApplyAcctVo(applyId, billsPublic);
            companyPreOpenAccountEntService.applyEditValidater(cryptoEditApplyAcctVo);
            log.info("修改预约单接口输入参数:" + JSON.toJSONString(cryptoEditApplyAcctVo));
            pushApplyEditAcct(cryptoEditApplyAcctVo);
        }

        //修改状态为开户成功或失败(开户成功或开户失败且本地存在预约信息)
        if(status == ApplyEnum.REGISTER_SUCCESS || (status == ApplyEnum.REGISTER_FAIL && result != null)) {
            sendModifyStatus(applyId, status, billsPublic.getApplyorganid());
        }

        if(status == ApplyEnum.REGISTER_SUCCESS) { //开户成功推送核准号
            if(CompanyAcctType.yiban != billsPublic.getAcctType() && CompanyAcctType.feiyusuan != billsPublic.getAcctType()
                    && StringUtils.isNotBlank(billsPublic.getAccountKey())) {
                acctKeyMessage.setApplyId(applyId);
                acctKeyMessage.setOrganId(billsPublic.getApplyorganid());
                acctKeyMessage.setAccountKey(billsPublic.getAccountKey());
                log.info("开户成功推送核准号接口输入参数:" + JSON.toJSONString(acctKeyMessage));
                pushApplyAcctKey(acctKeyMessage);
            }
        }

    }

    /**
     * 预约新增接口赋值
     * @param billsPublicDTO
     * @return
     */
    private CryptoAddApplyAcctVo copyAddApplyAcctVo(AllBillsPublicDTO billsPublicDTO) {
        CryptoAddApplyAcctVo applyAcctVo = new CryptoAddApplyAcctVo();

        BeanValueUtils.copyProperties(billsPublicDTO, applyAcctVo);
        applyAcctVo.setName(billsPublicDTO.getDepositorName());
        applyAcctVo.setBillType(com.ideatech.common.enums.BillType.str2enum(billsPublicDTO.getBillType().name()));
        applyAcctVo.setType(com.ideatech.common.enums.CompanyAcctType.str2enum(billsPublicDTO.getAcctType().name()));

        OrganizationDto rootOrgan = organizationService.findTopPbcCode(rsaCode.getOrganid());
        if(rootOrgan != null) {
            applyAcctVo.setBank(rootOrgan.getName());
        }
        applyAcctVo.setBranch(billsPublicDTO.getBankName());

        applyAcctVo.setOrganId(billsPublicDTO.getApplyorganid());
        applyAcctVo.setOperator(billsPublicDTO.getApplyOperator());
        applyAcctVo.setPhone(billsPublicDTO.getApplyPhone());
        applyAcctVo.setApplyDate(DateUtils.DateToStr(new Date(), ""));
        applyAcctVo.setOperator(billsPublicDTO.getLegalName());
        applyAcctVo.setPhone(billsPublicDTO.getLegalTelephone());

        applyAcctVo.setCancelReason(billsPublicDTO.getAcctCancelReason());

        return applyAcctVo;
    }

    /**
     * 预约修改接口赋值
     * @param billsPublicDTO
     * @return
     */
    private CryptoEditApplyAcctVo copyEditApplyAcctVo(String applyId, AllBillsPublicDTO billsPublicDTO) {
        CryptoEditApplyAcctVo applyAcctVo = new CryptoEditApplyAcctVo();

        BeanValueUtils.copyProperties(billsPublicDTO, applyAcctVo);
        applyAcctVo.setAcctType(com.ideatech.common.enums.CompanyAcctType.str2enum(billsPublicDTO.getAcctType().name()));
        if(StringUtils.isBlank(billsPublicDTO.getAcctName())) {
            applyAcctVo.setAcctName(billsPublicDTO.getDepositorName());
        }

        CompanyPreOpenAccountEntDto result = companyPreOpenAccountEntService.findByApplyid(applyId);
        if(result != null) {
            applyAcctVo.setApplyId(result.getApplyid());
            applyAcctVo.setOrganId(billsPublicDTO.getApplyorganid());
            applyAcctVo.setFirstSupplyOperator(result.getAccepter());
        }
        if(StringUtils.isBlank(applyId)) {  //客户临柜开户
            if(!DateUtils.isDateFormat(billsPublicDTO.getAcctCreateDate(), "yyyy-MM-dd HH:mm:ss")) {
                applyAcctVo.setBankApplyTime(billsPublicDTO.getAcctCreateDate() + " 00:00:00");
            }
        } else {
            if(StringUtils.isNotBlank(result.getBanktime())) {
                applyAcctVo.setBankApplyTime(result.getBanktime() + " " + result.getTimes());
            } else { //调用新增预约单接口--自动生成
                applyAcctVo.setBankApplyTime(DateUtils.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
        }

        if(StringUtils.isBlank(applyAcctVo.getFirstSupplyOperator())) { //调用新增预约单接口--自动生成
            applyAcctVo.setFirstSupplyOperator("virtual");
        }

        if(StringUtils.isNotBlank(billsPublicDTO.getIndustryCode())) {
            applyAcctVo.setIndustryCode(billsPublicDTO.getIndustryCode().substring(0, 1));
        }
        applyAcctVo.setAuthOperator1Phone(billsPublicDTO.getOperatorTelephone());
        applyAcctVo.setAuthOperator1Name(billsPublicDTO.getOperatorName());
        applyAcctVo.setAuthOperator1IdcardNo(billsPublicDTO.getOperatorIdcardNo());
        applyAcctVo.setAuthOperator1IdcardType(billsPublicDTO.getOperatorIdcardType());

        return applyAcctVo;
    }

    //预约新模式---上报后发起预约单补录接口
    private void applyFinalStatusUpdateExecutor(Boolean isPbcSyncFail, Boolean pbcSuccess, AllBillsPublicDTO billsPublic) {
        Boolean isSyncSuccess = false;
        if(!isPbcSyncFail && pbcSuccess) {
            isSyncSuccess = true;
        } else if(isPbcSyncFail && !pbcSuccess) {
            isSyncSuccess = false;
        } else {  //人行未上报
            return;
        }

        ApplyFinalStatusUpdateExecutor excutor = new ApplyFinalStatusUpdateExecutor();
        excutor.setBillsPublic(billsPublic);
        excutor.setIsSyncSuccess(isSyncSuccess);
        excutor.setAllBillsPublicService(allBillsPublicService);
        taskExecutor.execute(excutor);
    }

    public static void main(String[] args) {
        AllBillsPublicDTO allBillsPublicDTO = new AllBillsPublicDTO();
        allBillsPublicDTO.setAcctType(CompanyAcctType.str2enum("feilinshi"));// 账户性质
        PbcAccountDto pbcAccountDto = new PbcAccountDto();
        UserDto userDto = new UserDto();
        new AllBillsPublicServiceImpl().switchAcctType(pbcAccountDto,allBillsPublicDTO,userDto);
    }

    @Override
    public List<String> getNeedOpenFiledList(AllBillsPublicDTO allBillsPublicDTO, AllBillsPublicDTO billsPublic) {
        List<String> getNewOpenFiledList = new ArrayList<String>();
        allBillsPublicDTO.setRefBillId(billsPublic.getRefBillId());
        Map<String, String> oldMap = null;
        Map<String, String> newMap = null;
        final Map<String, Object> beforeChangeFieldValueMap = new HashMap<>(16);
        final Map<String, Object> afterChangeFieldValueMap = new HashMap<>(16);
        List<String> sameChangeFieldValueSet = new ArrayList<>(16);
        final List<String> changFieldNameList = new ArrayList<>(16);
        try {
            oldMap = org.apache.commons.beanutils.BeanUtils.describe(allBillsPublicDTO);
            newMap = org.apache.commons.beanutils.BeanUtils.describe(billsPublic);

        } catch (Exception e) {
            log.error("对象转化为map失败", e);
        }
        List<String> changeFields = new ArrayList<>(newMap.keySet());
        List<String> ignoreFidlds = new ArrayList<>(16);
        ignoreField(ignoreFidlds);
        changeFields.removeAll(ignoreFidlds);
        CompareUtils.compare(oldMap, newMap, changeFields, beforeChangeFieldValueMap, afterChangeFieldValueMap, sameChangeFieldValueSet, changFieldNameList, new HashMap<String, String>(), false);
        if (changFieldNameList != null && changFieldNameList.size() != 0) {
            getNewOpenFiledList = getNeedOpenFiledList(changFieldNameList);
        }
        return getNewOpenFiledList;
    }

    /**
     * 返回变更需要重新开户的字段
     *
     * @param fields
     * @return
     */
    private List<String> getNeedOpenFiledList(List<String> fields) {
        List<String> newOpenFiledList = new ArrayList<String>();
        //一下字段需要先销户再开户，如果包含则直接无需同步
        List<String> needFields = Arrays.asList(new String[]{"depositorType", "regAreaCode"});
        List<String> needNewOpenFields = Arrays.asList(new String[]{"acctType", "depositorType", "organCode", "regAreaCode"});
        //无存量数据变更判断注册地区代码跟存款人类别变更需要先肖后开
        if (!datenbestand) {
            for (String needNewOpenField : needFields) {
                if (fields.contains(needFields)) {
                    newOpenFiledList.add(needNewOpenField);
                }
            }
        } else {
            //有存量数据的情况下都判断
            for (String needNewOpenField : needNewOpenFields) {
                if (fields.contains(needNewOpenField)) {
                    newOpenFiledList.add(needNewOpenField);
                }
            }
        }
        return newOpenFiledList;
    }

}
