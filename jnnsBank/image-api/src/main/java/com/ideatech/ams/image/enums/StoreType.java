package com.ideatech.ams.image.enums;

public enum StoreType {
    nginx("应用服务器存储"),
    fastdfs("文件服务器存储");
    private String value;
    StoreType(String value){
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
