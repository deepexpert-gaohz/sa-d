package com.ideatech.ams.customer.dto.illegal;

import com.ideatech.ams.customer.enums.illegal.IllegalQueryExpiredStatus;
import com.ideatech.ams.customer.enums.illegal.IllegalQueryStatus;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class IllegalQueryDto extends BaseMaintainableDto {

    /**
     * ID
     */
    private Long id;
    /**
     * 注册号
     */
    private String regNo;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 违法状态
     */
    private IllegalQueryStatus illegalStatus;

    /**
     * 工商状态
     */
    private String saicStatus;

    /**
     * 是否存在经营异常
     */
    private Boolean isChangemess;

    /**
     * 机构号
     */
    private String organCode;

    /**
     * 营业执照到期日
     */
    private String fileEndDate;

    /**
     * 是否有经营异常
     */
    private IllegalQueryStatus changemess;

    /**
     * 批次ID
     */
    private Long illegalQueryBatchId;

    /**
     * 机构FULLID
     */
    private String organFullId;

    /**
     * 营业执照是否过期判断
     */
    private IllegalQueryExpiredStatus fileDueExpired;
}
