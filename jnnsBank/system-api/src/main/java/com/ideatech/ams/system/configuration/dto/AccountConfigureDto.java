package com.ideatech.ams.system.configuration.dto;

import lombok.Data;

@Data
public class AccountConfigureDto {

    private Long id;
    /**
     * 账户类型
     */
    private String acctType;

    /**
     * 存款人性质
     */
    private String depositorType;

    /**
     * 业务类型
     */
    private String operateType;
}
