package com.ideatech.ams.account.view;

import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "yd_report_status_v")
public class ReportStatus implements Serializable {
    @Id
    private String createdDate;//日期
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CompanyAcctType acctType;//账户类型
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CompanySyncStatus pbcSyncStatus;//人行上报状态
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CompanySyncStatus eccsSyncStatus;//信用代码证上报状态
    private String organFullId;
}

