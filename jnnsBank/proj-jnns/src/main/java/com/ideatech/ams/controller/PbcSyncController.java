package com.ideatech.ams.controller;


import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.dao.bill.AccountBillsAllDao;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.dao.*;
import com.ideatech.ams.domain.*;
import com.ideatech.ams.dto.JnResultDto;
import com.ideatech.ams.dto.SyncCompareInfo;
import com.ideatech.ams.dto.gmsp.GMSP;
import com.ideatech.ams.pbc.dto.AmsJibenUniqueCheckCondition;
import com.ideatech.ams.service.JnnsImageService;
import com.ideatech.ams.service.SyncCompareService;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.ws.api.service.PbcApiService;
import com.ideatech.ams.ws.api.service.PbcSearchService;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ideatech.ams.utils.DateUtils.getNowDateShort;

@RequestMapping("/interface")
@RestController

public class PbcSyncController {


    public static Logger log = LoggerFactory.getLogger(PbcSyncController.class);
    @Autowired
    private PbcApiService pbcApiService;

    @Autowired
    private PbcSearchService pbcSearchService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SyncCompareService syncCompareService;

    @Autowired
    private AccountsAllDao accountsAllDao;

    @Autowired
    private AccountBillsAllDao accountBillsAllDao;

    @Autowired
    private JnnsImageBillAllDao jnnsImageBillAllDao;

    @Autowired
    private JnnsImageAllDao jnnsImageAllDao;

    @Autowired
    private AccountsBillsAllDao accountsBillsAllDao;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private JnnsCorrectBillAllDao jnnsCorrectBillAllDao;

    @Autowired
    private JnnsCorrectAllDao jnnsCorrectAllDao;

    @Autowired
    private JnnsImageService jnnsImageService;

    @Autowired
    private AllBillsPublicService allBillsPublicService;


    /**
     * 是否基于存量数据。否则变更、久悬、销户 对于无存量数据的情况下，直接新建客户账户信息
     */
    @Value("${ams.company.datenbestand:true}")
    private Boolean datenbestand;


    String jsonStr = null;

    private String format = "yyyy-MM-dd";


