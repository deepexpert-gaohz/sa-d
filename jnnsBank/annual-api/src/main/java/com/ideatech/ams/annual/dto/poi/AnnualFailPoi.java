package com.ideatech.ams.annual.dto.poi;

import lombok.Data;

/**
 * 年检失败导出类
 * @author jzh
 * @date 2019/4/25.
 */
@Data
public class AnnualFailPoi {

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
     * 异常状态
     * 多值用逗号分隔
     */
    private String abnormal;

    /**
     * 失败原因
     */
    private String pbcSubmitErrorMsg;

    /**
     * 账户性质
     */
    private String acctType;
}
