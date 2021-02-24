package com.ideatech.ams.controller.kyc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.compare.dto.CompareResultDto;
import com.ideatech.ams.compare.service.CompareResultService;
import com.ideatech.ams.compare.service.CompareTaskService;
import com.ideatech.ams.compare.service.CustomerAbnormalService;
import com.ideatech.ams.customer.dto.SaicMonitorDto;
import com.ideatech.ams.customer.enums.SaicMonitorEnum;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.kyc.dto.*;
import com.ideatech.ams.kyc.dto.poi.SaicPoi;
import com.ideatech.ams.kyc.entity.Beneficiary;
import com.ideatech.ams.kyc.entity.ChangeMess;
import com.ideatech.ams.kyc.entity.SaicInfo;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.service.*;
import com.ideatech.ams.system.batch.enums.BatchTypeEnum;
import com.ideatech.ams.system.batch.service.BatchService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.template.dto.TemplateDto;
import com.ideatech.ams.system.template.service.TemplateService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.vo.CustomerTuneExcelRowVo;
import com.ideatech.ams.ws.api.service.BlacklistValidationService;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.ImportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.Future;

@RestController
@RequestMapping(value = "/kyc")
@Slf4j
class SaicController {
    @Autowired
    private KycSearchHistoryService kycSearchHistoryService;
    @Autowired
    private UserService userService;

    @Autowired
    private SaicInfoService saicInfoService;
    
    @Autowired
    private StockHolderService stockHolderService;
    
    @Autowired
    private EquityShareService equityShareService;
    
    @Autowired
    private BeneficiaryService beneficiaryService;
    
    @Autowired
    private ManagementService managementService;
    
    @Autowired
    private BaseAccountService baseAccountService;
    
    @Autowired
    private ChangeMessService changeMessService;

    @Autowired
    private ChangeRecordService changeRecordService;
    
    @Autowired
    private IllegalService illegalService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private BlacklistValidationService blacklistValidationService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private BatchService batchService;

    @Autowired
    private SaicMonitorService saicMonitorService;

    @Autowired
    private CompareTaskService compareTaskService;

    @Autowired
    private CustomerAbnormalService customerAbnormalService;

    @Autowired
    private CompareResultService compareResultService;

    @Autowired
    private JudicialInformationService judicialInformationService;

