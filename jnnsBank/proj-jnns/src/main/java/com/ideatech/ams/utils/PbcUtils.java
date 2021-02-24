package com.ideatech.ams.utils;

import com.ideatech.ams.account.dao.bill.AccountBillsAllDao;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.dao.AccountsBillsAllDao;
import com.ideatech.ams.dao.JnnsCorrectBillAllDao;
import com.ideatech.ams.dao.JnnsImageAllDao;
import com.ideatech.ams.dao.JnnsImageBillAllDao;
import com.ideatech.ams.domain.JnnsCorrectBillAll;
import com.ideatech.ams.domain.JnnsImageAll;
import com.ideatech.ams.domain.JnnsImageBillAll;
import com.ideatech.ams.domain.SyncCompare;
import com.ideatech.ams.dto.SyncCompareInfo;
import com.ideatech.ams.dto.gmsp.GMSP;
import com.ideatech.ams.service.SyncCompareService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public  class PbcUtils {

    @Autowired
    private  JnnsImageBillAllDao jnnsImageBillAllDao;

    @Autowired
    private  AccountsBillsAllDao accountsBillsAllDao;

    @Autowired
    private  JnnsImageAllDao jnnsImageAllDao;

    @Autowired
    private JnnsCorrectBillAllDao jnnsCorrectBillAllDao;

    @Autowired
    private SyncCompareService syncCompareService;

    @Autowired
    private OrganizationService organizationService;


    @Autowired
    private AllBillsPublicService allBillsPublicService;

    /**
     * 是否基于存量数据。否则变更、久悬、销户 对于无存量数据的情况下，直接新建客户账户信息
     */
    @Value("${ams.company.datenbestand:true}")
    private Boolean datenbestand;



    String format = "yyyy-MM-dd";
    public  void doAfterSync(GMSP billsPublics, BillType billType, SyncCompareInfo syncCompareInfo) {

        //报文柜面影像流水Id不为空，处理 业务影像表和 冲正记录表
        if (StringUtils.isNotEmpty(billsPublics.getJnBillId())) {
            AccountBillsAll accountBillsAll = null;
            //根据账号和账户类型查出最新的一笔流水
            if (billType == BillType.ACCT_OPEN) {
                accountBillsAll = accountsBillsAllDao.findTopByAcctNoAndBillTypeOrderByLastUpdateDateDesc(billsPublics.getAcctNo(), billType);
            } else {
                accountBillsAll = accountsBillsAllDao.findTopByAcctNoAndBillTypeOrderByLastUpdateDateDesc(billsPublics.getExChange() + billsPublics.getAcctNo(), billType);
                log.info("账号====" + billsPublics.getExChange() + billsPublics.getAcctNo());
            }
            log.info("账号====" + billsPublics.getExChange() + billsPublics.getAcctNo());
            if (accountBillsAll == null) {
                log.info("账号" + billsPublics.getAcctNo() + "报送后，对于业务影像及冲正记录进行处理时，从业务流水表查询信息为空");
                return;
            }
            try {
                //根据流水ID查询到对应的影像号
                JnnsImageBillAll jnnsImageBillAll = jnnsImageBillAllDao.findByJnBillId(billsPublics.getJnBillId());
                log.info("影像流水表存在信息：" + (jnnsImageBillAll == null ? "为空" : jnnsImageBillAll.getAcctNo()));
                if (jnnsImageBillAll != null) {
                    log.info("产品业务流水表存在信息：" + (accountBillsAll == null ? "为空" : accountBillsAll.getAcctNo()));
                    if (accountBillsAll != null) {
                        //更新影像流水表状态
                        jnnsImageBillAll.setBillType(billType);                   //操作类型
                        jnnsImageBillAll.setBillId(Long.toString(accountBillsAll.getId()));//流水表ID
                        jnnsImageBillAll.setOrganFullId(accountBillsAll.getOrganFullId());
                        jnnsImageBillAll.setOrganCode(billsPublics.getBankCode());
                        jnnsImageBillAll.setSaveImageDate(DateUtils.getNowDateShort(format));                  //操作时间
                        jnnsImageBillAllDao.save(jnnsImageBillAll);                         //流水表保存
                        //保存业务影像表信息
                        JnnsImageAll jnnsImageAll = new JnnsImageAll();
                        BeanCopierUtils.copyProperties(jnnsImageBillAll, jnnsImageAll);
                        jnnsImageAll.setId(null);                                           //置空主键ID
                        jnnsImageAll.setAcctName(accountBillsAll.getDepositorName());      //企业名称
                        jnnsImageAll.setAcctType(accountBillsAll.getAcctType());           //账户性质
                        jnnsImageAllDao.save(jnnsImageAll);                                 //影像主表保存
                    }
                }
            } catch (Exception e) {
                log.error("报送流程中，处理业务影像异常", e);
            }
            if (billType == BillType.ACCT_OPEN || billType == BillType.ACCT_REVOKE) {
                try {
                    //冲正流水表
                    JnnsCorrectBillAll jnnsCorrectBillAll = new JnnsCorrectBillAll();
                    //冲正流水实体类
                    if (billType == BillType.ACCT_OPEN) {
                        log.info("冲正开户账号获得=====" + billsPublics.getAcctNo());
                        jnnsCorrectBillAll.setAcctNo(billsPublics.getAcctNo());
                    } else {
                        jnnsCorrectBillAll.setAcctNo(billsPublics.getExChange() + billsPublics.getAcctNo());                 //账号

                    }
                    jnnsCorrectBillAll.setAcctName(billsPublics.getAcctName());             //账户名称
                    jnnsCorrectBillAll.setJnBillId(billsPublics.getJnBillId());             //行内流水号
                    jnnsCorrectBillAll.setAcctType(accountBillsAll.getAcctType());         //账户性质
                    jnnsCorrectBillAll.setBillType(billType);                              //业务类型
                    jnnsCorrectBillAll.setBillId(Long.toString(accountBillsAll.getId()));  //流水ID
                    jnnsCorrectBillAll.setOrganFullId(accountBillsAll.getOrganFullId());   //机构fullId
                    jnnsCorrectBillAll.setCorrectType("0");                                 //冲正类型
                    jnnsCorrectBillAll.setOrganCode(billsPublics.getBankCode());     //核心机构号
                    jnnsCorrectBillAllDao.save(jnnsCorrectBillAll);
                    log.info("冲正保存成功============================");
                } catch (Exception e) {
                    log.error("报送流程中，处理冲正记录异常", e);
                }
            }
            if (syncCompareInfo != null) {// 变更 当日开变销
                if (StringUtils.isNotEmpty(accountBillsAll.getPbcSyncStatus().toString())) {
                    syncCompareInfo.setPbcStarts(accountBillsAll.getPbcSyncStatus().toString());
                    log.info("变更人行状态" + accountBillsAll.getPbcSyncStatus().toString());
                }
                if (CompanyAcctType.jiben.equals(billsPublics.getAcctType())) {
                    syncCompareInfo.setEccsStarts(accountBillsAll.getEccsSyncStatus().toString());
                }
                log.info("当日开变销页面变更信息第二次保存");
                if (syncCompareInfo.getId() != null) {
                    syncCompareService.update(syncCompareInfo);
                } else {
                    syncCompareService.create(syncCompareInfo);
                }
            }
        }
    }


    /**
     * 是否基于基本户开户
     *
     * @param acctType
     * @return
     */
    private boolean isOpenByJiben(CompanyAcctType acctType) {
        if (acctType == null) {
            return true;
        } else if (acctType == CompanyAcctType.jiben) {
            return false;
        } else if (acctType == CompanyAcctType.linshi) {
            return false;
        } else if (acctType == CompanyAcctType.teshu) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 上级字段全部为空
     *
     * @param billsPublic
     */
    private void setParValueEmpty(AllBillsPublicDTO billsPublic) {
        //上级所有字段都不为空
        billsPublic.setParLegalName("");
        billsPublic.setParLegalIdcardType("");
        billsPublic.setParLegalType("");
        billsPublic.setParLegalIdcardNo("");
        billsPublic.setParAccountKey("");
        billsPublic.setParCorpName("");
        billsPublic.setParLegalTelephone("");
        billsPublic.setParOrgCode("");
        billsPublic.setParOrgEccsNo("");
        billsPublic.setParRegNo("");
        billsPublic.setParRegType("");
    }

    public void syncInit(GMSP billsPublics, AllBillsPublicDTO billsPublic, BillType billType, boolean amsSync, boolean eccsSync) {
        BeanCopierUtils.copyProperties(billsPublics, billsPublic); //对象copy

        //注册资金取出
        String reg = billsPublics.getRegisteredCapital();
        //两类转化
        if (StringUtils.isNotEmpty(reg)) {
            //加入注册资金
            billsPublic.setRegisteredCapital(new BigDecimal(reg));
        }
        billsPublic.setBillType(billType);                    //操作类型
        //机构信用代码上报状态 基本户进行上报，其他不上报
        if (billsPublic.getAcctType() != null && billsPublic.getAcctType() == CompanyAcctType.jiben && eccsSync) {
            billsPublic.setEccsSyncStatus(CompanySyncStatus.weiTongBu);
        } else {
            billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
        }
        if (amsSync) {
            billsPublic.setPbcSyncStatus(CompanySyncStatus.weiTongBu);      //人行上报状态
        } else {
            billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);      //人行上报状态
        }
        //设置信用代码证经营范围
        if (StringUtils.isNotBlank(billsPublic.getBusinessScope()) && billsPublic.getAcctType() != null && billsPublic.getAcctType() == CompanyAcctType.jiben) {
            billsPublic.setBusinessScopeEccs(billsPublic.getBusinessScope());
        }
        //开发环境由于核心日期不正确，默认设置当前时间 SIT或UAT注释掉
//        billsPublic.setAcctCreateDate(DateUtils.getNowDateShort("yyyyMMdd"));
        //基于基本户开户的账户性质（一般户、预算、非预算、非临时）时信息初始化
        if (isOpenByJiben(billsPublic.getAcctType())) {
            //证明文件编号，基于基本户开户的账户性质 证明文件种类和类型是 acctFileNo和acctFileType
            billsPublic.setAcctFileNo(billsPublic.getFileNo());
            billsPublic.setAcctFileType(billsPublic.getFileType());
            billsPublic.setAcctFileNo2(billsPublic.getFileNo2());
            billsPublic.setAcctFileType2(billsPublic.getFileType2());
            //默认为空
            billsPublic.setFileNo("");
            billsPublic.setFileType("");
            billsPublic.setFileType2("");
            billsPublic.setFileNo2("");
            //设置上级机构信息
            setParValueEmpty(billsPublic);
        }
        //基于基本户开户的账户性质，基本户注册地地区代码赋值
        if (isOpenByJiben(billsPublic.getAcctType())) {
            billsPublic.setBasicAcctRegArea(billsPublic.getRegAreaCode());
        }
        billsPublic.setBasicAcctRegArea(billsPublic.getRegAreaCode()); //注册区地区代码
    }




    /**
     * 变革处理当日开变销记录，变更记录
     *
     * @param billsPublics
     */
    public SyncCompareInfo processSyncCompanyInfo(GMSP billsPublics) {


        SyncCompareInfo syncCompareInfo = new SyncCompareInfo();
        //账号
        if (StringUtils.isNotEmpty(billsPublics.getAcctNo())){
            syncCompareInfo.setAcctNo(billsPublics.getAcctNo());
        }
        if (StringUtils.isNotEmpty(billsPublics.getDepositorName())) {
            syncCompareInfo.setDepositorName(billsPublics.getDepositorName());
        }
        if (StringUtils.isNotEmpty(billsPublics.getAcctName())) {
            syncCompareInfo.setDepositorName(billsPublics.getAcctName());
        }
        //根据柜面传过来的账户性质大类来保存账户性质。
        if (StringUtils.isNotBlank(billsPublics.getAcctBigType())) {
            if ("0001".equals(billsPublics.getAcctBigType())) {
                syncCompareInfo.setAcctType("jiben");
            }
            if ("0002".equals(billsPublics.getAcctBigType())) {
                syncCompareInfo.setAcctType("yiban");
            }
            if ("0003".equals(billsPublics.getAcctBigType())) {
                syncCompareInfo.setAcctType("linshi");
            }
            if ("0004".equals(billsPublics.getAcctBigType())) {
                syncCompareInfo.setAcctType("zhuanhu");
            }
        }
        //查找fullId
        syncCompareInfo.setOrganCode(billsPublics.getBankCode());
        if (billsPublics.getBankCode() != null) {
            OrganizationDto organizationDto = organizationService.findByCode(billsPublics.getBankCode());
            if (organizationDto != null) {
                syncCompareInfo.setOrganFullId(organizationDto.getFullId());
            } else {
                //未找到机构，则默认机构为总行归属账号
                syncCompareInfo.setOrganFullId("1");
            }
        }
        syncCompareInfo.setKaixhubz("03");

        syncCompareInfo.setBusinessDate(DateUtils.getNowDateShort("yyyyMMdd"));
        syncCompareInfo.setAcctOpenDate(DateUtils.getNowDateShort(format));
        log.info("查询前的实体类：" + syncCompareInfo.toString());
        SyncCompare syncCompare = syncCompareService.findTopByAcctNoAndKaixhubzAndBusinessDateOrderByLastUpdateDateDesc(syncCompareInfo.getAcctNo(), syncCompareInfo.getKaixhubz(), syncCompareInfo.getBusinessDate());
        if (syncCompare != null) {
            BeanCopierUtils.copyProperties(syncCompare, syncCompareInfo);
            syncCompareService.delete(syncCompare.getId());
        }
        syncCompareInfo.setPbcStarts("weiShangBao");
        syncCompareInfo.setEccsStarts("buTongBu");
        syncCompareService.create(syncCompareInfo);
        return syncCompareInfo;
    }




    /**
     * 获取变更字段所包含的需要重新开户的字段
     *
     * @param billsPublic
     * @return 所包含的需要重新开户的字段
     */
    private List<String> getneedNewOpenFieldList(AllBillsPublicDTO billsPublic) {
        List<String> needNewOpenFieldList = new ArrayList<String>();
        try {
            AllBillsPublicDTO originalBills;
            if (billsPublic.getBillType() == BillType.ACCT_CHANGE && datenbestand) {
                originalBills = allBillsPublicService.changeCompareWithOriginal(billsPublic);
                needNewOpenFieldList = allBillsPublicService.getNeedOpenFiledList(originalBills, billsPublic);
            }
        } catch (Exception e) {
            log.info("获取变更字段所包含的需要重新开户的字段方法异常", e);
        }
        return needNewOpenFieldList;
    }



}
