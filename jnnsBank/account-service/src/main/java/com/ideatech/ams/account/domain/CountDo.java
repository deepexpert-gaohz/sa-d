package com.ideatech.ams.account.domain;

import lombok.Data;

/**
 * 分组查询后的返回数据实体
 */
@Data
public class CountDo {
    private String groupByStr;
    private Long count;

    public CountDo(String groupByStr, Long count) {
        this.groupByStr = groupByStr;
        this.count = count;
    }
}