    /**
     * 冲正接口
     */
    @PostMapping("/postCorrect")
    public ResultDto getCorrect(@RequestBody GMSP billsPublics, HttpServletResponse response) {
        ResultDto resultDto = new ResultDto();
        try {
            log.info("————————————————————冲正接口进入————————————————————————");
            jsonStr = JSONObject.toJSONString(billsPublics);
            log.info("请求报文：" + jsonStr);
            if (StringUtils.isEmpty(billsPublics.getJnBillId())) {
                log.info("流水号为空...");
                return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), "流水号为空");
            }
            JnnsCorrectBillAll jnnsCorrectBillAll = jnnsCorrectBillAllDao.findByJnBillId(billsPublics.getJnBillId());
            JnnsCorrectAll jnnsCorrectAll = new JnnsCorrectAll();
            if (jnnsCorrectBillAll != null) {
                BeanCopierUtils.copyProperties(jnnsCorrectBillAll, jnnsCorrectAll);
                if (BillType.ACCT_OPEN.equals(jnnsCorrectBillAll.getBillType())) {
                    AccountsAll accountsAll = accountsAllDao.findByAcctNo(jnnsCorrectBillAll.getAcctNo());
                    jnnsCorrectAll.setAccountStatus(AccountStatus.notExist);
                    accountsAllDao.delete(accountsAll);
                    accountBillsAllDao.delete(Long.parseLong(jnnsCorrectBillAll.getBillId()));
                }
                if (BillType.ACCT_REVOKE.equals(jnnsCorrectBillAll.getBillType())) {
                    AccountsAll accountsAll = accountsAllDao.findByAcctNo(jnnsCorrectBillAll.getAcctNo());
                    accountsAll.setAccountStatus(AccountStatus.normal);
                    jnnsCorrectAll.setAccountStatus(AccountStatus.normal);
                    accountsAllDao.save(accountsAll);
                    accountBillsAllDao.delete(Long.parseLong(jnnsCorrectBillAll.getBillId()));
                }
            } else {
                log.info("账号冲正" + billsPublics.getAcctNo() + "未查到对应行内流水号...");
                return ResultDtoFactory.toApiError(ResultCode.DATA_IS_WRONG.code(), "未查到对应行内流水号");
            }
            jnnsCorrectAll.setId(null);
            jnnsCorrectAllDao.save(jnnsCorrectAll);
            resultDto.setCode("1");
            resultDto.setMessage("冲正成功！");
        } catch (Exception e) {
            log.error("冲正接口连接失败", e);
            resultDto.setCode("0");
            resultDto.setMessage(e.getMessage());
        }
        return resultDto;
    }

    /**
     * 影像批次号
     */
    @PostMapping("/postImageCode")
    public ResultDto getImageCode(@RequestBody GMSP billsPublics, HttpServletResponse response) {
        ResultDto resultDto = new ResultDto();
        String movieCode = "";
        try {
            log.info("————————————————————影像批次号接口进入————————————————————————");
            jsonStr = JSONObject.toJSONString(billsPublics);
            log.info("请求报文：" + jsonStr);
            if (StringUtils.isEmpty(billsPublics.getAcctNo())) {
                log.info("账号为空...");
                return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), "账号为空");
            }
            if (StringUtils.isEmpty(billsPublics.getJnBillId())) {
                log.info("行内流水号为空...");
                return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), "行内流水号为空");
            }
            if (StringUtils.isEmpty(billsPublics.getShifoubz())) {

                log.info("是否标志为空...");
                return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), "是否标志为空");
            }



            JnnsImageBillAll jnnsImageBillAll = new JnnsImageBillAll();
            jnnsImageBillAll.setJnBillId(billsPublics.getJnBillId());    //添加行内流水号
            jnnsImageBillAll.setAcctNo(billsPublics.getAcctNo());        //账号
            jnnsImageBillAll.setShifoubz(billsPublics.getShifoubz());
            movieCode = jnnsImageService.heightQueryExample(billsPublics.getImageCode());

            if (movieCode.equals(",")) {
                log.info("本地信息维护影像高级查新进入====");
                movieCode = jnnsImageService.heightQueryExample1(billsPublics.getImageCode());
            }
            String imageCode = "";
            String busiStartDate = "";
            String[] strArray = movieCode.split(",");
            for (int i = 0; i < strArray.length; i++) {
                busiStartDate = strArray[0];
                imageCode = strArray[1];

            }
            log.info("根据影像流水号或的影像批次号：" + imageCode);
            jnnsImageBillAll.setImageCode(imageCode);  //影像批次号
            log.info("查询日期======" + busiStartDate);
            jnnsImageBillAll.setBusiStartDate(busiStartDate);//查询日期
            jnnsImageBillAllDao.save(jnnsImageBillAll);

            if (jnnsImageBillAll != null && jnnsImageBillAll.getId() != null) {
                resultDto.setCode("1");
                resultDto.setMessage("影像批次号保存成功！");
            } else {
                resultDto.setCode("0");
                resultDto.setMessage("影像批次号保存失败！");
            }
        } catch (Exception e) {
            log.error("影像批次号接口连接失败", e);
            resultDto.setCode("0");
            resultDto.setMessage(e.getMessage());
        }
        return resultDto;
    }

    /**
     * 唯一性校验
     *
     * @param
     * @return 返回999999 则表示该企业基本户已在人行开立
     */
    @PostMapping("/postCheck")
    public ResultDto postCheck(@RequestBody AmsJibenUniqueCheckCondition amsJibenUniqueCheckCondition, HttpServletResponse response) {
        ResultDto resultDto = new ResultDto();
        try {
            log.info("————————————————唯一性校验接口进入————————————————————");
            if (amsJibenUniqueCheckCondition == null) {
                log.info("报文为空...");
                return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), "请求报文为空");
            }
            jsonStr = JSONObject.toJSONString(amsJibenUniqueCheckCondition);
            log.info("请求报文：" + jsonStr);
            log.info(amsJibenUniqueCheckCondition.toString());


            if(StringUtils.isNotBlank(amsJibenUniqueCheckCondition.getBankCode())){
                amsJibenUniqueCheckCondition.setOrganCode(amsJibenUniqueCheckCondition.getBankCode());
            } else {
                return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), "传入机构号为空");
            }
            //存款人类别选择14-无字号的个体工商户时，进行设置
            String depositorType = amsJibenUniqueCheckCondition.getDepositorType();
            if (StringUtils.isNotBlank(depositorType) && depositorType.equals("14")) {
                String depositorName = amsJibenUniqueCheckCondition.getDepositorName();
                if (!StringUtils.startsWith(depositorName, "个体户")) {
                    amsJibenUniqueCheckCondition.setDepositorName("个体户" + depositorName);
                }
                amsJibenUniqueCheckCondition.setDepositorType("13");
                log.info("唯一性校验存款人类别" + amsJibenUniqueCheckCondition.getDepositorType());
            }
            resultDto = pbcSearchService.jiBenUniqueCheck(amsJibenUniqueCheckCondition, amsJibenUniqueCheckCondition.getOrganCode());
            if (resultDto != null) {
                if (resultDto.getCode().equals("1")) {
                    resultDto.setCode("1");
                    resultDto.setMessage("唯一性校验成功，该客户未在人行已开立基本户信息");
                } else {
                    //该企业基本户已在人行账管系统开立
                    if (StringUtils.isNotBlank(resultDto.getMessage()) && (resultDto.getMessage().contains("该存款人名称重复") || resultDto.getMessage().contains("工商营业执照注册号不唯一") || resultDto.getMessage().contains("存款人已开立基本存款账户"))) {
                        resultDto.setCode("999999");
                        resultDto.setMessage(resultDto.getMessage());
                    } else {
                        resultDto.setCode(resultDto.getCode());
                        resultDto.setMessage(resultDto.getMessage());
                    }
                }
            } else {
                resultDto.setCode("0");
                resultDto.setMessage("唯一性校验失败");
            }
        } catch (Exception e) {
            log.error("唯一性校验失败", e);
            resultDto.setCode("0");
            resultDto.setMessage(e.getMessage());
        }
        return resultDto;
    }

    /**
     * 开户校验久悬户
     *
     * @param billsPublic
     * @return
     */
    @PostMapping("/getAccountState")
    public ResultDto getAccountState(@RequestBody AllBillsPublicDTO billsPublic, HttpServletResponse response) {
        ResultDto resultDto = new ResultDto();
        try {
            log.info("——————————————————校验久悬户接口进入——————————————————");
            if (billsPublic == null) {
                log.info("报文为空...");
                return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), "请求报文为空");
            }
            if(StringUtils.isNotBlank(billsPublic.getAccountKey())){
                if("30400".equals(billsPublic.getAccountKey().substring(1,6))){
                    billsPublic.setRegAreaCode("304000");
                }
            }
            jsonStr = JSONObject.toJSONString(billsPublic);
            log.info("请求报文：" + jsonStr);

            log.info("开始查询久悬...");
            resultDto = pbcApiService.checkPbcInfo(billsPublic.getAccountKey(), billsPublic.getRegAreaCode(), billsPublic.getOrganCode());
            if (resultDto != null) {
                if ("1".equals(resultDto.getCode())) {
                    resultDto.setCode("1");
                    resultDto.setMessage("账户未发现异常！");
                    if (resultDto.getData() != null) {
                        resultDto.setData(JSONObject.toJSONString(resultDto.getData()));
                    }
                } else {
                    if (resultDto.getMessage().contains("存款人有其他久悬")) {
                        resultDto.setCode("999999");
                        resultDto.setMessage(resultDto.getMessage());
                    } else if (resultDto.getMessage().contains("已撤销")) {
                        resultDto.setCode("888888");
                        resultDto.setMessage(resultDto.getMessage());
                    } else {
                        resultDto.setCode(resultDto.getCode());
                        resultDto.setMessage(resultDto.getMessage());
                    }
                }
            } else {
                resultDto.setCode("0");
                resultDto.setMessage("查询久悬失败！");
            }
        } catch (Exception e) {
            log.error("久悬校验失败", e);
            resultDto.setCode("0");
            resultDto.setMessage(e.getMessage());
        }
        return resultDto;
    }


    /**
     * 纯接口开户模式上报
     *
     * @param billsPublics
     * @return
     */
    @PostMapping("/postAcctInfoByPbc")
    public JnResultDto getPostAcctInfoByPbcResult(@RequestBody GMSP billsPublics, HttpServletResponse response) {
        AllBillsPublicDTO billsPublic = new AllBillsPublicDTO();
        AllBillsPublicDTO billsPublic1 = new AllBillsPublicDTO();
        boolean syncAms = false;//人行账管系统上报
        boolean syncEccs = false;//信用代码证系统上报
        if ("0".equals(billsPublics.getAcctAmsOpenType())) {
            syncAms = true;
        }
        if ("0".equals(billsPublics.getAcctEccsOpenType()) && billsPublics.getAcctType() == CompanyAcctType.jiben) {
            syncEccs = true;
        }
        JnResultDto jnResultDto = new JnResultDto();
        try {
            log.info("————————————————————人行上报接口进入————————————————————");
            if (billsPublics == null) {
                log.info("报文为空...");
                jnResultDto.setCode(ResultCode.PARAM_IS_BLANK.code());
                jnResultDto.setMessage("请求报文解析异常");
                return jnResultDto;
            }
            jsonStr = JSONObject.toJSONString(billsPublics);
//            jsonStr = JSON.toJSONString(billsPublics.noSensitiveObj());
            log.info("上报接口接收到的报文：" + jsonStr);
            log.info("柜面业务流水号：" + billsPublics.getJnBillId());
            //数据初始化
            syncInit(billsPublics, billsPublic, BillType.ACCT_OPEN, syncAms, syncEccs);
            //数据转换
            toByTO(billsPublic);
            jsonStr = JSONObject.toJSONString(billsPublic);
            //调用报送接口
            //上生产注释
            billsPublic.setCreatedDate(getNowDateShort("yyyyMMdd"));
//           billsPublic.setFileDue(billsPublics.getCredentialDue());
            billsPublic.setCredentialDue(DateUtils.strToStrAsFormat(billsPublics.getCredentialDue(), format));
            billsPublic.setFileDue(DateUtils.strToStrAsFormat(billsPublics.getCredentialDue(), format));
            billsPublic.setSetupDate(DateUtils.strToStrAsFormat(billsPublics.getSetupDate(), format));
            billsPublic.setEffectiveDate(DateUtils.strToStrAsFormat(billsPublics.getEffectiveDate(), format));

            log.info("+++++++++++++++"+billsPublic.getFileDue());
            ResultDto resultDto = pbcApiService.reenterablePbcSync(billsPublic.getOrganCode(), billsPublic, syncAms, syncEccs);
            //报送后处理 业务影像和冲正记录
            doAfterSync(billsPublics, BillType.ACCT_OPEN, null);

            if (resultDto != null) {
                if (resultDto.getCode().equals("1")) {
                    returnSyncSuccessResult(billsPublics.getAcctType(), resultDto, jnResultDto);
                } else {
                    jnResultDto.setCode(resultDto.getCode());
                    jnResultDto.setMessage(resultDto.getMessage());
                }
            } else {
                jnResultDto.setCode("0");
                jnResultDto.setMessage("账户上报失败");
            }
        } catch (Exception e) {
            log.error("开户接口上报失败", e);
            jnResultDto.setCode("0");
            jnResultDto.setMessage(e.getMessage());
        }
        log.info("开户报送接口返回信息="+jnResultDto);
        return jnResultDto;
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

    /**
     * 英文字段转中文字段
     *
     * @param fieldName
     * @return
     */
    private String getFieldCHNName(String fieldName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("acctType", "账户性质");
        map.put("depositorType", "存款人类别");
        map.put("organCode", "所属机构");
        map.put("regAreaCode", "注册地地区地区代码");
        if (StringUtils.isNotBlank(fieldName)) {
            return map.get(fieldName);
        } else {
            return fieldName;
        }
    }

    /**
     * 变更
     *
     * @param
     * @return
     */
    @PostMapping("/postChangeAcctInfoByPbc")
    public ResultDto getPostChangeAcctInfoByPbcResult(@RequestBody GMSP billsPublics, HttpServletResponse response) {
        AllBillsPublicDTO billsPublic = new AllBillsPublicDTO();
        AllBillsPublicDTO billsPublic1 = new AllBillsPublicDTO();

        SyncCompareInfo syncCompareInfo;
        ResultDto resultDto = new ResultDto();
        boolean syncAms = false;//人行账管系统上报
        boolean syncEccs = false;//信用代码证系统上报
        if ("0".equals(billsPublics.getAcctAmsOpenType())) {
            syncAms = true;
        }
        if ("0".equals(billsPublics.getAcctEccsOpenType()) && billsPublic.getAcctType() == CompanyAcctType.jiben) {
            syncEccs = true;
        }
        try {
            log.info("——————————————变更接口进入成功——————————————");
//            BeanCopierUtils.copyProperties(billsPublics, billsPublic1); //对象copy
            jsonStr = JSONObject.toJSONString(billsPublics);
            log.info("变更接口接收到的报文：" + jsonStr);
            //校验报文规范
            validateReportInfo(billsPublics, resultDto);
            if (StringUtils.isNotBlank(resultDto.getMessage())) {
                return resultDto;
            }
            //变更时当日开变销记录
            syncCompareInfo = processSyncCompanyInfo(billsPublics);
            //柜面在第一次变更信息保存的时候传的acctType为10，即为tempAcct值。
            if (CompanyAcctType.tempAcct.equals(billsPublics.getAcctType())) {
                resultDto.setCode("1");
                resultDto.setMessage("当日开变销信息保存成功");
                return resultDto;
            } else {
                if (billsPublics.getAcctType().equals(CompanyAcctType.jiben)) {
                    syncCompareInfo.setAcctType("jiben");
                } else if (billsPublics.getAcctType().equals(CompanyAcctType.yiban)) {
                    syncCompareInfo.setAcctType("yiban");
                } else if (billsPublics.getAcctType().equals(CompanyAcctType.linshi) || billsPublics.getAcctType().equals(CompanyAcctType.feilinshi)) {
                    syncCompareInfo.setAcctType("linshi");
                } else if (billsPublics.getAcctType().equals(CompanyAcctType.yusuan) || billsPublics.getAcctType().equals(CompanyAcctType.feiyusuan)) {
                    syncCompareInfo.setAcctType("zhuanhu");
                } else if (billsPublics.getAcctType().equals(CompanyAcctType.teshu)) {
                    syncCompareInfo.setAcctType("teshu");
                }
                //数据初始化
                syncInit(billsPublics, billsPublic, BillType.ACCT_CHANGE, syncAms, syncEccs);
                //数据转换
                toByTO(billsPublic);
//                jsonStr = JSON.toJSONString(billsPublics.noSensitiveObj());

                //交换号

                if (StringUtils.isNotBlank(billsPublics.getExChange())) {
                    billsPublic.setAcctNo(billsPublics.getExChange() + billsPublics.getAcctNo());
                    log.info("拼接账号==========" + billsPublic.getAcctNo());
                }
                //校验是否存在需要先销后开的字段集合
                List<String> needNewOpenList = getneedNewOpenFieldList(billsPublic);
                //调用远程报送\
                log.info("机构号=============+"+billsPublics.getOrganCode());
                billsPublic.setCreatedDate(getNowDateShort("yyyyMMdd"));
                billsPublic.setFileDue(DateUtils.strToStrAsFormat(billsPublics.getCredentialDue(), format));
                billsPublic.setSetupDate(DateUtils.strToStrAsFormat(billsPublics.getSetupDate(), format));
                billsPublic.setEffectiveDate(DateUtils.strToStrAsFormat(billsPublics.getEffectiveDate(), format));
                billsPublic.setCredentialDue(DateUtils.strToStrAsFormat(billsPublics.getCredentialDue(), format));
                log.info("+++++++++++++++"+billsPublic.getFileDue());
                log.info("系统日期为。。。。。。。。。"+System.currentTimeMillis());
                resultDto = pbcApiService.reenterablePbcSync(billsPublic.getOrganCode(), billsPublic, syncAms, syncEccs);
                //报送后处理 业务影像、当日开变销 账户状态
                doAfterSync(billsPublics, BillType.ACCT_CHANGE, syncCompareInfo);
                log.info("变更接口返回信息：" + resultDto.getMessage());
                if (resultDto != null && StringUtils.isNotBlank(resultDto.getCode())) {
                    if (resultDto.getCode().equals("1")) {
                        if (needNewOpenList != null && needNewOpenList.size() > 0) {
                            StringBuffer needFieldStr = new StringBuffer(" ");
                            for (String s : needNewOpenList) {
                                needFieldStr.append(getFieldCHNName(s)).append("、");
                            }
                            resultDto.setCode("2");
                            resultDto.setMessage("账户处理成功,变更内容包含需手工报备字段（" + StringUtils.substringBeforeLast(needFieldStr.toString(), "、") + "），需线下人行账管系统手工进行报备");
                        } else {
                            resultDto.setCode("1");
                            resultDto.setMessage("账户处理成功！");
                        }
                    } else {
                        resultDto.setCode(resultDto.getCode());
                        resultDto.setMessage(resultDto.getMessage());
                    }
                } else {
                    resultDto.setCode("0");
                    resultDto.setMessage("账户变更上报失败");
                }
            }
        } catch (Exception e) {
            log.error("变更接口上报失败", e);
            resultDto.setCode("0");
            resultDto.setMessage(e.getMessage());
        }
        return resultDto;
    }

    /**
     * 销户
     *
     * @param
     * @return
     */
    @PostMapping("/postDestoryAcctInfoByPbc")
    public ResultDto getPostRevokerAcctInfoByPbcResult(@RequestBody GMSP billsPublics, HttpServletResponse response) {
        AllBillsPublicDTO billsPublic = new AllBillsPublicDTO();
        AllBillsPublicDTO billsPublic1 = new AllBillsPublicDTO();

        ResultDto resultDto = new ResultDto();
        try {
            log.info("——————————————销户接口进入成功——————————————");
            jsonStr = JSONObject.toJSONString(billsPublics);
            log.info("销户接口接收到的报文：" + jsonStr);
            //校验报文规范
            validateReportInfo(billsPublics, resultDto);
            if (StringUtils.isNotBlank(resultDto.getMessage())) {
                return resultDto;
            }
            if (billsPublics.getAcctCancelReason() == null) {
                log.info("销户接口传过来的销户原因为空...");
                return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), "销户原因为空");
            }

            BeanCopierUtils.copyProperties(billsPublics, billsPublic); //对象copy
            BeanCopierUtils.copyProperties(billsPublic, billsPublic1); //对象copy

            billsPublic.setBillType(BillType.ACCT_REVOKE);
            billsPublic.setPbcSyncStatus(CompanySyncStatus.weiTongBu);
            billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);

            jsonStr = JSONObject.toJSONString(billsPublics);
