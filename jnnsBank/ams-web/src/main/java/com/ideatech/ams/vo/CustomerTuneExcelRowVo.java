package com.ideatech.ams.vo;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

@Data
public class CustomerTuneExcelRowVo {
    @ExcelField(title = "企业名称", column = 1)
    private String entname;

}
