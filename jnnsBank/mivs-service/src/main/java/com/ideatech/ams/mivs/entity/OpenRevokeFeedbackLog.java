package com.ideatech.ams.mivs.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019-08-08.
 * 开销户疑义反馈记录
 */

@Entity
@Data
@Table(name = "mivs_openrevoke_fb_log")
public class OpenRevokeFeedbackLog extends ExtendEntity {

    /**
     * --EntityName
     * 企业名称	<EntNm>	[0..1]	Max100Text	市场主体类型：企业
     */
    @Column(length = 200)
    private String entNm;

    /**
     * --TradeName
     * 字号名称	<TraNm>	[0..1]	Max256Text	市场主体类型：个体户
     */
    @Column(length = 512)
    private String traNm;

    /**
     * --UniformSocialCreditCode
     *    统一社会信用代码	<UniSocCdtCd>	[1..1]	Max18Text	企业客户的统一社会信用代码
     * 禁止中文
     */
    @Column(length = 36)
    private String uniSocCdtCd;

    /**
     * --AccountStatus
     *    账户状态标识	<AcctSts>	[1..1]	CompanyAccountStatusCode (Max4Text)
     * 企业账户状态标识
     * OPEN：已开户
     * CLOS：已销户
     */
    @Column(length = 8)
    private String acctSts;

    /**
     * --ChangeDate
     *    变更日期	<ChngDt>	[1..1]	ISODate
     */
    @Column(length = 20)
    private String chngDt;



    //通用处理报文

    /**
     * ProcessStatus
     * 业务状态
     * <PrcSts>  [1..1] ProcessCode (Max4Text） 禁止中文
     */
    @Column(length = 8)
    private String prcSts;

    /**
     * ProcessCode
     * 业务处理码
     * <PrcCd>  [0..1]  Max8Text  禁止中文
     */
    @Column(length = 16)
    private String prcCd;

    /**
     * PartyIdentification
     * 拒绝业务的参与机构行号
     * <PtyId>  [0..1]  Max14Text  禁止中文
     */
    @Column(length = 28)
    private String ptyId;

    /**
     * PartyProcessCode
     * 参与机构业务拒绝码
     * <PtyPrcCd>  [0..1] RejectCode （Max4Text） 禁止中文
     */
    @Column(length = 8)
    private String ptyPrcCd;

    /**
     * RejectInformation
     * 业务拒绝信息
     * <RjctInf>  [0..1]  Max105Text  允许中文
     */
    @Column(length = 210)
    private String rjctInf2;

    /**
     * ProcessDate
     * 处理日期（终态日期）
     * <PrcDt>  [0..1]  ISODate  禁止中文
     */
    @Column(length = 20)
    private String prcDt;

    /**
     * NettingRound
     * 轧差场次
     * <NetgRnd>  [0..1]  Max2Text  禁止中文
     */
    @Column(length = 4)
    private String netgRnd;
}
