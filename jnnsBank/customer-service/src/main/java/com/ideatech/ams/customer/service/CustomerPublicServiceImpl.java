package com.ideatech.ams.customer.service;

import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.dao.HeGuiYuJingAllDao;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.entity.HeGuiYuJingAll;
import com.ideatech.ams.customer.dao.CustomerPublicDao;
import com.ideatech.ams.customer.dto.CustomerAllResponse;
import com.ideatech.ams.customer.dto.CustomerPublicInfo;
import com.ideatech.ams.customer.dto.CustomersAllInfo;
import com.ideatech.ams.customer.entity.CustomerPublic;
import com.ideatech.ams.customer.event.AccountsAllEvent;
import com.ideatech.ams.customer.poi.FileDueExport;
import com.ideatech.ams.customer.poi.FileDuePoi;
import com.ideatech.ams.customer.poi.LegalDueExport;
import com.ideatech.ams.customer.poi.LegalDuePoi;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtil;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author vantoo
 * @date 15:49 2018/5/25
 */
@Service
@Transactional
@Slf4j
public class CustomerPublicServiceImpl implements CustomerPublicService {

    @Autowired
    private CustomerPublicDao customerPublicDao;

    @Autowired
    private CustomersAllService customersAllService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

   @Autowired
   private AccountsAllDao accountsAllDao;

    @PersistenceContext
    private EntityManager entityManager; //注入EntityManager




    @Override
    public CustomerPublicInfo getOne(Long id) {
        return po2dto(customerPublicDao.findOne(id));
    }

    @Override
    public void save(CustomerPublicInfo customerPublicInfo) {
        CustomerPublic customerPublic = null;
        if (customerPublicInfo.getId() != null) {
            customerPublic = customerPublicDao.findOne(customerPublicInfo.getId());
        }
        if (customerPublic == null) {
            customerPublic = new CustomerPublic();
        }
        BeanCopierUtils.copyProperties(customerPublicInfo, customerPublic);
        customerPublic = customerPublicDao.save(customerPublic);
        customerPublicInfo.setId(customerPublic.getId());
    }

    @Override
    public CustomerPublicInfo getByCustomerNo(String customerNo) {
        CustomersAllInfo customersAllInfo = customersAllService.findByCustomerNo(customerNo);
        if (customersAllInfo != null) {
            return po2dto(customerPublicDao.findByCustomerId(customersAllInfo.getId()));
        } else {
            return null;
        }
    }

    @Override
    public CustomerPublicInfo getByCustomerId(Long customerId) {
        return po2dto(customerPublicDao.findByCustomerId(customerId));
    }

    private CustomerPublicInfo po2dto(CustomerPublic customerPublic) {
        if (customerPublic != null) {
            CustomerPublicInfo customerPublicInfo = new CustomerPublicInfo();
            BeanCopierUtils.copyProperties(customerPublic, customerPublicInfo);
            return customerPublicInfo;
        } else {
            return null;
        }
    }

	@Override
	public CustomerPublicInfo getByDepositorName(String depositorName) {
		CustomersAllInfo customersAllInfo = customersAllService.findByDepositorName(depositorName);
		if(customersAllInfo != null) {
			return po2dto(customerPublicDao.findByCustomerId(customersAllInfo.getId()));
		}else {
			return null;
		}
	}

    @Override
    public Map<Long, CustomerPublicInfo> findAllInMap() {
        List<CustomerPublic> all = customerPublicDao.findAll();
        Map<Long, CustomerPublicInfo> map = new HashMap<>();
        for(CustomerPublic customerPublic : all){
            CustomerPublicInfo customerPublicInfo = po2dto(customerPublic);
            if(customerPublicInfo !=null){
                map.put(customerPublicInfo.getCustomerId(),customerPublicInfo);
            }
        }
        return map;
    }

    /**
     * 证明文件到期日数量统计
     * @param afterDateStr
     * @param beforeDateStr
     * @param organFullId
     * @param fileOverConfigEnabled
     * @return
     */
    @Override
    public Long countFileDueBefore(final String afterDateStr, final String beforeDateStr, final String organFullId, final Boolean fileOverConfigEnabled) {
        final String nowDateStr = DateUtils.DateToStr(new Date(), "");

        Specification<CustomerPublic> specification = new Specification<CustomerPublic>() {
            @Override
            public Predicate toPredicate(Root<CustomerPublic> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                //过期逻辑判断
                expressions.add(cb.like(root.<String>get("organFullId"), organFullId + "%"));
                Predicate greaterThanFileDue = cb.greaterThanOrEqualTo(root.<String>get("fileDue"), nowDateStr);
                Predicate lessThanFileDue = cb.lessThanOrEqualTo(root.<String>get("fileDue"), afterDateStr);
                Predicate greaterThanFileDue2 = cb.greaterThanOrEqualTo(root.<String>get("fileDue2"), nowDateStr);
                Predicate lessThanFileDue2 = cb.lessThanOrEqualTo(root.<String>get("fileDue2"), afterDateStr);

                //超期逻辑判断
                Predicate fileDuePred = cb.and(greaterThanFileDue, lessThanFileDue);
                Predicate fileDue2Pred = cb.and(greaterThanFileDue2, lessThanFileDue2);

                if(!fileOverConfigEnabled) {  //关闭超期提醒
                    expressions.add(cb.or(fileDuePred, fileDue2Pred));
                    expressions.add(cb.equal(root.<Boolean>get("isFileDueOver"), false));
                } else {
                    Predicate overFileDue = cb.between(root.<String>get("fileDue"), "1900-01-01", beforeDateStr);
                    Predicate overFileDue2 = cb.between(root.<String>get("fileDue2"), "1900-01-01", beforeDateStr);
                    expressions.add(cb.or(fileDuePred, fileDue2Pred, overFileDue, overFileDue2));
                }

                return predicate;
            }
        };

        return customerPublicDao.count(specification);
    }

