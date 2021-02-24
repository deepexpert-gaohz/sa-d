package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.service.CompanyPreOpenAccountEntService;
import com.ideatech.ams.apply.vo.CompanyPreOpenAccountEntVo;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.BeanCopierUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 预约接口服务service
 */
public class ApplyApiServiceImpl implements ApplyApiService {
    @Autowired
    private CompanyPreOpenAccountEntService companyPreOpenAccountEntService;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    /**
     * 预约写入接口
     * @param companyPreOpenAccountEntDto
     * @param bankCode
     */
    @Override
    public ResultDto saveApply(CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto, String bankCode) {
        try {
            companyPreOpenAccountEntService.saveApply(companyPreOpenAccountEntDto,bankCode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDtoFactory.toNack(e.getMessage());
        }
        return ResultDtoFactory.toAck("预约写入成功");
    }

    /**
     * 预约查询的接口
     * @param dto
     * @return
     */
    @Override
    public List<CompanyPreOpenAccountEntVo> searchApplyList(CompanyPreOpenAccountEntDto dto) {
        List<CompanyPreOpenAccountEntDto> cpoaedList = companyPreOpenAccountEntService.query(dto);
        List<CompanyPreOpenAccountEntVo> listVo = new ArrayList<>();
        for (CompanyPreOpenAccountEntDto cpoaed : cpoaedList){
            CompanyPreOpenAccountEntVo cpoaev = new CompanyPreOpenAccountEntVo();
            BeanCopierUtils.copyProperties(cpoaed, cpoaev);
            listVo.add(cpoaev);
        }
        return listVo;
    }

    @Override
    public CompanyPreOpenAccountEntDto queryApplyIdAndName(String applyId, String companyName) {
        CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto = null;
        if(StringUtils.isEmpty(applyId) || StringUtils.isEmpty(companyName)){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR,"预约编号跟企业名称不能为空!");
        }

        if(StringUtils.isNotBlank(applyId) && StringUtils.isNotBlank(companyName)){
            companyPreOpenAccountEntDto =companyPreOpenAccountEntService.findByApplyIdAndName(applyId,companyName);
            if(companyPreOpenAccountEntDto != null){
                return companyPreOpenAccountEntDto;
            }
        }
        return null;
    }

    @Override
    public CompanyPreOpenAccountEntDto getAcctNoAndAccountStatus(String applyId, String depositorName) {
        if(StringUtils.isEmpty(applyId) && StringUtils.isEmpty(depositorName)){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR,"预约编号跟企业名称不能为空！");
        }
        CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto = companyPreOpenAccountEntService.findByApplyid(applyId);
        if(companyPreOpenAccountEntDto ==  null){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR,"预约编号"+applyId+"查询预约信息失败，无此预约数据信息！");
        }
        List<AccountBillsAllInfo> obj = accountBillsAllService.findByPreOpenAcctIdAndDepositorName(applyId,depositorName);
        if(CollectionUtils.isEmpty(obj)){
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR,"预约编号"+applyId+"企业名称"+depositorName+"查询信息失败，无此数据信息！");
        }
        AccountBillsAllInfo accountBillsAll = obj.get(0);;
        companyPreOpenAccountEntDto.setAcctNo(accountBillsAll.getAcctNo());
        companyPreOpenAccountEntDto.setAccountstatus(accountBillsAll.getStatus().getValue());
        return companyPreOpenAccountEntDto;
    }
}
