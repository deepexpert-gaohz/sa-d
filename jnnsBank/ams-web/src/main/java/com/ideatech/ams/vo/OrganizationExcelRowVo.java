package com.ideatech.ams.vo;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

/**
 * @author liangding
 * @create 2018-05-10 下午10:26
 **/
@Data
public class OrganizationExcelRowVo {
    @ExcelField(title = "机构名称(请统一规范填写)", column = 0)
    private String name;

    @ExcelField(title = "机构号（核心）", column = 1)
    private String code;

    @ExcelField(title = "上级机构号（核心）", column = 2)
    private String parentCode;

    @ExcelField(title = "人行机构号（12位）", column = 3)
    private String pbcCode;

    @ExcelField(title = "机构联系人", column = 4)
    private String mobile;

    @ExcelField(title = "网点电话", column = 5)
    private String telephone;

    @ExcelField(title = "人行系统IP地址", column = 6)
    private String pbcIp;

    @ExcelField(title = "人行账管系统用户名\n(2级操作员)", column = 7)
    private String pbcAmsUsername;

    @ExcelField(title = "人行账管系统用户密码", column = 8)
    private String pbcAmsPassword;

    @ExcelField(title = "是否取消核准机构（是/否）", column = 9)
    private String cancelHeZhunOrg;

    @ExcelField(title = "人行账管系统用户名\n(4级操作员-取消核准专用)", column = 10)
    private String pbcAmsFourUsername;

    @ExcelField(title = "人行账管系统用户密码\n(4级操作员-取消核准专用)", column = 11)
    private String pbcAmsFourPassword;

    @ExcelField(title = "机构信用代码系统用户名", column = 12)
    private String eccsUsername;

    @ExcelField(title = "机构信用代码系统密码", column = 13)
    private String eccsPassword;

    @ExcelField(title = "联网核查IP地址", column = 14)
    private String picpIp;

    @ExcelField(title = "联网核查用户名", column = 15)
    private String picpUsername;

    @ExcelField(title = "联网核查密码", column = 16)
    private String picpPassord;
}
