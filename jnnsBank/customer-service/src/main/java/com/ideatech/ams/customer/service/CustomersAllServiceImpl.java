package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dao.CustomerPublicDao;
import com.ideatech.ams.customer.dao.CustomersAllDao;
import com.ideatech.ams.customer.dao.bill.CustomerBillsAllDao;
import com.ideatech.ams.customer.domain.CustomerDo;
import com.ideatech.ams.customer.dto.CustomerAllResponse;
import com.ideatech.ams.customer.dto.CustomersAllInfo;
import com.ideatech.ams.customer.entity.CustomerPublic;
import com.ideatech.ams.customer.entity.CustomersAll;
import com.ideatech.ams.system.dict.dao.DictionaryDao;
import com.ideatech.ams.system.dict.dao.OptionDao;
import com.ideatech.ams.system.dict.dto.OptionDto;
import com.ideatech.ams.system.dict.entity.DictionaryPo;
import com.ideatech.ams.system.dict.entity.OptionPo;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.eav.service.EavService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.constant.IdeaConstant;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
//import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author vantoo
 * @date 10:13 2018/5/25
 */

@Service
@Transactional
@Slf4j
public class CustomersAllServiceImpl implements CustomersAllService {

    @Autowired
    private CustomersAllDao customersAllDao;

    @Autowired
    private CustomerPublicDao customerPublicDao;

    @Autowired
    private CustomerBillsAllDao customerBillsAllDao;

    @Autowired
    private DictionaryDao dictionaryDao;

    @Autowired
    private OptionDao optionDao;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private EavService eavService;

    @Autowired
    private CustomerNoGenerateService customerNoGenerateService;

    @PersistenceContext
    private EntityManager em; //注入EntityManager

    @Override
    public void save(CustomersAllInfo customersAllInfo) {
        CustomersAll customersAll = null;
        if (customersAllInfo.getId() != null) {
            customersAll = customersAllDao.findOne(customersAllInfo.getId());
        }
        if (customersAll == null) {
            customersAll = new CustomersAll();
        }
        BeanCopierUtils.copyProperties(customersAllInfo, customersAll);

        customersAll = customersAllDao.save(customersAll);
        customersAllInfo.setId(customersAll.getId());
    }

    @Override
    public void saveCustomerInfo(CustomerAllResponse info) {
        CustomersAll customersAll = new CustomersAll();
        CustomerPublic customerPublic = new CustomerPublic();

        if (StringUtils.isEmpty(info.getCustomerNo())) {
            String generate = customerNoGenerateService.generate(info);
            info.setCustomerNo(generate);
        }

        if (StringUtils.isEmpty(info.getCustomerNo())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "客户号不能为空");
        }

        validateCustomerNo(info.getCustomerNo());

        BeanCopierUtils.copyProperties(info, customersAll);
        BeanCopierUtils.copyProperties(info, customerPublic);

