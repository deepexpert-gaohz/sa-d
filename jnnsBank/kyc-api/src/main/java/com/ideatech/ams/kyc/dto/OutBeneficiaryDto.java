package com.ideatech.ams.kyc.dto;

import lombok.Data;

import java.util.List;

@Data
public class OutBeneficiaryDto {
    private String updateTime;

    private String company;

    private String reason;

    private List<BeneficiaryDto> finalBeneficiary;
}
