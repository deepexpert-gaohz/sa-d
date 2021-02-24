package com.ideatech.ams.controller.bills;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.AccountBillsAllSearchInfo;
import com.ideatech.ams.account.dto.AllBillsPublicSearchDTO;
import com.ideatech.ams.account.dto.ReportStatisticsForDateDTO;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.OuterSysCode;
import com.ideatech.ams.account.enums.bill.BillStatus;
import com.ideatech.ams.account.enums.bill.CompanyAmsCheckStatus;
import com.ideatech.ams.account.enums.bill.CompanySyncOperateType;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.account.service.PbcSyncListService;
import com.ideatech.ams.account.service.SyncHistoryService;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.template.dto.TemplateDto;
import com.ideatech.ams.system.template.service.TemplateService;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.ws.api.service.PrintingApiService;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.DepositorType;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.DateUtil;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.PdfGenerator;
import com.ideatech.common.util.SecurityUtils;
import com.ideatech.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/allBillsPublic")
@Slf4j
public class AllBillsPublicController {

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private PbcSyncListService pbcSyncListService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PrintingApiService printingApiService;

    @Autowired
    private SyncHistoryService syncHistoryService;

    /**
     * 业务流水表
     * @param accountBillsAllSearchInfo
     * @param bankCode
     * @param pageable
     * @param code
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableResultResponse<AllBillsPublicSearchDTO> pre(AccountBillsAllSearchInfo accountBillsAllSearchInfo, String bankCode,
                                                            @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable, String code) {
        allBillsPublicService.setCondition(code, accountBillsAllSearchInfo);
        if (!this.preParameterInit(accountBillsAllSearchInfo, bankCode)) {
            TableResultResponse<AllBillsPublicSearchDTO> tableResultResponse = new TableResultResponse<>();
            tableResultResponse.setRows(new ArrayList<AllBillsPublicSearchDTO>());
            return tableResultResponse;
        }
        TableResultResponse<AllBillsPublicSearchDTO> tableResultResponse = allBillsPublicService.query(accountBillsAllSearchInfo, code, pageable);

        return tableResultResponse;
    }

    /**
     * 开变销流水列表（与/allBillsPublic/list类似）
     * @param accountBillsAllSearchInfo
     * @param bankCode
     * @param pageable
     * @param code
     * @return
     */
    @RequestMapping(value = "/statisticsList", method = RequestMethod.GET)
    public TableResultResponse<AllBillsPublicSearchDTO> pr22e(AccountBillsAllSearchInfo accountBillsAllSearchInfo, String bankCode,
                                                            @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable, String code) {
        allBillsPublicService.setCondition(code, accountBillsAllSearchInfo);
        if (!this.preParameterInit(accountBillsAllSearchInfo, bankCode)) {
            TableResultResponse<AllBillsPublicSearchDTO> tableResultResponse = new TableResultResponse<>();
            tableResultResponse.setRows(new ArrayList<AllBillsPublicSearchDTO>());
            return tableResultResponse;
        }

        List<CompanySyncStatus> pbcSyncStatusList = new ArrayList<>();
        pbcSyncStatusList.add(CompanySyncStatus.buTongBu);
        pbcSyncStatusList.add(CompanySyncStatus.tongBuChengGong);
        List<CompanySyncStatus> eccsSyncStatusList = new ArrayList<>();
        eccsSyncStatusList.add(CompanySyncStatus.buTongBu);
        eccsSyncStatusList.add(CompanySyncStatus.tongBuChengGong);
        List<CompanyAmsCheckStatus> pbcCheckStatusList = new ArrayList<>();
        pbcCheckStatusList.add(CompanyAmsCheckStatus.NoCheck);
        pbcCheckStatusList.add(CompanyAmsCheckStatus.CheckPass);
        accountBillsAllSearchInfo.setPbcSyncStatuses(pbcSyncStatusList);
        accountBillsAllSearchInfo.setEccsSyncStatuses(eccsSyncStatusList);
        accountBillsAllSearchInfo.setPbcCheckStatuses(pbcCheckStatusList);

        TableResultResponse<AllBillsPublicSearchDTO> tableResultResponse = allBillsPublicService.query(accountBillsAllSearchInfo, code, pageable);

        return tableResultResponse;
    }

