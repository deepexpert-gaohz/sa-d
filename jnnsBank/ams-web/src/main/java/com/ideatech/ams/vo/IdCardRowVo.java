package com.ideatech.ams.vo;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

@Data
public class IdCardRowVo {

    @ExcelField(title = "姓名", column = 0)
    private String idCardName;

    @ExcelField(title = "身份证号码", column = 1)
    private String idCardNo;

}
