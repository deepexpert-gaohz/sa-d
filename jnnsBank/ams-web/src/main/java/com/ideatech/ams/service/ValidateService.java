package com.ideatech.ams.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.customer.dto.CompanyPartnerInfo;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.kyc.dto.StockHolderDto;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsCheckResultInfo;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.ams.pbc.service.PbcMockService;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.pbc.utils.NumberUtils;
import com.ideatech.ams.system.area.dto.AreaDto;
import com.ideatech.ams.system.area.service.AreaService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class ValidateService {

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private PbcAccountService pbcAccountService;

	@Autowired
	private AmsMainService amsMainService;

	@Autowired
	private AllBillsPublicService allBillsPublicService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private PbcAmsService pbcAmsService;

	@Autowired
	private PbcMockService pbcMockService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private DictionaryService dictionaryService;

	/**
	 * 营业执照日期转换
	 */
	@Value("${ams.company.syncFileDue:2099-12-31}")
	private String syncFileDue;

	/**
	 * 营业执照日期是否转换
	 */
	@Value("${ams.company.convertFileDue:false}")
	private Boolean convertFileDue;

    /**
     * 从KYC返回
     * @param allBillsPublicDTO
     * @param saicIdpInfo
     * @throws ParseException
     */
    public void getFromKYC(AllBillsPublicDTO allBillsPublicDTO,SaicIdpInfo saicIdpInfo) throws ParseException {
    	allBillsPublicDTO.setDepositorName(saicIdpInfo.getName());//存款人名称
		allBillsPublicDTO.setAcctShortName(saicIdpInfo.getName());//账户简称
		allBillsPublicDTO.setRegAddress(saicIdpInfo.getAddress());//注册地址
		allBillsPublicDTO.setRegOffice("G");//登记部门
		allBillsPublicDTO.setSetupDate(DateUtils.strToStrAsFormat(saicIdpInfo.getStartdate(), "yyyy-MM-dd"));//成立时间
		allBillsPublicDTO.setBusinessLicenseDue(DateUtils.strToStrAsFormat(saicIdpInfo.getEnddate(), "yyyy-MM-dd"));//到期日期
		allBillsPublicDTO.setFileSetupDate(DateUtils.strToStrAsFormat(saicIdpInfo.getStartdate(), "yyyy-MM-dd"));//成立时间
		allBillsPublicDTO.setFileDue(DateUtils.strToStrAsFormat(saicIdpInfo.getEnddate(), "yyyy-MM-dd"));//到期日期
		//如果营业执照有效期为9999-12-31 就是进行转转，配置文件中ams.company.syncFileDue进行配置  根据各地人行的要求进行日期的转换上报
		if(convertFileDue){
			log.info("工商反显营业执照日期进行转换...");
			if(StringUtils.contains(allBillsPublicDTO.getFileDue(),"9999")){
				log.info("字段FileDue转换日期为：" + syncFileDue);
				allBillsPublicDTO.setFileDue(syncFileDue);
			}
			if(StringUtils.contains(allBillsPublicDTO.getBusinessLicenseDue(),"9999")){
				log.info("字段BusinessLicenseDue转换日期为：" + syncFileDue);
				allBillsPublicDTO.setBusinessLicenseDue(syncFileDue);
			}
		}
		if(saicIdpInfo.getRegistfund()!=null) {
			allBillsPublicDTO.setRegisteredCapital(NumberUtils.convertCapital(saicIdpInfo.getRegistfund()));//注册资金
			if(StringUtils.contains(saicIdpInfo.getRegistfund(), "人民币")) {//只处理人民币的类型
				allBillsPublicDTO.setRegCurrencyType("CNY");
			}
		}
		allBillsPublicDTO.setBusinessScope(saicIdpInfo.getScope());//经营范围
		allBillsPublicDTO.setLegalType("1");//法定代表人
		allBillsPublicDTO.setLegalName(saicIdpInfo.getLegalperson());//法人姓名
		List<StockHolderDto> stockholders = saicIdpInfo.getStockholders();
		Set<CompanyPartnerInfo> companyPartnerInfos = new HashSet<CompanyPartnerInfo>();
		for (StockHolderDto stockHolderDto : stockholders) {
			CompanyPartnerInfo companyPartnerInfo = new CompanyPartnerInfo();
			companyPartnerInfo.setPartnerType(stockHolderDto.getStrtype());//股东类型
			companyPartnerInfo.setName(stockHolderDto.getName());//姓名
			companyPartnerInfos.add(companyPartnerInfo);
		}
		allBillsPublicDTO.setCompanyPartners(companyPartnerInfos);//股东
		allBillsPublicDTO.setRegFullAddress(saicIdpInfo.getAddress());//详细地址
		allBillsPublicDTO.setWorkAddress(saicIdpInfo.getAddress());//工作地址
		allBillsPublicDTO.setBusinessScopeEccs(saicIdpInfo.getScope());//经营范围--信用机构
		if(StringUtils.isNotBlank(saicIdpInfo.getUnitycreditcode())) {//存在18位的营业执照编号
			allBillsPublicDTO.setRegType("07");//工商注册类型-07-统一社会信用代码
			allBillsPublicDTO.setRegNo(saicIdpInfo.getUnitycreditcode());//工商注册编号
			allBillsPublicDTO.setFileType("01");//证明文件1种类-社会统一信用代码
	    	allBillsPublicDTO.setFileNo(saicIdpInfo.getUnitycreditcode());//证明文件1编号--工商注册号
	    	allBillsPublicDTO.setBusinessLicenseNo(saicIdpInfo.getUnitycreditcode());//营业执照编号
	    	allBillsPublicDTO.setOrgCode(StringUtils.substring(saicIdpInfo.getUnitycreditcode(), 8, 17));//第9—17位数字：代表主体识别码，也就是组织机构代码。
	    	allBillsPublicDTO.setTaxRegNo(saicIdpInfo.getUnitycreditcode());//纳税人识别号（地税）
	    	allBillsPublicDTO.setStateTaxRegNo(saicIdpInfo.getUnitycreditcode());//纳税人识别号（国税）
			String areaCode = StringUtils.substring(saicIdpInfo.getUnitycreditcode(),2,8);
	    	List<AreaDto> areas = areaService.getRegistAreaCode(areaCode);
	    	if(areas.size()==1 && areas.get(0).getRegCode() != null) {
		    	allBillsPublicDTO.setRegAreaCode(areas.get(0).getRegCode());
	    	}
	    	AreaDto[] array =  areaService.getRegistLists(areaCode);
	    	if(array[0] !=null) {
	    		allBillsPublicDTO.setRegProvince(array[0].getAreaCode());
	    	}
	    	if(array[1] !=null) {
	    		allBillsPublicDTO.setRegCity(array[1].getAreaCode());
	    	}
	    	if(array[2] !=null) {
	    		allBillsPublicDTO.setRegArea(array[2].getAreaCode());
	    	}
		}else if(StringUtils.isNotBlank(saicIdpInfo.getRegistno())){
			allBillsPublicDTO.setRegType("01");//工商注册类型-01-工商注册号
			allBillsPublicDTO.setRegNo(saicIdpInfo.getRegistno());//工商注册编号
			allBillsPublicDTO.setFileType("01");//证明文件1种类-工商营业执照
	    	allBillsPublicDTO.setFileNo(saicIdpInfo.getRegistno());//证明文件1编号--工商注册号
	    	allBillsPublicDTO.setBusinessLicenseNo(saicIdpInfo.getRegistno());//营业执照编号
	    	String areaCode = saicIdpInfo.getRegistno().substring(0, 6);
	    	List<AreaDto> areas = areaService.getRegistAreaCode(areaCode);
	    	if(areas.size()==1 && areas.get(0).getRegCode() != null) {
		    	allBillsPublicDTO.setRegAreaCode(areas.get(0).getRegCode());
	    	}
	    	AreaDto[] array =  areaService.getRegistLists(areaCode);
	    	if(array[0] !=null) {
	    		allBillsPublicDTO.setRegProvince(array[0].getAreaCode());
	    	}
	    	if(array[1] !=null) {
	    		allBillsPublicDTO.setRegCity(array[1].getAreaCode());
	    	}
	    	if(array[2] !=null) {
	    		allBillsPublicDTO.setRegArea(array[2].getAreaCode());
	    	}
		}
    }

    /**
     * 从PBC返回
     * @param allBillsPublicDTO
     * @param amsAccountInfo
     * @throws ParseException
     */
    public void getFromPBC(AllBillsPublicDTO allBillsPublicDTO,AmsAccountInfo amsAccountInfo) throws ParseException {
		try {
			this.setFileType(amsAccountInfo);
		} catch (Exception e) {
			log.error("人行数据转换类型错误！", e);
		}
		allBillsPublicDTO.setAccountKey(amsAccountInfo.getAccountKey());//基本户核准号
    	allBillsPublicDTO.setRegAreaCode(amsAccountInfo.getRegAreaCode());//注册地地区代码
    	allBillsPublicDTO.setIndustryCode(amsAccountInfo.getIndustryCode());//行业归属
    	allBillsPublicDTO.setDepositorName(amsAccountInfo.getDepositorName());//存款人名称
    	allBillsPublicDTO.setAcctShortName(amsAccountInfo.getDepositorName());//账户简称
    	allBillsPublicDTO.setDepositorType(amsAccountInfo.getDepositorType());//存款人类别
    	allBillsPublicDTO.setRegAddress(amsAccountInfo.getRegAddress());//工商注册地址
//		allBillsPublicDTO.setRegOffice("R");//登记部门
    	allBillsPublicDTO.setFileNo(amsAccountInfo.getFileNo());//证明文件1编号
    	allBillsPublicDTO.setFileType(amsAccountInfo.getFileType());//证明文件1种类
		allBillsPublicDTO.setFileNo2(amsAccountInfo.getFileNo2());//证明文件2编号
		allBillsPublicDTO.setFileType2(amsAccountInfo.getFileType2());//证明文件2种类
    	allBillsPublicDTO.setAcctFileNo(amsAccountInfo.getAccountFileNo());//账户证明文件1编号
    	allBillsPublicDTO.setAcctFileType(amsAccountInfo.getAccountFileType());//账户证明文件1种类
    	allBillsPublicDTO.setAcctFileNo2(amsAccountInfo.getAccountFileNo2());//账户证明文件2编号
    	allBillsPublicDTO.setAcctFileType2(amsAccountInfo.getAccountFileType2());//账户证明文件2种类
    	allBillsPublicDTO.setRegCurrencyType(amsAccountInfo.getRegCurrencyType());//注册资金币种
		if(StringUtils.isNotBlank(amsAccountInfo.getRegisteredCapital())) {
			allBillsPublicDTO.setRegisteredCapital(NumberUtils.convertCapital(amsAccountInfo.getRegisteredCapital()));//注册资金
			if(StringUtils.contains(amsAccountInfo.getRegisteredCapital(), "人民币")) {//只处理人民币的类型
				allBillsPublicDTO.setRegCurrencyType("CNY");
			}
		}
    	allBillsPublicDTO.setBusinessScope(amsAccountInfo.getBusinessScope());//经营范围
    	allBillsPublicDTO.setLegalType(amsAccountInfo.getLegalType());//法人代表（负责）人
    	allBillsPublicDTO.setLegalName(amsAccountInfo.getLegalName());//法人姓名
    	allBillsPublicDTO.setLegalIdcardNo(amsAccountInfo.getLegalIdcardNo());//法人证件号码
    	allBillsPublicDTO.setLegalIdcardType(amsAccountInfo.getLegalIdcardType());//法人证件类型
//    	allBillsPublicDTO.setLegalIdcardDue(amsAccountInfo.getLegalIdcardToVoidDate());//法人证件到期日
//    	allBillsPublicDTO.setOrgCodeDue(DateUtils.DateToStr(DateUtils.parseDate(amsAccountInfo.getOrgCodeToVoidDate()), "yyyy-MM-dd"));//组织机构证到期日
//    	allBillsPublicDTO.setTaxDue(DateUtils.DateToStr(DateUtils.parseDate(amsAccountInfo.getTaxRegNoToVoidDate()), "yyyy-MM-dd"));//纳税人识别号（地税）到期日
    	allBillsPublicDTO.setOrgCode(amsAccountInfo.getOrgCode());//组织机构代号
    	allBillsPublicDTO.setTaxRegNo(amsAccountInfo.getTaxRegNo());//纳税人识别号（地税）
    	allBillsPublicDTO.setZipcode(amsAccountInfo.getZipCode());//邮政编码
    	allBillsPublicDTO.setTelephone(amsAccountInfo.getTelephone());//联系电话
//    	allBillsPublicDTO.setFinanceName(amsAccountInfo.getFinanceName());//财务主管姓名
    	allBillsPublicDTO.setParAccountKey(amsAccountInfo.getParAccountKey());//上级机构信息-基本户开户许可核准号
    	allBillsPublicDTO.setParOrgCode(amsAccountInfo.getParOrgCode());//上机机构信息-组织机构代码
    	allBillsPublicDTO.setParCorpName(amsAccountInfo.getParCorpName());//上机机构信息-机构名称
    	allBillsPublicDTO.setParLegalName(amsAccountInfo.getParLegalName());//上级机构信息-法人姓名
    	allBillsPublicDTO.setParLegalType(amsAccountInfo.getParLegalType());//上级机构信息-法人类型
    	allBillsPublicDTO.setParLegalIdcardType(amsAccountInfo.getParLegalIdcardType());//上级机构信息-法人证件类型
    	allBillsPublicDTO.setParLegalIdcardNo(amsAccountInfo.getParLegalIdcardNo());//上级机构信息-法人证件号码
		allBillsPublicDTO.setBasicAcctRegArea(amsAccountInfo.getBasicAcctRegArea());//基本户注册地地区代码
//    	allBillsPublicDTO.setParLegalIdcardDue(DateUtils.DateToStr(DateUtils.parseDate(amsAccountInfo.getParLegalIdcardDate()), "yyyy-MM-dd"));//上级机构信息-上级法人证件到期日
    }


    /**
     * PBC和KYC部分返回
     * @param allBillsPublicDTO
     * @param amsAccountInfo
     * @param saicIdpInfo
     * @throws ParseException
     */
    public void getFromPBCAndKYC(AllBillsPublicDTO allBillsPublicDTO,AmsAccountInfo amsAccountInfo,SaicIdpInfo saicIdpInfo) throws ParseException {
		try {
			this.setFileType(amsAccountInfo);
		} catch (Exception e) {
			log.error("人行数据转换类型错误！", e);
		}
    	allBillsPublicDTO.setAccountKey(amsAccountInfo.getAccountKey());//基本户核准号
    	allBillsPublicDTO.setRegAreaCode(amsAccountInfo.getRegAreaCode());//注册地地区代码
    	allBillsPublicDTO.setDepositorName(saicIdpInfo.getName());//存款人名称
		allBillsPublicDTO.setAcctShortName(saicIdpInfo.getName());//账户简称
    	allBillsPublicDTO.setDepositorType(amsAccountInfo.getDepositorType());//存款人类别（PBC）
		allBillsPublicDTO.setRegAddress(saicIdpInfo.getAddress());//注册地址
//		allBillsPublicDTO.setRegOffice("G");//登记部门-G工商部门
    	allBillsPublicDTO.setFileNo(amsAccountInfo.getFileNo());//证明文件1编号（PBC）
    	allBillsPublicDTO.setFileType(amsAccountInfo.getFileType());//证明文件1种类（PBC）
    	allBillsPublicDTO.setFileNo2(amsAccountInfo.getFileNo2());//证明文件1编号（PBC）
    	allBillsPublicDTO.setFileType2(amsAccountInfo.getFileType2());//证明文件1种类（PBC）
    	allBillsPublicDTO.setAcctFileNo(amsAccountInfo.getAccountFileNo());//证明文件1编号（PBC）
    	allBillsPublicDTO.setAcctFileType(amsAccountInfo.getAccountFileType());//证明文件1种类（PBC）
    	allBillsPublicDTO.setAcctFileNo2(amsAccountInfo.getAccountFileNo2());//证明文件2编号（PBC）
    	allBillsPublicDTO.setAcctFileType2(amsAccountInfo.getAccountFileType2());//证明文件2种类（PBC）
		allBillsPublicDTO.setSetupDate(DateUtils.strToStrAsFormat(saicIdpInfo.getStartdate(), "yyyy-MM-dd"));//成立时间
		allBillsPublicDTO.setBusinessLicenseDue(DateUtils.strToStrAsFormat(saicIdpInfo.getEnddate(), "yyyy-MM-dd"));//到期日期
		allBillsPublicDTO.setFileSetupDate(DateUtils.strToStrAsFormat(saicIdpInfo.getStartdate(), "yyyy-MM-dd"));//成立时间
		allBillsPublicDTO.setFileDue(DateUtils.strToStrAsFormat(saicIdpInfo.getEnddate(), "yyyy-MM-dd"));//到期日期
		if(StringUtils.isNotBlank(saicIdpInfo.getRegistfund())) {
			allBillsPublicDTO.setRegisteredCapital(NumberUtils.convertCapital(saicIdpInfo.getRegistfund()));//注册资金
			if(StringUtils.contains(saicIdpInfo.getRegistfund(), "人民币")) {//只处理人民币的类型
				allBillsPublicDTO.setRegCurrencyType("CNY");
			}
		}
		allBillsPublicDTO.setBusinessScope(saicIdpInfo.getScope());//经营范围
    	allBillsPublicDTO.setLegalType(amsAccountInfo.getLegalType());//法人代表（负责）人（PBC）
		allBillsPublicDTO.setLegalName(saicIdpInfo.getLegalperson());//法人姓名
    	allBillsPublicDTO.setLegalIdcardNo(amsAccountInfo.getLegalIdcardNo());//法人证件号码（PBC）
    	allBillsPublicDTO.setLegalIdcardType(amsAccountInfo.getLegalIdcardType());//法人证件类型（PBC）
//    	allBillsPublicDTO.setLegalIdcardDue(DateUtils.DateToStr(DateUtils.parseDate(amsAccountInfo.getLegalIdcardToVoidDate()), "yyyy-MM-dd"));//法人证件到期日（PBC）
//    	allBillsPublicDTO.setOrgCodeDue(DateUtils.DateToStr(DateUtils.parseDate(amsAccountInfo.getOrgCodeToVoidDate()), "yyyy-MM-dd"));//组织机构证到期日（PBC）
//    	allBillsPublicDTO.setTaxDue(DateUtils.DateToStr(DateUtils.parseDate(amsAccountInfo.getTaxRegNoToVoidDate()), "yyyy-MM-dd"));//纳税人识别号（地税）到期日（PBC）
    	allBillsPublicDTO.setOrgCode(amsAccountInfo.getOrgCode());//组织机构代号（PBC）
    	allBillsPublicDTO.setTaxRegNo(amsAccountInfo.getTaxRegNo());//纳税人识别号（地税）（PBC）
    	allBillsPublicDTO.setZipcode(amsAccountInfo.getZipCode());//邮政编码（PBC）
    	allBillsPublicDTO.setTelephone(amsAccountInfo.getTelephone());//联系电话（PBC）
//    	allBillsPublicDTO.setFinanceName(amsAccountInfo.getFinanceName());//财务主管姓名（PBC）
    	allBillsPublicDTO.setParAccountKey(amsAccountInfo.getParAccountKey());//上级机构信息-基本户开户许可核准号（PBC）
    	allBillsPublicDTO.setParOrgCode(amsAccountInfo.getParOrgCode());//上机机构信息-组织机构代码（PBC）
    	allBillsPublicDTO.setParCorpName(amsAccountInfo.getParCorpName());//上机机构信息-机构名称（PBC）
    	allBillsPublicDTO.setParLegalName(amsAccountInfo.getParLegalName());//上级机构信息-法人姓名（PBC）
    	allBillsPublicDTO.setParLegalType(amsAccountInfo.getParLegalType());//上级机构信息-法人类型（PBC）
    	allBillsPublicDTO.setParLegalIdcardType(amsAccountInfo.getParLegalIdcardType());//上级机构信息-法人证件类型（PBC）
    	allBillsPublicDTO.setParLegalIdcardNo(amsAccountInfo.getParLegalIdcardNo());//上级机构信息-法人证件号码（PBC）
//    	allBillsPublicDTO.setParLegalIdcardDue(DateUtils.DateToStr(DateUtils.parseDate(amsAccountInfo.getParLegalIdcardDate()), "yyyy-MM-dd"));//上级机构信息-上级法人证件到期日（PBC）
		List<StockHolderDto> stockholders = saicIdpInfo.getStockholders();
		Set<CompanyPartnerInfo> companyPartnerInfos = new HashSet<CompanyPartnerInfo>();
		for (StockHolderDto stockHolderDto : stockholders) {
			CompanyPartnerInfo companyPartnerInfo = new CompanyPartnerInfo();
			companyPartnerInfo.setPartnerType(stockHolderDto.getStrtype());//股东类型
			companyPartnerInfo.setName(stockHolderDto.getName());//姓名
			companyPartnerInfos.add(companyPartnerInfo);
		}
		allBillsPublicDTO.setCompanyPartners(companyPartnerInfos);

		allBillsPublicDTO.setRegFullAddress(saicIdpInfo.getAddress());//详细地址
		allBillsPublicDTO.setWorkAddress(saicIdpInfo.getAddress());//工作地址
		allBillsPublicDTO.setBusinessScopeEccs(saicIdpInfo.getScope());//经营范围--信用机构
		allBillsPublicDTO.setBasicAcctRegArea(amsAccountInfo.getBasicAcctRegArea());//基本户注册地地区代码(PBC)
		if(StringUtils.isNotBlank(saicIdpInfo.getUnitycreditcode())) {//存在18位的营业执照编号
			allBillsPublicDTO.setRegType("07");//工商注册类型-07-统一社会信用代码
			allBillsPublicDTO.setRegNo(saicIdpInfo.getUnitycreditcode());//工商注册编号
			allBillsPublicDTO.setFileType("00");//证明文件1种类-社会统一信用代码
	    	allBillsPublicDTO.setFileNo(saicIdpInfo.getUnitycreditcode());//证明文件1编号--工商注册号
	    	allBillsPublicDTO.setBusinessLicenseNo(saicIdpInfo.getUnitycreditcode());//营业执照编号
	    	allBillsPublicDTO.setOrgCode(StringUtils.substring(saicIdpInfo.getUnitycreditcode(), 8, 17));//第9—17位数字：代表主体识别码，也就是组织机构代码。
			String areaCode = StringUtils.substring(saicIdpInfo.getUnitycreditcode(),2,8);
	    	List<AreaDto> areas = areaService.getRegistAreaCode(areaCode);
	    	if(areas.size()==1 && areas.get(0).getRegCode() != null) {
		    	allBillsPublicDTO.setRegAreaCode(areas.get(0).getRegCode());
	    	}
	    	AreaDto[] array =  areaService.getRegistLists(areaCode);
	    	if(array[0] !=null) {
	    		allBillsPublicDTO.setRegProvince(array[0].getAreaCode());
	    	}
	    	if(array[1] !=null) {
	    		allBillsPublicDTO.setRegCity(array[1].getAreaCode());
	    	}
	    	if(array[2] !=null) {
	    		allBillsPublicDTO.setRegArea(array[2].getAreaCode());
	    	}
//	    	allBillsPublicDTO.setTaxRegNo(saicIdpInfo.getUnitycreditcode());//纳税人识别号（地税）
//	    	allBillsPublicDTO.setStateTaxRegNo(saicIdpInfo.getUnitycreditcode());//纳税人识别号（国税）
		}else if(StringUtils.isNotBlank(saicIdpInfo.getRegistno())){
			allBillsPublicDTO.setRegType("01");//工商注册类型-01-工商注册号
			allBillsPublicDTO.setRegNo(saicIdpInfo.getRegistno());//工商注册编号
			allBillsPublicDTO.setFileType("01");//证明文件1种类-工商营业执照
	    	allBillsPublicDTO.setFileNo(saicIdpInfo.getRegistno());//证明文件1编号--工商注册号
	    	allBillsPublicDTO.setBusinessLicenseNo(saicIdpInfo.getRegistno());//营业执照编号
	    	String areaCode = saicIdpInfo.getRegistno().substring(0, 6);
	    	List<AreaDto> areas = areaService.getRegistAreaCode(areaCode);
	    	if(areas.size()==1 && areas.get(0).getRegCode() != null) {
		    	allBillsPublicDTO.setRegAreaCode(areas.get(0).getRegCode());
	    	}
	    	AreaDto[] array =  areaService.getRegistLists(areaCode);
	    	if(array[0] !=null) {
	    		allBillsPublicDTO.setRegProvince(array[0].getAreaCode());
	    	}
	    	if(array[1] !=null) {
	    		allBillsPublicDTO.setRegCity(array[1].getAreaCode());
	    	}
	    	if(array[2] !=null) {
	    		allBillsPublicDTO.setRegArea(array[2].getAreaCode());
	    	}
		}
    }

    /**
     * pbc和客户数据进行比较
     *
     * 企业名称、注册地址、法人名称
     * @param allBillsPublicDTO
     * @param amsAccountInfo
     * @return
     */
    public boolean compareLocalWithPBC(AllBillsPublicDTO allBillsPublicDTO,AmsAccountInfo amsAccountInfo) {
    	return StringUtils.equals(allBillsPublicDTO.getDepositorName(), amsAccountInfo.getDepositorName())
    			&& StringUtils.equals(allBillsPublicDTO.getRegAddress(), amsAccountInfo.getRegAddress())
    			&& StringUtils.equals(allBillsPublicDTO.getLegalName(), amsAccountInfo.getLegalName())
    			&& StringUtils.equals(allBillsPublicDTO.getFileNo(), amsAccountInfo.getFileNo());
    }


    /**
     * kyc和客户数据进行比较
     *
     * 企业名称、注册地址、法人名称
     * @param allBillsPublicDTO
     * @param saicIdpInfo
     * @return
     */
    public boolean compareLocalWithKYC(AllBillsPublicDTO allBillsPublicDTO,SaicIdpInfo saicIdpInfo) {
    	return StringUtils.equals(allBillsPublicDTO.getDepositorName(), saicIdpInfo.getName())
    			&& StringUtils.equals(allBillsPublicDTO.getRegAddress(), saicIdpInfo.getAddress())
    			&& StringUtils.equals(allBillsPublicDTO.getLegalName(), saicIdpInfo.getLegalperson())
    			&& StringUtils.equals(allBillsPublicDTO.getFileNo(), (saicIdpInfo.getUnitycreditcode() !=null) ? saicIdpInfo.getUnitycreditcode() : saicIdpInfo.getRegistno());
    }


	/**
	 * pbc和kyc数据进行比较
	 *
	 * 企业名称、注册地址、法人名称
	 * @param amsAccountInfo
	 * @param saicIdpInfo
	 * @return
	 */
	public boolean comparePBCWithKYC(AmsAccountInfo amsAccountInfo,SaicIdpInfo saicIdpInfo) {
		return StringUtils.equals(amsAccountInfo.getDepositorName(), saicIdpInfo.getName())
				&& StringUtils.equals(amsAccountInfo.getRegAddress(), saicIdpInfo.getAddress())
				&& StringUtils.equals(amsAccountInfo.getLegalName(), saicIdpInfo.getLegalperson())
				&& StringUtils.equals(amsAccountInfo.getFileNo(), (saicIdpInfo.getUnitycreditcode() !=null) ? saicIdpInfo.getUnitycreditcode() : saicIdpInfo.getRegistno());
	}

    /**
     *  pbc覆盖客户数据
     *
     * 企业名称、注册地址、法人名称
     * @param allBillsPublicDTO
     * @param amsAccountInfo
     */
    public void coverLocalFromPBC(AllBillsPublicDTO allBillsPublicDTO,AmsAccountInfo amsAccountInfo) {
    	allBillsPublicDTO.setDepositorName(amsAccountInfo.getDepositorName());
    	allBillsPublicDTO.setRegAddress(amsAccountInfo.getRegAddress());
    	allBillsPublicDTO.setLegalName(amsAccountInfo.getLegalName());
    }

    /**
     *  kyc覆盖客户数据
     *
     * 企业名称、注册地址、法人名称
     * @param allBillsPublicDTO
     * @param saicIdpInfo
     */
    public void coverLocalFromKYC(AllBillsPublicDTO allBillsPublicDTO,SaicIdpInfo saicIdpInfo) {
    	allBillsPublicDTO.setDepositorName(saicIdpInfo.getName());
    	allBillsPublicDTO.setRegAddress(saicIdpInfo.getAddress());
    	allBillsPublicDTO.setLegalName(saicIdpInfo.getLegalperson());
    }

	/**
	 * 当进行基于基本户开户（如一般户）时，
	 * 有本地数据时，先校验本地和人行数据是否相同，若不同，根据系统中的配置，返回前端默认账户数据。同时再进行工商和人行校验，将校验结果在前端进行提示
	 * 没有有本地数据时（此时allBillsPublicDTO参数中的值都为null值），直接进行工商和人行校验，若不相同，根据系统中的配置，返回前端默认账户数据
	 * @param saicIdpInfo	工商数据
	 * @param amsAccountInfo 人行数据
	 * @param allBillsPublicDTO  本地数据
	 */
	public void coverForConfig(SaicIdpInfo saicIdpInfo, AmsAccountInfo amsAccountInfo, AllBillsPublicDTO allBillsPublicDTO) throws Exception {
		List<ConfigDto> configList = configService.findByKey("openPriority");
		if (CollectionUtils.isNotEmpty(configList) && configList.get(0) != null) {
			String openPriority = configList.get(0).getConfigValue();
			if ("local".equals(openPriority)) {
				log.info("返显本地账管数据");
			} else if ("pbc".equals(openPriority)) {
				AllBillsPublicDTO allBillsPublicDTONew = new AllBillsPublicDTO();
				BeanCopierUtils.copyProperties(allBillsPublicDTONew, allBillsPublicDTO);//清空本地数据
				this.getFromPBC(allBillsPublicDTO, amsAccountInfo);//采用人行数据
				log.info("返显人行数据");
			} else if ("saic".equals(openPriority)) {
				AllBillsPublicDTO allBillsPublicDTONew = new AllBillsPublicDTO();
				BeanCopierUtils.copyProperties(allBillsPublicDTONew, allBillsPublicDTO);//清空本地数据
				this.getFromPBCAndKYC(allBillsPublicDTO, amsAccountInfo, saicIdpInfo);//采用部分工商数据和人行数据
				log.info("返显部分工商数据和人行数据");
			}
		}
		log.info("默认返显本地账管数据");
	}

    /**
     * 获得人行信息
     * @param accountKey
     * @param regAreaCode
     * @return
     * @throws Exception
     */
    public AmsAccountInfo getPBCAmsAccountInfo(String accountKey, String regAreaCode) throws Exception {
    	if(StringUtils.isNotBlank(accountKey) &&StringUtils.isNotBlank(regAreaCode)) {
			if (pbcMockService.isLoginMockOpen()) {
				log.info("人行挡板开启，默认返回NULL......");
    			return null;
			}
			try {
				SecurityUtils.UserInfo current = SecurityUtils.getCurrentUser();
				Long orgId = current.getOrgId();
				OrganizationDto organizationDto = organizationService.findById(orgId);
				if(organizationDto != null) {
					PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(organizationDto.getCode(), EAccountType.AMS);
					if (pbcAccountDto != null) {
						AmsCheckResultInfo amsCheckResultInfo = pbcAmsService.checkPbcByAccountKeyAndRegAreaCode(organizationDto.getCode(), accountKey, regAreaCode);
//	                AmsCheckResultInfo amsCheckResultInfo = amsMainService.checkPbcByAccountKeyAndRegAreaCode(allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto), accountKey, regAreaCode);
						if (amsCheckResultInfo.isCheckPass()) {
							return amsCheckResultInfo.getAmsAccountInfo();
						}
					}
				}
			}catch (Exception e){
				log.error(e.getMessage());
			}
    	}
    	return null;
    }


	/**
	 * 获得人行信息
	 * @param
	 * @param
	 * @return
	 * @throws Exception
	 */
	public AmsAccountInfo getPBCAmsAccountInfoByAcctNo(String acctNo) throws Exception {
		if(StringUtils.isNotBlank(acctNo) ) {
			if (pbcMockService.isLoginMockOpen()) {
				log.info("人行挡板开启，默认返回NULL......");
				return null;
			}
			try {
				SecurityUtils.UserInfo current = SecurityUtils.getCurrentUser();
				Long orgId = current.getOrgId();
				OrganizationDto organizationDto = organizationService.findById(orgId);
				if(organizationDto != null) {
					PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(organizationDto.getCode(), EAccountType.AMS);
					if (pbcAccountDto != null) {
						AmsAccountInfo amsAccountInfo = amsMainService.getAmsAccountInfoByAcctNo(allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto), acctNo);
						return amsAccountInfo;
					}
				}
			}catch (Exception e){
				log.error(e.getMessage());
			}
		}
		return null;
	}


    /**
     * 分类开户类型
     * A&C : 1
     * B : 2
     * other : 0
     * @param type
     * @return
     */
    public int checkCustomerType(String type) {
    	if(StringUtils.isNotBlank(type)) {
    		//2018-10-24 增加验资和增资
    		if(StringUtils.equals("jiben", type)||StringUtils.equals("linshi", type)||StringUtils.equals("yanzi", type)||StringUtils.equals("zengzi", type)||StringUtils.equals("teshu", type)) {
    			return 1;
    		}else if(StringUtils.equals("yiban", type)||StringUtils.equals("yusuan", type)||StringUtils.equals("feiyusuan", type)||StringUtils.equals("feilinshi", type)) {
    			return 2;
    		}
    	}
    	return 0;
    }

	/**
	 * 获取开户验证比对结果列表展示数据
	 *
	 * @param core 核心数据
	 * @param pbc  人行数据
	 * @param saic 工商数据
	 */
	public JSONArray getOpenCompareTableJson(AllBillsPublicDTO core, AmsAccountInfo pbc, SaicIdpInfo saic) {
		JSONArray compareList = new JSONArray();
		JSONObject json1 = new JSONObject();
		JSONObject json2 = new JSONObject();
		JSONObject json3 = new JSONObject();
		JSONObject json4 = new JSONObject();
		json1.put("name", "客户名称");
		json2.put("name", "注册（登记）地址");
		json3.put("name", "法人姓名");
		json4.put("name", "工商注册号");
		if (core != null) {
			json1.put("CORE", StringUtils.isNotBlank(core.getDepositorName())?core.getDepositorName():"");
			if(StringUtils.isNotBlank(core.getRegFullAddress()) && !StringUtils.equals("null",core.getRegFullAddress())){
				json2.put("CORE",core.getRegFullAddress());
			}else{
				json2.put("CORE", StringUtils.isNotBlank(core.getRegAddress())?core.getRegAddress():"");
			}
			json3.put("CORE", StringUtils.isNotBlank(core.getLegalName())?core.getLegalName():"");
			json4.put("CORE", (StringUtils.isBlank(core.getFileNo()) ||  StringUtils.equals("null",core.getFileNo()))?"":core.getFileNo());
		}
		json1.put("PBC", pbc == null ? "" : pbc.getDepositorName());
		json2.put("PBC", pbc == null ? "" : pbc.getRegAddress());
		json3.put("PBC", pbc == null ? "" : pbc.getLegalName());
		json4.put("PBC", pbc == null ? "" : pbc.getFileNo());
		json1.put("SAIC", saic == null ? "" : saic.getName());
		json2.put("SAIC", saic == null ? "" : saic.getAddress());
		json3.put("SAIC", saic == null ? "" : saic.getLegalperson());
		json4.put("SAIC", saic == null ? "" : ((StringUtils.isNotBlank(saic.getUnitycreditcode())) ? saic.getUnitycreditcode() : saic.getRegistno()));
		compareList.add(json1);
		compareList.add(json2);
		compareList.add(json3);
		compareList.add(json4);
		return compareList;
	}

	private void setFileType(AmsAccountInfo amsAccountInfo) {

		//通用部分赋值
		seturrencyValue(amsAccountInfo);

		//根据账户类型赋值
		if (amsAccountInfo.getAcctType() == AccountType.jiben) {
			getJibenFile(amsAccountInfo);
			//存款人类型
			if (StringUtils.isNotBlank(amsAccountInfo.getDepositorType())) {
				amsAccountInfo.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)", amsAccountInfo.getDepositorType()));
			}
		}
		if (amsAccountInfo.getAcctType() == AccountType.linshi) {
			if (StringUtils.isNotBlank(amsAccountInfo.getFileType())) {
				amsAccountInfo.setFileType(dictionaryService.transalteLike("证明文件1种类(临时户)", amsAccountInfo.getFileType()));
			}
//			//临时户账户许可证号
//			if (StringUtils.isNotBlank(amsAccountInfo.getAccountLicenseNo())) {
//				amsAccountInfo.setAccountLicenseNo(amsAccountInfo.getAccountLicenseNo());
//			}
		}
		if (amsAccountInfo.getAcctType() == AccountType.teshu) {
			if (StringUtils.isNotBlank(amsAccountInfo.getFileType())) {
				amsAccountInfo.setAccountFileType(dictionaryService.transalteLike("证明文件1种类(特殊户)", amsAccountInfo.getAccountFileType()));
			}
		}
		if (amsAccountInfo.getAcctType() == AccountType.yiban) {
			if (StringUtils.isNotBlank(amsAccountInfo.getAccountFileType())) {
				amsAccountInfo.setAccountFileType(dictionaryService.transalteLike("证明文件1种类(一般户)", amsAccountInfo.getAccountFileType()));
			}
			//基本户种类赋值
			getJibenFile(amsAccountInfo);
		}
		if (amsAccountInfo.getAcctType() == AccountType.feiyusuan || amsAccountInfo.getAcctType() == AccountType.yusuan) {
			if (StringUtils.isNotBlank(amsAccountInfo.getAccountFileType())) {
				amsAccountInfo.setAccountFileType(dictionaryService.transalteLike("证明文件1种类(专用户)", amsAccountInfo.getAccountFileType()));
			}
			if (StringUtils.isNotBlank(amsAccountInfo.getAccountFileType2())) {
				amsAccountInfo.setAccountFileType2(dictionaryService.transalteLike("证明文件2种类(专用户)", amsAccountInfo.getAccountFileType2()));
			}
//			if (StringUtils.isNotBlank(amsAccountInfo.getAccountFileNo())) {
//				amsAccountInfo.setAccountFileNo(amsAccountInfo.getAccountFileNo());
//			}
//			if (StringUtils.isNotBlank(amsAccountInfo.getAccountFileNo2())) {
//				amsAccountInfo.setAccountFileNo2(amsAccountInfo.getAccountFileNo2());
//			}
			//基本户证明文件种类赋值
			getJibenFile(amsAccountInfo);
		}
		if (amsAccountInfo.getAcctType() == AccountType.feilinshi) {
			if (StringUtils.isNotBlank(amsAccountInfo.getAccountFileType())) {
				amsAccountInfo.setAccountFileType(dictionaryService.transalteLike("证明文件1种类(非临时)", amsAccountInfo.getAccountFileType()));
			}
			//基本户证明文件种类赋值
			getJibenFile(amsAccountInfo);
		}
	}

	private void seturrencyValue(AmsAccountInfo amsAccountInfo) {
		//法人证件类型
		if (StringUtils.isNotBlank(amsAccountInfo.getLegalIdcardType())) {
			amsAccountInfo.setLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型", amsAccountInfo.getLegalIdcardType()));
		}
		//币种
		if (StringUtils.isNotBlank(amsAccountInfo.getRegCurrencyType())) {
			amsAccountInfo.setRegCurrencyType(dictionaryService.transalteLike("注册币种", amsAccountInfo.getRegCurrencyType()));
		}
//		//资金转换BigDecimal
//		if (StringUtils.isNotBlank(amsAccountInfo.getRegisteredCapital())) {
//			amsAccountInfo.setRegisteredCapital(amsAccountInfo.getRegisteredCapital());
//		}
		//行业归属
		if (StringUtils.isNotBlank(amsAccountInfo.getIndustryCode())) {
			amsAccountInfo.setIndustryCode(dictionaryService.transalteLike("行业归属", amsAccountInfo.getIndustryCode()));
		}
//		//邮政编码
//		if (StringUtils.isNotBlank(amsAccountInfo.getZipCode())) {
//			amsAccountInfo.setZipCode(amsAccountInfo.getZipCode());
//		}
		//账户名称构成方式
		if (StringUtils.isNotBlank(amsAccountInfo.getAccountNameFrom())) {
			amsAccountInfo.setAccountNameFrom(dictionaryService.transalteLike("账户构成方式", amsAccountInfo.getAccountNameFrom()));
		}
		//资金性质
		if (StringUtils.isNotBlank(amsAccountInfo.getCapitalProperty())) {
			amsAccountInfo.setCapitalProperty(dictionaryService.transalteLike("资金性质", amsAccountInfo.getCapitalProperty()));
		}
		//上级法人证件类型
		if (StringUtils.isNotBlank(amsAccountInfo.getParLegalIdcardType())) {
			amsAccountInfo.setParLegalIdcardType(dictionaryService.transalteLike("法人身份证件类型", amsAccountInfo.getParLegalIdcardType()));
		}
	}

	private void getJibenFile(AmsAccountInfo amsAccountInfo) {
		if (StringUtils.isNotBlank(amsAccountInfo.getFileType())) {
			amsAccountInfo.setFileType(dictionaryService.transalteLike("证明文件1种类(基本户)", amsAccountInfo.getFileType()));
		}
		if (StringUtils.isNotBlank(amsAccountInfo.getFileType2())) {
			amsAccountInfo.setFileType2(dictionaryService.transalteLike("证明文件2种类(基本户)", amsAccountInfo.getFileType2()));
		}
		//存款人类型
		if (StringUtils.isNotBlank(amsAccountInfo.getDepositorType())) {
			amsAccountInfo.setDepositorType(dictionaryService.transalteLike("存款人类别(基本户)", amsAccountInfo.getDepositorType()));
		}
	}
}
