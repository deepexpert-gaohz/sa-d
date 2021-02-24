package com.ideatech.ams.vo;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

/**
 * 机构导入人行账号密码excel数据对应实体
 **/
@Data
public class OrganizationPbcAccountExcelRowVo {
    @ExcelField(title = "机构号（核心）", column = 0)
    private String code;

    @ExcelField(title = "人行系统IP地址", column = 1)
    private String pbcIp;

    @ExcelField(title = "人行账管系统用户名\n(2级操作员)", column = 2)
    private String pbcAmsUsername;

    @ExcelField(title = "人行账管系统用户密码", column = 3)
    private String pbcAmsPassword;

    @ExcelField(title = "人行账管系统用户名\n(4级操作员-取消核准专用)", column = 4)
    private String pbcAmsFourUsername;

    @ExcelField(title = "人行账管系统用户密码\n(4级操作员-取消核准专用)", column = 5)
    private String pbcAmsFourPassword;

    @ExcelField(title = "机构信用代码系统用户名", column = 6)
    private String eccsUsername;

    @ExcelField(title = "机构信用代码系统密码", column = 7)
    private String eccsPassword;

    @ExcelField(title = "联网核查IP地址", column = 8)
    private String picpIp;

    @ExcelField(title = "联网核查用户名", column = 9)
    private String picpUsername;

    @ExcelField(title = "联网核查密码", column = 10)
    private String picpPassord;
}
