package com.ideatech.ams.account.service;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.dao.HeGuiYuJingAllDao;
import com.ideatech.ams.account.dao.OrganizationMapDao;
import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.AccountsAllSearchInfo;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.entity.HeGuiYuJingAll;
import com.ideatech.ams.account.entity.OrganizationMapPo;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.vo.AnnualAccountVo;
import com.ideatech.ams.customer.dto.CustomerPublicInfo;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.dto.CustomersAllInfo;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.customer.service.CustomersAllService;
import com.ideatech.ams.pbc.dto.AmsPrintInfo;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author vantoo
 * @date 15:52 2018/5/28
 */
@Service
@Transactional
@Slf4j
public class AccountsAllServiceImpl implements AccountsAllService {

    @Autowired
    private AccountsAllDao accountsAllDao;

    @Autowired
    private AccountPublicService accountPublicService;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private CustomersAllService customersAllService;

    @Autowired
    private CustomerPublicService customerPublicService;

    @Autowired
    private CustomerPublicLogService customerPublicLogService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationMapDao organizationMapDao;

    @PersistenceContext
    private EntityManager em; //注入EntityManager

    @Value("${ams.notice.noticeFlag:true}")
    private Boolean noticeFlag;

    @Override
    public AccountsAllInfo getOne(Long accountId) {
        return po2dto(accountsAllDao.findOne(accountId));
    }

    @Override
    public void save(AccountsAllInfo accountsAllInfo) {
        AccountsAll accountsAll = null;
        //更新需要先查询
        if (accountsAllInfo.getId() != null) {
            accountsAll = accountsAllDao.findOne(accountsAllInfo.getId());
        }
        if (accountsAll == null) {
            accountsAll = new AccountsAll();
        }
        BeanCopierUtils.copyProperties(accountsAllInfo, accountsAll);
        accountsAll = accountsAllDao.save(accountsAll);
        accountsAllInfo.setId(accountsAll.getId());
    }

    @Override
    public AccountsAllInfo findByRefBillId(Long refBillId) {
        AccountsAllInfo accountsAllInfo = new AccountsAllInfo();
        AccountsAll accountsAll = accountsAllDao.findByRefBillId(refBillId);

        if(accountsAll != null) {
            BeanCopierUtils.copyProperties(accountsAll, accountsAllInfo);
            return  accountsAllInfo;
        }

        return null;
    }

    @Override
    public List<AccountsAllInfo> findByAccountKey(String accountKey,CompanyAcctType acctType) {
        List<AccountsAll> accountsAllList = accountsAllDao.findByAccountKeyAndAcctType(accountKey,acctType);
        return ConverterService.convertToList(accountsAllList, AccountsAllInfo.class);
    }

    @Override
    public AccountsAllInfo findByAcctNo(String acctNo) {
        AccountsAllInfo info = null;
        AccountsAll accountsAll = accountsAllDao.findByAcctNo(acctNo);

        if(accountsAll != null) {
            info = new AccountsAllInfo();
            BeanCopierUtils.copyProperties(accountsAll, info);
        }

        return info;
    }

    @Override
    public AccountsAllInfo findByDepositorName(String depositorName) throws Exception {
        List<AccountsAllInfo> accountsAllInfoList = new ArrayList<>();
        List<CustomerPublicLogInfo> customerPublicLogInfoList = customerPublicLogService.getByDepositorName(depositorName);
        for (CustomerPublicLogInfo log : customerPublicLogInfoList) {
            List<AccountsAll> accountsAllList = accountsAllDao.findByCustomerLogId(log.getId());
            for (AccountsAll accountsAll : accountsAllList) {
                if ("jiben".equals(accountsAll.getAcctType().name())) {
                    AccountsAllInfo accountsAllInfo = new AccountsAllInfo();
                    BeanUtils.copyProperties(accountsAll, accountsAllInfo);
                    accountsAllInfoList.add(accountsAllInfo);
                }
            }
        }
        if (accountsAllInfoList.size() == 1) {
            return accountsAllInfoList.get(0);
        } else if (accountsAllInfoList.size() > 1) {
            log.error("客户“" + depositorName + "”基本户信息异常，存在多个基本户信息" + JSON.toJSONString(accountsAllInfoList));
            throw new Exception("客户“" + depositorName + "”基本户信息异常，存在多个基本户信息");
        } else {
            return null;
        }
    }

    @Override
    public List<AccountsAllInfo> findByCustomerLogId(Long customerLogId) {
        List<AccountsAll> accountsAlls = accountsAllDao.findByCustomerLogId(customerLogId);
        return ConverterService.convertToList(accountsAlls,AccountsAllInfo.class);
    }

