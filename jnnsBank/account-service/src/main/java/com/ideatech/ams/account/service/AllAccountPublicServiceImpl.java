package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.dao.AllAccountPublicDao;
import com.ideatech.ams.account.dao.spec.AccountsAllSpec;
import com.ideatech.ams.account.dto.*;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.poi.CompanyAccountPoi;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.poi.CompanyAccountPoiExport;
import com.ideatech.ams.customer.dto.*;
import com.ideatech.ams.customer.service.*;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.jpa.IdeaNamingStrategy;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.BeanValueUtils;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import com.ideatech.common.util.SqlBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class AllAccountPublicServiceImpl implements AllAccountPublicService {

    @Autowired
    private AllAccountPublicDao allAccountPublicDao;

    @Autowired
    private AccountsAllDao accountsAllDao;

    @Autowired
    private AccountPublicService accountPublicService;

    @Autowired
    private AccountPublicLogService accountPublicLogService;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private CustomerPublicLogService customerPublicLogService;

    @Autowired
    private CustomerPublicMidService customerPublicMidService;

    @Autowired
    private CustomerPublicService customerPublicService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CompanyPartnerMidService companyPartnerMidService;

    @Autowired
    private CompanyPartnerLogService companyPartnerLogService;

    @Autowired
    private CompanyPartnerService companyPartnerService;

    @Autowired
    private RelateCompanyService relateCompanyService;

    @Autowired
    private RelateCompanyMidService relateCompanyMidService;

    @Autowired
    private RelateCompanyLogService relateCompanyLogService;

    @Override
    public ObjectRestResponse<AllAccountPublicDTO> getDetailsByAccountId(Long accountId){
        AllAccountPublicDTO allAccountPublicDTO = new AllAccountPublicDTO();
        AccountsAll accountsAll = accountsAllDao.findOne(accountId);

        String[] ignoreProperties = {"id", "createdDate", "createdBy", "lastUpdateBy", "lastUpdateDate","acctType"};
        BeanUtils.copyProperties(accountsAll, allAccountPublicDTO,ignoreProperties);
        String[] ignoreProperties1 = {"id", "createdDate", "createdBy", "lastUpdateBy", "lastUpdateDate","acctType","string006"};
        AccountBillsAllInfo accountBillsAllInfo = null;
        Long customerLogId = accountsAll.getCustomerLogId();

        //查询账户日志表，（每次操作AccountsAll表前，都会将当前的信息拷贝到账户日志表）
        AccountPublicLogInfo accountPublicLogInfo = accountPublicLogService.getMaxSeq(accountId);
        if (accountPublicLogInfo!=null){
            //拷贝账户日志表
            BeanUtils.copyProperties(accountPublicLogInfo, allAccountPublicDTO,ignoreProperties1);

            //查询流水表信息（账户日志表关联流水）
            accountBillsAllInfo = accountBillsAllService.getOne(accountPublicLogInfo.getRefBillId());
            if (accountBillsAllInfo!=null){
                customerLogId = accountBillsAllInfo.getCustomerLogId();
                BeanUtils.copyProperties(accountBillsAllInfo, allAccountPublicDTO);
                if(accountBillsAllInfo.getCreatedDate() != null){
                    allAccountPublicDTO.setCreatedDate(DateUtils.DateToStr(accountBillsAllInfo.getCreatedDate(),"yyyy-MM-dd HH:mm:ss"));
                }
            }
        }else{
            AccountPublicInfo accountPublicInfo = accountPublicService.findByAccountId(accountsAll.getId());
            BeanUtils.copyProperties(accountPublicInfo, allAccountPublicDTO, ignoreProperties1);
        }

        //为解决流水完成时，账户日志表没有最新的账户信息。（之前设计账户主表，对公表，对公日志表的逻辑导致）
        //查询流水信息（AccountsAll关联流水），如果流水完成则覆盖账户日志表关联流水信息（覆盖上一步操作）。
        accountBillsAllInfo = accountBillsAllService.getOne(accountsAll.getRefBillId());
        if (accountBillsAllInfo!=null){
            //判断当前流水是否完成，或者是否为开户操作
            if (accountBillsAllInfo.getFinalStatus()== CompanyIfType.Yes||accountBillsAllInfo.getBillType()==BillType.ACCT_OPEN){
                customerLogId = accountBillsAllInfo.getCustomerLogId();
                BeanUtils.copyProperties(accountBillsAllInfo, allAccountPublicDTO);
                if(accountBillsAllInfo.getCreatedDate() != null){
                    allAccountPublicDTO.setCreatedDate(DateUtils.DateToStr(accountBillsAllInfo.getCreatedDate(),"yyyy-MM-dd HH:mm:ss"));
                }
                BeanUtils.copyProperties(accountsAll, allAccountPublicDTO,ignoreProperties1);
            }
            //流水完成时，
            if(accountBillsAllInfo.getFinalStatus()== CompanyIfType.Yes){
                AccountPublicInfo accountPublicInfo = accountPublicService.findByAccountId(accountsAll.getId());
                BeanUtils.copyProperties(accountPublicInfo, allAccountPublicDTO, ignoreProperties1);
            }
        }


        //TODO 主表、日志表、中间表需完善
        CustomerPublicLogInfo customerPublicLogInfo = customerPublicLogService.getOne(customerLogId);
        if(customerPublicLogInfo != null){
            String[] ignoreProperties2 = {"id", "createdDate", "createdBy", "lastUpdateBy", "lastUpdateDate","acctType","operatorIdcardDue","accountKey","string006"};
            BeanUtils.copyProperties(customerPublicLogInfo, allAccountPublicDTO, ignoreProperties2);

            //股东信息
            List<CompanyPartnerLogInfo> companyPartnerLogInfoList= companyPartnerLogService.getAllByCustomerPublicLogId(customerPublicLogInfo.getId());
            List<CompanyPartnerInfo> companyPartnerInfos = ConverterService.convertToList(companyPartnerLogInfoList, CompanyPartnerInfo.class);
            Set<CompanyPartnerInfo> companyPartnerInfoHashSet = new TreeSet<>();
            for(CompanyPartnerInfo c : companyPartnerInfos){
                companyPartnerInfoHashSet.add(c);
            }
            allAccountPublicDTO.setCompanyPartners(companyPartnerInfoHashSet);

            //关联企业
            List<RelateCompanyLogInfo> relateCompanyLogInfos = relateCompanyLogService.getAllByCustomerPublicLogId(customerPublicLogInfo.getId());
            List<RelateCompanyInfo> relateCompanyInfos = ConverterService.convertToList(relateCompanyLogInfos, RelateCompanyInfo.class);
            Set<RelateCompanyInfo> relateCompanyInfoset = new TreeSet<>();
            for(RelateCompanyInfo c : relateCompanyInfos){
                relateCompanyInfoset.add(c);
            }
            allAccountPublicDTO.setRelateCompanys(relateCompanyInfoset);

        }else{
            CustomerPublicInfo customerPublicInfo = customerPublicService.getByCustomerId(customerLogId);
            if (customerPublicInfo!=null){
                BeanUtils.copyProperties(customerPublicInfo, allAccountPublicDTO, ignoreProperties);

                //股东信息
                List<CompanyPartnerInfo> companyPartnerInfoList= companyPartnerService.getAllByCustomerPublicId(customerPublicInfo.getId());
                Set<CompanyPartnerInfo> companyPartnerInfoHashSet = new TreeSet<>();
                for(CompanyPartnerInfo c : companyPartnerInfoList){
                    companyPartnerInfoHashSet.add(c);
                }
                allAccountPublicDTO.setCompanyPartners(companyPartnerInfoHashSet);

                //关联企业
                List<RelateCompanyInfo> relateCompanyInfos = relateCompanyService.getAllByCustomerPublicId(customerPublicInfo.getId());
                Set<RelateCompanyInfo> relateCompanyInfoset = new TreeSet<>();
                for(RelateCompanyInfo c : relateCompanyInfos){
                    relateCompanyInfoset.add(c);
                }
                allAccountPublicDTO.setRelateCompanys(relateCompanyInfoset);

            }else {
                CustomerPublicMidInfo customerPublicMidInfo = customerPublicMidService.getOne(customerLogId);
                if(customerPublicMidInfo !=null){
                    BeanUtils.copyProperties(customerPublicMidInfo, allAccountPublicDTO, ignoreProperties);

                    //股东信息
                    List<CompanyPartnerMidInfo> companyPartnerMidInfoList = companyPartnerMidService.getAllByCustomerPublicMidId(customerPublicMidInfo.getId());
                    List<CompanyPartnerInfo> companyPartnerInfoList = ConverterService.convertToList(companyPartnerMidInfoList, CompanyPartnerInfo.class);
                    Set<CompanyPartnerInfo> companyPartnerInfoHashSet = new TreeSet<>();
                    for(CompanyPartnerInfo c : companyPartnerInfoList){
                        companyPartnerInfoHashSet.add(c);
                    }
                    allAccountPublicDTO.setCompanyPartners(companyPartnerInfoHashSet);


                    //关联企业
                    List<RelateCompanyMidInfo> relateCompanyMidInfos = relateCompanyMidService.getAllByCustomerPublicMidId(customerPublicMidInfo.getId());
                    List<RelateCompanyInfo> relateCompanyInfos = ConverterService.convertToList(relateCompanyMidInfos, RelateCompanyInfo.class);
                    Set<RelateCompanyInfo> relateCompanyInfoset = new TreeSet<>();
                    for(RelateCompanyInfo c : relateCompanyInfos){
                        relateCompanyInfoset.add(c);
                    }
                    allAccountPublicDTO.setRelateCompanys(relateCompanyInfoset);
                }
            }
        }

        return new ObjectRestResponse<AllAccountPublicDTO>().rel(true).result(allAccountPublicDTO);
    }

    /**
     * 取消yd_public_accountallinfo_v
     * 已经废弃
     * 使用getDetailsByAccountId(Long accountId)方法
     * @param dataId
     * @return
     */
    @Override
    @Deprecated
    public ObjectRestResponse<AllAccountPublicDTO> getDetails(Long dataId) {
        AllAccountPublicDTO info1 = allAccountPublicDao.findById(dataId);

        if (info1 != null) {
            //处理创建时间后面的数字
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                Date date = sdf.parse(info1.getCreatedDate());

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                info1.setCreatedDate(format.format(date));
            } catch (Exception e) {

                e.printStackTrace();
            }
            return new ObjectRestResponse<AllAccountPublicDTO>().rel(true).result(info1);
        }
        return new ObjectRestResponse<AllAccountPublicDTO>().rel(false).result("");
    }

    @Override
    public TableResultResponse<AllAccountPublicSearchDTO> query(AllAccountPublicSearchDTO info, String certainOrganFullId, Pageable pageable){

        AccountsAllSearchInfo accountsAllSearchInfo = new AccountsAllSearchInfo();
        AccountPublicInfo accountPublicInfo = null;
        AccountPublicLogInfo accountPublicLogInfo = null;
        List<AllAccountPublicSearchDTO> accountsAllVoList = new ArrayList<>();

        info.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        String orgCode = info.getOrgCode();
        if(StringUtils.isNotBlank(orgCode)){
            OrganizationDto organizationDtoByCode = organizationService.findByCode(orgCode);
//            if(organizationDtoByCode!=null && StringUtils.contains(info.getOrganFullId(),organizationDtoByCode.getFullId())){
            if(organizationDtoByCode!=null && StringUtils.startsWith(organizationDtoByCode.getFullId(),info.getOrganFullId())){
                info.setOrganFullId(organizationDtoByCode.getFullId());
            }
        }
        BeanUtils.copyProperties(info, accountsAllSearchInfo);
        accountsAllSearchInfo.setCertainOrganFullId(certainOrganFullId);
        //存量数据查询
        List<String> string003s = new ArrayList<>();
        if(org.apache.commons.lang.StringUtils.equals(accountsAllSearchInfo.getString003(),"1")){//存量数据
            string003s.add("1");
            accountsAllSearchInfo.setString003s(string003s);
        }else if(org.apache.commons.lang.StringUtils.equals(accountsAllSearchInfo.getString003(),"0")){//增量
            string003s.add("");
            string003s.add("0");
            string003s.add(null);
            accountsAllSearchInfo.setString003s(string003s);
        }

        if(!"accountStatistics".equals(info.getString005())) {  //排除账户信息统计详情页白名单判断
            //白名单增加查询条件
            List<String> whiteLists = new ArrayList<String>();
            if(StringUtils.isEmpty(info.getWhiteList()) || "0".equals(info.getWhiteList())){
                whiteLists.add("0");
                whiteLists.add("");
                whiteLists.add(null);
            }else{
                whiteLists.add("1");
            }

            accountsAllSearchInfo.setWhiteLists(whiteLists);
        }

        Page<AccountsAll> page = accountsAllDao.findAll(new AccountsAllSpec(accountsAllSearchInfo), pageable);
        long count = accountsAllDao.count(new AccountsAllSpec(accountsAllSearchInfo));
         List<AccountsAll> accountsAllList = page.getContent();

        //tc-353  先销户后开户时  查找账户log日志覆盖时  忽略账户状态
        String[] ignoreProperties = {"id", "createdDate", "createdBy", "lastUpdateBy", "lastUpdateDate","acctType","cancelDate","organFullId","accountStatus"};
        if(CollectionUtils.isNotEmpty(accountsAllList)) {
            for(AccountsAll accountsAll : accountsAllList) {
                BeanUtils.copyProperties(accountsAll, info);
                accountPublicInfo = accountPublicService.findByAccountId(accountsAll.getId());
                if (accountPublicInfo !=null){
                    BeanUtils.copyProperties(accountPublicInfo, info, ignoreProperties);
                }
                AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.findFirstByAcctNoOrderByCreatedDateDesc(accountsAll.getAcctNo());

                //流水表根据账号查询没有就跳过
                if(accountBillsAllInfo.getId() == null){
                    continue;
                }

                if(accountBillsAllInfo != null) {
                    BeanUtils.copyProperties(accountBillsAllInfo, info, ignoreProperties);
                    if(accountBillsAllInfo.getCreatedDate() != null){
                        info.setCreatedDate(DateUtils.DateToStr(accountBillsAllInfo.getCreatedDate(),"yyyy-MM-dd HH:mm:ss"));
                    }
                }

                //当前流水未完成时，或者accountsAll关联流水不等于accountBillsAllInfo的ID，查询账户历史表。
                if (accountBillsAllInfo.getFinalStatus()==CompanyIfType.No||!accountBillsAllInfo.getId().equals(accountsAll.getRefBillId())){
                    accountPublicLogInfo = accountPublicLogService.getMaxSeq(accountsAll.getId());
                    if(accountPublicLogInfo!=null){
                        BeanUtils.copyProperties(accountPublicLogInfo, info, ignoreProperties);
                    }
                }

                //log数据
                if (accountsAll.getCustomerLogId() != null) {
                    CustomerPublicLogInfo customerPublicLogInfo = customerPublicLogService.getOne(accountsAll.getCustomerLogId());
                    if (customerPublicLogInfo != null) {
                        BeanUtils.copyProperties(customerPublicLogInfo, info, ignoreProperties);
                    }

                    //mid数据
                    CustomerPublicMidInfo customerPublicMidInfo = customerPublicMidService.getOne(accountsAll.getCustomerLogId());
                    if (customerPublicMidInfo != null) {
                        BeanUtils.copyProperties(customerPublicMidInfo, info, ignoreProperties);
                    }
                }

                if (accountBillsAllInfo.getFinalStatus()==CompanyIfType.No){
                    //原来逻辑取最新一条流水，存在问题（流水未完成时不应该在列表页显示未完成流水的信息）
                    //怕改变原有逻辑带来未知错误，现增加覆盖数据操作（取最新已完成的流水信息进行覆盖）
                    //放在CustomerPublicLog、CustomerPublicMid之后，是因为完成流水表中一些信息可能会被CustomerPublic操作覆盖：比如存款人名称
                    AccountBillsAllInfo accountBillsAllInfoFinished = accountBillsAllService.findLatestFinishedByAcctNo(accountsAll.getAcctNo());
                    if (accountBillsAllInfoFinished!=null){
                        BeanUtils.copyProperties(accountBillsAllInfoFinished, info, ignoreProperties);
                        if(accountBillsAllInfoFinished.getCreatedDate() != null){
                            info.setCreatedDate(DateUtils.DateToStr(accountBillsAllInfoFinished.getCreatedDate(),"yyyy-MM-dd HH:mm:ss"));
                        }
                    }
                }

                if (StringUtils.isNotBlank(info.getOrganFullId())) {
                    OrganizationDto od = organizationService.findByOrganFullId(info.getOrganFullId());
                    if (od != null) {
                        info.setKernelOrgCode(od.getCode());
                        info.setKernelOrgName(od.getName());
                    }
                }

                accountsAllVoList.add(info);
                info = new AllAccountPublicSearchDTO();
            }
        }

        return new TableResultResponse<AllAccountPublicSearchDTO>((int)count, accountsAllVoList);
    }

    @Override
    public TableResultResponse<AllAccountPublicSearchDTO> query(Long id, Pageable pageable) {
        List<CustomerPublicLogInfo> custPubLogList = customerPublicLogService.getByCustomerId(id);
        if (custPubLogList.size() == 0) {
            return new TableResultResponse<AllAccountPublicSearchDTO>(0, new ArrayList<AllAccountPublicSearchDTO>());
        }
        List<Long> customerLogIdList = new ArrayList<>();
        for (CustomerPublicLogInfo cpli : custPubLogList) {
            customerLogIdList.add(cpli.getId());
        }

        AccountPublicInfo accountPublicInfo = null;
        AllAccountPublicSearchDTO info = new AllAccountPublicSearchDTO();
        List<AllAccountPublicSearchDTO> accountsAllVoList = new ArrayList<>();
        AccountsAllSearchInfo accountsAllSearchInfo = new AccountsAllSearchInfo();
        accountsAllSearchInfo.setCustomerLogIdList(customerLogIdList);

        Page<AccountsAll> page = accountsAllDao.findAll(new AccountsAllSpec(accountsAllSearchInfo), pageable);
        long count = accountsAllDao.count(new AccountsAllSpec(accountsAllSearchInfo));
        List<AccountsAll> accountsAllList = page.getContent();

        String[] ignoreProperties = {"id", "createdDate", "createdBy", "lastUpdateBy", "lastUpdateDate","acctType","cancelDate"};
        if(CollectionUtils.isNotEmpty(accountsAllList)) {
            for(AccountsAll accountsAll : accountsAllList) {
                BeanUtils.copyProperties(accountsAll, info);
                accountPublicInfo = accountPublicService.findByAccountId(accountsAll.getId());
                BeanUtils.copyProperties(accountPublicInfo, info, ignoreProperties);

                AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.findFirstByAcctNoOrderByCreatedDateDesc(accountsAll.getAcctNo());
                if(accountBillsAllInfo != null) {
                    BeanUtils.copyProperties(accountBillsAllInfo, info, ignoreProperties);
                    if(accountBillsAllInfo.getCreatedDate() != null){
                        info.setCreatedDate(DateUtils.DateToStr(accountBillsAllInfo.getCreatedDate(),"yyyy-MM-dd HH:mm:ss"));
                    }
                }

                //log数据
                if (accountsAll.getCustomerLogId() != null) {
                    CustomerPublicLogInfo customerPublicLogInfo = customerPublicLogService.getOne(accountsAll.getCustomerLogId());
                    if (customerPublicLogInfo != null) {
                        BeanUtils.copyProperties(customerPublicLogInfo, info, ignoreProperties);
                    }

                    //mid数据
                    CustomerPublicMidInfo customerPublicMidInfo = customerPublicMidService.getOne(accountsAll.getCustomerLogId());
                    if (customerPublicMidInfo != null) {
                        BeanUtils.copyProperties(customerPublicMidInfo, info, ignoreProperties);
                    }
                }

                if (StringUtils.isNotBlank(info.getOrganFullId())) {
                    OrganizationDto od = organizationService.findByOrganFullId(info.getOrganFullId());
                    if (od != null) {
                        info.setKernelOrgCode(od.getCode());
                        info.setKernelOrgName(od.getName());
                    }
                }

                accountsAllVoList.add(info);
                info = new AllAccountPublicSearchDTO();
            }
        }

        return new TableResultResponse<AllAccountPublicSearchDTO>((int)count, accountsAllVoList);
    }

    @Override
    public TableResultResponse<AllAccountPublicDTO> queryStockList(final AllAccountPublicDTO info, Pageable pageable) {
        info.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        Specification<AccountsAll> specification = new Specification<AccountsAll>() {
            @Override
            public Predicate toPredicate(Root<AccountsAll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (StringUtils.isNotBlank(info.getOrganFullId())) {
                    expressions.add(cb.like(root.<String>get("organFullId"), info.getOrganFullId() + "%"));

                }
                if (StringUtils.isNotBlank(info.getAcctNo())) {
                    expressions.add(cb.like(root.<String>get("acctNo"), "%" + info.getAcctNo() + "%"));
                }
                if (StringUtils.isNotBlank(info.getAcctName())) {
                    expressions.add(cb.like(root.<String>get("acctName"), "%" + info.getAcctName() + "%"));
                }
                if (StringUtils.isNotBlank(info.getBankCode())) {
                    expressions.add(cb.like(root.<String>get("bankCode"), "%" + info.getBankCode() + "%"));
                }
                if (StringUtils.isNotBlank(info.getCustomerNo())) {
                    expressions.add(cb.like(root.<String>get("customerNo"), "%" + info.getCustomerNo() + "%"));
                }
                if (info.getAccountStatus() != null) {
                    expressions.add(cb.equal(root.get("accountStatus"), info.getAccountStatus()));
                }
                if (info.getOpenAccountSiteType() != null) {
                    expressions.add(cb.equal(root.get("openAccountSiteType"), info.getOpenAccountSiteType()));
                }
                if (info.getAcctType() != null) {
                    expressions.add(cb.equal(root.get("acctType"), info.getAcctType()));
                }
                if (StringUtils.isNotBlank(info.getString003())) {
                    expressions.add(cb.equal(root.get("string003"), info.getString003()));
                }
                if (StringUtils.isNotBlank(info.getString004())) {
                    if (info.getString004().equals("0")) {
                        Predicate predicate1 = cb.equal(root.get("string004"), "0");
                        Predicate predicate2 = cb.isNull(root.get("string004"));
                        expressions.add(cb.or(predicate1, predicate2));
                    } else {
                        expressions.add(cb.equal(root.get("string004"), info.getString004()));
                    }
                }
                return predicate;
            }
        };

        Page<AccountsAll> page = accountsAllDao.findAll(specification, pageable);
        long count = accountsAllDao.count(specification);
        List<AccountsAll> aaList = page.getContent();
        List<AllAccountPublicDTO> aapdList = new ArrayList<>();
        for (AccountsAll aa : aaList) {
            AllAccountPublicDTO aapd = new AllAccountPublicDTO();
            BeanUtils.copyProperties(aa, aapd);
            aapdList.add(aapd);
        }

        return new TableResultResponse<>((int) count, aapdList);
    }

    @Override
    public IExcelExport query(AllAccountPublicSearchDTO info) {
        IExcelExport iExcelExport = new CompanyAccountPoiExport();
        List<CompanyAccountPoi> companyAccountPois = new ArrayList<>();

        AccountsAllSearchInfo accountsAllSearchInfo = new AccountsAllSearchInfo();
        info.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        String orgCode = info.getOrgCode();
        if(StringUtils.isNotBlank(orgCode)){
            OrganizationDto organizationDtoByCode = organizationService.findByCode(orgCode);
            if(organizationDtoByCode!=null && StringUtils.startsWith(organizationDtoByCode.getFullId(),info.getOrganFullId())){
                info.setOrganFullId(organizationDtoByCode.getFullId());
            }
        }
        BeanUtils.copyProperties(info, accountsAllSearchInfo);
//        accountsAllSearchInfo.setCertainOrganFullId(info.getOrganFullId());
        //存量数据查询
        List<String> string003s = new ArrayList<>();
        if(org.apache.commons.lang.StringUtils.equals(accountsAllSearchInfo.getString003(),"1")){//存量数据
            string003s.add("1");
            accountsAllSearchInfo.setString003s(string003s);
        }else if(org.apache.commons.lang.StringUtils.equals(accountsAllSearchInfo.getString003(),"0")){//增量
            string003s.add("");
            string003s.add("0");
            string003s.add(null);
            accountsAllSearchInfo.setString003s(string003s);
        }

        if(!"accountStatistics".equals(info.getString005())) {  //排除账户信息统计详情页白名单判断
            //白名单增加查询条件
            List<String> whiteLists = new ArrayList<String>();
            if(StringUtils.isEmpty(info.getWhiteList()) || "0".equals(info.getWhiteList())){
                whiteLists.add("0");
                whiteLists.add("");
                whiteLists.add(null);
            }else{
                whiteLists.add("1");
            }

            accountsAllSearchInfo.setWhiteLists(whiteLists);
        }

        List<AccountsAllInfo> accountsAllInfos = ConverterService.convertToList(accountsAllDao.findAll(new AccountsAllSpec(accountsAllSearchInfo)), AccountsAllInfo.class);

        for(AccountsAllInfo accountsAllInfo : accountsAllInfos){
            CompanyAccountPoi companyAccountPoi = new CompanyAccountPoi();
            BeanUtils.copyProperties(accountsAllInfo,companyAccountPoi);
            companyAccountPoi.setAcctType(accountsAllInfo.getAcctType().getValue());
            companyAccountPoi.setAccountStatus(accountsAllInfo.getAccountStatus().getFullName());
            companyAccountPoi.setAccountSiteType(accountsAllInfo.getOpenAccountSiteType() == null ? "" : accountsAllInfo.getOpenAccountSiteType().getValue());
            companyAccountPois.add(companyAccountPoi);
        }

        iExcelExport.setPoiList(companyAccountPois);
        return iExcelExport;
    }

    /**
     *
     * 取消yd_public_accountallinfo_v
     * 已经废弃
     * 使用query(AllAccountPublicDTO info, Pageable pageable)方法
     * @param info
     * @param pageable
     * @return
     */
    @Override
    @Deprecated
    public TableResultResponse<AllAccountPublicDTO> queryLastBills(AllAccountPublicDTO info, Pageable pageable) {
        Page<AllAccountPublicDTO> page = null;

        String[] lastestBillsFields = {"acctNo", "acctName", "acctType", "accountStatus", "bankCode", "pbcSyncStatus", "eccsSyncStatus", "pbcSyncTime", "eccsSyncTime", "pbcCheckDate", "billType"};
        boolean[] isLikes = {true, true, false, true, true, true, true, true, true, true, true};
        page = allAccountPublicDao.findPage(makeQuerySQL(info, lastestBillsFields, isLikes).toSQLString(), null,
                pageable);

        return new TableResultResponse<AllAccountPublicDTO>((int)page.getTotalElements(), page.getContent());
    }

    /**
     * 取消yd_public_accountallinfo_v
     * 已经废弃
     * @param info
     * @param fields
     * @param isLikes
     * @return
     */
    @Deprecated
    private SqlBuilder makeQuerySQL(AllAccountPublicDTO info, String[] fields, boolean[] isLikes) {
        SqlBuilder sqlBuilder = new SqlBuilder();
        List<String> statuses = new ArrayList<>();
        List<String> pbcSyncStatuses = null;

        info.setOrganFullId(SecurityUtils.getCurrentOrgFullId());

        sqlBuilder.select("*").from("YD_PUBLIC_ACCOUNTALLINFO_V");

        sqlBuilder.startsWith("yd_organ_full_id", BeanValueUtils.getValue(info, "organFullId"));

        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            String tableField = IdeaNamingStrategy.PREFIX + field;

            if("yd_acctName".equals(tableField)) {
                tableField = "yd_acct_name";
            }
            if("yd_acctNo".equals(tableField)) {
                tableField = "yd_acct_no";
            }
            if("yd_acctType".equals(tableField)) {
                tableField = "yd_acct_type";
            }

            boolean isLike = isLikes[i];
            if (isLike) {
                sqlBuilder.likeByCondition(tableField, BeanValueUtils.getValue(info, field));
            } else {
                sqlBuilder.eq(tableField, BeanValueUtils.getValue(info, field));
            }
        }

        sqlBuilder.orderBy("yd_last_update_date desc");

        return sqlBuilder;
    }

    public String changeRegOffice(String regOffice) {
        if ("G".equals(regOffice)) {
            return "工商部门";
        } else if ("R".equals(regOffice)) {
            return "人民银行";
        } else if ("M".equals(regOffice)) {
            return "民政部门";
        } else if ("B".equals(regOffice)) {
            return "机构编制部门";
        } else if ("S".equals(regOffice)) {
            return "司法行政部门";
        } else if ("W".equals(regOffice)) {
            return "外交部门";
        } else if ("Z".equals(regOffice)) {
            return "宗教部门";
        } else if ("Q".equals(regOffice)) {
            return "其他";
        } else {
            return "";
        }
    }

}
