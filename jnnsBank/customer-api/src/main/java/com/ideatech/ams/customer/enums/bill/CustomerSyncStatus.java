package com.ideatech.ams.customer.enums.bill;

import org.apache.commons.lang.StringUtils;

/**
 * 同步状态
 *
 * @author van
 * @date 2018/7/14 13:48
 */
public enum CustomerSyncStatus {

    buTongBu("无需上报"),

    weiTongBu("未同步"),

    tongBuChengGong("上报成功"),

    tongBuZhong("处理中"),

    tuiHui("退回"),

    tongBuShiBai("上报失败");

    CustomerSyncStatus(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static CustomerSyncStatus str2enum(String sync) {
        if (StringUtils.isBlank(sync)) {
            return null;
        }
        if (sync.equals("buTongBu")) {
            return CustomerSyncStatus.buTongBu;
        } else if (sync.equals("weiTongBu")) {
            return CustomerSyncStatus.weiTongBu;
        } else if (sync.equals("tongBuChengGong")) {
            return CustomerSyncStatus.tongBuChengGong;
        } else if (sync.equals("tongBuShiBai")) {
            return CustomerSyncStatus.tongBuShiBai;
        } else if (sync.equals("tongBuZhong")) {
            return CustomerSyncStatus.tongBuZhong;
        } else if (sync.equals("tuiHui")) {
            return CustomerSyncStatus.tuiHui;
        }
        return null;
    }

}
