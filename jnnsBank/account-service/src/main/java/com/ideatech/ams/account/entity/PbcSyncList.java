package com.ideatech.ams.account.entity;

import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 人行同步核准列表
 * @author vantoo
 * @date 17:52 2018/5/20
 */
@Entity
@Data
public class PbcSyncList extends BaseMaintainablePo {

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 基本户开户许可证
     */
    private String accountKey;

    /**
     * 账户开户许可证
     */
    private String acctAccountKey;

    /**
     * 机构ID
     */
    private Long organId;

    /**
     * 机构FullID
     */
    private String organFullId;

    /**
     * 业务类型
     */
    @Enumerated(EnumType.STRING)
    private BillType billType;

    /**
     * 是否核准
     */
    @Enumerated(EnumType.STRING)
    private CompanyIfType syncStatus;

    /**
     * 是否推送
     */
    @Enumerated(EnumType.STRING)
    private CompanyIfType isPush;

    /**
     * 流水id
     */
    private Long billId;

    /**
     * 是否取消核准(取消核准推送。基本)
     */
    private Boolean cancelHeZhun;

    /**
     * 取消核准推送开户日期
     */
    private String acctCreateDate;

    /**
     * 取消核准推送增加临时 非临时 有效日期
     */
    private String effectiveDate;

}