    @Override
    public AccountsAllInfo findByAcctNoAndIdNot(String acctNo, Long id) {
        AccountsAllInfo info = null;
        AccountsAll accountsAll = accountsAllDao.findByAcctNoAndIdNot(acctNo,id);

        if(accountsAll != null) {
            info = new AccountsAllInfo();
            BeanCopierUtils.copyProperties(accountsAll, info);
        }

        return info;
    }

    @Override
    public AccountsAllInfo findByAcctTypeAndDepositorName(CompanyAcctType acctType, String depositorName) {
        AccountsAllInfo info = null;
        String sql = "select t1 from AccountsAll t1,CustomerPublicLog t2 where t1.customerLogId = t2.id ";
        if (acctType != null) {
            sql += "and t1.acctType = ?1 ";
        }
        if (StringUtils.isNotBlank(depositorName)) {
            sql += "and t2.depositorName = ?2 ";
        }
        Query query = em.createQuery(sql);

        if (acctType != null) {//账户类型
            query.setParameter(1, acctType);
        }
        if (StringUtils.isNotBlank(depositorName)) {//存款人名称
            query.setParameter(2, depositorName);
        }
        //查询
        List<AccountsAll> list = query.getResultList();
        if (list.size() > 0) {
            AccountsAll accountsAll = list.get(0);
            if (accountsAll != null) {
                info = new AccountsAllInfo();
                BeanCopierUtils.copyProperties(accountsAll, info);
            }
        } else {
            return null;
        }
        return info;
    }

    @Override
    public Boolean getChangeAuthority(String depositorName) {
        CustomersAllInfo info = customersAllService.getInfo(depositorName, SecurityUtils.getCurrentOrgFullId());
        if(info != null) {
            return true;
        }

        List<AccountsAll> accountsAllList = accountsAllDao.findByOrganFullId(SecurityUtils.getCurrentOrgFullId());

        if(CollectionUtils.isNotEmpty(accountsAllList)) {
            for(AccountsAll accountsAll : accountsAllList) {
                if(StringUtils.isNotBlank(accountsAll.getAcctName()) && depositorName.indexOf(accountsAll.getAcctName()) != -1) {
                    return true;
                }
            }
        }

        return false;
    }

	@Override
	public Long countByAcctNo(String acctNo) {
		return accountsAllDao.countByAcctNo(acctNo);
	}

