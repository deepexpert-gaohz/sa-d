package com.ideatech.ams.kyc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.kyc.dto.*;
import com.ideatech.ams.kyc.dto.holiday.HolidayDto;
import com.ideatech.ams.kyc.dto.newcompany.OutFreshCompanyQueryDto;
import com.ideatech.ams.kyc.dto.saicentrust.EntrustResultDto;
import com.ideatech.ams.kyc.enums.HolidayTypeEnum;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.proof.dto.ProofReportDto;
import com.ideatech.ams.system.proof.enums.ProofType;
import com.ideatech.ams.system.proof.service.ProofReportService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.DesEncrypter;
import com.ideatech.common.util.HttpRequest;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Service
@Transactional(noRollbackFor = EacException.class)
@Slf4j
public class SaicRequestServiceImpl implements  SaicRequestService{
    @Autowired
    private HttpRequest httpRequest;

    @Autowired
    private SaicInfoService saicInfoService;
    // 基本工商数据的key
    @Value("${saic.api.key:111}")
    private String realTimeKey;
    /**
     * 基本工商数据的加密开关
     */
    @Value("${saic.realTimeEncryptFlag}")
    private boolean realTimeEncryptFlag;
    /**
     * 基本工商数据实时接口
     */
    @Value("${saic.url.realTime}")
    private String realTimeUrl;

    /**
     * 基本工商数据实时接口--加密
     */
    @Value("${saic.url.realTime-encrypt}")
    private String realTimeEncryptUrl;

    @Value("${saic.url.exact}")
    private String exactUrl;

    @Value("${saic.url.equityShareList}")
    private String equityShareListUrl;
    /**
     * 受益所有人数据存量接口
     */
    @Value("${saic.url.beneficiary}")
    private String beneficiaryUrl;
    /**
     * 基本户履历数据存量接口
     */
    @Value("${saic.url.baseAccount}")
    private String baseAccountUrl;
    /**
     * 节假日接口
     */
    @Value("${saic.url.holiday}")
    private String holidayUrl;

    /**
     * 严重违法失信接口
     */
    @Value("${saic.url.illegalQuery}")
    private String illegalQueryUrl;

    @Value("${saic.url.carrierOperator:111}")
    private String carrierOperateUrl;
    @Value("${ams.company.writeMoney}")
    private boolean writeMoney;
    /**
     * 司法信息接口
     */
    @Value("${saic.url.judicialInformation}")
    private String judicialInformationUrl;
    @Autowired
    private ConfigService configService;
    @Autowired
    private ProofReportService proofReportService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;

    /**
     * 获取新增企业接口url
     */
    @Value("${saic.url.freshCompany:111}")
    private String freshCompanyUrl;

    /**
     * 委托企业进行更新
     */
    @Value("${saic.url.entrustUpdate}")
    private String entrustUpdateUrl;

    /**
     * 查询委托状态
     */
    @Value("${saic.url.entrustUpdateResult}")
    private String entrustUpdateResultUrl;

    @Override
    public SaicIdpInfo getSaicInfoRealTime(String keyword){
        return getSaicInfoRealTimeCommon(keyword, "",null);
    }

    @Override
    public SaicIdpInfo getSaicInfoRealTime(String keyword, String username){
        return getSaicInfoRealTimeCommon(keyword, username,null);
    }

    @Override
    public SaicIdpInfo getSaicInfoRealTime(String keyword, String username, SearchType t){
        return getSaicInfoRealTimeCommon(keyword, username,t);
    }

    @Override
    public String getSaicInfoRealTimeJson(String keyword) {
        log.debug("获取即时工商信息--json");
        String jsonResult;
        Map<String, String> map;
        if(realTimeEncryptFlag){
            log.info("获取即时工商信息--json--加密接口");
            try {
                map = getReqParamsEncrypt(keyword);
                jsonResult = encryptRealTime(getIdpRequest(realTimeEncryptUrl,map));
            } catch (Exception e) {
                log.error("获取即时工商信息--json--加密接口报错",e);
                return null;
            }
        }else{
            map = getReqParams(keyword);
            jsonResult = getIdpRequest(realTimeUrl,map);
        }
        return jsonResult;
    }

    @Override
    public String getSaicInfoRealTimeUrl() {
        if(realTimeEncryptFlag){
            return realTimeEncryptUrl;
        }else{
            return realTimeUrl;
        }
    }

    @Override
    public SaicIdpInfo getSaicInfoExact(String keyword){
        return getSaicInfoExactCommon(keyword, "",null);
    }

    @Override
    public SaicIdpInfo getSaicInfoExact(String keyword, String usernmae){
        return getSaicInfoExactCommon(keyword, usernmae,null);
    }

