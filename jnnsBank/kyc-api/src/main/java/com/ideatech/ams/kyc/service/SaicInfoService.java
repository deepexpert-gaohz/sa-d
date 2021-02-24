package com.ideatech.ams.kyc.service;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.kyc.dto.*;
import com.ideatech.ams.kyc.dto.poi.SaicPoi;
import com.ideatech.ams.kyc.dto.saicentrust.EntrustResultDto;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.Future;

public interface SaicInfoService {

    SaicInfoDto getSaicInfoBase(SearchType type, String username, String keyword, String orgfullid);

    SaicIdpInfo getSaicInfoBaseLocal(String keyword);

    SaicIdpInfo getSaicInfoBaseLocalByIdNoValidTime(Long saicInfoId);

    SaicIdpInfo getSaicInfoBaseLocalJustSaic(String keyword);

    SaicIdpInfo getSaicInfoFull(SearchType type, String username, String keyword, String orgfullid);
    
    JSONObject getCompanyPeopleInformation(SupplementQueryDto supplementQueryVo);
    
    SupplementDto saveCompanyPeopleInformation(SupplementDto supplementDto);
    
    SaicInfoDto findById(Long id);
    
	IExcelExport generateSaicReport(Long saicInfoId, SaicPoi saicPoi);
    
    IExcelExport generateBeneficiaryReport(Long saicInfoId, String name);
    
    boolean checkIfIllegals(List<IllegalDto> list);
    
    boolean checkIfChangeMess(List<ChangeMessDto> list);
    
    boolean checkIfDateNormal(String startDate,String endDate);
    
    boolean checkIfStateNormal(String state);

    boolean checkIfSimpleCancel(String notice);

    void inesrtSaicInfoByNull(String username, String keyword, String url, SearchType searchType, String orgfullid, String batchNo);

    boolean isInValidDays(SaicIdpInfo saicIdpInfo);

    void inesrtSaicInfoByType(String username, SaicIdpInfo saicIdpInfo, String url,SearchType searchType, String orgfullid);

    void inesrtSaicInfoByNull(String username,String keyword, String url,SearchType searchType, String orgfullid);

    boolean queryIsKyc(String name);

    List<Future<Long>> getBatchSaicInfoBase(List<KycSearchHistoryDto> kycSearchHistoryDtoList, String batchNo);

    void clearFuture();

    void valiCollectCompleted() throws Exception;

    SaicInfoDto getSaicInfoBase(SearchType type, String username, String keyword, String orgfullid, String batchNo);

    void saveSaicInfo(SaicIdpInfo saicIdpInfo);

    /**
     * 工商信息委托更新
     * @param keyword
     * @return
     */
    EntrustResultDto entrustUpdate(String keyword);

    /**
     * 工商信息查询委托状态
     * @param keyword
     * @return
     */
    EntrustResultDto getEntrustUpdateResult(String keyword);
}
