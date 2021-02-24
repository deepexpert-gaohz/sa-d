package com.ideatech.ams.controller.customersearch;

import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.customer.dto.CustomerTuneSearchHistoryDto;
import com.ideatech.ams.customer.enums.CustomerTuneSearchEntranceType;
import com.ideatech.ams.customer.enums.CustomerTuneSearchType;
import com.ideatech.ams.customer.service.CustomerTuneSearchHistoryService;
import com.ideatech.ams.kyc.dto.idcard.IdCheckLogDto;
import com.ideatech.ams.kyc.service.CarrierOperatorService;
import com.ideatech.ams.kyc.service.idcard.IdCardComperService;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.EccsAccountInfo;
import com.ideatech.ams.pbc.dto.EccsSearchCondition;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.ws.api.service.EccsApiService;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 客户综合尽调
 */
@RequestMapping(value = "/customerSearch")
@RestController
@Slf4j
public class CustomerSearchController {

    @Autowired
    private PbcAmsService pbcAmsService;

    @Autowired
    private EccsApiService eccsApiService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CustomerTuneSearchHistoryService customerTuneSearchHistoryService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private CarrierOperatorService carrierOperatorService;

    @Autowired
    private IdCardComperService idCardComperService;

    /**
     * 账号查人行信息
     * @param acctNo
     * @return
     * @throws Exception
     */
    @GetMapping("/getPbcInfoByAcctNo")
    public ObjectRestResponse<AmsAccountInfo> getPbcInfoByAcctNo(String acctNo) throws Exception {
        AmsAccountInfo aai;
        try {
            aai = pbcAmsService.getAmsAccountInfoByAcctNo(SecurityUtils.getCurrentUser().getOrgId(), acctNo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjectRestResponse<AmsAccountInfo>().rel(false).msg(e.getMessage());
        }
        return new ObjectRestResponse<AmsAccountInfo>().rel(true).result(aai);
    }

    /**
     * 机构信用代码信息查询
     * @param condition  机构信用代码查询条件
     * @return
     * @throws Exception
     */
    @PostMapping("/getEccsAccountInfoByCondition")
    public ObjectRestResponse<EccsAccountInfo> getEccsAccountInfoByCondition(EccsSearchCondition condition) throws Exception {
        OrganizationDto organizationDto = organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId());

        return eccsApiService.getEccsAccountInfoByCondition(organizationDto.getCode(), condition);
    }

    /**
     * 记录基本户唯一性验证日志
     */
    @RequestMapping(value = "/jibenUniqueValidateLog", method = RequestMethod.POST)
    public void jibenUniqueValidateLog(CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto) {
        //记录日志
        try {
            //获取人行用户名
            PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganId(Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgId(), EAccountType.AMS);
            if (pbcAccountDto != null) {
                customerTuneSearchHistoryDto.setPbcAccount(pbcAccountDto.getAccount());
            }
            customerTuneSearchHistoryDto.setType(CustomerTuneSearchType.VERIFY_UNIQUE_JIBEN);
            customerTuneSearchHistoryDto.setEntranceType(CustomerTuneSearchEntranceType.ENTRANCE_VERIFY_UNIQUE_JIBEN);
            customerTuneSearchHistoryDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
            customerTuneSearchHistoryDto.setOrganId(Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgId());

            customerTuneSearchHistoryService.save(customerTuneSearchHistoryDto);
        } catch (Exception e) {
            log.error("记录基本户唯一性验证日志失败", e.getMessage());
        }
    }

    /**
     * 记录账号查人行信息查询日志
     *
     * @param acctNo        账号
     * @param depositorName 存款人名称
     * @param result      结果
     */
    @RequestMapping(value = "/queryPbcResultByAcctNoLog", method = RequestMethod.POST)
    public void queryPbcResultByAcctNoLog(String acctNo, String depositorName, String result) {
        //记录日志
        try {
            CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto = new CustomerTuneSearchHistoryDto();
            //获取人行用户名
            PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganId(Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgId(), EAccountType.AMS);
            if (pbcAccountDto != null) {
                customerTuneSearchHistoryDto.setPbcAccount(pbcAccountDto.getAccount());
            }
            customerTuneSearchHistoryDto.setAcctNo(acctNo);
            customerTuneSearchHistoryDto.setDepositorName(depositorName);
            customerTuneSearchHistoryDto.setResult(result);

            customerTuneSearchHistoryDto.setType(CustomerTuneSearchType.PBCRESULT_BY_ACCTNO);
            customerTuneSearchHistoryDto.setEntranceType(CustomerTuneSearchEntranceType.ENTRANCE_PBCRESULT_BY_ACCTNO);
            customerTuneSearchHistoryDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
            customerTuneSearchHistoryDto.setOrganId(Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgId());

            customerTuneSearchHistoryService.save(customerTuneSearchHistoryDto);
        } catch (Exception e) {
            log.error("记录账号查人行信息查询日志失败", e.getMessage());
        }
    }

