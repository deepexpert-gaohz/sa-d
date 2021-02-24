package com.ideatech.ams.kyc.dto;

import lombok.Data;

import java.util.List;

@Data
public class OutBaseAccountDto {
    private int total;

    List<BaseAccountDto> items;
}