//            jsonStr = JSON.toJSONString(billsPublics.noSensitiveObj());
            log.info("上报人行的信息为：" + jsonStr);


            //交换号
            if (StringUtils.isNotBlank(billsPublics.getExChange())) {
                billsPublic.setAcctNo(billsPublics.getExChange() + billsPublics.getAcctNo());
            }
            //销户报送
            billsPublic.setBasicAcctRegArea(billsPublics.getRegAreaCode());
            if (billsPublics.getAcctType()==CompanyAcctType.jiben){
                AccountBillsAll byAcctNoAndBilltType = accountBillsAllDao.findByAcctNoAndBillType(billsPublic.getAcctNo(), BillType.ACCT_OPEN);
                if(byAcctNoAndBilltType != null){
                    billsPublic.setDepositorType(byAcctNoAndBilltType.getDepositorType());
                    billsPublic.setFileNo(byAcctNoAndBilltType.getFileNo());
                    log.info("存款人类别"+byAcctNoAndBilltType.getDepositorType());
                    log.info("证明文件1编号"+byAcctNoAndBilltType.getFileNo());
                }
            }

            resultDto = pbcApiService.reenterablePbcSync(billsPublic.getOrganCode(), billsPublic);
            //报送后处理业务影像和冲正
            doAfterSync(billsPublics, BillType.ACCT_REVOKE, null);
            //接口返回处理结果
            if (resultDto != null) {
                if (resultDto.getCode().equals("1")) {
                    resultDto.setCode("1");
                    resultDto.setMessage("销户成功！");
                } else {
                    resultDto.setCode(resultDto.getCode());
                    resultDto.setMessage(resultDto.getMessage());
                }
            } else {
                ResultDto resultDto1 = new ResultDto();
                resultDto1.setCode("0");
                resultDto1.setMessage("账户销户上报失败");
            }
        } catch (Exception e) {
            log.info("上报失败", e);
            resultDto.setCode("0");
            resultDto.setMessage(e.getMessage());

        }
        return resultDto;
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
     * 开户数据初始化
     *
     * @param billsPublics 报文对象
     * @param billsPublic  产品业务流水对象
     * @param amsSync      人行账管是否报备
     * @param eccsSync     机构信用代码是否报备
     */
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
     * 账户报送后，对于业务影像及冲正记录进行处理
     *
     * @param billsPublics
     * @param billType
     * @param syncCompareInfo
     */
    private void doAfterSync(GMSP billsPublics, BillType billType, SyncCompareInfo syncCompareInfo) {

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
     * 报送成功后，处理返回报文，企业类基本户需返回许可证及查询密码
     *
     * @param acctType
     * @param resultDto
     * @param jnResultDto
     */
    private void returnSyncSuccessResult(CompanyAcctType acctType, ResultDto resultDto, JnResultDto jnResultDto) {
        String resultDtoObject = JSONObject.toJSONString(resultDto);
        JSONObject jso = JSONObject.parseObject(resultDtoObject);
        JSONObject datajson = JSONObject.parseObject(jso.getString("data"));
        jnResultDto.setCode(resultDto.getCode());
        jnResultDto.setMessage("账户上报成功");
        //基本户会返回许可证及查询密码
        if (acctType != null && acctType == CompanyAcctType.jiben) {
            jnResultDto.setSelectPwd(datajson.getString("selectPwd"));
            jnResultDto.setOpenKey(datajson.getString("openKey"));
            jnResultDto.setAcctName(datajson.getString("acctName"));
            jnResultDto.setAcctNo(datajson.getString("acctNo"));
            jnResultDto.setBankName(datajson.getString("bankName"));
            jnResultDto.setLegalName(datajson.getString("legalName"));
            jnResultDto.setAccountKey(datajson.getString("accountKey"));
            jnResultDto.setDepositoryName(datajson.getString("depositoryName"));
        }
    }


    /**
     * 校验报文是否满足规范
     *
     * @param billsPublics
     * @param resultDto
     */
    private void validateReportInfo(GMSP billsPublics, ResultDto resultDto) {
        resultDto.setCode(ResultCode.PARAM_IS_BLANK.code());
        if (billsPublics == null) {
            resultDto.setMessage("请求报文解析异常");
        } else if (billsPublics.getAcctNo() == null) {
            resultDto.setMessage("报送时账号为空");
        } else if (billsPublics.getAcctType() == null && billsPublics.getBillType() == null) {
            resultDto.setMessage("账户性质不能为空");
        }
    }

    /**
     * 变革处理当日开变销记录，变更记录
     *
     * @param billsPublics
     */
    private SyncCompareInfo processSyncCompanyInfo(GMSP billsPublics) {


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

    //上报数据格式类型转化器
    public void toByTO(AllBillsPublicDTO billsPublicDTO) {
        /**
         * 工商注册类型转换
         */
        if (null != billsPublicDTO.getRegType()) {
            String regType = dictionaryService.transalte("core2dataRegType", billsPublicDTO.getRegType());
            if (StringUtils.isNotBlank(regType)) {
                billsPublicDTO.setRegType(regType);
            }
        }

        /**
         * 行业归属转换
         */
        if (null != billsPublicDTO.getIndustryCode()) {
            String industryCode = dictionaryService.transalte("core2dataIndustryCode", billsPublicDTO.getIndustryCode());
            if (StringUtils.isNotBlank(industryCode)) {
                billsPublicDTO.setIndustryCode(industryCode);
            }
        }

        /**
         * 建筑施工安装及实时负责人身份证件种
         */
        if (null != billsPublicDTO.getNontmpLegalIdcardType()) {
            String nontmpLegalIdcardType = dictionaryService.transalte("core2dataLegalIdCardType", billsPublicDTO.getNontmpLegalIdcardType());
            if (StringUtils.isNotBlank(nontmpLegalIdcardType)) {
                billsPublicDTO.setNontmpLegalIdcardType(nontmpLegalIdcardType);
            }
        }

        /**
         * 负责人身份证件种类
         */
        if (null != billsPublicDTO.getInsideLeadIdcardType()) {
            String insideLeadIdcardType = dictionaryService.transalte("core2dataLegalIdCardType", billsPublicDTO.getInsideLeadIdcardType());
            if (StringUtils.isNotBlank(insideLeadIdcardType)) {
                billsPublicDTO.setInsideLeadIdcardType(insideLeadIdcardType);
            }
        }

        /**
         * 资金管理人身份证种类
         */
        if (null != billsPublicDTO.getFundManagerIdcardType()) {
            String fundManagerIdcardType = dictionaryService.transalte("core2dataLegalIdCardType", billsPublicDTO.getFundManagerIdcardType());
            if (StringUtils.isNotBlank(fundManagerIdcardType)) {
                billsPublicDTO.setFundManagerIdcardType(fundManagerIdcardType);
            }
        }

        /**
         * 上级法人证件类型转换
         */
        if (null != billsPublicDTO.getParLegalIdcardType()) {
            String parLegalIdcardType = dictionaryService.transalte("core2dataLegalIdCardType", billsPublicDTO.getParLegalIdcardType());
            if (StringUtils.isNotBlank(parLegalIdcardType)) {
                billsPublicDTO.setParLegalIdcardType(parLegalIdcardType);
            }
        }

        /**
         * 法人证件类型转换
         */
        if (null != billsPublicDTO.getLegalIdcardType()) {
            String legalIdcardType = dictionaryService.transalte("core2dataLegalIdCardType", billsPublicDTO.getLegalIdcardType());
            if (StringUtils.isNotBlank(legalIdcardType)) {
                billsPublicDTO.setLegalIdcardType(legalIdcardType);
            }
        }

        //判断请求是否有核心机构号没有默认root
        if (StringUtils.isEmpty(billsPublicDTO.getOrganCode())) {
            if (StringUtils.isNotEmpty(billsPublicDTO.getBankCode())) {
                log.info("核心机构号为空，默认为开户银行金融机构编码:" + billsPublicDTO.getBankCode());
                billsPublicDTO.setOrganCode(billsPublicDTO.getBankCode());
            } else {
                billsPublicDTO.setOrgCode("root");
            }
        }
        //开户日期格式转化
        try {
            String acctCreateDate = billsPublicDTO.getAcctCreateDate();
            if (StringUtils.isNotEmpty(acctCreateDate) && acctCreateDate.length() == 8) {
                billsPublicDTO.setAcctCreateDate(DateUtils.strToStrAsFormat(acctCreateDate, format));
            } else {
                log.info("开户日期为null，初始化当前日期...");
                billsPublicDTO.setAcctCreateDate(DateUtils.getNowDateShort(format));
            }
        } catch (Exception e) {
            billsPublicDTO.setAcctCreateDate(DateUtils.getNowDateShort(format));
        }
        //临时户有效日期格式转化
        if (billsPublicDTO.getAcctType() == CompanyAcctType.linshi || billsPublicDTO.getAcctType() == CompanyAcctType.feilinshi) {
            try {
                String effectiveDate = billsPublicDTO.getEffectiveDate();
                if (StringUtils.isNotEmpty(effectiveDate) && effectiveDate.length() == 8) {
                    billsPublicDTO.setEffectiveDate(DateUtils.strToStrAsFormat(effectiveDate, format));
                } else {
                    log.info("临时户有效日期null，初始化当前日期...");
                    billsPublicDTO.setEffectiveDate(DateUtils.getNowDateShort("yyyy-MM-dd"));
                }
            } catch (Exception e) {
                billsPublicDTO.setEffectiveDate(DateUtils.getNowDateShort(format));
            }
        }
        //法定代表人证件到期日
        try {
            if (billsPublicDTO.getAcctType() == CompanyAcctType.jiben || billsPublicDTO.getAcctType() == CompanyAcctType.linshi || billsPublicDTO.getAcctType() == CompanyAcctType.feilinshi) {
                String legalIdcardDue = billsPublicDTO.getLegalIdcardDue();
                if (StringUtils.isNotEmpty(legalIdcardDue)) {
                    billsPublicDTO.setLegalIdcardDue(DateUtils.strToStrAsFormat(legalIdcardDue, format));
                } else {
                    billsPublicDTO.setLegalIdcardDue("");
                    log.info("法定代表人证件到期日为空设置默认值：" + billsPublicDTO.getLegalIdcardDue());
                }
            }
        } catch (ParseException e) {
            billsPublicDTO.setLegalIdcardDue("");
        }
        log.info("转化结束,返回转化结果...");
    }
}
