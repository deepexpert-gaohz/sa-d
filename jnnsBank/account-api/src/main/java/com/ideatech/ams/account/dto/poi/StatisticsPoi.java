package com.ideatech.ams.account.dto.poi;

import lombok.Data;

/**
 * @Description 账户开变销统计excel导出对象
 * @Author houxianghua
 * @Date 2019/7/4
 **/
@Data
public class StatisticsPoi {
    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 新开户数量
     */
    private String openCount;

    /**
     * 变更数量
     */
    private String changeCount;

    /**
     * 销户数量
     */
    private String revokeCount;

    /**
     * 久悬数量
     */
    private String suspendCount;
}
