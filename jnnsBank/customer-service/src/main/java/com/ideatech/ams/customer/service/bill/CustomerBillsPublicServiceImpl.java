package com.ideatech.ams.customer.service.bill;

import com.ideatech.ams.customer.dto.CustomerPublicMidInfo;
import com.ideatech.ams.customer.dto.bill.CustomerBillsAllInfo;
import com.ideatech.ams.customer.enums.bill.CustomerBillType;
import com.ideatech.ams.customer.service.CustomerPublicMidService;
import com.ideatech.common.util.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;


/**
 * @author vantoo
 * @date 10:18 2018/5/25
 */

@Service
@Transactional
@Slf4j
public class CustomerBillsPublicServiceImpl implements CustomerBillsPublicService {

    @Autowired
    private CustomerBillsAllService customerBillsAllService;

    @Autowired
    private CustomerPublicMidService customerPublicMidService;

    //TODO 客户流水需要完善
    @Override
    public void save(CustomerBillsAllInfo customerBillsAllInfo, Long userId) throws Exception {

        //1.校验关键字段
        customerBillsAllInfo.validate();
        //2.检查是否有其他流水没有走完(当前流水号为空时）
        if (customerBillsAllInfo.getId() == null) {
            validateUnfinishedBill(customerBillsAllInfo.getCustomerLogId());
        }

        if (customerBillsAllInfo.getBillType() == CustomerBillType.OPEN) {
            //3.1开户流水处理
            //3.1.1保存至中间表
            CustomerPublicMidInfo custPublicMidInfo = saveCustPublicMid(customerBillsAllInfo, userId);


        } else if (customerBillsAllInfo.getBillType() == CustomerBillType.CHANGE) {
            //3.2 变更流水处理

        }

    }


