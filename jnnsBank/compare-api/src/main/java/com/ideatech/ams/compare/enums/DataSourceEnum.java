package com.ideatech.ams.compare.enums;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * 数据源枚举
 * @author vantoo
 */
@Getter
public enum DataSourceEnum {

    AMS("本系统账管数据", "CompareData1","0"),

    PBC("人行账管数据", "CompareData2","0,1"),

    SAIC("工商数据", "CompareData3","0,2"),

    CORE("核心数据", "CompareData4","-1"),

    OTHER("其他数据", "","-1");

    private String value;

    private String domain;

    private String priority;

    DataSourceEnum(String value, String domain,String priority) {
        this.value = value;
        this.domain = domain;
        this.priority = priority;
    }

    /**
     * 遍历出低于当前优先级且大于等于0的数据源
     * @return
     */
    public List<DataSourceEnum> findLowPriority(){
        List<DataSourceEnum> list = new ArrayList<>();
        String[] split = StringUtils.split(this.getPriority(), ",");
        for(int i=0;i<split.length-1;i++){
            for(DataSourceEnum single:DataSourceEnum.values()){
                if(!StringUtils.equals(single.getPriority(),"-1")) {
                    if(StringUtils.length(single.getPriority()) > 2 ){//多级
                        String[] newStr = StringUtils.split(single.getPriority(),",");
                        if(StringUtils.equals(newStr[newStr.length-1],split[i])){
                            list.add(single);
                            break;
                        }
                    }else{//单级
                        if(StringUtils.equals(single.getPriority(),split[i])) {
                            list.add(single);
                            break;
                        }
                    }
                }
            }

        }
        return list;
    }


    public static DataSourceEnum str2enum(String dataSourceEnumStr) {
        if (StringUtils.isBlank(dataSourceEnumStr)) {
            return null;
        }
        if (dataSourceEnumStr.equals("本系统账管数据") || dataSourceEnumStr.equals("AMS")) {
            return DataSourceEnum.AMS;
        } else if (dataSourceEnumStr.equals("人行账管数据") || dataSourceEnumStr.equals("PBC")) {
            return DataSourceEnum.PBC;
        } else if (dataSourceEnumStr.equals("工商数据") || dataSourceEnumStr.equals("SAIC")) {
            return DataSourceEnum.SAIC;
        } else if (dataSourceEnumStr.equals("核心数据") || dataSourceEnumStr.equals("CORE")) {
            return DataSourceEnum.CORE;
        } else {
            return DataSourceEnum.OTHER;
        }
    }

}
