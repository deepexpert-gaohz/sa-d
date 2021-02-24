package com.ideatech.ams.annual.dto.poi;

import lombok.Data;

/**
 * 手工处理成功导出类
 * @author jzh
 * @date 2019/4/25.
 */
@Data
public class AnnualManualSuccessPoi {

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
     * 处理人
     */
    private String dataProcessPerson;

    /**
     * 处理时间
     */
    private String dataProcessDate;

    /**
     * 账户性质
     */
    private String acctType;
}
