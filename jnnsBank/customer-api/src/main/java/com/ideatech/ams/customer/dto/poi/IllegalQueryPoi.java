package com.ideatech.ams.customer.dto.poi;

import com.ideatech.ams.customer.enums.illegal.IllegalQueryStatus;
import lombok.Data;

@Data
public class IllegalQueryPoi {

    /**
     * 机构号
     */
    private String organCode;
    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 工商状态
     */
    private String saicStatus;
    /**
     * 注册号
     */
    private String regNo;

    /**
     * 营业执照到期日
     */
    private String fileEndDate;

    /**
     * 营业执照到期日
     */
    private String fileDueExpired;

    /**
     * 是否经营异常
     */
    private String changemess;

    /**
     * 严重违法状态
     */
    private String illegalStatus;
}
