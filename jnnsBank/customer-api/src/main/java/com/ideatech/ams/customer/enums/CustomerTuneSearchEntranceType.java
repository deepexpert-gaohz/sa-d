package com.ideatech.ams.customer.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 客户综合尽调查询入口类型
 * （如：运营商校验，除了“客户综合尽调”这个入口点击查询，记录日志以外，还有其他地方可以进行运营商校验）
 */
public enum CustomerTuneSearchEntranceType {
    ENTRANCE_VERIFY_UNIQUE_JIBEN("客户综合尽调-基本户唯一性校验"),
    ENTRANCE_PBCRESULT_BY_ACCTNO("客户综合尽调-账号查人行信息"),
    ENTRANCE_PBCRESULT_BY_ACCOUNTKEY("客户综合尽调-开户许可证查人行信息"),
    ENTRANCE_ECCSRESULT("客户综合尽调-查代码证系统信息"),
    ENTRANCE_SAIC_BY_NAME("客户综合尽调-工商基本信息查询"),
    ENTRANCE_VERIFY_CARRIER_OPERATOR("客户综合尽调-运营商校验"),
    ENTRANCE_VERIFY_ID_CARD("客户综合尽调-联网身份核查"),
    ENTRANCE_JUDICIAL_INFORMATION("客户综合尽调-司法信息查询"),
    INTERFACE_VERIFY_CARRIER_OPERATOR("接口模式-运营商校验"),
    INTERFACE_SAIC_BY_NAME("接口模式-工商基本信息查询");

    private String value;

    CustomerTuneSearchEntranceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static CustomerTuneSearchEntranceType str2enum(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        if (type.equalsIgnoreCase("ENTRANCE_VERIFY_UNIQUE_JIBEN") || type.equals("客户综合尽调-基本户唯一性校验")) {
            return CustomerTuneSearchEntranceType.ENTRANCE_VERIFY_UNIQUE_JIBEN;
        } else if (type.equalsIgnoreCase("ENTRANCE_PBCRESULT_BY_ACCTNO") || type.equals("客户综合尽调-账号查人行信息")) {
            return CustomerTuneSearchEntranceType.ENTRANCE_PBCRESULT_BY_ACCTNO;
        } else if (type.equalsIgnoreCase("ENTRANCE_PBCRESULT_BY_ACCOUNTKEY") || type.equals("客户综合尽调-开户许可证查人行信息")) {
            return CustomerTuneSearchEntranceType.ENTRANCE_PBCRESULT_BY_ACCOUNTKEY;
        } else if (type.equalsIgnoreCase("ENTRANCE_ECCSRESULT") || type.equals("客户综合尽调-查代码证系统信息")) {
            return CustomerTuneSearchEntranceType.ENTRANCE_ECCSRESULT;
        } else if (type.equalsIgnoreCase("ENTRANCE_SAIC_BY_NAME") || type.equals("客户综合尽调-工商基本信息查询")) {
            return CustomerTuneSearchEntranceType.ENTRANCE_SAIC_BY_NAME;
        } else if (type.equalsIgnoreCase("VERIFY_CARRIER_OPERATOR") || type.equals("客户综合尽调-运营商校验")) {
            return CustomerTuneSearchEntranceType.ENTRANCE_VERIFY_CARRIER_OPERATOR;
        } else if (type.equalsIgnoreCase("VERIFY_ID_CARD") || type.equals("客户综合尽调-联网身份核查")) {
            return CustomerTuneSearchEntranceType.ENTRANCE_VERIFY_ID_CARD;
        } else if (type.equalsIgnoreCase("ENTRANCE_JUDICIAL_INFORMATION") || type.equals("客户综合尽调-司法信息查询")) {
            return CustomerTuneSearchEntranceType.ENTRANCE_JUDICIAL_INFORMATION;
        } else if (type.equalsIgnoreCase("INTERFACE_VERIFY_CARRIER_OPERATOR") || type.equals("接口模式-运营商校验")) {
            return CustomerTuneSearchEntranceType.INTERFACE_VERIFY_CARRIER_OPERATOR;
        }else if (type.equalsIgnoreCase("INTERFACE_SAIC_BY_NAME") || type.equals("接口模式-工商基本信息查询")) {
            return CustomerTuneSearchEntranceType.INTERFACE_SAIC_BY_NAME;
        }

        return null;
    }
}