    /**
     *
     * @param keyword  查询企业
     * @param searchType 查询操作的类型(区分客户尽调查询与实时查询)
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/saic/basic", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<SaicInfoDto> base(String keyword, SearchType searchType) throws UnsupportedEncodingException {
        if(searchType == null) {  //默认实时查询
            searchType = SearchType.REAL_TIME;
        }
        return querySaic(keyword, searchType, true);
//        return querySaic(keyword, SearchType.REAL_TIME, true);
    }

    @RequestMapping(value = "/saic/realTime", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<SaicInfoDto> queryRealTime(String keyword) throws UnsupportedEncodingException {
        return querySaic(keyword, SearchType.REAL_TIME, false);
    }

    @RequestMapping(value = "/saic/exact", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<SaicInfoDto> queryExact(String keyword) throws UnsupportedEncodingException {
        return querySaic(keyword, SearchType.EXACT, false);
    }

    /**
     * 客户尽调-工商股权结构
     * @param saicInfoId 工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/equityshare", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<JSONObject> kycEquityshare(Long saicInfoId) {
      if(saicInfoId == null){
        log.info("工商ID为空");
        return new ObjectRestResponse<JSONObject>().rel(false).msg("工商ID为空");
      } else {
        UserDto userDto = userService.findByUsername(SecurityUtils.getCurrentUsername());
        if (userDto == null) {
          return new ObjectRestResponse<SaicInfoDto>().rel(false).msg("系统获取用户信息请求超时，请稍后重试");
        }
        JSONObject resultJson = equityShareService.getEquityShareTreeJsonObject(SecurityUtils.getCurrentUsername(),saicInfoId,
        		SecurityUtils.getCurrentOrgFullId());
        return new ObjectRestResponse<JSONObject>().rel(true).result(resultJson);
      }
    }

    /**
     * 客户尽调-受益人
     * @param saicInfoId 工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/beneficiary", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<Beneficiary>> kycBeneficiary(Long saicInfoId,Boolean naturalPerson) {
        if(saicInfoId == null){
            log.info("工商ID为空");
            return new ObjectRestResponse<List<Beneficiary>>().rel(false).msg("工商ID为空");
        } else {
            UserDto userDto = userService.findByUsername(SecurityUtils.getCurrentUsername());
            if (userDto == null) {
                return new ObjectRestResponse<SaicInfo>().rel(false).msg("系统获取用户信息请求超时，请稍后重试");
            }
            List<BeneficiaryDto> beneficiaryList = beneficiaryService.getBeneficiaryListBySaicInfoId(SecurityUtils.getCurrentUsername(),saicInfoId,SecurityUtils.getCurrentOrgFullId());

            //开户时校验管理调用的，直接返回数据
            if(naturalPerson == null){
                return new ObjectRestResponse<List<Beneficiary>>().rel(true).result(beneficiaryList);
            }

            //客户尽调时，分类返回数据
            if(beneficiaryList!=null){
                Iterator<BeneficiaryDto> it = beneficiaryList.iterator();
                List<String> typeList = new ArrayList<>(Arrays.asList("自然人股东","自然人", "个人","境内中国公民","非农民自然人","外籍自然人",
                        "台湾居民","外国公民","华侨","香港居民","合伙人","农民自然人","其他股东","个人独资","普通合伙人","有限合伙人"));
                while(it.hasNext()){
                    BeneficiaryDto x = it.next();
                    //类型已知时
                    if(x.getType()!=null){
                        if (naturalPerson){
                            //要自然人，移除不是自然人股东类型的
                            if(!typeList.contains(x.getType())){
                                it.remove();
                            }
                        }else {
                            //要非自然人，移除自然人股东类型的
                            if(typeList.contains(x.getType())){
                                it.remove();
                            }
                        }
                        //类型未知时
                    }else {
                        if (naturalPerson){
                            //要自然人，移除名字大于8字节的
                            if (x.getName().length()>8){
                                it.remove();
                            }
                        }else {
                            //要非自然人，移除名称小于等于8字节的
                            if (x.getName().length()<=8){
                                it.remove();
                            }
                        }
                    }
                }
            }

            return new ObjectRestResponse<List<Beneficiary>>().rel(true).result(beneficiaryList);
        }
    }
    

    /**
     * 客户尽调-信息补录(实际控制人)
     * @param supplementQueryVo 查询VO
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/supplement", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<JSONObject> kycSupplement(@Valid SupplementQueryDto supplementQueryVo, BindingResult bindingResult) {
      if(bindingResult.hasErrors()){
        String errorMsg = bindingResult.getFieldError().getDefaultMessage();
        return new ObjectRestResponse<JSONObject>().rel(false).msg(errorMsg);
      } else {
        JSONObject jsonObject = saicInfoService.getCompanyPeopleInformation(supplementQueryVo);
        return new ObjectRestResponse<JSONObject>().rel(true).result(jsonObject);
      }
    }


    /**
     * 客户尽调-信息补录
     * @param supplementDto
     * @param bindingResult
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/supplement", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Object> saveKycSupplement(@Valid SupplementDto supplementDto, BindingResult bindingResult) {
      if(bindingResult.hasErrors()){
        String errorMsg = bindingResult.getFieldError().getDefaultMessage();
        return new ObjectRestResponse<Object>().rel(false).msg(errorMsg);
      } else {
          SupplementDto saved = saicInfoService.saveCompanyPeopleInformation(supplementDto);
          return new ObjectRestResponse<Object>().rel(true).result(saved);
      }
    }
    
    /**
     * 客户尽调-股东信息
     * @param saicInfoId 内部工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/stockholders", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<StockHolderDto>> kycStockHolders(Long saicInfoId, Boolean naturalPerson) {

        if (saicInfoId == null) {
            log.info("工商ID为空");
            return new ObjectRestResponse<List<StockHolderDto>>().rel(false).msg("工商ID为空");
        } else {
            List<StockHolderDto> stockHolderList = stockHolderService.findBySaicInfoId(saicInfoId);

            //开户时校验管理调用的，直接返回数据
            if (naturalPerson == null) {
                return new ObjectRestResponse<List<StockHolderDto>>().rel(true).result(stockHolderList);
            }

            //客户尽调时，分类返回数据
            if (stockHolderList != null) {
                Iterator<StockHolderDto> it = stockHolderList.iterator();
                List<String> typeList = new ArrayList<>(Arrays.asList("自然人股东", "自然人", "个人", "境内中国公民", "非农民自然人", "外籍自然人",
                        "台湾居民", "外国公民", "华侨", "香港居民", "合伙人", "农民自然人", "其他股东", "个人独资", "普通合伙人", "有限合伙人"));
                while (it.hasNext()) {
                    StockHolderDto x = it.next();
                    //类型已知时
                    if (x.getStrtype() != null) {
                        if (naturalPerson) {
                            //要自然人，移除不是自然人股东类型的
                            if (!typeList.contains(x.getStrtype())) {
                                it.remove();
                            }
                        } else {
                            //要非自然人，移除自然人股东类型的
                            if (typeList.contains(x.getStrtype())) {
                                it.remove();
                            }
                        }
                        //类型未知时
                    } else {
                        if (naturalPerson) {
                            //要自然人，移除名字大于8字节的
                            if (x.getName().length() > 8) {
                                it.remove();
                            }
                        } else {
                            //要非自然人，移除名称小于等于8字节的
                            if (x.getName().length() <= 8) {
                                it.remove();
                            }
                        }
                    }
                }
            }

            return new ObjectRestResponse<List<StockHolderDto>>().rel(true).result(stockHolderList);
        }
    }

    /**
     * 客户尽调-工商年报
     * @param saicInfoId 内部工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = {"/report","/history/report"}, method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<ReportDto>> kycReport(Long saicInfoId){

      if(saicInfoId == null){
        log.info("工商ID为空");
        return new ObjectRestResponse<List<ReportDto>>().rel(false).msg("工商ID为空");
      } else {
        List<ReportDto> saicReportList = reportService.findBySaicInfoId(saicInfoId);
        return new ObjectRestResponse<List<ReportDto>>().rel(true).result(saicReportList);
      }
    }


    /**
     * 客户尽调-董监高
     * @param saicInfoId 内部工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/managers", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<JSONObject> kycManagers(Long saicInfoId) throws UnsupportedEncodingException {

      if(saicInfoId == null){
        log.info("工商ID为空");
        return new ObjectRestResponse<JSONObject>().rel(false).msg("工商ID为空");
      } else {
        JSONObject jsonObject = managementService.getManagersInfoBySaicInfoIdInLocal(saicInfoId);

        return new ObjectRestResponse<JSONObject>().rel(true).result(jsonObject);
      }
    }
    
    
    /**
     * 客户尽调-基本户履历
     * @param saicInfoId 工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/baseaccount", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<BaseAccountDto>> kycBaseAccount(Long saicInfoId) {
      if(saicInfoId == null){
        log.info("工商ID为空");
        return new ObjectRestResponse<BaseAccountDto>().rel(false).msg("工商ID为空");
      } else {
        UserDto userDto = userService.findByUsername(SecurityUtils.getCurrentUsername());
        if (userDto == null) {
          return new ObjectRestResponse<BaseAccountDto>().rel(false).msg("系统获取用户信息请求超时，请稍后重试");
        }
        List<BaseAccountDto> baseAccountList = baseAccountService.getBaseAccountListBySaicInfoId(SecurityUtils.getCurrentUsername(),saicInfoId,
        		SecurityUtils.getCurrentOrgFullId());
        return new ObjectRestResponse<BaseAccountDto>().rel(true).result(baseAccountList);
      }
    }

    /**
     * 客户尽调-工商信息变更
     * @param saicInfoId 内部工商ID
     * @return
     */
    @GetMapping("/changes")
    public ResultDto<List<ChangeRecordDto>> kycChanegs(Long saicInfoId) {
        if (saicInfoId == null) {
            log.warn("工商ID为空");
            return ResultDtoFactory.toNack("工商ID为空");
        } else {
            List<ChangeRecordDto> changes = changeRecordService.findBySaicInfoId(saicInfoId);
            return ResultDtoFactory.toAckData(changes);
        }
    }
    