    @Override
    public SaicIdpInfo getSaicInfoExact(String keyword, String usernmae,SearchType type){
        return getSaicInfoExactCommon(keyword, usernmae,type);
    }

    @Override
    public String getSaicInfoExactJson(String keyword) {
        log.debug("获取存量工商信息--json");
        Map<String, String> map = getReqParams(keyword);
        String jsonResult = getIdpRequest(exactUrl,map);
        return jsonResult;
    }

    @Override
    public String getSaicInfoExactUrl() {
        return exactUrl;
    }

    @Override
    public OutEquityShareDto getOutEquityShareDto(String keyword){
        log.debug("获取股权结构");
        Map<String, String> map = getReqParams(keyword);
        String jsonResult = getIdpRequest(equityShareListUrl,map);
        if(StringUtils.isNotBlank(jsonResult)){
            if(jsonResult.contains("数据正在计算")){
                return null;
            }
            OutEquityShareDto outEquityShareDto = JSON.parseObject(jsonResult, OutEquityShareDto.class);
            return outEquityShareDto;
        }else{
            return null;
        }
    }

    @Override
    public String getOutEquityShareDtoUrl() {
        return equityShareListUrl;
    }

    @Override
    public OutBaseAccountDto getOutBaseAccountDto(String keyword){
        log.debug("获取基本户履历");
        Map<String, String> map = getReqParams(keyword);
        String jsonResult = getIdpRequest(baseAccountUrl,map);
        if(StringUtils.isNotBlank(jsonResult)){
            OutBaseAccountDto outBaseAccountDto = JSON.parseObject(jsonResult, OutBaseAccountDto.class);
            return outBaseAccountDto;
        }else{
            return null;
        }
    }

    @Override
    public String getOutBaseAccountDtoUrl() {
        return baseAccountUrl;
    }

    @Override
    public List<HolidayDto> getHolidayDto(int year) {
        log.debug("获取节假日信息");
        Map<String, String> map = new HashMap<>();
        map.put("y", year + "");
        String jsonResult = getIdpRequest(holidayUrl, map);
        List<HolidayDto> holidayDtoList = new ArrayList<>();

        if (StringUtils.isNotBlank(jsonResult)) {
            JSONArray jsonArray = JSON.parseArray(jsonResult);
            HolidayDto holidayDto = null;
            for (Object obj : jsonArray) {
                holidayDto = new HolidayDto();
                JSONObject jObj = (JSONObject) obj;
                String dateStr = jObj.getString("date");
                try {
                    dateStr = DateUtils.DateToStr(DateUtils.parseDate(dateStr), "yyyy-MM-dd");
                } catch (ParseException e) {
                    //ignore
                }
                holidayDto.setDateStr(dateStr);
                holidayDto.setHolidayType("1".equals(jObj.getString("type")) ? HolidayTypeEnum.WORKDAY_REST : HolidayTypeEnum.HOLIDAY_WORK);
                holidayDtoList.add(holidayDto);
            }
            return holidayDtoList;
        } else {
            return null;
        }
    }


    @Override
    public OutBeneficiaryDto getOutBeneficiaryDto(String keyword){
        log.debug("获取受益人");
        Map<String, String> map = getReqParams(keyword);
        String jsonResult = getIdpRequest(beneficiaryUrl,map);
        if(StringUtils.isNotBlank(jsonResult)){
            OutBeneficiaryDto outBeneficiaryDto = new OutBeneficiaryDto();
            if(jsonResult.contains("数据正在计算")){
                outBeneficiaryDto.setReason("数据正在计算，请稍后再试");
            }else{
                outBeneficiaryDto = JSON.parseObject(jsonResult, OutBeneficiaryDto.class);
            }
            return outBeneficiaryDto;
        }else{
            return null;
        }
    }

    @Override
    public String getOutBeneficiaryDtoUrl() {
        return beneficiaryUrl;
    }

    private String getIdpRequest(String url,Map<String, String> map){
        return httpRequest.getIdpRequest(url, map);
    }

    private String getIdpRequest(String url,Map<String, String> map, Integer timeOut){
        return httpRequest.getIdpRequest(url, map, timeOut);
    }

    /**
     * 组装基础工商实时接口的参数
     * @param keyword
     * @return
     * @throws Exception
     */
    private Map<String, String> getReqParams(String keyword){
        Map<String, String> params = new HashMap<String, String>();
        params.put("keyWord", keyword);
        return params;
    }

