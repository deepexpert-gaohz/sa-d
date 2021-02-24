package com.ideatech.ams.kyc.dto;

import lombok.Data;

@Data
public class KycSearchHistoryDto {
    /**
     * 查询企业
     */
    private String entname;
    /**
     * 内部工商ID
     */
    private Long saicinfoId;
    /**
     * 查询日期
     */
    private String querydate;
    /**
     * 查询结果Y/N
     */
    private String queryresult;
    /**
     * 查询类型
     */
    private String searchtype;
    /**
     * 查询URL
     */
    private String searchurl;
    /**
     * 用户
     */
    private String username;
    /**
     * 机构的fullid
     */
    private String orgfullid;
    /**
     * 机构的名称
     */
    private String orgfullname;
    /**
     * 用户名称
     */
    private String cname;
    /**
     * 关联批次号
     */
    private String batchNo;

}
