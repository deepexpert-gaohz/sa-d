package com.ideatech.ams.controller.account;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.*;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BatchSuspendSourceEnum;
import com.ideatech.ams.account.service.*;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.compare.dto.CustomerAbnormalSearchDto;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.image.service.ImageAllService;
import com.ideatech.ams.pbc.dto.AmsPrintInfo;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.operateLog.service.OperateLogService;
import com.ideatech.ams.system.batch.enums.BatchTypeEnum;
import com.ideatech.ams.system.batch.service.BatchService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.vo.BatchSuspendExcelRowVo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.ImportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.SecurityUtils;
import com.ideatech.common.util.XSSUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebMethod;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/allAccountPublic")
@Slf4j
public class AllAccountPublicController {

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private AllAccountPublicService allAccountPublicService;

    @Autowired
    private AccountPublicService accountPublicService;

    @Autowired
    private BatchSuspendService batchSuspendService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AccountChangeSummaryService accountChangeSummaryService;

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private BatchService batchService;

    @Autowired
    private ImageAllService imageAllService;

    @Autowired
    private CustomerPublicLogService customerPublicLogService;

    @Autowired
    private OperateLogService operateLogService;

    @Value("${ams.company.daibulu.syncModel:auto}")
    private String syncModel;

    @Value("${ams.company.pbc.eccs:true}")
    private String syncEccs;

    @Value(("${import.file.pbcCoverCore:false}"))
    private boolean pbcCoverCore;

    @Autowired
    private ConfigService configService;

    @Autowired
    private AccountImageService accountImageService;

    private final static String UPDATE_ACCTTYPE_ATTR = "updateAccttype";

    @RequestMapping(value = "/form/submit", method = RequestMethod.POST)
    public Map<String, Object> submit(HttpServletRequest request) throws Exception {
        AllBillsPublicDTO allBillsPublic = null;
        Map<String, Object> map = new HashMap<>();
        Map<String, String> formData = getFormData(request);

        log.info("开始调用/form/submit方法......");
        //初始补录提交
        if ("addInfoForm".equals(formData.get("action"))) {
            allBillsPublic = allBillsPublicService.submitRecord(SecurityUtils.getCurrentUserId(), formData, formData.get("action"));
            //记录操作日志 ceshi
            allBillsPublicService.writeLog(formData, allBillsPublic.getRefBillId());
            /*if(formData.containsKey("tempId")){
                accountImageService.updateAccountImage(allBillsPublic.getAccountId(),allBillsPublic.getRefBillId(),formData.get("tempId"));
            }*/
        } else {
            if (!StringUtils.equals(formData.get("action"), "keepForm")) { //保持时不校验
                allBillsPublicService.toValidate(formData);
            }

            // 保存数据库AllPublicAccountFormProcessor
            allBillsPublic = allBillsPublicService.submit(SecurityUtils.getCurrentUserId(), formData);

            /*if(formData.containsKey("tempId")){
                accountImageService.updateAccountImage(allBillsPublic.getAccountId(),allBillsPublic.getRefBillId(),formData.get("tempId"));recId
            }*/
            //记录操作日志
            allBillsPublicService.writeLog(formData, allBillsPublic.getRefBillId());
            if (StringUtils.equals(formData.get("action"), "syncForm")) {
                // 同步到人行账管系统或信用机构代码
                return allBillsPublicService.synchronizeData(formData, allBillsPublic);
            }
            setCustomerIdAndAcctId(allBillsPublic.getRefBillId(), allBillsPublic.getAcctId());
        }

        if (allBillsPublic != null) {
            map.put("refBillId", allBillsPublic.getRefBillId() == null ? "" : String.valueOf(allBillsPublic.getRefBillId()));
        }

        return map;

    }

