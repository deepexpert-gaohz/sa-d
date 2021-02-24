package com.ideatech.ams.risk.riskdata.service;

import com.google.common.collect.Sets;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.service.ams.AmsSearchService;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.risk.Constant.OracleSQLConstant;
import com.ideatech.ams.risk.highRisk.entity.HighRiskData;
import com.ideatech.ams.risk.model.dto.ModelDto;
import com.ideatech.ams.risk.model.service.ModelService;
import com.ideatech.ams.risk.procedure.service.ProcService;
import com.ideatech.ams.risk.riskdata.dao.RiskApiDao;
import com.ideatech.ams.risk.riskdata.dao.RiskCheckInfoDao;
import com.ideatech.ams.risk.riskdata.dto.RiskDataInfoDto;
import com.ideatech.ams.risk.riskdata.dto.RiskRecordInfoDto;
import com.ideatech.ams.risk.riskdata.entity.RiskRecordInfo;
import com.ideatech.ams.risk.rule.dto.RuleConfigureDto;
import com.ideatech.ams.risk.rule.service.RuleConfigService;
import com.ideatech.ams.risk.util.RiskUtil;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.util.FileUtils;
import com.ideatech.common.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 开户风险处理流程
 *
 * @author yangcq
 * @date 2019-06-12
 * @address wulmq
 */
@Service

public class RiskApiServiceImpl implements RiskApiService {
    private static final Logger log = LoggerFactory.getLogger(RiskApiServiceImpl.class);
    @Autowired
    private RiskApiDao riskApiDao;

    @Autowired
    private AmsSearchService amsSearchService;

    @Autowired

    private EntityManager entityManager;

    @Autowired
    private ModelService modelService;

    @Autowired
    private RuleConfigService ruleConfigService;


    @Autowired
    private AmsMainService amsMainService;

    @Autowired
    private ProcService procService;

    @Autowired
    private PlatformTransactionManager transactionManager;


    @Autowired
    private RiskCheckInfoDao riskDzDao;