    /**
     * 记录开户许可证查人行信息查询日志
     *
     * @param acctNo        账号
     * @param depositorName 存款人名称
     * @param result      结果
     */
    @RequestMapping(value = "/queryPbcResultByAccountKeyLog", method = RequestMethod.POST)
    public void queryPbcResultByAccountKeyLog(String acctNo, String depositorName, String result) {
        //记录日志
        try {
            CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto = new CustomerTuneSearchHistoryDto();
            //获取机构数据
            OrganizationDto organizationDto = organizationService.findById(Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgId());
            if (organizationDto != null) {
                //获取人行用户名
                PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(organizationDto.getCode(), EAccountType.AMS);
                if (pbcAccountDto != null) {
                    customerTuneSearchHistoryDto.setPbcAccount(pbcAccountDto.getAccount());
                }
            }
            customerTuneSearchHistoryDto.setAcctNo(acctNo);
            customerTuneSearchHistoryDto.setDepositorName(depositorName);
            customerTuneSearchHistoryDto.setResult(result);

            customerTuneSearchHistoryDto.setType(CustomerTuneSearchType.PBCRESULT_BY_ACCOUNTKEY);
            customerTuneSearchHistoryDto.setEntranceType(CustomerTuneSearchEntranceType.ENTRANCE_PBCRESULT_BY_ACCOUNTKEY);
            customerTuneSearchHistoryDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
            customerTuneSearchHistoryDto.setOrganId(Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgId());

            customerTuneSearchHistoryService.save(customerTuneSearchHistoryDto);
        } catch (Exception e) {
            log.error("记录开户许可证查人行信息查询日志失败", e.getMessage());
        }
    }

    /**
     * 记录机构信用代码查人行信息查询日志
     *
     * @param orgCode    组织机构代码
     * @param accountKey 开户许可证核准号
     * @param result     结果
     */
    @RequestMapping(value = "/queryEccsResultLog", method = RequestMethod.POST)
    public void queryEccsResultLog(String orgCode, String accountKey, String result) {
        //记录日志
        try {
            CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto = new CustomerTuneSearchHistoryDto();
            //获取人行用户名
            PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullId(SecurityUtils.getCurrentOrgFullId(), EAccountType.ECCS);
            if (pbcAccountDto != null) {
                customerTuneSearchHistoryDto.setPbcAccount(pbcAccountDto.getAccount());
            }
            customerTuneSearchHistoryDto.setOrgCode(orgCode);
            customerTuneSearchHistoryDto.setAccountKey(accountKey);
            customerTuneSearchHistoryDto.setResult(result);

            customerTuneSearchHistoryDto.setType(CustomerTuneSearchType.ECCSRESULT);
            customerTuneSearchHistoryDto.setEntranceType(CustomerTuneSearchEntranceType.ENTRANCE_ECCSRESULT);
            customerTuneSearchHistoryDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
            customerTuneSearchHistoryDto.setOrganId(Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgId());

            customerTuneSearchHistoryService.save(customerTuneSearchHistoryDto);
        } catch (Exception e) {
            log.error("记录查询代码证系统信息查询日志失败", e.getMessage());
        }
    }

    /**
     * 记录工商基本信息查询日志
     */
    @RequestMapping(value = "/querySaicInfoByNameLog", method = RequestMethod.POST)
    public void querySaicInfoByNameLog(String customerName, String result, Long refId) {
        //记录日志
        try {
            CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto = new CustomerTuneSearchHistoryDto();
            customerTuneSearchHistoryDto.setCustomerName(customerName);
            customerTuneSearchHistoryDto.setRefId(refId);
            customerTuneSearchHistoryDto.setResult(result);

            customerTuneSearchHistoryDto.setType(CustomerTuneSearchType.SAIC_BY_NAME);
            customerTuneSearchHistoryDto.setEntranceType(CustomerTuneSearchEntranceType.ENTRANCE_SAIC_BY_NAME);
            customerTuneSearchHistoryDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
            customerTuneSearchHistoryDto.setOrganId(Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgId());

            customerTuneSearchHistoryService.save(customerTuneSearchHistoryDto);
        } catch (Exception e) {
            log.error("记录工商基本信息查询日志失败", e.getMessage());
        }
    }