    /**
     * 客户尽调-经营异常-历史
     * @param saicInfoId 内部工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/changemess", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<ChangeMessDto>> kycChangeMess(Long saicInfoId) throws UnsupportedEncodingException {

        if(saicInfoId == null){
          log.info("工商ID为空");
          return new ObjectRestResponse<List<ChangeMess>>().rel(false).msg("工商ID为空");
        } else {
          List<ChangeMessDto> changeMessList = changeMessService.findBySaicInfoId(saicInfoId);
          return new ObjectRestResponse<List<ChangeMess>>().rel(true).result(changeMessList);
        }
      }
    

    /**
     * 客户尽调-严重违法信息
     * @param saicInfoId 内部工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/illegals", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<IllegalDto>> kycIllegals(Long saicInfoId) throws UnsupportedEncodingException {

      if(saicInfoId==null){
        log.info("工商ID为空");
        return new ObjectRestResponse<List<IllegalDto>>().rel(false).msg("工商ID为空");
      } else {
        List<IllegalDto> illegalList = illegalService.findBySaicInfoId(saicInfoId);
        return new ObjectRestResponse<List<IllegalDto>>().rel(true).result(illegalList);
      }
    }
    

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadKycResultExcel(Long saicInfoId,HttpServletRequest request, HttpServletResponse response)
            throws Exception {
      if(saicInfoId == null){ 
        log.info("查询企业ID为空");
        return;
      }

      //工商基础信息
      SaicInfoDto saicInfoDto = saicInfoService.findById(saicInfoId);
      SaicPoi saicPoi = new SaicPoi();
      BeanUtils.copyProperties(saicInfoDto,saicPoi);

      //基本信息-excel
      IExcelExport saicRecordExport = saicInfoService.generateSaicReport(saicInfoId,saicPoi);

      //最终受益人-excel
      IExcelExport excelExport = saicInfoService.generateBeneficiaryReport(saicInfoId,saicInfoDto.getName());

      response.setHeader("Content-disposition", "attachment; filename="+generateFileName(saicInfoDto,request)+".xls");
      response.setContentType("application/octet-stream");
      OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
      ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",saicRecordExport,excelExport);
      toClient.flush();
      response.getOutputStream().close();
    }
    
    @SuppressWarnings("unchecked")
	private ObjectRestResponse<SaicInfoDto> querySaic(String keyword, SearchType type, boolean base) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(keyword)) {
            log.info("查询关键字为空");
            return new ObjectRestResponse<SaicInfoDto>().rel(false).msg("查询关键字为空");
        } else {
            keyword = keyword.replaceAll("\\s*", "");
            UserDto userDto = userService.findByUsername(SecurityUtils.getCurrentUsername());
            SaicMonitorDto saicMonitorDto = null;
            if (userDto == null) {
                return new ObjectRestResponse<SaicInfoDto>().rel(false).msg("系统获取用户信息请求超时，请稍后重试");
            }
            if (base) {
                SaicInfoDto saicInfoDto = saicInfoService.getSaicInfoBase(type, SecurityUtils.getCurrentUsername(), URLDecoder.decode(keyword, "UTF-8"),
                        SecurityUtils.getCurrentOrgFullId());
                //保存工商查询到工商统计表数据库  方便后续统计
                log.info(keyword + "工商查询添加进工商统计表......");
                if(saicInfoDto != null){
                    saicMonitorDto = saicMonitorService.getSaicMonitor(userDto.getUsername(),userDto.getOrgId(),keyword,saicInfoDto.getId(),StringUtils.isNotBlank(saicInfoDto.getUnitycreditcode()) ? saicInfoDto.getUnitycreditcode() : saicInfoDto.getRegistno(),SaicMonitorEnum.KYC);
                    if(saicInfoDto.getLastUpdateDate() != null) {
                        try {
                            saicInfoDto.setLastUpdateDays(DateUtils.daysBetween(saicInfoDto.getLastUpdateDate(), new Date()));
                        } catch (ParseException e) {
                            log.error("工商数据最近更新时间转换出错", e);
                        }
                    }
                }else{
                    saicMonitorDto = saicMonitorService.getSaicMonitor(userDto.getUsername(),userDto.getOrgId(),keyword,null,"",SaicMonitorEnum.KYC);
                }
                saicMonitorService.save(saicMonitorDto);
                return new ObjectRestResponse<SaicInfoDto>().rel(true).result(saicInfoDto);
            } else {
                SaicIdpInfo saicIdpInfo = saicInfoService.getSaicInfoFull(type, SecurityUtils.getCurrentUsername(), URLDecoder.decode(keyword, "UTF-8"),
                        SecurityUtils.getCurrentOrgFullId());
                log.info(keyword + "工商查询添加进工商统计表......");
                if(saicIdpInfo != null){
                    saicMonitorDto = saicMonitorService.getSaicMonitor(userDto.getUsername(),userDto.getOrgId(),keyword,saicIdpInfo.getId(),StringUtils.isNotBlank(saicIdpInfo.getUnitycreditcode()) ? saicIdpInfo.getUnitycreditcode() : saicIdpInfo.getRegistno(),SaicMonitorEnum.KYC);
                }else{
                    saicMonitorDto = saicMonitorService.getSaicMonitor(userDto.getUsername(),userDto.getOrgId(),keyword,null,"",SaicMonitorEnum.KYC);
                }
                saicMonitorService.save(saicMonitorDto);
                return new ObjectRestResponse<SaicInfoDto>().rel(true).result(saicIdpInfo);
            }
        }
    }

    /**
     * 生成导出文件名
     * @param saicInfoDto
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    private String generateFileName(SaicInfoDto saicInfoDto,HttpServletRequest request) throws UnsupportedEncodingException {
      StringBuilder fileName = new StringBuilder();
      fileName.append(BrowserUtil.generateFileName(saicInfoDto.getName(),request))
              .append("-").append(DateUtils.DateToStr(saicInfoDto.getCreatedDate(),"yyyy-MM-dd"));
      return fileName.toString();
    }
    /****************************************************历史********************************************************/
    /**
     * 查询历史列表
     * @param kycSearchHistorySearchDto
     * @return
     */
    @GetMapping("/history/list")
    public ResultDto kycSaicHistoryPage(KycSearchHistorySearchDto kycSearchHistorySearchDto) {
        String orgFullId = SecurityUtils.getCurrentUser().getOrgFullId();
        kycSearchHistorySearchDto.setOrgFullId(orgFullId);
        kycSearchHistorySearchDto = kycSearchHistoryService.search(kycSearchHistorySearchDto);
        List<KycSearchHistoryDto> list = kycSearchHistorySearchDto.getList();
        for(KycSearchHistoryDto kyc : list){
            if(StringUtils.isNotBlank(kyc.getUsername())){
                UserDto byUsername = userService.findByUsername(kyc.getUsername());
                if(byUsername != null){
                    kyc.setCname(byUsername.getCname());
                }else{
                    kyc.setCname(kyc.getUsername());
                }
            }
            if(StringUtils.isNotBlank(kyc.getOrgfullid())){
                OrganizationDto byOrganFullId = organizationService.findByOrganFullId(kyc.getOrgfullid());
                if(byOrganFullId != null){
                    kyc.setOrgfullname(byOrganFullId.getName());
                }else{
                    kyc.setOrgfullname(kyc.getOrgfullid());
                }
            }
        }
        return ResultDtoFactory.toAckData(kycSearchHistorySearchDto);
    }

