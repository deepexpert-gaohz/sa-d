package com.ideatech.ams.mivs.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019-08-06.
 * 公共的疑义反馈报文记录
 */

@Entity
@Data
@Table(name = "mivs_common_feedback_log")
public class CommonFeedbackLog extends ExtendEntity{


    /**
     * 相关报文日志记录id
     */
    private Long msgLogId;

    /**
     * 报文类型
     */
    private String msgType;

    /**
     * Content
     * 疑义反馈内容
     * <Cntt>  [1..1]  Max256Text  允许中文
     */
    @Column(length = 512)
    private String cntt;

    /**
     * ContactName
     * 联系人姓名
     * <ContactNm>  [1..1]  Max140Text
     */
    @Column(length = 280)
    private String contactNm;

    /**
     * ContactNumber
     * 联系人电话
     * <ContactNb>  [1..1]  Max30Text  禁止中文
     */
    @Column(length = 60)
    private String contactNb;

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
