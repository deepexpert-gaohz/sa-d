package com.ideatech.ams.kyc.entity.po;

import java.util.List;

import com.ideatech.ams.kyc.entity.EquityShare;

import lombok.Data;

@Data
public class OutEquityShareInfo {

    private int total;

    List<EquityShare> item;

}
