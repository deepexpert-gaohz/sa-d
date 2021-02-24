package com.ideatech.ams.poi;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

@Data
public class UserPoi {

    /**
     * 中文名称
     */
    private String cname;

    /**
     * 用户名
     */
    private String username;
}
