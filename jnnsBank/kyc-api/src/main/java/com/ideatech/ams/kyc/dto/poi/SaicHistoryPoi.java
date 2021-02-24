package com.ideatech.ams.kyc.dto.poi;

import lombok.Data;

/**
 * 最终受益人
 * @author wangqingan
 * @version 20/04/2018 2:39 PM
 */
@Data
public class SaicHistoryPoi {

    /**
     * 查询日期
     */
    private String querydate;
    /**
     * 查询企业
     */
    private String entname;

    /**
     * 机构的fullid
     */
    private String orgfullname;

    /**
     * 用户
     */
    private String cname;


}
