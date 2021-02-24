package com.ideatech.ams.controller.validate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.customer.dto.CustomerPublicInfo;
import com.ideatech.ams.customer.dto.CustomersAllInfo;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.customer.service.CustomersAllService;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsCheckResultInfo;
import com.ideatech.ams.pbc.dto.AmsJibenUniqueCheckCondition;
import com.ideatech.ams.pbc.dto.AmsPrintInfo;
import com.ideatech.ams.pbc.enums.AccountStatus;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.service.ValidateService;
import com.ideatech.ams.system.blacklist.enums.BlackListResultEnum;
import com.ideatech.ams.system.blacklist.service.BlackListService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.permission.dto.PermissionDto;
import com.ideatech.ams.system.permission.service.PermissionService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.vo.ComposedValidationResultVo;
import com.ideatech.ams.ws.api.service.CoreJibenValidateApiService;
import com.ideatech.ams.ws.api.service.PbcSearchService;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.enums.SaicStatusEnum;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.FileExtraUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 校验管理
 * @author wanghongjie
 *
 * @version 2018-06-10 13:13
 */
@RestController
@RequestMapping("/validate")
@Slf4j
public class ValidateController {

    @Autowired
    private UserService userService;

    @Autowired
    private SaicInfoService saicInfoService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private AmsMainService amsMainService;

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CustomerPublicService customerPublicService;

    @Autowired
    private CustomersAllService customersAllService;

    @Autowired
    private ValidateService validateService;

    @Autowired
	private BlackListService blackListService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private CoreJibenValidateApiService coreJibenValidateApiService;

	@Autowired
	private PbcSearchService pbcSearchService;

	@Autowired
	private PbcAmsService pbcAmsService;

	//0-客户维护后台，1-客户维护前台
    @Value("${validate.create-customer.type}")
    private int createCustomerType;

    //PBC数据测试
    @Value("${validate.create-customer.pbcTest}")
    private boolean pbcTest;

    //强行开户：若为true，则无论工商、人行、核心数据验证结果是否一致，都能进行继续开户，默认为false
    @Value("${validate.create-customer.forceOpenAccount}")
    private boolean forceOpenAccount;

	@Value("${ams.image.path}")
	private String filePath;

