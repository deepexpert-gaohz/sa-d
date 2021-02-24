package com.ideatech.ams.risk.customerTransaction.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.compare.dao.CsrMessageDao;
import com.ideatech.ams.compare.dto.CsrMessageDto;
import com.ideatech.ams.compare.entity.CsrMessage;
import com.ideatech.ams.customer.dao.CustomerPublicDao;
import com.ideatech.ams.customer.entity.CustomerPublic;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.kyc.dao.SaicInfoDao;
import com.ideatech.ams.kyc.dto.SaicInfoDto;
import com.ideatech.ams.kyc.entity.SaicInfo;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.risk.CustomerTransaction.dto.*;
import com.ideatech.ams.risk.CustomerTransaction.service.CustomerTransactionService;
import com.ideatech.ams.risk.customerTransaction.dao.BusinessChangesDao;
import com.ideatech.ams.risk.customerTransaction.dao.CusotmerAbmormalDao;
import com.ideatech.ams.risk.customerTransaction.dao.CustomerBusinessDao;
import com.ideatech.ams.risk.customerTransaction.entity.BusinessChanges;
import com.ideatech.ams.risk.customerTransaction.entity.CustomerAbnormal;
import com.ideatech.ams.risk.customerTransaction.entity.CustomerBusiness;
import com.ideatech.ams.risk.highRisk.dao.AnomalyaDao;
import com.ideatech.ams.risk.highRisk.dao.HighRiskApiDao;
import com.ideatech.ams.risk.highRisk.dao.LllegalDao;
import com.ideatech.ams.risk.highRisk.entity.Anomalya;
import com.ideatech.ams.risk.highRisk.entity.Lllegal;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.HttpRequest;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@Transactional
public class CustomerTransactionServiceImpl implements CustomerTransactionService {

    //工商基础信息调用接口地址
    @Value("${ams.business.url}")
    private String businessUrl;

    //工商经营异常名录信息接口地址
    @Value("${ams.companyException.url}")
    private String companyExceptionUrl;

    //工商严重违法信息接口地址
    @Value("${ams.companyBlack.url}")
    private String companyBlackUrl;

    //工商信息有效日期
    @Value("${saic.data.validDays}")
    private String valiDays;

    @Autowired
    private CustomerPublicDao customerPublicDao;
    @Autowired
    private CustomerBusinessDao customerBusinessDao;
    @Autowired
    private AnomalyaDao anomalyaDao;
    @Autowired
    private LllegalDao lllegalDao;
    @Autowired
    HttpRequest httpRequest;
    @Autowired
    CusotmerAbmormalDao cusotmerAbmormalDao;
    @Autowired
    CsrMessageDao csrMessageDao;
    @Autowired
    HighRiskApiDao highRiskApiDao;
    @Autowired
    BusinessChangesDao businessChangesDao;
    @Autowired
    SaicInfoDao saicInfoDao;
    @Autowired
    OrganizationDao organizationDao;
    @Autowired
    AccountsAllDao accountsAllDao;
    @Autowired
    private UserService userService;
    @Autowired
    private SaicMonitorService saicMonitorService;
    @Autowired
    private SaicInfoService saicInfoService;

    /**
     * 客户异动
     *
     * @param bankcode
     */
    public void queryBusinessDataBase(String bankcode) {
        List<String> accountsAlls = null;
        List<SaicInfo> forInsert0 = new ArrayList<>();
        List<Anomalya> forInsert1 = new ArrayList<>();
        List<Lllegal> forInsert2 = new ArrayList<>();
        if (StringUtils.isNotBlank(bankcode)) {
            OrganizationPo organizationPoCode = organizationDao.findByCode(bankcode);
            accountsAlls = accountsAllDao.findByOrganFullIdAndAccountStatus(organizationPoCode.getFullId(), AccountStatus.normal);
        } else {
            accountsAlls = accountsAllDao.findAllByAccountStatus(AccountStatus.normal);
        }
        List<String> list = new ArrayList<>();
        for (String s : accountsAlls) {
            if (!list.contains(s)) {
                list.add(s);
            }
        }
        int num = list.size();
        if (list.size() > 0) {
            log.info("开始查询工商数据：工商需要查询数据：" + list.size());
            for (int i = 0; i < num; i++) {
                if (StringUtils.isNotBlank(list.get(i))) {
                    String keyWord = list.get(i);
                    log.info("账户名称：" + keyWord);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("keyWord", keyWord);
                    //基本工商
                    try {
                        String idpRequest0 = httpRequest.getIdpRequest(businessUrl, params);
                        if (StringUtils.isNotBlank(idpRequest0)) {
                            SaicInfo saicInfo = this.jsonToCustomerBusiness(idpRequest0);
                            forInsert0.add(saicInfo);
                            saicInfoDao.deleteByName(keyWord);
                        } else {
                            log.info("工商未找到符合企业");
//                            continue;
                        }
                    } catch (Exception e) {
                        log.info("工商查询失败");
                    }
                    //经营异常
                    try {
                        String idpRequest1 = httpRequest.getIdpRequest(companyExceptionUrl, params);
                        List<Anomalya> anomalyas = this.jsonToAnomalya(idpRequest1);
                        //如果一条超出时效的该单位的经营异常数据  则将该单位有经营异常数据删除  并插入最新
                        if (StringUtils.isNotBlank(idpRequest1)) {
                            if (anomalyas != null) {
                                forInsert1.addAll(anomalyas);
                            }
                            anomalyaDao.deleteByName(keyWord);
                        } else {
                            log.info("经营异常未找到符合企业");
//                            continue;
                        }
                    } catch (Exception e) {
                        log.info("经营异常查询失败");
                    }
                    //严重违法
                    try {

                        String idpRequest2 = httpRequest.getIdpRequest(companyBlackUrl, params);
                        if (StringUtils.isNotBlank(idpRequest2)) {
                            List<Lllegal> lllegals1 = this.jsonToLllegal(idpRequest2);
                            forInsert2.addAll(lllegals1);
                            lllegalDao.deleteByName(keyWord);
                        } else {
                            log.info("严重违法未找到符合企业");
//                        continue;
                        }
                    } catch (Exception e) {
                        log.info("严重违法查询失败");
                    }
                }
            }
            saicInfoDao.save(forInsert0);
            anomalyaDao.save(forInsert1);
            lllegalDao.save(forInsert2);
            log.info("工商数据查询结束，总共：" + forInsert0.size() + "条");
            log.info("经营异常查询结束，总共：" + forInsert1.size() + "条");
            log.info("严重违法查询结束，总共：" + forInsert2.size() + "条");
        } else {
            log.info("没有工商需要查询数据");
        }
    }