        customersAll.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        CustomersAll customers = customersAllDao.save(customersAll);
        customerPublic.setCustomerId(customers.getId());
        customerPublic.setOrganFullId(customersAll.getOrganFullId());
        customerPublicDao.save(customerPublic);
        eavService.save(customers.getId(), IdeaConstant.DOC_CUSTOMER, info.getExt());
    }

    @Override
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void editCustomerInfo(CustomerAllResponse info) {
        CustomersAll customersAll = customersAllDao.findOne(info.getId());
        CustomerPublic customerPublic = customerPublicDao.findByCustomerId(info.getId());

        if (StringUtils.isEmpty(info.getCustomerNo())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "客户号不能为空");
        }
        if(!info.getCustomerNo().equals(customersAll.getCustomerNo())) {  //客户号变动时重复校验
            validateCustomerNo(info.getCustomerNo());
        }

        String[] ignoreProperties = {"id", "createdDate", "createdBy", "lastUpdateBy", "lastUpdateDate"};
        BeanUtils.copyProperties(info, customersAll, ignoreProperties);
        BeanUtils.copyProperties(info, customerPublic, ignoreProperties);
        customersAllDao.save(customersAll);
        customerPublicDao.save(customerPublic);
        eavService.save(customersAll.getId(), IdeaConstant.DOC_CUSTOMER, info.getExt());

    }

    private void validateCustomerNo(String customerNo) {
        CustomersAll customersAll = customersAllDao.findByCustomerNo(customerNo);
        if(customersAll != null) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "客户号不能重复");
        }
    }

    @Override
    public CustomersAllInfo findByCustomerNo(String customerNo) {
        return po2dto(customersAllDao.findByCustomerNo(customerNo));
    }

    @Override
    public CustomersAllInfo findOne(Long id) {
        return po2dto(customersAllDao.findOne(id));
    }

    @Override
    public CustomersAllInfo findByDepositorName(String depositorName) {
        List<CustomersAll> list = customersAllDao.findByDepositorName(depositorName);
        if(CollectionUtils.isNotEmpty(list) && list.size()>0){
            return po2dto(list.get(0));
        }else{
            return null;
        }
    }

    private CustomersAllInfo po2dto(CustomersAll customersAll) {
        if (customersAll != null) {
            CustomersAllInfo customersAllInfo = new CustomersAllInfo();
            BeanCopierUtils.copyProperties(customersAll, customersAllInfo);
            return customersAllInfo;
        } else {
            return null;
        }
    }

    /**
     * 客户列表查询
     * @param info
     * @param pageable
     * @return
     */
    @Override
    public TableResultResponse<CustomerAllResponse> query(CustomerAllResponse info, Pageable pageable) {
        List<OptionDto> economyTypeOpList = dictionaryService.findOptionsByDictionaryName("economyTypeValue2Item");
        List<OptionDto> credentialTypeOpList = dictionaryService.findOptionsByDictionaryNameStartWith("证明文件1");

        String sql = "SELECT " +
                "t1.YD_ID, " +
                "t1.YD_CREATED_DATE, " +
                "t1.YD_CUSTOMER_NO, " +
                "t1.YD_DEPOSITOR_NAME, " +
                "t2.YD_ECONOMY_TYPE, " +
                "t2.YD_LEGAL_TYPE, " +
                "t2.YD_ACCOUNT_KEY, " +
                "t2.YD_REG_NO, " +
                "t1.YD_CREDENTIAL_TYPE, " +
                "t2.YD_FILE_NO, " +
                "t2.YD_LEGAL_NAME, " +
                "t3.YD_NAME AS YD_ORGAN_NAME, " +
                "t4.YD_ID AS YD_CRSC_ID, " +
                "t4.YD_SAIC_INFO_ID, " +
                "t4.YD_COMPARE_TASK_ID, " +
                "t4.YD_COMPARE_RESULT_ID, " +
                "t4.YD_ABNORMAL, " +
                "t4.YD_ABNORMAL_TIME ";
        String countSql = "SELECT COUNT(1) ";
        String fromStr = "FROM YD_CUSTOMERS_ALL t1 " +
                "LEFT JOIN YD_CUSTOMER_PUBLIC t2 ON t1.YD_ID = t2.YD_CUSTOMER_ID " +
                "LEFT JOIN YD_SYS_ORGANIZATION t3 ON t1.YD_ORGAN_FULL_ID = t3.YD_FULL_ID " +
                "LEFT JOIN YD_CRSC_NEW_ALL_V t4 ON t4.YD_DEPOSITOR_NAME = t1.YD_DEPOSITOR_NAME ";
        sql += fromStr;
        countSql += fromStr;
        String whereStr = "WHERE 1=1 ";
        if (StringUtils.isNotBlank(info.getCustomerNo())) {//客户号
            whereStr += "and t1.YD_CUSTOMER_NO like ?1 ";
        }
        if (StringUtils.isNotBlank(info.getDepositorName())) {//客户名称/企业名称
            whereStr += "and t1.YD_DEPOSITOR_NAME like ?2 ";
        }
        if (StringUtils.isNotBlank(info.getEconomyType())) {//经济类型
            whereStr += "and t2.YD_ECONOMY_TYPE = ?3 ";
        }
        if (StringUtils.isNotBlank(info.getLegalType())) {//法人类型
            whereStr += "and t2.YD_LEGAL_TYPE = ?4 ";
        }
        if (StringUtils.isNotBlank(info.getStateTaxRegNo())) {//国税
            whereStr += "and t2.YD_STATE_TAX_REG_NO like ?5 ";
        }
        if (StringUtils.isNotBlank(info.getTaxRegNo())) {//地税
            whereStr += "and t2.YD_TAX_REG_NO like ?6 ";
        }
        if (StringUtils.isNotBlank(info.getAccountKey())) {//基本户核准号
            whereStr += "and t2.YD_ACCOUNT_KEY like ?7 ";
        }
        if (StringUtils.isNotBlank(info.getLegalName())) {//法人姓名
            whereStr += "and t2.YD_LEGAL_NAME like ?8 ";
        }
        if (StringUtils.isNotBlank(info.getRegNo())) {//工商注册编号/统一社会信用代码
            whereStr += "and t2.YD_REG_No like ?9 ";
        }
        if (StringUtils.isNotBlank(info.getCredentialType())) {//证件类型
            whereStr += "and t1.YD_CREDENTIAL_TYPE = ?10 ";
        }
        if (StringUtils.isNotBlank(info.getFileNo())) {//证件号
            whereStr += "and t2.YD_FILE_NO = ?11 ";
        }
        if (StringUtils.isNotBlank(info.getOrganName())) {//客户开立机构
            whereStr += "and t3.YD_NAME = ?12 ";
        }
        if (info.getAbnormal() != null) {//是否风险异动
            whereStr += "and t4.YD_ABNORMAL = ?13 ";
        }
        sql = sql + whereStr;
        countSql = countSql + whereStr;

        System.out.println(sql);
        System.out.println(countSql);

        Query query = em.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);//将结果集转为map
        Query queryCount = em.createNativeQuery(countSql);

        if (StringUtils.isNotBlank(info.getCustomerNo())) {//客户号
            query.setParameter(1, "%" + info.getCustomerNo() + "%");
            queryCount.setParameter(1, "%" + info.getCustomerNo() + "%");
        }
        if (StringUtils.isNotBlank(info.getDepositorName())) {//客户名称/企业名称
            query.setParameter(2, "%" + info.getDepositorName() + "%");
            queryCount.setParameter(2, "%" + info.getDepositorName() + "%");
        }
        if (StringUtils.isNotBlank(info.getEconomyType())) {//经济类型
            query.setParameter(3, info.getEconomyType());
            queryCount.setParameter(3, info.getEconomyType());
        }
        if (StringUtils.isNotBlank(info.getLegalType())) {//法人类型
            query.setParameter(4, info.getLegalType());
            queryCount.setParameter(4, info.getLegalType());
        }
        if (StringUtils.isNotBlank(info.getStateTaxRegNo())) {//国税
            query.setParameter(5, "%" + info.getStateTaxRegNo() + "%");
            queryCount.setParameter(5, "%" + info.getStateTaxRegNo() + "%");
        }
        if (StringUtils.isNotBlank(info.getTaxRegNo())) {//地税
            query.setParameter(6, "%" + info.getTaxRegNo() + "%");
            queryCount.setParameter(6, "%" + info.getTaxRegNo() + "%");
        }
        if (StringUtils.isNotBlank(info.getAccountKey())) {//基本户核准号
            query.setParameter(7, "%" + info.getAccountKey() + "%");
            queryCount.setParameter(7, "%" + info.getAccountKey() + "%");
        }
        if (StringUtils.isNotBlank(info.getLegalName())) {//法人姓名
            query.setParameter(8, "%" + info.getLegalName() + "%");
            queryCount.setParameter(8, "%" + info.getLegalName() + "%");
        }
        if (StringUtils.isNotBlank(info.getRegNo())) {//工商注册编号/统一社会信用代码
            query.setParameter(9, "%" + info.getRegNo() + "%");
            queryCount.setParameter(9, "%" + info.getRegNo() + "%");
        }
        if (StringUtils.isNotBlank(info.getCredentialType())) {//证件类型
            query.setParameter(10, info.getCredentialType());
            queryCount.setParameter(10, info.getCredentialType());
        }
        if (StringUtils.isNotBlank(info.getFileNo())) {//证件号
            query.setParameter(11, info.getFileNo());
            queryCount.setParameter(11, info.getFileNo());
        }
        if (StringUtils.isNotBlank(info.getOrganName())) {//客户开立机构
            query.setParameter(12, info.getOrganName());
            queryCount.setParameter(12, info.getOrganName());
        }
        if (info.getAbnormal() != null) {//是否风险异动
            query.setParameter(13, info.getAbnormal());
            queryCount.setParameter(13, info.getAbnormal());
        }

        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        //查询
        List<Object> rows = query.getResultList();
        Object totalObj = queryCount.getSingleResult();
        Long count = this.sqlResultToLong(totalObj);

        List<CustomerAllResponse> customerAllResponseList = new ArrayList<>();
        for (Object obj : rows) {
            Map map = (Map) obj;
            CustomerAllResponse customerAllResponse = new CustomerAllResponse();
            customerAllResponse.setId(this.sqlResultToLong(map.get("YD_ID")));
            customerAllResponse.setCreatedDate(DateUtils.DateToStr((Date) map.get("YD_CREATED_DATE"), "yyyy-MM-dd HH:mm:ss"));
            customerAllResponse.setCustomerNo((String) map.get("YD_CUSTOMER_NO"));
            customerAllResponse.setDepositorName((String) map.get("YD_DEPOSITOR_NAME"));
            customerAllResponse.setEconomyType((String) map.get("YD_ECONOMY_TYPE"));
            customerAllResponse.setLegalType((String) map.get("YD_LEGAL_TYPE"));
            customerAllResponse.setAccountKey((String) map.get("YD_ACCOUNT_KEY"));
            customerAllResponse.setRegNo((String) map.get("YD_REG_NO"));
            customerAllResponse.setCredentialType((String) map.get("YD_CREDENTIAL_TYPE"));
            customerAllResponse.setFileNo((String) map.get("YD_FILE_NO"));
            customerAllResponse.setLegalName((String) map.get("YD_LEGAL_NAME"));
            customerAllResponse.setOrganName((String) map.get("YD_ORGAN_NAME"));
            customerAllResponse.setCompareResultSaicCheckId(this.sqlResultToLong(map.get("YD_CRSC_ID")));
            customerAllResponse.setSaicInfoId(this.sqlResultToLong(map.get("YD_SAIC_INFO_ID")));
            customerAllResponse.setCompareTaskId(this.sqlResultToLong(map.get("YD_COMPARE_TASK_ID")));
            customerAllResponse.setCompareResultId(this.sqlResultToLong(map.get("YD_COMPARE_RESULT_ID")));
            customerAllResponse.setAbnormal(this.sqlResultToBoolean(map.get("YD_ABNORMAL")));
            customerAllResponse.setAbnormalTime((String) map.get("YD_ABNORMAL_TIME"));
            for (OptionDto option : economyTypeOpList) {
                if (option.getName().equals(customerAllResponse.getEconomyType())) {
                    customerAllResponse.setEconomyType(option.getValue());
                    break;
                }
            }
            for (OptionDto option : credentialTypeOpList) {
                if (option.getValue().equals(customerAllResponse.getCredentialType())) {
                    customerAllResponse.setCredentialType(option.getName());
                    break;
                }
            }
            customerAllResponseList.add(customerAllResponse);
        }
        return new TableResultResponse<CustomerAllResponse>(count.intValue(), customerAllResponseList);
    }

    private Long sqlResultToLong(Object obj){
        Long l = null;
        if (obj != null) {
            if (obj instanceof BigDecimal) {
                BigDecimal bd = (BigDecimal) obj;
                l = bd.longValue();
            } else if (obj instanceof BigInteger) {
                BigInteger bd = (BigInteger) obj;
                l = bd.longValue();
            } else {
                l = Long.parseLong(obj.toString());
            }
        }
        return l;
    }

    private Boolean sqlResultToBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        Long l = this.sqlResultToLong(obj);
        if (l == null) {
            return null;
        } else if (l.equals(1L)) {
            return true;
        } else if (l.equals(0L)) {
            return false;
        }
        return null;
    }