    /**
     * 行内核心校验--预留
     * @param keyword
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/core", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<String> core(String keyword){
    	if (StringUtils.isBlank(keyword)) {
            log.info("查询关键字为空");
            return new ObjectRestResponse<String>().rel(false).msg("查询关键字为空");
        } else {
        	return new ObjectRestResponse<String>().rel(true).msg("核心校验通过");
        }
    }


    /**
     * 黑名单校验--预留
     * @param keyword
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/black", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<String> black(String keyword){
    	if (StringUtils.isBlank(keyword)) {
            log.info("查询关键字为空");
            return new ObjectRestResponse<String>().rel(false).msg("查询关键字为空");
        } else {
			BlackListResultEnum blackListResult = blackListService.findByNameMixWhite(keyword);
			if(blackListResult == BlackListResultEnum.NORMAL){
				return new ObjectRestResponse<String>().rel(true).msg("该企业属于正常状态，通过黑名单校验");
			}else if(blackListResult == BlackListResultEnum.WHITE){
				return new ObjectRestResponse<String>().rel(true).msg("该企业处于白名单中，通过黑名单校验");
			}else{
				return new ObjectRestResponse<String>().rel(false).msg("该企业处于黑名单中，未通过黑名单校验");

			}
        }
    }

    /**
     * 工商校验
     *
     * @param keyword 查询企业
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/saic", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<String> saic(String keyword) throws UnsupportedEncodingException, ParseException {
		keyword = StringUtils.trim(keyword);
    	if (StringUtils.isBlank(keyword)) {
            log.info("查询关键字为空");
            return new ObjectRestResponse<String>().rel(false).msg("查询关键字为空");
        } else {
            UserDto userDto = userService.findByUsername(SecurityUtils.getCurrentUsername());
            if (userDto == null) {
                return new ObjectRestResponse<String>().rel(false).msg("系统获取用户信息请求超时，请稍后重试");
            }
            SaicIdpInfo saicIdpInfo = saicInfoService.getSaicInfoFull(SearchType.EXACT, SecurityUtils.getCurrentUsername(), URLDecoder.decode(keyword, "UTF-8"),
                    SecurityUtils.getCurrentOrgFullId());
            if(saicIdpInfo ==null) {
                return new ObjectRestResponse<String>().rel(false).msg("未查询到工商信息，请确认输入的企业名称再进行重试");
            }else if(!saicInfoService.checkIfIllegals(saicIdpInfo.getIllegals())) {
				return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业存在严重违法行为").result("tab_8");
			}else if(!saicInfoService.checkIfChangeMess(saicIdpInfo.getChangemess())) {
				return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业存在经营异常行为").result("tab_7");
			}else if(!saicInfoService.checkIfStateNormal(saicIdpInfo.getState())) {
				return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业的工商状态异常").result("tab_0");
			}else if(!saicInfoService.checkIfDateNormal(saicIdpInfo.getStartdate(), saicIdpInfo.getEnddate())) {
                return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业的营业期限异常").result("tab_0");
			}else if(!saicInfoService.checkIfSimpleCancel(saicIdpInfo.getNotice())) {
                return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业正在进行简易注销公告").result("tab_0");
            }
            return new ObjectRestResponse<String>().rel(true).msg("工商校验通过");
        }
    }


	@GetMapping("/composed")
	public ResultDto<ComposedValidationResultVo> composedValidation(@RequestParam String keyword,@RequestParam String accountKey,@RequestParam String regAreaCode) throws UnsupportedEncodingException {
		keyword = StringUtils.trim(keyword);
    	ComposedValidationResultVo resultVo = new ComposedValidationResultVo();
    	// 首先校验工商
		if (StringUtils.isBlank(keyword)) {
			log.info("查询关键字为空");
			resultVo.setSaicCheckPassed(Boolean.FALSE);
			resultVo.setSaicValidationMessage("工商校验：查询关键字为空");
		} else {
			UserDto userDto = userService.findByUsername(SecurityUtils.getCurrentUsername());
			if (userDto == null) {
				throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, "系统获取用户信息请求超时，请稍后重试");
			}
			SaicIdpInfo saicIdpInfo = saicInfoService.getSaicInfoFull(SearchType.EXACT, SecurityUtils.getCurrentUsername(), URLDecoder.decode(keyword.trim(), "UTF-8"),
					SecurityUtils.getCurrentOrgFullId());
			resultVo.setSaicCheckPassed(Boolean.FALSE);
			resultVo.setSaicIdpInfo(saicIdpInfo);
			if(saicIdpInfo ==null) {
				resultVo.setSaicValidationMessage("未查询到工商信息，请确认输入的企业名称再进行重试");
			}else if(!saicInfoService.checkIfStateNormal(saicIdpInfo.getState())) {
				resultVo.setSaicValidationMessage("请注意，该企业的工商状态异常");
			}else if(!saicInfoService.checkIfDateNormal(saicIdpInfo.getStartdate(), saicIdpInfo.getEnddate())) {
				resultVo.setSaicValidationMessage("请注意，该企业的营业期限异常");
			}else if(!saicInfoService.checkIfIllegals(saicIdpInfo.getIllegals())) {
				resultVo.setSaicValidationMessage("请注意，该企业存在严重违法行为");
			}else if(!saicInfoService.checkIfChangeMess(saicIdpInfo.getChangemess())) {
				resultVo.setSaicValidationMessage("请注意，该企业存在经营异常行为");
			} else {
				resultVo.setSaicCheckPassed(Boolean.TRUE);
				resultVo.setSaicValidationMessage("工商校验通过");
			}
		}

		if (StringUtils.isBlank(keyword)) {
			log.info("查询关键字为空");
			resultVo.setBlackCheckPassed(Boolean.FALSE);
			resultVo.setBlackValidationMessage("黑名单校验：查询关键字为空");
		} else {
			BlackListResultEnum blackListResult = blackListService.findByNameMixWhite(keyword);
			if(blackListResult == BlackListResultEnum.NORMAL){
				resultVo.setBlackCheckPassed(Boolean.TRUE);
				resultVo.setBlackValidationMessage("黑名单校验通过");
			}else if(blackListResult == BlackListResultEnum.WHITE){
				resultVo.setBlackCheckPassed(Boolean.TRUE);
				resultVo.setBlackValidationMessage("该企业处于白名单中，通过黑名单校验");
			}else{
				resultVo.setBlackCheckPassed(Boolean.FALSE);
				resultVo.setBlackValidationMessage("企业处于黑名单中，未通过黑名单校验");

			}
		}

		// 人行校验, 如果基本户核准号和机构号都为空，则表示不校验人行
		if(StringUtils.isBlank(accountKey) && StringUtils.isBlank(regAreaCode)){
			return ResultDtoFactory.toAckData(resultVo);
		}

		if (StringUtils.isBlank(accountKey) || StringUtils.isBlank(regAreaCode)) {
			resultVo.setPbcCheckPassed(Boolean.FALSE);
			resultVo.setPbcValidationMessage("基本户核准号和基本户地区代码不能为空");
		}
		SecurityUtils.UserInfo current = SecurityUtils.getCurrentUser();
		Long orgId = current.getOrgId();
		OrganizationDto organizationDto = organizationService.findById(orgId);

		if (organizationDto==null || StringUtils.isBlank(organizationDto.getCode())) {
			throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, "组织机构代号为空，请确认");
		}
		try {
			if(pbcTest) {
				resultVo.setPbcCheckPassed(Boolean.TRUE);
				resultVo.setPbcValidationMessage("人行校验通过");
				resultVo.setAmsAccountInfo(getAmsAccountInfo(accountKey, regAreaCode));
			} else {
				PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(organizationDto.getCode(), EAccountType.AMS);
				if (pbcAccountDto != null) {
					AmsCheckResultInfo amsCheckResultInfo = pbcAmsService.checkPbcByAccountKeyAndRegAreaCode(organizationDto.getCode(), accountKey, regAreaCode);
//					AmsCheckResultInfo amsCheckResultInfo = amsMainService.checkPbcByAccountKeyAndRegAreaCode(allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto), accountKey, regAreaCode);
					if (amsCheckResultInfo.isCheckPass()) {
						resultVo.setPbcCheckPassed(Boolean.TRUE);
						resultVo.setPbcValidationMessage("人行校验通过");
						resultVo.setAmsAccountInfo(amsCheckResultInfo.getAmsAccountInfo());
					} else {
						resultVo.setPbcCheckPassed(Boolean.FALSE);
						resultVo.setPbcValidationMessage(amsCheckResultInfo.getNotPassMessage());
						resultVo.setAmsAccountInfo(amsCheckResultInfo.getAmsAccountInfo());
					}
				} else {
					resultVo.setPbcCheckPassed(Boolean.FALSE);
					List<PbcAccountDto> pbcAccountDtos = pbcAccountService.listByOrgCodeAndType(organizationDto.getCode(), EAccountType.AMS);
					if (pbcAccountDtos == null || pbcAccountDtos.size() == 0) {
						resultVo.setPbcValidationMessage(ResultCode.ORGAN_NOT_CONFIG_SYNC_USER.message());
					} else {
						resultVo.setPbcValidationMessage(ResultCode.NO_VALID_PBCACCOUNT.message());
					}
				}
			}
		} catch (Exception e) {
			resultVo.setPbcCheckPassed(Boolean.FALSE);
			resultVo.setPbcValidationMessage("人行校验异常:"+e.getMessage());
		}

		return ResultDtoFactory.toAckData(resultVo);
	}

    /**
     * 人行校验
     * @param accountKey
     * @param regAreaCode
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/pbc", method = RequestMethod.GET)
    @ResponseBody
	public ObjectRestResponse<Object> checkPbcInfo(String accountKey, String regAreaCode, @RequestParam(required = false, defaultValue = "false") Boolean flag) {
        if (StringUtils.isBlank(accountKey) || StringUtils.isBlank(regAreaCode)) {
        	return new ObjectRestResponse<Object>().rel(false).msg("基本户核准号和基本户地区代码不能为空");
        }
        SecurityUtils.UserInfo current = SecurityUtils.getCurrentUser();
        Long orgId = current.getOrgId();
        OrganizationDto organizationDto = organizationService.findById(orgId);

        if (organizationDto==null || StringUtils.isBlank(organizationDto.getCode())) {
        	return new ObjectRestResponse<Object>().rel(false).msg("组织机构代号为空，请确认");
        }
        try {
        	if(pbcTest) {
            	return new ObjectRestResponse<AmsAccountInfo>().rel(true).result(getAmsAccountInfo(accountKey, regAreaCode));
        	}
            PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(organizationDto.getCode(), EAccountType.AMS);
            if (pbcAccountDto != null) {
				AmsCheckResultInfo amsCheckResultInfo;
            	if (flag){//一般户开户校验
					amsCheckResultInfo = pbcAmsService.checkPbcByAccountKeyAndRegAreaCodeForYiBanOpen(organizationDto.getCode(), accountKey, regAreaCode);
				}else {
					amsCheckResultInfo = pbcAmsService.checkPbcByAccountKeyAndRegAreaCode(organizationDto.getCode(), accountKey, regAreaCode);
				}
//                AmsCheckResultInfo amsCheckResultInfo = amsMainService.checkPbcByAccountKeyAndRegAreaCode(allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto), accountKey, regAreaCode);
				if (amsCheckResultInfo.isCheckPass()) {
					if (amsCheckResultInfo.getAmsAccountInfo() != null && StringUtils.isNotBlank(amsCheckResultInfo.getAmsAccountInfo().getDepositorName())) {
						return new ObjectRestResponse<AmsAccountInfo>().rel(true).result(amsCheckResultInfo.getAmsAccountInfo());
					} else {
						return new ObjectRestResponse<AmsAccountInfo>().rel(false).msg("获取基本户信息失败，请重试");
					}
				} else {
                	return new ObjectRestResponse<Object>().rel(false).msg(amsCheckResultInfo.getNotPassMessage());
                }
            } else {
				List<PbcAccountDto> pbcAccountDtos = pbcAccountService.listByOrgCodeAndType(organizationDto.getCode(), EAccountType.AMS);
				if (pbcAccountDtos == null || pbcAccountDtos.size() == 0) {
					return new ObjectRestResponse<Object>().rel(false).msg(ResultCode.ORGAN_NOT_CONFIG_SYNC_USER.message());
				} else {
					return new ObjectRestResponse<Object>().rel(false).msg(ResultCode.NO_VALID_PBCACCOUNT.message());
				}
            }
        } catch (Exception e) {
            String message = e.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = ResultCode.PBC_VALIDATION_NOT_PASS.message();
            }
            if(message.contains("Connection timed out")){
				message = ResultCode.NETWORK_TIMEOUT.message();
			}
			if(message.contains("基本存款账户与一般存款账户不得开立在同一银行机构")){
				message = "基本存款账户与一般存款账户不得开立在同一银行机构";
			}
        	return new ObjectRestResponse<Object>().rel(false).msg(message);
        }
    }

    private AmsAccountInfo getAmsAccountInfo(String accountKey, String regAreaCode) {
    	AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
    	amsAccountInfo.setBasicAcctRegArea(regAreaCode);
    	amsAccountInfo.setAcctNo("账号-test");
    	amsAccountInfo.setAcctName("账户名称-test");
    	amsAccountInfo.setRegAreaCode(regAreaCode);
    	amsAccountInfo.setAccountStatus(AccountStatus.normal);
    	amsAccountInfo.setDepositorName("浙江省易得融信软件有限公司");
    	amsAccountInfo.setDepositorType("01");
    	amsAccountInfo.setFileType("证明文件种类1-test");
    	amsAccountInfo.setFileNo("913302115953861679");
    	amsAccountInfo.setFileType2("证明文件种类2-test");
    	amsAccountInfo.setFileNo2("证明文编号2-test");
    	amsAccountInfo.setLegalType("1");
    	amsAccountInfo.setLegalName("徐涛");
    	amsAccountInfo.setLegalIdcardType("1");
    	amsAccountInfo.setLegalIdcardNo("法人证件编号-test");
    	amsAccountInfo.setZipCode("315000");
    	amsAccountInfo.setTelephone("13888888888");
    	amsAccountInfo.setRegisteredCapital("20000000");
    	amsAccountInfo.setRegCurrencyType("注册币种-test");
    	amsAccountInfo.setTaxRegNo("纳税人识别号(地税)-test");
    	amsAccountInfo.setStateTaxRegNo("纳税人识别号(国税)-test");
    	amsAccountInfo.setOrgCode("组织机构代码-test");
    	amsAccountInfo.setNoTaxProve("无需办理税务登记证的文件或税务机关出具的证明-test");
    	amsAccountInfo.setBusinessScope("经营范围-test");
    	amsAccountInfo.setAccountKey(accountKey);
    	amsAccountInfo.setParOrgCode("上级组织机构代码-test");
    	amsAccountInfo.setParCorpName("上级存款人名称-test");
    	amsAccountInfo.setParAccountKey("上级基本户开户许可证-test");
    	amsAccountInfo.setParLegalType("1");
    	amsAccountInfo.setParLegalName("上级法人姓名-test");
    	amsAccountInfo.setParLegalIdcardType("1");
    	amsAccountInfo.setParLegalIdcardNo("上级法人证件编号-test");
    	amsAccountInfo.setRegAddress("宁波市镇海区庄市街道中官西路777号创e慧谷12号1-1、2-1、3-1、4-1");
    	return amsAccountInfo;
    }

	private AmsAccountInfo getYiBanAmsAccountInfo() {
    	String json = "{" +
				"\"accountFileNo\": \"\"," +
				"\"accountFileType\": \"其他结算需要的证明\"," +
				"\"accountKey\": \"-\"," +
				"\"accountLicenseNo\": \"-\"," +
				"\"accountStatus\": \"normal\"," +
				"\"acctCreateDate\": \"2018-05-11\"," +
				"\"acctName\": \"润行（福建）国际贸易有限公司\"," +
				"\"acctNo\": \"100032632870010003\"," +
				"\"acctType\": \"yiban\"," +
				"\"bankCode\": \"313391080015\"," +
				"\"bankName\": \"福建海峡银行股份有限公司营业部\"," +
				"\"businessScope\": \"自营和代理各类商品和技术的进出口，但国家限定公司经营或禁止进出口的商品和技术除外；日用品、预包装食品、化妆品、文化用品、家用电器、卫生用品、五金交电、开关电器、纺织品、服装鞋帽、药品、化工产品、工艺品、建筑材料的批发、代购代销及网上经营。（依法须经批准的项目，经相关部门批准后方可开展经营活动）\"," +
				"\"cancelDate\": \"-\"," +
				"\"depositorName\": \"润行（福建）国际贸易有限公司\"," +
				"\"depositorType\": \"企业法人\"," +
				"\"fileNo\": \"91350104550987778X\"," +
				"\"fileNo2\": \"\"," +
				"\"fileType\": \"工商营业执照\"," +
				"\"fileType2\": \"\"," +
				"\"industryCode\": \"批发和零售业\"," +
				"\"legalIdcardNo\": \"350103196809170074\"," +
				"\"legalIdcardType\": \"身份证\"," +
				"\"legalName\": \"陈志行\"," +
				"\"noTaxProve\": \"\"," +
				"\"orgCode\": \"550987778\"," +
				"\"parAccountKey\": \"\"," +
				"\"parCorpName\": \"\"," +
				"\"parLegalIdcardNo\": \"\"," +
				"\"parLegalIdcardType\": \"\"," +
				"\"parLegalName\": \"\"," +
				"\"parOrgCode\": \"\"," +
				"\"regAddress\": \"福建省福州市仓山区城门镇南江滨西大道198号福州海峡国际会展中心地下一层IBC0029（自贸试验区内）\"," +
				"\"regAreaCode\": \"391000\"," +
				"\"regCurrencyType\": \"人民币\"," +
				"\"registeredCapital\": \"10000000.00\"," +
				"\"stateTaxRegNo\": \"91350104550987778X\"," +
				"\"taxRegNo\": \"91350104550987778X\"," +
				"\"telephone\": \"87500931\"," +
				"\"zipCode\": \"350001\"" +
				"}";
    	JSONObject jsonObject = JSON.parseObject(json);
		AmsAccountInfo amsAccountInfo = JSON.parseObject(JSONObject.toJSONString(jsonObject), AmsAccountInfo.class);
		return amsAccountInfo;
	}
    /**
     * 客户维护
     * @param name
     * @param type
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/open/customerInfo", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Object> openCustomerInfo(String name,String type,String accountKey, String regAreaCode) throws Exception {
    	if(!checkIfIgnoreSaic()){
			if(StringUtils.isBlank(name) || StringUtils.isBlank(type)) {
				return new ObjectRestResponse<Object>().rel(false).msg("企业名字或者开户类型不能为空");
			}
		}

    	int openType = validateService.checkCustomerType(type);
    	if(openType ==0) {
    		return new ObjectRestResponse<Object>().rel(false).msg("企业的开户类型有误，请确认重试");
    	}
    	CustomersAllInfo customersAllInfo = null;
    	List<CustomersAllInfo> list = customersAllService.findByDepositorNameList(name);
		if(CollectionUtils.isNotEmpty(list) && list.size()>0){
			customersAllInfo = list.get(0);
		}
		//本地无客户，创建客户
    	if(customersAllInfo ==null) {
        	return createCustomer(openType,name,accountKey,regAreaCode);
    	}else {//本地有客户，更新客户
    		CustomerPublicInfo customerPublicInfo =customerPublicService.getByCustomerId(customersAllInfo.getId());
    		return updateCustomer(openType,name,accountKey,regAreaCode,customersAllInfo,customerPublicInfo);
    	}
    }

	/**
	 * 变更前三方比对验证
	 * @param name	客户名称----(本地客户信息)
	 * @param type 账户类型
	 * @param accountKey 基本开户许可核准号
	 * @param regAreaCode 基本户地区代码
	 * @param acctNo 账号
	 * @param regAddress 注册（登记）地址----(本地客户信息)
	 * @param legalName 法人姓名----(本地客户信息)
	 * @param fileNo 工商注册号----(本地客户信息)
	 */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/changeOpen/customerInfo", method = RequestMethod.GET)
    @ResponseBody
	public ObjectRestResponse<Object> changeOpenCustomerInfo(String name, String type, String accountKey, String regAreaCode, String acctNo,
	String regAddress, String legalName, String fileNo ,String regFullAddress) throws Exception {
        if(!checkIfIgnoreSaic()){
            if(StringUtils.isBlank(name) || StringUtils.isBlank(type)) {
                return new ObjectRestResponse<Object>().rel(false).msg("企业名字或者开户类型不能为空");
            }
        }

        int openType = validateService.checkCustomerType(type);
        if(openType ==0) {
            return new ObjectRestResponse<Object>().rel(false).msg("企业的开户类型有误，请确认重试");
        }
		AllBillsPublicDTO allBillsPublicDTO = new AllBillsPublicDTO();
		allBillsPublicDTO.setDepositorName(name);
		allBillsPublicDTO.setRegAddress(regAddress);
		allBillsPublicDTO.setLegalName(legalName);
		allBillsPublicDTO.setFileNo(fileNo);
		allBillsPublicDTO.setRegFullAddress(regFullAddress);
		return changeOpenCompare(allBillsPublicDTO, openType, name, accountKey, regAreaCode, acctNo);
    }

