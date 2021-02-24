package com.ideatech.ams.controller;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dao.AccountPublicDao;
import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.core.AmsCoreAccountService;
import com.ideatech.ams.customer.dao.CustomerPublicDao;
import com.ideatech.ams.customer.service.newcompany.FreshCompanyService;
import com.ideatech.ams.dto.SyncCompareInfo;
import com.ideatech.ams.kyc.util.DateUtils;
import com.ideatech.ams.pbc.utils.NumberUtils;
import com.ideatech.ams.service.EsbService;
import com.ideatech.ams.service.SyncCompareService;
import com.ideatech.ams.service.SyncCoreComparOpenAcctService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.ws.api.service.PbcApiService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testJnns")
@Slf4j
public class JnyhTestController {
    @Autowired
    private PbcApiService pbcApiService;


    @Autowired
    private AccountsAllDao accountsAllDao;



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
    private EsbService esbService;

    @Autowired
    private SyncCompareService syncCompareService;


    @Autowired
    private AccountPublicDao accountPublicDao;


    @Autowired
    private CustomerPublicDao customerPublicDao;

    @Autowired
    private SyncCoreComparOpenAcctService syncCoreComparOpenAcctService;
    Pageable pageable;

    @Value(("${change.file}"))
    private String changeFile;

    @GetMapping("/dianliang")
    public ResultDto dianliang() {
        Map<String, Map<String, String>> map= esbService.getUsernameDianLiang();
        ResultDto resultDto = new ResultDto();
        resultDto.setMessage(map.toString());
        return resultDto;
    }

    @GetMapping("/save")
    public void save() {
        SyncCompareInfo info=new SyncCompareInfo();
        info.setAcctNo("111111111111112222");
        info.setAcctOpenDate(DateUtils.getCurrentDate());
        info.setAcctType("feiyusuan");
        info.setDepositorName("浙江省易得融信軟件有限公司");
        info.setOrganCode("1111");
        info.setOrganFullId("11111-222222-33333");
        syncCompareService.create(info);
        System.out.println("-----测试完毕-------");
    }

