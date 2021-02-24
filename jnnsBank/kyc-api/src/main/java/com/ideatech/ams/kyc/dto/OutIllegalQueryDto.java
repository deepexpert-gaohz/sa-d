package com.ideatech.ams.kyc.dto;

import lombok.Data;

import java.util.List;

@Data
public class OutIllegalQueryDto {

    private int total;

    List<IllegalQueriesDto> items;

    /**
     * 违法状态
     */
    private String illegalStatus;

}
