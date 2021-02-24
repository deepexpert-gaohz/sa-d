package com.ideatech.ams.vo;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

@Data
public class CoreBeneficiaryExcelRowVo {
    /**
     * 账号
     */
    @ExcelField(title = "账号", column = 2)
    private String acctNo;

    /**
     * 客户号
     */
    @ExcelField(title = "客户号", column = 0)
    private String customerNo;

    /**
     * 客户名称
     */
    @ExcelField(title = "客户名称", column = 1)
    private String customerName;

    /**
     * 受益人名称1
     */
    @ExcelField(title = "受益人名称1", column = 3)
    private String beneficiaryName1;

    /**
     * 受益人比例1
     */
    @ExcelField(title = "受益人比例1", column = 4)
    private Double beneficiaryRatio1;

    /**
     * 受益人名称2
     */
    @ExcelField(title = "受益人名称2", column = 5)
    private String beneficiaryName2;

    /**
     * 受益人比例2
     */
    @ExcelField(title = "受益人比例2", column = 6)
    private Double beneficiaryRatio2;

    /**
     * 受益人名称3
     */
    @ExcelField(title = "受益人名称3", column = 7)
    private String beneficiaryName3;

    /**
     * 受益人比例3
     */
    @ExcelField(title = "受益人比例3", column = 8)
    private Double beneficiaryRatio3;

    /**
     * 受益人名称4
     */
    @ExcelField(title = "受益人名称4", column = 9)
    private String beneficiaryName4;

    /**
     * 受益人比例4
     */
    @ExcelField(title = "受益人比例4", column = 10)
    private Double beneficiaryRatio4;

    /**
     * 法人
     */
    @ExcelField(title = "法人", column = 11)
    private String legalName;

    /**
     * 核心机构号
     */
    @ExcelField(title = "核心机构号", column = 12)
    private String organCode;
}
