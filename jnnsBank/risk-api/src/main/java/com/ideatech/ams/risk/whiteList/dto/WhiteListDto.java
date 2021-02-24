package com.ideatech.ams.risk.whiteList.dto;

import lombok.Data;

/**
 * @Author: yinjie
 * @Date: 2019/5/27 10:10
 * @description
 */

@Data
public class WhiteListDto {

    private Long id;

    //账户号
    private String accountId;

    //账户名称
    private String accountName;

    //社会统一编码
    private String socialUnifiedCode;

    private String corporateBank;

}
