package com.ideatech.ams.risk.whiteList.vo;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

@Data
public class RiskWhiteListVo {

    /**
     * @author yangcq
     * @create 20191123
     **/
    @ExcelField(title = "账户号", column = 0)
    private String accountId;

    @ExcelField(title = "账号名称", column = 1)
    private String accountName;

    @ExcelField(title = "社会统一编码", column = 2)
    private String socialUnifiedCode;
}
