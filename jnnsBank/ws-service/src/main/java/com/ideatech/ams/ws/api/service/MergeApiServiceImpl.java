package com.ideatech.ams.ws.api.service;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.kyc.util.SaicUtils;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsCheckResultInfo;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.ws.dto.MergeApiDto;
import com.ideatech.ams.ws.enums.OpenAccountCode;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.enums.SaicStatusEnum;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class MergeApiServiceImpl implements MergeApiService {

    @Autowired
    private PbcAmsService pbcAmsService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private SaicInfoService saicInfoService;

    @Override
    public ResultDto querySaicAndPbc(String saicParam, String accountKey, String organCode) {
        return querySaicAndPbc(saicParam, accountKey, organCode, (accountKey != null && accountKey.length() >= 7) ? accountKey.substring(1, 7) : "");
    }

    @Override
    public ResultDto querySaicAndPbc(String saicParam, String accountKey, String organCode, String regAreaCode) {
        if (StringUtils.isBlank(saicParam) && StringUtils.isBlank(accountKey)) {
            return toResult(ResultCode.PARAM_IS_BLANK, null);
        }

        AmsAccountInfo amsAccountInfo = null;
        SaicIdpInfo saicIdpInfo = null;
        JSONObject jsonObject = new JSONObject();
        ResultCode pbcResultCode = null;
        OpenAccountCode openAccountCode = null;
        String openAccountErrInfo = "";
        boolean match = true;
        //查询人行
        if (StringUtils.isNotBlank(accountKey)) {
            try {
                PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(organCode, EAccountType.AMS);
                if (pbcAccountDto != null) {
                    AmsCheckResultInfo amsCheckResultInfo = pbcAmsService.checkPbcByAccountKeyAndRegAreaCode(organCode, accountKey, regAreaCode);
                    if (amsCheckResultInfo != null) {
                        if (amsCheckResultInfo.isCheckPass()) {
                            amsAccountInfo = amsCheckResultInfo.getAmsAccountInfo();
                            pbcResultCode = ResultCode.SUCCESS;
                        } else {
                            if (StringUtils.contains(amsCheckResultInfo.getNotPassMessage(), "存款人有其他久悬")) {
                                pbcResultCode = ResultCode.PBC_BASICACCT_INCLUDE_SUSPEND;
                            }
                        }
                    }
                } else {
                    pbcResultCode = ResultCode.PBC_USER_NOT_EXIST;
                }
            } catch (Exception e) {
                if (e instanceof BizServiceException) {
                    pbcResultCode = ResultCode.getResultCodeByErrorCode(((BizServiceException) e).getError());
                } else if (e instanceof SyncException) {
                    if (StringUtils.contains(((SyncException) e).getMessage(), "开户许可证长度不正确")) {
                        pbcResultCode = ResultCode.PARAM_IS_INVALID;
                    }
                } else if (e instanceof ConnectException) {
                    pbcResultCode = ResultCode.NETWORK_ERROR;
                } else {
                    pbcResultCode = ResultCode.UNKNOWN_SYSTEM_ERROR;
                }
                log.error("合并接口人行数据查询异常", e);
            }
        }

        ResultCode saicResultCode = null;
        SaicStatusEnum saicStatusEnum = null;
        //查询工商
        if (StringUtils.isNotBlank(saicParam)) {
            try {
                saicIdpInfo = saicInfoService.getSaicInfoFull(SearchType.REAL_TIME, "", saicParam, null);
                if (saicIdpInfo != null) {
                    saicResultCode = ResultCode.SUCCESS;
                    //营业执照到期
                    if (BooleanUtils.isTrue(SaicUtils.isBusinessLicenseExpired(saicIdpInfo.getEnddate()))) {
                        saicResultCode = ResultCode.SAIC_BUSINESS_LICENSE_EXPIRED;
                    }
                    //根据工商字段内容判断其他反馈内容
                    saicStatusEnum = SaicStatusEnum.saicState2Enum(saicIdpInfo.getState());
                    if (saicStatusEnum == SaicStatusEnum.REVOKE) {
                        saicResultCode = ResultCode.SAIC_STATUS_REVOKE;
                    } else if (saicStatusEnum == SaicStatusEnum.CANCEL) {
                        saicResultCode = ResultCode.SAIC_STATUS_CANCEL;
                    } else if (saicStatusEnum == SaicStatusEnum.QUIT) {
                        saicResultCode = ResultCode.SAIC_STATUS_QUIT;
                    }
                    //经营异常，优先级次之
                    if (BooleanUtils.isTrue(SaicUtils.isAbnormalOperation(saicIdpInfo.getChangemess()))) {
                        saicResultCode = ResultCode.SAIC_INCLUDE_ABNORMAL_OPERATION;
                    }
                    //严重违法，优先级最高
                    if (BooleanUtils.isTrue(SaicUtils.isIllegal(saicIdpInfo.getIllegals()))) {
                        saicResultCode = ResultCode.SAIC_INCLUDE_ILLEGAL;
                    }
                } else {
                    saicResultCode = ResultCode.SAIC_QUERY_ERROR;
                }
            } catch (Exception e) {
                if (e instanceof ConnectException) {
                    saicResultCode = ResultCode.NETWORK_ERROR;
                } else {
                    saicResultCode = ResultCode.UNKNOWN_SYSTEM_ERROR;
                }
                log.error("合并接口工商数据查询异常", e);
            }
        }

        if (amsAccountInfo == null && pbcResultCode == null) {
            pbcResultCode = ResultCode.PBC_NOT_FOUND;
        }
        if (saicIdpInfo == null && saicResultCode == null) {
            saicResultCode = ResultCode.SAIC_NOT_FOUND;
        }

        openAccountErrInfo = pbcResultCode.message();

        jsonObject.put("pbcContent", amsAccountInfo);
        jsonObject.put("pbcCheckResult", pbcResultCode);

        jsonObject.put("saicContent", saicIdpInfo);
        jsonObject.put("saicCheckResult", saicResultCode);

        //返回结果信息
        ResultCode finalResultCode = null;
        //1.都没有结果
        if (amsAccountInfo == null && saicIdpInfo == null) {
            finalResultCode = ResultCode.NO_DATA_EXIST;
            openAccountErrInfo = "工商查询异常且人行查询异常";
        } else if (amsAccountInfo != null && saicIdpInfo != null) {
            //2.都有,需要比对
            //比对字段[企业名称、注册号、法人名称]
            JSONObject compareResult = new JSONObject();


            Map<String, Object> fieldMap = new HashMap<>(16);
            fieldMap.put("pbc", amsAccountInfo.getDepositorName());
            fieldMap.put("saic", saicIdpInfo.getName());
            if (!StringUtils.equals(amsAccountInfo.getDepositorName(), saicIdpInfo.getName())) {
                match = false;
            }
            fieldMap.put("match", StringUtils.equals(amsAccountInfo.getDepositorName(), saicIdpInfo.getName()));
            compareResult.put("depositorName", fieldMap);

            fieldMap = new HashMap<>(16);
            fieldMap.put("pbc", amsAccountInfo.getFileNo());
            String saicRegNo = StringUtils.isBlank(saicIdpInfo.getUnitycreditcode()) ? saicIdpInfo.getRegistno() : saicIdpInfo.getUnitycreditcode();
            fieldMap.put("saic", saicRegNo);
            if (!StringUtils.equals(amsAccountInfo.getFileNo(), saicRegNo)) {
                match = false;
            }
            fieldMap.put("match", StringUtils.equals(amsAccountInfo.getFileNo(), saicRegNo));
            compareResult.put("regNo", fieldMap);

            fieldMap = new HashMap<>(16);
            fieldMap.put("pbc", amsAccountInfo.getLegalName());
            fieldMap.put("saic", saicIdpInfo.getLegalperson());
            if (!StringUtils.equals(amsAccountInfo.getLegalName(), saicIdpInfo.getLegalperson())) {
                match = false;
            }
            fieldMap.put("match", StringUtils.equals(amsAccountInfo.getLegalName(), saicIdpInfo.getLegalperson()));
            compareResult.put("legalName", fieldMap);

            compareResult.put("match", match);

            jsonObject.put("compareResult", compareResult);

            //合并数据
            MergeApiDto mergeApiDto = makeMergeData(amsAccountInfo, saicIdpInfo);
            jsonObject.put("mergeData", mergeApiDto);

            finalResultCode = ResultCode.SUCCESS;
        } else {
            //3.仅人行/仅工商
            finalResultCode = ResultCode.SUCCESS;
        }

        //可以开户
        if (match && pbcResultCode == ResultCode.SUCCESS && saicStatusEnum == SaicStatusEnum.REGISTERED) {
            openAccountCode = OpenAccountCode.ALLOWED;
        }

        if (!match) {
            openAccountCode = OpenAccountCode.NOT_ALLOWED;
            openAccountErrInfo = "比对不一致";
        }

        if (resultIsOk(pbcResultCode) || resultIsOk(saicResultCode)) {
            openAccountCode = OpenAccountCode.ALLOWED_BUT_PROMPT;
            openAccountErrInfo = pbcResultCode != null ? pbcResultCode.message() : saicResultCode.message();
        }

        jsonObject.put("openAccountCode",openAccountCode);
        jsonObject.put("openAccountErrInfo",openAccountErrInfo);
        return toResult(finalResultCode, jsonObject);

    }


    public <T> ResultDto<T> toResult(ResultCode resultCode, T data) {
        ResultDto<T> dto = new ResultDto<T>();
        dto.setCode(resultCode.code());
        dto.setMessage(resultCode.message());
        dto.setData(data);
        return dto;

    }

    private MergeApiDto makeMergeData(AmsAccountInfo amsAccountInfo, SaicIdpInfo saicIdpInfo) {
        MergeApiDto mergeApiDto = new MergeApiDto();

        if (amsAccountInfo != null) {
            BeanCopierUtils.copyProperties(amsAccountInfo, mergeApiDto);
        }
        //如果没有工商数据，将以下人行字段赋值
        if (saicIdpInfo == null) {
            saicIdpInfo = new SaicIdpInfo();
        }
        pbcData2SaicData(amsAccountInfo, saicIdpInfo);

        BeanCopierUtils.copyProperties(saicIdpInfo, mergeApiDto);
        return mergeApiDto;
    }

    private void pbcData2SaicData(AmsAccountInfo amsAccountInfo, SaicIdpInfo saicIdpInfo) {
        if (amsAccountInfo == null) {
            return;
        }
        if (StringUtils.isBlank(saicIdpInfo.getName())) {
            saicIdpInfo.setName(amsAccountInfo.getDepositorName());
        }
        if (StringUtils.isBlank(saicIdpInfo.getUnitycreditcode())) {
            saicIdpInfo.setUnitycreditcode(amsAccountInfo.getFileNo());
        }
        if (StringUtils.isBlank(saicIdpInfo.getLegalperson())) {
            saicIdpInfo.setLegalperson(amsAccountInfo.getLegalName());
        }
        if (StringUtils.isBlank(saicIdpInfo.getRegistfund())) {
            saicIdpInfo.setRegistfund(amsAccountInfo.getRegisteredCapital());
        }
        if (StringUtils.isBlank(saicIdpInfo.getAddress())) {
            saicIdpInfo.setAddress(amsAccountInfo.getRegAddress());
        }
        if (StringUtils.isBlank(saicIdpInfo.getScope())) {
            saicIdpInfo.setScope(amsAccountInfo.getBusinessScope());
        }
        if (StringUtils.isBlank(saicIdpInfo.getEnddate())) {
            saicIdpInfo.setEnddate(amsAccountInfo.getTovoidDate());
        }
    }

    private boolean resultIsOk(ResultCode resultCode) {
        ResultCode[] okStatus = {ResultCode.PARAM_IS_BLANK, ResultCode.PBC_QUERY_ERROR, ResultCode.ORGAN_NOT_CONFIG_SYNC_ECCS_USER, ResultCode.ORGAN_NOT_CONFIG
                , ResultCode.ORGAN_NOT_CONFIG_IP, ResultCode.PBC_USER_NOT_2_LEVEL, ResultCode.ORGAN_NOT_CONFIG_SYNC_USER, ResultCode.NO_VALID_PBCACCOUNT, ResultCode.NO_DATA_EXIST
                , ResultCode.PBC_VALIDATION_NOT_PASS, ResultCode.UNKNOWN_SYSTEM_ERROR, ResultCode.PBCACCOUNT_NOT_FOUND, ResultCode.PBC_BASICACCT_INCLUDE_SUSPEND
                , ResultCode.PBC_CHECKDETAIL_FAILURE, ResultCode.PBC_NOT_FOUND, ResultCode.PBC_USER_NOT_EXIST, ResultCode.NETWORK_CONNECTION_ERROR, ResultCode.NETWORK_ERROR
                , ResultCode.NETWORK_TIMEOUT, ResultCode.PBC_LOGIN_ERROR, ResultCode.PBC_USER_LOCK, ResultCode.PBC_USER_OR_PASSWORD_EMPTY, ResultCode.USER_NOT_EXIST
                , ResultCode.USER_NOT_LOGGED_IN, ResultCode.PARAM_IS_INVALID};

        return (Arrays.asList(okStatus).contains(resultCode));
    }

}
