package com.ideatech.ams.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.dto.OutBeneficiary.FinalBeneficiary;
import com.ideatech.ams.dto.OutBeneficiary.OutBeneDto;
import com.ideatech.ams.dto.OutEquity.Children1;
import com.ideatech.ams.dto.OutEquity.OutEquityDto;
import com.ideatech.ams.dto.esb.*;
import com.ideatech.ams.dto.saic.*;
import com.ideatech.ams.kyc.dto.*;
import com.ideatech.ams.kyc.dto.holiday.HolidayDto;
import com.ideatech.ams.kyc.dto.newcompany.OutFreshCompanyQueryDto;
import com.ideatech.ams.kyc.dto.saicentrust.EntrustResultDto;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.service.SaicRequestService;
import com.ideatech.ams.utils.HttpIntefaceUtils;
import com.ideatech.common.util.BeanCopierUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ideatech.ams.utils.DateUtils.getNowDateShort;

@Transactional
@Service("saicRequestService")
public class JnnsSaicRequestServiceImpl implements SaicRequestService {
    private static final Logger log = LoggerFactory.getLogger(JnnsSaicRequestServiceImpl.class);
    @Value("${saic.api.key:111}")
    private String realTimeKey;
    @Value("${saic.realTimeEncryptFlag}")
    private boolean realTimeEncryptFlag;
    @Value("${saic.url.realTime}")
    private String realTimeUrl;
    @Value("${saic.url.realTime-encrypt}")
    private String realTimeEncryptUrl;
    @Value("${saic.url.exact}")
    private String exactUrl;
    @Value("${saic.url.beneficiary}")
    private String beneficiaryUrl;
    @Value("${saic.url.baseAccount}")
    private String baseAccountUrl;
    @Value("${saic.url.holiday}")
    private String holidayUrl;
    @Value("${saic.url.illegalQuery}")
    private String illegalQueryUrl;
    @Value("${saic.url.carrierOperator:111}")
    private String carrierOperateUrl;
    @Value("${saic.url.freshCompany:111}")
    private String freshCompanyUrl;
    @Value("${jnns.esb.url}")
    private String esbUrl;
    @Value("${saic.url.equityShareListUrl}")
    private String equityShareListUrl;
    @Autowired
    private EsbService esbService;

