package com.ideatech.ams.vo;

import com.ideatech.ams.system.blacklist.enums.EBlackListEntrySource;
import com.ideatech.ams.system.whitelist.enums.WhiteListEntrySource;
import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

/**
 * @author liangding
 * @create 2018-06-26 下午2:29
 **/
@Data
public class WhiteListExcelRowVo {
    @ExcelField(title = "企业名称（必填）", column = 0)
    private String entName;
    //    @ExcelField(title = "名单来源", column = 1, fieldType = WhiteListEntrySource.class)
//    private WhiteListEntrySource source;
    @ExcelField(title = "所属机构（内部机构号）", column = 1)
    private String organCode;
}
