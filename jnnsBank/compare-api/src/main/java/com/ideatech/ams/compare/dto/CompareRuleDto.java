package com.ideatech.ams.compare.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

import java.util.Date;

/**
 * @author jzh
 * @date 2019/1/16.
 */
@Data
public class CompareRuleDto{
    private Long id;
    private String lastUpdateBy;
    private Date lastUpdateDate;
    private Date createdDate;
    private String createdBy;

    /**
     * 名称
     */
    private String name;

    /**
     * 使用次数
     */
    private Integer count;

    /**
     * 创建时间
     *  yyyy-mm-dd
     */
    private String createTime;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 法人黑名单
     */
    private Boolean personBlackList;
    /**
     * 企业黑名单
     */
    private Boolean bussBlackList;

    /**
     * 比对规则字段
     */
    private String compareFields;

    /**
     * 机构fullId
     */
    private String organFullId;


}