    /**
     * 法人证件到期日提醒
     * @param afterDateStr
     * @param beforeDateStr
     * @param organFullId
     * @param legalOverConfigEnabled
     * @return
     */
    @Override
    public Long countLegalIdcardDueBefore(final String afterDateStr, final String beforeDateStr, final String organFullId, final Boolean legalOverConfigEnabled) {
        final String nowDateStr = DateUtils.DateToStr(new Date(), "");

        Specification<CustomerPublic> specification = new Specification<CustomerPublic>() {
            @Override
            public Predicate toPredicate(Root<CustomerPublic> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                //过期逻辑判断
                expressions.add(cb.like(root.<String>get("organFullId"), organFullId + "%"));
                Predicate greaterThanFileDue = cb.greaterThanOrEqualTo(root.<String>get("legalIdcardDue"), nowDateStr);
                Predicate lessThanFileDue = cb.lessThanOrEqualTo(root.<String>get("legalIdcardDue"), afterDateStr);

                Predicate legalIdcardDuePred = cb.and(greaterThanFileDue, lessThanFileDue);

                if(!legalOverConfigEnabled) { //关闭超期提醒
                    expressions.add(cb.or(legalIdcardDuePred));
                    expressions.add(cb.equal(root.<Boolean>get("isLegalIdcardDueOver"), false));
                } else {
                    //超期逻辑判断
                    Predicate overLegalIdcardDue = cb.lessThanOrEqualTo(root.<String>get("legalIdcardDue"), beforeDateStr);
                    Predicate minOverLegalIdcardDue = cb.greaterThanOrEqualTo(root.<String>get("legalIdcardDue"), "1900-01-01");
                    Predicate overLegalIdcardDuePred = cb.and(overLegalIdcardDue, minOverLegalIdcardDue);

                    expressions.add(cb.or(legalIdcardDuePred, overLegalIdcardDuePred));
                }

                return predicate;
            }
        };

        return customerPublicDao.count(specification);
    }

