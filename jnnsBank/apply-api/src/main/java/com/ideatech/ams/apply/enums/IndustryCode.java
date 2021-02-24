package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum IndustryCode {

    INDUSTRY_CODE_01 ("A$$农、林、牧、渔业$$第一产业 $$1", "农、林、牧、渔业"),
    INDUSTRY_CODE_02 ("B$$采矿业$$第二产业 $$2", "采矿业"),
    INDUSTRY_CODE_03 ("C$$制造业$$第二产业 $$2", "制造业"),
    INDUSTRY_CODE_04 ("D$$电力、煤气及水的生产和供应业$$第二产业 $$2", "电力、煤气及水的生产和供应业"),
    INDUSTRY_CODE_05 ("E$$建筑业$$第二产业 $$2", "建筑业"),
    INDUSTRY_CODE_06 ("F$$交通运输、仓储及邮政业$$第一层次 $$3", "交通运输、仓储及邮政业"),
    INDUSTRY_CODE_07 ("G$$信息传输、计算机服务和软件业$$第二层次 $$4", "信息传输、计算机服务和软件业"),
    INDUSTRY_CODE_08 ("H$$批发和零售业$$第二层次 $$4", "批发和零售业"),
    INDUSTRY_CODE_09 ("I$$住宿和餐饮业$$第二层次 $$4", "住宿和餐饮业"),
    INDUSTRY_CODE_10 ("J$$金融业$$第二层次 $$4", "金融业"),
    INDUSTRY_CODE_11 ("K$$房地产业$$第二层次 $$4", "房地产业"),
    INDUSTRY_CODE_12 ("L$$租赁和商务服务业$$第二层次 $$4", "租赁和商务服务业"),
    INDUSTRY_CODE_13 ("M$$科学研究、技术服务和地质勘查业$$第三层次 $$5", "科学研究、技术服务和地质勘查业"),
    INDUSTRY_CODE_14 ("N$$水利、环境和公共设施管理业$$第三层次 $$5", "水利、环境和公共设施管理业"),
    INDUSTRY_CODE_15 ("O$$居民服务和其他服务业$$第三层次 $$5", "居民服务和其他服务业"),
    INDUSTRY_CODE_16 ("P$$教育$$第三层次 $$5", "教育"),
    INDUSTRY_CODE_17 ("Q$$卫生、社会保障和社会福利业$$第三层次 $$5", "卫生、社会保障和社会福利业"),
    INDUSTRY_CODE_18 ("R$$文化、体育和娱乐业$$第三层次 $$5", "文化、体育和娱乐业"),
    INDUSTRY_CODE_19 ("S$$公共管理和社会组织$$第四层次 $$6", "公共管理和社会组织"),
    INDUSTRY_CODE_20 ("T$$国际组织（其他行业）$$第四层次 $$6", "国际组织（其他行业）"),
    INDUSTRY_CODE_21 ("U$$其他$$第一层次 $$3", "其他");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(IndustryCode type:IndustryCode.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }



    private String value;

    private String displayName;

    private IndustryCode(String value, String displayName) {
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
