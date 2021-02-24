package com.ideatech.ams.customer.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 客户综合尽调查询分类
 */
public enum CustomerTuneSearchType {
    VERIFY_UNIQUE_JIBEN("基本户唯一性校验"),
    PBCRESULT_BY_ACCTNO("账号查人行信息"),
    PBCRESULT_BY_ACCOUNTKEY("开户许可证查人行信息"),
    ECCSRESULT("查代码证系统信息"),
    SAIC_BY_NAME("工商基本信息查询"),
    VERIFY_CARRIER_OPERATOR("运营商校验"),
    VERIFY_ID_CARD("联网身份核查"),
    JUDICIAL_INFORMATION("司法信息查询");

    private String value;

    CustomerTuneSearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static CustomerTuneSearchType str2enum(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        if (type.equalsIgnoreCase("VERIFY_UNIQUE_JIBEN") || type.equals("基本户唯一性校验")) {
            return CustomerTuneSearchType.VERIFY_UNIQUE_JIBEN;
        } else if (type.equalsIgnoreCase("PBCRESULT_BY_ACCTNO") || type.equals("账号查人行信息")) {
            return CustomerTuneSearchType.PBCRESULT_BY_ACCTNO;
        } else if (type.equalsIgnoreCase("PBCRESULT_BY_ACCOUNTKEY") || type.equals("开户许可证查人行信息")) {
            return CustomerTuneSearchType.PBCRESULT_BY_ACCOUNTKEY;
        } else if (type.equalsIgnoreCase("ECCSRESULT") || type.equals("查代码证系统信息")) {
            return CustomerTuneSearchType.ECCSRESULT;
        } else if (type.equalsIgnoreCase("SAIC_BY_NAME") || type.equals("工商基本信息查询")) {
            return CustomerTuneSearchType.SAIC_BY_NAME;
        } else if (type.equalsIgnoreCase("VERIFY_CARRIER_OPERATOR") || type.equals("运营商校验")) {
            return CustomerTuneSearchType.VERIFY_CARRIER_OPERATOR;
        } else if (type.equalsIgnoreCase("VERIFY_ID_CARD") || type.equals("联网身份核查")) {
            return CustomerTuneSearchType.VERIFY_ID_CARD;
        }else if (type.equalsIgnoreCase("JUDICIAL_INFORMATION") || type.equals("司法信息查询")) {
            return CustomerTuneSearchType.JUDICIAL_INFORMATION;
        }

        return null;
    }
}
