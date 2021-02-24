package com.ideatech.ams.dto.esb.response;

public class Response {
    @Override
    public String toString() {
        return "Response{" +
                "bizHeader='" + bizHeader + '\'' +
                ", bizBody='" + bizBody + '\'' +
                '}';
    }

    private String bizHeader;
    private String bizBody;

    public String getBizHeader() {
        return bizHeader;
    }

    public void setBizHeader(String bizHeader) {
        this.bizHeader = bizHeader;
    }

    public String getBizBody() {
        return bizBody;
    }

    public void setBizBody(String bizBody) {
        this.bizBody = bizBody;
    }
}
