package com.ideatech.ams.compare.spi;

import com.ideatech.ams.compare.dto.ComparePbcInfoDto;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.service.DataTransformation;
import com.ideatech.ams.kyc.dto.SaicInfoDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component("pbcDataTransformation")
public class PbcDataTransformation implements DataTransformation<ComparePbcInfoDto> {

    @Override
    public void dataTransformation(CompareDataDto compareDataDto, ComparePbcInfoDto comparePbcInfoDto) {
        //TODO 缺少的是ACCOUNT_LICENSE_NO("账户开户许可核准号", "accountLicenseNo"), ECONOMY_INDUSTRY("经济行业分类", "economyIndustry"),REG_OFFICE("登记机关", "regOffice"),
        compareDataDto.setAcctNo(comparePbcInfoDto.getAcctNo());
        compareDataDto.setDepositorName(comparePbcInfoDto.getDepositorName());
        //人行setBankCode
        compareDataDto.setOrganCode(comparePbcInfoDto.getBankCode());
        compareDataDto.setOrganFullId(comparePbcInfoDto.getOrganFullId());
        compareDataDto.setLegalName(comparePbcInfoDto.getLegalName());
        compareDataDto.setRegNo(comparePbcInfoDto.getFileNo());
        compareDataDto.setBusinessScope(comparePbcInfoDto.getBusinessScope());
        compareDataDto.setRegAddress(comparePbcInfoDto.getRegAddress());
        compareDataDto.setRegisteredCapital(comparePbcInfoDto.getRegisteredCapital());
        compareDataDto.setAccountStatus(comparePbcInfoDto.getAccountStatus() == null ? null : comparePbcInfoDto.getAccountStatus().getFullName());
        compareDataDto.setStateTaxRegNo(comparePbcInfoDto.getStateTaxRegNo());
        compareDataDto.setTaxRegNo(comparePbcInfoDto.getTaxRegNo());
        compareDataDto.setBankName(comparePbcInfoDto.getBankName());
        compareDataDto.setAcctType(comparePbcInfoDto.getAcctType() == null ? null : comparePbcInfoDto.getAcctType().name());
        compareDataDto.setAcctCreateDate(comparePbcInfoDto.getAcctCreateDate());
        compareDataDto.setAccountKey(comparePbcInfoDto.getAccountKey());
        compareDataDto.setFileType(comparePbcInfoDto.getFileType());
        compareDataDto.setFileNo(comparePbcInfoDto.getFileNo());
        compareDataDto.setTelephone(comparePbcInfoDto.getTelephone());
        compareDataDto.setZipcode(comparePbcInfoDto.getZipCode());
        compareDataDto.setDepositorType(comparePbcInfoDto.getDepositorType());
        compareDataDto.setLegalIdcardType(comparePbcInfoDto.getLegalIdcardType());
        compareDataDto.setLegalIdcardNo(comparePbcInfoDto.getLegalIdcardNo());
        compareDataDto.setIndustryCode(comparePbcInfoDto.getIndustryCode());
        compareDataDto.setRegAreaCode(comparePbcInfoDto.getRegAreaCode());
        compareDataDto.setCancelDate(comparePbcInfoDto.getCancelDate());
        compareDataDto.setOrgCode(comparePbcInfoDto.getOrgCode());
    }

}
