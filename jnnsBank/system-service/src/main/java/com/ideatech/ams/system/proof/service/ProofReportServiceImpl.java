package com.ideatech.ams.system.proof.service;

import com.ideatech.ams.system.proof.dao.ProofReportDao;
import com.ideatech.ams.system.proof.dao.spec.ProofReportSpec;
import com.ideatech.ams.system.proof.dto.ProofReportDto;
import com.ideatech.ams.system.proof.entity.ProofReport;
import com.ideatech.ams.system.proof.enums.ProofType;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.proof.poi.KycExport;
import com.ideatech.ams.system.proof.poi.KycPoi;
import com.ideatech.ams.system.proof.poi.PriceExport;
import com.ideatech.ams.system.proof.poi.PricePoi;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProofReportServiceImpl extends BaseServiceImpl<ProofReportDao, ProofReport, ProofReportDto> implements ProofReportService {
    @Autowired
    private OrganizationService organizationService;
    @Override
    public ProofReportDto findByAcctNoAndType(String acctNo, ProofType type) {
        ProofReportDto dto = null;
        ProofReport accountKycReport = getBaseDao().findByAcctNoAndType(acctNo,type);
        if(accountKycReport!=null){
            dto = new ProofReportDto();
            BeanUtils.copyProperties(accountKycReport,dto);
        }
        return dto;
    }

    @Override
    public TableResultResponse query(ProofReportDto accountKycReportDto, Pageable pageable) {
        SecurityUtils.UserInfo current = SecurityUtils.getCurrentUser();
        OrganizationDto organizationDto = organizationService.findById(current.getOrgId());
        accountKycReportDto.setOrganFullId(organizationDto.getFullId());
        Page<ProofReport> data = getBaseDao().findAll(new ProofReportSpec(accountKycReportDto),pageable);
        return new TableResultResponse((int)data.getTotalElements(), data.getContent());
    }

    @Override
    public Map<String,Object> searchAll(ProofReportDto accountKycReportDto) {
        SecurityUtils.UserInfo current = SecurityUtils.getCurrentUser();
        OrganizationDto organizationDto = organizationService.findById(current.getOrgId());
        accountKycReportDto.setOrganFullId(organizationDto.getFullId());
        double totle =0;
        Map<String,Object> map = new HashMap<>();
        List<ProofReportDto> reportDtos = new ArrayList<>();
        List<ProofReport> list = getBaseDao().findAll(new ProofReportSpec(accountKycReportDto));
        for (ProofReport proof:list) {
            ProofReportDto dto = new ProofReportDto();
            BeanUtils.copyProperties(proof,dto);
            if(proof.getAcctType()!=null){
                dto.setAcctTypeStr(proof.getAcctType().getValue());
            }
            if(proof.getKycFlag()!=null){
                if(proof.getKycFlag()==CompanyIfType.Yes){
                    dto.setKycFlagStr("是");
                }else{
                    dto.setKycFlagStr("否");
                }
            }
            if(proof.getType()!=null){
                dto.setTypeStr(proof.getType().getValue());
            }
            if(StringUtils.isNotBlank(proof.getPrice())){
                totle = sum(totle,Double.valueOf(proof.getPrice())) ;
            }
            reportDtos.add(dto);
        }
        map.put("data",reportDtos);
        map.put("totle",totle);
        return map;
    }

    @Override
    public IExcelExport export(ProofReportDto accountKycReportDto) {
        if(accountKycReportDto.getType()==ProofType.KYC){
            List<ProofType> list = new ArrayList<>();
            list.add(ProofType.KYC);
            accountKycReportDto.setTypeList(list);
        }else{
            List<ProofType> list = new ArrayList<>();
            list.add(ProofType.PBC);
            list.add(ProofType.SAIC);
            list.add(ProofType.PHONE);
            accountKycReportDto.setTypeList(list);
        }
        Map<String,Object> map = searchAll(accountKycReportDto);
        return create((List<ProofReportDto>)map.get("data"),accountKycReportDto);
    }

    @Override
    public Long count(ProofReportDto accountKycReportDto) {

        return null;
    }

    private IExcelExport create(List<ProofReportDto> list,ProofReportDto accountKycReportDto){
        IExcelExport iExcelExport = null;
        if(accountKycReportDto.getType()==ProofType.KYC){
            List<KycPoi> list1 = new ArrayList<>();
            iExcelExport = new KycExport();
            for (ProofReportDto dto:list) {
                KycPoi kycPoi = new KycPoi();
                kycPoi.setAcctNo(dto.getAcctNo());
                kycPoi.setAcctName(StringUtils.isBlank(dto.getAcctName())?"":dto.getAcctName());
                kycPoi.setAcctTypeStr(dto.getAcctTypeStr());
                kycPoi.setOpenBankName(dto.getOpenBankName());
                kycPoi.setDateTime(dto.getDateTime());
                kycPoi.setUsername(dto.getUsername());
                kycPoi.setKycFlagStr(dto.getKycFlagStr());
                kycPoi.setProofBankName(dto.getProofBankName());
                list1.add(kycPoi);
            }
            iExcelExport.setPoiList(list1);
        }else{
            iExcelExport =new  PriceExport();
            List<PricePoi> list1 = new ArrayList<>();
            for (ProofReportDto dto:list) {
                PricePoi poi = new PricePoi();
                poi.setPhone(StringUtils.isBlank(dto.getPhone())?"":dto.getPhone());
                poi.setEntname(StringUtils.isBlank(dto.getEntname())?"":dto.getEntname());
                poi.setTypeStr(dto.getTypeStr());
                poi.setDateTime(dto.getDateTime());
                poi.setUsername(dto.getUsername());
                poi.setProofBankName(dto.getProofBankName());
                poi.setPrice(dto.getPrice());
                list1.add(poi);
            }
            iExcelExport.setPoiList(list1);
        }
        return iExcelExport;
    }
    private  double sum(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }

}