    @PostMapping(value ="/testChangePast")
    public ResultDto testChangePast(@RequestBody AllBillsPublicDTO bill, @RequestParam String org) {
        ResultDto resultDto = pbcApiService.syncPbc(org, bill);
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


    @RequestMapping(value = "/testChange")
    public ResultDto testChange() {
        AllBillsPublicDTO billsPublic = new AllBillsPublicDTO();

        billsPublic.setBillType(BillType.ACCT_OPEN);
        billsPublic.setAcctType(CompanyAcctType.yiban);
        billsPublic.setAccountKey("j1234567890123");
        billsPublic.setAcctNo("987987987");
        billsPublic.setPbcSyncStatus(CompanySyncStatus.weiTongBu);
        billsPublic.setOrganCode("root");

        billsPublic.setDepositorName("宁波海远环境电器有限公司");
        billsPublic.setBankName("组织机构根节点");
        billsPublic.setAcctCreateDate("2018-11-16");
        billsPublic.setDepositorType("02");
        billsPublic.setRegProvince("330000");
        billsPublic.setRegProvinceChname("浙江省");
        billsPublic.setRegCity("330200");
        billsPublic.setRegCityChname("宁波市");
        billsPublic.setRegArea("330212");
        billsPublic.setRegAreaChname("鄞州区");
        billsPublic.setRegCountry("1");
        billsPublic.setRegAddress("宁波市鄞州区天童南路555号");
        billsPublic.setRegFullAddress("宁波市鄞州区天童南路555号");
        billsPublic.setRegAreaCode("332000");
        billsPublic.setIndustryCode("B$$采矿业$$第二产业            $$2");
        billsPublic.setRegOffice("G");
        billsPublic.setRegType("07");
        billsPublic.setRegNo("91330212583978982F");
        billsPublic.setFileType("02");
        billsPublic.setFileNo("91330212583978982F");
        billsPublic.setFileSetupDate("2011-11-09");
        billsPublic.setFileDue("9999-09-09");
        billsPublic.setBusinessLicenseNo("91330212583978982F");
        billsPublic.setBusinessLicenseDue("9999-09-09");
        billsPublic.setIsIdentification("1");
        billsPublic.setRegCurrencyType("CNY");
        billsPublic.setRegisteredCapital(NumberUtils.formatCapital(NumberUtils.convertCapital("3000000")));
        billsPublic.setBusinessScope("新风机、空气净化设备、饮水设备、净水器、中央空调、数码电子产品、仪器仪表、加湿器、除湿机、工艺品、文化用品、办公用品的销售;室内空气治理、空气净化设备租赁、零售及网上销售;机械设备技术咨询服务。(依法须经批准的项目,经相关部门批准后方可开展经营活动)");
        billsPublic.setBusinessScopeEccs("新风机、空气净化设备、饮水设备、净水器、中央空调、数码电子产品、仪器仪表、加湿器、除湿机、工艺品、文化用品、办公用品的销售;室内空气治理、空气净化设备租赁、零售及网上销售;机械设备技术咨询服务。(依法须经批准的项目,经相关部门批准后方可开展经营活动)");
        billsPublic.setLegalType("1");
        billsPublic.setLegalName("崔红亚");
        billsPublic.setLegalIdcardType("2");
        billsPublic.setLegalIdcardNo("360203195701141530");
        billsPublic.setLegalIdcardDue("2018-11-30");
        billsPublic.setOrgType("2");
        billsPublic.setOrgTypeDetail("21");
        billsPublic.setStateTaxRegNo("91330212583978982F");
        billsPublic.setTaxRegNo("91330212583978982F");
        billsPublic.setIsSameRegistArea("1");
        billsPublic.setWorkProvince("330000");
        billsPublic.setWorkProvinceChname("浙江省");
        billsPublic.setWorkCity("330200");
        billsPublic.setWorkCityChname("宁波市");
        billsPublic.setWorkArea("330212");
        billsPublic.setWorkAreaChname("鄞州区");
        billsPublic.setWorkAddress("宁波市鄞州区天童南路555号");
        billsPublic.setTelephone("18367803054");
        billsPublic.setZipcode("657856");
        billsPublic.setFinanceTelephone("18367803054");
        billsPublic.setEconomyIndustryCode("天然气开采");
        billsPublic.setEconomyIndustryName("b0720");
        billsPublic.setOrgStatus("1");
        billsPublic.setBasicAccountStatus("1");

        String o = JSONObject.toJSONString(billsPublic);

        ResultDto resultDto = pbcApiService.reenterablePbcSync(billsPublic.getOrganCode(), billsPublic);
        return resultDto;
    }

    @RequestMapping(value = "/testBills")
    public void test() throws Exception {
        accountBillsAllService.updateApplyStatus();
    }

    @RequestMapping(value = "/testAddFreshCompany")
    public void testAddFreshCompany() throws Exception {
        freshCompanyService.add("ZJ", "20181001", "20181027");
    }

    private AllBillsPublicDTO convertToObject(List<String> s) {
        AllBillsPublicDTO allBillsPublicDTO = new AllBillsPublicDTO();
        if(s.size() >1) {
            String[] method = StringUtils.splitByWholeSeparatorPreserveAllTokens(s.get(0), "|!");
            String[] values = StringUtils.splitByWholeSeparatorPreserveAllTokens(s.get(1), "|!");
            amsCoreAccountService.arrayToCorePublicAccount(method, values, allBillsPublicDTO,new HashMap<String, Integer>());
            System.out.println(allBillsPublicDTO.toString());
        }
        return allBillsPublicDTO;
    }


    @GetMapping("/check")
    public void CkeckStatus(HttpServletResponse res, @RequestParam("id") String ids ) {
        String[] idList = ids.split(",");
        for (String id : idList) {
            accountsAllDao.updateckeckStatus(Long.valueOf(id));
        }
    }

    @GetMapping("/check1")
    public void CkeckStatus1(HttpServletResponse res, @RequestParam("id") String ids ) {
        String[] idList = ids.split(",");
        for (String id : idList) {
            customerPublicDao.updateCheckStatus1( Long.valueOf(id));
        }
    }

  /*  @GetMapping("/check2")
    public void CkeckStatus2(HttpServletResponse res, @RequestParam("id") String ids ) {
        String[] idList = ids.split(",");
        for (String id : idList) {
            accountsAllDao.updateCheckStatus1( Long.valueOf(id));

        }
    }*/
  @GetMapping("/heGuiYuJing")
  public void heGuiYuJing()  {
      syncCoreComparOpenAcctService.insertYuJingData( pageable);
  }


}
