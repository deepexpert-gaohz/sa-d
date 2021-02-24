package com.ideatech.ams.vo.chaibing;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

/**
 * 机构拆并用户上传
 */
@Data
public class UserExcelVo {
    @ExcelField(title = "中文姓名", column = 0)
    private String cname;
    @ExcelField(title = "用户名", column = 1)
    private String username;
    @ExcelField(title = "迁移机构（内部机构号）", column = 2)
    private String toOrganCode;
}
