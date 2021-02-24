package com.ideatech.ams.account.dto;

import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;


/**
 * 报送历史记录
 */
@Data
public class SyncHistoryDto extends BaseMaintainableDto {
    private Long id;
    /**
     * 机构名称
     */
    private String organName;
    /**
     * 机构号（核心）
     */
    private String organCode;
    /**
     * 完整机构ID
     */
    private String organFullId;
    /**
     * 人行12位机构代码
     */
    private String bankCode;
    /**
     * 账号
     */
    private String acctNo;
    /**
     * 账户类型
     */
    private CompanyAcctType acctType;
    /**
     * 单据类型
     */
    private BillType billType;
    /**
     * 报送人
     */
    private String syncName;
    /**
     * 报送时间
     */
    private String syncDateTime;
    /**
     * 报送系统
     */
    private EAccountType syncType;

    /**
     * 关联单据ID
     */
    private Long refBillId;
    /**
     * 上报结果
     */
    private CompanySyncStatus syncStatus;
    /**
     * 同步失败原因
     */
    private String failMsg;

    private String beginDate;
    private String endDate;
}