    /**
     * 根据账户账号获取流水列表
     *
     * @param acctNo 账号
     */
    @RequestMapping(value = "/listForAccount", method = RequestMethod.GET)
    public TableResultResponse<AllBillsPublicSearchDTO> listForAccount(String acctNo, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return allBillsPublicService.query(acctNo, pageable);
    }

    @RequestMapping(value = "/getFormDetails", method = RequestMethod.GET)
    public ObjectRestResponse<AllBillsPublicDTO> getFormDetails(Long id, String billType) throws Exception {
        return allBillsPublicService.getOneDetails(id,billType);
    }

    /**
     * 对公页面流水列表条数
     * @return
     */
    @RequestMapping(value = "/counts", method = RequestMethod.GET)
    public List<String> getSyncstatusCounts() {
        return allBillsPublicService.getBillsCounts();
    }

    /**
     * 对公页面流水列表条数  增加白名单查询
     * @return
     */
    @RequestMapping(value = "/whiteListsCounts", method = RequestMethod.GET)
    public List<String> getSyncstatusCounts(String whiteList) {
        return allBillsPublicService.getBillsCounts(whiteList);
    }

    /**getCounts
     * 打印预览
     * @param billType
     * @param depositorType
     * @return
     */
    @GetMapping("/getPrintPreview")
    public ResponseEntity<byte[]> getTemplateNameList(Long id, BillType billType, DepositorType depositorType, String templateName) throws Exception {
        AllBillsPublicDTO allBillsPublicDTO = allBillsPublicService.findOne(id);
        Map<String, String> describe = new HashMap<>();
        Map<String, Object> describe2 = new HashMap<>();
        Map<String, Object> describe3 = new HashMap<>();
        //取消核准打印模板字段
        describe3.put("cancelHeZhunAcctName",allBillsPublicDTO.getAcctName());//账户名称
        describe3.put("cancelHeZhunAcctNo",allBillsPublicDTO.getAcctNo());//账号
        describe3.put("cancelHeZhunBankCode",allBillsPublicDTO.getBankCode());//银行代码
        describe3.put("cancelHeZhunLegalName",allBillsPublicDTO.getLegalName());//法人姓名
        describe3.put("cancelHeZhunAccountKey",allBillsPublicDTO.getAccountKey());//基本户开户许可证
        describe3.put("cancelHeZhunSelectPwd",allBillsPublicDTO.getSelectPwd());//查询密码
        describe3.put("cancelHeZhunDepositorName",allBillsPublicDTO.getDepositorName());//存款人名称
        //变更时，查找变更内容
        if("ACCT_CHANGE".equals(billType.name())){
            allBillsPublicDTO = allBillsPublicService.printChang(id,allBillsPublicDTO);
        }
        if(StringUtils.isNotBlank(allBillsPublicDTO.getAcctName())){
            describe3.put("cancelHeZhunAcctName",allBillsPublicDTO.getAcctName());//账户名称
        }
        if(StringUtils.isNotBlank(allBillsPublicDTO.getLegalName())){
            describe3.put("cancelHeZhunLegalName",allBillsPublicDTO.getLegalName());//法人姓名
        }
        allBillsPublicDTO = allBillsPublicService.conversion(allBillsPublicDTO); //字典转换


        for (Field f : allBillsPublicDTO.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if (f.getType() == String.class && f.get(allBillsPublicDTO) == null) { //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
                f.set(allBillsPublicDTO, "");
            }
            if (f.getType() == BigDecimal.class && f.get(allBillsPublicDTO) == null) {//若资金字段为空，设置值为0
                f.set(allBillsPublicDTO, new BigDecimal("0"));
            }
        }

        describe = BeanUtils.describe(allBillsPublicDTO);
        //格式化打印开户数据
        if ("ACCT_OPEN".equals(billType.name())) {
            //注册资金
            describe.put("registeredCapital", describe.get("registeredCapital") + "元");
            //法人类型
            describe.put("legalType", formatBracketSelect("法定代表人", describe.get("legalType"))
                    + "\r\n" + formatBracketSelect("单位负责人", describe.get("legalType"))
            );
            //上级法人类型
            describe.put("parLegalType", formatBracketSelect("法定代表人", describe.get("parLegalType"))
                    + "\r\n" + formatBracketSelect("单位负责人", describe.get("parLegalType"))
            );
            //行业分类
            if (describe.get("industryCode") != null && describe.get("industryCode").length() >= 1) {
                String industryCodeSimple = describe.get("industryCode").substring(0, 1);
                describe.put("industryCode", formatBracketSelect("A", industryCodeSimple)
                        + formatBracketSelect("B", industryCodeSimple)
                        + formatBracketSelect("C", industryCodeSimple)
                        + formatBracketSelect("D", industryCodeSimple)
                        + formatBracketSelect("E", industryCodeSimple)
                        + formatBracketSelect("F", industryCodeSimple)
                        + formatBracketSelect("G", industryCodeSimple)
                        + formatBracketSelect("H", industryCodeSimple)
                        + formatBracketSelect("I", industryCodeSimple)
                        + formatBracketSelect("J", industryCodeSimple)
                        + "\r\n" + formatBracketSelect("K", industryCodeSimple)
                        + formatBracketSelect("L", industryCodeSimple)
                        + formatBracketSelect("M", industryCodeSimple)
                        + formatBracketSelect("N", industryCodeSimple)
                        + formatBracketSelect("O", industryCodeSimple)
                        + formatBracketSelect("P", industryCodeSimple)
                        + formatBracketSelect("Q", industryCodeSimple)
                        + formatBracketSelect("R", industryCodeSimple)
                        + formatBracketSelect("S", industryCodeSimple)
                        + formatBracketSelect("T", industryCodeSimple)
                );
            }
            //账户性质
            describe.put("acctType", formatBracketSelect("基本", ("jiben".equals(describe.get("acctType")) ? "基本" : ""))
                    + " " + formatBracketSelect("一般", ("yiban".equals(describe.get("acctType")) ? "一般" : ""))
                    + " " + formatBracketSelect("专用", ("yusuan".equals(describe.get("acctType")) || "feiyusuan".equals(describe.get("acctType")) ? "专用" : ""))
                    + " " + formatBracketSelect("临时", ("linshi".equals(describe.get("acctType")) || "feilinshi".equals(describe.get("acctType")) ? "临时" : ""))
            );
            //临时户到期日
            if (StringUtils.isNotBlank(describe.get("effectiveDate"))) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
                    String effectiveDate = sdf2.format(sdf.parse(describe.get("effectiveDate")));
                    describe.put("effectiveDate", effectiveDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    log.info("临时户到期日格式化错误");
                    describe.put("effectiveDate", "");
                }
            }

//            describe.put("openDate", DateUtils.DateToStr(new Date(),"yyyy年MM月dd日"));
            describe.put("openDate", StringUtils.isBlank(allBillsPublicDTO.getString006()) ? DateUtils.DateToStr(new Date(),"yyyy年MM月dd日") : allBillsPublicDTO.getString006());
        }
        if("ACCT_CHANGE".equals(billType.name())){
            BigDecimal temp = new BigDecimal("0");
            if(allBillsPublicDTO.getRegisteredCapital().compareTo(temp)==0){
                describe.put("registeredCapital", "");
            }
        }
//        describe.put("openDate", DateUtils.DateToStr(new Date(),"yyyy年MM月dd日"));
        describe.put("openDate", StringUtils.isBlank(allBillsPublicDTO.getString006()) ? DateUtils.DateToStr(new Date(),"yyyy年MM月dd日") : allBillsPublicDTO.getString006());
        //开户许可证当前年月日拆分打印
        String nowDate = DateUtils.DateToStr(new Date(),"yyyy-MM-dd");
        String accYear = nowDate.split("-")[0];
        String accMon = nowDate.split("-")[1];
        String accDay = nowDate.split("-")[2];
        describe.put("accYear", accYear);
        describe.put("accMonth", accMon);
        describe.put("accDay", accDay);
        describe2.putAll(describe);
        describe2.putAll(describe3);