    private void setCustomerIdAndAcctId(Long acctBillsId, Long acctId) {
        //设置客户id和账户id
        AllBillsPublicDTO dto = allBillsPublicService.findOne(acctBillsId);
        Long customerId = null;
        if (dto != null) {
            if (dto.getCustomerLogId() != null) {
                CustomerPublicLogInfo log = customerPublicLogService.getOne(dto.getCustomerLogId());
                if (log != null) {
                    if (log.getCustomerId() != null) {
                        customerId = log.getCustomerId();
                    }
                }
            }
        }
        imageAllService.setCustomerIdAndAcctId(acctBillsId, customerId, acctId);
    }

    @RequestMapping(value = "/form/reject", method = RequestMethod.POST)
    public Map<String, String> reject(Long formId, HttpServletRequest request) throws Exception {
        Map<String, String> formData = getFormData(request);
        //驳回操作
        return allBillsPublicService.reject(SecurityUtils.getCurrentUserId(), formId, formData);
    }

    /**
     * 数据到审核岗后三种形式：1、审核通过 2、审核不通过 3、审核并上报
     *
     * @param formId
     * @param request
     * @return 审核通过
     * @throws Exception
     */
    @RequestMapping(value = "/form/verifyPass", method = RequestMethod.POST)
    public Map<String, String> verifyPass(Long formId, HttpServletRequest request) throws Exception {
        Map<String, String> formData = getFormData(request);
        formData.put("action", "verifyPass");
        //审核通过
        return allBillsPublicService.verifyPass(SecurityUtils.getCurrentUserId(), formId, formData);
    }

    @RequestMapping(value = "/changedItem", method = RequestMethod.GET)
    public Map<String, String> changedItem(Long billId) {
        return accountChangeSummaryService.findByAccountChangeItems(billId);
    }

    @RequestMapping(value = "/changedItemShow", method = RequestMethod.GET)
    public String changedItemShow() {
        List<ConfigDto> list = configService.findByKey("changeItemShow");
        ConfigDto configDto = null;
        if(CollectionUtils.isNotEmpty(list)){
            configDto = list.get(0);
            return configDto.getConfigValue();
        }
        //默认返回true
        return "true";
    }

    @RequestMapping(value = "/getChangeItems", method = RequestMethod.GET)
    public JSONArray getChangeItems(Long billId) {
        return accountChangeSummaryService.findByAccountChangeItemsAll(billId);
    }

    /**
     * 根据id获取最新流水信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public ObjectRestResponse<AllAccountPublicDTO> getFormInitData(Long id) {
        return allAccountPublicService.getDetailsByAccountId(id);
    }

    /**
     * 大类账户类型根据accountId变更小类账户类型
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/updateAcctType", method = RequestMethod.POST)
    public void updateAcctType(Long accountId, CompanyAcctType acctType, Long refBillId) {
        accountPublicService.updateAcctType(accountId, acctType, refBillId);
    }

    /**
     * 最新流水表
     *
     * @param info
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableResultResponse<AllAccountPublicSearchDTO> pre(AllAccountPublicSearchDTO info, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        String certainOrganFullId = null;
        if (StringUtils.isNotBlank(info.getKernelOrgCode())) {
            OrganizationDto od = organizationService.findByCode(info.getKernelOrgCode().trim());
            if (od == null) {
                TableResultResponse<AllAccountPublicSearchDTO> tableResultResponse = new TableResultResponse<>();
                tableResultResponse.setRows(new ArrayList<AllAccountPublicSearchDTO>());
                return tableResultResponse;
            } else {
                certainOrganFullId = od.getFullId();
            }
        }

        TableResultResponse<AllAccountPublicSearchDTO> tableResultResponse = allAccountPublicService.query(info, certainOrganFullId, pageable);

        return tableResultResponse;
    }

    /**
     * 根据客户id获取所有流程结束的账户信息
     *
     * @param id       客户主表id
     * @param pageable 分页参数
     */
    @RequestMapping(value = "/listForFinished", method = RequestMethod.GET)
    public TableResultResponse<AllAccountPublicSearchDTO> pre2(Long id, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        TableResultResponse<AllAccountPublicSearchDTO> tableResultResponse = allAccountPublicService.query(id, pageable);

        return tableResultResponse;
    }

