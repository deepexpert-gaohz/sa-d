package com.ideatech.ams.risk.highRisk.dto;

import com.ideatech.ams.risk.highRisk.entity.HighRiskApi;
import com.ideatech.common.dto.PagingDto;

/**
 * @author yangwz
 * @Description
 * @date 2019-10-29 11:09
 */
public class HighRiskApiDto extends PagingDto<HighRiskApi> {

    /**
     * jiekou id
     */
    private String apiNo;
    /**
     * 接口名
     */
    private String apiName;
    /**
     * 接口地址
     */
    private String apiUrl;

    /**
     * 搜索关键字
     */
    private String keyWord;

    /**
     * 返回的数据类型key
     */
    private String retData;
}
