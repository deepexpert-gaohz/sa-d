package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;

/**
 * 补打基本存款账户信息
 */
public interface AmsResetJiBenPrintService {

    AmsAccountInfo resetJiBenPrintFristStep(LoginAuth auth, String acctNo, String pbcCode, String bankName) throws Exception ;

    String[] resetJiBenPrintSecondStep(LoginAuth auth,String fileType) throws Exception ;
}
