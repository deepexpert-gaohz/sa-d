package com.ideatech.ams.account.service.bill;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dao.AccountPublicLogDao;
import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.dao.AllBillsPublicDao;
import com.ideatech.ams.account.dao.OpenAccountStatisticsDao;
import com.ideatech.ams.account.dao.bill.AccountBillsAllDao;
import com.ideatech.ams.account.dao.bill.spec.AccountBillsAllSearchSpec;
import com.ideatech.ams.account.dao.spec.OpenAccountStatisticsSpec;
import com.ideatech.ams.account.dto.AccountBillsAllSearchInfo;
import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.OpenAccountStatisticsSearchDto;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.entity.AccountPublicLog;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.OuterSysCode;
import com.ideatech.ams.account.enums.bill.*;
import com.ideatech.ams.account.executor.CoreBillsUpdateExecutor;
import com.ideatech.ams.account.service.AccountPublicService;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.enums.ApplyEnum;
import com.ideatech.ams.apply.service.CompanyPreOpenAccountEntService;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.customer.service.CustomersAllService;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.enums.AccountStatus;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.permission.service.PermissionService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.lang.Long.parseLong;

/**
 * @author vantoo
 * @date 10:10 2018/5/28
 */
@Service
//@Transactional
@Slf4j
public class AccountBillsAllServiceImpl implements AccountBillsAllService {

    @Autowired
    private AccountBillsAllDao accountBillsAllDao;

    @Autowired
    private AccountsAllDao accountsAllDao;

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionUtils transactionUtils;

    @Autowired
    private AllBillsPublicDao allBillsPublicDao;

    @Autowired
    private PbcAmsService pbcAmsService;

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private AccountPublicService accountPublicService;

    @Autowired
    private CompanyPreOpenAccountEntService companyPreOpenAccountEntService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AmsMainService amsMainService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private AccountPublicLogDao accountPublicLogDao;

    @Autowired
    private OpenAccountStatisticsDao openAccountStatisticsDao;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private CustomerPublicService customerPublicService;

    @Autowired
    private CustomersAllService customersAllService;

    @Autowired
    private CustomerPublicLogService customerPublicLogService;

    @Autowired
    private ThreadPoolTaskExecutor pbcCoverCoreExecutor;

    /**
     * 是否启用人行限制采集机制
     */
    @Value("${ams.company.pbcCollectionLimit.use:false}")
    private Boolean pbcCollectionLimitUse;

    /**
     * 人行限制采集数量
     */
    @Value("${ams.company.pbcCollectionLimit.num:10000}")
    private Long pbcCollectionLimitNum;


    private List<Future<Long>> futureCoreBillsUpdateList;

    @Override
    public AccountBillsAllInfo getOne(Long id) {
        return po2dto(accountBillsAllDao.findOne(id));
    }

    @Override
    public void save(AccountBillsAllInfo accountBillsAllInfo) {
        AccountBillsAll accountBillsAll = null;
        if (accountBillsAllInfo.getId() != null) {
            accountBillsAll = accountBillsAllDao.findOne(accountBillsAllInfo.getId());
        }
        if (accountBillsAll == null) {
            accountBillsAll = new AccountBillsAll();
        }
        BeanCopierUtils.copyProperties(accountBillsAllInfo, accountBillsAll);
        log.info("存款人类别---------" + accountBillsAll.getDepositorType());
        accountBillsAll = accountBillsAllDao.save(accountBillsAll);
        accountBillsAllInfo.setId(accountBillsAll.getId());
    }

    @Override
    public long countUnfinishedByAccountId(Long accountId) {
        return accountBillsAllDao.countByFinalStatusAndAccountId(CompanyIfType.No, accountId);
    }

    @Override
    public AccountBillsAllInfo findLatestUnfinishedByAcctNo(String acctNo, BillType billType) {
        AccountBillsAll accountBillsAll = accountBillsAllDao.findTopByFinalStatusAndAcctNoAndBillTypeOrderByCreatedDateDesc(CompanyIfType.No, acctNo, billType);
        return po2dto(accountBillsAll);

    }

    @Override
    public AccountBillsAllInfo findLatestFinishedByAcctNo(String acctNo) {
        AccountBillsAll accountBillsAll = accountBillsAllDao.findTopByFinalStatusAndAcctNoOrderByCreatedDateDesc(CompanyIfType.Yes, acctNo);
        return po2dto(accountBillsAll);
    }

    @Override
    public AccountBillsAllInfo findLatestUnFinishedByAcctNo(String acctNo) {
        AccountBillsAll accountBillsAll = accountBillsAllDao.findTopByFinalStatusAndAcctNoOrderByCreatedDateDesc(CompanyIfType.No, acctNo);
        return po2dto(accountBillsAll);
    }

    @Override
    public long countUnfinishedByAcctNo(String acctNo) {
        return accountBillsAllDao.countByFinalStatusAndAcctNo(CompanyIfType.No, acctNo);
    }