    /**
     * 调用工商数据存入yd_customer_business
     *
     * @author liuz
     * @Date 20191016
     */
    public void queryBusinessData(String bankcode) throws ParseException {
        List<SaicInfo> lastCList = null;
        if (StringUtils.isNotBlank(bankcode)) {
            OrganizationPo organizationPoCode = organizationDao.findByCode(bankcode);
            List<String> accountsAlls = accountsAllDao.findByOrganFullIdAndAccountStatus(organizationPoCode.getFullId(), AccountStatus.normal);
//            for (int i = 0; i < accountsAlls.size(); i++) {
            int num = accountsAlls.size();
            if (num >= 30) {
                num = 30;
            }
            for (int i = 0; i < num; i++) {
                if (lastCList == null) {
                    lastCList = saicInfoDao.findAllByName(accountsAlls.get(i));
                } else {
                    lastCList.addAll(saicInfoDao.findAllByName(accountsAlls.get(i)));
                }
            }
        } else {
            lastCList = saicInfoDao.findAll();
        }
        log.info("工商数量:" + lastCList.size());
        Set<SaicInfo> treeSet = new TreeSet<SaicInfo>(new Comparator<SaicInfo>() {
            @Override
            public int compare(SaicInfo o1, SaicInfo o2) {
                int compareTo = o1.getName().compareTo(o2.getName());
                return compareTo;
            }
        });
        treeSet.addAll(lastCList);
        //放入新的list 或者把当前的list进行close
        List<SaicInfo> arrayList = new ArrayList<>(treeSet);

        List<SaicInfo> forUpDateCbList = new ArrayList<>();
        if (lastCList != null) {
            Iterator<SaicInfo> SaicInfos = arrayList.iterator();
            while (SaicInfos.hasNext()) {
                try {
                    SaicInfo c = SaicInfos.next();
                    if (c == null) {
                        continue;
                    }
                    int i = 8;
                    if (StringUtils.isNotBlank(c.getCreatedDate().toString())) {
                        Date createdDate = c.getCreatedDate();
                        //天数
                        i = DateUtils.daysBetween(createdDate, new Date());
                    }
                    if (i > Integer.parseInt(valiDays)) {
//                    //更新超出期限工商数据
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("keyWord", c.getName());
                        String idpRequest = httpRequest.getIdpRequest(businessUrl, params);
                        if (StringUtils.isNotBlank(idpRequest)) {
                            SaicInfo saicInfo = this.jsonToCustomerBusiness(idpRequest);
                            //customerBusiness.setOrganFullId(c.getOrganFullId());
                            //customerBusiness.setId(c.getId());
                            forUpDateCbList.add(saicInfo);
                            SaicInfos.remove();
                        } else {
                            log.info("未找到符合企业");
                            continue;
                        }
                    }
                } catch (EacException eacException) {
                    log.info("获取数据报错！");
                    continue;
                }
            }
        }
        log.info("数据获取完成保存数据为：" + forUpDateCbList.size());
        //批量更行
        saicInfoDao.save(forUpDateCbList);

//        //查询所有对公客户  customerPublic   customerAll
//        List<CustomerPublic> customerList = customerPublicDao.findAll();
//
//        Map<String, CustomerPublic> cpMap = new HashMap<>();
////        int num = 0;
//        log.info(customerList.size());
//        for (CustomerPublic customerPublic :customerList){ if (!bankcode.equals("") && bankcode != null) {
//            OrganizationPo organizationPo = organizationDao.findByFullId(customerPublic.getOrganFullId());
//            if (organizationPo.getCode().equals(bankcode)) {
//                log.info("真实调用工商数据："+organizationPo.getCode());
////                    if (num < 5) {
//                cpMap.put(customerPublic.getDepositorName(), customerPublic);
////                        num++;
////                    }
////                    if (num == 5) {
////                        break;
////                    }
//            }
//        }else{
//            for(int b=0;b<10;b++) {
//                cpMap.put(customerPublic.getDepositorName(), customerPublic);
//            }
//        }
////            cpMap.put(customerPublic.getDepositorName(), customerPublic);
//        }
////        for (int i = 0; i < 5; i++) {
////            log.info(customerList.get(i).getOrganFullId());
////            cpMap.put(customerList.get(i).getDepositorName(), customerList.get(i));
////        }
//
//        Iterator<SaicInfo> iterator1 = arrayList.iterator();
//        while (iterator1.hasNext()) {
//            SaicInfo saicInfo = iterator1.next();
//            if (cpMap.get(saicInfo.getName()) != null) {
//                if (cpMap.get(saicInfo.getName()).getDepositorName().equals(saicInfo.getName())) {
//                    cpMap.remove(saicInfo.getName());
//                }
//            }
//        }
//
//        List<CustomerPublic> customerListForSave = new ArrayList(cpMap.values());
//
//        List<SaicInfo> listForSave = new ArrayList<>();
//        for (CustomerPublic c : customerListForSave) {
//            try {
//                if (c == null) {
//                    continue;
//                }
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("keyWord", c.getDepositorName());
//                String idpRequest = httpRequest.getIdpRequest(businessUrl, params);
//                if (StringUtils.isNotBlank(idpRequest)) {
//                    SaicInfo saicInfo = this.jsonToCustomerBusiness(idpRequest);
//                    //saicInfo.setOrganFullId(null);
//                    listForSave.add(saicInfo);
//                } else {
//                    log.info("未找到符合企业");
//                    continue;
//                }
//            } catch (EacException eacException) {
//                log.info("未找到符合企业");
//                continue;
//            }
//        }
//        //保存工商数据
//        saicInfoDao.save(listForSave);
    }