    /**
     * 查询历史列表导出
     * @param kycSearchHistorySearchDto
     * @return
     */
    @GetMapping("/history/historyExport")
    public void kycSaicHistoryExport(KycSearchHistorySearchDto kycSearchHistorySearchDto, HttpServletResponse response) throws IOException {
        String orgFullId = SecurityUtils.getCurrentUser().getOrgFullId();
        kycSearchHistorySearchDto.setOrgFullId(orgFullId);
        IExcelExport iExcelExport = kycSearchHistoryService.searchAll(kycSearchHistorySearchDto);
        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode("客户尽调历史", "UTF-8")+".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",iExcelExport);
        toClient.flush();
        response.getOutputStream().close();
    }

    /**
     * 工商基本信息-历史数据查询
     * @param saicInfoId
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/history/basic", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<SaicInfoDto> historyBase(Long saicInfoId) throws UnsupportedEncodingException {
        return new ObjectRestResponse<SaicInfoDto>().rel(true).result(saicInfoService.findById(saicInfoId));
    }
    /**
     * 客户尽调-工商股权结构-历史数据查询
     * @param saicInfoId 工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/history/equityshare", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<JSONObject> kycEquityshareHistory(Long saicInfoId) {
        return kycEquityshare(saicInfoId);
    }

    /**
     * 客户尽调-受益人-历史数据查询
     * @param saicInfoId 工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/history/beneficiary", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<Beneficiary>> kycBeneficiaryHistory(Long saicInfoId,Boolean naturalPerson) {
        return kycBeneficiary(saicInfoId,naturalPerson);
    }
    /**
     * 客户尽调-股东信息
     * @param saicInfoId 内部工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/history/stockholders", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<StockHolderDto>> kycStockHoldersHistory(Long saicInfoId, Boolean naturalPerson){
        return kycStockHolders(saicInfoId, naturalPerson);
    }
    /**
     * 客户尽调-董监高
     * @param saicInfoId 内部工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/history/managers", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<JSONObject> kycManagersHistory(Long saicInfoId) throws UnsupportedEncodingException {
        return kycManagers(saicInfoId);
    }
    /**
     * 客户尽调-基本户履历
     * @param saicInfoId 工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/history/baseaccount", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<BaseAccountDto>> kycBaseAccountHistory(Long saicInfoId) {
        return kycBaseAccount(saicInfoId);
    }
    /**
     * 客户尽调-经营异常-历史
     * @param saicInfoId 内部工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/history/changemess", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<ChangeMessDto>> kycChangeMessHistory(Long saicInfoId) throws UnsupportedEncodingException {
        return kycChangeMess(saicInfoId);
    }

    /**
     * 客户尽调-工商信息变更-历史
     * @param saicInfoId 内部工商ID
     * @return
     */
    @GetMapping("/history/changes")
    public ResultDto<List<ChangeRecordDto>> kycChangesHistory(Long saicInfoId) {
        return kycChanegs(saicInfoId);
    }
    /**
     * 客户尽调-严重违法信息
     * @param saicInfoId 内部工商ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/history/illegals", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<IllegalDto>> kycIllegalsHistory(Long saicInfoId) throws UnsupportedEncodingException {
        return kycIllegals(saicInfoId);
    }

    @GetMapping("/validation/blacklist")
    public ResultDto validateBlacklist(String name, String bod, String nationality) {
        return blacklistValidationService.check(name, bod, nationality);
    }

    /**getCounts
     * 打印预览
     * @return
     */
    @GetMapping("/getPrintPreview")
    public ResponseEntity<byte[]> getTemplateNameList(Long id,String templateName) throws Exception {
        SaicInfoDto saicInfoDto = saicInfoService.findById(id);

        Map<String, String> describe = new HashMap<>();
        Map<String, Object> describe2 = new HashMap<>();
        for (Field f : saicInfoDto.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if (f.getType() == String.class && f.get(saicInfoDto) == null) { //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
                f.set(saicInfoDto, "");
            }
        }

        describe = org.apache.commons.beanutils.BeanUtils.describe(saicInfoDto);

        describe2.putAll(describe);

        TemplateDto byId = templateService.findByTemplateName(URLDecoder.decode(templateName, "UTF-8"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));

        return new ResponseEntity<byte[]>(PdfGenerator.generate(byId.getTemplaeContent(), describe2), headers, HttpStatus.OK);
    }

    /**
     * 客户尽调综合查询
     * @param file
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/batch/upload",method = RequestMethod.POST)
    @WebMethod(exclude = true)
    public void upload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        log.info("开始导入客户尽调数据...");
        ResultDto dto = new ResultDto();
        List<String> regNoList = new ArrayList<>();

        try {
            ImportExcel importExcel = new ImportExcel(file, 0, 0);
            if (importExcel.getRow(2) == null || (importExcel.getRow(2) != null && importExcel.getRow(2).getPhysicalNumberOfCells() != 2)) {
                ResultDtoFactory.toNack("导入失败，错误的模板");
                dto.setCode(ResultCode.NACK);
                dto.setMessage("导入失败，错误的模板");
                log.info("导入模版错误...");
            } else {
                List<CustomerTuneExcelRowVo> dataList = importExcel.getDataList(CustomerTuneExcelRowVo.class);
                for(Iterator<CustomerTuneExcelRowVo> it = dataList.iterator(); it.hasNext();){
                    CustomerTuneExcelRowVo customerTuneExcelRowVo = it.next();
                    if(StringUtils.isBlank(customerTuneExcelRowVo.getEntname()) || customerTuneExcelRowVo.getEntname().indexOf("公司名称") != -1){
                        it.remove();
                    }
                }

                if(CollectionUtils.isNotEmpty(dataList)) {
                    List<KycSearchHistoryDto> kycSearchHistoryDtoList = ConverterService.convertToList(dataList, KycSearchHistoryDto.class);

                    String batchNo = batchService.createBatch(file.getOriginalFilename(), file.getSize(), (long)kycSearchHistoryDtoList.size(),BatchTypeEnum.BATCH_CUSTOMERTUNE);

                    List<Future<Long>> batchSaicInfoIds = saicInfoService.getBatchSaicInfoBase(kycSearchHistoryDtoList, batchNo);


                    dto.setCode(ResultCode.ACK);
                    dto.setMessage("导入文件成功");
                    dto.setData(batchNo);
                } else {
                    dto.setCode(ResultCode.NACK);
                    dto.setMessage("导入文件为空...请检查后再次导入...");
                }

            }

            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        } catch (Exception e) {
            log.error("导入Excel失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入Excel失败");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }

    }

    /**
     * 登记信息异动
     */
    @RequestMapping(value = "/registerAbnormal")
    public ObjectRestResponse getRegisterAbnormal(Long compareResultId) {
        CompareResultDto compareResultDto = compareResultService.findById(compareResultId);
        JSONArray array = customerAbnormalService.getChangedList(compareResultDto);
        return new ObjectRestResponse<>().rel(true).result(array);
    }

    /**
     * 将工商返回的工商状态中文数据进行分类
     */
    @RequestMapping(value = "/getSaicStateForState")
    public String getSaicStateForState(String state) {
        return compareTaskService.getSaicStateForState(state);
    }

    /**
     * 客户尽调-司法信息查询
     * @param companyName 企业名称
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/judicialInformation", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<JudicialInformationDto> judicialInformatioDetail(String companyName, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return judicialInformationService.getDetailFromCompanyName(companyName,pageable);
    }

    /**
     * 司法信息查询-历史数据查询
     * @param caseNo
     * @return
     * @throws
     */
    @RequestMapping(value = "/judicialInformatioDetail", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<JudicialInformationDto> getJudicialInformatio(String caseNo){
        return new ObjectRestResponse<JudicialInformationDto>().rel(true).result(judicialInformationService.findByCaseNo(caseNo));
    }

    /**
     * 工商信息委托更新
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/entrustUpdate", method = RequestMethod.GET)
    public ResultDto entrustUpdate(String keyword) {
        if(StringUtils.isBlank(keyword)) {
            log.warn("企业名称为空");
            return ResultDtoFactory.toNack("企业名称为空");
        }

        return ResultDtoFactory.toAckData(saicInfoService.entrustUpdate(keyword));
    }

    /**
     * 查询工商信息委托状态
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/getEntrustUpdateResult", method = RequestMethod.GET)
    public ResultDto getEntrustUpdateResult(String keyword) {
        if(StringUtils.isBlank(keyword)) {
            log.warn("企业名称为空");
            return ResultDtoFactory.toNack("企业名称为空");
        }

        return ResultDtoFactory.toAckData(saicInfoService.getEntrustUpdateResult(keyword));
    }

}
