package com.ideatech.ams.risk.CustomerTransaction.dto;


import lombok.Data;

@Data
public class BusinessChangesDto {
    private String changesType;
    private String changesBeforeContent;
    private String changesAfterContent;
    private String changesDate;
}
