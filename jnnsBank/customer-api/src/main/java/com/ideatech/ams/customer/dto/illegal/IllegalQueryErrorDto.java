package com.ideatech.ams.customer.dto.illegal;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class IllegalQueryErrorDto extends BaseMaintainableDto {

    private Long id;
    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 注册号
     */
    private String regNo;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 关联批次号
     */
    private Long illegalQueryBatchId;
}
