package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum OrgTypeDetails {

    ORG_TYPE_DETAILS_10 ("10", "企业法人"),
    ORG_TYPE_DETAILS_11 ("11", "其他企业"),
    ORG_TYPE_DETAILS_12 ("12", "农民专业合作社"),
    ORG_TYPE_DETAILS_13 ("13", "个人独资、合伙企业"),
    ORG_TYPE_DETAILS_14 ("14", "企业的分支机构"),

    ORG_TYPE_DETAILS_20 ("20", "事业法人"),
    ORG_TYPE_DETAILS_21 ("21", "未登记的事业单位"),
    ORG_TYPE_DETAILS_24 ("24", "事业单位的分支机构"),

    ORG_TYPE_DETAILS_30 ("30", "机关法人"),
    ORG_TYPE_DETAILS_31 ("31", "机关的内设机构"),
    ORG_TYPE_DETAILS_32 ("32", "机关的下设机构"),

    ORG_TYPE_DETAILS_40 ("40", "社会团体法人"),
    ORG_TYPE_DETAILS_41 ("41", "社会团体分支机构"),

    ORG_TYPE_DETAILS_70 ("70", "个体工商户"),

    ORG_TYPE_DETAILS_51 ("51", "民办非企业"),
    ORG_TYPE_DETAILS_52 ("52", "基金会"),
    ORG_TYPE_DETAILS_53 ("53", "居委会"),
    ORG_TYPE_DETAILS_54 ("54", "村委会"),

    ORG_TYPE_DETAILS_60 ("60", "律师事务所、司法鉴定所"),
    ORG_TYPE_DETAILS_61 ("61", "宗教活动场所"),
    ORG_TYPE_DETAILS_62 ("62", "境外在境内成立的组织机构"),

    ORG_TYPE_DETAILS_99 ("99", "其他");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(OrgTypeDetails type:OrgTypeDetails.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;

    private String displayName;

    OrgTypeDetails(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return this.value;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
