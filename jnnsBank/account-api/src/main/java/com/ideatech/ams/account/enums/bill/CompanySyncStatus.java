package com.ideatech.ams.account.enums.bill;

import org.apache.commons.lang.StringUtils;

/**
 * @author zoulang
 * @ClassName: CompanySyncStatus
 * @Description: 账户同步到系统状态
 * @date 2015年11月13日 上午10:32:02
 */
public enum CompanySyncStatus {

    buTongBu("无需上报"),

    weiTongBu("未同步"),

    tongBuChengGong("上报成功"),

    tongBuZhong("处理中"),

    tuiHui("退回"),

    tongBuShiBai("上报失败");

    CompanySyncStatus(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static CompanySyncStatus str2enum(String sync) {
        if (StringUtils.isBlank(sync)) {
            return null;
        }
        if (sync.equals("buTongBu")) {
            return CompanySyncStatus.buTongBu;
        } else if (sync.equals("weiTongBu")) {
            return CompanySyncStatus.weiTongBu;
        } else if (sync.equals("tongBuChengGong")) {
            return CompanySyncStatus.tongBuChengGong;
        } else if (sync.equals("tongBuShiBai")) {
            return CompanySyncStatus.tongBuShiBai;
        } else if (sync.equals("tongBuZhong")) {
            return CompanySyncStatus.tongBuZhong;
        } else if (sync.equals("tuiHui")) {
            return CompanySyncStatus.tuiHui;
        }
        return null;
    }

}