    /**
     * 客户尽调信息
     *
     * @param keyword
     * @return
     */
    public SaicIdpInfo getSaicInfoRealTime(String keyword) {
        log.info("----------------------------获取即时工商信息方法进入------------------------");
        JSONObject jsonObject = new JSONObject();
        String jsonStr = jsonObject.toJSONString(getRequestBody("P00002012904", "ExactSaicInfo", "ydrx_exactSaicInfo", keyword));
        String jsonStr2 = jsonStr.replaceAll("\"body\"", "\"Body\"").replaceAll("\"header\"", "\"Header\"").replaceAll("\"transaction\"", "\"Transaction\"");
        log.info("工商请求报文！！！：" + jsonStr2);
        String httpIntefaceUtils = HttpIntefaceUtils.send(esbUrl, jsonStr2);
        String ResponseCustom = esbService.getResponseCustom(httpIntefaceUtils);
        log.info("工商提取后的返回报文" + ResponseCustom);
        if (ResponseCustom.contains("bizMsg")) {
            return null;
        } else {
            SaicInfomation saicInfomation = JSON.parseObject(ResponseCustom, SaicInfomation.class);
            SaicIdpInfo saicIdpInfo = new SaicIdpInfo();
            BeanCopierUtils.copyProperties(saicInfomation, saicIdpInfo);
            List<Changes> changes = saicInfomation.getChanges();
            ArrayList<ChangeRecordDto> listChange = new ArrayList<>();
            if (changes != null && changes.size() > 0) {
                for (Changes change : changes) {
                    ChangeRecordDto changeRecordDto = new ChangeRecordDto();
                    changeRecordDto.setAftercontent(change.getAfterContent());
                    changeRecordDto.setBeforecontent(change.getBeforeContent());
                    changeRecordDto.setChangedate(change.getChangeDate());
                    changeRecordDto.setType(change.getType());
                    listChange.add(changeRecordDto);
                }
            }
            List<Branchs> branchs = saicInfomation.getBranchs();
            ArrayList<BranchDto> listBranch = new ArrayList<>();
            if (branchs != null && branchs.size() > 0) {
                for (Branchs branch : branchs) {
                    BranchDto branchDto = new BranchDto();
                    branchDto.setName(branch.getName());
                    branchDto.setBraddr(branch.getBrAddr());
                    branchDto.setBrregno(branch.getBrRegNo());
                    branchDto.setCbuitem(branch.getCbuItem());
                    branchDto.setBrprincipal(branch.getBrPrincipal());
                    listBranch.add(branchDto);
                }
            }
            List<Employees> employees = saicInfomation.getEmployees();
            ArrayList<EmployeeDto> listEmployee = new ArrayList<>();
            if (employees != null && employees.size() > 0) {
                for (Employees employee : employees) {
                    EmployeeDto employeeDto = new EmployeeDto();
                    employeeDto.setName(employee.getName());
                    employeeDto.setJob(employee.getJob());
                    employeeDto.setSex(employee.getSex());
                    employeeDto.setType(employee.getType());
                    listEmployee.add(employeeDto);
                }
            }
            List<Stockholders> stockholders = saicInfomation.getStockholders();
            ArrayList<StockHolderDto> listStockHolder = new ArrayList<>();
            if (stockholders != null && stockholders.size() > 0) {
                for (Stockholders stockholder : stockholders) {
                    StockHolderDto stockHolderDto = new StockHolderDto();
                    stockHolderDto.setName(stockholder.getName());
                    stockHolderDto.setStrtype(stockholder.getStrType());
                    stockHolderDto.setIdcardtype(stockholder.getIdentifyType());
                    stockHolderDto.setInvesttype(stockholder.getInvestType());
                    stockHolderDto.setSubconam(stockholder.getSubconam());
                    stockHolderDto.setCondate(stockholder.getConDate());
                    stockHolderDto.setRealtype(stockholder.getRealType());
                    stockHolderDto.setRealamount(stockholder.getRealAmount());
                    stockHolderDto.setRealdate(stockholder.getRealDate());
                    stockHolderDto.setFundedratio(stockholder.getFundedRatio());
                    stockHolderDto.setRegcapcur(stockholder.getRegCapCur());
                    stockHolderDto.setIdcardno(stockholder.getIdentifyNo());
                    listStockHolder.add(stockHolderDto);
                }
            }
            List<Changemess> changemess = saicInfomation.getChangemess();
            ArrayList<ChangeMessDto> listChangeMess = new ArrayList<>();
            if (changemess != null && changemess.size() > 0) {
                for (Changemess changemess1 : changemess) {
                    ChangeMessDto changeMessDto = new ChangeMessDto();
                    changeMessDto.setInreason(changemess1.getInreason());
                    changeMessDto.setIndate(changemess1.getIndate());
                    changeMessDto.setOutreason(changemess1.getOutreason());
                    changeMessDto.setOutdate(changemess1.getOutdate());
                    changeMessDto.setOutorgan(changemess1.getOutOrgan());
                    changeMessDto.setBelongorg(changemess1.getBelongorg());
                    listChangeMess.add(changeMessDto);
                }
            }
            List<Reports> reports = saicInfomation.getReports();
            ArrayList<ReportDto> listReport = new ArrayList<>();
            if (reports != null && reports.size() > 0) {
                for (Reports report : reports) {
                    ReportDto reportDto = new ReportDto();
                    reportDto.setAnnualreport(report.getAnnualreport());
                    reportDto.setReleasedate(report.getReleasedate());
                    listReport.add(reportDto);
                }
            }

            List<Illegals> illegals = saicInfomation.getIllegals();
            ArrayList<IllegalDto> listIllegal = new ArrayList<>();
            if (illegals != null && illegals.size() > 0) {
                for (Illegals illegal : illegals) {
                    IllegalDto illegalDto = new IllegalDto();
                    illegalDto.setOrder(illegal.getOrder());
                    illegalDto.setType(illegal.getType());
                    illegalDto.setReason(illegal.getReason());
                    illegalDto.setDate(illegal.getDate());
                    illegalDto.setOrgan(illegal.getOrgan());
                    illegalDto.setReasonout(illegal.getReasonOut());
                    illegalDto.setDateout(illegal.getDateOut());
                    illegalDto.setOrganout(illegal.getOrganOut());
                    listIllegal.add(illegalDto);
                }
            }
            ArrayList<BeneficiaryDto> listBeneficiaryDto = new ArrayList<>();
            ArrayList<EquityShareDto> listEquityShareDto = new ArrayList<>();
            ArrayList<BaseAccountDto> listBaseAccountDto = new ArrayList<>();
            saicIdpInfo.setChanges(listChange);
            saicIdpInfo.setEmployees(listEmployee);
            saicIdpInfo.setBranchs(listBranch);
            saicIdpInfo.setStockholders(listStockHolder);
            saicIdpInfo.setReports(listReport);
            saicIdpInfo.setChangemess(listChangeMess);
            saicIdpInfo.setIllegals(listIllegal);
            saicIdpInfo.setBeneficiaryList(listBeneficiaryDto);
            saicIdpInfo.setEquityShareList(listEquityShareDto);
            saicIdpInfo.setBaseAccountList(listBaseAccountDto);
            saicIdpInfo.setUnitycreditcode(saicInfomation.getUnityCreditCode());
            saicIdpInfo.setRegistno(saicInfomation.getRegistNo());
            saicIdpInfo.setOpendate(saicInfomation.getOpenDate());
            saicIdpInfo.setRegistfund(saicInfomation.getRegistFund());
            saicIdpInfo.setLicensedate(saicInfomation.getLicenseDate());
            saicIdpInfo.setRegistorgan(saicInfomation.getRegistOrgan());
            saicIdpInfo.setStartdate(saicInfomation.getStartDate());
            saicIdpInfo.setEnddate(saicInfomation.getEndDate());
            log.info("查询到的工商数据" + saicIdpInfo);
            return saicIdpInfo;
        }

    }

