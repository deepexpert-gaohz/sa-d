package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.vo.CompanyPreOpenAccountEntVo;
import com.ideatech.common.dto.ResultDto;

import java.util.List;

public interface ApplyApiService {

    ResultDto saveApply(CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto, String bankCode);

    List<CompanyPreOpenAccountEntVo> searchApplyList(CompanyPreOpenAccountEntDto dto);
    /**
     * 根据预约编号跟企业名称去查询预约数据以及预约状态
     * @param applyId
     * @param companyName
     * @return
     */
    CompanyPreOpenAccountEntDto queryApplyIdAndName(String applyId,String companyName);

    /**
     * 根据预约编号跟企业名称去查询预约数据以及账户账号跟开户状态
     * @param applyId
     * @param depositorName
     * @return
     */
    CompanyPreOpenAccountEntDto getAcctNoAndAccountStatus(String applyId,String depositorName);
}