    /**
     * 通知提醒中的到期提醒分页
     * @param type
     * @param customerPublicInfo
     * @param afterDateStr  到期提醒日
     * @param beforeDateStr 超期提醒日
     * @param organFullId
     * @param pagingDto
     * @return
     */
    @Autowired
    private HeGuiYuJingAllDao heGuiYuJingAllDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Override
    public PagingDto<CustomerAllResponse> listCustDueBefore(String type, CustomerPublicInfo customerPublicInfo, final String afterDateStr, final String beforeDateStr, String organFullId, PagingDto pagingDto) throws Exception {
        final String nowDateStr = DateUtils.DateToStr(new Date(), "");
        Page<CustomerPublic> page;
        Page<CustomerPublic> page1;
        Pageable pageable = new PageRequest(Math.max(pagingDto.getOffset(), 0), pagingDto.getLimit());
        Pageable pageable1 = new PageRequest(Math.max(pagingDto.getOffset(), 0), 10000);
        customerPublicInfo.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        final String currentOrgFullId = SecurityUtils.getCurrentOrgFullId();
        final String depositorName = customerPublicInfo.getDepositorName();
        final String fileNo = customerPublicInfo.getFileNo();
        final String fileDue = customerPublicInfo.getFileDue();
        final String fileDue2 = customerPublicInfo.getFileDue2();
        final String legalName = customerPublicInfo.getLegalName();
        final String legalIdcardDue = customerPublicInfo.getLegalIdcardDue();
        final Boolean isFileDueOver = customerPublicInfo.getIsFileDueOver();
        final Boolean isLegalIdcardDueOver = customerPublicInfo.getIsLegalIdcardDueOver();
        final String acctNo = customerPublicInfo.getAcctNo();
        final String openBank = customerPublicInfo.getOpenBank();
        final String checkStatus = customerPublicInfo.getCheckStatus();

        Date beforeDate = DateUtils.parse(beforeDateStr, "yyyy-MM-dd");

        if("legalDueNotice".equals(type)) {
            Specification<CustomerPublic> specification = new Specification<CustomerPublic>() {
                @Override
                public Predicate toPredicate(Root<CustomerPublic> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                    Predicate predicate = cb.conjunction();
                    List<Expression<Boolean>> expressions = predicate.getExpressions();

                    //过期逻辑判断
                    expressions.add(cb.like(root.<String>get("organFullId"), currentOrgFullId + "%"));
                    Predicate greaterThanFileDue = cb.greaterThanOrEqualTo(root.<String>get("legalIdcardDue"), nowDateStr);
                    Predicate lessThanFileDue = cb.lessThanOrEqualTo(root.<String>get("legalIdcardDue"), afterDateStr);

                    //超期逻辑判断
                    Predicate overLegalIdcardDue = cb.lessThanOrEqualTo(root.<String>get("legalIdcardDue"), beforeDateStr);
                    Predicate minOverLegalIdcardDue = cb.greaterThanOrEqualTo(root.<String>get("legalIdcardDue"), "1900-01-01");

                    Predicate legalIdcardDuePred = cb.and(greaterThanFileDue, lessThanFileDue);
                    Predicate overLegalIdcardDuePred = cb.and(overLegalIdcardDue, minOverLegalIdcardDue);

                    expressions.add(cb.or(legalIdcardDuePred, overLegalIdcardDuePred));

                    if(StringUtils.isNotBlank(depositorName)) {
                        expressions.add(cb.and(cb.like(root.<String>get("depositorName"), "%" + depositorName + "%")));
                    }
                    if(StringUtils.isNotBlank(acctNo)) {
                        expressions.add(cb.and(cb.like(root.<String>get("acctNo"), "%" + acctNo + "%")));
                    }
                    if(StringUtils.isNotBlank(openBank)) {
                        expressions.add(cb.and(cb.like(root.<String>get("openBank"), "%" + openBank + "%")));
                    }
                    if(StringUtils.isNotBlank(legalName)) {
                        expressions.add(cb.and(cb.equal(root.<String>get("legalName"), legalName)));
                    }
                    if(StringUtils.isNotBlank(legalIdcardDue)) {
                        expressions.add(cb.and(cb.equal(root.<String>get("legalIdcardDue"), legalIdcardDue)));
                    }

                    if(isLegalIdcardDueOver != null) {
                        expressions.add(cb.and(cb.equal(root.<String>get("isLegalIdcardDueOver"), isLegalIdcardDueOver)));
                    }

                    if(StringUtils.isNotBlank(checkStatus) && !"选择".equals(checkStatus)){
                        expressions.add(cb.and(cb.equal(root.<String>get("checkStatus"), checkStatus)));
                    }
                    if(StringUtils.isBlank(checkStatus)){
                        expressions.add(cb.and(cb.isNull(root.get("checkStatus"))));
                    }

                    return predicate;
                }
            };

            page = customerPublicDao.findAll(specification, pageable);
            page1 = customerPublicDao.findAll(specification, pageable1);
        } else {
            Specification<CustomerPublic> specification = new Specification<CustomerPublic>() {
                @Override
                public Predicate toPredicate(Root<CustomerPublic> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                    Predicate predicate = cb.conjunction();
                    List<Expression<Boolean>> expressions = predicate.getExpressions();

                    //过期逻辑判断
                    expressions.add(cb.like(root.<String>get("organFullId"), currentOrgFullId + "%"));
                    Predicate greaterThanFileDue = cb.greaterThanOrEqualTo(root.<String>get("fileDue"), nowDateStr);
                    Predicate lessThanFileDue = cb.lessThanOrEqualTo(root.<String>get("fileDue"), afterDateStr);
                    Predicate greaterThanFileDue2 = cb.greaterThanOrEqualTo(root.<String>get("fileDue2"), nowDateStr);
                    Predicate lessThanFileDue2 = cb.lessThanOrEqualTo(root.<String>get("fileDue2"), afterDateStr);

                    //超期逻辑判断
                    Predicate overFileDue = cb.between(root.<String>get("fileDue"), "1900-01-01", beforeDateStr);
                    Predicate overFileDue2 = cb.between(root.<String>get("fileDue2"), "1900-01-01", beforeDateStr);

                    Predicate fileDuePred = cb.and(greaterThanFileDue, lessThanFileDue);
                    Predicate fileDue2Pred = cb.and(greaterThanFileDue2, lessThanFileDue2);

                    expressions.add(cb.or(fileDuePred, fileDue2Pred, overFileDue, overFileDue2));

                    if(StringUtils.isNotBlank(depositorName)) {
                        expressions.add(cb.and(cb.like(root.<String>get("depositorName"), "%" + depositorName + "%")));
                    }
                    if(StringUtils.isNotBlank(acctNo)) {
                        expressions.add(cb.and(cb.like(root.<String>get("acctNo"), "%" + acctNo + "%")));
                    }
                    if(StringUtils.isNotBlank(openBank)) {
                        expressions.add(cb.and(cb.like(root.<String>get("openBank"), "%" + openBank + "%")));
                    }
                    if(StringUtils.isNotBlank(fileDue)) {
                        expressions.add(cb.and(cb.equal(root.<String>get("fileDue"), fileDue)));

                    }
                    if(StringUtils.isNotBlank(fileDue2)) {
                        expressions.add(cb.and(cb.equal(root.<String>get("fileDue2"), fileDue2)));
                    }

                    if(isFileDueOver != null) {
                        expressions.add(cb.and(cb.equal(root.<String>get("isFileDueOver"), isFileDueOver)));
                    }
                    if(StringUtils.isNotBlank(fileNo)) {
                        expressions.add(cb.and(cb.like(root.<String>get("fileNo"), "%" + fileNo + "%")));
                    }
                    if(StringUtils.isNotBlank(checkStatus) && !"选择".equals(checkStatus)){
                        expressions.add(cb.and(cb.equal(root.<String>get("checkStatus"), checkStatus)));
                    }
                    if(StringUtils.isBlank(checkStatus)){
                        expressions.add(cb.and(cb.isNull(root.get("checkStatus"))));
                    }

                    return predicate;
                }
            };

            page = customerPublicDao.findAll(specification, pageable);
            page1 = customerPublicDao.findAll(specification, pageable1);

        }

        List<CustomerPublicInfo> dtos = ConverterService.convertToList(page.getContent(), CustomerPublicInfo.class);
        List<CustomerPublicInfo> dtos1 = ConverterService.convertToList(page1.getContent(), CustomerPublicInfo.class);



        List<CustomerAllResponse> customerAllResponseList = ConverterService.convertToList(dtos, CustomerAllResponse.class);
        List<CustomerAllResponse> customerAllResponseList1 = ConverterService.convertToList(dtos1, CustomerAllResponse.class);
        Long fileOverNoticeDay = configService.findOneByKey("fileOverNoticeDay");
        Long legalOverNoticeDay = configService.findOneByKey("legalOverNoticeDay");
        if(fileOverNoticeDay == null) {
            fileOverNoticeDay = 3L;
        }
        if(legalOverNoticeDay == null) {
            legalOverNoticeDay = 3L;
        }

        for(CustomerAllResponse customerAllResponse : customerAllResponseList) {

            CustomersAllInfo customersAllInfo = customersAllService.findOne(customerAllResponse.getId());
            if(customersAllInfo != null) {
               OrganizationDto organizationDto = organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId());
               //通过企业名称查询账户表对象
                List<AccountsAll> list = accountsAllDao.findByAcctName(customerAllResponse.getDepositorName());

                customerAllResponse.setOrganName(organizationDto.getName());
                customerAllResponse.setCustomerNo(customersAllInfo.getCustomerNo());
                customerAllResponse.setCreatedDate(DateUtils.DateToStr(customersAllInfo.getCreatedDate(), ""));

                //是否超期字段设置
                if ("fileDueNotice".equals(type)) {  //证明文件到期提醒列表
                    CustomersAllInfo customersInfo = customersAllService.findOne(customerAllResponse.getId());
                    AccountsAllEvent accountsAllEvent = new AccountsAllEvent(customerAllResponse);
                    accountsAllEvent.setCustomerLogId(customersInfo.getRefCustomerLogId());
                    accountsAllEvent.setCustomerAllResponse(customerAllResponse);
                    applicationEventPublisher.publishEvent(accountsAllEvent);

                    String fileType = customerAllResponse.getFileType();
                    String acctTypeStr = customerAllResponse.getString001();

                    setFileType(customerAllResponse, fileType, acctTypeStr);
                } else if ("legalDueNotice".equals(type)) {
                    if (StringUtils.isNotBlank(customerAllResponse.getLegalIdcardType())) {
                        String legalIdcardTypeName = dictionaryService.transalte("legalIdcardTypeValue2Item", customerAllResponse.getLegalIdcardType());
                        customerAllResponse.setLegalIdcardType(legalIdcardTypeName);
                    }

                }
            }

            if(customerAllResponse.getCheckStatus()==null || customerAllResponse.getCheckStatus().equals("")){
                customerAllResponse.setCheckStatus("未核实");
            }else{
                customerAllResponse.setCheckStatus("已核实");
            }

            if(customerAllResponse.getFileDue() != null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date bt =sdf.parse(customerAllResponse.getFileDue());
                String date =  sdf.format(new Date());
                Date ft =sdf.parse(date);
                if(bt.after(ft) || bt.compareTo(ft) == 0){
                    customerAllResponse.setIsFileDueOver(false);
                    customerPublicDao.update_is_file_due_over(customerAllResponse.getId(),customerAllResponse.getIsFileDueOver());
                }else {
                    customerAllResponse.setIsFileDueOver(true);
                    customerPublicDao.update_is_file_due_over(customerAllResponse.getId(),customerAllResponse.getIsFileDueOver());
                }
            }
            if(customerAllResponse.getLegalIdcardDue() != null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date bt =sdf.parse(customerAllResponse.getLegalIdcardDue());
                String date =  sdf.format(new Date());
                Date ft =sdf.parse(date);
                if(bt.after(ft) || bt.compareTo(ft) == 0){
                    customerAllResponse.setIsLegalIdcardDueOver(false);
                    customerPublicDao.update_is_legal_idcard_due_over(customerAllResponse.getId(),customerAllResponse.getIsLegalIdcardDueOver());
                }else {
                    customerAllResponse.setIsLegalIdcardDueOver(true);
                    customerPublicDao.update_is_legal_idcard_due_over(customerAllResponse.getId(),customerAllResponse.getIsLegalIdcardDueOver());
                }
            }
        }
        pagingDto.setList(customerAllResponseList);
        pagingDto.setTotalRecord(page.getTotalElements());
        pagingDto.setTotalPages(page.getTotalPages());
        return pagingDto;
    }
    public PagingDto<CustomerAllResponse> listCustDueBefore1(String type, CustomerPublicInfo customerPublicInfo, final String afterDateStr, final String beforeDateStr, String organFullId, PagingDto pagingDto) throws Exception {
        final String nowDateStr = DateUtils.DateToStr(new Date(), "");
        Page<CustomerPublic> page;
        Page<CustomerPublic> page1;
        Pageable pageable = new PageRequest(Math.max(pagingDto.getOffset(), 0), pagingDto.getLimit());
        Pageable pageable1 = new PageRequest(0, 10000);
        customerPublicInfo.setOrganFullId("1");
        customerPublicInfo.setFileDue("");
        customerPublicInfo.setFileNo("");
        customerPublicInfo.setLegalName("");
        customerPublicInfo.setLegalIdcardDue("");
        customerPublicInfo.setDepositorName("");
        customerPublicInfo.setCheckStatus("选择");
        final String currentOrgFullId ="1";
        final String depositorName = customerPublicInfo.getDepositorName();
        final String fileNo = customerPublicInfo.getFileNo();
        final String fileDue = customerPublicInfo.getFileDue();
        final String fileDue2 = customerPublicInfo.getFileDue2();
        final String legalName = customerPublicInfo.getLegalName();
        final String legalIdcardDue = customerPublicInfo.getLegalIdcardDue();
        final Boolean isFileDueOver = customerPublicInfo.getIsFileDueOver();
        final Boolean isLegalIdcardDueOver = customerPublicInfo.getIsLegalIdcardDueOver();
        final String acctNo = customerPublicInfo.getAcctNo();
        final String openBank = customerPublicInfo.getOpenBank();
        final String checkStatus = customerPublicInfo.getCheckStatus();
        Date beforeDate = DateUtils.parse(beforeDateStr, "yyyy-MM-dd");
        if("legalDueNotice".equals(type)) {
            Specification<CustomerPublic> specification = new Specification<CustomerPublic>() {
                @Override
                public Predicate toPredicate(Root<CustomerPublic> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                    Predicate predicate = cb.conjunction();
                    List<Expression<Boolean>> expressions = predicate.getExpressions();
                    expressions.add(cb.like(root.<String>get("organFullId"), currentOrgFullId + "%"));
                    Predicate greaterThanFileDue = cb.greaterThanOrEqualTo(root.<String>get("legalIdcardDue"), nowDateStr);
                    Predicate lessThanFileDue = cb.lessThanOrEqualTo(root.<String>get("legalIdcardDue"), afterDateStr);
                    Predicate overLegalIdcardDue = cb.lessThanOrEqualTo(root.<String>get("legalIdcardDue"), beforeDateStr);
                    Predicate minOverLegalIdcardDue = cb.greaterThanOrEqualTo(root.<String>get("legalIdcardDue"), "1900-01-01");
                    Predicate legalIdcardDuePred = cb.and(greaterThanFileDue, lessThanFileDue);
                    Predicate overLegalIdcardDuePred = cb.and(overLegalIdcardDue, minOverLegalIdcardDue);
                    expressions.add(cb.or(legalIdcardDuePred, overLegalIdcardDuePred));
                    if(StringUtils.isNotBlank(depositorName)) {
                        expressions.add(cb.and(cb.like(root.<String>get("depositorName"), "%" + depositorName + "%")));
                    }
                    if(StringUtils.isNotBlank(acctNo)) {
                        expressions.add(cb.and(cb.like(root.<String>get("acctNo"), "%" + acctNo + "%")));
                    }
                    if(StringUtils.isNotBlank(openBank)) {
                        expressions.add(cb.and(cb.like(root.<String>get("openBank"), "%" + openBank + "%")));
                    }
                    if(StringUtils.isNotBlank(legalName)) {
                        expressions.add(cb.and(cb.equal(root.<String>get("legalName"), legalName)));
                    }
                    if(StringUtils.isNotBlank(legalIdcardDue)) {
                        expressions.add(cb.and(cb.equal(root.<String>get("legalIdcardDue"), legalIdcardDue)));
                    }
                    if(isLegalIdcardDueOver != null) {
                        expressions.add(cb.and(cb.equal(root.<String>get("isLegalIdcardDueOver"), isLegalIdcardDueOver)));
                    }
                    if(StringUtils.isNotBlank(checkStatus) && !"选择".equals(checkStatus)){
                        expressions.add(cb.and(cb.equal(root.<String>get("checkStatus"), checkStatus)));
                    }
                    if(StringUtils.isBlank(checkStatus)){
                        expressions.add(cb.and(cb.isNull(root.get("checkStatus"))));
                    }
                    return predicate;
                }
            };
            page = customerPublicDao.findAll(specification, pageable);
            page1 = customerPublicDao.findAll(specification, pageable1);
        } else {
            Specification<CustomerPublic> specification = new Specification<CustomerPublic>() {
                @Override
                public Predicate toPredicate(Root<CustomerPublic> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                    Predicate predicate = cb.conjunction();
                    List<Expression<Boolean>> expressions = predicate.getExpressions();
                    expressions.add(cb.like(root.<String>get("organFullId"), currentOrgFullId + "%"));
                    Predicate greaterThanFileDue = cb.greaterThanOrEqualTo(root.<String>get("fileDue"), nowDateStr);
                    Predicate lessThanFileDue = cb.lessThanOrEqualTo(root.<String>get("fileDue"), afterDateStr);
                    Predicate greaterThanFileDue2 = cb.greaterThanOrEqualTo(root.<String>get("fileDue2"), nowDateStr);
                    Predicate lessThanFileDue2 = cb.lessThanOrEqualTo(root.<String>get("fileDue2"), afterDateStr);
                    Predicate overFileDue = cb.between(root.<String>get("fileDue"), "1900-01-01", beforeDateStr);
                    Predicate overFileDue2 = cb.between(root.<String>get("fileDue2"), "1900-01-01", beforeDateStr);
                    Predicate fileDuePred = cb.and(greaterThanFileDue, lessThanFileDue);
                    Predicate fileDue2Pred = cb.and(greaterThanFileDue2, lessThanFileDue2);
                    expressions.add(cb.or(fileDuePred, fileDue2Pred, overFileDue, overFileDue2));
                    if(StringUtils.isNotBlank(depositorName)) {
                        expressions.add(cb.and(cb.like(root.<String>get("depositorName"), "%" + depositorName + "%")));
                    }
                    if(StringUtils.isNotBlank(acctNo)) {
                        expressions.add(cb.and(cb.like(root.<String>get("acctNo"), "%" + acctNo + "%")));
                    }
                    if(StringUtils.isNotBlank(openBank)) {
                        expressions.add(cb.and(cb.like(root.<String>get("openBank"), "%" + openBank + "%")));
                    }
                    if(StringUtils.isNotBlank(fileDue)) {
                        expressions.add(cb.and(cb.equal(root.<String>get("fileDue"), fileDue)));
                    }
                    if(StringUtils.isNotBlank(fileDue2)) {
                        expressions.add(cb.and(cb.equal(root.<String>get("fileDue2"), fileDue2)));
                    }
                    if(isFileDueOver != null) {
                        expressions.add(cb.and(cb.equal(root.<String>get("isFileDueOver"), isFileDueOver)));
                    }
                    if(StringUtils.isNotBlank(fileNo)) {
                        expressions.add(cb.and(cb.like(root.<String>get("fileNo"), "%" + fileNo + "%")));
                    }
                    if(StringUtils.isNotBlank(checkStatus) && !"选择".equals(checkStatus)){
                        expressions.add(cb.and(cb.equal(root.<String>get("checkStatus"), checkStatus)));
                    }
                    if(StringUtils.isBlank(checkStatus)){
                        expressions.add(cb.and(cb.isNull(root.get("checkStatus"))));
                    }
                    return predicate;
                }
            };
            page = customerPublicDao.findAll(specification, pageable);
            page1 = customerPublicDao.findAll(specification, pageable1);
        }
        List<CustomerPublicInfo> dtos = ConverterService.convertToList(page.getContent(), CustomerPublicInfo.class);
        List<CustomerPublicInfo> dtos1 = ConverterService.convertToList(page1.getContent(), CustomerPublicInfo.class);
        List<CustomerAllResponse> customerAllResponseList = ConverterService.convertToList(dtos, CustomerAllResponse.class);
        List<CustomerAllResponse> customerAllResponseList1 = ConverterService.convertToList(dtos1, CustomerAllResponse.class);
        Long fileOverNoticeDay = configService.findOneByKey("fileOverNoticeDay");
        Long legalOverNoticeDay = configService.findOneByKey("legalOverNoticeDay");
        if(fileOverNoticeDay == null) {
            fileOverNoticeDay = 3L;
        }
        if(legalOverNoticeDay == null) {
            legalOverNoticeDay = 3L;
        }
        for(CustomerAllResponse customerAllResponse : customerAllResponseList) {
            CustomersAllInfo customersAllInfo = customersAllService.findOne(customerAllResponse.getId());

            if(customersAllInfo != null) {
                OrganizationDto organizationDto = organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId());
                List<AccountsAll> list = accountsAllDao.findByAcctName(customerAllResponse.getDepositorName());
                customerAllResponse.setOrganName(organizationDto.getName());
                customerAllResponse.setCustomerNo(customersAllInfo.getCustomerNo());
                customerAllResponse.setCreatedDate(DateUtils.DateToStr(customersAllInfo.getCreatedDate(), ""));
                if ("fileDueNotice".equals(type)) {  //证明文件到期提醒列表
                    CustomersAllInfo customersInfo = customersAllService.findOne(customerAllResponse.getId());
                    AccountsAllEvent accountsAllEvent = new AccountsAllEvent(customerAllResponse);
                    accountsAllEvent.setCustomerLogId(customersInfo.getRefCustomerLogId());
                    accountsAllEvent.setCustomerAllResponse(customerAllResponse);
                    applicationEventPublisher.publishEvent(accountsAllEvent);
                    String fileType = customerAllResponse.getFileType();
                    String acctTypeStr = customerAllResponse.getString001();
                    setFileType(customerAllResponse, fileType, acctTypeStr);
                } else if ("legalDueNotice".equals(type)) {
                    if (StringUtils.isNotBlank(customerAllResponse.getLegalIdcardType())) {
                        String legalIdcardTypeName = dictionaryService.transalte("legalIdcardTypeValue2Item", customerAllResponse.getLegalIdcardType());
                        customerAllResponse.setLegalIdcardType(legalIdcardTypeName);
                    }
                }
            }
            if(customerAllResponse.getCheckStatus()==null || customerAllResponse.getCheckStatus().equals("")){
                customerAllResponse.setCheckStatus("未核实");
            }else{
                customerAllResponse.setCheckStatus("已核实");
            }
            if(customerAllResponse.getFileDue() != null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date bt =sdf.parse(customerAllResponse.getFileDue());
                String date =  sdf.format(new Date());
                Date ft =sdf.parse(date);
                if(bt.after(ft) || bt.compareTo(ft) == 0){
                    customerAllResponse.setIsFileDueOver(false);
                    customerPublicDao.update_is_file_due_over(customerAllResponse.getId(),customerAllResponse.getIsFileDueOver());
                }else {
                    customerAllResponse.setIsFileDueOver(true);
                    customerPublicDao.update_is_file_due_over(customerAllResponse.getId(),customerAllResponse.getIsFileDueOver());
                }
            }



            if(customerAllResponse.getLegalIdcardDue() != null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date bt =sdf.parse(customerAllResponse.getLegalIdcardDue());
                String date =  sdf.format(new Date());
                Date ft =sdf.parse(date);
                if(bt.after(ft) || bt.compareTo(ft) == 0){
                    customerAllResponse.setIsLegalIdcardDueOver(false);
                    customerPublicDao.update_is_legal_idcard_due_over(customerAllResponse.getId(),customerAllResponse.getIsLegalIdcardDueOver());
                }else {
                    customerAllResponse.setIsLegalIdcardDueOver(true);
                    customerPublicDao.update_is_legal_idcard_due_over(customerAllResponse.getId(),customerAllResponse.getIsLegalIdcardDueOver());
                }
            }

            if("legalDueNotice".equals(type)){
                heGuiYuJingAllDao.deleteAllByYuJingType("2");
                log.info("保存全量预警数据法人到期开始-----");
                if(customerAllResponseList1!=null && customerAllResponseList1.size() > 0){

                    for(CustomerAllResponse    dto : customerAllResponseList1){
                        HeGuiYuJingAll heGuiYuJingAll=new HeGuiYuJingAll();
                        if(StringUtils.isNotBlank(dto.getDepositorName())){
                            heGuiYuJingAll.setDepositorName(dto.getDepositorName());
                        }
                        if(StringUtils.isNotBlank(dto.getLegalName())){
                            heGuiYuJingAll.setLegalName(dto.getLegalName());
                        }
                        if(StringUtils.isNotBlank(dto.getLegalIdcardNo())){
                            heGuiYuJingAll.setLegalIdcardNo(dto.getLegalIdcardNo());
                        }
                        if(StringUtils.isNotBlank(dto.getLegalIdcardType())){
                            heGuiYuJingAll.setLegalIdcardType(dto.getLegalIdcardType());
                        }
                        if(StringUtils.isNotBlank(dto.getLegalIdcardDue())){
                            heGuiYuJingAll.setLegalIdcardDue(dto.getLegalIdcardDue());
                        }
                        if(StringUtils.isNotBlank(dto.getCheckStatus())){
                            heGuiYuJingAll.setCheckStatus(dto.getCheckStatus());
                        }
                        heGuiYuJingAll.setIsLegalIdcardDueOver(dto.getIsLegalIdcardDueOver());
                        heGuiYuJingAll.setYuJingType("2");
                        heGuiYuJingAllDao.save(heGuiYuJingAll);
                    }
                    log.info("保存全量预警数据法人到期-----");
                }
            }else {
                heGuiYuJingAllDao.deleteAllByYuJingType("3");
                log.info("证明文件保存开始---");
                if(customerAllResponseList1!=null && customerAllResponseList1.size() > 0){
                    for(CustomerAllResponse    dto : customerAllResponseList1){
                        HeGuiYuJingAll heGuiYuJingAll=new HeGuiYuJingAll();
                        if(StringUtils.isNotBlank(dto.getDepositorName())){
                            heGuiYuJingAll.setDepositorName(dto.getDepositorName());
                        }
                        if(StringUtils.isNotBlank(dto.getFileDue())){
                            heGuiYuJingAll.setFileDue(dto.getFileDue());
                        }
                        if(StringUtils.isNotBlank(dto.getFileNo())){
                            heGuiYuJingAll.setFileNo(dto.getFileNo());
                        }
                        if(StringUtils.isNotBlank(dto.getFileType())){
                            heGuiYuJingAll.setFileType(dto.getFileType());
                        }
                        if(StringUtils.isNotBlank(dto.getFileSetupDate())){
                            heGuiYuJingAll.setFileSetupDate(dto.getFileSetupDate());
                        }
                        if(StringUtils.isNotBlank(dto.getCheckStatus())){
                            heGuiYuJingAll.setCheckStatus(dto.getCheckStatus());
                        }
                        heGuiYuJingAll.setIsFileDueOver(dto.getIsFileDueOver());
                        heGuiYuJingAll.setYuJingType("3");
                        heGuiYuJingAllDao.save(heGuiYuJingAll);
                    }
                }
            }
        }

        log.info("保存全量预警数据证件到期-----");
        pagingDto.setList(customerAllResponseList);
        pagingDto.setTotalRecord(page.getTotalElements());
        pagingDto.setTotalPages(page.getTotalPages());
        return pagingDto;
    }