    /**
     * 组装基础工商实时接口的参数--加密
     * @param keyword
     * @return
     * @throws Exception
     */
    private Map<String,String> getReqParamsEncrypt(String keyword) throws Exception {
        DesEncrypter desEncrypter = new DesEncrypter(realTimeKey);
        String encryptParam = desEncrypter.encrypt("keyWord="+keyword);
        Map<String, String> map = new HashMap<>();
        map.put("encryptParam",encryptParam);
        return map;
    }

    /**
     * 解密基础工商实时接口的返回值
     * @param jsonResult
     * @return
     */
    private String encryptRealTime(String jsonResult) throws Exception {
//        OuterInfo outerInfo = JSON.parseObject(jsonResult, OuterInfo.class);
        DesEncrypter des = new DesEncrypter(realTimeKey);
        return des.decrypt(jsonResult);
//        return des.decrypt(outerInfo.getResult());
    }

    @Override
    public OutIllegalQueryDto getOutIllegalQueryDto(String keyWord) {
        log.debug("获取严重违法失信信息");
        String jsonResult = "";
        OutIllegalQueryDto outIllegalQueryDto = new OutIllegalQueryDto();
        Map<String, String> map = getReqParams(keyWord);

        try {
            jsonResult = getIdpRequest(illegalQueryUrl, map);
        }  catch (Exception e) {
            if(e.getMessage().contains("获取结果失败")) {
                outIllegalQueryDto.setIllegalStatus("EMPTY");
                return outIllegalQueryDto;
            }
        }

        if(StringUtils.isNotBlank(jsonResult)){
            outIllegalQueryDto = JSON.parseObject(jsonResult, OutIllegalQueryDto.class);
            return outIllegalQueryDto;
        }else{
            return outIllegalQueryDto;
        }
    }

    @Override
    public String getOutIllegalQueryDtoUrl() {
        return illegalQueryUrl;
    }

    @Override
    public String getCarrierResponseStr(String encryptParam) {
        log.debug("获取运营商校验信息");
        Map<String, String> map = new HashMap<>();
        map.put("encryptParam", encryptParam);

        return getIdpRequest(carrierOperateUrl, map);
    }

    @Override
    public String getJudicialInformation(String encryptParam,int pageIndex,int pageSize) {
        log.info("获取司法信息");
        Map<String, String> map = new HashMap<>();
        map.put("keyWord", encryptParam);
        map.put("pageIndex", (pageIndex + 1) + "");
        map.put("pageSize", pageSize + "");
        return getIdpRequest(judicialInformationUrl, map);
    }

    @Override
    public OutFreshCompanyQueryDto getFreshCompanyList(String provinceCode, String startDate, String endDate, Integer pageIndex, Integer pageSize) {
        log.info("获取新增公司信息");
        Map<String, String> map = new HashMap<>();
        OutFreshCompanyQueryDto dto = new OutFreshCompanyQueryDto();

        map.put("provinceCode", provinceCode);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(pageSize));

        String jsonResult = getIdpRequest(freshCompanyUrl, map);

        if(StringUtils.isNotBlank(jsonResult)){
            dto = JSON.parseObject(jsonResult, OutFreshCompanyQueryDto.class);
            return dto;
        }

