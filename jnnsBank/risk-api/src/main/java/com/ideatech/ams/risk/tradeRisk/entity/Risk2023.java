package com.ideatech.ams.risk.tradeRisk.entity;

import com.ideatech.common.entity.DealBase;
import com.ideatech.common.entity.util.Comment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "RISK_2023")
@Getter
@Setter
/**
 * 短期内 频繁收付的大额存款、整存零付或零收整付且金额大致相当的账户
 */
public class Risk2023 extends DealBase {
    @Comment("日期")
    @Column(name = "stat_date", columnDefinition = " varchar(255) ")
    private String stat_date;
    @Comment("状态")
    @Column(name = "status", columnDefinition = " varchar(255) ")
    private String status;
    @Comment("开始时间")
    @Column(name = "start_time", columnDefinition = " varchar(255) ")
    private String start_time;
    @Comment("结束时间")
    @Column(name = "end_time", columnDefinition = " varchar(255) ")
    private String end_time;
    @Comment("机构名称")
    @Column(name = "org_name", columnDefinition = " varchar(255)")
    private String org_name;
    @Comment("客户号")
    @Column(name = "khtybh", columnDefinition = " varchar(255) ")
    private String khtybh;
    @Comment("客户名称")
    @Column(name = "khmc", columnDefinition = " varchar(255)")
    private String khmc;
    @Comment("机构号")
    @Column(name = "nbjgh", columnDefinition = " varchar(255) ")
    private String nbjgh;
    @Comment("机构名称")
    @Column(name = "yxjgmc", columnDefinition = " varchar(255) ")
    private String yxjgmc;
    @Comment("存款账号")
    @Column(name = "ckzh", columnDefinition = " varchar(255) ")
    private String ckzh;
    @Comment("核心交易日期")
    @Column(name = "hxjyrq", columnDefinition = " varchar(255) ")
    private String hxjyrq;
    @Comment("核心交易时间")
    @Column(name = "hxjysj", columnDefinition = " varchar(255) ")
    private String hxjysj;
    @Comment("借贷标志")
    @Column(name = "jdbz", columnDefinition = " varchar(255) ")
    private String jdbz;
    @Comment("交易金额")
    @Column(name = "jyje", columnDefinition = " varchar(255) ")
    private Double jyje;
    @Comment("对方账号")
    @Column(name = "dfzh", columnDefinition = " number")
    private String dfzh;
    @Comment("对方户名")
    @Column(name = "dfhm", columnDefinition = " varchar(255)")
    private String dfhm;
    @Comment("交易渠道")
    @Column(name = "tran_channel", columnDefinition = " varchar(255) ")
    private String tranChannel;
    @Comment("交易用途")
    @Column(name = "purpose", columnDefinition = " varchar(255) ")
    private String purpose;
    @Comment("对方行号")
    @Column(name = "df_bank_no", columnDefinition = " varchar(255)  ")
    private String dfBankNo;
    @Comment("对方行名")
    @Column(name = "df_bank_name", columnDefinition = " varchar(255)  ")
    private String dfBankName;


    @Comment("核心交易流水号")
    @Column(name = "tran_no", columnDefinition = " varchar(255)  ")
    private String tranNo;
    @Comment("子交易流水号")
    @Column(name = "sub_tran_no", columnDefinition = " varchar(255)  ")
    private String subTranNo;
    @Comment("笔次序号")
    @Column(name = "batch_seq_no", columnDefinition = " varchar(255)  ")
    private Double batchSeqNo;
    @Comment("业务编号")
    @Column(name = "internal_key", columnDefinition = " varchar(255)  ")
    private String internalKey;
    @Comment("账户名称")
    @Column(name = "acct_name", columnDefinition = " varchar(255)  ")
    private String acctName;
    @Comment("交易类型")
    @Column(name = "tran_type", columnDefinition = " varchar(255)  ")
    private String tranType;
    @Comment("开户行机构号")
    @Column(name = "open_bank_branch", columnDefinition = " varchar(255)  ")
    private String openBankBranch;
    @Comment("账户余额")
    @Column(name = "actual_bal", columnDefinition = " varchar(255)  ")
    private Double actualBal;
    @Comment("对方客户号")
    @Column(name = "oth_client_no", columnDefinition = " varchar(255)  ")
    private String othClientNo;
    @Comment("对方行政区划")
    @Column(name = "oth_state", columnDefinition = " varchar(255)  ")
    private String othState;
    @Comment("币种")
    @Column(name = "ccy", columnDefinition = " varchar(255)  ")
    private String ccy;
    @Comment("现转标志")
    @Column(name = "cash_tran_ind", columnDefinition = " varchar(255)  ")
    private String cashTranInd;
    @Comment("代办人姓名")
    @Column(name = "deputy_name", columnDefinition = " varchar(255)  ")
    private String deputyName;
    @Comment("代办人证件类别")
    @Column(name = "deputy_document_type", columnDefinition = " varchar(255)  ")
    private String deputyDocumentType;
    @Comment("代办人证件号码")
    @Column(name = "deputy_document_no", columnDefinition = " varchar(255)  ")
    private String deputyDocumentNo;
    @Comment("交易柜员号")
    @Column(name = "officer_id", columnDefinition = " varchar(255)  ")
    private String officerId;
    @Comment("柜员流水号")
    @Column(name = "officer_tran_no", columnDefinition = " varchar(255)  ")
    private String officerTranNo;
    @Comment("授权柜员号")
    @Column(name = "override_officer", columnDefinition = " varchar(255)  ")
    private String overrideOfficer;
    @Comment("冲补抹标志")
    @Column(name = "reversal_indl", columnDefinition = " varchar(255)  ")
    private String reversalIndl;
    @Comment("法人机构行")
    @Column(name = "legal_bank", columnDefinition = " varchar(255)  ")
    private String legalBank;
}