    /**
     * 记录运营商校验日志
     */
    @RequestMapping(value = "/queryCarrierOperatorResultLog", method = RequestMethod.POST)
//    public void getCarrierOperatorResult(CarrierOperatorDto carrierOperatorDto) {
    public void queryCarrierOperatorResultLog(String name, String mobile, String cardNo, String result, String idCardedType) {
//        //运营商校验
//        CarrierOperatorDto dto = carrierOperatorService.getCarrierOperatorResult(carrierOperatorDto);

        //记录日志
        try {
            CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto = new CustomerTuneSearchHistoryDto();
            customerTuneSearchHistoryDto.setName(name);
            customerTuneSearchHistoryDto.setMobile(mobile);
            customerTuneSearchHistoryDto.setCardNo(cardNo);
            customerTuneSearchHistoryDto.setResult(result);
            customerTuneSearchHistoryDto.setIdCardedType(idCardedType);

            customerTuneSearchHistoryDto.setType(CustomerTuneSearchType.VERIFY_CARRIER_OPERATOR);
            customerTuneSearchHistoryDto.setEntranceType(CustomerTuneSearchEntranceType.ENTRANCE_VERIFY_CARRIER_OPERATOR);
            customerTuneSearchHistoryDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
            customerTuneSearchHistoryDto.setOrganId(Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgId());

            customerTuneSearchHistoryService.save(customerTuneSearchHistoryDto);
        } catch (Exception e) {
            log.error("记录运营商校验日志失败", e.getMessage());
        }
    }
    /**
     * 记录联网身份核查日志
     */
    @RequestMapping(value = "/queryIdCardLog", method = RequestMethod.POST)
    public void queryIdCardLog(String name, String cardNo, String result) {
        //记录日志
        try {
            CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto = new CustomerTuneSearchHistoryDto();

            //获取人行用户名
            List<PbcAccountDto> pbcAccountDtos = pbcAccountService.listByOrgIdAndType(Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgId(), EAccountType.PICP);
            if (pbcAccountDtos != null && pbcAccountDtos.size() > 0) {
                customerTuneSearchHistoryDto.setPbcAccount(pbcAccountDtos.get(0).getAccount());
            }

            customerTuneSearchHistoryDto.setName(name);
            customerTuneSearchHistoryDto.setCardNo(cardNo);
            customerTuneSearchHistoryDto.setResult(result);

            customerTuneSearchHistoryDto.setType(CustomerTuneSearchType.VERIFY_ID_CARD);
            customerTuneSearchHistoryDto.setEntranceType(CustomerTuneSearchEntranceType.ENTRANCE_VERIFY_ID_CARD);
            customerTuneSearchHistoryDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
            customerTuneSearchHistoryDto.setOrganId(Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgId());

            customerTuneSearchHistoryService.save(customerTuneSearchHistoryDto);
        } catch (Exception e) {
            log.error("记录联网身份核查日志失败", e.getMessage());
        }
    }

    /**
     * 记录联网身份核查日志
     */
    @RequestMapping(value = "/queryIdCardLogBatch", method = RequestMethod.POST)
    public void queryIdCardLogBatch() {
        //记录日志
        try {
            List<IdCheckLogDto> list = idCardComperService.findAll();
            for (IdCheckLogDto dto:list) {
                CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto = new CustomerTuneSearchHistoryDto();
                //获取人行用户名
                List<PbcAccountDto> pbcAccountDtos = pbcAccountService.listByOrgIdAndType(Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgId(), EAccountType.PICP);
                if (pbcAccountDtos != null && pbcAccountDtos.size() > 0) {
                    customerTuneSearchHistoryDto.setPbcAccount(pbcAccountDtos.get(0).getAccount());
                }
                customerTuneSearchHistoryDto.setName(dto.getIdCardName());
                customerTuneSearchHistoryDto.setCardNo(dto.getIdCardNo());
                customerTuneSearchHistoryDto.setResult(dto.getCheckResult());

                customerTuneSearchHistoryDto.setType(CustomerTuneSearchType.VERIFY_ID_CARD);
                customerTuneSearchHistoryDto.setEntranceType(CustomerTuneSearchEntranceType.ENTRANCE_VERIFY_ID_CARD);
                customerTuneSearchHistoryDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                customerTuneSearchHistoryDto.setOrganId(Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgId());

                customerTuneSearchHistoryService.save(customerTuneSearchHistoryDto);
                idCardComperService.delete(dto.getIdCardName(),dto.getIdCardNo());
            }
        } catch (Exception e) {
            log.error("记录联网身份核查日志失败", e.getMessage());
        }
    }

    /**
     * 日志查询列表
     *
     * @param dto      查询参数
     * @param pageable 分页参数
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableResultResponse<CustomerTuneSearchHistoryDto> pro(CustomerTuneSearchHistoryDto dto, Pageable pageable) {
        TableResultResponse<CustomerTuneSearchHistoryDto> response = customerTuneSearchHistoryService.query(dto, pageable);
        return response;
    }

    /**
     * 记录司法信息查询日志
     */
    @RequestMapping(value = "/queryJudicialInformationLog", method = RequestMethod.POST)
    public void queryJudicialInformationLog(String companyName) {
        //记录日志
        try {
            CustomerTuneSearchHistoryDto customerTuneSearchHistoryDto = new CustomerTuneSearchHistoryDto();
            customerTuneSearchHistoryDto.setCustomerName(companyName);
//            customerTuneSearchHistoryDto.setRefId(refId);

            customerTuneSearchHistoryDto.setType(CustomerTuneSearchType.JUDICIAL_INFORMATION);
            customerTuneSearchHistoryDto.setEntranceType(CustomerTuneSearchEntranceType.ENTRANCE_JUDICIAL_INFORMATION);
            customerTuneSearchHistoryDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
            customerTuneSearchHistoryDto.setOrganId(Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgId());

            customerTuneSearchHistoryService.save(customerTuneSearchHistoryDto);
        } catch (Exception e) {
            log.error("记录工商基本信息查询日志失败", e.getMessage());
        }
    }
}
