package com.ideatech.ams.vo;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

@Data
public class TelecomValidationExcelRowVo {
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
     * 核心机构号
     */
    @ExcelField(title = "核心机构号", column = 3)
    private String bankCode;

    /**
     * 姓名
     */
    @ExcelField(title = "姓名", column = 4)
    private String name;

    /**
     * 身份证号
     */
    @ExcelField(title = "身份证号", column = 5)
    private String idNo;

    /**
     * 手机号
     */
    @ExcelField(title = "手机号", column = 6)
    private String mobile;
}