        return null;
    }

    @Override
    public EntrustResultDto entrustUpdate(String keyword) throws Exception {
        log.info("委托企业进行更新开始");
        EntrustResultDto dto = null;
        Map<String, String> map = new HashMap<>();
        map.put("keyWord", keyword);

        String jsonResult = getIdpRequest(entrustUpdateUrl, map);
        if(StringUtils.isNotBlank(jsonResult)) {
            dto = JSON.parseObject(jsonResult, EntrustResultDto.class);
            return dto;
        }

        log.info("委托企业进行更新结束");
        return null;
    }

    @Override
    public EntrustResultDto getEntrustUpdateResult(String keyword) throws Exception {
        log.info("查询委托状态开始");
        EntrustResultDto dto = null;
        Map<String, String> map = new HashMap<>();
        map.put("keyWord", keyword);

        String jsonResult = getIdpRequest(entrustUpdateResultUrl, map);
        if(StringUtils.isNotBlank(jsonResult)) {
            dto = JSON.parseObject(jsonResult, EntrustResultDto.class);
            return dto;
        }

        log.info("查询委托状态结束");
        return null;
    }

    private SaicIdpInfo getSaicInfoRealTimeCommon(String keyword, String username,SearchType type) {
        log.debug("获取即时工商信息");
        String jsonResult = getSaicInfoRealTimeJson(keyword);
        if(StringUtils.isNotBlank(jsonResult)){
            SaicIdpInfo saicIdpInfo = JSON.parseObject(jsonResult, SaicIdpInfo.class);
            if(saicIdpInfo!=null && writeMoney && type != SearchType.KHJD){
                ProofReportDto accountProofReportDto = new ProofReportDto();
                if(StringUtils.isBlank(username)) {
                    username =  SecurityUtils.getCurrentUsername();
                    if(StringUtils.isBlank(username)){
                        UserDto userDto = userService.findById(2L);
                        OrganizationDto organizationDto = organizationService.findById(userDto.getOrgId());
                        accountProofReportDto.setTypeDetil("接口方式工商查询_即时工商信息");
                        accountProofReportDto.setOrganFullId(organizationDto.getFullId());
                        accountProofReportDto.setUsername(userDto.getUsername());
                        accountProofReportDto.setProofBankName(organizationDto.getName());
                    }else{
                        OrganizationDto organizationDto = organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId());
                        accountProofReportDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                        accountProofReportDto.setUsername(username);
                        accountProofReportDto.setTypeDetil("账管系统工商查询_即时工商信息");
                        accountProofReportDto.setProofBankName(organizationDto.getName());
                    }
                } else {
                    UserDto userDto = userService.findByUsername(username);
                    OrganizationDto organizationDto = organizationService.findById(userDto.getOrgId());
                    accountProofReportDto.setTypeDetil("接口方式工商查询_即时工商信息");
                    accountProofReportDto.setOrganFullId(organizationDto.getFullId());
                    accountProofReportDto.setUsername(userDto.getUsername());
                    accountProofReportDto.setProofBankName(organizationDto.getName());
                }

                ConfigDto configDto = configService.findOneByConfigKey("saicMoney");
                if(configDto!=null){
                    accountProofReportDto.setPrice(configDto.getConfigValue());
                }else{
                    accountProofReportDto.setPrice("0");
                }
                accountProofReportDto.setType(ProofType.SAIC);
                accountProofReportDto.setEntname(saicIdpInfo.getName());
                accountProofReportDto.setDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
                proofReportService.save(accountProofReportDto);
            }
            return saicIdpInfo;
        }else{
            return null;
        }
    }

    private SaicIdpInfo getSaicInfoExactCommon(String keyword, String username,SearchType type) {
        log.debug("获取存量工商信息");
        Map<String, String> map = getReqParams(keyword);
        String jsonResult = "";
        try {
            jsonResult = getIdpRequest(exactUrl, map, 20000);//设置查询存量工商数据时，设置20秒超时
        }  catch (Exception e) {
            if(e.getMessage().contains("获取数据超时")) {
                return null;
            }
        }
        if(StringUtils.isNotBlank(jsonResult)){
            SaicIdpInfo saicIdpInfo = JSON.parseObject(jsonResult, SaicIdpInfo.class);
            if(writeMoney && type != SearchType.KHJD){
                ProofReportDto accountProofReportDto = new ProofReportDto();
                if(StringUtils.isBlank(username)) {
                    username =  SecurityUtils.getCurrentUsername();
                    if(StringUtils.isBlank(username)){
                        UserDto userDto = userService.findById(2L);
                        OrganizationDto organizationDto = organizationService.findById(userDto.getOrgId());
                        accountProofReportDto.setTypeDetil("接口方式工商查询_存量工商信息");
                        accountProofReportDto.setOrganFullId(organizationDto.getFullId());
                        accountProofReportDto.setUsername(userDto.getUsername());
                        accountProofReportDto.setProofBankName(organizationDto.getName());
                    }else{
                        OrganizationDto organizationDto = organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId());
                        accountProofReportDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                        accountProofReportDto.setUsername(username);
                        accountProofReportDto.setTypeDetil("账管系统工商查询_存量工商信息");
                        accountProofReportDto.setProofBankName(organizationDto.getName());
                    }
                } else {
                    UserDto userDto = userService.findByUsername(username);
                    OrganizationDto organizationDto = organizationService.findById(userDto.getOrgId());
                    accountProofReportDto.setTypeDetil("接口方式工商查询_存量工商信息");
                    accountProofReportDto.setOrganFullId(organizationDto.getFullId());
                    accountProofReportDto.setUsername(userDto.getUsername());
                    accountProofReportDto.setProofBankName(organizationDto.getName());
                }

                ConfigDto configDto = configService.findOneByConfigKey("saicMoney");
                if(configDto!=null){
                    accountProofReportDto.setPrice(configDto.getConfigValue());
                }else{
                    accountProofReportDto.setPrice("0");
                }
                accountProofReportDto.setType(ProofType.SAIC);
                accountProofReportDto.setEntname(saicIdpInfo.getName());
                accountProofReportDto.setDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
                proofReportService.save(accountProofReportDto);
            }
            return saicIdpInfo;
        }else{
            return null;
        }

    }

}
