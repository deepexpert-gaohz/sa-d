package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dto.*;
import com.ideatech.ams.kyc.dto.holiday.HolidayDto;
import com.ideatech.ams.kyc.dto.newcompany.OutFreshCompanyQueryDto;
import com.ideatech.ams.kyc.dto.saicentrust.EntrustResultDto;
import com.ideatech.ams.kyc.enums.SearchType;

import java.util.List;

public interface SaicRequestService {
    SaicIdpInfo getSaicInfoRealTime(String keyword);

    SaicIdpInfo getSaicInfoRealTime(String keyword, String username);

    SaicIdpInfo getSaicInfoRealTime(String keyword, String username, SearchType type);

    String getSaicInfoRealTimeJson(String keyword);
    String getSaicInfoRealTimeUrl();
    SaicIdpInfo getSaicInfoExact(String keyword);

    SaicIdpInfo getSaicInfoExact(String keyword, String usernmae);

    SaicIdpInfo getSaicInfoExact(String keyword, String usernmae, SearchType type);

    String getSaicInfoExactJson(String keyword);
    String getSaicInfoExactUrl();
    OutEquityShareDto getOutEquityShareDto(String keyword);
    String getOutEquityShareDtoUrl();
    OutBeneficiaryDto getOutBeneficiaryDto(String keyword);
    String getOutBeneficiaryDtoUrl();
    OutBaseAccountDto getOutBaseAccountDto(String keyword);
    String getOutBaseAccountDtoUrl();
    OutIllegalQueryDto getOutIllegalQueryDto(String keyWord);
    String getOutIllegalQueryDtoUrl();
    List<HolidayDto> getHolidayDto(int year);

    /**
     * 获取运营商校验
     * @param encryptParam
     * @return
     */
    String getCarrierResponseStr(String encryptParam);

    /**
     * 获取运营商校验
     * @param encryptParam
     * @return
     */
    String getJudicialInformation(String encryptParam,int pageIndex,int pageSize);

    OutFreshCompanyQueryDto getFreshCompanyList(String provinceCode, String startDate, String endDate, Integer pageIndex, Integer pageSize);

    /**
     * 工商委托状态更新
     * @param keyWord
     * @return
     */
    EntrustResultDto entrustUpdate(String keyword) throws Exception;

    /**
     * 获取工商委托状态
     * @param keyWord
     * @return
     */
    EntrustResultDto getEntrustUpdateResult(String keyword) throws Exception;
}
