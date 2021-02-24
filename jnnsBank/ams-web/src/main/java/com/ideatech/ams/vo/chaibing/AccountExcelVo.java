package com.ideatech.ams.vo.chaibing;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

@Data
public class AccountExcelVo {

    @ExcelField(title = "账号", column = 0)
    private String acctNo;
    @ExcelField(title = "账户名称", column = 1)
    private String acctName;
    @ExcelField(title = "机构号", column = 2)
    private String bankCode;
    @ExcelField(title = "机构名称", column = 3)
    private String bankName;
    @ExcelField(title = "迁移机构（内部机构号）", column = 4)
    private String toOrganCode;
}