    /**
     * 生成流水id
     *
     * @return
     */
    @RequestMapping(value = "/getBillId", method = RequestMethod.GET)
    public Long getBillId() {
        return allBillsPublicService.getRecId();
    }

    private Map<String, String> getFormData(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> formData = new HashMap<String, String>();
        Set<String> keys = parameterMap.keySet();
        for (String key : keys) {
            if(StringUtils.equals("changeFields",key)){
                System.out.println(key);
            }
            String[] values = parameterMap.get(key);
            if(key.contains("currency0")){
                String val = "";
                for(String a : values){
                    val += a + ",";
                }
                values = new String[1];
                values[0] = val.substring(0,val.length() -1);
                log.info("values : " + values);
            }
            if (ArrayUtils.isNotEmpty(values)) {
                if (values.length > 1) {
                    throw new RuntimeException("字段" + key + "不支持多值");
                } else {
                    if (StringUtils.equals(values[0], "null")) {
                        values[0] = null;
                    }
                    if (StringUtils.equals(values[0], "undefined")) {
                        values[0] = null;
                    }
                    if(key.contains("currency0")){
                    //XSSUtil.stripXSS 防止字段值不规范引起XSS攻击
                        formData.put("currency0", XSSUtil.stripXSS(values[0]));
                    }else{
                    formData.put(key, XSSUtil.stripXSS(values[0]));
                    }
                }
            }
        }
        String[] changeFields = request.getParameterValues("changeFields");
        if (ArrayUtils.isNotEmpty(changeFields)) {
            formData.put("changeFields", changeFields[0]);
        }
        return formData;
    }


    @PostMapping("/suspend/upload")
    @WebMethod(exclude = true)
    public ResultDto upload(@RequestParam("file") MultipartFile file) {
        try {
            ImportExcel importExcel = new ImportExcel(file, 0, 0);
            if (importExcel.getRow(0).getPhysicalNumberOfCells() != 6) {
                return ResultDtoFactory.toNack("导入失败，错误的模板");
            }
            String batchNo = batchService.createBatch(file.getOriginalFilename(), file.getSize(), BatchTypeEnum.BATCH_SUSPEND);
            List<BatchSuspendExcelRowVo> dataList = importExcel.getDataList(BatchSuspendExcelRowVo.class);
            List<BatchSuspendDto> batchSuspendEntryDtos = ConverterService.convertToList(dataList, BatchSuspendDto.class);
            batchSuspendService.initSave(batchNo, BatchSuspendSourceEnum.HTML_UPLOAD, batchSuspendEntryDtos);
            batchSuspendService.doProcess(batchNo);
            batchService.finishBatch(batchNo);
            return ResultDtoFactory.toAckData(dataList.size());
        } catch (Exception e) {
            log.error("导入批量久悬数据失败", e);
            return ResultDtoFactory.toNack("导入批量久悬数据失败");
        }
    }

