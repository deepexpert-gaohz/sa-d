package com.ideatech.ams.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.core.AmsCoreAccountService;
import com.ideatech.ams.account.service.core.TpoService;
import com.ideatech.ams.annual.service.export.AnnualResultExportService;
import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.service.CompanyPreOpenAccountEntService;
import com.ideatech.ams.customer.service.newcompany.FreshCompanyService;
import com.ideatech.ams.pbc.dto.auth.AmsAuth;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.service.ams.AmsSearchService;
import com.ideatech.ams.pbc.service.ams.cancel.AmsResetJiBenPrintService;
import com.ideatech.ams.pbc.utils.AtomicLongUtils;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.system.whitelist.dto.WhiteListDto;
import com.ideatech.ams.system.whitelist.service.ExternalWhiteListService;
import com.ideatech.ams.ws.api.service.ApplyApiService;
import com.ideatech.ams.ws.api.service.PbcApiService;
import com.ideatech.ams.ws.api.service.SaicApiService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.util.ApplicationContextUtil;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

	@Autowired
	private PbcApiService pbcApiService;

	@Autowired
	private AmsCoreAccountService amsCoreAccountService;
	
	@Autowired
	private AllBillsPublicService allBillsPublicService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private AccountBillsAllService accountBillsAllService;

	@Autowired
	private FreshCompanyService freshCompanyService;

	@Autowired
	private SaicApiService saicApiService;

	@Autowired
	private TpoService tpoService;

	@Autowired
	private ExternalWhiteListService externalWhiteListService;

	@Autowired
    private CompanyPreOpenAccountEntService companyPreOpenAccountEntService;

	@Autowired
	private ApplyApiService applyApiService;

	@Autowired
	private AmsResetJiBenPrintService amsResetJiBenPrintService;

	@Autowired
	private AmsSearchService amsSearchService;

	@Value(("${change.file}"))
	private String changeFile;

	@Autowired
	private AnnualResultExportService annualResultExportService;

	@Autowired
	private ConfigurableEnvironment environment;

	@GetMapping(value ="/whiteListSaveTest")
	public void whiteListSaveTest(String name,String organCode) {
		externalWhiteListService.whiteLististSave(name,organCode);
	}

	@GetMapping(value ="/whiteListDel")
	public void whiteListDel(String name ,String organCode) {
		externalWhiteListService.whiteListDel(name,organCode);
	}

	@GetMapping(value ="/whiteListSearchTest")
	public WhiteListDto whiteListSearchTest(String name,String organCode) {
		WhiteListDto dto = externalWhiteListService.searchWhiteObj(name,organCode);
		return dto;
	}

	@PostMapping(value ="/testChangePast")
	public ResultDto testChangePast(String organCode, AllBillsPublicDTO allBillsPublicDTO) {
		ResultDto resultDto = pbcApiService.reenterablePbcSync(organCode, allBillsPublicDTO,true,true);
		return resultDto;
	}

	@GetMapping(value ="/testChangePast")
	public ResultDto testChangePast(String accountKey, String regAreaCode, String organCode) {
		ResultDto resultDto = pbcApiService.checkPbcInfo( accountKey,  regAreaCode,  organCode);
		return resultDto;
	}

	@GetMapping(value ="/testSaicInterface")
	public ResultDto testSaicInterface(String name) {
		ResultDto resultDto = saicApiService.querySaicLocal(name);
		return resultDto;
	}

	@GetMapping(value ="/testSaicFull")
	public ResultDto testSaicFull(String name) {
		ResultDto resultDto = saicApiService.querySaicFull(name);
		return resultDto;
	}
	
	@RequestMapping(value = "/testUpdateStatus")
	public void testUpdateStatus(Long billId) throws Exception {
		AllBillsPublicDTO allBillsPublicDTO = allBillsPublicService.findOne(billId);

        String currentUserName = SecurityUtils.getCurrentUsername();
        UserDto userDto;
        if (StringUtils.isNotBlank(currentUserName)) {
            userDto = userService.findByUsername(currentUserName);
        } else {
            userDto = userService.findVirtualUser();
        }
		allBillsPublicService.updateFinalStatus(allBillsPublicDTO, userDto.getId());
	}

	@RequestMapping(value = "/updateFinalStatus")
	public void testUpdateStatus(String billNo) throws Exception {
		AllBillsPublicDTO allBillsPublicDTO = allBillsPublicService.findFullInfoByBillNo(billNo);

		String currentUserName = SecurityUtils.getCurrentUsername();
		UserDto userDto;
		if (StringUtils.isNotBlank(currentUserName)) {
			userDto = userService.findByUsername(currentUserName);
		} else {
			userDto = userService.findVirtualUser();
		}
		allBillsPublicService.updateFinalStatus(allBillsPublicDTO, userDto.getId());
	}
	
	
	@RequestMapping(value = "/testChange")
	public ResultDto testChange(String depositorName,String acctNo,String acctType,String org,String accountKey) {
		try {
			List<String> s =FileUtils.readLines(new File(changeFile), "GBK");
//			String s = FileUtils.readFileToString(new File(changeFile), "GBk");
			AllBillsPublicDTO billsPublic = convertToObject(s);

//            billsPublic.setDepositorName("宁波耐吉新星自动化设备有限公司");
            billsPublic.setDepositorName(depositorName);
            billsPublic.setCredentialType("01");
            billsPublic.setCredentialNo("01");
//            billsPublic.setAcctNo("009999");
            billsPublic.setAcctNo(acctNo);
            billsPublic.setCustomerNo("");
            billsPublic.setBillType(BillType.ACCT_CHANGE);
            billsPublic.setAcctType(CompanyAcctType.str2enum(acctType));
            billsPublic.setAccountKey(accountKey);

			String o = JSONObject.toJSONString(billsPublic);
			log.info(o);

			ResultDto resultDto = pbcApiService.syncPbc(org, billsPublic);
//            billsPublic.setAcctType(CompanyAcctType.yusuan);
//            ResultDto resultDto = pbcApiService.syncPbc("root", billsPublic);
            log.info(JSON.toJSONString(resultDto));
            return resultDto;
		} catch (IOException e) {
		}
		return new ResultDto("报错","报错",null);
	}

	@RequestMapping(value = "/testBills")
	public void test() throws Exception {
		accountBillsAllService.updateApplyStatus();
	}

	@RequestMapping(value = "/testAddFreshCompany")
	public void testAddFreshCompany() throws Exception {
		freshCompanyService.add("ZJ", "20181001", "20181027");
	}

	@GetMapping("/tpo")
	public void testTpo(){
		tpoService.processTxtFile();
	}


	private AllBillsPublicDTO convertToObject(List<String> s) {
		AllBillsPublicDTO allBillsPublicDTO = new AllBillsPublicDTO();
		if(s.size() >1) {
			String[] method = StringUtils.splitByWholeSeparatorPreserveAllTokens(s.get(0), "|!");
			String[] values = StringUtils.splitByWholeSeparatorPreserveAllTokens(s.get(1), "|!");
			amsCoreAccountService.arrayToCorePublicAccount(method, values, allBillsPublicDTO,new HashMap<String, Integer>());
			log.info(allBillsPublicDTO.toString());
		}
		return allBillsPublicDTO;
	}

    @GetMapping("/getAcctNoAndAccountStatus")
    public Object getAcctNoAndAccountStatus(String applyId,String name){
        CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto = null;
        Object obj = applyApiService.getAcctNoAndAccountStatus(applyId,name);
        companyPreOpenAccountEntDto = (CompanyPreOpenAccountEntDto)obj;
        return companyPreOpenAccountEntDto;
    }

    @GetMapping("/queryApplyIdAndName")
    public Object queryApplyIdAndName(String applyId,String name){
        CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto = applyApiService.queryApplyIdAndName(applyId,name);
        if(companyPreOpenAccountEntDto == null){
            return null;
        }
        return companyPreOpenAccountEntDto;
    }


	@GetMapping("/session")
	public String test(HttpServletRequest request){
		HttpSession session = request.getSession();
		return (JSON.toJSONString(session));
	}

	@GetMapping("/jibenResetPrint")
	public void jibenResetPrint(HttpServletRequest request)  throws Exception {
		amsResetJiBenPrintService.resetJiBenPrintFristStep(new AmsAuth("1.1.1.1","222222","222222"),"123123","root","123");
//		amsResetJiBenPrintService.resetJiBenPrintSecondStep(new AmsAuth("1.1.1.1","222222","222222"));
	}

	@GetMapping("/statistsics/{taskId}")
	public void statistsics(@PathVariable("taskId") Long taskId) {
		annualResultExportService.createAnnualResultExport(taskId);
		log.info("年检统计导出结束！");

	}

	@GetMapping("/testAtomicLong1")
	public void testAtomicLong1()  throws Exception {
        AtomicLongUtils.resetNum(2L);
	}

	//是否启用人行采集限制机制
	private Boolean pbcCollectionLimitUse = true;

	//人行采集数量控制
	private Long pbcCollectionLimitNum = 2l;

	@GetMapping("/testAtomicLong2")
	public void testAtomicLong2()  throws Exception {
		LoginAuth auth = new AmsAuth("1.1.1.1","222222","222222");
		if(pbcCollectionLimitUse && !AtomicLongUtils.isPause(pbcCollectionLimitNum)){
			log.info("进入人行查询");
			AtomicLongUtils.al.getAndIncrement();

			amsSearchService.getAmsAccountInfoByRevokeAcctNo(auth,"123123123");
		}
	}

	@GetMapping("/testAtomicLong3")
	public void testAtomicLong3()  throws Exception {
		LoginAuth auth = new AmsAuth("1.1.1.1","222222","222222");
		amsSearchService.getAmsAccountInfoByAccountKey(auth,"J1111111111111","123123");
	}

	@GetMapping("/api/testSynchronizeData")
	public Map<String, Object> testSynchronizeData(Long id) {
		AllBillsPublicDTO billsPublic = allBillsPublicService.findOne(id);
		if(billsPublic!=null){
			Map<String, String> formData = new HashMap<>();
			if ( billsPublic.getPbcSyncStatus() != CompanySyncStatus.tongBuChengGong) {
				formData.put("isSyncAms","true");
			}else{
				formData.put("isSyncAms","false");
			}
			if (billsPublic.getEccsSyncStatus() != CompanySyncStatus.tongBuChengGong) {
				formData.put("isSyncEccs","true");
			}else{
				formData.put("isSyncEccs","true");
			}
			return allBillsPublicService.synchronizeData(formData, billsPublic);
		}else{
			log.info("流水未找到，流水id："+id);
			return null;
		}
	}
    @GetMapping("/prop")
    public Object printProp() throws Exception {
        //打印所有配置文件信息
        return ApplicationContextUtil.getAllProp(environment);
    }

	@GetMapping("/testCunLiang")
	public void testCunliang() throws Exception {
		File file = new File("/Users/chenluxiang/Desktop/txt-test/testCunliang.txt");
		List<String> list = new ArrayList<>();
		String head = "acctNo|!customerNo|!depositorName|!acctBigType|!organFullId|!accountStatus";
		list.add(head);
		for(int i = 0; i < 4000; i++){
			list.add( i + "|!6600120"+i+"|!河北森野园林工程有限公司承秦高速秦皇岛段ＬＨ２合同项目经理部"+i+"|!jiben|!root|!B");
		}
		FileUtils.writeLines(file,"GBK",list,true);
	}
}