    @Override
    public List<AnnualAccountVo> getAnnualAccountsAll(String endDay, String organFullId) {
        List<AnnualAccountVo> accountVoList = new ArrayList<>();
        AnnualAccountVo accountVo = null;
        String[] ignoreProperties = {"id", "createdBy", "createdDate", "lastUpdateBy", "lastUpdateDate","organFullId"};
        String[] ignoreProperty = {"id", "createdBy", "createdDate", "lastUpdateBy", "lastUpdateDate", "depositorName", "acctType","organFullId"};
        List<AccountsAll> accountsAllList = null;
        if (StringUtils.isNotBlank(organFullId)) {
            if (endDay == null) {//未配置结束时间
                accountsAllList = accountsAllDao.findByAccountStatusAndOrganFullIdStartsWith(AccountStatus.normal, organFullId);
            } else {//配置结束时间
                accountsAllList = accountsAllDao.findByAccountStatusAndAcctCreateDateLessThanEqualAndOrganFullIdStartsWith(AccountStatus.normal, endDay, organFullId);
            }
        } else {
            if (endDay == null) {//未配置结束时间
                accountsAllList = accountsAllDao.findByAccountStatus(AccountStatus.normal);
            } else {//配置结束时间
                accountsAllList = accountsAllDao.findByAccountStatusAndAcctCreateDateLessThanEqual(AccountStatus.normal, endDay);
            }
        }
        Map<Long, AccountPublicInfo> accountPublicMap = accountPublicService.findAllInMap();
        Map<Long, CustomerPublicLogInfo> customerPublicLogMap = customerPublicLogService.findAllInMap();
        Map<Long, CustomersAllInfo> customersAllInfoMap = customersAllService.findAllInMap();
        Map<Long, CustomerPublicInfo> customerPublicInfoMap = customerPublicService.findAllInMap();
        Map<String, OrganizationDto> organMap = organizationService.findAllInMap();

        if(CollectionUtils.isNotEmpty(accountsAllList)) {
            for(AccountsAll accountsAll : accountsAllList) {
                accountVo = new AnnualAccountVo();
                BeanUtils.copyProperties(accountsAll, accountVo);

                if(accountPublicMap.containsKey(accountsAll.getId())){
                    BeanUtils.copyProperties(accountPublicMap.get(accountsAll.getId()), accountVo, ignoreProperties);
                }

                if(customerPublicLogMap.containsKey(accountsAll.getCustomerLogId())){
                    CustomerPublicLogInfo customerPublicLogInfo = customerPublicLogMap.get(accountsAll.getCustomerLogId());
                    if(customerPublicLogInfo.getCustomerId() !=null){
                        if(customersAllInfoMap.containsKey(customerPublicLogInfo.getCustomerId())){
                            CustomersAllInfo customersAllInfo = customersAllInfoMap.get(customerPublicLogInfo.getCustomerId());
                            BeanUtils.copyProperties(customersAllInfoMap.get(customerPublicLogInfo.getCustomerId()), accountVo, ignoreProperties);

                            if(customerPublicInfoMap.containsKey(customersAllInfo.getId())){
                                BeanUtils.copyProperties(customerPublicInfoMap.get(customersAllInfo.getId()), accountVo, ignoreProperty);
                            }
                        }
                    }
                }



                if(StringUtils.isNotBlank(accountVo.getOrganFullId())) {
                    if(organMap.containsKey(accountVo.getOrganFullId())){
                        accountVo.setOrganCode(organMap.get(accountVo.getOrganFullId()).getCode());
                    }
                }

                accountVoList.add(accountVo);
            }
        }

        /*if(CollectionUtils.isNotEmpty(accountsAllList)) {
            for(AccountsAll accountsAll : accountsAllList) {
                accountVo = new AnnualAccountVo();
                BeanUtils.copyProperties(accountsAll, accountVo);

                AccountPublicInfo accountPublic = accountPublicService.findByAccountId(accountsAll.getId());
                if(accountPublic != null) {
                    BeanUtils.copyProperties(accountPublic, accountVo, ignoreProperties);
                }

                CustomerPublicLogInfo customerPublicLogInfo = customerPublicLogService.getOne(accountsAll.getCustomerLogId());
                if(customerPublicLogInfo != null && customerPublicLogInfo.getCustomerId() != null) {
                    CustomersAllInfo customersAllInfo = customersAllService.findOne(customerPublicLogInfo.getCustomerId());
                    if(customersAllInfo != null) {
                        BeanUtils.copyProperties(customersAllInfo, accountVo, ignoreProperties);

                        CustomerPublicInfo customerPublicInfo = customerPublicService.getByCustomerId(customersAllInfo.getId());
                        if(customerPublicInfo != null) {
                            BeanUtils.copyProperties(customerPublicInfo, accountVo, ignoreProperty);
                        }
                    }

                }

                if(StringUtils.isNotBlank(accountVo.getOrganFullId())) {
                    OrganizationDto organizationDto = organizationService.findByOrganFullId(accountVo.getOrganFullId());
                    if(organizationDto != null) {
                        accountVo.setOrganCode(organizationDto.getCode());
                    }
                }

                accountVoList.add(accountVo);
            }
        }*/

        return accountVoList;
    }

    @Override
    public Set<String> findAcctNoAllInSet() {
        return accountsAllDao.findAcctNoAllInSet();
    }

    @Override
    public Long countTempAcctBefore(final String afterDateStr, final String beforeDateStr, final String organFullId, final Boolean tempAcctOverConfigEnabled) throws ParseException {
        final String nowDateStr = DateUtils.DateToStr(new Date(), "");
        final List<CompanyAcctType> acctTypes = new ArrayList<>();

        acctTypes.add(CompanyAcctType.feilinshi);
        acctTypes.add(CompanyAcctType.linshi);
        acctTypes.add(CompanyAcctType.tempAcct);

        Specification<AccountsAll> specification = new Specification<AccountsAll>() {

            @Override
            public Predicate toPredicate(Root<AccountsAll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                //过期逻辑判断
                expressions.add(cb.like(root.<String>get("organFullId"), organFullId + "%"));

                if (acctTypes.size() > 0) {
                    List<Predicate> list = new ArrayList<>();
                    CriteriaBuilder.In<CompanyAcctType> acctTypeIn = cb.in(root.<CompanyAcctType>get("acctType"));
                    for (CompanyAcctType acctType : acctTypes) {
                        acctTypeIn.value(acctType);
                    }
                    list.add(acctTypeIn);

                    Predicate[] p = new Predicate[list.size()];
                    Predicate and = cb.and(list.toArray(p));
                    expressions.add(and);
                }

                Predicate greaterThanFileDue = cb.greaterThanOrEqualTo(root.<String>get("effectiveDate"), nowDateStr);
                Predicate lessThanFileDue = cb.lessThanOrEqualTo(root.<String>get("effectiveDate"), afterDateStr);

                Predicate effectiveDatePred = cb.and(greaterThanFileDue, lessThanFileDue);
                //超期逻辑判断
                if(tempAcctOverConfigEnabled) {  //开放超期提醒
                    Predicate overEffectiveDateDue = cb.lessThanOrEqualTo(root.<String>get("effectiveDate"), beforeDateStr);
                    Predicate minOverEffectiveDateDue = cb.greaterThanOrEqualTo(root.<String>get("effectiveDate"), "1900-01-01");
                    Predicate overEffectiveDatePred = cb.and(overEffectiveDateDue, minOverEffectiveDateDue);
                    expressions.add(cb.or(effectiveDatePred, overEffectiveDatePred));
                } else {
                    expressions.add(cb.and(effectiveDatePred));
                    expressions.add(cb.equal(root.<Boolean>get("isEffectiveDateOver"), false));
                }

                if (noticeFlag){
                    //增加账户状态过滤
                    expressions.add(cb.equal(root.<AccountStatus>get("accountStatus"),AccountStatus.normal));
                }

                return predicate;
            }
        };

        Long count = accountsAllDao.count(specification);

        return count;
    }
    @Autowired
    private HeGuiYuJingAllDao heGuiYuJingAllDao;
    @Autowired
    private OrganizationDao organizationDao;