    @Override
    public SaicIdpInfo getSaicInfoRealTime(String keyword, String username) {
        return null;
    }

    @Override
    public SaicIdpInfo getSaicInfoRealTime(String keyword, String username, SearchType type) {
        return null;
    }


    /**
     * 采集工商信息，年检使用
     *
     * @param keyword
     * @return
     */
    public String getSaicInfoExactJson(String keyword) {
        log.debug("获取即时工商信息");
        log.debug("----------------------------获取即时工商信息方法进入------------------------");
        JSONObject jsonObject = new JSONObject();
        String jsonStr = jsonObject.toJSONString(getRequestBody("P00002012904", "ExactSaicInfo", "ydrx_exactSaicInfo", keyword));
        String jsonStr2 = jsonStr.replaceAll("\"body\"", "\"Body\"").replaceAll("\"header\"", "\"Header\"").replaceAll("\"transaction\"", "\"Transaction\"");
        log.info("工商请求报文！！！：" + jsonStr2);
        String httpIntefaceUtils = HttpIntefaceUtils.send(esbUrl, jsonStr2);
        String ResponseCustom = esbService.getResponseCustom(httpIntefaceUtils);
        return ResponseCustom;
    }

    /**
     * 客户尽调，股权穿透
     *
     * @param keyword
     * @return
     */
    public OutEquityShareDto getOutEquityShareDto(String keyword) {
        log.info("----------------------------------股权信息进入--------------------------------------------------");
        JSONObject jsonObject = new JSONObject();
        String jsonStr = jsonObject.toJSONString(getRequestBody("P00001012703", "EquShareListQuery", "ydrx_equityShareList", keyword));
        String jsonStr2 = jsonStr.replaceAll("\"body\"", "\"Body\"").replaceAll("\"header\"", "\"Header\"").replaceAll("\"transaction\"", "\"Transaction\"");
        log.info("股权请求报文----------------" + jsonStr2);
        String httpIntefaceUtils = HttpIntefaceUtils.send(this.esbUrl, jsonStr2);
        String ResponseCustom = esbService.getResponseCustom(httpIntefaceUtils);
        log.info("股权结构提取后的报文：" + ResponseCustom);
        if (ResponseCustom.contains("bizMsg")) {
            return null;
        } else {
            OutEquityDto outEquityDto = JSON.parseObject(ResponseCustom, OutEquityDto.class);

            OutEquityShareDto outEquityShareDto = new OutEquityShareDto();
            BeanCopierUtils.copyProperties(outEquityDto, outEquityShareDto);
            List<Children1> childrenes = outEquityDto.getItem();
            ArrayList<EquityShareDto> listEquityShare = new ArrayList<>();
            if (childrenes != null && childrenes.size() > 0) {
                listEquityShare = getChidren(listEquityShare, childrenes, outEquityDto);
            }
            outEquityShareDto.setItem(listEquityShare);
            outEquityShareDto.setTotal(listEquityShare.size());
            log.info("整合到产品中的股权结构类：" + outEquityShareDto);
            return outEquityShareDto;
        }
    }


