package com.ideatech.ams.risk.enums;


public enum HandleMode {

    BCL("1"), ZF("2"), ZTFGM("3"), JF("4"), HFFGM("5");
    private String fullName;

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    HandleMode(String fullName) {
        this.fullName = fullName;
    }

}