    @Override
    public PagingDto<AccountsAllInfo> listTempAcctBefore(final AccountsAllSearchInfo info, final String afterDateStr, final String beforeDateStr, PagingDto pagingDto) throws Exception {
        final String nowDateStr = DateUtils.DateToStr(new Date(), "");
        Pageable pageable = new PageRequest(Math.max(pagingDto.getOffset(), 0), pagingDto.getLimit());
        Pageable pageable1 = new PageRequest(Math.max(pagingDto.getOffset(), 0), 10000);
        final List<CompanyAcctType> acctTypes = new ArrayList<>();
        acctTypes.add(CompanyAcctType.feilinshi);
        acctTypes.add(CompanyAcctType.linshi);
        acctTypes.add(CompanyAcctType.tempAcct);

        info.setAcctTypes(acctTypes);

        Specification<AccountsAll> specification = new Specification<AccountsAll>() {

            @Override
            public Predicate toPredicate(Root<AccountsAll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                //过期逻辑判断
                expressions.add(cb.like(root.<String>get("organFullId"), info.getOrganFullId() + "%"));

                if (acctTypes.size() > 0) {
                    List<Predicate> list = new ArrayList<>();
                    CriteriaBuilder.In<CompanyAcctType> acctTypeIn = cb.in(root.<CompanyAcctType>get("acctType"));
                    for (CompanyAcctType acctType : acctTypes) {
                        acctTypeIn.value(acctType);
                    }
                    list.add(acctTypeIn);

                    Predicate[] p = new Predicate[list.size()];
                    Predicate and = cb.and(list.toArray(p));
                    expressions.add(and);
                }

                Predicate greaterThanFileDue = cb.greaterThanOrEqualTo(root.<String>get("effectiveDate"), nowDateStr);
                Predicate lessThanFileDue = cb.lessThanOrEqualTo(root.<String>get("effectiveDate"), afterDateStr);

                //超期逻辑判断
                Predicate overEffectiveDateDue = cb.lessThanOrEqualTo(root.<String>get("effectiveDate"), beforeDateStr);
                Predicate minOverEffectiveDateDue = cb.greaterThanOrEqualTo(root.<String>get("effectiveDate"), "1900-01-01");

                Predicate effectiveDatePred = cb.and(greaterThanFileDue, lessThanFileDue);
                Predicate overEffectiveDatePred = cb.and(overEffectiveDateDue, minOverEffectiveDateDue);
                expressions.add(cb.or(effectiveDatePred, overEffectiveDatePred));

                if (StringUtils.isNotBlank(info.getAcctNo())) {
                    expressions.add(cb.and(cb.like(root.<String>get("acctNo"), "%" + info.getAcctNo() + "%")));
                }
                if (StringUtils.isNotBlank(info.getAcctName())) {

                    expressions.add(cb.and(cb.like(root.<String>get("acctName"), "%" + info.getAcctName() + "%")));
                }

                if (StringUtils.isNotBlank(info.getBankName())) {
                    expressions.add(cb.and(cb.like(root.<String>get("bankName"), "%" + info.getBankName() + "%")));
                }

                if (StringUtils.isNotBlank(info.getEffectiveDate())) {
                    expressions.add(cb.and(cb.equal(root.<String>get("effectiveDate"), info.getEffectiveDate())));
                }
                if(StringUtils.isNotBlank(info.getCkeckStatus())&&!info.getCkeckStatus().equals("选择")) {
                    expressions.add(cb.and(cb.equal(root.<String>get("ckeckStatus"), info.getCkeckStatus())));
            }
                if(StringUtils.isBlank(info.getCkeckStatus())){
                    expressions.add(cb.and(cb.isNull(root.get("ckeckStatus"))));
        }
                if(info.getIsEffectiveDateOver() != null) {
                    expressions.add(cb.and(cb.equal(root.<String>get("isEffectiveDateOver"), info.getIsEffectiveDateOver())));
                }

                if (noticeFlag){
                    //增加账户状态过滤
                    expressions.add(cb.equal(root.<AccountStatus>get("accountStatus"),AccountStatus.normal));
                }

                return predicate;
            }
        };

//            Page<AccountsAll> page = accountsAllDao.findByAcctTypeInAndEffectiveDateLessThanAndOrganFullIdLike(acctTypes, before, organFullId + "%", pageable);
        Page<AccountsAll> page = accountsAllDao.findAll(specification, pageable);
        Page<AccountsAll> page1 = accountsAllDao.findAll(specification, pageable1);
        List<AccountsAllInfo> dtos = ConverterService.convertToList(page.getContent(), AccountsAllInfo.class);
        List<AccountsAllInfo> dtos1 = ConverterService.convertToList(page1.getContent(), AccountsAllInfo.class);
        for (AccountsAllInfo dto : dtos) {
            if (dto.getCkeckStatus()==null||dto.getCkeckStatus().equals("")){
                dto.setCkeckStatus("未核实");
            }else {
                dto.setCkeckStatus("已核实");
            }

            if(dto.getEffectiveDate()!= null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date bt =sdf.parse(dto.getEffectiveDate());
                String date =  sdf.format(new Date());
                Date ft =sdf.parse(date);
                if(bt.after(ft) || bt.compareTo(ft) == 0){
                    dto.setIsEffectiveDateOver(false);
                    accountsAllDao.updateIsEffectiveDateOver(dto.getId(),dto.getIsEffectiveDateOver());
                }else {
                    dto.setIsEffectiveDateOver(true);
                    accountsAllDao.updateIsEffectiveDateOver(dto.getId(),dto.getIsEffectiveDateOver());
                }
            }
        }
        pagingDto.setList(dtos);
        pagingDto.setTotalRecord(page.getTotalElements());
        pagingDto.setTotalPages(page.getTotalPages());
        return pagingDto;
    }
    @Override
    public PagingDto<AccountsAllInfo> listTempAcctBefore1(final AccountsAllSearchInfo info, final String afterDateStr, final String beforeDateStr, PagingDto pagingDto) throws Exception {
        final String nowDateStr = DateUtils.DateToStr(new Date(), "");
        Pageable pageable = new PageRequest(Math.max(pagingDto.getOffset(), 0), pagingDto.getLimit());
        Pageable pageable1 = new PageRequest(0, 10000);
        final List<CompanyAcctType> acctTypes = new ArrayList<>();
        acctTypes.add(CompanyAcctType.feilinshi);
        acctTypes.add(CompanyAcctType.linshi);
        acctTypes.add(CompanyAcctType.tempAcct);
        info.setAcctTypes(acctTypes);
        info.setOrganFullId("1");
        Specification<AccountsAll> specification = new Specification<AccountsAll>() {
            @Override
            public Predicate toPredicate(Root<AccountsAll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.like(root.<String>get("organFullId"), info.getOrganFullId() + "%"));
                if(acctTypes.size() > 0){
                    List<Predicate> list = new ArrayList<>();
                    CriteriaBuilder.In<CompanyAcctType> acctTypeIn = cb.in(root.<CompanyAcctType>get("acctType"));
                    for (CompanyAcctType acctType : acctTypes) {
                        acctTypeIn.value(acctType);
                    }
                    list.add(acctTypeIn);
                    Predicate[] p = new Predicate[list.size()];
                    Predicate and = cb.and(list.toArray(p));
                    expressions.add(and);
                }
                Predicate greaterThanFileDue = cb.greaterThanOrEqualTo(root.<String>get("effectiveDate"), nowDateStr);
                Predicate lessThanFileDue = cb.lessThanOrEqualTo(root.<String>get("effectiveDate"), afterDateStr);
                Predicate overEffectiveDateDue = cb.lessThanOrEqualTo(root.<String>get("effectiveDate"), beforeDateStr);
                Predicate minOverEffectiveDateDue = cb.greaterThanOrEqualTo(root.<String>get("effectiveDate"), "1900-01-01");
                Predicate effectiveDatePred = cb.and(greaterThanFileDue, lessThanFileDue);
                Predicate overEffectiveDatePred = cb.and(overEffectiveDateDue, minOverEffectiveDateDue);
                expressions.add(cb.or(effectiveDatePred, overEffectiveDatePred));
                if (noticeFlag){
                    expressions.add(cb.equal(root.<AccountStatus>get("accountStatus"),AccountStatus.normal));
                }
                return predicate;
            }
        };
        info.setAcctNo("");
        info.setAcctName("");
        info.setEffectiveDate("");
        info.setBankName("");
        info.setCkeckStatus("选择");
        Page<AccountsAll> page = accountsAllDao.findAll(specification, pageable);
        Page<AccountsAll> page1 = accountsAllDao.findAll(specification, pageable1);
        List<AccountsAllInfo> dtos = ConverterService.convertToList(page.getContent(), AccountsAllInfo.class);
        List<AccountsAllInfo> dtos1 = ConverterService.convertToList(page1.getContent(), AccountsAllInfo.class);
        for (AccountsAllInfo dto : dtos) {
            if (dto.getCkeckStatus()==null||dto.getCkeckStatus().equals("")){
                dto.setCkeckStatus("未核实");
            }else {
                dto.setCkeckStatus("已核实");
            }
            if(dto.getEffectiveDate()!= null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date bt =sdf.parse(dto.getEffectiveDate());
                String date =  sdf.format(new Date());
                Date ft =sdf.parse(date);
                if(bt.after(ft) || bt.compareTo(ft) == 0){
                    dto.setIsEffectiveDateOver(false);
                    accountsAllDao.updateIsEffectiveDateOver(dto.getId(),dto.getIsEffectiveDateOver());
                }else {
                    dto.setIsEffectiveDateOver(true);
                    accountsAllDao.updateIsEffectiveDateOver(dto.getId(),dto.getIsEffectiveDateOver());
                }
            }
        }
        heGuiYuJingAllDao.deleteAllByYuJingType("1");
        log.info("进入临时户全量表赋值");
        log.info("list数量"+dtos1.size());
        log.info("list数量"+dtos.size());
           if(dtos1!=null && dtos1.size() > 0){
                for(AccountsAllInfo dto : dtos1){
                  try {
                      HeGuiYuJingAll heGuiYuJingAll = new HeGuiYuJingAll();
                      if (StringUtils.isNotBlank(dto.getCkeckStatus())) {
                          heGuiYuJingAll.setCkeckStatus(dto.getCkeckStatus());
                      }
                      if (StringUtils.isNotBlank(dto.getEffectiveDate())) {
                          heGuiYuJingAll.setEffectiveDate(dto.getEffectiveDate());
                      }
                      if (StringUtils.isNotBlank(dto.getAcctCreateDate())) {
                          heGuiYuJingAll.setAcctCreateDate(dto.getAcctCreateDate());
                      }
                      if (StringUtils.isNotBlank(dto.getBankName())) {
                          heGuiYuJingAll.setBankName(dto.getBankName());
                      }
                      if (StringUtils.isNotBlank(dto.getBankCode())) {
                          heGuiYuJingAll.setBankCode(dto.getBankCode());
                      }
                      if (StringUtils.isNotBlank(dto.getAcctName())) {
                          heGuiYuJingAll.setAcctName(dto.getAcctName());
                      }
                      if (StringUtils.isNotBlank(dto.getAcctNo())) {
                          heGuiYuJingAll.setAcctNo(dto.getAcctNo());
                      }
                      if (dto.getIsEffectiveDateOver() != null) {
                          heGuiYuJingAll.setIsEffectiveDateOver(dto.getIsEffectiveDateOver());
                      }
                      if (StringUtils.isNotBlank(dto.getBankCode())) {
                          List<OrganizationPo> organizationPo = organizationDao.findByPbcCode(dto.getBankCode());
                          if (organizationPo != null && organizationPo.size() > 0) {
                              heGuiYuJingAll.setOrganCode(organizationPo.get(0).getCode());
                              OrganizationMapPo organizationMapPo = organizationMapDao.findByOrgancode(organizationPo.get(0).getCode());
                              if (organizationMapPo != null) {
                                  heGuiYuJingAll.setDmpOrganCode(organizationMapPo.getDmpOrganCode());
                              } }}
                      heGuiYuJingAll.setYuJingType("1");
                      heGuiYuJingAllDao.save(heGuiYuJingAll);
                  }catch (Exception e){
                   log.error("报存报错"+e.getMessage());
                  }
                }
                log.info("保存全量预警数据临时户-----");
            }

