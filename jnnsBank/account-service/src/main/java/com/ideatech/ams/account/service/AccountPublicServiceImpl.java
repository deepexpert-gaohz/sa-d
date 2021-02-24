package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dao.AccountPublicDao;
import com.ideatech.ams.account.dao.spec.AccountPublicSpec;
import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.dto.AccountPublicLogInfo;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.entity.AccountPublic;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.customer.dto.CustomerPublicInfo;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.customer.service.CustomerPublicMidService;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @author vantoo
 * @date 16:17 2018/5/28
 */
@Service
@Transactional
@Slf4j
public class AccountPublicServiceImpl implements AccountPublicService {

    @Autowired
    private AccountPublicDao accountPublicDao;

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private AccountPublicService accountPublicService;

    @Autowired
    private AccountPublicLogService accountPublicLogService;

    @Autowired
    private CustomerPublicLogService customerPublicLogService;

    @Autowired
    private CustomerPublicService customerPublicService;

    @Autowired
    private CustomerPublicMidService customerPublicMidService;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Override
    public AccountPublicInfo findByAccountId(Long accountId) {
        return po2dto(accountPublicDao.findByAccountId(accountId));
    }

    @Override
    public List<AccountPublicInfo> findListByAccountId(Long accountId) {
        return null;
    }

    @Override
    public AccountPublicInfo findByAcctNo(String acctNo) {
        AccountsAllInfo accountsAllInfo = accountsAllService.findByAcctNo(acctNo);
        if (accountsAllInfo != null) {
            return po2dto(accountPublicDao.findByAccountId(accountsAllInfo.getId()));
        } else {
            return null;
        }
    }

    @Override
    public void save(AccountPublicInfo accountPublicInfo) {
        AccountPublic accountPublic = null;
        //更新需要先查询
        if (accountPublicInfo.getId() != null) {
            accountPublic = accountPublicDao.findOne(accountPublicInfo.getId());
        }
        if (accountPublic == null) {
            accountPublic = new AccountPublic();
        }
        BeanCopierUtils.copyProperties(accountPublicInfo, accountPublic);
        accountPublic = accountPublicDao.save(accountPublic);
        accountPublicInfo.setId(accountPublic.getId());
    }

    @Override
    public void updateAcctType(Long accountId, CompanyAcctType acctType, Long refBillId) {
        AccountPublic accountPublic = accountPublicDao.findByAccountId(accountId);
        accountPublic.setAcctType(acctType);

        accountPublicDao.save(accountPublic);

        AccountsAllInfo accountsAllInfo = accountsAllService.getOne(accountPublic.getAccountId());
        accountsAllInfo.setAcctType(acctType);
        accountsAllService.save(accountsAllInfo);

        if(refBillId != null) {
            AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getOne(refBillId);
            accountBillsAllInfo.setAcctType(acctType);
            accountBillsAllService.save(accountBillsAllInfo);
        }
    }