    @Override
    public long count(BillType billType, String organFullId, String billBeginDate, String billEndDate,
                      List<CompanySyncStatus> pbcSyncStatusList, List<CompanySyncStatus> eccsSyncStatusList, List<CompanyAmsCheckStatus> pbcCheckStatusList, String acctType) {
        AccountBillsAllSearchInfo abasi = new AccountBillsAllSearchInfo();
        if (StringUtils.isNotBlank(billBeginDate)) {
            try {
                abasi.setBeginDate(DateUtils.parse(billBeginDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            } catch (ParseException e) {
                log.error("日期转换错误！", e);
            }
        }
        if (StringUtils.isNotBlank(billEndDate)) {
            try {
                abasi.setEndDate(DateUtils.parse(billEndDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
            } catch (ParseException e) {
                log.error("日期转换错误！", e);
            }
        }
        abasi.setBillType(billType);
        abasi.setOrganFullId(organFullId);
        abasi.setPbcSyncStatuses(pbcSyncStatusList);
        abasi.setEccsSyncStatuses(eccsSyncStatusList);
        abasi.setPbcCheckStatuses(pbcCheckStatusList);
        abasi.setAcctType(CompanyAcctType.str2enum(acctType));

        return accountBillsAllDao.count(new AccountBillsAllSearchSpec(abasi));
    }

    @Override
    public long countByBillTypeAndOrganFullIdStartsWithAndBillDateBetween(BillType billType, String organFullId, String createddatestart, String createddateend) {
        return accountBillsAllDao.countByBillTypeAndOrganFullIdStartsWithAndBillDateBetween(billType, organFullId, createddatestart, createddateend);
    }

    @Override
    public long countByBillTypeAndOrganFullIdStartsWith(BillType billType, String organFullId) {
        return accountBillsAllDao.countByBillTypeAndOrganFullIdStartsWith(billType, organFullId);
    }

    @Override
    public long countById(Long id) {
        return accountBillsAllDao.countById(id);
    }

    @Override
    public int updateFinalStatus(Long id) {
        AccountBillsAll bills = accountBillsAllDao.findOne(id);
        bills.setFinalStatus(CompanyIfType.Yes);
        accountBillsAllDao.save(bills);
        return 1;
        //return accountBillsAllDao.updateFinalStatus(id);
    }

    @Override
    public String getActiveDate(Date date) {
        /*try {
            int hzPlusDays = systemConfigService.getSystemConfig(SystemConfigItem.HZ_PLUS_DAYS, Integer.class);
            Date activeDate = holidayService.addWorkday(DateFormatUtils.ISO_DATE_FORMAT.format(date), hzPlusDays);
            return DateFormatUtils.ISO_DATE_FORMAT.format(activeDate);
        } catch (Exception e) {
            LOG.error("获取节假日期异常", e);
        }*/
        return "";
    }

    /**
     * @param billId 流水id, sysCode 系统标识编a码 , status 同步状态, SyncMethod 上报方式 msg 同步信息
     * @author jogy.he
     * @description 更新系统同步状态 pbc人行 eccs信用代码证系统 core核心系统
     */
    @Override
    public void updateSyncStatus(Long billId, OuterSysCode sysCode, CompanySyncStatus status, CompanySyncOperateType syncMethod, String msg, Long userId) {
        try {
            if (billId == null) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请输入billId!");
            }
            if (sysCode == null) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请输入系统编码!");
            }
            if (status == null) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请输入更新状态!");
            }
            if (userId == null) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请输入操作员id!");
            }
            AccountBillsAll bills = accountBillsAllDao.findOne(billId);

            String syncDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            if (bills != null && bills.getId() > 0) {
                if (sysCode.equals(OuterSysCode.PBC)) {
                    // 人行同步状态
                    bills.setPbcSyncStatus(status);
                    if (status != CompanySyncStatus.buTongBu) {
                        bills.setPbcSyncTime(syncDate);
                    }
                    bills.setPbcOperator(userId);
                    bills.setPbcSyncMethod(syncMethod);
                    bills.setPbcSyncError(msg);
                    bills.setLastUpdateBy(userId + "");
                    bills.setLastUpdateDate(new Date());
                    accountBillsAllDao.save(bills);

                } else if (sysCode.equals(OuterSysCode.ECCS)) {
                    // 信用代码证同步状态
                    bills.setEccsSyncStatus(status);
                    if (status != CompanySyncStatus.buTongBu) {
                        bills.setEccsSyncTime(syncDate);
                    }
                    bills.setEccsOperator(userId);
                    bills.setEccsSyncError(msg);
                    bills.setLastUpdateBy(userId + "");
                    bills.setLastUpdateDate(new Date());
                    accountBillsAllDao.save(bills);
                } else {
                    //TODO 同步记录
                    // 其它系统同步状态需更新SyncOuterSystem信息
                    /*SyncOuterSystemInfo syncOuterSystemInfo = syncOuterSystemService.findByBillIdAndSyncCode(billId, sysCode);

                    if (syncOuterSystemInfo != null && syncOuterSystemInfo.getId() > 0) {
                        syncOuterSystemInfo.setSyncStatus(status);
                        syncOuterSystemInfo.setSyncTime(syncDate);
                        syncOuterSystemInfo.setOperator(userId);
                        syncOuterSystemInfo.setSyncError(msg);
                        syncOuterSystemInfo.setDescription(description);
                        syncOuterSystemInfo.setLastUpdateBy(userId);
                        syncOuterSystemInfo.setLastUpdateDate(new Date());
                        syncOuterSystemService.save(syncOuterSystemInfo);
                    } else {
                        SyncOuterSystemInfo syncOuterSystemInfo1 = new SyncOuterSystemInfo();
                        syncOuterSystemInfo1.setBillId(billId);
                        syncOuterSystemInfo1.setSyncCode(sysCode);
                        syncOuterSystemInfo1.setCreatedBy(userId);
                        syncOuterSystemInfo1.setSyncStatus(status);
                        syncOuterSystemInfo1.setSyncTime(syncDate);
                        syncOuterSystemInfo1.setOperator(userId);
                        syncOuterSystemInfo1.setSyncError(msg);
                        syncOuterSystemInfo1.setDescription(description);
                        syncOuterSystemInfo1.setLastUpdateBy(userId);
                        syncOuterSystemInfo1.setLastUpdateDate(new Date());
                        syncOuterSystemService.save(syncOuterSystemInfo1);
                    }*/

                }
            } else {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "不存在到对应的流水信息！");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePbcCheckStatus(Long billId, CompanyAmsCheckStatus status) {
        try {
            if (billId == null) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请输入billId!");
            }
            /*if (status == null) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请输入更新状态!");
            }*/
            AccountBillsAll bills = accountBillsAllDao.findOne(billId);

            String syncDate = DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis());
            if (bills != null && bills.getId() > 0) {
                bills.setPbcCheckStatus(status);
                //成功才会更新时间
                if (status == CompanyAmsCheckStatus.CheckPass) {
                    bills.setPbcCheckDate(syncDate);
                }
                accountBillsAllDao.save(bills);
            } else {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "不存在到对应的流水信息！");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启新事物更新状态
     *
     * @param billId
     * @param pbcSyncStatus
     * @param eccsSyncStatus
     * @param pbcCheckStatus
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updatePbcEccsCheckStatus(Long billId, CompanySyncStatus pbcSyncStatus, CompanySyncStatus eccsSyncStatus, CompanyAmsCheckStatus pbcCheckStatus) {
        try {
            if (billId == null) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请输入billId!");
            }
            AccountBillsAll bills = accountBillsAllDao.findOne(billId);

            if (bills != null && bills.getId() > 0) {
                bills.setPbcSyncStatus(pbcSyncStatus);
                bills.setEccsSyncStatus(eccsSyncStatus);
                bills.setPbcCheckStatus(pbcCheckStatus);
                accountBillsAllDao.save(bills);
            } else {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "不存在到对应的流水信息！");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AccountBillsAllInfo getByCustomerNo(String customerNo) {
        return po2dto(accountBillsAllDao.findByCustomerNo(customerNo));
    }

    @Override
    public AccountBillsAllInfo getByBillNo(String billNo) {
        return po2dto(accountBillsAllDao.findByBillNo(billNo));
    }

    @Override
    public void updateBillStatus(Long billId, BillStatus status, Long userId, String description) {
        try {
            if (billId == null) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请输入billId!");
            }
            if (status == null) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请输入流水状态信息!");
            }
            if (userId == null) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请输入操作员id!");
            }

            AccountBillsAll bills = accountBillsAllDao.findOne(billId);

            String apprDate = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());

            if (bills != null && bills.getId() > 0) {
                bills.setStatus(status);
                bills.setApproveDate(apprDate);
                bills.setApprover(userId);
                // 增加单独退回原因栏位
                if (status == BillStatus.REJECT) {
                    bills.setDenyReason(description);
                } else {
                    bills.setApproveDesc(description);
                }
                bills.setLastUpdateBy(userId + "");
                bills.setLastUpdateDate(new Date());
                log.info("查询日志方法:updateBillStatus：accountKey:" + bills.getAccountKey() + ";selectPwd:" + bills.getSelectPwd() + ";openKey:" + bills.getOpenKey());
                accountBillsAllDao.save(bills);
            } else {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "不存在到对应的流水信息！");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AccountBillsAllInfo> getAllAccountBillsAllInfo() {
        List<AccountBillsAllInfo> accountBillsInfoList = new ArrayList<>();
        List<AccountBillsAll> accountBillsList = accountBillsAllDao.findAll();

        if (accountBillsList != null && accountBillsList.size() != 0) {
            for (AccountBillsAll accountBillsAll : accountBillsList) {
                accountBillsInfoList.add(po2dto(accountBillsAll));
            }
        }

        return accountBillsInfoList;
    }

    @Override
    public Boolean isCheckAcctNoAndCustomerNo(String acctNo, String customerNo, String billNo) {
        Long count1 = accountBillsAllDao.countByBillNoAndAcctNo(billNo, acctNo);
        Long count2 = accountBillsAllDao.countByBillNoAndCustomerNo(billNo, customerNo);

        if (count1 > 1 || count2 > 1) {
            return false;
        }

        return true;
    }

    @Override
    public void autoSync() {
        //找到待补录中上报失败的数据
        List<AccountBillsAllInfo> billsInfos = getUnSyncSuccessBill();
        if (CollectionUtils.isNotEmpty(billsInfos)) {
            for (AccountBillsAllInfo billsInfo : billsInfos) {
                AllBillsPublicDTO billsPublicDTO = allBillsPublicService.findOne(billsInfo.getId());
                try {
                    sync(billsPublicDTO);
                } catch (Exception e) {
                    log.error("定时自动报送账户异常", e);
                }
            }
        }

    }

    protected void sync(AllBillsPublicDTO billsPublic) throws Exception {
        //保存
        UserDto userDto = null;
        String currentUserName = SecurityUtils.getCurrentUsername();
        if (StringUtils.isNotBlank(currentUserName)) {
            userDto = userService.findByUsername(currentUserName);
        } else {
            userDto = userService.findVirtualUser();
        }
        CompanySyncOperateType syncType = CompanySyncOperateType.autoSyncType;
        //自动重报之前县查找人行是否已经报送（只支持甄别开户、久悬、销户数据）
        if (billsPublic.getBillType() == BillType.ACCT_OPEN || billsPublic.getBillType() == BillType.ACCT_SUSPEND || billsPublic.getBillType() == BillType.ACCT_REVOKE) {
            AmsAccountInfo amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(userDto.getOrgId(), billsPublic.getAcctNo());
            boolean offLineSync = false;
            switch (billsPublic.getBillType()) {
                case ACCT_OPEN:
                    offLineSync = amsAccountInfo != null;
                    break;
                case ACCT_REVOKE:
                    offLineSync = amsAccountInfo != null && amsAccountInfo.getAccountStatus() == AccountStatus.revoke;
                    break;
                case ACCT_SUSPEND:
                    offLineSync = amsAccountInfo != null && amsAccountInfo.getAccountStatus() == AccountStatus.suspend;
                    break;
                default:
                    break;
            }

            if (offLineSync) {
                billsPublic.setPbcSyncStatus(CompanySyncStatus.tongBuChengGong);
                syncType = CompanySyncOperateType.offLineSyncType;
            }
        }
        allBillsPublicService.syncAndUpdateStaus(true, false, billsPublic, userDto, syncType);
    }

    @Override
    public List<AccountBillsAllInfo> getUnSyncSuccessBill() {
        Specification<AccountBillsAll> specification = new Specification<AccountBillsAll>() {
            @Override
            public Predicate toPredicate(Root<AccountBillsAll> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();


//                expressions.add(cb.or(root.<CompanySyncStatus>get("pbcSyncStatus"), CompanySyncStatus.tongBuShiBai,CompanySyncStatus.weiTongBu));
//                expressions.add(cb.equal(root.<CompanySyncStatus>get("eccsSyncStatus"), CompanySyncStatus.tongBuShiBai));

//                List<Predicate> list = new ArrayList<>();
//                CriteriaBuilder.In<CompanySyncStatus> pbcSyncStatusIn = cb.in(root.<CompanySyncStatus>get("pbcSyncStatus"));
//
//                pbcSyncStatusIn.value(CompanySyncStatus.tongBuShiBai);
//                pbcSyncStatusIn.value(CompanySyncStatus.weiTongBu);
//
//                list.add(pbcSyncStatusIn);
//                Predicate[] p = new Predicate[list.size()];
//                Predicate and = cb.and(list.toArray(p));


                Predicate pbcSyncStatusFail = cb.equal(root.get("pbcSyncStatus"), CompanySyncStatus.tongBuShiBai);

                Predicate pbcSyncStatusWait = cb.equal(root.get("pbcSyncStatus"), CompanySyncStatus.weiTongBu);
                Predicate dsb = cb.equal(root.get("status"), BillStatus.APPROVED);
                dsb = cb.and(dsb, pbcSyncStatusWait);

                Predicate and = cb.or(dsb, pbcSyncStatusFail);
                expressions.add(and);

                return predicate;
            }
        };

        List<AccountBillsAllInfo> accountBillsInfoList = new ArrayList<>();
        List<AccountBillsAll> accountBillsList = accountBillsAllDao.findAll(specification);
        if (accountBillsList != null && accountBillsList.size() != 0) {
            for (AccountBillsAll accountBillsAll : accountBillsList) {
                accountBillsInfoList.add(po2dto(accountBillsAll));
            }
        }
        return accountBillsInfoList;
    }

    @Override
    public int updateCustomerLogId(Long id, Long customerLogid) {
        try {
            AccountBillsAll bills = accountBillsAllDao.findOne(id);
            bills.setCustomerLogId(customerLogid);
            accountBillsAllDao.save(bills);
            return 1;
            //return accountBillsAllDao.updateCustomerLogId(id, customerLogid);
        } catch (Exception e) {
            log.error("更新流水表客户logId异常", e);
            log.info("更新流水表客户logId异常，参数id：{},customerLogId：{}", id, customerLogid);
            return 0;
        }

    }

    @Override
    public int updateCancelHezhun(Long id, String selectPwd, String openKey, String accountKey) {
        //update AccountBillsAll b set b.selectPwd =?2, b.openKey =?3, b.accountKey =?4 where b.id =?1 ")
        try {
            AccountBillsAll bills = accountBillsAllDao.findOne(id);
            bills.setSelectPwd(selectPwd);
            bills.setOpenKey(openKey);
            bills.setAccountKey(accountKey);
            accountBillsAllDao.save(bills);
            return 1;
            //return accountBillsAllDao.updateCancelHezhun(id, selectPwd,openKey,accountKey);
        } catch (Exception e) {
            log.error("更新流水表取消核准相关信息异常", e);
            log.info("更新流水表取消核准相关信息异常，参数为{},{},{},{}", id, selectPwd, openKey, accountKey);
            return 0;
        }
    }


    /**
     * 返回该机构下需要转换为小类的accountsAll列表
     *
     * @return
     */
    private List<AccountsAll> findUpdateAcctTypeList() {
        List<CompanyAcctType> acctTypeList = new ArrayList<>();
        acctTypeList.add(CompanyAcctType.specialAcct);
        acctTypeList.add(CompanyAcctType.tempAcct);
        String organFullId = SecurityUtils.getCurrentOrgFullId();
        if (organFullId == null) {
            organFullId = "1";
        }
        List<AccountsAll> accountsAllList = accountsAllDao.findAllByAcctTypeInAndOrganFullIdLike(acctTypeList, organFullId + "%");
        log.info("accountsAll表中CompanyAcctType=specialAcct或tempAcct的数据有{}条", accountsAllList.size());
        return accountsAllList;
    }


    @Override
    public long updateAcctTypeFromPbc() {
        int failNum = 0;
        List<AccountsAll> accountsAllList = findUpdateAcctTypeList();
        log.info("存量账户性质更新开始");
        log.info("需要更新的账户总数：{}", accountsAllList.size());
        for (AccountsAll accountsAll : accountsAllList) {
            try {
                //查询人行返回对象
                String organFullId = accountsAll.getOrganFullId();
                String[] strings = organFullId.split("-");
                AmsAccountInfo amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(Long.valueOf(strings[strings.length - 1]), accountsAll.getAcctNo());

                //模拟人行返回对象，全都转成基本户。
                if (configService.findByKey("pbcIgnore").size() > 0) {
                    amsAccountInfo = new AmsAccountInfo();
                    amsAccountInfo.setAcctType(AccountType.jiben);
                }
                if (amsAccountInfo.getAcctType() != null) {
                    //更新accountsAll和accountPublic和流水表的账户状态。
                    accountPublicService.updateAcctType(accountsAll.getId(), CompanyAcctType.valueOf(amsAccountInfo.getAcctType().name()), accountsAll.getRefBillId());
                    log.info("账号:{}，转化后的账户性质：{}", accountsAll.getAcctNo(), amsAccountInfo.getAcctType().getFullName());
                } else {
                    log.warn("账号:{}，账户性质小类为空。", accountsAll.getAcctNo());
                }
            } catch (Exception e) {
                failNum++;
                log.error("账号" + accountsAll.getAcctNo() + "更新账户性质异常", e);
            }
        }
        log.info("存量账户性质更新完成，成功数量：{}", accountsAllList.size() - failNum);
        return accountsAllList.size();
    }

    /**
     * 更新存量账户（人行覆盖核心），页面触发。
     *
     * @return
     */
    @Override
    public long updateBills() {

        List<OrganizationDto> organizationDtoList = organizationService.listAll();
        futureCoreBillsUpdateList = new ArrayList<Future<Long>>();

        //需要更新的存量账户总数。
        int num = 0;
        boolean pbcCoverCoreTimeUse = false;
        String pbcCoverCoreTime = null;
        List<ConfigDto> pbcLogin = configService.findByKey("pbcCoverCoreTimeUse");
        if (pbcLogin != null && pbcLogin.size() > 0) {
            pbcCoverCoreTimeUse = Boolean.valueOf(pbcLogin.get(0).getConfigValue());
            log.info("是否使用暂定方式(存量导入人行覆盖核心)：{}", pbcCoverCoreTimeUse);
        }
        List<ConfigDto> start = configService.findByKey("pbcCoverCoreTimeWorkStart");
        List<ConfigDto> stop = configService.findByKey("pbcCoverCoreTimeWorkStop");
        if (start != null && start.size() > 0) {
            log.info("人行覆盖核心(周一至周五)不执行时间段(其他时间默认执行)开始配置：{}", start.get(0).getConfigValue());
            pbcCoverCoreTime = start.get(0).getConfigValue();
        }
        if (stop != null && stop.size() > 0) {
            log.info("人行覆盖核心(周一至周五)不执行时间段(其他时间默认执行)停止配置：{}", stop.get(0).getConfigValue());
            pbcCoverCoreTime = pbcCoverCoreTime + "-" + stop.get(0).getConfigValue();
        }
        for (OrganizationDto organizationDto : organizationDtoList) {

            if (organizationDto.getFullId() == null) {
                log.info("机构fullId异常:{}", organizationDto.toString());
                continue;
            }

            //现有逻辑不会更新之前的存量数据（更新本代码之前存在库中的存量数据）
            List<AccountBillsAll> all = accountBillsAllDao.findAllByBillTypeAndFromSourceAndString005AndOrganFullId(BillType.ACCT_INIT, BillFromSource.INIT, "0", organizationDto.getFullId());
            log.info("{}机构:BillType=ACCT_INIT、BillFromSource=INIT、String005=0、OrganFullId={}的数量为{}", organizationDto.getName(), organizationDto.getFullId(), all.size());

            if (all.size() == 0) {
                log.info("{}机构-存量账户为空，跳过。", organizationDto.getName());
                continue;
            }

            CoreBillsUpdateExecutor coreBillsUpdateExecutor = new CoreBillsUpdateExecutor();
            coreBillsUpdateExecutor.setPbcCollectionLimitNum(pbcCollectionLimitNum);
            coreBillsUpdateExecutor.setPbcCollectionLimitUse(pbcCollectionLimitUse);
            coreBillsUpdateExecutor.setDictionaryService(dictionaryService);
            coreBillsUpdateExecutor.setOrganizationService(organizationService);
            coreBillsUpdateExecutor.setPbcAmsService(pbcAmsService);
            coreBillsUpdateExecutor.setConfigService(configService);
            coreBillsUpdateExecutor.setAccountsAllService(accountsAllService);
            coreBillsUpdateExecutor.setAccountPublicService(accountPublicService);
            coreBillsUpdateExecutor.setAccountBillsAllDao(accountBillsAllDao);
            coreBillsUpdateExecutor.setCustomerPublicService(customerPublicService);
            coreBillsUpdateExecutor.setCustomersAllService(customersAllService);
            coreBillsUpdateExecutor.setCustomerPublicLogService(customerPublicLogService);
            coreBillsUpdateExecutor.setAccountBillsAllList(all);
            coreBillsUpdateExecutor.setPbcCoverCoreTime(pbcCoverCoreTime);
            coreBillsUpdateExecutor.setPbcCoverCoreTimeUse(pbcCoverCoreTimeUse);
            futureCoreBillsUpdateList.add(pbcCoverCoreExecutor.submit(coreBillsUpdateExecutor));
            log.info("{}机构-启动人行覆盖核心机构成功。", organizationDto.getName());
            num += all.size();
        }

        return num;
    }

    @Override
    public AccountBillsAllInfo findFirstByAcctNoOrderByCreatedDateDesc(String acctNo) {
        AccountBillsAllInfo accountBillsAllInfo = new AccountBillsAllInfo();
        AccountBillsAll firstByAcctNoOrderByCreatedDateDesc = accountBillsAllDao.findFirstByAcctNoOrderByCreatedDateDesc(acctNo);
        BeanCopierUtils.copyProperties(firstByAcctNoOrderByCreatedDateDesc, accountBillsAllInfo);
        return accountBillsAllInfo;
    }

    @Override
    public void updateApplyStatus() {
        List<AccountBillsAll> accountBillsAllList = null;
        List<CompanyPreOpenAccountEntDto> preOpenAccountEntList = companyPreOpenAccountEntService.getBreakAppointInfo();

        if (preOpenAccountEntList != null) {
            for (CompanyPreOpenAccountEntDto preOpenAccountEntDto : preOpenAccountEntList) {
                accountBillsAllList = accountBillsAllDao.findByPreOpenAcctId(preOpenAccountEntDto.getApplyid());
                if (CollectionUtils.isEmpty(accountBillsAllList)) {  //该预约数据未开户
                    CompanyPreOpenAccountEntDto dto = companyPreOpenAccountEntService.findOne(preOpenAccountEntDto.getApplyid()).getResult();
                    dto.setStatus(ApplyEnum.BREAK_APPOINT.getValue());
                    companyPreOpenAccountEntService.update(dto);
                }

            }
        }

    }

    @Override
    public JSONObject getIndexBillsCounts() {
        AccountBillsAllSearchInfo accountBillsAllInfo = null;
        JSONObject jsonObject = new JSONObject();
        List<String> billAllCountsList = null;
        CompanyPreOpenAccountEntDto preOpenAccountEntDto = null;
        int dayBetween = 0;
        Long addAcctCount = null;
        Long addBusinessCount = null;
        Long preUnCompleteCount = null;
        Long zhshCheckCount = null;
        Long preSuccessCount = null;
        Long syncSuccsssCount = null;
        Long hzSuccsssCount = null;

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

        if (permissionService.findByCode("addAcct_div")) {
            accountBillsAllInfo = setCondition("addAcct", accountBillsAllInfo, dayBetween);  //新增账户
            addAcctCount = accountBillsAllDao.count(new AccountBillsAllSearchSpec(accountBillsAllInfo));
        }

        if (permissionService.findByCode("addBusiness_div")) {
            accountBillsAllInfo = setCondition("addBusiness", accountBillsAllInfo, dayBetween);  //新增业务
            addBusinessCount = accountBillsAllDao.count(new AccountBillsAllSearchSpec(accountBillsAllInfo));
        }

        if (permissionService.findByCode("dealtRemind_div")) {
            if (permissionService.findByCode("preUnComplete_div")) {
                preOpenAccountEntDto = new CompanyPreOpenAccountEntDto();  //预约待受理
                preOpenAccountEntDto.setOrganfullid(SecurityUtils.getCurrentOrgFullId());
                preOpenAccountEntDto.setStatus(ApplyEnum.UnComplete.getValue());
                preUnCompleteCount = companyPreOpenAccountEntService.getCompanyPreCount(preOpenAccountEntDto);
            }

            if (permissionService.findByCode("zhshCheck_div")) {
                accountBillsAllInfo = setCondition("zhshCheck", accountBillsAllInfo, dayBetween);  //待审核
                zhshCheckCount = accountBillsAllDao.count(new AccountBillsAllSearchSpec(accountBillsAllInfo));
            }

            if (permissionService.findByCode("dsbCheck_div")) {
                billAllCountsList = new ArrayList<>();
                accountBillsAllInfo = setCondition("dsbCheck", accountBillsAllInfo, dayBetween);  //待上报
                allBillsPublicService.addBillCounts("dsbCheck", accountBillsAllInfo, billAllCountsList);
                jsonObject.put("dsbCheckCount", billAllCountsList.get(0));
            }

            if (permissionService.findByCode("dbllb_div")) {
                billAllCountsList = new ArrayList<>();
                accountBillsAllInfo = setCondition("dbllb", accountBillsAllInfo, dayBetween);  //待补录
                allBillsPublicService.addBillCounts("dbllb", accountBillsAllInfo, billAllCountsList);
                jsonObject.put("dbllbCount", billAllCountsList.get(0));
            }
        }

        if (permissionService.findByCode("passRemind_div")) {
            if (permissionService.findByCode("preSuccess_div")) {
                preOpenAccountEntDto = new CompanyPreOpenAccountEntDto();  //预约受理成功
                preOpenAccountEntDto.setOrganfullid(SecurityUtils.getCurrentOrgFullId());
                preOpenAccountEntDto.setStatus(ApplyEnum.SUCCESS.getValue());
                if (dayBetween == 0) {
                    preOpenAccountEntDto.setBeginDate("");
                    preOpenAccountEntDto.setEndDate("");
                } else {
                    preOpenAccountEntDto.setBeginDate(DateUtils.DateToStr(DateUtils.dayBefore(new Date(), dayBetween), ""));
                    preOpenAccountEntDto.setEndDate(DateUtils.DateToStr(new Date(), ""));
                }
                preSuccessCount = companyPreOpenAccountEntService.getCompanyPreCount(preOpenAccountEntDto);
            }

            if (permissionService.findByCode("syncSuccsss_div")) {
                accountBillsAllInfo = setCondition("syncSuccsss", accountBillsAllInfo, dayBetween);  //上报成功
                syncSuccsssCount = accountBillsAllDao.count(new AccountBillsAllSearchSpec(accountBillsAllInfo));
            }

            if (permissionService.findByCode("hzSuccsss_div")) {
                accountBillsAllInfo = setCondition("hzSuccsss", accountBillsAllInfo, dayBetween);  //核准通过
                hzSuccsssCount = accountBillsAllDao.count(new AccountBillsAllSearchSpec(accountBillsAllInfo));
            }
        }

        jsonObject.put("addAcctCount", addAcctCount);
        jsonObject.put("addBusinessCount", addBusinessCount);
        jsonObject.put("preUnCompleteCount", preUnCompleteCount);
        jsonObject.put("zhshCheckCount", zhshCheckCount);
        jsonObject.put("preSuccessCount", preSuccessCount);
        jsonObject.put("syncSuccsssCount", syncSuccsssCount);
        jsonObject.put("hzSuccsssCount", hzSuccsssCount);

        return jsonObject;
    }

    private AccountBillsAllSearchInfo setCondition(String code, AccountBillsAllSearchInfo accountBillsAllInfo, int dayBetween) {
        List<BillStatus> statuses = null;
        List<CompanySyncStatus> eccsSyncStatuses = null;
        List<CompanySyncStatus> pbcSyncStatuses = null;

        accountBillsAllInfo = new AccountBillsAllSearchInfo();
        accountBillsAllInfo.setOrganFullId(SecurityUtils.getCurrentOrgFullId());

        if ("addAcct".equals(code) || "addBusiness".equals(code) || "syncSuccsss".equals(code) || "hzSuccsss".equals(code)) {
            if (dayBetween == 0) {
                accountBillsAllInfo.setBeginDate(null);
                accountBillsAllInfo.setEndDate(null);
            } else {
                accountBillsAllInfo.setBeginDate(DateUtils.dayBefore(new Date(), dayBetween));
                accountBillsAllInfo.setEndDate(new Date());
            }
        }

        if (!"addAcct".equals(code)) {
            accountBillsAllInfo.setBillTypeEx(BillType.ACCT_INIT);
        }

        if ("addAcct".equals(code)) {  //新增账户
            accountBillsAllInfo.setBillType(BillType.ACCT_OPEN);
        } else if ("addBusiness".equals(code)) {  //新增业务

        } else if ("zhshCheck".equals(code)) {  //待审核
            accountBillsAllInfo.setStatus(BillStatus.APPROVING);
        } else if ("dsbCheck".equals(code)) {  //待上报
            accountBillsAllInfo.setStatus(BillStatus.WAITING_REPORTING);
//            accountBillsAllInfo.setStatus(BillStatus.APPROVED);
//            accountBillsAllInfo.setPbcSyncStatus(CompanySyncStatus.weiTongBu);
//            accountBillsAllInfo.setEccsSyncStatus(CompanySyncStatus.weiTongBu);
        } else if ("dbllb".equals(code)) {  //待补录
            accountBillsAllInfo.setStatus(BillStatus.WAITING_SUPPLEMENT);
            accountBillsAllInfo.setPbcSyncStatus(CompanySyncStatus.tongBuShiBai);
            accountBillsAllInfo.setEccsSyncStatus(CompanySyncStatus.tongBuShiBai);
        } else if ("syncSuccsss".equals(code)) {   //上报成功
            eccsSyncStatuses = new ArrayList<>();
            pbcSyncStatuses = new ArrayList<>();

            accountBillsAllInfo.setStatus(BillStatus.APPROVED);
            pbcSyncStatuses.add(CompanySyncStatus.buTongBu);
            pbcSyncStatuses.add(CompanySyncStatus.tongBuChengGong);
            accountBillsAllInfo.setPbcSyncStatuses(pbcSyncStatuses);
            eccsSyncStatuses.add(CompanySyncStatus.buTongBu);
            eccsSyncStatuses.add(CompanySyncStatus.tongBuChengGong);
            accountBillsAllInfo.setEccsSyncStatuses(eccsSyncStatuses);
        } else if ("hzSuccsss".equals(code)) {   //核准成功
            accountBillsAllInfo.setPbcCheckStatus(CompanyAmsCheckStatus.CheckPass);
        }

        return accountBillsAllInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBillsAndAccount(Long id) throws Exception {
        AccountBillsAll accountBillsAll = accountBillsAllDao.findOne(id);
        if (accountBillsAll != null && accountBillsAll.getFinalStatus() == CompanyIfType.No && accountBillsAll.getBillType() == BillType.ACCT_OPEN) {
            Long accountId = accountBillsAll.getAccountId();
            AccountsAllInfo accountsAllInfo = accountsAllService.getOne(accountId);
            AccountPublicInfo accountPublicInfo = accountPublicService.findByAccountId(accountId);
            //判断条件：流水为开户流水 & 流水不是最终状态 & 账户为未激活
            //1. 基本户：信用机构上报状态不为上报成功 & 核准状态不为核准成功
            //2. 核准类： 核准状态不为核准成功
            //3. 报备类： 人行上报状态不为上报成功
            if (accountsAllInfo != null && accountPublicInfo != null && accountsAllInfo.getAccountStatus() == com.ideatech.ams.account.enums.AccountStatus.notActive) {
                if ((accountBillsAll.getAcctType() == CompanyAcctType.jiben && accountBillsAll.getEccsSyncStatus() != CompanySyncStatus.tongBuChengGong && accountBillsAll.getPbcSyncStatus() != CompanySyncStatus.tongBuChengGong)
                        || (accountBillsAll.getAcctType() == CompanyAcctType.jiben && accountBillsAll.getEccsSyncStatus() == CompanySyncStatus.buTongBu && accountBillsAll.getPbcCheckStatus() != CompanyAmsCheckStatus.CheckPass)
                        || (accountBillsAll.getAcctType() != CompanyAcctType.jiben && accountBillsAll.getAcctType().isHeZhun() && accountBillsAll.getPbcCheckStatus() != CompanyAmsCheckStatus.CheckPass)
                        || ((accountBillsAll.getAcctType() == CompanyAcctType.yiban || accountBillsAll.getAcctType() == CompanyAcctType.feiyusuan) && accountBillsAll.getPbcSyncStatus() != CompanySyncStatus.tongBuChengGong)) {
                    accountBillsAllDao.delete(id);
                    accountsAllService.deleteById(accountId);
                    accountPublicService.deleteByAccountId(accountId);
                    if (accountBillsAll.getAcctType().isHeZhun() && accountBillsAll.getPbcSyncStatus() == CompanySyncStatus.tongBuChengGong) {
                        //核准类进行上报删除
                        PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullId(accountBillsAll.getOrganFullId(), EAccountType.AMS);
                        try {
                            pbcAmsService.deleteAccount(pbcAccountDto, accountBillsAll.getAcctNo());
                        } catch (Exception e) {
                            log.error("开户删除异常{}" + e.getMessage());
                            throw new BizServiceException(EErrorCode.PBC_QUERY_PARAM_EMPTY, e.getMessage());
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBills(Long id) throws Exception {
        AccountBillsAll accountBillsAll = accountBillsAllDao.findOne(id);
        if (accountBillsAll != null && accountBillsAll.getBillType() != BillType.ACCT_OPEN) {
            accountBillsAllDao.delete(id);
            AccountsAllInfo accountsAllInfo = accountsAllService.getOne(accountBillsAll.getAccountId());
            if (accountBillsAll.getBillType().equals(BillType.ACCT_CHANGE)) {
                //删除该流水的同时，删除因该流水产生的账户日志,恢复AccountsAll表信息。（方便账户详情的查看）
                AccountPublicLog accountPublicLog = accountPublicLogDao.findByAccountIdAndMaxSequence(accountBillsAll.getAccountId());
                if (accountPublicLog != null) {
                    String[] ignoreProperties = {"id"};
                    BeanUtils.copyProperties(accountPublicLog, accountsAllInfo, ignoreProperties);
                    accountsAllService.save(accountsAllInfo);
                    if (accountPublicLog.getSequence() > 1) {
                        accountPublicLogDao.delete(accountPublicLog.getId());
                    }
                }
            } else {
                //账户执行久悬销户操作，不会生成accountPublicLog信息，只是修改了账户状态，当删除久悬流水时，仅需要恢复AccountsAll表关联的最新流水id即可
                AccountBillsAll accountBillNew = accountBillsAllDao.findFirstByAccountIdOrderByCreatedDateDesc(accountBillsAll.getAccountId());//获取当前账户最新流水信息
                //没有最新流水查询到说明该账户在账管不存在存量流水或者其他的交易流水   故删除账户表该数据
                if (accountBillNew == null) {
                    //删除accountPublic数据
                    AccountPublicInfo accountPublicInfo = accountPublicService.findByAccountId(accountsAllInfo.getId());
                    if (accountPublicInfo != null) {
                        accountPublicService.deleteByAccountId(accountsAllInfo.getId());
                    }
                    accountsAllService.deleteById(accountsAllInfo.getId());
                } else {
                    accountsAllInfo.setRefBillId(accountBillNew.getId());
                    accountsAllService.save(accountsAllInfo);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBillsCgsb(Long id) throws Exception {
        AccountBillsAll accountBillsAll = accountBillsAllDao.findOne(id);
        if (accountBillsAll != null && accountBillsAll.getBillType() != BillType.ACCT_OPEN && accountBillsAll.getFinalStatus() == CompanyIfType.No) {
            //判断条件： 流水不是最终状态
            //1. 基本户：信用机构上报状态不为上报成功 & 核准状态不为核准成功
            //2. 核准类： 核准状态不为核准成功
            //3. 报备类： 人行上报状态不为上报成功
            if ((accountBillsAll.getAcctType() == CompanyAcctType.jiben && accountBillsAll.getEccsSyncStatus() != CompanySyncStatus.tongBuChengGong && accountBillsAll.getPbcCheckStatus() != CompanyAmsCheckStatus.CheckPass)
                    || (accountBillsAll.getAcctType() != CompanyAcctType.jiben && accountBillsAll.getAcctType().isHeZhun() && accountBillsAll.getPbcCheckStatus() != CompanyAmsCheckStatus.CheckPass)
                    || ((accountBillsAll.getAcctType() == CompanyAcctType.yiban || accountBillsAll.getAcctType() == CompanyAcctType.feiyusuan) && accountBillsAll.getPbcSyncStatus() != CompanySyncStatus.tongBuChengGong)) {
                accountBillsAllDao.delete(id);
                if (accountBillsAll.getAcctType().isHeZhun() && accountBillsAll.getPbcSyncStatus() == CompanySyncStatus.tongBuChengGong) {
                    //核准类进行上报删除
                    PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullId(accountBillsAll.getOrganFullId(), EAccountType.AMS);
                    try {
                        pbcAmsService.deleteAccount(pbcAccountDto, accountBillsAll.getAcctNo());
                    } catch (Exception e) {
                        log.error("删除异常{}" + e.getMessage());
                        throw new BizServiceException(EErrorCode.PBC_QUERY_PARAM_EMPTY, e.getMessage());
                    }
                }
                //删除该流水的同时，删除因该流水产生的账户日志,恢复AccountsAll表信息。（方便账户详情的查看）
                AccountPublicLog accountPublicLog = accountPublicLogDao.findByAccountIdAndMaxSequence(accountBillsAll.getAccountId());
                if (accountPublicLog != null) {
                    String[] ignoreProperties = {"id"};
                    AccountsAllInfo accountsAllInfo = accountsAllService.getOne(accountBillsAll.getAccountId());
                    BeanUtils.copyProperties(accountPublicLog, accountsAllInfo, ignoreProperties);
                    accountsAllService.save(accountsAllInfo);
                    if (accountPublicLog.getSequence() > 1) {
                        accountPublicLogDao.delete(accountPublicLog.getId());
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<AccountBillsAllInfo> findByAcctNo(String acctNo) {

        List<AccountBillsAllInfo> infoList = new ArrayList<>();
        List<AccountBillsAll> list = accountBillsAllDao.findByAcctNo(acctNo);

        if (CollectionUtils.isNotEmpty(list)) {
            infoList = ConverterService.convertToList(list, AccountBillsAllInfo.class);
            return infoList;
        }
        return infoList;
    }

    private AccountBillsAllInfo po2dto(AccountBillsAll accountBillsAll) {
        if (accountBillsAll != null) {
            AccountBillsAllInfo accountBillsAllInfo = new AccountBillsAllInfo();
            BeanCopierUtils.copyProperties(accountBillsAll, accountBillsAllInfo);
            return accountBillsAllInfo;
        } else {
            return null;
        }
    }


    @Override
    public List<AccountBillsAllInfo> findByOrganFullIdLike(String organFullId) {
        return ConverterService.convertToList(accountBillsAllDao.findByOrganFullIdLike(organFullId), AccountBillsAllInfo.class);
    }

    @Override
    public List<AccountBillsAllInfo> findByAccountId(Long accountId) {
        return ConverterService.convertToList(accountBillsAllDao.findByAccountId(accountId), AccountBillsAllInfo.class);
    }

    @Override
    public List<AccountBillsAllInfo> findByAccountIdIn(List<Long> accountIds) {
        return ConverterService.convertToList(accountBillsAllDao.findByAccountIdIn(accountIds), AccountBillsAllInfo.class);
    }

    @Override
    public List<AccountBillsAllInfo> findByIdIn(List<Long> ids) {
        return ConverterService.convertToList(accountBillsAllDao.findByIdIn(ids), AccountBillsAllInfo.class);
    }

    @Override
    public List<AccountBillsAllInfo> findByCustomerLogIdIn(List<Long> customerLogId) {
        return ConverterService.convertToList(accountBillsAllDao.findByCustomerLogIdIn(customerLogId), AccountBillsAllInfo.class);
    }

    @Override
    public AccountBillsAllInfo getFirstBillByAccountId(Long accountId) {
        AccountBillsAllInfo abai = new AccountBillsAllInfo();
        AccountBillsAll accountBillsAll = accountBillsAllDao.findFirstByAccountIdOrderByCreatedDate(accountId);
        BeanCopierUtils.copyProperties(accountBillsAll, abai);
        return abai;
    }


    @Override
    public AccountBillsAllInfo getFirstBillByCustomerLogId(Long customerLogId) {
        AccountBillsAllInfo abai = new AccountBillsAllInfo();
        AccountBillsAll accountBillsAll = accountBillsAllDao.findFirstByCustomerLogIdOrderByCreatedDate(customerLogId);
        BeanCopierUtils.copyProperties(accountBillsAll, abai);
        return abai;
    }

//    @Override
//    public Object getAcctNoAndAccountStatus(String applyId, String depositorName) {
//        if(StringUtils.isEmpty(applyId) && StringUtils.isEmpty(depositorName)){
//            throw new BizServiceException(EErrorCode.SYSTEM_ERROR,"预约编号跟企业名称不能为空！");
//        }
//        CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto = companyPreOpenAccountEntService.findByApplyid(applyId);
//        if(companyPreOpenAccountEntDto ==  null){
//            throw new BizServiceException(EErrorCode.SYSTEM_ERROR,"预约编号"+applyId+"查询预约信息失败，无此预约数据信息！");
//        }
//        List<AccountBillsAll> obj = accountBillsAllDao.findByPreOpenAcctIdAndDepositorName(applyId,depositorName);
//        if(CollectionUtils.isEmpty(obj)){
//            throw new BizServiceException(EErrorCode.SYSTEM_ERROR,"预约编号"+applyId+"企业名称"+depositorName+"查询信息失败，无此数据信息！");
//        }
//        AccountBillsAll accountBillsAll = obj.get(0);;
//        companyPreOpenAccountEntDto.setAcctNo(accountBillsAll.getAcctNo());
//        companyPreOpenAccountEntDto.setAccountstatus(accountBillsAll.getStatus().getValue());
//        return companyPreOpenAccountEntDto;
//    }

    @Override
    public List<AccountBillsAllInfo> findByPreOpenAcctIdAndDepositorName(String applyId, String depositorName) {
        return ConverterService.convertToList(accountBillsAllDao.findByPreOpenAcctIdAndDepositorName(applyId, depositorName), AccountBillsAllInfo.class);
    }

    @Override
    public void updateSelectPwd(AllBillsPublicDTO billsPublic) {
        try {
            if (billsPublic.getId() == null) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请输入billId!");
            }
            AccountBillsAll bills = accountBillsAllDao.findOne(billsPublic.getId());

            AccountsAllInfo accountsAll = accountsAllService.findByRefBillId(billsPublic.getId());

            if (bills != null && bills.getId() > 0) {
                //取消核准进行存款人密码等信息保存
                if (bills.getCancelHeZhun() != null && bills.getCancelHeZhun()) {
                    if (StringUtils.isNotBlank(billsPublic.getSelectPwd())) {
                        log.info("查询密码：" + billsPublic.getSelectPwd());
                        bills.setSelectPwd(billsPublic.getSelectPwd());
                    }
                    if (StringUtils.isNotBlank(billsPublic.getOpenKey())) {
                        log.info("openKey：" + billsPublic.getOpenKey());
                        bills.setOpenKey(billsPublic.getOpenKey());
                    }

                    //两边比较是否变更
                    if (StringUtils.isNotBlank(bills.getAccountKey()) && StringUtils.isNotBlank(billsPublic.getAccountKey())) {
                        if (!StringUtils.equals(bills.getAccountKey(), billsPublic.getAccountKey())) {
                            log.info("核准号变更后记录基本户编号生成日期：" + billsPublic.getString006());
                            bills.setString006(billsPublic.getString006());
                        }
                    } else if (StringUtils.isBlank(bills.getAccountKey()) && StringUtils.isNotBlank(billsPublic.getAccountKey())) {
                        log.info("记录基本户编号生成日期：" + billsPublic.getString006());
                        bills.setString006(billsPublic.getString006());
                    }
                    if (StringUtils.isNotBlank(billsPublic.getAccountKey())) {
                        log.info("基本户开户许可证：" + billsPublic.getAccountKey());
                        bills.setAccountKey(billsPublic.getAccountKey());
                    }
                    if (StringUtils.isNotBlank(billsPublic.getAccountLicenseNo())) {
                        log.info("开户许可证：" + billsPublic.getAccountLicenseNo());
                        bills.setAccountLicenseNo(billsPublic.getAccountLicenseNo());
                    }
//                   bills.setOpenAccountSiteType(billsPublic.getOpenAccountSiteType());
//                   accountBillsAllDao.save(bills);

                    log.info("账号：" + billsPublic.getAcctNo() + "取消核准AccountsAllInfo保存基本户查询密码;{}，开户许可证号:{},基本存款账户编号：{}", bills.getSelectPwd(), bills.getOpenKey(), bills.getAccountKey());
                    if (accountsAll != null) {
                        if (StringUtils.isNotBlank(billsPublic.getSelectPwd())) {
                            accountsAll.setSelectPwd(billsPublic.getSelectPwd());
                        }
                        if (StringUtils.isNotBlank(billsPublic.getOpenKey())) {
                            accountsAll.setOpenKey(billsPublic.getOpenKey());
                        }
                        if (StringUtils.isNotBlank(billsPublic.getAccountKey())) {
                            accountsAll.setAccountKey(billsPublic.getAccountKey());
                        }
                        if (StringUtils.isEmpty(accountsAll.getString006()) && StringUtils.isNotEmpty(billsPublic.getString006())) {
                            log.info("记录基本户编号生成日期：" + billsPublic.getString006());
                            accountsAll.setString006(billsPublic.getString006());
                        }
                        //如果两者都有值  说明是变更操作，判断两个字段的日期是否eq  不eq说明有更新  用billsPublic替换accountsAll编号生成日期
                        if (StringUtils.isNotEmpty(accountsAll.getString006()) && StringUtils.isNotEmpty(billsPublic.getString006())) {
                            if (!accountsAll.getString006().equals(billsPublic.getString006())) {
                                log.info("记录变更基本户编号生成日期：" + billsPublic.getString006());
                                accountsAll.setString006(billsPublic.getString006());
                            }
                        }
                        accountsAll.setOpenAccountSiteType(billsPublic.getOpenAccountSiteType());
                        accountsAllService.save(accountsAll);
                    }
//                   accountsAllService.updateCancelHezhun(billsPublic.getAccountId(),billsPublic.getSelectPwd(),billsPublic.getOpenKey(),billsPublic.getAccountKey());
                    accountPublicService.updateCancelHezhun(billsPublic.getAccountId(), billsPublic.getAccountLicenseNo());
                }
                //2019年10月15日11:25:32 邹郎  保存从人行查询的本异地标识
                bills.setOpenAccountSiteType(billsPublic.getOpenAccountSiteType());
                accountBillsAllDao.save(bills);
            }
            if (accountsAll != null) {
                //2019年10月15日11:25:32 邹郎  保存从人行查询的本异地标识
                accountsAll.setOpenAccountSiteType(billsPublic.getOpenAccountSiteType());
                accountsAllService.save(accountsAll);
            }
        } catch (RuntimeException e) {
            log.error("修改查询密码异常", e);
            e.printStackTrace();
        }
    }

    @Override
    public void updateImageSyncStatus(Long billId, CompanySyncStatus imgaeSyncStatus) {
        AccountBillsAll bills = accountBillsAllDao.findOne(billId);
        if (bills != null) {
            bills.setImgaeSyncStatus(imgaeSyncStatus);
            accountBillsAllDao.save(bills);
        } else {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "不存在到对应的流水信息！");
        }
    }

    @Override
    public Long isCheckBillNoAndAcctNo(String billNo, String acctNo) {
        Long count = accountBillsAllDao.countByBillNoAndAcctNo(billNo, acctNo);
        return count;
    }

    @Override
    public long countOpenSuccess(String depositorType, String acctType, String organFullId, String beginDate, String endDate) {
        OpenAccountStatisticsSearchDto oassd = new OpenAccountStatisticsSearchDto();
        oassd.setDepositorType(depositorType);
        oassd.setAcctType(CompanyAcctType.str2enum(acctType));
        oassd.setOrganFullId(organFullId);
        if (StringUtils.isNotBlank(beginDate)) {
            oassd.setBeginDate(beginDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            oassd.setEndDate(endDate);
        }
        long total = openAccountStatisticsDao.count(new OpenAccountStatisticsSpec(oassd));
        return total;
    }

    /**
     * 判断核心流水更新是否完成
     *
     * @throws Exception
     */
    private void verifyUpdateCompleted() {
        for (Iterator<Future<Long>> iterator = futureCoreBillsUpdateList.iterator(); iterator.hasNext(); ) {
            Future<Long> future = iterator.next();
            if (future.isDone()) {
                iterator.remove();
            }
        }
        if (futureCoreBillsUpdateList.size() > 0) {
            try {
                // 暂停5秒
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            verifyUpdateCompleted();
        }


    }
    @Override
    public List<AccountBillsAllInfo> findByAcctNoLike(String acctNo) {
        return ConverterService.convertToList(accountBillsAllDao.findByAcctNoLike(acctNo), AccountBillsAllInfo.class);
    }

    /**
     * 江南银行查询xml
     * 实体类填充信息
     *
     */
    @Override
    public void updateDownstatus(String id) {
        accountBillsAllDao.updateDownLoadStatus(parseLong(id));
    }


    /**
     * 江南银行查询xml
     * 实体类填充信息
     *
     */
    @Override
    public void updateUploadstatus(Long id) {
        accountBillsAllDao.updateUploadstatus(id);
    }



    /**
     * 江南银行查询xml
     * 实体类填充信息
     *
     */
    @Override
    public AccountBillsAllInfo findById(Long id) {
        return po2dto(accountBillsAllDao.findById(id));
    }


    /**
     * 江南银行江苏影像开发要求
     * 根据机构号查出当日该机构下的
     * 所有流水表中的信息
     */
    @Override
    public List<AccountBillsAllInfo> findByOrganFullIdAndBillId(String organFullID,String billDate) {
        return ConverterService.convertToList(accountBillsAllDao.findByOrganFullIdAndBillDate(organFullID,billDate), AccountBillsAllInfo.class);
    }

}