        pagingDto.setList(dtos);
        pagingDto.setTotalRecord(page.getTotalElements());
        pagingDto.setTotalPages(page.getTotalPages());
        return pagingDto;
    }

    @Override
    public List<AccountsAllInfo> getTempAcctDueAndOver(final String afterDateStr, final String beforeDateStr, final Boolean tempAcctOverConfigEnabled) {

        final List<CompanyAcctType> acctTypes = new ArrayList<>();
        acctTypes.add(CompanyAcctType.feilinshi);
        acctTypes.add(CompanyAcctType.linshi);
        acctTypes.add(CompanyAcctType.tempAcct);

        Specification<AccountsAll> specification = new Specification<AccountsAll>() {

            @Override
            public Predicate toPredicate(Root<AccountsAll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if(acctTypes.size() > 0){
                    List<Predicate> list = new ArrayList<>();
                    CriteriaBuilder.In<CompanyAcctType> acctTypeIn = cb.in(root.<CompanyAcctType>get("acctType"));
                    for (CompanyAcctType acctType : acctTypes) {
                        acctTypeIn.value(acctType);
                    }
                    list.add(acctTypeIn);

                    Predicate[] p = new Predicate[list.size()];
                    Predicate and = cb.and(list.toArray(p));
                    expressions.add(and);
                }

                //过期逻辑判断
                Predicate lessThanFileDue = cb.equal(root.<String>get("effectiveDate"), afterDateStr);

                //超期配置判断
                if(tempAcctOverConfigEnabled) {  //超期
                    Predicate overEffectiveDateDue = cb.equal(root.<String>get("effectiveDate"), beforeDateStr);
                    expressions.add(cb.or(lessThanFileDue, overEffectiveDateDue));
                } else {
                    expressions.add(cb.and(lessThanFileDue));
                }

                if (noticeFlag){
                    //增加账户状态过滤
                    expressions.add(cb.equal(root.<AccountStatus>get("accountStatus"),AccountStatus.normal));
                }

                return predicate;
            }
        };

        List<AccountsAll> accountsAllList = accountsAllDao.findAll(specification);
        return ConverterService.convertToList(accountsAllList,AccountsAllInfo.class);
    }

    @Override
    public void deleteById(Long id) {
        accountsAllDao.delete(id);
    }

    private AccountsAllInfo po2dto(AccountsAll accountsAll) {
        if (accountsAll != null) {
            AccountsAllInfo accountsAllInfo = new AccountsAllInfo();
            BeanCopierUtils.copyProperties(accountsAll, accountsAllInfo);
            return accountsAllInfo;
        } else {
            return null;
        }
    }


    @Override
    public List<AccountsAllInfo> findByOrganFullIdLike(String organFullId) {
        return ConverterService.convertToList(accountsAllDao.findByOrganFullIdLike(organFullId),AccountsAllInfo.class);
    }

    @Override
    public List<AccountsAllInfo> findByCustomerNo(String customerNo) {
        return ConverterService.convertToList(accountsAllDao.findByCustomerNo(customerNo),AccountsAllInfo.class);
    }

    @Override
    public AccountBillsAllInfo getFirstBillByCustomerNo(String customerNo) {
        AccountsAll accountsAll = accountsAllDao.findFirstByCustomerNoOrderByCreatedDate(customerNo);
        if (accountsAll != null) {
            return accountBillsAllService.getFirstBillByAccountId(accountsAll.getId());
        } else {
            return null;
        }
    }

    @Override
    public AccountBillsAllInfo getFirstBillByCustomerId(Long customerId) {
        CustomerPublicLogInfo cpl = customerPublicLogService.getMaxSeq(customerId);//存在问题

        if (cpl != null) {
            return accountBillsAllService.getFirstBillByCustomerLogId(cpl.getId());
        } else {
            return null;
        }
    }

    @Override
    public boolean isHeZhun2BaoBei(AllBillsPublicDTO allBillsPublicDTO) {
        return false;
    }

    @Override
    public int updateCancelHezhun(Long id, String selectPwd, String openKey, String accountKey) {
        return accountsAllDao.updateCancelHezhun(id, selectPwd,openKey,accountKey);
    }

    /**
     * 导出已开立其他银行账户清单excel
     * @param depositorName 存款人名称
     * @param accountKey    基本存款账户销户日期
     * @param cancelDate    基本存款账户销户日期
     * @param apiList       其他银行账户清单
     */
    @Override
    public HSSFWorkbook exportExcel(String depositorName, String accountKey, String cancelDate, List<AmsPrintInfo> apiList) {
        HSSFWorkbook wb = new HSSFWorkbook();//创建Excel工作簿对象

        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 18);//字号
        HSSFFont font2 = wb.createFont();
        font2.setFontName("宋体");
        font2.setFontHeightInPoints((short) 12);//字号

        HSSFFont redFont = wb.createFont();
        redFont.setColor(HSSFColor.RED.index);// 红色
        //文本格式
        HSSFCellStyle textStyle = wb.createCellStyle();
        HSSFDataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));//设置格式为文本格式
        //标题样式
        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setFont(font);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
        style1.setWrapText(true);//自动换行
        //表格内容样式
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
        style2.setWrapText(true);//自动换行
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        HSSFSheet sheet = wb.createSheet("已开立其他银行账户清单");//创建Excel工作表对象
        //设置列宽
        double[] columnWidthArr = {7, 17, 34, 22, 24, 57, 10, 10, 10};
        for (int i = 0; i < columnWidthArr.length; i++) {
            sheet.setColumnWidth(i, (short) (256 * columnWidthArr[i]));//设置列宽
            sheet.setDefaultColumnStyle(i, textStyle);//设置格式为文本格式
        }
        //设置表头
        HSSFRow row0 = sheet.createRow((short) 0); //创建Excel工作表的第一行
        row0.setHeight((short) (66 * 20));//设置行高
        HSSFCell cell = row0.createCell(0);
        cell.setCellStyle(style1); //设置Excel指定单元格的样式
        HSSFRichTextString row1String = new HSSFRichTextString("已开立其他银行账户清单");
        cell.setCellValue(row1String); //设置Excel指定单元格的值

        HSSFRow row1 = sheet.createRow((short) 1);
        row1.setHeight((short) (22 * 20));//设置行高
        setCell(row1, 0, style2, "存款人名称");
        setCell(row1, 2, style2, StringUtils.defaultString(depositorName));
        HSSFRow row2 = sheet.createRow((short) 2);
        row2.setHeight((short) (22 * 20));//设置行高
        setCell(row2, 0, style2, "基本存款账户编号");
        setCell(row2, 2, style2, StringUtils.defaultString(accountKey));
        HSSFRow row3 = sheet.createRow((short) 3);
        row3.setHeight((short) (22 * 20));//设置行高
        setCell(row3, 0, style2, "基本存款账户销户日期");
        setCell(row3, 2, style2, StringUtils.defaultString(cancelDate));

        HSSFRow row5 = sheet.createRow((short) 5);
        setCell(row5, 0, style2, "序号");
        setCell(row5, 1, style2, "开户地地区代码");
        setCell(row5, 2, style2, "开户银行名称");
        setCell(row5, 3, style2, "账户性质");
        setCell(row5, 4, style2, "账号");
        setCell(row5, 5, style2, "账户名称");
        setCell(row5, 6, style2, "开户日期");
        setCell(row5, 7, style2, "账户状态");
        setCell(row5, 8, style2, "销户日期");

        for (int i = 0; i < apiList.size(); i++) {
            HSSFRow row = sheet.createRow((short) (6 + i));
            setCell(row, 0, style2, String.valueOf(i + 1));//序号
            setCell(row, 1, style2, StringUtils.defaultString(apiList.get(i).getBankAreaCode()));//开户地地区代码
            setCell(row, 2, style2, StringUtils.defaultString(apiList.get(i).getBankName()));//开户银行名称
            setCell(row, 3, style2, StringUtils.defaultString(apiList.get(i).getAcctType()));//账户性质
            setCell(row, 4, style2, StringUtils.defaultString(apiList.get(i).getAcctNo()));//账号
            setCell(row, 5, style2, StringUtils.defaultString(apiList.get(i).getAcctName()));//账户名称
            setCell(row, 6, style2, StringUtils.defaultString(apiList.get(i).getAcctCreateDate()));//开户日期
            setCell(row, 7, style2, StringUtils.defaultString(apiList.get(i).getAccountStatus()));//账户状态
            setCell(row, 8, style2, StringUtils.defaultString(apiList.get(i).getCancelDate()));//销户日期
        }
        //合并单元格
        String[] mergedRegionArr = {"$A$1:$I$1", "$A$2:$B$2", "$A$3:$B$3", "$A$4:$B$4", "$C$2:$I$2", "$C$3:$I$3", "$C$4:$I$4"};
        for (int i = 0; i < mergedRegionArr.length; i++) {
            CellRangeAddress cra = CellRangeAddress.valueOf(mergedRegionArr[i]);
            sheet.addMergedRegion(cra);
            if (i == 0) {
                continue;
            }
            //额外设置合并单元格后的边框
            RegionUtil.setBorderBottom(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
            RegionUtil.setBorderLeft(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
            RegionUtil.setBorderTop(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
            RegionUtil.setBorderRight(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
        }
        return wb;
    }

    @Override
    public Set<String> findAcctNoFromAccountStatus(AccountStatus accountStatus) {
        return accountsAllDao.findAcctNoFromAccountStatus(accountStatus);
    }

    private void setCell(HSSFRow row, int column, HSSFCellStyle style, Object value) {
        HSSFCell cell = row.createCell(column);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(value == null ? "" : String.valueOf(value)));
    }

}
