package com.ideatech.ams.controller.risk;

import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.risk.enums.HandleMode;
import com.ideatech.ams.risk.procedure.service.ProcService;
import com.ideatech.ams.risk.riskdata.RiskRegisterInfoDto;
import com.ideatech.ams.risk.riskdata.dto.RiskDataSearchDto;
import com.ideatech.ams.risk.riskdata.dto.RiskHandleInfoDto;
import com.ideatech.ams.risk.riskdata.entity.RiskHandleInfo;
import com.ideatech.ams.risk.riskdata.service.RiskApiService;
import com.ideatech.ams.risk.riskdata.service.RiskDataService;
import com.ideatech.ams.risk.riskdata.service.RiskHandleInfoService;
import com.ideatech.ams.risk.riskdata.service.RiskRegisterInfoService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

/**
 * 风险处理登记簿查询
 */
@RestController
@RequestMapping("riskHandleInfo")
public class RiskHandleInfoController {
    private static final Logger log = LoggerFactory.getLogger(RiskHandleInfoController.class);
    @Autowired
    RiskHandleInfoService riskHandleInfoService;
    @Autowired
    RiskRegisterInfoService riskRegisterInfoService;
    @Autowired
    RiskApiService riskApiService;
    @Autowired
    RiskDataService riskDataService;

    @Autowired
    ProcService procService;
    @Autowired
    AccountsAllDao accountsAllDao;


    /**
     * 待处理风险数据查询
     *
     * @param riskDataSearchDto
     * @return
     * @author yangcq
     * @date 20190626
     * @address wulmq
     */
    @GetMapping("querytodoRiskInfo")
    public ResultDto querytodoRiskInfo(RiskDataSearchDto riskDataSearchDto) {
        return ResultDtoFactory.toAckData(riskHandleInfoService.querytodoRiskInfo(riskDataSearchDto));
    }

    /**
     * 处理结果登记簿查询
     *
     * @param riskDataSearchDto
     * @return
     * @author yangcq
     * @date 20190626
     * @address wulmq
     */
    @GetMapping("queryHandleRiskInfo")
    public ResultDto queryHandleRiskInfo(RiskDataSearchDto riskDataSearchDto) {
        return ResultDtoFactory.toAckData(riskRegisterInfoService.queryRegisterRiskInfo(riskDataSearchDto));
    }

    /**
     * 统计当前用户风险数据处理登记簿数量
     *
     * @param riskDataSearchDto
     * @return
     * @author yangcq
     * @date 20190626
     * @address wulmq
     */
    @GetMapping("countHandle")
    public ResultDto countHandle(RiskDataSearchDto riskDataSearchDto) {
        return ResultDtoFactory.toAckData(riskRegisterInfoService.countRegisterRiskInfo(riskDataSearchDto));
    }