    /**
     * 传入选择的流水ID 从数据库查询数据进行久悬（沿用文件上报处理的方式进行批量勾选久悬上报）
     * @param ids
     * @return
     */
    @PostMapping("/selectSuspend")
    public ResultDto selectSuspend(@RequestParam("ids[]") Long[] ids) {
        int size = ids.length;
        try {
            //参数fileSize在勾选上报的情况下用勾选的数量传入
            String batchNo = batchService.createBatch("批量勾选久悬上报", new Long(size), BatchTypeEnum.BATCH_SUSPEND);
            List<BatchSuspendDto> batchSuspendEntryDtos = getBatchSuspendDto(ids);
            batchSuspendService.initSave(batchNo, BatchSuspendSourceEnum.HTML_UPLOAD, batchSuspendEntryDtos);
            batchSuspendService.doProcess(batchNo);
            batchService.finishBatch(batchNo);
        }catch (Exception e) {
            log.error("导入批量久悬数据失败", e);
            return ResultDtoFactory.toNack("勾选久悬上报失败");
        }
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/suspend/process")
    public ResultDto suspendProcess() {
        BatchSuspendDto batchSuspendDto = batchSuspendService.isProcessing();
        Map<String, Object> map = new HashMap<>();
        if (batchSuspendDto != null) {
            map.put("process", true);
            map.put("batchNo", batchSuspendDto.getBatchNo());
        } else {
            map.put("process", false);
        }
        return ResultDtoFactory.toAckData(map);
    }

    @GetMapping("/suspend/process/{batchNo}")
    public ObjectRestResponse suspendProcessInfo(@PathVariable("batchNo") String batchNo) {
        return batchSuspendService.getProcessByBatchNo(batchNo);
    }

    @PutMapping("/suspend/process/{batchNo}")
    public ResultDto finishBatch(@PathVariable("batchNo") String batchNo) {
        batchSuspendService.finishByBatchNo(batchNo);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/getChangeAuthority")
    public Boolean getChangeAuthority(String depositorName) {
        Boolean a = accountsAllService.getChangeAuthority(depositorName);

        return a;
    }

    @GetMapping("/updateAcctTypeFromPbc")
    public ResultDto updateAcctTypeFromPbc() {
        if (servletContext.getAttribute(UPDATE_ACCTTYPE_ATTR) != null) {
            return ResultDtoFactory.toNack("后台批处理任务正在运行");
        }
        servletContext.setAttribute(UPDATE_ACCTTYPE_ATTR, "");

        if (pbcCoverCore) {

            log.info("人行账管数据更新开始");
            long size = accountBillsAllService.updateBills();
            log.info("人行账管数据更新后台进行中，size={}", size);
            servletContext.removeAttribute(UPDATE_ACCTTYPE_ATTR);
            if (size == 0) {
                return ResultDtoFactory.toNack("没有需要处理的数据");
            }
            return ResultDtoFactory.toNack("后台更新中...");
        }

        log.info("大类转小类处理前");
        long size = accountBillsAllService.updateAcctTypeFromPbc();
        log.info("大类转小类处理后，size={}", size);
        servletContext.removeAttribute(UPDATE_ACCTTYPE_ATTR);
        if (size == 0) {
            return ResultDtoFactory.toNack("没有需要处理的数据");
        }
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/checkSyncModel")
    public String checkSyncModel() {
        return syncModel + "," + syncEccs;
    }

    /**
     * 存量数据
     *
     * @param info
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/stockList", method = RequestMethod.GET)
    public TableResultResponse<AllAccountPublicDTO> stockList(AllAccountPublicDTO info, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        TableResultResponse<AllAccountPublicDTO> tableResultResponse = allAccountPublicService.queryStockList(info, pageable);

        return tableResultResponse;
    }

    /**
     * 影像补录
     *
     * @param acctNo
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/changeImageStatus/{acctNo}")
    public ObjectRestResponse changeImageStatus(@PathVariable("acctNo") String acctNo) throws Exception {
        AccountsAllInfo accountsAllInfo = accountsAllService.findByAcctNo(acctNo);
        if (accountsAllInfo == null) {
            return new ObjectRestResponse<>().rel(false).result("acctNo无效");
        }
        if (!"1".equals(accountsAllInfo.getString003())) {
            return new ObjectRestResponse<>().rel(false).result("非存量数据");
        }
        if ("1".equals(accountsAllInfo.getString004())) {
            return new ObjectRestResponse<>().rel(false).result("该数据影像已补录");
        }
        accountsAllInfo.setString004("1");
        accountsAllService.save(accountsAllInfo);

        AccountPublicInfo api = accountPublicService.findByAcctNo(acctNo);
        if (api != null && "1".equals(api.getString003())
                && (api.getString004() == null || "0".equals(api.getString004()))) {
            api.setString004("1");
            accountPublicService.save(api);
        }
        return new ObjectRestResponse<>().rel(true).result("");
    }


    /**
     * 白名单列表删除后删除对应的流水信息
     *
     * @param
     * @return
     */
    @GetMapping(value = "/deleteAccountsAll")
    public boolean updateAcctType(Long id) {
        boolean result = accountPublicService.deleteAccountsAll(id);
        return result;
    }

    /**
     * 获取指定客户的第一个账户的第一笔流水id
     *
     * @return
     */
    @GetMapping(value = "/getFirstBillByCustomerNo")
    public ObjectRestResponse getFirstBillByCustomerNo(String customerNo) {
        AccountBillsAllInfo abai = accountsAllService.getFirstBillByCustomerNo(customerNo);
        return new ObjectRestResponse<>().result(abai);
    }

    /**
     * 根据CustomerId获取指定客户的第一个账户的第一笔流水id
     *
     * @return
     */
    @GetMapping(value = "/getFirstBillByCustomerId")
    public ObjectRestResponse getFirstBillByCustomerId(Long customerId) {
        AccountBillsAllInfo abai = accountsAllService.getFirstBillByCustomerId(customerId);
        return new ObjectRestResponse<>().result(abai);
    }

    /**
     * 导出已开立其他银行账户清单
     */
    @RequestMapping(value = "/otherBankAccount/export", method = RequestMethod.POST)
    public void exportXLS(String depositorName, String accountKey, String cancelDate, String allAccountData, HttpServletResponse response) {
        List<AmsPrintInfo> apiList = JSONObject.parseArray(allAccountData, AmsPrintInfo.class);
        HSSFWorkbook wb = accountsAllService.exportExcel(depositorName, accountKey, cancelDate, apiList);
        try {
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode("已开立其他银行账户清单", "UTF-8") + ".xls");
            response.setContentType("application/vnd.ms-excel");
            OutputStream fileOut = response.getOutputStream();
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据客户名称获取该客户基本户信息
     * 若没有基本户，返回null
     */
    @RequestMapping(value = "/findAccountByDepositorName", method = RequestMethod.GET)
    public ObjectRestResponse findAccountByDepositorName(String depositorName) {
        AccountsAllInfo accountsAllInfo = null;
        try {
            accountsAllInfo = accountsAllService.findByDepositorName(depositorName);
        } catch (Exception e) {
            return new ObjectRestResponse<>().rel(false).msg(e.getMessage());
        }
        return new ObjectRestResponse<>().rel(true).result(accountsAllInfo);
    }

    @GetMapping(value = "/companyAccountExport")
    public void export(AllAccountPublicSearchDTO info, HttpServletResponse response) throws IOException {

        IExcelExport iExcelExport = allAccountPublicService.query(info);
        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode("账户信息统计列表", "UTF-8")+".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",iExcelExport);
        toClient.flush();
        response.getOutputStream().close();
    }

    public List<BatchSuspendDto> getBatchSuspendDto(Long[] ids){
        List<BatchSuspendDto> batchSuspendDtos = new ArrayList<>();
        List<Long> idsList = new ArrayList<>();
        for(Long id :ids){
            idsList.add(id);
        }
        List<AccountBillsAllInfo> accountBillsAllInfos = accountBillsAllService.findByIdIn(idsList);
        if(CollectionUtils.isNotEmpty(accountBillsAllInfos)){
            for(AccountBillsAllInfo a : accountBillsAllInfos){
                BatchSuspendDto batchSuspendDto = new BatchSuspendDto();
                batchSuspendDto.setAcctNo(a.getAcctNo());
                batchSuspendDto.setAcctType(a.getAcctType());
                batchSuspendDto.setDepositorName(a.getDepositorName());
                batchSuspendDto.setOrganFullId(a.getOrganFullId());
                batchSuspendDtos.add(batchSuspendDto);
            }
        }
        return batchSuspendDtos;
    }
}