        TemplateDto byId = templateService.findByBillTypeAndDepositorTypeAndTemplateName(billType, depositorType, templateName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));

        return new ResponseEntity<byte[]>(PdfGenerator.generate(byId.getTemplaeContent(), describe2), headers, HttpStatus.OK);
    }

    /**
     * 添加打印带括号选择的字符串
     * @param name
     * @param value
     * @return
     */
    private String formatBracketSelect(String name, String value) {
        if (name.equals(value)) {
            return name + "（√）";
        } else {
            return name + "（  ）";
        }
    }

    @PutMapping("/finishSync/{id}")
    public ObjectRestResponse finishSync(@PathVariable("id") Long billId) throws Exception {
        AllBillsPublicDTO billsPublic = allBillsPublicService.findOne(billId);
        Long userId = SecurityUtils.getCurrentUserId();
        //更新审核状态
        allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.APPROVED, userId, "");

        Boolean isSyncAms = allBillsPublicService.getSyncStatus(EAccountType.AMS, billsPublic);
        if (isSyncAms) {
            accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.PBC, CompanySyncStatus.tongBuChengGong, CompanySyncOperateType.personSyncType, "", userId);
        }
        Boolean isSyncEccs = allBillsPublicService.getSyncStatus(EAccountType.ECCS, billsPublic);
        if (isSyncEccs) {
            accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.ECCS, CompanySyncStatus.tongBuChengGong, CompanySyncOperateType.personSyncType, "", userId);
        }

        //根据上报状态来判断是否更新最终态
        if (billsPublic.getAcctType().isHeZhun() && (billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_OPEN || billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_CHANGE
                || billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_REVOKE || billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_EXTENSION)) {
            //核准类的开户和变更需要经过人行的核准才可以修改完最终态
            //核准类开户或者变更加入此列表
            if (isSyncAms) {
                pbcSyncListService.savePbcSyncList(billsPublic);
            }
            //取消核准后基本户开变销，非临时开变销展期业务
            if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()){
                accountBillsAllService.updatePbcCheckStatus(billsPublic.getId(), CompanyAmsCheckStatus.NoCheck);
                allBillsPublicService.updateFinalStatus(billsPublic, userId);
            }
        } else {
            if ((isSyncAms || billsPublic.getPbcSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getPbcSyncStatus() == CompanySyncStatus.buTongBu)
                    && (isSyncEccs || billsPublic.getEccsSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getEccsSyncStatus() == CompanySyncStatus.buTongBu)) {
                allBillsPublicService.updateFinalStatus(billsPublic, userId);
            }
        }
        return new ObjectRestResponse<>().rel(true).result("");
    }

    /**
     * 报备成功（直接修改流水中的人行、信用代码证上报状态为成功）
     * @param billId 流水id
     * @param pbcSync 是否修改人行上报状态
     * @param eccsSync 是否修改信用代码证上报状态
     */
    @RequestMapping(value = "/finishSync",method = RequestMethod.POST)
    public ObjectRestResponse finishSync(Long billId, Boolean pbcSync, Boolean eccsSync) throws Exception {
        AllBillsPublicDTO billsPublic = allBillsPublicService.findOne(billId);
        Long userId = SecurityUtils.getCurrentUserId();
        //更新审核状态
        allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.APPROVED, userId, "");

        Boolean isSyncAms = allBillsPublicService.getSyncStatus(EAccountType.AMS, billsPublic) && pbcSync;
        if (isSyncAms) {
            accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.PBC, CompanySyncStatus.tongBuChengGong, CompanySyncOperateType.personSyncType, "", userId);
        }
        Boolean isSyncEccs = allBillsPublicService.getSyncStatus(EAccountType.ECCS, billsPublic) && eccsSync;
        if (isSyncEccs) {
            accountBillsAllService.updateSyncStatus(billsPublic.getId(), OuterSysCode.ECCS, CompanySyncStatus.tongBuChengGong, CompanySyncOperateType.personSyncType, "", userId);
        }

        //根据上报状态来判断是否更新最终态
        if (billsPublic.getAcctType().isHeZhun() && (billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_OPEN || billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_CHANGE
                || billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_REVOKE || billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_EXTENSION)) {
            //取消核准后基本户开变销，非临时开变销展期业务
            if (billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()) {
                if ((isSyncAms || billsPublic.getPbcSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getPbcSyncStatus() == CompanySyncStatus.buTongBu)
                        && (isSyncEccs || billsPublic.getEccsSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getEccsSyncStatus() == CompanySyncStatus.buTongBu)) {
                    accountBillsAllService.updatePbcCheckStatus(billsPublic.getId(), CompanyAmsCheckStatus.NoCheck);
                    allBillsPublicService.updateFinalStatus(billsPublic, userId);
                }
            } else {
                //核准类的开户和变更需要经过人行的核准才可以修改完最终态
                //核准类开户或者变更加入此列表
                if (isSyncAms) {
                    pbcSyncListService.savePbcSyncList(billsPublic);
                }
            }
        } else {
            if ((isSyncAms || billsPublic.getPbcSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getPbcSyncStatus() == CompanySyncStatus.buTongBu)
                    && (isSyncEccs || billsPublic.getEccsSyncStatus() == CompanySyncStatus.tongBuChengGong || billsPublic.getEccsSyncStatus() == CompanySyncStatus.buTongBu)) {
                allBillsPublicService.updateFinalStatus(billsPublic, userId);
            }
        }

        return new ObjectRestResponse<>().rel(true).result("");
    }

    @RequestMapping(value = "/findOne/{id}", method = RequestMethod.GET)
    public JSONObject findOne(@PathVariable("id")Long billId) {
        JSONObject map = new JSONObject();

        AllBillsPublicDTO allBillsPublicDTO = allBillsPublicService.findOne(billId);
        if(allBillsPublicDTO != null) {
            map.put("billType", allBillsPublicDTO.getBillType());
            map.put("acctType", allBillsPublicDTO.getAcctType());
            map.put("depositorType", allBillsPublicDTO.getDepositorType());
        }

        return map;
    }

    @RequestMapping("/delete/{id}")
    public ObjectRestResponse delete(@PathVariable("id") Long billId) throws Exception {
        AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getOne(billId);
        if (accountBillsAllInfo == null) {
            return new ObjectRestResponse<>().rel(false);
        }
        if (accountBillsAllInfo.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_OPEN) {
            boolean result = accountBillsAllService.deleteBillsAndAccount(billId);
            return new ObjectRestResponse<>().rel(result);
        } else {
            boolean result = accountBillsAllService.deleteBills(billId);
            return new ObjectRestResponse<>().rel(result);
        }
    }
    @RequestMapping("/deleteCgsb/{id}")
    public ObjectRestResponse deleteCgsb(@PathVariable("id") Long billId) throws Exception {
        AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getOne(billId);
        if (accountBillsAllInfo == null) {
            return new ObjectRestResponse<>().rel(false);
        }
        if (accountBillsAllInfo.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_OPEN) {
            boolean result = accountBillsAllService.deleteBillsAndAccount(billId);
            return new ObjectRestResponse<>().rel(result);
        } else {
            boolean result = accountBillsAllService.deleteBillsCgsb(billId);
            return new ObjectRestResponse<>().rel(result);
        }
    }
    @RequestMapping(value = "/findBillsCounts", method = RequestMethod.GET)
    public JSONObject findBills() {
        return accountBillsAllService.getIndexBillsCounts();
    }

    /**
     * 获取指定账户的第一笔流水id
     * @return
     */
    @GetMapping(value = "/getFirstBillByAccountId")
    public ObjectRestResponse getFirstBillByAccountId(Long accountId) {
        AccountBillsAllInfo abai = accountBillsAllService.getFirstBillByAccountId(accountId);
        return new ObjectRestResponse<>().result(abai);
    }

    @RequestMapping(value = "/listForBills", method = RequestMethod.GET)
    public TableResultResponse<AllBillsPublicSearchDTO> listForBills(AccountBillsAllSearchInfo accountBillsAllSearchInfo, String code,
                                                                     @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return allBillsPublicService.listForBills(accountBillsAllSearchInfo, code, pageable);
    }

    @RequestMapping(value = "/checkSyncStatus", method = RequestMethod.GET)
    public String checkSyncStatus () {
        return allBillsPublicService.checkSync();
    }

    /**
     * 当日报送数据统计列表
     */
    @RequestMapping(value = "/statisticsForDateList", method = RequestMethod.GET)
    public TableResultResponse<ReportStatisticsForDateDTO> statisticsForDateList(String startDate, String endDate, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        String orgFullId = SecurityUtils.getCurrentOrgFullId();
        TableResultResponse<ReportStatisticsForDateDTO> tableResultResponse = allBillsPublicService.statisticsForDateList(startDate,endDate,orgFullId, pageable);
        return tableResultResponse;
    }

    /**
     * 当日报送数据统计列表
     */
    @RequestMapping(value = "/statisticsForDateDetailList", method = RequestMethod.GET)
    public TableResultResponse<AllBillsPublicSearchDTO> statisticsForDateDetailList(AccountBillsAllSearchInfo accountBillsAllSearchInfo, String bankCode, String reportDate,
                                                                                    @PageableDefault(sort = {"pbcSyncStatus", "eccsSyncStatus"}, direction = Sort.Direction.DESC) Pageable pageable) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = sdf.parse(reportDate + " 00:00:00");
            endDate = sdf.parse(reportDate + " 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        accountBillsAllSearchInfo.setBeginDate(beginDate);
        accountBillsAllSearchInfo.setEndDate(endDate);

        if (!this.preParameterInit(accountBillsAllSearchInfo, bankCode)) {
            TableResultResponse<AllBillsPublicSearchDTO> tableResultResponse = new TableResultResponse<>();
            tableResultResponse.setRows(new ArrayList<AllBillsPublicSearchDTO>());
            return tableResultResponse;
        }

        TableResultResponse<AllBillsPublicSearchDTO> tableResultResponse = allBillsPublicService.statisticsForDateDetailList(accountBillsAllSearchInfo, pageable);
        return tableResultResponse;
    }

    @GetMapping(value = "/statisticsForDateDetailsExport")
    public void export(AccountBillsAllSearchInfo accountBillsAllSearchInfo, String startdate, String enddate, String reportDate, HttpServletResponse response) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginDate = null;
        Date endDate = null;
        try {
            if(StringUtils.isNotBlank(startdate) && StringUtils.isNotBlank(enddate)){
                beginDate = sdf.parse(startdate + " 00:00:00");
                endDate = sdf.parse(enddate + " 23:59:59");
            }else{
                beginDate = sdf.parse(reportDate + " 00:00:00");
                endDate = sdf.parse(reportDate + " 23:59:59");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        accountBillsAllSearchInfo.setBeginDate(beginDate);
        accountBillsAllSearchInfo.setEndDate(endDate);

        IExcelExport iExcelExport = allBillsPublicService.statisticsForDateDetailsExport(accountBillsAllSearchInfo);
        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode("账户信息统计列表", "UTF-8")+".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",iExcelExport);
        toClient.flush();
        response.getOutputStream().close();
    }

    @RequestMapping(value = "/getSyncErrorMsg", method = RequestMethod.GET)
    public JSONObject getSyncErrorMsg(Long refBillId) {
        return syncHistoryService.getSyncErrorMsg(refBillId);
    }

    /**
     * 业务流水表查询参数处理
     * @param accountBillsAllSearchInfo
     * @param bankCode
     * @return true：继续查询；false：查询结果肯定为空，直接返回
     */
    private boolean preParameterInit(AccountBillsAllSearchInfo accountBillsAllSearchInfo,String bankCode) {
        //父类的机构号
        String currentOrgFullId = SecurityUtils.getCurrentOrgFullId();

        //模糊查询申请人名字，获取申请人id集合
        if (StringUtils.isNotBlank(accountBillsAllSearchInfo.getCreatedBy())) {
            List<Long> userIdList = userService.findUserIdByLikeUsername(accountBillsAllSearchInfo.getCreatedBy());
            if (userIdList.size() == 0) {
                return false;
            }
            accountBillsAllSearchInfo.setCreatedBy(null);
            List<String> userIdStrList = new ArrayList<>();
            for (Long userId : userIdList) {
                userIdStrList.add(String.valueOf(userId));
            }
            accountBillsAllSearchInfo.setCreatedByes(userIdStrList);
        }

        //模糊查询人行机构号，获取机构fullId集合
        if (StringUtils.isNotBlank(bankCode)) {
            List<OrganizationDto> odList = organizationService.findByPbcCodeLike(bankCode);
            if (odList.size() == 0) {
                return false;
            } else {
                List<String> organFullIdList = new ArrayList<>();
                for (OrganizationDto od : odList) {
                    organFullIdList.add(od.getFullId());
                }
                accountBillsAllSearchInfo.setOrganFullIdList(organFullIdList);
            }
        } else {
            accountBillsAllSearchInfo.setOrganFullId(currentOrgFullId);
        }

        if (StringUtils.isNotBlank(accountBillsAllSearchInfo.getOrgCode())) {
            OrganizationDto organizationDtoByCode = organizationService.findByCode(accountBillsAllSearchInfo.getOrgCode());
            if (organizationDtoByCode != null && org.apache.commons.lang3.StringUtils.startsWith(organizationDtoByCode.getFullId(), currentOrgFullId)) {
                accountBillsAllSearchInfo.setOrganFullId(organizationDtoByCode.getFullId());
            }
        }

        //查询核心机构号，获取机构fullId集合
        if (StringUtils.isNotBlank(accountBillsAllSearchInfo.getKernelOrgCode()) || StringUtils.isNotBlank(accountBillsAllSearchInfo.getKernelOrgName())) {
            List<OrganizationDto> odList = organizationService.findByCodeLikeAndNameLike(accountBillsAllSearchInfo.getKernelOrgCode(), accountBillsAllSearchInfo.getKernelOrgName());
            if (odList.size() == 0) {
                return false;
            } else {
                List<String> kernelOrgFullIdList = new ArrayList<>();
                for (OrganizationDto od : odList) {
                    kernelOrgFullIdList.add(od.getFullId());
                }
                if (accountBillsAllSearchInfo.getOrganFullIdList() == null) {
                    accountBillsAllSearchInfo.setOrganFullIdList(new ArrayList<String>());
                }
                accountBillsAllSearchInfo.getOrganFullIdList().addAll(kernelOrgFullIdList);
            }
        }
        return true;
    }

    @GetMapping(value = "/statisticsForKBXExport")
    public void statisticsForKBXExport(AccountBillsAllSearchInfo accountBillsAllSearchInfo,HttpServletResponse response) throws IOException {
        List<CompanySyncStatus> pbcSyncStatusList = new ArrayList<>();
        pbcSyncStatusList.add(CompanySyncStatus.buTongBu);
        pbcSyncStatusList.add(CompanySyncStatus.tongBuChengGong);
        List<CompanySyncStatus> eccsSyncStatusList = new ArrayList<>();
        eccsSyncStatusList.add(CompanySyncStatus.buTongBu);
        eccsSyncStatusList.add(CompanySyncStatus.tongBuChengGong);
        List<CompanyAmsCheckStatus> pbcCheckStatusList = new ArrayList<>();
        pbcCheckStatusList.add(CompanyAmsCheckStatus.NoCheck);
        pbcCheckStatusList.add(CompanyAmsCheckStatus.CheckPass);
        accountBillsAllSearchInfo.setPbcSyncStatuses(pbcSyncStatusList);
        accountBillsAllSearchInfo.setEccsSyncStatuses(eccsSyncStatusList);
        accountBillsAllSearchInfo.setPbcCheckStatuses(pbcCheckStatusList);
        this.preParameterInit(accountBillsAllSearchInfo, "");
        IExcelExport iExcelExport = allBillsPublicService.statisticsForKXHtailsExport(accountBillsAllSearchInfo);
        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode("开变销统计列表", "UTF-8")+".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",iExcelExport);
        toClient.flush();
        response.getOutputStream().close();
    }
}
