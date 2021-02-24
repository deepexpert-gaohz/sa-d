package com.ideatech.ams.account.dto;

import com.ideatech.ams.account.enums.AcctBigType;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BatchSuspendSourceEnum;
import com.ideatech.common.dto.BaseMaintainableDto;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

/**
 * @author vantoo
 * @date 17:52 2018/5/20
 */
@Data
public class BatchSuspendDto extends BaseMaintainableDto {

    /**
     * 流水信息
     */
    private Long billId;

    private Long id;

    /**
     * 批次号
     */
    private String batchNo;

    private String acctNo;

    private String customerNo;

    private String depositorName;

    private String organCode;

    private String organFullId;

    private CompanyAcctType acctType;

    /**
     * 账户性质大类
     */
    private AcctBigType acctBigType;

    /**
     * 是否已上报
     */
    private CompanyIfType syncStatus;

    /**
     * 是否已经处理
     */
    private CompanyIfType processed;

    /**
     * 批量久悬类型
     */
    private BatchSuspendSourceEnum type;

    private String errorMessage;

    private String organName;

}
