package com.ideatech.ams.annual.dto.poi;

import com.ideatech.ams.annual.enums.ResultStatusEnum;
import lombok.Data;

/**
 * 系统年检成功导出类
 * @author jzh
 * @date 2019/4/25.
 */
@Data
public class AnnualSystemSuccessPoi {

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
     * 年检报送结果
     */
    private String result;

    /**
     * 数据一致性
     */
    private String match;

    /**
     * 账户性质
     */
    private String acctType;
}
