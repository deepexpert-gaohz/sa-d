package com.ideatech.ams.dto;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

/**
 * @author liangding
 * @create 2018-05-10 下午4:26
 **/
@Data
public class JnnsUserExcelRowVo {
    @ExcelField(title = "姓名", column = 0)
    private String cname;
    @ExcelField(title = "角色", column = 1)
    private String roleCode;
    @ExcelField(title = "机构号", column = 2)
    private String orgCode;
    @ExcelField(title = "用户名", column = 3)
    private String username;
    @ExcelField(title = "手机号", column = 4)
    private String mobile;
}