    /**
     * 工商查询，基本户履历
     *
     * @param keyword
     * @return
     */
    public OutBaseAccountDto getOutBaseAccountDto(String keyword) {
        log.info("-----------基本户履历方法进入------------------");
        JSONObject jsonObject = new JSONObject();
        String jsonStr = jsonObject.toJSONString(getRequestBody("P00001012841", "QueryAccRecord", "ydrx_account" +
                "Record", keyword));
        String jsonStr2 = jsonStr.replaceAll("\"body\"", "\"Body\"").replaceAll("\"header\"", "\"Header\"").replaceAll("\"transaction\"", "\"Transaction\"");
        log.info("基本户履历请求报文----------------" + jsonStr2);
        String httpIntefaceUtils = HttpIntefaceUtils.send(esbUrl, jsonStr2);
        //System.out.println("受益人---------"+jsonObject.toJSONString(getRequestBody("P00001011928", "ActBeneInfoQuery", "ydrx_finalBeneFiciary", keyword)));
        String ResponseCustom = esbService.getResponseCustom(httpIntefaceUtils);
        log.info("基本户履历提取后的报文---------------- " + ResponseCustom);
        if (ResponseCustom.contains("Msg")) {
            return null;
        } else {
            OutAccount outAccount = JSON.parseObject(ResponseCustom, OutAccount.class);
            OutBaseAccountDto outBaseAccountDto = new OutBaseAccountDto();
            BeanCopierUtils.copyProperties(outAccount, outBaseAccountDto);
            List<OutAccountBase> listOutAccountBases = outAccount.getItems();
            ArrayList<BaseAccountDto> listBaseAccount = new ArrayList<>();
            for (OutAccountBase listOutAccountBase : listOutAccountBases) {
                BaseAccountDto baseAccountDto = new BaseAccountDto();
                baseAccountDto.setLicensedate(listOutAccountBase.getLicensedate());
                baseAccountDto.setLicensekey(listOutAccountBase.getLicensekey());
                baseAccountDto.setName(listOutAccountBase.getName());
                baseAccountDto.setLicenseorg(listOutAccountBase.getLicenseorg());
                baseAccountDto.setLicensetype(listOutAccountBase.getLicensetype());
                listBaseAccount.add(baseAccountDto);
            }

            outBaseAccountDto.setItems(listBaseAccount);
            outBaseAccountDto.setTotal(outAccount.getTotal());
            log.info("整合到产品中的基本户履历类：" + outBaseAccountDto);
            return outBaseAccountDto;
        }
    }

