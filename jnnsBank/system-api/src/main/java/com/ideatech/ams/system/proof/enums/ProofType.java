package com.ideatech.ams.system.proof.enums;

public enum ProofType {
    PBC("人行校验"),

    SAIC("工商校验"),

    PHONE("手机号实名校验"),

    KYC("客户尽调"),

    JUDICIAL_INFORMATION("司法查询");
    private String value;

    ProofType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
