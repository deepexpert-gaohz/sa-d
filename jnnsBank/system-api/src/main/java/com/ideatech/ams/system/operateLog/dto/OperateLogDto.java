package com.ideatech.ams.system.operateLog.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * 操作记录
 */
@Data
public class OperateLogDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 审核失败原因
     */
    private String failMsg;
    /**
     * 机构号
     */
    private String organCode;

    /**
     * 关联单据ID
     */
    private Long refBillId;
    /**
     * 操作人
     */
    private String operateName;
    /**
     * 操作时间
     */
    private String operateDate;
    /**
     * 操作类型
     */
    private String operateType;
}
