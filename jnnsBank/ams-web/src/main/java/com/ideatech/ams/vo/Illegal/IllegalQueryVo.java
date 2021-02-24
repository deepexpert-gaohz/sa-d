package com.ideatech.ams.vo.Illegal;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

@Data
public class IllegalQueryVo {
    /**
     * 注册号
     */
    @ExcelField(title = "注册号", column = 1)
    private String regNo;

    /**
     * 企业名称
     */
    @ExcelField(title = "企业名称", column = 0)
    private String companyName;

    /**
     * 机构号
     */
    @ExcelField(title = "机构号", column = 2)
    private String organCode;
}