    /**
     * 跑批生成 双异地（注册地、经营地）单位开立银行账户 风险数据
     *
     * @param riskData
     * @author yangcq
     * @2019-05-30
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void generateRisk2003Data(String riskData, Boolean isHighRsikApi) {
        TransactionDefinition definition = new DefaultTransactionDefinition(
                TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        log.info("批量执行RISK_2003开始");
        String riskId = "RISK_2003";
        //获取相关账户客户信息包括（办公地址代码、注册地址代码和人行统一机构代码）
        ModelDto moedl2003 = modelService.findByModelId("RISK_2003");
        try {
            //moedl2003.getStatus 为1时代表该模型已经启用,否则为停用
            if (moedl2003 != null && moedl2003.getStatus().equals("1")) {
                List<Object[]> list = this.getCustomersInfo();
                int processNum = 0;
                if (CollectionUtils.isNotEmpty(list)) {
                    for (Object[] obj : list) {
                        processNum++;
                        String organCode = obj[5] == null ? "" : obj[5].toString();
                        if (organCode != "") {
                            organCode = organCode.substring(3, organCode.length() - 3);
                        }
                        String[] orgCode = new String[2];
                        orgCode[0] = obj[2] == null ? "" : obj[2].toString();
                        orgCode[1] = organCode;
                        String workCode = (obj[3] == null) ? "" : obj[3].toString();//注册地址代码
                        String regAreaCode = (obj[4] == null) ? "" : obj[4].toString();//注册地址代码
                        String customerId = (obj[0] == null) ? "" : obj[0].toString();//客户号
                        String accountNo = (obj[1] == null) ? "" : obj[1].toString();//账号
                        String accountName = (obj[6] == null) ? "" : obj[6].toString();//账号

                        //20201107 潘修改
                        String organcode = (obj[7] == null) ? "" : obj[7].toString();//机构号
                        /*经营地址代码和开户机构地区代码不同，同时注册地区代码与开户机构地区代码不同
                        如果两个条件同时成立，则为风险案例。则将风险数据存入YD_RISK_RECORD_INFO(风险信息表)，
                        账户系统用户开户时将查询这张表
                         */
                        //注册地区代码或办公主注册地区代码为空则不保存
                        if (StringUtils.isBlank(workCode) || StringUtils.isBlank(regAreaCode)) {
                            continue;
                        }
                        //比对地址并且保存
                        compareAddressAndSave(riskId, orgCode, workCode, regAreaCode, customerId, accountNo, isHighRsikApi, accountName,organcode);
                        if (processNum % 100 == 0) {
                            log.info("批量执行模型【RISK_2003】，总数一共：" + list.size() + ",已跑批" + processNum + "条数据");
                            transactionManager.commit(transaction);
                            transaction = transactionManager.getTransaction(definition);
                        }
                    }
                    transactionManager.commit(transaction);
                }
            }
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            log.error("批量执行RISK_2003时异常", e);
        }
        log.info("批量执行RISK_2003结束");
    }

    /**
     * 跑批生成 多个主体的法定代表人、财务负责人、财务经办人员预留同一个联系方式等 风险数据
     *
     * @param riskData
     * @author yangcq
     * @2019-05-30
     */
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Transactional
    public List<RiskRecordInfo> generateRisk2007Data(String riskData, Boolean isHighRsikApi) {
//        TransactionDefinition definition = new DefaultTransactionDefinition(
//                TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//        TransactionStatus transaction = transactionManager.getTransaction(definition);
        log.info("批量执行RISK_2007开始");
        ExecutorService riskExecPool = Executors.newFixedThreadPool(5);//线程数
        List<RiskRecordInfo> infoDtoList = new ArrayList<>();
        //获取账管系统中所有对公客户的财务经办人电话、法定代表人和财务经办人电话
        try {
            List<RiskDataInfoDto> telList = this.getAllCustomersTel();
            List<List<RiskDataInfoDto>> averageList = FileUtils.averageList(telList, 1);
            HashMap<String, List<RiskRecordInfoDto>> mp = new HashMap();
            ModelDto moedl2007 = modelService.findByModelId("RISK_2007");
            //moedl2007.getStatus 为1时代表该模型已经启用,否则为停用
            if (moedl2007 != null && moedl2007.getStatus().equals("1")) {
                for (List<RiskDataInfoDto> mList : averageList) {
                    int j = 0;
                    Thread thread = new Thread(new ExecutRiskDataServiceImpl(telList, mList, mp, j));
                    riskExecPool.execute(thread);
                }
                riskExecPool.shutdown();
                boolean isTreadClose = true;
                while (isTreadClose) {
                    Thread.sleep(10000);
                    if (riskExecPool.isTerminated()) {
                        log.info("-------------风险模型Risk2007程已经结束---------");
                        isTreadClose = false;
                    } else {
                        log.info("--------风险模型Risk2007线程还未结束-----------------");
                    }
                }
                if (mp != null && mp.size() > 0) {
                    Iterator<Map.Entry<String, List<RiskRecordInfoDto>>> it = mp.entrySet().iterator();
                    int processNum = 0;
                    while (it.hasNext()) {
                        Map.Entry<String, List<RiskRecordInfoDto>> itMap = it.next();
                        for (RiskRecordInfoDto rto : itMap.getValue()) {
                            processNum++;
                            this.saveRiskRecodInfo(rto);
                            if (processNum % 100 == 0) {
//                                transactionManager.commit(transaction);
//                                transaction = transactionManager.getTransaction(definition);
                                log.info("批量执行模型【RISK_2007】，总数一.共：" + telList.size() + ",已跑批" + processNum + "条数据");
                            }
                        }
                    }
//                    transactionManager.commit(transaction);
                }
                log.info("----------------结束-------------");
            }
        } catch (InterruptedException e) {
//            transactionManager.rollback(transaction);
            log.error("批量执行RISK_2007时异常", e);
        }
        log.info("批量执行RISK_2007结束");
        return infoDtoList;
    }


    /**
     * 获取账户的经营地址代码、开户机构地区代码和注册地址代码,只判断基本户
     *
     * @return
     * @author :yangcq
     * @date :2019-05-30
     */
    public List<Object[]> getCustomersInfo() {
        String sql = OracleSQLConstant.getCustomersInfoSql;
        //String sql = MysqlSQLConstant.getCustomersInfoSql;
        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = nativeQuery.getResultList();
        return resultList;
    }

    /**
     * @param riskRecordInfoDto
     * @yangcq
     * @date 2019-05-30
     * @desc 将风险数据 插入表中
     */
    @Transactional
    public void saveRiskRecodInfo(RiskRecordInfoDto riskRecordInfoDto) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        riskRecordInfoDto.setRiskDate(formatter.format(new Date()));
        RiskRecordInfo info = new RiskRecordInfo();
        if (null != riskRecordInfoDto.getId()) {
            if (info == null) {
                info = new RiskRecordInfo();
            }
        }
        ConverterService.convert(riskRecordInfoDto, info);
        info.setId(null);
        riskApiDao.save(info);
    }

    /**
     * 更新开户监测流程状态
     *
     * @param riskRecordInfoDto
     * @author yangcq
     * @2019-05-31
     */
    @Transactional
    public void save(RiskRecordInfoDto riskRecordInfoDto) {
        RiskRecordInfo info = new RiskRecordInfo();
        if (null != riskRecordInfoDto.getId()) {
            info = riskApiDao.findOne(riskRecordInfoDto.getId());
            if (info == null) {
                info = new RiskRecordInfo();
            }
        }
        info.setStatus(riskRecordInfoDto.getStatus());
        //  info.setLastUpdateDate(new Date());
        riskApiDao.save(info);
    }

    @Override
    public RiskRecordInfoDto findById(String countId) {
        RiskRecordInfoDto riskRecordInfoDto = new RiskRecordInfoDto();
        Long id = Long.parseLong(countId);
        RiskRecordInfo info = riskApiDao.findOne(id);
        ConverterService.convert(info, riskRecordInfoDto);
        return riskRecordInfoDto;
    }


    /**
     * 根据规则将风险数据筛查出来，并且组装成必要数据格式通过接口发送给柜面等其他系统
     * 开户实时风险逻辑
     *
     * @param billsPublic
     * @author yangcq
     * @date 2019-06-14
     */
    @Override
    public String syncAccountInfo(AllBillsPublicDTO billsPublic) {
        StringBuffer bf = new StringBuffer();
        log.info("开始对开户进行实时分析判断");
        try {
            //从本地获取该账户的统计信息,
            String customerNo = billsPublic.getCustomerNo();//客户号
            String accountNo = billsPublic.getAcctNo();//账户号
            String bankCode = billsPublic.getBankCode();
            //从账管本地表中获取信息
            List<RiskDataInfoDto> dtoList = this.getAccountInfoFromAms(customerNo, accountNo, bankCode);
            //获取RISK_2001模型信息
            log.info("获取RISK_2001模型信息");
            //ModelDto moedl2001 = modelService.findByModelId("RISK_2001");
            ModelDto moedl2001 = modelService.findByModelIdForSechdule("RISK_2001", bankCode);
            //获取所有规则信息
            log.info("获取所有规则信息");
            List<RuleConfigureDto> RuleList = ruleConfigService.getModelRuleByModelId("RISK_2001", bankCode);
            //循环该账户的预警前的一些信息
            String risk2001Csbf = "";
            int risk2001Index = 0;
            StringBuffer risk2001CustNo = new StringBuffer();
            for (RiskDataInfoDto riskDataInfoDto : dtoList) {
                RiskRecordInfoDto dto = new RiskRecordInfoDto();
                //获取规则中的设定的次数
                for (RuleConfigureDto ruledto : RuleList) {
                    /*(基本户)开户人频繁开户、销户或变更账户信息 逻辑处理 begin*/
                    if (billsPublic.getBillType() != null) {
                        if (billsPublic.getBillType().getValue().equals("变更") || billsPublic.getBillType().getValue().equals("新开户") ||
                                billsPublic.getBillType().getValue().equals("销户")) {
                            //moedl2002.getStatus 为1时代表该模型已经启用
                            if (riskDataInfoDto.getRiskId().equals("RISK_2001") && ruledto.getField().equals("rn") && moedl2001.getStatus().equals("1")) {
                                boolean flag = this.createRuleCoditionVal(ruledto, riskDataInfoDto, billsPublic);
                                if (flag) {
                                    dto.setCustomerId(billsPublic.getCustomerNo());//客户号
                                    risk2001Index++;
                                    risk2001Csbf = ruledto.getValue();
                                }
                            }
                        }
                    }
                }
            }
            if (risk2001Index != 0) {
                String risk2001Desc = "同一单位客户(" + billsPublic.getCustomerNo() + ")在天内发生" + risk2001Csbf + "次以上的开户、销户或变更信息操作！";
                bf.append(risk2001Desc + ",");
            }
            /*（所有户）开户人频繁开户、销户或变更账户信息 逻辑处理 end*/
            //==========================================================================================================
            List<RiskDataInfoDto> dtoListSec = this.getAccountInfoFromAms("", "", bankCode);//从账管本地表中获取信息
            String risk2002Csbf = "";
            StringBuffer risk2002CustNo = new StringBuffer();
            String risk2005Csbf = "";
            StringBuffer risk2005CustNo = new StringBuffer();
            String risk2006Csbf = "";
            int risk2005Index = 1, risk2006Index = 1;
            //
            log.info("获取模型信息");
            ModelDto moedl2002 = modelService.findByModelIdForSechdule("RISK_2002", bankCode);
            ModelDto moedl2005 = modelService.findByModelIdForSechdule("RISK_2005", bankCode);
            ModelDto moedl2006 = modelService.findByModelIdForSechdule("RISK_2006", bankCode);

            //获取模型规则信息
            List<RuleConfigureDto> RuleRisk2002List = ruleConfigService.getModelRuleByModelId("RISK_2002", bankCode);
            List<RuleConfigureDto> RuleRisk2005List = ruleConfigService.getModelRuleByModelId("RISK_2005", bankCode);
            List<RuleConfigureDto> RuleRisk2006List = ruleConfigService.getModelRuleByModelId("RISK_2006", bankCode);
            log.info("单位同一注册地址被多次使用 逻辑处理");
            log.info("同一法定代表人或负责人开立多个账户");
            log.info("同一经办人员代理多个开立账户");
            for (RiskDataInfoDto riskDataInfoDto : dtoListSec) {
                for (RuleConfigureDto ruledto : RuleRisk2002List) {
                    /*单位同一注册地址被多次使用 逻辑处理 begin*/
                    //moedl2002.getStatus 为1时代表该模型已经启用
                    if (riskDataInfoDto.getRiskId().equals("RISK_2002") && ruledto.getField().equals("rn") && moedl2002.getStatus().equals("1")) {
                        boolean flag = this.createRuleCoditionVal(ruledto, riskDataInfoDto, billsPublic);
                        if (flag) {
                            risk2002Csbf = ruledto.getValue();
                            risk2002CustNo.append(riskDataInfoDto.getCustomerNo() + ",");
                        }
                    }
                }

                for (RuleConfigureDto ruledto : RuleRisk2005List) {
                    /*单位同一注册地址被多次使用 逻辑处理 end*/
                    /*同一法定代表人或负责人开立多个账户 begin*/
                    if (riskDataInfoDto.getRiskId().equals("RISK_2005") && ruledto.getField().equals("rn") && moedl2005.getStatus().equals("1")) {
                        boolean flag = this.createRuleCoditionVal(ruledto, riskDataInfoDto, billsPublic);
                        if (flag) {
                            risk2005Index++;
                            risk2005Csbf = ruledto.getValue();
                        }
                    }
                }

                for (RuleConfigureDto ruledto : RuleRisk2006List) {
                    /*同一法定代表人或负责人开立多个账户 end*/
                    /*同一经办人员代理多个开立账户 begin*/
                    if (riskDataInfoDto.getRiskId().equals("RISK_2006") && ruledto.getField().equals("rn") && moedl2006.getStatus().equals("1")) {
                        boolean flag = this.createRuleCoditionVal(ruledto, riskDataInfoDto, billsPublic);
                        if (flag) {
                            risk2006Csbf = ruledto.getValue();
                            risk2006Index++;
                        }
                    }
                    /*同一经办人员代理多个开立账户 end*/
                }
            }
            if (StringUtils.isNotBlank(risk2002Csbf)) {
                String risk2002Desc = "多(" + risk2002Csbf + ")个单位客户(" + risk2002CustNo.toString() + ")预留同一注册地址!";
                bf.append(risk2002Desc + ",");
            }
            if (risk2005Index != 1) {
                String risk2005Desc = "同一法人或负责人(" + billsPublic.getLegalIdcardNo() + ")已经开立了多(" + risk2005Index + ")个账户！";
                bf.append(risk2005Desc + ",");
            }
            if (risk2006Index != 1) {
                String risk2006Desc = "同一经办人员(" + billsPublic.getOperatorIdcardNo() + ")已经代理了多(" + risk2006Index + ")个账户！";
                bf.append(risk2006Desc + ",");
            }
            /*多个主体中法定代表人、财务负责人、财务经办人预留了同一个联系号码 begin*/
            /*判断非空*/
            String legTel = StringUtil.nullToString(billsPublic.getLegalTelephone());//法定代表人电话
            String operTel = StringUtil.nullToString(billsPublic.getOperatorTelephone());//财务经办人电话
            String finTel = StringUtil.nullToString(billsPublic.getFinanceTelephone());//财务主管电话
            //获取账管系统中所有对公客户的财务经办人电话、法定代表人和财务经办人电话
            log.info("多个主体中法定代表人、财务负责人、财务经办人预留了同一个联系号码");
            List<RiskDataInfoDto> telList = this.getAllCustomersTel();
            RiskRecordInfoDto dtoRisk2007 = new RiskRecordInfoDto();
            StringBuffer telSb = new StringBuffer();
            String tel = "";
            ModelDto moedl2007 = modelService.findByModelIdForSechdule("RISK_2007", bankCode);

            if (moedl2007.getStatus().equals("1")) {
                if (!("").equals(finTel) || !("").equals(legTel) || !("").equals(operTel)) {
                    for (RiskDataInfoDto dto : telList) {
                        dtoRisk2007.setCustomerId(dto.getCustomerNo());//账号
                        dtoRisk2007.setRiskId("RISK_2007");
                        if (dto != null) {
                            String finTel2007 = dto.getFinTel() == null ? "" : dto.getFinTel();
                            String legTel2007 = dto.getLegTel() == null ? "" : dto.getLegTel();
                            String operTel2007 = dto.getOperTel() == null ? "" : dto.getOperTel();
                            //如果都不预留电话，则跳过
                            if (("").equals(finTel2007) && ("").equals(legTel2007) && ("").equals(operTel2007)) {
                                continue;
                            }
                            if ((finTel.equals(finTel2007) && !finTel.equals("") && !finTel2007.equals("")) || (finTel.equals(legTel2007) && !finTel.equals("") && !legTel2007.equals("")) || (finTel.equals(operTel2007) && !finTel.equals("") && !operTel2007.equals(""))) {
                                telSb.append(dto.getCustomerNo() + ",");
                                tel = finTel;
                                continue;
                            } else if ((legTel.equals(finTel2007) && !legTel.equals("") && !finTel2007.equals("")) || (legTel.equals(legTel2007) && !legTel.equals("") && !legTel2007.equals("")) || (legTel.equals(operTel2007) && !legTel.equals("") && !operTel2007.equals(""))) {
                            /*String risk2007Desc ="多个主体的法定代表人、财务负责人、财务经办人员预留同一个联系方式("+legTel+")";
                            dtoRisk2007.setRiskDesc ( risk2007Desc );*/
                                telSb.append(dto.getCustomerNo() + ",");
                                tel = legTel;
                                continue;
                            } else if ((operTel.equals(finTel2007) && !operTel.equals("") && !finTel2007.equals("")) || (operTel.equals(legTel2007) && !operTel.equals("") && !legTel2007.equals("")) || (operTel.equals(operTel2007) && !operTel.equals("") && !operTel2007.equals(""))) {
                          /*  String risk2007Desc ="多个主体的法定代表人、财务负责人、财务经办人员预留同一个联系方式("+operTel+")!";
                            dtoRisk2007.setRiskDesc ( risk2007Desc );*/
                                telSb.append(dto.getCustomerNo() + ",");
                                tel = operTel;
                                continue;
                            }
                        }
                    }
                    if (!telSb.toString().equals("")) {
                        //将字符串数组转化为List集合
                        String[] arr = telSb.toString().split(",");
                        Set<String> set = Sets.newHashSet(Arrays.asList(arr));
                        Iterator<String> it = set.iterator();
                        StringBuffer telSbsBuff = new StringBuffer();
                        while (it.hasNext()) {
                            String telSbs = it.next();
                            telSbsBuff.append(telSbs + ",");
                        }
                        String risk2007Desc = "多个主体(" + telSbsBuff.toString() + ")的法定代表人、财务负责人、财务经办人员预留同一个联系方式(" + tel + ")";
                        bf.append(risk2007Desc);
                    }
                }
            }
            /*多个主体中法定代表人、财务负责人、财务经办人预留了同一个联系号码 end*/
            /*同一主体中法定代表人、财务负责人、财务经办人预留了同一个联系号码 begin*/
            log.info("同一主体中法定代表人、财务负责人、财务经办人预留了同一个联系号码 ");
            RiskRecordInfoDto dtoRisk2008 = new RiskRecordInfoDto();
            ModelDto moedl2008 = modelService.findByModelIdForSechdule("RISK_2008", bankCode);

            if (moedl2008.getStatus().equals("1")) {
                if (!finTel.equals("") && !legTel.equals("") && !operTel.equals("") && finTel.equals(legTel) && legTel.equals(operTel)) {
                    String risk2008Desc = "同一客户(" + billsPublic.getCustomerNo() + ")中法定代表人、财务负责人、财务经办人预留了同一个联系号码" + operTel;
                    dtoRisk2008.setRiskDesc(risk2008Desc);
                    bf.append(risk2008Desc + ",");
                }
            }
        } catch (Exception e) {
            log.info("实时开户风险监测接口报错！");
        } finally {
            //注册地址与开户行机构地址不同
            //获取人行IP、userId和password
            try {
                if (StringUtils.isNotBlank(billsPublic.getBankCode())) {
                    PbcUserAccount pbcUserAccount;
                    String accountKey = billsPublic.getAccountKey();//开户银行金融机构编码
                    pbcUserAccount = this.getPBCAddressInfo(billsPublic.getBankCode());
                    LoginAuth auth = amsMainService.amsLogin(pbcUserAccount);
                    String orgCode[] = amsSearchService.getBankAreCode(auth, accountKey);
                    log.info("开户机构地区代码:" + orgCode[0] + "------" + orgCode[1]);
                    String NewOrgCode = "";
                    if (StringUtils.isNotBlank(orgCode[1]) && orgCode[1].length() == 4) {
                        NewOrgCode = orgCode[1] + "00";
                    }
                    if (StringUtils.isNotBlank(orgCode[1]) && StringUtils.isNotBlank(billsPublic.getRegAreaCode()) && billsPublic.getRegAreaCode().length() == 6
                            && billsPublic.getRegAreaCode().equals(NewOrgCode)) {
                        bf.append("注册地址与开户行机构地址不同");
                    }
                }
            } catch (Exception e) {
                log.info("登录人行获取开户行地址失败！");
            } finally {
                log.info("结束对开户进行实时分析判断");
                log.info(bf.toString());
                return bf.toString();
            }
        }
    }

    /**
     * 通过人行机构号获取人行系统登录信息
     *
     * @author yangcq
     * @date 20190618
     */
    private PbcUserAccount getPBCAddressInfo(String organCode) {
        PbcUserAccount pbcUserAccount = new PbcUserAccount();
        String sql = OracleSQLConstant.getPBCAddressInfoSql + organCode + OracleSQLConstant.getPBCAddressInfoEndSql;
        //String sql = MysqlSQLConstant.getPBCAddressInfoSql + organCode + MysqlSQLConstant.getPBCAddressInfoEndSql;
        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = nativeQuery.getResultList();
        for (Object[] obj : resultList) {
            pbcUserAccount.setLoginIp(obj[0] == null ? "" : obj[0].toString());
            pbcUserAccount.setLoginUserName(obj[1] == null ? "" : obj[1].toString());
            pbcUserAccount.setLoginPassWord(obj[2] == null ? "" : obj[2].toString());
        }
        return pbcUserAccount;
    }

    /**
     * 获取所有基本户主体客户的法人电话、财务负责人电话和经办人电话
     *
     * @author yangcq
     * @date 2019-06-15
     */
    private List<RiskDataInfoDto> getAllCustomersTel() {
        List<RiskDataInfoDto> list = new ArrayList<>();
        //String sql = MysqlSQLConstant.getAllCustomersTelSql;
        String sql = OracleSQLConstant.getAllCustomersTelSql;
        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = nativeQuery.getResultList();
        for (Object[] obj : resultList) {
            RiskDataInfoDto riskDataDto = new RiskDataInfoDto();
            riskDataDto.setFinTel(obj[0] == null ? "" : obj[0].toString());
            riskDataDto.setLegTel(obj[1] == null ? "" : obj[1].toString());
            riskDataDto.setOperTel(obj[2] == null ? "" : obj[2].toString());
            riskDataDto.setCustomerNo(obj[3] == null ? "" : obj[3].toString());
            riskDataDto.setAccountNo(obj[4] == null ? "" : obj[4].toString());
            riskDataDto.setAccountName(obj[5] == null ? "" : obj[5].toString());
            riskDataDto.setOrganCode(obj[6] == null ? "" : obj[6].toString());
            list.add(riskDataDto);
        }
        return list;
    }

    /**
     * 比较双异地,然后将数据保存到数据库
     *
     * @param riskId
     * @param orgCode
     * @param workCode
     * @param regAreaCode
     * @param customerId
     * @param accountNo
     * @author yangcq
     * @date 20190614
     */
    @Transactional
    protected void compareAddressAndSave(String riskId, String[] orgCode, String workCode, String regAreaCode, String customerId, String accountNo, boolean flag, String accountName,String ograncode) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String currentDate = formatter.format(new Date());
        RiskRecordInfoDto riskRecordInfoDto = new RiskRecordInfoDto();
        if (!orgCode[1].substring(0, 4).equalsIgnoreCase(workCode.substring(0, 4)) && !orgCode[1].substring(0, 4).equalsIgnoreCase(regAreaCode.substring(0, 4))) {
            riskRecordInfoDto.setAccountNo(accountNo);
            riskRecordInfoDto.setCustomerId(customerId);
            riskRecordInfoDto.setRiskCnt("");
            riskRecordInfoDto.setRiskDate(currentDate);
            riskRecordInfoDto.setRiskType("");
            riskRecordInfoDto.setDeleted(false);
            riskRecordInfoDto.setRiskPoint(customerId);
            riskRecordInfoDto.setRiskId(riskId);
            riskRecordInfoDto.setStatus("0");//处理状态
            riskRecordInfoDto.setAccountName(accountName);
            riskRecordInfoDto.setOrganCode(ograncode);
            //riskRecordInfoDto.setCorporateBank ( code );
            String riskDesc = "同一单位客户(" + customerId + ")经营地址和开户机构地区不同，同时注册地区与开户机构地区不同！";
            riskRecordInfoDto.setRiskDesc(riskDesc);
            if (!flag) {
                this.saveRiskRecodInfo(riskRecordInfoDto);
            }
        }
    }

    /**
     * 组装规则条件，根据规则中配置的条件进行判断大小
     *
     * @param ruleConfigureDto
     * @author yangcq
     * @date 2019-06-14
     */
    private boolean createRuleCoditionVal(RuleConfigureDto ruleConfigureDto, RiskDataInfoDto riskDataInfoDto, AllBillsPublicDTO billsPublic) {
        String riskCnt = riskDataInfoDto.getRiskCnt();//当前该账户的开变更销次数
        String ruleCnt = ruleConfigureDto.getValue();//规则中规定的开变销次数
        boolean flag = false;//判断是否为预警信息
        int riskCntI = 0, ruleCntI = 0;
        //判断非空
        if (StringUtils.isNotBlank(riskCnt) && StringUtils.isNotBlank(ruleCnt)) {
            riskCntI = Integer.parseInt(riskCnt);
            ruleCntI = Integer.parseInt(ruleCnt);
        }
        switch (ruleConfigureDto.getCondition()) {
            case "":
                break;
            case ">":
                if (riskDataInfoDto.getRiskId().equals("RISK_2001")) {
                    if (riskCntI + 1 > ruleCntI) {
                        flag = true;//代表该账户或客户为有风险存在；
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2002")) {
                    //去掉特殊字符只保留英文 汉字和数字
                    String address[] = this.convertAddress(riskDataInfoDto, billsPublic);
                    if (address[0].equals(address[1])) {//比较注册地址是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 > ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2005")) {
                    if (riskDataInfoDto.getRiskDesc().equals(billsPublic.getLegalIdcardNo())) {//比较法人证件号是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 > ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2006")) {
                    if (riskDataInfoDto.getRiskDesc().equals(billsPublic.getOperatorIdcardNo())) {//比较经办人证件号是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 > ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                }
                break;
            case "<":
                if (riskDataInfoDto.getRiskId().equals("RISK_2001")) {
                    if (riskCntI + 1 < ruleCntI) {
                        flag = true;//代表该账户或客户为有风险存在；
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2002")) {
                    //去掉特殊字符只保留英文 汉字和数字
                    String address[] = this.convertAddress(riskDataInfoDto, billsPublic);
                    if (address[0].equals(address[1])) {//比较注册地址是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 < ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2005")) {
                    if (riskDataInfoDto.getRiskDesc().equals(billsPublic.getLegalIdcardNo())) {//比较法人证件号是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 < ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2006")) {
                    if (riskDataInfoDto.getRiskDesc().equals(billsPublic.getOperatorIdcardNo())) {//比较经办人证件号是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 < ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                }
                break;
            case "=":
                if (riskDataInfoDto.getRiskId().equals("RISK_2001")) {
                    if (riskCntI + 1 == ruleCntI) {
                        flag = true;//代表该账户或客户为有风险存在；
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2002")) {
                    //去掉特殊字符只保留英文 汉字和数字
                    String address[] = this.convertAddress(riskDataInfoDto, billsPublic);
                    if (address[0].equals(address[1])) {//比较注册地址是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 == ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2005")) {
                    if (riskDataInfoDto.getRiskDesc().equals(billsPublic.getLegalIdcardNo())) {//比较法人证件号是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 == ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2006")) {
                    if (riskDataInfoDto.getRiskDesc().equals(billsPublic.getOperatorIdcardNo())) {//比较经办人证件号是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 == ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                }
                break;
            case ">=":
                if (riskDataInfoDto.getRiskId().equals("RISK_2001")) {
                    if (riskCntI + 1 >= ruleCntI) {
                        flag = true;//代表该账户或客户为有风险存在；
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2002")) {
                    //去掉特殊字符只保留英文 汉字和数字
                    String address[] = this.convertAddress(riskDataInfoDto, billsPublic);
                    if (address[0].equals(address[1])) {//比较注册地址是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 >= ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2005")) {
                    if (riskDataInfoDto.getRiskDesc().equals(billsPublic.getLegalIdcardNo())) {//比较法人证件号是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 >= ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2006")) {
                    if (riskDataInfoDto.getRiskDesc().equals(billsPublic.getOperatorIdcardNo())) {//比较经办人证件号是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 >= ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                }
                break;
            case "<=":
                if (riskDataInfoDto.getRiskId().equals("RISK_2001")) {
                    if (riskCntI + 1 <= ruleCntI) {
                        flag = true;//代表该账户或客户为有风险存在；
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2002")) {
                    //去掉特殊字符只保留英文 汉字和数字
                    String address[] = this.convertAddress(riskDataInfoDto, billsPublic);
                    if (address[0].equals(address[1])) {//比较注册地址是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 <= ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2005")) {
                    if (riskDataInfoDto.getRiskDesc().equals(billsPublic.getLegalIdcardNo())) {//比较法人证件号是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 <= ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2006")) {
                    if (riskDataInfoDto.getRiskDesc().equals(billsPublic.getOperatorIdcardNo())) {//比较经办人证件号是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 <= ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                }
                break;
            case "!=":
                if (riskDataInfoDto.getRiskId().equals("RISK_2001")) {
                    if (riskCntI + 1 != ruleCntI) {
                        flag = true;//代表该账户或客户为有风险存在；
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2002")) {
                    //去掉特殊字符只保留英文 汉字和数字
                    String address[] = this.convertAddress(riskDataInfoDto, billsPublic);
                    if (address[0].equals(address[1])) {//比较注册地址是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 != ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2005")) {
                    if (riskDataInfoDto.getRiskDesc().equals(billsPublic.getLegalIdcardNo())) {//比较法人证件号是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 != ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                } else if (riskDataInfoDto.getRiskId().equals("RISK_2006")) {
                    if (riskDataInfoDto.getRiskDesc().equals(billsPublic.getOperatorIdcardNo())) {//比较经办人证件号是否相同，如果相同则跟规则值比较
                        if (riskCntI + 1 != ruleCntI) {
                            flag = true;//代表该账户或客户为有风险存在；
                        }
                    }
                }
                break;
            default:
                break;
        }
        return flag;
    }

    /**
     * 去掉特殊字符只保留英文 汉字和数字
     *
     * @param
     * @param
     * @return
     */
    public String[] convertAddress(RiskDataInfoDto riskDataInfoDto, AllBillsPublicDTO billsPublic) {
        String address[] = new String[2];
        String riskDesc = "";
        if (StringUtils.isNotBlank(riskDataInfoDto.getRiskDesc())) {
            riskDesc = riskDataInfoDto.getRiskDesc().replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "").replaceAll("[\\s*|\t|\r|\n]", "");  // 去除所有空格，制表符
            address[0] = riskDesc;
        } else {
            address[0] = "1";
        }
        String regAddress = "";
        if (StringUtils.isNotBlank(billsPublic.getRegAddress())) {
            regAddress = billsPublic.getRegAddress().replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "").replaceAll("[\\s*|\t|\r|\n]", "");  // 去除所有空格，制表符
            address[1] = regAddress;
        } else {
            address[1] = "2";
        }
        return address;
    }

    /**
     * 从账户信息中间表中 获取相关客户信息
     *
     * @param customerNo
     * @param accountNo
     * @return
     * @author yangcq
     * @2019-06-17
     * @address wulmq
     */
    public List<RiskDataInfoDto> getAccountInfoFromAms(String customerNo, String accountNo, String corporateBank) {
        List<RiskDataInfoDto> list = new ArrayList<>();

        String sql = OracleSQLConstant.getAccountInfoFromAmsSql;
        //String sql = MysqlSQLConstant.getAccountInfoFromAmsSql;
        if (StringUtils.isNotBlank(accountNo)) {
            sql += " and t.yd_account_no = ?1";
        }
        if (StringUtils.isNotBlank(customerNo)) {
            sql += " and t.yd_customer_id = ?2";
        }
        Query nativeQuery = entityManager.createNativeQuery(sql);
        if (StringUtils.isNotBlank(accountNo)) {
            nativeQuery.setParameter(1, accountNo);
        }
        if (StringUtils.isNotBlank(customerNo)) {
            nativeQuery.setParameter(2, customerNo);
        }
        List<Object[]> resultList = nativeQuery.getResultList();
        for (Object[] obj : resultList) {
            RiskDataInfoDto riskDataDto = new RiskDataInfoDto(
                    0L,
                    (obj[0] == null) ? "" : obj[0].toString(),
                    (obj[1] == null) ? "" : obj[1].toString(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    (obj[2] == null) ? "" : obj[2].toString(),
                    (obj[3] == null) ? "" : obj[3].toString(),
                    (obj[4] == null) ? "" : obj[4].toString(),
                    (obj[5] == null) ? "" : obj[5].toString()
            );
            list.add(riskDataDto);
        }
        return list;
    }

    /**
     * r
     * 同步开户风险数据
     *
     * @param
     * @author yangcq
     * @date 2019-05-30
     */
    @Override
    public void syncRiskData(String currentDate) {
        log.info("删除开户风险数据*******************************");
        this.deleteAllRiskApi();
        log.info("删除开户风险数据完成*******************************");
        //同步基本开户人频繁开户、销户或变更账户信息 风险数据
        try {
            procService.risk2001Application(currentDate, false);
        } catch (Exception e) {
            log.info("生成RISK_2001模型失败");
        }
        try {                                         // ----------有account_all_data
           procService.risk2002Application(currentDate, false);
        } catch (Exception e) {
            log.info("生成RISK_2002模型失败");
        }
        //同步双异地（注册地、经营地）单位开立银行账户 风险数据  ----------有account_all_data
        try {
            this.generateRisk2003Data(currentDate, false);
        } catch (Exception e) {
            log.info("生成RISK_2003模型失败");
        }
        try {                                       //----------有account_all_data
           procService.risk2005Application(currentDate, false);
        } catch (Exception e) {
            log.info("生成RISK_2005模型失败");
        }
        try {
            procService.risk2006Application(currentDate, false);
        } catch (Exception e) {
            log.info("生成RISK_2006模型失败");
        }
        //同步多个主体的法定代表人、财务负责人、财务经办人员预留同一个联系方式等 风险数据
        try {
           this.generateRisk2007Data(currentDate, false);
        } catch (Exception e) {
            log.info("生成RISK_2007模型失败");
        }
        //同步同一主体的法定代表人、财务负责人、财务经办人员预留同一个联系方式等 风险数据
        try {
            procService.risk2008Appliaction(currentDate, false);
        } catch (Exception e) {
            log.info("生成RISK_2008模型失败");
        }
        //新增模型总的触发跑批方法。
        try {
           procService.risk3000Appliaction(currentDate, false);
        } catch (Exception e) {
            log.info("生成RISK_3000模型失败");
        }
    }

    @Transactional
    public void deleteAllRiskApi() {
        riskApiDao.deleteAll();
    }

    public static void getAllHighDateToList(String riskDate, Map<String, Object[]> ma, List<HighRiskData> allHighRisk, RiskRecordInfo riskRecordInfo, String cusId) {
        if (StringUtils.isNotBlank(cusId)) {
            Object[] obj = ma.get(cusId);
            if (obj != null) {
                HighRiskData highRiskData = new HighRiskData();
                highRiskData.setCustomerNo(obj[0] == null ? "" : obj[0].toString());
                highRiskData.setLegalName(obj[1] == null ? "" : obj[1].toString());
                highRiskData.setLegalIdcardNo(obj[2] == null ? "" : obj[2].toString());
                highRiskData.setLegalIdcardType(obj[3] == null ? "" : obj[3].toString());
                highRiskData.setDepositorName(obj[4] == null ? "" : obj[4].toString());
                highRiskData.setDepositorcardNo(obj[5] == null ? "" : obj[5].toString());
                highRiskData.setDepositorcardType(obj[6] == null ? "" : obj[6].toString());
                highRiskData.setRiskDate(riskDate);
                highRiskData.setRiskDesc(riskRecordInfo.getRiskDesc());
                highRiskData.setRiskId(riskRecordInfo.getRiskId());
                highRiskData.setAccountNo(riskRecordInfo.getAccountNo());
                highRiskData.setCorporateBank(RiskUtil.getOrganizationCode());
                String status = obj[8] == null ? "" : obj[8].toString();
                if (status.equals("")) {
                    highRiskData.setStatus("0");
                } else {
                    highRiskData.setStatus(status);
                }
                allHighRisk.add(highRiskData);
            }

        }
    }


}




