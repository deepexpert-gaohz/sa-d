package com.ideatech.ams.mivs.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019/7/25.
 * 5.1	手机号码联网核查记录
 *
 *  * 手机号码 mobNb 应答和申请复用
 *  * 证件号码 id改为identification
 */

@Entity
@Data
@Table(name = "mivs_phone_number_log")
public class MobilePhoneNumberLog extends ExtendEntity {

    /**
     * MobilePhoneNumber
     * 手机号码
     * <MobNb>  [1..1] E164 (Max13NumericText) 禁止中文
     */
    private String mobNb;

    /**
     * Name
     * 姓名
     * <Nm>  [1..1] Max140Text
     */
    private String nm;

    /**
     * IdentificationType
     * 证件类型
     * <IdTp>  [1..1] IdTypeCode (Max4Text) 禁止中文
     */
    private String idTp;

    /**
     * Identification
     * 证件号码
     * <Id>  [1..1] Max35Text  禁止中文
     */
    private String identification;//原id，但是与主键冲突。

    /**
     * UniformSocialCreditCode
     * 统一社会信用代码
     * <UniSocCdtCd>  [1..1] Max18Text 企业客户的统一社会信用代码
     */
    private String uniSocCdtCd;

    /**
     * BusinessRegistrationNumber
     * 工商注册号
     * <BizRegNb>  [1..1] Max15Text 企业客户的工商注册号
     */
    private String bizRegNb;

    /**
     * OperatorName
     * 操作员姓名
     * <OpNm>  [1..1] Max140Text 参与机构核查人员姓名
     */
    private String opNm;

    /**
     * MobilePhoneNumber
     * 手机号码
     * <MobNb>  [1..1] E164 (Max13NumericText) 禁止中文
     */
    //private String mobNb;

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

}
