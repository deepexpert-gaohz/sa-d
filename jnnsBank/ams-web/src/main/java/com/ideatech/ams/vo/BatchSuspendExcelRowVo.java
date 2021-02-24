package com.ideatech.ams.vo;

import com.ideatech.ams.account.enums.AcctBigType;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

/**
 * @author van
 * @date 2018/7/11 14:41
 */
@Data
public class BatchSuspendExcelRowVo {
    @ExcelField(title = "账号", column = 0)
    private String acctNo;
    @ExcelField(title = "企业名称", column = 1)
    private String depositorName;
    @ExcelField(title = "行内机构号", column = 2)
    private String organCode;
    @ExcelField(title = "客户号", column = 3)
    private String customerNo;
    @ExcelField(title = "账户性质", column = 4, fieldType = CompanyAcctType.class)
    private CompanyAcctType acctType;
    @ExcelField(title = "账户性质大类", column = 5, fieldType = AcctBigType.class)
    private AcctBigType acctBigType;
}
