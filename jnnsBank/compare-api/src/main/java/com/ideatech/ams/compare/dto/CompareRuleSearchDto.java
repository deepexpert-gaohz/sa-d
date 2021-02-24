package com.ideatech.ams.compare.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

import java.util.List;

/**
 * @author jzh
 * @date 2019/1/16.
 */

@Data
public class CompareRuleSearchDto  extends PagingDto<CompareRuleDto> {

    /**
     * 名称
     */
    private String name;

    /**
     * 开始时间
     */
    private String beginDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 创建人员
     */
    private String creater;

    /**
     * 机构fullId
     */
    private String organFullId;

    /**
     * fullId集合
     */
    private List<String> fullIdList;

    private String createdBy;
}
