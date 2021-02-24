package com.ideatech.ams.dto.esb;

public class Transaction {
    @Override
    public String toString() {
        return "Transaction{" +
                "Header=" + Header +
                ", Body=" + Body +
                '}';
    }

    private Header Header;
    private com.ideatech.ams.dto.esb.Body Body;

    public com.ideatech.ams.dto.esb.Header getHeader() {
        return Header;
    }

    public void setHeader(com.ideatech.ams.dto.esb.Header header) {
        Header = header;
    }

    public com.ideatech.ams.dto.esb.Body getBody() {
        return Body;
    }

    public void setBody(com.ideatech.ams.dto.esb.Body body) {
        Body = body;
    }
}