    /**
     * 客户尽调，受益人信息查询
     *
     * @param keyword
     * @return
     */
    public OutBeneficiaryDto getOutBeneficiaryDto(String keyword) {
        log.debug("-----------------------------获取受益人方法进入--------------------------------------");
        JSONObject jsonObject = new JSONObject();
        String jsonStr = jsonObject.toJSONString(getRequestBody("P00001011928", "ActBeneInfoQuery", "ydrx_finalBeneficiary", keyword));
        String jsonStr2 = jsonStr.replaceAll("\"body\"", "\"Body\"").replaceAll("\"header\"", "\"Header\"").replaceAll("\"transaction\"", "\"Transaction\"");
        log.info("受益人请求报文----------------" + jsonStr2);
        String httpIntefaceUtils = HttpIntefaceUtils.send(esbUrl, jsonStr2);
        //System.out.println("受益人---------"+jsonObject.toJSONString(getRequestBody("P00001011928", "ActBeneInfoQuery", "ydrx_finalBeneFiciary", keyword)));
        String ResponseCustom = esbService.getResponseCustom(httpIntefaceUtils);
        log.info("受益人提取后的报文---------------- " + ResponseCustom);
        if (ResponseCustom.contains("bizMsg")) {
            return null;
        } else {
            OutBeneDto outBeneDto = JSON.parseObject(ResponseCustom, OutBeneDto.class);
            OutBeneficiaryDto outBeneficiaryDto = new OutBeneficiaryDto();
            BeanCopierUtils.copyProperties(outBeneDto, outBeneficiaryDto);
            List<FinalBeneficiary> listfinalBeneficiaries = outBeneDto.getFinalBeneficiary();
            ArrayList<BeneficiaryDto> listBeneficiary = new ArrayList<>();
            if (listfinalBeneficiaries != null && listfinalBeneficiaries.size() > 0) {
                for (FinalBeneficiary listfinalBeneficiary : listfinalBeneficiaries) {
                    BeneficiaryDto beneficiaryDto = new BeneficiaryDto();
                    beneficiaryDto.setName(listfinalBeneficiary.getName());
                    beneficiaryDto.setType(listfinalBeneficiary.getType());
                    beneficiaryDto.setIdentifytype(listfinalBeneficiary.getIdentifyType());
                    beneficiaryDto.setIdentifyno(listfinalBeneficiary.getIdentifyNo());
                    beneficiaryDto.setCapital(listfinalBeneficiary.getCapital());
                    beneficiaryDto.setCapitalpercent(listfinalBeneficiary.getCapitalPercent());
                    beneficiaryDto.setCapitalchain(listfinalBeneficiary.getCapitalChain());
                    listBeneficiary.add(beneficiaryDto);
                }
            }
            outBeneficiaryDto.setFinalBeneficiary(listBeneficiary);
            log.info("整合到产品中的受益人类：" + outBeneficiaryDto);
            return outBeneficiaryDto;
        }

    }

    /**
     * 股权穿透，递归方法
     *
     * @param listEquityShare
     * @param childrenes
     * @param outEquityDto
     * @return
     */
    private ArrayList<EquityShareDto> getChidren(ArrayList<EquityShareDto> listEquityShare, List<Children1> childrenes, OutEquityDto outEquityDto) {
        for (Children1 childrene : childrenes) {
            log.info("childrene--------------------------------------------" + childrene.toString());
            EquityShareDto equityShareDto = new EquityShareDto();
            equityShareDto.setName(childrene.getName());
            equityShareDto.setCapital(childrene.getCapital());
            equityShareDto.setPercent(childrene.getPercent());
            equityShareDto.setLayer(childrene.getLayer());
            equityShareDto.setParent(childrene.getParent());
            listEquityShare.add(equityShareDto);
            if (childrene.getItem() != null && childrenes.size() > 0) {
                getChidren(listEquityShare, childrene.getItem(), outEquityDto);
            }
        }
        return listEquityShare;
    }

    public RequestBody getRequestBody(String serviceCd, String operation, String tranCode, String keyword) {
        RequestBody requestBody = new RequestBody();
        requestBody.setTransaction(getTransaction(serviceCd, operation, tranCode, keyword));
        return requestBody;
    }