    @Override
    public void updateAccountKey(AmsAccountInfo info) {
        if(StringUtils.isNotBlank(info.getAcctNo())) {
            AccountPublicInfo accountPublicInfo = accountPublicService.findByAcctNo(info.getAcctNo());
            if(accountPublicInfo != null && accountPublicInfo.getAccountId() != null) {
                AccountsAllInfo acountsAllInfo = accountsAllService.getOne(accountPublicInfo.getAccountId());

                //更新账户核准号
                if(info.getAcctType() != null && (info.getAcctType() == AccountType.yiban || info.getAcctType() == AccountType.yusuan
                        || info.getAcctType() == AccountType.feiyusuan || info.getAcctType() == AccountType.feilinshi)) {
                    accountPublicInfo.setAccountLicenseNo(info.getAccountLicenseNo());
                    accountPublicService.save(accountPublicInfo);

                    AccountPublicLogInfo accountPublicLogInfo = accountPublicLogService.findByAccountIdBackup(accountPublicInfo.getAccountId());
                    if(accountPublicLogInfo != null) {
                        accountPublicLogInfo.setAccountLicenseNo(info.getAccountLicenseNo());
                        accountPublicLogService.save(accountPublicLogInfo);
                    }
                }

                //更新基本开户许可核准号
                if(info.getAcctType() != null && (info.getAcctType() == AccountType.yusuan || info.getAcctType() == AccountType.linshi
                        || info.getAcctType() == AccountType.feilinshi || info.getAcctType() == AccountType.teshu)) {
                    if (acountsAllInfo != null && acountsAllInfo.getCustomerLogId() != null) {
                        CustomerPublicLogInfo customerPublicLogInfo = customerPublicLogService.getOne(acountsAllInfo.getCustomerLogId());
                        if (customerPublicLogInfo != null) {
                            customerPublicLogInfo.setAccountKey(info.getAccountKey());
                            customerPublicLogService.save(customerPublicLogInfo);

                            CustomerPublicInfo customerPublicInfo = customerPublicService.getByCustomerId(customerPublicLogInfo.getCustomerId());
                            if (customerPublicInfo != null) {
                                customerPublicInfo.setAccountKey(info.getAccountKey());
                                customerPublicService.save(customerPublicInfo);
                            }
                        }

                    }
                }
            }

        }

    }

    @Override
    public Map<Long, AccountPublicInfo> findAllInMap() {
        List<AccountPublic> all = accountPublicDao.findAll();
        Map<Long, AccountPublicInfo> map = new HashMap<>();
        for(AccountPublic accountPublic : all){
            AccountPublicInfo accountPublicInfo = po2dto(accountPublic);
            if(accountPublicInfo != null){
                map.put(accountPublicInfo.getAccountId(),accountPublicInfo);
            }
        }
        return map;
    }

    @Override
    public void deleteByAccountId(Long accountId) {
        accountPublicDao.deleteByAccountId(accountId);
    }

    private AccountPublicInfo po2dto(AccountPublic accountPublic) {
        if (accountPublic != null) {
            AccountPublicInfo accountPublicInfo = new AccountPublicInfo();
            BeanCopierUtils.copyProperties(accountPublic, accountPublicInfo);
            return accountPublicInfo;
        } else {
            return null;
        }
    }

    @Override
    public List<AccountPublicInfo> findByOrganFullId(String organFullId) {
        return ConverterService.convertToList(accountPublicDao.findByOrganFullIdLike(organFullId),AccountPublicInfo.class);
    }

    @Override
    public boolean deleteAccountsAll(Long id) {
        AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getOne(id);
        try{
            //白名单删除后处理后台状态变更
            Long accountsId = null;
            if(accountBillsAllInfo != null){
                accountsId = accountBillsAllInfo.getAccountId();
                AccountsAllInfo accountsAllInfo = accountsAllService.getOne(accountsId);
                if(accountsAllInfo != null){
                    accountsAllInfo.setWhiteList("0");
                    accountsAllInfo.setAccountStatus(AccountStatus.notActive);
                    accountsAllService.save(accountsAllInfo);
                }
                accountBillsAllInfo.setWhiteList("0");
                accountBillsAllInfo.setEccsSyncStatus(CompanySyncStatus.weiTongBu);
                accountBillsAllInfo.setPbcSyncStatus(CompanySyncStatus.weiTongBu);
                accountBillsAllInfo.setCreatedBy(SecurityUtils.getCurrentUserId() + "");
                accountBillsAllService.save(accountBillsAllInfo);
                return true;
            }
            return false;
        }catch (Exception e){
            log.error("删除白名单流水信息失败。",e);
            return false;
        }
    }

    @Override
    public int updateCancelHezhun(Long accountId, String accountLicenseNo) {
        return accountPublicDao.updateCancelHezhun(accountId,accountLicenseNo);
    }

}
