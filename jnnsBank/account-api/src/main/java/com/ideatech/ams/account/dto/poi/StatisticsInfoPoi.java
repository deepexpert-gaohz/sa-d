package com.ideatech.ams.account.dto.poi;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

/**
 * @Description 账户信息统计excel导出对象
 * @Author wanghongjie
 * @Date 2019/1/9
 **/
@Data
public class StatisticsInfoPoi {
    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 正常账户数量
     */
    private String normalCount;

    /**
     * 撤销账户数量
     */
    private String revokeCount;

    /**
     * 久悬账户数量
     */
    private String suspendCount;
}
