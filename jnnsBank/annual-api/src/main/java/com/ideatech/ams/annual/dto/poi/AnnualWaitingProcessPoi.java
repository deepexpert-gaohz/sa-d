package com.ideatech.ams.annual.dto.poi;

import lombok.Data;

/**
 * 年检待处理导出类
 * @author jzh
 * @date 2019/4/25.
 */
@Data
public class AnnualWaitingProcessPoi {

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 企业名称
     */
    private String depositorName;

    /**
     * 人行机构号
     */
    private String organPbcCode;

    /**
     * 网点机构号
     */
    private String organCode;

    /**
     * 单边状态
     */
    private String unilateral;

    /**
     * 异常状态
     * 多值用逗号分隔
     */
    private String abnormal;

    /**
     * 工商状态
     */
    private String saicStatus;

    /**
     * 数据一致性
     */
    private String match;

    /**
     * 处理状态
     */
    private String dataProcessStatus;

    /**
     * 账户性质
     */
    private String acctType;
}