    @RequestMapping(value = "/open/getCustomerInfo", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<AllBillsPublicDTO> getCustomerInfo(String name,String type) throws Exception {
        if(StringUtils.isBlank(name) || StringUtils.isBlank(type)) {
            return new ObjectRestResponse<Object>().rel(false).msg("企业名字或者开户类型不能为空");
        }
        CustomersAllInfo customersAllInfo = customersAllService.findByDepositorName(name);
        if(customersAllInfo != null) {
            return new ObjectRestResponse<Object>().rel(false).msg("客户名字已存在,无需开立");
        }

        return getCustomerInfo(name);
    }

    private ObjectRestResponse<AllBillsPublicDTO> getCustomerInfo(String name) throws Exception {
        AllBillsPublicDTO allBillsPublicDTO = new AllBillsPublicDTO();
        SaicIdpInfo saicIdpInfo = saicInfoService.getSaicInfoFull(SearchType.EXACT, SecurityUtils.getCurrentUsername(), URLDecoder.decode(name, "UTF-8"),
                SecurityUtils.getCurrentOrgFullId());
        if(saicIdpInfo !=null) {//有KYC信息,直接使用kyc信息
            validateService.getFromKYC(allBillsPublicDTO,saicIdpInfo);
            log.info("有KYC信息,直接使用kyc信息");
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(true).result(allBillsPublicDTO);
        }else {
            log.info("无工商信息，无本地客户信息");
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(true).result(allBillsPublicDTO).msg("无工商信息，无本地客户信息");
        }

    }

	/**
	 * 判断是否具有开户具有“跳过”权限
	 * @return
	 */
	private boolean checkIfIgnorePbc(){
		((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities();
		List<PermissionDto> menusByRoleId = permissionService.findElementsByRoleId(SecurityUtils.getCurrentUser().getRoleId());
		for(PermissionDto permissionDto : menusByRoleId){
			if("account:open_pbcIgnore".equals(permissionDto.getCode())){
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否具有开户具有“工商跳过”权限
	 * @return
	 */
	private boolean checkIfIgnoreSaic(){
		((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities();
		List<PermissionDto> menusByRoleId = permissionService.findElementsByRoleId(SecurityUtils.getCurrentUser().getRoleId());
		for(PermissionDto permissionDto : menusByRoleId){
			if("account:open_saicIgnore".equals(permissionDto.getCode())){
				return true;
			}
		}
		return false;
	}

    /**
     * 本地无客户，创建客户
     * @param type
     * @param name
     * @param accountKey
     * @param regAreaCode
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	private ObjectRestResponse<Object> createCustomer(int type,String name,String accountKey, String regAreaCode) throws Exception {//本地无客户，创建客户
    	AllBillsPublicDTO allBillsPublicDTO = new AllBillsPublicDTO();
		JSONObject json = new JSONObject();
		json.put("final", allBillsPublicDTO);
		json.put("coreFlag", false);//前端核心人行工商比较结果显示列表中是否显示核心列数据列
    	if(createCustomerType ==0) {//客户维护后台
    		if(type ==1) {//A&C  如：jiben linshi zengzi yanzi teshu
    			SaicIdpInfo saicIdpInfo = saicInfoService.getSaicInfoFull(SearchType.REAL_TIME, SecurityUtils.getCurrentUsername(), URLDecoder.decode(name, "UTF-8"),
                        SecurityUtils.getCurrentOrgFullId());
    			if(saicIdpInfo !=null) {//有KYC信息,直接使用kyc信息
    				validateService.getFromKYC(allBillsPublicDTO,saicIdpInfo);
        			log.info("有KYC信息,直接使用kyc信息");
        			//成立日期过滤
        			if ("9999-12-31".equals(json.getObject("final",AllBillsPublicDTO.class).getFileSetupDate())){
						allBillsPublicDTO.setFileSetupDate(null);
					}
					return new ObjectRestResponse<Object>().rel(true).code("1").result(json);
    			}else {
        			log.info("无工商信息，无本地客户信息");
					//成立日期过滤
					if ("9999-12-31".equals(json.getObject("final",AllBillsPublicDTO.class).getFileSetupDate())){
						allBillsPublicDTO.setFileSetupDate(null);
					}
                	return new ObjectRestResponse<Object>().rel(true).code("1").result(json).msg("无工商信息，无本地客户信息");
    			}
    		}else {//B  备案类账户，如：yiban yusuan feiyusuan feilinshi
				SaicIdpInfo saicIdpInfo = saicInfoService.getSaicInfoFull(SearchType.REAL_TIME, SecurityUtils.getCurrentUsername(), URLDecoder.decode(name, "UTF-8"),
                        SecurityUtils.getCurrentOrgFullId());
    			AmsAccountInfo amsAccountInfo;
    			if(pbcTest) {
    				amsAccountInfo = getAmsAccountInfo(accountKey, regAreaCode);
    			}else {
    				amsAccountInfo = validateService.getPBCAmsAccountInfo(accountKey, regAreaCode);
    			}
//    			AmsAccountInfo amsAccountInfo = getPBCAmsAccountInfo(accountKey, regAreaCode);
//    			AmsAccountInfo amsAccountInfo = getAmsAccountInfo(accountKey, regAreaCode);
				json.put("compareList",  validateService.getOpenCompareTableJson(null, amsAccountInfo, saicIdpInfo));

    			if(saicIdpInfo != null && amsAccountInfo !=null) {//kyc和pbc信息都有，进行部分采取
					if (!validateService.comparePBCWithKYC(amsAccountInfo, saicIdpInfo)) {//人行和工商数据进行比较
						validateService.coverForConfig(saicIdpInfo, amsAccountInfo, allBillsPublicDTO);
						log.info("人行信息和工商信息不一致，是否继续开户");//存在基本户在其他行开的户，后来又做了工商变更，不能即时变更基本户的情况
						//成立日期过滤
						if ("9999-12-31".equals(json.getObject("final",AllBillsPublicDTO.class).getFileSetupDate())){
							allBillsPublicDTO.setFileSetupDate(null);
						}
						return new ObjectRestResponse<Object>().rel(false).code("1").result(json).msg("人行信息和工商信息不一致，是否继续开户");
					} else {
						validateService.getFromPBCAndKYC(allBillsPublicDTO, amsAccountInfo, saicIdpInfo);
						log.info("kyc和pbc信息都有，进行部分采取");
						//成立日期过滤
						if ("9999-12-31".equals(json.getObject("final",AllBillsPublicDTO.class).getFileSetupDate())){
							allBillsPublicDTO.setFileSetupDate(null);
						}
						return new ObjectRestResponse<Object>().rel(true).code("1").result(json);
					}
    			}else if(saicIdpInfo != null) {//只有kyc信息，直接kyc覆盖#无人行不行
					validateService.getFromKYC(allBillsPublicDTO,saicIdpInfo);
					if (checkIfIgnorePbc() || forceOpenAccount) {
						log.info("无人行信息，是否继续开户");
						//成立日期过滤
						if ("9999-12-31".equals(json.getObject("final",AllBillsPublicDTO.class).getFileSetupDate())){
							allBillsPublicDTO.setFileSetupDate(null);
						}
						return new ObjectRestResponse<Object>().rel(false).code("1").msg("无人行信息，是否继续开户").result(json);
					}else{
						log.info("无人行信息，流程结束");
						return new ObjectRestResponse<Object>().rel(false).code("0").msg("无人行信息，流程结束");
					}
    			}else if(amsAccountInfo !=null) {//只有pbc信息，直接pbc覆盖
    				validateService.getFromPBC(allBillsPublicDTO,amsAccountInfo);
        			log.info("无工商信息，使用人行信息");
					//成立日期过滤
					if ("9999-12-31".equals(json.getObject("final",AllBillsPublicDTO.class).getFileSetupDate())){
						allBillsPublicDTO.setFileSetupDate(null);
					}
	            	return new ObjectRestResponse<Object>().rel(true).code("1").result(json).msg("无工商信息，使用人行信息");
    			}else {
					if(checkIfIgnorePbc() || forceOpenAccount){
						log.info("无人行信息和工商信息，是否继续开户");
						//成立日期过滤
						if ("9999-12-31".equals(json.getObject("final",AllBillsPublicDTO.class).getFileSetupDate())){
							allBillsPublicDTO.setFileSetupDate(null);
						}
						return new ObjectRestResponse<Object>().rel(false).code("1").msg("无人行信息和工商信息，是否继续开户").result(json);
					}else{
						log.info("无人行信息和工商信息，流程结束");
						return new ObjectRestResponse<Object>().rel(false).code("0").msg("无人行信息和工商信息，流程结束");
					}
				}
    		}
    	}else {//客户维护前台
//    		TODO: 客户前端维护
        	return new ObjectRestResponse<Object>().rel(true).code("1").result(json);
    	}
    }



    /**
     * 本地有客户，更新客户
     * @param type
     * @param name
     * @param accountKey
     * @param regAreaCode
     * @param customersAllInfo
     * @param customerPublicInfo
	 * @return ref=true表示直接成功 code=0表示直接失败 code=1表示提示用户是否继续开户
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	private ObjectRestResponse<Object> updateCustomer(int type,String name,String accountKey, String regAreaCode,CustomersAllInfo customersAllInfo,CustomerPublicInfo customerPublicInfo) throws Exception {//本地有客户，更新客户
    	AllBillsPublicDTO allBillsPublicDTO = new AllBillsPublicDTO();
		JSONObject json = new JSONObject();
		json.put("final", allBillsPublicDTO);
		json.put("coreFlag", true);//前端核心人行工商比较结果显示列表中是否显示核心列数据列

		//返回给前端时去除存量数据的标记
		if("1".equals(customerPublicInfo.getString003())) {
			customerPublicInfo.setString003(null);
		}
    	if(createCustomerType ==0) {//客户维护后台
    		if(type ==1) {//A&C 有后台本地客户时默认KYC直接覆盖本地数据
    			BeanCopierUtils.copyProperties(customersAllInfo,allBillsPublicDTO);
    			BeanCopierUtils.copyProperties(customerPublicInfo,allBillsPublicDTO);
    			//设置账户简称为存款人名称
				allBillsPublicDTO.setAcctShortName(allBillsPublicDTO.getDepositorName());
    			allBillsPublicDTO.setId(null);
    			SaicIdpInfo saicIdpInfo = saicInfoService.getSaicInfoFull(SearchType.EXACT, SecurityUtils.getCurrentUsername(), URLDecoder.decode(name, "UTF-8"),
                        SecurityUtils.getCurrentOrgFullId());
    			if(saicIdpInfo !=null) {//有KYC信息,直接使用kyc信息覆盖
        			log.info("有KYC信息,直接使用kyc信息覆盖");
        			validateService.coverLocalFromKYC(allBillsPublicDTO,saicIdpInfo);
					//成立日期过滤
					if ("9999-12-31".equals(json.getObject("final",AllBillsPublicDTO.class).getFileSetupDate())){
						allBillsPublicDTO.setFileSetupDate(null);
					}
                	return new ObjectRestResponse<Object>().rel(true).code("1").result(json);
    			}else {
        			log.info("无工商信息，使用本地客户信息");
					//成立日期过滤
					if ("9999-12-31".equals(json.getObject("final",AllBillsPublicDTO.class).getFileSetupDate())){
						allBillsPublicDTO.setFileSetupDate(null);
					}
                	return new ObjectRestResponse<Object>().rel(true).code("1").result(json).msg("本次未能查到该客户的工商信息，将使用客户在本行中的历史信息");
    			}
    		}else {//B 有本地数据时，后台进行比对，分别为本地和人行比，本地和KYC比对，人行与本地不一致时进行提示z，并结束流程，人行和本地一致时再比对本地和KYC，如不一致进行提示并以KYC覆盖本地，同时在流程结束后同步至客户正本数据中，根据关联账户发起异动提示；
    			BeanCopierUtils.copyProperties(customersAllInfo,allBillsPublicDTO);
    			BeanCopierUtils.copyProperties(customerPublicInfo,allBillsPublicDTO);
				//设置账户简称为存款人名称
				allBillsPublicDTO.setAcctShortName(allBillsPublicDTO.getDepositorName());
    			allBillsPublicDTO.setId(null);
    			SaicIdpInfo saicIdpInfo = saicInfoService.getSaicInfoFull(SearchType.EXACT, SecurityUtils.getCurrentUsername(), URLDecoder.decode(name, "UTF-8"),
                        SecurityUtils.getCurrentOrgFullId());
    			AmsAccountInfo amsAccountInfo;
    			if(pbcTest) {
    				amsAccountInfo = getAmsAccountInfo(accountKey, regAreaCode);
    			}else {
    				amsAccountInfo = validateService.getPBCAmsAccountInfo(accountKey, regAreaCode);
    			}
//    			AmsAccountInfo amsAccountInfo = getPBCAmsAccountInfo(accountKey, regAreaCode);
//    			AmsAccountInfo amsAccountInfo = getAmsAccountInfo(accountKey, regAreaCode);

				json.put("compareList", validateService.getOpenCompareTableJson(allBillsPublicDTO, amsAccountInfo, saicIdpInfo));

    			if(saicIdpInfo != null && amsAccountInfo !=null) {//kyc和pbc信息都有，进行比对
					if (!validateService.compareLocalWithPBC(allBillsPublicDTO, amsAccountInfo)) {//pbc和本地客户比较,不一致结束流程
						//成立日期过滤
						if ("9999-12-31".equals(json.getObject("final",AllBillsPublicDTO.class).getFileSetupDate())){
							allBillsPublicDTO.setFileSetupDate(null);
						}
						if (forceOpenAccount) {
							if(validateService.comparePBCWithKYC(amsAccountInfo, saicIdpInfo)){//人行与工商数据进行比较
								validateService.coverForConfig(saicIdpInfo, amsAccountInfo, allBillsPublicDTO);
								log.info("人行信息和本地客户信息不一致，人行信息和工商信息一致，是否继续开户");
								return new ObjectRestResponse<Object>().rel(false).code("1").result(json).msg("人行信息和本地客户信息不一致，人行信息和工商信息一致，是否继续开户");
							} else {
								validateService.coverForConfig(saicIdpInfo, amsAccountInfo, allBillsPublicDTO);
								log.info("本地客户信息和人行信息不一致，人行信息和工商信息不一致，是否继续开户");
								return new ObjectRestResponse<Object>().rel(false).code("1").result(json).msg("人行信息和本地客户信息不一致，人行信息和工商信息不一致，是否继续开户");
							}
//							log.info("人行信息和本地客户信息不一致，是否继续开户");
//							return new ObjectRestResponse<Object>().rel(false).code("1").result(json).msg("人行信息和本地客户信息不一致，是否继续开户");
						} else {
							log.info("人行信息和本地客户信息不一致，流程结束");
							return new ObjectRestResponse<Object>().rel(false).code("0").result(json).msg("人行信息和本地客户信息不一致，流程结束");
						}
					} else {//pbc和本地客户一致，再进行kyc比对
						if (!validateService.compareLocalWithKYC(allBillsPublicDTO, saicIdpInfo)) {//KYC和本地客户不一致，KYC覆盖并且提示
							validateService.coverLocalFromKYC(allBillsPublicDTO, saicIdpInfo);
							log.info("工商信息和本地客户信息不一致，已经用工商信息覆盖");
							//成立日期过滤
							if ("9999-12-31".equals(json.getObject("final",AllBillsPublicDTO.class).getFileSetupDate())){
								allBillsPublicDTO.setFileSetupDate(null);
							}
							return new ObjectRestResponse<Object>().rel(true).code("1").result(json).msg("工商信息和本地客户信息不一致，已经用工商信息覆盖");
						} else {//KYC和本地客户一致
							log.info("工商信息和本地客户信息一致");
							AllBillsPublicDTO allBillsPublicDTONew = new AllBillsPublicDTO();
							BeanCopierUtils.copyProperties(allBillsPublicDTONew, allBillsPublicDTO);//清空本地数据
							validateService.getFromPBCAndKYC(allBillsPublicDTO, amsAccountInfo, saicIdpInfo);//采用部分工商数据和人行数据
							log.info("返显部分工商数据和人行数据");
							//成立日期过滤
							if ("9999-12-31".equals(json.getObject("final",AllBillsPublicDTO.class).getFileSetupDate())){
								allBillsPublicDTO.setFileSetupDate(null);
							}
							return new ObjectRestResponse<Object>().rel(true).code("1").result(json);
						}
					}
				}else if(saicIdpInfo != null) {//只有kyc信息，无人行信息，提示并流程结束
					validateService.getFromKYC(allBillsPublicDTO,saicIdpInfo);
					if (checkIfIgnorePbc() || forceOpenAccount) {
						log.info("无人行信息，是否继续开户");
						//成立日期过滤
						if ("9999-12-31".equals(json.getObject("final", AllBillsPublicDTO.class).getFileSetupDate())) {
							allBillsPublicDTO.setFileSetupDate(null);
						}
						return new ObjectRestResponse<Object>().rel(false).code("1").msg("无人行信息，是否继续开户").result(json);
					} else {
						log.info("无人行信息，流程结束");
						return new ObjectRestResponse<Object>().rel(false).code("0").msg("无人行信息，流程结束");
					}
    			}else if(amsAccountInfo !=null) {//只有pbc信息，直接返回
//    				coverLocalFromKYC(allBillsPublicDTO,amsAccountInfo);
        			log.info("本次未能查到该客户的工商信息，将使用客户在本行中的历史信息");
					//成立日期过滤
					if ("9999-12-31".equals(json.getObject("final",AllBillsPublicDTO.class).getFileSetupDate())){
						allBillsPublicDTO.setFileSetupDate(null);
					}
	            	return new ObjectRestResponse<Object>().rel(true).code("1").result(json).msg("本次未能查到该客户的工商信息，将使用客户在本行中的历史信息");
    			}else {
					if (checkIfIgnorePbc() || forceOpenAccount) {
						log.info("无人行信息和工商信息，是否继续开户");
						//成立日期过滤
						if ("9999-12-31".equals(json.getObject("final", AllBillsPublicDTO.class).getFileSetupDate())) {
							allBillsPublicDTO.setFileSetupDate(null);
						}
						return new ObjectRestResponse<Object>().rel(false).code("1").msg("无人行信息和工商信息，是否继续开户").result(json);
					} else {
						log.info("无人行信息和工商信息，流程结束");
						return new ObjectRestResponse<Object>().rel(false).code("0").msg("无人行信息和工商信息，流程结束");
					}

    			}
    		}
    	}else {//客户维护前台
//    		TODO: 客户前端维护
        	return new ObjectRestResponse<Object>().rel(true).result(json);
    	}
    }

    /**
     * 本地有客户，更新客户
     * @param
     * @param name
     * @param accountKey
     * @param regAreaCode
     * @return ref=true表示直接成功 code=0表示直接失败 code=1表示提示用户是否继续开户
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	private ObjectRestResponse<Object> changeOpenCompare(AllBillsPublicDTO allBillsPublicDTO, int type, String name, String accountKey, String regAreaCode, String acctNo) throws Exception {//本地有客户，更新客户
        JSONObject json = new JSONObject();
        json.put("final", allBillsPublicDTO);
        json.put("coreFlag", true);//前端核心人行工商比较结果显示列表中是否显示核心列数据列
        if(createCustomerType ==0) {//客户维护后台
            AmsAccountInfo amsAccountInfo = null;
            if(type ==1) {
                if(pbcTest) {
                    amsAccountInfo = getAmsAccountInfo(accountKey, regAreaCode);
                }else {
                    amsAccountInfo = validateService.getPBCAmsAccountInfoByAcctNo(acctNo);
                }
            }else{
                //基于基本户开户获取基本户信息进行比对
                if(pbcTest) {
                    amsAccountInfo = getAmsAccountInfo(accountKey, regAreaCode);
                }else {
                    amsAccountInfo = validateService.getPBCAmsAccountInfo(accountKey,regAreaCode);
                }
            }
            //设置账户简称为存款人名称
            allBillsPublicDTO.setAcctShortName(allBillsPublicDTO.getDepositorName());
            allBillsPublicDTO.setId(null);
            SaicIdpInfo saicIdpInfo = saicInfoService.getSaicInfoFull(SearchType.EXACT, SecurityUtils.getCurrentUsername(), URLDecoder.decode(name, "UTF-8"),
                    SecurityUtils.getCurrentOrgFullId());

			json.put("compareList", validateService.getOpenCompareTableJson(allBillsPublicDTO, amsAccountInfo, saicIdpInfo));

            if(saicIdpInfo != null && amsAccountInfo !=null) {//kyc和pbc信息都有，进行比对
                if (!validateService.compareLocalWithPBC(allBillsPublicDTO, amsAccountInfo)) {//pbc和本地客户比较,不一致结束流程
                    log.info("人行信息和本地客户信息不一致，流程结束");
                    return new ObjectRestResponse<Object>().rel(false).code("0").result(json).msg("数据比对信息");
                } else {//pbc和本地客户一致，再进行kyc比对
                    if (!validateService.compareLocalWithKYC(allBillsPublicDTO, saicIdpInfo)) {//KYC和本地客户不一致，KYC覆盖并且提示
                        validateService.coverLocalFromKYC(allBillsPublicDTO, saicIdpInfo);
                        log.info("工商信息和本地客户信息不一致，已经用工商信息覆盖");
                        return new ObjectRestResponse<Object>().rel(true).code("1").result(json).msg("工商信息和本地客户信息不一致，已经用工商信息覆盖");
                    } else {//KYC和本地客户一致
                        log.info("工商信息和本地客户信息一致");
                        return new ObjectRestResponse<Object>().rel(true).code("1").result(json);
                    }
                }
            }else if(saicIdpInfo != null) {//只有kyc信息，无人行信息，提示并流程结束
                validateService.getFromKYC(allBillsPublicDTO,saicIdpInfo);
                if(checkIfIgnorePbc()){
                    log.info("无人行信息，是否继续开户");
                    return new ObjectRestResponse<Object>().rel(false).code("1").msg("无人行信息，是否继续开户").result(json);
                }else{
                    log.info("无人行信息，流程结束");
                    return new ObjectRestResponse<Object>().rel(false).code("0").msg("无人行信息，流程结束");
                }
            }else if(amsAccountInfo !=null) {//只有pbc信息，直接返回
//    				coverLocalFromKYC(allBillsPublicDTO,amsAccountInfo);
                log.info("本次未能查到该客户的工商信息，将使用客户在本行中的历史信息");
                return new ObjectRestResponse<Object>().rel(true).code("1").result(json).msg("本次未能查到该客户的工商信息，将使用客户在本行中的历史信息");
            }else {
                if(checkIfIgnorePbc()){
                    log.info("无人行信息和工商信息，是否继续开户");
                    return new ObjectRestResponse<Object>().rel(false).code("1").msg("无人行信息和工商信息，是否继续开户").result(json);
                }else{
                    log.info("无人行信息和工商信息，流程结束");
                    return new ObjectRestResponse<Object>().rel(false).code("0").msg("无人行信息和工商信息，流程结束");
                }

            }
        }else {//客户维护前台
//    		TODO: 客户前端维护
            return new ObjectRestResponse<Object>().rel(true).result(json);
        }
    }

	/**
	 * 工商校验，提取本地数据，不生成客户尽调记录
	 *
	 * @param keyword 查询企业
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saic/local", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<String> saicLocal(String keyword,Long saicInfoId) throws UnsupportedEncodingException, ParseException {
		keyword = StringUtils.trim(keyword);
		if (StringUtils.isBlank(keyword) && (saicInfoId == null || saicInfoId ==0)) {
			log.info("查询关键字为空");
			return new ObjectRestResponse<String>().rel(false).msg("查询关键字为空");
		} else {
			SaicIdpInfo saicIdpInfo = new SaicIdpInfo();
			if(StringUtils.isNotBlank(keyword)){
				saicIdpInfo = saicInfoService.getSaicInfoBaseLocal(URLDecoder.decode(keyword, "UTF-8"));
			}else if(saicInfoId !=null && saicInfoId >0){
				saicIdpInfo = saicInfoService.getSaicInfoBaseLocalByIdNoValidTime(saicInfoId);
			}
//			SaicIdpInfo saicIdpInfo = saicInfoService.getSaicInfoBaseLocal(URLDecoder.decode(keyword, "UTF-8"));

			if (saicIdpInfo == null) {
				return new ObjectRestResponse<String>().rel(false).msg("未查询到工商信息，请确认输入的企业名称再进行重试");
			}
			SaicStatusEnum saicStatusEnum = SaicStatusEnum.saicState2Enum(saicIdpInfo.getState());//工商异常状态
			if (saicStatusEnum == SaicStatusEnum.REVOKE) {
				if(!saicInfoService.checkIfDateNormal(saicIdpInfo.getStartdate(), saicIdpInfo.getEnddate())){
					return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业已经被吊销,企业的营业期限异常").result("tab_0");
				}
				return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业已经被吊销").result("tab_0");
			} else if (saicStatusEnum == SaicStatusEnum.CANCEL) {
				return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业已经被注销撤销").result("tab_0");
			} else if (!saicInfoService.checkIfIllegals(saicIdpInfo.getIllegals())) {
				return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业存在严重违法行为").result("tab_8");
			} else if (!saicInfoService.checkIfChangeMess(saicIdpInfo.getChangemess())) {
				return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业存在经营异常行为").result("tab_7");
//			} else if (!saicInfoService.checkIfStateNormal(saicIdpInfo.getState())) {
//				return new ObjectRestResponse<JSONObject>().rel(false).msg("请注意，该企业的工商状态异常").result("tab_0");
			} else if (!saicInfoService.checkIfDateNormal(saicIdpInfo.getStartdate(), saicIdpInfo.getEnddate())) {
				return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业的营业期限异常").result("tab_0");
			} else if (saicStatusEnum == SaicStatusEnum.QUIT) {
				if(!saicInfoService.checkIfDateNormal(saicIdpInfo.getStartdate(), saicIdpInfo.getEnddate())){
					return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业正在进行简易注销公告,企业的营业期限异常").result("tab_0");
				}
				return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业已经被迁出").result("tab_0");
			}else if(!saicInfoService.checkIfSimpleCancel(saicIdpInfo.getNotice())) {
				if(!saicInfoService.checkIfDateNormal(saicIdpInfo.getStartdate(), saicIdpInfo.getEnddate())){
					return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业正在进行简易注销公告,企业的营业期限异常").result("tab_0");
				}
				return new ObjectRestResponse<String>().rel(false).msg("请注意，该企业正在进行简易注销公告").result("tab_0");
			}
			return new ObjectRestResponse<String>().rel(true).msg("工商校验通过");
		}
	}

	@GetMapping("/jibenUniqueValidate")
	public ResultDto jibenUniqueValidate(String organCode, AmsJibenUniqueCheckCondition amsJibenUniqueCheckCondition) {

		if (StringUtils.isBlank(organCode)) {
			organCode = organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId()).getCode();
		}

		//基本户唯一性核心校验
		Boolean coreFlag = coreJibenValidateApiService.jibenCoreValidate(ConverterService.convert(amsJibenUniqueCheckCondition, AllBillsPublicDTO.class));
		if (!coreFlag) {
			return ResultDtoFactory.toApiError(ResultCode.DATA_ALREADY_EXISTED.code(), "核心已存在", null);
		}

		String errorMessage = pbcAmsService.jiBenUniqueCheck(amsJibenUniqueCheckCondition, organCode);

		if (StringUtils.isBlank(errorMessage)) {
			return ResultDtoFactory.toAck();
		} else {
            if (errorMessage.contains("Connection timed out") || errorMessage.contains("Connection reset")) {
                return ResultDtoFactory.toApiError(ResultCode.NETWORK_TIMEOUT.code(), "网络连接超时", null);
            }
			return ResultDtoFactory.toApiError(ResultCode.DATA_ALREADY_EXISTED.code(), errorMessage, null);
		}

	}

	/**
	 * 销户校验
	 * @param depositorName    存款人名称
	 * @param bankCode         银行机构代码
	 * @param accountKey       基本存款账户开户许可证核准号
	 * @param selectPwd        存款人查询密码
	 * @return
	 */
	@PostMapping("/revokeValidate")
	public ResultDto revokeValidate(String depositorName, String bankCode, String accountKey, String selectPwd, CompanyAcctType acctType) {
		ResultDto resultDto = null;
		String code = "";
		String msg = "";
		try {
			resultDto = pbcSearchService.searchAllAccount(depositorName,accountKey,selectPwd,bankCode);
			if(StringUtils.equals("1",resultDto.getCode())){
				msg = "基本存款账户销户校验通过";
				List<AmsPrintInfo> dataList = (List<AmsPrintInfo>)resultDto.getData();
				if(acctType == CompanyAcctType.jiben){
					log.info("查询账户数量：{}",dataList.size());
					for (AmsPrintInfo data:dataList) {
						log.info("账户性质：{}",data.getAcctType());
						if(StringUtils.equals("基本存款账户",data.getAcctType()) || data.getAcctType().contains("基本存款账户")){
							continue;
						}
						log.info("账户状态：{}",data.getAccountStatus());
						if(StringUtils.equals("正常",data.getAccountStatus().trim()) || data.getAccountStatus().contains("正常")){
							code = ResultCode.PBC_HAVE_NORMAL.code();
							msg = ResultCode.PBC_HAVE_NORMAL.message()+",不可销户！";
							break;
						}
						//增加久悬账户的判断   基本户下存在久悬账户不允许进行销户
						if(StringUtils.equals("久悬",data.getAccountStatus().trim()) || data.getAccountStatus().contains("久悬")){
							code = ResultCode.PBC_HAVE_SUSPEND.code();
							msg = ResultCode.PBC_HAVE_SUSPEND.message()+",不可销户！";
							break;
						}
					}
				}
				//TODO:非基本户校验规则待确定
			}else{
				if(resultDto.getMessage().contains("Connection timed out")){
					code = ResultCode.NETWORK_CONNECTION_ERROR.code();
					msg = ResultCode.NETWORK_CONNECTION_ERROR.message();
				}else{
					code= resultDto.getCode();
					msg = resultDto.getMessage();
				}
			}
		}catch (Exception e){
			if(e.getMessage().contains("Connection timed out")){
				code = ResultCode.NETWORK_CONNECTION_ERROR.code();
				msg = ResultCode.NETWORK_CONNECTION_ERROR.message();
			}else{
				code = ResultCode.PBC_CHECKDETAIL_FAILURE.code();
				msg = e.getMessage();
			}
		}
		resultDto.setCode(code);
		resultDto.setMessage(msg);
		return resultDto;
	}


	@PostMapping("/getJibenDetails")
	public ResultDto getJibenDetails(String acctNo,String pbcCode) {
		String code = "";
		String msg = "";
		if(StringUtils.isBlank(acctNo)){
			return ResultDtoFactory.toApiError(code, "账号不能为空!", null);
		}
		try {
			return pbcSearchService.jiBenResetDetails(acctNo,pbcCode);
		}catch (Exception e){
			if(e.getMessage().contains("Connection timed out")){
				code = ResultCode.NETWORK_CONNECTION_ERROR.code();
				msg = ResultCode.NETWORK_CONNECTION_ERROR.message();
			}else{
				code = ResultCode.PBC_CHECKDETAIL_FAILURE.code();
				msg = e.getMessage();
			}
		}
		return ResultDtoFactory.toApiError(code, msg, null);
	}

	@PostMapping("/jiBenResetPrint")
	public ResultDto jiBenResetPrint(String acctNo,String pbcCode) {
		String code = "";
		String msg = "";
		try {
			return pbcSearchService.jiBenResetPrint(acctNo,pbcCode);
		}catch (Exception e){
			if(e.getMessage().contains("Connection timed out")){
				code = ResultCode.NETWORK_CONNECTION_ERROR.code();
				msg = ResultCode.NETWORK_CONNECTION_ERROR.message();
			}else{
				code = ResultCode.PBC_CHECKDETAIL_FAILURE.code();
				msg = e.getMessage();
			}
		}
		return ResultDtoFactory.toApiError(code, msg, null);
	}

	@RequestMapping(value = "/cancelAccount/pdf")
	public ResponseEntity<byte[]> cancelAccount(String allAccountData, HttpServletRequest request){
		HttpHeaders headers = new HttpHeaders();
		List<AmsPrintInfo> printData = JSON.parseArray(allAccountData, AmsPrintInfo.class);
		Map<String,Object> map = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(printData);
		InputStream inputStream = null;
		String fileName =null;
		String fileName2=null;
		InputStream is = null;
		try {
			fileName = FileExtraUtils.handleFileName(request, "cancelAccount.pdf");
			String path =creatPath();
			fileName2 = path + File.separator+fileName;
			inputStream = new ClassPathResource("jasperReport"+File.separator+"cancelAccount.jasper").getInputStream();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, jrDataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, fileName2);
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			is = new FileInputStream(fileName2);
			byte[] byteArray = IOUtils.toByteArray(is);
			return new ResponseEntity<>(byteArray, headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(is);
			if(StringUtils.isNotBlank(fileName2)){
				File file = new File(fileName2);
				if(file.exists()){
					FileUtils.deleteQuietly(file);
					log.info("删除第三方数据查询临时文件");
				}
			}
		}
		return null;
	}

	private String creatPath(){
		String path = filePath+"/temp";
		File file = new File(path);
		if(!file.exists()){
			log.info("开始创建临时文件夹："+path);
			file.mkdirs();
		}
		return path;
	}
}