    /**
     * 获取企业经营异常信息
     */
    public void queryCompanyException(String bankcode) throws ParseException {
        //更新超出期限企业经营异常信息数据
        List<Anomalya> anomalyaAllList = null;
        if (StringUtils.isNotBlank(bankcode)) {
            OrganizationPo organizationPoCode = organizationDao.findByCode(bankcode);
            List<String> accountsAlls = accountsAllDao.findByOrganFullIdAndAccountStatus(organizationPoCode.getFullId(), AccountStatus.normal);
//            for (int i = 0; i < accountsAlls.size(); i++) {
            int num = accountsAlls.size();
            if (num >= 30) {
                num = 30;
            }
            for (int i = 0; i < num; i++) {
                if (anomalyaAllList == null) {
                    anomalyaAllList = anomalyaDao.findByName(accountsAlls.get(i));
                } else {
                    anomalyaAllList.addAll(anomalyaDao.findByName(accountsAlls.get(i)));
                }
            }
        } else {
            anomalyaAllList = anomalyaDao.findAll();
        }
        log.info("经营异常数量:" + anomalyaAllList.size());
        //去重
        Set<Anomalya> treeSet = new TreeSet<Anomalya>(new Comparator<Anomalya>() {
            @Override
            public int compare(Anomalya o1, Anomalya o2) {
                int compareTo = o1.getName().compareTo(o2.getName());
                return compareTo;
            }
        });
        treeSet.addAll(anomalyaAllList);
        //放入新的list 或者把当前的list进行close
        List<Anomalya> arrayList = new ArrayList<>(treeSet);
        if (anomalyaAllList != null) {
            List<Anomalya> forUpdate = new ArrayList<>();
//            for (Anomalya anomalya : arrayList) {
            for (int a = 0; a < arrayList.size(); a++) {
                try {
//                    if (anomalya == null) {
//                        continue;
//                    }
                    int i = 8;
                    if (StringUtils.isNotBlank(arrayList.get(a).getCreatedDate().toString())) {
                        i = DateUtils.daysBetween(arrayList.get(a).getCreatedDate(), new Date());
                    }
                    if (i > Integer.parseInt(valiDays)) {
                        //更新超出期限工商数据
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("keyWord", arrayList.get(a).getName());
                        String idpRequest = httpRequest.getIdpRequest(companyExceptionUrl, params);
                        if (StringUtils.isNotBlank(idpRequest)) {
                            List<Anomalya> anomalyas = this.jsonToAnomalya(idpRequest);
                            //如果一条超出时效的该单位的经营异常数据  则将该单位有经营异常数据删除  并插入最新
                            anomalyaDao.deleteByName(arrayList.get(a).getName());
                            if (anomalyas != null) {
                                anomalyaDao.save(anomalyas);
                            }
                            arrayList.remove(arrayList.get(a));
                        } else {
                            log.info("未找到符合企业");
                            continue;
                        }
                    }
                } catch (EacException eacException) {
                    log.info("获取数据报错！");
                    continue;
                }
            }
        }
        //查询所有对公客户  customerPublic   customerAll
        List<CustomerPublic> customerList = null;
        if (StringUtils.isNotBlank(bankcode)) {
            OrganizationPo organizationPoCode = organizationDao.findByCode(bankcode);
            customerList = customerPublicDao.findByOrganFullId(organizationPoCode.getFullId());
        } else {
            customerList = customerPublicDao.findAll();
        }
        log.info(":" + customerList.size());
        Map<String, CustomerPublic> cpMap = new HashMap<>();
        int num = 0;
        for (CustomerPublic customerPublic : customerList) {
//            if (!bankcode.equals("") && bankcode != null) {
//                OrganizationPo organizationPo = organizationDao.findByFullId(customerPublic.getOrganFullId());
//                if (organizationPo.getCode().equals(bankcode)) {
//                    log.info("真实获取企业经营异常信息："+organizationPo.getCode());
            if (num < 30) {
                cpMap.put(customerPublic.getDepositorName(), customerPublic);
                num++;
            }
            if (num == 30) {
                break;
            }
//                }
//            }else{
//                cpMap.put(customerPublic.getDepositorName(), customerPublic);
//            }
//            cpMap.put(customerPublic.getDepositorName(), customerPublic);
        }
        log.info("对公客户数量" + cpMap.size());
//        for (int i = 0; i < 5; i++) {
//            log.info(customerList.get(i).getOrganFullId());
//            cpMap.put(customerList.get(i).getDepositorName(), customerList.get(i));
//        }
        //排除已存在的经营异常信息的企业
        Iterator<Anomalya> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            Anomalya anomalya = iterator.next();
            if (cpMap.get(anomalya.getName()) != null) {
                if (cpMap.get(anomalya.getName()).getDepositorName().equals(anomalya.getName())) {
                    cpMap.remove(anomalya.getName());
                }
            }
        }

