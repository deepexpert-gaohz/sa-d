package com.ideatech.ams.kyc.dto.newcompany;

import lombok.Data;

import java.util.List;

@Data
public class OutFreshCompanyQueryDto {
    private int total;

    List<FreshCompanyQueriesDto> items;

}
