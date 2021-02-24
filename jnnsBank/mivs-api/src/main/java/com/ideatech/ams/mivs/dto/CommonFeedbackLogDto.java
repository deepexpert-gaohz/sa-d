package com.ideatech.ams.mivs.dto;


import lombok.Data;

/**
 * @author jzh
 * @date 2019-08-06.
 */

@Data
public class CommonFeedbackLogDto extends ExtendDto{

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
    private String cntt;

    /**
     * ContactName
     * 联系人姓名
     * <ContactNm>  [1..1]  Max140Text
     */
    private String contactNm;

    /**
     * ContactNumber
     * 联系人电话
     * <ContactNb>  [1..1]  Max30Text  禁止中文
     */
    private String contactNb;

    //通用处理报文

    /**
     * ProcessStatus
     * 业务状态
     * <PrcSts>  [1..1] ProcessCode (Max4Text） 禁止中文
     */
    private String prcSts;

    /**
     * ProcessCode
     * 业务处理码
     * <PrcCd>  [0..1]  Max8Text  禁止中文
     */
    private String prcCd;

    /**
     * PartyIdentification
     * 拒绝业务的参与机构行号
     * <PtyId>  [0..1]  Max14Text  禁止中文
     */
    private String ptyId;

    /**
     * PartyProcessCode
     * 参与机构业务拒绝码
     * <PtyPrcCd>  [0..1] RejectCode （Max4Text） 禁止中文
     */
    private String ptyPrcCd;

    /**
     * RejectInformation
     * 业务拒绝信息
     * <RjctInf>  [0..1]  Max105Text  允许中文
     */
    private String rjctInf2;

    /**
     * ProcessDate
     * 处理日期（终态日期）
     * <PrcDt>  [0..1]  ISODate  禁止中文
     */
    private String prcDt;

    /**
     * NettingRound
     * 轧差场次
     * <NetgRnd>  [0..1]  Max2Text  禁止中文
     */
    private String netgRnd;
}