    /**
     * 处理风险数据 分为止付、不处理和暂停非柜面
     *
     * @return dubiousReason: 可疑原因，accountNo：账号，handleType：处理方式，
     * @author yangcq
     * @date 20190622
     * @address wulmq
     */
    @GetMapping("/handleRiskInfo/{id}/{handleType}/{accountNo}/{dubiousReason}/{counterEmp}")
    public ResultDto handleRiskInfo(@PathVariable("id") String id, @PathVariable("handleType") String handleType,
                                    @PathVariable("accountNo") String accountNo, @PathVariable("dubiousReason") String dubiousReason,
                                    @PathVariable("counterEmp") String counterEmp) {
        String msg = "";
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
        RiskHandleInfoDto riskHandleInfoDto = riskHandleInfoService.findByRId(id);
        RiskRegisterInfoDto riskRegisterInfoDto = new RiskRegisterInfoDto();
        String returnCode = "";
        //根据账户查询机构码
        //String bankCode = riskRegisterInfoService.getBankCodeByAccount(accountNo);
        //通过法人机构号，获取法人的code
        //String corporateBank = procService.findBankCodeByCode(bankCode);
        String dubiousReasonVal = "";
        String accountManagement = "";
        try {
            if (StringUtils.isNotBlank(accountNo) && riskHandleInfoDto != null) {
                riskRegisterInfoDto.setHandleDate(new Date());//处理时间
                dubiousReasonVal = URLDecoder.decode(dubiousReason, "GBK");//获取可疑原因
                if ("2".equals(handleType)) {//止付
                    riskRegisterInfoDto.setStatus(HandleMode.ZF.getFullName());//代表已止付
                    riskRegisterInfoDto.setHandleMode(HandleMode.ZF.getFullName());//处理方式：止付
                    //乌鲁木齐接口
                    // stopPayService.stopPay ( accountNo, StopPayService.RiskStatus.TRUE );//1:止付，0：解付，该接口只做止付
                    //神州数码融信云接口
                    //returnCode  = riskManagementService.RiskManagement ( accountNo,"2",bankCode,"01",counterEmp);
                    accountManagement = "1";
                    returnCode = "000000";
                } else if ("1".equals(handleType)) {//不处理
                    riskRegisterInfoDto.setHandleMode(HandleMode.BCL.getFullName());//处理方式:不处理
                    riskRegisterInfoDto.setStatus(HandleMode.BCL.getFullName());//代表已不处理
                    accountManagement = "5";
                    returnCode = "000000";
                } else {//暂停非柜面
                    riskRegisterInfoDto.setStatus(HandleMode.ZTFGM.getFullName());//已暂停非柜面
                    riskRegisterInfoDto.setHandleMode(HandleMode.ZTFGM.getFullName());//处理方式:暂停非柜面
                    /**乌鲁木齐接口
                     AccountsAllInfo info =  accountsAllService.findByAcctNo (accountNo);
                     String accountNameGbk = new String (info.getAcctName ().getBytes ( "GBK" ));
                     suspensionNonCounterService.suspensionNonCounter(accountNo,dubiousReasonVal,flag,accountNameGbk);*/
                    //神州数码融信云接口
                    //returnCode  = riskManagementService.RiskManagement ( accountNo,"1",bankCode,"01",counterEmp);
                    returnCode = "000000";
                    accountManagement = "2";
                }
            }
            if (StringUtils.equalsIgnoreCase("000000", returnCode)) {
                //查询该账号下所有未出来的风险数据，0：代表未处理
                List<RiskHandleInfo> handleInfoDtolist = riskHandleInfoService.findIdByAccountNoAndStatus(accountNo, "0");
                for (RiskHandleInfo riskHandrleInfo : handleInfoDtolist) {
                    //保存处理信息到登记簿
                    RiskRegisterInfoDto riskRegisterInfoDto1 = new RiskRegisterInfoDto();
                    riskRegisterInfoDto1.setRiskDesc(riskHandrleInfo.getRiskDesc());
                    riskRegisterInfoDto1.setAccountNo(accountNo);
                    riskRegisterInfoDto1.setHandleDate(new Date());
                    riskRegisterInfoDto1.setRiskDate(riskHandrleInfo.getRiskDate());
                    riskRegisterInfoDto1.setRiskId(riskHandrleInfo.getRiskId());//风险编号
                    riskRegisterInfoDto1.setRiskPoint(riskHandrleInfo.getRiskPoint());//风险点
                    riskRegisterInfoDto1.setStatus(riskRegisterInfoDto.getStatus());
                    riskRegisterInfoDto1.setDubiousReason(dubiousReasonVal);
                    riskRegisterInfoDto1.setHandler(String.valueOf(user.getId()));
                    riskRegisterInfoDto1.setHandleMode(riskRegisterInfoDto.getHandleMode());
                    //riskRegisterInfoDto1.setCorporateBank ( corporateBank );
                    //riskRegisterInfoDto1
                    riskRegisterInfoService.saveRegisterInfo(riskRegisterInfoDto1);
                    RiskHandleInfoDto dto = new RiskHandleInfoDto();
                    dto.setStatus("1");//代表已处理
                    dto.setId(riskHandrleInfo.getId());
                    dto.setRiskId(riskHandrleInfo.getRiskId());
                    dto.setAccountNo(accountNo);
                    dto.setRiskPoint(riskHandrleInfo.getRiskPoint());
                    dto.setRiskDesc(riskHandrleInfo.getRiskDesc());
                    //更新待处理数据的状态
                    riskHandleInfoService.saveHandleInfo(dto);

                }
                //将账户主表的账号处理状态更新
                AccountsAll byAcctNo = accountsAllDao.findByAcctNo(accountNo);
                AccountsAll accountsAll = accountsAllDao.findOne(byAcctNo.getId());
                //accountsAll.setCorporateBank ( corporateBank );
//                accountsAll.setAccountManagement ( accountManagement );
                accountsAllDao.save(accountsAll);
                msg = "风险数据处理成功！";
            } else {
                msg = "风险数据处理失败！";
                log.error("*********************************交易接口出现问题*********************************");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            msg = "风险数据处理失败！";
        }
        return ResultDtoFactory.toAck(msg);
    }


}