@Override
    public void updateCustomerNoticeDue(String type) {
        Long legalOverNoticeDay = configService.findOneByKey("legalOverNoticeDay");
        if(legalOverNoticeDay == null) {
            legalOverNoticeDay = 3L;
        }

        Date nowDate = DateUtil.beginOfDate(new Date());
        Date beforeDate = DateUtil.subDays(nowDate, (int) (legalOverNoticeDay + 0));

        //法人证件到期是否超期字段更新
        if("legalDueNotice".equals(type)) {  //法人证件到期
            List<CustomerPublic> list = customerPublicDao.findAll(new Specification<CustomerPublic>() {
                @Override
                public Predicate toPredicate(Root<CustomerPublic> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                    Predicate predicate = cb.conjunction();
                    List<Expression<Boolean>> expressions = predicate.getExpressions();
                    expressions.add(cb.isNotNull(root.get("legalIdcardDue")));

                    return predicate;
                }
            });

            if (CollectionUtils.isNotEmpty(list)) {
                for (CustomerPublic customerPublic : list) {
                    if (customerPublic.getIsLegalIdcardDueOver() == null || !customerPublic.getIsLegalIdcardDueOver()) {
                        if (StringUtils.isNotBlank(customerPublic.getLegalIdcardDue())) {
                            try {
                                Date legalIdcardDate = DateUtils.parse(customerPublic.getLegalIdcardDue(), "yyyy-MM-dd");
                                if (legalIdcardDate.equals(beforeDate) || legalIdcardDate.before(beforeDate)) {  //超期
                                    customerPublic.setIsLegalIdcardDueOver(true);
                                } else {
                                    customerPublic.setIsLegalIdcardDueOver(false);
                                }
                            } catch (ParseException e) {
                                log.info("通知提醒legalIdcardDate转换异常");
                                continue;
                            }

                            customerPublicDao.save(customerPublic);
                        }

                    }
                }
            }
        } else if("fileDueNotice".equals(type)) {
            //证明文件到期是否超期字段更新
            List<CustomerPublic> customerList = customerPublicDao.findAll(new Specification<CustomerPublic>() {
                @Override
                public Predicate toPredicate(Root<CustomerPublic> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                    Predicate predicate = cb.conjunction();
                    List<Expression<Boolean>> expressions = predicate.getExpressions();
                    Predicate fileDue1 = cb.isNotNull(root.get("fileDue"));
                    Predicate fileDue2 = cb.isNotNull(root.get("fileDue2"));
                    expressions.add(cb.or(fileDue1, fileDue2));

                    return predicate;
                }
            });


            if(CollectionUtils.isNotEmpty(customerList)) {
                for (CustomerPublic customerPublic : customerList) {
                    if (customerPublic.getIsFileDueOver() == null || !customerPublic.getIsFileDueOver()) {
                        if (StringUtils.isNotBlank(customerPublic.getFileDue())) {
                            try {
                                Date fileDueDate = DateUtils.parse(customerPublic.getFileDue(), "yyyy-MM-dd");

                                if (fileDueDate.equals(beforeDate) || fileDueDate.before(beforeDate)) {  //超期
                                    customerPublic.setIsFileDueOver(true);
                                } else {
                                    customerPublic.setIsFileDueOver(false);
                                    //证明文件到期日2判断是否超期
                                    setFileDue2Over(beforeDate, customerPublic);
                                }
                            } catch (ParseException e) {
                                log.info("通知提醒fileDue转换异常");
                                continue;
                            }

                            customerPublicDao.save(customerPublic);
                        }

                    }
                }
            }

        }


    }

    @Override
    public IExcelExport exportCustDueBefore(String type, CustomerPublicInfo customerPublicInfo, final String afterDateStr, final String beforeDateStr, String organFullId) throws Exception {
        final String nowDateStr = DateUtils.DateToStr(new Date(), "");
        List<CustomerPublic> customerPublicList = null;
        customerPublicInfo.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        final String currentOrgFullId = SecurityUtils.getCurrentOrgFullId();
        final String depositorName = customerPublicInfo.getDepositorName();
        final String fileDue = customerPublicInfo.getFileDue();
        final String fileDue2 = customerPublicInfo.getFileDue2();
        final String legalName = customerPublicInfo.getLegalName();
        final String legalIdcardDue = customerPublicInfo.getLegalIdcardDue();
        final Boolean isFileDueOver = customerPublicInfo.getIsFileDueOver();
        final Boolean isLegalIdcardDueOver = customerPublicInfo.getIsLegalIdcardDueOver();


        if("legalDueNotice".equals(type)) {

            //1、查询
            Specification<CustomerPublic> specification = new Specification<CustomerPublic>() {
                @Override
                public Predicate toPredicate(Root<CustomerPublic> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                    Predicate predicate = cb.conjunction();
                    List<Expression<Boolean>> expressions = predicate.getExpressions();

                    //过期逻辑判断
                    expressions.add(cb.like(root.<String>get("organFullId"), currentOrgFullId + "%"));
                    Predicate greaterThanFileDue = cb.greaterThanOrEqualTo(root.<String>get("legalIdcardDue"), nowDateStr);
                    Predicate lessThanFileDue = cb.lessThanOrEqualTo(root.<String>get("legalIdcardDue"), afterDateStr);

                    //超期逻辑判断
                    Predicate overLegalIdcardDue = cb.lessThanOrEqualTo(root.<String>get("legalIdcardDue"), beforeDateStr);

                    Predicate legalIdcardDuePred = cb.and(greaterThanFileDue, lessThanFileDue);

                    expressions.add(cb.or(legalIdcardDuePred, overLegalIdcardDue));

                    if(StringUtils.isNotBlank(depositorName)) {
                        expressions.add(cb.and(cb.like(root.<String>get("depositorName"), "%" + depositorName + "%")));
                    }
                    if(StringUtils.isNotBlank(legalName)) {
                        expressions.add(cb.and(cb.equal(root.<String>get("legalName"), legalName)));
                    }
                    if(StringUtils.isNotBlank(legalIdcardDue)) {
                        expressions.add(cb.and(cb.equal(root.<String>get("legalIdcardDue"), legalIdcardDue)));
                    }
                    if(isLegalIdcardDueOver != null) {
                        expressions.add(cb.and(cb.equal(root.<String>get("isLegalIdcardDueOver"), isLegalIdcardDueOver)));
                    }

                    return predicate;
                }
            };

            customerPublicList = customerPublicDao.findAll(specification);

            //2、导出
            IExcelExport iExcelExport = new LegalDueExport();
            List<LegalDuePoi> legalDuePoiList = new ArrayList<>();

            for (CustomerPublic customerPublic :customerPublicList){
                LegalDuePoi legalDuePoi = new LegalDuePoi();
                ConverterService.convert(customerPublic,legalDuePoi);
                legalDuePoi.setLegalType(dictionaryService.transalte("legalTypeValue2Item",customerPublic.getLegalType()));
                legalDuePoi.setLegalIdcardType(dictionaryService.transalte("legalIdcardTypeValue2Item",customerPublic.getLegalIdcardType()));
                if (null!=customerPublic.getIsLegalIdcardDueOver()&&customerPublic.getIsLegalIdcardDueOver()){
                    legalDuePoi.setIsLegalIdcardDueOver("是");
                }else {
                    legalDuePoi.setIsLegalIdcardDueOver("否");
                }
                legalDuePoiList.add(legalDuePoi);
            }

            iExcelExport.setPoiList(legalDuePoiList);

            return iExcelExport;

        } else if ("fileDueNotice".equals(type)){

            //1、查询
            Specification<CustomerPublic> specification = new Specification<CustomerPublic>() {
                @Override
                public Predicate toPredicate(Root<CustomerPublic> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                    Predicate predicate = cb.conjunction();
                    List<Expression<Boolean>> expressions = predicate.getExpressions();

                    //过期逻辑判断
                    expressions.add(cb.like(root.<String>get("organFullId"), currentOrgFullId + "%"));
                    Predicate greaterThanFileDue = cb.greaterThanOrEqualTo(root.<String>get("fileDue"), nowDateStr);
                    Predicate lessThanFileDue = cb.lessThanOrEqualTo(root.<String>get("fileDue"), afterDateStr);
                    Predicate greaterThanFileDue2 = cb.greaterThanOrEqualTo(root.<String>get("fileDue2"), nowDateStr);
                    Predicate lessThanFileDue2 = cb.lessThanOrEqualTo(root.<String>get("fileDue2"), afterDateStr);

                    //超期逻辑判断
                    Predicate overFileDue = cb.lessThanOrEqualTo(root.<String>get("fileDue"), beforeDateStr);
                    Predicate overFileDue2 = cb.lessThanOrEqualTo(root.<String>get("fileDue2"), beforeDateStr);

                    Predicate fileDuePred = cb.and(greaterThanFileDue, lessThanFileDue);
                    Predicate fileDue2Pred = cb.and(greaterThanFileDue2, lessThanFileDue2);

                    expressions.add(cb.or(fileDuePred, fileDue2Pred, overFileDue, overFileDue2));

                    if(StringUtils.isNotBlank(depositorName)) {
                        expressions.add(cb.and(cb.like(root.<String>get("depositorName"), "%" + depositorName + "%")));
                    }
                    if(StringUtils.isNotBlank(fileDue)) {
                        expressions.add(cb.and(cb.equal(root.<String>get("fileDue"), fileDue)));
                    }
                    if(StringUtils.isNotBlank(fileDue2)) {
                        expressions.add(cb.and(cb.equal(root.<String>get("fileDue2"), fileDue2)));
                    }
                    if(isFileDueOver != null) {
                        expressions.add(cb.and(cb.equal(root.<String>get("isFileDueOver"), isFileDueOver)));
                    }

                    return predicate;
                }
            };

            customerPublicList = customerPublicDao.findAll(specification);
            //2、导出
            IExcelExport iExcelExport = new FileDueExport();
            List<FileDuePoi> fileDuePoiList = new ArrayList<>();

            for (CustomerPublic customerPublic :customerPublicList){
                FileDuePoi fileDuePoi = new FileDuePoi();
                ConverterService.convert(customerPublic,fileDuePoi);

                //fileTypejiBenValueItem fileTypeTeShuValueItem fileTypeLinShiValueItem
                String fileType = dictionaryService.transalte("fileTypejiBenValueItem",customerPublic.getFileType());
                fileType = dictionaryService.transalte("fileTypeTeShuValueItem",fileType);
                fileType = dictionaryService.transalte("fileTypeLinShiValueItem",fileType);
                fileDuePoi.setFileType(fileType);

                String fileType2 = dictionaryService.transalte("fileTypejiBenValueItem",customerPublic.getFileType2());
                fileType2 = dictionaryService.transalte("fileTypeTeShuValueItem",fileType2);
                fileType2 = dictionaryService.transalte("fileTypeLinShiValueItem",fileType2);
                fileDuePoi.setFileType2(fileType2);

                if (null!=customerPublic.getIsLegalIdcardDueOver()&&customerPublic.getIsLegalIdcardDueOver()){
                    fileDuePoi.setIsFileDueOver("是");
                }else {
                    fileDuePoi.setIsFileDueOver("否");
                }
                fileDuePoiList.add(fileDuePoi);
            }

            iExcelExport.setPoiList(fileDuePoiList);

            return iExcelExport;
        }
        return null;
    }

    @Override
    public List<CustomerAllResponse> getLegalDueAndOver(final String afterDateStr, final String beforeDateStr, final Boolean legalOverConfigEnabled) {
        //1、查询
        Specification<CustomerPublic> specification = new Specification<CustomerPublic>() {
            @Override
            public Predicate toPredicate(Root<CustomerPublic> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                //过期逻辑判断
                Predicate lessThanFileDue = cb.equal(root.<String>get("legalIdcardDue"), afterDateStr);

                //超期逻辑判断
                if(legalOverConfigEnabled) {  //超期
                    Predicate overLegalIdcardDue = cb.equal(root.<String>get("legalIdcardDue"), beforeDateStr);
                    expressions.add(cb.or(lessThanFileDue, overLegalIdcardDue));
                } else {
                    expressions.add(cb.and(lessThanFileDue));
                }

                return predicate;
            }
        };
        List<CustomerPublic> customerPublicList = customerPublicDao.findAll(specification);
        return ConverterService.convertToList(customerPublicList,CustomerAllResponse.class);
    }

    @Override
    public List<CustomerAllResponse> getFileDueAndOver(final String afterDateStr, final String beforeDateStr, final Boolean fileOverConfigEnabled) {
        //1、查询
        Specification<CustomerPublic> specification = new Specification<CustomerPublic>() {
            @Override
            public Predicate toPredicate(Root<CustomerPublic> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                //过期逻辑判断
                Predicate lessThanFileDue = cb.equal(root.<String>get("fileDue"), afterDateStr);
                Predicate lessThanFileDue2 = cb.equal(root.<String>get("fileDue2"), afterDateStr);

                //超期逻辑判断
                if(fileOverConfigEnabled) {  //启动超期配置
                    Predicate overFileDue = cb.equal(root.<String>get("fileDue"), beforeDateStr);
                    Predicate overFileDue2 = cb.equal(root.<String>get("fileDue2"), beforeDateStr);
                    expressions.add(cb.or(lessThanFileDue, lessThanFileDue2, overFileDue, overFileDue2));
                } else {
                    expressions.add(cb.or(lessThanFileDue, lessThanFileDue2));
                }


                return predicate;
            }
        };

        List<CustomerPublic> customerPublicList = customerPublicDao.findAll(specification);
        return ConverterService.convertToList(customerPublicList,CustomerAllResponse.class);
    }

    @Override
    public List<CustomerPublicInfo> findAll() {
        List<CustomerPublic> all = customerPublicDao.findAll();
        return ConverterService.convertToList(all,CustomerPublicInfo.class);
    }

    private void setFileType(CustomerAllResponse customerAllResponse, String fileType, String acctTypeStr) {
        if (StringUtils.isNotBlank(fileType)) {
            if ("14".equals(fileType) || "15".equals(fileType)
                    || "16".equals(fileType)) {
                customerAllResponse.setFileType(
                        dictionaryService.transalte("fileTypeFeiLinShiValueItem", fileType));
            }
            if ("linshi".equals(acctTypeStr)
                    && ("01".equals(fileType) || "09".equals(fileType))) {
                customerAllResponse.setFileType(dictionaryService.transalte("fileTypeLinShiValueItem", fileType));
            }
            if ("teshu".equals(acctTypeStr) || "12".equals(fileType)
                    || "17".equals(fileType)) {
                customerAllResponse.setFileType(dictionaryService.transalte("fileTypeTeShuValueItem", fileType));
            }
            if (("jiben".equals(acctTypeStr) || "yiban".equals(acctTypeStr)
                    || "yusuan".equals(acctTypeStr) || "feiyusuan".equals(acctTypeStr)
                    || "feilinshi".equals(acctTypeStr))
                    && ("01".equals(fileType) || "02".equals(fileType)
                    || "03".equals(fileType) || "04".equals(fileType))) {
                customerAllResponse.setFileType(dictionaryService.transalte("fileTypejiBenValueItem", fileType));
            }
        }
        if (StringUtils.isNotBlank(customerAllResponse.getFileType2())) {
            if (("jiben".equals(acctTypeStr) || "yiban".equals(acctTypeStr)
                    || "yusuan".equals(acctTypeStr) || "feiyusuan".equals(acctTypeStr)
                    || "linshi".equals(acctTypeStr))
                    && ("02".equals(customerAllResponse.getFileType2()) || "03".equals(customerAllResponse.getFileType2())
                    || "04".equals(customerAllResponse.getFileType2()) || "08".equals(customerAllResponse.getFileType2()))) {
                customerAllResponse.setFileType2(
                        dictionaryService.transalte("fileTypejiBenValue2Item", customerAllResponse.getFileType2()));
            }
            if ("teshu".equals(acctTypeStr) || "13".equals(customerAllResponse.getFileType2())
                    || "17".equals(customerAllResponse.getFileType2())) {
                customerAllResponse.setFileType2(
                        dictionaryService.transalte("fileTypeTeShuValue2Item", customerAllResponse.getFileType2()));
            }
        }
    }


    private void setFileDue2Over(Date beforeDate, CustomerPublic customerPublic) throws ParseException {
        if(StringUtils.isNotBlank(customerPublic.getFileDue2())) {
            Date fileDueDate2 = DateUtils.parse(customerPublic.getFileDue2(), "yyyy-MM-dd");
            if(fileDueDate2.equals(beforeDate) || fileDueDate2.before(beforeDate)) {
                customerPublic.setIsFileDueOver(true);
            }
        }
    }

}