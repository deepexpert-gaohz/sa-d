package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.DepositorType;
import com.ideatech.common.msg.ObjectRestResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by houmingwan on 2019/1/23.
 */
public interface PrintingApiService {

    ObjectRestResponse<AllBillsPublicDTO> getCompletePDF(Long billId, BillType billType, DepositorType depositorType) throws IOException;
}
