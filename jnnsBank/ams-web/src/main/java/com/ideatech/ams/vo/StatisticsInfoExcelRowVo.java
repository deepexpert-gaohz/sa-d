package com.ideatech.ams.vo;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

/**
 * @Description 账户信息统计excel导出
 * @Author wanghongjie
 * @Date 2019/1/9
 **/
@Data
public class StatisticsInfoExcelRowVo {
    @ExcelField(title = "机构名称", column = 0)
    private String orgName;

    @ExcelField(title = "正常账号数量", column = 1)
    private String normalCount;

    @ExcelField(title = "撤销账号数量", column = 2)
    private String revokeCount;

    @ExcelField(title = "久悬账号数量", column = 3)
    private String suspendCount;
}
