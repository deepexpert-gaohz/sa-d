package com.ideatech.ams.vo;

import com.ideatech.ams.system.blacklist.enums.EBlackListEntrySource;
import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

/**
 * @author liangding
 * @create 2018-06-26 下午2:29
 **/
@Data
public class BlackListExcelRowVo {
    @ExcelField(title = "企业名称（必填）", column = 0)
    private String entName;
    @ExcelField(title = "名单来源", column = 1, fieldType = EBlackListEntrySource.class)
    private EBlackListEntrySource source;
    @ExcelField(title = "名单级别", column = 2)
    private String level;
    @ExcelField(title = "类型", column = 3)
    private String type;
    /*@ExcelField(title = "白名单", column = 4)
    private String isWhiteStr;
    private Boolean isWhite;*/
}
