package com.ideatech.ams.account.dto;

import lombok.Data;

/**
 * Created by houxianghua on 2018/11/28.
 */
@Data
public class AllAccountPublicSearchDTO extends AllAccountPublicDTO {
    /**
     * kernelOrgCode 网点机构号（核心机构号）
     */
    private String kernelOrgCode;

    /**
     * kernelOrgCode 网点机构名称（创建机构）
     */
    private String kernelOrgName;

    /**
     * 白名单
     */
    private String whiteList;
}
