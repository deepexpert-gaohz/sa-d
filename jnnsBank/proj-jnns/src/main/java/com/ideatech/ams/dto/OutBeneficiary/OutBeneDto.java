package com.ideatech.ams.dto.OutBeneficiary;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public class OutBeneDto {
    private String status;
    private String currentTime;
    private String  code;
    private String reason;
    private T result;
    private String updateTime;
    private String company;
    private List<FinalBeneficiary> finalBeneficiary;


    @Override
    public String toString() {
        return "OutBeneDto{" +
                "status='" + status + '\'' +
                ", currentTime='" + currentTime + '\'' +
                ", code='" + code + '\'' +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                ", updateTime='" + updateTime + '\'' +
                ", company='" + company + '\'' +
                ", finalBeneficiary=" + finalBeneficiary +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public List<FinalBeneficiary> getFinalBeneficiary() {
        return finalBeneficiary;
    }

    public void setFinalBeneficiary(List<FinalBeneficiary> finalBeneficiary) {
        this.finalBeneficiary = finalBeneficiary;
    }

}
