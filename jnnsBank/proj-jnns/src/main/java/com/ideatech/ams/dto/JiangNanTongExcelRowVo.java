package com.ideatech.ams.dto;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

@Data
public class JiangNanTongExcelRowVo {

    @ExcelField(title = "经办人行员号", column = 0)
    private String operatorNo;
    @ExcelField(title = "经办人姓名", column = 1)
    private String operatorName;
    @ExcelField(title = "有权人行员号", column = 2)
    private String personNo;
    @ExcelField(title = "有权人姓名", column = 3)
    private String personName;
    @ExcelField(title = "账号", column = 4)
    private String acctNo;
}