//    /**
//     * 客户列表查询
//     * @param info
//     * @param pageable
//     * @return
//     */
//    @Override
//    public TableResultResponse<CustomerAllResponse> query(CustomerAllResponse info, Pageable pageable) {
//        List<OptionPo> opList = new ArrayList<>();
//        DictionaryPo dictionaryPo = dictionaryDao.findByName("economyTypeValue2Item");
//        if (dictionaryPo != null) {
//            opList = optionDao.findByDictionaryIdOrderByName(dictionaryPo.getId());
//        }
//
//        String sql = "select new com.ideatech.ams.customer.domain.CustomerDo(t1,t2,t3) ";
//        String countSql = "select count(*) ";
////        String fromStr = "from CustomersAll t1 " +
////                "left join CustomerPublic t2 ON t1.id = t2.customerId " +
////                "left join OrganizationPo t3 ON t1.organFullId = t3.fullId "
////                ;
//        String fromStr = "from CustomersAll t1,CustomerPublic t2,OrganizationPo t3 where t1.id = t2.customerId and t1.organFullId = t3.fullId ";
//        sql += fromStr;
//        countSql += fromStr;
//        String whereStr = "";
//        if (StringUtils.isNotBlank(info.getCustomerNo())) {
//            whereStr += "and t1.customerNo like ?1 ";
//        }
//        if (StringUtils.isNotBlank(info.getDepositorName())) {
//            whereStr += "and t1.depositorName like ?2 ";
//        }
//        if (StringUtils.isNotBlank(info.getEconomyType())) {
//            whereStr += "and t2.economyType = ?3 ";
//        }
//        if (StringUtils.isNotBlank(info.getLegalType())) {
//            whereStr += "and t2.legalType = ?4 ";
//        }
//        if (StringUtils.isNotBlank(info.getStateTaxRegNo())) {
//            whereStr += "and t2.stateTaxRegNo like ?5 ";
//        }
//        if (StringUtils.isNotBlank(info.getTaxRegNo())) {
//            whereStr += "and t2.taxRegNo like ?6 ";
//        }
//        if (StringUtils.isNotBlank(info.getAccountKey())) {
//            whereStr += "and t2.accountKey like ?7 ";
//        }
//        if (StringUtils.isNotBlank(info.getLegalName())) {
//            whereStr += "and t2.legalName like ?8 ";
//        }
//        if (StringUtils.isNotBlank(info.getRegNo())) {
//            whereStr += "and t2.regNo like ?9 ";
//        }
////        if (whereStr.startsWith("and ")) {
////            whereStr = whereStr.substring("and ".length());
////            System.out.println(whereStr);
////            whereStr = " where " + whereStr;
////        }
//        sql = sql + whereStr;
//        countSql = countSql + whereStr;
//
//        System.out.println(sql);
//        System.out.println(countSql);
//
//        Query query = em.createQuery(sql);
//        Query queryCount = em.createQuery(countSql);
//
//        if (StringUtils.isNotBlank(info.getCustomerNo())) {//客户号
//            query.setParameter(1, "%" + info.getCustomerNo() + "%");
//            queryCount.setParameter(1, "%" + info.getCustomerNo() + "%");
//        }
//        if (StringUtils.isNotBlank(info.getDepositorName())) {//客户名称/企业名称
//            query.setParameter(2, "%" + info.getDepositorName() + "%");
//            queryCount.setParameter(2, "%" + info.getDepositorName() + "%");
//        }
//        if (StringUtils.isNotBlank(info.getEconomyType())) {//经济类型
//            query.setParameter(3, info.getEconomyType());
//            queryCount.setParameter(3, info.getEconomyType());
//        }
//        if (StringUtils.isNotBlank(info.getLegalType())) {//法人类型
//            query.setParameter(4, info.getLegalType());
//            queryCount.setParameter(4, info.getLegalType());
//        }
//        if (StringUtils.isNotBlank(info.getStateTaxRegNo())) {//国税
//            query.setParameter(5, "%" + info.getStateTaxRegNo() + "%");
//            queryCount.setParameter(5, "%" + info.getStateTaxRegNo() + "%");
//        }
//        if (StringUtils.isNotBlank(info.getTaxRegNo())) {//地税
//            query.setParameter(6, "%" + info.getTaxRegNo() + "%");
//            queryCount.setParameter(6, "%" + info.getTaxRegNo() + "%");
//        }
//        if (StringUtils.isNotBlank(info.getAccountKey())) {//基本户核准号
//            query.setParameter(7, "%" + info.getAccountKey() + "%");
//            queryCount.setParameter(7, "%" + info.getAccountKey() + "%");
//        }
//        if (StringUtils.isNotBlank(info.getLegalName())) {//法人姓名
//            query.setParameter(8, "%" + info.getLegalName() + "%");
//            queryCount.setParameter(8, "%" + info.getLegalName() + "%");
//        }
//        if (StringUtils.isNotBlank(info.getRegNo())) {//工商注册编号/统一社会信用代码
//            query.setParameter(9, "%" + info.getRegNo() + "%");
//            queryCount.setParameter(9, "%" + info.getRegNo() + "%");
//        }
//
//        query.setFirstResult(pageable.getOffset());
//        query.setMaxResults(pageable.getPageSize());
//        //查询
//        List<CustomerDo> list = query.getResultList();
//        Long count = (Long) queryCount.getSingleResult();
//
//        List<CustomerAllResponse> customerAllResponseList = new ArrayList<>();
//        for (CustomerDo cd : list) {
//            CustomerAllResponse customerAllResponse = new CustomerAllResponse();
//            BeanCopierUtils.copyProperties(cd.getCustomerPublic(), customerAllResponse);
//            BeanCopierUtils.copyProperties(cd.getCustomersAll(), customerAllResponse);
//            customerAllResponse.setCreatedDate(DateUtils.DateToStr(cd.getCustomersAll().getCreatedDate(), "yyyy-MM-dd HH:mm:ss"));
//            customerAllResponse.setOrganName(cd.getOrganizationPo().getName());
//            for (OptionPo option : opList) {
//                if (option.getName().equals(customerAllResponse.getEconomyType())) {
//                    customerAllResponse.setEconomyType(option.getValue());
//                    break;
//                }
//            }
//            customerAllResponseList.add(customerAllResponse);
//        }
//        return new TableResultResponse<CustomerAllResponse>(count.intValue(), customerAllResponseList);
//    }

    @Override
    public CustomerAllResponse findById(Long customerId) {
        CustomerAllResponse customerAllResponse = new CustomerAllResponse();
        CustomersAll customersAll = customersAllDao.findOne(customerId);
        CustomerPublic customerPublic = customerPublicDao.findByCustomerId(customerId);

        if (customersAll != null) {
            BeanCopierUtils.copyProperties(customersAll, customerAllResponse);
            if(customerPublic != null) {
                String[] ignoreProperties = {"id", "createdDate", "createdBy", "lastUpdateBy", "lastUpdateDate"};
                BeanUtils.copyProperties(customerPublic, customerAllResponse, ignoreProperties);
            }
            customerAllResponse.setCreatedDate(DateUtils.DateToStr(customersAll.getCreatedDate(),"yyyy-MM-dd HH:mm:ss"));
            if (StringUtils.isNotBlank(customersAll.getOrganFullId())) {
                OrganizationDto organization = organizationService.findByOrganFullId(customersAll.getOrganFullId());
                customerAllResponse.setOrganName(organization.getName());
                customerAllResponse.setOrganCode(organization.getCode());
            }
        }
        Map<String, String> byEntityIdAndDocCode = eavService.findByEntityIdAndDocCode(customerId, IdeaConstant.DOC_CUSTOMER);
        customerAllResponse.setExt(byEntityIdAndDocCode);
        return customerAllResponse;
    }

    @Override
    public CustomersAllInfo getInfo(String depositorName, String organFullId) {
        CustomersAllInfo customersAllInfo = null;
        CustomersAll customersAll = customersAllDao.findByDepositorNameAndOrganFullId(depositorName, organFullId);

        if(customersAll != null) {
            customersAllInfo = new CustomersAllInfo();
            customersAllInfo = ConverterService.convert(customersAll, CustomersAllInfo.class);
        }

        return customersAllInfo;
    }

    @Override
    public Map<Long, CustomersAllInfo> findAllInMap() {
        List<CustomersAll> all = customersAllDao.findAll();
        HashMap<Long, CustomersAllInfo> map = new HashMap<>();
        for(CustomersAll customersAll:all){
            CustomersAllInfo customersAllInfo = po2dto(customersAll);
            if(customersAllInfo != null){
                map.put(customersAllInfo.getId(),customersAllInfo);
            }
        }
        return map;
    }

    @Override
    public List<CustomersAllInfo> findByOrganFullIdLike(String organFullId) {
        return ConverterService.convertToList(customersAllDao.findByOrganFullIdLike(organFullId),CustomersAllInfo.class);
    }

    @Override
    public List<CustomersAllInfo> findByDepositorNameList(String depositorName) {
        return ConverterService.convertToList(customersAllDao.findByDepositorName(depositorName),CustomersAllInfo.class);
    }
}
