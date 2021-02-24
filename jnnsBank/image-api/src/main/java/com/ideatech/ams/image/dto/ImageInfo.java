package com.ideatech.ams.image.dto;

import com.ideatech.ams.image.enums.BillType;
import com.ideatech.ams.image.enums.CompanyAcctType;
import com.ideatech.ams.image.enums.IsUpload;
import lombok.Data;

@Data
public class ImageInfo {
    private Long id;
    private String batchNumber;

    private String docCode;

    private String docName;

    private BillType operateType;

    private CompanyAcctType acctType;

    private String depositorType;

    private String fileName;
    private String imgPath;
    private Long acctId;//账户表的ID
    private Long refBillId;//流水ID
    private String number;
    private IsUpload isUpload;
    private String fileNmeCN;
    /**
     * 证件到期日
     */
    private String expireDateStr;
}
