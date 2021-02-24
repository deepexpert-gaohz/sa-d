package com.ideatech.ams.mivs.dto.mrd;

import com.ideatech.ams.mivs.dto.ReplyMsgDto;
import lombok.Data;


/**
 * @author jzh
 * @date 2019/7/17.
 * 手机号码联网核查应答报文传输对象
 */
@Data
public class MobilePhoneNumberReplyDto implements ReplyMsgDto {

    /**********************************************成功start***********************************************/
    /**
     * MobilePhoneNumber
     * 手机号码
     * <MobNb>  [1..1] E164 (Max13NumericText) 禁止中文
     */
    private String mobNb;

    /**
     * Result
     * 手机号码核查结果
     * <Rslt>  [1..1] PhoneNumberVerificationResultCode (Max4Text)
     */
    private String rslt;

    /**
     * MobileCarrier
     * 手机运营商
     * <MobCrr>  [0..1] MobileCarrierCode(Max4Text)  禁止中文
     * 当“手机号码核查结果”为 MCHD 时 填写
     */
    private String mobCrr;

    /**
     * LocationOfMobileNumber
     * 手机号归属地代码 <LocMobNb>  [0..1] Max4Text 禁止中文
     * 当“手机号码核查结果”为 MCHD 时填写
     */
    private String locMobNb;

    /**
     * LocationNameOfMobileNumber
     * 手机号归属地名称 <LocNmMobNb>  [0..1] Max10Text
     * 当“手机号码核查结果”为 MCHD 时填写
     */
    private String locNmMobNb;

    /**
     *
     * CardType
     * 客户类型 <CdTp>  [0..1] Max4Text
     *
     * 当“手机号码核查结果”为 MCHD 时填写
     * INDV：个人用户
     * ENTY：单位用户
     */
    private String cdTp;

    /**
     * Status
     * 手机号码状态 <Sts>  [0..1] Max4Text
     * 当“手机号码核查结果”为 MCHD 时填写
     * ENBL 正常
     * DSBL 停机
     */
    private String sts;

    /**********************************************成功end***********************************************/

}