        List<CustomerPublic> customerListForSave = new ArrayList(cpMap.values());
        List<Anomalya> forInsert = new ArrayList<>();
        if (customerListForSave != null && customerListForSave.size() > 0) {
            for (CustomerPublic c : customerListForSave) {
                try {
                    if (c == null) {
                        continue;
                    }
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("keyWord", c.getDepositorName());
                    String idpRequest = httpRequest.getIdpRequest(companyExceptionUrl, params);
                    if (StringUtils.isNotBlank(idpRequest)) {
                        List<Anomalya> anomalyas = this.jsonToAnomalya(idpRequest);
                        forInsert.addAll(anomalyas);
                    } else {
                        log.info("未找到符合企业");
                        continue;
                    }
                } catch (EacException eacException) {
                    log.info("获取数据报错！");
                    continue;
                }
            }
        }
        //保存所有信息
        if (forInsert != null) {
            anomalyaDao.save(forInsert);
        }
    }


    /**
     * 获取企业严重违法信息
     */

    public void queryCompanyBlack(String bankcode) throws ParseException {
        List<Lllegal> lllegals = null;
        if (StringUtils.isNotBlank(bankcode)) {
            OrganizationPo organizationPoCode = organizationDao.findByCode(bankcode);
            List<String> accountsAlls = accountsAllDao.findByOrganFullIdAndAccountStatus(organizationPoCode.getFullId(), AccountStatus.normal);
//            for (int i = 0; i < accountsAlls.size(); i++) {
            int num = accountsAlls.size();
            if (num >= 30) {
                num = 30;
            }
            for (int i = 0; i < num; i++) {
                if (lllegals == null) {
                    lllegals = lllegalDao.findByName(accountsAlls.get(i));
                } else {
                    lllegals.addAll(lllegalDao.findByName(accountsAlls.get(i)));
                }
            }
        } else {
            lllegals = lllegalDao.findAll();
        }
        //更新已有超出了期限的严重违法信息
        log.info("严重违法数据：" + lllegals.size());
        Set<Lllegal> treeSet = new TreeSet<Lllegal>(new Comparator<Lllegal>() {
            @Override
            public int compare(Lllegal o1, Lllegal o2) {
                int compareTo = o1.getName().compareTo(o2.getName());
                return compareTo;
            }
        });
        treeSet.addAll(lllegals);
        //放入新的list 或者把当前的list进行close
        List<Lllegal> arrayList = new ArrayList<>(treeSet);
        int nn = 0;
        if (arrayList != null && arrayList.size() > 0) {
            log.info("" + nn);
//            for (Lllegal lllegal : arrayList) {
            for (int a = 0; a < arrayList.size(); a++) {
                log.info("" + nn);
                try {
//                    if (lllegal == null) {
//                        continue;
//                    }
                    int i = 8;
                    if (StringUtils.isNotBlank(arrayList.get(a).getCreatedDate().toString())) {
                        i = DateUtils.daysBetween(arrayList.get(a).getCreatedDate(), new Date());
                    }
                    if (i > Integer.parseInt(valiDays)) {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("keyWord", arrayList.get(a).getName());
                        String idpRequest = httpRequest.getIdpRequest(companyBlackUrl, params);
                        if (StringUtils.isNotBlank(idpRequest)) {
                            List<Lllegal> lllegals1 = this.jsonToLllegal(idpRequest);
                            lllegalDao.deleteByName(arrayList.get(a).getName());
                            if (lllegals1 != null) {
                                lllegalDao.save(lllegals1);
                            }
                            arrayList.remove(arrayList.get(a));
                        } else {
                            log.info("未找到符合企业");
                            continue;
                        }
                    }
                } catch (EacException eacException) {
                    log.info("获取数据报错！");
                    continue;
                }
                nn++;
                log.info("" + nn);
            }
        }
        //查询所有对公客户  customerPublic
//        List<CustomerPublic> customerList = customerPublicDao.findAll();
        List<CustomerPublic> customerList = null;
        if (StringUtils.isNotBlank(bankcode)) {
            OrganizationPo organizationPoCode = organizationDao.findByCode(bankcode);
            customerList = customerPublicDao.findByOrganFullId(organizationPoCode.getFullId());
        } else {
            customerList = customerPublicDao.findAll();
        }

        Map<String, CustomerPublic> cpMap = new HashMap<>();
        int num = 0;
        for (CustomerPublic customerPublic : customerList) {
//            if (!bankcode.equals("") && bankcode != null) {
//                OrganizationPo organizationPo = organizationDao.findByFullId(customerPublic.getOrganFullId());
//                if (organizationPo.getCode().equals(bankcode)) {
//                    log.info("真实获取企业严重违法信息："+organizationPo.getCode());
            if (num < 30) {
                cpMap.put(customerPublic.getDepositorName(), customerPublic);
                num++;
            }
            if (num == 30) {
                break;
            }
//                }
//            }else{
//                for(int b=0;b<10;b++) {
//                    cpMap.put(customerPublic.getDepositorName(), customerPublic);
//                }
//            }

//            cpMap.put(customerPublic.getDepositorName(), customerPublic);
        }
        log.info("对公客户数量:" + cpMap.size());
//        for (int i = 0; i < 5; i++) {
//            log.info(customerList.get(i).getOrganFullId());
//            cpMap.put(customerList.get(i).getDepositorName(), customerList.get(i));
//        }
//        for (Lllegal lllegal : arrayList) {
        for (int a = 0; a < arrayList.size(); a++) {
            if (cpMap.get(arrayList.get(a).getName()) != null) {
                if (arrayList.get(a).getName().equals(cpMap.get(arrayList.get(a).getName()).getDepositorName())) {
                    cpMap.remove(arrayList.get(a).getName());
                }
            }
        }
        List<CustomerPublic> customerListForSave = new ArrayList(cpMap.values());

        List<Lllegal> forInsert = new ArrayList<>();
        for (CustomerPublic c : customerListForSave) {
            try {
                if (c == null) {
                    continue;
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("keyWord", c.getDepositorName());
                String idpRequest = httpRequest.getIdpRequest(companyBlackUrl, params);
                if (StringUtils.isNotBlank(idpRequest)) {
                    List<Lllegal> lllegals1 = this.jsonToLllegal(idpRequest);
                    forInsert.addAll(lllegals1);
                } else {
                    log.info("未找到符合企业");
                    continue;
                }
            } catch (EacException eacException) {
                log.info("获取数据报错！");
                continue;
            }
        }
        //保存所有信息
        if (forInsert != null) {
            lllegalDao.save(forInsert);
        }

    }


    /**
     * 生成客户异动数据
     */
    public void checkBusinessData() throws ParseException {
        //所有对公客户信息
        List<CustomerPublic> customerPublicDaoAll = customerPublicDao.findAll();
        //所有工商基本数据
        List<SaicInfo> saicInfos = saicInfoDao.findAll();
        List<CustomerBusiness> customerBusinessDaoAll = new ArrayList<>();
        for (SaicInfo saicInfo : saicInfos) {
            CustomerBusiness customerBusiness = new CustomerBusiness();
            customerBusiness.setName(saicInfo.getName());
            customerBusiness.setEndDate(saicInfo.getEnddate());
            customerBusiness.setLegalPerson(saicInfo.getLegalperson());
            customerBusiness.setRegistCapital(saicInfo.getRegistfund());
            customerBusiness.setScope(saicInfo.getScope());
            customerBusiness.setUnityCreditCode(saicInfo.getUnitycreditcode());
            customerBusiness.setState(saicInfo.getState());
            customerBusiness.setOrgCode(saicInfo.getSaiccode());
            customerBusinessDaoAll.add(customerBusiness);
        }
        //所有企业经营异常信息
        List<Anomalya> anomalyaDaoAll = anomalyaDao.findAll();
        //所有企业严重违法数据
        List<Lllegal> lllegalDaoAll = lllegalDao.findAll();

        //把工商数据  企业经营异常   严重违法数据放入map
        Map<String, CustomerBusiness> mapCb = new HashMap<>();
        Map<String, Anomalya> mapAy = new HashMap<>();
        Map<String, Lllegal> mapLg = new HashMap<>();
        Map<String, CustomerBusiness> mapCbOrgCode = new HashMap<>();
        Map<String, Anomalya> mapAyOrgCode = new HashMap<>();
        Map<String, Lllegal> mapLgOrgCode = new HashMap<>();
        for (CustomerBusiness customerBusiness : customerBusinessDaoAll) {
            mapCb.put(customerBusiness.getName(), customerBusiness);
        }
        for (Anomalya anomalya : anomalyaDaoAll) {
            mapAy.put(anomalya.getName(), anomalya);
        }
        for (Lllegal lllegal : lllegalDaoAll) {
            mapLg.put(lllegal.getName(), lllegal);
        }
        for (CustomerBusiness customerBusiness : customerBusinessDaoAll) {
            if (StringUtils.isNotEmpty(customerBusiness.getUnityCreditCode())) {
                mapCbOrgCode.put(customerBusiness.getUnityCreditCode().substring(8, 17), customerBusiness);
            }
        }
        for (Anomalya anomalya : anomalyaDaoAll) {
            if (StringUtils.isNotEmpty(anomalya.getUnityCreditCode())) {
                mapAyOrgCode.put(anomalya.getUnityCreditCode().substring(8, 17), anomalya);
            }
        }
        for (Lllegal lllegal : lllegalDaoAll) {
            if (StringUtils.isNotEmpty(lllegal.getUnityCreditCode())) {
                mapLgOrgCode.put(lllegal.getUnityCreditCode().substring(8, 17), lllegal);
            }
        }
        //对比
        List<CustomerAbnormal> forSave = new ArrayList<>();
        List<String> forDelete = new ArrayList<>();
        List<BusinessChanges> businessChangesList = new ArrayList<>();

        for (CustomerPublic cp : customerPublicDaoAll) {
            CustomerAbnormalDto customerAbnormalDto = new CustomerAbnormalDto();
            //判断用户是否存在任何异常标识
            Boolean flag = false;
            Boolean isChange = false;
            //对比工商基本信息
            if (StringUtils.isNotEmpty(cp.getOrgCode())) {
                if (mapCbOrgCode.get(cp.getOrgCode()) != null && mapCbOrgCode != null) {
                    if (cp.getOrgCode().equals(mapCbOrgCode.get(cp.getOrgCode()).getUnityCreditCode().substring(8, 17))) {
                        //工商状态异常（是否注销或者吊销是否正常营业状态）=============================================================
                        CustomerBusiness cb = mapCbOrgCode.get(cp.getOrgCode());
                        if (cb.getState() != null) {
                            if (this.ToDBC(cb.getState()).equals(this.ToDBC("开业")) || this.ToDBC(cb.getState()).equals(this.ToDBC("存续（在营、开业、在册）"))) {
                                customerAbnormalDto.setAbnormalState(false);
                            } else {
                                customerAbnormalDto.setAbnormalState(true);
                                flag = true;
                            }
                        }
                        //判断经营是否到期  ============================================================
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
                        //log.info(cb.getEndDate());
                        Date endDate = formatter.parse(cb.getEndDate());
                        Date nowDate = new Date();
                        boolean notEnd = nowDate.before(endDate);
                        if (notEnd) {
                            //经营未到期
                            customerAbnormalDto.setBusinessExpires(false);
                        } else {
                            //经营到期
                            customerAbnormalDto.setBusinessExpires(true);
                            flag = true;
                        }

                        //判断登记信息是否异常 ============================================================
                        //变更信息
                        //判断  企业名称   法人  注册资本   经营范围是否存在变动
                        //默认为false无异动
                        customerAbnormalDto.setChanged(false);
                        if (cb.getName() != null && cp.getDepositorName() != null) {
                            if (!this.ToDBC(cb.getName()).equals(this.ToDBC(cp.getDepositorName()))) {
                                customerAbnormalDto.setChanged(true);
                                BusinessChanges business1 = this.createBussinessChanges(cp.getDepositorName(), cb.getName(), "企业名称", cp.getDepositorName());
                                businessChangesList.add(business1);
                                flag = true;
                                isChange = true;
                            }
                        }
                        if (cb.getLegalPerson() != null && cp.getLegalName() != null) {
                            if (!this.ToDBC(cb.getLegalPerson()).equals(this.ToDBC(cp.getLegalName()))) {
                                customerAbnormalDto.setChanged(true);
                                BusinessChanges business2 = this.createBussinessChanges(cp.getLegalName(), cb.getLegalPerson(), "法人变更", cp.getDepositorName());
                                businessChangesList.add(business2);
                                flag = true;
                                isChange = true;
                            }
                        }
                        if (cp.getRegisteredCapital() != null && cb.getRegistCapital() != null) {
                            BigDecimal bigDecimal2 = new BigDecimal(cb.getRegistCapital().replace(",", ""));
                            if (cp.getRegisteredCapital().compareTo(bigDecimal2) != 0) {
                                //两次注册资金不相同不相同
                                customerAbnormalDto.setChanged(true);
                                BusinessChanges business3 = this.createBussinessChanges(cp.getRegisteredCapital().toString(), cb.getRegistCapital(), "注册资金变动", cp.getDepositorName());
                                businessChangesList.add(business3);
                                flag = true;
                                isChange = true;
                            }
                        }
//                        if (cp.getBusinessScope() != null && cb.getScope() != null) {
//                            if (!cp.getBusinessScope().equals(cb.getScope())) {
//                                customerAbnormalDto.setChanged(true);
//                                BusinessChanges business4 = this.createBussinessChanges(cp.getBusinessScope(), cb.getScope(), "经营范围", cp.getDepositorName());
//                                businessChangesList.add(business4);
//                                flag = true;
//                                isChange = true;
//                            }
//
//                        }
                    }
                } else {
                    customerAbnormalDto.setAbnormalState(false);
                    customerAbnormalDto.setBusinessExpires(false);
                    customerAbnormalDto.setChanged(false);
                }

                //判断是否存在经营异常  =========================================================
                if (mapAyOrgCode.get(cp.getOrgCode()) != null && mapAyOrgCode != null) {
                    if (mapAyOrgCode.get(cp.getOrgCode()).getUnityCreditCode().substring(8, 17).equals(cp.getOrgCode())) {
                        flag = true;
                        customerAbnormalDto.setChangeMess(true);
                    } else {
                        customerAbnormalDto.setChangeMess(false);
                    }
                } else {
                    customerAbnormalDto.setChangeMess(false);
                }


                //判断是否存在严重违法=======================================================
                if (mapLgOrgCode.get(cp.getOrgCode()) != null && mapLgOrgCode != null) {
                    if (mapLgOrgCode.get(cp.getOrgCode()).getUnityCreditCode().substring(8, 17).equals(cp.getOrgCode())) {
                        customerAbnormalDto.setIllegal(true);
                        flag = true;
                    } else {
                        customerAbnormalDto.setIllegal(false);
                    }
                } else {
                    customerAbnormalDto.setIllegal(false);
                }
            } else {
                //如果组织机构代码为空  则使用客户名称对比
                if (mapCb.get(cp.getDepositorName()) != null && mapCb != null) {
                    if (cp.getDepositorName().equals(mapCb.get(cp.getDepositorName()).getName())) {
                        //工商状态异常（是否注销或者吊销是否正常营业状态）=============================================================
                        CustomerBusiness cb = mapCb.get(cp.getDepositorName());
                        if (cb.getState() != null) {
                            if (this.ToDBC(cb.getState()).equals(this.ToDBC("开业")) || this.ToDBC(cb.getState()).equals(this.ToDBC("存续（在营、开业、在册）"))) {
                                customerAbnormalDto.setAbnormalState(false);
                            } else {
                                customerAbnormalDto.setAbnormalState(true);
                                flag = true;
                            }
                        }
                        //判断经营是否到期  ============================================================
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
                        //log.info(cb.getEndDate());
                        Date endDate = formatter.parse(cb.getEndDate());
                        Date nowDate = new Date();
                        boolean notEnd = nowDate.before(endDate);
                        if (notEnd) {
                            //经营未到期
                            customerAbnormalDto.setBusinessExpires(false);
                        } else {
                            //经营到期
                            customerAbnormalDto.setBusinessExpires(true);
                            flag = true;
                        }


                        //判断登记信息是否异常 ============================================================
                        //变更信息
                        //判断  企业名称   法人  注册资本   经营范围是否存在变动
                        customerAbnormalDto.setChanged(false);
                        if (cb.getName() != null && cp.getDepositorName() != null) {
                            if (!this.ToDBC(cb.getName()).equals(this.ToDBC(cp.getDepositorName()))) {
                                customerAbnormalDto.setChanged(true);
                                BusinessChanges business5 = this.createBussinessChanges(cp.getDepositorName(), cb.getName(), "企业名称", cp.getDepositorName());
                                businessChangesList.add(business5);
                                flag = true;
                                isChange = true;
                            }
                        }
                        if (cb.getLegalPerson() != null && cp.getLegalName() != null) {
                            if (!this.ToDBC(cb.getLegalPerson()).equals(this.ToDBC(cp.getLegalName()))) {
                                customerAbnormalDto.setChanged(true);
                                BusinessChanges business6 = this.createBussinessChanges(cp.getLegalName(), cb.getLegalPerson(), "法人变更", cp.getDepositorName());
                                businessChangesList.add(business6);
                                flag = true;
                                isChange = true;
                            }
                        }
                        if (cp.getRegisteredCapital() != null && cb.getRegistCapital() != null) {
                            BigDecimal bigDecimal = new BigDecimal(cb.getRegistCapital().replace(",", ""));
                            if (cp.getRegisteredCapital().compareTo(bigDecimal) != 0) {
                                //两次注册资金不相同不相同
                                customerAbnormalDto.setChanged(true);
                                BusinessChanges business7 = this.createBussinessChanges(cp.getRegisteredCapital().toString(), cb.getRegistCapital(), "注册资金变动", cp.getDepositorName());
                                businessChangesList.add(business7);
                                flag = true;
                                isChange = true;
                            }
                        }
//                        if (cp.getBusinessScope() != null && cb.getScope() != null) {
//                            if (!cp.getBusinessScope().equals(cb.getScope())) {
//                                customerAbnormalDto.setChanged(true);
//                                BusinessChanges business8 = this.createBussinessChanges(cp.getBusinessScope(), cb.getScope(), "经营范围", cp.getDepositorName());
//                                businessChangesList.add(business8);
//                                flag = true;
//                                isChange = true;
//                            }
//                        }
                    }
                } else {
                    customerAbnormalDto.setAbnormalState(false);
                    customerAbnormalDto.setBusinessExpires(false);
                    customerAbnormalDto.setChanged(false);
                }

                //判断是否存在经营异常  =========================================================
                if (mapAy.get(cp.getDepositorName()) != null && mapAy != null) {
                    if (mapAy.get(cp.getDepositorName()).getName().equals(cp.getDepositorName())) {
                        flag = true;
                        customerAbnormalDto.setChangeMess(true);
                    } else {
                        customerAbnormalDto.setChangeMess(false);
                    }
                } else {
                    customerAbnormalDto.setChangeMess(false);
                }


                //判断是否存在严重违法=======================================================
                if (mapLg.get(cp.getDepositorName()) != null && mapLg != null) {
                    if (mapLg.get(cp.getDepositorName()).getName().equals(cp.getDepositorName())) {
                        customerAbnormalDto.setIllegal(true);
                        flag = true;
                    } else {
                        customerAbnormalDto.setIllegal(false);
                    }
                } else {
                    customerAbnormalDto.setIllegal(false);
                }
            }
            if (flag) {
                //客户名称
                customerAbnormalDto.setDepositorName(cp.getDepositorName());
                //银行机构名称
                customerAbnormalDto.setOrganName(cp.getBasicBankName());
                //银行机构代码
                customerAbnormalDto.setCode(cp.getBasicBankCode());
                //短信发送状态(未发送)
                customerAbnormalDto.setMessage("0");
                //机构orgfullId
                customerAbnormalDto.setOrganFullId(cp.getOrganFullId());
                //系统异动时间
                Date date = new Date();
                //设置字符串格式
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String dateString = simpleDateFormat.format(date);
                customerAbnormalDto.setAbnormalTime(dateString);
                forDelete.add(customerAbnormalDto.getDepositorName());
                forSave.add(ConverterService.convert(customerAbnormalDto, CustomerAbnormal.class));
            }
        }
        cusotmerAbmormalDao.deleteAll();
        cusotmerAbmormalDao.save(forSave);
        businessChangesDao.deleteAll();
        businessChangesDao.save(businessChangesList);

    }

    /**
     * 组装登记信息异常
     */
    public BusinessChanges createBussinessChanges(String beforeChange, String afterChang, String type, String orgName) {
        BusinessChanges businessChanges = new BusinessChanges();
        businessChanges.setChangesBeforeContent(beforeChange);
        businessChanges.setChangesAfterContent(afterChang);
        businessChanges.setChangesType(type);
        businessChanges.setCorporateFullId(null);
        businessChanges.setName(orgName);
        //系统异动时间
        Date date = new Date();
        //设置字符串格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateString = simpleDateFormat.format(date);
        businessChanges.setChangesDate(dateString);
        return businessChanges;
    }


    /**
     * 工商接口返回数据转为customerBusiness实体类
     */
    public SaicInfo jsonToCustomerBusiness(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        //获取工商基础信息中的变更信息
        SaicInfo saicInfo = JSON.parseObject(jsonObject.toJSONString(), SaicInfo.class);
        if (StringUtils.isNotEmpty(saicInfo.getUnitycreditcode())) {
            saicInfo.setUnitycreditcode(saicInfo.getUnitycreditcode().substring(8, 17));
        }

        return saicInfo;
    }

    /**
     * 解析工商经营异常名录信息接口返回数据
     */
    public List<Anomalya> jsonToAnomalya(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray items = jsonObject.getJSONArray("items");
        List<Anomalya> anomalyas = items.toJavaList(Anomalya.class);
        List<Anomalya> remAnomalyas = new ArrayList<>();
        for (Anomalya a : anomalyas) {
//            a.setOrganFullId(orgFullId);
            if (StringUtils.isNotBlank(a.getUnityCreditCode())) {
                a.setOrgCode(a.getUnityCreditCode().substring(8, 17));
            }
            if (StringUtils.isNotBlank(a.getOutReason()) || StringUtils.isNotBlank(a.getOutDate()) || StringUtils.isNotBlank(a.getOutOrgan())) {
                remAnomalyas.add(a);
            }
        }
        for (Anomalya a : remAnomalyas) {
            anomalyas.remove(a);
        }
        return anomalyas;
    }

    /**
     * 解析企业严重违法信息接口返回数据
     */
    public List<Lllegal> jsonToLllegal(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray items = jsonObject.getJSONArray("items");
        List<Lllegal> lllegals = items.toJavaList(Lllegal.class);
        List<Lllegal> remLllegals = new ArrayList<>();
        for (Lllegal a : lllegals) {
//            a.setOrganFullId(orgFullId);
            if (StringUtils.isNotBlank(a.getUnityCreditCode())) {
                a.setOrgCode(a.getUnityCreditCode().substring(8, 17));
            }
            if (StringUtils.isNotBlank(a.getOutReason()) || StringUtils.isNotBlank(a.getOutDate()) || StringUtils.isNotBlank(a.getOutOrgan())) {
                lllegals.remove(a);
            }
        }
        for (Lllegal a : remLllegals) {
            remLllegals.remove(a);
        }
        return lllegals;
    }


    /**
     * 查询客户异动信息列表
     *
     * @author liuz 20191018
     */
    public CustomerTransactionSearchDto search(final CustomerTransactionSearchDto searchDto) {
        Specification specification = new Specification() {
            List<Predicate> predicates = new ArrayList<>(); //所有的断言

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(searchDto.getDepositorName())) {
                    predicates.add(cb.like(root.get("depositorName").as(String.class), "%" + searchDto.getDepositorName() + "%"));
                }
                if (StringUtils.isNotBlank(searchDto.getOrganName())) {
                    predicates.add(cb.like(root.get("organName").as(String.class), "%" + searchDto.getOrganName() + "%"));
                }
                if (StringUtils.isNotBlank(searchDto.getCode())) {
                    predicates.add(cb.like(root.get("code").as(String.class), "%" + searchDto.getCode() + "%"));
                }
                if (StringUtils.isNotBlank(searchDto.getBeginDate())) {//系统异动开始时间
                    predicates.add(cb.greaterThanOrEqualTo(root.get("abnormalTime").as(String.class), searchDto.getBeginDate()));
                }
                if (StringUtils.isNotBlank(searchDto.getEndDate())) {//系统异动结束时间
                    predicates.add(cb.lessThanOrEqualTo(root.get("abnormalTime").as(String.class), searchDto.getEndDate()));
                }
                if (searchDto.getIllegal() != null) {
                    predicates.add(cb.equal(root.get("illegal").as(Boolean.class), searchDto.getIllegal()));
                }
                if (searchDto.getChangeMess() != null) {//经营异常
                    predicates.add(cb.equal(root.get("changeMess").as(Boolean.class), searchDto.getChangeMess()));
                }
                if (searchDto.getBusinessExpires() != null) {//经营到期
                    predicates.add(cb.equal(root.get("businessExpires").as(Boolean.class), searchDto.getBusinessExpires()));
                }
                if (searchDto.getAbnormalState() != null) {//工商状态异常
                    predicates.add(cb.equal(root.get("abnormalState").as(Boolean.class), searchDto.getAbnormalState()));
                }
                if (searchDto.getChanged() != null) {//登记信息异动
                    predicates.add(cb.equal(root.get("changed").as(Boolean.class), searchDto.getChanged()));
                }
                if (SecurityUtils.getCurrentOrgFullId() != null) {
                    predicates.add(cb.like(root.get("organFullId").as(String.class), "%" + SecurityUtils.getCurrentOrgFullId() + "%"));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
        Pageable pageable = new PageRequest(Math.max(searchDto.getOffset() - 1, 0), searchDto.getLimit());
        Page all = cusotmerAbmormalDao.findAll(specification, pageable);
        List<CustomerTransactionDto> list = ConverterService.convertToList(all.getContent(), CustomerTransactionDto.class);
        if (list != null && list.size() > 0) {
            for (CustomerTransactionDto c : list) {
                OrganizationPo byCode = organizationDao.findByCode(c.getCode());
                if (byCode != null && StringUtils.isNotBlank((byCode.getName()))) {
                    c.setOrganName(byCode.getName());
                }
            }
        }
        searchDto.setList(list);
        searchDto.setTotalPages(all.getTotalPages());
        searchDto.setTotalRecord(all.getTotalElements());
        return searchDto;
    }


    /**
     * 企业严重违法详情查看
     *
     * @param name
     * @return
     */
    @Override
    public List<Lllegal> findIllegalsDetails(String name) {
        //CustomerPublic byDepositorName = customerPublicDao.findByDepositorName(name);
        List<Lllegal> byName = lllegalDao.findByName(name);
        return byName;
    }

    @Override
    public List<Anomalya> anomalyaDetails(String name) {
        //CustomerPublic byDepositorName = customerPublicDao.findByDepositorName(name);
        List<Anomalya> byName = anomalyaDao.findByName(name);
        return byName;
    }

    @Override
    public SaicInfoDto findOpenDate(String name) {
        // CustomerPublic byDepositorName = customerPublicDao.findByDepositorName(name);
        SaicInfo saicInfo = saicInfoDao.findByName(name);
        return ConverterService.convert(saicInfo, SaicInfoDto.class);
    }

    @Override
    public List<BusinessChangesDto> findChangesDetails(String name) {
//        CustomerBusiness customerBusiness = customerBusinessDao.findByName(name);
//        String changes = customerBusiness.getChanges();
//        JSONArray objects = JSON.parseArray(changes);
//        List<BusinessChangesDto> businessChangesDtos = objects.toJavaList(BusinessChangesDto.class);
        List<BusinessChanges> byNameAndCorporateFullIdLike = businessChangesDao.findByName(name);
        return ConverterService.convertToList(byNameAndCorporateFullIdLike, BusinessChangesDto.class);
    }

    @Override
    public CustomerTransactionDto findOneById(Long id) {
        CustomerAbnormal one = cusotmerAbmormalDao.findOne(id);
        return ConverterService.convert(one, CustomerTransactionDto.class);
    }

    /**
     * 异动信息列表导出
     *
     * @param customerTransactionSearchDto
     */
    @Override
    public void exportTransaction(CustomerTransactionSearchDto customerTransactionSearchDto, HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        //sheet名称
        HSSFSheet sheet = workbook.createSheet("用户操作日志表");
        //获取表数据，根据自己实际情况获取
        CustomerTransactionSearchDto customerTransactionSearchDto1 = this.searchAll(customerTransactionSearchDto);
        List<CustomerTransactionDto> searchList = customerTransactionSearchDto1.getList();

        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        String[] headers = {"客户名称", "银行机构名称", "银行机构代码", "系统异动时间", "严重违法", "经营异常", "经营到期", "工商状态异常", "登记信息异动", "短信发送状态"};
        //headers表示excel表中第一行的表头
        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //在表中存放查询到的数据放入对应的列
        for (CustomerTransactionDto customerT : searchList) {
            HSSFRow row1 = sheet.createRow(rowNum);
            if (customerT != null) {
                if (StringUtils.isNotBlank(customerT.getDepositorName())) {
                    row1.createCell(0).setCellValue(customerT.getDepositorName());
                }
                {
                    row1.createCell(0).setCellValue("");
                }
                if (StringUtils.isNotBlank(customerT.getOrganName())) {
                    row1.createCell(1).setCellValue(customerT.getOrganName());
                }
                {
                    row1.createCell(1).setCellValue("");
                }
                if (StringUtils.isNotBlank(customerT.getCode())) {
                    row1.createCell(2).setCellValue(customerT.getCode());
                }
                {
                    row1.createCell(2).setCellValue("");
                }
                if (StringUtils.isNotBlank(customerT.getAbnormalTime())) {
                    row1.createCell(3).setCellValue(customerT.getAbnormalTime());
                }
                {
                    row1.createCell(3).setCellValue("");
                }
                if (customerT.getIllegal() != null) {
                    row1.createCell(4).setCellValue((customerT.getIllegal() == true ? "是" : "否"));
                }
                {
                    row1.createCell(4).setCellValue("");
                }
                if (customerT.getChangeMess() != null) {
                    row1.createCell(5).setCellValue((customerT.getChangeMess() == true ? "是" : "否"));
                }
                {
                    row1.createCell(5).setCellValue("");
                }
                if (customerT.getBusinessExpires() != null) {
                    row1.createCell(6).setCellValue((customerT.getBusinessExpires() == true ? "是" : "否"));
                }
                {
                    row1.createCell(6).setCellValue("");
                }
                if (customerT.getAbnormalState() != null) {
                    row1.createCell(7).setCellValue((customerT.getAbnormalState() == true ? "是" : "否"));
                }
                {
                    row1.createCell(7).setCellValue("");
                }
                if (customerT.getChanged() != null) {
                    row1.createCell(8).setCellValue((customerT.getChanged() == true ? "是" : "否"));
                }
                {
                    row1.createCell(8).setCellValue("");
                }


            }
            String isSendMessage = "";
            //1发送成功 2发送失败 0未发送
            if (customerT.getMessage().equals("1")) {
                isSendMessage = "发送成功";
            } else if (customerT.getMessage().equals("2")) {
                isSendMessage = "发送失败";
            } else {
                isSendMessage = "未发送";
            }
            row1.createCell(9).setCellValue(isSendMessage);
            rowNum++;
        }
        //设置自动列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 13 / 10);
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(
                "客户异动信息.xls".getBytes(), "iso-8859-1"));
        workbook.write(response.getOutputStream());
    }


    /**
     * 查询客户异动信息有条件不分页
     */
    public CustomerTransactionSearchDto searchAll(final CustomerTransactionSearchDto searchDto) {
        Specification specification = new Specification() {
            List<Predicate> predicates = new ArrayList<>(); //所有的断言

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(searchDto.getDepositorName())) {
                    predicates.add(cb.like(root.get("depositorName").as(String.class), "%" + searchDto.getDepositorName() + "%"));
                }
                if (StringUtils.isNotBlank(searchDto.getOrganName())) {
                    predicates.add(cb.like(root.get("organName").as(String.class), "%" + searchDto.getOrganName() + "%"));
                }
                if (StringUtils.isNotBlank(searchDto.getCode())) {
                    predicates.add(cb.like(root.get("code").as(String.class), "%" + searchDto.getCode() + "%"));
                }
                if (StringUtils.isNotBlank(searchDto.getBeginDate())) {//系统异动开始时间
                    predicates.add(cb.greaterThanOrEqualTo(root.get("abnormalTime").as(String.class), searchDto.getBeginDate()));
                }
                if (StringUtils.isNotBlank(searchDto.getEndDate())) {//系统异动结束时间
                    predicates.add(cb.lessThanOrEqualTo(root.get("abnormalTime").as(String.class), searchDto.getEndDate()));
                }
                if (searchDto.getIllegal() != null) {
                    predicates.add(cb.equal(root.get("illegal").as(Boolean.class), searchDto.getIllegal()));
                }
                if (searchDto.getChangeMess() != null) {//经营异常
                    predicates.add(cb.equal(root.get("changeMess").as(Boolean.class), searchDto.getChangeMess()));
                }
                if (searchDto.getBusinessExpires() != null) {//经营到期
                    predicates.add(cb.equal(root.get("businessExpires").as(Boolean.class), searchDto.getBusinessExpires()));
                }
                if (searchDto.getAbnormalState() != null) {//工商状态异常
                    predicates.add(cb.equal(root.get("abnormalState").as(Boolean.class), searchDto.getAbnormalState()));
                }
                if (searchDto.getChanged() != null) {//登记信息异动
                    predicates.add(cb.equal(root.get("changed").as(Boolean.class), searchDto.getChanged()));
                }
                if (SecurityUtils.getCurrentOrgFullId() != null) {
                    predicates.add(cb.like(root.get("organFullId").as(String.class), SecurityUtils.getCurrentOrgFullId() + "%"));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
        List<CustomerAbnormal> all = cusotmerAbmormalDao.findAll(specification);
        List<CustomerTransactionDto> list = ConverterService.convertToList(all, CustomerTransactionDto.class);
        if (list != null && list.size() > 0) {
            for (CustomerTransactionDto c : list) {
                OrganizationPo byCode = organizationDao.findByCode(c.getCode());
                if (byCode != null && StringUtils.isNotBlank((byCode.getName()))) {
                    c.setOrganName(byCode.getName());
                }
            }
        }
        searchDto.setList(list);
        return searchDto;
    }

    @Override
    public void saveMessage(CsrMessageDto csrMessageDto) {
        CsrMessage csrMessage = ConverterService.convert(csrMessageDto, CsrMessage.class);
        csrMessageDao.save(csrMessage);
    }

    @Override
    public void updateMessageById(String message, Long id) {
        cusotmerAbmormalDao.updateMessageById(message, id);
    }

    @Override
    public List<CsrMessageDto> messageList(CsrMessageDto csrMessageDto) {

        Specification specification = new Specification() {
            List<Predicate> predicates = new ArrayList<>(); //所有的断言

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(SecurityUtils.getCurrentOrgFullId())) {
                    predicates.add(cb.like(root.get("corporateFullId").as(String.class), SecurityUtils.getCurrentOrgFullId() + "%"));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };

        Pageable pageable = new PageRequest(Math.max(csrMessageDto.getOffset() - 1, 0), csrMessageDto.getLimit());
        //20201017李伟业修改代码，等待确认
        Page all = csrMessageDao.findAll(pageable);
        List<CsrMessageDto> list = ConverterService.convertToList(all.getContent(), CsrMessageDto.class);
        return list;
    }

    @Override
    public JSONObject getIndexAbnormalCounts(String orgFullId) {
        JSONObject jsonObject = new JSONObject();
        Long abnormalAllCount = null;
        Long illegalCount = null;
        Long changeMessCount = null;
        Long businessExpiresCount = null;
        Long abnormalStateCount = null;
        Long changedCount = null;
//        if (permissionService.findByCode("abnormalStatistics_div")) {
//            if (permissionService.findByCode("abnormalAll_div")) {//企业异动总数
//                abnormalAllCount = this.statisticsAbnormalAllCount(organFullId);
//            }
//            if (permissionService.findByCode("illegal_div")) {//严重违法数
//                illegalCount = this.statisticsIllegalCount(organFullId);
//            }
//            if (permissionService.findByCode("changeMess_div")) {//经营异常数
//                changeMessCount = this.statisticsChangeMessCount(organFullId);
//            }
//            if (permissionService.findByCode("businessExpires_div")) {//营业到期数
//                businessExpiresCount = this.statisticsBusinessExpiresCount(organFullId);
//            }
//            if (permissionService.findByCode("abnormalState_div")) {//工商异常状态数
//                abnormalStateCount = this.statisticsAbnormalStateCount(organFullId);
//            }
//            if (permissionService.findByCode("changed_div")) {//工商登记信息异动数
//                changedCount = this.statisticsChangedCount(organFullId);
//            }
//        }
        List<CustomerAbnormal> byOrganFullId = cusotmerAbmormalDao.findByOrganFullIdLike(orgFullId);

        jsonObject.put("abnormalAllCount", byOrganFullId.size());
        jsonObject.put("illegalCount", cusotmerAbmormalDao.findByOrganFullIdLikeAndIllegal(orgFullId + "%", true).size());
        jsonObject.put("changeMessCount", cusotmerAbmormalDao.findByOrganFullIdLikeAndChangeMess(orgFullId + "%", true).size());
        jsonObject.put("businessExpiresCount", cusotmerAbmormalDao.findByOrganFullIdLikeAndBusinessExpires(orgFullId + "%", true).size());
        jsonObject.put("abnormalStateCount", cusotmerAbmormalDao.findByOrganFullIdLikeAndAbnormalState(orgFullId + "%", true).size());
        jsonObject.put("changedCount", cusotmerAbmormalDao.findByOrganFullIdLikeAndChanged(orgFullId + "%", true).size());
        return jsonObject;
    }


    /**
     * 全角转半角
     *
     * @param input String.
     * @return 半角字符串
     */
    public String ToDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        String returnString = new String(c);
        return returnString;
    }

}
