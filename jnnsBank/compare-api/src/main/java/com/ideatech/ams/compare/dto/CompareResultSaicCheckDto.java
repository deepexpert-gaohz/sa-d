package com.ideatech.ams.compare.dto;

import com.ideatech.ams.compare.enums.SaicStateEnum;
import lombok.Data;

/**
 * @author jzh
 * @date 2019/6/5.
 */
@Data
public class CompareResultSaicCheckDto {

    private Long id;

    /**
     * 比对任务id
     */
    private Long compareTaskId;

    /**
     * 比对结果ID
     */
    private Long compareResultId;


    /**
     * 存款人名称
     */
    private String depositorName;


    /**
     * 机构id
     */
    private String organFullId;

    /**
     * YD_SYS_ORGANIZATION code
     */
    private String code;

    /**
     * 机构名称
     */
    private String organName;

    /**
     * 原始工商信息saicInfo
     */
    private Long saicInfoId;

    /**
     * 是否严重违法
     */
    private Boolean illegal;

    /**
     * 是否经营异常
     */
    private Boolean changeMess;

    /**
     * 是否营业到期
     */
    private Boolean businessExpires;

    /**
     * 工商状态
     */
    private SaicStateEnum saicState;

    /**
     * 是否异常工商状态
     */
    private Boolean abnormalState;

    /**
     * 是否信息异动
     */
    private Boolean changed;

    /**
     * 是否为异常客户（客户异动管理页面过滤使用）
     */
    private Boolean abnormal;

    /**
     * 异动时间
     */
    private String abnormalTime;

    /**
     * 备用字段
     */
    private String string001;

    /**
     * 备用字段
     */
    private String string002;

    /**
     * 备用字段
     */
    private String string003;

    /**
     * 备用字段
     */
    private String string004;

    /**
     * 备用字段
     */
    private String string005;
}
