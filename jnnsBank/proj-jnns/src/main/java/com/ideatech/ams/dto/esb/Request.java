package com.ideatech.ams.dto.esb;

import lombok.Data;

@Data
public class Request {
    private BizHeader bizHeader;
    private BizBody bizBody;

    public BizHeader getBizHeader() {
        return bizHeader;
    }

    public void setBizHeader(BizHeader bizHeader) {
        this.bizHeader = bizHeader;
    }

    public BizBody getBizBody() {
        return bizBody;
    }

    public void setBizBody(BizBody bizBody) {
        this.bizBody = bizBody;
    }
}
