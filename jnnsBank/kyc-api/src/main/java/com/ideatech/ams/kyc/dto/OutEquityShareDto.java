package com.ideatech.ams.kyc.dto;

import lombok.Data;

import java.util.List;

@Data
public class OutEquityShareDto {

    private int total;

    List<EquityShareDto> item;
}
