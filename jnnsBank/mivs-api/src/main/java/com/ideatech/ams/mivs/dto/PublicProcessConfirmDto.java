package com.ideatech.ams.mivs.dto;

import lombok.Data;

/**
 * 通用处理确认报文接收对象
 * @author jzh
 * @date 2019/7/19.
 */

@Data
public class PublicProcessConfirmDto implements ReplyMsgDto{
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
    private String rjctInf;

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
