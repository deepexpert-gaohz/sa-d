package com.ideatech.ams.image.enums;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 * @author jzh
 * @date 2020/5/9.
 */

@Getter
public enum  RecordTypeEnum {

    REMOTERECORD("remoteRecord","远程双录"),
    LOCALUPLOAD("localUpload","本地上传"),
    CLOSETRECORD("closetRecord","临柜双录");

    private String value;

    private String name;

    RecordTypeEnum(String value,String name){
        this.value = value;
        this.name = name;
    }

    public static RecordTypeEnum str2enum(String recordType) {
        if (StringUtils.isBlank(recordType)) {
            return null;
        }
        if (recordType.equals("remoteRecord") || recordType.equals("远程双录")) {
            return RecordTypeEnum.REMOTERECORD;
        } else if (recordType.equals("localUpload") || recordType.equals("本地上传")) {
            return RecordTypeEnum.LOCALUPLOAD;
        }else if (recordType.equals("closetRecord") || recordType.equals("临柜双录")) {
            return RecordTypeEnum.CLOSETRECORD;
        }
        return null;
    }
}
