package com.ideatech.ams.vo.chaibing;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

@Data
public class CustomerExcelVo {

    @ExcelField(title = "ID", column = 0)
    private String customerId;
    @ExcelField(title = "客户号", column = 1)
    private String customerNo;
    @ExcelField(title = "企业名称", column = 2)
    private String depositorName;
    @ExcelField(title = "机构名称", column = 3)
    private String orgName;
    @ExcelField(title = "机构号", column = 4)
    private String organCode;
    @ExcelField(title = "迁移机构（内部机构号）", column = 5)
    private String toOrganCode;
}
