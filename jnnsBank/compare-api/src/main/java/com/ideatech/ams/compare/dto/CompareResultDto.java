package com.ideatech.ams.compare.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import com.ideatech.common.enums.SaicStatusEnum;
import lombok.Data;

import javax.persistence.Column;

/**
 * 比对结果
 *
 * @author fantao
 */
@Data
public class CompareResultDto extends BaseMaintainableDto {

    public CompareResultDto() {
    }

    public CompareResultDto(Long compareTaskId, String account) {
        this.compareTaskId = compareTaskId;
        this.account = account;
    }

    private Long id;

    /**
     * 比对任务id
     */
    private Long compareTaskId;
    /**
     * 企业账号
     */
    private String account;
    /**
     * 是否匹配
     */
    private Boolean match;
    /**
     * 列标记，标记每一列是否匹配
     */
    private String columnColor;
    /**
     * 详情，所有的字段比对结果
     */
    private String details;
    /**
     * 内容，用于页面查询时展示表格的行的数据
     */
    private String rowContent;
    /**
     * 机构id，用于控制权限
     */
    private String organFullId;
    /**
     * 单边数据源，用来记录比对的账号是哪个数据源的单边数据
     */
    private String alone;

    /**
     * 根据task和uuid锁定compareTask外检的数据。
     */
    private String uuid;

    /**
     * 是否是黑名单
     */
    private String isBlacklist;

    /**
     * 工商状态
     */
    private SaicStatusEnum saicStatus;

    private Long organId;

    /**
     * 存款人名称
     */
    private String depositorName;

}