    public Transaction getTransaction(String serviceCd, String operation, String tranCode, String keyword) {
        Transaction Transaction = new Transaction();
        Transaction.setBody(getBody(tranCode, keyword));
        Transaction.setHeader(getHeader(serviceCd, operation, keyword));
        return Transaction;
    }

    public Header getHeader(String serviceCd, String operation, String keyword) {
        Header Header = new Header();
        Header.setSysHeader(getSysHead(serviceCd, operation));
        return Header;
    }

    public Body getBody(String tranCode, String keyword) {
        Body Body = new Body();
        Body.setRequest(getRequest(tranCode, keyword));
        return Body;
    }

    public Request getRequest(String tranCode, String keyword) {
        Request request = new Request();
        request.setBizBody(getBizBody(keyword));
        request.setBizHeader(getBizHeader(tranCode, keyword));
        return request;
    }

    public BizHeader getBizHeader(String tranCode, String keyword) {
        BizHeader bizHead = new BizHeader();
        bizHead.setInterfaceId(tranCode);//发起系统标示
        bizHead.setUserName("dgzh");
        bizHead.setUserKey("99f2e92ca7e14404b179af417b27aba6");
        bizHead.setVer("1");
        return bizHead;
    }

    public BizBody getBizBody(String keyword) {
        BizBody bizBody = new BizBody();
        bizBody.setKeyWord(keyword);
        return bizBody;
    }

    public SysHeader getSysHead(String serviceCd, String operation) {
        String clientCd = "339";
        String serverCd = "261";
        SysHeader sysHeader = new SysHeader();
        sysHeader.setMsgId("0" + clientCd + getNowDateShort("yyyyMMdd") + getNowDateShort("HHmmssSSS") + (int) ((Math.random() * 9 + 1) * 1000));
        sysHeader.setServiceCd(serviceCd);
        sysHeader.setOperation(operation);
        sysHeader.setClientCd(clientCd);
        sysHeader.setServerCd(serverCd);
        sysHeader.setOrgCode("");
        sysHeader.setMsgDate("");
        sysHeader.setMsgTime("");
        return sysHeader;
    }


    public String getSaicInfoRealTimeJson(String keyword) {
        return null;
    }


    public String getSaicInfoRealTimeUrl() {
        return this.realTimeEncryptFlag ? this.realTimeEncryptUrl : this.realTimeUrl;
    }

    public SaicIdpInfo getSaicInfoExact(String keyword) {
        return null;
    }

    @Override
    public SaicIdpInfo getSaicInfoExact(String keyword, String usernmae) {
        return null;
    }

    @Override
    public SaicIdpInfo getSaicInfoExact(String keyword, String usernmae, SearchType type) {
        return null;
    }

    public String getOutBaseAccountDtoUrl() {
        return this.baseAccountUrl;
    }

    public List<HolidayDto> getHolidayDto(int year) {
        return null;
    }

    public String getSaicInfoExactUrl() {
        return this.exactUrl;
    }

    public String getOutEquityShareDtoUrl() {
        return this.equityShareListUrl;
    }

    public String getOutBeneficiaryDtoUrl() {
        return null;
    }

    public OutIllegalQueryDto getOutIllegalQueryDto(String keyWord) {
        return null;
    }

    public String getOutIllegalQueryDtoUrl() {
        return this.illegalQueryUrl;
    }

    public String getCarrierResponseStr(String encryptParam) {
        return null;
    }

    @Override
    public String getJudicialInformation(String encryptParam, int pageIndex, int pageSize) {
        return null;
    }

    public OutFreshCompanyQueryDto getFreshCompanyList(String provinceCode, String startDate, String endDate, Integer pageIndex, Integer pageSize) {
        return null;
    }

    @Override
    public EntrustResultDto entrustUpdate(String keyword) throws Exception {
        return null;
    }

    @Override
    public EntrustResultDto getEntrustUpdateResult(String keyword) throws Exception {
        return null;
    }

}
