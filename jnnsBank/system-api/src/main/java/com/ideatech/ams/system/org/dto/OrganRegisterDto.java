package com.ideatech.ams.system.org.dto;

import lombok.Data;

@Data
public class OrganRegisterDto {

    private Long id;

    /**
     * 机构Id
     */
    private Long organId;

    /**
     * 机构名称
     */
    private String name;

    /**
     * 机构人行号
     */
    private String pbcCode;

    private String fullId;
}
