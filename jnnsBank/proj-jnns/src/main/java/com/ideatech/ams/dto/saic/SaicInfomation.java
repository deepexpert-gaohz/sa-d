package com.ideatech.ams.dto.saic;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Data
public class SaicInfomation {

    private String status;
    private String message;
    private T data;
    private String name;//name
    private String historyName;
    private String registNo;
    private String unityCreditCode;
    private String saicCode;
    private String type;
    private String legalPerson;
    private String legalPersonType;
    private String registFund;
    private String registFundCurrency;
    private String openDate;
    private String businessMan;
    private String startDate;
    private String endDate;
    private String registOrgan;
    private String licenseDate;
    private String state;
    private String address;//address
    private String scope;
    private String revokeDate;
    private Boolean isOnStock;
//    (0为未上市，1为上市)
    private String priIndustry;
    private String subIndustry;
    private String province;
    private String ancheYear;
    private String ancheYearDate;
    private String contactPhone;
    private String contactEmail;
    private String legalPersonSurname;
    private String legalPersonName;
    private String registCapital;
    private String registCurrency;
    private String industryCategoryCode;
    private String industryLargeClassCode;
    private String typeCode;
    private String country;
    private String city;
    private String area;
    private List<Employees> employees;
    private List<Branchs> branchs;
    private List<Changes> changes;
    private List<Stockholders> stockholders;
    private List<Changemess> changemess;
    private List<Reports> reports;
    private List<Illegals> illegals;




}