    private CustomerPublicMidInfo saveCustPublicMid(CustomerBillsAllInfo customerBillsAllInfo, Long userId) {
        customerBillsAllInfo.validate();

        // 判断中间表记录是否存在
        if (customerBillsAllInfo.getCustomerMidId() != null && customerBillsAllInfo.getCustomerMidId() > 0) {
            // 编辑更新中间表记录
            CustomerPublicMidInfo custPubMidInfo = customerPublicMidService.getOne(customerBillsAllInfo.getCustomerMidId());
            // 同步中间表信息
            String[] ignoreProperties = {"id", "createdBy", "createdDate", "customerId", "refBillId"};
            BeanUtils.copyProperties(customerBillsAllInfo, custPubMidInfo, ignoreProperties);
            custPubMidInfo.setId(customerBillsAllInfo.getCustomerMidId());
            custPubMidInfo.setLastUpdateBy(userId + "");
            custPubMidInfo.setLastUpdateDate(new Date());
            // 保存中间表信息
            customerPublicMidService.save(custPubMidInfo);

            //TODO 判断股东信息与关联企业
            /*if (billsPublic.getCompanyPartners() != null) {
                Set<CompanyPartner> companyPartners = billsPublic.getCompanyPartners();
                Set<CompanyPartnerMid> companyPartnerMids = new HashSet<CompanyPartnerMid>();
                StringBuilder idsBuilder = new StringBuilder();

                for (CompanyPartner cp : companyPartners) {
                    //判断股东信息是否为后增
                    if (cp.getId() != null && cp.getId() > 0) {
                        CompanyPartnerMidInfo comPartInfo = companyPartnerMidService
                                .findByPartnerIdAndRefBillId(cp.getId(), billsPublic.getId());
                        String[] ignoreProperties1 = {"id", "createdBy", "createdDate", "customerId",
                                "refBillId"};
                        BeanUtils.copyProperties(cp, comPartInfo, ignoreProperties1);
                        comPartInfo.setLastUpdateBy(userId);
                        comPartInfo.setLastUpdateDate(new Date());
                        CompanyPartnerMid companyPartnerMid = companyPartnerMidService.save(comPartInfo);
                        companyPartnerMids.add(companyPartnerMid);

                        idsBuilder.append("," + companyPartnerMid.getId());
                    } else {
                        CompanyPartnerMidInfo comPartInfo = new CompanyPartnerMidInfo();
                        BeanUtils.copyProperties(cp, comPartInfo);
                        comPartInfo.setCustomerId(custPublicMid.getCustomerId());
                        // 关联主对象
                        comPartInfo.setCustomerPublicMid(custPublicMid);
                        // 提前生成Id
                        Long id = getRecId();
                        comPartInfo.setId(id);
                        comPartInfo.setPartnerId(id);
                        comPartInfo.setCustomerId(custPublicMid.getCustomerId());
                        comPartInfo.setRefBillId(billsPublic.getRefBillId());
                        comPartInfo.setCreatedBy(userId);
                        comPartInfo.setCreatedDate(new Date());
                        CompanyPartnerMid companyPartnerMid = companyPartnerMidService.save(comPartInfo);
                        companyPartnerMids.add(companyPartnerMid);

                        idsBuilder.append("," + companyPartnerMid.getId());
                    }
                }
                //删除id未在传入数据范围内的记录
                String ids = idsBuilder.substring(1).toString();
                int count = companyPartnerMidService.deleteBatchByCustIdAndId(
                        custPublicMid.getCustomerId(), custPublicMid.getRefBillId(), ids);

                logger.info("删除客户:" + custPublicMid.getCustomerId() + "流水：" + custPublicMid.getRefBillId()
                        + "股东信息" + count + "笔记录");
                // 设置对象
                custPublicMid.setCompanyPartnersMid(companyPartnerMids);
            }

            if (billsPublic.getRelateCompanys() != null) {
                Set<RelateCompany> relateCompanys = billsPublic.getRelateCompanys();
                Set<RelateCompanyMid> relateCompanyMids = new HashSet<RelateCompanyMid>();
                StringBuilder idsBuilder = new StringBuilder();

                for (RelateCompany rc : relateCompanys) {
                    //判断关联企业信息是否为后增
                    if (rc.getId() != null && rc.getId() > 0) {
                        RelateCompanyMidInfo relateCompInfo = relateCompanyMidService
                                .findByRelateIdAndRefBillId(rc.getId(), billsPublic.getId());
                        String[] ignoreProperties1 = {"id", "createdBy", "createdDate", "customerId",
                                "refBillId"};
                        BeanUtils.copyProperties(rc, relateCompInfo, ignoreProperties1);
                        relateCompInfo.setLastUpdateBy(userId);
                        relateCompInfo.setLastUpdateDate(new Date());

                        RelateCompanyMid relateCompanyMid = relateCompanyMidService.save(relateCompInfo);
                        relateCompanyMids.add(relateCompanyMid);
                        idsBuilder.append("," + relateCompanyMid.getId());
                    } else {
                        RelateCompanyMidInfo relateCompInfo = new RelateCompanyMidInfo();
                        BeanUtils.copyProperties(rc, relateCompInfo);
                        relateCompInfo.setCustomerId(custPublicMid.getCustomerId());
                        // 关联主对象
                        relateCompInfo.setCustomerPublicMid(custPublicMid);
                        // 提前生成Id
                        Long id = getRecId();
                        relateCompInfo.setId(id);
                        relateCompInfo.setRelateId(id);
                        relateCompInfo.setCustomerId(custPublicMid.getCustomerId());
                        relateCompInfo.setRefBillId(billsPublic.getRefBillId());
                        relateCompInfo.setCreatedBy(userId);
                        relateCompInfo.setCreatedDate(new Date());
                        RelateCompanyMid relateCompanyMid = relateCompanyMidService.save(relateCompInfo);
                        relateCompanyMids.add(relateCompanyMid);
                        idsBuilder.append("," + relateCompanyMid.getId());
                    }
                }
                //删除id未在传入数据范围内的记录
                String ids = idsBuilder.substring(1).toString();
                int count = relateCompanyMidService.deleteBatchByCustIdAndId(custPublicMid.getCustomerId(),
                        custPublicMid.getRefBillId(), ids);

                logger.info("删除客户:" + custPublicMid.getCustomerId() + "流水：" + custPublicMid.getRefBillId()
                        + "关联企业" + count + "笔记录");

                custPublicMid.setRelateCompanysMid(relateCompanyMids);
            }*/

            return custPubMidInfo;
        } else {
            CustomerPublicMidInfo custPubMidInfo = new CustomerPublicMidInfo();
            // 新增记录
            BeanCopierUtils.copyProperties(customerBillsAllInfo, custPubMidInfo);
            custPubMidInfo.setCreatedBy(userId + "");
            custPubMidInfo.setCreatedDate(new Date());
            // 保存中间表信息
            customerPublicMidService.save(custPubMidInfo);

            //TODO 判断股东信息与关联企业
            /*if (billsPublic.getCompanyPartners() != null) {
                Set<CompanyPartner> companyPartners = billsPublic.getCompanyPartners();
                Set<CompanyPartnerMid> companyPartnerMids = new HashSet<CompanyPartnerMid>();

                for (CompanyPartner cp : companyPartners) {
                    CompanyPartnerMidInfo comPartInfo = new CompanyPartnerMidInfo();
                    BeanUtils.copyProperties(cp, comPartInfo);
                    comPartInfo.setCustomerId(custPublicMid.getCustomerId());
                    // 关联主对象
                    comPartInfo.setCustomerPublicMid(custPublicMid);
                    // 已存在的信息
                    if (cp.getId() != null) {
                        comPartInfo.setPartnerId(cp.getId());
                    } else {
                        // 提前生成Id
                        Long id = getRecId();
                        comPartInfo.setId(id);
                        comPartInfo.setPartnerId(id);
                    }

                    comPartInfo.setRefBillId(billsPublic.getRefBillId());
                    comPartInfo.setCreatedBy(userId);
                    comPartInfo.setCreatedDate(new Date());

                    CompanyPartnerMid companyPartnerMid = companyPartnerMidService.save(comPartInfo);
                    companyPartnerMids.add(companyPartnerMid);
                }

                // 设置对象
                custPublicMid.setCompanyPartnersMid(companyPartnerMids);
            }

            if (billsPublic.getRelateCompanys() != null) {
                Set<RelateCompany> relateCompanys = billsPublic.getRelateCompanys();
                Set<RelateCompanyMid> relateCompanyMids = new HashSet<RelateCompanyMid>();

                for (RelateCompany rc : relateCompanys) {
                    RelateCompanyMidInfo relateCompInfo = new RelateCompanyMidInfo();
                    BeanUtils.copyProperties(rc, relateCompInfo);

                    relateCompInfo.setCustomerId(custPublicMid.getCustomerId());
                    // 关联主对象
                    relateCompInfo.setCustomerPublicMid(custPublicMid);
                    // 已存在的信息
                    if (rc.getId() != null) {
                        relateCompInfo.setRelateId(rc.getId());
                    } else {
                        // 提前生成Id
                        Long id = getRecId();
                        relateCompInfo.setId(id);
                        relateCompInfo.setRelateId(id);
                    }

                    relateCompInfo.setRefBillId(billsPublic.getRefBillId());
                    relateCompInfo.setCreatedBy(userId);
                    relateCompInfo.setCreatedDate(new Date());

                    RelateCompanyMid relateCompanyMid = relateCompanyMidService.save(relateCompInfo);
                    relateCompanyMids.add(relateCompanyMid);
                }

                custPublicMid.setRelateCompanysMid(relateCompanyMids);
            }*/
            return custPubMidInfo;

        }

    }


    private void validateUnfinishedBill(Long customerId) {
        long unfinishedNum = customerBillsAllService.countUnfinishedByCustomerId(customerId);
        if (unfinishedNum > 0) {
            throw new RuntimeException("该账户存在未完成的操作流水！");
        }
    }
}
