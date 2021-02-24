package com.ideatech.ams.risk.tradeRisk.entity;

import com.ideatech.common.entity.DealBase;
import com.ideatech.common.entity.util.Comment;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yangwz
 * @Description 账户短期内发生频繁或大额交易, 后突然停止使用或者销户
 * @date 2019-10-30 21:52
 */
@Entity
@Table(name = "risk_2020")
@Data
public class Risk2020 extends DealBase {
    @Comment("机构名称")
    @Column(name = "org_Name", columnDefinition = " varchar(255)")
    private String orgName;
    @Comment("客户号")
    @Column(name = "khtybh", columnDefinition = " varchar(255) ")
    private String khtybh;
    @Comment("客户名称")
    @Column(name = "khmc", columnDefinition = " varchar(255)  ")
    private String khmc;
    @Comment("内部机构号")
    @Column(name = "nbjgh", columnDefinition = " varchar(255) ")
    private String nbjgh;
    @Comment("存款账号")
    @Column(name = "ckzh", columnDefinition = " varchar(255) ")
    private String ckzh;
    @Comment("账户类型")
    @Column(name = "ckzhlx", columnDefinition = " varchar(255) ")
    private String ckzhlx;
    @Comment("开户日期")
    @Column(name = "khrq", columnDefinition = " varchar(255)  ")
    private String khrq;
    @Comment("销户日期")
    @Column(name = "xhrq", columnDefinition = " varchar(255) ")
    private String xhrq;
    @Comment("核心交易日期")
    @Column(name = "hxjyrq", columnDefinition = " varchar(255) ")
    private String hxjyrq;
    @Comment("核心交易时间")
    @Column(name = "hxjysj", columnDefinition = " varchar(255) ")
    private String hxjysj;
    @Comment("上次动户日期")
    @Column(name = "scdhrq", columnDefinition = " varchar(255) ")
    private String scdhrq;
    @Comment("借贷标志")
    @Column(name = "jdbz", columnDefinition = " varchar(255) ")
    private String jdbz;
    @Comment("交易金额")
    @Column(name = "jyje", columnDefinition = " number ")
    private double jyje;
    @Comment("对方账号")
    @Column(name = "dfzh", columnDefinition = " varchar(255) ")
    private String dfzh;
    @Comment("对方户名")
    @Column(name = "dfhm", columnDefinition = " varchar(255) ")
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
