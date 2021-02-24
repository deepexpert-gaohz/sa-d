package com.ideatech.ams.dto.OutBeneficiary;

public class FinalBeneficiary {

    private String  name;//name
    private String  type;//type
    private String  identifyType;//identifyType
    private String  identifyNo;
    private String  capital;
    private String  capitalPercent;
    private String  capitalChain;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    public String getIdentifyNo() {
        return identifyNo;
    }

    public void setIdentifyNo(String identifyNo) {
        this.identifyNo = identifyNo;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCapitalPercent() {
        return capitalPercent;
    }

    public void setCapitalPercent(String capitalPercent) {
        this.capitalPercent = capitalPercent;
    }

    public String getCapitalChain() {
        return capitalChain;
    }

    public void setCapitalChain(String capitalChain) {
        this.capitalChain = capitalChain;
    }


    @Override
    public String toString() {
        return "FinalBeneficiary{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", identifyType='" + identifyType + '\'' +
                ", identifyNo='" + identifyNo + '\'' +
                ", capital='" + capital + '\'' +
                ", capitalPercent='" + capitalPercent + '\'' +
                ", capitalChain='" + capitalChain + '\'' +
                '}';
    }
}
