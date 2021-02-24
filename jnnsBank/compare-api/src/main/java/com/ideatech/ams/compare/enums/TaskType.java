package com.ideatech.ams.compare.enums;

public enum TaskType {
        NOW("即时任务"), TIMING("定时任务");

    private String fullName;

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName
     *            the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    TaskType(String fullName) {
        this.fullName = fullName;
    }
}
