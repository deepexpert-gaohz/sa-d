package com.ideatech.ams.account.entity;

import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 报送历史记录
 */
@Entity
@Table(name = "sync_history",indexes = {@Index(name = "sync_history_an_idx",columnList = "acctNo"),
        @Index(name = "sync_history_of_idx",columnList = "organFullId")})
@Data
public class SyncHistoryPo extends BaseMaintainablePo implements Serializable {
    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635341L;
    /**
     * 机构名称
     */
    private String organName;
    /**
     * 机构号（核心）
     */
    @Column(length = 15)
    private String organCode;
    /**
     * 完整机构ID
     */
    private String organFullId;
    /**
     * 人行12位机构代码
     */
    @Column(length = 15)
    private String bankCode;
    /**
     * 账号
     */
    @Column(length = 32)
    private String acctNo;
    /**
     * 账户类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CompanyAcctType acctType;
    /**
     * 单据类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private BillType billType;
    /**
     * 报送人
     */
    @Column(length = 50)
    private String syncName;
    /**
     * 报送时间
     */
    @Column(length = 50)
    private String syncDateTime;
    /**
     * 报送系统
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EAccountType syncType;

    /**
     * 关联单据ID
     */
    @Column(length = 22)
    private Long refBillId;
    /**
     * 上报结果
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CompanySyncStatus syncStatus;
    /**
     * 同步失败原因
     */
    @Column(length = 1000)
    private String failMsg;
}
