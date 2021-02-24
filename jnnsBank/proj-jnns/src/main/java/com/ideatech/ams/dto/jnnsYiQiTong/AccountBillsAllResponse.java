package com.ideatech.ams.dto.jnnsYiQiTong;

import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillStatus;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.enums.bill.CompanyAmsCheckStatus;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import lombok.Data;

@Data
public class AccountBillsAllResponse {

    private  Long id;///
    private  String billNo;///
    private  String billDate;///
    private String billType;
    private BillStatus status;
    private CompanyAmsCheckStatus pbcCheckStatus;
    private  String acctNo;///
    private  String customerNo;///
    private  String organFullId;///
    private CompanySyncStatus pbcSyncStatus;
    private CompanyAcctType acctType;
    private  String depositorType;///
    private  String depositorName;///
}
