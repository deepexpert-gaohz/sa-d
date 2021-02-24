package com.ideatech.ams.account.dto;

import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.common.dto.BaseMaintainableDto;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

/**
 * @author vantoo
 * @date 17:52 2018/5/20
 */
@Data
public class PbcSyncListDto extends BaseMaintainableDto {

    private Long id;

    private String acctNo;

    private String accountKey;

    private String acctAccountKey;

    private Long organId;

    private String organFullId;

    private BillType billType;

    private CompanyIfType syncStatus;

    private CompanyIfType isPush;

    private Long billId;

    private AmsAccountInfo amsAccountInfo;

    private Boolean cancelHeZhun;

    private String acctCreateDate;

    private String effectiveDate;

}
