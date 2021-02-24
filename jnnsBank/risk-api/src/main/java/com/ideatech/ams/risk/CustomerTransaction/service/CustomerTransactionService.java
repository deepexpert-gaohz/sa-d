package com.ideatech.ams.risk.CustomerTransaction.service;


import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.compare.dto.CsrMessageDto;
import com.ideatech.ams.kyc.dto.SaicInfoDto;
import com.ideatech.ams.risk.CustomerTransaction.dto.*;
import com.ideatech.ams.risk.highRisk.entity.Anomalya;
import com.ideatech.ams.risk.highRisk.entity.Lllegal;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

@Service
public interface CustomerTransactionService {
    void queryBusinessDataBase(String keyword) throws UnsupportedEncodingException;
    void queryBusinessData(String bankcode) throws ParseException;
    void queryCompanyException(String bankcode) throws ParseException;
    void queryCompanyBlack(String bankcode) throws ParseException;
    void checkBusinessData() throws ParseException;
    CustomerTransactionSearchDto search(CustomerTransactionSearchDto searchDto);
    List<Lllegal> findIllegalsDetails(String name);
    List<Anomalya> anomalyaDetails(String name);
    SaicInfoDto findOpenDate(String name);
    List<BusinessChangesDto> findChangesDetails(String name);
    CustomerTransactionDto findOneById(Long id);
    void exportTransaction(CustomerTransactionSearchDto customerTransactionSearchDto, HttpServletResponse response) throws IOException;
    void saveMessage(CsrMessageDto csrMessageDto);
    void updateMessageById(String message, Long id);
    List<CsrMessageDto> messageList(CsrMessageDto csrMessageDto);
    JSONObject getIndexAbnormalCounts(String orgFullId);
}
