package com.ideatech.ams.vo;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

@Data
public class CoreStockHolderExcelRowVo {
    /**
     * 账号
     */
    @ExcelField(title = "", column = 2)
    private String acctNo;

    /**
     * 客户号
     */
    @ExcelField(title = "", column = 0)
    private String customerNo;

    /**
     * 客户名称
     */
    @ExcelField(title = "", column = 1)
    private String customerName;

    /**
     * 控股股东名称
     */
    @ExcelField(title = "", column = 3)
    private String stockHolderName;

    /**
     * 控股股东持股比例
     */
    @ExcelField(title = "", column = 4)
    private Double stockHolderRatio;

    /**
     * 核心机构号
     */
    @ExcelField(title = "核心机构号", column = 5)
    private String organCode;
}
