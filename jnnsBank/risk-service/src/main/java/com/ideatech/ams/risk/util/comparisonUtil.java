package com.ideatech.ams.risk.util;


import com.ideatech.ams.customer.entity.CustomerPublic;
import org.apache.commons.lang.StringUtils;


public class comparisonUtil {

    //判断注册地址是否为重庆地区
    public static boolean comparisonRegAreaCode(String depositorName,String fileNo,String regFullAddress,String regArea,String  regCity,String regCityChName,String regProvince,String regProvinceChName,String zipCode) {
        boolean b = false;
        if (StringUtils.isNotBlank(depositorName) && StringUtils.isNotBlank(fileNo) && StringUtils.isNotBlank(regFullAddress) &&
                StringUtils.isNotBlank(regArea) && StringUtils.isNotBlank(regCity) && StringUtils.isNotBlank(regCityChName) &&
                StringUtils.isNotBlank(regProvince) && StringUtils.isNotBlank(regProvinceChName) && StringUtils.isNotBlank(zipCode) ) {
            if(depositorName.length() >= 2){
                if(depositorName.substring(0, 2).equals("重庆")){
                    b = true;
                    return b;
                }
            }
            if(fileNo.length() >= 1){
                if(fileNo.substring(0, 1).equals("渝")){
                    b = true;
                    return b;
                }
            }
            if(fileNo.length() >= 2){
                if(fileNo.substring(0, 2).equals("50")){
                    b = true;
                    return b;
                }
            }
            if(fileNo.length() >= 4){
                if(fileNo.substring(2, 4).equals("50")){
                    b = true;
                    return b;
                }
            }
            if(regFullAddress.length() >= 2){
                if(regFullAddress.substring(0, 2).equals("重庆")){
                    b = true;
                    return b;
                }
            }
            if(regArea.length() == 6){
                if(regArea.substring(0, 2).equals("50")){
                    b = true;
                    return b;
                }
            }
            if(regCity.length() ==6){
                if(regCity.equals("500000")){
                    b = true;
                    return b;
                }
            }
            if(regCityChName.length() >= 2){
                if(regCityChName.substring(0, 2).equals("重庆")){
                    b = true;
                    return b;
                }
            }
            if(regProvince.length() >= 2){
                if(regProvince.substring(0, 2).equals("50")){
                    b = true;
                    return b;
                }
            }
            if(regProvinceChName.length() >= 2){
                if(regProvinceChName.substring(0, 2).equals("重庆")){
                    b = true;
                    return b;
                }
            }
            if(zipCode.length() >= 2){
                if(zipCode.substring(0, 2).equals("40")){
                    b = true;
                    return b;
                }
            }
        }
        return b;
    }


    //判断法人身份证是否为重庆地区
    public static boolean comparisonLegalIdcardNo(String legalIdcardNo) {
        boolean b = false;
        if (StringUtils.isNotBlank(legalIdcardNo)) {
            if(legalIdcardNo.length()==18 || legalIdcardNo.length()==15){
                if(legalIdcardNo.substring(0,4).equals("3204")){
                    b=true;
                }
            }
        }
        return b;
    }
}
